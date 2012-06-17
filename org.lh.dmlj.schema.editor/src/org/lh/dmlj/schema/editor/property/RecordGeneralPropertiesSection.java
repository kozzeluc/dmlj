package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.SchemaPackage;

public class RecordGeneralPropertiesSection 
	extends AbstractRecordPropertiesSection {

	private static final EStructuralFeature[] FEATURES = 
		{SchemaPackage.eINSTANCE.getSchemaRecord_Name(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_Id(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode(),
		 SchemaPackage.eINSTANCE.getSchemaRecord_AreaSpecification()};

	public RecordGeneralPropertiesSection() {
		super();
	}	
	
	@Override
	protected String getDescription(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			return "Identifies the database record description";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_Id()) {
			return "Assigns a number that uniquely identifies each schema " +
				   "record type";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode()) {
			return "Indicates if the record has a fixed or variable length " +
				   "and whether or not it is compressed";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode()) {
			return "Defines the technique that CA IDMS/DB will use to " +
				   "physically store occurrences of the record type";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_AreaSpecification()) {
			return "Identifies the area in which occurrences of the record " +
				   "type will be located";
		}
		return super.getDescription(feature);
	}
	
	@Override
	protected List<EStructuralFeature> getFeatures() {		
		return Arrays.asList(FEATURES);
	}
	
	@Override
	protected String getLabel(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			return "Name";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_Id()) {
			return "Record ID";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_StorageMode()) {
			return "Storage mode";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode()) {
			return "Location mode";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_AreaSpecification()) {
			return "Area";
		}
		return super.getLabel(feature);
	}
	
	@Override
	protected String getValue(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			StringBuilder p = 
				new StringBuilder(target.getName());
			if (p.charAt(p.length() - 1) == '_') {
				p.setLength(p.length() - 1);
			}
			return p.toString();
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_AreaSpecification()) {
			return target.getAreaSpecification().getArea().getName();
		} else {
			return super.getValue(feature);
		}
	}

}