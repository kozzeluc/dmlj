/**
 * Copyright (C) 2015  Luc Hermans
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
import org.lh.dmlj.schema.editor.command.ChangeLocationModeCommandFactory;
import org.lh.dmlj.schema.editor.property.IRecordProvider;
import org.lh.dmlj.schema.editor.property.ui.LocationModeDialog;

public class LocationModeHandler implements IHyperlinkHandler<EAttribute, Command> {

	private IRecordProvider recordProvider;	
	
	public LocationModeHandler(IRecordProvider recordProvider) {
		super();
		this.recordProvider = recordProvider;		
	}
	
	@Override
	public Command hyperlinkActivated(EAttribute attribute) {		
		
		// create and open the dialog for maintaining a record's location mode data; if the user 
		// presses the cancel button, get out and return a null Command
		LocationModeDialog dialog = 
			new LocationModeDialog(Display.getCurrent().getActiveShell(), recordProvider.getRecord());
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			// cancel button pressed
			return null;
		}
		
		// the fact that the user was able to press the OK button means that he has effectively 
		// changed something; get the record from the IRecordProvider - depending on the record's 
		// current location mode, assemble a command to be executed on the command stack and return 
		// it to the caller
		ChangeLocationModeCommandFactory factory = new ChangeLocationModeCommandFactory();
		return (Command) factory.getCommand(recordProvider.getRecord(), dialog);
		
		
	}

}
