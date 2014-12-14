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
package org.lh.dmlj.schema.editor.property.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.editor.command.ModelChangeCompoundCommand;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.property.IMemberRoleProvider;
import org.lh.dmlj.schema.editor.property.ui.IndexedSetPointersDialog;

public class IndexedSetPointersHandler 
	extends AbstractSetPointersHandler
	implements IHyperlinkHandler<EAttribute, Command> {	
	
	private IMemberRoleProvider memberRoleProvider;
	
	public IndexedSetPointersHandler(IMemberRoleProvider memberRoleProvider) {
		super();
		this.memberRoleProvider = memberRoleProvider;
	}	

	@Override
	public Command hyperlinkActivated(EAttribute attribute) {		
		
		// we need the MemberRole		
		MemberRole memberRole = memberRoleProvider.getMemberRole();
		
		// create and open the dialog for maintaining an indexed set's pointer settings; 
		// if the user presses the cancel button, get out and return a null 
		// Command		
		IndexedSetPointersDialog dialog = 
			new IndexedSetPointersDialog(Display.getCurrent().getActiveShell(),
										 memberRole);
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			// cancel button pressed
			return null;
		}
		
		// the user pressed the OK button, which means something has to be
		// changed (regarding the member record's pointers); build a list of 
		// commands to execute on the command stack...
		List<Command> commands = new ArrayList<>();
		
		// index pointers
		if (memberRole.getIndexDbkeyPosition() == null && 
			dialog.isIndexPointers()) {
			
			// there was no index pointer but the user wants it to be appended
			// to the prefix of the member record
			commands.add(createAppendPointerCommand(memberRole.getRecord(),
					  								memberRole,
					  								MEMBER_INDEX_POINTER_POSITION));
		} else if (memberRole.getIndexDbkeyPosition() != null && 
				   !dialog.isIndexPointers()) {
			
			// there was an index pointer for the set but the user wants it
			// removed from the member record's prefix
			commands.addAll(createShiftPointersCommands(memberRole.getRecord(), 
			  		  									memberRole,
			  		  									MEMBER_INDEX_POINTER_POSITION));
		}
		
		// owner pointers
		if (memberRole.getOwnerDbkeyPosition() == null && 
			dialog.isOwnerPointers()) {
			
			// there was no owner pointer but the user wants it to be appended
			// to the prefix of the member record
			commands.add(createAppendPointerCommand(memberRole.getRecord(),
					  								memberRole,
					  								MEMBER_OWNER_POINTER_POSITION));
		} else if (memberRole.getOwnerDbkeyPosition() != null && 
				   !dialog.isOwnerPointers()) {
			
			// there was an owner pointer for the set but the user wants it
			// removed from the member record's prefix
			commands.addAll(createShiftPointersCommands(memberRole.getRecord(), 
			  		  									memberRole,
			  		  									MEMBER_OWNER_POINTER_POSITION));
		}				
		
		// create a compound command if needed; there should always be at least 
		// 1 command to add to it
		if (commands.isEmpty()) {
			throw new RuntimeException("logic error: no commands created");				
		} else if (commands.size() > 1) {			
			String label = 
				"Change pointers for set '" + 
				Tools.removeTrailingUnderscore(memberRole.getSet().getName()) + 
				"'";			
			ModelChangeCompoundCommand cc = new ModelChangeCompoundCommand(label);
			for (Command command : commands) {
				cc.add(command);
			}
			return cc;
		} else {
			return commands.get(0);
		}
		
	}

}
