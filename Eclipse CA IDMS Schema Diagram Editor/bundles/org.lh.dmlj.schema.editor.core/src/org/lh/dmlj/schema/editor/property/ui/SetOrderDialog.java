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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
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
	private static final String ASC = "ASC ";
	private static final String DESC = "DESC ";
	
	private final Set set;
	private SetOrder setOrder;
	private final Map<String, java.util.List<String>> availableSortElements = new HashMap<>();
	private final Map<String, java.util.List<String>> selectedSortElements = new HashMap<>();
	private final Map<String, Boolean> naturalSequences = new HashMap<>();	
	private boolean compressed;
	private final Map<String, DuplicatesOption> duplicatesOptions = new HashMap<>();	
	private final Map<String, MemberRole> members = new HashMap<>();
	private int 	selectedMemberRecordIndex = -1;
	private SortSequence dbkeySortSequence = null;
	private final Map<String, java.util.List<String>> copyOfSelectedSortElements = new HashMap<>();
	private final Map<String, Boolean> copyOfNaturalSequences = new HashMap<>();
	private final Map<String, DuplicatesOption> copyOfDuplicatesOptions = new HashMap<>();
	private SortSequence copyOfDbkeySortSequence = null;
	
	private Button btnFirst;
	private Button btnLast;
	private Button btnNext;
	private Button btnPrior;
	private Button btnSorted;
	private Label lblMemberRecord;
	private Table tableMemberRecords;
	private Label lblAvailableSortElements;
	private Table tableAvailableSortElements;
	private Table tableSelectedSortElements;
	private Button btnAddSortElementOrDbkey;
	private Button btnRemoveSortElementOrDbkey;
	private Button btnAsc;
	private Button btnDesc;
	private Button btnUp;
	private Button btnDown;
	private Label lblKeyLength;
	private Label lblKeylengthValue;
	private Button btnNaturalSequence;
	private Button btnCompressed;
	private Label lblDuplicates;
	private Combo comboDuplicatesOption;
	private Label lblSelectedSortElements;
	private Label lblIfNoSort;
	private Font boldTableFont;
	private Font italicTableFont;
		
	public SetOrderDialog(Shell parentShell, Set set) {
		super(parentShell);
		this.set = set;
		setShellStyle(getShellStyle() | SWT.RESIZE); 
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Edit set order for " + Tools.removeTrailingUnderscore(set.getName()));
	}
		
	@Override
	protected Point getInitialSize() {
		return new Point(625, 450);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		var container = (Composite) super.createDialogArea(parent);
		var gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 4;
		
		btnFirst = new Button(container, SWT.RADIO);
		btnFirst.addSelectionListener(createSelectionListener(this::captureDataEnableAndDisable));
		btnFirst.setText("FIRST");
		
		btnLast = new Button(container, SWT.RADIO);
		btnLast.addSelectionListener(createSelectionListener(this::captureDataEnableAndDisable));
		btnLast.setText("LAST");
		
		btnNext = new Button(container, SWT.RADIO);
		btnNext.addSelectionListener(createSelectionListener(this::captureDataEnableAndDisable));
		btnNext.setText("NEXT");
		
		btnPrior = new Button(container, SWT.RADIO);
		btnPrior.addSelectionListener(createSelectionListener(this::captureDataEnableAndDisable));
		btnPrior.setText("PRIOR");
		
		var label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		var gdLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gdLabel.verticalIndent = 10;
		gdLabel.horizontalIndent = 15;
		label.setLayoutData(gdLabel);
		
		btnSorted = new Button(container, SWT.RADIO);
		btnSorted.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		btnSorted.addSelectionListener(createSelectionListener(this::captureDataEnableAndDisable));
		btnSorted.setText("SORTED:");
		
		var compositeSorted = new Composite(container, SWT.NONE);
		var glCompositeSorted = new GridLayout(5, false);
		glCompositeSorted.marginWidth = 0;
		compositeSorted.setLayout(glCompositeSorted);
		var gdCompositeSorted = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		gdCompositeSorted.horizontalIndent = 15;
		compositeSorted.setLayoutData(gdCompositeSorted);
		
		lblMemberRecord = new Label(compositeSorted, SWT.NONE);
		lblMemberRecord.setText("Member record:");
		
		lblAvailableSortElements = new Label(compositeSorted, SWT.NONE);
		var gdLblSortElementsOr = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdLblSortElementsOr.horizontalIndent = 10;
		lblAvailableSortElements.setLayoutData(gdLblSortElementsOr);
		lblAvailableSortElements.setText("Available sort elements:");
		new Label(compositeSorted, SWT.NONE);
		
		lblSelectedSortElements = new Label(compositeSorted, SWT.NONE);
		lblSelectedSortElements.setEnabled(false);
		lblSelectedSortElements.setText("Selected sort elements\u00B9:");
		new Label(compositeSorted, SWT.NONE);
		
		tableMemberRecords = new Table(compositeSorted, SWT.BORDER);
		tableMemberRecords.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		var gdTableMemberRecords = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 6);
		gdTableMemberRecords.widthHint = 125;
		gdTableMemberRecords.heightHint = 75;
		tableMemberRecords.setLayoutData(gdTableMemberRecords);
		tableMemberRecords.addSelectionListener(createSelectionListener(() -> memberRecordSelectionChanged(true)));		
		
		tableAvailableSortElements = new Table(compositeSorted, SWT.BORDER);
		tableAvailableSortElements.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tableAvailableSortElements.addSelectionListener(createSelectionListener(this::enableAndDisable));
		var gdListAvailableSortElementsOrDbkey = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 4);
		gdListAvailableSortElementsOrDbkey.horizontalIndent = 10;
		gdListAvailableSortElementsOrDbkey.widthHint = 150;
		gdListAvailableSortElementsOrDbkey.heightHint = 75;
		tableAvailableSortElements.setLayoutData(gdListAvailableSortElementsOrDbkey);
		
		btnAddSortElementOrDbkey = new Button(compositeSorted, SWT.NONE);
		btnAddSortElementOrDbkey.addSelectionListener(createSelectionListener(() -> addSortElement(tableAvailableSortElements.getSelectionIndex())));
		btnAddSortElementOrDbkey.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 3));
		btnAddSortElementOrDbkey.setText(">");
		
		tableSelectedSortElements = new Table(compositeSorted, SWT.BORDER);
		tableSelectedSortElements.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tableSelectedSortElements.addSelectionListener(createSelectionListener(this::enableAndDisable));
		var gdListSelectedSortElementsOrDbkey = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 4);
		gdListSelectedSortElementsOrDbkey.widthHint = 150;
		gdListSelectedSortElementsOrDbkey.heightHint = 75;
		tableSelectedSortElements.setLayoutData(gdListSelectedSortElementsOrDbkey);
		
		btnAsc = new Button(compositeSorted, SWT.NONE);
		btnAsc.addSelectionListener(createSelectionListener(() -> flipSortSequence(tableSelectedSortElements.getSelectionIndex())));
		var gdBtnAsc = new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1);
		gdBtnAsc.widthHint = 40;
		btnAsc.setLayoutData(gdBtnAsc);
		btnAsc.setText("ASC");
		
		btnDesc = new Button(compositeSorted, SWT.NONE);
		btnDesc.addSelectionListener(createSelectionListener(() -> flipSortSequence(tableSelectedSortElements.getSelectionIndex())));
		var gdBtnDesc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdBtnDesc.widthHint = 40;
		btnDesc.setLayoutData(gdBtnDesc);
		btnDesc.setText("DESC");
		
		btnUp = new Button(compositeSorted, SWT.NONE);
		btnUp.addSelectionListener(createSelectionListener(() -> moveSortElementUp(tableSelectedSortElements.getSelectionIndex())));
		var gdBtnUp = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdBtnUp.widthHint = 40;
		btnUp.setLayoutData(gdBtnUp);
		btnUp.setText("Up");
		
		btnRemoveSortElementOrDbkey = new Button(compositeSorted, SWT.NONE);
		btnRemoveSortElementOrDbkey.setText("<");
		btnRemoveSortElementOrDbkey.addSelectionListener(createSelectionListener(() -> removeSortElement(tableSelectedSortElements.getSelectionIndex())));
		
		btnDown = new Button(compositeSorted, SWT.NONE);
		btnDown.addSelectionListener(createSelectionListener(() -> moveSortElementDown(tableSelectedSortElements.getSelectionIndex())));
		var gdBtnDown = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdBtnDown.widthHint = 40;
		btnDown.setLayoutData(gdBtnDown);
		btnDown.setText("Down");
		new Label(compositeSorted, SWT.NONE);
		new Label(compositeSorted, SWT.NONE);
		
		var compositeKeyLength = new Composite(compositeSorted, SWT.NONE);
		var gdCompositeKeyLength = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gdCompositeKeyLength.verticalIndent = -5;
		compositeKeyLength.setLayoutData(gdCompositeKeyLength);
		var glCompositeKeyLength = new GridLayout(2, false);
		glCompositeKeyLength.marginWidth = 0;
		glCompositeKeyLength.horizontalSpacing = 0;
		compositeKeyLength.setLayout(glCompositeKeyLength);
		
		lblKeyLength = new Label(compositeKeyLength, SWT.NONE);
		lblKeyLength.setEnabled(false);
		lblKeyLength.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKeyLength.setText("Key length:");
		
		lblKeylengthValue = new Label(compositeKeyLength, SWT.RIGHT);
		lblKeylengthValue.setEnabled(false);
		lblKeylengthValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeSorted, SWT.NONE);
		
		var composite1 = new Composite(compositeSorted, SWT.NONE);
		var glComposite1 = new GridLayout(1, false);
		glComposite1.marginHeight = 0;
		glComposite1.marginWidth = 0;
		composite1.setLayout(glComposite1);
		var gdComposite1 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gdComposite1.horizontalIndent = 10;
		composite1.setLayoutData(gdComposite1);
		
		var lblNewLabel = new Label(composite1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		
		btnNaturalSequence = new Button(composite1, SWT.CHECK);
		btnNaturalSequence.addSelectionListener(createSelectionListener(this::captureDataEnableAndDisable));
		btnNaturalSequence.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnNaturalSequence.setText("Natural sequence");
		
		btnCompressed = new Button(composite1, SWT.CHECK);
		btnCompressed.addSelectionListener(createSelectionListener(this::captureDataEnableAndDisable));
		btnCompressed.setText("Compressed");
		
		var composite2 = new Composite(compositeSorted, SWT.NONE);
		composite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		var glComposite2 = new GridLayout(2, false);
		glComposite2.marginHeight = 0;
		glComposite2.marginWidth = 0;
		composite2.setLayout(glComposite2);
		new Label(composite2, SWT.NONE);
		
		comboDuplicatesOption = new Combo(composite2, SWT.READ_ONLY);
		comboDuplicatesOption.addSelectionListener(createSelectionListener(this::captureDataEnableAndDisable));
		var gdComboDuplicatesOption = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 3);
		gdComboDuplicatesOption.widthHint = 125;
		comboDuplicatesOption.setLayoutData(gdComboDuplicatesOption);
		
		lblDuplicates = new Label(composite2, SWT.NONE);
		lblDuplicates.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblDuplicates.setText("Duplicates:");
		new Label(composite2, SWT.NONE);
		
		lblIfNoSort = new Label(compositeSorted, SWT.NONE);
		lblIfNoSort.setEnabled(false);
		var gdLblIfNoSort = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gdLblIfNoSort.verticalIndent = 10;
		lblIfNoSort.setLayoutData(gdLblIfNoSort);
		lblIfNoSort.setText("\u00B9If no sort elements are selected, the set is sorted by dbkey (indexed sets only).");
		
		collectData();
		initializeControls();

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
	
	private void addSortElement(int index) {
		// get the selected member record's name
		var recordName = tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
		
		// given the index, get the element name from the member's list of available sort elements and remove the
		// item from that list, as well as the dialog's list of available sort elements
		var memberAvailableSortElements = availableSortElements.get(recordName);
		var elementName = memberAvailableSortElements.get(index);
		memberAvailableSortElements.remove(index);
		tableAvailableSortElements.remove(index);
		
		// create a sort element description and add it to both the member's list of selected sort elements and
		// the dialog's list of selected sort elements
		var p = new StringBuilder();
		p.append(ASC);
		p.append(elementName);
		var memberSelectedSortElements = selectedSortElements.get(recordName);		
		if (memberSelectedSortElements.isEmpty()) {
			tableSelectedSortElements.removeAll(); // make sure "DBKEY" is removed first
			dbkeySortSequence = null;
		}
		memberSelectedSortElements.add(p.toString());
		var item = new TableItem(tableSelectedSortElements, SWT.NONE);
		item.setText(0, p.toString());		
		// change the font to normal in the member records table if exactly 1 sort element is selected for the
		// member record (the font would have been bold so far)
		if (memberSelectedSortElements.size() == 1) {
			var i = 0;
			while (!tableMemberRecords.getItem(i).getText().equals(recordName)) {
				i += 1;
			}
			item = tableMemberRecords.getItems()[i];
			item.setFont(tableMemberRecords.getFont());			
		}
		
		// select the added item in the dialog's list of selected sort elements and make sure it's visible
		tableSelectedSortElements.select(tableSelectedSortElements.getItemCount() - 1);
		tableSelectedSortElements.showSelection();
		
		enableAndDisable();
	}
	
	private void removeSortElement(int index) {
		var recordName = tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
		
		// given the index, get the element name and remove the item from both the member's list of selected
		// elements and the dialog's list of selected elements
		var memberSelectedSortElements = selectedSortElements.get(recordName);		
		var sortElementDescription = memberSelectedSortElements.get(index);
		memberSelectedSortElements.remove(index);		
		tableSelectedSortElements.remove(index);
		// change the font to bold in the member records table if no sort elements are selected for the member
		// record (chained sets only)
		if (memberSelectedSortElements.isEmpty() && set.getMode() == SetMode.CHAINED) {
			var i = 0;
			while (!tableMemberRecords.getItem(i).getText().equals(recordName)) {
				i += 1;
			}
			var item = tableMemberRecords.getItems()[i];
			item.setFont(boldTableFont);			
		}
		// reset the sort sequence when sorted on dbkey (indexed sets only); make sure the duplicates option is
		// set to 'not allowed' as well
		if (memberSelectedSortElements.isEmpty() && set.getMode() == SetMode.INDEXED) {
			dbkeySortSequence = copyOfDbkeySortSequence == SortSequence.DESCENDING ? SortSequence.DESCENDING : SortSequence.ASCENDING;
			duplicatesOptions.put(recordName, DuplicatesOption.NOT_ALLOWED);
			comboDuplicatesOption.select(2);
		}
		
		var elementName = sortElementDescription.substring(sortElementDescription.indexOf(" ") + 1);
		
		// insert the element in both the member's list of available sort elements and the dialog's list of
		// available sort elements
		var memberAvailableSortElements = availableSortElements.get(recordName);		
		var schemaRecord = members.get(recordName).getRecord();
		int i = schemaRecord.getElements().indexOf(schemaRecord.getElement(elementName));
		int insertionIndex = 0;
		for (var otherElementName : memberAvailableSortElements) {
			var j = schemaRecord.getElements().indexOf(schemaRecord.getElement(otherElementName));
			if (j < i) {
				insertionIndex += 1;
			}
		}
		memberAvailableSortElements.add(insertionIndex, elementName);
		tableAvailableSortElements.removeAll();
		for (var availableSortElement : memberAvailableSortElements) {
			var item = new TableItem(tableAvailableSortElements, SWT.NONE);
			item.setText(0, availableSortElement);
		}
		
		// select the added item in the dialog's list of selected sort elements and make sure it is visible
		tableAvailableSortElements.select(insertionIndex);
		tableAvailableSortElements.showSelection();
		
		enableAndDisable();		
	}

	private void moveSortElementUp(int index) {		
		var recordName = tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
		
		// move the element 1 place up in the member's list of selected sort elements		
		var memberSelectedSortElements = selectedSortElements.get(recordName);			
		String sortElementDescription = memberSelectedSortElements.get(index);		
		memberSelectedSortElements.remove(index);				
		memberSelectedSortElements.add(index - 1, sortElementDescription);
		
		// refresh the dialog's list of selected sort elements
		tableSelectedSortElements.removeAll();
		for (var selectedSortElement : memberSelectedSortElements) {
			var item = new TableItem(tableSelectedSortElements, SWT.NONE);
			item.setText(0, selectedSortElement);
		}
		
		// select the added item in the dialog's list of selected sort elements and make sure it's visible
		tableSelectedSortElements.select(index - 1);
		tableSelectedSortElements.showSelection();		
		
		enableAndDisable();
	}

	private void moveSortElementDown(int index) {
		var recordName = tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
						
		// move the element 1 place down in the member's list of selected sort elements
		var memberSelectedSortElements = selectedSortElements.get(recordName);			
		var sortElementDescription = memberSelectedSortElements.get(index);		
		memberSelectedSortElements.remove(index);		
		memberSelectedSortElements.add(index + 1, sortElementDescription);
		
		// refresh the dialog's list of selected sort elements
		tableSelectedSortElements.removeAll();
		for (var selectedSortElement : memberSelectedSortElements) {
			var item = new TableItem(tableSelectedSortElements, SWT.NONE);
			item.setText(0, selectedSortElement);
		}
		
		// select the added item in the dialog's list of selected sort elements and make sure it's visible
		tableSelectedSortElements.select(index + 1);
		tableSelectedSortElements.showSelection();		
		
		enableAndDisable();
	}

	private void flipSortSequence(int index) {
		// get the selected member record's name
		var recordName = tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
				
		// given the index, flip the sort sequence in both the member's list of selected elements and the dialog's
		// list of selected elements
		var memberSelectedSortElements = selectedSortElements.get(recordName);
		if (!isSortedByDbkey()) {
			var item = tableSelectedSortElements.getItem(index);
			var sortElementDescription = memberSelectedSortElements.get(index);				
			var newSortSequence = sortElementDescription.startsWith(ASC) ? DESC : ASC;
			var elementName = sortElementDescription.substring(sortElementDescription.indexOf(" ") + 1);		
			var p = new StringBuilder();
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

	private void captureDataEnableAndDisable() {
		captureData();
		enableAndDisable();
	}
	
	private void captureData() {
		// this method does NOT deal with the sort elements related stuff; this is done in the add/remove/move
		// up/down sort element and flip sort sequence methods
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
		var recordName = tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);		
		naturalSequences.put(recordName, Boolean.valueOf(btnNaturalSequence.getSelection()));
		compressed = btnCompressed.getSelection();
		duplicatesOptions.put(recordName, DuplicatesOption.valueOf(comboDuplicatesOption.getText().replace(" ", "_")));
	}	

	private void collectData() {
		setOrder = set.getOrder();
		for (var memberRole : set.getMembers()) {
			var schemaRecord = memberRole.getRecord();
			var recordName = Tools.removeTrailingUnderscore(schemaRecord.getName());
			members.put(recordName, memberRole);
			collectAvailableAndEligibleSortElements(memberRole);
			collectSelectedSortElementsAndOtherData(memberRole);
		}
		selectedSortElements.keySet().stream()
				.forEach(key -> copyOfSelectedSortElements.put(key, List.copyOf(selectedSortElements.get(key))));
		naturalSequences.keySet().stream()
				.forEach(key -> copyOfNaturalSequences.put(key, naturalSequences.get(key)));
		duplicatesOptions.keySet().stream()
				.forEach(key -> copyOfDuplicatesOptions.put(key, duplicatesOptions.get(key)));
		copyOfDbkeySortSequence = dbkeySortSequence;
	}

	private void collectAvailableAndEligibleSortElements(MemberRole memberRole) {
		var schemaRecord = memberRole.getRecord();
		var recordName = schemaRecord.getName();
		var memberAvailableSortElements = availableSortElements.get(recordName);
		if (memberAvailableSortElements == null) {
			memberAvailableSortElements = new ArrayList<>();
			availableSortElements.put(recordName, memberAvailableSortElements);
		}			
		for (var element : schemaRecord.getElements()) {
			var selected = false;			
			if (set.getOrder() == SetOrder.SORTED) {
				selected = memberRole.getSortKey().getElements().stream()
						.map(KeyElement::getElement)
						.anyMatch(e -> e == element);
			}
			if (!selected && !element.getName().equals("FILLER") && !Tools.isInvolvedInRedefines(element) &&
				!Tools.isInvolvedInOccurs(element) && element.getLevel() != 88) {
				
				memberAvailableSortElements.add(element.getName());
			}
		}		
	}

	private void collectSelectedSortElementsAndOtherData(MemberRole memberRole) {
		var recordName = memberRole.getRecord().getName();
		var memberSelectedSortElements = selectedSortElements.computeIfAbsent(recordName, n -> new ArrayList<>());
		if (set.getOrder() == SetOrder.SORTED) {
			for (var keyElement : memberRole.getSortKey().getElements()) {					
				if (!keyElement.isDbkey()) {
					var p = new StringBuilder();
					if (keyElement.getSortSequence() == SortSequence.ASCENDING) {
						p.append(ASC);
					} else {
						p.append(DESC);
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
			naturalSequences.put(recordName, false);
			compressed = false;
			duplicatesOptions.put(recordName, DuplicatesOption.NOT_ALLOWED);
		}		
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
		
		// we use a bold font to indicate that no sort elements are selected for a given member record; create
		// that font
		var font = tableMemberRecords.getFont();
		var fontData = font.getFontData();
		fontData[0].setStyle(SWT.BOLD);
		boldTableFont = new Font(font.getDevice(), fontData);
		
		// fill the list of member records, sorted alphabetically, and select the first one				
		var recordNames = new ArrayList<>(members.keySet());
		Collections.sort(recordNames);		
		for (var recordName : recordNames) {
			var item = new TableItem(tableMemberRecords, SWT.NONE);
			item.setText(0, recordName);
			var memberSelectedSortElements = selectedSortElements.get(recordName);
			if (memberSelectedSortElements.isEmpty() && set.getMode() != SetMode.INDEXED) {
				item.setFont(boldTableFont);
			}
		} 
		tableMemberRecords.select(0);
		
		// fill the combo containing the duplicates option values taking care of limitations for both indexed
		// sets and VSAM indexes
		if (set.getMode() != SetMode.VSAM_INDEX) {
			comboDuplicatesOption.add(DuplicatesOption.FIRST.toString());
			comboDuplicatesOption.add(DuplicatesOption.LAST.toString());
		}
		comboDuplicatesOption.add(DuplicatesOption.NOT_ALLOWED.toString().replace("_", " "));
		if (set.getMode() == SetMode.INDEXED) {
			// only indexed sets can have their duplicates sorted by dbkey
			comboDuplicatesOption.add(DuplicatesOption.BY_DBKEY.toString().replace("_", " "));
		}
		if (set.getMode() == SetMode.VSAM_INDEX) {
			// only VSAM indexes can have their duplicates unordered
			comboDuplicatesOption.add(DuplicatesOption.UNORDERED.toString());
		}
		
		// initialize the sorted set controls for the (alphabetically) first member record
		memberRecordSelectionChanged(false);
	}

	private void memberRecordSelectionChanged(boolean callEnableOrDisable) {
		// no changes needed if the previously selected record is still selected
		if (tableMemberRecords.getSelectionIndex() == selectedMemberRecordIndex) {
			return; 
		}
		selectedMemberRecordIndex = tableMemberRecords.getSelectionIndex();
				
		var recordName = tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
		
		// fill the list of available sort elements for the selected member record
		tableAvailableSortElements.removeAll();
		var memberAvailableSortElements = availableSortElements.get(recordName);
		for (var elementName : memberAvailableSortElements) {
			var item = new TableItem(tableAvailableSortElements, SWT.NONE);
			item.setText(0, elementName);
		}
		
		// fill the list of selected sort elements for the selected member record
		tableSelectedSortElements.removeAll();
		var memberSelectedSortElements = selectedSortElements.get(recordName);
		for (var sortElementDescription : memberSelectedSortElements) {			
			var item = new TableItem(tableSelectedSortElements, SWT.NONE);
			item.setText(0, sortElementDescription); // prefixed with sort sequence
		}
		
		// set the natural sequence option		
		btnNaturalSequence.setSelection(naturalSequences.get(recordName).booleanValue());
		
		// set the compressed option
		btnCompressed.setSelection(compressed);
		
		// select the appropriate duplicates option		
		var firstMemberDuplicatesOption = duplicatesOptions.get(recordName);
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

	private void enableAndDisable() {
		var recordName = tableMemberRecords.getItem(tableMemberRecords.getSelectionIndex()).getText(0);
		var schemaRecord = members.get(recordName).getRecord();
				
		var keyLength = computeKeyLength(schemaRecord);
		lblKeylengthValue.setText(String.valueOf(keyLength));		
		
		enableAndDisableSortedSetRelatedControls(schemaRecord, keyLength);
		if (isSortedByDbkey()) {
			enableAndDisableSortedByDbkeyRelatedControls();
		}
		
		var sorted = setOrder == SetOrder.SORTED;
		var selectedSortElement = tableSelectedSortElements.getSelectionCount() == 1 ?
			tableSelectedSortElements.getItem(tableSelectedSortElements.getSelectionIndex()).getText(0) : null;
		// ASCENDING must be specified for native VSAM sets -> disable the ASC and DESC buttons
		btnAsc.setEnabled(sorted && !set.isVsam() && tableSelectedSortElements.getSelectionCount() == 1 &&
				  		  selectedSortElement.startsWith(DESC) || tableSelectedSortElements.getItemCount() == 1 &&
				  		  tableSelectedSortElements.getItem(0).getText(0).equals("DESC DBKEY"));
		btnDesc.setEnabled(sorted && !set.isVsam() && tableSelectedSortElements.getSelectionCount() == 1 &&
				   		   selectedSortElement.startsWith(ASC) || tableSelectedSortElements.getItemCount() == 1 &&
					  	   tableSelectedSortElements.getItem(0).getText(0).equals("ASC DBKEY"));
		btnUp.setEnabled(sorted && tableSelectedSortElements.getSelectionCount() == 1 && tableSelectedSortElements.getSelectionIndex() > 0);
		btnDown.setEnabled(sorted && tableSelectedSortElements.getSelectionCount() == 1 &&
		 		   		   tableSelectedSortElements.getSelectionIndex() < (tableSelectedSortElements.getItemCount() - 1));
		lblKeyLength.setEnabled(sorted);
		lblKeylengthValue.setEnabled(sorted);
		btnNaturalSequence.setEnabled(sorted);
		btnCompressed.setEnabled(sorted && set.getMode() == SetMode.INDEXED);
		lblDuplicates.setEnabled(sorted);
		comboDuplicatesOption.setEnabled(sorted && dbkeySortSequence == null);
				
		var okButton = getButton(IDialogConstants.OK_ID);		
		if (isDataValidAndComplete()) {
			okButton.setEnabled(isAnythingChanged());
		} else {
			okButton.setEnabled(false);
		}		
	}

	private int computeKeyLength(SchemaRecord schemaRecord) {
		var recordName = schemaRecord.getName();
		var memberSelectedSortElements = selectedSortElements.get(recordName);
		if (isSortedByDbkey()) {
			return 4;
		} else {			
			return memberSelectedSortElements.stream()
				.map(e -> e.substring(e.indexOf(" ") + 1))
				.map(schemaRecord::getElement)
				.mapToInt(Element::getLength)
				.sum();
		}
	}

	private void enableAndDisableSortedSetRelatedControls(SchemaRecord schemaRecord, int keyLength) {
		var recordName = schemaRecord.getName();
		var sorted = setOrder == SetOrder.SORTED;
		var sortElementSelected = !selectedSortElements.get(recordName).isEmpty();
		lblMemberRecord.setEnabled(sorted);
		lblAvailableSortElements.setEnabled(sorted);
		lblSelectedSortElements.setEnabled(sorted);
		tableMemberRecords.setEnabled(sorted);
		tableAvailableSortElements.setEnabled(sorted);
		var keyLengthWouldExceed256BytesForSelectedElement = false;
		for (var i = 0; i < tableAvailableSortElements.getItemCount(); i++) {
			var item = tableAvailableSortElements.getItem(i);
			var element = schemaRecord.getElement(item.getText(0));
			if (keyLength + element.getLength() > 256) {
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
				if (i == tableAvailableSortElements.getSelectionIndex()) {
					keyLengthWouldExceed256BytesForSelectedElement = true;
				}
			}
		}
		tableSelectedSortElements.setEnabled(sorted);
		lblIfNoSort.setEnabled(sorted);
		btnAddSortElementOrDbkey.setEnabled(sorted && tableAvailableSortElements.getSelectionCount() == 1 && !keyLengthWouldExceed256BytesForSelectedElement);
		btnRemoveSortElementOrDbkey.setEnabled(sorted && tableSelectedSortElements.getSelectionCount() == 1 && sortElementSelected);		
	}
	
	private void enableAndDisableSortedByDbkeyRelatedControls() {
		// we use an italic font for the DBKEY replacement sort element; create this font if not already done
		if (italicTableFont == null) {
			var font = tableMemberRecords.getFont();
			var fontData = font.getFontData();
			fontData[0].setStyle(SWT.ITALIC);
			italicTableFont = new Font(font.getDevice(), fontData);
		}
		
		tableSelectedSortElements.removeAll();
		var item = new TableItem(tableSelectedSortElements, SWT.NONE);
		if (dbkeySortSequence == SortSequence.ASCENDING) {
			item.setText("ASC DBKEY");
		} else {
			item.setText("DESC DBKEY");
		}
		item.setFont(italicTableFont);
	}
	
	private boolean isDataValidAndComplete() {
		// the data is complete if either the set is not sorted, or the set is sorted and at least 1 sort element
		// is selected for each member record (i.e. if the set is chained)
		if (setOrder != SetOrder.SORTED) {
			return true;
		}		
		for (var memberSelectedSortElements : selectedSortElements.values()) {
			if (memberSelectedSortElements.isEmpty() && set.getMode() != SetMode.INDEXED) {
				return false;
			}
		}
		return true;
	}

	private boolean isAnythingChanged() {
		if (setOrder != set.getOrder()) {
			return true; 
		}
		if (setOrder != SetOrder.SORTED) {
			return false; 
		}
		if (set.getMode() == SetMode.INDEXED && compressed != set.getMembers().get(0).getSortKey().isCompressed()) {
			return true;
		}
		for (var memberRole : set.getMembers()) {
			var recordName = Tools.removeTrailingUnderscore(memberRole.getRecord().getName());
			var memberSelectedSortElements = selectedSortElements.get(recordName);
			var copyOfMemberSelectedSortElements = copyOfSelectedSortElements.get(recordName);
			if (!memberSelectedSortElements.equals(copyOfMemberSelectedSortElements)) {
				return true;
			}
			var naturalSequence = naturalSequences.get(recordName);
			var copyOfNaturalSequence = copyOfNaturalSequences.get(recordName);
			if (!naturalSequence.equals(copyOfNaturalSequence)) {
				return true;
			}
			var duplicatesOption = duplicatesOptions.get(recordName);
			var copyOfDuplicatesOption = copyOfDuplicatesOptions.get(recordName);
			if (duplicatesOption != copyOfDuplicatesOption) {
				return true;
			}
		}
		return dbkeySortSequence != copyOfDbkeySortSequence;
	}

	private boolean isSortedByDbkey() {
		// will return true only when the set is indexed and no sort elements are selected
		return dbkeySortSequence != null;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		enableAndDisable();
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

	public SetOrder getSetOrder() {
		return setOrder;
	}
	
	public ISortKeyDescription[] getSortKeyDescriptions() {
		var sortKeyDecriptions = new ISortKeyDescription[set.getMembers().size()];
		for (var i = 0; i < set.getMembers().size(); i++) {
			var memberRole = set.getMembers().get(i);
			sortKeyDecriptions[i] = new SortKeyDescription(memberRole.getRecord());
		}
		return sortKeyDecriptions;
	}

	private class SortKeyDescription implements ISortKeyDescription {
		private final String recordName;
		
		private SortKeyDescription(SchemaRecord schemaRecord) {
			recordName = Tools.removeTrailingUnderscore(schemaRecord.getName());
		}
		
		@Override
		public String[] getElementNames() {
			if (!isSortedByDbkey()) {
				var memberSelectedSortElements = selectedSortElements.get(recordName);
				var sortElementNames = new String[memberSelectedSortElements.size()];
				for (var i = 0; i < sortElementNames.length; i++) {
					var sortElementDescription = memberSelectedSortElements.get(i);
					var j = sortElementDescription.indexOf(" ");
					sortElementNames[i] = sortElementDescription.substring(j + 1);
				}
				return sortElementNames;
			} else {
				return new String[] { ISortKeyDescription.DBKEY_ELEMENT };
			}
		}

		@Override
		public SortSequence[] getSortSequences() {
			if (!isSortedByDbkey()) {
				var memberSelectedSortElements = selectedSortElements.get(recordName);
				var sortSequences = new SortSequence[memberSelectedSortElements.size()];
				for (var i = 0; i < sortSequences.length; i++) {
					var sortElementDescription = memberSelectedSortElements.get(i);
					sortSequences[i] = sortElementDescription.startsWith(ASC) ? SortSequence.ASCENDING : SortSequence.DESCENDING;
				}
				return sortSequences;
			} else {
				return new SortSequence[] { dbkeySortSequence };
			}
		}

		@Override
		public DuplicatesOption getDuplicatesOption() {
			if (!isSortedByDbkey()) {
				Assert.isTrue(duplicatesOptions.containsKey(recordName));
				return duplicatesOptions.get(recordName);
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
			Assert.isTrue(naturalSequences.containsKey(recordName));
			return naturalSequences.get(recordName).booleanValue();
		}
		
	}
	
}
