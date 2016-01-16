/**
 * Copyright (C) 2016  Luc Hermans
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
		
		// export preferences
		store.setDefault(PreferenceConstants.SORT_SCHEMA_ENTITIES_ON_EXPORT_TO_SYNTAX, false);
		
		// printing preferences; margins are stored in pels (logical pixels; 72 pels == 1 inch)...
		store.setDefault(PreferenceConstants.TOP_MARGIN, 0);
		store.setDefault(PreferenceConstants.BOTTOM_MARGIN, 0);	
		store.setDefault(PreferenceConstants.LEFT_MARGIN, 0);	
		store.setDefault(PreferenceConstants.RIGHT_MARGIN, 0);
		
		// default diagram label preferences
		store.setDefault(PreferenceConstants.DIAGRAMLABEL_ORGANISATION, 
						 "Eclipse CA IDMS™/DB Schema Diagram Editor");
		store.setDefault(PreferenceConstants.DIAGRAMLABEL_SHOW_LAST_MODIFIED, true);
		store.setDefault(PreferenceConstants.DIAGRAMLABEL_LAST_MODIFIED_DATE_FORMAT_PATTERN, 
						 "dd-MM-yyyy HH:mm:ss");
		
		// default diagram data for new and imported schemas
		store.setDefault(PreferenceConstants.SHOW_RULERS, true);
		store.setDefault(PreferenceConstants.SHOW_GRID, false);
		store.setDefault(PreferenceConstants.SNAP_TO_GUIDES, true);
		store.setDefault(PreferenceConstants.SNAP_TO_GRID, true);
		store.setDefault(PreferenceConstants.SNAP_TO_GEOMETRY, true);	
		
		// operating system text size
		store.setDefault(PreferenceConstants.OPERATING_SYSTEM_TEXT_SIZE, 100);		
	}

}
