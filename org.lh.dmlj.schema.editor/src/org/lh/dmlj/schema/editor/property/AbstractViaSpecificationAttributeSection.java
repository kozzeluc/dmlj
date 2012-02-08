package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.ViaSpecification;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to edit an attribute in a ViaSpecification object.  Setting the new 
 * attribute value is done through the command stack.<br><br>
 * Subclasses must supply the attribute and an indicator if the attribute is 
 * offered read-only during construction and, if they want a description to
 * be shown, the getDescription method of this class' superclass.<br><br>
 * The only valid edit part model object type for these kind of sections is 
 * SchemaRecord.
 */
public abstract class AbstractViaSpecificationAttributeSection 
	extends AbstractStructuralFeatureSection<ViaSpecification> {		
	

	public AbstractViaSpecificationAttributeSection(EAttribute attribute, 
													boolean readOnly) {
		super(attribute, readOnly);		
	}

	@Override
	protected final ViaSpecification getModelObject(Object editPartModelObject) {		
		SchemaRecord record = (SchemaRecord) editPartModelObject;
		return record.getViaSpecification();
	}	
	
	@Override
	protected final Class<?>[] getValidEditPartModelObjectTypes() {
		return new Class<?>[] {SchemaRecord.class};
	}
	
}