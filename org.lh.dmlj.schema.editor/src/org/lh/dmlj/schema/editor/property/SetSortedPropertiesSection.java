package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

public class SetSortedPropertiesSection extends AbstractSetPropertiesSection {

	private static final EAttribute[] ATTRIBUTES = 
		{SchemaPackage.eINSTANCE.getKey_ElementSummary(),
		 SchemaPackage.eINSTANCE.getKey_DuplicatesOption()};
	
	public SetSortedPropertiesSection() {
		super();	
	}	
	
	@Override
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary()) {
			return "Identifies the member record element(s) on whose values " +
				   "the set is to be sorted (that is, the sort control " +
				   "element)";			
		} else if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return "Specifies how CA IDMS/DB handles a record occurrence " +
				   "whose sort key duplicates an existing occurrence's sort " +
				   "key";
		}
		return super.getDescription(attribute);
	}
	
	@Override
	protected List<EAttribute> getAttributes() {		
		return Arrays.asList(ATTRIBUTES);
	}
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary()) {
			return "Key element(s)";
		} else if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return "Duplicates";
		} return super.getLabel(attribute);
	}
	
	@Override
	protected String getValue(EAttribute attribute) {		
		
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary()) {
			return target.getSortKey().getElementSummary();
		} else if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return target.getSortKey()
						 .getDuplicatesOption()
						 .toString()
						 .replaceAll("_", " ");
		}
		
		return super.getValue(attribute);
	}

}