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
package org.lh.dmlj.schema.editor.dictionary.tools;

import java.io.File;
import java.net.InetAddress;
import java.sql.Driver;
import java.sql.DriverManager;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.lh.dmlj.schema.editor.dictionary.tools.encryption.EncDec;
import org.lh.dmlj.schema.editor.dictionary.tools.preference.PreferenceConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class Plugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.lh.dmlj.schema.editor.dictionary.tools";

	// The shared instance
	private static Plugin plugin;
	
	private static final String IDMS_JDBC_DRIVER_CLASS = "ca.idms.jdbc.IdmsJdbcDriver";
	private static final String DRIVER_NOT_INSTALLED = "NOT INSTALLED";
	
	private String driverVersion = DRIVER_NOT_INSTALLED;
	
	private String driverBundleId ="N/A";
	private String driverBundleVersion ="N/A";
	private String driverBundleName ="N/A";
	private String driverBundleVendor ="N/A";
	
	private File dictionaryFolder;
	
	private String bootEncryptionKey = getBundle().getSymbolicName().substring(0, 16);
	private String bootInitializationVector = "AAAAAAAAAAAAAAAA";	
	
	private String personalEncryptionKey = bootEncryptionKey;
	private String personalInitializationVector = bootInitializationVector;

	static {
		try {
			Class.forName(IDMS_JDBC_DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
		}
	}

	private static String generateString(String input, int length) {
		StringBuilder p = new StringBuilder(input);		
		if (p.length() >= length) {
			p.setLength(length);
		} else {			
			while (p.length() < length) {
				p.append(input);
				if (p.length() >= length) {
					p.setLength(length);
					break;
				}
			}
		}
		return p.toString();
	}

	public Plugin() {
	}

	public static Plugin getDefault() {
		return plugin;
	}

	public File getDictionaryFolder() {
		return dictionaryFolder;
	}
	
	public String getDriverBundleId() {
		return driverBundleId;
	}
	
	public String getDriverBundleName() {
		return driverBundleName;
	}
	
	public String getDriverBundleVendor() {
		return driverBundleVendor;
	}
	
	public String getDriverBundleVersion() {
		return driverBundleVersion;
	}
	
	private void getDriverInformation() {
		try {
			Driver driver = DriverManager.getDriver("jdbc:idms://xyz/APPLDICT");
			driverVersion = driver.getMajorVersion() + "." + driver.getMinorVersion();
			Bundle bundle = FrameworkUtil.getBundle(driver.getClass());
			driverBundleId = bundle.getSymbolicName();
			driverBundleVersion = bundle.getVersion().toString();
			driverBundleName = bundle.getHeaders().get("Bundle-Name");
			driverBundleVendor = bundle.getHeaders().get("Bundle-Vendor");
		} catch (Throwable t) {
		}
	}

	public String getDriverVersion() {
		return driverVersion;
	}
	
	public String getPersonalEncryptionKey(){
		return personalEncryptionKey;
	}

	public String getPersonalInitializationVector() {
		return personalInitializationVector;
	}

	public boolean isDriverInstalled() {
		return !driverVersion.equals(DRIVER_NOT_INSTALLED);
	}

	private void prepareDictionaryFolder() {
		dictionaryFolder = new File(Plugin.getDefault().getStateLocation().toFile(), "dictionaries");
		if (!dictionaryFolder.exists()) {
			dictionaryFolder.mkdir();
		}
	}

	private void prepareEncryptionData() {
		try {
			IPreferenceStore preferenceStore = getPreferenceStore();
			if (!preferenceStore.contains(PreferenceConstants.PERSONAL_ENCRYPTION_KEY) ||
				!preferenceStore.contains(PreferenceConstants.PERSONAL_INITIALIZATION_VECTOR)) {
								
				// bundle started for the very first time: compute the personal encryption key, 
				// encode and encrypt it and store it in the preference store as a hexadecimal 
				// string
				String computerName = InetAddress.getLocalHost().getHostName();
				personalEncryptionKey = generateString(computerName, 16);
				byte[] encodedAndEncryptedPersonalEncryptionKey =
					EncDec.encodeAndEncrypt(personalEncryptionKey, bootEncryptionKey, 
							 	 			bootInitializationVector);
				String encodedAndEncryptedPersonalEncryptionKeyAsHex =
					DatatypeConverter.printHexBinary(encodedAndEncryptedPersonalEncryptionKey);
				preferenceStore.setValue(PreferenceConstants.PERSONAL_ENCRYPTION_KEY, 
						 				 encodedAndEncryptedPersonalEncryptionKeyAsHex);
				
				// next, compute the personal initialization vector, encode and encrypt it and store  
				// it in the preference store as a hexadecimal string
				String ipAddress = InetAddress.getLocalHost().getHostAddress();
				personalInitializationVector = generateString(ipAddress, 16);
				byte[] encodedAndEncryptedPersonalInitializationVector =
					EncDec.encodeAndEncrypt(personalInitializationVector, bootEncryptionKey, 
							 	 			bootInitializationVector);
				String encodedAndEncryptedPersonalInitializationVectorAsHex =
					DatatypeConverter.printHexBinary(encodedAndEncryptedPersonalInitializationVector);				
				preferenceStore.setValue(PreferenceConstants.PERSONAL_INITIALIZATION_VECTOR, 
										 encodedAndEncryptedPersonalInitializationVectorAsHex);
				
			} else {
				
				// the bundle has already been started in the past: get the personal encryption key 
				// from the preference store
				String encodedAndEncryptedPersonalEncryptionKeyAsHex =
					preferenceStore.getString(PreferenceConstants.PERSONAL_ENCRYPTION_KEY);
				byte[] encodedAndEncryptedPersonalEncryptionKey =
					DatatypeConverter.parseHexBinary(encodedAndEncryptedPersonalEncryptionKeyAsHex);
				personalEncryptionKey = 
					EncDec.decryptAndDecode(encodedAndEncryptedPersonalEncryptionKey, 
											bootEncryptionKey, bootInitializationVector);
				
				// next, get the personal init. vector from the preference store
				String encodedAndEncryptedPersonalInitializationVectorAsHex =
					preferenceStore.getString(PreferenceConstants.PERSONAL_INITIALIZATION_VECTOR);
				byte[] encodedAndEncryptedPersonalInitializationVector =
					DatatypeConverter.parseHexBinary(encodedAndEncryptedPersonalInitializationVectorAsHex);
				personalInitializationVector = 
					EncDec.decryptAndDecode(encodedAndEncryptedPersonalInitializationVector, 
											bootEncryptionKey, bootInitializationVector);
				
			}
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		prepareEncryptionData();			
		getDriverInformation();
		prepareDictionaryFolder();
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

}
