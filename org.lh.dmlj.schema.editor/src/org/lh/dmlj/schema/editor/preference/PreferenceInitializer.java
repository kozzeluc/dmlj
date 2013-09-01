package org.lh.dmlj.schema.editor.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.lh.dmlj.schema.editor.Plugin;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public PreferenceInitializer() {
		super();
	}

	@Override
	public void initializeDefaultPreferences() {		
		IPreferenceStore store = Plugin.getDefault().getPreferenceStore();
		
		// general preferences...
		store.setDefault(PreferenceConstants.UNITS, Unit.CENTIMETERS.toString());
		store.setDefault(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES, false);
		
		// import preferences...
		store.setDefault(PreferenceConstants.COMPRESSION_PROCEDURES, "IDMSCOMP");
		
		// printing preferences; margins are stored in pels (logical pixels; 72 pels == 1 inch)...
		store.setDefault(PreferenceConstants.TOP_MARGIN, 0);
		store.setDefault(PreferenceConstants.BOTTOM_MARGIN, 0);	
		store.setDefault(PreferenceConstants.LEFT_MARGIN, 0);	
		store.setDefault(PreferenceConstants.RIGHT_MARGIN, 0);
		
		// default diagram label preferences
		store.setDefault(PreferenceConstants.ORGANISATION, 
						 "Eclipse CA IDMS™/DB Schema Diagram Editor");
		
		// default diagram data for new and imported schemas
		store.setDefault(PreferenceConstants.SHOW_RULERS, true);
		store.setDefault(PreferenceConstants.SHOW_GRID, false);
		store.setDefault(PreferenceConstants.SNAP_TO_GUIDES, true);
		store.setDefault(PreferenceConstants.SNAP_TO_GRID, true);
		store.setDefault(PreferenceConstants.SNAP_TO_GEOMETRY, true);		
		
	}

}