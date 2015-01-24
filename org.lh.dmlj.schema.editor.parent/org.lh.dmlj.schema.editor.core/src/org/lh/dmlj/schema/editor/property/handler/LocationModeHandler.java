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

import java.util.List;
import java.util.MissingResourceException;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.PluginPropertiesCache;
import org.lh.dmlj.schema.editor.command.ChangeCalcKeyCommand;
import org.lh.dmlj.schema.editor.command.ChangeViaSpecificationCommand;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.MakeRecordCalcCommand;
import org.lh.dmlj.schema.editor.command.MakeRecordDirectCommand;
import org.lh.dmlj.schema.editor.command.MakeRecordViaCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeCompoundCommand;
import org.lh.dmlj.schema.editor.command.SetObjectAttributeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
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
		
		// create and open the dialog for maintaining a record's location mode 
		// data; if the user presses the cancel button, get out and return a
		// null Command
		LocationModeDialog dialog = 
			new LocationModeDialog(Display.getCurrent().getActiveShell(),
								   recordProvider.getRecord());
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			// cancel button pressed
			return null;
		}
		
		// the fact that the user was able to press the OK button means that
		// he has effectively changed something; get the record from the 
		// IRecordProvider
		SchemaRecord record = recordProvider.getRecord();
		
		// depending on the record's current location mode, assemble a command
		// to be executed on the commandstack and return it to the caller		
		if (record.getLocationMode() == LocationMode.CALC) {
			// the record is currently CALC
			if (dialog.getLocationMode() == LocationMode.CALC) {
				// 1 or more CALC key attributes have changed; if it's only the
				// duplicates option that has been changed, change it directly;
				// if there are changes to the CALC key elements, use a compound
				// command to deal with the request
				boolean calcKeyElementsChanged = false;
				List<KeyElement> oldCalcKeyElements =
					record.getCalcKey().getElements();
				List<Element> newCalcKeyElements =
					dialog.getCalcKeyElements();
				if (newCalcKeyElements.size() != oldCalcKeyElements.size()) {					
					calcKeyElementsChanged = true;
				} else {
					for (int i = 0; i < newCalcKeyElements.size(); i++) {
						Element oldElement = 
							oldCalcKeyElements.get(i).getElement();
						Element newElement = newCalcKeyElements.get(i);
						if (newElement != oldElement) {
							calcKeyElementsChanged = true;
							break;
						}
					}
				}
				ModelChangeContext context = new ModelChangeContext(ModelChangeType.CHANGE_CALCKEY);
				context.putContextData(record);
				if (!calcKeyElementsChanged) {
					// only the duplicates option has changed
					String attributeLabel;
					try {
						String key = 
							"label.org.lh.dmlj.schema.Key.duplicatesOption";
						attributeLabel = PluginPropertiesCache.get(Plugin.getDefault(), key);
					} catch (MissingResourceException e) {
						throw new RuntimeException(e);
					}
					IModelChangeCommand command =  
						new SetObjectAttributeCommand(record.getCalcKey(), 
													  SchemaPackage.eINSTANCE.getKey_DuplicatesOption(), 
													  dialog.getDuplicatesOption(), attributeLabel);
					command.setContext(context);
					return (Command) command;
				} else {
					// the CALC key elements and maybe also the duplicates 
					// option have changed
					IModelChangeCommand command = 
						new ChangeCalcKeyCommand(record, dialog.getCalcKeyElements(), 
												 dialog.getDuplicatesOption());
					command.setContext(context);
					return (Command) command;
				}
			} else if (dialog.getLocationMode() == LocationMode.DIRECT) {
				// the record has to be made DIRECT
				ModelChangeContext context = 
					new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
				context.putContextData(record);
				IModelChangeCommand command = new MakeRecordDirectCommand(record);
				command.setContext(context);
				return (Command) command;
			} else {				
				// the record has to be made VIA; we use a compound command to
				// achieve this
				Command makeRecordDirectCommand = 
					new MakeRecordDirectCommand(record);
				Command makeRecordViaCommand =
					new MakeRecordViaCommand(record, dialog.getViaSetName(), 
							 				 dialog.getSymbolicDisplacementName(), 
							 				 dialog.getDisplacementPageCount());						
				ModelChangeContext context = 
						new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
					context.putContextData(record);
				ModelChangeCompoundCommand cc = new ModelChangeCompoundCommand();
			    cc.setLabel(makeRecordViaCommand.getLabel());
			    cc.setContext(context);
		        cc.add(makeRecordDirectCommand);
			    cc.add(makeRecordViaCommand);
			    return cc;
			}
		} else if (record.getLocationMode() == LocationMode.DIRECT) {
			// the record is currently DIRECT, its location mode must be 
			// changed to either CALC or VIA since nothing else is stored for
			// this kind of location mode
			ModelChangeContext context = 
				new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
			context.putContextData(record);
			if (dialog.getLocationMode() == LocationMode.CALC) {
				// the record has to be made CALC
				IModelChangeCommand command = 
					new MakeRecordCalcCommand(record, dialog.getCalcKeyElements(), 
											  dialog.getDuplicatesOption());
				command.setContext(context);
				return (Command) command;
			} else if (dialog.getLocationMode() == LocationMode.DIRECT) {
				// cannot happen but report it if it does
				throw new RuntimeException("logic error: no changes for a " +
										   "record that is already DIRECT");
			} else {
				// the record has to be made VIA
				IModelChangeCommand command = 
					new MakeRecordViaCommand(record, dialog.getViaSetName(), 
										     dialog.getSymbolicDisplacementName(), 
											 dialog.getDisplacementPageCount());
				command.setContext(context);
				return (Command) command;
			}
		} else {
			// the record is currently VIA
			if (dialog.getLocationMode() == LocationMode.CALC) {
				// the record has to be made CALC; we use a compound command to
				// achieve this
				Command makeRecordDirectCommand = 
					new MakeRecordDirectCommand(record);
				Command makeRecordCalcCommand =
					new MakeRecordCalcCommand(record, 
											  dialog.getCalcKeyElements(), 
											  dialog.getDuplicatesOption());						
				
			    ModelChangeContext context = 
			    	new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
			    context.putContextData(record);
				ModelChangeCompoundCommand cc = new ModelChangeCompoundCommand();
			    cc.setLabel(makeRecordCalcCommand.getLabel());
			    cc.setContext(context);
		        cc.add(makeRecordDirectCommand);
			    cc.add(makeRecordCalcCommand);
			    return cc;
			} else if (dialog.getLocationMode() == LocationMode.DIRECT) {
				// the record has to be made DIRECT
				ModelChangeContext context = 
					new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
				context.putContextData(record);
				IModelChangeCommand command = new MakeRecordDirectCommand(record);
				command.setContext(context);
				return (Command) command;
			} else {
				// 1 or more VIA specification attributes have changed, only if the VIA set has 
				// changed, change the whole ViaSpecification (the model change will always be of
				// type CHANGE_VIA_SPECIFICTION though)
				ModelChangeContext context = 
					new ModelChangeContext(ModelChangeType.CHANGE_VIA_SPECIFICATION);
				context.putContextData(record);
				if (!dialog.getViaSetName().equals(record.getViaSpecification().getSet().getName())) {					
					// the VIA set has changed; replace the whole // ViaSpecification
					IModelChangeCommand command = 
						new ChangeViaSpecificationCommand(record, dialog.getViaSetName(), 
														  dialog.getSymbolicDisplacementName(), 
														  dialog.getDisplacementPageCount());
					command.setContext(context);
					return (Command) command;
				} else {
					// only the displacement specification has changed
					if (dialog.getSymbolicDisplacementName() == null &&
						dialog.getDisplacementPageCount() == null) {
						
						// no displacement specification
						if (record.getViaSpecification().getSymbolicDisplacementName() != null) {
							// nullify the existing symbolic displacement name
							String attributeLabel;
							try {
								String key = 
									"label.org.lh.dmlj.schema.ViaSpecification." +
									"symbolicDisplacementName";
								attributeLabel = 
									PluginPropertiesCache.get(Plugin.getDefault(), key);
							} catch (MissingResourceException e) {
								throw new RuntimeException(e);
							}
							IModelChangeCommand command = 
								new SetObjectAttributeCommand(record.getViaSpecification(), 
															  SchemaPackage.eINSTANCE
															  			   .getViaSpecification_SymbolicDisplacementName(),
															  dialog.getSymbolicDisplacementName(),
															  attributeLabel);
							command.setContext(context);
							return (Command) command;
						} else if (record.getViaSpecification().getDisplacementPageCount() != null) {
							// nullify the existing displacement page count							
							String attributeLabel;
							try {
								String key = 
									"label.org.lh.dmlj.schema.ViaSpecification." +
									"displacementPageCount";
								attributeLabel = 
									PluginPropertiesCache.get(Plugin.getDefault(), key);
							} catch (MissingResourceException e) {
								throw new RuntimeException(e);
							}
							IModelChangeCommand command = 
								new SetObjectAttributeCommand(record.getViaSpecification(), 
														      SchemaPackage.eINSTANCE
															  		       .getViaSpecification_DisplacementPageCount(),
															  dialog.getSymbolicDisplacementName(),
															  attributeLabel);	
							command.setContext(context);
							return (Command) command;
						} else {
							String message = "logic error: no displacement " +
											 "specification was set";
							throw new RuntimeException(message);
						}
					} else if (dialog.getSymbolicDisplacementName() != null) {
						// symbolic displacement name specified
						String attributeLabel;
						try {
							String key = 
								"label.org.lh.dmlj.schema.ViaSpecification.symbolicDisplacementName";
							attributeLabel = PluginPropertiesCache.get(Plugin.getDefault(), key);
						} catch (MissingResourceException e) {
							throw new RuntimeException(e);
						}
						SetObjectAttributeCommand setSymbolicDisplacementCommand = 
							new SetObjectAttributeCommand(record.getViaSpecification(), 
														  SchemaPackage.eINSTANCE
														  		       .getViaSpecification_SymbolicDisplacementName(), 
														  dialog.getSymbolicDisplacementName(), 
														  attributeLabel);
						if (record.getViaSpecification().getDisplacementPageCount() == null) {
							setSymbolicDisplacementCommand.setContext(context);
							return (Command) setSymbolicDisplacementCommand;
						} else {
							// the displacement page count is currently set, so we need to nullify
							// it
							SetObjectAttributeCommand nullifyDisplacementCountCommand = 
								new SetObjectAttributeCommand(record.getViaSpecification(), 
															  SchemaPackage.eINSTANCE
															  		       .getViaSpecification_DisplacementPageCount(), 
															  null, null);
							ModelChangeCompoundCommand compoundCommand =
								new ModelChangeCompoundCommand(setSymbolicDisplacementCommand.getLabel());
							compoundCommand.setContext(context);
							compoundCommand.add(nullifyDisplacementCountCommand);
							compoundCommand.add(setSymbolicDisplacementCommand);
							return (Command) compoundCommand;
						}
					} else if (dialog.getDisplacementPageCount() != null) {
						// displacement pages specified
						String attributeLabel;
						try {
							String key = 
								"label.org.lh.dmlj.schema.ViaSpecification.displacementPageCount";
							attributeLabel = PluginPropertiesCache.get(Plugin.getDefault(), key);
						} catch (MissingResourceException e) {
							throw new RuntimeException(e);
						}
						SetObjectAttributeCommand setDisplacementCountCommand = 
							new SetObjectAttributeCommand(record.getViaSpecification(), 
														  SchemaPackage.eINSTANCE
														  		       .getViaSpecification_DisplacementPageCount(), 
														  dialog.getDisplacementPageCount(), 
														  attributeLabel);
						if (record.getViaSpecification().getSymbolicDisplacementName() == null) {
							setDisplacementCountCommand.setContext(context);
							return (Command) setDisplacementCountCommand;
						} else {
							// the symbolic displacement is currently set, so we need to nullify it
							SetObjectAttributeCommand nullifySymbolicDisplacementCommand = 
								new SetObjectAttributeCommand(record.getViaSpecification(), 
															  SchemaPackage.eINSTANCE
															  		       .getViaSpecification_SymbolicDisplacementName(), 
															  null, null);
							ModelChangeCompoundCommand compoundCommand =
								new ModelChangeCompoundCommand(setDisplacementCountCommand.getLabel());
							compoundCommand.setContext(context);
							compoundCommand.add(nullifySymbolicDisplacementCommand);
							compoundCommand.add(setDisplacementCountCommand);
							return (Command) compoundCommand;
						}
					} else {
						String message = "logic error: displacement " +
										 "specification not changed";
						throw new RuntimeException(message);
					}
				}
			}
		}		
		
	}

}
