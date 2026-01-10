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
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.SchemaPackage;
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

public class RecordGeneralPropertiesSection extends AbstractRecordPropertiesSection implements IAreaSpecificationProvider {
	private static final List<EAttribute> ATTRIBUTES = List.of(
			SchemaPackage.eINSTANCE.getSchemaRecord_Name(),
			SchemaPackage.eINSTANCE.getSchemaRecord_Id(),
			SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode(),
			SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode(),
			SchemaPackage.eINSTANCE.getSchemaArea_Name());

	private final IHyperlinkHandler<EAttribute, Command> areaHandler = new AreaHandler(this);
	private final IHyperlinkHandler<EAttribute, Command> locationModeHandler = new LocationModeHandler(this);
	private final EditRecordElementsHandler editRecordElementsHandler = new EditRecordElementsHandler(this);
	
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
		return ATTRIBUTES;
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
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name() || attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Id() ||
			attribute == SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode()) {
			
			return target;
		} else {
			return super.getEditableObject(attribute);
		}
	}
	
	@Override
	public IEditHandler getEditHandler(EAttribute attribute, Object newValue) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			return getRecordNameEditHandler(attribute, newValue);
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Id()) {
			return  getRecordIdEditHandler(attribute, newValue);
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode()) {
			return getStorageModeEditHandler(attribute, newValue);
		} else {
			return super.getEditHandler(attribute, newValue);
		}
	}
	
	private IEditHandler getRecordNameEditHandler(EAttribute attribute, Object newValue) {
		var newRecordName = newValue != null ? ((String) newValue).toUpperCase() : null;
		var errorMessage = Stream.of(
					checkAgainstNamingConventions(newRecordName),
					checkAgainstSchemaName(newRecordName),
					checkAgainstAreaNames(newRecordName),
					checkAgainstProcedureNames(newRecordName),
					checkAgainstRecordNames(newRecordName),
					checkAgainstSetNames(newRecordName))
				.flatMap(Function.identity())
				.findFirst();
		if (errorMessage.isEmpty()) {
			return super.getEditHandler(attribute, newRecordName);
		} else {				
			return new ErrorEditHandler(errorMessage.orElseThrow());
		}
	}
	
	private Stream<String> checkAgainstNamingConventions(String newRecordName) {
		var validationResult = NamingConventions.validate(newRecordName, NamingConventions.Type.RECORD_NAME);	
		if (validationResult.getStatus() == ValidationResult.Status.ERROR) {				
			return Stream.of(validationResult.getMessage());
		} else {
			return Stream.empty();
		}
	}
	
	private Stream<String> checkAgainstSchemaName(String newRecordName) {
		if (target.getSchema().getName().equalsIgnoreCase(newRecordName)) {
			return Stream.of("same as schema name");
		} else {
			return Stream.empty();
		}
	}
	
	private Stream<String> checkAgainstAreaNames(String newRecordName) {
		return target.getSchema().getAreas().stream()
				.filter(area -> area.getName().equalsIgnoreCase(newRecordName))
				.map(area -> "same as area '" + area.getName() + "'")
				.findFirst()
				.stream();
	}
	
	private Stream<String> checkAgainstProcedureNames(String newRecordName) {
		return target.getSchema().getProcedures().stream()
				.filter(procedure -> procedure.getName().equalsIgnoreCase(newRecordName))
				.map(procedure -> "same as procedure '" + procedure.getName() + "'")
				.findFirst()
				.stream();
	}
	
	private Stream<String> checkAgainstRecordNames(String newRecordName) {
		return target.getSchema().getRecords().stream()
				.filter(schemaRecord -> schemaRecord != target && schemaRecord.getName().equalsIgnoreCase(newRecordName))
				.map(schemaRecord -> "same as record '" + schemaRecord.getName() + "'")
				.findFirst()
				.stream();
	}
	
	private Stream<String> checkAgainstSetNames(String newRecordName) {
		return target.getSchema().getSets().stream()
				.filter(set -> set.getName().equalsIgnoreCase(newRecordName))
				.map(set -> "same as set '" + set.getName() + "'")
				.findFirst()
				.stream();
	}
	
	private IEditHandler getRecordIdEditHandler(EAttribute attribute, Object newValue) {
		// get the new record id
		var newRecordId = ((Short) newValue).shortValue();			
		// newRecordId must be an unsigned integer in the range 10 through 9999
		var validationResult = NamingConventions.validate(newRecordId, NamingConventions.Type.RECORD_ID);
		if (validationResult.getStatus() == ValidationResult.Status.ERROR) {
			return new ErrorEditHandler(validationResult.getMessage());
		}
		// record ids can be duplicated across areas in the schema, however, record ids must be unique for all
		// records within one area	
		var area = target.getAreaSpecification().getArea();
		for (var schemaRecord : area.getRecords()) {
			if (schemaRecord != target && schemaRecord.getId() == newRecordId) {
				return new ErrorEditHandler("must be unique for all records within one area");
			}
		}
		return super.getEditHandler(attribute, newRecordId);		
	}
	
	private IEditHandler getStorageModeEditHandler(EAttribute attribute, Object newValue) {
		var oldValue = target.getStorageMode();
		if ((oldValue == StorageMode.FIXED || oldValue == StorageMode.VARIABLE) &&
			(newValue == StorageMode.FIXED_COMPRESSED || newValue == StorageMode.VARIABLE_COMPRESSED) || (oldValue == StorageMode.FIXED_COMPRESSED || oldValue == StorageMode.VARIABLE_COMPRESSED) &&
			(newValue == StorageMode.FIXED || newValue == StorageMode.VARIABLE)) {
							
			return super.getEditHandler(attribute, newValue, "no modifications were made to the procedures called");				
		} else {
			return super.getEditHandler(attribute, newValue);
		}
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
			// remove the trailing underscore from the record name if we're dealing with a DDLCATLOD record
			return Tools.removeTrailingUnderscore(target.getName());
		} else {
			return super.getValue(attribute);
		}
	}

}
