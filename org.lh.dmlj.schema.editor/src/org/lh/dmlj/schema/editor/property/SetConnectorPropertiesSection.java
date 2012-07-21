package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.SetStringAttributeCommand;

public class SetConnectorPropertiesSection extends AbstractSetPropertiesSection {

	public SetConnectorPropertiesSection() {
		super();
	}
	
	@Override
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getConnector_Label()) {
			return "A text to be shown in this set's connectors";
		}
		return super.getDescription(attribute);
	}
	
	@Override
	protected EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getConnector_Label()) {
			// although there will always be 2 connectors involved, capturing
			// the model changes from the first will do
			return target.getConnectionParts().get(0).getConnector();
		}
		return super.getEditableObject(attribute);
	}
	
	@Override
	protected IEditHandler getEditHandler(EAttribute attribute,
										  Object newValue) {
		
		if (attribute != SchemaPackage.eINSTANCE.getConnector_Label()) {		
			return super.getEditHandler(attribute, newValue);
		}
		
		// we need to set the label on both connectors, so go grab them (they 
		// should always be there)
		Connector[] connector = new Connector[2];
		connector[0] = target.getConnectionParts().get(0).getConnector();
		connector[1] = target.getConnectionParts().get(1).getConnector();
		
		// create 2 SetStringAttributeCommands to set the new label
		String value = (String) newValue;
		String label = getLabel(attribute);
		Command[] command = new Command[2];
		command[0] = new SetStringAttributeCommand(connector[0], attribute, 
												   value, label);
		command[1] = new SetStringAttributeCommand(connector[1], attribute, 
												   value, label);
		
		// chain the 2 commands and wrap the result in an IEditHandler
		final Command theCommand = command[0].chain(command[1]);		
		return new IEditHandler() {	
			@Override
			public Command getEditCommand() {
				return theCommand;
			}
			@Override
			public String getMessage() {
				return null;
			}
			@Override
			public boolean isValid() {
				return true;
			}
		};
	}
	
	@Override
	protected List<EAttribute> getAttributes() {
		List<EAttribute> attributes = new ArrayList<>();
		attributes.add(SchemaPackage.eINSTANCE.getConnector_Label());
		return attributes;
	}
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getConnector_Label()) {
			return "Label";
		}
		return super.getLabel(attribute);
	}
	
	@Override
	protected String getValue(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getConnector_Label()) {
			return target.getConnectionParts().get(0).getConnector().getLabel();
		}
		return super.getValue(attribute);
	}	

}