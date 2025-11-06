/**
 * Copyright (C) 2025  Luc Hermans
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

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.property.handler.EditRecordElementsHandler;
import org.lh.dmlj.schema.editor.property.handler.ErrorEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;

public class RecordLengthPropertiesSection extends AbstractRecordPropertiesSection {
	private static final List<EAttribute> ATTRIBUTES = List.of(
			SchemaPackage.eINSTANCE.getSchemaRecord_PrefixLength(),
			SchemaPackage.eINSTANCE.getSchemaRecord_DataLength(),
			SchemaPackage.eINSTANCE.getSchemaRecord_ControlLength(),
			SchemaPackage.eINSTANCE.getSchemaRecord_MinimumRootLength(),		
			SchemaPackage.eINSTANCE.getSchemaRecord_MinimumFragmentLength());
	
	private EditRecordElementsHandler editRecordElementsHandler = new EditRecordElementsHandler(this);
	
	@Override
	public List<EAttribute> getAttributes() {		
		return ATTRIBUTES;		
	}
	
	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumRootLength() ||
			attribute == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumFragmentLength()) {
			
			return target;
		} else {
			return super.getEditableObject(attribute);
		}
	}
	
	@Override
	public IEditHandler getEditHandler(EAttribute attribute, Object newValue) {		
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumRootLength()) {
			return getMinimumRootLengthEditHandler(attribute, newValue);
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumFragmentLength()) {
			return getMinimumFragmentLengthEditHandler(attribute, newValue);
		} else {
			return super.getEditHandler(attribute, newValue);
		}
	}
	
	private IEditHandler getMinimumRootLengthEditHandler(EAttribute attribute, Object newValue) {
		var newMinimumRootLength = (Short) newValue;
		// newMinimumRootLength must include all CALC, index, and sort control elements. It must be an unsigned
		// integer; if it is not a multiple of 4, we will make it so by rounding up - we treat null and 0 the
		// same here
		if (newMinimumRootLength != null && newMinimumRootLength.shortValue() > 0) {
			// round up the new value if not a multiple of 4
			var i = newMinimumRootLength; 
			while (i % 4 > 0) {
				i++;
			}
			newMinimumRootLength = i;
			// compute the minimum value; the control length counts an extra 4 bytes if the record is fragmented,
			// so we have to take that into account
			var minimumValue = !target.isFragmented() ? target.getControlLength() : (short) (target.getControlLength() - 4);
			// perform the minimum value check
			if (newMinimumRootLength < minimumValue) {
				return new ErrorEditHandler("must include all CALC, index, and sort control elements");
			} else {
				return super.getEditHandler(attribute, newMinimumRootLength);
			}
		} else if (newMinimumRootLength != null && newMinimumRootLength.shortValue() < 0) {
			return new ErrorEditHandler("must be an unsigned integer");				
		} else {
			return super.getEditHandler(attribute, null);
		}			
	}
	
	private IEditHandler getMinimumFragmentLengthEditHandler(EAttribute attribute, Object newValue) {
		var newMinimumFragmentLength = (Short) newValue;
		// newMinimumFragmentLength must be an unsigned integer; if it is not a multiple of 4, we will make it so
		// by rounding up - we treat null and 0 the same here
		if (newMinimumFragmentLength != null && newMinimumFragmentLength.shortValue() > 0) {
			// round up the new value if not a multiple of 4
			var i = newMinimumFragmentLength; 
			while (i % 4 > 0) {
				i++;
			}
			newMinimumFragmentLength = i;
			// the value entered is valid
			return super.getEditHandler(attribute, newMinimumFragmentLength);
		} else if (newMinimumFragmentLength != null && newMinimumFragmentLength.shortValue() < 0) {
			return new ErrorEditHandler("must be an unsigned integer");
		} else {
			return super.getEditHandler(attribute, null);
		}
	}
	
	@Override
	public IHyperlinkHandler<EAttribute, Command> getHyperlinkHandler(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_DataLength() ||
			attribute == SchemaPackage.eINSTANCE.getSchemaRecord_ControlLength()) {
			
			return editRecordElementsHandler;
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}

}
