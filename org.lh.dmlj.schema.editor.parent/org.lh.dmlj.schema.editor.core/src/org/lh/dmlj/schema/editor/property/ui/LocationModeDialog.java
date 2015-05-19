/**
 * Copyright (C) 2015  Luc Hermans
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

import java.util.ArrayList;
import java.util.Arrays;

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
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.ViaSpecification;
import org.lh.dmlj.schema.editor.command.ILocationModeDetailsProvider;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;

public class LocationModeDialog extends Dialog implements ILocationModeDetailsProvider {

	private java.util.List<Element> availableCalcElements = new ArrayList<>();
	private java.util.List<Element> availableVsamCalcElements = new ArrayList<>();
	private Button 		 		    btnAddCalcElements;
	private Button 		 		    btnCalc;
	private Button 		 			btnDirect;
	private Button 		 			btnDisplacementPages;
	private Button 		 			btnMoveCalcElementDown;
	private Button 		 			btnMoveCalcElementUp;
	private Button 		 			btnNoDisplacement;
	private Button 		 			btnRemoveCalcElements;
	private Button 		 			btnSymbolicDisplacement;
	private Button 		 			btnVia;
	private java.util.List<Element> calcElements = new ArrayList<>();
	private java.util.List<Element> vsamCalcElements = new ArrayList<>();
	private Combo 		 			comboCalcDuplicatesOption;
	private Combo 		 			comboViaSet;
	private Short					displacementPageCount;
	private DuplicatesOption		calcDuplicatesOption;
	private DuplicatesOption		vsamCalcDuplicatesOption;
	private List 		 			listAvailableCalcElements;
	private List 		 			listCalcElements;
	private LocationMode			locationMode;
	private SchemaRecord 			record;
	private String 					symbolicDisplacementName;
	private Text   		 			textDisplacementPages;
	private Text 		 			textSymbolicDisplacement;
	private String				    viaSetName;
	private Button btnVsam;
	private Button btnVsamCalc;
	private Label lblNewLabel;
	private Label lblNewLabel_1;
	private List listAvailableVsamCalcElements;
	private List listVsamCalcElements;
	private Button btnAddVsamCalcElements;
	private Button btnRemoveVsamCalcElements;
	private Label lblVsamCalcDuplicates;
	private Combo comboVsamCalcDuplicatesOption;
	private Button btnMoveVsamCalcElementUp;
	private Button btnMoveVsamCalcElementDown;
	private Label lblCalcDuplicates;
	
	private static boolean isOccursInvolved(Element element) {		
		for (Element current = element; current != null;
			 current = current.getParent()) {
			
			if (current.getOccursSpecification() != null) {
				return true;
			}
		}
		return false;
	}	
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public LocationModeDialog(Shell parentShell, SchemaRecord record) {
		super(parentShell);
		this.record = record;
	}	
	
	protected void addCalcElements() {
		
		int[] i = listAvailableCalcElements.getSelectionIndices();
		Arrays.sort(i);
		
		java.util.List<Element> tmpList = new ArrayList<>();
		int k = listCalcElements.getItemCount();
		for (int j = 0; j < i.length; j++) {
			Element element = availableCalcElements.get(i[j]);
			calcElements.add(element);
			listCalcElements.add(element.getName());
			tmpList.add(element);
		}	
		int[] m = new int[i.length];
		for (int n = 0; n < m.length; n++) {
			m[n] = k + n;
		}
		listCalcElements.deselectAll();
		listCalcElements.select(m);
		
		availableCalcElements.removeAll(tmpList);
		listAvailableCalcElements.remove(i);
		
		enableAndDisable();
		
	}
	
	protected void addVsamCalcElements() {
		
		int[] i = listAvailableVsamCalcElements.getSelectionIndices();
		Arrays.sort(i);
		
		java.util.List<Element> tmpList = new ArrayList<>();
		int k = listVsamCalcElements.getItemCount();
		for (int j = 0; j < i.length; j++) {
			Element element = availableVsamCalcElements.get(i[j]);
			vsamCalcElements.add(element);
			listVsamCalcElements.add(element.getName());
			tmpList.add(element);
		}	
		int[] m = new int[i.length];
		for (int n = 0; n < m.length; n++) {
			m[n] = k + n;
		}
		listVsamCalcElements.deselectAll();
		listVsamCalcElements.select(m);
		
		availableVsamCalcElements.removeAll(tmpList);
		listAvailableVsamCalcElements.remove(i);
		
		enableAndDisable();
		
	}

	/**
	 * Checks whether the user has changed anything to the record's location 
	 * mode data.
	 * @return true if anything has changed to the record's location mode, false
	 *         if not
	 */
	private boolean anythingChanged() {
		
		// did the location mode itself change ?
		if (btnCalc.getSelection() && record.getLocationMode() != LocationMode.CALC ||
			btnDirect.getSelection() && record.getLocationMode() != LocationMode.DIRECT ||
			btnVia.getSelection() && record.getLocationMode() != LocationMode.VIA ||
			btnVsam.getSelection() && record.getLocationMode() != LocationMode.VSAM ||
			btnVsamCalc.getSelection() && record.getLocationMode() != LocationMode.VSAM_CALC) {
			
			return true;
		}
		
		// if the location mode is DIRECT or VSAM, there's nothing else that can be changed
		if (record.getLocationMode() == LocationMode.DIRECT ||
			record.getLocationMode() == LocationMode.VSAM) {
			
			return false;
		}
		
		// separate checks for CALC, VSAM CALC and VIA
		if (record.getLocationMode() == LocationMode.CALC) {
			// CALC record, check the CALC key elements			
			if (calcElements.size() != record.getCalcKey().getElements().size()) {
				// the number of elements in the CALC key has changed
				return true;
			}
			for (int i = 0; i < calcElements.size(); i++) {
				Element modelElement = 
					record.getCalcKey().getElements().get(i).getElement();
				Element dialogElement = calcElements.get(i);
				if (dialogElement != modelElement) {
					// element mismatch
					return true;
				}
			}
			// check the duplicates option
			String modelDuplicatesOption = record.getCalcKey()
					  							 .getDuplicatesOption()
					  							 .toString()
					  							 .replaceAll("_", " ");
			if (!comboCalcDuplicatesOption.getText().equals(modelDuplicatesOption)) {
				// duplicates option changed
				return true;
			}
		} else if (record.getLocationMode() == LocationMode.VIA) {
			// VIA record, check the VIA set first
			String modelSetName = 
				record.getViaSpecification().getSet().getName();
			String dialogSetName = comboViaSet.getText();
			if (!dialogSetName.equals(modelSetName)) {
				// VIA set changed
				return true;
			}
			// check the displacement specification
			ViaSpecification via = record.getViaSpecification();
			if (btnNoDisplacement.getSelection() &&
				(via.getSymbolicDisplacementName() != null || 
				 via.getDisplacementPageCount() != null)) {
				 
				// user has removed symbolic displacement name or displacement
				// pages
				return true;
			} else if (btnSymbolicDisplacement.getSelection() &&
					   (via.getSymbolicDisplacementName() == null ||
					    via.getDisplacementPageCount() != null)) {
				
				// user has specified that a symbolic displacement name has to
				// be used
				return true;
			} else if (btnSymbolicDisplacement.getSelection() &&
					   !textSymbolicDisplacement.getText()
					   							.trim()
					   							.equals(via.getSymbolicDisplacementName())) {
			
				// user has modified the symbolic displacement name
				return true;
			} else if (btnDisplacementPages.getSelection() &&
					   (via.getSymbolicDisplacementName() != null ||
					    via.getDisplacementPageCount() == null)) {
				
				// user has specified a displacement page count
				return true;
			} else if (btnDisplacementPages.getSelection() &&
					   Short.valueOf(textDisplacementPages.getText().trim())
							.shortValue() != via.getDisplacementPageCount()
												.shortValue()) {
				
				// user has changed the displacement page count
				return true;
			}
		} else {
			// VSAM CALC record, check the VSAM CALC key elements			
			if (vsamCalcElements.size() != record.getCalcKey().getElements().size()) {
				// the number of elements in the VSAM CALC key has changed
				return true;
			}
			for (int i = 0; i < vsamCalcElements.size(); i++) {
				Element modelElement = record.getCalcKey().getElements().get(i).getElement();
				Element dialogElement = vsamCalcElements.get(i);
				if (dialogElement != modelElement) {
					// element mismatch
					return true;
				}
			}
			// check the duplicates option
			String modelDuplicatesOption = record.getCalcKey()
					  							 .getDuplicatesOption()
					  							 .toString()
					  							 .replaceAll("_", " ");
			if (!comboVsamCalcDuplicatesOption.getText().equals(modelDuplicatesOption)) {
				// duplicates option changed
				return true;
			}
		}
		// if we get here, no changes were entered
		return false;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
	    shell.setText("Edit location mode specification for " + 
	    			  record.getName());	    
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
		gridLayout.numColumns = 5;
		
		btnCalc = new Button(container, SWT.RADIO);
		btnCalc.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		btnCalc.setText("CALC using :");
		new Label(container, SWT.NONE);
		
		listAvailableCalcElements = new List(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		listAvailableCalcElements.setToolTipText("Available elements");
		GridData gd_listAvailableElements = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 2, 2);
		gd_listAvailableElements.heightHint = 75;
		gd_listAvailableElements.widthHint = 150;
		gd_listAvailableElements.minimumWidth = -1;
		gd_listAvailableElements.horizontalIndent = 15;
		listAvailableCalcElements.setLayoutData(gd_listAvailableElements);
		
		btnAddCalcElements = new Button(container, SWT.NONE);
		btnAddCalcElements.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		btnAddCalcElements.setText(">");
		
		listCalcElements = new List(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		listCalcElements.setToolTipText("CALC key elements");
		GridData gd_listCalcElements = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 2);
		gd_listCalcElements.heightHint = 75;
		gd_listCalcElements.widthHint = 150;
		gd_listCalcElements.minimumWidth = 100;
		listCalcElements.setLayoutData(gd_listCalcElements);
		
		btnMoveCalcElementUp = new Button(container, SWT.NONE);
		GridData gd_btnMoveCalcElementUp = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gd_btnMoveCalcElementUp.widthHint = 40;
		btnMoveCalcElementUp.setLayoutData(gd_btnMoveCalcElementUp);
		btnMoveCalcElementUp.setText("Up");
		
		btnRemoveCalcElements = new Button(container, SWT.NONE);
		btnRemoveCalcElements.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemoveCalcElements.setText("<");
		
		btnMoveCalcElementDown = new Button(container, SWT.NONE);
		GridData gd_btnMoveCalcElementDown = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnMoveCalcElementDown.widthHint = 40;
		btnMoveCalcElementDown.setLayoutData(gd_btnMoveCalcElementDown);
		btnMoveCalcElementDown.setText("Down");
		
		lblCalcDuplicates = new Label(container, SWT.NONE);
		lblCalcDuplicates.setEnabled(false);
		GridData gd_lblDuplicates = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDuplicates.verticalIndent = 5;
		gd_lblDuplicates.horizontalIndent = 15;
		lblCalcDuplicates.setLayoutData(gd_lblDuplicates);
		lblCalcDuplicates.setText("Duplicates :");
		
		comboCalcDuplicatesOption = new Combo(container, SWT.READ_ONLY);
		GridData gd_comboCalcDuplicatesOption = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_comboCalcDuplicatesOption.verticalIndent = 5;
		gd_comboCalcDuplicatesOption.widthHint = 100;
		comboCalcDuplicatesOption.setLayoutData(gd_comboCalcDuplicatesOption);
		
		Label label_1 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gd_label_1.verticalIndent = 10;
		gd_label_1.horizontalIndent = 15;
		label_1.setLayoutData(gd_label_1);
		
		btnDirect = new Button(container, SWT.RADIO);
		btnDirect.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		btnDirect.setText("DIRECT");
		new Label(container, SWT.NONE);
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gd_label.verticalIndent = 10;
		gd_label.horizontalIndent = 15;
		label.setLayoutData(gd_label);
		
		btnVia = new Button(container, SWT.RADIO);
		btnVia.setText("VIA set :");
		
		comboViaSet = new Combo(container, SWT.READ_ONLY);
		GridData gd_comboViaSet = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_comboViaSet.widthHint = 100;
		comboViaSet.setLayoutData(gd_comboViaSet);
		
		Label lblDisplacement = new Label(container, SWT.NONE);
		lblDisplacement.setEnabled(false);
		lblDisplacement.setAlignment(SWT.RIGHT);
		GridData gd_lblDisplacement = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblDisplacement.verticalIndent = 5;
		gd_lblDisplacement.horizontalIndent = 15;
		lblDisplacement.setLayoutData(gd_lblDisplacement);
		lblDisplacement.setText("Displacement :");
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		
		btnNoDisplacement = new Button(composite, SWT.RADIO);
		btnNoDisplacement.setText("None");
		new Label(composite, SWT.NONE);
		
		btnSymbolicDisplacement = new Button(composite, SWT.RADIO);
		btnSymbolicDisplacement.setText("USING symbolic displacement :");
		
		textSymbolicDisplacement = new Text(composite, SWT.BORDER);
		GridData gd_textSymbolicDisplacement = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textSymbolicDisplacement.widthHint = 150;
		textSymbolicDisplacement.setLayoutData(gd_textSymbolicDisplacement);
		
		btnDisplacementPages = new Button(composite, SWT.RADIO);
		btnDisplacementPages.setText("Pages :");
		
		textDisplacementPages = new Text(composite, SWT.BORDER | SWT.RIGHT);
		GridData gd_textDisplacementPages = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textDisplacementPages.widthHint = 50;
		textDisplacementPages.setLayoutData(gd_textDisplacementPages);
		
		lblNewLabel = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_lblNewLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gd_lblNewLabel.horizontalIndent = 15;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("New Label");
		
		btnVsam = new Button(container, SWT.RADIO);
		btnVsam.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnVsam.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		btnVsam.setText("VSAM");
		new Label(container, SWT.NONE);
		
		lblNewLabel_1 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_lblNewLabel_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gd_lblNewLabel_1.horizontalIndent = 15;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		lblNewLabel_1.setText("New Label");
		
		btnVsamCalc = new Button(container, SWT.RADIO);
		btnVsamCalc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnVsamCalc.setText("VSAM CALC");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		listAvailableVsamCalcElements = new List(container, SWT.BORDER);
		listAvailableVsamCalcElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		listAvailableVsamCalcElements.setEnabled(false);
		GridData gd_listAvailableVsamCalcElements = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2);
		gd_listAvailableVsamCalcElements.heightHint = 75;
		gd_listAvailableVsamCalcElements.horizontalIndent = 15;
		listAvailableVsamCalcElements.setLayoutData(gd_listAvailableVsamCalcElements);
		
		btnAddVsamCalcElements = new Button(container, SWT.NONE);
		btnAddVsamCalcElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addVsamCalcElements();
			}
		});
		btnAddVsamCalcElements.setEnabled(false);
		btnAddVsamCalcElements.setText(">");
		
		listVsamCalcElements = new List(container, SWT.BORDER);
		listVsamCalcElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		listVsamCalcElements.setEnabled(false);
		listVsamCalcElements.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
		
		btnMoveVsamCalcElementUp = new Button(container, SWT.NONE);
		btnMoveVsamCalcElementUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveVsamCalcElementUp();
			}
		});
		btnMoveVsamCalcElementUp.setEnabled(false);
		GridData gd_btnMoveVsamCalcElementUp = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnMoveVsamCalcElementUp.widthHint = 40;
		btnMoveVsamCalcElementUp.setLayoutData(gd_btnMoveVsamCalcElementUp);
		btnMoveVsamCalcElementUp.setText("Up");
		
		btnRemoveVsamCalcElements = new Button(container, SWT.NONE);
		btnRemoveVsamCalcElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeVsamCalcElements();
			}
		});
		btnRemoveVsamCalcElements.setEnabled(false);
		btnRemoveVsamCalcElements.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemoveVsamCalcElements.setText("<");
		
		btnMoveVsamCalcElementDown = new Button(container, SWT.NONE);
		btnMoveVsamCalcElementDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveVsamCalcElementDown();	
			}
		});
		btnMoveVsamCalcElementDown.setEnabled(false);
		GridData gd_btnMoveVsamCalcElementDown = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnMoveVsamCalcElementDown.widthHint = 40;
		btnMoveVsamCalcElementDown.setLayoutData(gd_btnMoveVsamCalcElementDown);
		btnMoveVsamCalcElementDown.setText("Down");
		
		lblVsamCalcDuplicates = new Label(container, SWT.NONE);
		lblVsamCalcDuplicates.setEnabled(false);
		GridData gd_lblVsamCalcDuplicates = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblVsamCalcDuplicates.verticalIndent = 5;
		gd_lblVsamCalcDuplicates.horizontalIndent = 15;
		lblVsamCalcDuplicates.setLayoutData(gd_lblVsamCalcDuplicates);
		lblVsamCalcDuplicates.setText("Duplicates :");
		
		comboVsamCalcDuplicatesOption = new Combo(container, SWT.READ_ONLY);
		comboVsamCalcDuplicatesOption.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		comboVsamCalcDuplicatesOption.setEnabled(false);
		GridData gd_comboVsamCalcDuplicatesOption = new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1);
		gd_comboVsamCalcDuplicatesOption.verticalIndent = 5;
		gd_comboVsamCalcDuplicatesOption.widthHint = 100;
		comboVsamCalcDuplicatesOption.setLayoutData(gd_comboVsamCalcDuplicatesOption);

		btnCalc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		
		btnDirect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		
		btnVia.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		
		listAvailableCalcElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		
		listCalcElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		
		btnAddCalcElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addCalcElements();				
			}						
		});
		
		btnRemoveCalcElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeCalcElements();				
			}						
		});
		
		btnMoveCalcElementUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveCalcElementUp();				
			}						
		});
		
		btnMoveCalcElementDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveCalcElementDown();				
			}						
		});
		
		comboCalcDuplicatesOption.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}						
		});
		
		comboViaSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}						
		});
			
		btnNoDisplacement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}						
		});
		
		btnSymbolicDisplacement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}						
		});
		
		btnDisplacementPages.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();				
			}						
		});
		
		textSymbolicDisplacement.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				enableAndDisable();				
			}
		});
		
		textSymbolicDisplacement.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || 		 // enter-key
					e.keyCode == 16777296 || // enter-key								
					e.keyCode == SWT.ESC) {	
					
					String p = textSymbolicDisplacement.getText().toUpperCase();
					textSymbolicDisplacement.setText(p);
					textSymbolicDisplacement.setSelection(p.length());
					enableAndDisable();					
				}
			}
		});
		
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
		
		initialize();
		
		return container;
	}

	private void enableAndDisable() {
		
		// Note: the location mode radiobuttons are enabled/disabled during initialization (see
		// initialize() method); once enabled or disabled, this is fixed for the lifetime of the 
		// dialog) 
		
		// CALC stuff
		listAvailableCalcElements.setEnabled(btnCalc.getSelection());
		listCalcElements.setEnabled(btnCalc.getSelection());
		btnAddCalcElements.setEnabled(btnCalc.getSelection() &&
									  listAvailableCalcElements.getSelectionCount() > 0 &&
									  getComputedCalcKeyLength() <= 256);
		btnRemoveCalcElements.setEnabled(btnCalc.getSelection() &&
				 					 	 listCalcElements.getSelectionCount() > 0);
		btnMoveCalcElementUp.setEnabled(btnCalc.getSelection() &&
				 						listCalcElements.getSelectionCount() == 1 &&
				 						listCalcElements.getSelectionIndex() > 0);
		btnMoveCalcElementDown.setEnabled(btnCalc.getSelection() &&
										  listCalcElements.getSelectionCount() == 1 &&
					 					  listCalcElements.getSelectionIndex() < listCalcElements.getItemCount() - 1);
		lblCalcDuplicates.setEnabled(btnCalc.getSelection());
		comboCalcDuplicatesOption.setEnabled(btnCalc.getSelection());
		
		// VIA stuff
		comboViaSet.setEnabled(btnVia.getSelection());
		btnNoDisplacement.setEnabled(btnVia.getSelection());
		btnSymbolicDisplacement.setEnabled(btnVia.getSelection());
		btnDisplacementPages.setEnabled(btnVia.getSelection());
		textSymbolicDisplacement.setEnabled(btnVia.getSelection() &&
											btnSymbolicDisplacement.getSelection());
		textDisplacementPages.setEnabled(btnVia.getSelection() &&
										 btnDisplacementPages.getSelection());
		
		// VSAM CALC stuff
		listAvailableVsamCalcElements.setEnabled(btnVsamCalc.getSelection());
		listVsamCalcElements.setEnabled(btnVsamCalc.getSelection());
		btnAddVsamCalcElements.setEnabled(btnVsamCalc.getSelection() &&
									  	  listAvailableVsamCalcElements.getSelectionCount() > 0 &&
									  	  getComputedCalcKeyLength() <= 256);
		btnRemoveVsamCalcElements.setEnabled(btnVsamCalc.getSelection() &&
				 					 	 	 listVsamCalcElements.getSelectionCount() > 0);
		btnMoveVsamCalcElementUp.setEnabled(btnVsamCalc.getSelection() &&
				 							listVsamCalcElements.getSelectionCount() == 1 &&
				 							listVsamCalcElements.getSelectionIndex() > 0);
		btnMoveVsamCalcElementDown.setEnabled(btnVsamCalc.getSelection() &&
										  	  listVsamCalcElements.getSelectionCount() == 1 &&
										  	  listVsamCalcElements.getSelectionIndex() < listVsamCalcElements.getItemCount() - 1);
		lblVsamCalcDuplicates.setEnabled(btnVsamCalc.getSelection());
		comboVsamCalcDuplicatesOption.setEnabled(btnVsamCalc.getSelection());
		
		// OK button 
		Button okButton = getButton(IDialogConstants.OK_ID);		
		boolean enabled = false;
		if (btnCalc.getSelection()) {
			// CALC radio button selected; check if we have at least 1 CALC
			// key element and a value for the duplicates option
			enabled = !calcElements.isEmpty() && 
					  comboCalcDuplicatesOption.getSelectionIndex() > -1;
		} else if (btnDirect.getSelection()) {
			// DIRECT radio button selected; nothing needed
			enabled = true;
		} else if (btnVia.getSelection()) {
			// VIA radio button selected; check if we have a set and, if
			// specified, if the displacement specification is valid
			if (comboViaSet.getSelectionIndex() > -1) {
				if (btnSymbolicDisplacement.getSelection()) {
					ValidationResult validationResult = 
						NamingConventions.validate(textSymbolicDisplacement.getText(), 
												   NamingConventions.Type
												   .SYMBOLIC_DISPLACEMENT);
					if (validationResult.getStatus() == ValidationResult.Status.OK) {
						enabled = true;
					}
				} else if (btnDisplacementPages.getSelection()) {						
					try {
						// get the new displacement page count
						Short i = Short.valueOf(textDisplacementPages.getText())
								 	   .shortValue();
						// must be an unsigned integer in the range 0 through 
						// 32,767
						if (i > 0) {
							enabled = true;
						}
					} catch (NumberFormatException e) {							
					}
				} else {
					enabled = true;
				}
			}
		} else if (btnVsam.getSelection()) {
			// VSAM radio button selected; nothing needed
			enabled = true;
		} else if (btnVsamCalc.getSelection()) {
			// VSAM CALC radio button selected; check if we have at least 1 VSAM CALC key element 
			// and a value for the duplicates option
			enabled = !vsamCalcElements.isEmpty() && 
					  comboVsamCalcDuplicatesOption.getSelectionIndex() > -1;
		}
		if (enabled) {
			enabled = anythingChanged();
		}
		okButton.setEnabled(enabled);
		
		if (!enabled) {
			return;
		}
		
		// make sure we've got all information available should the user press
		// the OK button
		calcDuplicatesOption = null;
		vsamCalcDuplicatesOption = null;
		viaSetName = null;
		symbolicDisplacementName = null;
		displacementPageCount = null;
		if (btnCalc.getSelection()) {
			locationMode = LocationMode.CALC;
			if (comboCalcDuplicatesOption.getSelectionIndex() > -1) {
				for (DuplicatesOption aDuplicatesOption : DuplicatesOption.VALUES) {
					if (aDuplicatesOption.toString()
										 .replaceAll("_", " ")
										 .equals(comboCalcDuplicatesOption.getText())) {
						
						calcDuplicatesOption = aDuplicatesOption;
						break;
					}
				}
			}
		} else if (btnDirect.getSelection()) {
			locationMode = LocationMode.DIRECT;
		} else if (btnVia.getSelection()) {
			locationMode = LocationMode.VIA;
			viaSetName = comboViaSet.getText();
			if (btnSymbolicDisplacement.getSelection()) {
				symbolicDisplacementName = 
					textSymbolicDisplacement.getText().trim();
			} else if (btnDisplacementPages.getSelection()) {
				displacementPageCount = 
					Short.valueOf(textDisplacementPages.getText().trim());
			}
		} else if (btnVsam.getSelection()) {
			locationMode = LocationMode.VSAM;
		} else if (btnCalc.getSelection()) {
			locationMode = LocationMode.VSAM_CALC;
			if (comboVsamCalcDuplicatesOption.getSelectionIndex() > -1) {
				for (DuplicatesOption aDuplicatesOption : DuplicatesOption.VALUES) {
					if (aDuplicatesOption.toString()
										 .replaceAll("_", " ")
										 .equals(comboVsamCalcDuplicatesOption.getText())) {
						
						vsamCalcDuplicatesOption = aDuplicatesOption;
						break;
					}
				}
			}
		}		
		
	}

	private void enableAndDisableLocationModeRadioButtons() {
		boolean hasAtLeastOneKeyElementCandidate = !availableCalcElements.isEmpty();
		btnCalc.setEnabled(record.isCalc() || 
						   record.isDirect() && hasAtLeastOneKeyElementCandidate ||
						   record.isVia() && hasAtLeastOneKeyElementCandidate ||
						   record.isVsam() && hasAtLeastOneKeyElementCandidate && record.getMemberRoles().isEmpty() ||
						   record.isVsamCalc() && record.getMemberRoles().isEmpty());
		btnDirect.setEnabled(record.isCalc() ||
							 record.isDirect() ||
							 record.isVia() ||
							 record.isVsam() && record.getMemberRoles().isEmpty() ||
							 record.isVsamCalc() && record.getMemberRoles().isEmpty());
		btnVia.setEnabled(record.isCalc() && !record.getMemberRoles().isEmpty() ||
						  record.isDirect() && !record.getMemberRoles().isEmpty() ||
						  record.isVia());
		btnVsam.setEnabled(record.isCalc() && record.getMemberRoles().isEmpty() ||
				 		   record.isDirect() && record.getMemberRoles().isEmpty() ||
				 		   record.isVsam() ||
				 		   record.isVsamCalc());
		btnVsamCalc.setEnabled(record.isCalc() && record.getMemberRoles().isEmpty() ||
		 		   		   	   record.isDirect() && hasAtLeastOneKeyElementCandidate && record.getMemberRoles().isEmpty() ||
		 		   		   	   record.isVsam() && hasAtLeastOneKeyElementCandidate ||
		 		   		   	   record.isVsamCalc());
	}

	@Override
	public java.util.List<Element> getCalcKeyElements() {
		if (locationMode == LocationMode.CALC) {
			return new ArrayList<>(calcElements);
		} else if (locationMode == LocationMode.VSAM_CALC) {
			return new ArrayList<>(vsamCalcElements);
		} else {
			return null;
		}
	}
	
	private int getComputedCalcKeyLength() {
		// compute the length of the CALC key given the CALC key elements in the
		// right list and all selected elements in the left list
		int i = 0;
		for (Element element : calcElements) {
			i += element.getLength();
		}
		int[] j = listAvailableCalcElements.getSelectionIndices();
		for (int k : j) {
			Element element = availableCalcElements.get(k);
			i += element.getLength();
		}
		return i;
	}
	
	@Override
	public Short getDisplacementPageCount() {
		return displacementPageCount;
	}

	public DuplicatesOption getDuplicatesOption() {
		if (locationMode == LocationMode.CALC) {
			return calcDuplicatesOption;
		} else if (locationMode == LocationMode.VSAM_CALC) {
			return vsamCalcDuplicatesOption;
		} else {
			return null;
		}
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(485, 625);
	}
	
	@Override
	public LocationMode getLocationMode() {
		return locationMode;
	}
	
	@Override
	public String getSymbolicDisplacementName() {
		return symbolicDisplacementName;
	}

	@Override
	public String getViaSetName() {
		return viaSetName;
	}

	private void initialize() {
		
		// fill the list with the available (potential) CALC and VSAM CALC key elements; the list 
		// with the actual CALC and VSAM CALC key elements, if relevant, will be filled later - take 
		// care of the backing list as well (in order to have the underlying Element instances 
		// available)
		for (Element element : record.getElements()) {
			if (!element.getName().equals("FILLER") &&
				!isOccursInvolved(element)) {
				
				// restrictions :
				// - no element named FILLER can be used in the CALC or VSAM CALC key
				// - no repeating element (that is, one defined with an OCCURS clause) and no 
				// element subordinate to a repeating element can be used in the CALC key
				boolean calcKeyElement = false;
				boolean vsamCalcKeyElement = false;
				if (record.getLocationMode() == LocationMode.CALC) {					
					for (KeyElement keyElement : element.getKeyElements()) {
						if (keyElement.getKey() == record.getCalcKey()) {
							calcKeyElement = true;
							break;
						}
					}
				} else if (record.getLocationMode() == LocationMode.VSAM_CALC) {
					for (KeyElement keyElement : element.getKeyElements()) {
						if (keyElement.getKey() == record.getCalcKey()) {
							vsamCalcKeyElement = true;
							break;
						}
					}						
				}
				if (!calcKeyElement) {
					availableCalcElements.add(element);
					listAvailableCalcElements.add(element.getName());
				}
				if (!vsamCalcKeyElement) {
					availableVsamCalcElements.add(element);
					listAvailableVsamCalcElements.add(element.getName());
				}
			}
		}
		
		// fill the combo containing the CALC key duplicates option values
		comboCalcDuplicatesOption.add(DuplicatesOption.FIRST
													  .toString()
													  .replaceAll("_", " "));
		comboCalcDuplicatesOption.add(DuplicatesOption.LAST
													  .toString()
													  .replaceAll("_", " "));
		comboCalcDuplicatesOption.add(DuplicatesOption.BY_DBKEY
													  .toString()
													  .replaceAll("_", " "));
		comboCalcDuplicatesOption.add(DuplicatesOption.NOT_ALLOWED
													  .toString()
													  .replaceAll("_", " "));
		
		// fill the combo containing the VSAM CALC key duplicates option values
		comboVsamCalcDuplicatesOption.add(DuplicatesOption.NOT_ALLOWED.toString().replaceAll("_", " "));
		comboVsamCalcDuplicatesOption.add(DuplicatesOption.UNORDERED.toString().replaceAll("_", " "));

		// fill the combo containing the VIA set names; add all sets in which
		// the record participates as a member
		for (MemberRole memberRole : record.getMemberRoles()) {
			comboViaSet.add(memberRole.getSet().getName());
		}
		
		// set the location mode radio button selections and initialize location 
		// mode specific controls 
		if (record.getLocationMode() == LocationMode.CALC) {
			// location mode is CALC
			btnCalc.setSelection(true);
			// fill the list with CALC key elements and its backing list
			for (KeyElement keyElement : record.getCalcKey().getElements()) {
				Element element = keyElement.getElement();
				calcElements.add(element);
				listCalcElements.add(element.getName());
			}
			// select the duplicates option in the combo
			String duplicatesOption = record.getCalcKey()
											.getDuplicatesOption()
											.toString()
											.replaceAll("_", " ");
			for (int i = 0; i < comboCalcDuplicatesOption.getItemCount(); i++) {
				if (comboCalcDuplicatesOption.getItem(i).equals(duplicatesOption)) {
					comboCalcDuplicatesOption.select(i);
					break;
				}
			}
			//
			btnNoDisplacement.setSelection(true);
		} else if (record.getLocationMode() == LocationMode.DIRECT) {
			// location mode is DIRECT
			btnDirect.setSelection(true);
			//
			btnNoDisplacement.setSelection(true);
		} else if (record.getLocationMode() == LocationMode.VIA) {
			// location mode is VIA
			btnVia.setSelection(true);
			// select the set in the combo
			String setName = record.getViaSpecification().getSet().getName();
			for (int i = 0; i < comboViaSet.getItemCount(); i++) {
				if (comboViaSet.getItem(i).equals(setName)) {
					comboViaSet.select(i);
					break;
				}
			}
			// set the displacement data
			if (record.getViaSpecification().getSymbolicDisplacementName() != null) {
				btnSymbolicDisplacement.setSelection(true);
				textSymbolicDisplacement.setText(record.getViaSpecification()
													   .getSymbolicDisplacementName());
			} else if (record.getViaSpecification().getDisplacementPageCount() != null) {
				btnDisplacementPages.setSelection(true);
				short pages = record.getViaSpecification()
									.getDisplacementPageCount()
									.shortValue();
				textDisplacementPages.setText(String.valueOf(pages));
			} else {
				btnNoDisplacement.setSelection(true);
			}
		} else if (record.getLocationMode() == LocationMode.VSAM) {
			// location mode is VSAM
			btnVsam.setSelection(true);
			btnNoDisplacement.setSelection(true);
		} else if (record.getLocationMode() == LocationMode.VSAM_CALC) {
			// location mode is VSAM CALC
			btnVsamCalc.setSelection(true);
			// fill the list with VSAM CALC key elements and its backing list
			for (KeyElement keyElement : record.getCalcKey().getElements()) {
				Element element = keyElement.getElement();
				vsamCalcElements.add(element);
				listVsamCalcElements.add(element.getName());
			}
			// select the duplicates option in the combo
			String duplicatesOption = record.getCalcKey()
											.getDuplicatesOption()
											.toString()
											.replaceAll("_", " ");
			for (int i = 0; i < comboVsamCalcDuplicatesOption.getItemCount(); i++) {
				if (comboVsamCalcDuplicatesOption.getItem(i).equals(duplicatesOption)) {
					comboVsamCalcDuplicatesOption.select(i);
					break;
				}
			}
			btnNoDisplacement.setSelection(true);		
		}
		
		enableAndDisableLocationModeRadioButtons();
	}

	protected void moveCalcElementDown() {		
		
		int i = listCalcElements.getSelectionIndex();
		Element element = calcElements.get(i);
		
		calcElements.remove(i);
		calcElements.add(i + 1, element);
		
		listCalcElements.remove(i);
		listCalcElements.add(element.getName(), i + 1);
		listCalcElements.select(i + 1);
		
		enableAndDisable();
		
	}

	protected void moveCalcElementUp() {		
		
		int i = listCalcElements.getSelectionIndex();
		Element element = calcElements.get(i);
		
		calcElements.remove(i);
		calcElements.add(i - 1, element);
		
		listCalcElements.remove(i);
		listCalcElements.add(element.getName(), i - 1);
		listCalcElements.select(i - 1);
		
		enableAndDisable();
		
	}
	
	protected void moveVsamCalcElementDown() {		
		
		int i = listVsamCalcElements.getSelectionIndex();
		Element element = vsamCalcElements.get(i);
		
		vsamCalcElements.remove(i);
		vsamCalcElements.add(i + 1, element);
		
		listVsamCalcElements.remove(i);
		listVsamCalcElements.add(element.getName(), i + 1);
		listVsamCalcElements.select(i + 1);
		
		enableAndDisable();
		
	}

	protected void moveVsamCalcElementUp() {		
		
		int i = listVsamCalcElements.getSelectionIndex();
		Element element = vsamCalcElements.get(i);
		
		vsamCalcElements.remove(i);
		vsamCalcElements.add(i - 1, element);
		
		listVsamCalcElements.remove(i);
		listVsamCalcElements.add(element.getName(), i - 1);
		listVsamCalcElements.select(i - 1);
		
		enableAndDisable();
		
	}	

	protected void removeCalcElements() {		
		
		int[] i = listCalcElements.getSelectionIndices();		
		
		java.util.List<Element> tmpList = new ArrayList<>();
		java.util.List<Element> allElements = record.getElements();
		for (int j = 0; j < i.length; j++) {
			Element element = calcElements.get(i[j]);
			int m = allElements.indexOf(element);
			boolean inserted = false;
			for (int k = 0; k < availableCalcElements.size(); k++) {
				int n = allElements.indexOf(availableCalcElements.get(k));			
				if (m < n) {			
					availableCalcElements.add(k, element);
					listAvailableCalcElements.add(element.getName(), k);
					tmpList.add(element);
					inserted = true;
					break;
				}
			}
			if (!inserted) {
				availableCalcElements.add(element);
				listAvailableCalcElements.add(element.getName());
				tmpList.add(element);
			}
		}	
		int[] m = new int[i.length];
		for (int n = 0; n < m.length; n++) {
			m[n] = availableCalcElements.indexOf(tmpList.get(n));
		}
		listAvailableCalcElements.deselectAll();
		listAvailableCalcElements.select(m);
		
		calcElements.removeAll(tmpList);
		listCalcElements.remove(i);
		
		enableAndDisable();		
		
	}
	
	protected void removeVsamCalcElements() {		
		
		int[] i = listVsamCalcElements.getSelectionIndices();		
		
		java.util.List<Element> tmpList = new ArrayList<>();
		java.util.List<Element> allElements = record.getElements();
		for (int j = 0; j < i.length; j++) {
			Element element = vsamCalcElements.get(i[j]);
			int m = allElements.indexOf(element);
			boolean inserted = false;
			for (int k = 0; k < availableVsamCalcElements.size(); k++) {
				int n = allElements.indexOf(availableVsamCalcElements.get(k));			
				if (m < n) {			
					availableVsamCalcElements.add(k, element);
					listAvailableVsamCalcElements.add(element.getName(), k);
					tmpList.add(element);
					inserted = true;
					break;
				}
			}
			if (!inserted) {
				availableVsamCalcElements.add(element);
				listAvailableVsamCalcElements.add(element.getName());
				tmpList.add(element);
			}
		}	
		int[] m = new int[i.length];
		for (int n = 0; n < m.length; n++) {
			m[n] = availableVsamCalcElements.indexOf(tmpList.get(n));
		}
		listAvailableVsamCalcElements.deselectAll();
		listAvailableVsamCalcElements.select(m);
		
		vsamCalcElements.removeAll(tmpList);
		listVsamCalcElements.remove(i);
		
		enableAndDisable();		
		
	}

}
