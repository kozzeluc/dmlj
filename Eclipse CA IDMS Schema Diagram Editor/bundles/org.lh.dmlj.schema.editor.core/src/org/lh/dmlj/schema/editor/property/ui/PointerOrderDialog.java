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
import java.util.List;
import java.util.stream.IntStream;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.prefix.Prefix;

public class PointerOrderDialog extends Dialog {
	private final Prefix originalPrefix;
	private final List<Pointer> desiredPointerList = new ArrayList<>();
	private final List<Pointer> idealPointerList = new ArrayList<>();
	
	private Button btnGroup;
	private Button btnGroupDoublePlus;
	private Button btnGroupPlus;
	private Button btnMoveDown;
	private Button btnMoveUp;
	private Table table;
	private Button btnReset;
	 
	private static boolean doListsContainSameElements(List<?> list1, List<?> list2) {
		return list1.size() != list2.size() || IntStream.range(0, list1.size())
				.allMatch(i -> list1.get(i) == list2.get(i));
	}
	
	private static String getRole(Pointer pointer) {
		if (pointer.isOwnerDefined()) {
			return "owner";
		} else {
			return "member";
		}
	}

	private static String getType(Pointer pointer) {
		var pointerTypeAsString = pointer.getType().toString();
		return pointerTypeAsString.substring(pointerTypeAsString.indexOf("_") + 1);
	}

	private static int intFor(PointerType type) {
		return switch (type) {
			case OWNER_NEXT, MEMBER_NEXT, MEMBER_INDEX -> 0;
			case OWNER_PRIOR, MEMBER_PRIOR -> 1;
			case MEMBER_OWNER -> 2;
			default -> 3;
		};
	}
	
	public PointerOrderDialog(Shell parentShell, Prefix prefix) {
		super(parentShell);
		originalPrefix = prefix;
		desiredPointerList.addAll(originalPrefix.getPointers());		
		createIdealPointerList();
		setShellStyle(getShellStyle() | SWT.RESIZE); 
	}
	
