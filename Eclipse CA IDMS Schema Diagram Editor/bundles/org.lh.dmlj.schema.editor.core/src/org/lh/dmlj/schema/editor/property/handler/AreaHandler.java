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

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.PluginPropertiesCache;
import org.lh.dmlj.schema.editor.command.ChangeAreaSpecificationCommand;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeCompoundCommand;
import org.lh.dmlj.schema.editor.command.MoveRecordOrIndexToOtherAreaCommand;
import org.lh.dmlj.schema.editor.command.SetObjectAttributeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.property.IAreaSpecificationProvider;
import org.lh.dmlj.schema.editor.property.ui.AreaDialog;

public class AreaHandler implements IHyperlinkHandler<EAttribute, Command> {
	private final IAreaSpecificationProvider areaSpecificationProvider;
	
	public AreaHandler(IAreaSpecificationProvider areaSpecificationProvider) {
		this.areaSpecificationProvider = areaSpecificationProvider;
	}	

	@Override
	public Command hyperlinkActivated(EAttribute attribute) {	
		// create and open the dialog for maintaining a record's or system owner area specification; if the user
		// presses the cancel button, get out and return a null Command, otherwise get the entered data
		var dialog = new AreaDialog(Display.getCurrent().getActiveShell(), areaSpecificationProvider.getAreaSpecification());
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			return null;
		}
		var enteredData = dialog.getEnteredData();
		
		// the fact that the user was able to press the OK button means that he/she has effectively changed
		// something; get the area specification from the IAreaSpecificationProvider
		var areaSpecification = areaSpecificationProvider.getAreaSpecification();
			
		// now is a good time to create the model change context
		var context = new ModelChangeContext(ModelChangeType.CHANGE_AREA_SPECIFICATION);
		if (areaSpecification.getRecord() != null) {
			context.putContextData(areaSpecification.getRecord());
		} else {
			context.putContextData(areaSpecification.getSystemOwner().getSet(), ModelChangeContext.setContextDataAssembler);
		}
		
