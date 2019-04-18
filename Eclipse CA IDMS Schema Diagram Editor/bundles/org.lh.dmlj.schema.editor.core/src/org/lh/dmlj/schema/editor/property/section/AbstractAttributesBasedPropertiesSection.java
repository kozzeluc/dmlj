/**
 * Copyright (C) 2019  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.property.section;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.PluginPropertiesCache;
import org.lh.dmlj.schema.editor.property.filter.IEnumFilter;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;
import org.lh.dmlj.schema.editor.property.handler.StandardEditHandler;

public abstract class AbstractAttributesBasedPropertiesSection<T extends EObject>
	extends AbstractPropertiesSection {
	
	private List<EAttribute>    	attributes = new ArrayList<>();
	private DescriptionManager  	descriptionManager;
	private Plugin 					plugin;
	private PropertyEditor	    	propertyEditor;
	private Table			    	table;
	protected T	   			    	target;
	private Link bottomLink;	
	
	public AbstractAttributesBasedPropertiesSection(Plugin plugin) {
		super();
		this.plugin = plugin;
	}
	
	@Override
	public final void createControls(Composite parent, TabbedPropertySheetPage page) {
	
		super.createControls(parent, page); // this will set the page field
		
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
		column2.setWidth(300);
		column2.setText("Value");
		
		String linkText = getBottomHyperlinkText();
		if (linkText != null && !linkText.trim().isEmpty()) {
			bottomLink = new Link(composite, SWT.NONE);
			bottomLink.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));		
			GridData gd_link = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_link.horizontalIndent = 5;
			bottomLink.setLayoutData(gd_link);
			bottomLink.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Command command = getHyperlinkHandler(null).hyperlinkActivated(null);
					if (command != null) {
						CommandStack commandStack = (CommandStack) editor.getAdapter(CommandStack.class);
						commandStack.execute(command);
						refresh();
					}
				}
			});
			bottomLink.setText("<a>" + linkText + "</a>");
		}
		
		// add the description manager
		descriptionManager = new DescriptionManager(page, this, table);		
		
		// add the property editor
		propertyEditor = new PropertyEditor(page, this, table);		
	
	}
	
	@Override
	public void dispose() {
		if (descriptionManager != null) {
			descriptionManager.dispose();
		}
		if (propertyEditor != null) {
			propertyEditor.dispose();
		}
		super.dispose();
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

	public abstract List<EAttribute> getAttributes();	

	/**
	 * Subclasses should override this method if a hyperlink is to be shown below the table containing
	 * the attributes.  This hyperlink is NOT related to any of the attributes in the property section
	 * and can be used to produce a command to change the model (beware that the editor can be open in
	 * read-only mode; the bottom hyperlink will NEVER be shown when in read-only mode).<br><br>
	 * Note that the <i>target</i> model object will NOT be available when this method is called, so
	 * it is advised to return a fixed string.
	 * @return the bottom hyperlink text or null if no bottom hyperlink is to be shown
	 */
	protected String getBottomHyperlinkText() {
		return null;
	}

	public String getDescription(EAttribute attribute) {
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
	public EObject getEditableObject(EAttribute attribute) {
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
	public IEditHandler getEditHandler(EAttribute attribute, Object newValue) {		
		
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
	public IEnumFilter<? extends Enum<?>> getEnumFilter(EAttribute attribute) {
		return null;
	}
	
	/**
	 * Subclasses should override this method if a hyperlink has to be created
	 * when the mouse pointer hovers above the attribute value.  
	 * This method is called multiple times.
	 * @param attribute the attribute or null for the bottom hyperlink handler
	 * @return 
	 */
	public IHyperlinkHandler<EAttribute, Command> getHyperlinkHandler(EAttribute attribute) {
		return null;
	}

	public String getLabel(EAttribute attribute) {		
		String key = "label." + attribute.getContainerClass().getName() + "." + attribute.getName();		
		String label = getPluginProperty(key);		
		if (label != null) {
			return label;
		} else {
			return attribute.getName();
		}
	}	
	
	protected String getPluginProperty(String key) {
		return PluginPropertiesCache.get(plugin, key);
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
		if (value != null && !value.toString().isEmpty()) {
			// if the attribute is an enum, replace all underscores by spaces			
			if (value instanceof Enumerator) {
				return value.toString().replaceAll("_", " ");
			} else {
				return value.toString();
			}
		} else {
			if (!isReadOnlyMode() && getHyperlinkHandler(attribute) != null) {
				return "[...]";
			} else {
				return "";
			}
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
				// cell's foreground color to blue... don't show hyperlinks when
				// in read-only mode
				if (!isReadOnlyMode() && getHyperlinkHandler(attribute) != null) {
					if (Plugin.getDefault().isDarkThemeActive()) {
						item.setForeground(1, SWTResourceManager.getColor(131, 196, 234));
					} else {
						item.setForeground(1, ColorConstants.blue);
					}
				}				
				item.setText(1, value);				
			} else {
				item.setText(1, "");
			}
			
		}
	
		// we don't want any vertical scrollbar in the table; the following
		// sequence allows us to do just that (i.e. vertically stretch the table
		// as needed)          
		for (Composite parent = table.getParent(); parent != null; parent = parent.getParent()) {                      		     
			parent.layout();                                          
		}
	
		if (bottomLink != null) {
			bottomLink.setVisible(!isReadOnlyMode());
		}
	}	

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {
		
		super.setInput(part, selection);
		
		// get the target model object; this is not always the same as the current selection edit
		// part's model object
	    target = getTarget(modelObject);
	    
	    // get the command stack from the editor and pass it to the property editor (all model 
	    // changes are carried out via commands that are put and executed on the command stack);
	    // always set the command stack, even when in read-only mode (the command stack shouldn't be
	    // used)
	    CommandStack commandStack = (CommandStack) editor.getAdapter(CommandStack.class);
	    Assert.isNotNull(commandStack, "no command stack available");
	    propertyEditor.setCommandStack(commandStack);
	
	} 	

}
