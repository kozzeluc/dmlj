package org.lh.dmlj.schema.editor.property;

import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EObject;

/**
 * An abstract superclass for sections in the tabbed properties that show the 
 * DDL for a given object (record, set, ...).  Subclasses must supply the valid
 * edit part model object types and a template during construction and must
 * override method getTemplateObject if the template object is different from 
 * the edit part model object.
 */
public abstract class AbstractSyntaxSection 
	extends AbstractSectionWithStyledText {
	
	private Method generateMethod;
	private Object template;	
		
	protected AbstractSyntaxSection(Class<?>[] validEditPartModelObjectTypes,
									Object template) {
		super(validEditPartModelObjectTypes);
		this.template = template;
		try {
			generateMethod = 
				template.getClass().getDeclaredMethod("generate", Object.class);
		} catch (Throwable t) {			
			t.printStackTrace();
			throw new Error(t);
		}
	}

	protected EObject getTemplateObject(Object editPartModelObject) {
		if (editPartModelObject instanceof EObject) {
			return (EObject) editPartModelObject;
		}
		throw new Error("no template object");
	}			
	
	@Override
	protected String getValue(Object editPartModelObject) {
		EObject templateObject = getTemplateObject(editPartModelObject);
		String syntax;
		try {
			syntax = (String) generateMethod.invoke(template, templateObject);
		} catch (Throwable t) {
			t.printStackTrace();
			syntax = "an error occurred while generating the DDL: " + 
					 t.getClass().getName() + "(" + t.getMessage() + ")";
		}
		return syntax;
	}
}