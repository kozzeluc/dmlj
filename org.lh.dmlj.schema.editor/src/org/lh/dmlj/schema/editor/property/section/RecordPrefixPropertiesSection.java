/**
 * Copyright (C) 2013  Luc Hermans
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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.Prefix;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
                      
public class RecordPrefixPropertiesSection extends AbstractPropertiesSection {	

	private Table		   table;
	protected SchemaRecord target;

	public RecordPrefixPropertiesSection() {
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

		// create the first table column, holding the pointer position
		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setWidth(50);
		column1.setText("Pos.");		
		
		// create the second table column, holding the set to which the pointer
		// applies
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setWidth(125);
		column2.setText("Set");
		
		// create the third table column, holding the pointer type
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setWidth(60);
		column3.setText("Role");
		
		// create the third table column, holding the pointer type
		TableColumn column4 = new TableColumn(table, SWT.NONE);
		column4.setWidth(60);
		column4.setText("Pointer");	

	}		

	@Override
	public final void refresh() {		
	
		// remove all table rows
		table.removeAll();		
		
		// (re-)populate the table
		Prefix prefix = PrefixFactory.newPrefixForInquiry(target);
		for (Pointer<?> pointer : prefix.getPointers()) {												
			TableItem item = new TableItem(table, SWT.NONE);			
			item.setText(0, String.valueOf(pointer.getCurrentPositionInPrefix()));			
			item.setText(1, pointer.getSetName());						
			if (pointer.isOwnerDefined()) {
				item.setText(2, "owner");
			} else {
				item.setText(2, "member");
			}
			String pointerTypeAsString = pointer.getType().toString();
			item.setText(3, pointerTypeAsString.substring(pointerTypeAsString.indexOf("_") + 1));
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
	public void setInput(IWorkbenchPart part, ISelection selection) {		
		super.setInput(part, selection);
		target = (SchemaRecord) modelObject;
	}

}
