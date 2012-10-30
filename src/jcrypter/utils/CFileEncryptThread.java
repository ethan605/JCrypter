package jcrypter.utils;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.security.*;
import java.util.*;
import javax.crypto.*;
import javax.swing.*;
import jcrypter.*;

public class CFileEncryptThread implements Runnable {
	private JProgressBar progressBar;
	private String cryptAlgorithm, messageDigestAlgorithm, authenticationPassword, inputFilePath, outputFilePath;
	private int cipherBlockSize, cleanInputFileMode;
	private FileChannel outFileChannel;
	
	public CFileEncryptThread(String _cryptAlgorithm, String _messageDigestAlgorithm, boolean _passwordProtected,
							String _inputFilePath, String _outputFilePath, int _cleanInputFileMode,
							JProgressBar _progressBar) {
		
		this.cryptAlgorithm = _cryptAlgorithm;
		this.messageDigestAlgorithm = _messageDigestAlgorithm;
	
		try {
			this.cipherBlockSize = Cipher.getInstance(this.cryptAlgorithm).getBlockSize();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
		CPasswordInputDialog passwordInputDialog = new CPasswordInputDialog(null, true);
		if (_passwordProtected)
			passwordInputDialog.setVisible(true);
		this.authenticationPassword = passwordInputDialog.enteredPassword;
		
		this.inputFilePath = _inputFilePath;
		this.outputFilePath = CFileCrypter.checkExistedFile(_outputFilePath);
		this.cleanInputFileMode = _cleanInputFileMode;
		this.progressBar = _progressBar;
	}
	
	public void run() {
		CCrypter crypter = new CCrypter(this.cryptAlgorithm, this.messageDigestAlgorithm, this.authenticationPassword);
		try {
			FileChannel inFileChannel = new RandomAccessFile(this.inputFilePath, "rw").getChannel();
			outFileChannel = new RandomAccessFile(this.outputFilePath, "rw").getChannel();
			ByteBuffer bClearBuffer = ByteBuffer.allocate(CConstants.CLEAR_BUFFER_ALLOCATION),
						bEncryptedBuffer = ByteBuffer.allocate(CConstants.ENCRYPTED_BUFFER_ALLOCATION);
			bClearBuffer.clear();
			bEncryptedBuffer.clear();
			long byteRead = 0, totalByteRead = 0, inFileSize = inFileChannel.size();
			int percentage = 0;
			
			this.initFileHeader();
			
			this.progressBar.setStringPainted(true);
			do {
				byteRead = inFileChannel.read(bClearBuffer);
				if (byteRead == -1)
					break;
				
				bClearBuffer.flip();
				byte[] clearData = new byte[bClearBuffer.limit()],
						encryptedData = new byte[(bClearBuffer.limit()/this.cipherBlockSize+1)*this.cipherBlockSize];
				for (int i = 0; i < bClearBuffer.limit(); i++)
					clearData[i] = bClearBuffer.array()[i];
				encryptedData = crypter.encrypt(clearData);
				bEncryptedBuffer.put(encryptedData, 0, (bClearBuffer.limit()/this.cipherBlockSize+1)*this.cipherBlockSize);
				bEncryptedBuffer.flip();
				while (bEncryptedBuffer.hasRemaining())
					outFileChannel.write(bEncryptedBuffer);
				bClearBuffer.clear();
				bEncryptedBuffer.clear();
				
				totalByteRead += byteRead;
				percentage = (int)((double)(totalByteRead)*100/inFileSize);
				this.progressBar.setValue(percentage);
				this.progressBar.setString(CConstants.FET_ENCRYPTING_STATUS + percentage + "%)");
			}
			while (byteRead != -1 && bClearBuffer.hasRemaining());
			this.progressBar.setString(CConstants.FET_ENCRYPTING_DONE_STATUS);
			this.progressBar.setValue(100);
			
			this.fillFileChecksum();

			inFileChannel.close();
			outFileChannel.close();
			
			this.cleanInputFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initFileHeader() {
		ByteBuffer infoBuffer;

		try {
			byte[] initChecksum = new byte[CConstants.DEF_FILE_CHECKSUM_LENGTH];
			for (int i = 0; i < CConstants.DEF_FILE_CHECKSUM_LENGTH; i++)
				initChecksum[i] = 0;
			infoBuffer = ByteBuffer.allocate(CConstants.DEF_FILE_CHECKSUM_LENGTH);
			infoBuffer.clear();
			infoBuffer.put(initChecksum);
			infoBuffer.flip();
			while (infoBuffer.hasRemaining())
				outFileChannel.write(infoBuffer);
			
			byte[] passwordBytes = CCrypter.getMessageDigest(this.messageDigestAlgorithm, this.authenticationPassword);
			String inFileName = new File(this.inputFilePath).getName();
			infoBuffer = ByteBuffer.allocate(CConstants.DEF_FILE_HEADER_LENGTH);
			infoBuffer.clear();
			infoBuffer.put(new byte[] {new Integer(CCrypter.getCryptAlgorithmCode(this.cryptAlgorithm)).byteValue(),
										new Integer(CCrypter.getMessageDigestAlgorithmCode(this.messageDigestAlgorithm)).byteValue(),
										new Integer((this.authenticationPassword.compareTo("") == 0) ? 0 : 1).byteValue(),
										new Integer(passwordBytes.length).byteValue(),
										new Integer(inFileName.length()).byteValue()});
			infoBuffer.flip();
			while (infoBuffer.hasRemaining())
				outFileChannel.write(infoBuffer);
			
			infoBuffer = ByteBuffer.allocate(passwordBytes.length);
			infoBuffer.clear();
			infoBuffer.put(passwordBytes);
			infoBuffer.flip();
			while (infoBuffer.hasRemaining())
				outFileChannel.write(infoBuffer);
			
			infoBuffer = ByteBuffer.allocate(inFileName.length());
			infoBuffer.clear();
			infoBuffer.put(inFileName.getBytes());
			infoBuffer.flip();
			while (infoBuffer.hasRemaining())
				outFileChannel.write(infoBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void fillFileChecksum() {
		try {
			outFileChannel.position(0);
			ByteBuffer infoBuffer = ByteBuffer.allocate(CConstants.DEF_FILE_CHECKSUM_LENGTH);
			infoBuffer.clear();
			infoBuffer.put(CFileCrypter.getFileChecksum(this.outputFilePath, CConstants.DEF_FILE_CHECKSUM_LENGTH));
			infoBuffer.flip();
			outFileChannel.write(infoBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void cleanInputFile() {
		switch (this.cleanInputFileMode) {
			case CConstants.NO_CLEAN_INPUT_FILE_MODE:
				return;
			case CConstants.REMOVE_INPUT_FILE_MODE:
				new File(this.inputFilePath).delete();
				return;
			case CConstants.WIPE_INPUT_FILE_MODE:
				this.wipeInputFile();
				return;
		}
	}
	
	private void wipeInputFile() {
		try {
			File inFile = new File(this.inputFilePath);
			FileOutputStream inFileStream = new FileOutputStream(inFile);
			long inFileSize = inFile.length();
			
			Random randGen = new Random();
			
			this.progressBar.setIndeterminate(true);
			this.progressBar.setString(CConstants.FET_WIPING_FILE_STATUS);
			for (int i = 0; i < CConstants.DEF_WIPE_INPUT_FILE_TIMES; i++) {
				byte[] randomBuffer = new byte[CConstants.CLEAR_BUFFER_ALLOCATION];
				for (int j = 0; j < randomBuffer.length; j++)
					randomBuffer[j] = new Integer(randGen.nextInt(Integer.MAX_VALUE)).byteValue();
				for (int j = 0; j < (inFileSize/CConstants.CLEAR_BUFFER_ALLOCATION+1); j++) {
					inFileStream.write(randomBuffer);
				}
			}
			this.progressBar.setIndeterminate(false);
			this.progressBar.setString(CConstants.FET_WIPING_FILE_DONE_STATUS);
			this.progressBar.setValue(100);
			
			inFileStream.close();
			inFile.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class CPasswordInputDialog extends JDialog {
		private static final long serialVersionUID = 1L;
		protected String enteredPassword = "";
		
		private JButton jbtnOk, jbtnCancel;
		private JLabel jlblPassword, jlblPasswordAgain;
		private JPasswordField jpwdPassword, jpwdPasswordAgain;
		
		protected CPasswordInputDialog(JFrame jfrmParentFrame, boolean blnModal) {
			super(jfrmParentFrame, blnModal);
			initComponents();
			
			jbtnOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					jbtnOkAction();
				}
			});
			
			jbtnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					JOptionPane.showMessageDialog(null, CConstants.DLG_PASSWORD_INPUT_CANCEL_INFO_CONTENT,
													CConstants.DLG_PASSWORD_INPUT_CANCEL_INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
			});
		}
		
		private void initComponents() {
			this.setLocation(300, 300);
			this.setResizable(false);
			this.setSize(360, 180);
			this.setTitle(CConstants.DLG_NEW_PASSWORD_TITLE);
			this.getContentPane().setLayout(null);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			
			Font fntInterfaceFont = new Font(CConstants.DEF_INTERFACE_FONT, 0, 13); 
			
			jpwdPassword = new JPasswordField(15);
			jpwdPassword.setBounds(new Rectangle(130, 10, 200, 30));
			jpwdPassword.setEchoChar(CConstants.DEF_PASSWORD_CHAR);
			jpwdPassword.setFont(fntInterfaceFont);
			this.getContentPane().add(jpwdPassword);
			
			jpwdPasswordAgain = new JPasswordField(15);
			jpwdPasswordAgain.setBounds(new Rectangle(130, 50, 200, 30));
			jpwdPasswordAgain.setEchoChar(CConstants.DEF_PASSWORD_CHAR);
			jpwdPasswordAgain.setFont(fntInterfaceFont);
			this.getContentPane().add(jpwdPasswordAgain);
			
			jlblPassword = new JLabel(CConstants.DLG_LBL_PASSWORD_CAPTION);
			jlblPassword.setBounds(new Rectangle(10, 10, 100, 30));
			jlblPassword.setDisplayedMnemonic(CConstants.DLG_LBL_PASSWORD_MNEMONIC);
			jlblPassword.setFont(fntInterfaceFont);
			jlblPassword.setLabelFor(jpwdPassword);
			this.getContentPane().add(jlblPassword);
			
			jlblPasswordAgain = new JLabel(CConstants.DLG_LBL_PASSWORD_CONFIRM_CAPTION);
			jlblPasswordAgain.setBounds(new Rectangle(10, 50, 110, 30));
			jlblPasswordAgain.setDisplayedMnemonic(CConstants.DLG_LBL_PASSWORD_CONFIRM_MNEMONIC);
			jlblPasswordAgain.setFont(fntInterfaceFont);
			jlblPasswordAgain.setLabelFor(jpwdPasswordAgain);
			this.getContentPane().add(jlblPasswordAgain);
			
			jbtnOk = new JButton(CConstants.DLG_BTN_OK_CAPTION);
			jbtnOk.setBounds(new Rectangle(100, 100, 50, 30));
			jbtnOk.setFont(fntInterfaceFont);
			jbtnOk.setMnemonic(CConstants.DLG_BTN_OK_MNEMONIC);
			this.getContentPane().add(jbtnOk);
			
			jbtnCancel = new JButton(CConstants.DLG_BTN_CANCEL_CAPTION);
			jbtnCancel.setBounds(new Rectangle(190, 100, 70, 30));
			jbtnCancel.setFont(fntInterfaceFont);
			jbtnCancel.setMnemonic(CConstants.DLG_BTN_CANCEL_MNEMONIC);
			this.getContentPane().add(jbtnCancel);
			
			this.getRootPane().setDefaultButton(jbtnOk);
		}
		
		private void jbtnOkAction() {
			if (new String(jpwdPassword.getPassword()).equals(""))
				JOptionPane.showMessageDialog(null, CConstants.DLG_PASSWORD_BLANK_ERROR_CONTENT,
												CConstants.DLG_PASSWORD_BLANK_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			else
				if (!new String(jpwdPassword.getPassword()).equals(new String(jpwdPasswordAgain.getPassword())))
					JOptionPane.showMessageDialog(null, CConstants.DLG_PASSWORD_NOT_MATCH_ERROR_CONTENT,
													CConstants.DLG_PASSWORD_NOT_MATCH_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
				else {
					JOptionPane.showMessageDialog(null, CConstants.DLG_PASSWORD_MATCH_INFO_CONTENT,
													CConstants.DLG_PASSWORD_MATCH_INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
					this.enteredPassword = new String(jpwdPassword.getPassword());
					this.dispose();
				}
		}
	}
}