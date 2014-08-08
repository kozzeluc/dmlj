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
package org.lh.dmlj.schema.editor.dictionary.tools.preference;

import java.io.File;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;

public class DictionaryPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	private Table table;
	private Text textDefaultSchema;
	private Button btnAddDictionary;
	private Button btnDeleteDictionary;
	private Button btnEditDictionary;
	private Button btnTestConnection;
	
	private File dictionaryFolder;
	private List<Dictionary> dictionaries;

	public DictionaryPreferencePage() {
		super();
		setDescription("Dictionary settings:");
	}

	private void addDictionary() {
		EditDictionaryDialog dialog = new EditDictionaryDialog(getShell(), null);
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			return;
		}
		Dictionary dictionary;
		try {
			dictionary = Dictionary.newInstance(dictionaryFolder);
		} catch (Throwable t) {
			t.printStackTrace();
			MessageDialog.openError(getShell(), "Error while preparing new dictionary", 
									t.getMessage());
			return;
		}
		dictionary.setId(dialog.getDictionaryId());
		dictionary.setHostname(dialog.getDictionaryHostname());
		dictionary.setPort(dialog.getDictionaryPort());
		dictionary.setDictname(dialog.getDictionaryDictname());
		dictionary.setUser(dialog.getDictionaryUser());
		dictionary.setPassword(dialog.getDictionaryPassword());
		dictionary.setSchema(dialog.getDictionarySchema());
		try {
			dictionary.toFile(dictionaryFolder);
		} catch (Throwable t) {
			t.printStackTrace();
			MessageDialog.openError(getShell(), "Error while saving dictionary data", 
									t.getMessage());
		}
		initializeTable(dictionary);
		enableAndDisable();
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
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnId = new TableColumn(table, SWT.NONE);
		tblclmnId.setWidth(250);
		tblclmnId.setText("Id");
		
		btnAddDictionary = new Button(container, SWT.NONE);
		btnAddDictionary.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addDictionary();
			}
		});
		btnAddDictionary.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, true, 1, 1));
		btnAddDictionary.setText("Add...");
		
		btnDeleteDictionary = new Button(container, SWT.NONE);
		btnDeleteDictionary.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteDictionary();
			}
		});
		btnDeleteDictionary.setEnabled(false);
		btnDeleteDictionary.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnDeleteDictionary.setText("Delete");
		
		btnEditDictionary = new Button(container, SWT.NONE);
		btnEditDictionary.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editDictionary();
			}
		});
		btnEditDictionary.setEnabled(false);
		btnEditDictionary.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnEditDictionary.setText("Edit...");
		
		btnTestConnection = new Button(container, SWT.NONE);
		btnTestConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testConnection();
			}
		});
		btnTestConnection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnTestConnection.setEnabled(false);
		btnTestConnection.setText("Test Conn.");
		
		Label lblDefaultCatalogSchema = new Label(container, SWT.WRAP);
		GridData gd_lblDefaultCatalogSchema = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDefaultCatalogSchema.widthHint = 200;
		gd_lblDefaultCatalogSchema.verticalIndent = 10;
		lblDefaultCatalogSchema.setLayoutData(gd_lblDefaultCatalogSchema);
		lblDefaultCatalogSchema.setText("Default name for catalog schemas that map to IDMSNTWK (SYSDIRL):");
		
		textDefaultSchema = new Text(container, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
		gd_text.verticalIndent = 10;
		textDefaultSchema.setLayoutData(gd_text);
		new Label(container, SWT.NONE);
		
		initializeValues();
		
		return container;
		
	}
	
	private void deleteDictionary() {
		Dictionary dictionary = dictionaries.get(table.getSelectionIndex());
		String message = "Are you sure you want to delete dictionary " + dictionary.getId() + "?";
		if (!MessageDialog.openConfirm(getShell(), "Delete Dictionary", message)) {
			return;
		}		
		if (!dictionary.remove(dictionaryFolder)) {
			MessageDialog.openError(getShell(), "Delete Dictionary", 
									"Dictionary could NOT be deleted.");
		}
		initializeTable(null);
		enableAndDisable();
		
	}

	@Override
	protected IPreferenceStore doGetPreferenceStore() {		
		return Plugin.getDefault().getPreferenceStore();
	}

	private void editDictionary() {
		Dictionary dictionary = dictionaries.get(table.getSelectionIndex());
		EditDictionaryDialog dialog = new EditDictionaryDialog(getShell(), dictionary);
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			return;
		}
		dictionary.setId(dialog.getDictionaryId());
		dictionary.setHostname(dialog.getDictionaryHostname());
		dictionary.setPort(dialog.getDictionaryPort());
		dictionary.setDictname(dialog.getDictionaryDictname());
		dictionary.setUser(dialog.getDictionaryUser());
		dictionary.setPassword(dialog.getDictionaryPassword());
		dictionary.setSchema(dialog.getDictionarySchema());
		try {
			dictionary.toFile(dictionaryFolder);
		} catch (Throwable t) {
			t.printStackTrace();
			MessageDialog.openError(getShell(), "Error while saving dictionary data", 
									t.getMessage());
		}
		initializeTable(dictionary);
		enableAndDisable();
	}

	private void enableAndDisable() {
		btnDeleteDictionary.setEnabled(table.getSelectionCount() > 0);
		btnEditDictionary.setEnabled(table.getSelectionCount() > 0);
		btnTestConnection.setEnabled(Plugin.getDefault().isDriverInstalled() && 
									 table.getSelectionCount() > 0);
	}

	@Override
	public void init(IWorkbench workbench) {
		dictionaryFolder = Plugin.getDefault().getDictionaryFolder();
	}

	private void initializeDefaults() {	
		IPreferenceStore store = getPreferenceStore();
		String defaultSchema = store.getDefaultString(PreferenceConstants.DEFAULT_SCHEMA);
		textDefaultSchema.setText(defaultSchema);
	}

	private void initializeTable(Dictionary dictionaryToSelect) {
		table.removeAll();
		try {
			dictionaries = Dictionary.list(dictionaryFolder);
			for (Dictionary dictionary : dictionaries) {
				TableItem tableItem = new TableItem(table, SWT.NONE);
				tableItem.setText(0, dictionary.getId());
			}
			if (dictionaryToSelect != null) {
				int rowToSelect = dictionaries.indexOf(dictionaryToSelect);
				table.select(rowToSelect);
			}
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	private void initializeValues() {		
		
		initializeTable(null);
		
		IPreferenceStore store = getPreferenceStore();
		String defaultSchema = store.getString(PreferenceConstants.DEFAULT_SCHEMA);
		textDefaultSchema.setText(defaultSchema);
		
		enableAndDisable();
		
	}
	
	@Override
	protected void performApply() {
		storeValues();
	}
	
	@Override
	protected void performDefaults() {
		super.performDefaults();
		initializeDefaults();
	}

	private boolean storeValues() {
		IPreferenceStore store = getPreferenceStore();
		store.setValue(PreferenceConstants.DEFAULT_SCHEMA, textDefaultSchema.getText().trim());
		return true;
	}

	private void testConnection() {
		Dictionary dictionary = dictionaries.get(table.getSelectionIndex());
		JdbcTools.testConnection(getShell(), dictionary);
		initializeTable(dictionary); // the password might be added, so refresh the dictionary
		enableAndDisable();
	}
	
}
