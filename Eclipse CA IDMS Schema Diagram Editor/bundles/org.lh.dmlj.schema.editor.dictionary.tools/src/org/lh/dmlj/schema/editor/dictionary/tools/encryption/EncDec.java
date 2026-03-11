/**
 * Copyright (C) 2025  Luc Hermans
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

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;

public final class EncDec {
	private static final String AES = "AES";
	private static final int ENCODED_LENGTH_LENGTH = 2;
	
	public static byte[] encodeAndEncrypt(String aString) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		
		return encodeAndEncrypt(aString, Plugin.getDefault().getPersonalEncryptionKey(),
				Plugin.getDefault().getPersonalInitializationVector());
	}
	
	public static byte[] encodeAndEncrypt(String aString, String encryptionKey, String iv) throws NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		
		var cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
	    var key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), AES);
	    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
	    return cipher.doFinal(toEncodedByteArray(aString, 16));
	}
	
	public static String decryptAndDecode(byte[] cipherText) throws InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		return decryptAndDecode(cipherText, Plugin.getDefault().getPersonalEncryptionKey(),
				Plugin.getDefault().getPersonalInitializationVector());
	}
		 
	public static String decryptAndDecode(byte[] cipherText, String encryptionKey, String iv) throws InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		var cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
	    var key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), AES);
	    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
	    var decrypted = cipher.doFinal(cipherText);
	    int decodedLength =
	    	Integer.parseInt(new String(decrypted, 0, ENCODED_LENGTH_LENGTH, StandardCharsets.UTF_8));
	    return new String(decrypted, ENCODED_LENGTH_LENGTH, decodedLength, StandardCharsets.UTF_8);
	}
	
	private static byte[] toEncodedByteArray(String aString, int lengthShouldBeMultipleOf){
		var encodedString = new StringBuilder(String.valueOf(aString.length()));
		if (encodedString.length() > ENCODED_LENGTH_LENGTH) {
			throw new IllegalStateException("max. length exceeded");
		}
		while (encodedString.length() < ENCODED_LENGTH_LENGTH) {
			encodedString.insert(0, '0');
		}
		encodedString.append(aString);
		
		var b1 = encodedString.toString().getBytes(StandardCharsets.UTF_8);
		var i = b1.length % lengthShouldBeMultipleOf;
		var j = i == 0 ? b1.length : b1.length + (lengthShouldBeMultipleOf - i);
		var b2 = new byte[j];
		System.arraycopy(b1, 0, b2, 0, b1.length);
		return b2;
	}
	
	private EncDec() {
	}
	
}