	private void createIdealPointerList() {
		idealPointerList.addAll(originalPrefix.getPointers());				
		Collections.sort(idealPointerList, (p1, p2) -> {
			if (!p1.getSetName().equals(p2.getSetName())) {
				return p1.getSetName().compareTo(p2.getSetName());
			} else {
				return intFor(p1.getType()) - intFor(p2.getType());
			}
		});
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Reorder pointers for record " + originalPrefix.getRecord().getName());	    
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(450, 400);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		var container = (Composite) super.createDialogArea(parent);
		var gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(createSelectionListener(this::enableAndDisable));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		var tblclmnPos = new TableColumn(table, SWT.NONE);
		tblclmnPos.setWidth(50);
		tblclmnPos.setText("Pos.");
		
		var tblclmnSet = new TableColumn(table, SWT.NONE);
		tblclmnSet.setWidth(125);
		tblclmnSet.setText("Set");
		
		var tblclmnRole = new TableColumn(table, SWT.NONE);
		tblclmnRole.setWidth(60);
		tblclmnRole.setText("Role");
		
		var tblclmnPointer = new TableColumn(table, SWT.NONE);
		tblclmnPointer.setWidth(60);
		tblclmnPointer.setText("Pointer");
		
		btnMoveUp = new Button(container, SWT.NONE);
		btnMoveUp.setToolTipText("Move pointer 1 position up");
		btnMoveUp.addSelectionListener(createSelectionListener(this::moveSelectedPointerUp));
		btnMoveUp.setEnabled(false);
		btnMoveUp.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, true, 1, 1));
		btnMoveUp.setText("Up");
		
		btnMoveDown = new Button(container, SWT.NONE);
		btnMoveDown.addSelectionListener(createSelectionListener(this::moveSelectedPointerDown));
		btnMoveDown.setToolTipText("Move pointer 1 position down");
		btnMoveDown.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnMoveDown.setEnabled(false);
		btnMoveDown.setText("Down");
		
		btnGroup = new Button(container, SWT.NONE);
		btnGroup.addSelectionListener(createSelectionListener(() -> group(getPointerListGroupedBySet())));
		btnGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnGroup.setToolTipText("Group pointers by set");
		btnGroup.setText("Group");
		
		btnGroupPlus = new Button(container, SWT.NONE);
		btnGroupPlus.addSelectionListener(createSelectionListener(() -> group(getPointerListGroupedBySetWithPointersSorted())));			
		btnGroupPlus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnGroupPlus.setToolTipText("Same as 'Group' plus order pointers (N/I/P/O)");
		btnGroupPlus.setText("Group+");
		
		btnGroupDoublePlus = new Button(container, SWT.NONE);
		btnGroupDoublePlus.addSelectionListener(createSelectionListener(() -> group(idealPointerList)));
		btnGroupDoublePlus.setToolTipText("Same as 'Group+' plus order sets alphabetically");
		btnGroupDoublePlus.setText("Group++");
		
		btnReset = new Button(container, SWT.NONE);
		btnReset.addSelectionListener(createSelectionListener(this::moveAllPointersToTheirOriginalPosition));
		btnReset.setEnabled(false);
		btnReset.setToolTipText("Move each pointer to its original position again");
		btnReset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnReset.setText("Reset");
		
		var textGroupDescriptions = new Text(container, SWT.NO_FOCUS | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		textGroupDescriptions.setBackground(container.getBackground());
		textGroupDescriptions.setText("Group: group pointers by set\nGroup+: same as 'Group' plus order pointers (N/I/P/O)\nGroup++: same as 'Group+' plus order sets alphabetically");
		textGroupDescriptions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		refreshTable();

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

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		enableAndDisable();
	}

	private void group(List<Pointer> groupedByList) {
		desiredPointerList.clear();
		desiredPointerList.addAll(groupedByList);
		table.deselectAll();
		refreshTable();
		enableAndDisable();
	}

	private void moveAllPointersToTheirOriginalPosition() {
		desiredPointerList.clear();
		desiredPointerList.addAll(originalPrefix.getPointers());
		table.deselectAll();
		refreshTable();
		enableAndDisable();
	}

	private void moveSelectedPointerDown() {
		var i = table.getSelectionIndex();
		Collections.swap(desiredPointerList, i, i + 1);
		refreshTable();
		table.setSelection(i + 1);
		enableAndDisable();		
	}

	private void moveSelectedPointerUp() {
		var i = table.getSelectionIndex();
		Collections.swap(desiredPointerList, i, i - 1);
		refreshTable();
		table.setSelection(i - 1);
		enableAndDisable();	
	}
	
	private void refreshTable() {
		table.removeAll();
		for (var pointer : desiredPointerList) {
			var item = new TableItem(table, SWT.NONE);
			item.setText(0, getPosition(pointer));			
			item.setText(1, Tools.removeTrailingUnderscore(pointer.getSetName()));						
			item.setText(2, getRole(pointer));
			item.setText(3, getType(pointer));
		}
		table.redraw();
	}

	private String getPosition(Pointer pointer) {
		var pos = new StringBuilder();
		var currentPos = desiredPointerList.indexOf(pointer) + 1;
		pos.append(String.valueOf(currentPos));
		if (currentPos != pointer.getCurrentPositionInPrefix()) {
			pos.append(" (");
			pos.append(String.valueOf(pointer.getCurrentPositionInPrefix()));
			pos.append(")");
		}
		return pos.toString();
	}

	private void enableAndDisable() {
		var i = table.getSelectionIndex();
		btnMoveUp.setEnabled(i > 0);
		btnMoveDown.setEnabled(i > -1 && i < (table.getItemCount() - 1));
		
		btnGroup.setEnabled(!doListsContainSameElements(desiredPointerList, getPointerListGroupedBySet()));
		btnGroupPlus.setEnabled(!doListsContainSameElements(desiredPointerList, getPointerListGroupedBySetWithPointersSorted()));
		btnGroupDoublePlus.setEnabled(!doListsContainSameElements(desiredPointerList, idealPointerList));
		
		var anythingChanged = !doListsContainSameElements(originalPrefix.getPointers(), desiredPointerList);
		btnReset.setEnabled(anythingChanged);
		getButton(IDialogConstants.OK_ID).setEnabled(anythingChanged);
	}
	
	private List<Pointer> getPointerListGroupedBySet() {
		var pointerListGroupedBySet = new ArrayList<>(desiredPointerList);
		var setNamesInOrderOfAppearance = getSetNamesInOrderOfAppearance();
		Collections.sort(pointerListGroupedBySet, (p1, p2) -> {
			if (!p1.getSetName().equals(p2.getSetName())) {
				return setNamesInOrderOfAppearance.indexOf(p1.getSetName()) - setNamesInOrderOfAppearance.indexOf(p2.getSetName());
			} else {
				return desiredPointerList.indexOf(p1) - desiredPointerList.indexOf(p2);
			}
		});
		return pointerListGroupedBySet;
	}
	
	private List<Pointer> getPointerListGroupedBySetWithPointersSorted() {
		var pointerListGroupedBySetWithPointersSorted = new ArrayList<>(desiredPointerList);			
		var setNamesInOrderOfAppearance = getSetNamesInOrderOfAppearance();
		Collections.sort(pointerListGroupedBySetWithPointersSorted, (p1, p2) -> {
			if (!p1.getSetName().equals(p2.getSetName())) {
				return setNamesInOrderOfAppearance.indexOf(p1.getSetName()) - setNamesInOrderOfAppearance.indexOf(p2.getSetName());
			} else {
				return intFor(p1.getType()) - intFor(p2.getType());
			}
		});
		return pointerListGroupedBySetWithPointersSorted;
	}
	
	private List<String> getSetNamesInOrderOfAppearance() {
		return desiredPointerList.stream()
				.map(Pointer::getSetName)
				.distinct()
				.toList();
	}

	public List<Pointer> getDesiredPointerList() {
		return new ArrayList<>(desiredPointerList);
	}

}
