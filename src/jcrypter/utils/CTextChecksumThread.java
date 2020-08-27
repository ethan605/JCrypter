package jcrypter.utils;

import java.security.*;
import java.util.zip.*;
import javax.swing.*;
import jcrypter.*;

public class CTextChecksumThread {
	private boolean upperCase;
	private JTextArea checksumResult;
	private String inputText, checksumAlgorithm;
	
	public CTextChecksumThread(String _inputText, String _checksumAlgorithm, JTextArea _checksumResult, boolean _upperCase) {
		this.inputText = _inputText;
		this.checksumAlgorithm = _checksumAlgorithm;
		this.checksumResult = _checksumResult;
		this.upperCase = _upperCase;
	}
	
	public void checksum() {
		if (CCrypter.getMessageDigestAlgorithmCode(this.checksumAlgorithm) == CConstants.MESSAGE_DIGEST_ALGORITHMS.length)
			this.checksumResult.setText(this.builtInChecksumCalculation());
		else
			this.checksumResult.setText(this.messageDigestCalculation());
	}
	
	private String builtInChecksumCalculation() {
		Checksum checksum = (this.checksumAlgorithm.equals("CRC32")) ? new CRC32() : new Adler32();
		byte[] inputTextBytes = this.inputText.getBytes();
		
		checksum.update(inputTextBytes, 0, inputTextBytes.length);
		
		if (this.upperCase)
			return Long.toHexString(checksum.getValue()).toUpperCase();
		return Long.toHexString(checksum.getValue());
	}
	
	private String messageDigestCalculation() {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(this.checksumAlgorithm);
			messageDigest.update(this.inputText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		if (this.upperCase)
			return CCrypter.getMessageDigestString(messageDigest.digest()).toUpperCase();
		return CCrypter.getMessageDigestString(messageDigest.digest());
	}
}
