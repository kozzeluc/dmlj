package org.lh.dmlj.schema.editor.property;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to edit an attribute in a SchemaRecord object.  Setting the new 
 * attribute value is done through the command stack.<br><br>
 * Subclasses must supply the attribute and an indicator if the attribute is 
 * offered read-only during construction and, if they want a description to
 * be shown, the getDescription method of this class' superclass.<br><br>
 * Valid edit part model object types for these kind of sections are 
 * ConnectionPart, SchemaRecord, ConnectionLabel and SystemOwner.  In the case 
 * of ConnectionPart and ConnectionLabel model objects, subclasses should 
 * override the isOwner() method to indicate if the set's owner or member record 
 * is to be used (in the case of system owned indexed sets, the member record 
 * will ALWAYS be used).
 */
public abstract class AbstractSchemaRecordAttributeSection 
	extends AbstractStructuralFeatureSection<SchemaRecord> {		

	public AbstractSchemaRecordAttributeSection(EAttribute attribute, 
										    boolean readOnly) {
		super(attribute, readOnly);		
	}

	@Override
	protected final SchemaRecord getModelObject(Object editPartModelObject) {
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
			
			// if we're in owner mode, get the owner record (which really should
			// be a user record), otherwise the member record, and return it...
			if (isOwner()) {
				Assert.isTrue(memberRole.getSet().getSystemOwner() == null, 
							  "set not user owned");
				return memberRole.getSet().getOwner().getRecord();
			} else {
				return memberRole.getRecord();
			}
			
		} else if (editPartModelObject instanceof SchemaRecord) {
			return (SchemaRecord) editPartModelObject;
		} else {
			
			// SystemOwner model object, ALWAYS return the member record...
			SystemOwner systemOwner = (SystemOwner) editPartModelObject;
			return systemOwner.getSet().getMembers().get(0).getRecord();
			
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