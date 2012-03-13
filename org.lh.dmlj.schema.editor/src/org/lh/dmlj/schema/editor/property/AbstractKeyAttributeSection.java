package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to VIEW an attribute or reference in a Key object.<br><br>
 * Subclasses must supply the attribute during construction and, if they want a 
 * description to be shown, the getDescription method of this class' superclass.
 * <br><br>
 * Valid edit part model object types for these kind of sections are 
 * ConnectionPart, SchemaRecord, ConnectionLabel, SystemOwner and Connector.  
 * For set related edit part model object types the set's sort key will be taken 
 * as the section's model object, for a schema record it will be the CALC key.
 */
public abstract class AbstractKeyAttributeSection
    extends AbstractStructuralFeatureSection<Key> {	

	public AbstractKeyAttributeSection(EAttribute attribute) {
		super(attribute, true); // read-only		
	}
	
	public AbstractKeyAttributeSection(EReference reference) {
		super(reference); // read-only		
	}

	@Override
	protected final Key getModelObject(Object editPartModelObject) {		
		if (editPartModelObject instanceof SchemaRecord) {							
			SchemaRecord record = (SchemaRecord) editPartModelObject;
			return record.getCalcKey();
		} else if (editPartModelObject instanceof ConnectionPart) {
			MemberRole memberRole = 
				((ConnectionPart) editPartModelObject).getMemberRole();
			return memberRole.getSortKey();
		} else if (editPartModelObject instanceof ConnectionLabel) {
			ConnectionLabel connectionLabel = 
				(ConnectionLabel) editPartModelObject;
			return connectionLabel.getMemberRole().getSortKey();
		} else if (editPartModelObject instanceof Connector) {
			Connector connector = (Connector) editPartModelObject;
			return connector.getConnectionPart().getMemberRole().getSortKey();
		} else {
			// system owner
			SystemOwner systemOwner = (SystemOwner) editPartModelObject;
			return systemOwner.getSet().getMembers().get(0).getSortKey();
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