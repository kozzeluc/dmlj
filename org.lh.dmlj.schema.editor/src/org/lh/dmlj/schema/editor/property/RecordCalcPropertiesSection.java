package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

public class RecordCalcPropertiesSection 
	extends AbstractRecordPropertiesSection {

	private static final EAttribute[] ATTRIBUTES = 
		{SchemaPackage.eINSTANCE.getKey_ElementSummary(),
		 SchemaPackage.eINSTANCE.getKey_DuplicatesOption()};

	public RecordCalcPropertiesSection() {
		super();
	}	
	
	@Override
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary()) {
			return "Specifies the record element(s) whose value(s) will be " +
				   "used to calculate the page to store an occurrence of the " +
				   "record";
		} else if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return "Specifies whether occurrences of a record type with " +
				   "duplicate CALC key values are allowed and, if allowed, " +
				   "how they are logically positioned relative to the " +
				   "duplicate record already stored";
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
		}
		return super.getLabel(attribute);
	}	
	
	@Override
	protected String getValue(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary()) {			
			return target.getCalcKey().getElementSummary();
		} else if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return target.getCalcKey()
						 .getDuplicatesOption()
						 .toString()
						 .replaceAll("_", " ");
		}
		return super.getValue(attribute);
	}

}