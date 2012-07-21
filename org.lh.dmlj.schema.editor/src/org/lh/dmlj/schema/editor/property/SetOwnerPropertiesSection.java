package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

public class SetOwnerPropertiesSection extends AbstractSetPropertiesSection {

	public SetOwnerPropertiesSection() {
		super();	
	}	
	
	@Override
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			if (set.getSystemOwner() != null) {
				return "Specifies that the indexed set is owned by an " +
					   "internal owner record (SR7 system record)";
			} else {
				return "Identifies the record type that owns the set";
			}
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return "Specifies the area in which the (system) owner (record) " +
				   "and, in the case of an indexed set, the index structure " +
				   "is to reside";
		} else if (attribute == SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition()) {
			return "Represents the sequential position of the NEXT set " +
				   "pointer within the owner record's prefix";
		} else if (attribute == SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition()) {
			return "Represents the sequential position of the PRIOR set " +
				   "pointer within the owner record's prefix";
		}
		return super.getDescription(attribute);
	}
	
	@Override
	protected List<EAttribute> getAttributes() {		
		List<EAttribute> attributes = new ArrayList<>();
		attributes.add(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		attributes.add(SchemaPackage.eINSTANCE.getSchemaArea_Name());
		if (set.getSystemOwner() == null) {		
			attributes.add(SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition());
			if (set.getOwner().getPriorDbkeyPosition() != null) {
				attributes.add(SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition());
			}
		}		
		return attributes;
	}
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			return "Name";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return "Area";
		} else if (attribute == SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition()) {
			return "Next dbkey position";
		} else if (attribute == SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition()) {
			return "Prior dbkey position";
		}
		return super.getLabel(attribute);
	}
	
	@Override
	protected String getValue(EAttribute attribute) {		
		if (set.getSystemOwner() != null) {
			if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {			
				return "SYSTEM";
			} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
				return set.getSystemOwner()
						  .getAreaSpecification()
						  .getArea()
						  .getName();
			}			
		} else {			
			if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {											
				StringBuilder p = new StringBuilder(target.getSet()
														  .getOwner()
														  .getRecord()
														  .getName());
				if (p.charAt(p.length() - 1) == '_') {
					p.setLength(p.length() - 1);
				}
				return p.toString();							
			} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
				return set.getOwner()
						  .getRecord()
						  .getAreaSpecification()
						  .getArea()
						  .getName();
			} else if (attribute == SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition()) {
				return String.valueOf(set.getOwner().getNextDbkeyPosition());
			} else if (attribute == SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition()) {
				if (set.getOwner().getPriorDbkeyPosition() != null) {
					return set.getOwner().getPriorDbkeyPosition().toString();
				} else {
					return "";
				}
			}			
		}		
		
		return super.getValue(attribute);
	}

}