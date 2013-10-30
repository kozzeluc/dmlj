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

	private Button 	   btnOwnerPointers;
	private Button 	   btnOwnerPointersAllMembers;
	private Button     btnOwnerPointersCurrentMember;
	private Button 	   btnPriorPointers;
	private MemberRole memberRole;
	private boolean    ownerPointerManipulationForAllMembers;
	private boolean    ownerPointers;
	private boolean    priorPointers;
	
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ChainedSetPointersDialog(Shell parentShell, MemberRole memberRole) {
		super(parentShell);
		this.memberRole = memberRole;
	}

	private boolean anythingChanged() {
		
		// check if the user has (un)checked the prior dbkey position checkbox
		boolean hasPriorPointers = memberRole.getPriorDbkeyPosition() != null;
		if (btnPriorPointers.getSelection() != hasPriorPointers) {
			return true;
		}
		
		// check if the user has (un)checked the owner dbkey position checkbox
		boolean hasOwnerPointers = memberRole.getOwnerDbkeyPosition() != null;
		if (btnOwnerPointers.getSelection() != hasOwnerPointers) {
			return true;
		}
		
		// if we're not dealing with a multiple member set and the user has NOT
		// checked the "For all other member records as well." checkbox, we're 
		// done (i.e. no changes to perform)
		if (memberRole.getSet().getMembers().size() == 1 ||
			!btnOwnerPointersAllMembers.getSelection()) {
			
			return false;
		}
		
		// check if the multiple member set's other member records are affected
		for (MemberRole aMemberRole : memberRole.getSet().getMembers()) {
			hasOwnerPointers = aMemberRole.getOwnerDbkeyPosition() != null;
			if (btnOwnerPointers.getSelection() != hasOwnerPointers) {
				return true;
			}
		}
		
		// no changes (multiple member set)
		return false;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		Assert.isTrue(memberRole.getSet().getMode() == SetMode.CHAINED, 
					  "logic error: not a chained set");
		shell.setText("Edit pointers for set " + 
					  Tools.removeTrailingUnderscore(memberRole.getSet()
							  						 		   .getName()));	    
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
		
		Button btnNextPointers = new Button(container, SWT.CHECK);
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
		GridData gd_btnOwnerPointersCurrentMember = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOwnerPointersCurrentMember.horizontalIndent = 15;
		btnOwnerPointersCurrentMember.setLayoutData(gd_btnOwnerPointersCurrentMember);
		btnOwnerPointersCurrentMember.setText("For member record [...] only");
		
		Label lblotherMemberRecords = new Label(container, SWT.NONE);
		GridData gd_lblotherMemberRecords = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_lblotherMemberRecords.verticalIndent = -10;
		gd_lblotherMemberRecords.horizontalIndent = 30;
		lblotherMemberRecords.setLayoutData(gd_lblotherMemberRecords);
		lblotherMemberRecords.setText("(other member records, if any, will remain unchanged)");
		
		btnOwnerPointersAllMembers = new Button(container, SWT.RADIO);
		btnOwnerPointersAllMembers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		GridData gd_btnOwnerPointersAllMembers = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOwnerPointersAllMembers.horizontalIndent = 15;
		btnOwnerPointersAllMembers.setLayoutData(gd_btnOwnerPointersAllMembers);
		btnOwnerPointersAllMembers.setText("For all other member records as well.");

		initialize();
		
		return container;
	}
	
	private void enableAndDisable() {
		
		// no checks to perform
		
		// make sure we've got all information available should the user press
		// the OK button
		priorPointers = btnPriorPointers.getSelection();
		ownerPointers = btnOwnerPointers.getSelection();
		ownerPointerManipulationForAllMembers = 
			btnOwnerPointersAllMembers.getSelection();
		
		// only enable the OK Button if anything will change
		Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(anythingChanged());
		
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(375, 250);
	}
	
	private void initialize() {
		
		btnPriorPointers.setSelection(memberRole.getPriorDbkeyPosition() != null);
		
		btnOwnerPointers.setSelection(memberRole.getOwnerDbkeyPosition() != null);
		
		String recordName = 
			Tools.removeTrailingUnderscore(memberRole.getRecord().getName()); 
		btnOwnerPointersCurrentMember.setText("For member record " + 
											  recordName + " only");
		
		boolean multipleMemberSet = memberRole.getSet().getMembers().size() > 1;
		btnOwnerPointersAllMembers.setEnabled(multipleMemberSet);
		
		if (multipleMemberSet) {
			boolean b = memberRole.getOwnerDbkeyPosition() != null;
			boolean b2 = true;
			for (MemberRole aMemberRole : memberRole.getSet().getMembers()) {
				if (aMemberRole != memberRole &&
					!(aMemberRole.getOwnerDbkeyPosition() != null) == b) {
					
					b2 = false;
					break;
				}
			}
			btnOwnerPointersCurrentMember.setSelection(!b2);
			btnOwnerPointersAllMembers.setSelection(b2);
		}
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
