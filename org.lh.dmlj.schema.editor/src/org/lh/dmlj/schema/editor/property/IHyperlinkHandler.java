package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public interface IHyperlinkHandler {

	EObject getModelObject();
	
	void hyperlinkActivated(EAttribute attribute);
	
}