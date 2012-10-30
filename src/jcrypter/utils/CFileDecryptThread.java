package jcrypter.utils;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import javax.swing.*;
import jcrypter.*;

public class CFileDecryptThread implements Runnable {
	private boolean authenticated;
	private JProgressBar progressBar;
	private String cryptAlgorithm, messageDigestAlgorithm, authenticationPassword, inputFilePath, outputFilePath;
	private FileChannel inFileChannel;
	
	public CFileDecryptThread(String _inputFilePath, String _outputFilePath, JProgressBar _progressBar) {
		this.inputFilePath = _inputFilePath;
		this.outputFilePath = CFileCrypter.checkExistedFile(_outputFilePath);
		this.progressBar = _progressBar;
		this.getFileHeader();
	}
	
	public void run() {
		if (!this.authenticated)
			return;
		
		CCrypter crypter = new CCrypter(this.cryptAlgorithm, this.messageDigestAlgorithm, this.authenticationPassword);
		
		try {
			FileChannel outFileChannel = new RandomAccessFile(outputFilePath, "rw").getChannel();
			ByteBuffer bEncryptedBuffer = ByteBuffer.allocate(CConstants.ENCRYPTED_BUFFER_ALLOCATION),
						bClearBuffer = ByteBuffer.allocate(CConstants.CLEAR_BUFFER_ALLOCATION);
			bEncryptedBuffer.clear();
			bClearBuffer.clear();
				
			long byteRead = 0, totalByteRead = 0, inFileSize = inFileChannel.size();
			int percentage = 0;
			
			do {
				byteRead = inFileChannel.read(bEncryptedBuffer);
				if (byteRead == -1)
					break;

				bEncryptedBuffer.flip();
				byte[] encryptedData = new byte[bEncryptedBuffer.limit()],
						clearData = new byte[bEncryptedBuffer.limit()];
				for (int i = 0; i < bEncryptedBuffer.limit(); i++)
					encryptedData[i] = bEncryptedBuffer.array()[i];
				clearData = crypter.decrypt(encryptedData);
				bClearBuffer.put(clearData, 0, clearData.length);
				bClearBuffer.flip();
				while (bClearBuffer.hasRemaining())
					outFileChannel.write(bClearBuffer);
				bEncryptedBuffer.clear();
				bClearBuffer.clear();
				
				totalByteRead += byteRead;
				percentage = (int)((double)(totalByteRead)*100/inFileSize);
				this.progressBar.setValue(percentage);
				this.progressBar.setString(CConstants.FDT_DECRYPTING_STATUS + percentage + "%)");
			}
			while (byteRead != -1 && bEncryptedBuffer.hasRemaining());
			
			this.progressBar.setString(CConstants.FDT_DECRYPTING_DONE_STATUS);
			this.progressBar.setValue(100);
			
			inFileChannel.close();
			outFileChannel.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void getFileHeader() {
		try {
			inFileChannel = new RandomAccessFile(inputFilePath, "r").getChannel();
			int byteRead;
			
			inFileChannel.position(CConstants.DEF_FILE_CHECKSUM_LENGTH);
			
			ByteBuffer infoBuffer = ByteBuffer.allocate(CConstants.DEF_FILE_HEADER_LENGTH);
			infoBuffer.clear();
			inFileChannel.read(infoBuffer);
			infoBuffer.flip();

			byte[] infoBufferArray = infoBuffer.array();
			int cryptAlgorithmCode = new Byte(infoBufferArray[0]).intValue();
			int messageDigestAlgorithmCode = new Byte(infoBufferArray[1]).intValue();
			int messageDigestLength = new Byte(infoBufferArray[3]).intValue();
			int fileNameLength = new Byte(infoBufferArray[4]).intValue();
			
			this.cryptAlgorithm = CConstants.CRYPT_ALGORITHMS[cryptAlgorithmCode];
			this.messageDigestAlgorithm = CConstants.MESSAGE_DIGEST_ALGORITHMS[messageDigestAlgorithmCode];
			
			infoBuffer = ByteBuffer.allocate(messageDigestLength);
			infoBuffer.clear();
			do
				byteRead = inFileChannel.read(infoBuffer);
			while (byteRead != -1 && infoBuffer.hasRemaining());
			infoBuffer.flip();
			
			if (CFileCrypter.isPasswordProtected(this.inputFilePath)) {
				CPasswordAuthenticateDialog passwordAuthenticateDialog = new CPasswordAuthenticateDialog(null, true, this.messageDigestAlgorithm, infoBuffer.array());
				passwordAuthenticateDialog.setVisible(true);
				this.authenticationPassword = passwordAuthenticateDialog.authenticatedPassword;
				this.authenticated = passwordAuthenticateDialog.authenticated;
			}
			else {
				this.authenticationPassword = "";
				this.authenticated = true;
			}
			
			inFileChannel.position(inFileChannel.position() + fileNameLength);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class CPasswordAuthenticateDialog extends JDialog {
		private static final long serialVersionUID = 1L;
		protected boolean authenticated;
		protected String authenticatedPassword;
		
		private JButton jbtnOk, jbtnCancel;
		private JLabel jlblPassword;
		private JPasswordField jpwdPassword;
		private byte[] messageDigest;
		private String messageDigestAlgorithm;
		
		protected CPasswordAuthenticateDialog(JFrame jfrmParentFrame, boolean blnModal, String _messageDigestAlgorithm, byte[] _messageDigest) {
			super(jfrmParentFrame, blnModal);
			
			this.messageDigestAlgorithm = _messageDigestAlgorithm;
			this.messageDigest = _messageDigest;
			initComponents();
			
			jbtnOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					jbtnOkAction();
				}
			});
			
			jbtnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					JOptionPane.showMessageDialog(null, CConstants.DLG_PASSWORD_AUTHENTICATE_CANCEL_INFO_CONTENT,
													CConstants.DLG_PASSWORD_AUTHENTICATE_CANCEL_INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
					authenticated = false;
					dispose();
				}
			});
		}
		
