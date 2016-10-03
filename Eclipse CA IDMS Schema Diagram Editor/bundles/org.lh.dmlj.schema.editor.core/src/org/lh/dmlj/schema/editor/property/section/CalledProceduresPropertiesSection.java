/**
 * Copyright (C) 2016  Luc Hermans
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

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.property.ui.EditProcedureCallsDialog;
import org.lh.dmlj.schema.editor.property.ui.IDslFacetModifier;
                      
/**
 * A properties section that shows the procedures being called for either an area or a record.
 * Apart from each procedure's name, the time when it is called is listed.
 * @author Luc Hermans
 */
public class CalledProceduresPropertiesSection extends AbstractPropertiesSection {	

	private Table		 table;
	private SchemaArea 	 targetArea;	// either an area...
	private SchemaRecord targetRecord;  // or a record are set, not both
	
	private CommandStack commandStack;
	private Link link;
		       
	public CalledProceduresPropertiesSection() {
		super();
	}	

	/**
	 * @wbp.parser.entryPoint
	 */
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
		column2.setWidth(200);
		column2.setText("Called");		
		
		link = new Link(composite, SWT.NONE);
		link.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));		
		GridData gd_link = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_link.horizontalIndent = 5;
		link.setLayoutData(gd_link);
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editProcedureCalls();
			}
		});
		link.setText("<a>Add, edit or remove a procedure call</a>");
	}		

	protected void editProcedureCalls() {
		
		Shell parentShell = getPart().getSite().getShell();
		
		IDslFacetModifier dslFacetModifier; 
		if (targetArea != null) {
			dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(targetArea);
		} else {
			dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(targetRecord);
		}
		
		EditProcedureCallsDialog dialog = new EditProcedureCallsDialog(parentShell, dslFacetModifier);
		if (dialog.open() == IDialogConstants.CANCEL_ID || !dslFacetModifier.hasChanges()) {			
			return;
		}
		
		commandStack.execute(dslFacetModifier.getCommand());
	}

	@Override
	public final void refresh() {		
	
		// remove all table rows
		table.removeAll();		
		
		// (re-)populate the table		
		if (targetArea != null) {
			// list the procedures called for the target area
			for (AreaProcedureCallSpecification callSpec : targetArea.getProcedures() ) {				
				TableItem item = new TableItem(table, SWT.NONE);			
				item.setText(0, callSpec.getProcedure().getName());
				item.setText(1, callSpec.getCallTime() + " " + 
							 callSpec.getFunction().toString().replaceAll("_", " "));
			}
		} else {
			// list the procedures called for the target record
			for (RecordProcedureCallSpecification callSpec : targetRecord.getProcedures() ) {				
				TableItem item = new TableItem(table, SWT.NONE);			
				item.setText(0, callSpec.getProcedure().getName());
				item.setText(1, callSpec.getCallTime() + " " + 
							 callSpec.getVerb().toString().replaceAll("_", " "));
			}
		}
	
		// we don't want any vertical scrollbar in the table; the following
		// sequence allows us to do just that (i.e. vertically stretch the table
		// as needed)          
		for (Composite parent = table.getParent(); parent != null; 
			 parent = parent.getParent()) {                      
		     
			parent.layout();                                          
		}
		
		link.setVisible(!isReadOnlyMode());
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {

		super.setInput(part, selection);
		
		Assert.isTrue(modelObject instanceof SchemaArea || modelObject instanceof SchemaRecord, 
					  "not a SchemaArea nor a SchemaRecord");
        if (modelObject instanceof SchemaArea) {
        	targetArea = (SchemaArea) modelObject;
        	targetRecord = null;
        } else {
        	targetArea = null;
        	targetRecord = (SchemaRecord) modelObject;
        }
        
        commandStack = (CommandStack) editor.getAdapter(CommandStack.class);
	    Assert.isNotNull(commandStack, "no command stack available");
	}	

}
