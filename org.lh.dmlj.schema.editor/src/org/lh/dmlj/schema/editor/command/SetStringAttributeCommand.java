package org.lh.dmlj.schema.editor.command;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

public class SetStringAttributeCommand extends Command {
	
	private EObject 	object;
	private EAttribute	attribute;
	private String 		value;
	
	private String 		oldValue;

	public SetStringAttributeCommand(EObject object, EAttribute attribute, 
							 		 String value, String label) {
		
		super("Set '" + label + "' to '" + value + "'");
		this.object = object;
		this.attribute = attribute;
		this.value = value;
	}
	
	@Override
	public void execute() {
		oldValue = (String) object.eGet(attribute);
		object.eSet(attribute, value);
	}
		
	@Override
	public void undo() {
		object.eSet(attribute, oldValue);
	}
}