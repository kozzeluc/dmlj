package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.SchemaPackage;

public class SetSortedPropertiesSection extends AbstractSetPropertiesSection {

	private static final EStructuralFeature[] FEATURES = 
		{SchemaPackage.eINSTANCE.getKey_Elements(),
		 SchemaPackage.eINSTANCE.getKey_DuplicatesOption()};
	
	public SetSortedPropertiesSection() {
		super();	
	}	
	
	@Override
	protected String getDescription(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getKey_Elements()) {
			return "Identifies the member record element(s) on whose values " +
				   "the set is to be sorted (that is, the sort control " +
				   "element)";			
		} else if (feature == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return "Specifies how CA IDMS/DB handles a record occurrence " +
				   "whose sort key duplicates an existing occurrence's sort " +
				   "key";
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
		} return super.getLabel(feature);
	}
	
	@Override
	protected String getValue(EStructuralFeature feature) {		
		
		if (feature == SchemaPackage.eINSTANCE.getKey_Elements()) {
			List<KeyElement> keyElements = target.getSortKey().getElements();			
			if (keyElements.size() == 1 && keyElements.get(0).isDbkey()) {
				return "DBKEY";
			} else {
				StringBuilder p = new StringBuilder();
				for (KeyElement keyElement : keyElements) {
					if (p.length() > 0) {
						p.append(", ");
					}
					p.append(keyElement.getElement().getName());
					p.append(" ");
					p.append(keyElement.getSortSequence().toString());
				}
				return p.toString();
			}
		} else if (feature == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return target.getSortKey()
						 .getDuplicatesOption()
						 .toString()
						 .replaceAll("_", " ");
		}
		
		return super.getValue(feature);
	}

}