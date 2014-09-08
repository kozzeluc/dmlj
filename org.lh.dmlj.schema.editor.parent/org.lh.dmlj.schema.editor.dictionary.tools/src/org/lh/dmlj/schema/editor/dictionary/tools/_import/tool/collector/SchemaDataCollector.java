/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.S_010;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Schemacmt_181;
import org.lh.dmlj.schema.editor.importtool.ISchemaDataCollector;

public class SchemaDataCollector implements ISchemaDataCollector {
	
	private List<String> comments;
	private String schemaDescription;
	private String schemaMemoDate;	

	public SchemaDataCollector(SchemaImportSession session) {
		super();
		collectData(session);		
	}
	
	private void collectData(SchemaImportSession session) {
		// schema comments are maintained in SCHEMACMT-181 record occurrences; 1 such record 
		// contains 1 line of comment, split in 2 part holding 50 bytes each; we'll make sure that 
		// any comment line we return does not exceed 80 characters; 80 is the maximum for any line 
		// of comment
		Query schemaDescriptionAndCommentListQuery = 
			new Query.Builder().forSchemaDescriptionAndCommentList(session).build();
		final boolean[] first = {true};
		session.runQuery(schemaDescriptionAndCommentListQuery, new IRowProcessor() {		
			@Override
			public void processRow(ResultSet row) throws SQLException {
				if (first[0]) {
					String descr_010 = JdbcTools.removeTrailingSpaces(row.getString(S_010.DESCR_010));
					if (!descr_010.equals("")) {
						schemaDescription = descr_010; 
					}
					String sDt_010 = JdbcTools.removeTrailingSpaces(row.getString(S_010.S_DT_010));
					if (!sDt_010.equals("")) {
						schemaMemoDate = sDt_010;
					}
					first[0] = false;
				}
				int cmtId_181 = row.getInt(Schemacmt_181.CMT_ID_181);
				if (cmtId_181 == -1) {
					StringBuilder line = new StringBuilder();
					String cmtInfo_181_01 = row.getString(Schemacmt_181.CMD_INFO_181_01);
					String line1 = JdbcTools.removeTrailingSpaces(cmtInfo_181_01);
					line.append(line1);
					String cmtInfo_181_02 = row.getString(Schemacmt_181.CMD_INFO_181_02);
					String line2 = JdbcTools.removeTrailingSpaces(cmtInfo_181_02); 
					if (!line2.equals("") && line1.length() < 50) {
						// line1 shouldn't have been right trimmed, so restore that part of the line 
						// comment to its former glory:
						while (line.length() < 50) {
							line.append(' ');
						}
					}
					line.append(line2);
					if (line.length() > 80) {
						line.setLength(80);
					}
					comments.add(line.toString());
				}
			}
		});
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
