/**
 * Copyright (C) 2016  Luc Hermans
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
package org.lh.dmlj.schema.editor.anchor;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

/**
 * An anchor that will locate the target (member) connection endpoint for a 
 * record as follows :
 * <ul>
 * <li>If the targetEndpointLocation attribute of the connection's 
 *     ConnectionPart model object is set, these offsets to the record figure 
 *     location (stored at a scale of 1) are used to calculate the location of 
 *     the connection endpoint.<br><br></li>
 * <li>If the targetEndpointLocation attribute of the connection's 
 *     ConnectionPart model object is NOT set, the anchor location is determined 
 *     depending on the kind of set we're dealing with :<br><br>
 *     <ul> 
 *     <li>In the case of a <i>user owned set</i>, the location returned by a 
 *         standard chopbox anchor for the record figure will be used.
 *         <br><br></li>
 *     <li>In the case of a <i>system owned set</i> or <i>VSAM index</i>, the location returned is 1 
 *         of the 4 corners on the record figure, depending on the reference 
 *         location.</li>
 *     </ul>
 * </ul>
 */
public class RecordTargetAnchor extends AbstractRecordAnchor {

	/**
	 * Constructs a RecordTargetAnchor with the given record figure.
	 * @param figure The target (member) record figure
	 * @param connectionPart The ConnectionPart model object representing the 
	 *        connection
	 */
	public RecordTargetAnchor(RecordFigure figure, 
									ConnectionPart connectionPart) {
		super(figure, connectionPart);
	}

	@Override
	protected Point getDefaultLocation(Point originalReference) {
		
		// in the case of a user owned set, just call the super implementation of this method...
		Set set = connectionPart.getMemberRole().getSet();
		if (set.isChained() || set.isIndexed() && set.getSystemOwner() == null) {
			return super.getDefaultLocation(originalReference);
		}
		
		// go pick 1 of the 4 corners depending on the reference location...
		
		PrecisionPoint reference = new PrecisionPoint(originalReference);		
		
		PrecisionPoint top = 
			new PrecisionPoint(getOwner().getBounds().getTop());
		getOwner().translateToAbsolute(top);
		
		PrecisionPoint left = 
			new PrecisionPoint(getOwner().getBounds().getLeft());
		getOwner().translateToAbsolute(left);
		
		if (reference.preciseX() <= top.preciseX() &&
			reference.preciseY() <= left.preciseY()) {
			
			PrecisionPoint topLeft = 
				new PrecisionPoint(getOwner().getBounds().getTopLeft());
			getOwner().translateToAbsolute(topLeft);
			return topLeft;
		}
		
		PrecisionPoint right = 
			new PrecisionPoint(getOwner().getBounds().getRight());
		getOwner().translateToAbsolute(right);
		
		if (reference.preciseX() > top.preciseX() &&
			reference.preciseY() <= right.preciseY()) {
			
			PrecisionPoint topRight = 
				new PrecisionPoint(getOwner().getBounds().getTopRight());
			getOwner().translateToAbsolute(topRight);
			return topRight;
		}
		
		PrecisionPoint bottom = 
			new PrecisionPoint(getOwner().getBounds().getBottom());
		getOwner().translateToAbsolute(bottom);
		
		if (reference.preciseX() <= bottom.preciseX() &&
			reference.preciseY() > left.preciseY()) {
			
			PrecisionPoint bottomLeft = 
				new PrecisionPoint(getOwner().getBounds().getBottomLeft());
			getOwner().translateToAbsolute(bottomLeft);
			return bottomLeft;
		}
		
		PrecisionPoint bottomRight = 
			new PrecisionPoint(getOwner().getBounds().getBottomRight());
		getOwner().translateToAbsolute(bottomRight);
		
		return bottomRight;		
	}

	@Override
	protected DiagramLocation getModelEndpoint() {
		return connectionPart.getTargetEndpointLocation();
	}
	
}
