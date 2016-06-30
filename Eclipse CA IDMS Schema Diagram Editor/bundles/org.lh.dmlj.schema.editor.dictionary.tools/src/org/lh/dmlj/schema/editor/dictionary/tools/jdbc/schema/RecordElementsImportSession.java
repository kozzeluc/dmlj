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
package org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema;

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.DictionarySession;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;

public class RecordElementsImportSession extends DictionarySession {
	
	private String recordSynonymName;
	private int recordSynonymVersion;

	public RecordElementsImportSession(Dictionary dictionary, String recordSynonymName) {

		super(dictionary, "Import record elements from record synonym " + recordSynonymName + 
			  " version ? from dictionary " + dictionary.getId());
		this.recordSynonymName = recordSynonymName;
		this.recordSynonymVersion = -1;
	}	
	
	public RecordElementsImportSession(Dictionary dictionary, String recordSynonymName, 
									   int recordSynonymVersion) {
		
		super(dictionary, "Import record elements from record synonym " + recordSynonymName + 
			  " version " + recordSynonymVersion + " from dictionary " + dictionary.getId());
		this.recordSynonymName = recordSynonymName;
		this.recordSynonymVersion = recordSynonymVersion;
	}
	
	public String getRecordSynonymName() {
		return recordSynonymName;
	}

	public int getRecordSynonymVersion() {
		return recordSynonymVersion;
	}

}
