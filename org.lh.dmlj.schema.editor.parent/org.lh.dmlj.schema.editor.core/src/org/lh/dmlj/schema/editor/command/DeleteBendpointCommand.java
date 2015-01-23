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
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;

public class DeleteBendpointCommand extends AbstractBendpointCommand {	
	
	protected ConnectionPart connectionPart;
	private DiagramLocation bendpoint;
	
	private int locationsIndex;
	
	public DeleteBendpointCommand(ConnectionPart connectionPart, int connectionPartIndex) {
		super(connectionPart, connectionPartIndex);
		this.connectionPart = connectionPart;
	}
	
	@Override
	public void execute() {
		bendpoint = connectionPart.getBendpointLocations().get(connectionPartIndex);
		DiagramData diagramData = 
			connectionPart.getMemberRole().getSet().getSchema().getDiagramData();
		locationsIndex = diagramData.getLocations().indexOf(bendpoint);
		redo();
	}	
	
	@Override
	public void redo() {
		removeBendpoint(connectionPartIndex); // will set oldX and oldY
	}
	
	@Override
	public void undo() {
		restoreBendpoint(bendpoint, connectionPartIndex, locationsIndex);		
	}
}
