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
package org.lh.dmlj.schema.editor.command.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;

public class ModelChangeContext {

	private List<ModelChangeContext> children = new ArrayList<>();
	private CommandExecutionMode commandExecutionMode;
	private Map<String, String> contextData = new HashMap<>();
	private Object listenerData;
	private ModelChangeContext parent;
	private ModelChangeType modelChangeType;
	private Schema schema;
	
	private static boolean appliesTo(ModelChangeContext context, ConnectionPart connectionPart) {
		checkAppliesTo(context, connectionPart);
		MemberRole memberRole = connectionPart.getMemberRole();
		Set set = memberRole.getSet();
		SchemaRecord record = memberRole.getRecord();
		int connectionPartIndex = memberRole.getConnectionParts().indexOf(connectionPart);
		return context.contextData.size() == 3 && 
			   set.getName().equals(context.contextData.get(IContextDataKeys.SET_NAME)) && 
			   record.getName().equals(context.contextData.get(IContextDataKeys.RECORD_NAME)) &&
			   context.contextData.containsKey(IContextDataKeys.CONNECTION_PART_INDEX) &&
			   connectionPartIndex == Integer.valueOf(context.contextData.get(IContextDataKeys.CONNECTION_PART_INDEX)) ||				   
			   context.contextData.size() == 4 &&
			   context.contextData.containsKey(IContextDataKeys.PROPERTY_NAME) &&
			   set.getName().equals(context.contextData.get(IContextDataKeys.SET_NAME)) &&
			   record.getName().equals(context.contextData.get(IContextDataKeys.RECORD_NAME)) &&
			   context.contextData.containsKey(IContextDataKeys.CONNECTION_PART_INDEX) &&
			   connectionPartIndex == Integer.valueOf(context.contextData.get(IContextDataKeys.CONNECTION_PART_INDEX));
	}
	
	private static boolean appliesTo(ModelChangeContext context, Connector connector) {
		checkAppliesTo(context, connector);
		MemberRole memberRole = connector.getConnectionPart().getMemberRole();
		Set set = memberRole.getSet();
		SchemaRecord record = memberRole.getRecord();
		int connectionPartIndex = 
			memberRole.getConnectionParts().indexOf(connector.getConnectionPart());
		return context.contextData.size() == 3 && 
			   set.getName().equals(context.contextData.get(IContextDataKeys.SET_NAME)) && 
			   record.getName().equals(context.contextData.get(IContextDataKeys.RECORD_NAME)) &&
			   context.contextData.containsKey(IContextDataKeys.CONNECTION_PART_INDEX) &&
			   connectionPartIndex == Integer.valueOf(context.contextData.get(IContextDataKeys.CONNECTION_PART_INDEX)) ||				   
			   context.contextData.size() == 4 &&
			   context.contextData.containsKey(IContextDataKeys.PROPERTY_NAME) &&
			   set.getName().equals(context.contextData.get(IContextDataKeys.SET_NAME)) &&
			   record.getName().equals(context.contextData.get(IContextDataKeys.RECORD_NAME)) &&
			   context.contextData.containsKey(IContextDataKeys.CONNECTION_PART_INDEX) &&
			   connectionPartIndex == Integer.valueOf(context.contextData.get(IContextDataKeys.CONNECTION_PART_INDEX));				
	}
	
	private static boolean appliesTo(ModelChangeContext context, Guide guide) {
		checkAppliesTo(context, guide);
		Ruler ruler = guide.getRuler();
		int rulerIndex = ruler.getDiagramData().getRulers().indexOf(ruler);
		int guideIndex = ruler.getGuides().indexOf(guide);
		return context.contextData.size() == 2 &&
			   context.contextData.containsKey(IContextDataKeys.RULER_INDEX) &&
			   rulerIndex == Integer.valueOf(context.contextData.get(IContextDataKeys.RULER_INDEX)) &&
			   context.contextData.containsKey(IContextDataKeys.GUIDE_INDEX) &&
			   guideIndex == Integer.valueOf(context.contextData.get(IContextDataKeys.GUIDE_INDEX)) ||
			   context.contextData.size() == 3 &&
			   context.contextData.containsKey(IContextDataKeys.PROPERTY_NAME) &&
			   context.contextData.containsKey(IContextDataKeys.RULER_INDEX) &&
			   rulerIndex == Integer.valueOf(context.contextData.get(IContextDataKeys.RULER_INDEX)) &&
			   context.contextData.containsKey(IContextDataKeys.GUIDE_INDEX) &&
			   guideIndex == Integer.valueOf(context.contextData.get(IContextDataKeys.GUIDE_INDEX));		
	}
	
