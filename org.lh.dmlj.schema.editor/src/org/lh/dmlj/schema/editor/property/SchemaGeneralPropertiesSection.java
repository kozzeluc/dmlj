package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.SchemaPackage;

public class SchemaGeneralPropertiesSection 
	extends AbstractSchemaPropertiesSection {

	public SchemaGeneralPropertiesSection() {
		super();
	}
	
	@Override
	protected String getDescription(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSchema_Name()) {
			return "Identifies the schema";
		} else if (feature == SchemaPackage.eINSTANCE.getSchema_Version()) {
			return "Qualifies the schema with a version number, which " +
				   "distinguishes this schema from others that have the same " +
				   "name";
		} else if (feature == SchemaPackage.eINSTANCE.getSchema_Description()) {
			return "Optionally specifies a name that is more descriptive " +
				   "than the 8-character schema name required by CA IDMS/DB, " +
				   "but can be used to store any type of information";
		} else if (feature == SchemaPackage.eINSTANCE.getSchema_MemoDate()) {
			return "Specifies any date the user wishes to supply";
		}
		return super.getDescription(feature);
	}
	
	@Override
	protected List<EStructuralFeature> getFeatures() {
		List<EStructuralFeature> features = new ArrayList<>();
		features.add(SchemaPackage.eINSTANCE.getSchema_Name());
		features.add(SchemaPackage.eINSTANCE.getSchema_Version());
		features.add(SchemaPackage.eINSTANCE.getSchema_Description());
		features.add(SchemaPackage.eINSTANCE.getSchema_MemoDate());
		return features;
	}
	
	@Override
	protected String getLabel(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSchema_Name()) {
			return "Name";
		} else if (feature == SchemaPackage.eINSTANCE.getSchema_Version()) {
			return "Version";
		} else if (feature == SchemaPackage.eINSTANCE.getSchema_Description()) {
			return "Description";
		} else if (feature == SchemaPackage.eINSTANCE.getSchema_MemoDate()) {
			return "Memo date";
		}
		return super.getLabel(feature);
	}

}