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
package org.lh.dmlj.schema.editor.command;

import java.util.List;
import java.util.MissingResourceException;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.PluginPropertiesCache;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;

public class ChangeLocationModeCommandFactory {

	public ChangeLocationModeCommandFactory() {
		super();
	}
	
	public IModelChangeCommand getCommand(SchemaRecord record, 
										  ILocationModeDetailsProvider locationModeDetailsProvider) {		
		if (record.getLocationMode() == LocationMode.CALC) {
			return getCommandForCalcRecord(record, locationModeDetailsProvider);
		} else if (record.getLocationMode() == LocationMode.DIRECT) {
			return getCommandForDirectRecord(record, locationModeDetailsProvider);
		} else if (record.getLocationMode() == LocationMode.VIA) {
			return getCommandForViaRecord(record, locationModeDetailsProvider);
		} else if (record.getLocationMode() == LocationMode.VSAM) {
			// TODO return getCommandForVsamRecord(record, locationModeDetailsProvider);
		} else if (record.getLocationMode() == LocationMode.VSAM_CALC) {
			// TODO return getCommandForVsamCalcRecord(record, locationModeDetailsProvider);
		} else {
			String message = "record has invalid location mode: " + record.getLocationMode();
			throw new IllegalArgumentException(message);
		}
		return null; // TODO remove this line
	}

