package jcrypter.utils;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.*;
import java.util.Base64;

import jcrypter.CConstants;

public class CTextCryptThread {
	private boolean encryptMode;
	private JTextArea jtxtOutput;
	private SecretKeySpec secretKey;
	private String cryptAlgorithm, keyWord, inputText;
	
	public CTextCryptThread(String _cryptAlgorithm, String _keyWord, String _inputText, JTextArea _jtxtOutput, boolean _encryptMode) {
		this.cryptAlgorithm = _cryptAlgorithm;
		this.keyWord = _keyWord;
		this.inputText = _inputText;
		this.jtxtOutput = _jtxtOutput;
		this.encryptMode = _encryptMode;
	}
	
	public void crypt() {
		getSecretKey();
		if (this.encryptMode)
			this.jtxtOutput.setText(this.encrypt(this.inputText));
		else
			this.jtxtOutput.setText(this.decrypt(this.inputText));
	}
	
	private void getSecretKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(this.cryptAlgorithm);
			keyGenerator.init(new SecureRandom());
			byte[] secretKeyBytes = new byte[keyGenerator.generateKey().getEncoded().length],
					keyWordBytes = this.keyWord.getBytes();
			int minLength = (secretKeyBytes.length < keyWordBytes.length) ?
							secretKeyBytes.length : keyWordBytes.length;

			for (int i = 0; i < minLength; i++)
				secretKeyBytes[i] = keyWordBytes[i];

			if (minLength == keyWordBytes.length) {
				for (int i = minLength; i < secretKeyBytes.length; i++)
					secretKeyBytes[i] = (byte) (CConstants.DEFAULT_BYTE_PADDED + i);
			}
			
			secretKey = new SecretKeySpec(secretKeyBytes, this.cryptAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private String encrypt(String clearData) {
    	byte[] encryptedDataBytes = null;
		try {
			Cipher cipher = Cipher.getInstance(this.cryptAlgorithm + "/" + CConstants.CRYPT_MODE + "/" + CConstants.CRYPT_PADDING);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			encryptedDataBytes = cipher.doFinal(clearData.getBytes());
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
 
    	return Base64.getEncoder().encodeToString(encryptedDataBytes);
    }
	
	private String decrypt(String encryptedData) {
    	String clearData = null;
		try {
			Cipher cipher = Cipher.getInstance(this.cryptAlgorithm + "/" + CConstants.CRYPT_MODE + "/" + CConstants.CRYPT_PADDING);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData), 
					clearDataBytes = cipher.doFinal(encryptedDataBytes);
			clearData = new String(clearDataBytes, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			JOptionPane.showMessageDialog(null, CConstants.TCT_CANNOT_DECRYPT_TEXT_ERROR_CONTENT,
											CConstants.TCT_CANNOT_DECRYPT_TEXT_ERROR_TITLE, JOptionPane.INFORMATION_MESSAGE);
		} catch (BadPaddingException e) {
			JOptionPane.showMessageDialog(null, CConstants.TCT_CANNOT_DECRYPT_TEXT_ERROR_CONTENT,
											CConstants.TCT_CANNOT_DECRYPT_TEXT_ERROR_TITLE, JOptionPane.INFORMATION_MESSAGE);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
 
    	return clearData;
    }
}
