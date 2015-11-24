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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.command.ISortKeyDescription;
import org.lh.dmlj.schema.editor.common.Tools;

public class SetOrderDialog extends Dialog {

	private Button 	  btnFirst;
	private Button 	  btnLast;
	private Button 	  btnNext;
	private Button 	  btnPrior;
	
	private Button 	  btnSorted;
	private Label 	  lblMemberRecord;
	private Table	  tableMemberRecords;
	private Label 	  lblAvailableSortElements;
	private Table 	  tableAvailableSortElements;
	private Table 	  tableSelectedSortElements;
	private Button 	  btnAddSortElementOrDbkey;
	private Button 	  btnRemoveSortElementOrDbkey;
	private Button 	  btnAsc;
	private Button 	  btnDesc;
	private Button 	  btnUp;
	private Button 	  btnDown;
	private Label 	  lblKeyLength;
	private Label 	  lblKeylengthValue;
	private Button 	  btnNaturalSequence;
	private Button 	  btnCompressed;
	private Label 	  lblDuplicates;
	private Combo 	  comboDuplicatesOption;
	private Label lblSelectedSortElements;
	private Label lblIfNoSort;
	
	private Font 	  boldTableFont;
	private Font 	  italicTableFont;
	
	private Set 	  set;
	
	// the selected set order
	private SetOrder  setOrder;
	
	// a map holding the available sort elements for each member record
	private Map<String, java.util.List<String>> availableSortElements = new HashMap<>();
	
	// a map holding the selected sort elements for each member record
	private Map<String, java.util.List<String>> selectedSortElements = new HashMap<>();	
	
	// the selected natural sequence option (we keep it on a per member record basis)
	private Map<String, Boolean> naturalSequences = new HashMap<>();	
	
	// the selected compressed option (indexed sets can have only 1 member record)
	private boolean compressed;
	
	// the selected duplicates option (we keep it on a per member record basis)
	private Map<String, DuplicatesOption> duplicatesOptions = new HashMap<>();				
	
	// a map that allows to quickly locate the member roles involved + the index of the currently
	// selected member record
	private Map<String, MemberRole> members = new HashMap<>();
	private int 					selectedMemberRecordIndex = -1;
	private SortSequence dbkeySortSequence = null; // indexed sets only
	
	// a copy of the initial situation (member record specific data only)
	private Map<String, java.util.List<String>> copyOfSelectedSortElements = new HashMap<>();
	private Map<String, Boolean> 				copyOfNaturalSequences = new HashMap<>();
	private Map<String, DuplicatesOption> 		copyOfDuplicatesOptions = new HashMap<>();
	private SortSequence copyOfDbkeySortSequence = null; // indexed sets only
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SetOrderDialog(Shell parentShell, Set set) {
		super(parentShell);
		this.set = set;
	}

	private void addSortElement(int index) {
		
		// get the selected member record's name
		String recordName = 
			tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
		
		// given the index, get the element name from the member's list of available sort elements 
		// and remove the item from that list, as well as the dialog's list of available sort 
		// elements
		java.util.List<String> memberAvailableSortElements = availableSortElements.get(recordName);
		String elementName = memberAvailableSortElements.get(index);
		memberAvailableSortElements.remove(index);
		tableAvailableSortElements.remove(index);
		
		// create a sort element description and add it to both the member's list of selected sort 
		// elements and the dialog's list of selected sort elements
		StringBuilder p = new StringBuilder();
		p.append("ASC ");
		p.append(elementName);
		java.util.List<String> memberSelectedSortElements = selectedSortElements.get(recordName);		
		if (memberSelectedSortElements.isEmpty()) {
			tableSelectedSortElements.removeAll(); // make sure "DBKEY" is removed first
			dbkeySortSequence = null;
		}
		memberSelectedSortElements.add(p.toString());
		TableItem item = new TableItem(tableSelectedSortElements, SWT.NONE);
		item.setText(0, p.toString());		
		// change the font to normal in the member records table if exactly 1 sort element is 
		// selected for the member record (the font would have been bold so far)
		if (memberSelectedSortElements.size() == 1) {
			int i = 0;
			while (!tableMemberRecords.getItem(i).getText().equals(recordName)) {
				i += 1;
			}
			item = tableMemberRecords.getItems()[i];
			item.setFont(tableMemberRecords.getFont());			
		}
		
		// select the added item in the dialog's list of selected sort elements and make sure it's 
		// visible
		tableSelectedSortElements.select(tableSelectedSortElements.getItemCount() - 1);
		tableSelectedSortElements.showSelection();
		
		enableAndDisable();
		
	}

