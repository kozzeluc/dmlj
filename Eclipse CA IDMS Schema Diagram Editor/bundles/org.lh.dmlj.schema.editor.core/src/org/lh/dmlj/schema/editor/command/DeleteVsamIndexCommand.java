/**
 * Copyright (C) 2015  Luc Hermans
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

import java.util.Arrays;

import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.command.helper.RemovableMemberRole;

public class DeleteVsamIndexCommand extends ModelChangeBasicCommand {	
	
	private Schema schema;
	
	private VsamIndex vsamIndex;
	
	private RemovableMemberRole memberRoleToRemove;	
	
	private int setInSchemaIndex;
	
	public DeleteVsamIndexCommand(VsamIndex vsamIndex) {
		super("Delete VSAM index");
		this.vsamIndex = vsamIndex;		
	}		

	@Override
	public void execute() {
		rememberState();
		deleteVsamIndex();
	}
	
	private void rememberState() {		
		rememberSchema();
		rememberSetData();
		rememberMembershipData();
	}

	private void rememberSchema() {
		schema = vsamIndex.getSet().getSchema();		
	}
	
	private void rememberSetData() {
		setInSchemaIndex = schema.getSets().indexOf(vsamIndex.getSet());		
	}
	
	private void rememberMembershipData() {		
		memberRoleToRemove = 
			new RemovableMemberRole(vsamIndex.getMemberRole(), 
									Arrays.asList(vsamIndex.getDiagramLocation()));				
	}
			
	private void deleteVsamIndex() {						
		removeMembershipData();
		removeSet();					
	}

	private void removeMembershipData() {
		memberRoleToRemove.remove();
	}	
	
	private void removeSet() {
		schema.getSets().remove(vsamIndex.getSet());
	}	
	
	@Override
	public void redo() {
		deleteVsamIndex();
	}

	@Override
	public void undo() {
		restoreVsamIndex();
	}
	
	private void restoreVsamIndex() {		
		restoreMembershipData();
		restoreSet();						
	}

	private void restoreMembershipData() {
		memberRoleToRemove.restore();
	}

	private void restoreSet() {
		schema.getSets().add(setInSchemaIndex, vsamIndex.getSet());
	}
	
}