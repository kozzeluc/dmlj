package org.lh.dmlj.schema.editor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Plugin extends AbstractUIPlugin implements IPropertyChangeListener {

	// The plug-in ID
	public static final String 		PLUGIN_ID = 
		"org.lh.dmlj.schema.editor"; //$NON-NLS-1$	
	
	private final static String 	PLUGIN_PROPERTIES = "plugin.properties";

	// The shared instance
	private static Plugin 			plugin;
			
	private Font 					figureFont = 
		new Font(Display.getCurrent(), "Arial", 6, SWT.NORMAL);
	private Map<String, Image> 	 	images = new HashMap<String, Image>();
	private boolean 				logDebugMessages;
	private PropertyResourceBundle  pluginProperties;
	private Font 					syntaxFont = 
		new Font(Display.getCurrent(), "Courier New", 10, SWT.NORMAL);
	private File 					tmpFolder;
	
	private static IStatus createStatus(int severity, int code, String message, 
										Throwable exception) {
		
		return new Status(severity, PLUGIN_ID, code, message, exception);
	}

	private static void deleteDirectoryContents(File directory) {
		for (String fileName : directory.list()) {
			File file = new File(directory, fileName);
			if (file.isDirectory()) {
				deleteDirectoryContents(file);
			}
			file.delete();			
		}		
	}	
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Plugin getDefault() {
		return plugin;
	}

	private static void log(int severity, int code, String message, Throwable exception) {
		log(createStatus(severity, code, message, exception));
	}

	private static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	public static void logDebug(String message) {
		if (getDefault().isLogDebugMessages()) {
			log(IStatus.INFO, IStatus.OK, message, null);
		}
	}

	public static void logError(String message, Throwable exception) {
		log(IStatus.ERROR, IStatus.OK, message, exception);
	}

	public static void logInfo(String message) {
		log(IStatus.INFO, IStatus.OK, message, null);
	}

	public static void logWarning(String message) {
		log(IStatus.WARNING, IStatus.OK, message, null);
	}

	/**
	 * The constructor
	 */
	public Plugin() {
	}

	public File createTmpFile(String fileExtension) {		
		File file = 
			new File(tmpFolder, String.valueOf(new Date().getTime()) + "." + 
						fileExtension);
		while (file.exists()) {
			file = 
				new File(tmpFolder, String.valueOf(new Date().getTime()) + "." + 
						 fileExtension);
		}
		return file;
	}
	
	public File createTmpFolder() {		
		File folder = 
			new File(tmpFolder, String.valueOf(new Date().getTime()));
		while (folder.exists()) {
			folder = new File(tmpFolder, String.valueOf(new Date().getTime()));
		}
		if (!folder.mkdir()) {
			throw new RuntimeException("cannot create temporary files folder");
		}
		return folder;
	}	

	public Font getFigureFont() {
		return figureFont;
	}
	
	public Image getImage(String path) {
		if (images.containsKey(path)) {
			return images.get(path);
		}
		Image image = null;
		URL baseURL = getBundle().getEntry("/");
		try {
			URL url = new URL(baseURL, path);
			image = ImageDescriptor.createFromURL(url).createImage();
		} catch (MalformedURLException e) {			
		}
		if (image != null) {
			images.put(path, image);
		}
		return image;
	}	
	
	public PropertyResourceBundle getPluginProperties() {
		return pluginProperties;
	}

	public Font getSyntaxFont() {
		return syntaxFont;
	}

	public File getTemporaryFilesFolder() {
		return tmpFolder;
	}

	private boolean isLogDebugMessages() {
		return logDebugMessages;
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES)) {
			logDebugMessages = ((Boolean) event.getNewValue()).booleanValue();
		}
	}	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;		
				
		try {
			InputStream in = 
				FileLocator.openStream(this.getBundle(), 
									   new Path(PLUGIN_PROPERTIES), false);
			pluginProperties = new PropertyResourceBundle(in);
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		// Locate the temporary files folder in the workspace.  We should 
		// probably put our temporary files somewhere else, since the folder 
		// returned by the call to the plug-in's getStateLocation() method 
		// "is recommended for plug-in preference settings and other 
		// configuration parameters".  That location is perfectly usable though.
		tmpFolder = new File(getStateLocation().toFile(), "tmp");
		if (tmpFolder.exists()) {
			deleteDirectoryContents(tmpFolder);
			tmpFolder.delete();	
		}	
		if (!tmpFolder.exists()) {
			tmpFolder.mkdir();
		}
		
		logDebugMessages = 
			getPreferenceStore().getBoolean(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES);
		getPreferenceStore().addPropertyChangeListener(this);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {		
		
		getPreferenceStore().removePropertyChangeListener(this);
		
		// dispose images
		for (Image image : images.values()) {
			image.dispose();
		}
		
		// cleanup our temporary file folder...
		deleteDirectoryContents(tmpFolder);
		tmpFolder.delete();		
		
		figureFont.dispose();
		syntaxFont.dispose();
		plugin = null;
		super.stop(context);
	}

}
