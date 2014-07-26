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
package org.lh.dmlj.schema.editor.property.section;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.property.handler.ErrorEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;

public class AreaGeneralPropertiesSection extends AbstractAreaPropertiesSection {

	private static final EAttribute[] ATTRIBUTES = {SchemaPackage.eINSTANCE.getSchemaArea_Name()};
	
	public AreaGeneralPropertiesSection() {
		super();
	}
	
	@Override
	public List<EAttribute> getAttributes() {		
		return Arrays.asList(ATTRIBUTES);
	}
	
	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {			
			return target;
		} else {
			return super.getEditableObject(attribute);
		}
	}
	
	@Override
	public IEditHandler getEditHandler(EAttribute attribute, Object newValue) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			// get the new area name
			String newAreaName = newValue != null ? ((String) newValue).toUpperCase() : null;
			// validate the record name
			ValidationResult validationResult = 
				NamingConventions.validate(newAreaName, NamingConventions.Type.LOGICAL_AREA_NAME);			
			if (validationResult.getStatus() == ValidationResult.Status.ERROR) {				
				return new ErrorEditHandler(validationResult.getMessage());
			}
			// newAreaName must not be the same as the schema name or the name 
			// of any other component (including synonyms) within the schema.
			if (newAreaName.equalsIgnoreCase(target.getSchema().getName())) {
				String message = "same as schema name";
				return new ErrorEditHandler(message);
			}
			for (SchemaArea area : target.getSchema().getAreas()){
				if (area != target && area.getName().equalsIgnoreCase(newAreaName)) {
					String message = "same as area '" + area.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (Procedure procedure : target.getSchema().getProcedures()){
				if (procedure.getName().equalsIgnoreCase(newAreaName)) {
					String message = "same as procedure '" + procedure.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (SchemaRecord record : target.getSchema().getRecords()){
				if (record.getName().equalsIgnoreCase(newAreaName)) {					
					String message = "same as record '" + record.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (Set set : target.getSchema().getSets()){
				if (set.getName().equalsIgnoreCase(newAreaName)) {
					String message = "same as set '" + set.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			// pass the new area name to the set attribute command
			return super.getEditHandler(attribute, newAreaName);			
		}
		return super.getEditHandler(attribute, newValue);	
	}

}
