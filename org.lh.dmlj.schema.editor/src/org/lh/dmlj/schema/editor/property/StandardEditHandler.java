package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.editor.command.SetBooleanAttributeCommand;
import org.lh.dmlj.schema.editor.command.SetShortAttributeCommand;
import org.lh.dmlj.schema.editor.command.SetStringAttributeCommand;

public class StandardEditHandler implements IEditHandler {

	private Command command;
	private String  message;	
	
	@SuppressWarnings("unused")
	private StandardEditHandler() {
		super();
	}
	
	StandardEditHandler(EObject target, EAttribute attribute, String label, 
						Object newValue) {
		super();
		if (attribute.getEType().getName().equals("EString")) {
			command = 
				new SetStringAttributeCommand(target, attribute, 
											  (String) newValue, label);
		} else if (attribute.getEType().getName().equals("EBoolean")) {
			boolean b = ((Boolean)newValue).booleanValue();
			command = 
				new SetBooleanAttributeCommand(target, attribute, b, label);
		} else if (attribute.getEType().getName().equals("EShort")) {
			short i = ((Short)newValue).shortValue();
			command = 
				new SetShortAttributeCommand(target, attribute, i, label);
		} else {
			message = "unsupported type: " + attribute.getEType().getName();
		}
	}

	@Override
	public Command getEditCommand() {
		return command;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public boolean isValid() {
		return command != null;
	}

}
