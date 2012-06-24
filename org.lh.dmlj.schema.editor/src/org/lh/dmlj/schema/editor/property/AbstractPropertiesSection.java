package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
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
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.SetBooleanAttributeCommand;
import org.lh.dmlj.schema.editor.command.SetShortAttributeCommand;
import org.lh.dmlj.schema.editor.command.SetStringAttributeCommand;

public abstract class AbstractPropertiesSection<T extends EObject>
	extends AbstractPropertySection {		

	private DescriptionManager	     descriptionManager;
	private List<EStructuralFeature> editableFeatures = new ArrayList<>();
	private List<EObject>  			 editableObjects = new ArrayList<>();
	private List<EStructuralFeature> features = new ArrayList<>();
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
		propertyEditor = new PropertyEditor(this, table);
	
	}
	
	@Override
	public void dispose() {
		// We need to remove the model change listener from all editable objects
		// in order to prevent "Widget is disposed" SWTExceptions when typing a
		// new value in a cell and then selecting another edit part (i.e. 
		// without pressing the enter or 1 of the tab keys).
		removeModelChangeListeners();
	};

	/**
	 * Subclasses should override this method if the feature applies to another
	 * object than the 'target' or if anything else than just setting the new
	 * feature value has to be done.
	 * @param feature the feature whose value was edited
	 * @param newValue the new value for the feature
	 * @return the command that will set the feature's value
	 */
	protected Command getEditCommand(EStructuralFeature feature, 
									 String newValue) {
		if (!(feature instanceof EAttribute)) {
			throw new Error("unsupported type: " + feature.getEType().getName());
		}
		EAttribute attribute = (EAttribute) feature;
		String label = getLabel(feature);
		if (feature.getEType().getName().equals("EString")) {
			return new SetStringAttributeCommand(target, attribute, newValue, 
												 label);
		} else if (feature.getEType().getName().equals("EBoolean")) {
			boolean b = Boolean.valueOf(newValue).booleanValue();
			return new SetBooleanAttributeCommand(target, attribute, b, label);
		} else if (feature.getEType().getName().equals("EShort")) {
			short i = Short.valueOf(newValue).shortValue();
			return new SetShortAttributeCommand(target, attribute, i, label);
		} else {
			throw new Error("unsupported type: " + feature.getEType().getName());
		}
	}
	
	protected abstract List<EStructuralFeature> getFeatures();

	protected String getDescription(EStructuralFeature feature) {
		return null;
	}
	
	/**
	 * Subclasses that provide editable features should override this method and
	 * return the object that contains the given feature, so that we can provide 
	 * a cell editor and listen for model changes in order to keep the 
	 * properties table synchronized with the model.<br><br>
	 * By default, a feature is read-only in the properties table.
	 * @param feature the feature for which an editable object is needed
	 * @return the object that contains the given feature or null if the feature
	 *         is read-only
	 */
	protected EObject getEditableObject(EStructuralFeature feature) {
		// we might need to change the type of the return value to a list or
		// array in the future if we need to listen to several objects for the
		// feature
		return null;
	}

	protected String getLabel(EStructuralFeature feature) {
		return feature.getName();
	}	
	
	protected abstract T getTarget(Object object);

	protected String getValue(EStructuralFeature feature) {
		Object value = target.eGet(feature);
		if (value != null) {
			return value.toString();
		} else {
			return "";
		}
	}

	/**
	 * Subclasses should override this method if a cell editor is to be provided
	 * for 1 or more features.
	 * @param feature a feature from the list returned by getFeatures()
	 * @return true if the feature's value should be editable
	 *//*
	protected boolean isEditableFeature(EStructuralFeature feature) {
		return false;
	}*/

	@Override
	public final void refresh() {
	
		// clear the description area
		descriptionManager.clear();
	
		// remove all table rows
		table.removeAll();
	
		// get the list of (relevant) features
		features.clear();
		features.addAll(getFeatures());
		
		// create the list of features for which we have to provide editing and
		// make sure we are notified of changes to the model
		editableFeatures.clear();
		removeModelChangeListeners();
		editableObjects.clear();
		for (EStructuralFeature feature : features) {
			EObject editableObject = getEditableObject(feature);
			if (editableObject != null) {
				// feature is editable because an editable object is provided
				editableFeatures.add(feature);								
				if (!editableObjects.contains(editableObject)) {
					// just keep 1 reference to the editable object
					editableObjects.add(editableObject);
					editableObject.eAdapters().add(modelChangeListener);
				}
			}
		}
		
		// add the (relevant) properties to the table		
		for (EStructuralFeature feature : features) {
			
			TableItem item = new TableItem(table, SWT.NONE);
			
			String name = getLabel(feature);
			item.setText(0, name);
			
			String value = getValue(feature);	
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