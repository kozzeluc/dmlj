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
public class SetObjectAttributeCommand extends ModelChangeBasicCommand {
	
	@Owner	  private EObject      		   owner;
	@Features private EStructuralFeature[] features;
	
	private Object oldValue;
	protected Object newValue;
	
	protected ISupplier<EObject> eObjectSupplier;
	
	private static String getLabel(EAttribute attribute, Object value, String attributeLabel) {		
		if (value != null) {
			if (attribute.getEType().getInstanceClass().isEnum()) {
				String sValue = value.toString();
				return "Set '" + attributeLabel + "' to '" + sValue.replaceAll("_", " ") + "'";
			} else {
				return "Set '" + attributeLabel + "' to '" + value + "'";
			}
		} else {
			return "Remove '" + attributeLabel + "'";
		}
	}
	
	public SetObjectAttributeCommand(EObject owner, EAttribute attribute, Object newValue, 
									 String attributeLabel) {
		
		super(getLabel(attribute, newValue, attributeLabel));
		this.owner = owner;
		this.features = new EStructuralFeature[] {attribute};
		this.newValue = newValue;		
	}
	
	public SetObjectAttributeCommand(ISupplier<EObject> eObjectSupplier, EAttribute attribute, 
									 Object newValue, String attributeLabel) {

		super(getLabel(attribute, newValue, attributeLabel));
		this.eObjectSupplier = eObjectSupplier;
		this.features = new EStructuralFeature[] {attribute};
		this.newValue = newValue;		
	}	
	
	@Override
	public void execute() {
		if (eObjectSupplier != null) {
			owner = eObjectSupplier.supply();
		}
		oldValue = owner.eGet(features[0]);
		owner.eSet(features[0], newValue);
	}	
			
	@Override
	public void undo() {
		owner.eSet(features[0], oldValue);
	}
	
	@Override
	public void redo() {
		owner.eSet(features[0], newValue);
	}
}
