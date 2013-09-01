package org.lh.dmlj.schema.editor.property.section;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * A description manager for a property section of type AbstractAttributesBasedPropertiesSection<T>.
 * <br><br>  
 * An instance of this class hooks itself as a selection listener to the property table of the 
 * section and will show the description, if any, for the selected property (attribute).  The 
 * descriptions are provided by implementations of AbstractAttributesBasedPropertiesSection<T> and 
 * made available through the getDescription(EAttribute attribute) method. 
 */
public class DescriptionManager extends SelectionAdapter {

	private AbstractAttributesBasedPropertiesSection<?> section;
	private IStatusLineManager 		 	 				statusLineManager;
	private Table 										table;
	
	public DescriptionManager(TabbedPropertySheetPage page, 
							  AbstractAttributesBasedPropertiesSection<?> section, Table table) {
		super();
		this.section = section;
		this.table = table;
		statusLineManager = page.getSite().getActionBars().getStatusLineManager();
		table.addSelectionListener(this);
	}
	
	/**
	 * Clears whatever description is shown.
	 */
	public void clear() {
		setMessage(null);
	}
	
	public void dispose() {
		if (!table.isDisposed()) {
			table.removeSelectionListener(this);
		}
	}
	
	private void setMessage(String message) {
		statusLineManager.setErrorMessage(null);
		statusLineManager.setMessage(message);
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		
		// clear the message area
		clear();
		
		// get the Table instance
		Table table = (Table) e.getSource();
		
		// exit this method if no row is selected
		if (table.getSelectionCount() != 1) {					
			return;
		}
		
		// identify the selected row
		int i = table.getSelectionIndex();
		
		// set the property description if available
		EAttribute attribute = section.getAttributes().get(i);
		String description = section.getDescription(attribute);
		if (description != null) {
			setMessage(description);
		}
		
	}
	
}