	private static boolean appliesTo(ModelChangeContext context, MemberRole memberRole) {
		checkAppliesTo(context, memberRole);
		Set set = memberRole.getSet();
		SchemaRecord record = memberRole.getRecord();
		return context.contextData.size() == 2 && 
			   set.getName().equals(context.contextData.get(IContextDataKeys.SET_NAME)) && 
			   record.getName().equals(context.contextData.get(IContextDataKeys.RECORD_NAME)) ||
			   context.contextData.size() == 3 &&
					   context.contextData.containsKey(IContextDataKeys.PROPERTY_NAME) &&
			   set.getName().equals(context.contextData.get(IContextDataKeys.SET_NAME)) &&
			   record.getName().equals(context.contextData.get(IContextDataKeys.RECORD_NAME));		
	}
	
	private static boolean appliesTo(ModelChangeContext context, Ruler ruler) {
		checkAppliesTo(context, ruler);
		int rulerIndex = ruler.getDiagramData().getRulers().indexOf(ruler);
		return context.contextData.size() == 1 &&
			   context.contextData.containsKey(IContextDataKeys.RULER_INDEX) &&
			   rulerIndex == Integer.valueOf(context.contextData.get(IContextDataKeys.RULER_INDEX)) ||
			   context.contextData.size() == 2 &&
			   context.contextData.containsKey(IContextDataKeys.PROPERTY_NAME) &&
			   context.contextData.containsKey(IContextDataKeys.RULER_INDEX) &&
			   rulerIndex == Integer.valueOf(context.contextData.get(IContextDataKeys.RULER_INDEX));		
	}
	
	private static boolean appliesTo(ModelChangeContext context, SchemaArea area) {
		checkAppliesTo(context, area);
		return context.contextData.size() == 1 && 
			   area.getName().equals(context.contextData.get(IContextDataKeys.AREA_NAME)) ||
			   context.contextData.size() == 2 &&
			   context.contextData.containsKey(IContextDataKeys.PROPERTY_NAME) &&
			   area.getName().equals(context.contextData.get(IContextDataKeys.AREA_NAME));		
	}
	
	private static boolean appliesTo(ModelChangeContext context, SchemaRecord record) {
		checkAppliesTo(context, record);
		return context.contextData.size() == 1 && 
			   record.getName().equals(context.contextData.get(IContextDataKeys.RECORD_NAME)) ||
			   context.contextData.size() == 2 &&
			   context.contextData.containsKey(IContextDataKeys.PROPERTY_NAME) &&
			   record.getName().equals(context.contextData.get(IContextDataKeys.RECORD_NAME));		
	}
	
	private static boolean appliesTo(ModelChangeContext context, Set set) {
		checkAppliesTo(context, set);
		return context.contextData.size() == 1 && 
			   set.getName().equals(context.contextData.get(IContextDataKeys.SET_NAME)) ||
			   context.contextData.size() == 2 &&
			   context.contextData.containsKey(IContextDataKeys.PROPERTY_NAME) &&
			   set.getName().equals(context.contextData.get(IContextDataKeys.SET_NAME));		
	}

	private static void checkAppliesTo(ModelChangeContext context, EObject model) {
		if (model == null) {
			throw new IllegalArgumentException("Invalid model: null");
		}
		if (context.modelChangeType == ModelChangeType.SET_PROPERTY &&
			!context.contextData.containsKey(IContextDataKeys.PROPERTY_NAME)) {
			
			throw new IllegalStateException("Feature name NOT found in context data");
		}
		if (context.contextData.containsKey(IContextDataKeys.PROPERTY_NAME) &&
			context.modelChangeType != ModelChangeType.SET_PROPERTY) {
			
			throw new IllegalStateException("Feature name should NOT be present in context data: " +
											context.modelChangeType);
		}		
	}

	public static String getQualifiedFeatureName(EStructuralFeature feature) {
		return feature.getContainerClass().getSimpleName() + "." + feature.getName();
	}
	
	public ModelChangeContext(ModelChangeType modelChangeType) {
		super();
		this.modelChangeType = modelChangeType;
	}

	public boolean appliesTo(ConnectionPart connectionPart) {
		if (children.isEmpty()) {
			return appliesTo(this, connectionPart);
		} else {
			for (ModelChangeContext child : children) {
				try {
					if (child.appliesTo(connectionPart)) {
						return true;
					}
				} catch (IllegalStateException e) {					
				}
			}
			return false;
		}
	}
	
