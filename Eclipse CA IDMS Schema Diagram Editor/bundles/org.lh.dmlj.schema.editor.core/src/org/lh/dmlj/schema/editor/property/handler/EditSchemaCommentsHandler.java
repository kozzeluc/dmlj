/**
 * Copyright (C) 2016  Luc Hermans
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
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.ChangeSchemaCommentsCommand;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.property.ISchemaProvider;
import org.lh.dmlj.schema.editor.property.ui.EditSchemaCommentsDialog;

public class EditSchemaCommentsHandler implements IHyperlinkHandler<EAttribute, Command> {
	
	private ISchemaProvider schemaProvider;		

	public EditSchemaCommentsHandler(ISchemaProvider schemaProvider) {
		super();
		this.schemaProvider = schemaProvider;
	}
	
	@Override
	public Command hyperlinkActivated(EAttribute context) {	
		Schema schema = schemaProvider.getSchema();
		EditSchemaCommentsDialog dialog = 
			new EditSchemaCommentsDialog(Display.getCurrent().getActiveShell(), schema);
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			return null;
		} else {
			ModelChangeContext mcc = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
			mcc.putContextData(schema, SchemaPackage.eINSTANCE.getSchema_Comments());
			IModelChangeCommand command = new ChangeSchemaCommentsCommand(schema, dialog.getNewValue());
			command.setContext(mcc);			
			return (Command) command;
		}
	}

}
