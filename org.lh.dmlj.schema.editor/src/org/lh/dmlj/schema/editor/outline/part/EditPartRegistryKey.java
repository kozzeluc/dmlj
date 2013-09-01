package org.lh.dmlj.schema.editor.outline.part;

import org.eclipse.emf.ecore.EObject;

public class EditPartRegistryKey<T extends EObject> {
	
	private T model;
	
	public EditPartRegistryKey(T model) {
		super();
		this.model = model;
	}
	
	public T getModel() {
		return model;
	}
	
}