	public boolean appliesTo(Connector connector) {
		if (children.isEmpty()) {
			return appliesTo(this, connector);
		} else {
			for (ModelChangeContext child : children) {
				try {
					if (child.appliesTo(connector)) {
						return true;
					}
				} catch (IllegalStateException e) {					
				}
			}
			return false;
		}			
	}
	
	public boolean appliesTo(Guide guide) {
		if (children.isEmpty()) {
			return appliesTo(this, guide);
		} else {
			for (ModelChangeContext child : children) {
				try {
					if (child.appliesTo(guide)) {
						return true;
					}
				} catch (IllegalStateException e) {					
				}
			}
			return false;
		}		
	}
	
	public boolean appliesTo(MemberRole memberRole) {
		if (children.isEmpty()) {
			return appliesTo(this, memberRole);
		} else {
			for (ModelChangeContext child : children) {
				try {
					if (child.appliesTo(memberRole)) {
						return true;
					}
				} catch (IllegalStateException e) {					
				}
			}
			return false;
		}		
	}
	
	public boolean appliesTo(Ruler ruler) {
		if (children.isEmpty()) {
			return appliesTo(this, ruler);
		} else {
			for (ModelChangeContext child : children) {
				try {
					if (child.appliesTo(ruler)) {
						return true;
					}
				} catch (IllegalStateException e) {					
				}
			}
			return false;
		}		
	}
	
	public boolean appliesTo(SchemaArea area) {
		if (children.isEmpty()) {
			return appliesTo(this, area);
		} else {
			for (ModelChangeContext child : children) {
				try {
					if (child.appliesTo(area)) {
						return true;
					}
				} catch (IllegalStateException e) {					
				}
			}
			return false;
		}		
	}
	
	public boolean appliesTo(SchemaRecord record) {
		if (children.isEmpty()) {
			return appliesTo(this, record);
		} else {
			for (ModelChangeContext child : children) {
				try {
					if (child.appliesTo(record)) {
						return true;
					}
				} catch (IllegalStateException e) {					
				}
			}
			return false;
		}		
	}
	
	public boolean appliesTo(Set set) {
		if (children.isEmpty()) {
			return appliesTo(this, set);
		} else {
			for (ModelChangeContext child : children) {
				try {
					if (child.appliesTo(set)) {
						return true;
					}
				} catch (IllegalStateException e) {					
				}
			}
			return false;
		}		
	}
	
	public List<ModelChangeContext> getChildren() {
		return children;		
	}
	
