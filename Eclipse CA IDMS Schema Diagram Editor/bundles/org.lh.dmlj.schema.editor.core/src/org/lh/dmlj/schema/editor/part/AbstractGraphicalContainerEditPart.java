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
package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.infrastructure.ContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;

public abstract class AbstractGraphicalContainerEditPart<T extends EObject> extends AbstractGraphicalEditPart implements IModelChangeListener {
	protected static final String TO_CREATE_OR_REMOVE = "toCreateOrRemove";
	protected static final String TO_REFRESH = "toRefresh";
	
	protected enum Scope {ALL, CONNECTORS_ONLY}

	protected static Map<String, List<EObject>> createModelChildrenActionMap(ModelChangeContext context) {
		var map = new HashMap<String, List<EObject>>();
		map.put(TO_CREATE_OR_REMOVE, new ArrayList<>());
		map.put(TO_REFRESH, new ArrayList<>());
		context.setListenerData(map);
		return map;
	}	
	
	protected IModelChangeProvider modelChangeProvider;
	protected SchemaEditor schemaEditor;

	protected AbstractGraphicalContainerEditPart(T model, SchemaEditor schemaEditor) {
		setModel(model);
		this.schemaEditor = schemaEditor;
		modelChangeProvider = (IModelChangeProvider) schemaEditor.getAdapter(IModelChangeProvider.class);
	}
	
	@Override
	public final void addNotify() {
		super.addNotify();
		modelChangeProvider.addModelChangeListener(this);
	}
	
	protected final void collectObjectsForMemberRole(ModelChangeContext context, Scope scope) {
		var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
		var memberRecordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
		
		var map = createModelChildrenActionMap(context);
		var toCreateOrRemove = map.get(TO_CREATE_OR_REMOVE);
		var toRefresh = map.get(TO_REFRESH);
		
		var memberRecord = context.getSchema().getRecord(memberRecordName);
		var memberRole = (MemberRole) memberRecord.getRole(setName);
		var set = memberRole.getSet();
		if (set.getSystemOwner() != null) {
			if (scope == Scope.ALL) {
				toCreateOrRemove.add(set.getSystemOwner());
			} else if (scope == Scope.CONNECTORS_ONLY) {
				toRefresh.add(set.getSystemOwner());
			}
		} else if (set.getVsamIndex() != null) {
			if (scope == Scope.ALL) {
				toCreateOrRemove.add(set.getVsamIndex());
			} else if (scope == Scope.CONNECTORS_ONLY) {
				toRefresh.add(set.getVsamIndex());
			}		
		} else {
			toRefresh.add(set.getOwner().getRecord());
		}
		toRefresh.add(memberRecord);
		
		if (scope == Scope.ALL) {
			toCreateOrRemove.add(memberRole.getConnectionLabel());
		}
		if (memberRole.getConnectionParts().size() > 1) {
			toCreateOrRemove.add(memberRole.getConnectionParts().get(0).getConnector());
			toCreateOrRemove.add(memberRole.getConnectionParts().get(1).getConnector());
		}
	}
	
	protected final void collectObjectsForRecord(ModelChangeContext context) {
		var recordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
		var schemaRecord = context.getSchema().getRecord(recordName);
		
		var map = createModelChildrenActionMap(context);
		var toCreate = map.get(TO_CREATE_OR_REMOVE);
		var toRefresh = map.get(TO_REFRESH);
		
		toCreate.add(schemaRecord);
		addItemsViaOwnerRoles(schemaRecord, toCreate, toRefresh);
		addItemsViaMemberRoles(schemaRecord, toCreate, toRefresh);
	}
	
	private void addItemsViaOwnerRoles(SchemaRecord schemaRecord, List<EObject> toCreate, List<EObject> toRefresh) {
		for (var ownerRole : schemaRecord.getOwnerRoles()) {
			var set = ownerRole.getSet();
			for (var memberRole : set.getMembers()) {
				toCreate.add(memberRole.getConnectionLabel());
				if (memberRole.getConnectionParts().size() > 1) {
					toCreate.add(memberRole.getConnectionParts().get(0).getConnector());
					toCreate.add(memberRole.getConnectionParts().get(1).getConnector());
				}
				if (!toRefresh.contains(memberRole.getRecord())) {
					toRefresh.add(memberRole.getRecord());
				}
			}
		}
	}
	
