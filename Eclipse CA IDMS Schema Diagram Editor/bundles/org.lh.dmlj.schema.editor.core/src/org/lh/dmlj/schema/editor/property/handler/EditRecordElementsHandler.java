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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.editor.property.IRecordProvider;
import org.lh.dmlj.schema.editor.wizard._import.elements.ImportRecordElementsWizard;

public class EditRecordElementsHandler implements IHyperlinkHandler<EAttribute, Command> {
	
	private IRecordProvider recordProvider;		

	public EditRecordElementsHandler(IRecordProvider recordProvider) {
		super();
		this.recordProvider = recordProvider;
	}
	
	@Override
	public Command hyperlinkActivated(EAttribute context) {
		ImportRecordElementsWizard importWizard = new ImportRecordElementsWizard(recordProvider.getRecord());
		ISelection selection = PlatformUI.getWorkbench()
		 					  			 .getActiveWorkbenchWindow()
		 					  			 .getSelectionService()
		 					  			 .getSelection();
		importWizard.init(PlatformUI.getWorkbench(), (IStructuredSelection) selection);
		WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), importWizard);
		wizardDialog.setHelpAvailable(false);
		wizardDialog.create();
		// we should move the wizard title to plugin.properties...
		wizardDialog.setTitle("Elements for Record " + importWizard.getRecordName());
		if (wizardDialog.open() == Dialog.OK) {
			return (Command) importWizard.getCommand();
		} else {
			return null;
		}
	}

}
