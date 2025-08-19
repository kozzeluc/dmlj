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
package org.lh.dmlj.schema.editor.command.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
import org.lh.dmlj.schema.VsamType;
import org.lh.dmlj.schema.editor.Plugin;

public class ModelChangeContext {
	public static final Function<SchemaArea, List<ContextData>> areaContextDataAssembler = a -> List.of(new ContextData(ContextDataKeys.AREA_NAME, a.getName()));
	public static final Function<SchemaRecord, List<ContextData>> recordContextDataAssembler = r -> List.of(new ContextData(ContextDataKeys.RECORD_NAME, r.getName()));
	public static final Function<Set, List<ContextData>> setContextDataAssembler = s -> List.of(new ContextData(ContextDataKeys.SET_NAME, s.getName()));
	public static final Function<ConnectionPart, List<ContextData>> connectionPartContextDataAssembler = c -> List.of(
			new ContextData(ContextDataKeys.SET_NAME, c.getMemberRole().getSet().getName()),
			new ContextData(ContextDataKeys.RECORD_NAME, c.getMemberRole().getRecord().getName()),
			new ContextData(ContextDataKeys.CONNECTION_PART_INDEX, String.valueOf(c.getMemberRole().getConnectionParts().indexOf(c)))
		);
	public static final Function<Connector, List<ContextData>> connectorContextDataAssembler = c -> List.of(
			new ContextData(ContextDataKeys.SET_NAME, c.getConnectionPart().getMemberRole().getSet().getName()),
			new ContextData(ContextDataKeys.RECORD_NAME, c.getConnectionPart().getMemberRole().getRecord().getName()),
			new ContextData(ContextDataKeys.CONNECTION_PART_INDEX,
					String.valueOf(c.getConnectionPart().getMemberRole().getConnectionParts().indexOf(c.getConnectionPart())))
		);
	public static final Function<Guide, List<ContextData>> guideContextDataAssembler = g -> List.of(
			new ContextData(ContextDataKeys.RULER_INDEX, String.valueOf(g.getRuler().getDiagramData().getRulers().indexOf(g.getRuler()))),
			new ContextData(ContextDataKeys.GUIDE_INDEX, String.valueOf(g.getRuler().getGuides().indexOf(g)))
		);
	public static final Function<MemberRole, List<ContextData>> memberRoleContextDataAssembler = m -> List.of(
			new ContextData(ContextDataKeys.SET_NAME, m.getSet().getName()),
			new ContextData(ContextDataKeys.RECORD_NAME, m.getRecord().getName())
		);
	public static final Function<Ruler, List<ContextData>> rulerContextDataAssembler = r -> List.of(
			new ContextData(ContextDataKeys.RULER_INDEX, String.valueOf(r.getDiagramData().getRulers().indexOf(r)))
		);
		
	private final ModelChangeType modelChangeType;
	private List<ModelChangeContext> children = new ArrayList<>();
	private CommandExecutionMode commandExecutionMode;
	private Map<String, String> contextData = new HashMap<>();
	private Object listenerData;
	private ModelChangeContext parent;
	private Schema schema;

	public static String getQualifiedFeatureName(EStructuralFeature feature) {
		return feature.getContainerClass().getSimpleName() + "." + feature.getName();
	}
	
	public ModelChangeContext(ModelChangeType modelChangeType) {
		this.modelChangeType = modelChangeType;
	}
	
	public boolean appliesTo(EObject model) {
		if (children.isEmpty()) {
			return ModelChangeContextChecker.forModel(model).appliesTo(this, model);
		} else {
			for (var child : children) {
				try {
					if (child.appliesTo(model)) {
						return true;
					}
				} catch (IllegalStateException e) {
					Plugin.getDefault().getLog().error(e.getMessage(), e);
				}
			}
			return false;
		}
	}
	
	public List<ModelChangeContext> getChildren() {
		return children;		
	}
	
	public ModelChangeContext copy() {
		var copy = new ModelChangeContext(modelChangeType);
		copy.setSchema(schema);
		contextData.entrySet().stream()
				.forEach(entry -> copy.getContextData().put(entry.getKey(), entry.getValue()));
		for (var child : children) {
			var copyOfChild = child.copy();
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
			throw new IllegalStateException("Context has wrong model change type: " + modelChangeType + " (expected: " + ModelChangeType.SET_PROPERTY + ")");
		}
		var featureName = contextData.get(ContextDataKeys.PROPERTY_NAME);
		if (featureName == null) {
			throw new IllegalStateException("Context has no feature in its context data");
		}
		for (var feature : features) {
			if (featureName.equals(getQualifiedFeatureName(feature))) {
				return true;
			}
		}
		return false;
	}
		
