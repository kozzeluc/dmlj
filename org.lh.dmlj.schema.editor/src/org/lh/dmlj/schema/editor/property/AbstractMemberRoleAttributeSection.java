package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SystemOwner;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to edit an attribute in a MemberRole object.  Setting the new attribute 
 * value is done through the command stack.<br><br>
 * Subclasses must supply the attribute and an indicator if the attribute is 
 * offered read-only during construction and, if they want a description to
 * be shown, the getDescription method of this class' superclass.<br><br>
 * Valid edit part model object types for these kind of sections are 
 * ConnectionPart, ConnectionLabel and SystemOwner.
 */
public abstract class AbstractMemberRoleAttributeSection
    extends AbstractStructuralFeatureSection<MemberRole> {	

	public AbstractMemberRoleAttributeSection(EAttribute attribute, 
											  boolean readOnly) {
		super(attribute, readOnly);		
	}

	@Override
	protected final MemberRole getModelObject(Object editPartModelObject) {
		if (editPartModelObject instanceof ConnectionPart) {
			return ((ConnectionPart) editPartModelObject).getMemberRole();
		} else if (editPartModelObject instanceof ConnectionLabel) {
			ConnectionLabel setDescription = 
				(ConnectionLabel) editPartModelObject;
			return setDescription.getMemberRole();
		} else {
			SystemOwner systemOwner = (SystemOwner) editPartModelObject;
			return systemOwner.getSet().getMembers().get(0);
		}
	}	
	
	@Override
	protected final Class<?>[] getValidEditPartModelObjectTypes() {
		return new Class<?>[] {ConnectionLabel.class,
							   ConnectionPart.class,
							   SystemOwner.class};
	}
	
}