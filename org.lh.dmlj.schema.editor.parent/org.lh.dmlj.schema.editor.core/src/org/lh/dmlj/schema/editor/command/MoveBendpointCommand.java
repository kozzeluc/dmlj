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

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.SET_FEATURES;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;

@ModelChange(category=SET_FEATURES)
public class MoveBendpointCommand extends ModelChangeBasicCommand {	
	
	@Owner    private DiagramLocation 	   bendpoint;	
	@Features private EStructuralFeature[] features = {
		SchemaPackage.eINSTANCE.getDiagramLocation_X(),
		SchemaPackage.eINSTANCE.getDiagramLocation_Y()
	};	
	
	private int oldX;
	private int oldY;
	
	private int newX;
	private int newY;
	
	public MoveBendpointCommand(ConnectionPart connectionPart, int index, int x, int y) {
		super();
		bendpoint = connectionPart.getBendpointLocations().get(index);
		newX = x;
		newY = y;
	}
	
	@Override
	public void execute() {
		oldX = bendpoint.getX();
		oldY = bendpoint.getY();
		redo();
	}
	
	@Override
	public void redo() {
		bendpoint.setX(newX);
		bendpoint.setY(newY);
	}
	
	@Override
	public void undo() {
		bendpoint.setX(oldX);
		bendpoint.setY(oldY);
	}
	
}
