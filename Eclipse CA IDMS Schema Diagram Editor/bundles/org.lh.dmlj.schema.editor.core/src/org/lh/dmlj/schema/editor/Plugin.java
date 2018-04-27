/**
 * Copyright (C) 2018  Luc Hermans
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

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressService;
import org.lh.dmlj.schema.editor.dictguide.DictguidesRegistry;
import org.lh.dmlj.schema.editor.log.LogProvidingPlugin;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;
import org.lh.dmlj.schema.editor.service.ServicesPlugin;
import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Plugin extends AbstractUIPlugin implements IPropertyChangeListener, LogProvidingPlugin {

	// The plug-in ID
	public static final String 		PLUGIN_ID = 
		"org.lh.dmlj.schema.editor.core"; //$NON-NLS-1$
	
	// The shared instance
	private static Plugin 			plugin;
			
	private Font 					figureFont;
	private Font 					figureFontBold;
	private Font 					figureFontItalic;
	private Font 					figureFontSmall;
	private Map<String, Image> 	 	images = new HashMap<String, Image>();
	private boolean 				logDebugMessages;
	private File 					tmpFolder;
	
	private IWorkbenchListener workbenchListener = new WorkbenchListener();	

	private static void deleteDirectoryContents(File directory) {
		for (String fileName : directory.list()) {
			File file = new File(directory, fileName);
			if (file.isDirectory()) {
				deleteDirectoryContents(file);
			}
			file.delete();			
		}		
	}	
	
	public static Plugin getDefault() {
		return plugin;
	}

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
	
	public String getDefaultFileExtension() {
		return getPreferenceStore().getString(PreferenceConstants.DEFAULT_FILE_EXTENSION);
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
	
	public Font getFigureFontSmall() {
		return figureFontSmall;
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

	private void hookWorkbenchListener() {
		// we may need to close .schemadsl editors on workbench shutdown 
		PlatformUI.getWorkbench().addWorkbenchListener(workbenchListener);
	}

	@Override
	public boolean isDebugEnabled() {
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

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES)) {
			logDebugMessages = ((Boolean) event.getNewValue()).booleanValue();
		}
	}	

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		// create the fonts for the diagram editor
		int operatingSystemTextSize = 
			getPreferenceStore().getInt(PreferenceConstants.OPERATING_SYSTEM_TEXT_SIZE);
		int normalHeight;
		int smallHeight;
		if (operatingSystemTextSize == 100) {
			normalHeight = 6;
			smallHeight = 5;
		} else if (operatingSystemTextSize == 125) {
			normalHeight = 5;
			smallHeight = 4;
		} else if (operatingSystemTextSize == 150) {
			normalHeight = 4;
			smallHeight = 3;
		} else {			
			normalHeight = 3; // Note: 3 is too small and 4 too big...
			smallHeight = 3;
		}
		figureFont = new Font(Display.getCurrent(), "Arial", normalHeight, SWT.NORMAL);
		figureFontBold = new Font(Display.getCurrent(), "Arial", normalHeight, SWT.BOLD);
		figureFontItalic = new Font(Display.getCurrent(), "Arial", normalHeight, SWT.ITALIC);
		figureFontSmall = new Font(Display.getCurrent(), "Arial", smallHeight, SWT.NORMAL);
		
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
		
		// avoid a delay the first time the user selects a DSL tab in the Properties view
		// note: the delay can only be avoided AFTER the warm up job has completed
		new DSLWarmUpJob().schedule(); 
		
		hookWorkbenchListener();
	}

	@Override
	public void stop(BundleContext context) throws Exception {		
		
		unhookWorkbenchListener();
		
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
		figureFontSmall.dispose();
		
		plugin = null;
		super.stop(context);
	}

	private void unhookWorkbenchListener() {
		PlatformUI.getWorkbench().removeWorkbenchListener(workbenchListener);
	}

}
