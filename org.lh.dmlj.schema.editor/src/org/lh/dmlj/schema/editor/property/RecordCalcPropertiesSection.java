package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
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
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary() ||
			attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			
			return target.getCalcKey();
		} else {
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	protected List<EAttribute> getAttributes() {		
		return Arrays.asList(ATTRIBUTES);
	}
	
	@Override
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary()) {
			return getPluginProperty("description.calc.record.properties.elementSummary");
		} else if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return getPluginProperty("description.calc.record.properties.duplicatesOption");
		} else {
			return super.getDescription(attribute);
		}
	}	

	@Override
	protected EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return target.getCalcKey();
		} else {
			return super.getEditableObject(attribute);
		}		
	}
	
}