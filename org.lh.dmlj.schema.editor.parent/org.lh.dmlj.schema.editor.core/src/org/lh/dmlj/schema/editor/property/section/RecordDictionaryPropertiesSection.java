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
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.property.handler.ErrorEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;

public class RecordDictionaryPropertiesSection extends AbstractRecordPropertiesSection 
	 {

	private static final EAttribute[] ATTRIBUTES = 
		{SchemaPackage.eINSTANCE.getSchemaRecord_SynonymName(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_SynonymVersion(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_BaseName(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_BaseVersion()};
	
	public RecordDictionaryPropertiesSection() {
		super();
	}
	
	@Override
	public List<EAttribute> getAttributes() {		
		return Arrays.asList(ATTRIBUTES);
	}
	
	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_BaseName() ||
			attribute == SchemaPackage.eINSTANCE.getSchemaRecord_BaseVersion() ||
			attribute == SchemaPackage.eINSTANCE.getSchemaRecord_SynonymName() ||
			attribute == SchemaPackage.eINSTANCE.getSchemaRecord_SynonymVersion()) {
			
			return target;
		} else {
			return super.getEditableObject(attribute);
		}
	}
	
	@Override
	public IEditHandler getEditHandler(EAttribute attribute, Object newValue) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_BaseName()) {
			// get the new base (primary) record name
			String newBaseRecordName = newValue != null ? ((String) newValue).toUpperCase() : null;
			// validate the base (primary) record name
			ValidationResult validationResult = 
				NamingConventions.validate(newBaseRecordName, NamingConventions.Type.PRIMARY_RECORD_NAME);
			if (validationResult.getStatus() == ValidationResult.Status.ERROR) {				
				return new ErrorEditHandler(validationResult.getMessage());
			}
			// we have no clue of whether the primary record exists in any dictionary, so we perform
			// no further checks here; pass the new base (primary) record name to the set attribute 
			// command
			return super.getEditHandler(attribute, newBaseRecordName);
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_BaseVersion()) {			
			// get the new base (primary) record version
			short newBaseRecordVersion = newValue != null ? ((Short) newValue).shortValue() : null;
			// validate the base (primary) record version
			ValidationResult validationResult = 
				NamingConventions.validate(newBaseRecordVersion, 
										   NamingConventions.Type.VERSION_SPECIFICATION);
			if (validationResult.getStatus() == ValidationResult.Status.ERROR) {				
				return new ErrorEditHandler(validationResult.getMessage());
			}
			// we have no clue of whether the primary record exists in any dictionary, so we perform
			// no further checks here; pass the new base (primary) record version to the set 
			// attribute command
			return super.getEditHandler(attribute, newBaseRecordVersion);						
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_SynonymName()) {
			// get the new record synonym name
			String newRecordSynonymName = newValue != null ? ((String) newValue).toUpperCase() : null;
			// validate the record synonym name
			ValidationResult validationResult = 
				NamingConventions.validate(newRecordSynonymName, NamingConventions.Type.RECORD_NAME);
			if (validationResult.getStatus() == ValidationResult.Status.ERROR) {				
				return new ErrorEditHandler(validationResult.getMessage());
			}
			// we have no clue of whether the record synonym exists in any dictionary, so we perform
			// no further checks here (even not making sure the name is equal to the schema record 
			// name); pass the new record synonym name to the set attribute command
			return super.getEditHandler(attribute, newRecordSynonymName);		
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_SynonymVersion()) {
			// get the new record synonym version
			short newBaseRecordVersion = newValue != null ? ((Short) newValue).shortValue() : null;
			// validate the record synonym version
			ValidationResult validationResult = 
				NamingConventions.validate(newBaseRecordVersion, 
										   NamingConventions.Type.VERSION_SPECIFICATION);
			if (validationResult.getStatus() == ValidationResult.Status.ERROR) {				
				return new ErrorEditHandler(validationResult.getMessage());
			}
			// we have no clue of whether the record synonym exists in any dictionary, so we perform
			// no further checks here; pass the new record synonym version to the set attribute 
			// command
			return super.getEditHandler(attribute, newBaseRecordVersion);				
		}
		return super.getEditHandler(attribute, newValue);		
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
