package org.lh.dmlj.schema.editor.command;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

public class SetObjectAttributeCommand extends Command {
	
	private EObject    object;
	private EAttribute attribute;
	private Object	   value;
	
	private Object	   oldValue;

	public SetObjectAttributeCommand(EObject object, EAttribute attribute, 
							 		 Object value, String label) {
		
		super(value != null ? "Set '" + label + "' to '" + value + "'" :
			  "Remove '" + label + "'");
		this.object = object;
		this.attribute = attribute;
		this.value = value;
	}
	
	@Override
	public void execute() {
		oldValue = object.eGet(attribute);
		object.eSet(attribute, value);
	}
		
	@Override
	public void undo() {
		object.eSet(attribute, oldValue);
	}
}