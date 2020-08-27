package jcrypter;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

import jcrypter.utils.*;

public class CFileChecksumPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JButton jbtnBrowseInput, jbtnChecksum;
	private JCheckBox jckbUpperCaseDisplay;
	private JComboBox<String> jcbxChecksumAlgorithm;
	private JLabel jlblInputFile, jlblChecksumAlgorithm, jlblChecksumResult;
	private JProgressBar jpgbProgBar;
	private JTextArea jtxtChecksumResult;
	private JTextField jtxtInputFile;

	protected CFileChecksumPanel() {
		this.initComponents();
		this.assignActionListeners();
	}
	
	private void assignActionListeners() {
		jtxtInputFile.getDocument().addDocumentListener(new jtxtInputAction());
		jbtnBrowseInput.addActionListener(new jbtnBrowseAction());
		jckbUpperCaseDisplay.addActionListener(new jckbUpperCaseAction());
		jtxtChecksumResult.addFocusListener(new jtxtChecksumFocusAction());
		
		jcbxChecksumAlgorithm.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				jbtnChecksum.setEnabled(true);
			}
		});
		
		jbtnChecksum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new Thread(new CFileChecksumThread(jtxtInputFile.getText(),
													(String)jcbxChecksumAlgorithm.getSelectedItem(),
													jtxtChecksumResult,
													jckbUpperCaseDisplay.isSelected(),
													jpgbProgBar)).start();
				jbtnChecksum.setEnabled(false);
			}
		});
	}
	
	private void initComponents() {
		Font fntInterfaceFont = new Font(CConstants.DEF_INTERFACE_FONT, 0, 13);

		this.setBackground(new Color(245, 245, 245));
		this.setLayout(null);
		this.setPreferredSize(new Dimension(500, 320));

		String currentDir = System.getProperty("user.dir"),
				pathAppend = (currentDir.charAt(currentDir.length()-1) == File.separatorChar) ? "" : File.separator;
		jtxtInputFile = new JTextField(currentDir + pathAppend);
		jtxtInputFile.setBounds(new Rectangle(80, 20, 330, 30));
		jtxtInputFile.setFont(fntInterfaceFont);
		jtxtInputFile.setToolTipText(CConstants.FCS_TXT_INPUT_TOOLTIP);
		this.add(jtxtInputFile);
		
		jlblInputFile = new JLabel(CConstants.FCS_TXT_INPUT_CAPTION);
		jlblInputFile.setBounds(new Rectangle(15, 20, 60, 30));
		jlblInputFile.setDisplayedMnemonic(CConstants.FCS_TXT_INPUT_MNEMONIC);
		jlblInputFile.setLabelFor(jtxtInputFile);
		jlblInputFile.setFont(fntInterfaceFont);
		this.add(jlblInputFile);
		
		jbtnBrowseInput = new JButton(CConstants.FCS_BTN_BROWSE_INPUT_CAPTION);
		jbtnBrowseInput.setBounds(new Rectangle(420, 20, 70, 30));
		jbtnBrowseInput.setFont(fntInterfaceFont);
		jbtnBrowseInput.setMnemonic(CConstants.FCS_BTN_BROWSE_INPUT_MNEMONIC);
		jbtnBrowseInput.setToolTipText(CConstants.FCS_BTN_BROWSE_INPUT_TOOLTIP);
		this.add(jbtnBrowseInput);
		
		jcbxChecksumAlgorithm = new JComboBox<String>(CConstants.CHECKSUM_ALGORITHMS);
		jcbxChecksumAlgorithm.setBounds(new Rectangle(170, 70, 90, 30));
		jcbxChecksumAlgorithm.setEnabled(false);
		jcbxChecksumAlgorithm.setFont(fntInterfaceFont);
		this.add(jcbxChecksumAlgorithm);
		
		jlblChecksumAlgorithm = new JLabel(CConstants.FCS_LBL_CHECKSUM_ALGORITHM_CAPTION);
		jlblChecksumAlgorithm.setBounds(new Rectangle(50, 70, 110, 30));
		jlblChecksumAlgorithm.setDisplayedMnemonic(CConstants.FCS_LBL_CHECKSUM_ALGORITHM_MNEMONIC);
		jlblChecksumAlgorithm.setFont(fntInterfaceFont);
		jlblChecksumAlgorithm.setLabelFor(jcbxChecksumAlgorithm);
		this.add(jlblChecksumAlgorithm);
		
		jckbUpperCaseDisplay = new JCheckBox(CConstants.FCS_CKB_UPPER_CASE_DISPLAY_CAPTION);
		jckbUpperCaseDisplay.setBounds(new Rectangle(330, 70, 120, 30));
		jckbUpperCaseDisplay.setEnabled(false);
		jckbUpperCaseDisplay.setMnemonic(CConstants.FCS_CKB_UPPER_CASE_DISPLAY_MNEMONIC);
		jckbUpperCaseDisplay.setFont(fntInterfaceFont);
		this.add(jckbUpperCaseDisplay);
		
		jtxtChecksumResult = new JTextArea();
		jtxtChecksumResult.setBounds(new Rectangle(50, 130, 400, 60));
		jtxtChecksumResult.setEditable(false);
		jtxtChecksumResult.setEnabled(false);
		jtxtChecksumResult.setFont(new Font(CConstants.DEF_TEXT_CODE_FONT, 0, 13));
		jtxtChecksumResult.setLineWrap(true);
		this.add(jtxtChecksumResult);
		
		jlblChecksumResult = new JLabel(CConstants.FCS_LBL_CHECKSUM_RESULT_CAPTION);
		jlblChecksumResult.setBounds(new Rectangle(190, 105, 120, 30));
		jlblChecksumResult.setDisplayedMnemonic(CConstants.FCS_LBL_CHECKSUM_RESULT_MNEMONIC);
		jlblChecksumResult.setFont(fntInterfaceFont);
		jlblChecksumResult.setLabelFor(jtxtChecksumResult);
		this.add(jlblChecksumResult);
		
		jbtnChecksum = new JButton(CConstants.FCS_BTN_CHECKSUM_CAPTION);
		jbtnChecksum.setBounds(new Rectangle(190, 210, 120, 50));
		jbtnChecksum.setEnabled(false);
		jbtnChecksum.setFont(new Font(CConstants.DEF_INTERFACE_FONT, 0, 15));
		jbtnChecksum.setMnemonic(CConstants.FCS_BTN_CHECKSUM_MNEMONIC);
		jbtnChecksum.setToolTipText(CConstants.FCS_BTN_CHECKSUM_TOOLTIP);
		this.add(jbtnChecksum);
		
		jpgbProgBar = new JProgressBar();
		jpgbProgBar.setBounds(new Rectangle(30, 280, 440, 20));
		jpgbProgBar.setFont(new Font(CConstants.DEF_INTERFACE_FONT, 0, 11));
		jpgbProgBar.setValue(0);
		this.add(jpgbProgBar);
	}
	
	private class jbtnBrowseAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			String fileChooserFolder;
			fileChooserFolder = jtxtInputFile.getText();
		
			if (fileChooserFolder.charAt(fileChooserFolder.length()-1) != File.separatorChar)
				fileChooserFolder = fileChooserFolder.substring(0, fileChooserFolder.lastIndexOf(File.separator));
			
			JFileChooser fileChooser = new JFileChooser(fileChooserFolder);
			
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				String filePath = fileChooser.getSelectedFile().getAbsolutePath();
				jtxtInputFile.setText(filePath);
			}
		}
	}
	
	private class jckbUpperCaseAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if (jckbUpperCaseDisplay.isSelected())
				jtxtChecksumResult.setText(jtxtChecksumResult.getText().toUpperCase());
			else
				jtxtChecksumResult.setText(jtxtChecksumResult.getText().toLowerCase());
		}
	}
	
	private class jtxtInputAction implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			updateAction();
		}
		public void insertUpdate(DocumentEvent e) {
			updateAction();
		}
		public void removeUpdate(DocumentEvent e) {
			updateAction();
		}
		public void updateAction() {
			File file = new File(jtxtInputFile.getText());
			if (file.isFile()) {
				jcbxChecksumAlgorithm.setEnabled(true);
				jtxtChecksumResult.setEnabled(true);
				jckbUpperCaseDisplay.setEnabled(true);
				jbtnChecksum.setEnabled(true);
				getRootPane().setDefaultButton(jbtnChecksum);
			}
			else {
				jcbxChecksumAlgorithm.setEnabled(false);
				jtxtChecksumResult.setEnabled(false);
				jtxtChecksumResult.setText("");
				jckbUpperCaseDisplay.setEnabled(false);
				jbtnChecksum.setEnabled(false);
				jpgbProgBar.setValue(0);
				jpgbProgBar.setStringPainted(false);
			}
		}
	}
	
	private class jtxtChecksumFocusAction implements FocusListener {
		public void focusGained(FocusEvent fe) {
			jtxtChecksumResult.selectAll();
			jtxtChecksumResult.setToolTipText(CConstants.COPIED_TO_CLIPBOARD_TOOLTIP);
			ToolTipManager.sharedInstance().setInitialDelay(0);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
		    		new StringSelection(jtxtChecksumResult.getText()), null);
			ToolTipManager.sharedInstance().mouseMoved(
					new MouseEvent(jtxtChecksumResult, 0, 0, 0,	100, 10, 0, false));
			ToolTipManager.sharedInstance().setInitialDelay(750);
		}
		public void focusLost(FocusEvent fe) {
			jtxtChecksumResult.setToolTipText(null);
		}		
	}
}
