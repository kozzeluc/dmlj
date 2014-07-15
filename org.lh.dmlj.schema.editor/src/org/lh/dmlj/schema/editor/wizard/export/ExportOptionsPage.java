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
package org.lh.dmlj.schema.editor.wizard.export;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;

public class ExportOptionsPage extends WizardPage {

	private Button btnSortSchemaEntities;
	private boolean sortSchemaEntities;
	
	/**
	 * Create the wizard.
	 */
	public ExportOptionsPage() {
		super("xxportOptionsPage");
		setTitle("CA IDMS/DB Schema Syntax");
		setDescription("Set option(s)");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		btnSortSchemaEntities = new Button(container, SWT.CHECK);
		btnSortSchemaEntities.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		btnSortSchemaEntities.setText("Sort schema entities");
		
		initialize();
		
		setPageComplete(true);
		
	}

	private void initialize() {
		IPreferenceStore store = Plugin.getDefault().getPreferenceStore();
		sortSchemaEntities = 
			store.getBoolean(PreferenceConstants.SORT_SCHEMA_ENTITIES_ON_EXPORT_TO_SYNTAX);
		btnSortSchemaEntities.setSelection(sortSchemaEntities);
	}

	public boolean isSortSchemaEntities() {
		return sortSchemaEntities;
	}

	private void validatePage() {		
		// currently no validation required 		
		sortSchemaEntities = btnSortSchemaEntities.getSelection();
	}	
	
}