	private void addItemsViaMemberRoles(SchemaRecord schemaRecord, List<EObject> toCreate, List<EObject> toRefresh) {
		for (var memberRole : schemaRecord.getMemberRoles()) {
			toCreate.add(memberRole.getConnectionLabel());
			if (memberRole.getConnectionParts().size() > 1) {
				toCreate.add(memberRole.getConnectionParts().get(0).getConnector());
				toCreate.add(memberRole.getConnectionParts().get(1).getConnector());
			}
			if (memberRole.getSet().getSystemOwner() != null) {
				toCreate.add(memberRole.getSet().getSystemOwner());
			} else if (memberRole.getSet().getOwner() != null && !toRefresh.contains(memberRole.getSet().getOwner().getRecord())) {
				toRefresh.add(memberRole.getSet().getOwner().getRecord());
			}
		}
	}
	
	protected final void collectObjectsForSet(ModelChangeContext context) {				
		var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
		var set = context.getSchema().getSet(setName);		
		collectObjectsForSet(context, set);
	}
	
	protected final void collectObjectsForSet(ModelChangeContext context, Set set) {
		var map = createModelChildrenActionMap(context);
		var toCreateOrRemove = map.get(TO_CREATE_OR_REMOVE);		
		var toRefresh = map.get(TO_REFRESH);
		if (set.getSystemOwner() != null) {
			toCreateOrRemove.add(set.getSystemOwner());			
		} else if (set.getVsamIndex() != null) {
			toCreateOrRemove.add(set.getVsamIndex());			
		} else if (set.getOwner() != null) {
			toRefresh.add(set.getOwner().getRecord());
		}
		for (var memberRole : set.getMembers()) {		
			var memberRecord = memberRole.getRecord();
			toRefresh.add(memberRecord);			
			toCreateOrRemove.add(memberRole.getConnectionLabel());
			if (memberRole.getConnectionParts().size() > 1) {
				toCreateOrRemove.add(memberRole.getConnectionParts().get(0).getConnector());
				toCreateOrRemove.add(memberRole.getConnectionParts().get(1).getConnector());
			}		
		}
	}	
	
	protected final void createAndAddChild(EObject model) {
		var newChild = SchemaDiagramEditPartFactory.createEditPart(model, modelChangeProvider, schemaEditor);
		addChild(newChild, getChildren().size());		
	}

	protected final void createAndAddChildren(ModelChangeContext context) {		
		@SuppressWarnings("unchecked")
		var map = (Map<String, List<EObject>>) context.getListenerData();
		var toCreate = map.get(TO_CREATE_OR_REMOVE);				
		for (var model : toCreate) {
			createAndAddChild(model);
		}
	}
	
	@Override
	protected final IFigure createFigure() {
		var figure = new FreeformLayer();
		figure.setBorder(new MarginBorder(3));
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}
	
	protected final void findAndRefreshChildren(ModelChangeContext context) {		
		@SuppressWarnings("unchecked")
		var map = (Map<String, List<EObject>>) context.getListenerData();
		var toRefresh = map.get(TO_REFRESH);
		findAndRefreshChildren(toRefresh);				
	}

	protected final void findAndRefreshChild(EObject model) {
		Assert.isNotNull(model, "model is null");
		var child = EditPart.class.cast(getViewer().getEditPartRegistry().get(model));
		Assert.isNotNull(child, "missing child edit part: " + model);
		child.refresh();
	}

	protected final void findAndRefreshChildren(List<EObject> models) {
		if (models == null || models.isEmpty()) {
			return;
		}
		for (var model : models) {
			findAndRefreshChild(model);
		}
	}
	
	protected final void findAndRemoveChildren(ModelChangeContext context) {		
		@SuppressWarnings("unchecked")
		var map = (Map<String, List<EObject>>) context.getListenerData();
		var toRemove = map.get(TO_CREATE_OR_REMOVE);				
		findAndRemoveChildren(toRemove);			
	}

	protected final void findAndRemoveChild(EObject model) {
		Assert.isNotNull(model, "model is null");
		var obsoleteChild = EditPart.class.cast(getViewer().getEditPartRegistry().get(model));
		Assert.isNotNull(obsoleteChild, "missing obsolete child edit part: " + model);
		removeChild(obsoleteChild);
	}

	protected final void findAndRemoveChildren(List<EObject> models) {
		if (models == null || models.isEmpty()) {
			return;
		}
		for (var model : models) {
			findAndRemoveChild(model);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final T getModel() {
		return (T) super.getModel();
	}
	
	protected boolean isReadOnlyMode() {
		return schemaEditor.isReadOnlyMode();
	}
	
	@Override
	public final void removeNotify() {		
		// note: this method doesn't seem to be called at all
		modelChangeProvider.removeModelChangeListener(this);
		super.removeNotify();
	}
	
}
