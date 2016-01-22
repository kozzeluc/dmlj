/**
 * Copyright (C) 2016  Luc Hermans
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
	
	private IndexedSetModeSpecification indexedSetModeSpecification;
	private Text textSymbolicIndex;
	private Text textKeyCount;
	private Text textDisplacementPages;
	private Button btnIndexBlockContainsAndDisplacementPages;
	private Button btnSymbolicIndex;
	
	private String symbolicIndexName;
	private Short keyCount;
	private Short displacementPages;

	public IndexedSetModeSpecificationDialog(Shell activeShell, 
											 IndexedSetModeSpecification indexedSetModeSpecification) {
		
		super(activeShell);
		this.indexedSetModeSpecification = indexedSetModeSpecification;
		setShellStyle(getShellStyle() | SWT.RESIZE); 
	}
	
	private boolean anythingChanged() {
		if (indexedSetModeSpecification.getSymbolicIndexName() != null) {
			return btnIndexBlockContainsAndDisplacementPages.getSelection() ||
				   !getSymbolicIndexNameFromUI().equalsIgnoreCase(indexedSetModeSpecification.getSymbolicIndexName());
		} else {
			return btnSymbolicIndex.getSelection() ||
				   !getKeyCountFromUI().equals(indexedSetModeSpecification.getKeyCount()) ||
				   (indexedSetModeSpecification.getDisplacementPageCount() == null &&
				    getDisplacementPagesFromUI() != null ||
				    indexedSetModeSpecification.getDisplacementPageCount() != null &&
				    getDisplacementPagesFromUI() == null ||
				    indexedSetModeSpecification.getDisplacementPageCount() != null &&
				    indexedSetModeSpecification.getDisplacementPageCount().shortValue() != 0 &&
				    !indexedSetModeSpecification.getDisplacementPageCount().equals(getDisplacementPagesFromUI()));
				    
		}
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Edit indexed set mode specification for set " + 
					  removeTrailingUnderscore(indexedSetModeSpecification.getSet().getName()));	    
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		enableAndDisable();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
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
		GridData gd_textSymbolicIndex = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_textSymbolicIndex.widthHint = 150;
		textSymbolicIndex.setLayoutData(gd_textSymbolicIndex);
		
		textSymbolicIndex.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String p = textSymbolicIndex.getText().toUpperCase();
				textSymbolicIndex.setText(p);
				textSymbolicIndex.selectAll();
				enableAndDisable();				
			}
		});
		
		textSymbolicIndex.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || 		 // enter-key
					e.keyCode == 16777296 || // enter-key								
					e.keyCode == SWT.ESC) {	
					
					String p = textSymbolicIndex.getText().toUpperCase();
					textSymbolicIndex.setText(p);
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
		GridData gd_btnIndexBlockContains = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnIndexBlockContains.verticalIndent = 5;
		btnIndexBlockContainsAndDisplacementPages.setLayoutData(gd_btnIndexBlockContains);
		btnIndexBlockContainsAndDisplacementPages.setText("Index block contains:");
		
		textKeyCount = new Text(container, SWT.BORDER);
		GridData gd_textKeyCount = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textKeyCount.verticalIndent = 5;
		gd_textKeyCount.widthHint = 50;
		textKeyCount.setLayoutData(gd_textKeyCount);
		
		textKeyCount.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		
		textKeyCount.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || 		 // enter-key
					e.keyCode == 16777296 || // enter-key								
					e.keyCode == SWT.ESC) {	
					
					enableAndDisable();					
				}
			}
		});
		
		Label lblKeys = new Label(container, SWT.NONE);
		GridData gd_lblKeys = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblKeys.verticalIndent = 5;
		lblKeys.setLayoutData(gd_lblKeys);
		lblKeys.setText("key(s)");
		
		Label lblDisplacement = new Label(container, SWT.NONE);
		GridData gd_lblDisplacement = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDisplacement.horizontalIndent = 15;
		lblDisplacement.setLayoutData(gd_lblDisplacement);
		lblDisplacement.setText("Displacement:");
		
		textDisplacementPages = new Text(container, SWT.BORDER);
		GridData gd_textDisplacementPages = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textDisplacementPages.widthHint = 50;
		textDisplacementPages.setLayoutData(gd_textDisplacementPages);
		
		textDisplacementPages.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		
		textDisplacementPages.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || 		 // enter-key
					e.keyCode == 16777296 || // enter-key								
					e.keyCode == SWT.ESC) {	
					
					enableAndDisable();					
				}
			}
		});
		
		Label lblPages = new Label(container, SWT.NONE);
		lblPages.setText("page(s)");
	
		initialize();
		
		return area;
	}
	
	private void enableAndDisable() {
	
		boolean ok = true;
		
		if (btnSymbolicIndex.getSelection()) {
			String symbolicIndexName = textSymbolicIndex.getText();
			ValidationResult validationResult = 
				NamingConventions.validate(symbolicIndexName, 
										   NamingConventions.Type.SYMBOLIC_INDEX_NAME);
			if (validationResult.getStatus() != ValidationResult.Status.OK) {
				ok = false;
			}
		} else {
			String sKeyCount = textKeyCount.getText().trim();
			String sDisplacementPages = textDisplacementPages.getText().trim();
			if (sKeyCount.isEmpty()) {
				ok = false;
			} else {
				try {
					int keyCount = Integer.valueOf(sKeyCount).intValue();
					if (keyCount < 3 || keyCount > 8180) {
						ok = false;
					}
				} catch (NumberFormatException e) {
					ok = false;
				}
			}
			if (!sDisplacementPages.isEmpty()) {
				try {
					int displacementPages = Integer.valueOf(sDisplacementPages).intValue();
					if (displacementPages < 1 || displacementPages > 32767) {
						ok = false;
					}
				} catch (NumberFormatException e) {
					ok = false;
				}
			}
		}
		
		if (ok) {
			ok = anythingChanged();
		}
		
		textSymbolicIndex.setEnabled(btnSymbolicIndex.getSelection());
		textKeyCount.setEnabled(btnIndexBlockContainsAndDisplacementPages.getSelection());
		textDisplacementPages.setEnabled(textKeyCount.isEnabled());
	
		Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(ok);
		if (!ok) {
			return;
		}
		
		// make sure we've got all information available should the user press the OK button
		symbolicIndexName = getSymbolicIndexNameFromUI();
		keyCount = getKeyCountFromUI();
		displacementPages = getDisplacementPagesFromUI();
	}

	public Short getDisplacementPages() {
		return displacementPages;
	}

	private Short getDisplacementPagesFromUI() {
		if (btnIndexBlockContainsAndDisplacementPages.getSelection() &&
			!textDisplacementPages.getText().trim().isEmpty() &&
			!textDisplacementPages.getText().trim().equals("0")) {
			
			return Short.valueOf(textDisplacementPages.getText().trim());
		} else {
			return null;
		}
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
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

}
