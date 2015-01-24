/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.ui.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.editor.wizard._import.elements.ImportRecordElementsWizard;

public class ImportRecordElementsHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = Display.getCurrent().getActiveShell();
		ImportRecordElementsWizard importWizard = new ImportRecordElementsWizard();
		ISelection selection = PlatformUI.getWorkbench()
		 					  			 .getActiveWorkbenchWindow()
		 					  			 .getSelectionService()
		 					  			 .getSelection();
		importWizard.init(PlatformUI.getWorkbench(), (IStructuredSelection) selection);
		final WizardDialog wizardDialog = new WizardDialog(shell, importWizard);
		wizardDialog.create();
		// we should move the wizard title to plugin.properties...
		wizardDialog.setTitle("Elements for Record " + importWizard.getRecordName());
		Display.getCurrent().syncExec(new Runnable() {
			public void run() {
				wizardDialog.open();
			}
		});	
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
	}

}
