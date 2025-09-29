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
package org.lh.dmlj.schema.editor.outline.part;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.ContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;

public class SchemaTreeEditPart extends AbstractSchemaTreeEditPart<Schema> {
	
	public SchemaTreeEditPart(Schema schema, IModelChangeProvider modelChangeProvider) {
		super(schema, modelChangeProvider);
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION) {		
			// whenever an area specification is changed, an area might be added or removed; get the 'old' area
			// from the context's listener data; if it has bcome obsolete, remove the corresponding child edit part
			var oldArea = (SchemaArea) context.getListenerData();
			if (getModel().getArea(oldArea.getName()) == null) {
				findAndRemoveChild(oldArea, true);
			}
			// get the 'new' area via the item contained in the context data; if it was effectively newly created,
			// create an edit part and add it as a child
			SchemaArea newArea;
			if (context.getContextData().containsKey(ContextDataKeys.RECORD_NAME)) {
				var recordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
				var schemaRecord = getModel().getRecord(recordName);
				newArea = schemaRecord.getAreaSpecification().getArea();
			} else {
				var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
				newArea = getModel().getSet(setName).getSystemOwner().getAreaSpecification().getArea();
			}
			if (!hasChildFor(newArea)) {				
				createAndAddChild(newArea);
			}
		} else if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
			afterExecuteOrRedoModelChange(context);
		} else {
			afterUndoModelChange(context);
		}
	}
	
	private void afterExecuteOrRedoModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_DIAGRAM_LABEL) {
			// a diagram label was added; avoid just refreshing the children since this may be costly; create the
			// edit part and add it as a child
			var diagramLabel = getModel().getDiagramData().getLabel();
			createAndAddChild(diagramLabel);
			findAndRemoveChild(diagramLabel, true);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_RECORD) {
			// a RECORD was added; avoid just refreshing the children since this may be costly; first, create an
			// edit part for the record and add it as a child
			var schemaRecord = getModel().getRecords().get(getModel().getRecords().size() - 1);			
			createAndAddChild(schemaRecord);
			
			// second, when a record is added, an AREA might also have been created and added (this is the case
			// when the schema has exactly 1 area and exactly 1 record at this point); create an edit part for
			// the record's area and add it as a child as well
			if (getModel().getAreas().size() == 1 && getModel().getRecords().size() == 1) {
				var area = schemaRecord.getAreaSpecification().getArea();
				createAndAddChild(area);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET) {
			// a system owned indexed set was added; avoid just refreshing the children since this may be
			// costly; first, create the appropriate edit part for the set and add it as a child
			var set = getModel().getSets().get(getModel().getSets().size() - 1); 
			createAndAddChild(set.getSystemOwner(), set);						
			
			// second, when a system owned indexed set is added, an area is also created and added; create an
			// edit part for the index AREA and add it as a child as well BUT ONLY WHEN NEEDED
			var area = set.getSystemOwner().getAreaSpecification().getArea();
			if (!hasChildFor(area)) {				
				// no child edit part exists for the area; create and add one				
				createAndAddChild(area);
			}		
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET) {
			// a user owned set was added; avoid just refreshing the children since this may be costly; create
			// the appropriate edit part for the set and add it as a child
			var set = getModel().getSets().get(getModel().getSets().size() - 1);
			createAndAddChild(set);		
		} else if (context.getModelChangeType() == ModelChangeType.ADD_VSAM_INDEX) {
			// a VSAM index was added; avoid just refreshing the children since this may be costly; create the
			// appropriate edit part for the set and add it as a child
			var set = getModel().getSets().get(getModel().getSets().size() - 1); 
			createAndAddChild(set.getVsamIndex(), set);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_DIAGRAM_LABEL) {
			// the diagram label was deleted; get the diagram label from the context's listener data and remove
			// the child edit part
			var diagramLabel = (DiagramLabel) context.getListenerData();
			findAndRemoveChild(diagramLabel, true);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_RECORD) {
			// a record was deleted; we've put it in the context's listener data when processing the before model
			// change event
			var schemaRecord = (SchemaRecord) context.getListenerData();
			findAndRemoveChild(schemaRecord, true);
			
			// make sure to remove the area child as well if the area was removed
			removeObsoleteChildren();
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET) {
			// a system owned indexed was removed; avoid just refreshing the children since this may be costly -
			// find the edit part and remove it as a child (we've put the system owner in the context's listener
			// data while processing the before model change event)
			var systemOwner = (SystemOwner) context.getListenerData();
			findAndRemoveChild(systemOwner, true);
			
			// remove the system owner's area child edit part as well when it no longer exists
			removeObsoleteChildren();
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET) {
			// a user owned set was removed; avoid just refreshing the children since this may be costly; find
			// the edit part and remove it as a child (we've put the set in the context's listener data while
			// processing the before model change event)
			var set = (Set) context.getListenerData();
			findAndRemoveChild(set, true);		
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_VSAM_INDEX) {
			// a VSAM index was removed; avoid just refreshing the children since this may be costly; find the
			// edit part and remove it as a child (we've put the VSAM index in the context's listener data while
			// processing the before model change event)
			var vsamIndex = (VsamIndex) context.getListenerData();
			findAndRemoveChild(vsamIndex, true);
		}		
	}
	
	private void afterUndoModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_DIAGRAM_LABEL) {
			// an add diagram label operation was undone; avoid just refreshing the children since this may be
			// costly; find the edit part and remove it as a child (we've put the diagram label in the context's
			// listener data on the before model change callback)
			var diagramLabel = (DiagramLabel) context.getListenerData();
			findAndRemoveChild(diagramLabel, true);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_RECORD) {
			// an add record operation was undone; the record will be in the context's listener data
			var schemaRecord = (SchemaRecord) context.getListenerData();
			findAndRemoveChild(schemaRecord, true);
			
			// make sure to remove the area child as well if the area was removed
			removeObsoleteChildren();
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET) {
			// an add system owned indexed set operation was undone; remove the appropriate child using the model
			// system owner which we put in the context's listener data while processing the before model change
			// event
			var systemOwner = (SystemOwner) context.getListenerData();
			findAndRemoveChild(systemOwner, true);
			
			// make sure we remove the child edit part for the removed area as well
			removeObsoleteChildren();
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET) {
			// an add user owned set operation was undone; remove the appropriate child using the model set which
			// we put in the context's listener data while processing the before model change event
			var set = (Set) context.getListenerData();
			findAndRemoveChild(set, true);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_VSAM_INDEX) {
			// an add VSAM index set operation was undone; remove the appropriate child using the model VSAM
			// index which we put in the context's listener data while processing the before model change event
			var vsamIndex = (VsamIndex) context.getListenerData();
			findAndRemoveChild(vsamIndex, true);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_DIAGRAM_LABEL) {
			// a delete diagram label operation was undone; avoid just refreshing the children since this may be
			// costly; create the edit part and add it as a child
			var diagramLabel = getModel().getDiagramData().getLabel();
			createAndAddChild(diagramLabel);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_RECORD) {
			// a delete record operation is being undone; get the record using the context data and create and
			// add a child edit part for it
			var recordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
			var schemaRecord = getModel().getRecord(recordName);			
			createAndAddChild(schemaRecord);
			
			// second, when a delete record operation is undone, an AREA might also have been added again (this
			// is the case when the schema has exactly 1 area and exactly 1 record at this point); create an edit
			// part for the record's area and add it as a child as well
			if (getModel().getAreas().size() == 1 && getModel().getRecords().size() == 1) {
				var area = schemaRecord.getAreaSpecification().getArea();
				createAndAddChild(area);
			}		
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET) {
			// a delete system owned indexed set operation was undone; avoid just refreshing the children since
			// this may be costly; first, create the appropriate edit part for the set and add it as a child
			var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
			var set = getModel().getSet(setName);
			createAndAddChild(set.getSystemOwner(), set);
			
			// take care of the system owner's area too
			var area = set.getSystemOwner().getAreaSpecification().getArea();
			if (!hasChildFor(area)) {				
				// no child edit part exists for the area; create and add one				
				createAndAddChild(area);
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET) {
			// a delete user owned set operation was undone; avoid just refreshing the children since this may be
			// costly; create the appropriate edit part for the set and add it as a child
			var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
			var set = getModel().getSet(setName);
			createAndAddChild(set);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_VSAM_INDEX) {
			// a delete VSAM index operation was undone; avoid just refreshing the children since this may be
			// costly; create the appropriate edit part for the set and add it as a child
			var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
			var set = getModel().getSet(setName);
			createAndAddChild(set.getVsamIndex(), set);			
		}		
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.CHANGE_AREA_SPECIFICATION) {
			// whenever an area specification is changed, an area might be added or removed; put the 'old'area
			// in the context's listener data
			SchemaArea area;
			if (context.getContextData().containsKey(ContextDataKeys.RECORD_NAME)) {
				var recordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
				var schemaRecord = getModel().getRecord(recordName);
				area = schemaRecord.getAreaSpecification().getArea();
			} else {
				var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
				area = getModel().getSet(setName).getSystemOwner().getAreaSpecification().getArea();
			}
			context.setListenerData(area);
		} else if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
			beforeExecuteOrRedoModelChange(context);
		} else {
			beforeUndoModelChange(context);
		}
	}

	private void beforeExecuteOrRedoModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.DELETE_DIAGRAM_LABEL) {
			// the diagram label is being deleted; put the diagram label in the context's listener data because
			// it will be gone when processing the after model change event
			var diagramLabel = getModel().getDiagramData().getLabel();
			context.setListenerData(diagramLabel);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_RECORD) {
			// a record is being deleted; put it in the context's listener data so that we can refer to it when
			// processing the after model change event
			var recordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
			var schemaRecord = getModel().getRecord(recordName);
			context.setListenerData(schemaRecord);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET) {
			// a system owned indexed set is being deleted; put the system owner in the context's listener data
			// so that we can refer to it when processing the after model change event
			var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
			var set = getModel().getSet(setName);
			context.setListenerData(set.getSystemOwner());			
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET) {
			// a user owned set is being deleted; put it in the context's listener data so that we can refer to
			// it when processing the after model change event
			var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
			var set = getModel().getSet(setName);
			context.setListenerData(set);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_VSAM_INDEX) {
			// a VSAM index is being deleted; put the VSAM index in the context's listener data so that we can
			// refer to it when processing the after model change event
			var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
			var set = getModel().getSet(setName);
			context.setListenerData(set.getVsamIndex());			
		}		
	}
	
	private void beforeUndoModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_DIAGRAM_LABEL) {
			// an add diagram label operation is being undone; put the diagram label in the context's listener
			// data because it will be gone when processing the after model change event
			var diagramLabel = getModel().getDiagramData().getLabel();
			context.setListenerData(diagramLabel);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_RECORD) {
			// an add record operation is being undone; put the record in the context's listener data so that we
			// can refer to it in the after model change callback
			var schemaRecord = getModel().getRecords().get(getModel().getRecords().size() - 1);
			context.setListenerData(schemaRecord);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET) {
			// an add system owned indexed set operation is being undone; put the system owner in the context's
			// listener data so that we can refer to it when processing the after model change event
			var set = getModel().getSets().get(getModel().getSets().size() - 1);
			context.setListenerData(set.getSystemOwner());
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET) {
			// an add user owned set operation is being undone; put the set in the context's listener data so
			// that we can refer to it when processing the after model change event
			var set = getModel().getSets().get(getModel().getSets().size() - 1);
			context.setListenerData(set);		
		} else if (context.getModelChangeType() == ModelChangeType.ADD_VSAM_INDEX) {
			// an add VSAM index operation is being undone; put the VSAM index in the context's listener data so
			// that we can refer to it when processing the after model change event
			var set = getModel().getSets().get(getModel().getSets().size() - 1);
			context.setListenerData(set.getVsamIndex());
		}		
	}

	@Override
	protected Class<?>[] getChildNodeTextProviderOrder() {
		return new Class<?>[] {DiagramLabel.class, SchemaArea.class, SchemaRecord.class, Set.class};	
	}	
	
	@Override
	protected String getImagePath() {
		return "icons/schema.gif";
	}	
	
	@Override
	protected List<?> getModelChildren() {
		var children = new ArrayList<EObject>();
		
		// add the diagram label, if present
		if (getModel().getDiagramData().getLabel() != null) {
			children.add(getModel().getDiagramData().getLabel());
		}
		
		// add the areas in alphabetical order
		getModel().getAreas().stream()
				.sorted(comparing(SchemaArea::getName, String.CASE_INSENSITIVE_ORDER))
				.forEach(children::add);
		
		// add the records in alphabetical order
		getModel().getRecords().stream()
				.sorted(comparing(SchemaRecord::getName, String.CASE_INSENSITIVE_ORDER))
				.forEach(children::add);
		
		// add the sets, system owners and VSAM indexes in alphabetical order
		getModel().getSets().stream()
				.sorted(comparing(Set::getName, String.CASE_INSENSITIVE_ORDER))
				.map(this::toRelevantEObject)
				.forEach(children::add);	
		
		return children;
	}
	
	private EObject toRelevantEObject(Set set) {
		if (set.isVsam()) {
			return set.getVsamIndex();
		} else if (set.getSystemOwner() != null) {
			return set.getSystemOwner();
		} else {
			return set;
		}
	}

	@Override
	protected WrappedNodeTextProvider getNodeTextProvider() {
		return new WrappedNodeTextProvider(getModel());
	}

	private void removeObsoleteChildren() {
		// remove child edit parts that refer to areas that no longer exist in the schema
		List.copyOf(getChildren()).stream()
				.filter(child -> EditPart.class.cast(child).getModel() instanceof SchemaArea area && area.getSchema() == null)
				.forEach(child -> removeChild(EditPart.class.cast(child)));
	}
	
}
