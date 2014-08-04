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

import java.sql.Driver;
import java.sql.DriverManager;

import org.eclipse.ui.plugin.AbstractUIPlugin;
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

	static {
		try {
			Class.forName(IDMS_JDBC_DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
		}
	}

	public Plugin() {
	}

	public static Plugin getDefault() {
		return plugin;
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
	
	public String getDriverVersion() {
		return driverVersion;
	}
	
	public boolean isDriverInstalled() {
		return !driverVersion.equals(DRIVER_NOT_INSTALLED);
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		getDriverInformation();
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

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

}
