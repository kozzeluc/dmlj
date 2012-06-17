package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.SchemaPackage;

public class RecordLengthPropertiesSection 
	extends AbstractRecordPropertiesSection {

	public RecordLengthPropertiesSection() {
		super();
	}	
	
	@Override
	protected String getDescription(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_PrefixLength()) {
			return "The length, in bytes, of the prefix portion of the " +
				   "record (including the pointers for the CALC set in the " +
				   "case of a CALC record)";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_DataLength()) {
			return "The length, in bytes, of the data portion of the record";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_ControlLength()) {
			return "The length of the initial portion of the record that " +
				   "includes all bytes up to and including the last CALC, " +
				   "index, or sort control element";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumRootLength()) {
			return "Specifies the minimum portion of a variable-length " +
				   "record that can be stored on a database page";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumFragmentLength()) {
			return "Specifies the minimum length of subsequent segments " +
					   "(fragments) of a variable-length record";
		}
		return super.getDescription(feature);
	}
	
	@Override
	protected List<EStructuralFeature> getFeatures() {		
		List<EStructuralFeature> features = new ArrayList<>();		
		features.add(SchemaPackage.eINSTANCE.getSchemaRecord_PrefixLength());
		features.add(SchemaPackage.eINSTANCE.getSchemaRecord_DataLength());
		features.add(SchemaPackage.eINSTANCE.getSchemaRecord_ControlLength());
		if (target.getMinimumRootLength() != null) {
			features.add(SchemaPackage.eINSTANCE
									  .getSchemaRecord_MinimumRootLength());
		}
		if (target.getMinimumFragmentLength() != null) {
			features.add(SchemaPackage.eINSTANCE
									  .getSchemaRecord_MinimumFragmentLength());
		}		
		return features;
	}
	
	@Override
	protected String getLabel(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_PrefixLength()) {
			return "Prefix";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_DataLength()) {
			return "Data";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_ControlLength()) {
			return "Control length";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumRootLength()) {
			return "Min. root length";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumFragmentLength()) {
			return "Min. fragment length";
		}
		return super.getLabel(feature);
	}	

}