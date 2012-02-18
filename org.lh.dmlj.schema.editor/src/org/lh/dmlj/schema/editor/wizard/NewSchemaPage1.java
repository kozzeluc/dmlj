package org.lh.dmlj.schema.editor.wizard;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewSchemaPage1 extends WizardNewFileCreationPage {	
	
	public NewSchemaPage1(IStructuredSelection selection) {
		super("page1", selection);	
		setTitle("Schema");
		setDescription("Create a new schema");
		setFileName("My.schema");
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
	        if (extension == null || !extension.equals("schema")) {
	        	setErrorMessage("The file extension must be .schema");
	        	return false;
	        }
	        return true;
	    }
	    return false;
	}

}