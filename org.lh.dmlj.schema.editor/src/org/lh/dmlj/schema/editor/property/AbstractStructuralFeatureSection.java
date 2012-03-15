package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.SetStringAttributeCommand;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to view/edit an attribute or view a reference in an Ecore model object.  
 * Setting the new attribute value is done through the command stack.<br><br>
 * Subclasses must supply the attribute or the reference and (in the case of an
 * attribute) an indicator if it is offered read-only during construction, must 
 * override the getModelObject and getValidEditPartModelObjectTypes methods and, 
 * if they want a description to be shown, the getDescription method.  
 * Subclasses should override the getLabel method if they want another label 
 * than the attribute's name to be used.  Subclasses can override the 
 * getStringValue method if they want to manipulate the attribute's value or 
 * reference to be shown.
 */
public abstract class AbstractStructuralFeatureSection<T extends EObject> 
	extends AbstractPropertySection {
	
	public static final int 	CUSTOM_LABEL_WIDTH = 140;

	private EStructuralFeature 	structuralFeature;
	private CustomKeyListener  	listener;
	private Text 	   		   	labelText;
	private ModelChangeListener	modelChangeListener;
	private T 		  		   	modelObject;
	private Class<?>[] 		   	validEditPartModelObjectTypes;
	
	public AbstractStructuralFeatureSection(EAttribute attribute) {
		this(attribute, true);
	}
	
	public AbstractStructuralFeatureSection(EAttribute attribute, 
										    boolean readOnly) {
		super();
		this.structuralFeature = attribute;
		validEditPartModelObjectTypes = getValidEditPartModelObjectTypes();
		if (!readOnly) {
			listener = new CustomKeyListener(attribute);
		}
	}
	
	public AbstractStructuralFeatureSection(EReference reference) {
		super();
		this.structuralFeature = reference;
		validEditPartModelObjectTypes = getValidEditPartModelObjectTypes();		
	}
	
	private String capitalize(String name) {
		if (name.length() == 0) {
			return name;
		} else if (name.length() == 1) {
			return name.toUpperCase();
		} else {
			return name.substring(0, 1).toUpperCase() + name.substring(1);
		}	
	}

	@Override
	public final void createControls(Composite parent, 
							   		 TabbedPropertySheetPage aTabbedPropertySheetPage) {
		
		super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = 
        	getWidgetFactory().createFlatFormComposite(parent);
        FormData data;
   
        labelText = getWidgetFactory().createText(composite, "");
        labelText.setEditable(listener != null);
        String toolTipText = getDescription();
        if (toolTipText != null) {
        	labelText.setToolTipText(toolTipText);
        }
        data = new FormData();
        data.left = new FormAttachment(0, CUSTOM_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        labelText.setLayoutData(data); 
        if (listener != null) {
        	labelText.addKeyListener(listener);
        }
   
        CLabel labelLabel = 
        	getWidgetFactory().createCLabel(composite, getLabel() + ":");
        if (toolTipText != null) {
        	labelLabel.setToolTipText(toolTipText);
        }
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = 
        	new FormAttachment(labelText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(labelText, 0, SWT.CENTER);
        labelLabel.setLayoutData(data);
        
        // make sure that any changes to the model object (e.g. due to an undo
        // or redo) are reflected in the text field...
        modelChangeListener = 
        	new ModelChangeListener(labelText, structuralFeature);        
        
	}
	
	@Override
	public void dispose() {
		if (modelObject != null) {
			modelObject.eAdapters().remove(modelChangeListener);
		}
	}
	
	protected String getLabel() {
		return capitalize(structuralFeature.getName());
	}

	/**
	 * Sets the description to be shown for the attribute or reference.
	 * @return the description for the attribute or reference
	 */
	protected String getDescription() {
		return null;
	}
	
	/**
	 * Determines the Ecore model object of which the attribute or reference 
	 * supplied during construction of this section is the target of this 
	 * property view section.
	 * @param editPartModelObject the EditPart's model object that contains a
	 *        direct or indirect reference to the Ecore model object
	 * @return the Ecore model object of type T whose attribute or reference is 
	 * 	  	   the target of this section
	 */
	protected abstract T getModelObject(Object editPartModelObject);
	
	protected String getStringValue(Object value) {		
		if (value != null) {		
			return value.toString();
		} else {
			return "";
		}
	}

	/**
	 * Specifies the valid types of EditPart model objects that should lead to
	 * the Ecore object whose attribute or reference is the target of this 
	 * section.
	 * @return the valid types of EditPart model objects
	 */
	protected abstract Class<?>[] getValidEditPartModelObjectTypes();
	
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
		if (listener != null) {
			labelText.removeKeyListener(listener);
		}
		Object value = modelObject.eGet(structuralFeature);		
		labelText.setText(getStringValue(value));
		if (listener != null) {
			labelText.addKeyListener(listener);
		}
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
        Assert.isTrue(isValidType(editPartModelObject), 
        			  "not a " + Arrays.asList(validEditPartModelObjectTypes));
        
        if (modelObject != null) {
			modelObject.eAdapters().remove(modelChangeListener);
		}
        
        modelObject = getModelObject(editPartModelObject);        
        
        // always listen for model changes, even if the attribute is read-only;
        // the model change listener might not yet be instantiated though
        modelObject.eAdapters().add(modelChangeListener);
        
        if (listener != null) {
        	listener.setCommandStack(editor.getCommandStack());
        	listener.setModelObject(modelObject);
        }        
        
	}
	
    private static class CustomKeyListener extends KeyAdapter {
    	
    	private EAttribute   attribute;
    	private CommandStack commandStack;
    	private EObject 	 modelObject;
    	
    	CustomKeyListener(EAttribute attribute) {
    		super();
    		this.attribute = attribute;
    	}

		@Override
		public void keyReleased(KeyEvent e) {
			Text labelText = (Text) e.getSource();
			if (e.keyCode == 13 || e.keyCode == 16777296) { // enter-keys								
				SetStringAttributeCommand command = 
					new SetStringAttributeCommand(modelObject,
										  	      attribute,
										  	      labelText.getText().trim());				
				commandStack.execute(command);
			}
		}
		
		void setCommandStack(CommandStack commandStack) {
			this.commandStack = commandStack;
		}
		
		void setModelObject(EObject modelObject) {
			this.modelObject = modelObject;
		}
        
    };
    
    private static class ModelChangeListener implements Adapter {

    	private EStructuralFeature structuralFeature;
    	private Text 			   text;
    	
    	private ModelChangeListener(Text text, 
    								EStructuralFeature structuralFeature) {
    		super();
    		this.text = text;
    		this.structuralFeature = structuralFeature;
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
				notification.getFeature() == structuralFeature) {
				
				if (!text.isDisposed()) {
					String newValue = notification.getNewStringValue();
					if (newValue != null) {
						text.setText(newValue);
					} else {
						text.setText("");
					}
				}
			}
		}

		@Override
		public void setTarget(Notifier newTarget) {						
		}
    	
    }    
	
}