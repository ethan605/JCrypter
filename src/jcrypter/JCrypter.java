package jcrypter;

import java.awt.*;
import javax.swing.*;

public class JCrypter extends JFrame {
  private static final long serialVersionUID = 1L;
  
  private JMenuBar jmbrMainMenu;
  private JPanel jpnlFileCrypter, jpnlFileChecksum, jpnlTextCrypter, jpnlTextChecksum;
  private JTabbedPane jtbpMainTabbedPane;
  
  public static void main(String args[]) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new JCrypter();
      }
    });
  }
  
  public JCrypter() {
    this.initLookNFeel();
    this.checkExistedInstance();
    this.initComponents();
  }
  
  private void initComponents() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocation(200, 200);
    this.setResizable(false);
    this.setSize(500, 410);
    this.setTitle(CConstants.MAIN_TITLE);
    this.getContentPane().setBackground(new Color(245, 245, 245));
    this.getContentPane().setLayout(new FlowLayout());
    
    jmbrMainMenu = new CMainMenu();
    this.setJMenuBar(jmbrMainMenu);
    
    jtbpMainTabbedPane = new JTabbedPane(JTabbedPane.TOP);
    jtbpMainTabbedPane.setFont(new Font(CConstants.DEF_INTERFACE_FONT, 0, 15));
    
    jpnlFileCrypter = new CFileCrypterPanel();
    jtbpMainTabbedPane.addTab(CConstants.FILE_CRYPTER_PANEL_TITLE, jpnlFileCrypter);
    jtbpMainTabbedPane.setMnemonicAt(0, CConstants.FILE_CRYPTER_PANEL_MNEMONIC);
    
    jpnlFileChecksum = new CFileChecksumPanel();
    jtbpMainTabbedPane.addTab(CConstants.FILE_CHECKSUM_PANEL_TITLE, jpnlFileChecksum);
    jtbpMainTabbedPane.setMnemonicAt(1, CConstants.FILE_CHECKSUM_PANEL_MNEMONIC);
    
    jpnlTextCrypter = new CTextCrypterPanel();
    jtbpMainTabbedPane.addTab(CConstants.TEXT_CRYPTER_PANEL_TITLE, jpnlTextCrypter);
    jtbpMainTabbedPane.setMnemonicAt(2, CConstants.TEXT_CRYPTER_PANEL_MNEMONIC);
    
    jpnlTextChecksum = new CTextChecksumPanel();
    jtbpMainTabbedPane.addTab(CConstants.TEXT_CHECKSUM_PANEL_TITLE, jpnlTextChecksum);
    jtbpMainTabbedPane.setMnemonicAt(3, CConstants.TEXT_CHECKSUM_PANEL_MNEMONIC);
    
    this.getContentPane().add(jtbpMainTabbedPane);
    this.setVisible(true);
  }
  
  private void initLookNFeel() {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
      UIManager.put("Component.showMnemonics", true);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }
  
  private void checkExistedInstance() {
    try { 
      new java.net.ServerSocket(CConstants.DEF_SOCK_PORT, 0, java.net.InetAddress.getByAddress(CConstants.DEF_LOCAL_HOST));
    } catch (java.net.BindException e) {
      JOptionPane.showMessageDialog(null, CConstants.EXISTED_INSTANCE_WARNING_CONTENT, CConstants.EXISTED_INSTANCE_WARNING_TITLE, JOptionPane.WARNING_MESSAGE);
      System.exit(1);
    } catch (java.io.IOException e) {
      e.printStackTrace();
      System.exit(2);
    }
  }
}
