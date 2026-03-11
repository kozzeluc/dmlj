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
package org.lh.dmlj.schema.editor.dictionary.tools;

import java.io.File;
import java.net.InetAddress;
import java.sql.DriverManager;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.lh.dmlj.schema.editor.dictionary.tools.encryption.EncDec;
import org.lh.dmlj.schema.editor.dictionary.tools.preference.IDefaultDictionaryPropertyProvider;
import org.lh.dmlj.schema.editor.dictionary.tools.preference.PreferenceConstants;
import org.lh.dmlj.schema.editor.log.LogProvidingPlugin;
import org.lh.dmlj.schema.editor.log.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class Plugin extends AbstractUIPlugin implements IDefaultDictionaryPropertyProvider, LogProvidingPlugin {
	public static final String PLUGIN_ID = "org.lh.dmlj.schema.editor.dictionary.tools";
	
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
	private static final String IDMS_JDBC_DRIVER_CLASS = "ca.idms.jdbc.IdmsJdbcDriver";
	private static final String DRIVER_NOT_INSTALLED = "NOT INSTALLED";	
	private static Plugin plugin;
	
	private String driverVersion = DRIVER_NOT_INSTALLED;
	private boolean driverInstalledInThisSession = false;
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
			// ignore exception
		}
	}

	private static String generateString(String input, int length) {
		var p = new StringBuilder(input);		
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

	public static Plugin getDefault() {
		return plugin;
	}

	@Override
	public int getDefaultQueryRowidListSizeMaximum() {
		return getPreferenceStore().getInt(PreferenceConstants.DEFAULT_QUERY_ROWID_LIST_SIZE_MAXIMUM);
	}

	@Override
	public String getDefaultSchema() {
		return getPreferenceStore().getString(PreferenceConstants.DEFAULT_SCHEMA);
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
			var driver = DriverManager.getDriver("jdbc:idms://xyz/APPLDICT");
			driverVersion = driver.getMajorVersion() + "." + driver.getMinorVersion();
			var bundle = FrameworkUtil.getBundle(driver.getClass());
			driverBundleId = bundle.getSymbolicName();
			driverBundleVersion = bundle.getVersion().toString();
			driverBundleName = bundle.getHeaders().get("Bundle-Name");
			driverBundleVendor = bundle.getHeaders().get("Bundle-Vendor");
		} catch (Exception e) {
			logger.error("IDMS JDBC Driver could not be loaded", e);
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
	
	public boolean isDriverInstalledInThisSession() {
		return driverInstalledInThisSession;
	}

	private void prepareDictionaryFolder() {
		dictionaryFolder = new File(Plugin.getDefault().getStateLocation().toFile(), "dictionaries");
		if (!dictionaryFolder.exists()) {
			dictionaryFolder.mkdir();
		}
	}

	private void prepareEncryptionData() {
		try {
			var preferenceStore = getPreferenceStore();
			if (!preferenceStore.contains(PreferenceConstants.PERSONAL_ENCRYPTION_KEY) ||
				!preferenceStore.contains(PreferenceConstants.PERSONAL_INITIALIZATION_VECTOR)) {
								
				// bundle started for the very first time: compute the personal encryption key, encode and encrypt
				// it and store it in the preference store as a hexadecimal string
				var computerName = InetAddress.getLocalHost().getHostName();
				personalEncryptionKey = generateString(computerName, 16);
				var encodedAndEncryptedPersonalEncryptionKey = EncDec.encodeAndEncrypt(personalEncryptionKey,
						bootEncryptionKey, bootInitializationVector);
				var encodedAndEncryptedPersonalEncryptionKeyAsHex = DatatypeConverter.printHexBinary(encodedAndEncryptedPersonalEncryptionKey);
				preferenceStore.setValue(PreferenceConstants.PERSONAL_ENCRYPTION_KEY, encodedAndEncryptedPersonalEncryptionKeyAsHex);
				
				// next, compute the personal initialization vector, encode and encrypt it and store it in the
				// preference store as a hexadecimal string
				var ipAddress = InetAddress.getLocalHost().getHostAddress();
				personalInitializationVector = generateString(ipAddress, 16);
				var encodedAndEncryptedPersonalInitializationVector = EncDec.encodeAndEncrypt(personalInitializationVector,
						bootEncryptionKey, bootInitializationVector);
				var encodedAndEncryptedPersonalInitializationVectorAsHex = DatatypeConverter.printHexBinary(encodedAndEncryptedPersonalInitializationVector);
				preferenceStore.setValue(PreferenceConstants.PERSONAL_INITIALIZATION_VECTOR, encodedAndEncryptedPersonalInitializationVectorAsHex);
			} else {
				// the bundle has already been started in the past: get the personal encryption key from the preference store
				var encodedAndEncryptedPersonalEncryptionKeyAsHex = preferenceStore.getString(PreferenceConstants.PERSONAL_ENCRYPTION_KEY);
				var encodedAndEncryptedPersonalEncryptionKey = DatatypeConverter.parseHexBinary(encodedAndEncryptedPersonalEncryptionKeyAsHex);
				personalEncryptionKey = EncDec.decryptAndDecode(encodedAndEncryptedPersonalEncryptionKey,
						bootEncryptionKey, bootInitializationVector);
				
				// next, get the personal init. vector from the preference store
				var encodedAndEncryptedPersonalInitializationVectorAsHex = preferenceStore.getString(PreferenceConstants.PERSONAL_INITIALIZATION_VECTOR);
				var encodedAndEncryptedPersonalInitializationVector = DatatypeConverter.parseHexBinary(encodedAndEncryptedPersonalInitializationVectorAsHex);
				personalInitializationVector = EncDec.decryptAndDecode(encodedAndEncryptedPersonalInitializationVector,
						bootEncryptionKey, bootInitializationVector);
			}
		} catch (Throwable t) {
			throw new IllegalStateException(t);
		}
	}
	
	public void setDriverInstalledInThisSession(boolean driverInstalledInThisSession) {
		this.driverInstalledInThisSession = driverInstalledInThisSession;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		prepareEncryptionData();			
		getDriverInformation();
		prepareDictionaryFolder();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

}
