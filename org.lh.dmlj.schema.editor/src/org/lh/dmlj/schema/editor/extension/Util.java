package org.lh.dmlj.schema.editor.extension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.lh.dmlj.schema.editor.Plugin;
import org.osgi.framework.Bundle;

public abstract class Util {
	
	private static final int BUFFSIZE = 63 * 1024;

	private static boolean copy(InputStream source, OutputStream target) {
		return copy(source, target, null);
	}	
	
    /**
	 * Copy an InputStream to an OutputStream, until EOF. Use only when you
	 * don't know the length.
	 * 
	 * @param source
	 *            InputStream, left open.
	 * 
	 * @param target
	 *            OutputStream, left open.
	 * 
	 * @param progressMonitor
	 *
	 * @return true if the copy was successful.
	 */
	private static boolean copy(InputStream source, OutputStream target,
			   				    IProgressMonitor progressMonitor) {
		try {

			// R E A D / W R I T E by chunks

			int chunkSize = BUFFSIZE;
			// code will work even when chunkSize = 0 or chunks = 0;
			// Even for small files, we allocate a big buffer, since we
			// don't know the size ahead of time.
			byte[] ba = new byte[chunkSize];

			// keep reading till hit eof

			while (true) {

				int bytesRead = readBlocking(source, ba, 0, chunkSize);
				if (bytesRead > 0) {
					target.write(ba, 0 /* offset in ba */, 
								 bytesRead /* bytes to write */);
					if (progressMonitor != null) {
						progressMonitor.worked(bytesRead);
					}
				} else {
					break; // hit eof
				}
			} // end while

			// C L O S E, done by caller if wanted.

		} catch (IOException e) {
			return false;
		}

		// all was ok
		return true;
	}	
	
	public static String getAttribute(IConfigurationElement configElement,
			   						  String name, String defaultValue) {

		String value = configElement.getAttribute(name);
		if (value != null) {
			return value;
		}
		if (defaultValue != null) {
			return defaultValue;
		}
		throw new IllegalArgumentException(configElement.getName() + 
										   " element with missing " + name + 
										   " attribute");
	}
	
	public static ImageDescriptor getImageDescriptor(IConfigurationElement element,
	  		 								  		 String attributeName) {
		
		String imagePath = element.getAttribute(attributeName);
		if (imagePath == null) {
			return null;
		}
		IExtension extension = element.getDeclaringExtension();
		String extendingPluginId = extension.getNamespaceIdentifier();
		ImageDescriptor imageDescriptor = 
			AbstractUIPlugin.imageDescriptorFromPlugin(extendingPluginId,
													   imagePath);
		return imageDescriptor;
	}		
	
	public static File getResourceAsFile(IConfigurationElement element,
	  		 							 String attributeName, 
	  		 							 String fileExtension) {
	
		String path = getAttribute(element, attributeName, "");
		if (!path.equals("")) {
			try {
				IExtension extension = element.getDeclaringExtension();
				Bundle bundle = 
					Platform.getBundle(extension.getNamespaceIdentifier());
				InputStream in = 
					FileLocator.openStream(bundle, new Path(path), false);
				File file = Plugin.getDefault().createTmpFile(fileExtension);
				OutputStream out = new FileOutputStream(file);
				copy(in, out);
				out.flush();
				out.close();
				in.close();	
				return file;
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}			
		} else {	
			return null;
		}
	}
	
	public static Properties getResourceAsProperties(IConfigurationElement element,
											  		 String attributeName) {
		
		Properties parameters = new Properties();		
		String path = getAttribute(element, attributeName, "");
		if (!path.equals("")) {
			try {	
				IExtension extension = element.getDeclaringExtension();
				Bundle bundle = 
					Platform.getBundle(extension.getNamespaceIdentifier());
				InputStream in = 
					FileLocator.openStream(bundle, new Path(path), false);
				PropertyResourceBundle prb = new PropertyResourceBundle(in);				
				for (String key : Collections.list(prb.getKeys())) {					
					parameters.put(key, prb.getString(key));
				}
				in.close();					
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}			
		}
		return parameters;
	}

	/**
	 * Reads exactly <code>len</code> bytes from the input stream into the
	 * byte array. This method reads repeatedly from the underlying stream until
	 * all the bytes are read. InputStream.read is often documented to block
	 * like this, but in actuality it does not always do so, and returns early
	 * with just a few bytes. readBlockiyng blocks until all the bytes are read,
	 * the end of the stream is detected, or an exception is thrown. You will
	 * always get as many bytes as you asked for unless you get an eof or other
	 * exception. Unlike readFully, you find out how many bytes you did get.
	 * 
	 * @param b
	 *            the buffer into which the data is read.
	 * @param off
	 *            the start offset of the data.
	 * @param len
	 *            the number of bytes to read.
	 * @return number of bytes actually read.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * 
	 */
	private static int readBlocking(InputStream in, byte b[], int off, int len)
		throws IOException {
		
		int totalBytesRead = 0;
		while (totalBytesRead < len) {
			int bytesRead = in.read(b, off + totalBytesRead, len
					- totalBytesRead);
			if (bytesRead < 0) {
				break;
			}
			totalBytesRead += bytesRead;
		}
		return totalBytesRead;
	}	
	
}