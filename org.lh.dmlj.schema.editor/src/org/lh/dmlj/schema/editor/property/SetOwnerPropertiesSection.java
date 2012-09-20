package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.common.Tools;

public class SetOwnerPropertiesSection 
	extends AbstractSetPropertiesSection 
	implements IAreaSpecificationProvider, IMemberRoleProvider {

	private IHyperlinkHandler areaHandler = new AreaHandler(this);
	private IHyperlinkHandler chainedSetPointersHandler = 
		new ChainedSetPointersHandler(this);
	private IHyperlinkHandler indexedSetPointersHandler = 
		new IndexedSetPointersHandler(this);
	
	public SetOwnerPropertiesSection() {
		super();	
	}	
	
	@Override
	public AreaSpecification getAreaSpecification() {
		if (set.getSystemOwner() != null) {
			return set.getSystemOwner().getAreaSpecification();
		} else {
			return set.getOwner().getRecord().getAreaSpecification();
		}
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
	protected IHyperlinkHandler getHyperlinkHandler(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return areaHandler;
		} else if (set.getMode() == SetMode.CHAINED &&
				   (attribute == SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition() ||
					attribute == SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition())) {
			
			return chainedSetPointersHandler;
		} else if (set.getMode() == SetMode.INDEXED &&
				   (attribute == SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition() ||
					attribute == SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition())) {
			
			return indexedSetPointersHandler;
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}	
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return getPluginProperty("label.owner.set.properties.area");
		}
		return super.getLabel(attribute);
	}
	
	@Override
	public MemberRole getMemberRole() {
		return target;
	}

	@Override
	protected String getValue(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			if (set.getSystemOwner() != null) {
				return "SYSTEM";				
			} else {
				// remove the trailing underscore from the record name if we're 
				// dealing with a DDLCATLOD owner record
				return Tools.removeTrailingUnderscore(target.getSet()
						  									.getOwner()
						  									.getRecord()
						  									.getName());
			}			
		} else if (attribute == SchemaPackage.eINSTANCE
				 							 .getOwnerRole_PriorDbkeyPosition() &&
				   target.getSet().getOwner().getPriorDbkeyPosition() == null) {
			
			
			return "OMITTED";			
		} else {				
			return super.getValue(attribute);
		}
	}

}