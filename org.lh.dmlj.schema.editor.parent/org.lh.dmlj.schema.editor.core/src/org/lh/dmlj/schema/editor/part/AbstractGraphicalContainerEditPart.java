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
package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.infrastructure.IContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;

public abstract class AbstractGraphicalContainerEditPart<T extends EObject> 
	extends AbstractGraphicalEditPart implements IModelChangeListener {
	
	protected static final String TO_CREATE_OR_REMOVE = "toCreateOrRemove";
	protected static final String TO_REFRESH = "toRefresh";
	
	protected static enum Scope {ALL, CONNECTORS_ONLY}

	protected static Map<String, List<EObject>> createModelChildrenActionMap(ModelChangeContext context) {
		Map<String, List<EObject>> map = new HashMap<>();
		map.put(TO_CREATE_OR_REMOVE, new ArrayList<EObject>());
		map.put(TO_REFRESH, new ArrayList<EObject>());
		context.setListenerData(map);
		return map;
	}	
	
	protected IModelChangeProvider modelChangeProvider;
	protected SchemaEditor schemaEditor;

	protected AbstractGraphicalContainerEditPart(T model, SchemaEditor schemaEditor) {
		super();
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
		
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		
		Map<String, List<EObject>> map = createModelChildrenActionMap(context);
		List<EObject> toCreateOrRemove = map.get(TO_CREATE_OR_REMOVE);
		List<EObject> toRefresh = map.get(TO_REFRESH);
		
		SchemaRecord memberRecord = context.getSchema().getRecord(memberRecordName);
		MemberRole memberRole = (MemberRole) memberRecord.getRole(setName);
		Set set = memberRole.getSet();
		
		if (set.getSystemOwner() != null) {
			if (scope == Scope.ALL) {
				toCreateOrRemove.add(set.getSystemOwner());
			} else if (scope == Scope.CONNECTORS_ONLY) {
				toRefresh.add(set.getSystemOwner());
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
		String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord record = context.getSchema().getRecord(recordName);
		
		Map<String, List<EObject>> map = createModelChildrenActionMap(context);
		List<EObject> toCreate = map.get(TO_CREATE_OR_REMOVE);
		List<EObject> toRefresh = map.get(TO_REFRESH);
		
		toCreate.add(record);		
		for (OwnerRole ownerRole : record.getOwnerRoles()) {
			Set set = ownerRole.getSet();
			for (MemberRole memberRole : set.getMembers()) {
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
		for (MemberRole memberRole : record.getMemberRoles()) {
			toCreate.add(memberRole.getConnectionLabel());
			if (memberRole.getConnectionParts().size() > 1) {
				toCreate.add(memberRole.getConnectionParts().get(0).getConnector());
				toCreate.add(memberRole.getConnectionParts().get(1).getConnector());
			}
			if (memberRole.getSet().getSystemOwner() != null) {
				toCreate.add(memberRole.getSet().getSystemOwner());
			} else {
				if (!toRefresh.contains(memberRole.getSet().getOwner().getRecord())) {
					toRefresh.add(memberRole.getSet().getOwner().getRecord());
				}
			}
		}		
	}
	
	protected final void collectObjectsForSet(ModelChangeContext context) {				
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		Set set = context.getSchema().getSet(setName);		
		collectObjectsForSet(context, set);
	}
	
	protected final void collectObjectsForSet(ModelChangeContext context, Set set) {		
		
		Map<String, List<EObject>> map = createModelChildrenActionMap(context);
		List<EObject> toRemove = map.get(TO_CREATE_OR_REMOVE);		
		List<EObject> toRefresh = map.get(TO_REFRESH);
		
		if (set.getSystemOwner() != null) {
			toRemove.add(set.getSystemOwner());			
		} else {
			toRefresh.add(set.getOwner().getRecord());
		}
		for (MemberRole memberRole : set.getMembers()) {		
			SchemaRecord memberRecord = memberRole.getRecord();
			toRefresh.add(memberRecord);			
			toRemove.add(memberRole.getConnectionLabel());
			if (memberRole.getConnectionParts().size() > 1) {
				toRemove.add(memberRole.getConnectionParts().get(0).getConnector());
				toRemove.add(memberRole.getConnectionParts().get(1).getConnector());
			}		
		}
	}	
	
	protected final void createAndAddChild(EObject model) {
		EditPart newChild = 
			SchemaDiagramEditPartFactory.createEditPart(model, modelChangeProvider, schemaEditor);
		addChild(newChild, getChildren().size());		
	}

	protected final void createAndAddChildren(ModelChangeContext context) {		
		@SuppressWarnings("unchecked")
		Map<String, List<EObject>> map = (Map<String, List<EObject>>) context.getListenerData();
		List<EObject> toCreate = map.get(TO_CREATE_OR_REMOVE);				
		for (EObject model : toCreate) {
			createAndAddChild(model);
		}
	}
	
	@Override
	protected final IFigure createFigure() {
		Figure figure = new FreeformLayer();
		figure.setBorder(new MarginBorder(3));
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}
	
	protected final void findAndRefreshChildren(ModelChangeContext context) {		
		@SuppressWarnings("unchecked")
		Map<String, List<EObject>> map = (Map<String, List<EObject>>) context.getListenerData();
		List<EObject> toRefresh = map.get(TO_REFRESH);
		findAndRefreshChildren(toRefresh);				
	}

	protected final void findAndRefreshChild(EObject model) {
		Assert.isNotNull(model, "model is null");
		EditPart child = (EditPart) getViewer().getEditPartRegistry().get(model);
		Assert.isNotNull(child, "missing child edit part: " + model);
		child.refresh();
	}

	protected final void findAndRefreshChildren(List<EObject> models) {
		if (models == null || models.isEmpty()) {
			return;
		}
		for (EObject model : models) {
			findAndRefreshChild(model);
		}
	}
	
	protected final void findAndRemoveChildren(ModelChangeContext context) {		
		@SuppressWarnings("unchecked")
		Map<String, List<EObject>> map = (Map<String, List<EObject>>) context.getListenerData();
		List<EObject> toRemove = map.get(TO_CREATE_OR_REMOVE);				
		findAndRemoveChildren(toRemove);			
	}

	protected final void findAndRemoveChild(EObject model) {
		Assert.isNotNull(model, "model is null");
		EditPart obsoleteChild = (EditPart) getViewer().getEditPartRegistry().get(model);
		Assert.isNotNull(obsoleteChild, "missing obsolete child edit part: " + model);
		removeChild(obsoleteChild);
	}

	protected final void findAndRemoveChildren(List<EObject> models) {
		if (models == null || models.isEmpty()) {
			return;
		}
		for (EObject model : models) {
			findAndRemoveChild(model);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final T getModel() {
		return (T) super.getModel();
	}
	
	@Override
	public final void removeNotify() {		
		// note: this method doesn't seem to be called at all
		modelChangeProvider.removeModelChangeListener(this);
		super.removeNotify();
	}
	
}
