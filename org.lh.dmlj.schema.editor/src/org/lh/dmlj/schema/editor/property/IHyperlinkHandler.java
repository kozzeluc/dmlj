package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public interface IHyperlinkHandler {

	EObject getModelObject();
	
	/**
	 * Deals with pressing a hyperlink in a property sheet's cell.
	 * @param attribute the hyperlink's attribute
	 * @return true if the pressing the hyperlink caused changes to the model,
	 *         false if not
	 */
	boolean hyperlinkActivated(EAttribute attribute);
	
}