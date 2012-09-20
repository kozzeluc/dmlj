package org.lh.dmlj.schema.editor.importtool.impl;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class OutputFileSelectionPage extends WizardNewFileCreationPage {	
	
	private IDataEntryContext context;	
	
	public OutputFileSelectionPage(IStructuredSelection selection) {
		super("page1", selection);	
		setTitle("CA IDMS Schema");
		
		setFileName("My.schema");
	}	
	
	private File getSchemaFile() {
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
	
	public void setContext(IDataEntryContext context) {
		this.context = context;
	}
	
	public void setImportToolDescriptor(ImportToolDescriptor importToolDescriptor)  {
		String description = 
			importToolDescriptor != null && 
			!importToolDescriptor.getSource().trim().equals("") ?
			"Import a CA IDMS schema from " + importToolDescriptor.getSource() :
			"Import a CA IDMS schema";
		setDescription(description);
	}
	
	@Override
	protected boolean validatePage() {
		context.clearAttribute(IDataEntryContext.ATTRIBUTE_OUTPUT_FILE);
		setErrorMessage(null);
		if (super.validatePage()) {
			String extension = new Path(getFileName()).getFileExtension();
	        if (extension == null || !extension.equals("schema")) {
	        	setErrorMessage("The file extension must be .schema");
	        	return false;
	        }
	        context.setAttribute(IDataEntryContext.ATTRIBUTE_OUTPUT_FILE,
	        					 getSchemaFile());
	        return true;
	    }
	    return false;
	}

}