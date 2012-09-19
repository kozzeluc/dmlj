package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.editor.common.Tools;

public class ChainedSetPointersHandler 
	extends AbstractSetPointersHandler
	implements IHyperlinkHandler {	
	
	private IMemberRoleProvider memberRoleProvider;
	
	ChainedSetPointersHandler(IMemberRoleProvider memberRoleProvider) {
		super();
		this.memberRoleProvider = memberRoleProvider;
	}	

	@Override
	public Command hyperlinkActivated(EAttribute attribute) {		
		
		// we need the MemberRole		
		MemberRole memberRole = memberRoleProvider.getMemberRole();
		
		// create and open the dialog for maintaining a set's pointer settings; 
		// if the user presses the cancel button, get out and return a null 
		// Command		
		ChainedSetPointersDialog dialog = 
			new ChainedSetPointersDialog(Display.getCurrent().getActiveShell(),
										 memberRole);
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			// cancel button pressed
			return null;
		}
		
		// the user pressed the OK button, which means something has to be
		// changed; build a list of commands to execute on the command stack...
		List<Command> commands = new ArrayList<>();
		
		// first stop: see if the user wants something to happen to the set's
		// prior pointers (next pointers are mandatory for chained sets); either
		// no or all participants (owner/member) in the sets are equiped with
		// prior pointers
		boolean oldPriorPointers = memberRole.getPriorDbkeyPosition() != null;
		boolean newPriorPointers = dialog.isPriorPointers();
		if (oldPriorPointers != newPriorPointers) {
			// yes, the user wants the prior pointers changed
			if (memberRole.getPriorDbkeyPosition() == null) {
				// the user has added prior pointers, so add a prior pointer in
				// the prefix of the owner and all member records at the first
				// available position (i.e. create the commands to get the job 
				// done)
				commands.add(createAppendPointerCommand(memberRole.getSet()
																  .getOwner()
																  .getRecord(),
														memberRole.getSet()
																  .getOwner(),
														OWNER_PRIOR_POINTER_POSITION));				
				for (MemberRole aMemberRole : memberRole.getSet().getMembers()) {
					commands.add(createAppendPointerCommand(aMemberRole.getRecord(),
															memberRole,
															MEMBER_PRIOR_POINTER_POSITION));
				}
			} else {
				// the user has removed the prior pointers for this set, so 
				// remove the prior pointer in the prefix of the owner and all
				// member records, shifting all pointers following the removed
				// prior pointer one place to the left (i.e. create the commands 
				// to get the job done)				
				commands.addAll(createShiftPointersCommands(memberRole.getSet()
															  		  .getOwner()
																	  .getRecord(), 
														    memberRole.getSet()
														   			  .getOwner(),
														    OWNER_PRIOR_POINTER_POSITION));
				for (MemberRole aMemberRole : memberRole.getSet().getMembers()) {
					commands.addAll(createShiftPointersCommands(aMemberRole.getRecord(),
															    aMemberRole,
													            MEMBER_PRIOR_POINTER_POSITION));
				}				
			}
		}
		
		// now deal with the owner pointers; owner pointers are only kept in the
		// prefix of a set's member record(s) and, in the case of multiple 
		// member sets, there can be a mix of member records with and member
		// records without owner pointers
		boolean newOwnerPointers = dialog.isOwnerPointers();
		for (MemberRole aMemberRole : memberRole.getSet().getMembers()) {
			boolean oldOwnerPointers = 				
				aMemberRole.getOwnerDbkeyPosition() != null;
			if (oldOwnerPointers != newOwnerPointers &&
				(aMemberRole == memberRole ||
				 dialog.isOwnerPointerManipulationForAllMembers())) {
				
				if (aMemberRole.getOwnerDbkeyPosition() == null) {
					// the user has added owner pointers, so add an owner 
					// pointer in the prefix of the member record at the first
					// available position (i.e. create the command to get the 
					// job done)
					Command command = 
						createAppendPointerCommand(aMemberRole.getRecord(), 
												   aMemberRole, 
												   MEMBER_OWNER_POINTER_POSITION);
					commands.add(command);
				} else {
					// the user has removed the owner pointers for this set, so 
					// remove the owner pointer in the prefix the member record,
					// shifting all pointers following the removed owner pointer 
					// one place to the left (i.e. create the commands to get 
					// the job done)
					List<Command> commands2 = 
						createShiftPointersCommands(aMemberRole.getRecord(), 
													aMemberRole, 
													MEMBER_OWNER_POINTER_POSITION);
					commands.addAll(commands2);
				}			
			}
		}
		
		// create a compound command; there should always be at least 1 command
		// to add to it
		if (commands.isEmpty()) {
			throw new RuntimeException("logic error: no commands created");				
		}			
		String label = 
				"Change pointers for set '" + 
				Tools.removeTrailingUnderscore(memberRole.getSet().getName()) + 
				"'";			
		CompoundCommand cc = new CompoundCommand(label);
		for (int i = 1; i < commands.size(); i++) {
			cc.add(commands.get(i));
		}		
		
		// return the compound command		
		return cc;
		
	}

}