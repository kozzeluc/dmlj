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
package org.lh.dmlj.schema.editor.importtool.elements.diagram;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class SchemaSelectionPage extends AbstractDataEntryPage {
	
	private Button btnOpenEditor;
	private Button btnBrowse;
	private Button btnFileSystem;
	private Combo comboOpenEditor;
	private Text textFileSystem;
	
	private List<SchemaEditor> openEditors = new ArrayList<>();
	private Schema currentSchema;
	private SchemaEditor currentEditor;
	private File currentSchemaFile;
	
	public SchemaSelectionPage() {
		super();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Control createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		Layout layout = new GridLayout(3, false);
		container.setLayout(layout);
		
		btnOpenEditor = new Button(container, SWT.RADIO);
		btnOpenEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
		btnOpenEditor.setSelection(true);
		btnOpenEditor.setText("From an open editor:");
		
		comboOpenEditor = new Combo(container, SWT.READ_ONLY);
		comboOpenEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
		comboOpenEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		btnFileSystem = new Button(container, SWT.RADIO);
		btnFileSystem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
		btnFileSystem.setText("From file system:");
		
		textFileSystem = new Text(container, SWT.BORDER);
		textFileSystem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				validate();
			}
		});
		textFileSystem.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validate();
			}
		});
		textFileSystem.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnBrowse = new Button(container, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(Display.getCurrent().getActiveShell());
				fileDialog.setText("Select a .schema File");
				fileDialog.setFileName(textFileSystem.getText());
				fileDialog.setFilterExtensions(new String[] {"*.schema"});
				String newValue = fileDialog.open();							
				if (newValue != null) {
					textFileSystem.setText(newValue);
					textFileSystem.redraw();
					validate();
				}
			}
		});
		btnBrowse.setText("Browse...");
		
		initialize();
		validate();
		
		return container;
	}

	private void enableAndDisable() {
		comboOpenEditor.setEnabled(btnOpenEditor.getSelection());
		textFileSystem.setEnabled(btnFileSystem.getSelection());
		btnBrowse.setEnabled(btnFileSystem.getSelection());
	}

	private void getOpenEditors() {
		for (IEditorReference editorReference : PlatformUI.getWorkbench()
				  										  .getActiveWorkbenchWindow()
				  										  .getActivePage()
				  										  .getEditorReferences()) {
			IEditorPart editorPart = editorReference.getEditor(true);
			if (editorPart instanceof SchemaEditor) {
				openEditors.add((SchemaEditor) editorPart);
			}
		}
		Collections.sort(openEditors, new Comparator<SchemaEditor>() {
			@Override
			public int compare(SchemaEditor e1, SchemaEditor e2) {
				Schema schema1 = e1.getSchema();
				Schema schema2 = e2.getSchema();
				if (schema1.getName().equals(schema2.getName())) {
					if (schema1.getVersion() == schema2.getVersion()) {
						IFile file1 = ((IFileEditorInput) e1.getEditorInput()).getFile();
						IFile file2 = ((IFileEditorInput) e2.getEditorInput()).getFile();
						return file1.getFullPath().toString().compareTo(file2.getFullPath().toString());
					} else {
						return schema1.getVersion() - schema2.getVersion();
					}
				} else {
					return schema1.getName().compareTo(schema2.getName());
				}				
			}			
		});
	}

	private void initialize() {
		
		currentEditor = (SchemaEditor) PlatformUI.getWorkbench()
												 .getActiveWorkbenchWindow()
												 .getActivePage()
												 .getActiveEditor();
		IFile file = ((IFileEditorInput) currentEditor.getEditorInput()).getFile();
		currentSchemaFile = new File(file.getParent().getLocation().toFile(), file.getName());
		
		getOpenEditors();
		SchemaRecord currentRecord = 
			getContext().getAttribute(IDataEntryContext.CURRENT_SCHEMA_RECORD);
		currentSchema = currentRecord.getSchema();
		for (SchemaEditor schemaEditor : openEditors) {
			file = ((IFileEditorInput) schemaEditor.getEditorInput()).getFile();
			StringBuilder entryText = new StringBuilder();
			if (schemaEditor.getSchema() == currentSchema) {
				entryText.append("*");
			}
			entryText.append(file.getName());
			entryText.append(" (");			
			entryText.append(file.getParent().getLocation());
			entryText.append(")");
			comboOpenEditor.add(entryText.toString());
		}
		comboOpenEditor.select(openEditors.indexOf(PlatformUI.getWorkbench()
				  											   .getActiveWorkbenchWindow()
				  											   .getActivePage()
				  											   .getActiveEditor()));
	}

	protected void validate() {
		
		boolean pageComplete = true;
		getController().setErrorMessage(null);
		
		if (btnOpenEditor.getSelection()) {
			int i = comboOpenEditor.getSelectionIndex();
			getContext().setAttribute(IDataEntryContext.SCHEMA, openEditors.get(i).getSchema());
		} else if (btnFileSystem.getSelection()) {
			if (textFileSystem.getText().trim().isEmpty()) {
				pageComplete = false;
			} else if (!new File(textFileSystem.getText()).exists()) {
				getController().setErrorMessage("File not found: " + textFileSystem.getText().trim());
				pageComplete = false;
			} else if (!new File(textFileSystem.getText()).exists()) {
				getController().setErrorMessage("Not a file: " + textFileSystem.getText().trim());
				pageComplete = false;
			} else {
				try {
					ResourceSet resourceSet = new ResourceSetImpl();
					resourceSet.getResourceFactoryRegistry()
					   		   .getExtensionToFactoryMap()
					   		   .put("schema", new XMIResourceFactoryImpl());
					File schemaFile = new File(textFileSystem.getText());
					URI uri = URI.createFileURI(schemaFile.getAbsolutePath());
					Resource resource = resourceSet.getResource(uri, true);
					Schema schema = (Schema) resource.getContents().get(0);					
					if (currentSchemaFile.equals(schemaFile) && !currentEditor.isDirty()) {
						// use the current schema if the current editor is not dirty
						getContext().setAttribute(IDataEntryContext.SCHEMA, currentSchema);
					} else {
						getContext().setAttribute(IDataEntryContext.SCHEMA, schema);
					}
				} catch (Throwable t) {
					getController().setErrorMessage("Not a valid file: " + t.getMessage());
					pageComplete = false;
				}
			}
		}
		
		enableAndDisable();
		getController().setPageComplete(pageComplete);
		
	}

}
