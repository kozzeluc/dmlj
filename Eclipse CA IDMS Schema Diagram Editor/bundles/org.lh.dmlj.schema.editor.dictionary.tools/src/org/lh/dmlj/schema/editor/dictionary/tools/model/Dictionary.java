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
package org.lh.dmlj.schema.editor.dictionary.tools.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.dictionary.tools.encryption.EncDec;
import org.lh.dmlj.schema.editor.dictionary.tools.preference.IDefaultDictionaryPropertyProvider;

public class Dictionary implements Comparable<Dictionary> {	
	public static final String USE_DEFAULT_SCHEMA_INDICATOR = "%default%";
	public static final int USE_DEFAULT_QUERY_ROWID_LIST_SIZE_MAXIMUM_INDICATOR = Integer.MIN_VALUE;
	
	private static final String KEY_INTERNAL_ID = "internalId";
	private static final String KEY_ID = "id";
	private static final String KEY_HOSTNAME = "hostname";
	private static final String KEY_PORT = "port";
	private static final String KEY_DICTNAME = "dictname";
	private static final String KEY_USER = "user";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_SCHEMA = "schema";
	private static final String KEY_QUERY_ROWID_LIST_SIZE_MAXIMUM = "queryDbkeyListSizeMaximum";
	private static final String KEY_SYSDIRL = "sysdirl";	
	
	private static final String DICTIONARY_PREFIX = "Dictionary_";
	private static final String PROPERTIES_SUFFIX = ".properties";
	private static final FilenameFilter FILENAME_FILTER = (dir, name) -> name.toLowerCase().startsWith("dictionary_") && name.toLowerCase().endsWith(PROPERTIES_SUFFIX);

	private final int internalId;
	private String id;
	private String hostname;
	private int port;
	private String dictname;
	private String user;
	private String password;
	private String schema;
	private int queryRowidListSizeMaximum;
	private boolean sysdirl;
	
