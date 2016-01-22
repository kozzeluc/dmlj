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
package org.lh.dmlj.schema.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.command.CreateIndexCommand;
import org.lh.dmlj.schema.editor.command.CreateVsamIndexCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;

/**
 * An edit policy that enables creating system owned indexed sets and VSAM indexes.
 */
public class RecordXYLayoutEditPolicy extends XYLayoutEditPolicy {

	private SchemaRecord record;	
	
	public RecordXYLayoutEditPolicy(SchemaRecord record) {
		super();
		this.record = record;		
	}
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {				
		if (request.getNewObjectType() == SystemOwner.class) {
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.ADD_SYSTEM_OWNED_SET);
			CreateIndexCommand command = new CreateIndexCommand(record);	
			command.setContext(context);
			return command;
		} else if (request.getNewObjectType() == VsamIndex.class &&
				   (record.isVsam() || record.isVsamCalc()) &&
				   Tools.getDefaultSortKeyElement(record) != null) {
			
			// VSAM indexes are always SORTED so make we need to be sure we have a relevant element 
			// that we can use as the sort key
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.ADD_VSAM_INDEX);
			CreateVsamIndexCommand command = new CreateVsamIndexCommand(record);	
			command.setContext(context);
			return command;
		} else {
			return null;
		}				
	}
	
}
