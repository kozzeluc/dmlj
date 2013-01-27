package org.lh.dmlj.schema.editor.wizard._import.schema;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;

public class ImportWizardPage extends WizardPage {
	
	private AbstractDataEntryPage dataEntryPage;
	
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
	public boolean isPageComplete() {
		return !isRelevant() || super.isPageComplete();
	}
	
	public boolean isRelevant() {
		return getDataEntryPage().isPageRelevant();
	}	
	
}