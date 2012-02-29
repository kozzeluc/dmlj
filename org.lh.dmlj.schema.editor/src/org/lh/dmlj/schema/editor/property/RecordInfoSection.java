package org.lh.dmlj.schema.editor.property;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.template.RecordInfoTemplate;

public class RecordInfoSection extends AbstractPropertySection {
	
	private static RecordInfoTemplate TEMPLATE = new RecordInfoTemplate();
	
	private Object  editPartModelObject;
	private Browser browser;
		
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
		
		final Control control = aTabbedPropertySheetPage.getControl();
		
		browser = new Browser(parent, SWT.NONE);
		Rectangle bounds = control.getBounds();
		browser.setBounds(0, 0, 0, bounds.height);
		
		// whenever the container control changes size, adjust the browser size
		// as well...
		control.addControlListener(new ControlAdapter() {			
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle bounds = control.getBounds();
				browser.setBounds(0, 0, 0, bounds.height);
			}			
		});
        
	}
	
	@Override
	public final void refresh() {
		SchemaRecord record = (SchemaRecord) editPartModelObject;
		RecordInfoValueObject valueObject =
			Plugin.getDefault().getRecordInfoValueObject(record.getName());
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
        
		Assert.isTrue(part instanceof SchemaEditor, "not a SchemaEditor");		
		
		Assert.isTrue(selection instanceof IStructuredSelection,
        			  "not a IStructuredSelection");
        Object input = ((IStructuredSelection) selection).getFirstElement();        
        
        Assert.isTrue(input instanceof EditPart, "not an EditPart");
        editPartModelObject = ((EditPart) input).getModel();
        Assert.isTrue(editPartModelObject instanceof SchemaRecord, 
  			  		  "not a SchemaRecord");
                
	}
	
}