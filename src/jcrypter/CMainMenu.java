package jcrypter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CMainMenu extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	private JMenu jmnuJCrypter, jmnuHelp;
	private JMenuItem jmniExit, jmniAbout;
	
	protected CMainMenu() {
		this.initComponents();
		this.assignActionListeners();
	}
	
	private void assignActionListeners() {
		jmniExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
		
		jmniAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new CAboutDialog(null, true).setVisible(true);
			}
		});
	}
	
	private void initComponents() {
		Font fntInterfaceFont = new Font(CConstants.DEF_INTERFACE_FONT, 0, 14);
		
		jmnuJCrypter = new JMenu(CConstants.MNU_JCrypter_CAPTION);
		jmnuJCrypter.setMnemonic(CConstants.MNU_JCRYPTER_MNEMONIC);
		jmnuJCrypter.setFont(fntInterfaceFont);
		
		jmniExit = new JMenuItem(CConstants.MNU_JCrypter_EXIT_CAPTION);
		jmniExit.setAccelerator(KeyStroke.getKeyStroke(CConstants.MNU_JCRYPTER_EXIT_KEYSTROKE));
		jmniExit.setMnemonic(CConstants.MNU_JCRYPTER_EXIT_MNEMONIC);
		jmniExit.setFont(fntInterfaceFont);
		jmnuJCrypter.add(jmniExit);
		this.add(jmnuJCrypter);
		
		jmnuHelp = new JMenu(CConstants.MNU_HELP_CAPTION);
		jmnuHelp.setMnemonic(CConstants.MNU_HELP_MNEMONIC);
		jmnuHelp.setFont(fntInterfaceFont);

		jmniAbout = new JMenuItem(CConstants.MNU_HELP_ABOUT_CAPTION);
		jmniAbout.setAccelerator(KeyStroke.getKeyStroke(CConstants.MNU_HELP_ABOUT_KEYSTROKE));
		jmniAbout.setMnemonic(CConstants.MNU_HELP_ABOUT_MNEMONIC);
		jmniAbout.setFont(fntInterfaceFont);
		jmnuHelp.add(jmniAbout);
		this.add(jmnuHelp);
	}
	
	private class CAboutDialog extends JDialog {
		private static final long serialVersionUID = 1L;
		
		private JButton jbtnOk;
		private JLabel jlblInformation;
		
		protected CAboutDialog(JFrame jfrmParentFrame, boolean blnModal) {
			super(jfrmParentFrame, blnModal);
			this.initComponents();
			
			jbtnOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					dispose();
				}
			});
		}
		
		private void initComponents() {
			this.setLocation(300, 300);
			this.setResizable(false);
			this.setSize(320, 310);
			this.setTitle(CConstants.DLG_ABOUT_TITLE);
			this.getContentPane().setLayout(null);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			
			Font fntInterfaceFont = new Font(CConstants.DEF_INTERFACE_FONT, 0, 13);
			
			jlblInformation = new JLabel(CConstants.DLG_TXT_INFORMATION_CONTENT);
			jlblInformation.setBounds(new Rectangle(10, 10, 300, 210));
			jlblInformation.setFont(fntInterfaceFont);
			this.getContentPane().add(jlblInformation);
			
			jbtnOk = new JButton(CConstants.DLG_BTN_OK_CAPTION);
			jbtnOk.setBounds(new Rectangle(135, 240, 50, 30));
			jbtnOk.setFont(fntInterfaceFont);
			jbtnOk.setMnemonic(CConstants.DLG_BTN_OK_MNEMONIC);
			this.getContentPane().add(jbtnOk);
			this.getRootPane().setDefaultButton(jbtnOk);
		}
	}
}
