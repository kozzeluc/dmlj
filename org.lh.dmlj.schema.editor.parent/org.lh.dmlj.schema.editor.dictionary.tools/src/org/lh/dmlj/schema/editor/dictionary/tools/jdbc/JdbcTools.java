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
package org.lh.dmlj.schema.editor.dictionary.tools.jdbc;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;

public abstract class JdbcTools {
	
	public static long getDbkey(ResultSet resultSet, String rowIdColumnLabel) throws SQLException {
		byte[] rid = resultSet.getBytes(rowIdColumnLabel);		
		return ByteBuffer.wrap(new byte[] {0, 0, 0, 0, rid[0], rid[1], rid[2], rid[3]}).getLong();
	}
	
	public static String removeTrailingSpaces(String aString) {
		StringBuilder p = new StringBuilder(aString);
		while (p.length() > 0 && p.charAt(p.length() - 1) == ' ') {
			p.setLength(p.length() - 1);
		}
		return p.toString();
	}

	public static void testConnection(Dictionary dictionary) {			
		String title = "Test Connection";
		ImportSession session = null;
		try {
			session = new ImportSession(dictionary);
			session.open();
			String context = "Test connection; dictionary='" + dictionary.getId() + "'";
			Query query = 
				new Query.Builder().forValidSchemaList(session).withContext(context).build();
			session.runQuery(query, new IRowProcessor() {
				@Override
				public void processRow(ResultSet row) throws SQLException {					
				}				
			});
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), title, 
										  "Connection was successful for dictionary " +
										  dictionary.getId() + ".");
		} catch (Throwable t) {
			StringBuilder fullMessage = 
				new StringBuilder("Connection was NOT successful for dictionary ");
			fullMessage.append(dictionary.getId());
			fullMessage.append(":\n\n");
			fullMessage.append(t.getMessage());
			MessageDialog.openError(Display.getCurrent().getActiveShell(), title, 
									fullMessage.toString());
		} finally {
			try {
				session.close();
			} catch (Throwable t) {
			}
		}
	}

	public static String toHexString(long dbkey) {
		StringBuilder p = new StringBuilder(Long.toHexString(dbkey));
		while (p.length() < 8) {
			p.insert(0, '0');
		}
		return p.toString();
	}	
	
}
