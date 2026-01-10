/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.importtool.elements.diagram;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class RecordSelectionPage extends AbstractDataEntryPage {
	private List listRecords;
	private java.util.List<SchemaRecord> modelRecords = new ArrayList<>();
	
	@Override
	public void aboutToShow() {
		populateRecordList();
		validate();
	}
	
	@Override
	public Control createControl(Composite parent) {
		var container = new Composite(parent, SWT.NONE);
		var layout = new GridLayout(2, false);
		container.setLayout(layout);
		
		var lblRecords = new Label(container, SWT.NONE);
		lblRecords.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblRecords.setText("Records:");
		
		listRecords = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		listRecords.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
		var gdListRecords = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gdListRecords.heightHint = 300;
		listRecords.setLayoutData(gdListRecords);
		
		return container;
	}

	private void populateRecordList() {
		listRecords.removeAll();
		modelRecords.clear();
		Schema schema = getContext().getAttribute(IDataEntryContext.SCHEMA);
		modelRecords.addAll(schema.getRecords());
		Collections.sort(modelRecords);
		SchemaRecord currentRecord = getContext().getAttribute(IDataEntryContext.CURRENT_SCHEMA_RECORD);
		for (var schemaRecord : modelRecords) {
			var entryText = new StringBuilder();
			if (schemaRecord == currentRecord) {
				entryText.append("*"); 
			}
			entryText.append(Tools.removeTrailingUnderscore(schemaRecord.getName()));
			listRecords.add(entryText.toString());
		}
	}

	protected void validate() {
		var pageComplete = true;
		getController().setErrorMessage(null);
		
		if (listRecords.getSelectionIndex() > -1) {
			var schemaRecord = modelRecords.get(listRecords.getSelectionIndex());
			if (schemaRecord != getContext().getAttribute(IDataEntryContext.CURRENT_SCHEMA_RECORD)) {
				getContext().setAttribute(IDataEntryContext.RECORD, schemaRecord);
			} else {
				getController().setErrorMessage("You must select a record other than the current selection");
				pageComplete = false;
			}
		} else {
			pageComplete = false;
		}
		
		getController().setPageComplete(pageComplete);
	}

}
