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
package org.lh.dmlj.schema.editor.command;

import java.util.MissingResourceException;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.IPluginPropertiesProvider;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.PluginPropertiesCache;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;

/**
 * Important note: when changing a record's location mode, it's important to always first make it DIRECT, after
 * which it can be made whatever; if a record is already DIRECT or has to be made DIRECT, a simple command can do
 * the job, but if the record is CALC, VIA, VSAM or VSAM CALC and has to be made anything other than DIRECT, a
 * compound command is needed; the first command in the list of the compound command's commands should always be
 * a MakeRecordDirectCommand.  The only EXCEPTION to this rule is when the record is VSAM and has to be made VSAM
 * CALC or when it is VSAM CALC and has to be made VSAM; it would be bad practice to make it DIRECT (albeit 
 * temporarily) because a VSAM path might be defined on the record.  For that reason, the 'make record VSAM' and
 * 'make record VSAM CALC' commands will be able to start with a VSAM CALC and VSAM record respectively.
 */
public class ChangeLocationModeCommandFactory {
	private static final String INVALID_TARGET_LOCATION_MODE = "invalid target location mode: ";
	
	private IPluginPropertiesProvider pluginPropertiesProvider;
	
	private String getAttributeLabel(EAttribute attribute) {
		// example result: "label.org.lh.dmlj.schema.Key.duplicatesOption"
		try {
			var key = "label." + attribute.getEContainingClass().getEPackage().getNsPrefix() + "." + 
					attribute.getEContainingClass().getName() + "." + attribute.getName();
			if (getPluginPropertiesProvider() != null) {
				return getPluginPropertiesProvider().getProperty(key);
			} else {
				return PluginPropertiesCache.get(Plugin.getDefault(), key);
			}
		} catch (MissingResourceException e) {
			throw new IllegalArgumentException("no label for " + attribute);
		}
	}

	private ChangeCalcKeyCommand getChangeCalcKeyCommand(SchemaRecord schemaRecord, 
			ILocationModeDetailsProvider detailsProvider, ModelChangeType modelChangeType) {
		
		var command = new ChangeCalcKeyCommand(schemaRecord, detailsProvider.getCalcKeyElements(), detailsProvider.getDuplicatesOption());
		if (modelChangeType != null) {
			var context = new ModelChangeContext(modelChangeType);
			context.putContextData(schemaRecord);
			command.setContext(context);
		}
		return command;
	}
	
	private ChangeViaSpecificationCommand getChangeViaSpecificationCommand(SchemaRecord schemaRecord, 
			ILocationModeDetailsProvider detailsProvider,ModelChangeType modelChangeType) {
		
		var command = new ChangeViaSpecificationCommand(schemaRecord, detailsProvider.getViaSetName(), 
				detailsProvider.getSymbolicDisplacementName(), detailsProvider.getDisplacementPageCount());
		if (modelChangeType != null) {
			var context = new ModelChangeContext(modelChangeType);
			context.putContextData(schemaRecord);
			command.setContext(context);
		}
		return command;
	}

	public IModelChangeCommand getCommand(SchemaRecord schemaRecord, ILocationModeDetailsProvider locationModeDetailsProvider) {		
		if (schemaRecord.getLocationMode() == LocationMode.CALC) {
			return getCommandForCalcRecord(schemaRecord, locationModeDetailsProvider);
		} else if (schemaRecord.getLocationMode() == LocationMode.DIRECT) {
			return getCommandForDirectRecord(schemaRecord, locationModeDetailsProvider);
		} else if (schemaRecord.getLocationMode() == LocationMode.VIA) {
			return getCommandForViaRecord(schemaRecord, locationModeDetailsProvider);
		} else if (schemaRecord.getLocationMode() == LocationMode.VSAM) {
			return getCommandForVsamRecord(schemaRecord, locationModeDetailsProvider);
		} else if (schemaRecord.getLocationMode() == LocationMode.VSAM_CALC) {
			return getCommandForVsamCalcRecord(schemaRecord, locationModeDetailsProvider);
		} else {
			var message = "record has invalid location mode: " + schemaRecord.getLocationMode();
			throw new IllegalArgumentException(message);
		}
	}

