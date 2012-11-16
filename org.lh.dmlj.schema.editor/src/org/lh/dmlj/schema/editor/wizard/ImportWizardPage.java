package org.lh.dmlj.schema.editor.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;

public class ImportWizardPage extends WizardPage {
	
	private AbstractDataEntryPage dataEntryPage;
	
	protected ImportWizardPage(AbstractDataEntryPage dataEntryPage, 
							   String pageName, String message) {
		
		super(pageName);
		this.dataEntryPage = dataEntryPage;
		setTitle("CA IDMS/DB Schema");
		setMessage(message);
		setPageComplete(false);
	}
	
	public void aboutToShow() {
		if (getControl() != null) {			
			dataEntryPage.aboutToShow();
		}
	}

	@Override
	public void createControl(Composite parent) {
		setControl(dataEntryPage.createControl(parent));		
		aboutToShow();
	}	

	@Override
	public boolean isPageComplete() {
		return !isRelevant() || super.isPageComplete();
	}
	
	public boolean isRelevant() {
		return dataEntryPage.isPageRelevant();
	}	
	
}