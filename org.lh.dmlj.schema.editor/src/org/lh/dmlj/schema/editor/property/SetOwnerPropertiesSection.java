package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.SchemaPackage;

public class SetOwnerPropertiesSection extends AbstractSetPropertiesSection {

	public SetOwnerPropertiesSection() {
		super();	
	}	
	
	@Override
	protected String getDescription(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSet_Owner()) {
			if (set.getSystemOwner() != null) {
				return "Specifies that the indexed set is owned by an " +
					   "internal owner record (SR7 system record)";
			} else {
				return "Identifies the record type that owns the set";
			}
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return "Specifies the area in which the (system) owner (record) " +
				   "and, in the case of an indexed set, the index structure " +
				   "is to reside";
		} else if (feature == SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition()) {
			return "Represents the sequential position of the NEXT set " +
				   "pointer within the owner record's prefix";
		} else if (feature == SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition()) {
			return "Represents the sequential position of the PRIOR set " +
				   "pointer within the owner record's prefix";
		}
		return super.getDescription(feature);
	}
	
	@Override
	protected List<EStructuralFeature> getFeatures() {		
		List<EStructuralFeature> features = new ArrayList<>();
		features.add(SchemaPackage.eINSTANCE.getSet_Owner());
		features.add(SchemaPackage.eINSTANCE.getSchemaArea_Name());
		if (set.getSystemOwner() == null) {		
			features.add(SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition());
			if (set.getOwner().getPriorDbkeyPosition() != null) {
				features.add(SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition());
			}
		}		
		return features;
	}
	
	@Override
	protected String getLabel(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getSet_Owner()) {
			return "Name";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return "Area";
		} else if (feature == SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition()) {
			return "Next dbkey position";
		} else if (feature == SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition()) {
			return "Prior dbkey position";
		}
		return super.getLabel(feature);
	}
	
	@Override
	protected String getValue(EStructuralFeature feature) {		
		if (set.getSystemOwner() != null) {
			if (feature == SchemaPackage.eINSTANCE.getSet_Owner()) {			
				return "SYSTEM";
			} else if (feature == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
				return set.getSystemOwner()
						  .getAreaSpecification()
						  .getArea()
						  .getName();
			}			
		} else {			
			if (feature == SchemaPackage.eINSTANCE.getSet_Owner()) {											
				StringBuilder p = new StringBuilder(target.getSet()
														  .getOwner()
														  .getRecord()
														  .getName());
				if (p.charAt(p.length() - 1) == '_') {
					p.setLength(p.length() - 1);
				}
				return p.toString();							
			} else if (feature == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
				return set.getOwner()
						  .getRecord()
						  .getAreaSpecification()
						  .getArea()
						  .getName();
			} else if (feature == SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition()) {
				return String.valueOf(set.getOwner().getNextDbkeyPosition());
			} else if (feature == SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition()) {
				if (set.getOwner().getPriorDbkeyPosition() != null) {
					return set.getOwner().getPriorDbkeyPosition().toString();
				} else {
					return "";
				}
			}			
		}		
		
		return super.getValue(feature);
	}

}