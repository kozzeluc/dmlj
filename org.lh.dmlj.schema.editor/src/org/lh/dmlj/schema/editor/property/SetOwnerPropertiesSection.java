package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.SchemaPackage;

public class SetOwnerPropertiesSection extends AbstractSetPropertiesSection {

	public SetOwnerPropertiesSection() {
		super();	
	}	
	
	@Override
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			String key;
			if (set.getSystemOwner() != null) {
				key = "description.owner.set.properties.system.owner";
			} else {
				key = "description.owner.set.properties.record";
			}
			return getPluginProperty(key);
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {			
			return getPluginProperty("description.owner.set.properties.area");
		} else {
			return super.getDescription(attribute);
		}
	}
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			// we need to override the getValue(EAttribute) method for this 
			// attribute as well, in order to deal with system owned sets
			return target.getSet().getOwner().getRecord();
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			if (set.getSystemOwner() == null) {
				// user owned set
				return set.getOwner()
						  .getRecord()
						  .getAreaSpecification()
						  .getArea();
			} else {
				// system owned indexed set
				return set.getSystemOwner()
						  .getAreaSpecification()
						  .getArea();
			}
		} else if (attribute == SchemaPackage.eINSTANCE
											 .getOwnerRole_NextDbkeyPosition() ||
				   attribute == SchemaPackage.eINSTANCE
				   							 .getOwnerRole_PriorDbkeyPosition()) {
			
			return set.getOwner();
		} else {		
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	protected List<EAttribute> getAttributes() {		
		List<EAttribute> attributes = new ArrayList<>();
		attributes.add(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		attributes.add(SchemaPackage.eINSTANCE.getSchemaArea_Name());
		if (set.getSystemOwner() == null) {		
			attributes.add(SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition());			
			attributes.add(SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition());			
		}		
		return attributes;
	}
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return getPluginProperty("label.owner.set.properties.area");
		}
		return super.getLabel(attribute);
	}
	
	@Override
	protected String getValue(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			if (set.getSystemOwner() != null) {
				return "SYSTEM";				
			} else {
				// remove the trailing underscore from the record name if we're 
				// dealing with a DDLCATLOD owner record
				StringBuilder p = new StringBuilder(target.getSet()
														  .getOwner()
														  .getRecord()
														  .getName());
				if (p.charAt(p.length() - 1) == '_') {
					p.setLength(p.length() - 1);
				}
				return p.toString();			
			}			
		} else {				
			return super.getValue(attribute);
		}
	}

}