package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SetMode;

public class SetMemberPropertiesSection extends AbstractSetPropertiesSection {

	public SetMemberPropertiesSection() {
		super();	
	}	
	
	@Override
	protected String getDescription(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getMemberRole_Record()) {
			return "Identifies a record type that is to participate as a " +
				   "member of the set";			
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return "Identifies the area in which occurrences of the member " +
				   "record type will be located";
		} else if (feature == SchemaPackage.eINSTANCE
				  						   .getMemberRole_NextDbkeyPosition()) {
			return "The sequential position of the next set pointer within " +
				   "the member record's prefix";
		} else if (feature == SchemaPackage.eINSTANCE
				  						   .getMemberRole_PriorDbkeyPosition()) {
			return "The sequential position of the prior set pointer within " +
				   "the member record's prefix";
		} else if (feature == SchemaPackage.eINSTANCE
				   						   .getMemberRole_IndexDbkeyPosition()) {
			return "The sequential position of the index set pointer within " +
				   "the member record's prefix";
		} else if (feature == SchemaPackage.eINSTANCE
				   						   .getMemberRole_OwnerDbkeyPosition()) {
			return "The relative position in the member record's prefix to " +
				   "be used for storing the database key of the owner record " +
				   "of the set";
		} else if (feature == SchemaPackage.eINSTANCE
				   						   .getMemberRole_MembershipOption()) {
			return "Specifies if occurrences of the member record type can " +
				   "be disconnected from the set other than through an ERASE " +
				   "function AND if they are connected implicitly to the set " +
				   "as part of the STORE function or only when the CONNECT " +
				   "function is issued";
		}
		return super.getDescription(feature);
	}
	
	@Override
	protected List<EStructuralFeature> getFeatures() {		
		List<EStructuralFeature> features = new ArrayList<>();
		features.add(SchemaPackage.eINSTANCE.getMemberRole_Record());
		features.add(SchemaPackage.eINSTANCE.getSchemaArea_Name());
		if (set.getMode() == SetMode.CHAINED) {
			// chained set
			features.add(SchemaPackage.eINSTANCE
									  .getMemberRole_NextDbkeyPosition());
			if (target.getPriorDbkeyPosition() != null) {
				features.add(SchemaPackage.eINSTANCE
										  .getMemberRole_PriorDbkeyPosition());
			}
		} else {
			// indexed set
			if (target.getIndexDbkeyPosition() != null) {
				features.add(SchemaPackage.eINSTANCE
										  .getMemberRole_IndexDbkeyPosition());
			}			
		}
		if (target.getOwnerDbkeyPosition() != null) {
			features.add(SchemaPackage.eINSTANCE
									  .getMemberRole_OwnerDbkeyPosition());
		}
		features.add(SchemaPackage.eINSTANCE.getMemberRole_MembershipOption());		
		return features;
	}
	
	@Override
	protected String getLabel(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getMemberRole_Record()) {
			return "Name";
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return "Area";
		} else if (feature == SchemaPackage.eINSTANCE
										   .getMemberRole_NextDbkeyPosition()) {
			return "Next dbkey position";
		} else if (feature == SchemaPackage.eINSTANCE
										   .getMemberRole_PriorDbkeyPosition()) {
			return "Prior dbkey position";
		} else if (feature == SchemaPackage.eINSTANCE
										   .getMemberRole_IndexDbkeyPosition()) {
			return "Index dbkey position";
		} else if (feature == SchemaPackage.eINSTANCE
										   .getMemberRole_OwnerDbkeyPosition()) {
			return "Owner dbkey position";
		} else if (feature == SchemaPackage.eINSTANCE
										   .getMemberRole_MembershipOption()) {
			return "Membership";
		}
		return super.getLabel(feature);
	}
	
	@Override
	protected String getValue(EStructuralFeature feature) {		
		if (feature == SchemaPackage.eINSTANCE.getMemberRole_Record()) {			
			StringBuilder p = new StringBuilder(target.getRecord().getName());
			if (p.charAt(p.length() - 1) == '_') {
				p.setLength(p.length() - 1);
			}
			return p.toString();	
		} else if (feature == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return target.getRecord()
						 .getAreaSpecification()
						 .getArea()
						 .getName();
		} else if (feature == SchemaPackage.eINSTANCE
										   .getMemberRole_MembershipOption()) {
			return target.getMembershipOption().toString().replaceAll("_", " ");
		}
		return super.getValue(feature);
	}

}