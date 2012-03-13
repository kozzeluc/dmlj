package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Set;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to edit an attribute in an OwnerRole object.  Setting the new attribute 
 * value is done through the command stack.<br><br>
 * Subclasses must supply the attribute and an indicator if the attribute is 
 * offered read-only during construction and, if they want a description to
 * be shown, the getDescription method of this class' superclass.<br><br>
 * Valid edit part model object types for these kind of sections are 
 * ConnectionPart, ConnectionLabel and Connector.
 */
public abstract class AbstractOwnerRoleAttributeSection
    extends AbstractStructuralFeatureSection<OwnerRole> {	

	public AbstractOwnerRoleAttributeSection(EAttribute attribute, boolean readOnly) {
		super(attribute, readOnly);		
	}

	@Override
	protected final OwnerRole getModelObject(Object editPartModelObject) {
		Set set = null;
		if (editPartModelObject instanceof ConnectionPart) {
			set = ((ConnectionPart) editPartModelObject).getMemberRole()
														.getSet();
		} else if (editPartModelObject instanceof ConnectionLabel) {
			ConnectionLabel connectionLabel =
				(ConnectionLabel) editPartModelObject;
			set = connectionLabel.getMemberRole().getSet();
		} else {
			Connector connector = (Connector) editPartModelObject;
			set = connector.getConnectionPart().getMemberRole().getSet();
		}
		return set.getOwner();		
	}	
	
	@Override
	protected final Class<?>[] getValidEditPartModelObjectTypes() {
		return new Class<?>[] {ConnectionLabel.class,				
							   ConnectionPart.class,
							   Connector.class};
	}
	
}