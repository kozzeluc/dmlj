package org.lh.dmlj.schema.editor.command;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

public class SetShortAttributeCommand extends Command {
	
	private EObject 	object;
	private EAttribute	attribute;
	private short 		value;
	
	private short 		oldValue;

	public SetShortAttributeCommand(EObject object, EAttribute attribute, 
							 		short value, String label) {
		
		super("Set '" + label + "' to '" + value + "'");
		this.object = object;
		this.attribute = attribute;
		this.value = value;
	}
	
	@Override
	public void execute() {
		oldValue = (short) object.eGet(attribute);
		object.eSet(attribute, value);
	}
		
	@Override
	public void undo() {
		object.eSet(attribute, oldValue);
	}
}