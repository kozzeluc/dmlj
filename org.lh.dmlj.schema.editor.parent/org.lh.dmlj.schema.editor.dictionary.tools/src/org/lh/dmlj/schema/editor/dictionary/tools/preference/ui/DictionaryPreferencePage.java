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
package org.lh.dmlj.schema.editor.dictionary.tools.preference.ui;

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
import org.lh.dmlj.schema.editor.dictionary.tools.preference.PreferenceConstants;

public class DictionaryPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	private Table table;
	private Text textDefaultSchema;
	private Text textDefaultQueryDbkeyListSizeMaximum;
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
		dictionary.setQueryDbkeyListSizeMaximum(dialog.getDictionaryQueryDbkeyListSizeMaximum());
		dictionary.setSysdirl(dialog.isDictionarySysdirl());
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
		tblclmnId.setWidth(200);
		tblclmnId.setText("Id");
		
		TableColumn tblclmnSysdirl = new TableColumn(table, SWT.CENTER);
		tblclmnSysdirl.setWidth(65);
		tblclmnSysdirl.setText("SYSDIRL ?");
		
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
		GridData gd_text = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1);
		gd_text.widthHint = 100;
		gd_text.verticalIndent = 10;
		textDefaultSchema.setLayoutData(gd_text);
		
		Label lblDefaultMaximumDbkeyList = new Label(container, SWT.WRAP);
		GridData gd_lblDefaultMaximumDbkeyList = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDefaultMaximumDbkeyList.widthHint = 200;
		lblDefaultMaximumDbkeyList.setLayoutData(gd_lblDefaultMaximumDbkeyList);
		lblDefaultMaximumDbkeyList.setText("Default maximum dbkey list size in queries:");
		
		textDefaultQueryDbkeyListSizeMaximum = new Text(container, SWT.BORDER | SWT.RIGHT);
		GridData gd_textQueryMaxdbkeylistsize = new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 1, 1);
		gd_textQueryMaxdbkeylistsize.widthHint = 25;
		textDefaultQueryDbkeyListSizeMaximum.setLayoutData(gd_textQueryMaxdbkeylistsize);
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
		dictionary.setQueryDbkeyListSizeMaximum(dialog.getDictionaryQueryDbkeyListSizeMaximum());
		dictionary.setSysdirl(dialog.isDictionarySysdirl());
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
		int defaultQueryDbkeyListSizeMaximum = 
			store.getDefaultInt(PreferenceConstants.DEFAULT_QUERY_DBKEY_LIST_SIZE_MAXIMUM);
		textDefaultQueryDbkeyListSizeMaximum.setText(String.valueOf(defaultQueryDbkeyListSizeMaximum));
	}

	private void initializeTable(Dictionary dictionaryToSelect) {
		table.removeAll();
		try {
			dictionaries = Dictionary.list(dictionaryFolder);
			for (Dictionary dictionary : dictionaries) {
				TableItem tableItem = new TableItem(table, SWT.NONE);
				tableItem.setText(0, dictionary.getId());
				tableItem.setText(1, (dictionary.isSysdirl() ? "Yes" : "No"));
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
		int defaultQueryDbkeyListSizeMaximum = 
			store.getInt(PreferenceConstants.DEFAULT_QUERY_DBKEY_LIST_SIZE_MAXIMUM);
		textDefaultQueryDbkeyListSizeMaximum.setText(String.valueOf(defaultQueryDbkeyListSizeMaximum));		
		
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
	
	@Override
	public boolean performOk() {
		return storeValues();		
	}

	private boolean storeValues() {
		
		IPreferenceStore store = getPreferenceStore();
		
		// deal with the 'default schema'
		if (textDefaultSchema.getText().trim().isEmpty()) {
			// set the schema name to 'SYSDICT' if it is not filled in
			textDefaultSchema.setText("SYSDICT");
		} else if (textDefaultSchema.getText().trim().length() > 18) {
			// make sure the schema name is no longer than 18 characters
			StringBuilder p = new StringBuilder(textDefaultSchema.getText().trim());
			p.setLength(18);
			textDefaultSchema.setText(p.toString());
		}
		store.setValue(PreferenceConstants.DEFAULT_SCHEMA, textDefaultSchema.getText().trim());
		
		// deal with the 'default query debkey list size maximum'
		int defaultQueryDbkeyListSizeMaximum = Integer.MIN_VALUE;	
		try {
			defaultQueryDbkeyListSizeMaximum = 
				Integer.valueOf(textDefaultQueryDbkeyListSizeMaximum.getText().trim());
			if (defaultQueryDbkeyListSizeMaximum < 1 || defaultQueryDbkeyListSizeMaximum > 1000) {
				defaultQueryDbkeyListSizeMaximum = Integer.MIN_VALUE;
			}
		} catch (NumberFormatException e) {
		}
		if (defaultQueryDbkeyListSizeMaximum == Integer.MIN_VALUE) {
			// the value of the 'default query debkey list size maximum' is invalid; restore it from
			// the preference store
			textDefaultQueryDbkeyListSizeMaximum.setText(String.valueOf(store.getInt(PreferenceConstants.DEFAULT_QUERY_DBKEY_LIST_SIZE_MAXIMUM)));
		} else {
			// the value of the 'default query debkey list size maximum' is valid
			store.setValue(PreferenceConstants.DEFAULT_QUERY_DBKEY_LIST_SIZE_MAXIMUM, 
						   defaultQueryDbkeyListSizeMaximum);
		}
		
		return true;
	}

	private void testConnection() {
		Dictionary dictionary = dictionaries.get(table.getSelectionIndex());
		JdbcTools.testConnectionWithOperationInProgressIndicator(dictionary);
		initializeTable(dictionary); // the password might be added, so refresh the dictionary
		enableAndDisable();
	}
	
}
