package org.lh.dmlj.schema.editor.ruler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Unit;
import org.lh.dmlj.schema.editor.command.CreateGuideCommand;
import org.lh.dmlj.schema.editor.command.DeleteGuideCommand;
import org.lh.dmlj.schema.editor.command.MoveGuideCommand;

public class SchemaEditorRulerProvider extends RulerProvider {
	
	private GuideListener listener;
	private Ruler 		  ruler;	
	
	public SchemaEditorRulerProvider(Ruler ruler) {
		super();
		
		this.ruler = ruler;
		
		// we need a listener so that we can notify our own listeners when a
		// guide is added, removed or moved - this is necessary to show/hide the
		// guide at the right position in the ruler
		listener = new GuideListener(this.listeners);
		
		// attach the guide listener to the ruler...
		ruler.eAdapters().add(listener);
		
		// ...and to each existing guide
		for (Guide guide : ruler.getGuides()) {
			guide.eAdapters().add(listener);
		}		
	}
	
	/**
	 * This method should be called when the ruler provider is no longer needed
	 * (e.g. when the editor is closed).
	 */
	public void dispose() {
		
				
		// attach the guide listener from each existing guide...
		for (Guide guide : ruler.getGuides()) {
			guide.eAdapters().remove(listener);
		}
		
		// and from the the ruler...
		ruler.eAdapters().remove(listener);
	}
	
	@Override
	public Command getCreateGuideCommand(int position) {
		return new CreateGuideCommand(ruler, position);
	}
	
	@Override
	public Command getDeleteGuideCommand(Object guide) {
		if (guide instanceof Guide) {
			return new DeleteGuideCommand(ruler, (Guide) guide);
		}
		return null;
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
			return new MoveGuideCommand((Guide) guide, positionDelta);
		}
		return null;
	}
	
	@Override
	public Object getRuler() {
		return ruler;
	}
	
	@Override
	public int getUnit() {
		Unit unit = ruler.getDiagramData().getUnit();
		if (unit == Unit.CENTIMETERS) {
			return UNIT_CENTIMETERS;
		} else if (unit == Unit.INCHES) {
			return UNIT_INCHES;
		} else {
			return UNIT_PIXELS;
		}		
	}
	
	class GuideListener implements Adapter {

		private List<?> listeners;
		
		GuideListener(List<?> listeners) {
			super();
			this.listeners = listeners;
		}
		
		@Override
		public Notifier getTarget() {			
			return null;
		}

		@Override
		public boolean isAdapterForType(Object type) {			
			return false;
		}

		@Override
		public void notifyChanged(Notification notification) {			
			
			int featureID = notification.getFeatureID(Ruler.class);
			
			if (featureID == SchemaPackage.RULER__GUIDES &&
				(notification.getEventType() == Notification.ADD ||
				 notification.getEventType() == Notification.REMOVE)) {
				
				// guide added or removed; get the guide
				final Guide guide;
				if (notification.getEventType() == Notification.ADD) {
					// guide added; add this listener					
					guide = (Guide) notification.getNewValue();					
					guide.eAdapters().add(this);																					
				} else {
					// guide removed; remove this listener					
					guide = (Guide) notification.getOldValue();
					guide.eAdapters().remove(this);
				}
				
				// if we directly traverse the list of listeners when adding a
				// guide, we get a java.util.ConcurrentModificationException, so 
				// traverse a copy of the list of listeners and notify each of
				// them of the new or obsolete guide
				for (Object listener : new ArrayList<>(listeners)) {			
					RulerChangeListener rulerChangeListener = 
						(RulerChangeListener) listener;
					rulerChangeListener.notifyGuideReparented(guide);
				}
				
			} else if (featureID == SchemaPackage.GUIDE__POSITION &&
					   notification.getEventType() == Notification.SET) {
				
				// guide moved; get the guide
				Guide guide = (Guide) notification.getNotifier();
				
				// traverse the ruler provider listeners and notify them of the
				// moved guide
				for (Object listener : listeners) {
					RulerChangeListener rulerChangeListener = 
						(RulerChangeListener) listener;
					rulerChangeListener.notifyGuideMoved(guide);
				}
			}
			
		}

		@Override
		public void setTarget(Notifier newTarget) {			
		}
		
	}	
	
}