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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.editor.property.IRecordProvider;
import org.lh.dmlj.schema.editor.wizard._import.elements.ImportRecordElementsWizard;

public class EditRecordElementsHandler implements IHyperlinkHandler<EAttribute, Command> {
	private IRecordProvider recordProvider;

	public EditRecordElementsHandler(IRecordProvider recordProvider) {
		this.recordProvider = recordProvider;
	}
	
	@Override
	public Command hyperlinkActivated(EAttribute context) {
		var importWizard = new ImportRecordElementsWizard(recordProvider.getRecord());
		var selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		importWizard.init(PlatformUI.getWorkbench(), (IStructuredSelection) selection);
		var wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), importWizard);
		wizardDialog.setHelpAvailable(false);
		wizardDialog.create();
		wizardDialog.setTitle("Elements for Record %s".formatted(importWizard.getRecordName()));
		if (wizardDialog.open() == IDialogConstants.OK_ID) {
			return (Command) importWizard.getCommand();
		} else {
			return null;
		}
	}

}
