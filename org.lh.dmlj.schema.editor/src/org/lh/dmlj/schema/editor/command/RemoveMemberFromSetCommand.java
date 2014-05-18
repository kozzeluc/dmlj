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
package org.lh.dmlj.schema.editor.command;

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.REMOVE_ITEM;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.command.helper.RemovableMemberRole;
import org.lh.dmlj.schema.editor.common.Tools;

@ModelChange(category=REMOVE_ITEM)
public class RemoveMemberFromSetCommand extends Command {
		
	@Owner 	   private Set 		  set;
	@Reference private EReference reference = SchemaPackage.eINSTANCE.getSet_Members();
	@Item 	   private MemberRole memberRole;
	
	private RemovableMemberRole memberRoleToBecomeObsolete;

	public RemoveMemberFromSetCommand(MemberRole memberRole) {
		super("Remove member record type from set " + 
			  Tools.removeTrailingUnderscore(memberRole.getSet().getName()));
		this.memberRole = memberRole;
		set = memberRole.getSet();		
	}
	
	@Override
	public void execute() {
		rememberState();
		removeRecordFromSet();		
	}

	private void rememberState() {
		memberRoleToBecomeObsolete = new RemovableMemberRole(memberRole);		
	}
	
	private void removeRecordFromSet() {		
		memberRoleToBecomeObsolete.remove();
	}
	
	@Override
	public void redo() {		
		removeRecordFromSet();		
	}
	
	@Override
	public void undo() {
		memberRoleToBecomeObsolete.restore();		
	}
	
}
