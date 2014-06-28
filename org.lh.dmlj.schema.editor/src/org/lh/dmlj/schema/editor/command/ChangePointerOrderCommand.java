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

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixForPointerReordering;

@ModelChange(category=ModelChangeCategory.SET_FEATURES)
public class ChangePointerOrderCommand extends Command {

	@Owner 
	private SchemaRecord record;
	
	@Features 
	private EStructuralFeature[] features = {
		SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition(),
		SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition(),
		SchemaPackage.eINSTANCE.getMemberRole_NextDbkeyPosition(),
		SchemaPackage.eINSTANCE.getMemberRole_PriorDbkeyPosition(),
		SchemaPackage.eINSTANCE.getMemberRole_OwnerDbkeyPosition(),
		SchemaPackage.eINSTANCE.getMemberRole_IndexDbkeyPosition(),
	};
	
	private List<Pointer<?>> newPointerOrder;
	private PrefixForPointerReordering prefix;
	
	public ChangePointerOrderCommand(SchemaRecord record, List<Pointer<?>> newPointerOrder) {
		super("Reorder pointers");
		this.record = record;
		this.newPointerOrder = newPointerOrder;
	}
	
	@Override
	public void execute() {
		createPrefix();
		reorderPointers();
	}
	
	private void createPrefix() {
		prefix = PrefixFactory.newPrefixForPointerReordering(record, newPointerOrder);		
	}

	private void reorderPointers() {
		prefix.reorderPointers();		
	}

	@Override
	public void undo() {
		resetPointers();
	}
	
	private void resetPointers() {
		prefix.reset();
	}

	@Override
	public void redo() {
		reorderPointers();
	}
	
}
