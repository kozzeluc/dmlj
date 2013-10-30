/**
 * Copyright (C) 2013  Luc Hermans
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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.editor.command.SetBooleanAttributeCommand;
import org.lh.dmlj.schema.editor.command.SetObjectAttributeCommand;
import org.lh.dmlj.schema.editor.command.SetShortAttributeCommand;

public class StandardEditHandler implements IEditHandler {

	private Command command;
	private String  message;	
	
	@SuppressWarnings("unused")
	private StandardEditHandler() {
		super();
	}
	
	StandardEditHandler(EObject target, EAttribute attribute, String label, 
						Object newValue) {
		
		this(target, attribute, label, newValue, null);
	}
	
	public StandardEditHandler(EObject target, EAttribute attribute, String label, 
						Object newValue, String message) {
		super();
		
		this.message = message;
		
		// we need to make sure that newValue is different from the old 
		// attribute value in target because there's no use in executing a 
		// command that changes nothing to the model but marks the schema editor 
		// as dirty
		EClassifier classifier = attribute.getEType();
		boolean valueChanged = true;
		Object oldValue = target.eGet(attribute);
		if (attribute.getEType().getName().equals("EString") ||
			attribute.getEType().getName().equals("EShortObject")) {
		
			if (oldValue == null) {
				valueChanged = newValue != null;
			} else if (newValue == null) {
				valueChanged = oldValue != null;
			} else {
				valueChanged = !oldValue.equals(newValue);
			}
		} else if (attribute.getEType().getName().equals("EBoolean")) {
			boolean oldB = ((Boolean) oldValue).booleanValue();
			boolean newB = ((Boolean) newValue).booleanValue();
			valueChanged = oldB != newB;
		} else if (attribute.getEType().getName().equals("EShort")) {
			short oldS = ((Short) oldValue).shortValue();
			short newS = ((Short) newValue).shortValue();
			valueChanged = oldS != newS;
		} else if (classifier.getInstanceClass().getSuperclass() == Enum.class) {
			valueChanged = !oldValue.equals(newValue);
		}
		
		// only if the value has changed (or we are dealing with an unsupported
		// type), create the edit command or set a message if we cannot set one
		if (valueChanged) {
			if (attribute.getEType().getName().equals("EString") ||
				attribute.getEType().getName().equals("EShortObject")) {
				
				command = new SetObjectAttributeCommand(target, attribute, 
												  		newValue, label);
			} else if (attribute.getEType().getName().equals("EBoolean")) {
				boolean b = ((Boolean)newValue).booleanValue();
				command = 
					new SetBooleanAttributeCommand(target, attribute, b, label);
			} else if (attribute.getEType().getName().equals("EShort")) {
				short i = ((Short)newValue).shortValue();
				command = 
					new SetShortAttributeCommand(target, attribute, i, label);
			} else if (classifier.getInstanceClass().isEnum()) {
				command = new SetObjectAttributeCommand(target, attribute, 
				  									    newValue, label);
			} else {
				message = "unsupported type: " + attribute.getEType().getName();
			}
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
