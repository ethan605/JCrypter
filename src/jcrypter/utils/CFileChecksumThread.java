package jcrypter.utils;

import java.io.*;
import java.security.*;
import java.util.zip.*;
import javax.swing.*;
import jcrypter.*;

public class CFileChecksumThread implements Runnable {
	
	private boolean upperCase;
	private JProgressBar progressBar;
	private JTextArea checksumResult;
	private String inputFilePath, checksumAlgorithm;
	
	public CFileChecksumThread(String _inputFilePath, String _checksumAlgorithm, JTextArea _checksumResult, boolean _upperCase, JProgressBar _progressBar) {
		this.inputFilePath = _inputFilePath;
		this.checksumAlgorithm = _checksumAlgorithm;
		this.checksumResult = _checksumResult;
		this.upperCase = _upperCase;
		this.progressBar = _progressBar;
	}
	
	public void run() {
		if (CCrypter.getMessageDigestAlgorithmCode(this.checksumAlgorithm) == CConstants.MESSAGE_DIGEST_ALGORITHMS.length)
			this.builtInChecksumCalculation();
		else
			this.messageDigestCalculation();
	}
	
	private void builtInChecksumCalculation() {
		try {
			Checksum checksum = (this.checksumAlgorithm.equals("CRC32")) ? new CRC32() : new Adler32();
			FileInputStream inputFile = new FileInputStream(this.inputFilePath);
			byte[] buffer = new byte[CConstants.DEFAULT_CHECKSUM_BYTE];
			
			int byteRead = 0, percentage = 0;
			long totalByteRead = 0, inputFileSize = new File(this.inputFilePath).length();
			
			this.progressBar.setStringPainted(true);
			do {
				byteRead = inputFile.read(buffer);
				if (byteRead == -1)
					break;
				checksum.update(buffer, 0, byteRead);
				
				totalByteRead += byteRead;
				percentage = (int)((double)(totalByteRead)*100/inputFileSize);
				this.progressBar.setValue(percentage);
				this.progressBar.setString("Calculating file checksum using " +
											this.checksumAlgorithm + "... (" +
											percentage + "%)");
			}
			while (byteRead != -1);
			
			this.progressBar.setString("Calculating file checksum using " +
										this.checksumAlgorithm + " done");
			this.progressBar.setValue(100);
			
			if (this.upperCase)
				this.checksumResult.setText(Long.toHexString(checksum.getValue()).toUpperCase());
			else
				this.checksumResult.setText(Long.toHexString(checksum.getValue()).toLowerCase());
			
			inputFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void messageDigestCalculation() {
		try {
			FileInputStream inputFile = new FileInputStream(this.inputFilePath);
			MessageDigest messageDigest = MessageDigest.getInstance(this.checksumAlgorithm);
			
			byte[] buffer = new byte[CConstants.DEFAULT_CHECKSUM_BYTE];
			
			int byteRead = 0, percentage = 0;
			long totalByteRead = 0, inputFileSize = new File(this.inputFilePath).length();
			
			this.progressBar.setStringPainted(true);
			do {
				byteRead = inputFile.read(buffer);
				if (byteRead == -1)
					break;
				messageDigest.update(buffer, 0, byteRead);
				
				totalByteRead += byteRead;
				percentage = (int)((double)(totalByteRead)*100/inputFileSize);
				this.progressBar.setValue(percentage);
				this.progressBar.setString("Calculating file checksum using " +
											this.checksumAlgorithm + "... (" +
											percentage + "%)");
			}
			while (byteRead != -1);
			
			this.progressBar.setString("Calculating file checksum using " +
										this.checksumAlgorithm + " done");
			this.progressBar.setValue(100);
			
			inputFile.close();
						
			if (this.upperCase)
				this.checksumResult.setText(CCrypter.getMessageDigestString(messageDigest.digest()).toUpperCase());
			else
				this.checksumResult.setText(CCrypter.getMessageDigestString(messageDigest.digest()).toLowerCase());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
