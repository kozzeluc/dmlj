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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.editor.command.ChangeIndexedSetModeSpecificationCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.property.IIndexedSetModeSpecificationProvider;
import org.lh.dmlj.schema.editor.property.ui.IndexedSetModeSpecificationDialog;

public class IndexedSetModeSpecificationHandler implements IHyperlinkHandler<EAttribute, Command> {
	private IIndexedSetModeSpecificationProvider indexedSetModeSpecificationProvider;

	public IndexedSetModeSpecificationHandler(IIndexedSetModeSpecificationProvider indexedSetModeSpecificationProvider) {
		this.indexedSetModeSpecificationProvider = indexedSetModeSpecificationProvider;
	}
	
	@Override
	public Command hyperlinkActivated(EAttribute attribute) {
		var dialog = new IndexedSetModeSpecificationDialog(Display.getCurrent().getActiveShell(),
				indexedSetModeSpecificationProvider.getIndexedSetModeSpecification());
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			return null;
		}
		
		// the fact that the user was able to press the OK button means that he has effectively changed
		// something; create a ChangeIndexedSetModeSpecificationCommand and return it
		var set = indexedSetModeSpecificationProvider.getIndexedSetModeSpecification().getSet();
		var context = new ModelChangeContext(ModelChangeType.CHANGE_INDEXED_SET_MODE_SPECIFICATION);
		context.putContextData(set, ModelChangeContext.setContextDataAssembler);		
				
		var command = new ChangeIndexedSetModeSpecificationCommand(set, dialog.getSymbolicIndexName(),
				dialog.getKeyCount(), dialog.getDisplacementPages());
		command.setContext(context);
		return command;
	}

}
