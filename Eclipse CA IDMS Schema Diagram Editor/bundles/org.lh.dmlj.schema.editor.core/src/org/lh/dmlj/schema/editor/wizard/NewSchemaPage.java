/**
 * Copyright (C) 2016  Luc Hermans
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
package org.lh.dmlj.schema.editor.wizard;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.lh.dmlj.schema.editor.Plugin;

public class NewSchemaPage extends WizardNewFileCreationPage {	
	
	public NewSchemaPage(IStructuredSelection selection) {
		super("page1", selection);	
		setTitle("CA IDMS Schema");
		setDescription("Create a new CA IDMS schema");
		setFileName("My." + Plugin.getDefault().getDefaultFileExtension());
	}	
	
	public File getSchemaFile() {
		try {
			return ResourcesPlugin.getWorkspace()
			  					  .getRoot()
					  			  .getFile(getContainerFullPath().append(getFileName()))
					  			  .getLocation()
					  			  .toFile();
		} catch (Throwable t) {
			return null;
		}
    }
	
	@Override
	protected boolean validatePage() {
		setErrorMessage(null);
		if (super.validatePage()) {
			String extension = new Path(getFileName()).getFileExtension();
	        if (extension == null || 
	        	!(extension.equals("schema") || extension.equals("schemadsl"))) {
	        	
	        	setErrorMessage("The file extension must be '.schema' or '.schemadsl'");
	        	return false;
	        }
	        return true;
	    }
	    return false;
	}

}
