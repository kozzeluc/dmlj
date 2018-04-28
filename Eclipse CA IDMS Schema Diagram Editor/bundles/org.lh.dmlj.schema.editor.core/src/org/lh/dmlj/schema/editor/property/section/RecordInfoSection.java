/**
 * Copyright (C) 2018  Luc Hermans
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

import java.io.IOException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.dictguide.DictguidesRegistry;
import org.lh.dmlj.schema.editor.log.Logger;
import org.lh.dmlj.schema.editor.property.RecordInfoValueObject;
import org.lh.dmlj.schema.editor.template.RecordInfoTemplate;

public class RecordInfoSection extends AbstractPropertiesSection {
	
	private static RecordInfoTemplate TEMPLATE = new RecordInfoTemplate();
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
	
	private Browser 		browser;
	private Control 		control;
	
	private ControlListener controlListener = new ControlAdapter() {			
		@Override
		public void controlResized(ControlEvent e) {
			Rectangle bounds = control.getBounds();
			browser.setBounds(0, 0, 0, bounds.height);
		}			
	};
		
	public RecordInfoSection() {
		super();		
	}
	
	@Override
	public final void createControls(Composite parent, 
							   		 TabbedPropertySheetPage aTabbedPropertySheetPage) {
		
		// We'll keep the browser width and height in sync with the tabbed
		// property sheet page; it seems that when we set the width to zero that
		// it always stretches horizontally, which is what we need; we need to
		// take care of the heigth though.
		
		super.createControls(parent, aTabbedPropertySheetPage);        		
		
		control = aTabbedPropertySheetPage.getControl();
		
		browser = new Browser(parent, SWT.NONE);
		Rectangle bounds = control.getBounds();
		browser.setBounds(0, 0, 0, bounds.height);
		
		// whenever the container control changes size, adjust the browser size
		// as well...
		control.addControlListener(controlListener);
        
	}
	
	@Override
	public void dispose() {
		if (control != null && !control.isDisposed()) {
			control.removeControlListener(controlListener);
		}
		super.dispose();
	}
	
	@Override
	public final void refresh() {
		SchemaRecord record = (SchemaRecord) modelObject;
		RecordInfoValueObject valueObject = null;
		try {
			valueObject = 
				DictguidesRegistry.INSTANCE
							  	  .getRecordInfoValueObject(record.getName());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		if (valueObject == null) {
			browser.setText("");
			return;
		}		
		String html = TEMPLATE.generate(valueObject);		
		browser.setText(html);		
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {
		
		super.setInput(part, selection);		
		
        Assert.isTrue(modelObject instanceof SchemaRecord, "not a SchemaRecord");
                
	}
	
}