	private void captureData() {
		
		// this method does NOT deal with the sort elements related stuff; this is done in the add/
		// remove/move up/down sort element and flip sort sequence methods
		
		// set the selected set order
		if (btnFirst.getSelection()) {
			setOrder = SetOrder.FIRST;
		} else if (btnLast.getSelection()) {
			setOrder = SetOrder.LAST;
		} else if (btnNext.getSelection()) {
			setOrder = SetOrder.NEXT;
		} else if (btnPrior.getSelection()) {
			setOrder = SetOrder.PRIOR;
		} else {
			setOrder = SetOrder.SORTED;
		}		
		
		// get the selected member record's name
		String recordName = 
			tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
		
		// set the selected natural sequence option		
		naturalSequences.put(recordName, Boolean.valueOf(btnNaturalSequence.getSelection()));
				
		// set the selected compressed option
		compressed = btnCompressed.getSelection();		
		
		// set the selected duplicates option						
		String p = comboDuplicatesOption.getText().replaceAll(" ", "_");
		DuplicatesOption duplicatesOption = DuplicatesOption.valueOf(p);
		duplicatesOptions.put(recordName, duplicatesOption);				
		
	}

	private void captureDataEnableAndDisable() {
		captureData();
		enableAndDisable();
	}

	private void collectData() {
	
		setOrder = set.getOrder();		
		
		for (MemberRole memberRole : set.getMembers()) {
			
			SchemaRecord record = memberRole.getRecord();
			String recordName = Tools.removeTrailingUnderscore(record.getName());
			members.put(recordName, memberRole);
			
			// gather all available (and eligable) sort elements
			java.util.List<String> memberAvailableSortElements = 
				availableSortElements.get(recordName);
			if (memberAvailableSortElements == null) {
				memberAvailableSortElements = new ArrayList<>();
				availableSortElements.put(recordName, memberAvailableSortElements);
			}			
			for (Element element : record.getElements()) {
				boolean selected = false;			
				if (set.getOrder() == SetOrder.SORTED) {
					for (KeyElement keyElement : memberRole.getSortKey().getElements()) {
						if (keyElement.getElement() == element) {
							selected = true;
							break;
						}
					}
				}
				if (!selected && !element.getName().equals("FILLER") &&
					!Tools.isInvolvedInRedefines(element) && !Tools.isInvolvedInOccurs(element) &&
					element.getLevel() != 88) {
					
					memberAvailableSortElements.add(element.getName());
				}
			}
			
			// gather the selected sort elements, natural sequence, compressed and duplicates option
			java.util.List<String> memberSelectedSortElements = 
				selectedSortElements.get(recordName);
			if (memberSelectedSortElements == null) {
				memberSelectedSortElements = new ArrayList<>();
				selectedSortElements.put(recordName, memberSelectedSortElements);
			}						
			if (set.getOrder() == SetOrder.SORTED) {
				for (KeyElement keyElement : memberRole.getSortKey().getElements()) {					
					if (!keyElement.isDbkey()) {
						StringBuilder p = new StringBuilder();
						if (keyElement.getSortSequence() == SortSequence.ASCENDING) {
							p.append("ASC ");
						} else {
							p.append("DESC ");
						}					
						p.append(keyElement.getElement().getName());
						memberSelectedSortElements.add(p.toString());
					} else {
						dbkeySortSequence = keyElement.getSortSequence();
					}
				}
				naturalSequences.put(recordName, memberRole.getSortKey().isNaturalSequence());
				compressed = memberRole.getSortKey().isCompressed();
				duplicatesOptions.put(recordName, memberRole.getSortKey().getDuplicatesOption());
			} else {
				// the set is not sorted; set the defaults
				naturalSequences.put(recordName, false);
				compressed = false;
				duplicatesOptions.put(recordName, DuplicatesOption.NOT_ALLOWED);
			}
							
		}	
		
		// create a copy of the initial situation (member record specific data only)		
		for (String key : selectedSortElements.keySet()) {			
			copyOfSelectedSortElements.put(key, new ArrayList<>(selectedSortElements.get(key)));
		}
		for (String key : naturalSequences.keySet()) {
			Boolean value = Boolean.valueOf(naturalSequences.get(key).booleanValue());
			copyOfNaturalSequences.put(key, value);
		}		
		for (String key : duplicatesOptions.keySet()) {
			copyOfDuplicatesOptions.put(key, duplicatesOptions.get(key));
		}
		copyOfDbkeySortSequence = dbkeySortSequence;
		
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Edit set order for " + Tools.removeTrailingUnderscore(set.getName()));
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
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
		gridLayout.numColumns = 4;
		
		btnFirst = new Button(container, SWT.RADIO);
		btnFirst.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				captureDataEnableAndDisable();
			}
		});
		btnFirst.setText("FIRST");
		
		btnLast = new Button(container, SWT.RADIO);
		btnLast.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				captureDataEnableAndDisable();
			}
		});
		btnLast.setText("LAST");
		
		btnNext = new Button(container, SWT.RADIO);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				captureDataEnableAndDisable();
			}
		});
		btnNext.setText("NEXT");
		
		btnPrior = new Button(container, SWT.RADIO);
		btnPrior.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				captureDataEnableAndDisable();
			}
		});
		btnPrior.setText("PRIOR");
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_label.verticalIndent = 10;
		gd_label.horizontalIndent = 15;
		label.setLayoutData(gd_label);
		
		btnSorted = new Button(container, SWT.RADIO);
		btnSorted.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		btnSorted.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				captureDataEnableAndDisable();
			}
		});
		btnSorted.setText("SORTED:");
		
		Composite compositeSorted = new Composite(container, SWT.NONE);
		GridLayout gl_compositeSorted = new GridLayout(5, false);
		gl_compositeSorted.marginWidth = 0;
		compositeSorted.setLayout(gl_compositeSorted);
		GridData gd_compositeSorted = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		gd_compositeSorted.horizontalIndent = 15;
		compositeSorted.setLayoutData(gd_compositeSorted);
		
		lblMemberRecord = new Label(compositeSorted, SWT.NONE);
		lblMemberRecord.setText("Member record:");
		
		lblAvailableSortElements = new Label(compositeSorted, SWT.NONE);
		GridData gd_lblSortElementsOr = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblSortElementsOr.horizontalIndent = 10;
		lblAvailableSortElements.setLayoutData(gd_lblSortElementsOr);
		lblAvailableSortElements.setText("Available sort elements:");
		new Label(compositeSorted, SWT.NONE);
		
		lblSelectedSortElements = new Label(compositeSorted, SWT.NONE);
		lblSelectedSortElements.setEnabled(false);
		lblSelectedSortElements.setText("Selected sort elements\u00B9:");
		new Label(compositeSorted, SWT.NONE);
		
		tableMemberRecords = new Table(compositeSorted, SWT.BORDER);
		tableMemberRecords.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_tableMemberRecords = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 6);
		gd_tableMemberRecords.widthHint = 125;
		gd_tableMemberRecords.heightHint = 75;
		tableMemberRecords.setLayoutData(gd_tableMemberRecords);
		tableMemberRecords.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				memberRecordSelectionChanged(true);
			}
		});		
		
		tableAvailableSortElements = new Table(compositeSorted, SWT.BORDER);
		tableAvailableSortElements.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tableAvailableSortElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		GridData gd_listAvailableSortElementsOrDbkey = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 4);
		gd_listAvailableSortElementsOrDbkey.horizontalIndent = 10;
		gd_listAvailableSortElementsOrDbkey.widthHint = 150;
		gd_listAvailableSortElementsOrDbkey.heightHint = 75;
		tableAvailableSortElements.setLayoutData(gd_listAvailableSortElementsOrDbkey);
		
		btnAddSortElementOrDbkey = new Button(compositeSorted, SWT.NONE);
		btnAddSortElementOrDbkey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addSortElement(tableAvailableSortElements.getSelectionIndex());
			}
		});
		btnAddSortElementOrDbkey.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 3));
		btnAddSortElementOrDbkey.setText(">");
		
		tableSelectedSortElements = new Table(compositeSorted, SWT.BORDER);
		tableSelectedSortElements.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tableSelectedSortElements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		GridData gd_listSelectedSortElementsOrDbkey = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 4);
		gd_listSelectedSortElementsOrDbkey.widthHint = 150;
		gd_listSelectedSortElementsOrDbkey.heightHint = 75;
		tableSelectedSortElements.setLayoutData(gd_listSelectedSortElementsOrDbkey);
		
		btnAsc = new Button(compositeSorted, SWT.NONE);
		btnAsc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				flipSortSequence(tableSelectedSortElements.getSelectionIndex());
			}
		});
		GridData gd_btnAsc = new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1);
		gd_btnAsc.widthHint = 40;
		btnAsc.setLayoutData(gd_btnAsc);
		btnAsc.setText("ASC");
		
		btnDesc = new Button(compositeSorted, SWT.NONE);
		btnDesc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				flipSortSequence(tableSelectedSortElements.getSelectionIndex());
			}
		});
		GridData gd_btnDesc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDesc.widthHint = 40;
		btnDesc.setLayoutData(gd_btnDesc);
		btnDesc.setText("DESC");
		
		btnUp = new Button(compositeSorted, SWT.NONE);
		btnUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveSortElementUp(tableSelectedSortElements.getSelectionIndex());
			}
		});
		GridData gd_btnUp = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnUp.widthHint = 40;
		btnUp.setLayoutData(gd_btnUp);
		btnUp.setText("Up");
		
		btnRemoveSortElementOrDbkey = new Button(compositeSorted, SWT.NONE);
		btnRemoveSortElementOrDbkey.setText("<");
		btnRemoveSortElementOrDbkey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				removeSortElement(tableSelectedSortElements.getSelectionIndex());
			}
		});
		
		
		btnDown = new Button(compositeSorted, SWT.NONE);
		btnDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveSortElementDown(tableSelectedSortElements.getSelectionIndex());
			}
		});
		GridData gd_btnDown = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDown.widthHint = 40;
		btnDown.setLayoutData(gd_btnDown);
		btnDown.setText("Down");
		new Label(compositeSorted, SWT.NONE);
		new Label(compositeSorted, SWT.NONE);
		
		Composite compositeKeyLength = new Composite(compositeSorted, SWT.NONE);
		GridData gd_compositeKeyLength = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_compositeKeyLength.verticalIndent = -5;
		compositeKeyLength.setLayoutData(gd_compositeKeyLength);
		GridLayout gl_compositeKeyLength = new GridLayout(2, false);
		gl_compositeKeyLength.marginWidth = 0;
		gl_compositeKeyLength.horizontalSpacing = 0;
		compositeKeyLength.setLayout(gl_compositeKeyLength);
		
		lblKeyLength = new Label(compositeKeyLength, SWT.NONE);
		lblKeyLength.setEnabled(false);
		lblKeyLength.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKeyLength.setText("Key length:");
		
		lblKeylengthValue = new Label(compositeKeyLength, SWT.RIGHT);
		lblKeylengthValue.setEnabled(false);
		lblKeylengthValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeSorted, SWT.NONE);
		
		Composite composite_1 = new Composite(compositeSorted, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.marginHeight = 0;
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_composite_1.horizontalIndent = 10;
		composite_1.setLayoutData(gd_composite_1);
		
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		
		btnNaturalSequence = new Button(composite_1, SWT.CHECK);
		btnNaturalSequence.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				captureDataEnableAndDisable();
			}
		});
		btnNaturalSequence.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnNaturalSequence.setText("Natural sequence");
		
		btnCompressed = new Button(composite_1, SWT.CHECK);
		btnCompressed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				captureDataEnableAndDisable();
			}
		});
		btnCompressed.setText("Compressed");
		
		Composite composite_2 = new Composite(compositeSorted, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		GridLayout gl_composite_2 = new GridLayout(2, false);
		gl_composite_2.marginHeight = 0;
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		new Label(composite_2, SWT.NONE);
		
		comboDuplicatesOption = new Combo(composite_2, SWT.READ_ONLY);
		comboDuplicatesOption.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				captureDataEnableAndDisable();
			}
		});
		GridData gd_comboDuplicatesOption = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 3);
		gd_comboDuplicatesOption.widthHint = 125;
		comboDuplicatesOption.setLayoutData(gd_comboDuplicatesOption);
		
		lblDuplicates = new Label(composite_2, SWT.NONE);
		lblDuplicates.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblDuplicates.setText("Duplicates:");
		new Label(composite_2, SWT.NONE);
		
		lblIfNoSort = new Label(compositeSorted, SWT.NONE);
		lblIfNoSort.setEnabled(false);
		GridData gd_lblIfNoSort = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_lblIfNoSort.verticalIndent = 10;
		lblIfNoSort.setLayoutData(gd_lblIfNoSort);
		lblIfNoSort.setText("\u00B9If no sort elements are selected, the set is sorted by dbkey (indexed sets only).");
		
		collectData();
		initializeControls();

		return container;
		
	}
	
	@Override
	public boolean close() {
		// dispose of the fonts created before closing the window
		boldTableFont.dispose();
		if (italicTableFont != null) {
			italicTableFont.dispose();
		}
		return super.close();
	}
	
	private void enableAndDisable() {
		
		// get the selected member record's name
		String recordName = 
			tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
		SchemaRecord record = members.get(recordName).getRecord();
		
		// compute and set the key length
		java.util.List<String> memberSelectedSortElements = selectedSortElements.get(recordName);
		int keyLength = 0;
		if (isSortedByDbkey()) {
			keyLength = 4; // indexed set sorted on dbkey
		} else {			
			for (String aSelectedSortElement : memberSelectedSortElements) {
				int i = aSelectedSortElement.indexOf(" ");
				String elementName = aSelectedSortElement.substring(i + 1);
				Element element = record.getElement(elementName);
				keyLength += element.getLength();
			}
		}
		lblKeylengthValue.setText(String.valueOf(keyLength));		
		
		// enable or disable the sorted set related components
		boolean sorted = setOrder == SetOrder.SORTED;
		boolean sortElementSelected = !selectedSortElements.get(recordName).isEmpty();
		lblMemberRecord.setEnabled(sorted);
		lblAvailableSortElements.setEnabled(sorted);
		lblSelectedSortElements.setEnabled(sorted);
		tableMemberRecords.setEnabled(sorted);
		tableAvailableSortElements.setEnabled(sorted);
		boolean keyLengthWouldExceed256BytesForSelectedElement = false;
		for (int i = 0; i < tableAvailableSortElements.getItemCount(); i++) {
			TableItem item = tableAvailableSortElements.getItem(i);
			Element element = record.getElement(item.getText(0));
			if (keyLength + element.getLength() > 256) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
				if (i == tableAvailableSortElements.getSelectionIndex()) {
					keyLengthWouldExceed256BytesForSelectedElement = true;
				}
			}
		}
		tableSelectedSortElements.setEnabled(sorted);
		lblIfNoSort.setEnabled(sorted);
		btnAddSortElementOrDbkey.setEnabled(sorted && 
											tableAvailableSortElements.getSelectionCount() == 1 &&
											!keyLengthWouldExceed256BytesForSelectedElement);		
		btnRemoveSortElementOrDbkey.setEnabled(sorted &&
											   tableSelectedSortElements.getSelectionCount() == 1 &&
											   sortElementSelected);
		
		// for indexed sets, show "ASC/DESC DBKEY" if sorted by dbkey
		if (isSortedByDbkey()) {
			
			// we use an italic font for the DBKEY replacement sort element; create this font if not
			// already done
			if (italicTableFont == null) {
				Font font = tableMemberRecords.getFont();
				FontData[] fontData = font.getFontData();
				fontData[0].setStyle(SWT.ITALIC);
				italicTableFont = new Font(font.getDevice(), fontData);
			}
			
			tableSelectedSortElements.removeAll();
			TableItem item = new TableItem(tableSelectedSortElements, SWT.NONE);
			if (dbkeySortSequence == SortSequence.ASCENDING) {
				item.setText("ASC DBKEY");
			} else {
				item.setText("DESC DBKEY");
			}
			item.setFont(italicTableFont);	
		}
		
		String selectedSortElement =
			tableSelectedSortElements.getSelectionCount() == 1 ?
			tableSelectedSortElements.getItem(tableSelectedSortElements.getSelectionIndex())
									 .getText(0) : 
			null;
		// ASCENDING must be specified for native VSAM sets -> disable the ASC and DESC buttons
		btnAsc.setEnabled(sorted && !set.isVsam() &&
						  tableSelectedSortElements.getSelectionCount() == 1 &&
				  		  selectedSortElement.startsWith("DESC ") ||
				  		  tableSelectedSortElements.getItemCount() == 1 &&
				  		  tableSelectedSortElements.getItem(0).getText(0).equals("DESC DBKEY"));
		btnDesc.setEnabled(sorted && !set.isVsam() && 
						   tableSelectedSortElements.getSelectionCount() == 1 &&
				   		   selectedSortElement.startsWith("ASC ") ||
					  	   tableSelectedSortElements.getItemCount() == 1 &&
					  	   tableSelectedSortElements.getItem(0).getText(0).equals("ASC DBKEY"));
		btnUp.setEnabled(sorted && tableSelectedSortElements.getSelectionCount() == 1 &&
				 		 tableSelectedSortElements.getSelectionIndex() > 0);
		btnDown.setEnabled(sorted && tableSelectedSortElements.getSelectionCount() == 1 &&
		 		   		   tableSelectedSortElements.getSelectionIndex() < (tableSelectedSortElements.getItemCount() - 1));
		lblKeyLength.setEnabled(sorted);
		lblKeylengthValue.setEnabled(sorted);
		btnNaturalSequence.setEnabled(sorted);
		btnCompressed.setEnabled(sorted && set.getMode() == SetMode.INDEXED);
		lblDuplicates.setEnabled(sorted);
		comboDuplicatesOption.setEnabled(sorted && dbkeySortSequence == null);
		
		// OK button
		Button okButton = getButton(IDialogConstants.OK_ID);		
		if (isDataValidAndComplete()) {
			okButton.setEnabled(isAnythingChanged());
		} else {
			okButton.setEnabled(false);
		}
		
	}
	
	private void flipSortSequence(int index) {
		
		// get the selected member record's name
		String recordName = 
			tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
				
		// given the index, flip the sort sequence in both the member's list of selected elements 
		// and the dialog's list of selected elements
		java.util.List<String> memberSelectedSortElements = selectedSortElements.get(recordName);
		if (!isSortedByDbkey()) {
			TableItem item = tableSelectedSortElements.getItem(index);
			String sortElementDescription = memberSelectedSortElements.get(index);				
			String newSortSequence = sortElementDescription.startsWith("ASC ") ? "DESC " : "ASC ";
			String elementName = 
				sortElementDescription.substring(sortElementDescription.indexOf(" ") + 1);		
			StringBuilder p = new StringBuilder();
			p.append(newSortSequence);
			p.append(elementName);		
			memberSelectedSortElements.set(index, p.toString());		
			item.setText(0, p.toString());
		} else {
			// sorted by dbkey (index might be equal to -1 since no item has to be selected)
			if (dbkeySortSequence == SortSequence.ASCENDING) {
				dbkeySortSequence = SortSequence.DESCENDING;
			} else {
				dbkeySortSequence = SortSequence.ASCENDING;
			}
		}
		
		enableAndDisable();
		
	}

	private DuplicatesOption getDuplicatesOption(String recordName) {
		Assert.isTrue(duplicatesOptions.containsKey(recordName));
		return duplicatesOptions.get(recordName);
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(625, 450);
	}
	
	public SetOrder getSetOrder() {
		return setOrder;
	}
	
	public ISortKeyDescription[] getSortKeyDescriptions() {
		ISortKeyDescription[] sortKeyDecriptions = new ISortKeyDescription[set.getMembers().size()];
		final SetOrderDialog fSetOrderDialog = this;
		for (int i = 0; i < set.getMembers().size(); i++) {
			MemberRole memberRole = set.getMembers().get(i);
			final String recordName = 
				Tools.removeTrailingUnderscore(memberRole.getRecord().getName());
			sortKeyDecriptions[i] = new ISortKeyDescription() {

				@Override
				public String[] getElementNames() {
					if (!isSortedByDbkey()) {
						List<String> memberSelectedSortElements = selectedSortElements.get(recordName);
						String[] sortElementNames = new String[memberSelectedSortElements.size()];
						for (int i = 0; i < sortElementNames.length; i++) {
							String sortElementDescription = memberSelectedSortElements.get(i);
							int j = sortElementDescription.indexOf(" ");
							sortElementNames[i] = sortElementDescription.substring(j + 1);
						}
						return sortElementNames;
					} else {
						return new String[] {ISortKeyDescription.DBKEY_ELEMENT};
					}
				}

				@Override
				public SortSequence[] getSortSequences() {
					if (!isSortedByDbkey()) {
						List<String> memberSelectedSortElements = selectedSortElements.get(recordName);
						SortSequence[] sortSequences = 
							new SortSequence[memberSelectedSortElements.size()];
						for (int i = 0; i < sortSequences.length; i++) {
							String sortElementDescription = memberSelectedSortElements.get(i);
							sortSequences[i] = sortElementDescription.startsWith("ASC ") ? 
											   SortSequence.ASCENDING : SortSequence.DESCENDING;
						}
						return sortSequences;
					} else {
						return new SortSequence[] {dbkeySortSequence};
					}
				}

				@Override
				public DuplicatesOption getDuplicatesOption() {
					if (!isSortedByDbkey()) {
						return fSetOrderDialog.getDuplicatesOption(recordName);
					} else {
						return DuplicatesOption.NOT_ALLOWED;
					}
				}

				@Override
				public boolean isCompressed() {
					return compressed;
				}

				@Override
				public boolean isNaturalSequence() {
					return fSetOrderDialog.isNaturalSequence(recordName);
				}
				
			};
		}
		return sortKeyDecriptions;
	}

	private void initializeControls() {		
		
		// select the applicable set order radio button
		if (set.getOrder() == SetOrder.FIRST) {
			// FIRST
			btnFirst.setSelection(true);
		} else if (set.getOrder() == SetOrder.LAST) {
			// LAST
			btnLast.setSelection(true);
		} else if (set.getOrder() == SetOrder.NEXT) {
			// NEXT
			btnNext.setSelection(true);
		} else if (set.getOrder() == SetOrder.PRIOR) {
			// PRIOR
			btnPrior.setSelection(true);
		} else {
			// SORTED
			btnSorted.setSelection(true);
		}
		
		// VSAM indexes are always SORTED
		if (set.isVsam()) {
			btnFirst.setEnabled(false);
			btnLast.setEnabled(false);
			btnNext.setEnabled(false);
			btnPrior.setEnabled(false);
		}
		
		// we use a bold font to indicate that no sort elements are selected for a given member
		// record; create that font
		Font font = tableMemberRecords.getFont();
		FontData[] fontData = font.getFontData();
		fontData[0].setStyle(SWT.BOLD);
		boldTableFont = new Font(font.getDevice(), fontData);
		
		// fill the list of member records, sorted alphabetically, and select the first one				
		java.util.List<String> recordNames = new ArrayList<>(members.keySet());
		Collections.sort(recordNames);		
		for (String recordName : recordNames) {
			TableItem item = new TableItem(tableMemberRecords, SWT.NONE);
			item.setText(0, recordName);
			java.util.List<String> memberSelectedSortElements = selectedSortElements.get(recordName);
			if (memberSelectedSortElements.isEmpty() && set.getMode() != SetMode.INDEXED) {
				item.setFont(boldTableFont);
			}
		} 
		tableMemberRecords.select(0);
		
		// fill the combo containing the duplicates option values taking care of limitations for
		// both indexed sets and VSAM indexes
		if (set.getMode() != SetMode.VSAM_INDEX) {
			comboDuplicatesOption.add(DuplicatesOption.FIRST.toString());
			comboDuplicatesOption.add(DuplicatesOption.LAST.toString());
		}
		comboDuplicatesOption.add(DuplicatesOption.NOT_ALLOWED.toString().replaceAll("_", " "));
		if (set.getMode() == SetMode.INDEXED) {
			// only indexed sets can have their duplicates sorted by dbkey
			comboDuplicatesOption.add(DuplicatesOption.BY_DBKEY.toString().replaceAll("_", " "));
		}
		if (set.getMode() == SetMode.VSAM_INDEX) {
			// only VSAM indexes can have their duplicates unordered
			comboDuplicatesOption.add(DuplicatesOption.UNORDERED.toString());
		}
		
		// initialize the sorted set controls for the (alphabetically) first member record
		memberRecordSelectionChanged(false);
		
	}
	
	private boolean isAnythingChanged() {
		
		// set order
		if (setOrder != set.getOrder()) {
			return true; 
		}
		
		// if the set is not sorted, the sort related data is irrelevant
		if (setOrder != SetOrder.SORTED) {
			return false; 
		}
		
		 // compressed option (indexed sets only)
		if (set.getMode() == SetMode.INDEXED &&
			compressed != set.getMembers().get(0).getSortKey().isCompressed()) {
			
			return true;
		}
		
		// traverse all members and see if anything was changed
		for (MemberRole memberRole : set.getMembers()) {
			
			// get the record name
			String recordName = Tools.removeTrailingUnderscore(memberRole.getRecord().getName());
			
			// selected sort elements
			java.util.List<String> memberSelectedSortElements = 
				selectedSortElements.get(recordName);
			java.util.List<String> copyOfMemberSelectedSortElements = 
				copyOfSelectedSortElements.get(recordName);
			if (!memberSelectedSortElements.equals(copyOfMemberSelectedSortElements)) {
				return true;
			}
			
			// natural sequence
			boolean naturalSequence = naturalSequences.get(recordName);
			boolean copyOfNaturalSequence = copyOfNaturalSequences.get(recordName);
			if (naturalSequence != copyOfNaturalSequence) {
				return true;
			}
			
			// duplicates option
			DuplicatesOption duplicatesOption = duplicatesOptions.get(recordName);
			DuplicatesOption copyOfDuplicatesOption = copyOfDuplicatesOptions.get(recordName);
			if (duplicatesOption != copyOfDuplicatesOption) {
				return true;
			}
			
		}
		if (dbkeySortSequence != copyOfDbkeySortSequence) {
			return true;
		}
		
		// nothing has changed
		return false;
		
	}

	private boolean isDataValidAndComplete() {
		
		// the data is complete if either the set is not sorted, or the set is sorted and at least 1
		// sort element is selected for each member record (i.e. if the set is chained)
		if (setOrder != SetOrder.SORTED) {
			return true;
		}		
		for (java.util.List<String> memberSelectedSortElements : selectedSortElements.values()) {
			if (memberSelectedSortElements.isEmpty() && set.getMode() != SetMode.INDEXED) {
				return false;
			}
		}
		
		// the data is valid and complete
		return true;
		
	}

	private boolean isNaturalSequence(String recordName) {
		Assert.isTrue(naturalSequences.containsKey(recordName));
		return naturalSequences.get(recordName).booleanValue();		
	}
	
	private boolean isSortedByDbkey() {
		// will return true only when the set is indexed and no sort elements are selected
		return dbkeySortSequence != null;
	}

	private void memberRecordSelectionChanged(boolean callEnableOrDisable) {
		
		// no changes needed if the previously selected record is still selected
		if (tableMemberRecords.getSelectionIndex() == selectedMemberRecordIndex) {
			return; 
		}
		selectedMemberRecordIndex = tableMemberRecords.getSelectionIndex();
		
		// get the selected member record's name
		String recordName = 
			tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
		
		// fill the list of available sort elements for the selected member record
		tableAvailableSortElements.removeAll();
		java.util.List<String> memberAvailableSortElements = availableSortElements.get(recordName);
		for (String elementName : memberAvailableSortElements) {
			TableItem item = new TableItem(tableAvailableSortElements, SWT.NONE);
			item.setText(0, elementName);
		}
		
		// fill the list of selected sort elements for the selected member record
		tableSelectedSortElements.removeAll();
		java.util.List<String> memberSelectedSortElements = selectedSortElements.get(recordName);
		for (String sortElementDescription : memberSelectedSortElements) {			
			TableItem item = new TableItem(tableSelectedSortElements, SWT.NONE);
			item.setText(0, sortElementDescription); // prefixed with sort sequence
		}
		
		// set the natural sequence option		
		btnNaturalSequence.setSelection(naturalSequences.get(recordName).booleanValue());
		
		// set the compressed option
		btnCompressed.setSelection(compressed);
		
		// select the appropriate duplicates option		
		DuplicatesOption firstMemberDuplicatesOption = duplicatesOptions.get(recordName);
		if (set.getMode() != SetMode.VSAM_INDEX) {
			if (firstMemberDuplicatesOption == DuplicatesOption.FIRST) {
				comboDuplicatesOption.select(0);
			} else if (firstMemberDuplicatesOption == DuplicatesOption.LAST) {
				comboDuplicatesOption.select(1);
			} else if (firstMemberDuplicatesOption == DuplicatesOption.NOT_ALLOWED) {
				comboDuplicatesOption.select(2);
			} else {
				comboDuplicatesOption.select(3);
			}
		} else {
			if (firstMemberDuplicatesOption == DuplicatesOption.NOT_ALLOWED) {
				comboDuplicatesOption.select(0);
			} else {
				comboDuplicatesOption.select(1);
			}
		}
		
		if (callEnableOrDisable) {
			enableAndDisable();
		}
		
	}
	
	private void moveSortElementDown(int index) {		
		
		// get the selected member record's name
		String recordName = 
			tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
						
		// move the element 1 place down in the member's list of selected sort elements
		java.util.List<String> memberSelectedSortElements = selectedSortElements.get(recordName);			
		String sortElementDescription = memberSelectedSortElements.get(index);		
		memberSelectedSortElements.remove(index);		
		memberSelectedSortElements.add(index + 1, sortElementDescription);
		
		// refresh the dialog's list of selected sort elements
		tableSelectedSortElements.removeAll();
		for (String selectedSortElement : memberSelectedSortElements) {
			TableItem item = new TableItem(tableSelectedSortElements, SWT.NONE);
			item.setText(0, selectedSortElement);
		}
		
		// select the added item in the dialog's list of selected sort elements and make sure it's 
		// visible
		tableSelectedSortElements.select(index + 1);
		tableSelectedSortElements.showSelection();		
		
		enableAndDisable();
		
	}	
	
	private void moveSortElementUp(int index) {		
		
		// get the selected member record's name
		String recordName = 
			tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
		
		// move the element 1 place up in the member's list of selected sort elements		
		java.util.List<String> memberSelectedSortElements = selectedSortElements.get(recordName);			
		String sortElementDescription = memberSelectedSortElements.get(index);		
		memberSelectedSortElements.remove(index);				
		memberSelectedSortElements.add(index - 1, sortElementDescription);
		
		// refresh the dialog's list of selected sort elements
		tableSelectedSortElements.removeAll();
		for (String selectedSortElement : memberSelectedSortElements) {
			TableItem item = new TableItem(tableSelectedSortElements, SWT.NONE);
			item.setText(0, selectedSortElement);
		}
		
		// select the added item in the dialog's list of selected sort elements and make sure it's 
		// visible
		tableSelectedSortElements.select(index - 1);
		tableSelectedSortElements.showSelection();		
		
		enableAndDisable();
		
	}

	private void removeSortElement(int index) {
	
		// get the selected member record's name
		String recordName = 
			tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
		
		// given the index, get the element name and remove the item from both the member's list of 
		// selected elements and the dialog's list of selected elements
		java.util.List<String> memberSelectedSortElements = selectedSortElements.get(recordName);		
		String sortElementDescription = memberSelectedSortElements.get(index);
		memberSelectedSortElements.remove(index);		
		tableSelectedSortElements.remove(index);
		// change the font to bold in the member records table if no sort elements are selected for
		// the member record (chained sets only)
		if (memberSelectedSortElements.isEmpty() && set.getMode() == SetMode.CHAINED) {
			int i = 0;
			while (!tableMemberRecords.getItem(i).getText().equals(recordName)) {
				i += 1;
			}
			TableItem item = tableMemberRecords.getItems()[i];
			item.setFont(boldTableFont);			
		}
		// reset the sort sequence when sorted on dbkey (indexed sets only); make sure the duplicates
		// option is set to 'not allowed' as well
		if (memberSelectedSortElements.isEmpty() && set.getMode() == SetMode.INDEXED) {
			dbkeySortSequence = copyOfDbkeySortSequence == SortSequence.DESCENDING ? 
								SortSequence.DESCENDING : SortSequence.ASCENDING;
			duplicatesOptions.put(recordName, DuplicatesOption.NOT_ALLOWED);
			comboDuplicatesOption.select(2);
		}
		
		// get the element name
		String elementName = 
			sortElementDescription.substring(sortElementDescription.indexOf(" ") + 1);
		
		// insert the element in both the member's list of available sort elements and the dialog's
		// list of available sort elements
		java.util.List<String> memberAvailableSortElements = availableSortElements.get(recordName);		
		SchemaRecord record = members.get(recordName).getRecord();
		int i = record.getElements().indexOf(record.getElement(elementName));
		int insertionIndex = 0;
		for (String otherElementName : memberAvailableSortElements) {
			int j = record.getElements().indexOf(record.getElement(otherElementName));
			if (j < i) {
				insertionIndex += 1;
			}
		}
		memberAvailableSortElements.add(insertionIndex, elementName);
		tableAvailableSortElements.removeAll();
		for (String availableSortElement : memberAvailableSortElements) {
			TableItem item = new TableItem(tableAvailableSortElements, SWT.NONE);
			item.setText(0, availableSortElement);
		}
		
		// select the added item in the dialog's list of selected sort elements and make sure it is 
		// visible
		tableAvailableSortElements.select(insertionIndex);
		tableAvailableSortElements.showSelection();
		
		enableAndDisable();		
		
	}
}