	private IModelChangeCommand getCommandForCalcRecord(SchemaRecord record, 
			 											ILocationModeDetailsProvider locationModeDetailsProvider) {
		
		// the record is currently CALC
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {
			// 1 or more CALC key attributes have changed; if it's only the duplicates option that 
			// has been changed, change it directly; if there are changes to the CALC key elements, 
			// use a compound command to deal with the request
			boolean calcKeyElementsChanged = false;
			List<KeyElement> oldCalcKeyElements = record.getCalcKey().getElements();
			List<Element> newCalcKeyElements = locationModeDetailsProvider.getCalcKeyElements();
			if (newCalcKeyElements.size() != oldCalcKeyElements.size()) {					
				calcKeyElementsChanged = true;
			} else {
				for (int i = 0; i < newCalcKeyElements.size(); i++) {
					Element oldElement = oldCalcKeyElements.get(i).getElement();
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
					String key = "label.org.lh.dmlj.schema.Key.duplicatesOption";
					attributeLabel = PluginPropertiesCache.get(Plugin.getDefault(), key);
				} catch (MissingResourceException e) {
					throw new RuntimeException(e);
				}
				IModelChangeCommand command =  
					new SetObjectAttributeCommand(record.getCalcKey(), 
												  SchemaPackage.eINSTANCE.getKey_DuplicatesOption(), 
												  locationModeDetailsProvider.getDuplicatesOption(), 
												  attributeLabel);
				command.setContext(context);
				return command;
			} else {
				// the CALC key elements and maybe also the duplicates option have changed
				IModelChangeCommand command = 
					new ChangeCalcKeyCommand(record, 
											 locationModeDetailsProvider.getCalcKeyElements(), 
											 locationModeDetailsProvider.getDuplicatesOption());
				command.setContext(context);
				return command;
			}
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// the record has to be made DIRECT
			ModelChangeContext context = 
				new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
			context.putContextData(record);
			IModelChangeCommand command = new MakeRecordDirectCommand(record);
			command.setContext(context);
			return command;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {				
			// the record has to be made VIA; we use a compound command to achieve this
			Command makeRecordDirectCommand = new MakeRecordDirectCommand(record);
			Command makeRecordViaCommand =
				new MakeRecordViaCommand(record, locationModeDetailsProvider.getViaSetName(), 
						 				 locationModeDetailsProvider.getSymbolicDisplacementName(), 
						 				 locationModeDetailsProvider.getDisplacementPageCount());						
			ModelChangeContext context = 
				new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
			context.putContextData(record);
			ModelChangeCompoundCommand cc = new ModelChangeCompoundCommand();
		    cc.setLabel(makeRecordViaCommand.getLabel());
		    cc.setContext(context);
	        cc.add(makeRecordDirectCommand);
		    cc.add(makeRecordViaCommand);
		    return cc;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			return null; // TODO handle the situation for changing a record from CALC to VSAM			
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {
			return null; // TODO handle the situation for changing a record from CALC to VSAM CALC
		} else {
			String message = "invalid target location mode: " + 
							 locationModeDetailsProvider.getLocationMode();
			throw new IllegalArgumentException(message);
		}
	}

	private IModelChangeCommand getCommandForDirectRecord(SchemaRecord record, 
			  											  ILocationModeDetailsProvider locationModeDetailsProvider) {
		
		// the record is currently DIRECT, its location mode must be changed to either CALC or VIA 
		// since nothing else is stored for this kind of location mode
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
		context.putContextData(record);
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {
			// the record has to be made CALC
			IModelChangeCommand command = 
				new MakeRecordCalcCommand(record, locationModeDetailsProvider.getCalcKeyElements(), 
										  locationModeDetailsProvider.getDuplicatesOption());
			command.setContext(context);
			return command;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// cannot happen but report it if it does
			throw new IllegalArgumentException("no changes for a record that is already DIRECT");
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {
			// the record has to be made VIA
			IModelChangeCommand command = 
				new MakeRecordViaCommand(record, locationModeDetailsProvider.getViaSetName(), 
									     locationModeDetailsProvider.getSymbolicDisplacementName(), 
										 locationModeDetailsProvider.getDisplacementPageCount());
			command.setContext(context);
			return command;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			return null; // TODO handle the situation for changing a record from DIRECT to VSAM
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {
			return null; // TODO handle the situation for changing a record from DIRECT to VSAM CALC
		} else {
			String message = "invalid target location mode: " + 
							 locationModeDetailsProvider.getLocationMode();
			throw new IllegalArgumentException(message);
		}		
	}

	private IModelChangeCommand getCommandForViaRecord(SchemaRecord record, 
			  										   ILocationModeDetailsProvider locationModeDetailsProvider) {
		
		// the record is currently VIA
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {
			// the record has to be made CALC; we use a compound command to achieve this
			Command makeRecordDirectCommand = 
				new MakeRecordDirectCommand(record);
			Command makeRecordCalcCommand =
				new MakeRecordCalcCommand(record, locationModeDetailsProvider.getCalcKeyElements(), 
										  locationModeDetailsProvider.getDuplicatesOption());						
			
		    ModelChangeContext context = 
		    	new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
		    context.putContextData(record);
			ModelChangeCompoundCommand cc = new ModelChangeCompoundCommand();
		    cc.setLabel(makeRecordCalcCommand.getLabel());
		    cc.setContext(context);
	        cc.add(makeRecordDirectCommand);
		    cc.add(makeRecordCalcCommand);
		    return cc;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// the record has to be made DIRECT
			ModelChangeContext context = 
				new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
			context.putContextData(record);
			IModelChangeCommand command = new MakeRecordDirectCommand(record);
			command.setContext(context);
			return command;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {
			// 1 or more VIA specification attributes have changed, only if the VIA set has changed, 
			// change the whole ViaSpecification (the model change will always be of type 
			// CHANGE_VIA_SPECIFICTION though)
			ModelChangeContext context = 
				new ModelChangeContext(ModelChangeType.CHANGE_VIA_SPECIFICATION);
			context.putContextData(record);
			if (!locationModeDetailsProvider.getViaSetName().equals(record.getViaSpecification().getSet().getName())) {					
				// the VIA set has changed; replace the whole // ViaSpecification
				IModelChangeCommand command = 
					new ChangeViaSpecificationCommand(record, locationModeDetailsProvider.getViaSetName(), 
													  locationModeDetailsProvider.getSymbolicDisplacementName(), 
													  locationModeDetailsProvider.getDisplacementPageCount());
				command.setContext(context);
				return command;
			} else {
				// only the displacement specification has changed
				if (locationModeDetailsProvider.getSymbolicDisplacementName() == null &&
					locationModeDetailsProvider.getDisplacementPageCount() == null) {
					
					// no displacement specification
					if (record.getViaSpecification().getSymbolicDisplacementName() != null) {
						// nullify the existing symbolic displacement name
						String attributeLabel;
						try {
							String key = 
								"label.org.lh.dmlj.schema.ViaSpecification.symbolicDisplacementName";
							attributeLabel = PluginPropertiesCache.get(Plugin.getDefault(), key);
						} catch (MissingResourceException e) {
							throw new RuntimeException(e);
						}
						IModelChangeCommand command = 
							new SetObjectAttributeCommand(record.getViaSpecification(), 
														  SchemaPackage.eINSTANCE
														  			   .getViaSpecification_SymbolicDisplacementName(),
														  locationModeDetailsProvider.getSymbolicDisplacementName(),
														  attributeLabel);
						command.setContext(context);
						return command;
					} else if (record.getViaSpecification().getDisplacementPageCount() != null) {
						// nullify the existing displacement page count							
						String attributeLabel;
						try {
							String key = 
								"label.org.lh.dmlj.schema.ViaSpecification.displacementPageCount";
							attributeLabel = PluginPropertiesCache.get(Plugin.getDefault(), key);
						} catch (MissingResourceException e) {
							throw new RuntimeException(e);
						}
						IModelChangeCommand command = 
							new SetObjectAttributeCommand(record.getViaSpecification(), 
													      SchemaPackage.eINSTANCE
														  		       .getViaSpecification_DisplacementPageCount(),
														  locationModeDetailsProvider.getSymbolicDisplacementName(),
														  attributeLabel);	
						command.setContext(context);
						return command;
					} else {
						String message = "no displacement specification was set";
						throw new IllegalArgumentException(message);
					}
				} else if (locationModeDetailsProvider.getSymbolicDisplacementName() != null) {
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
													  locationModeDetailsProvider.getSymbolicDisplacementName(), 
													  attributeLabel);
					if (record.getViaSpecification().getDisplacementPageCount() == null) {
						setSymbolicDisplacementCommand.setContext(context);
						return setSymbolicDisplacementCommand;
					} else {
						// the displacement page count is currently set, so we need to nullify it
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
						return compoundCommand;
					}
				} else if (locationModeDetailsProvider.getDisplacementPageCount() != null) {
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
													  locationModeDetailsProvider.getDisplacementPageCount(), 
													  attributeLabel);
					if (record.getViaSpecification().getSymbolicDisplacementName() == null) {
						setDisplacementCountCommand.setContext(context);
						return setDisplacementCountCommand;
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
						return compoundCommand;
					}
				} else {
					String message = "displacement specification not changed";
					throw new IllegalArgumentException(message);
				}
			}
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			String message = "cannot change a record from VIA to VSAM";
			throw new IllegalArgumentException(message);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {
			String message = "cannot change a record from VIA to VSAM CALC";
			throw new IllegalArgumentException(message);
		} else {
			String message = "invalid target location mode: " + 
							 locationModeDetailsProvider.getLocationMode();
			throw new IllegalArgumentException(message);
		}
	}
	
}
