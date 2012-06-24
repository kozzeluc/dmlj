package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * A model change listener that will synchronize the property values in an 
 * AbstractPropertiesSection<T> implementation with the model in the case of 
 * model changes.  It is the responsibility of the AbstractPropertiesSection<T>
 * to hook up this listener to the relevant model objects (and remove it from
 * them afterwards). 
 */
public class ModelChangeListener implements Adapter {    	
	
	private AbstractPropertiesSection<?> section;
	
	public ModelChangeListener(AbstractPropertiesSection<?> section) {
		super();    		
		this.section = section;
	}
	
	@Override
	public Notifier getTarget() {
		return null;
	}

	@Override
	public boolean isAdapterForType(Object type) {			
		return false;
	}

	@Override
	public void notifyChanged(Notification notification) {			
		if (notification.getEventType() == Notification.SET &&
			notification.getFeature() instanceof EStructuralFeature &&
			section.getEditableObject((EStructuralFeature) notification.getFeature()) != null) {				
			
			// because changing 1 property might cause the need to hide or show 
			// others, it's best to refresh the whole section; mind that the
			// property being edited will no longer be selected and thus its 
			// description will no longer be shown
			section.refresh();
			
		}
	}

	@Override
	public void setTarget(Notifier newTarget) {						
	}
	
} 