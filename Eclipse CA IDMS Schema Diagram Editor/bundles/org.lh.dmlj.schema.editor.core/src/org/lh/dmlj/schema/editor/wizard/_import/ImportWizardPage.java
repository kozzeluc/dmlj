/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.wizard._import;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;

/**
 * A generic import wizard page that wraps a data entry page.
 */
public class ImportWizardPage extends WizardPage {
	
	private AbstractDataEntryPage dataEntryPage;
	
	public ImportWizardPage(AbstractDataEntryPage dataEntryPage, String pageName, String title, 
						    String message) {
		
		super(pageName);
		this.dataEntryPage = dataEntryPage;
		setTitle(title);
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
