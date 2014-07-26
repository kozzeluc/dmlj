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
package org.lh.dmlj.schema.editor.property.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.prefix.Prefix;

public class PointerOrderDialog extends Dialog {

	private Button btnGroup;
	private Button btnGroupDoublePlus;
	private Button btnGroupPlus;
	private Button btnMoveDown;
	private Button btnMoveUp;
	private List<Pointer<?>> desiredPointerList = new ArrayList<>();
	private List<Pointer<?>> idealPointerList = new ArrayList<>();
	private Prefix originalPrefix;
	private Table table;
	private Button btnReset;
	 
	private static boolean areListsEqual(List<?> list1, List<?> list2) {
		for (int i = 0; i < list1.size(); i++) {
			if (list1.get(i) != list2.get(i)) {
				return false;
			}
		}
		return true;
	}
	
	private static String getRole(Pointer<?> pointer) {
		if (pointer.isOwnerDefined()) {
			return "owner";
		} else {
			return "member";
		}
	}

	private static String getType(Pointer<?> pointer) {
		String pointerTypeAsString = pointer.getType().toString();
		return pointerTypeAsString.substring(pointerTypeAsString.indexOf("_") + 1);
	}

	private static int intFor(PointerType type) {
		if (type == PointerType.OWNER_NEXT || type == PointerType.MEMBER_NEXT || 
			type == PointerType.MEMBER_INDEX) {
			
			return 0;
		} else if (type == PointerType.OWNER_PRIOR || type == PointerType.MEMBER_PRIOR) {
			return 1;
		} else if (type == PointerType.MEMBER_OWNER) {
			return 2;
		} else {
			return 3;
		}
	}

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param prefix 
	 */
	public PointerOrderDialog(Shell parentShell, Prefix prefix) {
		super(parentShell);
		originalPrefix = prefix;
		desiredPointerList.addAll(originalPrefix.getPointers());		
		createIdealPointerList();		
	}
	