	public ModelChangeContext copy() {
		ModelChangeContext copy = new ModelChangeContext(modelChangeType);
		copy.setSchema(schema);
		for (String key : contextData.keySet()) {
			copy.getContextData().put(key, contextData.get(key));
		}
		for (ModelChangeContext child : children) {
			ModelChangeContext copyOfChild = child.copy();
			copy.getChildren().add(copyOfChild);
			copyOfChild.setParent(copy);
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
	
	public ModelChangeContext getParent() {
		return parent;
	}

	public Schema getSchema() {
		return schema;
	}

	public boolean isPropertySet(EStructuralFeature... features) {
		if (modelChangeType != ModelChangeType.SET_PROPERTY) {
			throw new IllegalStateException("Context has wrong model change type: " + 
											modelChangeType + " (expected: " + 
											ModelChangeType.SET_PROPERTY + ")");
		}
		String featureName = contextData.get(IContextDataKeys.PROPERTY_NAME);
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

	public void putContextData(ConnectionPart connectionPart) {
		MemberRole memberRole = connectionPart.getMemberRole(); 
		String setName = memberRole.getSet().getName();
		String recordName = memberRole.getRecord().getName();
		int connectionPartIndex = memberRole.getConnectionParts().indexOf(connectionPart);
		contextData.put(IContextDataKeys.SET_NAME, setName);
		contextData.put(IContextDataKeys.RECORD_NAME, recordName);
		contextData.put(IContextDataKeys.CONNECTION_PART_INDEX, String.valueOf(connectionPartIndex));
	}
	
	public void putContextData(Connector connector) {
		MemberRole memberRole = connector.getConnectionPart().getMemberRole();
		String setName = memberRole.getSet().getName();
		String recordName = memberRole.getRecord().getName();
		int connectionPartIndex = 
			memberRole.getConnectionParts().indexOf(connector.getConnectionPart());
		contextData.put(IContextDataKeys.SET_NAME, setName);
		contextData.put(IContextDataKeys.RECORD_NAME, recordName);
		contextData.put(IContextDataKeys.CONNECTION_PART_INDEX, String.valueOf(connectionPartIndex));
	}
	
	public void putContextData(DiagramNode diagramNode) {
		if (diagramNode instanceof ConnectionLabel) {
			ConnectionLabel connectionLabel = (ConnectionLabel) diagramNode;
			putContextData(connectionLabel.getMemberRole());
		} else if (diagramNode instanceof Connector) {
			Connector connector = (Connector) diagramNode;
			putContextData(connector);
		} else if (diagramNode instanceof DiagramLabel) {
			// nothing to set
		} else if (diagramNode instanceof SchemaRecord) {
			SchemaRecord record = (SchemaRecord) diagramNode;
			putContextData(record);
		} else if (diagramNode instanceof SystemOwner) {
			SystemOwner systemOwner = (SystemOwner) diagramNode;
			putContextData(systemOwner.getSet());
		} else if (diagramNode instanceof VsamIndex) {
			VsamIndex vsamIndex = (VsamIndex) diagramNode;
			putContextData(vsamIndex.getSet());
		} else if (diagramNode != null) {
			throw new IllegalArgumentException("DiagramNode type invalid: " + 
											   diagramNode.getClass().getName());
		} else {
			throw new IllegalArgumentException("DiagramNode type invalid: null");
		}
	}

	public void putContextData(Guide guide) {
		Ruler ruler = guide.getRuler();
		DiagramData diagramData = ruler.getDiagramData();
		int rulerIndex = diagramData.getRulers().indexOf(ruler);
		int guideIndex = ruler.getGuides().indexOf(guide);
		contextData.put(IContextDataKeys.RULER_INDEX, String.valueOf(rulerIndex));
		contextData.put(IContextDataKeys.GUIDE_INDEX, String.valueOf(guideIndex));
	}
	
	public void putContextData(MemberRole memberRole) {
		String setName = memberRole.getSet().getName();
		String recordName = memberRole.getRecord().getName();
		contextData.put(IContextDataKeys.SET_NAME, setName);
		contextData.put(IContextDataKeys.RECORD_NAME, recordName);
	}
	
	public void putContextData(Ruler ruler) {
		DiagramData diagramData = ruler.getDiagramData();
		int rulerIndex = diagramData.getRulers().indexOf(ruler);
		contextData.put(IContextDataKeys.RULER_INDEX, String.valueOf(rulerIndex));
	}
	
	public void putContextData(SchemaArea area) {
		String areaName = area.getName();
		contextData.put(IContextDataKeys.AREA_NAME, areaName);
	}
	
	public void putContextData(SchemaRecord record) {
		contextData.put(IContextDataKeys.RECORD_NAME, record.getName());
	}
	
	public void putContextData(Set set) {
		contextData.put(IContextDataKeys.SET_NAME, set.getName());
	}
	
	public void putContextData(EObject model, EStructuralFeature feature) {		
		
		if (modelChangeType != ModelChangeType.SET_PROPERTY) {
			throw new IllegalStateException("Invalid model change type: " + modelChangeType);
		}		
		if (feature == null) {
			throw new IllegalArgumentException("Invalid feature: null");
		}
		
		String featureName = ModelChangeContext.getQualifiedFeatureName(feature);
		contextData.put(IContextDataKeys.PROPERTY_NAME, featureName);
		
		// the following is based on the types of defined attributes based properties sections...
		if (model instanceof SchemaArea) {
			putContextData((SchemaArea) model); 
		} else if (model instanceof DiagramLabel) {
			// nothing to set
		} else if (model instanceof DiagramData) {
			// nothing to set
		} else if (model instanceof SchemaRecord) {
			putContextData((SchemaRecord) model);
		} else if (model instanceof Schema) {
			// nothing to set
		} else if (model instanceof Set) {
			putContextData((Set) model);
		// ...but for completeness (and because it is necessary) we add the remaining types as well
		} else if (model instanceof ConnectionPart) {
			putContextData((ConnectionPart) model);
		} else if (model instanceof Connector) {
			putContextData((Connector) model);
		} else if (model instanceof Guide) {
			putContextData((Guide) model);
		} else if (model instanceof MemberRole) {
			putContextData((MemberRole) model);
		} else if (model instanceof Ruler) {
			putContextData((Ruler) model);
		} else if (model == null){
			throw new IllegalArgumentException("Model type invalid: null");
		} else {
			throw new IllegalArgumentException("Model type invalid: " + model.getClass().getName());
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
	
	public void setParent(ModelChangeContext parent) {
		this.parent = parent;
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
