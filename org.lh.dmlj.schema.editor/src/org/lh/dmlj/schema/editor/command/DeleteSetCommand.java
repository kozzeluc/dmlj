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

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;

@ModelChange(category=ModelChangeCategory.REMOVE_ITEM)
public class DeleteSetCommand extends Command {

	@Owner	   private Schema 	  schema;
	@Reference private EReference reference = SchemaPackage.eINSTANCE.getSchema_Sets();
	@Item 	   private Set 		  set;	
	
	public DeleteSetCommand(Set set) {
		super("Delete set");
		this.set = set;
		schema = set.getSchema();
	}
	
	
	@Override
	public void execute() {		
		System.out.println("DeleteSetCommand.execute()");
	}
	
	@Override
	public void redo() {
		
		Assert.isTrue(set.getMembers().size() == 1, "cannot directly delete a multiple-member set");
		
		System.out.println("DeleteSetCommand.redo()");
	}
	
	@Override
	public void undo() {
		System.out.println("DeleteSetCommand.undo()");
	}
	
}
