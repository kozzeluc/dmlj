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

import java.text.NumberFormat;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Schema;

public class SetZoomLevelCommand extends Command {
	
	private boolean canUndo = true;
	
	private Schema schema; 
	private double zoomLevel;
	
	private double oldZoomLevel;
	
	public SetZoomLevelCommand(Schema schema, double zoomLevel) {
		this(schema, zoomLevel, true);
	}
	
	public SetZoomLevelCommand(Schema schema, double zoomLevel, boolean canUndo) {
		super("Zoom to " + NumberFormat.getPercentInstance().format(zoomLevel));
		this.schema = schema;
		this.zoomLevel = zoomLevel;
		this.canUndo = canUndo;
	}	
	
	@Override
	public boolean canUndo() {
		return canUndo;
	}
	
	@Override
	public void execute() {
		oldZoomLevel = schema.getDiagramData().getZoomLevel();
		schema.getDiagramData().setZoomLevel(zoomLevel);
	}	

	@Override
	public void undo() {
		schema.getDiagramData().setZoomLevel(oldZoomLevel);
	}
	
}
