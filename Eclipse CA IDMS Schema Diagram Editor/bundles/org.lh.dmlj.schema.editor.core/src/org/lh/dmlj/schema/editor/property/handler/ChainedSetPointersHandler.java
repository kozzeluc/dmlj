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
package org.lh.dmlj.schema.editor.property.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeCompoundCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.property.IMemberRoleProvider;
import org.lh.dmlj.schema.editor.property.ui.ChainedSetPointersDialog;

public class ChainedSetPointersHandler extends AbstractSetPointersHandler implements IHyperlinkHandler<EAttribute, Command> {
	private IMemberRoleProvider memberRoleProvider;
	
	public ChainedSetPointersHandler(IMemberRoleProvider memberRoleProvider) {
		this.memberRoleProvider = memberRoleProvider;
	}	

	@Override
	public Command hyperlinkActivated(EAttribute attribute) {	
		var memberRole = memberRoleProvider.getMemberRole();
		
		// create and open the dialog for maintaining a chained set's pointer settings; if the user presses the
		// cancel button, get out and return null
		var dialog = new ChainedSetPointersDialog(Display.getCurrent().getActiveShell(), memberRole);
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			return null;
		}
		
		// the user pressed the OK button, which means something has to be changed; build a list of commands to
		// execute on the command stack...
		var commands = new ArrayList<IModelChangeCommand>();
		
		// first stop: see if the user wants something to happen to the set's prior pointers (next pointers are
		// mandatory for chained sets); either no or all participants (owner/member) in the sets are equiped with
		// prior pointers
		var oldPriorPointers = memberRole.getPriorDbkeyPosition() != null;
		var newPriorPointers = dialog.isPriorPointers();
		if (oldPriorPointers != newPriorPointers) {
			commands.addAll(createCommandsToChangePriorPointers(memberRole));
		}
		
		// now deal with the owner pointers; owner pointers are only kept in the prefix of a set's member
		// record(s) and, in the case of multiple member sets, there can be a mix of member records with and
		// member records without owner pointers
		commands.addAll(createCommandsToChangeOwnerPointers(memberRole, dialog.isOwnerPointers(), dialog.isOwnerPointerManipulationForAllMembers()));
		
		// now is a good time to create the model change context
		var context = new ModelChangeContext(ModelChangeType.ADD_OR_REMOVE_SET_POINTERS);
		if (dialog.isOwnerPointerManipulationForAllMembers()) {
			context.putContextData(memberRole.getSet(), ModelChangeContext.setContextDataAssembler);
		} else {
			context.putContextData(memberRole, ModelChangeContext.memberRoleContextDataAssembler);
		}
		
		return createFinalCommand(context, memberRole.getSet().getName(), commands);
	}
	
	private final List<IModelChangeCommand> createCommandsToChangePriorPointers(MemberRole memberRole) {
		var commands = new ArrayList<IModelChangeCommand>();
		if (memberRole.getPriorDbkeyPosition() == null) {
			// the user has added prior pointers, so add a prior pointer in the prefix of the owner and all
			// member records at the first available position (i.e. create the commands to get the job done)
			commands.add(createAppendPointerCommand(memberRole.getSet().getOwner().getRecord(), memberRole.getSet().getOwner(),
					OWNER_PRIOR_POINTER_POSITION));				
			memberRole.getSet().getMembers().stream()
					.map(m -> createAppendPointerCommand(m.getRecord(), m, MEMBER_PRIOR_POINTER_POSITION))
					.forEach(commands::add);
		} else {
			// the user has removed the prior pointers for this set, so remove the prior pointer in the prefix of
			// the owner and all member records, shifting all pointers following the removed prior pointer one
			// place to the left (i.e. create the commands to get the job done)
			commands.addAll(createShiftPointersCommands(memberRole.getSet().getOwner().getRecord(), memberRole.getSet().getOwner(),
					OWNER_PRIOR_POINTER_POSITION));
			memberRole.getSet().getMembers().stream()
					.map(m -> createShiftPointersCommands(m.getRecord(), m, MEMBER_PRIOR_POINTER_POSITION))
					.forEach(commands::addAll);
		}
		return commands;
	}
	
	private final List<IModelChangeCommand> createCommandsToChangeOwnerPointers(MemberRole memberRole, boolean newOwnerPointers,
			boolean ownerPointerManipulationAppliesToAllMembers) {
		
		var commands = new ArrayList<IModelChangeCommand>();
		for (var aMemberRole : memberRole.getSet().getMembers()) {
			var oldOwnerPointers = aMemberRole.getOwnerDbkeyPosition() != null;
			if (oldOwnerPointers != newOwnerPointers && (aMemberRole == memberRole || ownerPointerManipulationAppliesToAllMembers)) {
				if (aMemberRole.getOwnerDbkeyPosition() == null) {
					// the user has added owner pointers, so add an owner pointer in the prefix of the member
					// record at the first available position (i.e. create the command to get the job done)
					var command = createAppendPointerCommand(aMemberRole.getRecord(), aMemberRole, MEMBER_OWNER_POINTER_POSITION);
					commands.add(command);
				} else {
					// the user has removed the owner pointers for this set, so remove the owner pointer in the
					// prefix the member record, shifting all pointers following the removed owner pointer one
					// place to the left (i.e. create the commands to get the job done)
					var commands2 = createShiftPointersCommands(aMemberRole.getRecord(), aMemberRole, MEMBER_OWNER_POINTER_POSITION);
					commands.addAll(commands2);
				}			
			}
		}
		return commands;
	}
	
	private Command createFinalCommand(ModelChangeContext context, String setName, List<IModelChangeCommand> commands) {
		if (commands.isEmpty()) {
			throw new IllegalStateException("logic error: no commands created");				
		} else if (commands.size() > 1) {			
			var label = "Change pointers for set '" + Tools.removeTrailingUnderscore(setName) + "'";	
			var cc = new ModelChangeCompoundCommand(label);
			cc.setContext(context);
			commands.stream()
					.map(Command.class::cast)
					.forEach(cc::add);
			return cc;
		} else {
			var command = commands.get(0);
			command.setContext(context);
			return (Command) command;
		}
	}

}
