package org.lh.dmlj.schema.editor.property;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * An abstract superclass for sections in the tabbed properties view that can be
 * used to show a fixed and read-only property value.<br><br>
 * Subclasses must supply the label and constant value during construction and, 
 * if they want a description to be shown, they should override the 
 * getDescription method.
 */
public abstract class AbstractConstantValueSection 
	extends AbstractPropertySection {	
	
	private static final int CUSTOM_LABEL_WIDTH = 
		AbstractStructuralFeatureSection.CUSTOM_LABEL_WIDTH;
	
	private Text labelText;
	
	private String label;
	private String value;
	
	public AbstractConstantValueSection(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}

	@Override
	public final void createControls(Composite parent, 
							   		 TabbedPropertySheetPage aTabbedPropertySheetPage) {
		
		super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = 
        	getWidgetFactory().createFlatFormComposite(parent);
        FormData data;
   
        labelText = getWidgetFactory().createText(composite, "");
        labelText.setEditable(false);
        String toolTipText = getDescription();
        if (toolTipText != null) {
        	labelText.setToolTipText(toolTipText);
        }
        data = new FormData();
        data.left = new FormAttachment(0, CUSTOM_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        labelText.setLayoutData(data);         
   
        CLabel labelLabel = getWidgetFactory().createCLabel(composite, label);
        if (toolTipText != null) {
        	labelLabel.setToolTipText(toolTipText);
        }
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = 
        	new FormAttachment(labelText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(labelText, 0, SWT.CENTER);
        labelLabel.setLayoutData(data);
        
	}
	
	/**
	 * Sets the description to be shown for the attribute.
	 * @return the description for the attribute
	 */
	protected String getDescription() {
		return null;
	}	

	@Override
	public final void refresh() {
		labelText.setText(value);
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
	}	
	
}