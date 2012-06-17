package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.SchemaPackage;

public class RecordViaPropertiesSection 
	extends AbstractRecordPropertiesSection {	

	public RecordViaPropertiesSection() {
		super();
	}	
	
	@Override
	protected String getDescription(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getViaSpecification_SetName()) {
			return "Specifies that occurrences of the record are to be " +
				   "stored relative to their owner in a specific set";
		} else if (feature == SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName()) {
			return "Names a symbol used to represent the displacement";
		} else if (feature == SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount()) {
			return "Specifies how far away member records cluster from the owner " +
				   "record when the member and owner record occurrences are " +
				   "assigned to the same page range";
		}
		return super.getDescription(feature);
	}
	
	@Override
	protected List<EStructuralFeature> getFeatures() {
		List<EStructuralFeature> features = new ArrayList<>();
		features.add(SchemaPackage.eINSTANCE.getViaSpecification_SetName());
		if (target.getViaSpecification().getSymbolicDisplacementName() != null) {
			features.add(SchemaPackage.eINSTANCE
									  .getViaSpecification_SymbolicDisplacementName());
		}
		if (target.getViaSpecification().getDisplacementPageCount() != null) {
			features.add(SchemaPackage.eINSTANCE
								     .getViaSpecification_DisplacementPageCount());
		}
		return features;
	}
	
	@Override
	protected String getLabel(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getViaSpecification_SetName()) {
			return "Set";
		} else if (feature == SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName()) {
			return "Symbolic displ. name";
		} else if (feature == SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount()) {
			return "Displacement pages";
		}
		return super.getLabel(feature);
	}	
	
	@Override
	protected String getValue(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getViaSpecification_SetName()) {			
			StringBuilder p = 
				new StringBuilder(target.getViaSpecification().getSetName());
			if (p.charAt(p.length() - 1) == '_') {
				p.setLength(p.length() - 1);
			}
			return p.toString();
		} else if (feature == SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName()) {
			String symbolicDisplacementName = 
				target.getViaSpecification().getSymbolicDisplacementName();
			if (symbolicDisplacementName != null) {
				return symbolicDisplacementName;
			} else {
				return "";
			}
		} else if (feature == SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount()) {
			Short displacementPageCount = target.getViaSpecification().getDisplacementPageCount();
			if (displacementPageCount != null) {
				return String.valueOf(displacementPageCount.shortValue());
			} else {
				return "";
			}
		}
		return super.getValue(feature);
	}

}