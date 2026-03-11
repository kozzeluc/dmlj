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
package org.lh.dmlj.schema.editor.property.section;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.ModelChangeCompoundCommand;
import org.lh.dmlj.schema.editor.command.SetObjectAttributeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;

public class SetConnectorPropertiesSection extends AbstractSetPropertiesSection {
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getConnector_Label()) {
			return target.getConnectionParts().get(0).getConnector();
		} else {
			return super.getAttributeOwner(attribute);
		}
	}

	@Override
	public List<EAttribute> getAttributes() {
		return List.of(SchemaPackage.eINSTANCE.getConnector_Label());
	}	
	
	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getConnector_Label()) {
			// although there will always be 2 connectors involved, capturing the model changes from the first will do
			return target.getConnectionParts().get(0).getConnector();
		}
		return super.getEditableObject(attribute);
	}
	
	@Override
	public IEditHandler getEditHandler(EAttribute attribute, Object newValue) {
		if (attribute != SchemaPackage.eINSTANCE.getConnector_Label()) {		
			return super.getEditHandler(attribute, newValue);
		}
		
		// we need to set the label on both connectors, so go grab them (they should always be there)
		var connector = new Connector[] {
				target.getConnectionParts().get(0).getConnector(),
				target.getConnectionParts().get(1).getConnector()};
		
		// create 2 SetStringAttributeCommands to set the new label
		var value = (String) newValue;
		var label = getLabel(attribute);
		var command = new Command[] {
				new SetObjectAttributeCommand(connector[0], attribute, value, label),
				new SetObjectAttributeCommand(connector[1], attribute, value, label)};
		
		// create a compound command containing the 2 commands and wrap the result in an IEditHandler; it is
		// perfectly OK to use the SET_PROPERTY model change type here because the connector label is entered in
		// the Properties view (and that's where that model change type stands for)
		var context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(target.getConnectionParts().get(0).getConnector(), SchemaPackage.eINSTANCE.getConnector_Label());
		var cc = new ModelChangeCompoundCommand(); 
		cc.setLabel(command[0].getLabel());
		cc.setContext(context);
		cc.add(command[0]);
		cc.add(command[1]);
		return new IEditHandler() {	
			@Override
			public Command getEditCommand() {
				return cc;
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

}
