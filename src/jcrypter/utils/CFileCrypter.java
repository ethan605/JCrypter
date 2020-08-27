package jcrypter.utils;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import javax.swing.*;
import java.security.*;
import jcrypter.*;

public class CFileCrypter {
	public static byte[] getFileChecksum(String filePath, int offset) {
		MessageDigest messageDigest = null; 
		try {
			FileInputStream inputFile = new FileInputStream(filePath);
			messageDigest = MessageDigest.getInstance(CConstants.DEF_FILE_CHECKSUM_ALGORITHM);
			
			byte[] buffer;
			if (offset > 0) {
				buffer = new byte[offset];
				inputFile.read(buffer);
			}
			buffer = new byte[CConstants.DEFAULT_CHECKSUM_BYTE];
			int byteRead;
			do {
				byteRead = inputFile.read(buffer);
				if (byteRead == -1)
					break;
				messageDigest.update(buffer, 0, byteRead);
			} while (byteRead != -1);
			
			inputFile.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return messageDigest.digest();
	}
	
	public static int checkFile(String filePath) {
		try {
			FileChannel fileChannel = new RandomAccessFile(filePath, "r").getChannel();
			ByteBuffer infoBuffer = ByteBuffer.allocate(CConstants.DEF_FILE_CHECKSUM_LENGTH);
			infoBuffer.clear();
			fileChannel.read(infoBuffer);
			infoBuffer.flip();
			byte[] fileChecksum;
			fileChecksum = CFileCrypter.getFileChecksum(filePath, CConstants.DEF_FILE_CHECKSUM_LENGTH);
			for (int i = 0; i < CConstants.DEF_FILE_CHECKSUM_LENGTH; i++)
				if (infoBuffer.array()[i] != fileChecksum[i]) {
					fileChannel.close();
					return CConstants.FILE_CORRUPTED;
				}
			
			fileChannel.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return CConstants.FILE_HEALTHY;
	}
	
	public static String checkExistedFile(String filePath) { 
		File file = new File(filePath);
		String newFilePath = file.getName(),
				folderPath = file.getParent();
		
		if (folderPath.charAt(folderPath.length()-1) == File.separatorChar)
			folderPath = folderPath.substring(0, folderPath.length()-1);
		
		if (file.exists()) {
			if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(null, file.getName() + " existed\nDo you want to overwrite it?", "File existed", JOptionPane.YES_NO_OPTION))
				do
					newFilePath = JOptionPane.showInputDialog(null, file.getName() + " existed!\nPlease input a new name for your encrypted file!", "File existed", JOptionPane.INFORMATION_MESSAGE);
				while (newFilePath == null || new File(folderPath + File.separator + newFilePath).exists());
			else
				file.delete();
		}
		
		return (folderPath + File.separator + newFilePath);
	}
	
	public static boolean isPasswordProtected(String filePath) {
		int passwordProtection = 0;
		try {
			FileChannel fileChannel = new RandomAccessFile(filePath, "r").getChannel();
			fileChannel.position(CConstants.DEF_FILE_CHECKSUM_LENGTH + 2);
			ByteBuffer infoBuffer = ByteBuffer.allocate(1);
			infoBuffer.clear();
			fileChannel.read(infoBuffer);
			infoBuffer.flip();
			passwordProtection = new Byte(infoBuffer.array()[0]).intValue();
			fileChannel.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (passwordProtection == 1);
	}
	
	public static String getDecryptedFileName(String filePath) {
		String decryptedFileName = "";
		try {
			FileChannel fileChannel = new RandomAccessFile(filePath, "r").getChannel();
			fileChannel.position(CConstants.DEF_FILE_CHECKSUM_LENGTH + 3);
			
			ByteBuffer infoBuffer = ByteBuffer.allocate(2);
			infoBuffer.clear();
			fileChannel.read(infoBuffer);
			infoBuffer.flip();
			int messageDigestLength = new Byte(infoBuffer.array()[0]).intValue(),
				decryptedFileNameLength = new Byte(infoBuffer.array()[1]).intValue();
			
			fileChannel.position(fileChannel.position() + messageDigestLength);
			infoBuffer = ByteBuffer.allocate(decryptedFileNameLength);
			infoBuffer.clear();
			fileChannel.read(infoBuffer);
			infoBuffer.flip();
			decryptedFileName = new String(infoBuffer.array());
						
			fileChannel.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return decryptedFileName;
	}
}