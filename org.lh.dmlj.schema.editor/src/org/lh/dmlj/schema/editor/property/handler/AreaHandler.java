/**
 * Copyright (C) 2013  Luc Hermans
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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.OffsetExpression;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.PluginPropertiesCache;
import org.lh.dmlj.schema.editor.command.ChangeAreaSpecificationCommand;
import org.lh.dmlj.schema.editor.command.MoveRecordOrIndexToOtherAreaCommand;
import org.lh.dmlj.schema.editor.command.SetObjectAttributeCommand;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.property.IAreaSpecificationProvider;
import org.lh.dmlj.schema.editor.property.ui.AreaDialog;

public class AreaHandler implements IHyperlinkHandler {	
	
	private IAreaSpecificationProvider areaSpecificationProvider;
	
	public AreaHandler(IAreaSpecificationProvider areaSpecificationProvider) {
		super();
		this.areaSpecificationProvider = areaSpecificationProvider;
	}	

	@Override
	public Command hyperlinkActivated(EAttribute attribute) {		
		
		// create and open the dialog for maintaining a record's or system owner
		// area specification; if the user presses the cancel button, get out 
		// and return a null Command
		AreaDialog dialog = 
			new AreaDialog(Display.getCurrent().getActiveShell(),
						   areaSpecificationProvider.getAreaSpecification());
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			// cancel button pressed
			return null;
		}
		
		// the fact that the user was able to press the OK button means that
		// he has effectively changed something; get the area specification from 
		// the IAreaSpecificationProvider
		AreaSpecification areaSpecification = 
			areaSpecificationProvider.getAreaSpecification();
				
		// see what area the record or system owner has to be located in; if it
		// is the same (possibly renamed) area, we will not replace the whole
		// AreaSpecification, if the record is moved to a different (possibly
		// new) area, we will create a new AreaSpecification and, if no other
		// items are contained in the 'old' area, we will remove the 'old' area
		// from the schema
		AreaDialog.Action action = dialog.getAction();
		String areaName = dialog.getAreaName();
		if (action == AreaDialog.Action.KEEP_IN_CURRENT_AREA ||
			action == AreaDialog.Action.RENAME_AREA) {
			
			// the record remains located in the same area, but the area may 
			// have to be renamed
			List<Command> commands = new ArrayList<>();
			if (action == AreaDialog.Action.RENAME_AREA) {
				// rename area; create a command to set the new area name
				String attributeLabel;
				try {
					String key = "label.org.lh.dmlj.schema.SchemaArea.name";
					attributeLabel = PluginPropertiesCache.get(Plugin.getDefault(), key);
				} catch (MissingResourceException e) {
					throw new RuntimeException(e);
				}
				commands.add(new SetObjectAttributeCommand(areaSpecification.getArea(), 
												  		   attribute, areaName, 
												  		   attributeLabel));
			}
			
			// get the old and new area specification information...
			String oldSymbolicSubareaName = 
				areaSpecification.getSymbolicSubareaName(); 
			String newSymbolicSubareaName = dialog.getSymbolicSubareaName();			
			
			OffsetExpression offsetExpression = 
				areaSpecification.getOffsetExpression();
			
			Integer oldOffsetPageCount = offsetExpression == null ? null :
				offsetExpression.getOffsetPageCount();
			Integer newOffsetPageCount = dialog.getOffsetPageCount();
			
			Short oldOffsetPercent = offsetExpression == null ? null :
				offsetExpression.getOffsetPercent();
			Short newOffsetPercent = dialog.getOffsetPercent();
			
			Integer oldPageCount = offsetExpression == null ? null :
				offsetExpression.getPageCount();
			Integer newPageCount = dialog.getPageCount();
			
			Short oldPercent = offsetExpression == null ? null :
				offsetExpression.getPercent();
			Short newPercent = dialog.getPercent();
			
			// ... if something has changed, create the appropriate command to
			// deal with it
			if (oldSymbolicSubareaName != null &&
				!oldSymbolicSubareaName.equals(newSymbolicSubareaName) ||
				newSymbolicSubareaName != null &&
				!newSymbolicSubareaName.equals(oldSymbolicSubareaName) ||
				
				oldOffsetPageCount != null &&
			    !oldOffsetPageCount.equals(newOffsetPageCount) ||
			    newOffsetPageCount != null &&
			    !newOffsetPageCount.equals(oldOffsetPageCount) ||
			    
			    oldOffsetPercent != null &&
			    !oldOffsetPercent.equals(newOffsetPercent) ||
			    newOffsetPercent != null &&
			    !newOffsetPercent.equals(oldOffsetPercent) ||
			    
			    oldPageCount != null && !oldPageCount.equals(newPageCount) ||
			    newPageCount != null && !newPageCount.equals(oldPageCount) ||
			    
			    oldPercent != null && !oldPercent.equals(newOffsetPercent) ||			    
			    newPercent != null && !newPercent.equals(oldOffsetPercent)) {
				
				Command command = 
					new ChangeAreaSpecificationCommand(areaSpecification, 
													   newSymbolicSubareaName, 
													   newOffsetPageCount, 
													   newOffsetPercent, 
													   newPageCount, 
													   newPercent);
				commands.add(command);
			}
			
			if (commands.isEmpty()) {
				throw new RuntimeException("logic error: no commands created");				
			} else if (commands.size() > 1) {
				CompoundCommand cc = 
					new CompoundCommand("Change area specification");				
				for (Command command : commands) {
					cc.add(command);
				}
				return cc;
			} else {
				return commands.get(0);
			}
			
		} else {
						
			// the record or system owner (and index) move(s) to another area; we might need to 
			// create the new area first and a cleanup of the old area is possibly needed - return a 
			// compound command if either the symbolic subarea or 1 or more offset expression 
			// attributes have changed...
			if (isSymbolicSubareaChanged(areaSpecification, dialog) ||
				isOffsetExpressionChanged(areaSpecification, dialog)) {
				
				// the record moves to another area AND either the symbolic subarea or at least 1 
				// offset expression attribute have changed; we need to create a compound command
								
				// create the command to move the record or index to another area 
				String ccLabel;
				MoveRecordOrIndexToOtherAreaCommand moveRecordOrIndexToOtherAreaCommand;
				if (areaSpecification.getRecord() != null) {
					SchemaRecord record = areaSpecification.getRecord();
					ccLabel = "Move record '" + Tools.removeTrailingUnderscore(record.getName()) + 
							"' to area '" + dialog.getAreaName() + "'";
					moveRecordOrIndexToOtherAreaCommand = 
						new MoveRecordOrIndexToOtherAreaCommand(areaSpecification.getRecord(), 
											 	     			dialog.getAreaName());				
				} else {
					SystemOwner systemOwner = areaSpecification.getSystemOwner();
					ccLabel = "Move index '" + 
							Tools.removeTrailingUnderscore(systemOwner.getSet().getName()) + 
							"' to area '" + dialog.getAreaName() + "'";
					moveRecordOrIndexToOtherAreaCommand = 
						new MoveRecordOrIndexToOtherAreaCommand(areaSpecification.getSystemOwner(), 
			 	     		   									dialog.getAreaName());
				}
				
				// create the command to change the area specification (symbolic subarea or offset
				// expression
				Command changeAreaSpecificationCommand =
					new ChangeAreaSpecificationCommand(areaSpecification, 
													   dialog.getSymbolicSubareaName(), 
													   dialog.getOffsetPageCount(), 
													   dialog.getOffsetPercent(), 
													   dialog.getPageCount(), 
													   dialog.getPercent());				
				
				// create the compound command and return it			
				CompoundCommand cc = new CompoundCommand(ccLabel);				
				cc.add(moveRecordOrIndexToOtherAreaCommand);
				cc.add(changeAreaSpecificationCommand);
				return cc;
				
			} else {
				// the record or index moves to another area; neither the symbolic subarea nor any 
				// offset expression attribute have changed
				if (areaSpecification.getRecord() != null) {
					return new MoveRecordOrIndexToOtherAreaCommand(areaSpecification.getRecord(), 
	 	     												   	   dialog.getAreaName());
				} else {
					return new MoveRecordOrIndexToOtherAreaCommand(areaSpecification.getSystemOwner(), 
																   dialog.getAreaName());
				}
			}
		}		
		
	}
	
	private boolean isOffsetExpressionChanged(AreaSpecification areaSpecification, 
											  AreaDialog dialog) {
		
		// get the old and new area specification information...
		
		OffsetExpression offsetExpression = areaSpecification.getOffsetExpression();
		
		Integer oldOffsetPageCount = 
			offsetExpression == null ? null : offsetExpression.getOffsetPageCount();
		Integer newOffsetPageCount = dialog.getOffsetPageCount();
		
		Short oldOffsetPercent = 
			offsetExpression == null ? null : offsetExpression.getOffsetPercent();
		Short newOffsetPercent = dialog.getOffsetPercent();
		
		Integer oldPageCount = 
			offsetExpression == null ? null : offsetExpression.getPageCount();
		Integer newPageCount = dialog.getPageCount();
		
		Short oldPercent = 
			offsetExpression == null ? null : offsetExpression.getPercent();
		Short newPercent = dialog.getPercent();
		
		// ... and see if something has changed
		return oldOffsetPageCount != null && !oldOffsetPageCount.equals(newOffsetPageCount) ||
			   newOffsetPageCount != null && !newOffsetPageCount.equals(oldOffsetPageCount) ||
		    
			   oldOffsetPercent != null && !oldOffsetPercent.equals(newOffsetPercent) ||
			   newOffsetPercent != null && !newOffsetPercent.equals(oldOffsetPercent) ||
		    
			   oldPageCount != null && !oldPageCount.equals(newPageCount) ||
			   newPageCount != null && !newPageCount.equals(oldPageCount) ||
		    
			   oldPercent != null && !oldPercent.equals(newOffsetPercent) ||			    
			   newPercent != null && !newPercent.equals(oldOffsetPercent);
						
	}
	
	private boolean isSymbolicSubareaChanged(AreaSpecification areaSpecification, 
			  								 AreaDialog dialog) {
		
		// get the old and new area specification information...
		String oldSymbolicSubareaName = areaSpecification.getSymbolicSubareaName(); 
		String newSymbolicSubareaName = dialog.getSymbolicSubareaName();			
		
		// ... and see if something has changed
		return oldSymbolicSubareaName != null && 
				!oldSymbolicSubareaName.equals(newSymbolicSubareaName) ||
				
				newSymbolicSubareaName != null && 
				!newSymbolicSubareaName.equals(oldSymbolicSubareaName);
		
	}

}
