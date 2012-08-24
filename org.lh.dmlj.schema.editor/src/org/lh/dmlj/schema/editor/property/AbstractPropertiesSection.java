package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
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
	extends AbstractPropertySection implements CommandStackEventListener {		

	private List<EAttribute> 		 attributes = new ArrayList<>();
	private CommandStack 			 commandStack;
	private DescriptionManager	     descriptionManager;
	private TabbedPropertySheetPage  page;
	private PropertyEditor			 propertyEditor;
	private ISelection 			     selection;
	private Table			   	     table;
	protected T	   			   	     target;	
	
	public AbstractPropertiesSection() {
		super();
	}

	@Override
	public final void createControls(Composite parent,
							   		 TabbedPropertySheetPage page) {
	
		super.createControls(parent, page);
	
		this.page = page;
		
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
		// remove us as a command stack event listener if we have a command
		// stack
		if (commandStack != null) {
			commandStack.removeCommandStackEventListener(this);
		}
		if (propertyEditor != null) {
			propertyEditor.dispose();
		}
	};
	
	/**
	 * Subclasses should override this method if an attribute that does not
	 * belong to the target object is shown in the section.  In doing so,
	 * overriding the getValue(EAttribute) method might become unnecessary
	 * because the AbstractProperties class can directly retrieve the attribute
	 * value.
	 * @param attribute the attribute to be checked
	 * @return the EObject instance that owns the attribute; should never be null
	 */
	protected EObject getAttributeOwner(EAttribute attribute) {
		return target;
	}

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
		
		return getEditHandler(attribute, newValue, null);
	};
	
	protected final IEditHandler getEditHandler(EAttribute attribute, 
			  							  	    Object newValue, 
			  							  	    String message) {

		EObject attributeOwner = getAttributeOwner(attribute);
		return new StandardEditHandler(attributeOwner, attribute, 
									   getLabel(attribute), newValue, message);
	};
		
	/**
	 * Subclasses should override this method if a filter is needed for 
	 * filtering the items in a combo created for editing enum attributes.  The
	 * default behaviour is to not filter enum elements (i.e. show all of them
	 * in the combo).
	 * @param attribute the EAttribute for which a combo is created
	 * @return the filter to use or null if all enum elements are to be listed
	 *         in the combo
	 */
	protected IEnumFilter<? extends Enum<?>> getEnumFilter(EAttribute attribute) {
		return null;
	}
	
	/**
	 * Subclasses should override this method if a hyperlink has to be created
	 * when the mouse pointer above the attribute value.  
	 * This method is called multiple times.
	 * @param attribute
	 * @return 
	 */
	protected IHyperlinkHandler getHyperlinkHandler(EAttribute attribute) {
		return null;
	}

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
	
	TabbedPropertySheetPage getPage() {
		return page;
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

	/**
	 * Subclasses should only override this method in the case the value of the
	 * attribute is to be changed before shown - see method 
	 * getAttributeOwner(EAttribute).
	 * @param attribute the attribute for which the value is to be shown
	 * @return the value of the attribute to be shown in the section
	 */
	protected String getValue(EAttribute attribute) {
		EObject attributeOwner = getAttributeOwner(attribute);
		Assert.isNotNull(attributeOwner, "attribute owner is null: " +
					     attribute.getName());
		Object value = attributeOwner.eGet(attribute);
		if (value != null) {
			// if the attribute is an enum, replace all underscores by spaces			
			if (value instanceof Enumerator) {
				return value.toString().replaceAll("_", " ");
			} else {
				return value.toString();
			}
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
		
		// create the list of attributes for which we have to provide direct 
		// editing 
		/*editableAttributes.clear();				
		for (EAttribute attribute : attributes) {
			EObject editableObject = getEditableObject(attribute);
			if (editableObject != null) {
				// attribute is directly editable because an editable object is 
				// provided
				editableAttributes.add(attribute);												
			}
		}*/
		
		// add the (relevant) properties to the table		
		for (EAttribute attribute : attributes) {
			
			TableItem item = new TableItem(table, SWT.NONE);
			
			String name = getLabel(attribute);
			item.setText(0, name);
			
			String value = getValue(attribute);	
			if (value != null) {
				// we provide hyperlinks that allow dialog boxes to be shown or 
				// other cool things to happen in order to allow for more 
				// sophisticated model manipulation; if a hyperlink is to be
				// provided, make sure the user notices that by setting the 				
				// cell's foreground color to blue...
				if (getHyperlinkHandler(attribute) != null) {
					item.setForeground(1, ColorConstants.blue);
				}				
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

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {
	
		// remove us as a command stack event listener if we have a command
		// stack
		if (commandStack != null) {
			commandStack.removeCommandStackEventListener(this);
			commandStack = null;
		}
		
		super.setInput(part, selection);
		this.selection = selection;
	
		// we can only work with SchemaEditor instances
		Assert.isTrue(part instanceof SchemaEditor, "not a SchemaEditor");
		Assert.isTrue(selection instanceof IStructuredSelection,
	    			  "not a IStructuredSelection");
	    Object input = ((IStructuredSelection) selection).getFirstElement();        
	
	    Object modelObject = ((EditPart) input).getModel();
	    target = getTarget(modelObject);
	    
	    // we need the editor's command stack to change the model data
	    commandStack = ((SchemaEditor) part).getCommandStack();
	    propertyEditor.setCommandStack(commandStack);
	    commandStack.addCommandStackEventListener(this);
	
	}

	@Override
	public void stackChanged(CommandStackEvent event) {
		// by listening for command stack events we are able to refresh the
		// whole property sheet page; this seems to be the most practical
		// approach in keeping all tabs and sections in sync with the model
		if (event.isPostChangeEvent() &&
			(event.getDetail() == CommandStack.POST_EXECUTE ||
			 event.getDetail() == CommandStack.POST_REDO ||
			 event.getDetail() == CommandStack.POST_UNDO)) {
							
			// we need to execute this asynchronously to avoid Widget' is 
			// disposed' errors (don't ask why ;-)):
			Display.getCurrent().asyncExec(new Runnable() {
				@Override
				public void run() {
					// (simulate a) clear (of) the current selection
					page.selectionChanged(getPart(), new StructuredSelection());
					// set the selection again
					page.selectionChanged(getPart(), selection);					
				}				
			});
		}
	} 	

}