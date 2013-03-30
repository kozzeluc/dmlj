package org.lh.dmlj.schema.editor.wizard._import.schema;

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
	
	private boolean updateMode = true;
	
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
		// make sure the user cannot return to the import tool selection page; 
		// once an import tool is selected, it is important that the user cannot 
		// select another one because we wouldn't be able to replace the data
		// entry pages (import mode)
		return null;
	}	
	
	@Override
	public boolean isPageComplete() {
		if (updateMode) {
			// this page will never be shown when in update mode, so we must
			// always return true; if we rely on the super class' 
			// isPageComplete() method, we might get into a situation where the
			// wizard's finish button is never enabled, so make sure the page
			// is always considered to be complete when in update mode
			return true;
		} else {
			return super.isPageComplete();
		}
	}
	
	public void setImportToolExtensionElement(ImportToolExtensionElement importToolExtensionElement)  {
		String description = 
			importToolExtensionElement != null && 
			!importToolExtensionElement.getSource().trim().equals("") ?
			DESCRIPTION_PREFIX + " from " + importToolExtensionElement.getSource() :
			DESCRIPTION_PREFIX;
		setDescription(description);
	}
	
	public void setUpdateMode(boolean newValue) {
		updateMode = newValue;
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