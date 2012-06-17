package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.SchemaPackage;

public class RecordCalcPropertiesSection 
	extends AbstractRecordPropertiesSection {

	private static final EStructuralFeature[] FEATURES = 
		{SchemaPackage.eINSTANCE.getKey_Elements(),
		 SchemaPackage.eINSTANCE.getKey_DuplicatesOption()};

	public RecordCalcPropertiesSection() {
		super();
	}	
	
	@Override
	protected String getDescription(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getKey_Elements()) {
			return "Specifies the record element(s) whose value(s) will be " +
				   "used to calculate the page to store an occurrence of the " +
				   "record";
		} else if (feature == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return "Specifies whether occurrences of a record type with " +
				   "duplicate CALC key values are allowed and, if allowed, " +
				   "how they are logically positioned relative to the " +
				   "duplicate record already stored";
		}
		return super.getDescription(feature);
	}
	
	@Override
	protected List<EStructuralFeature> getFeatures() {		
		return Arrays.asList(FEATURES);
	}
	
	@Override
	protected String getLabel(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getKey_Elements()) {
			return "Key element(s)";
		} else if (feature == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return "Duplicates";
		}
		return super.getLabel(feature);
	}	
	
	@Override
	protected String getValue(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getKey_Elements()) {			
			List<KeyElement> keyElements = target.getCalcKey().getElements();			
			StringBuilder p = new StringBuilder();
			for (KeyElement keyElement : keyElements) {
				if (p.length() > 0) {
					p.append(", ");
				}
				p.append(keyElement.getElement().getName());
			}
			return p.toString();
		} else if (feature == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return target.getCalcKey()
						 .getDuplicatesOption()
						 .toString()
						 .replaceAll("_", " ");
		}
		return super.getValue(feature);
	}

}