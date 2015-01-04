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
package org.lh.dmlj.schema.editor.ruler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.CreateGuideCommand;
import org.lh.dmlj.schema.editor.command.DeleteGuideCommand;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.MoveGuideCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;
import org.lh.dmlj.schema.editor.preference.Unit;

public class SchemaEditorRulerProvider 
	extends RulerProvider implements IModelChangeListener, IPropertyChangeListener {
	
	private GraphicalViewer 	 graphicalViewer;
	private IModelChangeProvider modelChangeProvider;
	private Ruler 		  		 ruler;	
	
	public SchemaEditorRulerProvider(Ruler ruler, SchemaEditor schemaEditor) {
		super();
		
		this.ruler = ruler;
		this.graphicalViewer = (GraphicalViewer) schemaEditor.getAdapter(GraphicalViewer.class);
		
		// hookup to the model change provider
		modelChangeProvider = 
			(IModelChangeProvider) schemaEditor.getAdapter(IModelChangeProvider.class);
		modelChangeProvider.addModelChangeListener(this);		
		
		// make sure we can track changes in the preferred units 
		Plugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);		
		
	}
	
	@Override
	public void afterAddItem(EObject owner, EReference reference, Object item) {
	}

	@Override
	public void afterMoveItem(EObject oldOwner, EReference reference, Object item, 
							  EObject newOwner) {				
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_GUIDE &&
			context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
			context.appliesTo(ruler)) {			
			
			// a guide was added to our ruler (execute/redo); the new guide will always be appended 
			// to the ruler's list of guides, so get it from there and notify the listeners
			Guide guide = ruler.getGuides().get(ruler.getGuides().size() - 1);			
			notifyGuideReparented(guide);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_GUIDE &&
				   context.getCommandExecutionMode() == CommandExecutionMode.UNDO &&
				   context.appliesTo(ruler)) {
			
			// an 'add guide' model change that applies to our ruler was undone; get the removed 
			// guide from the context's listener data and notify the listeners
			Guide guide = (Guide) context.getListenerData();
			notifyGuideReparented(guide);			
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_GUIDE &&
				   context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
				   context.getListenerData() instanceof Guide) {
			
			// a guide was removed from our ruler (execute/redo); we've put the guide in the 
			// context's listener data on the before model change callback, so get the guide from
			// there and notify the listeners
			Guide guide = (Guide) context.getListenerData();
			notifyGuideReparented(guide);			
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_GUIDE &&
				   context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
			
			// a 'delete guide' model change applying to our ruler was undone; if the guide belongs
			// to our ruler, go notify the listeners
			Guide guide = findGuide(context); // null if guide belongs to other ruler			
			notifyGuideReparented(guide);
		} else if (context.getModelChangeType() == ModelChangeType.MOVE_GUIDE) {		
			// a guide was moved; if the guide belongs to our ruler, go notify the listeners
			Guide guide = findGuide(context); // null if guide belongs to other ruler
			notifyGuideMoved(guide);
		}
	}

	@Override
	public void afterRemoveItem(EObject owner, EReference reference, Object item) {
	}

	@Override
	public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_GUIDE &&
			context.getCommandExecutionMode() == CommandExecutionMode.UNDO &&
			context.appliesTo(ruler)) {
			
			// an 'add guide' model change applying to our ruler is to be undone; new guides are 
			// always appended to the ruler's list of guides, so keep a reference to the ruler's 
			// last guide in the context's listener data
			Guide guide = ruler.getGuides().get(ruler.getGuides().size() - 1);
			context.setListenerData(guide);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_GUIDE &&
				   context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
				
			// a guide is being deleted (execute/redo); find the guide and (only) if it belongs to 
			// our ruler, store it in the listener data
			Guide guide = findGuide(context);
			context.setListenerData(guide); // null when guide belongs to the other ruler
		}
	}

	/**
	 * This method should be called when the ruler provider is no longer needed (e.g. when the 
	 * editor is closed).
	 */
	public void dispose() {
		
		// remove us as a listener from the model change provider
		modelChangeProvider.removeModelChangeListener(this);		
		
		// remove the preference store listener
		Plugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
		
	}
	
	private Guide findGuide(ModelChangeContext context) {		
		for (Guide guide : ruler.getGuides()) {
			if (context.appliesTo(guide)) {
				return guide;
			}
		}
		return null;
	}

	@Override
	public Command getCreateGuideCommand(int position) {
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.ADD_GUIDE);
		context.putContextData(ruler);
		IModelChangeCommand command = new CreateGuideCommand(ruler, position);
		command.setContext(context);
		return (Command) command;
	}
	
	@Override
	public Command getDeleteGuideCommand(Object guide) {
		if (!(guide instanceof Guide)) {
			return null;
		}
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.DELETE_GUIDE);
		context.putContextData((Guide) guide);
		IModelChangeCommand command = new DeleteGuideCommand(ruler, (Guide) guide);
		command.setContext(context);
		return (Command) command;		
	}
	
	@Override
	public int getGuidePosition(Object guide) {
		if (guide instanceof Guide) {
			return ((Guide) guide).getPosition();
		}
		return super.getGuidePosition(guide);
	}

	@Override
	public int[] getGuidePositions() {
		int[] guidePositions = new int[ruler.getGuides().size()];
		for (int i = 0; i < guidePositions.length; i++) {
			guidePositions[i] = ruler.getGuides().get(i).getPosition();
		}
		return guidePositions;
	}
	
	@Override
	public List<?> getGuides() {
		return new ArrayList<Guide>(ruler.getGuides());
	}
	
	@Override
	public Command getMoveGuideCommand(Object guide, int positionDelta) {
		if (guide instanceof Guide) {
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.MOVE_GUIDE);
			context.putContextData((Guide) guide);
			IModelChangeCommand command = new MoveGuideCommand((Guide) guide, positionDelta);
			command.setContext(context);
			return (Command) command;
		}
		return null;
	}
	
	@Override
	public Object getRuler() {
		return ruler;
	}
	
	@Override
	public int getUnit() {
		IPreferenceStore store = Plugin.getDefault().getPreferenceStore();
		Unit unit = Unit.valueOf(store.getString(PreferenceConstants.UNITS));		
		if (unit == Unit.CENTIMETERS) {
			return UNIT_CENTIMETERS;
		} else if (unit == Unit.INCHES) {
			return UNIT_INCHES;
		} else {
			return UNIT_PIXELS;
		}		
	}
	
	@SuppressWarnings("unchecked")
	private void notifyGuideMoved(Guide guide) {
		if (guide == null) {
			return;
		}
		// if we directly traverse the list of listeners when adding a guide, we get a 
		// java.util.ConcurrentModificationException, so traverse a copy of the list of listeners 
		// and notify each of them of the new or obsolete guide
		for (Object listener : new ArrayList<>(listeners)) {			
			RulerChangeListener rulerChangeListener = (RulerChangeListener) listener;
			rulerChangeListener.notifyGuideMoved(guide);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void notifyGuideReparented(Guide guide) {
		if (guide == null) {
			return;
		}
		// if we directly traverse the list of listeners when adding a guide, we get a 
		// java.util.ConcurrentModificationException, so traverse a copy of the list of listeners 
		// and notify each of them of the new or obsolete guide
		for (Object listener : new ArrayList<>(listeners)) {			
			RulerChangeListener rulerChangeListener = (RulerChangeListener) listener;
			rulerChangeListener.notifyGuideReparented(guide);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// this method is called when the units are changed in the preferences
		if (event.getProperty().equals(PreferenceConstants.UNITS)) {						
			if (ruler.getDiagramData().isShowRulers()) {
				// repaint the ruler:
				graphicalViewer.setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY,
										    Boolean.valueOf(false)); // hide it...
				graphicalViewer.setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY,
					    					Boolean.valueOf(true));  // and show it again
			}
		}
	}
	
}
