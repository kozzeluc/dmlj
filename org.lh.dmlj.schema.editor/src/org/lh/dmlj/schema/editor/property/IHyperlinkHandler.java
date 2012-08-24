package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

public interface IHyperlinkHandler {

	EObject getModelObject();
	
	/**
	 * Assembles the Command to change the model when the hyperlink is activated
	 * on the given attribute.
	 * @param attribute the hyperlink's attribute
	 * @return the Command to be executed to change the model or null if the 
	 *         model is not to be changed
	 */
	Command hyperlinkActivated(EAttribute attribute);
	
}