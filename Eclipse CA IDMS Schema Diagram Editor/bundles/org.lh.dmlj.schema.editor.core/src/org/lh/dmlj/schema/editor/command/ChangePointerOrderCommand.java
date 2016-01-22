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

import java.util.List;

import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixForPointerReordering;

public class ChangePointerOrderCommand extends ModelChangeBasicCommand {

	protected SchemaRecord record;
	
	private List<Pointer<?>> newPointerOrder;
	private PrefixForPointerReordering prefix;
	
	protected ISupplier<List<Pointer<?>>> pointerSupplier;
	
	public ChangePointerOrderCommand(SchemaRecord record, List<Pointer<?>> newPointerOrder) {
		super("Reorder pointers");
		this.record = record;
		this.newPointerOrder = newPointerOrder;
	}
	
	public ChangePointerOrderCommand(SchemaRecord record, ISupplier<List<Pointer<?>>> pointerSupplier) {		
		super("Reorder pointers");
		this.record = record;
		this.pointerSupplier = pointerSupplier;
	}
	
	@Override
	public void execute() {
		if (pointerSupplier != null) {
			newPointerOrder = pointerSupplier.supply();
		}
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
