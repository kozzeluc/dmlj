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
package org.lh.dmlj.schema.editor.dictionary.tools;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class DictionaryPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	private Table table;
	private Text text;

	public DictionaryPreferencePage() {
		super();
		setDescription("Dictionary settings:");
	}

	@Override
	protected Control createContents(Composite parent) {
		
		final Composite container = new Composite(parent, SWT.NONE);		
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);
		
		Label lblDefinedDictionaries = new Label(container, SWT.NONE);
		lblDefinedDictionaries.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		lblDefinedDictionaries.setText("Dictionaries from which you want to import items:");
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnId = new TableColumn(table, SWT.NONE);
		tblclmnId.setWidth(75);
		tblclmnId.setText("Id");
		
		TableColumn tblclmnHostname = new TableColumn(table, SWT.NONE);
		tblclmnHostname.setWidth(75);
		tblclmnHostname.setText("Host:port");
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(75);
		tblclmnNewColumn.setText("DICTNAME");
		
		TableColumn tblclmnSchema = new TableColumn(table, SWT.NONE);
		tblclmnSchema.setWidth(75);
		tblclmnSchema.setText("Schema");
		
		Button btnNewButton_3 = new Button(container, SWT.NONE);
		btnNewButton_3.setEnabled(false);
		btnNewButton_3.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, true, 1, 1));
		btnNewButton_3.setText("Add...");
		
		Button btnNewButton_2 = new Button(container, SWT.NONE);
		btnNewButton_2.setEnabled(false);
		btnNewButton_2.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnNewButton_2.setText("Delete");
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.setEnabled(false);
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnNewButton.setText("Edit...");
		
		Button btnNewButton_1 = new Button(container, SWT.NONE);
		btnNewButton_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setText("Test Conn.");
		
		Label lblDefaultCatalogSchema = new Label(container, SWT.WRAP);
		GridData gd_lblDefaultCatalogSchema = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDefaultCatalogSchema.widthHint = 200;
		gd_lblDefaultCatalogSchema.verticalIndent = 10;
		lblDefaultCatalogSchema.setLayoutData(gd_lblDefaultCatalogSchema);
		lblDefaultCatalogSchema.setText("Default name for catalog schemas that map to IDMSNTWK (SYSDIRL):");
		
		text = new Text(container, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1);
		gd_text.verticalIndent = 10;
		gd_text.widthHint = 75;
		text.setLayoutData(gd_text);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		return container;
		
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
