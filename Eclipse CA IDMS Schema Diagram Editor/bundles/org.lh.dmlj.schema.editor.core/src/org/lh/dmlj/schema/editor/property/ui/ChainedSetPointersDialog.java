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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.common.Tools;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ChainedSetPointersDialog extends Dialog {
	private final MemberRole memberRole;
	private boolean ownerPointerManipulationForAllMembers;
	private boolean ownerPointers;
	private boolean priorPointers;
	
	private Button btnOwnerPointers;
	private Button btnOwnerPointersAllMembers;
	private Button btnOwnerPointersCurrentMember;
	private Button btnPriorPointers;
		
	public ChainedSetPointersDialog(Shell parentShell, MemberRole memberRole) {
		super(parentShell);
		this.memberRole = memberRole;
		setShellStyle(getShellStyle() | SWT.RESIZE); 
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		Assert.isTrue(memberRole.getSet().getMode() == SetMode.CHAINED, "logic error: not a chained set");
		shell.setText("Edit pointers for set " + Tools.removeTrailingUnderscore(memberRole.getSet().getName()));	    
	}	
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		enableAndDisable();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		var container = (Composite) super.createDialogArea(parent);
		
		var btnNextPointers = new Button(container, SWT.CHECK);
		btnNextPointers.setEnabled(false);
		btnNextPointers.setSelection(true);
		btnNextPointers.setText("NEXT pointers (owner record and all member records)");
		
		btnPriorPointers = new Button(container, SWT.CHECK);
		btnPriorPointers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnPriorPointers.setText("PRIOR pointers (owner record and all member records)");
		
		btnOwnerPointers = new Button(container, SWT.CHECK);
		btnOwnerPointers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnOwnerPointers.setText("OWNER pointers :");
		
		btnOwnerPointersCurrentMember = new Button(container, SWT.RADIO);
		btnOwnerPointersCurrentMember.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnOwnerPointersCurrentMember.setSelection(true);
		var gdBtnOwnerPointersCurrentMember = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdBtnOwnerPointersCurrentMember.horizontalIndent = 15;
		btnOwnerPointersCurrentMember.setLayoutData(gdBtnOwnerPointersCurrentMember);
		btnOwnerPointersCurrentMember.setText("For member record [...] only");
		
		var lblotherMemberRecords = new Label(container, SWT.NONE);
		var gdLblotherMemberRecords = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gdLblotherMemberRecords.verticalIndent = -10;
		gdLblotherMemberRecords.horizontalIndent = 30;
		lblotherMemberRecords.setLayoutData(gdLblotherMemberRecords);
		lblotherMemberRecords.setText("(other member records, if any, will remain unchanged)");
		
		btnOwnerPointersAllMembers = new Button(container, SWT.RADIO);
		btnOwnerPointersAllMembers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		var gdBtnOwnerPointersAllMembers = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdBtnOwnerPointersAllMembers.horizontalIndent = 15;
		btnOwnerPointersAllMembers.setLayoutData(gdBtnOwnerPointersAllMembers);
		btnOwnerPointersAllMembers.setText("For all other member records as well.");

		initialize();
		
		return container;
	}
	
	private void initialize() {
		btnPriorPointers.setSelection(memberRole.getPriorDbkeyPosition() != null);
		
		btnOwnerPointers.setSelection(memberRole.getOwnerDbkeyPosition() != null);
		
		var recordName = Tools.removeTrailingUnderscore(memberRole.getRecord().getName()); 
		btnOwnerPointersCurrentMember.setText("For member record " + recordName + " only");
		
		var multipleMemberSet = memberRole.getSet().getMembers().size() > 1;
		btnOwnerPointersAllMembers.setEnabled(multipleMemberSet);
		
		if (multipleMemberSet) {
			var b = memberRole.getOwnerDbkeyPosition() != null;
			var b2 = true;
			for (var aMemberRole : memberRole.getSet().getMembers()) {
				if (aMemberRole != memberRole && (aMemberRole.getOwnerDbkeyPosition() == null) == b) {
					b2 = false;
					break;
				}
			}
			btnOwnerPointersCurrentMember.setSelection(!b2);
			btnOwnerPointersAllMembers.setSelection(b2);
		}
	}

	private void enableAndDisable() {
		// no checks to perform
		
		// make sure we've got all information available should the user press the OK button
		priorPointers = btnPriorPointers.getSelection();
		ownerPointers = btnOwnerPointers.getSelection();
		ownerPointerManipulationForAllMembers = btnOwnerPointersAllMembers.getSelection();
				
		getButton(IDialogConstants.OK_ID).setEnabled(anythingChanged());
	}
	
	private boolean anythingChanged() {
		// check if the user has (un)checked the prior dbkey position checkbox
		var hasPriorPointers = memberRole.getPriorDbkeyPosition() != null;
		if (btnPriorPointers.getSelection() != hasPriorPointers) {
			return true;
		}
		
		// check if the user has (un)checked the owner dbkey position checkbox
		var hasOwnerPointers = memberRole.getOwnerDbkeyPosition() != null;
		if (btnOwnerPointers.getSelection() != hasOwnerPointers) {
			return true;
		}
		
		// if we're not dealing with a multiple member set and the user has NOT checked the "For all other member
		// records as well." checkbox, we're done (i.e. no changes to perform)
		if (memberRole.getSet().getMembers().size() == 1 || !btnOwnerPointersAllMembers.getSelection()) {
			return false;
		}
		
		// check if the multiple member set's other member records are affected
		for (var aMemberRole : memberRole.getSet().getMembers()) {
			hasOwnerPointers = aMemberRole.getOwnerDbkeyPosition() != null;
			if (btnOwnerPointers.getSelection() != hasOwnerPointers) {
				return true;
			}
		}
		
		// no changes (multiple member set)
		return false;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(375, 250);
	}
	
	public boolean isOwnerPointerManipulationForAllMembers() {
		return ownerPointerManipulationForAllMembers;
	}

	public boolean isOwnerPointers() {
		return ownerPointers;
	}

	public boolean isPriorPointers() {
		return priorPointers;
	}

}
