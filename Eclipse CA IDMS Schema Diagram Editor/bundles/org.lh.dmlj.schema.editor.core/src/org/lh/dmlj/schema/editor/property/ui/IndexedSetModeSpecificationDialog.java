/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.property.ui;

import static org.lh.dmlj.schema.editor.common.Tools.removeTrailingUnderscore;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;

public class IndexedSetModeSpecificationDialog extends Dialog {
	private final IndexedSetModeSpecification indexedSetModeSpecification;
	private String symbolicIndexName;
	private Short keyCount;
	private Short displacementPages;
	
	private Text textSymbolicIndex;
	private Text textKeyCount;
	private Text textDisplacementPages;
	private Button btnIndexBlockContainsAndDisplacementPages;
	private Button btnSymbolicIndex;

	public IndexedSetModeSpecificationDialog(Shell activeShell, IndexedSetModeSpecification indexedSetModeSpecification) {
		super(activeShell);
		this.indexedSetModeSpecification = indexedSetModeSpecification;
		setShellStyle(getShellStyle() | SWT.RESIZE); 
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Edit indexed set mode specification for set " + removeTrailingUnderscore(indexedSetModeSpecification.getSet().getName()));
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		var area = (Composite) super.createDialogArea(parent);
		var container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		btnSymbolicIndex = new Button(container, SWT.RADIO);
		btnSymbolicIndex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
				textSymbolicIndex.setFocus();
				textSymbolicIndex.selectAll();
			}
		});
		btnSymbolicIndex.setText("Symbolic index name:");
		
		textSymbolicIndex = new Text(container, SWT.BORDER);
		var gdTextSymbolicIndex = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gdTextSymbolicIndex.widthHint = 150;
		textSymbolicIndex.setLayoutData(gdTextSymbolicIndex);
		
		textSymbolicIndex.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				textSymbolicIndex.setText(textSymbolicIndex.getText().toUpperCase());
				textSymbolicIndex.selectAll();
				enableAndDisable();				
			}
		});
		
		textSymbolicIndex.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || e.keyCode == 16777296 || e.keyCode == SWT.ESC) { // enter and escape -keys
					textSymbolicIndex.setText(textSymbolicIndex.getText().toUpperCase());
					textSymbolicIndex.selectAll();
					enableAndDisable();					
				}
			}
		});
		
		btnIndexBlockContainsAndDisplacementPages = new Button(container, SWT.RADIO);
		btnIndexBlockContainsAndDisplacementPages.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
				textKeyCount.setFocus();
				textKeyCount.selectAll();
			}
		});
		var gdBtnIndexBlockContains = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdBtnIndexBlockContains.verticalIndent = 5;
		btnIndexBlockContainsAndDisplacementPages.setLayoutData(gdBtnIndexBlockContains);
		btnIndexBlockContainsAndDisplacementPages.setText("Index block contains:");
		
		textKeyCount = new Text(container, SWT.BORDER);
		var gdTextKeyCount = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTextKeyCount.verticalIndent = 5;
		gdTextKeyCount.widthHint = 50;
		textKeyCount.setLayoutData(gdTextKeyCount);
		
		textKeyCount.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		
		textKeyCount.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || e.keyCode == 16777296 || e.keyCode == SWT.ESC) { // enter and escape keys
					enableAndDisable();					
				}
			}
		});
		
		var lblKeys = new Label(container, SWT.NONE);
		var gdLblKeys = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdLblKeys.verticalIndent = 5;
		lblKeys.setLayoutData(gdLblKeys);
		lblKeys.setText("key(s)");
		
		var lblDisplacement = new Label(container, SWT.NONE);
		var gdLblDisplacement = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdLblDisplacement.horizontalIndent = 15;
		lblDisplacement.setLayoutData(gdLblDisplacement);
		lblDisplacement.setText("Displacement:");
		
		textDisplacementPages = new Text(container, SWT.BORDER);
		var gdTextDisplacementPages = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTextDisplacementPages.widthHint = 50;
		textDisplacementPages.setLayoutData(gdTextDisplacementPages);
		
		textDisplacementPages.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		
		textDisplacementPages.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || e.keyCode == 16777296 || e.keyCode == SWT.ESC) { // enter and escape keys					
					enableAndDisable();					
				}
			}
		});
		
		var lblPages = new Label(container, SWT.NONE);
		lblPages.setText("page(s)");
	
		initialize();
		
		return area;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		enableAndDisable();
	}

	private void initialize() {
		if (indexedSetModeSpecification.getSymbolicIndexName() != null) {
			btnSymbolicIndex.setSelection(true);
			textSymbolicIndex.setText(indexedSetModeSpecification.getSymbolicIndexName());
			textSymbolicIndex.setFocus();
			textSymbolicIndex.selectAll();
		} else {
			btnIndexBlockContainsAndDisplacementPages.setSelection(true);
			if (indexedSetModeSpecification.getKeyCount() != null) {
				// this should always be the case
				textKeyCount.setText(String.valueOf(indexedSetModeSpecification.getKeyCount().shortValue()));
				textKeyCount.setFocus();
				textKeyCount.selectAll();
			}
			if (indexedSetModeSpecification.getDisplacementPageCount() != null &&
				indexedSetModeSpecification.getDisplacementPageCount().shortValue() != 0) {
				
				textDisplacementPages.setText(String.valueOf(indexedSetModeSpecification.getDisplacementPageCount().shortValue()));
			}
		}
	}

	private void enableAndDisable() {
		textSymbolicIndex.setEnabled(btnSymbolicIndex.getSelection());
		textKeyCount.setEnabled(btnIndexBlockContainsAndDisplacementPages.getSelection());
		textDisplacementPages.setEnabled(textKeyCount.isEnabled());
			
		getButton(IDialogConstants.OK_ID).setEnabled(checkDataEntered() && anythingChanged());
		if (!getButton(IDialogConstants.OK_ID).isEnabled()) {
			return;
		}
		
		// make sure we've got all information available should the user press the OK button
		symbolicIndexName = getSymbolicIndexNameFromUI();
		keyCount = getKeyCountFromUI();
		displacementPages = getDisplacementPagesFromUI();
	}
	
	private boolean checkDataEntered() {
		if (btnSymbolicIndex.getSelection()) {
			var validationResult = NamingConventions.validate(textSymbolicIndex.getText(), NamingConventions.Type.SYMBOLIC_INDEX_NAME);
			return validationResult.getStatus() == ValidationResult.Status.OK;
		} else {
			return keyCountValid() && displacementPagesValid();
		}		
	}
	
	private boolean keyCountValid() {
		if (textKeyCount.getText().isBlank()) {
			return false;
		} else {
			try {
				var enteredKeyCount = Integer.parseInt(textKeyCount.getText().trim());
				return enteredKeyCount >= 3 && enteredKeyCount <= 8180;
			} catch (NumberFormatException e) {
				return false;
			}
		}
	}
	
	private boolean displacementPagesValid() {
		if (!textDisplacementPages.getText().isBlank()) {
			try {
				var enteredDisplacementPages = Integer.parseInt(textDisplacementPages.getText().trim());
				return enteredDisplacementPages >= 1 && enteredDisplacementPages <= 32767;
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return true;
		}
	}
	
	private boolean anythingChanged() {
		if (indexedSetModeSpecification.getSymbolicIndexName() != null) {
			return btnIndexBlockContainsAndDisplacementPages.getSelection() ||
				   !indexedSetModeSpecification.getSymbolicIndexName().equalsIgnoreCase(getSymbolicIndexNameFromUI());
		} else {
			return btnSymbolicIndex.getSelection() || !indexedSetModeSpecification.getKeyCount().equals(getKeyCountFromUI()) ||
				   (indexedSetModeSpecification.getDisplacementPageCount() == null && getDisplacementPagesFromUI() != null ||
				    indexedSetModeSpecification.getDisplacementPageCount() != null && getDisplacementPagesFromUI() == null ||
				    indexedSetModeSpecification.getDisplacementPageCount() != null &&
				    indexedSetModeSpecification.getDisplacementPageCount().shortValue() != 0 &&
				    !indexedSetModeSpecification.getDisplacementPageCount().equals(getDisplacementPagesFromUI()));
				    
		}
	}

	public Short getDisplacementPages() {
		return displacementPages;
	}

	private Short getDisplacementPagesFromUI() {
		if (btnIndexBlockContainsAndDisplacementPages.getSelection() && !textDisplacementPages.getText().trim().isEmpty() &&
			!textDisplacementPages.getText().trim().equals("0")) {
			
			return Short.valueOf(textDisplacementPages.getText().trim());
		} else {
			return null;
		}
	}
	
	public Short getKeyCount() {
		return keyCount;
	}

	private Short getKeyCountFromUI() {
		if (btnIndexBlockContainsAndDisplacementPages.getSelection()) {
			return Short.valueOf(textKeyCount.getText().trim());
		} else {
			return null;
		}
	}
	
	public String getSymbolicIndexName() {
		return symbolicIndexName;
	}

	private String getSymbolicIndexNameFromUI() {
		if (btnSymbolicIndex.getSelection()) {
			return textSymbolicIndex.getText().trim().toUpperCase();
		} else {
			return null;
		}
	}

}
