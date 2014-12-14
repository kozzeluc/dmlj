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
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.ResizableDiagramNode;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;

@ModelChange(category=SET_FEATURES)
public class ResizeDiagramNodeCommand extends ModelChangeBasicCommand {
	
	@Owner	  private ResizableDiagramNode diagramNode;
	@Features private EStructuralFeature[] features = {
		SchemaPackage.eINSTANCE.getResizableDiagramNode_Width(),
		SchemaPackage.eINSTANCE.getResizableDiagramNode_Height()
	};
	
	private short oldWidth;
	private short oldHeight;
	
	private short newWidth;
	private short newHeight;
	
	@SuppressWarnings("unused")
	private ResizeDiagramNodeCommand() {
		super();
	}
	
	public ResizeDiagramNodeCommand(ResizableDiagramNode diagramNode, short width, short height) {
		super();
		this.diagramNode = diagramNode;
		this.newWidth = width;
		this.newHeight = height;
		if (diagramNode instanceof DiagramLabel) {
			setLabel("Resize diagram label");
		} else {
			setLabel("Resize");
		}
	}
	
	@Override
	public void execute() {
		oldWidth = diagramNode.getWidth();
		oldHeight = diagramNode.getHeight();
		redo();
	}
	
	@Override
	public void redo() {
		diagramNode.setWidth(newWidth);
		diagramNode.setHeight(newHeight);
	}
	
	@Override
	public void undo() {
		diagramNode.setWidth(oldWidth);
		diagramNode.setHeight(oldHeight);
	}
	
}
