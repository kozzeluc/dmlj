package org.lh.dmlj.schema.editor.command;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

public class SetBooleanAttributeCommand extends Command {
	
	private EObject 	object;
	private EAttribute	attribute;
	private boolean		value;
	
	private boolean		oldValue;	
		
	public SetBooleanAttributeCommand(EObject object, EAttribute attribute, 
		 		  					  boolean value, String label) {		
		
		super("Set '" + label + "' to " + value);
		this.object = object;
		this.attribute = attribute;
		this.value = value;
	}
	
	@Override
	public void execute() {
		oldValue = (boolean) object.eGet(attribute);
		object.eSet(attribute, value);
	}
		
	public EAttribute getAttribute() {
		return attribute;
	}

	@Override
	public void undo() {
		object.eSet(attribute, oldValue);
	}
}