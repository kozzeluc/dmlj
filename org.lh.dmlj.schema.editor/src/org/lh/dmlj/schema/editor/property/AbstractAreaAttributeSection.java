package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to edit an attribute in a SchemaArea object.  Setting the new attribute 
 * value is done through the command stack.<br><br>
 * Subclasses must supply the attribute and an indicator if the attribute is 
 * offered read-only during construction and, if they want a description to
 * be shown, the getDescription method of this class' superclass.<br><br>
 * Valid edit part model object types for these kind of sections are 
 * ConnectionPart, SchemaRecord, ConnectionLabel and SystemOwner.  In the case 
 * of ConnectionPart, ConnectionLabel and SystemOwner edit part model objects, 
 * subclasses should override the isOwner() method to indicate if the set's 
 * (possibly system) owner- or member area is to be used.
 */
public abstract class AbstractAreaAttributeSection
    extends AbstractStructuralFeatureSection<SchemaArea> {	

	public AbstractAreaAttributeSection(EAttribute attribute, boolean readOnly) {
		super(attribute, readOnly);		
	}

	@Override
	protected final SchemaArea getModelObject(Object editPartModelObject) {	
		if (editPartModelObject instanceof ConnectionPart ||
			editPartModelObject instanceof ConnectionLabel) {
				
			// we're dealing with a set connection part or label, get the 
			// MemberRole...
			MemberRole memberRole;			
			if (editPartModelObject instanceof ConnectionPart) {
				memberRole = 
					((ConnectionPart) editPartModelObject).getMemberRole();
			} else {
				ConnectionLabel connectionLabel =
					(ConnectionLabel) editPartModelObject;
				memberRole = connectionLabel.getMemberRole();
			}
			
			// if we're in owner mode, get the owner area, otherwise the member 
			// area, and return it...
			if (isOwner()) {
				// we want the owner area
				if (memberRole.getSet().getSystemOwner() != null) {
					// system owned
					return memberRole.getSet()
									 .getSystemOwner()
									 .getAreaSpecification()
									 .getArea();
				} else {
					// user owned
					return memberRole.getSet()
									 .getOwner()
									 .getRecord()
									 .getAreaSpecification()
									 .getArea();
				}
			} else {
				// we want the member area
				return memberRole.getRecord().getAreaSpecification().getArea();
			}
			
		} else if (editPartModelObject instanceof  SchemaRecord) {
			
			// we have a SchemaRecord, return its area...
			SchemaRecord record = (SchemaRecord) editPartModelObject;					
			return record.getAreaSpecification().getArea();		
			
		} else {
			
			// SystemOwner model object
			SystemOwner systemOwner = (SystemOwner) editPartModelObject;
			if (isOwner()) {
				// we want the system owner area
				return systemOwner.getAreaSpecification().getArea();
			} else {
				// we want the member area
				return systemOwner.getSet()
								  .getMembers()
								  .get(0)
								  .getRecord()
								  .getAreaSpecification()
								  .getArea();
			}
			
		}
	}	
	
	@Override
	protected final Class<?>[] getValidEditPartModelObjectTypes() {
		return new Class<?>[] {ConnectionLabel.class,
							   ConnectionPart.class,
							   SchemaRecord.class,							   
							   SystemOwner.class};
	}
	
	protected boolean isOwner() {
		return true;
	}
	
}