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

import java.util.List;
import java.util.stream.IntStream;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.INodeTextProvider;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.common.Tools;

public abstract class AbstractSchemaTreeEditPart<T extends EObject> extends AbstractTreeEditPart implements IModelChangeListener {
	protected IModelChangeProvider modelChangeProvider;
	
	private static final int compareSimilarChildren(INodeTextProvider<?> newTextProvider, INodeTextProvider<?> existingTextProvider) {
		if (newTextProvider instanceof SchemaArea newSchemaArea && existingTextProvider instanceof SchemaArea existingSchemaArea) {
			// because we use mocks for unit testing, we have to compare the area names ourselves
			var newAreaName = newSchemaArea.getName();
			var existingAreaName = existingSchemaArea.getName();
			return newAreaName.toUpperCase().compareTo(existingAreaName.toUpperCase());
		} else if (newTextProvider instanceof SchemaRecord newSchemaRecord && existingTextProvider instanceof SchemaRecord existingSchemaRecord) {
			// because we use mocks for unit testing, we have to compare the record names ourselves
			var newRecordName = newSchemaRecord.getName();
			var existingRecordName = existingSchemaRecord.getName();
			return newRecordName.toUpperCase().compareTo(existingRecordName.toUpperCase());
		} else if (newTextProvider instanceof Set newSet && existingTextProvider instanceof Set existingSet) {
			// because we use mocks for unit testing, we have to compare the set names ourselves
			var newSetName = newSet.getName();
			var existingSetName = existingSet.getName();
			return newSetName.toUpperCase().compareTo(existingSetName.toUpperCase());
		} 
		throw new IllegalArgumentException("unexpected combination: " + newTextProvider.getClass().getSimpleName() + " " +
				existingTextProvider.getClass().getSimpleName());
	}
	
	protected static final int getInsertionIndex(List<?> children, INodeTextProvider<?> newChildNodeTextProvider,
			Class<?>[] childNodeTextProviderOrder) {
		
		// calculate the childOrder index for the new child's node text provider: this index points to the entry
		// in the childOrder array containing the interface for the newChild's node text provider; if we don't
		// find a match, return the size of the children list because the child is to be appended to the list of
		// children
		var childOrderIndex = calculateChildOrderIndex(newChildNodeTextProvider, childNodeTextProviderOrder);
		
		// calculate the index of the first child whose model class implements the interface found; we might not
		// be able to find such a child
		var firstModelCandidate = firstIndexOf(children, childNodeTextProviderOrder[childOrderIndex]);
		if (firstModelCandidate > -1) {
			return getInsertionIndexWithModelCandidatesAvailable(children, newChildNodeTextProvider,
					childNodeTextProviderOrder, childOrderIndex, firstModelCandidate);
		} else {
			return getInsertionIndexWithModelCandidatesMissing(children, childNodeTextProviderOrder, childOrderIndex);
		}
	}
	
	private static int calculateChildOrderIndex(INodeTextProvider<?> newChildNodeTextProvider, Class<?>[] childNodeTextProviderOrder) {
		return IntStream.range(0, childNodeTextProviderOrder.length)
				.filter(i -> childNodeTextProviderOrder[i].isInstance(newChildNodeTextProvider))
				.findFirst()
				.orElse(-1);	
	}
	
	private static int getInsertionIndexWithModelCandidatesAvailable(List<?> children, INodeTextProvider<?> newChildNodeTextProvider,
			Class<?>[] childNodeTextProviderOrder, int childOrderIndex, int firstModelCandidate) {
		
		// i contains the index of the first child whose node text provider class implements the INodeTextProvider
		// interface
		var j = children.size();
		if (childOrderIndex < (childNodeTextProviderOrder.length - 1)) {
			// there is at least 1 more child node text provider interface that we can check
			do {
				childOrderIndex += 1;
				var k = firstIndexOf(children, childNodeTextProviderOrder[childOrderIndex]);
				if (k > -1) {
					// bingo
					j = k;
					break;
				}
			} while (childOrderIndex < (childNodeTextProviderOrder.length - 1));
		}
		j -= 1; // j now points to the last (similar) child we need to check
		for (var k = firstModelCandidate; k <= j; k++) {
			var model = ((EditPart) children.get(k)).getModel();
			var noeTextProvider = toNodeTextProvider(model);
			if (noeTextProvider != null && compareSimilarChildren(newChildNodeTextProvider, noeTextProvider) < 0) {
				return k;
			}				
		}
		// if we get here, we need to insert the new child after the last similar child
		return j + 1;
	}
	
	private static int getInsertionIndexWithModelCandidatesMissing(List<?> children, Class<?>[] childNodeTextProviderOrder, int childOrderIndex) {
		// there currently is no child whose node text provider class implements the interface in the child
		// order array; if there is a child whose node text provider implements that child's interface in 1
		// of the next child order entries, the insertion index appended to corresponds to index; if not,
		// return the size of the children list, the child will be the list of children
		if (childOrderIndex < (childNodeTextProviderOrder.length - 1)) {
			// there is at least 1 more child order node text provider interface that we can check
			do {
				childOrderIndex += 1;
				var i = firstIndexOf(children, childNodeTextProviderOrder[childOrderIndex]);
				if (i > -1) {
					// bingo
					return i;
				}
			} while (childOrderIndex < (childNodeTextProviderOrder.length - 1));				
		}
		return children.size();	
	}
	
	private static final int firstIndexOf(List<?> children, Class<?> targetInterface) {		
		for (var i = 0; i < children.size(); i++) {
			var editPart = (EditPart) children.get(i); 
			if (targetInterface.isInstance(toNodeTextProvider(editPart.getModel()))) {
				return i;
			}							
		}
		return -1;
	}

