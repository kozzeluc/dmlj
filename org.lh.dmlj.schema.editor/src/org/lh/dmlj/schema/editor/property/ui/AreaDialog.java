/**
 * Copyright (C) 2013  Luc Hermans
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
import org.lh.dmlj.schema.OffsetExpression;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;

public class AreaDialog extends Dialog {

	public enum Action 
		{KEEP_IN_CURRENT_AREA, MOVE_TO_NEW_AREA, MOVE_TO_OTHER_EXISTING_AREA, RENAME_AREA};
	
	private Action 			  action;
	private String			  areaName;
	private AreaSpecification areaSpecification;
	private Button 			  btnNewArea;
	private Button 			  btnOffset;
	private Button 			  btnRenameArea;
	private Button 			  btnSelectArea;
	private Button 			  btnSymbolicSubarea;
	private Combo 			  comboExistingArea;
	private Combo 			  comboOffsetUnits;
	private Combo 			  comboSizeUnits;
	private Integer			  offsetPageCount;
	private Short 			  offsetPercent;
	private Integer 		  pageCount;
	private Short 			  percent;
	private Schema 		      schema;
	private String			  symbolicSubareaName;
	private Text 			  textNewArea;
	private Text 			  textOffset;
	private Text 			  textRenameArea;
	private Text 			  textSize;
	private Text 			  textSymbolicSubarea;	
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AreaDialog(Shell parentShell, AreaSpecification areaSpecification) {
		super(parentShell);
		this.areaSpecification = areaSpecification;
		schema = areaSpecification.getArea().getSchema();
	}

	private boolean anythingChanged() {
		
		if (!getAreaNameFromUI().equals(areaSpecification.getArea().getName())) {
			return true;
		}
		
		String oldSymbolicSubareaName = 
			areaSpecification.getSymbolicSubareaName();
		String newSymbolicSubareaName = getSymbolicSubareaNameFromUI();
		
		// symbolic subarea
		if (oldSymbolicSubareaName != null &&
			!oldSymbolicSubareaName.equals(newSymbolicSubareaName) ||
			newSymbolicSubareaName != null &&
			!newSymbolicSubareaName.equals(oldSymbolicSubareaName)) {
			
			return true;
		}
		
		// get the current offset expression, if any
		OffsetExpression offsetExpression = 
			areaSpecification.getOffsetExpression();
		
		// offset page count
		Integer oldOffsetPageCount = offsetExpression == null ? null :
			offsetExpression.getOffsetPageCount();
		Integer newOffsetPageCount = getOffsetPageCountFromUI();
		if (oldOffsetPageCount != null &&
			!oldOffsetPageCount.equals(newOffsetPageCount) ||
			newOffsetPageCount != null &&
			!newOffsetPageCount.equals(oldOffsetPageCount)) {
				
			return true;
		}
		
		// offset percent
		Short oldOffsetPercent = offsetExpression == null ? null :
			offsetExpression.getOffsetPercent();
		Short newOffsetPercent = getOffsetPercentFromUI();
		if (oldOffsetPercent != null && 
			!oldOffsetPercent.equals(newOffsetPercent) ||
			newOffsetPercent != null &&
			!newOffsetPercent.equals(oldOffsetPercent)) {
				
			return true;
		}
		
		// page count
		Integer oldPageCount = offsetExpression == null ? null :
			offsetExpression.getPageCount();
		Integer newPageCount = getPageCountFromUI();
		if (oldPageCount != null && !oldPageCount.equals(newPageCount) ||
			newPageCount != null && !newPageCount.equals(oldPageCount)) {
				
			return true;
		}
		
		// percent
		Short oldPercent = offsetExpression == null ? null :
			offsetExpression.getPercent();
		Short newPercent = getPercentFromUI();
		if (oldPercent != null && !oldPercent.equals(newPercent) ||
			newPercent != null && !newPercent.equals(oldPercent)) {
				
			return true;
		}
		
		// nothing has changed
		return false;
		
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		String p;
		if (areaSpecification.getRecord() != null) {
			p = "record " + areaSpecification.getRecord().getName();
		} else if (areaSpecification.getSystemOwner() != null) {
			p = "system owner of set " + areaSpecification.getSystemOwner()
														  .getSet()
														  .getName();
		} else {
			String message = "logic error: neither record nor system owner";
			throw new RuntimeException(message);
		}
	    shell.setText("Edit area specification for " + p);	    
	}	
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		enableAndDisable();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
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
		GridData gd_comboExistingArea = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_comboExistingArea.widthHint = 136;
		comboExistingArea.setLayoutData(gd_comboExistingArea);
		
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
		GridData gd_textNewArea = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textNewArea.widthHint = 150;
		textNewArea.setLayoutData(gd_textNewArea);
		
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
		GridData gd_textRenameArea = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textRenameArea.widthHint = 150;
		textRenameArea.setLayoutData(gd_textRenameArea);		
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		Composite composite = new Composite(container, SWT.NONE);
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
		GridData gd_textSymbolicSubarea = new GridData(SWT.LEFT, SWT.CENTER, true, false, 5, 1);
		gd_textSymbolicSubarea.widthHint = 150;
		textSymbolicSubarea.setLayoutData(gd_textSymbolicSubarea);
		
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
		GridData gd_textOffset = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textOffset.widthHint = 50;
		textOffset.setLayoutData(gd_textOffset);
		
		comboOffsetUnits = new Combo(composite, SWT.READ_ONLY);
		GridData gd_comboOffsetUnits = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboOffsetUnits.widthHint = 40;
		comboOffsetUnits.setLayoutData(gd_comboOffsetUnits);
		
		Label lblNewLabel = new Label(composite, SWT.CENTER);
		GridData gd_lblNewLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 25;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("for");
		
		textSize = new Text(composite, SWT.BORDER | SWT.RIGHT);
		textSize.setText("100");
		GridData gd_textSize = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textSize.widthHint = 50;
		textSize.setLayoutData(gd_textSize);
		
		comboSizeUnits = new Combo(composite, SWT.READ_ONLY);
		GridData gd_comboSizeUnits = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboSizeUnits.widthHint = 40;
		comboSizeUnits.setLayoutData(gd_comboSizeUnits);

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
				Point selection = textNewArea.getSelection();
				String p = textNewArea.getText().toUpperCase();
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
				Point selection = textRenameArea.getSelection();
				String p = textRenameArea.getText().toUpperCase();
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
				if (e.keyCode == 13 || 		 // enter-key
					e.keyCode == 16777296 || // enter-key								
					e.keyCode == SWT.ESC) {	
					
					String p = textSymbolicSubarea.getText().toUpperCase();
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
				if (e.keyCode == 13 || 		 // enter-key
					e.keyCode == 16777296 || // enter-key								
					e.keyCode == SWT.ESC) {	
					
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
				if (e.keyCode == 13 || 		 // enter-key
					e.keyCode == 16777296 || // enter-key								
					e.keyCode == SWT.ESC) {	
					
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
		
		// OK button 
		Button okButton = getButton(IDialogConstants.OK_ID);		
		boolean enabled1 = false;
		
		// check the area name in the case of a new or renamed area; the area 
		// must not already exist
		if (btnSelectArea.getSelection()) {
			enabled1 = true;
		} else if (btnNewArea.getSelection()) {
			// new area
			String areaName = textNewArea.getText().trim();
			ValidationResult validationResult = 
				NamingConventions.validate(areaName, 
									   	   NamingConventions.Type
									   	   					.LOGICAL_AREA_NAME);
			if (validationResult.getStatus() == ValidationResult.Status.OK &&
				schema.getArea(areaName) == null) {			
				
				enabled1 = true;
			}
		} else if (btnRenameArea.getSelection()) {
			// area to be renamed
			String areaName = textRenameArea.getText().trim();
			ValidationResult validationResult = 
				NamingConventions.validate(areaName, 
									   	   NamingConventions.Type
									   	   					.LOGICAL_AREA_NAME);
			if (validationResult.getStatus() == ValidationResult.Status.OK &&
				schema.getArea(areaName) == null) {			
					
				enabled1 = true;
			}
		}
		
		textSymbolicSubarea.setEnabled(btnSymbolicSubarea.getSelection());
		
		textOffset.setEnabled(btnOffset.getSelection());
		comboOffsetUnits.setEnabled(btnOffset.getSelection());
		textSize.setEnabled(btnOffset.getSelection());
		comboSizeUnits.setEnabled(btnOffset.getSelection());		
		
		boolean enabled2 = false;
		if (btnSymbolicSubarea.getSelection()) {
			// symbolic subarea selected
			String symbolicSubareaName = textSymbolicSubarea.getText();
			ValidationResult validationResult = 
				NamingConventions.validate(symbolicSubareaName, 
										   NamingConventions.Type.SYMBOLIC_DISPLACEMENT);
			if (validationResult.getStatus() == ValidationResult.Status.OK) {
				enabled2 = true;
			}			
		} else {
			// page count or percentage specified
			try {
				boolean b1 = false;			
				if (comboOffsetUnits.getSelectionIndex() == 0) {
					// pages; must be >= 0
					int offsetPageCount = 
						Integer.valueOf(textOffset.getText().trim()).intValue();
					if (offsetPageCount > -1) {
						b1 = true;
					}
				} else {
					// percent; range: 0-100
					short percent = 
						Short.valueOf(textOffset.getText().trim()).shortValue();
					if (percent > -1 && percent <= 100) {
						b1 = true;
					}
				}
				boolean b2 = false;	
				if (comboSizeUnits.getSelectionIndex() == 0) {
					// pages; must be >= 1
					int offsetPages = 
						Integer.valueOf(textSize.getText().trim()).intValue();
					if (offsetPages > 0) {
						b2 = true;
					}
				} else {
					// percent; range: 1-100
					short percent = 
						Short.valueOf(textSize.getText().trim()).shortValue();
					if (percent > 0 && percent <= 100) {
						b2 = true;
					}
				}
				enabled2 = b1 && b2;
			} catch (NumberFormatException e) {				
			}
		}		
		
		boolean enabled = enabled1 && enabled2;
		if (enabled) {
			enabled = anythingChanged();
		}
		okButton.setEnabled(enabled);
		
		if (!enabled) {
			return;
		}
		
		// make sure we've got all information available should the user press
		// the OK button	
		action = getActionFromUI();
		areaName = getAreaNameFromUI();		
				
		symbolicSubareaName = getSymbolicSubareaNameFromUI();
		offsetPageCount = getOffsetPageCountFromUI();
		offsetPercent = getOffsetPercentFromUI();
		pageCount = getPageCountFromUI();
		percent = getPercentFromUI();
		
	}
	
	public Action getAction() {
		return action;
	}

	private Action getActionFromUI() {
		if (btnSelectArea.getSelection()) {
			String areaName = areaSpecification.getArea().getName();
			if (comboExistingArea.getText().equals(areaName)) {
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
			int i = comboExistingArea.getSelectionIndex();
			return comboExistingArea.getItem(i);
		} else if (btnNewArea.getSelection()) {
			return textNewArea.getText().trim();
		} else {
			return textRenameArea.getText().trim();
		}
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	public Integer getOffsetPageCount() {
		return offsetPageCount;
	}

	private Integer	getOffsetPageCountFromUI() {
		if (btnOffset.getSelection() && 
			comboOffsetUnits.getSelectionIndex() == 0 &&
			!textOffset.getText().trim().equals("")) {
			
			return Integer.valueOf(textOffset.getText());
		} else {
			return null;
		}
	}
	
	public Short getOffsetPercent() {
		return offsetPercent;
	}

	private Short getOffsetPercentFromUI() {
		if (btnOffset.getSelection() && 
			comboOffsetUnits.getSelectionIndex() == 1 &&
			!textOffset.getText().trim().equals("")) {
			
			return Short.valueOf(textOffset.getText());
		} else {
			return null;
		}
	}
	
	public Integer getPageCount() {
		return pageCount;
	}

	private Integer	getPageCountFromUI() {
		if (btnOffset.getSelection() && 
			comboSizeUnits.getSelectionIndex() == 0 &&
			!textSize.getText().trim().equals("")) {
			
			return Integer.valueOf(textSize.getText());
		} else {
			return null;
		}
	}
	
	public Short getPercent() {
		return percent;
	}

	private Short getPercentFromUI() {
		if (btnOffset.getSelection() && 
			comboSizeUnits.getSelectionIndex() == 1 &&
			!textSize.getText().trim().equals("")) {
			
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
		
		// fill the list of area names and select the current area - the 'Area'
		// radio button is initially selected
		for (SchemaArea area : schema.getAreas()) {
			comboExistingArea.add(area.getName());
		}
		String areaName = areaSpecification.getArea().getName();
		for (int i = 0; i < comboExistingArea.getItemCount(); i++) {
			if (comboExistingArea.getItem(i).equals(areaName)) {
				comboExistingArea.select(i);
				break;
			}
		}
		// copy the area name to the rename area text control
		textRenameArea.setText(areaName);
		
		if (areaSpecification.getSymbolicSubareaName() != null) {
			btnSymbolicSubarea.setSelection(true);
			textSymbolicSubarea.setText(areaSpecification.getSymbolicSubareaName());			
			textOffset.setEnabled(false);
			comboOffsetUnits.setEnabled(false);
			textSize.setEnabled(false);
			comboSizeUnits.setEnabled(false);			
		} else {
			btnOffset.setSelection(true);
			OffsetExpression offsetExpression = 
				areaSpecification.getOffsetExpression();
			if (offsetExpression != null) {
				if (offsetExpression.getOffsetPageCount() != null) {
					int offsetPageCount = 
						offsetExpression.getOffsetPageCount().intValue();
					textOffset.setText(String.valueOf(offsetPageCount));
					comboOffsetUnits.select(0);
				} else if (offsetExpression.getOffsetPercent() != null) {
					short offsetPercent = 
						offsetExpression.getOffsetPercent().shortValue();
					textOffset.setText(String.valueOf(offsetPercent));
					comboOffsetUnits.select(1);
				}
				if (offsetExpression.getPageCount() != null) {
					int pageCount = offsetExpression.getPageCount().intValue();
					textSize.setText(String.valueOf(pageCount));
					comboSizeUnits.select(0);
				} else if (offsetExpression.getPercent() != null) {
					short percent = offsetExpression.getPercent().shortValue();
					textSize.setText(String.valueOf(percent));
					comboSizeUnits.select(1);
				}
			}
			textSymbolicSubarea.setEnabled(false);
		}
		
	}
	
}
