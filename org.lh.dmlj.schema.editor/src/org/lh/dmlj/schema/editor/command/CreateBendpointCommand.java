/**
 * Copyright (C) 2013  Luc Hermans
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

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.ADD_ITEM;

import org.eclipse.emf.ecore.EReference;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;

@ModelChange(category=ADD_ITEM)
public class CreateBendpointCommand extends AbstractBendpointCommand {
	
	@Owner 	   private ConnectionPart 	connectionPart;
	@Item  	   private DiagramLocation 	bendpoint;
	@Reference private EReference 		reference = 
		SchemaPackage.eINSTANCE.getConnectionPart_BendpointLocations();	
	
	public CreateBendpointCommand(ConnectionPart connectionPart, int index, int x, int y) {
		super(connectionPart, index, x, y);
		this.connectionPart = connectionPart;
	}
	
	@Override
	public void execute() {
		bendpoint = insertBendpoint(connectionPartIndex, x, y);		
	}
	
	@Override
	public void redo() {
		restoreBendpoint(bendpoint, connectionPartIndex, -1);
	}
	
	@Override
	public void undo() {
		removeBendpoint(connectionPartIndex);		
	}
}
