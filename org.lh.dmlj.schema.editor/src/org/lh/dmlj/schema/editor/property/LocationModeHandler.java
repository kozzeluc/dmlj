package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.editor.command.MakeRecordDirectCommand;

public class LocationModeHandler implements IHyperlinkHandler {

	private ICommandStackProvider commandStackProvider;
	private IRecordProvider 	  recordProvider;	
	
	LocationModeHandler(IRecordProvider recordProvider,
						ICommandStackProvider commandStackProvider) {
		super();
		this.recordProvider = recordProvider;
		this.commandStackProvider = commandStackProvider;
	}
	
	@Override
	public EObject getModelObject() {
		return recordProvider.getRecord();
	}

	@Override
	public boolean hyperlinkActivated(EAttribute attribute) {		
		
		// create and open the dialog for maintaining a record's location mode 
		// data; if the user presses the cancel button, get out
		LocationModeDialog dialog = 
			new LocationModeDialog(Display.getCurrent().getActiveShell(),
								   recordProvider.getRecord());
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			// cancel button pressed
			return false;
		}
		
		// the fact that the user was able to press the OK button means that
		// he has effectively changed something; the general idea is to create
		// a compound command, in which the first command will remove all 
		// current location mode data, by setting the location mode for the 
		// record to DIRECT - if the record is already DIRECT, we skip this 
		// action, if the record's location mode is set to DIRECT, that's all
		// that needs to be done
		Command makeRecordDirectCommand = 
			new MakeRecordDirectCommand(recordProvider.getRecord());
		
		commandStackProvider.getCommandStack().execute(makeRecordDirectCommand);
		
		return true;
		
	}

}