package org.lh.dmlj.schema.editor.property;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.SetStringAttributeCommand;

public class ConnectorLabelSection extends AbstractPropertySection {	

	private static final EAttribute ATTRIBUTE = 
		SchemaPackage.eINSTANCE.getConnector_Label();
	private static final String 	DESCRIPTION = 
		"A text to be shown in this set's connectors";
	
	private Connector 		  connector;
	private Text 	   		  labelText;
	private CustomKeyListener listener;
	
	public ConnectorLabelSection() {
		super();
		listener = new CustomKeyListener();
	}	
	
	private String capitalize(String name) {
		if (name.length() == 0) {
			return name;
		} else if (name.length() == 1) {
			return name.toUpperCase();
		} else {
			return name.substring(0, 1).toUpperCase() + name.substring(1);
		}	
	}

	@Override
	public final void createControls(Composite parent, 
							   		 TabbedPropertySheetPage aTabbedPropertySheetPage) {
		
		super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = 
        	getWidgetFactory().createFlatFormComposite(parent);
        FormData data;
   
        labelText = getWidgetFactory().createText(composite, "");
        labelText.setEditable(listener != null);        
        labelText.setToolTipText(DESCRIPTION);
        data = new FormData();
        data.left = 
        	new FormAttachment(0, AbstractStructuralFeatureSection.CUSTOM_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        labelText.setLayoutData(data); 
        if (listener != null) {
        	labelText.addKeyListener(listener);
        }
   
        CLabel labelLabel = 
        	getWidgetFactory().createCLabel(composite, 
        									capitalize(ATTRIBUTE.getName()) + 
        									":");
        labelLabel.setToolTipText(DESCRIPTION);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = 
        	new FormAttachment(labelText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(labelText, 0, SWT.CENTER);
        labelLabel.setLayoutData(data);
        
	}

	@Override
	public final void refresh() {		
		labelText.removeKeyListener(listener);		
		Object value = connector.eGet(ATTRIBUTE);
		if (value != null) {
			labelText.setText((String) value);
		} else {
			labelText.setText("");
		}
		labelText.addKeyListener(listener);
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {
		
		super.setInput(part, selection);
        
		Assert.isTrue(part instanceof SchemaEditor, "not a SchemaEditor");
		SchemaEditor editor = (SchemaEditor) part;
		
		Assert.isTrue(selection instanceof IStructuredSelection,
        			  "not a IStructuredSelection");
        Object input = ((IStructuredSelection) selection).getFirstElement();        
        
        Assert.isTrue(input instanceof EditPart, "not an EditPart");
        Object editPartModelObject = ((EditPart) input).getModel();
        
        connector = null; 
        if (editPartModelObject instanceof ConnectionPart) {
        	ConnectionPart connectionPart = 
        		(ConnectionPart) editPartModelObject;
        	connector = connectionPart.getConnector();
        } else if (editPartModelObject instanceof ConnectionLabel) {
        	ConnectionLabel connectionLabel = 
            	(ConnectionLabel) editPartModelObject;
            connector = connectionLabel.getMemberRole()
            						   .getConnectionParts()
            						   .get(0)
            						   .getConnector();
        } else if (editPartModelObject instanceof Connector) {        	
        	connector = (Connector) editPartModelObject;
        } else if (editPartModelObject instanceof SystemOwner) {
        	SystemOwner systemOwner = (SystemOwner) editPartModelObject;
        	connector = systemOwner.getSet()
        						   .getMembers()
        						   .get(0)
        						   .getConnectionParts()
        						   .get(0)
        						   .getConnector();
        }
        Assert.isTrue(connector != null, "no connector");
                
        listener.setCommandStack(editor.getCommandStack());
        listener.setConnector(connector);
	}
	
    private static class CustomKeyListener extends KeyAdapter {
    	    	
    	private CommandStack commandStack;
    	private Connector 	 connector;
    	
    	CustomKeyListener() {
    		super();    		
    	}

		@Override
		public void keyReleased(KeyEvent e) {
			Text labelText = (Text) e.getSource();
			if (e.keyCode == 13 || e.keyCode == 16777296) { // enter-keys				
				
				// we need to set the label on the second connector as well, so
				// go grab it (it should always be there)
				Connector otherConnector = null;
				for (ConnectionPart connectionPart: 
					 connector.getConnectionPart()
					 		  .getMemberRole()
					 		  .getConnectionParts()) {
					
					Connector aConnector = connectionPart.getConnector();
					if (aConnector != connector) {
						otherConnector = aConnector;
						break;
					}
				}				
				
				// chain 2 SetStringAttributeCommands to set the new label value
				// in both connectors and execute the compound command
				SetStringAttributeCommand command1 = 
					new SetStringAttributeCommand(connector, ATTRIBUTE,
										  	      labelText.getText().trim());
				SetStringAttributeCommand command2 = 
					new SetStringAttributeCommand(otherConnector, ATTRIBUTE,
										  	      labelText.getText().trim());
				commandStack.execute(command1.chain(command2));
			}
		}
		
		void setCommandStack(CommandStack commandStack) {
			this.commandStack = commandStack;
		}
		
		void setConnector(Connector connector) {
			this.connector = connector;
		}
        
    };	
	
}