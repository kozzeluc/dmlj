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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;

@ModelChange(category=SET_FEATURES)
public class SetShortAttributeCommand extends ModelChangeBasicCommand {
	
	@Owner 	  private EObject      		   owner;
	@Features private EStructuralFeature[] features;
	
	private short oldValue;
	private short newValue;		

	public SetShortAttributeCommand(EObject owner, EAttribute attribute, short newValue, 
									String attributeLabel) {
		
		super("Set '" + attributeLabel + "' to '" + newValue + "'");
		this.owner = owner;
		this.features = new EStructuralFeature[] {attribute};
		oldValue = (short) owner.eGet(attribute);
		this.newValue = newValue;
	}
	
	@Override
	public void execute() {
		owner.eSet(features[0], newValue);
	}
		
	@Override
	public void undo() {
		owner.eSet(features[0], oldValue);
	}
}
