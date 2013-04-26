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
		
		// printing preferences; margins are stored in pels (logical pixels; 72 pels == 1 inch)...
		store.setDefault(PreferenceConstants.TOP_MARGIN, 0);
		store.setDefault(PreferenceConstants.BOTTOM_MARGIN, 0);	
		store.setDefault(PreferenceConstants.LEFT_MARGIN, 0);	
		store.setDefault(PreferenceConstants.RIGHT_MARGIN, 0);	
		
	}

}