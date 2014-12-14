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

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;

@ModelChange(category=ModelChangeCategory.SET_FEATURES)
public class MoveGuideCommand extends ModelChangeBasicCommand {

	@Owner 	  private Guide 		 		guide; 
	@Features private EStructuralFeature[] 	features = {
		SchemaPackage.eINSTANCE.getGuide_Position()
	};
	
	private int positionDelta;
	
	public MoveGuideCommand(Guide guide, int positionDelta) {
		super("Move Guide");
		this.guide = guide;
		this.positionDelta = positionDelta;
	}		
	
	@Override
	public void execute() {		
		guide.setPosition(guide.getPosition() + positionDelta);		
	}
	
	@Override
	public void undo() {		
		guide.setPosition(guide.getPosition() - positionDelta);		
	}	
	
}
