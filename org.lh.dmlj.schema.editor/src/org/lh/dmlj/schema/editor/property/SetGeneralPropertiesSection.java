package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.SchemaPackage;

public class SetGeneralPropertiesSection extends AbstractSetPropertiesSection {

	private static final EStructuralFeature[] FEATURES = 
		{SchemaPackage.eINSTANCE.getSet_Name(),
		 SchemaPackage.eINSTANCE.getSet_Mode(),
		 SchemaPackage.eINSTANCE.getSet_Order()};

	public SetGeneralPropertiesSection() {
		super();
	}	
	
	@Override
	protected String getDescription(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSet_Name()) {
			return "Identifies the database set description";
		} else if (feature == SchemaPackage.eINSTANCE.getSet_Mode()) {
			return "Specifies the characteristic of the set that tells CA " +
				   "IDMS/DB how pointers are to be maintained at runtime";
		} else if (feature == SchemaPackage.eINSTANCE.getSet_Order()) {
			return "Specifies the logical order of adding new member record " +
				   "occurrences to a set occurrence at runtime";
		}
		return super.getDescription(feature);
	}
	
	@Override
	protected List<EStructuralFeature> getFeatures() {		
		return Arrays.asList(FEATURES);
	}
	
	@Override
	protected String getLabel(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSet_Name()) {
			return "Name";
		} else if (feature == SchemaPackage.eINSTANCE.getSet_Mode()) {
			return "Mode";
		} else if (feature == SchemaPackage.eINSTANCE.getSet_Order()) {
			return "Order";
		}
		return super.getLabel(feature);
	}
	
	@Override
	protected String getValue(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSet_Name()) {			
			StringBuilder p = new StringBuilder(set.getName());
			if (p.charAt(p.length() - 1) == '_') {
				p.setLength(p.length() - 1);
			}
			return p.toString();
		} else if (feature == SchemaPackage.eINSTANCE.getSet_Mode()) {
			return set.getMode().toString();
		} else if (feature == SchemaPackage.eINSTANCE.getSet_Order()) {
			return set.getOrder().toString();
		}
		return super.getValue(feature);
	}

}