/**
 * Copyright (C) 2014  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.dictionary.tools.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;

public abstract class EncDec {
	
	private static final int ENCODED_LENGTH_LENGTH = 2;
	
	public static byte[] encodeAndEncrypt(String aString) throws Throwable {
		return encodeAndEncrypt(aString, Plugin.getDefault().getPersonalEncryptionKey(), 
								Plugin.getDefault().getPersonalInitializationVector());
	}
	
	public static byte[] encodeAndEncrypt(String aString, String encryptionKey, String iv) 
		throws Throwable {
	    
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
	    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
	    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes("UTF-8")));
	    return cipher.doFinal(toEncodedByteArray(aString, 16));
	}
	
	public static String decryptAndDecode(byte[] cipherText) throws Throwable {
		return decryptAndDecode(cipherText, Plugin.getDefault().getPersonalEncryptionKey(), 
								Plugin.getDefault().getPersonalInitializationVector());
	}
		 
	public static String decryptAndDecode(byte[] cipherText, String encryptionKey, String iv) 
		throws Throwable {
	    
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
	    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
	    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes("UTF-8")));
	    byte[] decrypted = cipher.doFinal(cipherText);
	    int decodedLength =
	    	Integer.parseInt(new String(decrypted, 0, ENCODED_LENGTH_LENGTH, "UTF-8"));
	    return new String(decrypted, ENCODED_LENGTH_LENGTH, decodedLength, "UTF-8");
	}
	
	private static byte[] toEncodedByteArray(String aString, int lengthShouldBeMultipleOf) 
		throws Throwable {
		
		StringBuilder encodedString = new StringBuilder(String.valueOf(aString.length()));
		if (encodedString.length() > ENCODED_LENGTH_LENGTH) {
			throw new RuntimeException("max. length exceeded");
		}
		while (encodedString.length() < ENCODED_LENGTH_LENGTH) {
			encodedString.insert(0, '0');
		}
		encodedString.append(aString);
		
		byte[] b1 = encodedString.toString().getBytes("UTF-8");
		int i = b1.length % lengthShouldBeMultipleOf;
		int j = i == 0 ? b1.length : b1.length + (lengthShouldBeMultipleOf - i);
		byte[] b2 = new byte[j];
		System.arraycopy(b1, 0, b2, 0, b1.length);
		return b2;
			
	}
	
}
