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
package org.lh.dmlj.schema.editor.command.infrastructure;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;

public class ModelChangeContext {

	private CommandExecutionMode commandExecutionMode;
	private Map<String, String> contextData = new HashMap<>();
	private Object listenerData;
	private ModelChangeType modelChangeType;
	private Schema schema;
	
	public static String getQualifiedFeatureName(EStructuralFeature feature) {
		return feature.getContainerClass().getSimpleName() + "." + feature.getName();
	}
	
	public ModelChangeContext(ModelChangeType modelChangeType) {
		super();
		this.modelChangeType = modelChangeType;
	}

	public boolean appliesTo(EObject model) {
		
		if (model == null) {
			throw new IllegalArgumentException("Invalid model: null");
		}
		
		if (modelChangeType == ModelChangeType.SET_PROPERTY &&
			!contextData.containsKey(IContextDataKeys.FEATURE_NAME)) {
			
			throw new IllegalStateException("Feature name NOT found in context data");
		}
		if (contextData.containsKey(IContextDataKeys.FEATURE_NAME) &&
		    modelChangeType != ModelChangeType.SET_PROPERTY) {
			
			throw new IllegalStateException("Feature name should NOT be present in context data: " +
											modelChangeType);
		}
		
		if (model instanceof ConnectionLabel) {
			ConnectionLabel connectionLabel = (ConnectionLabel) model;
			Set set = connectionLabel.getMemberRole().getSet();
			SchemaRecord record = connectionLabel.getMemberRole().getRecord();
			return contextData.size() == 2 && 
				   set.getName().equals(contextData.get(IContextDataKeys.SET_NAME)) && 
				   record.getName().equals(contextData.get(IContextDataKeys.RECORD_NAME)) ||
				   contextData.size() == 3 &&
				   contextData.containsKey(IContextDataKeys.FEATURE_NAME) &&
				   set.getName().equals(contextData.get(IContextDataKeys.SET_NAME)) &&
				   record.getName().equals(contextData.get(IContextDataKeys.RECORD_NAME));			
		} else if (model instanceof Connector) {
			Connector connector = (Connector) model;
			Set set = connector.getConnectionPart().getMemberRole().getSet();
			SchemaRecord record = connector.getConnectionPart().getMemberRole().getRecord();
			return contextData.size() == 2 && 
				   set.getName().equals(contextData.get(IContextDataKeys.SET_NAME)) && 
				   record.getName().equals(contextData.get(IContextDataKeys.RECORD_NAME)) ||
				   contextData.size() == 3 &&
				   contextData.containsKey(IContextDataKeys.FEATURE_NAME) &&
				   set.getName().equals(contextData.get(IContextDataKeys.SET_NAME)) &&
				   record.getName().equals(contextData.get(IContextDataKeys.RECORD_NAME));			
		} else if (model instanceof DiagramData) {
			return contextData.isEmpty() ||
				   contextData.size() == 1 && contextData.containsKey(IContextDataKeys.FEATURE_NAME);
		} else if (model instanceof DiagramLabel) {
			return contextData.isEmpty() ||
				   contextData.size() == 1 && contextData.containsKey(IContextDataKeys.FEATURE_NAME);
		} else if (model instanceof MemberRole) {
			MemberRole memberRole = (MemberRole) model;
			Set set = memberRole.getSet();
			SchemaRecord record = memberRole.getRecord();
			return contextData.size() == 2 && 
				   set.getName().equals(contextData.get(IContextDataKeys.SET_NAME)) && 
				   record.getName().equals(contextData.get(IContextDataKeys.RECORD_NAME)) ||
				   contextData.size() == 3 &&
				   contextData.containsKey(IContextDataKeys.FEATURE_NAME) &&
				   set.getName().equals(contextData.get(IContextDataKeys.SET_NAME)) &&
				   record.getName().equals(contextData.get(IContextDataKeys.RECORD_NAME));
		} else if (model instanceof Schema) {
			return contextData.isEmpty() ||
				   contextData.size() == 1 && contextData.containsKey(IContextDataKeys.FEATURE_NAME);
		} else if (model instanceof SchemaArea) {
			SchemaArea area = (SchemaArea) model;
			return contextData.size() == 1 && 
				   area.getName().equals(contextData.get(IContextDataKeys.AREA_NAME)) ||
				   contextData.size() == 2 &&
				   contextData.containsKey(IContextDataKeys.FEATURE_NAME) &&
				   area.getName().equals(contextData.get(IContextDataKeys.AREA_NAME));
		} else if (model instanceof SchemaRecord) {
			SchemaRecord record = (SchemaRecord) model;
			return contextData.size() == 1 && 
				   record.getName().equals(contextData.get(IContextDataKeys.RECORD_NAME)) ||
				   contextData.size() == 2 &&
				   contextData.containsKey(IContextDataKeys.FEATURE_NAME) &&
				   record.getName().equals(contextData.get(IContextDataKeys.RECORD_NAME));
		} else if (model instanceof Set) {
			Set set = (Set) model;
			return contextData.size() == 1 && 
				   set.getName().equals(contextData.get(IContextDataKeys.SET_NAME)) ||
				   contextData.size() == 2 &&
				   contextData.containsKey(IContextDataKeys.FEATURE_NAME) &&
				   set.getName().equals(contextData.get(IContextDataKeys.SET_NAME));
		} else if (model instanceof SystemOwner) {
			SystemOwner systemOwner = (SystemOwner) model;
			Set set = systemOwner.getSet();
			return contextData.size() == 1 && 
				   set.getName().equals(contextData.get(IContextDataKeys.SET_NAME)) ||
				   contextData.size() == 2 &&
				   contextData.containsKey(IContextDataKeys.FEATURE_NAME) &&
				   set.getName().equals(contextData.get(IContextDataKeys.SET_NAME));
		} else {
			throw new IllegalArgumentException("Invalid model type: " + model);
		}
	}

