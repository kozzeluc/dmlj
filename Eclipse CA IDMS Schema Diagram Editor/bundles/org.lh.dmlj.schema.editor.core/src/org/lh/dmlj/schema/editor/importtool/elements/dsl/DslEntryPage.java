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
package org.lh.dmlj.schema.editor.importtool.elements.dsl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava;
import org.lh.dmlj.schema.editor.importtool.AbstractDataEntryPage;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class DslEntryPage extends AbstractDataEntryPage {
	
	private Text textDsl;

	public DslEntryPage() {
		super();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public Control createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		Layout layout = new GridLayout(2, false);
		container.setLayout(layout);
		
		textDsl = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textDsl.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				invalidate();
			}
		});
		textDsl.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL));
		GridData gd_textDsl = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
		gd_textDsl.heightHint = 200;
		textDsl.setLayoutData(gd_textDsl);
		
		Button btnValidate = new Button(container, SWT.NONE);
		btnValidate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
		btnValidate.setText("Validate");
		
		Button btnReset = new Button(container, SWT.NONE);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reset();
			}
		});
		btnReset.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnReset.setText("Reset");
		
		initialize();
		validate();
		getController().setPageComplete(false);
		
		return container;
	}
	
	private void initialize() {
		SchemaRecord record = getContext().getAttribute(IDataEntryContext.CURRENT_SCHEMA_RECORD);
		textDsl.setText(Tools.generateRecordElementsDSL(record));
	}
	
	private void invalidate() {
		getController().setPageComplete(false);
	}

	private void reset() {
		initialize();
		invalidate();
	}
	
	private void validate() {
		
		boolean pageComplete = true;
		getController().setErrorMessage(null);
		
		try {
			StringBuilder recordDsl = new StringBuilder();
			recordDsl.append("elements \"\"\"\n");
			recordDsl.append(textDsl.getText());			
			recordDsl.append("\n\"\"\"");
			SchemaRecord dummyRecord = ModelFromDslBuilderForJava.record(recordDsl.toString());
			getContext().setAttribute(IDataEntryContext.RECORD, dummyRecord);
		} catch (Throwable t) {
			getController().setErrorMessage(t.getMessage());
			pageComplete = false;
		}
		
		getController().setPageComplete(pageComplete);
	}

}
