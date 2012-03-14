package org.lh.dmlj.schema.editor.property;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.SetBooleanAttributeCommand;
import org.lh.dmlj.schema.editor.part.SchemaEditPart;

public class SchemaShowGridSection extends AbstractPropertySection {	

	private static final EAttribute ATTRIBUTE = 
		SchemaPackage.eINSTANCE.getDiagramData_ShowGrid();
	private static final String 	DESCRIPTION = 
		"Indicates whether the diagram grid is visible or not";
		
	private Button 	   		  		checkbox;
	private DiagramData 	  		diagramData;
	private CustomSelectionListener	listener = new CustomSelectionListener();
	
	public SchemaShowGridSection() {
		super();		
	}	

	@Override
	public final void createControls(Composite parent, 
							   		 TabbedPropertySheetPage aTabbedPropertySheetPage) {
		
		super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = 
        	getWidgetFactory().createFlatFormComposite(parent);        
   
        checkbox = getWidgetFactory().createButton(composite, "Show grid", 
        										   SWT.CHECK);
        checkbox.setToolTipText(DESCRIPTION);
        FormData data = new FormData();
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        checkbox.setLayoutData(data);         
        checkbox.addSelectionListener(listener);                
        
	}

	@Override
	public final void refresh() {		
		checkbox.removeSelectionListener(listener);		
		Object value = diagramData.eGet(ATTRIBUTE);		
		checkbox.setSelection((boolean) value);		
		checkbox.addSelectionListener(listener);
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {
		
		super.setInput(part, selection);
        
		Assert.isTrue(part instanceof SchemaEditor, "not a SchemaEditor");
		SchemaEditor editor = (SchemaEditor) part;
		
		Assert.isTrue(selection instanceof IStructuredSelection,
        			  "not a IStructuredSelection");
        Object input = ((IStructuredSelection) selection).getFirstElement();        
        
        Assert.isTrue(input instanceof SchemaEditPart, "not a SchemaEditPart");
        Schema schema = ((SchemaEditPart) input).getModel();
        
        diagramData = schema.getDiagramData();         
        Assert.isTrue(diagramData != null, "no diagram data");
                
        listener.setCommandStack(editor.getCommandStack());
        listener.setDiagramData(diagramData);
	}
	
    private static class CustomSelectionListener extends SelectionAdapter {
    	    	
    	private CommandStack commandStack;
    	private DiagramData  diagramData;
    	
    	CustomSelectionListener() {
    		super();    		
    	}

    	@Override
    	public void widgetSelected(SelectionEvent e) {		
			Button checkbox = (Button) e.getSource();
			String label = checkbox.getSelection() ? "Show grid" : "Hide grid";
			SetBooleanAttributeCommand command = 
				new SetBooleanAttributeCommand(diagramData, ATTRIBUTE,
									  	       checkbox.getSelection(), label);			
			commandStack.execute(command);
		}
		
		void setCommandStack(CommandStack commandStack) {
			this.commandStack = commandStack;
		}
		
		void setDiagramData(DiagramData diagramData) {
			this.diagramData = diagramData;
		}
        
    };	
	
}