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

import java.util.stream.IntStream;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.common.ValidationResult;

public class AreaDialog extends Dialog {
	public enum Action { KEEP_IN_CURRENT_AREA, MOVE_TO_NEW_AREA, MOVE_TO_OTHER_EXISTING_AREA, RENAME_AREA }
	
	private Action action;
	private String areaName;
	private AreaSpecification areaSpecification;
	private Button btnNewArea;
	private Button btnOffset;
	private Button btnRenameArea;
	private Button btnSelectArea;
	private Button btnSymbolicSubarea;
	private Combo comboExistingArea;
	private Combo comboOffsetUnits;
	private Combo comboSizeUnits;
	private Integer	offsetPageCount;
	private Short offsetPercent;
	private Integer pageCount;
	private Short percent;
	private Schema schema;
	private String symbolicSubareaName;
	private Text textNewArea;
	private Text textOffset;
	private Text textRenameArea;
	private Text textSize;
	private Text textSymbolicSubarea;	
		
	public AreaDialog(Shell parentShell, AreaSpecification areaSpecification) {
		super(parentShell);
		this.areaSpecification = areaSpecification;
		schema = areaSpecification.getArea().getSchema();
		setShellStyle(getShellStyle() | SWT.RESIZE); 
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		String p;
		if (areaSpecification.getRecord() != null) {
			p = "record " + areaSpecification.getRecord().getName();
		} else if (areaSpecification.getSystemOwner() != null) {
			p = "system owner of set " + areaSpecification.getSystemOwner().getSet().getName();
		} else {
			throw new IllegalStateException("logic error: neither record nor system owner");
		}
	    shell.setText("Edit area specification for " + p);	    
	}	
		
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		enableAndDisable();
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		var container = (Composite) super.createDialogArea(parent);
		var gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		btnSelectArea = new Button(container, SWT.RADIO);
		btnSelectArea.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnSelectArea.setText("Area :");
		
		comboExistingArea = new Combo(container, SWT.READ_ONLY);
		var gdComboExistingArea = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdComboExistingArea.widthHint = 150;
		comboExistingArea.setLayoutData(gdComboExistingArea);
		
		btnNewArea = new Button(container, SWT.RADIO);
		btnNewArea.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnNewArea.setText("New area :");
		
		textNewArea = new Text(container, SWT.BORDER);
		textNewArea.setEnabled(false);
		var gdTextNewArea = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdTextNewArea.widthHint = 150;
		textNewArea.setLayoutData(gdTextNewArea);
		
		btnRenameArea = new Button(container, SWT.RADIO);
		btnRenameArea.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnRenameArea.setText("Rename area :");
		
		textRenameArea = new Text(container, SWT.BORDER);
		textRenameArea.setEnabled(false);
		var gdTextRenameArea = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gdTextRenameArea.widthHint = 150;
		textRenameArea.setLayoutData(gdTextRenameArea);		
		
