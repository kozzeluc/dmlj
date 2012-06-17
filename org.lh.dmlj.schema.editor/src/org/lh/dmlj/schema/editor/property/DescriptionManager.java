package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * A description manager for a property section of type 
 * AbstractPropertiesSection<T>.<br><br>  
 * An instance of this class hooks itself as a selection listener to the 
 * property table of the section and will show the description, if any, for the 
 * selected property (feature).  The descriptions are provided by 
 * implementations of AbstractPropertiesSection<T> and made available through 
 * the getDescription(EStructuralFeature feature) method. 
 */
public class DescriptionManager extends SelectionAdapter {

	private AbstractPropertiesSection<?> section;
	private IStatusLineManager 		 	 statusLineManager;
	
	public DescriptionManager(TabbedPropertySheetPage page, 
							  AbstractPropertiesSection<?> section, 
							  Table table) {
		super();
		this.section = section;
		statusLineManager = 
			page.getSite().getActionBars().getStatusLineManager();
		table.addSelectionListener(this);
	}
	
	/**
	 * Clears whatever description is shown.
	 */
	public void clear() {
		setMessage(null);
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
		EStructuralFeature feature = section.getFeatures().get(i);
		String description = section.getDescription(feature);
		if (description != null) {
			setMessage(description);
		}
		
	}
	
}