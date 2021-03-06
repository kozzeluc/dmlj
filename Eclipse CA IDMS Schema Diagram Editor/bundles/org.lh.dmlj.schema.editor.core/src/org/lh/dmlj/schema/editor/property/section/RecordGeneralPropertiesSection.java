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
package org.lh.dmlj.schema.editor.property.section;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.property.IAreaSpecificationProvider;
import org.lh.dmlj.schema.editor.property.handler.AreaHandler;
import org.lh.dmlj.schema.editor.property.handler.EditRecordElementsHandler;
import org.lh.dmlj.schema.editor.property.handler.ErrorEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;
import org.lh.dmlj.schema.editor.property.handler.LocationModeHandler;

public class RecordGeneralPropertiesSection 
	extends AbstractRecordPropertiesSection 
	implements IAreaSpecificationProvider {

	private static final EAttribute[] ATTRIBUTES = 
		{SchemaPackage.eINSTANCE.getSchemaRecord_Name(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_Id(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode(),
		 SchemaPackage.eINSTANCE.getSchemaArea_Name()};

	private IHyperlinkHandler<EAttribute, Command> areaHandler = new AreaHandler(this);
	
	private IHyperlinkHandler<EAttribute, Command> locationModeHandler = 
		new LocationModeHandler(this);
	
	private EditRecordElementsHandler editRecordElementsHandler = new EditRecordElementsHandler(this);

	public RecordGeneralPropertiesSection() {
		super();
	}	
	
	@Override
	public AreaSpecification getAreaSpecification() {
		return target.getAreaSpecification();
	}

	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return target.getAreaSpecification().getArea();
		} else {
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	public List<EAttribute> getAttributes() {		
		return Arrays.asList(ATTRIBUTES);
	}

	@Override
	protected String getBottomHyperlinkText() {
		return "Edit record element(s)";
	}

	@Override
	public String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return getPluginProperty("description.general.record.properties.area");
		} else {
			return super.getDescription(attribute);
		}
	}
	
	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name() ||
			attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Id() ||
			attribute == SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode()) {
			
			return target;
		} else {
			return super.getEditableObject(attribute);
		}
	}
	
	@Override
	public IEditHandler getEditHandler(EAttribute attribute, Object newValue) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			// get the new record name
			String newRecordName = 
				newValue != null ? ((String) newValue).toUpperCase() : null;
			// validate the record name
			ValidationResult validationResult = 
				NamingConventions.validate(newRecordName, 
										   NamingConventions.Type.RECORD_NAME);			
			if (validationResult.getStatus() == ValidationResult.Status.ERROR) {				
				return new ErrorEditHandler(validationResult.getMessage());
			}
			// newRecordName must not be the same as the schema name or the name 
			// of any other component (including synonyms) within the schema.
			if (newRecordName.equalsIgnoreCase(target.getSchema().getName())) {
				String message = "same as schema name";
				return new ErrorEditHandler(message);
			}
			for (SchemaArea area : target.getSchema().getAreas()){
				if (area.getName().equalsIgnoreCase(newRecordName)) {
					String message = "same as area '" + area.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (Procedure procedure : target.getSchema().getProcedures()){
				if (procedure.getName().equalsIgnoreCase(newRecordName)) {
					String message = 
						"same as procedure '" + procedure.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (SchemaRecord record : target.getSchema().getRecords()){
				if (record != target &&
					record.getName().equalsIgnoreCase(newRecordName)) {
					
					String message = 
						"same as record '" + record.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (Set set : target.getSchema().getSets()){
				if (set.getName().equalsIgnoreCase(newRecordName)) {
					String message = "same as set '" + set.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			// pass the new record name to the set attribute command
			return super.getEditHandler(attribute, newRecordName);
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Id()) {			
			// get the new record id
			short newRecordId = ((Short) newValue).shortValue();			
			// newRecordId must be an unsigned integer in the range 10 through 
			// 9999.				
			ValidationResult validationResult = 
				NamingConventions.validate(newRecordId, 
										   NamingConventions.Type.RECORD_ID);			
			if (validationResult.getStatus() == ValidationResult.Status.ERROR) {				
				return new ErrorEditHandler(validationResult.getMessage());
			}
			// Record IDs can be duplicated across areas in the schema, however, 
			// record IDs must be unique for all records within one area	
			SchemaArea area = target.getAreaSpecification().getArea();
			for (SchemaRecord record : area.getRecords()) {
				if (record != target && record.getId() == newRecordId) {
					String message = 
						"must be unique for all records within one area";
					return new ErrorEditHandler(message);
				}
			}
			return super.getEditHandler(attribute, newRecordId);			
		} else if (attribute == SchemaPackage.eINSTANCE
											 .getSchemaRecord_StorageMode()) {
			
			StorageMode oldValue = target.getStorageMode();
			if ((oldValue == StorageMode.FIXED || 
				 oldValue == StorageMode.VARIABLE) &&
				(newValue == StorageMode.FIXED_COMPRESSED ||
				 newValue == StorageMode.VARIABLE_COMPRESSED) ||
				(oldValue == StorageMode.FIXED_COMPRESSED || 
				 oldValue == StorageMode.VARIABLE_COMPRESSED) &&
				(newValue == StorageMode.FIXED ||
				 newValue == StorageMode.VARIABLE)) {
				
				String message = 
					"no modifications were made to the procedures called";
				return super.getEditHandler(attribute, newValue, message);				
			}
		}
		return super.getEditHandler(attribute, newValue);		
	}
	
	@Override
	public IHyperlinkHandler<EAttribute, Command> getHyperlinkHandler(EAttribute attribute) {
		if (attribute == null) {
			return editRecordElementsHandler;
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode()) {
			return locationModeHandler;
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return areaHandler;
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}	
	
	@Override
	public String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {			
			return getPluginProperty("label.general.record.properties.area");
		} else {
			return super.getLabel(attribute);
		}
	}

	@Override
	protected String getValue(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			// remove the trailing underscore from the record name if we're 
			// dealing with a DDLCATLOD record
			return Tools.removeTrailingUnderscore(target.getName());			
		} else {
			return super.getValue(attribute);
		}
	}

}
