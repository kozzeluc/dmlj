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
import org.eclipse.swt.events.SelectionListener;
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
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.command.ILocationModeDetailsProvider;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.common.ValidationResult;

public class LocationModeDialog extends Dialog implements ILocationModeDetailsProvider {
	private final SchemaRecord schemaRecord;
	private java.util.List<Element> availableCalcElements = new ArrayList<>();
	private java.util.List<Element> availableVsamCalcElements = new ArrayList<>();
	private java.util.List<Element> calcElements = new ArrayList<>();
	private java.util.List<Element> vsamCalcElements = new ArrayList<>();
	private Short displacementPageCount;
	private DuplicatesOption	 calcDuplicatesOption;
	private DuplicatesOption	 vsamCalcDuplicatesOption;
	private LocationMode	locationMode;
	private String symbolicDisplacementName;
	private String viaSetName;
	
	private Button btnAddCalcElements;
	private Button btnCalc;
	private Button btnDirect;
	private Button btnDisplacementPages;
	private Button btnMoveCalcElementDown;
	private Button btnMoveCalcElementUp;
	private Button btnNoDisplacement;
	private Button btnRemoveCalcElements;
	private Button btnSymbolicDisplacement;
	private Button btnVia;
	private Combo comboCalcDuplicatesOption;
	private Combo comboViaSet;
	private List listAvailableCalcElements;
	private List listCalcElements;
	private Text textDisplacementPages;
	private Text textSymbolicDisplacement;
	private Button btnVsam;
	private Button btnVsamCalc;
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
		for (var current = element; current != null; current = current.getParent()) {
			if (current.getOccursSpecification() != null) {
				return true;
			}
		}
		return false;
	}	
		
	public LocationModeDialog(Shell parentShell, SchemaRecord schemaRecord) {
		super(parentShell);
		this.schemaRecord = schemaRecord;
		setShellStyle(getShellStyle() | SWT.RESIZE); 
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
	    shell.setText("Edit location mode specification for " + schemaRecord.getName());	    
	}
		
	@Override
	protected Point getInitialSize() {
		return new Point(550, 625);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		var container = (Composite) super.createDialogArea(parent);
		var gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 5;
		
		btnCalc = new Button(container, SWT.RADIO);
		btnCalc.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		btnCalc.setText("CALC using:");
		new Label(container, SWT.NONE);
		
		listAvailableCalcElements = new List(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		listAvailableCalcElements.setToolTipText("Available elements");
		var gdListAvailableElements = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 2, 2);
		gdListAvailableElements.heightHint = 75;
		gdListAvailableElements.widthHint = 150;
		gdListAvailableElements.minimumWidth = -1;
		gdListAvailableElements.horizontalIndent = 15;
		listAvailableCalcElements.setLayoutData(gdListAvailableElements);
		
		btnAddCalcElements = new Button(container, SWT.NONE);
		btnAddCalcElements.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		btnAddCalcElements.setText(">");
		
		listCalcElements = new List(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		listCalcElements.setToolTipText("CALC key elements");
		var gdListCalcElements = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 2);
		gdListCalcElements.heightHint = 75;
		gdListCalcElements.widthHint = 150;
		gdListCalcElements.minimumWidth = 100;
		listCalcElements.setLayoutData(gdListCalcElements);
		
		btnMoveCalcElementUp = new Button(container, SWT.NONE);
		btnMoveCalcElementUp.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnMoveCalcElementUp.setText("Up");
		
		btnRemoveCalcElements = new Button(container, SWT.NONE);
		btnRemoveCalcElements.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemoveCalcElements.setText("<");
		
		btnMoveCalcElementDown = new Button(container, SWT.NONE);
		btnMoveCalcElementDown.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnMoveCalcElementDown.setText("Down");
		
		lblCalcDuplicates = new Label(container, SWT.NONE);
		lblCalcDuplicates.setEnabled(false);
		var gdLblDuplicates = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdLblDuplicates.verticalIndent = 5;
		gdLblDuplicates.horizontalIndent = 15;
		lblCalcDuplicates.setLayoutData(gdLblDuplicates);
		lblCalcDuplicates.setText("Duplicates:");
		
		comboCalcDuplicatesOption = new Combo(container, SWT.READ_ONLY);
		var gdComboCalcDuplicatesOption = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gdComboCalcDuplicatesOption.verticalIndent = 5;
		gdComboCalcDuplicatesOption.widthHint = 200;
		comboCalcDuplicatesOption.setLayoutData(gdComboCalcDuplicatesOption);
		
		var label1 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		var gdLabel1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gdLabel1.verticalIndent = 10;
		gdLabel1.horizontalIndent = 15;
		label1.setLayoutData(gdLabel1);
		
		btnDirect = new Button(container, SWT.RADIO);
		btnDirect.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		btnDirect.setText("DIRECT");
		new Label(container, SWT.NONE);
		
		var label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		var gdLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gdLabel.verticalIndent = 10;
		gdLabel.horizontalIndent = 15;
		label.setLayoutData(gdLabel);
		
		btnVia = new Button(container, SWT.RADIO);
		btnVia.setText("VIA set:");
		
		comboViaSet = new Combo(container, SWT.READ_ONLY);
		var gdComboViaSet = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gdComboViaSet.widthHint = 200;
		comboViaSet.setLayoutData(gdComboViaSet);
		
		var lblDisplacement = new Label(container, SWT.NONE);
		lblDisplacement.setEnabled(false);
		lblDisplacement.setAlignment(SWT.RIGHT);
		var gdLblDisplacement = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gdLblDisplacement.verticalIndent = 5;
		gdLblDisplacement.horizontalIndent = 15;
		lblDisplacement.setLayoutData(gdLblDisplacement);
		lblDisplacement.setText("Displacement:");
		
		var composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		
		btnNoDisplacement = new Button(composite, SWT.RADIO);
		btnNoDisplacement.setText("None");
		new Label(composite, SWT.NONE);
		
		btnSymbolicDisplacement = new Button(composite, SWT.RADIO);
		btnSymbolicDisplacement.setText("USING symbolic displacement:");
		
		textSymbolicDisplacement = new Text(composite, SWT.BORDER);
		var gdTextSymbolicDisplacement = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gdTextSymbolicDisplacement.widthHint = 150;
		textSymbolicDisplacement.setLayoutData(gdTextSymbolicDisplacement);
		
		btnDisplacementPages = new Button(composite, SWT.RADIO);
		btnDisplacementPages.setText("Pages:");
		
		textDisplacementPages = new Text(composite, SWT.BORDER | SWT.RIGHT);
		var gdTextDisplacementPages = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdTextDisplacementPages.widthHint = 50;
		textDisplacementPages.setLayoutData(gdTextDisplacementPages);
		
		var lblNewLabel = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		var gdLblNewLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gdLblNewLabel.horizontalIndent = 15;
		lblNewLabel.setLayoutData(gdLblNewLabel);
		lblNewLabel.setText("New Label");
		
		btnVsam = new Button(container, SWT.RADIO);
		btnVsam.addSelectionListener(createSelectionListener(this::doChecks));
		btnVsam.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		btnVsam.setText("VSAM");
		new Label(container, SWT.NONE);
		
		var lblNewLabel1 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		var gdLblNewLabel1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1);
		gdLblNewLabel1.horizontalIndent = 15;
		lblNewLabel1.setLayoutData(gdLblNewLabel1);
		lblNewLabel1.setText("New Label");
		
		btnVsamCalc = new Button(container, SWT.RADIO);
		btnVsamCalc.addSelectionListener(createSelectionListener(this::doChecks));
		btnVsamCalc.setText("VSAM CALC");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		listAvailableVsamCalcElements = new List(container, SWT.BORDER);
		listAvailableVsamCalcElements.addSelectionListener(createSelectionListener(this::doChecks));
		listAvailableVsamCalcElements.setEnabled(false);
		var gdListAvailableVsamCalcElements = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2);
		gdListAvailableVsamCalcElements.heightHint = 75;
		gdListAvailableVsamCalcElements.horizontalIndent = 15;
		listAvailableVsamCalcElements.setLayoutData(gdListAvailableVsamCalcElements);
		
		btnAddVsamCalcElements = new Button(container, SWT.NONE);
		btnAddVsamCalcElements.addSelectionListener(createSelectionListener(this::addVsamCalcElements));
		btnAddVsamCalcElements.setEnabled(false);
		btnAddVsamCalcElements.setText(">");
		
		listVsamCalcElements = new List(container, SWT.BORDER);
		listVsamCalcElements.addSelectionListener(createSelectionListener(this::doChecks));
		listVsamCalcElements.setEnabled(false);
		listVsamCalcElements.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
		
		btnMoveVsamCalcElementUp = new Button(container, SWT.NONE);
		btnMoveVsamCalcElementUp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnMoveVsamCalcElementUp.addSelectionListener(createSelectionListener(this::moveVsamCalcElementUp));
		btnMoveVsamCalcElementUp.setEnabled(false);
		btnMoveVsamCalcElementUp.setText("Up");
		
		btnRemoveVsamCalcElements = new Button(container, SWT.NONE);
		btnRemoveVsamCalcElements.addSelectionListener(createSelectionListener(this::removeVsamCalcElements));
		btnRemoveVsamCalcElements.setEnabled(false);
		btnRemoveVsamCalcElements.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemoveVsamCalcElements.setText("<");
		
		btnMoveVsamCalcElementDown = new Button(container, SWT.NONE);
		btnMoveVsamCalcElementDown.addSelectionListener(createSelectionListener(this::moveVsamCalcElementDown));
		btnMoveVsamCalcElementDown.setEnabled(false);
		btnMoveVsamCalcElementDown.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnMoveVsamCalcElementDown.setText("Down");
		
		lblVsamCalcDuplicates = new Label(container, SWT.NONE);
		lblVsamCalcDuplicates.setEnabled(false);
		var gdLblVsamCalcDuplicates = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdLblVsamCalcDuplicates.verticalIndent = 5;
		gdLblVsamCalcDuplicates.horizontalIndent = 15;
		lblVsamCalcDuplicates.setLayoutData(gdLblVsamCalcDuplicates);
		lblVsamCalcDuplicates.setText("Duplicates:");
		
		comboVsamCalcDuplicatesOption = new Combo(container, SWT.READ_ONLY);
		comboVsamCalcDuplicatesOption.addSelectionListener(createSelectionListener(this::doChecks));
		comboVsamCalcDuplicatesOption.setEnabled(false);
		var gdComboVsamCalcDuplicatesOption = new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1);
		gdComboVsamCalcDuplicatesOption.verticalIndent = 5;
		gdComboVsamCalcDuplicatesOption.widthHint = 200;
		comboVsamCalcDuplicatesOption.setLayoutData(gdComboVsamCalcDuplicatesOption);

		btnCalc.addSelectionListener(createSelectionListener(this::doChecks));
		btnDirect.addSelectionListener(createSelectionListener(this::doChecks));
		btnVia.addSelectionListener(createSelectionListener(this::doChecks));
		listAvailableCalcElements.addSelectionListener(createSelectionListener(this::doChecks));
		listCalcElements.addSelectionListener(createSelectionListener(this::doChecks));
		btnAddCalcElements.addSelectionListener(createSelectionListener(this::addCalcElements));
		btnRemoveCalcElements.addSelectionListener(createSelectionListener(this::removeCalcElements));
		btnMoveCalcElementUp.addSelectionListener(createSelectionListener(this::moveCalcElementUp));
		btnMoveCalcElementDown.addSelectionListener(createSelectionListener(this::moveCalcElementDown));
		comboCalcDuplicatesOption.addSelectionListener(createSelectionListener(this::doChecks));
		comboViaSet.addSelectionListener(createSelectionListener(this::doChecks));
		btnNoDisplacement.addSelectionListener(createSelectionListener(this::doChecks));
		btnSymbolicDisplacement.addSelectionListener(createSelectionListener(this::doChecks));
		btnDisplacementPages.addSelectionListener(createSelectionListener(this::doChecks));
		textSymbolicDisplacement.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				doChecks();				
			}
		});
		textSymbolicDisplacement.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || e.keyCode == 16777296 || e.keyCode == SWT.ESC) { // enter and escape keys					
					var p = textSymbolicDisplacement.getText().toUpperCase();
					textSymbolicDisplacement.setText(p);
					textSymbolicDisplacement.setSelection(p.length());
					doChecks();					
				}
			}
		});
		textDisplacementPages.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				doChecks();				
			}
		});
		textDisplacementPages.addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent e) {				
				if (e.keyCode == 13 || e.keyCode == 16777296 || e.keyCode == SWT.ESC) { // Enter and escape keys
					doChecks();					
				}
			}
		});
		
		initialize();
		return container;
	}
	
	private SelectionListener createSelectionListener(Runnable widgetSelectedCode) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				widgetSelectedCode.run();
			}
		};
	}
	
	protected void addCalcElements() {
		var availableCalcElementsSelectionIndices = listAvailableCalcElements.getSelectionIndices();
		Arrays.sort(availableCalcElementsSelectionIndices);
		
		var tmpList = new ArrayList<Element>();
		var originalItemCount = listCalcElements.getItemCount();
		for (var i = 0; i < availableCalcElementsSelectionIndices.length; i++) {
			var element = availableCalcElements.get(availableCalcElementsSelectionIndices[i]);
			calcElements.add(element);
			listCalcElements.add(element.getName());
			tmpList.add(element);
		}	
		var newListCalcElementsSelectionIndices = new int[availableCalcElementsSelectionIndices.length];
		for (int i = 0; i < newListCalcElementsSelectionIndices.length; i++) {
			newListCalcElementsSelectionIndices[i] = originalItemCount + i;
		}
		listCalcElements.deselectAll();
		listCalcElements.select(newListCalcElementsSelectionIndices);
		
		availableCalcElements.removeAll(tmpList);
		listAvailableCalcElements.remove(availableCalcElementsSelectionIndices);
		
		doChecks();
	}

	protected void addVsamCalcElements() {
		var availableVsamCalcElementsSelectionIndices = listAvailableVsamCalcElements.getSelectionIndices();
		Arrays.sort(availableVsamCalcElementsSelectionIndices);
		
		var tmpList = new ArrayList<Element>();
		int originalItemCount = listVsamCalcElements.getItemCount();
		for (var i = 0; i < availableVsamCalcElementsSelectionIndices.length; i++) {
			var element = availableVsamCalcElements.get(availableVsamCalcElementsSelectionIndices[i]);
			vsamCalcElements.add(element);
			listVsamCalcElements.add(element.getName());
			tmpList.add(element);
		}	
		var newListVsamCalcElementsSelectionIndices = new int[availableVsamCalcElementsSelectionIndices.length];
		for (var i = 0; i < newListVsamCalcElementsSelectionIndices.length; i++) {
			newListVsamCalcElementsSelectionIndices[i] = originalItemCount + i;
		}
		listVsamCalcElements.deselectAll();
		listVsamCalcElements.select(newListVsamCalcElementsSelectionIndices);
		
		availableVsamCalcElements.removeAll(tmpList);
		listAvailableVsamCalcElements.remove(availableVsamCalcElementsSelectionIndices);
		
		doChecks();
	}
	
	protected void moveCalcElementDown() {		
		var i = listCalcElements.getSelectionIndex();
		var element = calcElements.get(i);
		
		calcElements.remove(i);
		calcElements.add(i + 1, element);
		
		listCalcElements.remove(i);
		listCalcElements.add(element.getName(), i + 1);
		listCalcElements.select(i + 1);
		
		doChecks();
	}

	protected void moveCalcElementUp() {
		var i = listCalcElements.getSelectionIndex();
		var element = calcElements.get(i);
		
		calcElements.remove(i);
		calcElements.add(i - 1, element);
		
		listCalcElements.remove(i);
		listCalcElements.add(element.getName(), i - 1);
		listCalcElements.select(i - 1);
		
		doChecks();
	}
	
	protected void moveVsamCalcElementDown() {
		var i = listVsamCalcElements.getSelectionIndex();
		var element = vsamCalcElements.get(i);
		
		vsamCalcElements.remove(i);
		vsamCalcElements.add(i + 1, element);
		
		listVsamCalcElements.remove(i);
		listVsamCalcElements.add(element.getName(), i + 1);
		listVsamCalcElements.select(i + 1);
		
		doChecks();
	}

	protected void moveVsamCalcElementUp() {
		var i = listVsamCalcElements.getSelectionIndex();
		var element = vsamCalcElements.get(i);
		
		vsamCalcElements.remove(i);
		vsamCalcElements.add(i - 1, element);
		
		listVsamCalcElements.remove(i);
		listVsamCalcElements.add(element.getName(), i - 1);
		listVsamCalcElements.select(i - 1);
		
		doChecks();
	}	

	protected void removeCalcElements() {
		var listCalcElementsSelectionIndices = listCalcElements.getSelectionIndices();		
		
		var tmpList = new ArrayList<Element>();
		var allElements = schemaRecord.getElements();
		for (var j = 0; j < listCalcElementsSelectionIndices.length; j++) {
			var element = calcElements.get(listCalcElementsSelectionIndices[j]);
			var m = allElements.indexOf(element);
			var inserted = false;
			for (var k = 0; k < availableCalcElements.size(); k++) {
				var n = allElements.indexOf(availableCalcElements.get(k));			
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
		var m = new int[listCalcElementsSelectionIndices.length];
		for (var n = 0; n < m.length; n++) {
			m[n] = availableCalcElements.indexOf(tmpList.get(n));
		}
		listAvailableCalcElements.deselectAll();
		listAvailableCalcElements.select(m);
		
		calcElements.removeAll(tmpList);
		listCalcElements.remove(listCalcElementsSelectionIndices);
		
		doChecks();
	}
	
	protected void removeVsamCalcElements() {
		var i = listVsamCalcElements.getSelectionIndices();		
		
		var tmpList = new ArrayList<Element>();
		var allElements = schemaRecord.getElements();
		for (var j = 0; j < i.length; j++) {
			var element = vsamCalcElements.get(i[j]);
			var m = allElements.indexOf(element);
			var inserted = false;
			for (var k = 0; k < availableVsamCalcElements.size(); k++) {
				var n = allElements.indexOf(availableVsamCalcElements.get(k));			
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
		var m = new int[i.length];
		for (var n = 0; n < m.length; n++) {
			m[n] = availableVsamCalcElements.indexOf(tmpList.get(n));
		}
		listAvailableVsamCalcElements.deselectAll();
		listAvailableVsamCalcElements.select(m);
		
		vsamCalcElements.removeAll(tmpList);
		listVsamCalcElements.remove(i);
		
		doChecks();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		doChecks();
	}
	
	private void initialize() {
		fillListsWithCalcAndVsamCalcKeyElements();
		
		// fill the combo containing the CALC key duplicates option values
		comboCalcDuplicatesOption.add(DuplicatesOption.FIRST.toString().replace("_", " "));
		comboCalcDuplicatesOption.add(DuplicatesOption.LAST.toString().replace("_", " "));
		comboCalcDuplicatesOption.add(DuplicatesOption.BY_DBKEY.toString().replace("_", " "));
		comboCalcDuplicatesOption.add(DuplicatesOption.NOT_ALLOWED.toString().replace("_", " "));
		
		// fill the combo containing the VSAM CALC key duplicates option values
		comboVsamCalcDuplicatesOption.add(DuplicatesOption.NOT_ALLOWED.toString().replace("_", " "));
		comboVsamCalcDuplicatesOption.add(DuplicatesOption.UNORDERED.toString().replace("_", " "));
	
		// fill the combo containing the VIA set names; add all sets in which the record participates as a member
		for (var memberRole : schemaRecord.getMemberRoles()) {
			comboViaSet.add(memberRole.getSet().getName());
		}
		
		initializeLocationModeSpecificControls();
		enableAndDisableLocationModeRadioButtons();
	}
	
	private void fillListsWithCalcAndVsamCalcKeyElements() {
		// fill the list with the available (potential) CALC and VSAM CALC key elements; the list with the actual
		// CALC and VSAM CALC key elements, if relevant, will be filled later - take care of the backing list as
		// well (in order to have the underlying Element instances available)
		schemaRecord.getElements().stream()
				.filter(element -> !element.getName().equals("FILLER"))
				.filter(element -> !isOccursInvolved(element))
				.forEach(this::fillListsWithCalcAndVsamCalcKeyElements);
	}
	
	private void fillListsWithCalcAndVsamCalcKeyElements(Element element) {
		var calcKeyElement = false;
		var vsamCalcKeyElement = false;
		if (schemaRecord.getLocationMode() == LocationMode.CALC) {			
			calcKeyElement = element.getKeyElements().stream()
				.anyMatch(keyElement -> keyElement.getKey() == schemaRecord.getCalcKey());
		} else if (schemaRecord.getLocationMode() == LocationMode.VSAM_CALC) {
			vsamCalcKeyElement = element.getKeyElements().stream()
					.anyMatch(keyElement -> keyElement.getKey() == schemaRecord.getCalcKey());
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
	
	private void initializeLocationModeSpecificControls() {
		if (schemaRecord.getLocationMode() == LocationMode.CALC) {
			initializeCalcLocationModeSpecificControls();
		} else if (schemaRecord.getLocationMode() == LocationMode.DIRECT) {
			btnDirect.setSelection(true);
			btnNoDisplacement.setSelection(true);
		} else if (schemaRecord.getLocationMode() == LocationMode.VIA) {
			initializeViaLocationModeSpecificControls();
		} else if (schemaRecord.getLocationMode() == LocationMode.VSAM) {
			btnVsam.setSelection(true);
			btnNoDisplacement.setSelection(true);
		} else if (schemaRecord.getLocationMode() == LocationMode.VSAM_CALC) {
			initializeVsamCalcLocationModeSpecificControls();
		}		
	}
	
	private void initializeCalcLocationModeSpecificControls() {
		btnCalc.setSelection(true);
		// fill the list with CALC key elements and its backing list
		for (var keyElement : schemaRecord.getCalcKey().getElements()) {
			var element = keyElement.getElement();
			calcElements.add(element);
			listCalcElements.add(element.getName());
		}
		// select the duplicates option in the combo
		var duplicatesOption = schemaRecord.getCalcKey().getDuplicatesOption().toString().replace("_", " ");
		for (var i = 0; i < comboCalcDuplicatesOption.getItemCount(); i++) {
			if (comboCalcDuplicatesOption.getItem(i).equals(duplicatesOption)) {
				comboCalcDuplicatesOption.select(i);
				break;
			}
		}
		btnNoDisplacement.setSelection(true);		
	}
	
	private void initializeViaLocationModeSpecificControls() {		
		btnVia.setSelection(true);
		// select the set in the combo
		var setName = schemaRecord.getViaSpecification().getSet().getName();
		for (var i = 0; i < comboViaSet.getItemCount(); i++) {
			if (comboViaSet.getItem(i).equals(setName)) {
				comboViaSet.select(i);
				break;
			}
		}
		// set the displacement data
		if (schemaRecord.getViaSpecification().getSymbolicDisplacementName() != null) {
			btnSymbolicDisplacement.setSelection(true);
			textSymbolicDisplacement.setText(schemaRecord.getViaSpecification().getSymbolicDisplacementName());
		} else if (schemaRecord.getViaSpecification().getDisplacementPageCount() != null) {
			btnDisplacementPages.setSelection(true);
			var pages = schemaRecord.getViaSpecification().getDisplacementPageCount().shortValue();
			textDisplacementPages.setText(String.valueOf(pages));
		} else {
			btnNoDisplacement.setSelection(true);
		}		
	}
	
	private void initializeVsamCalcLocationModeSpecificControls() {
		btnVsamCalc.setSelection(true);
		// fill the list with VSAM CALC key elements and its backing list
		for (var keyElement : schemaRecord.getCalcKey().getElements()) {
			var element = keyElement.getElement();
			vsamCalcElements.add(element);
			listVsamCalcElements.add(element.getName());
		}
		// select the duplicates option in the combo
		String duplicatesOption = schemaRecord.getCalcKey().getDuplicatesOption().toString().replace("_", " ");
		for (var i = 0; i < comboVsamCalcDuplicatesOption.getItemCount(); i++) {
			if (comboVsamCalcDuplicatesOption.getItem(i).equals(duplicatesOption)) {
				comboVsamCalcDuplicatesOption.select(i);
				break;
			}
		}
		btnNoDisplacement.setSelection(true);		
	}
	
	private void enableAndDisableLocationModeRadioButtons() {		
		enableOrDisableCalcLocationModeRadioButton();
		enableOrDisableDirectLocationModeRadioButton();
		enableOrDisableViaLocationModeRadioButton();
		enableOrDisableVsamLocationModeRadioButton();
		enableOrDisableVsamCalcLocationModeRadioButton();
	}
	
	private void enableOrDisableCalcLocationModeRadioButton() {
		var hasAtLeastOneKeyElementCandidate = !availableCalcElements.isEmpty();
		btnCalc.setEnabled((schemaRecord.isCalc() || schemaRecord.isDirect() && hasAtLeastOneKeyElementCandidate ||
				schemaRecord.isVia() && hasAtLeastOneKeyElementCandidate ||
			    schemaRecord.isVsam() && hasAtLeastOneKeyElementCandidate && schemaRecord.getMemberRoles().isEmpty() ||
			    schemaRecord.isVsamCalc() && schemaRecord.getMemberRoles().isEmpty()) &&
				(schemaRecord.getAreaSpecification().getArea().getRecords().size() == 1 ||
				 Tools.canHoldNonVsamRecords(schemaRecord.getAreaSpecification().getArea())));
	}
	
	private void enableOrDisableDirectLocationModeRadioButton() {	
		btnDirect.setEnabled((schemaRecord.isCalc() || schemaRecord.isDirect() || schemaRecord.isVia() ||
				schemaRecord.isVsam() && schemaRecord.getMemberRoles().isEmpty() ||
				schemaRecord.isVsamCalc() && schemaRecord.getMemberRoles().isEmpty()) &&
				(schemaRecord.getAreaSpecification().getArea().getRecords().size() == 1 ||
				 Tools.canHoldNonVsamRecords(schemaRecord.getAreaSpecification().getArea())));
	}
	
	private void enableOrDisableViaLocationModeRadioButton() {	
		btnVia.setEnabled((schemaRecord.isCalc() && !schemaRecord.getMemberRoles().isEmpty() ||
				schemaRecord.isDirect() && !schemaRecord.getMemberRoles().isEmpty() ||
				schemaRecord.isVia()) && (schemaRecord.getAreaSpecification().getArea().getRecords().size() == 1 ||
				Tools.canHoldNonVsamRecords(schemaRecord.getAreaSpecification().getArea())));
	}
	
	private void enableOrDisableVsamLocationModeRadioButton() {	
		btnVsam.setEnabled((schemaRecord.isCalc() && schemaRecord.getMemberRoles().isEmpty() ||
				schemaRecord.isDirect() && schemaRecord.getMemberRoles().isEmpty() ||
				schemaRecord.isVsam() || schemaRecord.isVsamCalc()) &&
				(schemaRecord.getAreaSpecification().getArea().getRecords().size() == 1 && 
				 schemaRecord.getAreaSpecification().getArea().getIndexes().isEmpty() ||
			     Tools.canHoldVsamRecords(schemaRecord.getAreaSpecification().getArea())));
	}
	
	private void enableOrDisableVsamCalcLocationModeRadioButton() {
		var hasAtLeastOneKeyElementCandidate = !availableCalcElements.isEmpty();
		btnVsamCalc.setEnabled((schemaRecord.isCalc() && schemaRecord.getMemberRoles().isEmpty() ||
				schemaRecord.isDirect() && hasAtLeastOneKeyElementCandidate && schemaRecord.getMemberRoles().isEmpty() ||
				schemaRecord.isVsam() && hasAtLeastOneKeyElementCandidate || schemaRecord.isVsamCalc()) &&
				(schemaRecord.getAreaSpecification().getArea().getRecords().size() == 1 &&
				 schemaRecord.getAreaSpecification().getArea().getIndexes().isEmpty() ||
				 Tools.canHoldVsamRecords(schemaRecord.getAreaSpecification().getArea())));
	}

	private void doChecks() {
		// Note: the location mode radio buttons are enabled/disabled during initialization (see initialize()
		// method); once enabled or disabled, this is fixed for the lifetime of the dialog)
		enableAndDisableCalcRelatedControls();
		enableAndDisableViaRelatedControls();
		enableAndDisableVsamCalcRelatedControls();
		getButton(IDialogConstants.OK_ID).setEnabled(calculateOkButtonEnablement());
		if (getButton(IDialogConstants.OK_ID).isEnabled()) {
			rememberEnteredData();
		}		
	}
	
	private void enableAndDisableCalcRelatedControls() {
		listAvailableCalcElements.setEnabled(btnCalc.getSelection());
		listCalcElements.setEnabled(btnCalc.getSelection());
		btnAddCalcElements.setEnabled(btnCalc.getSelection() && listAvailableCalcElements.getSelectionCount() > 0 && getComputedCalcKeyLength() <= 256);
		btnRemoveCalcElements.setEnabled(btnCalc.getSelection() && listCalcElements.getSelectionCount() > 0);
		btnMoveCalcElementUp.setEnabled(btnCalc.getSelection() && listCalcElements.getSelectionCount() == 1 && listCalcElements.getSelectionIndex() > 0);
		btnMoveCalcElementDown.setEnabled(btnCalc.getSelection() && listCalcElements.getSelectionCount() == 1 && listCalcElements.getSelectionIndex() < listCalcElements.getItemCount() - 1);
		lblCalcDuplicates.setEnabled(btnCalc.getSelection());
		comboCalcDuplicatesOption.setEnabled(btnCalc.getSelection());		
	}
	
	private void enableAndDisableViaRelatedControls() {
		comboViaSet.setEnabled(btnVia.getSelection());
		btnNoDisplacement.setEnabled(btnVia.getSelection());
		btnSymbolicDisplacement.setEnabled(btnVia.getSelection());
		btnDisplacementPages.setEnabled(btnVia.getSelection());
		textSymbolicDisplacement.setEnabled(btnVia.getSelection() && btnSymbolicDisplacement.getSelection());
		textDisplacementPages.setEnabled(btnVia.getSelection() && btnDisplacementPages.getSelection());
	}
	
	private void enableAndDisableVsamCalcRelatedControls() {
		listAvailableVsamCalcElements.setEnabled(btnVsamCalc.getSelection());
		listVsamCalcElements.setEnabled(btnVsamCalc.getSelection());
		btnAddVsamCalcElements.setEnabled(btnVsamCalc.getSelection() && listAvailableVsamCalcElements.getSelectionCount() > 0 && getComputedCalcKeyLength() <= 256);
		btnRemoveVsamCalcElements.setEnabled(btnVsamCalc.getSelection() && listVsamCalcElements.getSelectionCount() > 0);
		btnMoveVsamCalcElementUp.setEnabled(btnVsamCalc.getSelection() && listVsamCalcElements.getSelectionCount() == 1 && listVsamCalcElements.getSelectionIndex() > 0);
		btnMoveVsamCalcElementDown.setEnabled(btnVsamCalc.getSelection() && listVsamCalcElements.getSelectionCount() == 1 && listVsamCalcElements.getSelectionIndex() < listVsamCalcElements.getItemCount() - 1);
		lblVsamCalcDuplicates.setEnabled(btnVsamCalc.getSelection());
		comboVsamCalcDuplicatesOption.setEnabled(btnVsamCalc.getSelection());
	}
	
	private int getComputedCalcKeyLength() {
		var calcElementsLength = calcElements.stream()
				.mapToInt(Element::getLength)
				.sum();
		return calcElementsLength + Arrays.stream(listAvailableCalcElements.getSelectionIndices())
				.mapToObj(availableCalcElements::get)
				.mapToInt(Element::getLength)
				.sum();
	}
	
	private boolean calculateOkButtonEnablement() {
		boolean enabled = false;
		if (btnCalc.getSelection()) {
			// CALC radio button selected; check if we have at least 1 CALC key element and a value for the duplicates option
			enabled = !calcElements.isEmpty() && comboCalcDuplicatesOption.getSelectionIndex() > -1;
		} else if (btnDirect.getSelection() || btnVsam.getSelection()) {
			enabled = true;
		} else if (btnVia.getSelection()) {
			enabled = areViaControlsValid();
		} else if (btnVsamCalc.getSelection()) {
			// VSAM CALC radio button selected; check if we have at least 1 VSAM CALC key element and a value for the duplicates option
			enabled = !vsamCalcElements.isEmpty() && comboVsamCalcDuplicatesOption.getSelectionIndex() > -1;
		}
		return enabled && anythingChanged();
	}
	
	private boolean areViaControlsValid() {
		// check if we have a set and, if specified, if the displacement specification is valid
		if (comboViaSet.getSelectionIndex() > -1) {
			if (btnSymbolicDisplacement.getSelection()) {
				var validationResult = NamingConventions.validate(textSymbolicDisplacement.getText(), NamingConventions.Type.SYMBOLIC_DISPLACEMENT);
				return validationResult.getStatus() == ValidationResult.Status.OK;
			} else {
				try {
					// if a displacement page count is specified, it must be an unsigned integer in the range 0 through 32,767
					return !btnDisplacementPages.getSelection() || Short.parseShort(textDisplacementPages.getText()) > 0;	
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		}
		return false;
	}
	
	private boolean anythingChanged() {
		if (isLocationModeChanged()) {
			return true;
		} else if (schemaRecord.getLocationMode() == LocationMode.DIRECT || schemaRecord.getLocationMode() == LocationMode.VSAM) { // if the location mode is DIRECT or VSAM, there's nothing else that can be changed
			return false;
		} else if (schemaRecord.getLocationMode() == LocationMode.CALC) {
			return isCalcDataChanged();
		} else if (schemaRecord.getLocationMode() == LocationMode.VIA) {
			return isViaDataChanged();			
		} else {
			return isVsamCalcDataChanged();
		}
	}
	
	private boolean isLocationModeChanged() {
		return btnCalc.getSelection() && schemaRecord.getLocationMode() != LocationMode.CALC ||
			   btnDirect.getSelection() && schemaRecord.getLocationMode() != LocationMode.DIRECT ||
			   btnVia.getSelection() && schemaRecord.getLocationMode() != LocationMode.VIA ||
			   btnVsam.getSelection() && schemaRecord.getLocationMode() != LocationMode.VSAM ||
			   btnVsamCalc.getSelection() && schemaRecord.getLocationMode() != LocationMode.VSAM_CALC;
	}
	
	private boolean isCalcDataChanged() {
		if (calcElements.size() != schemaRecord.getCalcKey().getElements().size()) {
			// the number of elements in the CALC key has changed
			return true;
		} else {
			for (var i = 0; i < calcElements.size(); i++) {
				var modelElement = schemaRecord.getCalcKey().getElements().get(i).getElement();
				var dialogElement = calcElements.get(i);
				if (dialogElement != modelElement) {
					// element mismatch
					return true;
				}
			}
		}
		var modelDuplicatesOption = schemaRecord.getCalcKey().getDuplicatesOption().toString().replace("_", " ");
		return !comboCalcDuplicatesOption.getText().equals(modelDuplicatesOption);
	}
	
	private boolean isViaDataChanged() {
		var modelSetName = schemaRecord.getViaSpecification().getSet().getName();
		var dialogSetName = comboViaSet.getText();
		var via = schemaRecord.getViaSpecification();
		return !dialogSetName.equals(modelSetName) || // VIA set changed
			   btnNoDisplacement.getSelection() && (via.getSymbolicDisplacementName() != null || via.getDisplacementPageCount() != null) ||					// did user remove symbolic displacement name or displacement pages?
			   btnSymbolicDisplacement.getSelection() && (via.getSymbolicDisplacementName() == null || via.getDisplacementPageCount() != null) ||				// has user specified that a symbolic displacement name has to be used?
			   btnSymbolicDisplacement.getSelection() && !textSymbolicDisplacement.getText().trim().equals(via.getSymbolicDisplacementName()) ||				// has user modified the symbolic displacement name?
			   btnDisplacementPages.getSelection() && (via.getSymbolicDisplacementName() != null || via.getDisplacementPageCount() == null) ||					// hss user specified a displacement page count?
			   btnDisplacementPages.getSelection() && Short.parseShort(textDisplacementPages.getText().trim()) != via.getDisplacementPageCount().shortValue(); // has user changed the displacement page count?
	}
	
	private boolean isVsamCalcDataChanged() {
		if (vsamCalcElements.size() != schemaRecord.getCalcKey().getElements().size()) {
			// the number of elements in the VSAM CALC key has changed
			return true;
		} else {
			for (var i = 0; i < vsamCalcElements.size(); i++) {
				var modelElement = schemaRecord.getCalcKey().getElements().get(i).getElement();
				var dialogElement = vsamCalcElements.get(i);
				if (dialogElement != modelElement) {
					// element mismatch
					return true;
				}
			}
		}
		var modelDuplicatesOption = schemaRecord.getCalcKey().getDuplicatesOption().toString().replace("_", " ");
		return !comboVsamCalcDuplicatesOption.getText().equals(modelDuplicatesOption);
	}
	
	private void rememberEnteredData() {
		calcDuplicatesOption = null;
		vsamCalcDuplicatesOption = null;
		viaSetName = null;
		symbolicDisplacementName = null;
		displacementPageCount = null;
		if (btnCalc.getSelection()) {
			rememberEnteredCalcData();
		} else if (btnDirect.getSelection()) {
			locationMode = LocationMode.DIRECT;
		} else if (btnVia.getSelection()) {
			rememberEnteredViaData();
		} else if (btnVsam.getSelection()) {
			locationMode = LocationMode.VSAM;
		} else if (btnVsamCalc.getSelection()) {
			rememberEnteredVsamCalcData();
		}		
	}
	
	private void rememberEnteredCalcData() {
		locationMode = LocationMode.CALC;
		if (comboCalcDuplicatesOption.getSelectionIndex() > -1) {
			calcDuplicatesOption = DuplicatesOption.VALUES.stream()
					.filter(option -> option.toString().replace("_", " ").equals(comboCalcDuplicatesOption.getText()))
					.findFirst()
					.orElseThrow();
		}
	}
	
	private void rememberEnteredViaData() {
		locationMode = LocationMode.VIA;
		viaSetName = comboViaSet.getText();
		if (btnSymbolicDisplacement.getSelection()) {
			symbolicDisplacementName = textSymbolicDisplacement.getText().trim();
		} else if (btnDisplacementPages.getSelection()) {
			displacementPageCount = Short.valueOf(textDisplacementPages.getText().trim());
		}
	}
	
	private void rememberEnteredVsamCalcData() {
		locationMode = LocationMode.VSAM_CALC;
		if (comboVsamCalcDuplicatesOption.getSelectionIndex() > -1) {
			vsamCalcDuplicatesOption = DuplicatesOption.VALUES.stream()
					.filter(option -> option.toString().replace("_", " ").equals(comboVsamCalcDuplicatesOption.getText()))
					.findFirst()
					.orElseThrow();
		}
	}

	@Override
	public java.util.List<Element> getCalcKeyElements() {
		if (locationMode == LocationMode.CALC) {
			return new ArrayList<>(calcElements);
		} else if (locationMode == LocationMode.VSAM_CALC) {
			return new ArrayList<>(vsamCalcElements);
		} else {
			return java.util.List.of();
		}
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

}
