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
package org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.S010;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Schemacmt181;
import org.lh.dmlj.schema.editor.importtool.ISchemaDataCollector;

public class SchemaDataCollector implements ISchemaDataCollector {
	private List<String> comments = new ArrayList<>();
	private String schemaDescription;
	private String schemaMemoDate;

	public SchemaDataCollector(SchemaImportSession session) {
		collectData(session);		
	}
	
	private void collectData(SchemaImportSession session) {
		// schema comments are maintained in SCHEMACMT-181 record occurrences; 1 such record contains 1 line of
		// comment, split in 2 part holding 50 bytes each; we'll make sure that any comment line we return does
		// not exceed 80 characters; 80 is the maximum for any line of comment
		var schemaDescriptionAndCommentListQuery = new Query.Builder().forSchemaDescriptionAndCommentList(session).build();
		var first = new boolean[] { true };
		session.runQuery(schemaDescriptionAndCommentListQuery, new IRowProcessor() {		
			@Override
			public void processRow(ResultSet row) throws SQLException {
				if (first[0]) {
					setSchemaDescription(row);
					setSchemaMemoDate(row);
					first[0] = false;
				}
				var cmtId181 = row.getInt(Schemacmt181.CMT_ID_181);
				if (cmtId181 == -1) {
					addToComments(row);
				}
			}
		});
	}
	
	private void setSchemaDescription(ResultSet row) {
		try {
			var descr010 = JdbcTools.removeTrailingSpaces(row.getString(S010.DESCR_010));
			if (!descr010.isEmpty()) {
				schemaDescription = descr010; 
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void setSchemaMemoDate(ResultSet row) {
		try {
			var sDt010 = JdbcTools.removeTrailingSpaces(row.getString(S010.S_DT_010));
			if (!sDt010.isEmpty()) {
				schemaMemoDate = sDt010;
			}
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void addToComments(ResultSet row) {
		try {
			var line = new StringBuilder();
			var cmtInfo18101 = row.getString(Schemacmt181.CMT_INFO_181_1);
			var line1 = JdbcTools.removeTrailingSpaces(cmtInfo18101);
			line.append(line1);
			var cmtInfo18102 = row.getString(Schemacmt181.CMT_INFO_181_2);
			var line2 = JdbcTools.removeTrailingSpaces(cmtInfo18102); 
			if (!line2.isEmpty() && line1.length() < 50) {
				// line1 shouldn't have been right trimmed, so restore that part of the line comment to
				// its former glory:
				while (line.length() < 50) {
					line.append(' ');
				}
			}
			line.append(line2);
			if (line.length() > 80) {
				line.setLength(80);
			}
			comments.add(line.toString());
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	@Override
	public List<String> getComments() {
		return comments;
	}

	@Override
	public String getSchemaDescription() {
		return schemaDescription;
	}

	@Override
	public String getSchemaMemoDate() {
		return schemaMemoDate;
	}
	
}
