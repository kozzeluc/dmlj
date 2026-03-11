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
package org.lh.dmlj.schema.editor.ruler;

import java.util.List;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.CreateGuideCommand;
import org.lh.dmlj.schema.editor.command.DeleteGuideCommand;
import org.lh.dmlj.schema.editor.command.MoveGuideCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;
import org.lh.dmlj.schema.editor.preference.Unit;

public class SchemaEditorRulerProvider extends RulerProvider implements IModelChangeListener, IPropertyChangeListener {
	private final Ruler ruler;
	private final GraphicalViewer graphicalViewer;
	private final IModelChangeProvider modelChangeProvider; // null means we're in read-only mode
	
	public SchemaEditorRulerProvider(Ruler ruler, SchemaEditor schemaEditor) {
		this.ruler = ruler;
		this.graphicalViewer = (GraphicalViewer) schemaEditor.getAdapter(GraphicalViewer.class);
		if (!schemaEditor.isReadOnlyMode()) {
			modelChangeProvider = (IModelChangeProvider) schemaEditor.getAdapter(IModelChangeProvider.class);
			modelChangeProvider.addModelChangeListener(this);		
		} else {
			modelChangeProvider = null;
		}
		// make sure we can track changes in the preferred units 
		Plugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);		
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_GUIDE &&
			context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
			context.appliesTo(ruler)) {			
			
			// a guide was added to our ruler (execute/redo); the new guide will always be appended to the
			// ruler's list of guides, so get it from there and notify the listeners
			var guide = ruler.getGuides().get(ruler.getGuides().size() - 1);			
			notifyGuideReparented(guide);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_GUIDE &&
				   context.getCommandExecutionMode() == CommandExecutionMode.UNDO &&
				   context.appliesTo(ruler)) {
			
			// an 'add guide' model change that applies to our ruler was undone; get the removed guide from the
			// context's listener data and notify the listeners
			var guide = (Guide) context.getListenerData();
			notifyGuideReparented(guide);			
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_GUIDE &&
				   context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
				   context.getListenerData() instanceof Guide guide) {
			
			// a guide was removed from our ruler (execute/redo); we've put the guide in the context's listener
			// data on the before model change callback, so get the guide from there and notify the listeners
			notifyGuideReparented(guide);			
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_GUIDE &&
				   context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
			
			// a 'delete guide' model change applying to our ruler was undone; if the guide belongs to our ruler,
			// go notify the listeners
			var guide = findGuide(context); // null if guide belongs to other ruler			
			notifyGuideReparented(guide);
		} else if (context.getModelChangeType() == ModelChangeType.MOVE_GUIDE) {		
			// a guide was moved; if the guide belongs to our ruler, go notify the listeners
			var guide = findGuide(context); // null if guide belongs to other ruler
			notifyGuideMoved(guide);
		}
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_GUIDE &&
			context.getCommandExecutionMode() == CommandExecutionMode.UNDO &&
			context.appliesTo(ruler)) {
			
			// an 'add guide' model change applying to our ruler is to be undone; new guides are  always appended
			// to the ruler's list of guides, so keep a reference to the ruler's last guide in the context's
			// listener data
			var guide = ruler.getGuides().get(ruler.getGuides().size() - 1);
			context.setListenerData(guide);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_GUIDE &&
				   context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
				
			// a guide is being deleted (execute/redo); find the guide and (only) if it belongs to our ruler,
			// store it in the listener data
			var guide = findGuide(context);
			context.setListenerData(guide); // null when guide belongs to the other ruler
		}
	}
	
	public void dispose() {
		if (modelChangeProvider != null) {
			modelChangeProvider.removeModelChangeListener(this);
		}
		Plugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
	}
	
	private Guide findGuide(ModelChangeContext context) {		
		return ruler.getGuides().stream()
				.filter(context::appliesTo)
				.findFirst()
				.orElse(null);
	}

	@Override
	public Command getCreateGuideCommand(int position) {
		if (isReadOnlyMode()) {
			return null;
		} else {
			var context = new ModelChangeContext(ModelChangeType.ADD_GUIDE);
			context.putContextData(ruler, ModelChangeContext.rulerContextDataAssembler);
			var command = new CreateGuideCommand(ruler, position);
			command.setContext(context);
			return command;
		}
	}
	
	@Override
	public Command getDeleteGuideCommand(Object guide) {
		if (isReadOnlyMode() || !(guide instanceof Guide)) {
			return null;
		} else {
			var context = new ModelChangeContext(ModelChangeType.DELETE_GUIDE);
			context.putContextData((Guide) guide, ModelChangeContext.guideContextDataAssembler);
			var command = new DeleteGuideCommand(ruler, (Guide) guide);
			command.setContext(context);
			return command;
		}
	}
	
	@Override
	public int getGuidePosition(Object object) {
		if (object instanceof Guide guide) {
			return guide.getPosition();
		} else {
			return super.getGuidePosition(object);
		}
	}

	@Override
	public int[] getGuidePositions() {
		var guidePositions = new int[ruler.getGuides().size()];
		for (var i = 0; i < guidePositions.length; i++) {
			guidePositions[i] = ruler.getGuides().get(i).getPosition();
		}
		return guidePositions;
	}
	
	@Override
	public List<?> getGuides() {
		return List.copyOf(ruler.getGuides());
	}
	
	@Override
	public Command getMoveGuideCommand(Object object, int positionDelta) {
		if (!isReadOnlyMode() && object instanceof Guide guide) {
			var context = new ModelChangeContext(ModelChangeType.MOVE_GUIDE);
			context.putContextData(guide, ModelChangeContext.guideContextDataAssembler);
			var command = new MoveGuideCommand(guide, positionDelta);
			command.setContext(context);
			return command;
		} else {
			return null;
		}
	}
	
	@Override
	public Object getRuler() {
		return ruler;
	}
	
	@Override
	public int getUnit() {
		return switch (Unit.valueOf(Plugin.getDefault().getPreferenceStore().getString(PreferenceConstants.UNITS))) {
			case CENTIMETERS -> UNIT_CENTIMETERS;
			case INCHES -> UNIT_INCHES;
			default -> UNIT_PIXELS;
		};
	}
	
	private boolean isReadOnlyMode() {
		return modelChangeProvider == null;
	}
		
	private void notifyGuideMoved(Guide guide) {
		if (guide != null) {
			// if we directly traverse the list of listeners when adding a guide, we get a ConcurrentModificationException,
			// so traverse a copy of the list of listeners and notify each of them of the new or obsolete guide
			List.copyOf(listeners).stream()
					.forEach(l -> RulerChangeListener.class.cast(l).notifyGuideMoved(guide));
		}
	}
		
	private void notifyGuideReparented(Guide guide) {
		if (guide != null) {
			// if we directly traverse the list of listeners when adding a guide, we get a ConcurrentModificationException,
			// so traverse a copy of the list of listeners and notify each of them of the new or obsolete guide
			List.copyOf(listeners).stream()
					.forEach(l -> RulerChangeListener.class.cast(l).notifyGuideReparented(guide));
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// this method is called when the units are changed in the preferences
		if (event.getProperty().equals(PreferenceConstants.UNITS) && ruler.getDiagramData().isShowRulers()) {
			// repaint the ruler:
			graphicalViewer.setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, Boolean.valueOf(false)); // hide it...
			graphicalViewer.setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, Boolean.valueOf(true));  // and show it again
		}
	}
	
}
