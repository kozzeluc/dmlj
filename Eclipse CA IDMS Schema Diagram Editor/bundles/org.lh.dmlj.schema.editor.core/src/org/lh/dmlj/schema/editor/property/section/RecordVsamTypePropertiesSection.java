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
package org.lh.dmlj.schema.editor.property.section;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.SchemaPackage;

public class RecordVsamTypePropertiesSection extends AbstractRecordPropertiesSection {

	private static final EAttribute[] ATTRIBUTES = 
		{SchemaPackage.eINSTANCE.getVsamType_LengthType(),
		 SchemaPackage.eINSTANCE.getVsamType_Spanned()};
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getVsamType_LengthType() ||
			attribute == SchemaPackage.eINSTANCE.getVsamType_Spanned()) {
			
			return target.getVsamType();
		} else {
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	public List<EAttribute> getAttributes() {
		return Arrays.asList(ATTRIBUTES);
	}

	@Override
	public String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getVsamType_LengthType()) {
			return "Specifies whether the record has a fixed or variable length";
		} else if (attribute == SchemaPackage.eINSTANCE.getVsamType_Spanned()) {
			return "Specifies whether occurrences of the record can span VSAM control intervals";
		} else {
			return super.getDescription(attribute);
		}
	}

	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getVsamType_LengthType() ||
			attribute == SchemaPackage.eINSTANCE.getVsamType_Spanned()) {			
			
			return target.getVsamType();
		} else {
			return super.getEditableObject(attribute);
		}
	}
	
	@Override
	public String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getVsamType_LengthType()) {
			return "Length";
		} else if (attribute == SchemaPackage.eINSTANCE.getVsamType_Spanned()) {
			return "Spanned";
		} else {
			return super.getLabel(attribute);
		}
	}

}
