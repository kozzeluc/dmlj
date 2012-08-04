package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.SchemaEditor;

public abstract class AbstractPropertiesSection<T extends EObject>
	extends AbstractPropertySection {		

	private List<EAttribute> 		 attributes = new ArrayList<>();
	private DescriptionManager	     descriptionManager;
	private List<EAttribute> 		 editableAttributes = new ArrayList<>();
	private List<EObject>  			 editableObjects = new ArrayList<>();
	private ModelChangeListener	     modelChangeListener = 
		new ModelChangeListener(this);
	private PropertyEditor			 propertyEditor;
	private Table			   	     table;
	protected T	   			   	     target;
	
	public AbstractPropertiesSection() {
		super();
	}

	@Override
	public final void createControls(Composite parent,
							   		 TabbedPropertySheetPage page) {
	
		super.createControls(parent, page);
	
		// create the container to hold the table
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(Display.getCurrent()
			     					   .getSystemColor(SWT.COLOR_WHITE));
		GridLayout gridLayout = new GridLayout(1, false);
		composite.setLayout(gridLayout);
	
		// create the table and set its layout data
		table = new Table(composite, SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = SWT.FILL;
		table.setLayoutData(gridData);
	
		// create the first table column, holding the property names
		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setWidth(175);
		column1.setText("Property");
	
		// create the second table column, holding the property values
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setWidth(200);
		column2.setText("Value");
		
		// add the description manager
		descriptionManager = new DescriptionManager(page, this, table);		
		
		// add the property editor
		propertyEditor = new PropertyEditor(page, this, table);
	
	}
	
	@Override
	public void dispose() {
		// We need to remove the model change listener from all editable objects
		// in order to prevent "Widget is disposed" SWTExceptions when typing a
		// new value in a cell and then selecting another edit part (i.e. 
		// without pressing the enter or 1 of the tab keys).
		removeModelChangeListeners();
	};
	
	protected abstract List<EAttribute> getAttributes();

	protected String getDescription(EAttribute attribute) {
		String key = "description." + attribute.getContainerClass().getName() + 
					 "." + attribute.getName();
		return getPluginProperty(key);	
	}

	/**
	 * Subclasses that provide editable attributes should override this method 
	 * and return the object that contains the given atribute, so that we can 
	 * provide a cell editor and listen for model changes in order to keep the 
	 * properties table synchronized with the model.<br><br>
	 * By default, an attribute is read-only in the properties table.
	 * @param attribute the attribute for which an editable object is needed
	 * @return the object that contains the given attribute or null if the 
	 *         attribute is read-only
	 */
	protected EObject getEditableObject(EAttribute attribute) {
		// we might need to change the type of the return value to a list or
		// array in the future if we need to listen to several objects for the
		// attribute
		return null;
	}

	/**
	 * Subclasses should override this method if some validation needs to be 
	 * done prior to changing the given attribute's value, the attribute applies 
	 * to another object than the 'target' or if anything else than just setting 
	 * the new attribute value has to be done.
	 * @param attribute the attribute whose value is being edited
	 * @param newValue the new value for the attribute, as entered by the user 
	 *        and which can be safely casted to the attribute's type (primitive
	 *        types are wrapped in their type wrapper); can be null in the case
	 *        the attribute's type is not a primitive type
	 * @return the edit handler containing a validation message and/or that 
	 *         contains the command to set the attribute's value
	 */
	protected IEditHandler getEditHandler(EAttribute attribute, 
										  Object newValue) {
		
		return new StandardEditHandler(target, attribute, getLabel(attribute), 
									   newValue);
	};
	
	protected String getLabel(EAttribute attribute) {		
		String key = "label." + attribute.getContainerClass().getName() + "." +
					 attribute.getName();
		
		String label = getPluginProperty(key);		
		if (label != null) {
			return label;
		} else {
			return attribute.getName();
		}
	}	
	
	protected String getPluginProperty(String key) {
		try {
			return Plugin.getDefault()
						 .getPluginProperties()
						 .getString(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}
	
	protected abstract T getTarget(Object object);

	protected String getValue(EAttribute attribute) {
		Object value = target.eGet(attribute);
		if (value != null) {
			return value.toString();
		} else {
			return "";
		}
	}	

	@Override
	public final void refresh() {
	
		// clear the description area
		descriptionManager.clear();
	
		// remove all table rows
		table.removeAll();
	
		// get the list of (relevant) attributes
		attributes.clear();
		attributes.addAll(getAttributes());
		
		// create the list of attributes for which we have to provide editing 
		// and make sure we are notified of changes to the model
		editableAttributes.clear();
		removeModelChangeListeners();
		editableObjects.clear();
		for (EAttribute attribute : attributes) {
			EObject editableObject = getEditableObject(attribute);
			if (editableObject != null) {
				// attribute is editable because an editable object is provided
				editableAttributes.add(attribute);								
				if (!editableObjects.contains(editableObject)) {
					// just keep 1 reference to the editable object
					editableObjects.add(editableObject);
					editableObject.eAdapters().add(modelChangeListener);
				}
			}
		}
		
		// add the (relevant) properties to the table		
		for (EAttribute attribute : attributes) {
			
			TableItem item = new TableItem(table, SWT.NONE);
			
			String name = getLabel(attribute);
			item.setText(0, name);
			
			String value = getValue(attribute);	
			if (value != null) {
				item.setText(1, value);
			} else {
				item.setText(1, "");
			}
			
		}
	
		// we don't want any vertical scrollbar in the table; the following
		// sequence allows us to do just that (i.e. vertically stretch the table
		// as needed)          
		for (Composite parent = table.getParent(); parent != null; 
			 parent = parent.getParent()) {                      
		     
			parent.layout();                                          
		}
	
	}

	private void removeModelChangeListeners() {
		for (EObject object : editableObjects) {
			object.eAdapters().remove(modelChangeListener);
		}
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {

		super.setInput(part, selection);

		// we can only work with SchemaEditor instances
		Assert.isTrue(part instanceof SchemaEditor, "not a SchemaEditor");
		Assert.isTrue(selection instanceof IStructuredSelection,
        			  "not a IStructuredSelection");
        Object input = ((IStructuredSelection) selection).getFirstElement();        

        Object modelObject = ((EditPart) input).getModel();
        target = getTarget(modelObject);
        
        // we need the editor's command stack to change the model data
        CommandStack commandStack = ((SchemaEditor) part).getCommandStack();
        propertyEditor.setCommandStack(commandStack);

	} 	

}