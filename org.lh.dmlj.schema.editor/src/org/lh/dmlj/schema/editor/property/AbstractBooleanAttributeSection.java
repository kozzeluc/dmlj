package org.lh.dmlj.schema.editor.property;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.SetBooleanAttributeCommand;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to edit a boolean attribute in an Ecore model object (read-only mode NOT
 * supported).<br><br>  
 * Setting the new attribute value is done through the command stack.<br><br>
 * Subclasses must supply the attribute during construction, must 
 * override the getModelObject method and can optionally override the
 * getCheckboxLabel, getCommandLabelForFalse, getCommandLabelForTrue, 
 * getDescription and isCheckboxEnabled methods.
 */
public abstract class AbstractBooleanAttributeSection<T extends EObject> 
	extends AbstractPropertySection {	

	private EAttribute 				attribute;			
	private Button 	   		  		checkbox;
	private CustomSelectionListener	checkboxListener;
	private ModelChangeListener	    modelChangeListener;
	protected T 	  			    modelObject;
	
	public AbstractBooleanAttributeSection(EAttribute attribute) {
		super();	
		this.attribute = attribute;
	}	

	@Override
	public final void createControls(Composite parent, 
							   		 TabbedPropertySheetPage aTabbedPropertySheetPage) {
		
		super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = 
        	getWidgetFactory().createFlatFormComposite(parent);        
   
        checkbox = getWidgetFactory().createButton(composite, 
        										   getCheckboxLabel(), SWT.CHECK);
        checkbox.setToolTipText(getDescription());
        FormData data = new FormData();
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        checkbox.setLayoutData(data);
        checkboxListener = 
        	new CustomSelectionListener(attribute, getCommandLabelForFalse(), 
        								getCommandLabelForTrue());
        checkbox.addSelectionListener(checkboxListener);                
        
        // make sure that any changes to the model object (e.g. due to an undo
        // or redo) are reflected in the text field...
        modelChangeListener = 
        	new ModelChangeListener(this, checkbox, attribute);
        
	}
	
	@Override
	public void dispose() {
		if (modelObject != null) {
			modelObject.eAdapters().remove(modelChangeListener);
		}
	}
	
	/**
	 * Sets the label for the checkbox, which defaults to the attrubute name.
	 * @return the checkbox label
	 */
	protected String getCheckboxLabel() {
		return attribute.getName();
	}
	
	/**
	 * Sets the label for the command that will set the boolean attribute to 
	 * false; this label will be visible on the Edit menu (undo/redo).
	 * @return the command label
	 */
	protected String getCommandLabelForFalse() {
		return "Set " + attribute.getName() + " to false";
	}
	
	/**
	 * Sets the label for the command that will set the boolean attribute to 
	 * true; this label will be visible on the Edit menu (undo/redo).
	 * @return the command label
	 */
	protected String getCommandLabelForTrue() {
		return "Set " + attribute.getName() + " to true";
	}
	
	/**
	 * Sets the description to be shown for the attribute.
	 * @return the description for the attribute
	 */
	protected String getDescription() {
		return null;
	}
	
	/**
	 * Determines the Ecore model object of which the boolean attribute  
	 * supplied during construction of this section is the target of this 
	 * property section.
	 * @param editPartModelObject the EditPart's model object that contains a
	 *        direct or indirect reference to the Ecore model object
	 * @return the Ecore model object of type T whose attribute is the target of 
	 *         this section
	 */
	protected abstract T getModelObject(Object editPartModelObject);	

	
	
	/**
	 * Determines whether the checkbox is enabled or not.  Subclasses should
	 * override this method if another attribute of the same model object has
	 * an influence on this section's checkbox enabled status.  The model object
	 * is available via the modelObject field, which accessible by subclasses.
	 * @return whether the checkbox is enabled or not
	 */
	public boolean isCheckboxEnabled() {
		return true;
	}

	@Override
	public final void refresh() {		
		checkbox.removeSelectionListener(checkboxListener);		
		Object value = modelObject.eGet(attribute);		
		checkbox.setSelection((boolean) value);		
		checkbox.setEnabled(isCheckboxEnabled());
		checkbox.addSelectionListener(checkboxListener);
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {
		
		super.setInput(part, selection);
        
		Assert.isTrue(part instanceof SchemaEditor, "not a SchemaEditor");
		SchemaEditor editor = (SchemaEditor) part;
		
		Assert.isTrue(selection instanceof IStructuredSelection,
        			  "not a IStructuredSelection");
        Object input = ((IStructuredSelection) selection).getFirstElement();        
        
        Assert.isTrue(input instanceof EditPart, "not an EditPart");
        Object editPartModelObject = ((EditPart) input).getModel();
        
        if (modelObject != null) {
			modelObject.eAdapters().remove(modelChangeListener);
		}
        
        modelObject = getModelObject(editPartModelObject);         
        Assert.isTrue(modelObject != null, "no model object");
        
        modelObject.eAdapters().add(modelChangeListener);
                
        checkboxListener.setCommandStack(editor.getCommandStack());
        checkboxListener.setModelObject(modelObject);
	}
	
    private static class CustomSelectionListener extends SelectionAdapter {
    	    	
    	private EAttribute 	 attribute;
    	private String 		 commandLabelForFalse;
    	private String 		 commandLabelForTrue;
    	private CommandStack commandStack;    	
    	private EObject  	 modelObject;
    	
    	CustomSelectionListener(EAttribute attribute, 
    							String commandLabelForFalse,
    							String commandLabelForTrue) {
    		super();    		
    		this.attribute = attribute;
    		this.commandLabelForFalse = commandLabelForFalse;
    		this.commandLabelForTrue = commandLabelForTrue;
    	}

    	@Override
    	public void widgetSelected(SelectionEvent e) {		
			Button checkbox = (Button) e.getSource();
			String label = checkbox.getSelection() ? commandLabelForTrue : 
												     commandLabelForFalse;
			SetBooleanAttributeCommand command = 
				new SetBooleanAttributeCommand(modelObject, attribute,
									  	       checkbox.getSelection(), label);			
			commandStack.execute(command);
		}
		
		void setCommandStack(CommandStack commandStack) {
			this.commandStack = commandStack;
		}
		
		void setModelObject(EObject modelObject) {
			this.modelObject = modelObject;
		}
        
    };	
    
    private static class ModelChangeListener implements Adapter {

    	private EAttribute 						   attribute;
    	private Button 	   						   checkbox;
    	private AbstractBooleanAttributeSection<?> section;
    	
    	private ModelChangeListener(AbstractBooleanAttributeSection<?> section,
    								Button checkbox, EAttribute attribute) {
    		super();
    		this.section = section;
    		this.checkbox = checkbox;
    		this.attribute = attribute;
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
			if (!checkbox.isDisposed()) {
				if (notification.getEventType() == Notification.SET &&
					notification.getFeature() == attribute) {
										
					checkbox.setSelection(notification.getNewBooleanValue());					
				} else {				
					checkbox.setEnabled(section.isCheckboxEnabled());
				}
			}
		}

		@Override
		public void setTarget(Notifier newTarget) {						
		}
    	
    }
	
}