	public void putContextData(DiagramNode diagramNode) {
		if (diagramNode instanceof ConnectionLabel connectionLabel) {
			putContextData(connectionLabel.getMemberRole(), memberRoleContextDataAssembler);
		} else if (diagramNode instanceof Connector connector) {
			putContextData(connector, connectorContextDataAssembler);
		} else if (diagramNode instanceof DiagramLabel) {
			// nothing to set
		} else if (diagramNode instanceof SchemaRecord schemaRecord) {
			putContextData(schemaRecord, recordContextDataAssembler);
		} else if (diagramNode instanceof SystemOwner systemOwner) {
			putContextData(systemOwner.getSet(), setContextDataAssembler);
		} else if (diagramNode instanceof VsamIndex vsamIndex) {
			putContextData(vsamIndex.getSet(), setContextDataAssembler);
		} else if (diagramNode != null) {
			throw new IllegalArgumentException("DiagramNode type invalid: " + diagramNode.getClass().getName());
		} else {
			throw new IllegalArgumentException("DiagramNode type invalid: null");
		}
	}
	
	public void putContextData(EObject model, EStructuralFeature feature) {
		if (modelChangeType != ModelChangeType.SET_PROPERTY) {
			throw new IllegalStateException("Invalid model change type: " + modelChangeType);
		} else if (feature == null) {
			throw new IllegalArgumentException("Invalid feature: null");
		} else if (model == null){
			throw new IllegalArgumentException("Model type invalid: null");
		}
		
		var featureName = ModelChangeContext.getQualifiedFeatureName(feature);
		contextData.put(ContextDataKeys.PROPERTY_NAME, featureName);
				
		if (model instanceof SchemaArea schemaArea) {
			putContextData(schemaArea, areaContextDataAssembler);
		} else if (model instanceof SchemaRecord schemaRecord) {
			putContextData(schemaRecord, recordContextDataAssembler);	
		} else if (model instanceof Set set) {
			putContextData(set, setContextDataAssembler);
		} else if (model instanceof ConnectionPart connectionPart) {
			putContextData(connectionPart, connectionPartContextDataAssembler);
		} else if (model instanceof Connector connector) {
			putContextData(connector, connectorContextDataAssembler);
		} else if (model instanceof Guide guide) {
			putContextData(guide, guideContextDataAssembler);
		} else if (model instanceof MemberRole memberRole) {
			putContextData(memberRole, memberRoleContextDataAssembler);
		} else if (model instanceof Ruler ruler) {
			putContextData(ruler, rulerContextDataAssembler);
		} else if (model instanceof VsamType vsamType) {
			putContextData(vsamType.getRecord(), recordContextDataAssembler);
		} else if (!(model instanceof DiagramLabel) &&!(model instanceof DiagramData) &&!(model instanceof Schema)) {
			throw new IllegalArgumentException("Model type invalid: " + model.getClass().getName());
		}
	}
	
	public <T extends EObject> void putContextData(T model, Function<T, List<ContextData>> contextDataAssembler) {
		contextDataAssembler.apply(model).stream()
			.forEach(d -> contextData.put(d.key(), d.value()));
	}

	/**
	 * This attribute should only be set by the model change dispatcher and NOT by the component that creates the
	 * initial context before handling a model change command to the command stack.
	 * @param commandExecutionMode the command execution mode during which the context is passed to 
	 *        model change listeners
	 */
	public void setCommandExecutionMode(CommandExecutionMode commandExecutionMode) {
		this.commandExecutionMode = commandExecutionMode;
	}

	/**
	 * This attribute should NOT be set by the component that creates the initial context before handling a model
	 * change command to the command stack.  It is intended <i>only</i> for model change listeners, that can add
	 * a 'listener object' to the context <i>before</i> the model change is carried out, which can then be
	 * retrieved <i>after</i> the model has been changed.
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
		return "ModelChangeContext [modelChangeType=" + modelChangeType + ", commandExecutionMode=" + commandExecutionMode +
				", contextData=" + contextData + ", listenerData=" + listenerData + "]";
	}
	
	public static record ContextData(String key, String value) {
	}
	
}