	public ModelChangeContext copy() {
		ModelChangeContext copy = new ModelChangeContext(modelChangeType);
		copy.setSchema(schema);
		for (String key : contextData.keySet()) {
			copy.getContextData().put(key, contextData.get(key));
		}
		// don't copy the listener data and command execution mode
		return copy;
	}

	public CommandExecutionMode getCommandExecutionMode() {
		return commandExecutionMode;
	}

	public Map<String, String> getContextData() {
		return contextData;
	}

	public Object getListenerData() {
		return listenerData;
	}

	public ModelChangeType getModelChangeType() {
		return modelChangeType;
	}

	public Schema getSchema() {
		return schema;
	}

	public boolean isFeatureSet(EStructuralFeature... features) {
		if (modelChangeType != ModelChangeType.SET_PROPERTY) {
			throw new IllegalStateException("Context has wrong model change type: " + 
											modelChangeType + " (expected: " + 
											ModelChangeType.SET_PROPERTY + ")");
		}
		String featureName = contextData.get(IContextDataKeys.FEATURE_NAME);
		if (featureName == null) {
			throw new IllegalStateException("Context has no feature in its context data");
		}
		for (EStructuralFeature feature : features) {
			if (featureName.equals(getQualifiedFeatureName(feature))) {
				return true;
			}
		}
		return false;
	}

	public void putContextData(EStructuralFeature feature) {
		if (feature != null) {
			String featureName = ModelChangeContext.getQualifiedFeatureName(feature);
			contextData.put(IContextDataKeys.FEATURE_NAME, featureName);
		} else {
			throw new IllegalArgumentException("Invalid feature: null");
		}
	}
	
	public void putContextData(EObject model) {
		if (model instanceof ConnectionLabel) {
			ConnectionLabel connectionLabel = (ConnectionLabel) model;
			String setName = connectionLabel.getMemberRole().getSet().getName();
			String recordName = connectionLabel.getMemberRole().getRecord().getName();
			contextData.put(IContextDataKeys.SET_NAME, setName);
			contextData.put(IContextDataKeys.RECORD_NAME, recordName);
		} else if (model instanceof Connector) {
			Connector connector = (Connector) model;
			String setName = connector.getConnectionPart().getMemberRole().getSet().getName();
			String recordName = 
				connector.getConnectionPart().getMemberRole().getRecord().getName();
			contextData.put(IContextDataKeys.SET_NAME, setName);
			contextData.put(IContextDataKeys.RECORD_NAME, recordName);
		} else if (model instanceof DiagramData) {
			// no context data to be set 
		} else if (model instanceof DiagramLabel) {
			// no context data to be set
		} else if (model instanceof MemberRole) {
			MemberRole memberRole = (MemberRole) model;
			String setName = memberRole.getSet().getName();
			String recordName = memberRole.getRecord().getName();
			contextData.put(IContextDataKeys.SET_NAME, setName);
			contextData.put(IContextDataKeys.RECORD_NAME, recordName);
		} else if (model instanceof Schema) {
			// no context data to be set
		} else if (model instanceof SchemaArea) {
			SchemaArea area = (SchemaArea) model;
			String areaName = area.getName();
			contextData.put(IContextDataKeys.AREA_NAME, areaName);
		} else if (model instanceof SchemaRecord) {
			SchemaRecord record = (SchemaRecord) model;
			contextData.put(IContextDataKeys.RECORD_NAME, record.getName());
		} else if (model instanceof Set) {
			Set set = (Set) model;
			contextData.put(IContextDataKeys.SET_NAME, set.getName());
		} else if (model instanceof SystemOwner) {
			SystemOwner systemOwner = (SystemOwner) model;
			String setName = systemOwner.getSet().getName();
			contextData.put(IContextDataKeys.SET_NAME, setName);
		} else {
			throw new IllegalArgumentException("Invalid model type: " + model);
		}
	}
	
	/**
	 * This attribute should only be set by the model change dispatcher and NOT by the component 
	 * that creates the initial context before handling a model change command to the command stack.
	 * @param commandExecutionMode the command execution mode during which the context is passed to 
	 *        model change listeners
	 */
	public void setCommandExecutionMode(CommandExecutionMode commandExecutionMode) {
		this.commandExecutionMode = commandExecutionMode;
	}

	/**
	 * This attribute should NOT be set by the component that creates the initial context before 
	 * handling a model change command to the command stack.  It is intended <i>only</i> for model  
	 * change listeners, that can add a 'listener object' to the context <i>before</i> the model 
	 * change is carried out, which can then be retrieved <i>after</i> the model has been changed.
	 * @param listenerData an object containing listener specific data 
	 */
	public void setListenerData(Object listenerData) {
		this.listenerData = listenerData;
	}
	
	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public String toString() {
		return "ModelChangeContext [modelChangeType=" + modelChangeType + 
			   ", commandExecutionMode=" + commandExecutionMode + 
			   ", contextData=" + contextData + 
			   ", listenerData=" + listenerData + "]";
	}
	
}
