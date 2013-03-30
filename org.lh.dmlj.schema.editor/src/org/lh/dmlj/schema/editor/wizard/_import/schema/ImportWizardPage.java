package org.lh.dmlj.schema.editor.wizard._import.schema;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;

public class ImportWizardPage extends WizardPage {
	
	private AbstractDataEntryPage dataEntryPage;
	private boolean 			  firstDataEntryPageInUpdateMode = false;
	
	public ImportWizardPage(AbstractDataEntryPage dataEntryPage, 
							String pageName, String message) {
		
		super(pageName);
		this.dataEntryPage = dataEntryPage;
		setTitle("CA IDMS/DB Schema");
		setMessage(message);
		setPageComplete(false);
	}
	
	public void aboutToShow() {
		if (getControl() != null) {			
			getDataEntryPage().aboutToShow();
		}
	}

	@Override
	public void createControl(Composite parent) {
		setControl(getDataEntryPage().createControl(parent));		
		aboutToShow();
	}	

	public AbstractDataEntryPage getDataEntryPage() {
		return dataEntryPage;
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		if (firstDataEntryPageInUpdateMode) {
			// make sure the previous page button is disabled and that the user
			// cannot get back to the import tool selection page in update mode
			// (the output file selection page ALWAYS sits between the import
			// tool selection page and the first pre options data entry page, 
			// but is NEVER shown in update mode); this is consistent with the 
			// wizard's import mode where the import tool selection page is
			// NEVER shown again once its next page button is pressed...
			return null;
		} else {
			return super.getPreviousPage();
		}
	}

	@Override
	public boolean isPageComplete() {
		return !isRelevant() || super.isPageComplete();
	}
	
	public boolean isRelevant() {
		return getDataEntryPage().isPageRelevant();
	}	
	
	void setFirstDataEntryPageInUpdateMode(boolean newValue) {
		firstDataEntryPageInUpdateMode = newValue;
	}
	
}