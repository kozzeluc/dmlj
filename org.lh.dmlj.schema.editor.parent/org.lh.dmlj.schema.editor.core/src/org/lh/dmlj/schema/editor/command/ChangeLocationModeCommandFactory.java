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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.IPluginPropertiesProvider;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.PluginPropertiesCache;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;

/**
 * Important note: when changing a record's location mode, it's important to always first make it
 * DIRECT, after which it can be made whatever; if a record is already DIRECT or has to be made 
 * DIRECT, a simple command can do the job, but if the record is CALC, VIA, VSAM or VSAM CALC and 
 * has to be made anything other than DIRECT, a compound command is needed; the first command in the
 * list of the compound command's commands should always be a MakeRecordDirectCommand.  The only
 * EXCEPTION to this rule is when the record is VSAM and has to be made VSAM CALC or when it is
 * VSAM CALC and has to be made VSAM; it would be bad practice to make it DIRECT (albeit 
 * temporarily) because a VSAM path might be defined on the record.  For that reason, the 'make 
 * record VSAM' and 'make record VSAM CALC' commands will be able to start with a VSAM CALC and VSAM 
 * record respectively.
 */
public class ChangeLocationModeCommandFactory {
	
	private IPluginPropertiesProvider pluginPropertiesProvider;

	public ChangeLocationModeCommandFactory() {
		super();
	}
	
	private String getAttributeLabel(EAttribute attribute) {
		// example result: "label.org.lh.dmlj.schema.Key.duplicatesOption"
		try {
			String key = "label." + attribute.getEContainingClass().getEPackage().getNsPrefix() + 
						 "." + attribute.getEContainingClass().getName() + "." + attribute.getName();
			if (getPluginPropertiesProvider() != null) {
				return getPluginPropertiesProvider().getProperty(key);
			} else {
				return PluginPropertiesCache.get(Plugin.getDefault(), key);
			}
		} catch (MissingResourceException e) {
			throw new IllegalArgumentException("no label for " + attribute);
		}
	}

	private ChangeCalcKeyCommand getChangeCalcKeyCommand(SchemaRecord record,
														 ILocationModeDetailsProvider detailsProvider,
														 ModelChangeType modelChangeType) {
		ChangeCalcKeyCommand command = 
			new ChangeCalcKeyCommand(record, detailsProvider.getCalcKeyElements(), 
									 detailsProvider.getDuplicatesOption());
		
		if (modelChangeType != null) {
			ModelChangeContext context = new ModelChangeContext(modelChangeType);
			context.putContextData(record);
			command.setContext(context);
		}
		
		return command;
	}
	