		var label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		var composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(6, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		btnSymbolicSubarea = new Button(composite, SWT.RADIO);
		btnSymbolicSubarea.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnSymbolicSubarea.setText("Subarea :");
		
		textSymbolicSubarea = new Text(composite, SWT.BORDER);
		var gdTextSymbolicSubarea = new GridData(SWT.LEFT, SWT.CENTER, true, false, 5, 1);
		gdTextSymbolicSubarea.widthHint = 150;
		textSymbolicSubarea.setLayoutData(gdTextSymbolicSubarea);
		
		btnOffset = new Button(composite, SWT.RADIO);
		btnOffset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnOffset.setText("Offset :");
		
		textOffset = new Text(composite, SWT.BORDER | SWT.RIGHT);
		textOffset.setText("0");
		var gdTextOffset = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTextOffset.widthHint = 50;
		textOffset.setLayoutData(gdTextOffset);
		
		comboOffsetUnits = new Combo(composite, SWT.READ_ONLY);
		var gdComboOffsetUnits = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdComboOffsetUnits.widthHint = 100;
		comboOffsetUnits.setLayoutData(gdComboOffsetUnits);
		
		var lblNewLabel = new Label(composite, SWT.CENTER);
		var gdLblNewLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdLblNewLabel.widthHint = 25;
		lblNewLabel.setLayoutData(gdLblNewLabel);
		lblNewLabel.setText("for");
		
		textSize = new Text(composite, SWT.BORDER | SWT.RIGHT);
		textSize.setText("100");
		var gdTextSize = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTextSize.widthHint = 50;
		textSize.setLayoutData(gdTextSize);
		
		comboSizeUnits = new Combo(composite, SWT.READ_ONLY);
		var gdComboSizeUnits = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdComboSizeUnits.widthHint = 100;
		comboSizeUnits.setLayoutData(gdComboSizeUnits);

		comboOffsetUnits.add("pages");
		comboOffsetUnits.add("percent");
		comboOffsetUnits.select(0);
		
		comboSizeUnits.add("pages");
		comboSizeUnits.add("percent");
		comboSizeUnits.select(1);
		
		comboExistingArea.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}
		});
		textNewArea.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		textNewArea.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {								
				var selection = textNewArea.getSelection();
				var p = textNewArea.getText().toUpperCase();
				textNewArea.setText(p);
				textNewArea.setSelection(selection);
				enableAndDisable();				
			}
		});
		textRenameArea.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		textRenameArea.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				var selection = textRenameArea.getSelection();
				var p = textRenameArea.getText().toUpperCase();
				textRenameArea.setText(p);
				textRenameArea.setSelection(selection);
				enableAndDisable();
			}
		});
		textSymbolicSubarea.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		textSymbolicSubarea.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || e.keyCode == 16777296 || e.keyCode == SWT.ESC) { // enter + escape keys
					var p = textSymbolicSubarea.getText().toUpperCase();
					textSymbolicSubarea.setText(p);
					textSymbolicSubarea.setSelection(p.length());
					enableAndDisable();					
				}
			}
		});
		textOffset.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		textOffset.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || e.keyCode == 16777296 || e.keyCode == SWT.ESC) { // enter + escape keys
					enableAndDisable();					
				}
			}
		});
		comboOffsetUnits.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}
		});
		textSize.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		textSize.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || e.keyCode == 16777296 || e.keyCode == SWT.ESC) { // enter + escape keys
					enableAndDisable();					
				}
			}
		});
		comboSizeUnits.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}
		});
		initialize();
		return container;
	}

	private void enableAndDisable() {
		comboExistingArea.setEnabled(btnSelectArea.getSelection());
		textNewArea.setEnabled(btnNewArea.getSelection());
		textRenameArea.setEnabled(btnRenameArea.getSelection());
		textSymbolicSubarea.setEnabled(btnSymbolicSubarea.getSelection());
		textOffset.setEnabled(btnOffset.getSelection());
		comboOffsetUnits.setEnabled(btnOffset.getSelection());
		textSize.setEnabled(btnOffset.getSelection());
		comboSizeUnits.setEnabled(btnOffset.getSelection());
				
		var okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(checkAreaNameInCaseOfNewOrRenamedArea() && checkSymbolicsAndOtherData() && anythingChanged());
		if (!okButton.isEnabled()) {
			return;
		}
		
		// make sure we've got all information available should the user press the OK button
		action = getActionFromUI();
		areaName = getAreaNameFromUI();		
				
		symbolicSubareaName = getSymbolicSubareaNameFromUI();
		offsetPageCount = getOffsetPageCountFromUI();
		offsetPercent = getOffsetPercentFromUI();
		pageCount = getPageCountFromUI();
		percent = getPercentFromUI();
	}
	
	private boolean checkAreaNameInCaseOfNewOrRenamedArea() {
		// check the area name in the case of a new or renamed area; the area must not already exist
		if (btnSelectArea.getSelection()) {
			return true;
		} else if (btnNewArea.getSelection()) {
			var newAreaName = textNewArea.getText().trim();
			var validationResult = NamingConventions.validate(newAreaName, NamingConventions.Type.LOGICAL_AREA_NAME);
			if (validationResult.getStatus() == ValidationResult.Status.OK && schema.getArea(newAreaName) == null) {
				return true;
			}
		} else if (btnRenameArea.getSelection()) {
			var renamedAreaName = textRenameArea.getText().trim();
			var validationResult = NamingConventions.validate(renamedAreaName, NamingConventions.Type.LOGICAL_AREA_NAME);
			if (validationResult.getStatus() == ValidationResult.Status.OK && schema.getArea(renamedAreaName) == null) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkSymbolicsAndOtherData() {
		if (btnSymbolicSubarea.getSelection()) {
			var validationResult = NamingConventions.validate(textSymbolicSubarea.getText(), NamingConventions.Type.SYMBOLIC_DISPLACEMENT);
			return validationResult.getStatus() == ValidationResult.Status.OK;
		} else {
			// page count or percentage specified
			try {
				boolean b1;			
				if (comboOffsetUnits.getSelectionIndex() == 0) {
					// pages; must be >= 0
					var enteredOffsetPageCount = Integer.parseInt(textOffset.getText().trim());
					b1 = enteredOffsetPageCount > -1;
				} else {
					// percent; range: 0-100
					var enteredPercent = Short.parseShort(textOffset.getText().trim());
					b1 = enteredPercent > -1 && enteredPercent <= 100;
				}
				boolean b2;
				if (comboSizeUnits.getSelectionIndex() == 0) {
					// pages; must be >= 1
					var enteredOffsetPages = Integer.parseInt(textSize.getText().trim());
					b2 = enteredOffsetPages > 0;
				} else {
					// percent; range: 1-100
					var enteredPercent = Short.parseShort(textSize.getText().trim());
					b2 = enteredPercent > 0 && enteredPercent <= 100;
				}
				return b1 && b2;
			} catch (NumberFormatException e) {
				return false;
			}
		}
	}
	
	private boolean anythingChanged() {
		return areaNameChanged() || symbolicSubareaNameChanged() || offsetPageCountChanged() ||
			   offsetPercentChanged() || pageCountChanged() || percentChanged();		
	}
	
	private boolean areaNameChanged() {
		return !getAreaNameFromUI().equals(areaSpecification.getArea().getName());
	}
	
	private boolean symbolicSubareaNameChanged() {
		var oldSymbolicSubareaName = areaSpecification.getSymbolicSubareaName();
		var newSymbolicSubareaName = getSymbolicSubareaNameFromUI();
		return oldSymbolicSubareaName != null && !oldSymbolicSubareaName.equals(newSymbolicSubareaName) ||
			   newSymbolicSubareaName != null && !newSymbolicSubareaName.equals(oldSymbolicSubareaName);		
	}
	
	private boolean offsetPageCountChanged() {
		var oldOffsetExpression = areaSpecification.getOffsetExpression();
		var oldOffsetPageCount = oldOffsetExpression == null ? null : oldOffsetExpression.getOffsetPageCount();
		var newOffsetPageCount = getOffsetPageCountFromUI();
		return oldOffsetPageCount != null && !oldOffsetPageCount.equals(newOffsetPageCount) ||
			   newOffsetPageCount != null && !newOffsetPageCount.equals(oldOffsetPageCount);
		
	}
	
	private boolean offsetPercentChanged() {
		var oldOffsetExpression = areaSpecification.getOffsetExpression();
		var oldOffsetPercent = oldOffsetExpression == null ? null : oldOffsetExpression.getOffsetPercent();
		var newOffsetPercent = getOffsetPercentFromUI();
		return oldOffsetPercent != null && !oldOffsetPercent.equals(newOffsetPercent) ||
			   newOffsetPercent != null && !newOffsetPercent.equals(oldOffsetPercent);
	}
	
	private boolean pageCountChanged() {
		var oldOffsetExpression = areaSpecification.getOffsetExpression();
		var oldPageCount = oldOffsetExpression == null ? null : oldOffsetExpression.getPageCount();
		var newPageCount = getPageCountFromUI();
		return oldPageCount != null && !oldPageCount.equals(newPageCount) || newPageCount != null && !newPageCount.equals(oldPageCount);
	}
	
	private boolean percentChanged() {
		var oldOffsetExpression = areaSpecification.getOffsetExpression();
		var oldPercent = oldOffsetExpression == null ? null : oldOffsetExpression.getPercent();
		var newPercent = getPercentFromUI();
		return oldPercent != null && !oldPercent.equals(newPercent) || newPercent != null && !newPercent.equals(oldPercent);
	}
	
	public Action getAction() {
		return action;
	}

	private Action getActionFromUI() {
		if (btnSelectArea.getSelection()) {
			if (comboExistingArea.getText().equals(areaSpecification.getArea().getName())) {
				return Action.KEEP_IN_CURRENT_AREA;
			} else {
				return Action.MOVE_TO_OTHER_EXISTING_AREA;
			}			
		} else if (btnNewArea.getSelection()) {
			return Action.MOVE_TO_NEW_AREA;
		} else {
			return Action.RENAME_AREA;
		}
	}
	
	public String getAreaName() {
		return areaName;
	}

	private String getAreaNameFromUI() {
		if (btnSelectArea.getSelection()) {
			return comboExistingArea.getItem(comboExistingArea.getSelectionIndex());
		} else if (btnNewArea.getSelection()) {
			return textNewArea.getText().trim();
		} else {
			return textRenameArea.getText().trim();
		}
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(500, 300);
	}
	
	public Integer getOffsetPageCount() {
		return offsetPageCount;
	}

	private Integer	getOffsetPageCountFromUI() {
		if (btnOffset.getSelection() && comboOffsetUnits.getSelectionIndex() == 0 && !textOffset.getText().trim().equals("")) {
			return Integer.valueOf(textOffset.getText());
		} else {
			return null;
		}
	}
	
	public Short getOffsetPercent() {
		return offsetPercent;
	}

	private Short getOffsetPercentFromUI() {
		if (btnOffset.getSelection() && comboOffsetUnits.getSelectionIndex() == 1 && !textOffset.getText().trim().equals("")) {
			return Short.valueOf(textOffset.getText());
		} else {
			return null;
		}
	}
	
	public Integer getPageCount() {
		return pageCount;
	}

	private Integer	getPageCountFromUI() {
		if (btnOffset.getSelection() && comboSizeUnits.getSelectionIndex() == 0 && !textSize.getText().trim().equals("")) {
			return Integer.valueOf(textSize.getText());
		} else {
			return null;
		}
	}
	
	public Short getPercent() {
		return percent;
	}

	private Short getPercentFromUI() {
		if (btnOffset.getSelection() && comboSizeUnits.getSelectionIndex() == 1 && !textSize.getText().trim().equals("")) {
			return Short.valueOf(textSize.getText());
		} else {
			return null;
		}
	}
	
	public String getSymbolicSubareaName() {
		return symbolicSubareaName;
	}

	private String getSymbolicSubareaNameFromUI() {
		if (btnSymbolicSubarea.getSelection()) {
			return textSymbolicSubarea.getText().trim();
		} else {
			return null;
		}
	}
	
	private void initialize() {
		initializeComboExistingArea();
		textRenameArea.setText(areaSpecification.getArea().getName());
		if (areaSpecification.getSymbolicSubareaName() != null) {
			btnSymbolicSubarea.setSelection(true);
			textSymbolicSubarea.setText(areaSpecification.getSymbolicSubareaName());			
			textOffset.setEnabled(false);
			comboOffsetUnits.setEnabled(false);
			textSize.setEnabled(false);
			comboSizeUnits.setEnabled(false);			
		} else {
			btnOffset.setSelection(true);
			var offsetExpression = areaSpecification.getOffsetExpression();
			if (offsetExpression != null) {
				if (offsetExpression.getOffsetPageCount() != null) {
					textOffset.setText(String.valueOf(offsetExpression.getOffsetPageCount().intValue()));
					comboOffsetUnits.select(0);
				} else if (offsetExpression.getOffsetPercent() != null) {
					textOffset.setText(String.valueOf(offsetExpression.getOffsetPercent().shortValue()));
					comboOffsetUnits.select(1);
				}
				if (offsetExpression.getPageCount() != null) {
					textSize.setText(String.valueOf(offsetExpression.getPageCount().intValue()));
					comboSizeUnits.select(0);
				} else if (offsetExpression.getPercent() != null) {
					textSize.setText(String.valueOf(offsetExpression.getPercent().shortValue()));
					comboSizeUnits.select(1);
				}
			}
			textSymbolicSubarea.setEnabled(false);
		}
	}
	
	private void initializeComboExistingArea() {
		// fill the list of area names and select the current area - the 'Area' radio button is initially selected
		for (var area : schema.getAreas()) {
			// make sure the user cannot mix VSAM and non-VSAM items in an area
			var schemaRecord = areaSpecification.getRecord();
			if (schemaRecord != null) {
				if (Tools.areaMixesWithRecord(area, schemaRecord)) {				
					comboExistingArea.add(area.getName());	
				}
			} else if (Tools.canHoldSystemOwners(area)) {
				comboExistingArea.add(area.getName());				
			}
		}
		IntStream.range(0, comboExistingArea.getItemCount())
			.filter(i -> comboExistingArea.getItem(i).equals(areaSpecification.getArea().getName()))
			.limit(1L)
			.forEach(comboExistingArea::select);
	}
	
	public EnteredData getEnteredData() {
		return new EnteredData(getAction(), getAreaName(), getSymbolicSubareaName(), getOffsetPageCount(),
				getOffsetPercent(), getPageCount(), getPercent());
	}
	
	public record EnteredData(
			Action action,
			String areaName,
			String symbolicSubareaName,
			Integer offsetPageCount,
			Short offsetPercent,
			Integer pageCount,
			Short percent) {
	}
	
}
