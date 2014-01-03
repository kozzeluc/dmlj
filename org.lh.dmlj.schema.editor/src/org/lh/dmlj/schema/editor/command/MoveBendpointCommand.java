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

import org.lh.dmlj.schema.ConnectionPart;

public class MoveBendpointCommand extends AbstractBendpointCommand {	
	
	public MoveBendpointCommand(ConnectionPart connectionPart, int index, int x, 
								int y) {
		super(connectionPart, index, x, y);		
	}
	
	@Override
	public void execute() {
		removeBendpoint(connectionPartIndex);	// will set oldX and oldY
		insertBendpoint(connectionPartIndex, x, y);
	}
	
	@Override
	public void undo() {
		// saveguard oldX and oldY because they will be changed by the call to
		// removeBendpoint(index)...
		int oldXa = oldX;
		int oldYa = oldY;
		removeBendpoint(connectionPartIndex);	// will set oldX and oldY to x and y
		insertBendpoint(connectionPartIndex, oldXa, oldYa);
	}
	
}