	private ChangeViaSpecificationCommand getChangeViaSpecificationCommand(SchemaRecord record,
			 ILocationModeDetailsProvider detailsProvider,
			 ModelChangeType modelChangeType) {
		
		ChangeViaSpecificationCommand command = 
			new ChangeViaSpecificationCommand(record, detailsProvider.getViaSetName(), 
											  detailsProvider.getSymbolicDisplacementName(), 
											  detailsProvider.getDisplacementPageCount());
		
		if (modelChangeType != null) {
			ModelChangeContext context = new ModelChangeContext(modelChangeType);
			context.putContextData(record);
			command.setContext(context);
		}
		
		return command;
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
			return getCommandForVsamRecord(record, locationModeDetailsProvider);
		} else if (record.getLocationMode() == LocationMode.VSAM_CALC) {
			return getCommandForVsamCalcRecord(record, locationModeDetailsProvider);
		} else {
			String message = "record has invalid location mode: " + record.getLocationMode();
			throw new IllegalArgumentException(message);
		}
	}

	private IModelChangeCommand getCommandForCalcRecord(SchemaRecord record, 
			 											ILocationModeDetailsProvider locationModeDetailsProvider) {
		
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {
			// 1 or more CALC key attributes have changed; if it's only the duplicates option that 
			// has been changed, change it directly; if there are changes to the CALC key elements, 
			// use a change CALC key command to deal with the request
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
			if (!calcKeyElementsChanged) {
				// only the duplicates option has changed
				SetObjectAttributeCommand command = 
					getSetObjectAttributeCommand(record, record.getCalcKey(), 
												 SchemaPackage.eINSTANCE.getKey_DuplicatesOption(), 
												 locationModeDetailsProvider.getDuplicatesOption(), 
												 ModelChangeType.CHANGE_CALCKEY);
				return command;
			} else {
				// the CALC key elements and maybe also the duplicates option have changed
				ChangeCalcKeyCommand command = 
					getChangeCalcKeyCommand(record, locationModeDetailsProvider, 
										    ModelChangeType.CHANGE_CALCKEY);
				return command;
			}
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// the record has to be made DIRECT
			return getMakeRecordDirectCommand(record, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {				
			// the record has to be made VIA; we use a compound command to achieve this
			Command makeRecordDirectCommand = getMakeRecordDirectCommand(record, null);
			Command makeRecordViaCommand = 
				getMakeRecordViaCommand(record, locationModeDetailsProvider, null);
		    ModelChangeCompoundCommand cc = 
		    	getCompoundCommand(record, ModelChangeType.CHANGE_LOCATION_MODE, 
		    					   makeRecordDirectCommand, makeRecordViaCommand);
		    return cc;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			// the record has to be made VSAM; we use a compound command to achieve this
			MakeRecordDirectCommand makeRecordDirectCommand = 
				getMakeRecordDirectCommand(record, null);
			MakeRecordVsamCommand makeRecordVsamCommand = getMakeRecordVsamCommand(record, null);	
			ModelChangeCompoundCommand cc = 
			    getCompoundCommand(record, ModelChangeType.CHANGE_LOCATION_MODE, 
			   					   makeRecordDirectCommand, makeRecordVsamCommand);
			return cc;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {
			// the record has to be made VSAM CALC; we use a compound command to achieve this
			MakeRecordDirectCommand makeRecordDirectCommand = 
				getMakeRecordDirectCommand(record, null);
			MakeRecordVsamCalcCommand makeRecordVsamCalcCommand = 
				getMakeRecordVsamCalcCommand(record, locationModeDetailsProvider, null);
			ModelChangeCompoundCommand cc = 
		    	getCompoundCommand(record, ModelChangeType.CHANGE_LOCATION_MODE, 
		    					   makeRecordDirectCommand, makeRecordVsamCalcCommand);
		    return cc;
		} else {
			String message = 
				"invalid target location mode: " + locationModeDetailsProvider.getLocationMode();
			throw new IllegalArgumentException(message);
		}
	}

	private IModelChangeCommand getCommandForDirectRecord(SchemaRecord record, 
			  											  ILocationModeDetailsProvider locationModeDetailsProvider) {
		
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {
			// the record has to be made CALC
			MakeRecordCalcCommand command = 
				getMakeRecordCalcCommand(record, locationModeDetailsProvider, 
										 ModelChangeType.CHANGE_LOCATION_MODE);
			return command;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// cannot happen but report it if it does
			throw new IllegalArgumentException("no changes for a record that is already DIRECT");
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {
			// the record has to be made VIA
			IModelChangeCommand command = 
				getMakeRecordViaCommand(record, locationModeDetailsProvider, 
										ModelChangeType.CHANGE_LOCATION_MODE);
			return command;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			// the record has to be made VSAM
			MakeRecordVsamCommand command = 
				getMakeRecordVsamCommand(record, ModelChangeType.CHANGE_LOCATION_MODE);
			return command; 
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {
			// the record has to be made VSAM CALC
			MakeRecordVsamCalcCommand command = 
				getMakeRecordVsamCalcCommand(record, locationModeDetailsProvider, 
										 	 ModelChangeType.CHANGE_LOCATION_MODE);
			return command;
		} else {
			String message = "invalid target location mode: " + 
							 locationModeDetailsProvider.getLocationMode();
			throw new IllegalArgumentException(message);
		}		
	}

	private IModelChangeCommand getCommandForViaRecord(SchemaRecord record, 
			  										   ILocationModeDetailsProvider locationModeDetailsProvider) {
		
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {
			// the record has to be made CALC; we use a compound command to achieve this
			MakeRecordDirectCommand makeRecordDirectCommand = 
				getMakeRecordDirectCommand(record, null);
			MakeRecordCalcCommand makeRecordCalcCommand =
				getMakeRecordCalcCommand(record, locationModeDetailsProvider, null);		    
			ModelChangeCompoundCommand cc = 
				getCompoundCommand(record, ModelChangeType.CHANGE_LOCATION_MODE, 
								   makeRecordDirectCommand, makeRecordCalcCommand);		    
		    return cc;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// the record has to be made DIRECT
			return getMakeRecordDirectCommand(record, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {
			if (!locationModeDetailsProvider.getViaSetName()
											.equals(record.getViaSpecification()
														  .getSet().getName())) {
				
				// the VIA set (and possibly the displacement specification) has changed; replace 
				// the whole ViaSpecification
				ChangeViaSpecificationCommand command = 
					getChangeViaSpecificationCommand(record, locationModeDetailsProvider, 
													 ModelChangeType.CHANGE_VIA_SPECIFICATION);
				return command;
			} else {
				// only the displacement specification has changed
				if (locationModeDetailsProvider.getSymbolicDisplacementName() == null &&
					locationModeDetailsProvider.getDisplacementPageCount() == null) {
					
					// no displacement specification
					if (record.getViaSpecification().getSymbolicDisplacementName() != null) {
						// nullify the existing symbolic displacement name
						SetObjectAttributeCommand command = 
							getSetObjectAttributeCommand(record, record.getViaSpecification(), 
														 SchemaPackage.eINSTANCE
														 			  .getViaSpecification_SymbolicDisplacementName(), 
														 null, ModelChangeType.CHANGE_VIA_SPECIFICATION);
						return command;
					} else if (record.getViaSpecification().getDisplacementPageCount() != null) {						
						// nullify the existing displacement page count
						SetObjectAttributeCommand command = 
							getSetObjectAttributeCommand(record, record.getViaSpecification(), 
														 SchemaPackage.eINSTANCE
														 			  .getViaSpecification_DisplacementPageCount(), 
														 null, ModelChangeType.CHANGE_VIA_SPECIFICATION);
						return command;
					} else {
						String message = "no displacement specification was set";
						throw new IllegalArgumentException(message);
					}
				} else if (locationModeDetailsProvider.getSymbolicDisplacementName() != null) {
					// symbolic displacement name specified
					if (record.getViaSpecification().getDisplacementPageCount() == null) {
						SetObjectAttributeCommand command = 
							getSetObjectAttributeCommand(record, record.getViaSpecification(), 
														 SchemaPackage.eINSTANCE
														 			  .getViaSpecification_SymbolicDisplacementName(), 
														 locationModeDetailsProvider.getSymbolicDisplacementName(), 
														 ModelChangeType.CHANGE_VIA_SPECIFICATION);
						return command;
					} else {
						// the displacement page count is currently set, so we need to nullify it
						SetObjectAttributeCommand nullifyDisplacementCountCommand = 
							getSetObjectAttributeCommand(record, record.getViaSpecification(), 
														 SchemaPackage.eINSTANCE
														 			  .getViaSpecification_DisplacementPageCount(), 
														 null, null);
						SetObjectAttributeCommand setSymbolicDisplacementCommand = 
							getSetObjectAttributeCommand(record, record.getViaSpecification(), 
														 SchemaPackage.eINSTANCE
														 			  .getViaSpecification_SymbolicDisplacementName(), 
														 locationModeDetailsProvider.getSymbolicDisplacementName(), 
														 null);
						ModelChangeBasicCommand[] commands = 
							new ModelChangeBasicCommand[] {nullifyDisplacementCountCommand,
														   setSymbolicDisplacementCommand};
						ModelChangeCompoundCommand compoundCommand =
							getCompoundCommand(record, ModelChangeType.CHANGE_VIA_SPECIFICATION, 
											   commands);	
						return compoundCommand;
					}
				} else {
					// displacement pages specified
					if (record.getViaSpecification().getSymbolicDisplacementName() == null) {
						SetObjectAttributeCommand setDisplacementCountCommand =
							getSetObjectAttributeCommand(record, record.getViaSpecification(), 
														 SchemaPackage.eINSTANCE
														 			  .getViaSpecification_DisplacementPageCount(), 
														 locationModeDetailsProvider.getDisplacementPageCount(), 
														 ModelChangeType.CHANGE_VIA_SPECIFICATION);
						return setDisplacementCountCommand;
					} else {
						// the symbolic displacement is currently set, so we need to nullify it
						SetObjectAttributeCommand nullifySymbolicDisplacementCommand =
							getSetObjectAttributeCommand(record, record.getViaSpecification(), 
														 SchemaPackage.eINSTANCE
														 			  .getViaSpecification_SymbolicDisplacementName(), 
														 null, null);
						SetObjectAttributeCommand setDisplacementCountCommand =
							getSetObjectAttributeCommand(record, record.getViaSpecification(), 
														 SchemaPackage.eINSTANCE
														 			  .getViaSpecification_DisplacementPageCount(), 
														 locationModeDetailsProvider.getDisplacementPageCount(), 
														 null);							
						ModelChangeBasicCommand[] commands = 
							new ModelChangeBasicCommand[] {nullifySymbolicDisplacementCommand,
								setDisplacementCountCommand};
						ModelChangeCompoundCommand compoundCommand =
							getCompoundCommand(record, ModelChangeType.CHANGE_VIA_SPECIFICATION, 
											   commands);	
						return compoundCommand;
					}
				}
			}
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			String message = "cannot change a record from VIA to VSAM";
			throw new IllegalArgumentException(message);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {
			String message = "cannot change a record from VIA to VSAM CALC";
			throw new IllegalArgumentException(message);
		} else {
			String message = 
				"invalid target location mode: " + locationModeDetailsProvider.getLocationMode();
			throw new IllegalArgumentException(message);
		}
	}

	private IModelChangeCommand getCommandForVsamRecord(SchemaRecord record,
														ILocationModeDetailsProvider locationModeDetailsProvider) {
		
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {		
			// the record has to be made CALC; we use a compound command to achieve this
			MakeRecordDirectCommand makeRecordDirectCommand = 
				getMakeRecordDirectCommand(record, null);
			MakeRecordCalcCommand makeRecordCalcCommand =
				getMakeRecordCalcCommand(record, locationModeDetailsProvider, null);		    
			ModelChangeCompoundCommand cc = 
				getCompoundCommand(record, ModelChangeType.CHANGE_LOCATION_MODE, 
								   makeRecordDirectCommand, makeRecordCalcCommand);		    
		    return cc;
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// the record has to be made DIRECT
			return getMakeRecordDirectCommand(record, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {
			// we cannot make a VSAM record VIA because then it would be a member in at least 1 set
			String message = "cannot change a record from VSAM to VIA";
			throw new IllegalArgumentException(message);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			// cannot happen but report it if it does
			throw new IllegalArgumentException("no changes for a record that is already VSAM");
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {		
			// the record has to be made VSAM CALC
			MakeRecordVsamCalcCommand command = 
				getMakeRecordVsamCalcCommand(record, locationModeDetailsProvider, 
										 	 ModelChangeType.CHANGE_LOCATION_MODE);
			return command;
		} else {
			String message = "invalid target location mode: " + 
							 locationModeDetailsProvider.getLocationMode();
			throw new IllegalArgumentException(message);
		}
	}

	private IModelChangeCommand getCommandForVsamCalcRecord(SchemaRecord record,
															ILocationModeDetailsProvider locationModeDetailsProvider) {
		
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {
			// the record has to be made CALC; we use a compound command to achieve this
			MakeRecordDirectCommand makeRecordDirectCommand = 
				getMakeRecordDirectCommand(record, null);
			MakeRecordCalcCommand makeRecordCalcCommand =
				getMakeRecordCalcCommand(record, locationModeDetailsProvider, null);		    
			ModelChangeCompoundCommand cc = 
				getCompoundCommand(record, ModelChangeType.CHANGE_LOCATION_MODE, 
								   makeRecordDirectCommand, makeRecordCalcCommand);		    
		    return cc;			
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// the record has to be made DIRECT
			return getMakeRecordDirectCommand(record, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {
			// we cannot make a VSAM CALC record VIA because then it would be a member in at least 1 
			// set
			String message = "cannot change a record from VSAM CALC to VIA";
			throw new IllegalArgumentException(message);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			// the record has to be made VSAM
			MakeRecordVsamCommand command = 
				getMakeRecordVsamCommand(record, ModelChangeType.CHANGE_LOCATION_MODE);
			return command; 
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {			
			// 1 or more CALC key attributes have changed; if it's only the duplicates option that 
			// has been changed, change it directly; if there are changes to the CALC key elements, 
			// use a change CALC key command to deal with the request
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
			if (!calcKeyElementsChanged) {
				// only the duplicates option has changed
				SetObjectAttributeCommand command = 
					getSetObjectAttributeCommand(record, record.getCalcKey(), 
												 SchemaPackage.eINSTANCE.getKey_DuplicatesOption(), 
												 locationModeDetailsProvider.getDuplicatesOption(), 
												 ModelChangeType.CHANGE_CALCKEY);
				return command;
			} else {
				// the CALC key elements and maybe also the duplicates option have changed
				ChangeCalcKeyCommand command = 
					getChangeCalcKeyCommand(record, locationModeDetailsProvider, 
										    ModelChangeType.CHANGE_CALCKEY);
				return command;
			}			
		} else {
			String message = "invalid target location mode: " + 
							 locationModeDetailsProvider.getLocationMode();
			throw new IllegalArgumentException(message);
		}
	}

	private ModelChangeCompoundCommand getCompoundCommand(SchemaRecord record,
														  ModelChangeType modelChangeType, 
														  Command... commands) {
	
		ModelChangeCompoundCommand cc = new ModelChangeCompoundCommand();
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
		context.putContextData(record);
	    cc.setContext(context);
	    
	    for (Command command : commands) {
	    	cc.add(command);
	    }
	    Command lastCommand = (Command) cc.getCommands().get(cc.getCommands().size() - 1);
	    cc.setLabel(lastCommand.getLabel());
	    
	    return cc;
	}
	
	private MakeRecordCalcCommand getMakeRecordCalcCommand(SchemaRecord record,
														   ILocationModeDetailsProvider detailsProvider,
														   ModelChangeType modelChangeType) {
	
		MakeRecordCalcCommand command = 
			new MakeRecordCalcCommand(record, detailsProvider.getCalcKeyElements(), 
					 				  detailsProvider.getDuplicatesOption());
		
		if (modelChangeType != null) {
			ModelChangeContext context = new ModelChangeContext(modelChangeType);
			context.putContextData(record);
			command.setContext(context);
		}
		
		return command;
	}
	
	private MakeRecordDirectCommand getMakeRecordDirectCommand(SchemaRecord record,
															   ModelChangeType modelChangeType) {
		
		MakeRecordDirectCommand command = new MakeRecordDirectCommand(record);
		
		if (modelChangeType != null) {
			ModelChangeContext context = new ModelChangeContext(modelChangeType);
			context.putContextData(record);
			command.setContext(context);
		}
		
		return command;
	}

	private MakeRecordViaCommand getMakeRecordViaCommand(SchemaRecord record,
														 ILocationModeDetailsProvider detailsProvider,
														 ModelChangeType modelChangeType) {
	
		MakeRecordViaCommand command = 
			new MakeRecordViaCommand(record, detailsProvider.getViaSetName(), 
						 			 detailsProvider.getSymbolicDisplacementName(), 
						 			 detailsProvider.getDisplacementPageCount());
		
		if (modelChangeType != null) {
			ModelChangeContext context = new ModelChangeContext(modelChangeType);
			context.putContextData(record);
			command.setContext(context);
		}
		
		return command;
	}
	
	private MakeRecordVsamCalcCommand getMakeRecordVsamCalcCommand(SchemaRecord record,
			   												   	   ILocationModeDetailsProvider detailsProvider,
			   												   	   ModelChangeType modelChangeType) {
	
		MakeRecordVsamCalcCommand command = 
			new MakeRecordVsamCalcCommand(record, detailsProvider.getCalcKeyElements(), 
										  detailsProvider.getDuplicatesOption());
	
		if (modelChangeType != null) {
			ModelChangeContext context = new ModelChangeContext(modelChangeType);
			context.putContextData(record);
			command.setContext(context);
		}
	
		return command;
	}

	private MakeRecordVsamCommand getMakeRecordVsamCommand(SchemaRecord record,
			   											   ModelChangeType modelChangeType) {
		
		MakeRecordVsamCommand command = new MakeRecordVsamCommand(record);
		
		if (modelChangeType != null) {
			ModelChangeContext context = new ModelChangeContext(modelChangeType);
			context.putContextData(record);
			command.setContext(context);
		}
		
		return command;
	}

	private SetObjectAttributeCommand getSetObjectAttributeCommand(SchemaRecord record,
																   EObject attributeOwner, 
																   EAttribute attribute,
																   Object value,
																   ModelChangeType modelChangeType) {
	
		SetObjectAttributeCommand command =  
			new SetObjectAttributeCommand(attributeOwner, attribute, value, 
										  getAttributeLabel(attribute));
		
		if (modelChangeType != null) {
			ModelChangeContext context = new ModelChangeContext(modelChangeType);
			context.putContextData(record);
			command.setContext(context);
		}
		
		return command;
	}

	public IPluginPropertiesProvider getPluginPropertiesProvider() {
		return pluginPropertiesProvider;
	}

	public void setPluginPropertiesProvider(IPluginPropertiesProvider pluginPropertiesProvider) {
		this.pluginPropertiesProvider = pluginPropertiesProvider;
	}
	
}