	private boolean anythingChanged() {
		return !areListsEqual(originalPrefix.getPointers(), desiredPointerList);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Reorder pointers for record " + originalPrefix.getRecord().getName());	    
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
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 6));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnPos = new TableColumn(table, SWT.NONE);
		tblclmnPos.setWidth(50);
		tblclmnPos.setText("Pos.");
		
		TableColumn tblclmnSet = new TableColumn(table, SWT.NONE);
		tblclmnSet.setWidth(125);
		tblclmnSet.setText("Set");
		
		TableColumn tblclmnRole = new TableColumn(table, SWT.NONE);
		tblclmnRole.setWidth(60);
		tblclmnRole.setText("Role");
		
		TableColumn tblclmnPointer = new TableColumn(table, SWT.NONE);
		tblclmnPointer.setWidth(60);
		tblclmnPointer.setText("Pointer");
		
		btnMoveUp = new Button(container, SWT.NONE);
		btnMoveUp.setToolTipText("Move pointer 1 position up");
		btnMoveUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveSelectedPointerUp();
			}
		});
		btnMoveUp.setEnabled(false);
		btnMoveUp.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, true, 1, 1));
		btnMoveUp.setText("Up");
		
		btnMoveDown = new Button(container, SWT.NONE);
		btnMoveDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveSelectedPointerDown();
			}
		});
		btnMoveDown.setToolTipText("Move pointer 1 position down");
		btnMoveDown.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnMoveDown.setEnabled(false);
		btnMoveDown.setText("Down");
		
		btnGroup = new Button(container, SWT.NONE);
		btnGroup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				group(getPointerListGroupedBySet());
			}
		});
		btnGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnGroup.setToolTipText("Group pointers by set");
		btnGroup.setText("Group");
		
		btnGroupPlus = new Button(container, SWT.NONE);
		btnGroupPlus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				group(getPointerListGroupedBySetWithPointersSorted());				
			}
		});
		btnGroupPlus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnGroupPlus.setToolTipText("Same as Group plus order pointers (N/I/P/O)");
		btnGroupPlus.setText("Group+");
		
		btnGroupDoublePlus = new Button(container, SWT.NONE);
		btnGroupDoublePlus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				group(idealPointerList);
			}
		});
		btnGroupDoublePlus.setToolTipText("Same as Group+ plus order sets alphabetically");
		btnGroupDoublePlus.setText("Group++");
		
		btnReset = new Button(container, SWT.NONE);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveAllPointersToTheirOriginalPosition();
			}
		});
		btnReset.setEnabled(false);
		btnReset.setToolTipText("Move each pointer to its original position again");
		btnReset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnReset.setText("Reset");
		
		refreshTable();

		return container;
	}

	private void createIdealPointerList() {
		idealPointerList.addAll(originalPrefix.getPointers());				
		Collections.sort(idealPointerList, new Comparator<Pointer<?>>() {
			@Override
			public int compare(Pointer<?> p1, Pointer<?> p2) {
				if (!p1.getSetName().equals(p2.getSetName())) {
					return p1.getSetName().compareTo(p2.getSetName());
				} else {
					return intFor(p1.getType()) - intFor(p2.getType());
				}
			}}
		);
	}

	private void enableAndDisable() {
		
		int i = table.getSelectionIndex();
		btnMoveUp.setEnabled(i > 0);
		btnMoveDown.setEnabled(i > -1 && i < (table.getItemCount() - 1));
		
		btnGroup.setEnabled(!areListsEqual(desiredPointerList, getPointerListGroupedBySet()));
		btnGroupPlus.setEnabled(!areListsEqual(desiredPointerList, 
											   getPointerListGroupedBySetWithPointersSorted()));
		btnGroupDoublePlus.setEnabled(!areListsEqual(desiredPointerList, idealPointerList));
		
		boolean anythingChanged = anythingChanged();
		btnReset.setEnabled(anythingChanged);
		Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(anythingChanged);
		
	}
	
	public List<Pointer<?>> getDesiredPointerList() {
		return new ArrayList<>(desiredPointerList);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(425, 300);
	}
	
	private List<Pointer<?>> getPointerListGroupedBySet() {
		List<Pointer<?>> pointerListGroupedBySet = new ArrayList<>(desiredPointerList);
		final List<String> setNamesInOrderOfAppearance = getSetNamesInOrderOfAppearance();
		Collections.sort(pointerListGroupedBySet, new Comparator<Pointer<?>>() {
			@Override
			public int compare(Pointer<?> p1, Pointer<?> p2) {
				if (p1.getSetName() != p2.getSetName()) {
					return setNamesInOrderOfAppearance.indexOf(p1.getSetName()) - 
						   setNamesInOrderOfAppearance.indexOf(p2.getSetName());
				} else {
					return desiredPointerList.indexOf(p1) - desiredPointerList.indexOf(p2);
				}
			}}
		);
		return pointerListGroupedBySet;
	}
	
	private List<Pointer<?>> getPointerListGroupedBySetWithPointersSorted() {
		List<Pointer<?>> pointerListGroupedBySetWithPointersSorted = 
			new ArrayList<>(desiredPointerList);			
		final List<String> setNamesInOrderOfAppearance = getSetNamesInOrderOfAppearance();
		Collections.sort(pointerListGroupedBySetWithPointersSorted, new Comparator<Pointer<?>>() {
			@Override
			public int compare(Pointer<?> p1, Pointer<?> p2) {
				if (p1.getSetName() != p2.getSetName()) {
					return setNamesInOrderOfAppearance.indexOf(p1.getSetName()) - 
							setNamesInOrderOfAppearance.indexOf(p2.getSetName());
				} else {
					return intFor(p1.getType()) - intFor(p2.getType());
				}
			}}
		);
		return pointerListGroupedBySetWithPointersSorted;
	}
	
	private String getPosition(Pointer<?> pointer) {
		StringBuilder pos = new StringBuilder();
		int currentPos = desiredPointerList.indexOf(pointer) + 1;
		pos.append(String.valueOf(currentPos));
		if (currentPos != pointer.getCurrentPositionInPrefix()) {
			pos.append(" (");
			pos.append(String.valueOf(pointer.getCurrentPositionInPrefix()));
			pos.append(")");
		}
		return pos.toString();
	}
	
	private List<String> getSetNamesInOrderOfAppearance() {
		Set<String> setNames = new LinkedHashSet<>();
		for (Pointer<?> pointer : desiredPointerList) {
			setNames.add(pointer.getSetName());
		}
		return new ArrayList<>(setNames);
	}
	
	private void group(List<Pointer<?>> groupedByList) {
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
		int i = table.getSelectionIndex();
		Collections.swap(desiredPointerList, i, i + 1);
		refreshTable();
		table.setSelection(i + 1);
		enableAndDisable();		
	}

	private void moveSelectedPointerUp() {
		int i = table.getSelectionIndex();
		Collections.swap(desiredPointerList, i, i - 1);
		refreshTable();
		table.setSelection(i - 1);
		enableAndDisable();	
	}

	private void refreshTable() {
		table.removeAll();
		for (Pointer<?> pointer : desiredPointerList) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, getPosition(pointer));			
			item.setText(1, Tools.removeTrailingUnderscore(pointer.getSetName()));						
			item.setText(2, getRole(pointer));
			item.setText(3, getType(pointer));
		}
		table.redraw();
	}

}
