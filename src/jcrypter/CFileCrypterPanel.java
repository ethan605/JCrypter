package jcrypter;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import jcrypter.utils.*;

public class CFileCrypterPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  private JButton jbtnBrowseInput, jbtnBrowseOutput, jbtnEncrypt, jbtnDecrypt;
  private JCheckBox jckbMDAlgorithm, jckbRemoveInputFile, jckbWipeInputFile;
  private JComboBox<String> jcbxCryptAlgorithm, jcbxMDAlgorithm;
  private JLabel jlblInputFile, jlblOutputFile, jlblCryptAlgorithm;
  private JProgressBar jpgbProgBar;
  private JTextField jtxtInputFile, jtxtOutputFile;
    
  protected CFileCrypterPanel() {
    this.initComponents();
    this.assignActionListeners();
  }
  
  private void assignActionListeners() {
    jtxtInputFile.getDocument().addDocumentListener(new jtxtInputAction());
    jbtnBrowseInput.addActionListener(new jbtnBrowseAction());
    jbtnBrowseOutput.addActionListener(new jbtnBrowseAction());
    jckbMDAlgorithm.addActionListener(new jckbMDAlgorithmAction());
    jckbRemoveInputFile.addActionListener(new jckbCheckBoxesAction());
    jcbxCryptAlgorithm.addItemListener(new jcbxComboBoxItemAction());
    jcbxMDAlgorithm.addItemListener(new jcbxComboBoxItemAction());
    
    jbtnEncrypt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        new Thread(new CFileEncryptThread((String)jcbxCryptAlgorithm.getSelectedItem(),
                        (String)jcbxMDAlgorithm.getSelectedItem(),
                        jckbMDAlgorithm.isSelected(),
                        jtxtInputFile.getText(),
                        jtxtOutputFile.getText(),
                        getCleanInputFileMode(),
                        jpgbProgBar)).start();
        jbtnEncrypt.setEnabled(false);
      }
    });
    
    jbtnDecrypt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        new Thread(new CFileDecryptThread(jtxtInputFile.getText(),
                          jtxtOutputFile.getText(),
                          jpgbProgBar)).start();
        jbtnDecrypt.setEnabled(false);
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
    jtxtInputFile.setToolTipText(CConstants.FCR_TXT_INPUT_TOOLTIP);
    this.add(jtxtInputFile);
    
    jtxtOutputFile = new JTextField(currentDir + pathAppend);
    jtxtOutputFile.setBounds(new Rectangle(80, 60, 330, 30));
    jtxtOutputFile.setEditable(false);
    jtxtOutputFile.setFont(fntInterfaceFont);
    jtxtOutputFile.setToolTipText(CConstants.FCR_TXT_OUTPUT_TOOLTIP);
    this.add(jtxtOutputFile);
    
    jlblInputFile = new JLabel(CConstants.FCR_TXT_INPUT_CAPTION);
    jlblInputFile.setBounds(new Rectangle(15, 20, 60, 30));
    jlblInputFile.setDisplayedMnemonic(CConstants.FCR_LBL_INPUT_MNEMONIC);
    jlblInputFile.setLabelFor(jtxtInputFile);
    jlblInputFile.setFont(fntInterfaceFont);
    this.add(jlblInputFile);
    
    jlblOutputFile = new JLabel(CConstants.FCR_TXT_OUTPUT_CAPTION);
    jlblOutputFile.setBounds(new Rectangle(15, 60, 60, 30));
    jlblOutputFile.setLabelFor(jtxtOutputFile);
    jlblOutputFile.setFont(fntInterfaceFont);
    this.add(jlblOutputFile);
    
    jbtnBrowseInput = new JButton(CConstants.FCR_BTN_BROWSE_CAPTION);
    jbtnBrowseInput.setBounds(new Rectangle(420, 20, 70, 30));
    jbtnBrowseInput.setFont(fntInterfaceFont);
    jbtnBrowseInput.setMnemonic(CConstants.FCR_BTN_BROWSE_INPUT_MNEMONIC);
    jbtnBrowseInput.setToolTipText(CConstants.FCR_BTN_BROWSE_INPUT_TOOLTIP);
    this.add(jbtnBrowseInput);
    
    jbtnBrowseOutput = new JButton(CConstants.FCR_BTN_BROWSE_CAPTION);
    jbtnBrowseOutput.setBounds(new Rectangle(420, 60, 70, 30));
    jbtnBrowseOutput.setFont(fntInterfaceFont);
    jbtnBrowseOutput.setMnemonic(CConstants.FCR_BTN_BROWSE_OUTPUT_MNEMONIC);
    jbtnBrowseOutput.setToolTipText(CConstants.FCR_BTN_BROWSE_OUTPUT_TOOLTIP);
    this.add(jbtnBrowseOutput);
    
    jcbxCryptAlgorithm = new JComboBox<String>(CConstants.CRYPT_ALGORITHMS);
    jcbxCryptAlgorithm.setBounds(new Rectangle(120, 110, 80, 30));
    jcbxCryptAlgorithm.setEnabled(false);
    jcbxCryptAlgorithm.setFont(fntInterfaceFont);
    this.add(jcbxCryptAlgorithm);
    
    jlblCryptAlgorithm = new JLabel(CConstants.FCR_LBL_CRYPT_ALGORITHM_CAPTION);
    jlblCryptAlgorithm.setBounds(new Rectangle(20, 110, 90, 30));
    jlblCryptAlgorithm.setDisplayedMnemonic(CConstants.FCR_LBL_CRYPT_ALGORITHM_MNEMONIC);
    jlblCryptAlgorithm.setFont(fntInterfaceFont);
    jlblCryptAlgorithm.setLabelFor(jcbxCryptAlgorithm);
    this.add(jlblCryptAlgorithm);
    
    jckbMDAlgorithm = new JCheckBox(CConstants.FCR_LBL_MESSAGE_DIGEST_ALGORITHM_CAPTION);
    jckbMDAlgorithm.setBounds(new Rectangle(240, 110, 140, 30));
    jckbMDAlgorithm.setEnabled(false);
    jckbMDAlgorithm.setFont(fntInterfaceFont);
    jckbMDAlgorithm.setMnemonic(CConstants.FCR_LBL_MESSAGE_DIGEST_ALGORITHM_MNEMONIC);
    this.add(jckbMDAlgorithm);
    
    jcbxMDAlgorithm = new JComboBox<String>(CConstants.MESSAGE_DIGEST_ALGORITHMS);
    jcbxMDAlgorithm.setBounds(new Rectangle(390, 110, 90, 30));
    jcbxMDAlgorithm.setEnabled(false);
    jcbxMDAlgorithm.setFont(fntInterfaceFont);
    this.add(jcbxMDAlgorithm);
    
    jckbRemoveInputFile = new JCheckBox(CConstants.FCR_CKB_REMOVE_INPUT_CAPTION);
    jckbRemoveInputFile.setBounds(new Rectangle(20, 150, 220, 30));
    jckbRemoveInputFile.setEnabled(false);
    jckbRemoveInputFile.setFont(fntInterfaceFont);
    jckbRemoveInputFile.setMnemonic(CConstants.FCR_CKB_REMOVE_INPUT_MNEMONIC);
    this.add(jckbRemoveInputFile);
    
    jckbWipeInputFile = new JCheckBox(CConstants.FCR_CKB_WIPEOUT_INPUT_CAPTION);
    jckbWipeInputFile.setBounds(new Rectangle(280, 150, 200, 30));
    jckbWipeInputFile.setEnabled(false);
    jckbWipeInputFile.setFont(fntInterfaceFont);
    jckbWipeInputFile.setMnemonic(CConstants.FCR_CKB_WIPEOUT_INPUT_MNEMONIC);
    this.add(jckbWipeInputFile);
    
    jbtnEncrypt = new JButton(CConstants.FCR_BTN_ENCRYPT_CAPTION);
    jbtnEncrypt.setBounds(new Rectangle(100, 200, 120, 50));
    jbtnEncrypt.setEnabled(false);
    jbtnEncrypt.setFont(new Font(CConstants.DEF_INTERFACE_FONT, 0, 15));
    jbtnEncrypt.setMnemonic(CConstants.FCR_BTN_ENCRYPT_MNEMONIC);
    jbtnEncrypt.setToolTipText(CConstants.FCR_BTN_ENCRYPT_TOOLTIP);
    this.add(jbtnEncrypt);
    
    jbtnDecrypt = new JButton(CConstants.FCR_BTN_DECRYPT_CAPTION);
    jbtnDecrypt.setBounds(new Rectangle(280, 200, 120, 50));
    jbtnDecrypt.setFont(new Font(CConstants.DEF_INTERFACE_FONT, 0, 15));
    jbtnDecrypt.setEnabled(false);
    jbtnDecrypt.setMnemonic(CConstants.FCR_BTN_DECRYPT_MNEMONIC);
    jbtnDecrypt.setToolTipText(CConstants.FCR_BTN_DECRYPT_TOOLTIP);
    this.add(jbtnDecrypt);
    
    jpgbProgBar = new JProgressBar();
    jpgbProgBar.setBounds(new Rectangle(30, 280, 440, 20));
    jpgbProgBar.setFont(new Font(CConstants.DEF_INTERFACE_FONT, 0, 11));
    jpgbProgBar.setValue(0);
    this.add(jpgbProgBar);
  }
  
  private void afterCheckInputFile(int checkFileResult) {
    File inputFile = new File(jtxtInputFile.getText());
    
    String inputFileName = jtxtInputFile.getText();
    if (!inputFile.isFile() || new File(inputFileName.substring(0, inputFileName.length()-1)).isFile()) {
      jbtnEncrypt.setEnabled(false);
      jbtnDecrypt.setEnabled(false);
      jcbxCryptAlgorithm.setEnabled(false);
      jckbMDAlgorithm.setSelected(false);
      jcbxMDAlgorithm.setEnabled(false);
      jckbMDAlgorithm.setEnabled(false);
      jckbRemoveInputFile.setSelected(false);
      jckbWipeInputFile.setSelected(false);
      jckbRemoveInputFile.setEnabled(false);
      jckbWipeInputFile.setEnabled(false);
      jpgbProgBar.setValue(0);
      jpgbProgBar.setStringPainted(false);
      return;
    }
    
    if (checkFileResult == CConstants.FILE_HEALTHY) {
      jbtnEncrypt.setEnabled(false);
      jbtnDecrypt.setEnabled(true);
      jcbxCryptAlgorithm.setEnabled(false);
      jcbxMDAlgorithm.setEnabled(false);
      jckbMDAlgorithm.setEnabled(false);
      jckbRemoveInputFile.setEnabled(false);
      jckbWipeInputFile.setEnabled(false);
      this.getRootPane().setDefaultButton(jbtnDecrypt);
    }
    else {
      jbtnEncrypt.setEnabled(true);
      jbtnDecrypt.setEnabled(false);
      jcbxCryptAlgorithm.setEnabled(true);
      jcbxMDAlgorithm.setEnabled(false);
      jckbMDAlgorithm.setEnabled(true);
      jckbMDAlgorithm.setSelected(false);
      jckbRemoveInputFile.setEnabled(true);
      this.getRootPane().setDefaultButton(jbtnEncrypt);
    }
  }
  
  private void updateOutputFileName() {
    File inFile = new File(jtxtInputFile.getText());
    String inFilePath = inFile.getName(),
        outFilePath = jtxtOutputFile.getText(),
        outFileName = "";
    
    if (jbtnEncrypt.isEnabled())
      outFileName = inFilePath + CConstants.DEF_ENCRYPTED_EXTENSION;
    else
      if (jbtnDecrypt.isEnabled()) 
        outFileName = CFileCrypter.getDecryptedFileName(jtxtInputFile.getText());
    
    if (inFile.isFile()) {
      if (outFilePath.endsWith(File.separator))
        jtxtOutputFile.setText(jtxtOutputFile.getText() + outFileName);
    }
    else {
      String outFileFolder = outFilePath.substring(0, outFilePath.lastIndexOf(File.separator)+1);
      jtxtOutputFile.setText(outFileFolder + outFileName);
    }
  }
  
  private int getCleanInputFileMode() {
    int removeInputFile = (jckbRemoveInputFile.isSelected()) ? 1 : 0,
      wipeInputFile = (jckbRemoveInputFile.isSelected() && jckbWipeInputFile.isSelected()) ? 1 : 0;
    return (removeInputFile + wipeInputFile);
  }
  
  private class jckbMDAlgorithmAction implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      jcbxMDAlgorithm.setEnabled(jckbMDAlgorithm.isSelected());
      jbtnEncrypt.setEnabled(true);
    }
  }
  
  private class jbtnBrowseAction implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      String fileChooserFolder;
      fileChooserFolder = (ae.getSource() == jbtnBrowseInput) ?
                jtxtInputFile.getText() : jtxtOutputFile.getText();
    
      if (fileChooserFolder.charAt(fileChooserFolder.length()-1) != File.separatorChar)
        fileChooserFolder = fileChooserFolder.substring(0, fileChooserFolder.lastIndexOf(File.separator));
      
      JFileChooser fileChooser = new JFileChooser(fileChooserFolder);
      if (ae.getSource() == jbtnBrowseOutput)
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      
      if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        if (ae.getSource() == jbtnBrowseInput) {
          String filePath = fileChooser.getSelectedFile().getAbsolutePath();
          jtxtInputFile.setText(filePath);
        }
        else {
          if (fileChooser.getSelectedFile().getParent() != null)
            jtxtOutputFile.setText(fileChooser.getSelectedFile().getAbsolutePath() + File.separator);
          else
            jtxtOutputFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
          updateOutputFileName();
        }
    }
  }
  
  private class jtxtInputAction implements DocumentListener, Runnable {
    private int checkFileResult;
    
    public void run() {
      jpgbProgBar.setIndeterminate(true);
      jpgbProgBar.setString(CConstants.FCR_PGB_CHECKING_FILE_CONTENT);
      jpgbProgBar.setStringPainted(true);
      jpgbProgBar.setValue(100);
      this.checkFileResult = CFileCrypter.checkFile(jtxtInputFile.getText());
      jpgbProgBar.setIndeterminate(false);
      jpgbProgBar.setString(CConstants.FCR_PGB_CHECKING_FILE_DONE_CONTENT);
      afterCheckInputFile(this.checkFileResult);
      updateOutputFileName();
    }
    
    public void insertUpdate(DocumentEvent de) {
      this.updateAction();
    }
    public void removeUpdate(DocumentEvent de) {
      this.updateAction();
    }
    public void changedUpdate(DocumentEvent de) {
      this.updateAction();
    }
    
    private void updateAction() {
      File checkFile = new File(jtxtInputFile.getText());
      if (checkFile.isFile()) {
        File checkFile2 = new File(jtxtInputFile.getText().substring(0, jtxtInputFile.getText().length()-1));
        if (!checkFile2.isFile())
          new Thread(new jtxtInputAction()).start();
      }
      else {
        afterCheckInputFile(this.checkFileResult);
        updateOutputFileName();
      }
    }
  }
  
  private class jckbCheckBoxesAction implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      jckbWipeInputFile.setEnabled(jckbRemoveInputFile.isSelected());
    }
  }
  
  private class jcbxComboBoxItemAction implements ItemListener {
    public void itemStateChanged(ItemEvent ie) {
      if (new File(jtxtInputFile.getText()).exists())
        jbtnEncrypt.setEnabled(true);
    }
  }
}
