package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

public class SetGeneralPropertiesSection extends AbstractSetPropertiesSection {

	private static final EAttribute[] ATTRIBUTES = 
		{SchemaPackage.eINSTANCE.getSet_Name(),
		 SchemaPackage.eINSTANCE.getSet_Mode(),
		 SchemaPackage.eINSTANCE.getSet_Order()};

	public SetGeneralPropertiesSection() {
		super();
	}	
	
	@Override
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name()) {
			return "Identifies the database set description";
		} else if (attribute == SchemaPackage.eINSTANCE.getSet_Mode()) {
			return "Specifies the characteristic of the set that tells CA " +
				   "IDMS/DB how pointers are to be maintained at runtime";
		} else if (attribute == SchemaPackage.eINSTANCE.getSet_Order()) {
			return "Specifies the logical order of adding new member record " +
				   "occurrences to a set occurrence at runtime";
		}
		return super.getDescription(attribute);
	}
	
	@Override
	protected List<EAttribute> getAttributes() {		
		return Arrays.asList(ATTRIBUTES);
	}
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name()) {
			return "Name";
		} else if (attribute == SchemaPackage.eINSTANCE.getSet_Mode()) {
			return "Mode";
		} else if (attribute == SchemaPackage.eINSTANCE.getSet_Order()) {
			return "Order";
		}
		return super.getLabel(attribute);
	}
	
	@Override
	protected String getValue(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name()) {			
			StringBuilder p = new StringBuilder(set.getName());
			if (p.charAt(p.length() - 1) == '_') {
				p.setLength(p.length() - 1);
			}
			return p.toString();
		} else if (attribute == SchemaPackage.eINSTANCE.getSet_Mode()) {
			return set.getMode().toString();
		} else if (attribute == SchemaPackage.eINSTANCE.getSet_Order()) {
			return set.getOrder().toString();
		}
		return super.getValue(attribute);
	}

}