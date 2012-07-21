package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

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
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			return "Identifies the database record description";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Id()) {
			return "Assigns a number that uniquely identifies each schema " +
				   "record type";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode()) {
			return "Indicates if the record has a fixed or variable length " +
				   "and whether or not it is compressed";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode()) {
			return "Defines the technique that CA IDMS/DB will use to " +
				   "physically store occurrences of the record type";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return "Identifies the area in which occurrences of the record " +
				   "type will be located";
		}
		return super.getDescription(attribute);
	}
	
	@Override
	protected List<EAttribute> getAttributes() {		
		return Arrays.asList(ATTRIBUTES);
	}
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			return "Name";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Id()) {
			return "Record ID";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode()) {
			return "Storage mode";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode()) {
			return "Location mode";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return "Area";
		}
		return super.getLabel(attribute);
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