		private void initComponents() {
			this.setLocation(300, 300);
			this.setResizable(false);
			this.setSize(330, 160);
			this.setTitle(CConstants.DLG_AUTHENTICATE_PASSWORD_TITLE);
			this.getContentPane().setLayout(null);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			
			Font fntInterfaceFont = new Font(CConstants.DEF_INTERFACE_FONT, 0, 13); 
			
			jpwdPassword = new JPasswordField(15);
			jpwdPassword.setBounds(new Rectangle(90, 20, 200, 30));
			jpwdPassword.setEchoChar(CConstants.DEF_PASSWORD_CHAR);
			jpwdPassword.setFont(fntInterfaceFont);
			this.getContentPane().add(jpwdPassword);
			
			jlblPassword = new JLabel(CConstants.DLG_LBL_PASSWORD_CAPTION);
			jlblPassword.setBounds(new Rectangle(20, 20, 60, 30));
			jlblPassword.setDisplayedMnemonic(CConstants.DLG_LBL_PASSWORD_MNEMONIC);
			jlblPassword.setFont(fntInterfaceFont);
			jlblPassword.setLabelFor(jpwdPassword);
			this.getContentPane().add(jlblPassword);
			
			jbtnOk = new JButton(CConstants.DLG_BTN_OK_CAPTION);
			jbtnOk.setBounds(new Rectangle(90, 70, 50, 30));
			jbtnOk.setFont(fntInterfaceFont);
			jbtnOk.setMnemonic(CConstants.DLG_BTN_OK_MNEMONIC);
			this.getContentPane().add(jbtnOk);
			
			jbtnCancel = new JButton(CConstants.DLG_BTN_CANCEL_CAPTION);
			jbtnCancel.setBounds(new Rectangle(180, 70, 70, 30));
			jbtnCancel.setFont(fntInterfaceFont);
			jbtnCancel.setMnemonic(CConstants.DLG_BTN_CANCEL_MNEMONIC);
			this.getContentPane().add(jbtnCancel);
			
			this.getRootPane().setDefaultButton(jbtnOk);
		}
		
		private void jbtnOkAction() {
			if (new String(jpwdPassword.getPassword()).equals(""))
				JOptionPane.showMessageDialog(null, CConstants.DLG_PASSWORD_BLANK_ERROR_CONTENT, CConstants.DLG_PASSWORD_BLANK_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			else {
				boolean passwordCheck = true;
				String inputPassword = new String(jpwdPassword.getPassword());
				byte[] inputPasswordByte = CCrypter.getMessageDigest(this.messageDigestAlgorithm, inputPassword);
				for (int i = 0; i < inputPasswordByte.length; i++)
					if (inputPasswordByte[i] != this.messageDigest[i])
						passwordCheck = false;
				
				if (passwordCheck) {
					JOptionPane.showMessageDialog(null, CConstants.DLG_PASSWORD_AUTHENTICATED_INFO_CONTENT,
													CConstants.DLG_PASSWORD_AUTHENTICATED_INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
					this.authenticated = true;
					this.authenticatedPassword = inputPassword;
					this.dispose();
				}
				else
					JOptionPane.showMessageDialog(null, CConstants.DLG_PASSWORD_WRONG_ERROR_CONTENT, CConstants.DLG_PASSWORD_WRONG_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
