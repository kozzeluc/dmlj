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
package org.lh.dmlj.schema.editor.command;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

public class SetObjectAttributeCommand extends Command {
	
	private EObject    object;
	private EAttribute attribute;
	private Object	   value;
	
	private Object	   oldValue;

	private static String getLabel(EAttribute attribute, Object value,
								   String attributeLabel) {
		
		if (value != null) {
			if (attribute.getEType().getInstanceClass().isEnum()) {
				String sValue = value.toString();
				return "Set '" + attributeLabel + "' to '" + 
					   sValue.replaceAll("_", " ") + "'";
			} else {
				return "Set '" + attributeLabel + "' to '" + value + "'";
			}
		} else {
			return "Remove '" + attributeLabel + "'";
		}
	}
	
	public SetObjectAttributeCommand(EObject object, EAttribute attribute, 
							 		 Object value, String attributeLabel) {
		
		super(getLabel(attribute, value, attributeLabel));
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
