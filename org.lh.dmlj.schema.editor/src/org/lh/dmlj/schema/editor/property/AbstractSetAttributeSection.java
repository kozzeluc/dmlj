package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.model.SetDescription;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to edit an attribute in a Set object.  Setting the new attribute value 
 * is done through the command stack.<br><br>
 * Subclasses must supply the attribute and an indicator if the attribute is 
 * offered read-only during construction and, if they want a description to
 * be shown, the getDescription method of this class' superclass.<br><br>
 * Valid edit part model object types for these kind of sections are MemberRole,
 * SchemaRecord, SetDescription and SystemOwner.
 */
public abstract class AbstractSetAttributeSection 
	extends AbstractStructuralFeatureSection<Set> {		
	

	public AbstractSetAttributeSection(EAttribute attribute, boolean readOnly) {
		super(attribute, readOnly);		
	}

	@Override
	protected final Set getModelObject(Object editPartModelObject) {
		if (editPartModelObject instanceof MemberRole) {
			return ((MemberRole) editPartModelObject).getSet();
		} else if (editPartModelObject instanceof SchemaRecord) {
			SchemaRecord record = (SchemaRecord) editPartModelObject;
			return record.getViaSpecification().getSet();
		} else if (editPartModelObject instanceof SetDescription) {
			SetDescription setDescription =
				(SetDescription) editPartModelObject;
			return setDescription.getMemberRole().getSet();
		} else {
			SystemOwner systemOwner = (SystemOwner) editPartModelObject;
			return systemOwner.getSet();
		}
	}	
	
	@Override
	protected final Class<?>[] getValidEditPartModelObjectTypes() {
		return new Class<?>[] {MemberRole.class,
							   SchemaRecord.class,
							   SetDescription.class,
							   SystemOwner.class};
	}
	
}