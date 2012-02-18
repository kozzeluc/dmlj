package org.lh.dmlj.schema.editor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Plugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.lh.dmlj.schema.editor"; //$NON-NLS-1$

	// The shared instance
	private static Plugin plugin;
	
	private Font font = new Font(Display.getCurrent(), "Arial", 6, SWT.NORMAL);
	private File tmpFolder;
	
	private static boolean copy(InputStream source, File target) {
		try {
			OutputStream out = new FileOutputStream(target);
			boolean b = copy(source, out);
			out.flush();
			out.close();
			return b;
		} catch (IOException e) {
			return false;
		}
	}
	
	private static boolean copy(InputStream source, OutputStream target) {
		try {
			int chunkSize = 63 * 1024;			
			byte[] ba = new byte[chunkSize];
			while (true) {
				int bytesRead = readBlocking(source, ba, 0, chunkSize);
				if (bytesRead > 0) {
					target.write(ba, 0, bytesRead);					
				} else {
					break;
				}
			}
		} catch (IOException e) {
			return false;
		}
		return true;
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
	
	private static int readBlocking(InputStream in, byte b[], int off, int len)
		throws IOException {
		
		int totalBytesRead = 0;
		while (totalBytesRead < len) {
			int bytesRead = 
				in.read(b, off + totalBytesRead, len - totalBytesRead);
			if (bytesRead < 0) {
				break;
			}
			totalBytesRead += bytesRead;
		}
		return totalBytesRead;
	}	

	/**
	 * The constructor
	 */
	public Plugin() {
	}

	public Font getFont() {
		return font;
	}
	
	public File getTemporaryFilesFolder() {
		return tmpFolder;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;		
				
		// Locate the temporary files folder in the workspace.  We should 
		// probably put our temporary files somewhere else, since the folder 
		// returned by the call to the plug-in's getStateLocation() method 
		// "is recommended for plug-in preference settings and other 
		// configuration parameters".  That location is perfectly usable though.
		tmpFolder = new File(getStateLocation().toFile(), "tmp");
		if (!tmpFolder.exists()) {
			tmpFolder.mkdir();
		}
		
		// we need to place some files in the temporary files folder - we need
		// those files to create IDatabase instances denoting dictionaries; we
		// need the plug-in's classloader for this...
		ClassLoader cl = Plugin.class.getClassLoader(); 		
		
		// copy the IDMSNTWK mapping file to the temporary files folder...
		InputStream source = 
			cl.getResourceAsStream("/resources/org.lh.dmlj.idmsntwk.mapping");
		File target = new File(tmpFolder, "org.lh.dmlj.idmsntwk.mapping");
		if (!copy(source, target)) {
			throw new Error("cannot copy " + target.getName() + 
						    " to the temporary files folder");
		}
		
		// copy the IDMSNTWK (old-style) schema file to the temporary files 
		// folder...
		source = 
			cl.getResourceAsStream("/resources/IDMSNTWK version 1.xml");
		target = new File(tmpFolder, "IDMSNTWK version 1.xml");
		if (!copy(source, target)) {
			throw new Error("cannot copy " + target.getName() + 
						    " to the temporary files folder");
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
		// cleanup our temporary file folder...
		deleteDirectoryContents(tmpFolder);
		tmpFolder.delete();		
		
		font.dispose();
		plugin = null;
		super.stop(context);
	}

}
