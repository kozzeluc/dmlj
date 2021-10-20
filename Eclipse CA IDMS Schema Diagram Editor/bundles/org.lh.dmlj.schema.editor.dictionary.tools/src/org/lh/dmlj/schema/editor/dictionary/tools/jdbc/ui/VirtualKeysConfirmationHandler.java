/**
 * Copyright (C) 2021  Luc Hermans
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
package org.lh.dmlj.schema.editor.dictionary.tools.jdbc.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.DictionarySession;
import org.lh.dmlj.schema.editor.dictionary.tools.preference.PreferenceConstants;

public abstract class VirtualKeysConfirmationHandler {
	private static final int NO = 1;
	private static final int ALWAYS = 2;

	public static void handleConfirmation(DictionarySession session, Runnable acceptProcessor, Runnable rejectProcessor) {
		boolean confirmationRequiredWhenSchemaWithVirtualKeys = 
			Plugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CONFIRMATION_REQUIRED_WHEN_SCHEMA_DEFINED_WITH_VIRTUAL_KEYS);
		if (confirmationRequiredWhenSchemaWithVirtualKeys && session.isSchemaDefinedWithVirtualKeys()) {
			String question = "Schema " + session.getDictionary().getSchema() + " is defined WITH VIRTUAL KEYS, OK to proceed ?";
			MessageDialog dialog = 
				new MessageDialog(null, "Warning", null, question, MessageDialog.WARNING, new String[] { "Yes", "No", "Always" }, NO);
			int answer = dialog.open();
			if (answer == NO) {
				rejectProcessor.run();
				return;
			} else if (answer == ALWAYS) {
				Plugin.getDefault().getPreferenceStore().setValue(PreferenceConstants.CONFIRMATION_REQUIRED_WHEN_SCHEMA_DEFINED_WITH_VIRTUAL_KEYS, false);
			}
		}
		acceptProcessor.run();
	}
	
}
