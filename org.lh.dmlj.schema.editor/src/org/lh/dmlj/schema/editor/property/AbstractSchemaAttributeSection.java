package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.Schema;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to edit an attribute in a Schema object.  Setting the new attribute 
 * value is done through the command stack.<br><br>
 * Subclasses must supply the attribute and an indicator if the attribute is 
 * offered read-only during construction and, if they want a description to
 * be shown, the getDescription method of this class' superclass.<br><br>
 * The only valid edit part model object type for this kind of section is 
 * Schema.
 */
public abstract class AbstractSchemaAttributeSection
    extends AbstractStructuralFeatureSection<Schema> {	

	public AbstractSchemaAttributeSection(EAttribute attribute, boolean readOnly) {
		super(attribute, readOnly);		
	}

	@Override
	protected final Schema getModelObject(Object editPartModelObject) {		
		return (Schema) editPartModelObject;
	}	
	
	@Override
	protected final Class<?>[] getValidEditPartModelObjectTypes() {
		return new Class<?>[] {Schema.class};
	}	
	
}