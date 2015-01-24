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

import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;

public class CreateBendpointCommand extends AbstractBendpointCommand {
	
	private DiagramLocation bendpoint;
	protected ISupplier<ConnectionPart> connectionPartSupplier;	
	
	public CreateBendpointCommand(ConnectionPart connectionPart, int index, int x, int y) {
		super(connectionPart, index, x, y);
		this.connectionPart = connectionPart;
	}
	
	public CreateBendpointCommand(ISupplier<ConnectionPart> connectionPartSupplier, int index, 
								  int x, int y) {
		
		super(null, index, x, y); // we'll set the connectionPart and super.connectionPart when 
								  // execute() is invoked
		this.connectionPartSupplier = connectionPartSupplier;
	}	
	
	@Override
	public void execute() {
		if (connectionPartSupplier != null) {
			super.connectionPart = connectionPartSupplier.supply();
			connectionPart = super.connectionPart;
		}
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
