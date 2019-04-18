/**
 * Copyright (C) 2019 Luc Hermans
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
import java.util.Optional;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.dictguide.DictguidesRegistry;
import org.lh.dmlj.schema.editor.log.Logger;
import org.lh.dmlj.schema.editor.property.RecordInfoValueObject;
import org.lh.dmlj.schema.editor.template.RecordInfoTemplate;

public class RecordInfoSection extends AbstractPropertiesSection {
		
	private static RecordInfoTemplate template = new RecordInfoTemplate();
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
		
	private Control pageControl;
	private Composite stackComposite;
	private StackLayout stackLayout;
	private Composite dataComposite;
	private Browser browser;
	private GridData gd_browser;
	private Label emptyLabel;
	
	private Optional<String> html;
	
	private ControlListener controlListener = new ControlAdapter() {			
		@Override
		public void controlResized(ControlEvent e) {
			stackComposite.setBounds(0, 0, pageControl.getBounds().width - 100, pageControl.getBounds().height);
			gd_browser.widthHint = pageControl.getBounds().width - 100;
			gd_browser.heightHint = pageControl.getBounds().height;
		}			
	};
		
	public RecordInfoSection() {
		super();		
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public final void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);        		
				
		pageControl = aTabbedPropertySheetPage.getControl();
		pageControl.addControlListener(controlListener);		
		
		stackComposite = new Composite(parent, SWT.NONE);
		stackComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		stackComposite.setBounds(0, 0, pageControl.getBounds().width - 100, pageControl.getBounds().height);
		stackLayout = new StackLayout();
		stackComposite.setLayout(stackLayout);
				
		dataComposite = new Composite(stackComposite, SWT.NONE);
		dataComposite.setLayout(new GridLayout(2, false));
		
		browser = new Browser(dataComposite, SWT.FULL_SELECTION);
		gd_browser = new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1);
		gd_browser.verticalIndent = 5;
		gd_browser.widthHint = pageControl.getBounds().width - 100;
		gd_browser.heightHint = pageControl.getBounds().height;
		browser.setLayoutData(gd_browser);
		
		emptyLabel = new Label(stackComposite, SWT.NO_FOCUS);
		stackLayout.topControl = emptyLabel;
		stackComposite.layout();
	}
	
	@Override
	public void dispose() {
		if (pageControl != null && !pageControl.isDisposed()) {
			pageControl.removeControlListener(controlListener);
		}
		super.dispose();
	}
	
	@Override
	public final void refresh() {
		if (html.isPresent()) {
			browser.setText(html.get());
			stackLayout.topControl = dataComposite;
		} else {
			stackLayout.topControl = emptyLabel;
		}
		stackComposite.layout();
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
        try {
        	String recordName = ((SchemaRecord) modelObject).getName();
        	Optional<RecordInfoValueObject> data = 
        		Optional.ofNullable(DictguidesRegistry.INSTANCE.getRecordInfoValueObject(recordName));
        	if (data.isPresent()) {
        		html = Optional.of(template.generate(data.get()));
        	} else {
        		html = Optional.empty();
        	}
        } catch (IOException e) {
			logger.error(e.getMessage(), e);
			html = Optional.empty();
		}
	}
	
}
