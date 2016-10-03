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

public class PreviewPage extends WizardPage {
	
	private String recordElementsDSL;
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
	
	public void setRecordElementsDSL(String recordElementsDSL) {
		this.recordElementsDSL = recordElementsDSL;
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			textDSL.setText(recordElementsDSL);
		}
		super.setVisible(visible);
	}

}
