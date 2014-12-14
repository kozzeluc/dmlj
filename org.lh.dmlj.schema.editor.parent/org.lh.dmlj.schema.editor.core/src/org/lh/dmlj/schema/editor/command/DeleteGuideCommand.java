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

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.REMOVE_ITEM;

import org.eclipse.emf.ecore.EReference;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;

@ModelChange(category=REMOVE_ITEM)
public class DeleteGuideCommand extends ModelChangeBasicCommand {

	@Owner 	   private Ruler 	  ruler;
	@Reference private EReference reference = SchemaPackage.eINSTANCE.getRuler_Guides();
	@Item 	   private Guide 	  guide;
	
	private int i;
	
	public DeleteGuideCommand(Ruler ruler, Guide guide) {
		super("Delete Guide");
		this.ruler = ruler;
		this.guide = guide;
	}		
	
	@Override
	public void execute() {
		i = ruler.getGuides().indexOf(guide);
		redo();
	}
	
	@Override
	public void redo() {		
		ruler.getGuides().remove(guide);				
	}
	
	@Override
	public void undo() {		
		ruler.getGuides().add(i, guide);		
	}	
	
}
