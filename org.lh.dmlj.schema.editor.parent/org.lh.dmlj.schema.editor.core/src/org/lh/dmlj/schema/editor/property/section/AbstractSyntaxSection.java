/**
 * Copyright (C) 2014  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.property.section;

import java.lang.reflect.Method;
import java.util.Arrays;

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
	
	protected Object[] getTemplateParametersOtherThanTemplateObject() {
		return new Object[] {};
	}
	
	@Override
	protected String getValue(Object editPartModelObject) {
		EObject templateObject = getTemplateObject(editPartModelObject);
		Object[] templateParametersOtherThanTemplateObject = 
			getTemplateParametersOtherThanTemplateObject();
		String syntax;
		try {
			int argsLength = templateParametersOtherThanTemplateObject != null ? 
							 templateParametersOtherThanTemplateObject.length + 1 : 1; 
			Object args[] = new Object[argsLength];
			args[0] = templateObject;
			if (templateParametersOtherThanTemplateObject != null && 
				templateParametersOtherThanTemplateObject.length > 0) {
				
				System.arraycopy(templateParametersOtherThanTemplateObject, 0, args, 1, argsLength - 1);
			}
			syntax = (String) generateMethod.invoke(template, Arrays.asList(args));						
		} catch (Throwable t) {
			t.printStackTrace();
			syntax = "an error occurred while generating the DDL: " + 
					 t.getClass().getName() + "(" + t.getMessage() + ")";
		}
		return syntax;
	}
}
