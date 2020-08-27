package jcrypter.utils;

import java.io.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

import jcrypter.*;

public class CCrypter {
	private static final String CRYPT_MODE = "ECB";
	private static final String CRYPT_PADDING = "PKCS5Padding";
	
	private String cryptAlgorithm, messageDigestAlgorithm;
	private SecretKeySpec secretKey;
	
	public CCrypter() {
	}
	
	public CCrypter(String _cryptAlgorithm, String _messageDigestAlgorithm, String _authenticationPassword) {
		this.cryptAlgorithm = _cryptAlgorithm;
		this.messageDigestAlgorithm = _messageDigestAlgorithm;
    	this.getSecretKey(_authenticationPassword);
    }
	
	public static byte[] getMessageDigest(String messageDigestAlgorithm, String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(messageDigestAlgorithm);
			md.update(password.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md.digest();
	}

    public byte[] getSecretKey(String password) {
    	MessageDigest md = null;
    	byte[] secretKeyBytes = null, bytesFromDigest = null;
		
    	try {
			md = MessageDigest.getInstance(this.messageDigestAlgorithm);
			md.update(password.getBytes("UTF-8"));
			KeyGenerator keyGenerator = KeyGenerator.getInstance(this.cryptAlgorithm);
			keyGenerator.init(new SecureRandom());
			secretKeyBytes = new byte[keyGenerator.generateKey().getEncoded().length];
			bytesFromDigest = md.digest();
			int minLength = (secretKeyBytes.length < bytesFromDigest.length) ?
							secretKeyBytes.length : bytesFromDigest.length;
		
			for (int i = 0; i < minLength; i++)
				secretKeyBytes[i] = bytesFromDigest[i];
		
			if (minLength == bytesFromDigest.length) {
				for (int i = minLength; i < secretKeyBytes.length; i++)
					secretKeyBytes[i] = (byte) (CConstants.DEFAULT_BYTE_PADDED + i);
			}
		
			secretKey = new SecretKeySpec(secretKeyBytes, this.cryptAlgorithm);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return bytesFromDigest;
    }

    public byte[] encrypt(byte[] clearData) {
    	byte[] encryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance(this.cryptAlgorithm + "/" + CRYPT_MODE + "/" + CRYPT_PADDING);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			encryptedData = cipher.doFinal(clearData);
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
 
    	return encryptedData;
    }

    public byte[] decrypt(byte[] encryptedData) {
 
    	byte[] clearData = null;
		
		try {
			Cipher cipher = Cipher.getInstance(this.cryptAlgorithm + "/" + CRYPT_MODE + "/" + CRYPT_PADDING);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			clearData = cipher.doFinal(encryptedData);
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

		return clearData;
    }
    
    public static String getMessageDigestString(byte[] checksum) {
		String checksumString = new String("");
		for (int i = 0; i < checksum.length; i++)
			checksumString += Integer.toHexString((0x000000ff & checksum[i]) | 0xffffff00).substring(6);
		return checksumString;
	}
    
    public static int getCryptAlgorithmCode(String _cryptAlgorithm) {
		for (int i = 0; i < CConstants.CRYPT_ALGORITHMS.length; i++)
			if (CConstants.CRYPT_ALGORITHMS[i].equals(_cryptAlgorithm))
				return i;
		return CConstants.CRYPT_ALGORITHMS.length;
	}
	
	public static int getMessageDigestAlgorithmCode(String _messageDigestAlgorithm) {
		for (int i = 0; i < CConstants.MESSAGE_DIGEST_ALGORITHMS.length; i++)
			if (CConstants.MESSAGE_DIGEST_ALGORITHMS[i].equals(_messageDigestAlgorithm))
				return i;
		return CConstants.MESSAGE_DIGEST_ALGORITHMS.length;
	}
}