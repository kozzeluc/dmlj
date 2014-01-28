/**
 * Copyright (C) 2013  Luc Hermans
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.EditPart;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.INodeTextProvider;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;

public class SchemaTreeEditPart extends AbstractSchemaTreeEditPart<Schema> {
	
	public SchemaTreeEditPart(Schema schema, 
							  IModelChangeProvider modelChangeProvider) {
		
		super(schema, modelChangeProvider);
	}

	@Override
	public void afterAddItem(EObject owner, EReference reference, Object item) {
		
		if (owner == getModel().getDiagramData() && 
			reference == SchemaPackage.eINSTANCE.getDiagramData_Label()) {
			
			// a DIAGRAM LABEL was added; avoid just refreshing the children since this may be 
			// costly; create the edit part and add it as a child
			EditPart child = SchemaTreeEditPartFactory.createEditPart(item, modelChangeProvider);
			addChild(child, 0);		// the diagram label is always the first child			
		
		} else if (owner == getModel() && reference == SchemaPackage.eINSTANCE.getSchema_Areas()) {			
			
			// an AREA was added; avoid just refreshing the children since this may be costly; 			
			// first, create an edit part for the area and add it as a child
			SchemaArea area = (SchemaArea) item;
			int i = getInsertionIndex(getChildren(), area, getChildNodeTextProviderOrder());
			EditPart child = SchemaTreeEditPartFactory.createEditPart(area, modelChangeProvider);
			addChild(child, i);
			
		} else if (owner == getModel() && reference == SchemaPackage.eINSTANCE.getSchema_Records()) {
			
			// a RECORD was added; avoid just refreshing the children since this may be costly;  			
			// first, create an edit part for the record and add it as a child
			SchemaRecord record = (SchemaRecord) item;
			int i = getInsertionIndex(getChildren(), record, getChildNodeTextProviderOrder());
			EditPart child = SchemaTreeEditPartFactory.createEditPart(record, modelChangeProvider);
			addChild(child, i);
			
			// second, when a record is added, an AREA is also created and added; create an edit  
			// part for the record's area and add it as a child as well - THIS MAY CHANGE IN THE 
			// FUTURE
			SchemaArea area = record.getAreaSpecification().getArea();
			i = getInsertionIndex(getChildren(), area, getChildNodeTextProviderOrder());
			EditPart child2 = SchemaTreeEditPartFactory.createEditPart(area, modelChangeProvider);
			addChild(child2, i);			
		
		} else if (owner == getModel() && reference == SchemaPackage.eINSTANCE.getSchema_Sets()) {			
			
			// a SET was added; avoid just refreshing the children since this may be costly; first,
			// create the appropriate edit part for the set and add it as a child
			Set set = (Set) item;
			EObject model;
			if (set.getSystemOwner() == null) {
				// USER OWNER
				model = set.getMembers().get(0).getConnectionParts().get(0);
			} else {
				// SYSTEM OWNER
				model = set.getSystemOwner();
			}
			int i = getInsertionIndex(getChildren(), set, getChildNodeTextProviderOrder());
			EditPart child = SchemaTreeEditPartFactory.createEditPart(model, modelChangeProvider);			
			addChild(child, i); 	
			
			// second, when a system owned indexed set is added, an area is also created and added;  
			// create an edit part for the index AREA and add it as a child as well BUT ONLY WHEN
			// NEEDED
			if (set.getSystemOwner() != null) {				
				SchemaArea area = set.getSystemOwner().getAreaSpecification().getArea();
				try {
					childFor(area);
					// if we get here, there is already a child edit part for the area
				} catch (IllegalArgumentException e) {
					// no child edit partexists for the area; create and add one
					i = getInsertionIndex(getChildren(), area, getChildNodeTextProviderOrder());				
					EditPart child2 = 
						SchemaTreeEditPartFactory.createEditPart(area, modelChangeProvider);
					addChild(child2, i);
				}				
			}
		
		}
		
	}
	
	@Override
	public void afterMoveItem(EObject oldOwner, EReference reference, Object item, 
							  EObject newOwner) {
		
		if (reference == SchemaPackage.eINSTANCE.getSchemaArea_AreaSpecifications()) {
			// a record or system owner is moved to another area; remove the area edit part 
			// belonging to the old area if the old area was removed from the schema because it 
			// became empty...
			SchemaArea oldArea = (SchemaArea) oldOwner;
			if (getModel().getArea(oldArea.getName()) == null) {
				// the old area was removed from the schema
				EditPart child = childFor(oldArea);
				removeChild(child);
			}
			// ...and/or create a new edit part for the new area if it was created because of the 
			// move
			SchemaArea newArea = (SchemaArea) newOwner;
			boolean createNewEditPart = true;
			for (Object child : getChildren()) {
				if (((EditPart)child).getModel() == newArea) {
					// no need to create an edit part for the new area since it is already there; it
					// will refresh itself and its children itself
					createNewEditPart = false;
					break;
				}
			}
			if (createNewEditPart) {
				// we need to create a new child edit part because the new area is indeed new
				EditPart child = 
					SchemaTreeEditPartFactory.createEditPart(newArea, modelChangeProvider);
				int index = 
					getInsertionIndex(getChildren(), newArea, getChildNodeTextProviderOrder());
				addChild(child, index);
			}
		}
		
	}
	
	@Override
	public void afterRemoveItem(EObject owner, EReference reference, Object item) {
		if (owner == getModel().getDiagramData() && 
			reference == SchemaPackage.eINSTANCE.getDiagramData_Label()) {
			
			// the diagram label was removed; avoid just refreshing the children since this may be 
			// costly; find the edit part and remove it as a child
			EditPart child = (EditPart) getViewer().getEditPartRegistry().get(item);
			removeChild(child);
		
		} else if (owner == getModel() && reference == SchemaPackage.eINSTANCE.getSchema_Areas()) {
			
			// an area was removed; avoid just refreshing the children since this may be costly; 
			// find the edit part and remove it as a child
			EditPart child = (EditPart) getViewer().getEditPartRegistry().get(item);
			removeChild(child);
			
		} else if (owner == getModel() && reference == SchemaPackage.eINSTANCE.getSchema_Records()) {
			
			// a record was removed; avoid just refreshing the children since this may be costly; 
			// find the edit part and remove it as a child
			EditPart child = (EditPart) getViewer().getEditPartRegistry().get(item);
			removeChild(child);
			
			// remove the record's area when it no longer exists
			removeObsoleteChildren();
			
		} else if (owner == getModel() && reference == SchemaPackage.eINSTANCE.getSchema_Sets()) {			
			
			// a set was removed; avoid just refreshing the children since this may be costly; 
			// find the edit part and remove it as a child
			Set set = (Set) item;
			EObject model;
			if (set.getSystemOwner() == null) {
				// USER OWNER
				model = set.getMembers().get(0).getConnectionParts().get(0);
			} else {
				// SYSTEM OWNER
				model = set.getSystemOwner();
			}
			EditPart child = (EditPart) getViewer().getEditPartRegistry().get(model);
			removeChild(child);
			
			// in the case of a system owned indexed set: remove the system owner's area when it no 
			// longer exists
			if (set.getSystemOwner() != null) {
				removeObsoleteChildren();
			}
			
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
		
		List<Object> children = new ArrayList<>();
		
		// add the diagram label, if present
		if (getModel().getDiagramData().getLabel() != null) {
			children.add(getModel().getDiagramData().getLabel());
		}
		
		// add the areas in alphabetical order
		List<SchemaArea> areas = 
			new ArrayList<>(getModel().getAreas()); // we can't sort the model's list directly
		Collections.sort(areas);
		children.addAll(areas);
		
		// add the records in alphabetical order
		List<SchemaRecord> records = 
			new ArrayList<>(getModel().getRecords()); // we can't sort the model's list directly
		Collections.sort(records);
		children.addAll(records);
		
		// add the first connection part of the first set member or the system owner of the sets in 
		// alphabetical order
		List<Set> sets = new ArrayList<>(getModel().getSets());
		Collections.sort(sets);
		for (Set set : sets) {
			if (set.getSystemOwner() == null) {
				// user owned set (chained or indexed): first connection part of first member
				children.add(set.getMembers().get(0).getConnectionParts().get(0));
			} else {
				// system owned indexed set: system owner
				children.add(set.getSystemOwner());
			}
		}
		
		return children;
	}

	@Override
	protected INodeTextProvider<Schema> getNodeTextProvider() {
		return getModel();
	}

	private void removeObsoleteChildren() {
		
		// remove child edit parts that refer to areas that no longer exist in the schema
		List<EditPart> toRemove = new ArrayList<>();
		for (Object child : getChildren()) {
			Object model = ((EditPart) child).getModel();
			if (model instanceof SchemaArea) {
				SchemaArea area = (SchemaArea) model;
				if (area.getSchema() == null) {
					toRemove.add((EditPart) child);
				}
			}
		}
		for (EditPart editPart : toRemove) {
			removeChild(editPart);
		}
		
	}
	
}
