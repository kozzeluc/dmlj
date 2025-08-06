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
package org.lh.dmlj.schema.editor.command;

import java.util.List;
import java.util.function.Supplier;

import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixForPointerReordering;

public class ChangePointerOrderCommand extends ModelChangeBasicCommand {
	protected final SchemaRecord schemaRecord;
	
	private List<Pointer<?>> newPointerOrder;
	private PrefixForPointerReordering prefix;
	
	protected Supplier<List<Pointer<?>>> pointerSupplier;
	
	public ChangePointerOrderCommand(SchemaRecord schemaRecord, List<Pointer<?>> newPointerOrder) {
		super("Reorder pointers");
		this.schemaRecord = schemaRecord;
		this.newPointerOrder = newPointerOrder;
	}
	
	public ChangePointerOrderCommand(SchemaRecord schemaRecord, Supplier<List<Pointer<?>>> pointerSupplier) {		
		super("Reorder pointers");
		this.schemaRecord = schemaRecord;
		this.pointerSupplier = pointerSupplier;
	}
	
	@Override
	public void execute() {
		if (pointerSupplier != null) {
			newPointerOrder = pointerSupplier.get();
		}
		createPrefix();
		reorderPointers();
	}
	
	private void createPrefix() {
		prefix = PrefixFactory.newPrefixForPointerReordering(schemaRecord, newPointerOrder);		
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
