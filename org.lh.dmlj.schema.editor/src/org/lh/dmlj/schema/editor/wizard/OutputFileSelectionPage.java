package org.lh.dmlj.schema.editor.wizard;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.lh.dmlj.schema.editor.extension.ImportToolExtensionElement;

public class OutputFileSelectionPage extends WizardNewFileCreationPage {		
	
	private static final String DESCRIPTION_PREFIX = 
		"Import a CA IDMS/DB schema";
	
	public OutputFileSelectionPage(IStructuredSelection selection) {
		super("_outputFileSelectionPage", selection);	
		setTitle("CA IDMS/DB Schema");
		
		setFileName("My.schema");
	}	
	
	public File getOutputFile() {
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
	public IWizardPage getPreviousPage() {
		return null;
	}		
	
	public void setImportToolDescriptor(ImportToolExtensionElement importToolDescriptor)  {
		String description = 
			importToolDescriptor != null && 
			!importToolDescriptor.getSource().trim().equals("") ?
			DESCRIPTION_PREFIX + " from " + importToolDescriptor.getSource() :
			DESCRIPTION_PREFIX;
		setDescription(description);
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