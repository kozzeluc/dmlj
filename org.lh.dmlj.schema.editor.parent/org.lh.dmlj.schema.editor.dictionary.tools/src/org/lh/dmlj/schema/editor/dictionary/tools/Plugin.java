package org.lh.dmlj.schema.editor.dictionary.tools;

import java.sql.Driver;
import java.sql.DriverManager;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Plugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.lh.dmlj.schema.editor.dictionary.tools"; //$NON-NLS-1$

	// The shared instance
	private static Plugin plugin;
	
	private static final String IDMS_JDBC_DRIVER_CLASS = "ca.idms.jdbc.IdmsJdbcDriver";
	private static final String DRIVER_NOT_INSTALLED = "NOT INSTALLED";
	
	private String driverVersion = DRIVER_NOT_INSTALLED;

	static {
		try {
			Class.forName(IDMS_JDBC_DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
		}
	}

	/**
	 * The constructor
	 */
	public Plugin() {
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Plugin getDefault() {
		return plugin;
	}

	public String getDriverVersion() {
		return driverVersion;
	}
	
	public boolean isDriverInstalled() {
		return !driverVersion.equals(DRIVER_NOT_INSTALLED);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		try {
			Driver driver = DriverManager.getDriver("jdbc:idms://xyz/APPLDICT");
			driverVersion = driver.getMajorVersion() + "." + driver.getMinorVersion();
		} catch (Throwable t) {
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

}
