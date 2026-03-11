/**
 * Copyright (C) 2025  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.property.handler;

import java.util.Objects;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.SetBooleanAttributeCommand;
import org.lh.dmlj.schema.editor.command.SetObjectAttributeCommand;
import org.lh.dmlj.schema.editor.command.SetShortAttributeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;

public class StandardEditHandler implements IEditHandler {
	private final String message;
	private final IModelChangeCommand command;
	
	public StandardEditHandler(EObject target, EAttribute attribute, String label, Object newValue, String message) {
		this.message = message;
		
		// we need to make sure that newValue is different from the old attribute value in target because there's
		// no use in executing a command that changes nothing to the model but marks the schema editor as dirty
		var valueChanged = determineWhetherValueChanged(attribute, target.eGet(attribute), newValue);
		
		// only if the value has changed (or we are dealing with an unsupported type), create the edit command or
		// set a message if we cannot set one
		if (valueChanged) {
			if (attribute.getEType().getName().equals("EString") || attribute.getEType().getName().equals("EShortObject")) {
				command = new SetObjectAttributeCommand(target, attribute, newValue, label);
			} else if (attribute.getEType().getName().equals("EBoolean")) {
				command = new SetBooleanAttributeCommand(target, attribute, (Boolean) newValue, label);
			} else if (attribute.getEType().getName().equals("EShort")) {
				command = new SetShortAttributeCommand(target, attribute, (Short) newValue, label);
			} else if (attribute.getEType().getInstanceClass().isEnum()) {
				command = new SetObjectAttributeCommand(target, attribute, newValue, label);
			} else {
				message = "unsupported type: " + attribute.getEType().getName();
				command = null;
			}
			if (command != null) {
				var context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
				context.putContextData(target, attribute);
				command.setContext(context);
			}
		} else {
			command = null;
		}
	}
	
	private boolean determineWhetherValueChanged(EAttribute attribute, Object oldValue, Object newValue) {
		if (attribute.getEType().getName().equals("EString") || attribute.getEType().getName().equals("EShortObject") ||
			attribute.getEType().getName().equals("EBoolean") || attribute.getEType().getName().equals("EShort") ||
			attribute.getEType().getInstanceClass().getSuperclass() == Enum.class) {
			
			return !Objects.equals(oldValue, newValue);
		} else {
			return true;
		}
	}

	@Override
	public Command getEditCommand() {
		return (Command) command;
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
