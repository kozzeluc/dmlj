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
package org.lh.dmlj.schema.editor;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressService;
import org.lh.dmlj.schema.editor.dictguide.DictguidesRegistry;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;
import org.lh.dmlj.schema.editor.service.ServicesPlugin;
import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Plugin extends AbstractUIPlugin implements IPropertyChangeListener {

	// The plug-in ID
	public static final String 		PLUGIN_ID = 
		"org.lh.dmlj.schema.editor.core"; //$NON-NLS-1$

	public static enum DebugItem {
		CALLING_METHOD
	}
	
	// The shared instance
	private static Plugin 			plugin;
			
	private Font 					figureFont = 
		new Font(Display.getCurrent(), "Arial", 6, SWT.NORMAL);
	private Font 					figureFontBold = 
		new Font(Display.getCurrent(), "Arial", 6, SWT.BOLD);
	private Font 					figureFontItalic = 
		new Font(Display.getCurrent(), "Arial", 6, SWT.ITALIC);
	private Map<String, Image> 	 	images = new HashMap<String, Image>();
	private boolean 				logDebugMessages;
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
	
	private static String getSimpleClassName(String className) {
		if (className.indexOf(".") > -1 && ! className.endsWith(".")) {
			return className.substring(className.lastIndexOf(".") + 1);
		} else {
			return className;
		}
	}

	private static void log(int severity, int code, String message, Throwable exception) {
		log(createStatus(severity, code, message, exception));
	}

	private static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	public static void logDebug(String message) {
		// the test 'getDefault() != null' is only there for testing, in order to avoid a NPE
		if (getDefault() != null && getDefault().isLogDebugMessages()) {
			log(IStatus.INFO, IStatus.OK, message, null);
		}
	}
	
	public static void logDebug(DebugItem debugItem) {
		
		if (getDefault() == null || !getDefault().isLogDebugMessages()) {
			// not in debug mode
			return;
		}
		
		if (debugItem == DebugItem.CALLING_METHOD) {		
			Thread currentThread = Thread.currentThread();
			StackTraceElement[] stackTraceElements = currentThread.getStackTrace();
			if (stackTraceElements.length > 3) {
				Plugin.logDebug("Method " + stackTraceElements[2].getMethodName() + 
								"(...) in class " +
								stackTraceElements[2].getClassName() + 
								"\n         was called from " +
								stackTraceElements[3].getClassName() + "." + 
								stackTraceElements[3].getMethodName() + "(" +
								getSimpleClassName(stackTraceElements[3].getClassName()) + 
								".java:" +
								stackTraceElements[3].getLineNumber() + ")" +
								"\n         (thread=" + currentThread.getName() + ")");
			}						
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

	public DateFormat getDateFormat() {
		String pattern = 
			getPreferenceStore().getString(PreferenceConstants.DIAGRAMLABEL_LAST_MODIFIED_DATE_FORMAT_PATTERN);
		return new SimpleDateFormat(pattern);
	}
	
	public Font getFigureFont() {
		return figureFont;
	}
	
	public Font getFigureFontBold() {
		return figureFontBold;
	}
	
	public Font getFigureFontItalic() {
		return figureFontItalic;
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

	public File getTemporaryFilesFolder() {
		return tmpFolder;
	}

	private boolean isLogDebugMessages() {
		return logDebugMessages;
	}
	
	public void runWithOperationInProgressIndicator(IRunnableWithProgress runnableWithProgress) {
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		try {
			progressService.runInUI(progressService, runnableWithProgress, null);
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
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
		
		// initialize the dictguide registry; this registry contains all imported dictionary guides
		DictguidesRegistry.init(getStateLocation().toFile(),				 
								ServicesPlugin.getDefault()
											  .getService(IPdfExtractorService.class));
		
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
		
		// cleanup any fonts we created
		figureFont.dispose();
		figureFontBold.dispose();
		figureFontItalic.dispose();
		
		plugin = null;
		super.stop(context);
	}	

}
