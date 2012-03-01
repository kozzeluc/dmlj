package org.lh.dmlj.schema.editor.property;

import java.util.Arrays;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.SchemaEditor;

/**
 * An abstract superclass for sections in the tabbed properties view that show  
 * their information in a single styled text control.  Subclasses must supply  
 * the valid edit part model object types during construction and can override
 * the getFont method if they want another font than the standard 'syntax' font
 * to be used.
 */
public abstract class AbstractSectionWithStyledText 
	extends AbstractPropertySection {
	
	private Object		editPartModelObject;
	private StyledText	styledText;
	private Class<?>[]	validEditPartModelObjectTypes;	
		
	protected AbstractSectionWithStyledText(Class<?>[] validEditPartModelObjectTypes) {
		super();
		this.validEditPartModelObjectTypes = validEditPartModelObjectTypes;		
	}
	
	@Override
	public final void createControls(Composite parent, 
							   		 TabbedPropertySheetPage aTabbedPropertySheetPage) {
				
		super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = 
        	getWidgetFactory().createFlatFormComposite(parent);

		styledText = new StyledText(composite, SWT.MULTI | SWT.READ_ONLY);				
		styledText.setFont(getFont());
				
		FormData data = new FormData();	
		data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		styledText.setLayoutData(data);
		
		// if we ever want to add 'dynamic tooltips', a MouseTrackListener is
		// way to go...
		/*MouseTrackListener mouseTrackListener = getMouseTrackListener();
		if (mouseTrackListener != null) {
			styledText.addMouseTrackListener(mouseTrackListener);
		}*/
        
	}
	
	protected Font getFont() {
		return Plugin.getDefault().getSyntaxFont();
	}

	protected abstract String getValue(Object editPartModelObject);

	private boolean isValidType(Object object) {
		for (Class<?> _class : validEditPartModelObjectTypes) {
			if (_class.isInstance(object)) {
				return true;
			}
		}
		return false;
	}	
	
	@Override
	public final void refresh() {
		styledText.setText(getValue(editPartModelObject));			
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
        Assert.isTrue(isValidType(editPartModelObject), 
  			  		  "not a " + Arrays.asList(validEditPartModelObjectTypes));
                
	}	
	
}