package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;

public class SetMemberPropertiesSection extends AbstractSetPropertiesSection {

	public SetMemberPropertiesSection() {
		super();	
	}	
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			return target.getRecord();
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return target.getRecord().getAreaSpecification().getArea();
		} else {		
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	protected List<EAttribute> getAttributes() {		
		List<EAttribute> attributes = new ArrayList<>();
		attributes.add(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		attributes.add(SchemaPackage.eINSTANCE.getSchemaArea_Name());
		if (set.getMode() == SetMode.CHAINED) {
			// chained set
			attributes.add(SchemaPackage.eINSTANCE
									    .getMemberRole_NextDbkeyPosition());			
			attributes.add(SchemaPackage.eINSTANCE
										.getMemberRole_PriorDbkeyPosition());
		} else {
			// indexed set			
			attributes.add(SchemaPackage.eINSTANCE
									    .getMemberRole_IndexDbkeyPosition());
		}
		if (set.getSystemOwner() == null) {
			attributes.add(SchemaPackage.eINSTANCE
									    .getMemberRole_OwnerDbkeyPosition());
		}
		attributes.add(SchemaPackage.eINSTANCE.getMemberRole_MembershipOption());		
		return attributes;
	}
	
	@Override
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			return getPluginProperty("description.member.set.properties.record");
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return getPluginProperty("description.member.set.properties.area");
		} else {
			return super.getDescription(attribute);
		}
	}

	@Override
	protected EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE
									  .getMemberRole_MembershipOption()) {
			// in the case of system owned indexed sets without index pointers,
			// the membership option MUST be MANDATORY AUTOMATIC, so we will not
			// allow editing in that case
			if (set.getMode() == SetMode.INDEXED &&
				set.getSystemOwner() != null &&
				set.getMembers().get(0).getIndexDbkeyPosition() == null) {
				
				return super.getEditableObject(attribute);
			} else {
				return target;
			}
		} else {
			return super.getEditableObject(attribute);
		}
	}	
	
	@Override
	protected IEditHandler getEditHandler(EAttribute attribute, Object newValue) {
		if (attribute == SchemaPackage.eINSTANCE
				  					  .getMemberRole_MembershipOption()) {
						
			if (newValue == SetMembershipOption.MANDATORY_MANUAL ||
				newValue == SetMembershipOption.OPTIONAL_MANUAL &&
				target.getRecord().getLocationMode() == LocationMode.VIA &&
				target.getRecord().getViaSpecification().getSet() == set) {
				
				String message = 
					"specifying the MANUAL connect option for a set if the " +
					"member record is stored VIA a set is NOT recommended " +
					"since this option may result in the target page for the " +
					"member record being determined from a page that does " +
					"not hold the owner record";
				return super.getEditHandler(attribute, newValue, message);
			}
		}
		return super.getEditHandler(attribute, newValue);
	}
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {			
			return getPluginProperty("label.member.set.properties.area");	
		} else {
			return super.getLabel(attribute);
		}
	}
	
	@Override
	protected String getValue(EAttribute attribute) {		
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {			
			// remove the trailing underscore from the record name if we're 
			// dealing with a DDLCATLOD member record
			StringBuilder p = new StringBuilder(target.getRecord().getName());
			if (p.charAt(p.length() - 1) == '_') {
				p.setLength(p.length() - 1);
			}
			return p.toString();	
		}
		return super.getValue(attribute);
	}

}