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
package org.lh.dmlj.schema.editor.part;

import org.eclipse.gef.EditPolicy;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.editor.policy.ModifiedNonResizableEditPolicy;

/**
 * A superclass for all non-resizable diagram nodes; although the ResizableDiagramNode type can be
 * provided as the type parameter (T); the diagram node will NOT be resizable.
 * @author Luc Hermans
 *
 * @param <T> the model type
 */
public abstract class AbstractNonResizableDiagramNodeEditPart<T extends DiagramNode> 
	extends AbstractDiagramNodeEditPart<T> {
	
	protected AbstractNonResizableDiagramNodeEditPart(T diagramNode) {
		super(diagramNode);
	}
	
	@Override
	public EditPolicy getResizeEditPolicy() {
		return new ModifiedNonResizableEditPolicy();
	}
	
}
