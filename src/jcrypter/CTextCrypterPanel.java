package jcrypter;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import jcrypter.utils.*;

public class CTextCrypterPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JButton jbtnCrypt;
	private JComboBox jcbxCryptAlgorithm;
	private JLabel jlblKeyWord, jlblPlainText, jlblCryptedText, jlblCryptAlgorithm;
	private JScrollPane jscpPlainText, jscpCryptedText;
	private JTextArea jtxtPlainText, jtxtCryptedText;
	private JTextField jtxtKeyWord;

	protected CTextCrypterPanel() {
		this.initComponents();
		this.assignActionListeners();
	}
	
	private void assignActionListeners() {
		jtxtPlainText.addFocusListener(new jtxtTextFocusAction());
		jtxtCryptedText.addFocusListener(new jtxtTextFocusAction());
		
		jbtnCrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cryptText();
			}
		});
	}
	
	private void initComponents() {
		Font fntPreferedFont = new Font(CConstants.DEF_INTERFACE_FONT, 0, 13),
			fntTextCodeFont = new Font(CConstants.DEF_TEXT_CODE_FONT, 0, 13);

		this.setBackground(new Color(245, 245, 245));
		this.setLayout(null);
		this.setPreferredSize(new Dimension(500, 320));
		
		jtxtPlainText = new JTextArea();
		jtxtPlainText.setFont(fntTextCodeFont);
//		jtxtPlainText.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "none");
		Set<AWTKeyStroke> set = new HashSet<AWTKeyStroke>(jtxtPlainText.
				getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		set.add(KeyStroke.getKeyStroke("TAB"));
		jtxtPlainText.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, set);
		
		jscpPlainText = new JScrollPane(jtxtPlainText);
		jscpPlainText.setBounds(new Rectangle(80, 10, 400, 120));
		this.add(jscpPlainText);
		
		jlblPlainText = new JLabel(CConstants.TCR_LBL_PLAIN_TEXT_CAPTION);
		jlblPlainText.setBounds(new Rectangle(20, 10, 60, 120));
		jlblPlainText.setDisplayedMnemonic(CConstants.TCR_LBL_PLAIN_TEXT_MNEMONIC);
		jlblPlainText.setFont(fntPreferedFont);
		jlblPlainText.setLabelFor(jtxtPlainText);
		this.add(jlblPlainText);
		
		jtxtCryptedText = new JTextArea();
		jtxtCryptedText.setFont(fntTextCodeFont);
		jtxtCryptedText.setLineWrap(true);
		set = new HashSet<AWTKeyStroke>(jtxtCryptedText.
				getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
		set.add(KeyStroke.getKeyStroke("TAB"));
		jtxtCryptedText.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, set);
		this.add(jtxtCryptedText);
		
		jscpCryptedText = new JScrollPane(jtxtCryptedText);
		jscpCryptedText.setBounds(new Rectangle(80, 140, 400, 120));
		this.add(jscpCryptedText);
		
		jlblCryptedText = new JLabel(CConstants.TCR_LBL_CRYPTED_TEXT_CAPTION);
		jlblCryptedText.setBounds(new Rectangle(20, 140, 60, 120));
		jlblCryptedText.setDisplayedMnemonic(CConstants.TCR_LBL_CRYPTED_TEXT_MNEMONIC);
		jlblCryptedText.setFont(fntPreferedFont);
		jlblCryptedText.setLabelFor(jtxtCryptedText);
		this.add(jlblCryptedText);
		
		jtxtKeyWord = new JTextField();
		jtxtKeyWord.setBounds(new Rectangle(80, 275, 140, 30));
		jtxtKeyWord.setFont(fntTextCodeFont);
		this.add(jtxtKeyWord);
		
		jlblKeyWord = new JLabel(CConstants.TCR_LBL_KEY_WORD_CAPTION);
		jlblKeyWord.setBounds(new Rectangle(20, 275, 60, 30));
		jlblKeyWord.setDisplayedMnemonic(CConstants.TCR_LBL_KEY_WORD_MNEMONIC);
		jlblKeyWord.setFont(fntPreferedFont);
		jlblKeyWord.setLabelFor(jtxtKeyWord);
		this.add(jlblKeyWord);
		
		jbtnCrypt = new JButton(CConstants.TCR_BTN_CRYPT_CAPTION);
		jbtnCrypt.setBounds(new Rectangle(235, 275, 60, 30));
		jbtnCrypt.setFocusable(false);
		jbtnCrypt.setFont(fntPreferedFont);
		jbtnCrypt.setMnemonic(CConstants.TCR_BTN_CRYPT_MNEMONIC);
		this.add(jbtnCrypt);
		
		jcbxCryptAlgorithm = new JComboBox(CConstants.CRYPT_ALGORITHMS);
		jcbxCryptAlgorithm.setBounds(new Rectangle(400, 275, 80, 30));
		jcbxCryptAlgorithm.setFont(fntPreferedFont);
		this.add(jcbxCryptAlgorithm);
		
		jlblCryptAlgorithm = new JLabel(CConstants.TCR_LBL_CRYPT_ALGORITHM_CAPTION);
		jlblCryptAlgorithm.setBounds(new Rectangle(310, 275, 80, 30));
		jlblCryptAlgorithm.setDisplayedMnemonic(CConstants.TCR_LBL_CRYPT_ALGORITHM_MNEMONIC);
		jlblCryptAlgorithm.setFont(fntPreferedFont);
		jlblCryptAlgorithm.setLabelFor(jcbxCryptAlgorithm);
		this.add(jlblCryptAlgorithm);
	}
	
	private void cryptText() {
		if (!jtxtPlainText.getText().equals(""))
			new CTextCryptThread((String)jcbxCryptAlgorithm.getSelectedItem(),
									jtxtKeyWord.getText(),
									jtxtPlainText.getText(),
									jtxtCryptedText,
									true).crypt();
		else
			new CTextCryptThread((String)jcbxCryptAlgorithm.getSelectedItem(),
									jtxtKeyWord.getText(),
									jtxtCryptedText.getText(),
									jtxtPlainText,
									false).crypt();
	}
	
	private class jtxtTextFocusAction implements FocusListener {
		public void focusGained(FocusEvent fe) {
			JTextArea jtxtTextArea = (JTextArea)fe.getSource();
			jtxtTextArea.selectAll();
		}
		public void focusLost(FocusEvent fe) {
		}
	}
}
