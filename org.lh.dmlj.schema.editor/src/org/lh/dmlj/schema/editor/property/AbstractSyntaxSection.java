package org.lh.dmlj.schema.editor.property;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
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
 * An abstract superclass for sections in the tabbed properties that show the 
 * DDL for a given object (record, set, ...).  Subclasses must supply the valid
 * edit part model object types and a template during construction and must
 * override method getTemplateObject if the template object is different from 
 * the edit part model object.
 */
public abstract class AbstractSyntaxSection extends AbstractPropertySection {
	
	private Method 		generateMethod;
	private StyledText  styledText;
	private Object		template;
	private EObject 	templateObject;
	private Class<?>[]  validEditPartModelObjectTypes;	
		
	protected AbstractSyntaxSection(Class<?>[] validEditPartModelObjectTypes,
									Object template) {
		super();
		this.validEditPartModelObjectTypes = validEditPartModelObjectTypes;
		this.template = template;
		try {
			generateMethod = 
				template.getClass().getDeclaredMethod("generate", Object.class);
		} catch (Throwable t) {			
			t.printStackTrace();
			throw new Error(t);
		}
	}
	
	@Override
	public final void createControls(Composite parent, 
							   		 TabbedPropertySheetPage aTabbedPropertySheetPage) {
				
		super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = 
        	getWidgetFactory().createFlatFormComposite(parent);

		styledText = new StyledText(composite, SWT.MULTI | SWT.READ_ONLY);		
		
		styledText.setFont(Plugin.getDefault().getSyntaxFont());
				
		FormData data = new FormData();	
		data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		styledText.setLayoutData(data);
        
	}	

	protected EObject getTemplateObject(Object editPartModelObject) {
		if (editPartModelObject instanceof EObject) {
			return (EObject) editPartModelObject;
		}
		throw new Error("no template object");
	}	
	
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
		String syntax;
		try {
			syntax = (String) generateMethod.invoke(template, templateObject);
		} catch (Throwable t) {
			t.printStackTrace();
			syntax = "an error occurred while generating the DDL: " + 
					 t.getClass().getName() + "(" + t.getMessage() + ")";
		}
		styledText.setText(syntax);			
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {
		
		super.setInput(part, selection);
        
		Assert.isTrue(part instanceof SchemaEditor, "not a SchemaEditor");		
		
		Assert.isTrue(selection instanceof IStructuredSelection,
        			  "not a IStructuredSelection");
        Object input = ((IStructuredSelection) selection).getFirstElement();        
        
        Assert.isTrue(input instanceof EditPart, "not an EditPart");
        Object editPartModelObject = ((EditPart) input).getModel();
        Assert.isTrue(isValidType(editPartModelObject), 
  			  		  "not a " + Arrays.asList(validEditPartModelObjectTypes));
        templateObject = getTemplateObject(editPartModelObject);        
                
	}	
	
}