	private IModelChangeCommand getCommandForCalcRecord(SchemaRecord schemaRecord, ILocationModeDetailsProvider locationModeDetailsProvider) {
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {
			return createChangeCalcKeyCommand(schemaRecord, locationModeDetailsProvider);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// the record has to be made DIRECT
			return getMakeRecordDirectCommand(schemaRecord, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {				
			// the record has to be made VIA; we use a compound command to achieve this
			var makeRecordDirectCommand = getMakeRecordDirectCommand(schemaRecord, null);
			var makeRecordViaCommand = getMakeRecordViaCommand(schemaRecord, locationModeDetailsProvider, null);
		    return getCompoundCommand(schemaRecord, makeRecordDirectCommand, makeRecordViaCommand);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			// the record has to be made VSAM; we use a compound command to achieve this
			var makeRecordDirectCommand = getMakeRecordDirectCommand(schemaRecord, null);
			var makeRecordVsamCommand = getMakeRecordVsamCommand(schemaRecord, null);	
			return getCompoundCommand(schemaRecord, makeRecordDirectCommand, makeRecordVsamCommand);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {
			// the record has to be made VSAM CALC; we use a compound command to achieve this
			var makeRecordDirectCommand = getMakeRecordDirectCommand(schemaRecord, null);
			var makeRecordVsamCalcCommand = getMakeRecordVsamCalcCommand(schemaRecord, locationModeDetailsProvider, null);
			return  getCompoundCommand(schemaRecord, makeRecordDirectCommand, makeRecordVsamCalcCommand);
		} else {
			var message = INVALID_TARGET_LOCATION_MODE + locationModeDetailsProvider.getLocationMode();
			throw new IllegalArgumentException(message);
		}
	}
	
	private IModelChangeCommand createChangeCalcKeyCommand(SchemaRecord schemaRecord, ILocationModeDetailsProvider locationModeDetailsProvider) {
		// 1 or more CALC or VSAM CALC key attributes have changed; if it's only the duplicates option that has
		// been changed, change it directly; if there are changes to the CALC or VSAM CALC key elements, use a
		// change CALC key command to deal with the request
		var calcKeyElementsChanged = false;
		var oldCalcKeyElements = schemaRecord.getCalcKey().getElements();
		var newCalcKeyElements = locationModeDetailsProvider.getCalcKeyElements();
		if (newCalcKeyElements.size() != oldCalcKeyElements.size()) {					
			calcKeyElementsChanged = true;
		} else {
			for (var i = 0; i < newCalcKeyElements.size(); i++) {
				var oldElement = oldCalcKeyElements.get(i).getElement();
				var newElement = newCalcKeyElements.get(i);
				if (newElement != oldElement) {
					calcKeyElementsChanged = true;
					break;
				}
			}
		}
		if (!calcKeyElementsChanged) {
			// only the duplicates option has changed
			return getSetObjectAttributeCommand(schemaRecord, schemaRecord.getCalcKey(), 	SchemaPackage.eINSTANCE.getKey_DuplicatesOption(),
					locationModeDetailsProvider.getDuplicatesOption(), ModelChangeType.CHANGE_CALCKEY);
		} else {
			// the CALC key elements and maybe also the duplicates option have changed
			return getChangeCalcKeyCommand(schemaRecord, locationModeDetailsProvider, ModelChangeType.CHANGE_CALCKEY);
		}
	}

	private IModelChangeCommand getCommandForDirectRecord(SchemaRecord schemaRecord, ILocationModeDetailsProvider locationModeDetailsProvider) {
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {
			// the record has to be made CALC
			return getMakeRecordCalcCommand(schemaRecord, locationModeDetailsProvider, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// cannot happen but report it if it does
			throw new IllegalArgumentException("no changes for a record that is already DIRECT");
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {
			// the record has to be made VIA
			return getMakeRecordViaCommand(schemaRecord, locationModeDetailsProvider, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			// the record has to be made VSAM
			return getMakeRecordVsamCommand(schemaRecord, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {
			// the record has to be made VSAM CALC
			return getMakeRecordVsamCalcCommand(schemaRecord, locationModeDetailsProvider, ModelChangeType.CHANGE_LOCATION_MODE);
		} else {
			var message = INVALID_TARGET_LOCATION_MODE + locationModeDetailsProvider.getLocationMode();
			throw new IllegalArgumentException(message);
		}		
	}

	private IModelChangeCommand getCommandForViaRecord(SchemaRecord schemaRecord, ILocationModeDetailsProvider locationModeDetailsProvider) {
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {
			// the record has to be made CALC; we use a compound command to achieve this
			var makeRecordDirectCommand = getMakeRecordDirectCommand(schemaRecord, null);
			var makeRecordCalcCommand = getMakeRecordCalcCommand(schemaRecord, locationModeDetailsProvider, null);		    
			return getCompoundCommand(schemaRecord, makeRecordDirectCommand, makeRecordCalcCommand);		    
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// the record has to be made DIRECT
			return getMakeRecordDirectCommand(schemaRecord, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {
			return createChangeViaSpecificationCommand(schemaRecord, locationModeDetailsProvider);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			throw new IllegalArgumentException("cannot change a record from VIA to VSAM");
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {
			throw new IllegalArgumentException("cannot change a record from VIA to VSAM CALC");
		} else {
			var message = INVALID_TARGET_LOCATION_MODE + locationModeDetailsProvider.getLocationMode();
			throw new IllegalArgumentException(message);
		}
	}
	
	private IModelChangeCommand createChangeViaSpecificationCommand(SchemaRecord schemaRecord, ILocationModeDetailsProvider locationModeDetailsProvider) {
		if (!locationModeDetailsProvider.getViaSetName().equals(schemaRecord.getViaSpecification().getSet().getName())) {
			// the VIA set (and possibly the displacement specification) has changed; replace the whole ViaSpecification
			return getChangeViaSpecificationCommand(schemaRecord, locationModeDetailsProvider, ModelChangeType.CHANGE_VIA_SPECIFICATION);
		} else {
			// only the displacement specification has changed
			if (locationModeDetailsProvider.getSymbolicDisplacementName() == null && locationModeDetailsProvider.getDisplacementPageCount() == null) {
				// no displacement specification
				return createRemoveDisplacementItemsInViaSpecificationCommand(schemaRecord);
			} else if (locationModeDetailsProvider.getSymbolicDisplacementName() != null) {
				// symbolic displacement name specified
				return createSetSymbolicDisplacementNameInViaSpecificationCommand(schemaRecord, locationModeDetailsProvider);
			} else {
				// displacement pages specified
				return createSetDisplacementPagesInViaSpecificationCommand(schemaRecord, locationModeDetailsProvider);
			}
		}		
	}
	
	private IModelChangeCommand createRemoveDisplacementItemsInViaSpecificationCommand(SchemaRecord schemaRecord) {
		if (schemaRecord.getViaSpecification().getSymbolicDisplacementName() != null) {
			// nullify the existing symbolic displacement name
			return getSetObjectAttributeCommand(schemaRecord, schemaRecord.getViaSpecification(),
					SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName(), null, 
					ModelChangeType.CHANGE_VIA_SPECIFICATION);
		} else if (schemaRecord.getViaSpecification().getDisplacementPageCount() != null) {						
			// nullify the existing displacement page count
			return getSetObjectAttributeCommand(schemaRecord, schemaRecord.getViaSpecification(), 
					SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount(), null,
					ModelChangeType.CHANGE_VIA_SPECIFICATION);
		} else {
			String message = "no displacement specification was set";
			throw new IllegalArgumentException(message);
		}
	}
	
	private IModelChangeCommand createSetSymbolicDisplacementNameInViaSpecificationCommand(SchemaRecord schemaRecord, 
			ILocationModeDetailsProvider locationModeDetailsProvider) {

		if (schemaRecord.getViaSpecification().getDisplacementPageCount() == null) {
			return getSetObjectAttributeCommand(schemaRecord, schemaRecord.getViaSpecification(), 
					SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName(), 
					locationModeDetailsProvider.getSymbolicDisplacementName(), ModelChangeType.CHANGE_VIA_SPECIFICATION);
		} else {
			// the displacement page count is currently set, so we need to nullify it
			var nullifyDisplacementCountCommand = getSetObjectAttributeCommand(schemaRecord,
					schemaRecord.getViaSpecification(), SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount(), null, null);
			var setSymbolicDisplacementCommand = getSetObjectAttributeCommand(schemaRecord, schemaRecord.getViaSpecification(), 
					SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName(), 
					locationModeDetailsProvider.getSymbolicDisplacementName(), null);	
			return getCompoundCommand(schemaRecord, nullifyDisplacementCountCommand, setSymbolicDisplacementCommand);
		}		
	}
	
	private IModelChangeCommand createSetDisplacementPagesInViaSpecificationCommand(SchemaRecord schemaRecord, 
			ILocationModeDetailsProvider locationModeDetailsProvider) {

		if (schemaRecord.getViaSpecification().getSymbolicDisplacementName() == null) {
			return getSetObjectAttributeCommand(schemaRecord, schemaRecord.getViaSpecification(), 
					SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount(), 
					locationModeDetailsProvider.getDisplacementPageCount(), ModelChangeType.CHANGE_VIA_SPECIFICATION);
		} else {
			// the symbolic displacement is currently set, so we need to nullify it
			var nullifySymbolicDisplacementCommand = getSetObjectAttributeCommand(schemaRecord,
					schemaRecord.getViaSpecification(), SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName(), null, null);
			var setDisplacementCountCommand = getSetObjectAttributeCommand(schemaRecord, schemaRecord.getViaSpecification(), 
					SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount(), 
					locationModeDetailsProvider.getDisplacementPageCount(), null);						
			return getCompoundCommand(schemaRecord, nullifySymbolicDisplacementCommand, 
					setDisplacementCountCommand);						
		}
	}

	private IModelChangeCommand getCommandForVsamRecord(SchemaRecord schemaRecord, ILocationModeDetailsProvider locationModeDetailsProvider) {
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {		
			// the record has to be made CALC; we use a compound command to achieve this
			var makeRecordDirectCommand = getMakeRecordDirectCommand(schemaRecord, null);
			var makeRecordCalcCommand = getMakeRecordCalcCommand(schemaRecord, locationModeDetailsProvider, null);		    
			return getCompoundCommand(schemaRecord, makeRecordDirectCommand,
					makeRecordCalcCommand);		    
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// the record has to be made DIRECT
			return getMakeRecordDirectCommand(schemaRecord, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {
			// we cannot make a VSAM record VIA because then it would be a member in at least 1 set
			throw new IllegalArgumentException("cannot change a record from VSAM to VIA");
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			// cannot happen but report it if it does
			throw new IllegalArgumentException("no changes for a record that is already VSAM");
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {		
			// the record has to be made VSAM CALC
			return getMakeRecordVsamCalcCommand(schemaRecord, locationModeDetailsProvider, ModelChangeType.CHANGE_LOCATION_MODE);
		} else {	
			throw new IllegalArgumentException(INVALID_TARGET_LOCATION_MODE + locationModeDetailsProvider.getLocationMode());
		}
	}

	private IModelChangeCommand getCommandForVsamCalcRecord(SchemaRecord schemaRecord, ILocationModeDetailsProvider locationModeDetailsProvider) {
		if (locationModeDetailsProvider.getLocationMode() == LocationMode.CALC) {
			// the record has to be made CALC; we use a compound command to achieve this
			var makeRecordDirectCommand = getMakeRecordDirectCommand(schemaRecord, null);
			var makeRecordCalcCommand = getMakeRecordCalcCommand(schemaRecord, locationModeDetailsProvider, null);		    
			return getCompoundCommand(schemaRecord, makeRecordDirectCommand,
					makeRecordCalcCommand);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.DIRECT) {
			// the record has to be made DIRECT
			return getMakeRecordDirectCommand(schemaRecord, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VIA) {
			// we cannot make a VSAM CALC record VIA because then it would be a member in at least 1 set
			throw new IllegalArgumentException("cannot change a record from VSAM CALC to VIA");
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM) {
			// the record has to be made VSAM
			return getMakeRecordVsamCommand(schemaRecord, ModelChangeType.CHANGE_LOCATION_MODE);
		} else if (locationModeDetailsProvider.getLocationMode() == LocationMode.VSAM_CALC) {			
			return createChangeCalcKeyCommand(schemaRecord, locationModeDetailsProvider);
		} else {
			throw new IllegalArgumentException(INVALID_TARGET_LOCATION_MODE + locationModeDetailsProvider.getLocationMode());
		}
	}

	private ModelChangeCompoundCommand getCompoundCommand(SchemaRecord schemaRecord, Command... commands) {
		var cc = new ModelChangeCompoundCommand();
		
		var context = new ModelChangeContext(ModelChangeType.CHANGE_LOCATION_MODE);
		context.putContextData(schemaRecord);
	    cc.setContext(context);
	    
	    for (var command : commands) {
	    		cc.add(command);
	    }
	    var lastCommand = (Command) cc.getCommands().get(cc.getCommands().size() - 1);
	    cc.setLabel(lastCommand.getLabel());
	    
	    return cc;
	}
	
	private MakeRecordCalcCommand getMakeRecordCalcCommand(SchemaRecord schemaRecord,
			ILocationModeDetailsProvider detailsProvider, ModelChangeType modelChangeType) {
	
		var command = new MakeRecordCalcCommand(schemaRecord, detailsProvider.getCalcKeyElements(),
				detailsProvider.getDuplicatesOption());
		
		if (modelChangeType != null) {
			var context = new ModelChangeContext(modelChangeType);
			context.putContextData(schemaRecord);
			command.setContext(context);
		}
		
		return command;
	}
	
	private MakeRecordDirectCommand getMakeRecordDirectCommand(SchemaRecord schemaRecord, ModelChangeType modelChangeType) {
		var command = new MakeRecordDirectCommand(schemaRecord);
		
		if (modelChangeType != null) {
			var context = new ModelChangeContext(modelChangeType);
			context.putContextData(schemaRecord);
			command.setContext(context);
		}
		
		return command;
	}

	private MakeRecordViaCommand getMakeRecordViaCommand(SchemaRecord schemaRecord,
			ILocationModeDetailsProvider detailsProvider, ModelChangeType modelChangeType) {
	
		var command = new MakeRecordViaCommand(schemaRecord, detailsProvider.getViaSetName(), 
				detailsProvider.getSymbolicDisplacementName(), detailsProvider.getDisplacementPageCount());
		
		if (modelChangeType != null) {
			var context = new ModelChangeContext(modelChangeType);
			context.putContextData(schemaRecord);
			command.setContext(context);
		}
		
		return command;
	}
	
	private MakeRecordVsamCalcCommand getMakeRecordVsamCalcCommand(SchemaRecord schemaRecord,
			ILocationModeDetailsProvider detailsProvider, ModelChangeType modelChangeType) {
	
		var command = new MakeRecordVsamCalcCommand(schemaRecord, detailsProvider.getCalcKeyElements(), 
				detailsProvider.getDuplicatesOption());
	
		if (modelChangeType != null) {
			var context = new ModelChangeContext(modelChangeType);
			context.putContextData(schemaRecord);
			command.setContext(context);
		}
	
		return command;
	}

	private MakeRecordVsamCommand getMakeRecordVsamCommand(SchemaRecord schemaRecord, ModelChangeType modelChangeType) {	
		var command = new MakeRecordVsamCommand(schemaRecord);
		
		if (modelChangeType != null) {
			var context = new ModelChangeContext(modelChangeType);
			context.putContextData(schemaRecord);
			command.setContext(context);
		}
		
		return command;
	}

	private SetObjectAttributeCommand getSetObjectAttributeCommand(SchemaRecord schemaRecord, EObject attributeOwner, 
			EAttribute attribute, Object value, ModelChangeType modelChangeType) {
	
		var command = new SetObjectAttributeCommand(attributeOwner, attribute, value, getAttributeLabel(attribute));
		
		if (modelChangeType != null) {
			var context = new ModelChangeContext(modelChangeType);
			context.putContextData(schemaRecord);
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
