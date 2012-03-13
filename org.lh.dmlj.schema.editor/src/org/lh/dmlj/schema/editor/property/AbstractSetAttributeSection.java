package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to edit an attribute in a Set object.  Setting the new attribute value 
 * is done through the command stack.<br><br>
 * Subclasses must supply the attribute and an indicator if the attribute is 
 * offered read-only during construction and, if they want a description to
 * be shown, the getDescription method of this class' superclass.<br><br>
 * Valid edit part model object types for these kind of sections are 
 * ConnectionPart, SchemaRecord, ConnectionLabel, SystemOwner and Connector.
 */
public abstract class AbstractSetAttributeSection 
	extends AbstractStructuralFeatureSection<Set> {		
	

	public AbstractSetAttributeSection(EAttribute attribute, boolean readOnly) {
		super(attribute, readOnly);		
	}

	@Override
	protected final Set getModelObject(Object editPartModelObject) {
		if (editPartModelObject instanceof ConnectionPart) {
			return ((ConnectionPart) editPartModelObject).getMemberRole()
														 .getSet();
		} else if (editPartModelObject instanceof SchemaRecord) {
			SchemaRecord record = (SchemaRecord) editPartModelObject;
			return record.getViaSpecification().getSet();
		} else if (editPartModelObject instanceof ConnectionLabel) {
			ConnectionLabel setDescription =
				(ConnectionLabel) editPartModelObject;
			return setDescription.getMemberRole().getSet();
		} else if (editPartModelObject instanceof SystemOwner) {
			SystemOwner systemOwner = (SystemOwner) editPartModelObject;
			return systemOwner.getSet();
		} else {
			Connector connector = (Connector) editPartModelObject;
			return connector.getConnectionPart().getMemberRole().getSet();
		}
	}	
	
	@Override
	protected final Class<?>[] getValidEditPartModelObjectTypes() {
		return new Class<?>[] {ConnectionLabel.class,
							   ConnectionPart.class,
							   Connector.class,
							   SchemaRecord.class,							   
							   SystemOwner.class};
	}
	
}