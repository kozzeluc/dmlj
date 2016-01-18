/**
 * Copyright (C) 2016  Luc Hermans
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.common.Tools;

public class IndexedSetPointersDialog extends Dialog {

	private Button 	   btnIndexPointers;
	private Button 	   btnOwnerPointers;
	private boolean    indexPointers;
	private MemberRole memberRole;
	private boolean    ownerPointers;	

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public IndexedSetPointersDialog(Shell parentShell, MemberRole memberRole) {
		super(parentShell);
		this.memberRole = memberRole;
		setShellStyle(getShellStyle() | SWT.RESIZE); 
	}
	
	private boolean anythingChanged() {
		
		// check if the user has (un)checked the index dbkey position checkbox
		boolean hasIndexPointers = memberRole.getIndexDbkeyPosition() != null;
		if (btnIndexPointers.getSelection() != hasIndexPointers) {
			return true;
		}
		
		// check if the user has (un)checked the owner dbkey position checkbox
		boolean hasOwnerPointers = memberRole.getOwnerDbkeyPosition() != null;
		if (btnOwnerPointers.getSelection() != hasOwnerPointers) {
			return true;
		}
		
		// nothing changed
		return false;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		Assert.isTrue(memberRole.getSet().getMode() == SetMode.INDEXED, 
					  "logic error: not an indexed set");
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
		
		Button btnNextPointersowner = new Button(container, SWT.CHECK);
		btnNextPointersowner.setEnabled(false);
		btnNextPointersowner.setSelection(true);
		btnNextPointersowner.setText("NEXT pointers (owner record)");
		
		Button btnPriorPointrsowner = new Button(container, SWT.CHECK);
		btnPriorPointrsowner.setSelection(true);
		btnPriorPointrsowner.setEnabled(false);
		btnPriorPointrsowner.setText("PRIOR pointers (owner record)");
		
		btnIndexPointers = new Button(container, SWT.CHECK);
		btnIndexPointers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnIndexPointers.setText("INDEX pointers (member record)");
		
		btnOwnerPointers = new Button(container, SWT.CHECK);
		btnOwnerPointers.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableAndDisable();
			}
		});
		btnOwnerPointers.setText("OWNER pointers (member record)");
		
		initialize();

		return container;
	}

	private void enableAndDisable() {
	
		// no checks to perform
		
		// make sure we've got all information available should the user press
		// the OK button
		indexPointers = btnIndexPointers.getSelection();
		ownerPointers = btnOwnerPointers.getSelection();		
		
		// only enable the OK Button if anything will change
		Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(anythingChanged());				
		
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(375, 200);
	}
	
	private void initialize() {
		
		btnIndexPointers.setSelection(memberRole.getIndexDbkeyPosition() != null);
		btnIndexPointers.setEnabled(memberRole.getSet().getSystemOwner() != null &&
									memberRole.getMembershipOption() == SetMembershipOption.MANDATORY_AUTOMATIC);
		
		btnOwnerPointers.setSelection(memberRole.getOwnerDbkeyPosition() != null);
		btnOwnerPointers.setEnabled(memberRole.getSet().getSystemOwner() == null);		
		
	}
	
	public boolean isIndexPointers() {
		return indexPointers;
	}	
	
	public boolean isOwnerPointers() {
		return ownerPointers;
	}	

}
