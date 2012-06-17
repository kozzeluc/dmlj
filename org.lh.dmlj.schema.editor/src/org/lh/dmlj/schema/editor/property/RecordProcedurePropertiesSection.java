package org.lh.dmlj.schema.editor.property;        

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
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
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.SchemaEditor;
                      
public class RecordProcedurePropertiesSection 
	extends AbstractPropertySection {	

	private Table		   table;
	protected SchemaRecord target;
		       
	public RecordProcedurePropertiesSection() {
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

		// create the first table column, holding the procedure names
		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setWidth(100);
		column1.setText("Procedure");

		// create the second table column, holding the procedure call times and
		// verbs
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setWidth(150);
		column2.setText("Called");		

	}		

	@Override
	public final void refresh() {		
	
		// remove all table rows
		table.removeAll();		
		
		// (re-)populate the table		
		for (RecordProcedureCallSpecification callSpec : target.getProcedures() ) {
			
			TableItem item = new TableItem(table, SWT.NONE);			
			item.setText(0, callSpec.getProcedure().getName());
			item.setText(1, callSpec.getCallTime() + " " + callSpec.getVerb());
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

		super.setInput(part, selection);

		// we can only work with SchemaEditor instances
		Assert.isTrue(part instanceof SchemaEditor, "not a SchemaEditor");
		Assert.isTrue(selection instanceof IStructuredSelection,
        			  "not a IStructuredSelection");
        Object input = ((IStructuredSelection) selection).getFirstElement();        

        Object modelObject = ((EditPart) input).getModel();
        target = (SchemaRecord) modelObject;

	}	

}