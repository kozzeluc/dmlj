package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Set;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to edit an attribute in an OwnerRole object.  Setting the new attribute 
 * value is done through the command stack.<br><br>
 * Subclasses must supply the attribute and an indicator if the attribute is 
 * offered read-only during construction and, if they want a description to
 * be shown, the getDescription method of this class' superclass.<br><br>
 * Valid edit part model object types for these kind of sections are MemberRole
 * and ConnectionLabel.
 */
public abstract class AbstractOwnerRoleAttributeSection
    extends AbstractStructuralFeatureSection<OwnerRole> {	

	public AbstractOwnerRoleAttributeSection(EAttribute attribute, boolean readOnly) {
		super(attribute, readOnly);		
	}

	@Override
	protected final OwnerRole getModelObject(Object editPartModelObject) {
		Set set = null;
		if (editPartModelObject instanceof MemberRole) {
			set = ((MemberRole) editPartModelObject).getSet();
		} else {
			ConnectionLabel connectionLabel =
				(ConnectionLabel) editPartModelObject;
			set = connectionLabel.getMemberRole().getSet();
		}
		return set.getOwner();		
	}	
	
	@Override
	protected final Class<?>[] getValidEditPartModelObjectTypes() {
		return new Class<?>[] {MemberRole.class,
							   ConnectionLabel.class};
	}
	
}