	private static Dictionary fromFile(File file) throws IOException {
		var properties = new Properties();
		try (var in = new FileInputStream(file)) {
			properties.load(in);
		}
		
		var internalId = Integer.parseInt(properties.getProperty(KEY_INTERNAL_ID));
		var dictionary = new Dictionary(internalId);
		dictionary.setId(properties.getProperty(KEY_ID));
		dictionary.setHostname(properties.getProperty(KEY_HOSTNAME));
		var port = Integer.valueOf(properties.getProperty(KEY_PORT));
		dictionary.setPort(port);
		dictionary.setDictname(properties.getProperty(KEY_DICTNAME));
		dictionary.setUser(properties.getProperty(KEY_USER));
		if (properties.containsKey(KEY_PASSWORD)) {
			var encodedAndEncryptedAsHex = properties.getProperty(KEY_PASSWORD);
			try {				
				var password = EncDec.decryptAndDecode(DatatypeConverter.parseHexBinary(encodedAndEncryptedAsHex));
				dictionary.setPassword(password);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		if (properties.containsKey(KEY_SCHEMA)) {
			dictionary.setSchema(properties.getProperty(KEY_SCHEMA));
		} else {
			dictionary.setSchema(USE_DEFAULT_SCHEMA_INDICATOR);
		}
		if (properties.containsKey(KEY_QUERY_ROWID_LIST_SIZE_MAXIMUM)) {
			var queryRowidListSizeMaximum = Integer.parseInt(properties.getProperty(KEY_QUERY_ROWID_LIST_SIZE_MAXIMUM));
			dictionary.setQueryRowidListSizeMaximum(queryRowidListSizeMaximum);
		} else {
			dictionary.setQueryRowidListSizeMaximum(USE_DEFAULT_QUERY_ROWID_LIST_SIZE_MAXIMUM_INDICATOR);
		}
		if (properties.containsKey(KEY_SYSDIRL)) {
			dictionary.setSysdirl(Boolean.parseBoolean(properties.getProperty(KEY_SYSDIRL)));
		}
		return dictionary;
	}
	
	private static int getHighestInternalId(List<Dictionary> dictionaries) {
		return dictionaries.stream()
				.mapToInt(Dictionary::getInternalId)
				.max()
				.orElse(-1);
	}

	public static List<Dictionary> list(File folder) throws IOException {
		var dictionaries = new ArrayList<Dictionary>();
		for (var file : folder.listFiles(FILENAME_FILTER)) {
			dictionaries.add(fromFile(file));
		}
		Collections.sort(dictionaries);
		return dictionaries;
	}
	
	public static Dictionary newInstance(File folder) throws IOException {
		return new Dictionary(getHighestInternalId(list(folder)) + 1);		
	}
	
	public static Dictionary newTemporaryInstance() {
		return new Dictionary(-1);
	}
	
	public static Dictionary read(File folder, int internalId) throws IOException {
		var file = new File(folder, DICTIONARY_PREFIX + internalId + PROPERTIES_SUFFIX);
		return fromFile(file);
	}

	public Dictionary(int internalId) {
		this.internalId = internalId;
	}
	
	@Override
	public int compareTo(Dictionary dictionary) {
		if (id.equalsIgnoreCase(dictionary.id)) {
			return internalId - dictionary.internalId;
		} else {
			return id.toLowerCase().compareTo(dictionary.id.toLowerCase());
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof Dictionary)) {
			return false;
		}
		var otherDictionary = (Dictionary) other;
		return internalId == otherDictionary.internalId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(internalId);
	}

	public String getConnectionUrl() {
		return "jdbc:idms://" + hostname + ":" + port + "/" + dictname;
	}

	public String getDictname() {
		return dictname;
	}

	public String getHostname() {
		return hostname;
	}

	public String getId() {
		return id;
	}

	public int getInternalId() {
		return internalId;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}

	public int getQueryRowidListSizeMaximum() {
		return queryRowidListSizeMaximum;
	}
	
	public int getQueryRowidListSizeMaximumWithDefault(IDefaultDictionaryPropertyProvider defaultPropertyProvider) {
		if (isCustomQueryRowidListSizeMaximumSet()) {
			return queryRowidListSizeMaximum;
		} else {
			return defaultPropertyProvider.getDefaultQueryRowidListSizeMaximum();					
		}
	}
	
	public String getSchema() {
		return schema;
	}
	
	public String getSchemaWithDefault(IDefaultDictionaryPropertyProvider defaultPropertyProvider) {
		if (isCustomSchemaSet()) {		
			return schema;
		} else {
			return defaultPropertyProvider.getDefaultSchema();
		}
	}

	public String getUser() {
		return user;
	}

	private boolean isCustomQueryRowidListSizeMaximumSet() {
		return queryRowidListSizeMaximum != USE_DEFAULT_QUERY_ROWID_LIST_SIZE_MAXIMUM_INDICATOR;
	}

	private boolean isCustomSchemaSet() {
		return !schema.equals(USE_DEFAULT_SCHEMA_INDICATOR);
	}

	public boolean isSysdirl() {
		return sysdirl;
	}

	public boolean remove(File containingFolder) {
		var file = new File(containingFolder, DICTIONARY_PREFIX + internalId + PROPERTIES_SUFFIX);
		try {
			Files.delete(file.toPath());
			return true;
		} catch (IOException e) {
			Plugin.getDefault().getLog().error("deleting file %s threw an exception".formatted(file.getAbsolutePath()), e);
			return false;
		}
	}

	public void setDictname(String dictname) {
		this.dictname = dictname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setQueryRowidListSizeMaximum(int queryRowidListSizeMaximum) {
		this.queryRowidListSizeMaximum = queryRowidListSizeMaximum;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public void setSysdirl(boolean sysdirl) {
		this.sysdirl = sysdirl;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	private Properties toProperties() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		var properties = new Properties();
		properties.put(KEY_INTERNAL_ID, String.valueOf(internalId));
		properties.put(KEY_ID, id);
		properties.put(KEY_HOSTNAME, hostname);
		properties.put(KEY_PORT, String.valueOf(port));
		properties.put(KEY_DICTNAME, dictname);
		properties.put(KEY_USER, user);
		if (password != null) {
			var encodedAndEncryptedAsHex = DatatypeConverter.printHexBinary(EncDec.encodeAndEncrypt(password));
			properties.put(KEY_PASSWORD, encodedAndEncryptedAsHex);
		}
		if (isCustomSchemaSet()) {
			properties.put(KEY_SCHEMA, schema);
		} 
		if (isCustomQueryRowidListSizeMaximumSet()) {
			properties.put(KEY_QUERY_ROWID_LIST_SIZE_MAXIMUM, String.valueOf(queryRowidListSizeMaximum));
		}
		properties.put(KEY_SYSDIRL, String.valueOf(sysdirl));
		return properties;
	}
	
	public void toFile(File folder) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		
		if (internalId < 0) {
			throw new IllegalStateException("internalId is invalid: " + internalId);
		}
		var properties = toProperties();
		var file = new File(folder, DICTIONARY_PREFIX + internalId + PROPERTIES_SUFFIX);
		try (var out = new PrintWriter(new FileWriter(file))) {
			properties.store(out, "Dictionary Properties");
			out.flush();
		}
	}
	
}
