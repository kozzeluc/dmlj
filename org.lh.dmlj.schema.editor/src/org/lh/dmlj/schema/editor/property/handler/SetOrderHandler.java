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
package org.lh.dmlj.schema.editor.property.handler;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.editor.command.ChangeSetOrderCommand;
import org.lh.dmlj.schema.editor.command.ChangeSortKeysCommand;
import org.lh.dmlj.schema.editor.property.ISetProvider;
import org.lh.dmlj.schema.editor.property.ui.SetOrderDialog;

public class SetOrderHandler implements IHyperlinkHandler<EAttribute, Command> {

	private ISetProvider setProvider;	
	
	public SetOrderHandler(ISetProvider setProvider) {
		super();
		this.setProvider = setProvider;		
	}	

	@Override
	public Command hyperlinkActivated(EAttribute attribute) {		
		
		// create and open the dialog for maintaining a set's order data; if the user presses the 
		// cancel button, get out and return a null Command
		SetOrderDialog dialog = 
			new SetOrderDialog(Display.getCurrent().getActiveShell(), setProvider.getSet());
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			// cancel button pressed
			return null;
		}
		
		// the fact that the user was able to press the OK button means that he has effectively 
		// changed something; get the set from the ISetProvider
		Set set = setProvider.getSet();
		
		// create the appropriate command
		Command command = null;
		if (dialog.getSetOrder() != set.getOrder()) {
			// the set order has changed; things are slightly more complicated when the set becomes
			// sorted because a description for each member record's sort key has to be assembled
			// (fortunately, the set order dialog can do this for us)
			if (dialog.getSetOrder() == SetOrder.SORTED) {
				command = new ChangeSetOrderCommand(set, dialog.getSortKeyDescriptions());
			} else {
				command = new ChangeSetOrderCommand(set, dialog.getSetOrder());
			}
		} else {
			// the set order did not change which means the sortkey for at least 1 member record has
			// to be modified
			Assert.isTrue(set.getOrder() == SetOrder.SORTED, "expected a sorted set");
			command = new ChangeSortKeysCommand(set, dialog.getSortKeyDescriptions());
		}
		
		
		return command; 		
		
	}

}