		// see what area the record or system owner has to be located in; if it is the same (possibly renamed)
		// area, we will not replace the whole AreaSpecification, if the record is moved to a different (possibly
		// new) area, we will create a new AreaSpecification and, if no other items are contained in the 'old'
		// area, we will remove the 'old' area from the schema
		if (enteredData.action() == AreaDialog.Action.KEEP_IN_CURRENT_AREA || enteredData.action() == AreaDialog.Action.RENAME_AREA) {
			return createCommandsToKeepInSameAreaWithPossibleAreaRename(attribute, context, areaSpecification, enteredData);	
		} else {
			return createCommandsToMoveToAnotherArea(context, areaSpecification, enteredData);
		}
	}
	
	private Command createCommandsToKeepInSameAreaWithPossibleAreaRename(EAttribute attribute, ModelChangeContext context,
			AreaSpecification areaSpecification, AreaDialog.EnteredData enteredData) {
		
		var commands = new ArrayList<IModelChangeCommand>();
		commands.addAll(createCommandsForPossibleAreaRename(attribute, areaSpecification, enteredData));
				
		var oldSymbolicSubareaName = areaSpecification.getSymbolicSubareaName();
		var offsetExpression = areaSpecification.getOffsetExpression();
		var oldOffsetPageCount = offsetExpression == null ? null : offsetExpression.getOffsetPageCount();
		var oldOffsetPercent = offsetExpression == null ? null : offsetExpression.getOffsetPercent();
		var oldPageCount = offsetExpression == null ? null : offsetExpression.getPageCount();
		var oldPercent = offsetExpression == null ? null : offsetExpression.getPercent();
		if (!Objects.equals(oldSymbolicSubareaName, enteredData.symbolicSubareaName()) ||
			!Objects.equals(oldOffsetPageCount, enteredData.offsetPageCount()) ||
			!Objects.equals(oldOffsetPercent, enteredData.offsetPercent()) ||
			!Objects.equals(oldPageCount, enteredData.pageCount()) ||
			!Objects.equals(oldPercent, enteredData.percent())) {
			
			var command = new ChangeAreaSpecificationCommand(areaSpecification, enteredData.symbolicSubareaName(),
					enteredData.offsetPageCount(), enteredData.offsetPercent(), enteredData.pageCount(),
					enteredData.percent());
			commands.add(command);
		}
		return createFinalCommand(context, commands);
	}
	
	private List<IModelChangeCommand> createCommandsForPossibleAreaRename(EAttribute attribute, AreaSpecification areaSpecification,
			AreaDialog.EnteredData enteredData) {
		
		if (enteredData.action() == AreaDialog.Action.RENAME_AREA) {
			// rename area; create a command to set the new area name
			String attributeLabel;
			try {
				var key = "label.org.lh.dmlj.schema.SchemaArea.name";
				attributeLabel = PluginPropertiesCache.get(Plugin.getDefault(), key);
			} catch (MissingResourceException e) {
				throw new IllegalStateException(e);
			}
			return List.of(new SetObjectAttributeCommand(areaSpecification.getArea(), attribute, enteredData.areaName(), attributeLabel));
		} else {
			return List.of();
		}
	}
	
	private Command createCommandsToMoveToAnotherArea(ModelChangeContext context, AreaSpecification areaSpecification,
			AreaDialog.EnteredData enteredData) {
		
		// the record or system owner (and index) move(s) to another area; we might need to create the new area
		// first and a cleanup of the old area is possibly needed - return a compound command if either the
		// symbolic subarea or 1 or more offset expression attributes have changed...
		if (isSymbolicSubareaChanged(areaSpecification, enteredData) || isOffsetExpressionChanged(areaSpecification, enteredData)) {
			// the record moves to another area AND either the symbolic subarea or at least 1 offset expression
			// attribute have changed; we need to create a compound command
							
			// create the command to move the record or index to another area 
			String ccLabel;
			MoveRecordOrIndexToOtherAreaCommand moveRecordOrIndexToOtherAreaCommand;
			if (areaSpecification.getRecord() != null) {
				var schemaRecord = areaSpecification.getRecord();
				ccLabel = "Move record '" + Tools.removeTrailingUnderscore(schemaRecord.getName()) +  "' to area '" + enteredData.areaName() + "'";
				moveRecordOrIndexToOtherAreaCommand = new MoveRecordOrIndexToOtherAreaCommand(areaSpecification.getRecord(), enteredData.areaName());
			} else {
				var systemOwner = areaSpecification.getSystemOwner();
				ccLabel = "Move index '" + Tools.removeTrailingUnderscore(systemOwner.getSet().getName()) + "' to area '" + enteredData.areaName() + "'";
				moveRecordOrIndexToOtherAreaCommand = new MoveRecordOrIndexToOtherAreaCommand(areaSpecification.getSystemOwner(), enteredData.areaName());
			}
			
			// create the command to change the area specification (symbolic subarea or offset expression
			var changeAreaSpecificationCommand = new ChangeAreaSpecificationCommand(areaSpecification, enteredData.symbolicSubareaName(),
					enteredData.offsetPageCount(), enteredData.offsetPercent(), enteredData.pageCount(), enteredData.percent());
			
			// create the compound command and return it			
			var cc = new ModelChangeCompoundCommand(ccLabel);				
			cc.setContext(context);
			cc.add(moveRecordOrIndexToOtherAreaCommand);
			cc.add(changeAreaSpecificationCommand);
			return cc;
		} else {
			// the record or index moves to another area; neither the symbolic subarea nor any offset expression
			// attribute have changed
			if (areaSpecification.getRecord() != null) {
				var command = new MoveRecordOrIndexToOtherAreaCommand(areaSpecification.getRecord(), enteredData.areaName());
				command.setContext(context);
				return command;
			} else {
				var command = new MoveRecordOrIndexToOtherAreaCommand(areaSpecification.getSystemOwner(), enteredData.areaName());
				command.setContext(context);
				return command;
			}
		}
	}
	
	private Command createFinalCommand(ModelChangeContext context, List<IModelChangeCommand> commands) {
		if (commands.isEmpty()) {
			throw new IllegalStateException("logic error: no commands created");				
		} else if (commands.size() > 1) {
			var cc = new ModelChangeCompoundCommand("Change area specification");				
			cc.setContext(context);
			for (IModelChangeCommand command : commands) {
				cc.add((Command) command);
			}
			return cc;
		} else {
			var command = commands.get(0);
			command.setContext(context);
			return (Command) commands.get(0);
		}
	}
	
	private boolean isOffsetExpressionChanged(AreaSpecification areaSpecification, AreaDialog.EnteredData enteredData) {
		var offsetExpression = areaSpecification.getOffsetExpression();
		var oldOffsetPageCount = offsetExpression == null ? null : offsetExpression.getOffsetPageCount();
		var oldOffsetPercent = offsetExpression == null ? null : offsetExpression.getOffsetPercent();
		var oldPageCount = offsetExpression == null ? null : offsetExpression.getPageCount();
		var oldPercent = offsetExpression == null ? null : offsetExpression.getPercent();
		return !Objects.equals(oldOffsetPageCount,enteredData.offsetPageCount()) ||
			   !Objects.equals(oldOffsetPercent, enteredData.offsetPercent()) ||
			   !Objects.equals(oldPageCount, enteredData.pageCount()) ||
			   !Objects.equals(oldPercent, enteredData.percent());
	}
	
	private boolean isSymbolicSubareaChanged(AreaSpecification areaSpecification, AreaDialog.EnteredData enteredData) {
		// get the old and new area specification information...
		var oldSymbolicSubareaName = areaSpecification.getSymbolicSubareaName(); 
		var newSymbolicSubareaName = enteredData.symbolicSubareaName();			
		
		// ...and see if something has changed
		return oldSymbolicSubareaName != null && !oldSymbolicSubareaName.equals(newSymbolicSubareaName) ||
			   newSymbolicSubareaName != null && !newSymbolicSubareaName.equals(oldSymbolicSubareaName);
	}

}
