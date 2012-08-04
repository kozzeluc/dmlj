package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;

public class RecordGeneralPropertiesSection 
	extends AbstractRecordPropertiesSection {

	private static final EAttribute[] ATTRIBUTES = 
		{SchemaPackage.eINSTANCE.getSchemaRecord_Name(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_Id(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode(),
		 SchemaPackage.eINSTANCE.getSchemaArea_Name()};

	public RecordGeneralPropertiesSection() {
		super();
	}	
	
	@Override
	protected List<EAttribute> getAttributes() {		
		return Arrays.asList(ATTRIBUTES);
	}

	@Override
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return getPluginProperty("description.general.record.properties.area");
		} else {
			return super.getDescription(attribute);
		}
	}
	
	@Override
	protected EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name() ||
			attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Id()) {
			
			return target;
		} else {
			return super.getEditableObject(attribute);
		}
	}
	
	@Override
	protected IEditHandler getEditHandler(EAttribute attribute, 
										  Object newValue) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			// get the new record name
			String newRecordName = (String) newValue;
			// newRecordName must be a 1- to 16-character name. The first 
			// character must be A through Z (alphabetic), #, $, or @ 
			// (international symbols). The remaining characters can be 
			// alphabetic or international symbols, 0 through 9, or the hyphen 
			// (except as the last character or following another hyphen).
			// newRecordName must not be the same as the schema name or the name 
			// of any other component (including synonyms) within the schema.
			if (newRecordName == null || newRecordName.length() < 1 || 
				newRecordName.length() > 16) {
					
				String message = "must be a 1- to 16-character value";
				return new ErrorEditHandler(message);
			}
			String firstChar = newRecordName.substring(0, 1).toLowerCase();
			if ("abcdefghijklmnopqrstuvwxyz#$@".indexOf(firstChar) == -1) {
				String message = "first character must be A through Z " +
								 "(alphabetic), #, $, or @";
				return new ErrorEditHandler(message);
			}
			for (int i = 1; i < newRecordName.length(); i++) {
				String remainingChar = 
					newRecordName.substring(i, i + 1).toLowerCase();
				if ("abcdefghijklmnopqrstuvwxyz0123456789#$@-".indexOf(remainingChar) == -1) {
					String message = 
						"remaining characters can only be alphabetic or " +
						"international symbols (#, $, or @), 0 through 9, or " +
						"the hyphen";
					return new ErrorEditHandler(message);
				}
			}
			if (newRecordName.endsWith("-") || 
				newRecordName.indexOf("--") > -1) {
				
				String message = "the hyphen can not be the last character " +
								 "or follow another hyphen";
				return new ErrorEditHandler(message);
			}
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
			// convert the new schema name to uppercase before passing it to the 
			// set attribute command
			return super.getEditHandler(attribute, newRecordName.toUpperCase());
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Id()) {			
			// get the new record id
			short newRecordId = ((Short) newValue).shortValue();			
			// newRecordId must be an unsigned integer in the range 10 through 
			// 9999. Record IDs can be duplicated across areas in the schema, 
			// however, record IDs must be unique for all records within one 
			// area					
			if (newRecordId < 10 || newRecordId > 9999) {
				String message = 
					"must be an unsigned integer in the range 10 through 9999";	
				return new ErrorEditHandler(message);
			}
			SchemaArea area = target.getAreaSpecification().getArea();
			for (SchemaRecord record : area.getRecords()) {
				if (record != target && record.getId() == newRecordId) {
					String message = 
						"must be unique for all records within one area";
					return new ErrorEditHandler(message);
				}
			}
			return super.getEditHandler(attribute, newRecordId);			
		} else {
			return super.getEditHandler(attribute, newValue);
		}
	}
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {			
			return getPluginProperty("label.general.record.properties.area");
		} else {
			return super.getLabel(attribute);
		}
	}
	
	@Override
	protected String getValue(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			StringBuilder p = 
				new StringBuilder(target.getName());
			if (p.charAt(p.length() - 1) == '_') {
				p.setLength(p.length() - 1);
			}
			return p.toString();
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return target.getAreaSpecification().getArea().getName();
		} else {
			return super.getValue(attribute);
		}
	}

}