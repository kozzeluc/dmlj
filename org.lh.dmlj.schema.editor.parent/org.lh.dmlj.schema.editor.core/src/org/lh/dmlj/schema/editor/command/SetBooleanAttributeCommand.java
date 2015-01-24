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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;


public class SetBooleanAttributeCommand extends ModelChangeBasicCommand {
	
	private EObject 	 		   owner;
	private EStructuralFeature[] features;
	
	private boolean	oldValue;
	private boolean	newValue;			
		
	public SetBooleanAttributeCommand(EObject owner, EAttribute attribute, boolean newValue, 
									  String attributeLabel) {		
		
		super("Set '" + attributeLabel + "' to " + newValue);
		this.owner = owner;
		this.features = new EStructuralFeature[] {attribute};
		oldValue = (boolean) owner.eGet(attribute);
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
