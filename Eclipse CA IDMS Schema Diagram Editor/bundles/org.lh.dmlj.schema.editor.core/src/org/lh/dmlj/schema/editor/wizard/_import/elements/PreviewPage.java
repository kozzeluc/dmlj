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
package org.lh.dmlj.schema.editor.wizard._import.elements;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.dsl.builder.model.RecordModelBuilder;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.RecordSyntaxBuilder;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class PreviewPage extends WizardPage {
	
	private RecordElementsImportToolProxy proxy;
	private IDataEntryContext dataEntryContext;
	private Text textDSL;

	public PreviewPage(SchemaRecord record) {
		super("previewPage");
		setTitle("Elements for Record " + record.getName());
		setDescription("Review the record elements DSL");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		textDSL = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textDSL.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL));
		textDSL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		setPageComplete(true);
	}
	
	public void setImportToolProxy(RecordElementsImportToolProxy proxy) {
		this.proxy = proxy;
	}
	
	public void setContext(IDataEntryContext dataEntryContext) {
		this.dataEntryContext = dataEntryContext;
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			SchemaRecord record = new RecordModelBuilder().build("DUMMY");
			record.getRootElements().clear();
			record.getRootElements().addAll(proxy.invokeImportTool(dataEntryContext));
			String dsl = new RecordSyntaxBuilder().build(record);
			int i = dsl.indexOf("\"\"\"\n");
			int j = dsl.lastIndexOf("\n\"\"\"");
			textDSL.setText(dsl.substring(i + 3, j).replace("\n    ", "\n").substring(1));
		}
		super.setVisible(visible);
	}

}
