package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.SchemaEditor;

/**
 * An abstract superclass for sections in the tabbed properties view that show  
 * their information in a single styled text control.  Subclasses must supply  
 * the valid edit part model object types during construction and can override
 * the getFont method if they want another font than the standard 'syntax' font
 * to be used.  They can also override the getObjectsToMonitor() method so that
 * the StyledText content remains synchronized with the model.
 */
public abstract class AbstractSectionWithStyledText 
	extends AbstractPropertySection {
	
	private List<EObject> monitoredObjects = new ArrayList<>();
	private StyledText	  styledText;
	protected EObject	  target;
	private Class<?>[]	  validEditPartModelObjectTypes;
	
	private Adapter		  modelChangeListener = new Adapter() {
		@Override
		public void notifyChanged(Notification notification) {
			if (notification.getEventType() != Notification.REMOVING_ADAPTER) {
				refresh();
			}
		}
		@Override
		public Notifier getTarget() {
			return null;
		}
		@Override
		public void setTarget(Notifier newTarget) {					
		}
		@Override
		public boolean isAdapterForType(Object type) {
			return false;
		}
	};
		
	protected AbstractSectionWithStyledText(Class<?>[] validEditPartModelObjectTypes) {
		super();
		this.validEditPartModelObjectTypes = validEditPartModelObjectTypes;		
	}
	
	@Override
	public final void createControls(Composite parent, 
							   		 TabbedPropertySheetPage aTabbedPropertySheetPage) {
				
		super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = 
        	getWidgetFactory().createFlatFormComposite(parent);

		styledText = new StyledText(composite, SWT.MULTI | SWT.READ_ONLY);				
		styledText.setFont(getFont());
				
		FormData data = new FormData();	
		data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		styledText.setLayoutData(data);
		
		// if we ever want to add 'dynamic tooltips', a MouseTrackListener is
		// way to go...
		/*MouseTrackListener mouseTrackListener = getMouseTrackListener();
		if (mouseTrackListener != null) {
			styledText.addMouseTrackListener(mouseTrackListener);
		}*/
        
	}
	
	@Override
	public void dispose() {
		// We need to remove the model change listener from all monitored 
		// objects
		removeModelChangeListeners();
	};
	
	protected Font getFont() {
		return Plugin.getDefault().getSyntaxFont();
	}

	/**
	 * Subclasses must override this method and indicate which model objects are
	 * used to generate the StyledText content.
	 * @return a list of the EObjects to monitor for changes
	 */
	protected Collection<? extends EObject> getObjectsToMonitor() {
		return Collections.emptyList();
	}

	protected abstract String getValue(Object editPartModelObject);

	private boolean isValidType(Object object) {
		for (Class<?> _class : validEditPartModelObjectTypes) {
			if (_class.isInstance(object)) {
				return true;
			}
		}
		return false;
	}	
	
	@Override
	public final void refresh() {
		styledText.setText(getValue(target));
	}
	
	private void removeModelChangeListeners() {
		for (EObject object : monitoredObjects) {
			object.eAdapters().remove(modelChangeListener);
		}
		monitoredObjects.clear();
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {
		
		super.setInput(part, selection);
        
		Assert.isTrue(part instanceof SchemaEditor, "not a SchemaEditor");		
		
		Assert.isTrue(selection instanceof IStructuredSelection,
        			  "not a IStructuredSelection");
        Object input = ((IStructuredSelection) selection).getFirstElement();        
        
        removeModelChangeListeners();
        
        Assert.isTrue(input instanceof EditPart, "not an EditPart");
        target = (EObject) ((EditPart) input).getModel();
        Assert.isTrue(isValidType(target), 
  			  		  "not a " + Arrays.asList(validEditPartModelObjectTypes) +
  			  		  " but a " + target.getClass().getName());
        
        // we need to react to model changes too; although the user cannot edit
        // the StyledText directly, model changes can occur if the user undoes
        // or redoes a command...
        monitoredObjects.addAll(getObjectsToMonitor());
        for (EObject object : monitoredObjects) {
        	object.eAdapters().add(modelChangeListener);
        }
	}	
	
}