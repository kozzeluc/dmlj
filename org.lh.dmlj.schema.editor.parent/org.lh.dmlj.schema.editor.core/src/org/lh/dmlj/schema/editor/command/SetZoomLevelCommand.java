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

import java.text.NumberFormat;

import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.Schema;


public class SetZoomLevelCommand extends ModelChangeBasicCommand {
		
	private DiagramData diagramData; 
	
	private double oldZoomLevel;
	private double newZoomLevel;	
	
	private boolean canUndo = true;	
	
	public SetZoomLevelCommand(Schema schema, double zoomLevel) {
		this(schema, zoomLevel, true);
	}
	
	public SetZoomLevelCommand(Schema schema, double zoomLevel, boolean canUndo) {
		super("Zoom to " + NumberFormat.getPercentInstance().format(zoomLevel));
		this.diagramData = schema.getDiagramData();
		this.newZoomLevel = zoomLevel;
		this.canUndo = canUndo;
	}	
	
	@Override
	public boolean canUndo() {
		return canUndo;
	}
	
	@Override
	public void execute() {
		oldZoomLevel = diagramData.getZoomLevel();
		diagramData.setZoomLevel(newZoomLevel);
	}
	
	@Override
	public void redo() {
		if (!canUndo) {
			throw new RuntimeException("cannot redo (canUndo == false)");
		}		
		diagramData.setZoomLevel(newZoomLevel);
	}

	@Override
	public void undo() {
		if (!canUndo) {
			throw new RuntimeException("cannot undo");
		}
		diagramData.setZoomLevel(oldZoomLevel);
	}
	
}
