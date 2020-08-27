package jcrypter;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import jcrypter.utils.*;

public class CTextChecksumPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JButton jbtnClear;
	private JCheckBox jckbUpperCaseDisplay;
	private JComboBox jcbxChecksumAlgorithm;
	private JLabel jlblInputText, jlblChecksumResult, jlblChecksumAlgorithm;
	private JScrollPane jscpInputText;
	private JTextArea jtxtInputText, jtxtChecksumResult;
	
	protected CTextChecksumPanel() {
		this.initComponents();
		this.assignActionListeners();
	}
	
	private void assignActionListeners() {
		jtxtInputText.getDocument().addDocumentListener(new jtxtInputDocumentAction());
		jtxtInputText.addFocusListener(new jtxtChecksumFocusAction());
		jtxtChecksumResult.addFocusListener(new jtxtChecksumFocusAction());
		jcbxChecksumAlgorithm.addItemListener(new jcbxChecksumItemAction());
		jckbUpperCaseDisplay.addActionListener(new jckbUpperCaseAction());
		
		jbtnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				jtxtInputText.setText("");
				jtxtChecksumResult.setText("");
			}
		});
	}
	
	private void initComponents() {
		Font fntInterfaceFont = new Font(CConstants.DEF_INTERFACE_FONT, 0, 13),
			fntTextCodeFont = new Font(CConstants.DEF_TEXT_CODE_FONT, 0, 13);

		this.setBackground(new Color(245, 245, 245));
		this.setLayout(null);
		this.setPreferredSize(new Dimension(500, 320));
		
		jtxtInputText = new JTextArea();
		jtxtInputText.setFont(fntTextCodeFont);
		Set<AWTKeyStroke> set = new HashSet<AWTKeyStroke>(jtxtInputText.
				getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		set.add(KeyStroke.getKeyStroke("TAB"));
		jtxtInputText.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, set);
		
		jscpInputText = new JScrollPane(jtxtInputText);
		jscpInputText.setBounds(new Rectangle(90, 10, 390, 180));
		this.add(jscpInputText);
		
		jlblInputText = new JLabel(CConstants.TCS_LBL_INPUT_TEXT_CAPTION);
		jlblInputText.setBounds(new Rectangle(20, 10, 60, 180));
		jlblInputText.setDisplayedMnemonic(CConstants.TCS_LBL_INPUT_TEXT_MNEMONIC);
		jlblInputText.setFont(fntInterfaceFont);
		jlblInputText.setLabelFor(jtxtInputText);
		this.add(jlblInputText);
		
		jtxtChecksumResult = new JTextArea();
		jtxtChecksumResult.setBounds(new Rectangle(90, 200, 390, 60));
		jtxtChecksumResult.setEditable(false);
		jtxtChecksumResult.setFont(fntTextCodeFont);
		jtxtChecksumResult.setLineWrap(true);
		this.add(jtxtChecksumResult);
		
		jlblChecksumResult = new JLabel(CConstants.TCS_LBL_CHECKSUM_RESULT_CAPTION);
		jlblChecksumResult.setBounds(new Rectangle(20, 200, 60, 60));
		jlblChecksumResult.setDisplayedMnemonic(CConstants.TCS_LBL_CHECKSUM_RESULT_MNEMONIC);
		jlblChecksumResult.setFont(fntInterfaceFont);
		jlblChecksumResult.setLabelFor(jtxtChecksumResult);
		this.add(jlblChecksumResult);
		
		jcbxChecksumAlgorithm = new JComboBox(CConstants.CHECKSUM_ALGORITHMS);
		jcbxChecksumAlgorithm.setBounds(new Rectangle(150, 270, 90, 30));
		jcbxChecksumAlgorithm.setFont(fntInterfaceFont);
		this.add(jcbxChecksumAlgorithm);
		
		jlblChecksumAlgorithm = new JLabel(CConstants.TCS_LBL_CHECKSUM_ALGORITHM_CAPTION);
		jlblChecksumAlgorithm.setBounds(new Rectangle(30, 270, 110, 30));
		jlblChecksumAlgorithm.setDisplayedMnemonic(CConstants.TCS_LBL_CHECKSUM_ALGORITHM_MNEMONIC);
		jlblChecksumAlgorithm.setFont(fntInterfaceFont);
		jlblChecksumAlgorithm.setLabelFor(jcbxChecksumAlgorithm);
		this.add(jlblChecksumAlgorithm);
		
		jckbUpperCaseDisplay = new JCheckBox(CConstants.TCS_CKB_UPPER_CASE_DISPLAY_CAPTION);
		jckbUpperCaseDisplay.setBounds(new Rectangle(270, 270, 120, 30));
		jckbUpperCaseDisplay.setMnemonic(CConstants.TCS_CKB_UPPER_CASE_DISPLAY_MNEMONIC);
		jckbUpperCaseDisplay.setFont(fntInterfaceFont);
		this.add(jckbUpperCaseDisplay);
		
		jbtnClear = new JButton(CConstants.TCS_BTN_CLEAR_CAPTION);
		jbtnClear.setBounds(new Rectangle(410, 270, 60, 30));
		jbtnClear.setFont(fntInterfaceFont);
		jbtnClear.setMnemonic(CConstants.TCS_BTN_CLEAR_MNEMONIC);
		this.add(jbtnClear);
	}
	
	private void checksumInputText() {
		new CTextChecksumThread(jtxtInputText.getText(),
								(String)jcbxChecksumAlgorithm.getSelectedItem(),
								jtxtChecksumResult,
								jckbUpperCaseDisplay.isSelected()).checksum();
	}
	
	private class jckbUpperCaseAction implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if (jckbUpperCaseDisplay.isSelected())
				jtxtChecksumResult.setText(jtxtChecksumResult.getText().toUpperCase());
			else
				jtxtChecksumResult.setText(jtxtChecksumResult.getText().toLowerCase());
		}
	}
	
	private class jtxtInputDocumentAction implements DocumentListener {
		public void changedUpdate(DocumentEvent arg0) {	
			checksumInputText();
		}
		public void insertUpdate(DocumentEvent arg0) {	
			checksumInputText();
		}
		public void removeUpdate(DocumentEvent arg0) {
			checksumInputText();
		}
	}
	
	private class jcbxChecksumItemAction implements ItemListener {
		public void itemStateChanged(ItemEvent ie) {
			checksumInputText();
		}
	}
	
	private class jtxtChecksumFocusAction implements FocusListener {
		public void focusGained(FocusEvent fe) {
			if (fe.getSource() == jtxtInputText) {
				jtxtInputText.selectAll();
				return;
			}
			if (jtxtChecksumResult.getText().equals(""))
				return;
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