	/**
	 * Checks whether the feature of interest is set in a grouped model change.
	 * @param setFeatures the features set in the model change
	 * @param featureOfInterest the feature of interest
	 * @return true if the feature of interest is contained in the list of set features, false if not
	 */
	protected static final boolean isFeatureSet(EStructuralFeature[] setFeatures, EStructuralFeature featureOfInterest) {
		for (var feature : setFeatures) {
			if (feature == featureOfInterest) {
				return true;
			}
		}
		return false;
	}

	private static final INodeTextProvider<?> toNodeTextProvider(Object model) {
		if (model instanceof DiagramLabel) {
			return null;
		} else if (model instanceof SchemaArea) {
			return (INodeTextProvider<?>) model;
		} else if (model instanceof SchemaRecord) {
			return (INodeTextProvider<?>) model;
		} else if (model instanceof Set) {
			return (INodeTextProvider<?>) model;
		} else if (model instanceof SystemOwner systemOwner) {			
			return systemOwner.getSet();
		} else if (model instanceof VsamIndex vsamIndex) {			
			return vsamIndex.getSet();
		}			
		throw new IllegalArgumentException("unexpected: " + model.getClass().getSimpleName());
	}

	protected AbstractSchemaTreeEditPart(T model, IModelChangeProvider modelChangeProvider) {
		super(model);	
		this.modelChangeProvider = modelChangeProvider; // null means we're in read-only mode
	}
	
	@Override
	public final void activate() {	
		if (!isReadOnlyMode()) {
			modelChangeProvider.addModelChangeListener(this);
		}
		super.activate();
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {		
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {		
	}

	protected EditPart childFor(Object model) {
		for (var child : getChildren()) {
			var cChild = (EditPart) child;
			if (cChild.getModel() == model) {
				return cChild;
			}
		}
		throw new IllegalArgumentException("no child edit part for model: " + model);
	}
	
	protected void createAndAddChild(INodeTextProvider<?> model) {
		var child = SchemaTreeEditPartFactory.createEditPart(model, modelChangeProvider);					
		var index = getInsertionIndex(getChildren(), model, getChildNodeTextProviderOrder());					
		addChild(child, index);
	}
	
	protected void createAndAddChild(EObject model, INodeTextProvider<?> nodeTextProvider) {
		var child = SchemaTreeEditPartFactory.createEditPart(model, modelChangeProvider);					
		var index = getInsertionIndex(getChildren(), nodeTextProvider, getChildNodeTextProviderOrder());					
		addChild(child, index);
	}

	@Override
	public final void deactivate() {
		if (!isReadOnlyMode()) {
			modelChangeProvider.removeModelChangeListener(this);
		}
		super.deactivate();
	}
	
	protected void findAndRemoveChild(EObject model, boolean useEditPartRegistry) {
		Assert.isNotNull(model, "model is null");
		EditPart child;
		if (useEditPartRegistry) {
			child = (EditPart) getViewer().getEditPartRegistry().get(model);
			Assert.isNotNull(model, "no child edit part for model: " + model);
		} else {
			child = childFor(model);
		}
		removeChild(child);
	}
	
	/**
	 * Provides the order in which the edit part's children, if any, should be kept. Each subclass that refers to
	 * an edit part that contains children should override this method.
	 * @return the edit part's child order; within each type, the node text providers will be compared using the
	 *         standard Java compareTo(...) method (upper case compare)
	 */
	protected Class<?>[] getChildNodeTextProviderOrder() {
		throw new IllegalArgumentException("no override in subclass: " + getClass().getName());	
	}
	
	@Override
	protected final Image getImage() {
		var imagePath = getImagePath();
		if (imagePath == null) {
			return null;
		} else {
			return Plugin.getDefault().getImage(imagePath);
		}
	}
	
	protected abstract String getImagePath();
	
	@Override
	@SuppressWarnings("unchecked")
	public T getModel() {
		return (T) super.getModel();
	}	

	/**
	 * Returns the (wrapped) model's node text provider; this is the object that holds the node text and determines
	 * part of the child order for the parent edit part. Whenever the node text changes, method nodeTextChanged()
	 * should be invoked.
	 * @return the (wrapped) object that provides the node text
	 */
	protected abstract WrappedNodeTextProvider getNodeTextProvider();

	protected final EObject getParentModelObject() {
		if (getParent() instanceof AbstractSchemaTreeEditPart<?> parentEditPart) {
			return parentEditPart.getModel();
		} else {
			return null;
		}
	}
	
	@Override
	protected final String getText() {
		return Tools.removeTrailingUnderscore(getNodeTextProvider().value().getNodeText());
	}
	
	protected boolean hasChildFor(Object model) {
		try {
			childFor(model);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}		
	}
	
	protected boolean isReadOnlyMode() {
		return modelChangeProvider == null;
	}
	
	/**
	 * Causes the edit part's position in the parent's children list to be recalculated and the edit part will be
	 * moved to that new position. This method hould be called whenever the edit part's node text changes. 
	 */
	protected void nodeTextChanged() {
		// make sure the parent can maintain its order...
		if (!(getParent() instanceof AbstractSchemaTreeEditPart<?>)) {
			return;
		}
		var siblings = getParent().getChildren();
		siblings.remove(this);
		var parent = (AbstractSchemaTreeEditPart<?>) getParent(); 
		int newIndex = getInsertionIndex(siblings, getNodeTextProvider().value(), parent.getChildNodeTextProviderOrder());
		parent.reorderChild(this, newIndex);
	}
	
	protected static record WrappedNodeTextProvider(INodeTextProvider<?> value) {
	}
	
}
