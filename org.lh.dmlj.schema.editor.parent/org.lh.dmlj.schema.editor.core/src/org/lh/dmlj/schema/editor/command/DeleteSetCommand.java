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

import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.command.helper.RemovableMemberRole;
import org.lh.dmlj.schema.editor.command.helper.RemovableOwnerRole;

/**
 * A command that removes a set from a schema, disconnects the owner- and member roles from the
 * owner-and member record types and cleans up the sort key and its elements in the case of a sorted
 * set.  This command can <b>NOT</b> deal with :<ul>
 * <li>multiple-member sets</li>
 * <li>a set whose member record type is stored VIA the set that is to be deleted</li>
 * <li>multiple connection parts, i.e. no connectors should be present</li>
 * <li>bendpoints</li>
 *</ul>
 */
public class DeleteSetCommand extends ModelChangeBasicCommand {

	private Schema schema;
	protected Set set;	
	
	private int indexOfSetInSchemasSets;	
	private RemovableOwnerRole ownerRoleToBecomeObsolete;
	private RemovableMemberRole memberRoleToBecomeObsolete;
	
	public DeleteSetCommand(Set set) {
		super("Delete set");
		this.set = set;
		schema = set.getSchema();
	}
	
	@Override
	public void execute() {
		rememberState();		
		deleteSet();		
	}
	
	private void rememberState() {
		indexOfSetInSchemasSets = schema.getSets().indexOf(set);
		ownerRoleToBecomeObsolete = new RemovableOwnerRole(set.getOwner());
		memberRoleToBecomeObsolete = new RemovableMemberRole(set.getMembers().get(0));		
	}	

	private void deleteSet() {		
		ownerRoleToBecomeObsolete.remove();
		memberRoleToBecomeObsolete.remove();
		schema.getSets().remove(set);
	}

	@Override
	public void redo() {
		deleteSet();				
	}
	
	@Override
	public void undo() {
		restoreSet();
	}
	
	private void restoreSet() {		
		ownerRoleToBecomeObsolete.restore();
		memberRoleToBecomeObsolete.restore();
		schema.getSets().add(indexOfSetInSchemasSets, set);
	}
	
}
