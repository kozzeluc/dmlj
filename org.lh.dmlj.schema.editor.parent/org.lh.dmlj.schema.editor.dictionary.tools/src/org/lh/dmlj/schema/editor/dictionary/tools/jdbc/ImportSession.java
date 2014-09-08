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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;

public class ImportSession {
	
	protected Connection connection;
	private long connectionClosed = -1;
	private long connectionOpened = -1;
	protected Dictionary dictionary;
	private List<QueryStatistics> statistics = new ArrayList<>();
	
	private static Connection connect(String url, String userid, String password) 
		throws SQLException {
        
		return DriverManager.getConnection(url, userid, password);
    }
		
	private static Connection connect(Dictionary dictionary) throws Throwable {	
		String password;
		if (dictionary.getPassword() == null) {
			PromptForPasswordDialog dialog = 
				new PromptForPasswordDialog(Display.getCurrent().getActiveShell(), dictionary);
			if (dialog.open() == IDialogConstants.CANCEL_ID) {
				throw new RuntimeException("Password required for dictionary " + dictionary.getId());
			}
			password = dialog.getPassword();
			if (dialog.isStorePassword()) {
				dictionary.setPassword(password);
				try {
					dictionary.toFile(Plugin.getDefault().getDictionaryFolder());
				} catch (Throwable t) {
					throw new RuntimeException("Error while saving dictionary data", t);
				}
			}
		} else {
			password = dictionary.getPassword();
		}		
		return connect(dictionary.getConnectionUrl(), dictionary.getUser(), password);		
	}	
	
	private static void logDebug(String message) {
		org.lh.dmlj.schema.editor.Plugin.logDebug(message);
	}

	private static void logInfo(String message) {
		org.lh.dmlj.schema.editor.Plugin.logInfo(message);
	}

	@SuppressWarnings("unused")
	private ImportSession() {
	}
	
	public ImportSession(Dictionary dictionary) {
		super();
		this.dictionary = dictionary;
	}
	
	public Dictionary getDictionary() {
		return dictionary;
	}
	
	public final void close() {
		if (connectionClosed != -1) {
			throw new RuntimeException("already closed");
		}
		try {
			connection.close();
			connectionClosed = System.currentTimeMillis();
		} catch (SQLException e) {
			connectionClosed = System.currentTimeMillis();
			throw new RuntimeException("Error while closing the JDBC connection", e);
		}
		StringBuilder p = new StringBuilder();
		p.append("Import session opened on: " + connectionOpened + "\n");
		p.append("               closed on: " + connectionOpened + "\n");
		p.append("#Queries executed:        " + statistics.size() + ":\n");
		for (QueryStatistics queryStatistics : statistics) {
			p.append(queryStatistics.toString());
		}
		logInfo(p.toString());
	}
	
	public final void open() {
		if (connectionOpened != -1) {
			throw new RuntimeException("already opened");
		}
		try {
			connection = connect(dictionary);
			connectionOpened = System.currentTimeMillis();
		} catch (Throwable t) {
			throw new RuntimeException("Error while opening the JDBC connection", t);
		}
	}

	public final void runQuery(Query query, IRowProcessor rowProcessor) {
		if (connectionOpened != -1) {
			throw new RuntimeException("session not open");
		}
		if (connectionClosed != -1) {
			throw new RuntimeException("session closed");
		}
		long end1 = -1;
		long end2 = -1;
		int rowsProcessed = 0;
		long start = System.currentTimeMillis();
		try {
			logDebug("Start execution of " + query);
			PreparedStatement ps = connection.prepareStatement(query.getSql());
			ResultSet rs = ps.executeQuery();
			end1 = System.currentTimeMillis();
			logDebug("Start processing rows for query '" + query.getDescription() + "'");
			while (rs.next()) {
				int row = rs.getRow();
				rowProcessor.processRow(rs);
				if (rs.getRow() != row) {
					throw new RuntimeException("internal error: current row changed");
				}
				rowsProcessed += 1;
			}
			ps.close();	
			end2 = System.currentTimeMillis();
			logDebug("Processed " + rowsProcessed + " rows for query '" + query.getDescription() + 
					 "'");
			statistics.add(new QueryStatistics(query, start, end1, end2, rowsProcessed, null));
		} catch (Throwable t) {
			String message = "exception while executing query '" + query.getDescription() + "'";
			logDebug("Exception while running query '" + query.getDescription() + "': " +
					 t.getClass().getName() + " (" + t.getMessage() + ")");
			statistics.add(new QueryStatistics(query, start, end1, end2, rowsProcessed, t));
			throw new RuntimeException(message, t);
		}
	}
	
	public static class QueryStatistics {
		
		private long end1;
		private long end2;
		private Query query;
		private int rowsProcessed;
		private long start;
		private Throwable t;
		
		public QueryStatistics(Query query, long start, long end1, long end2, int rowsProcessed, 
							   Throwable t) {
			
			this.query = query;
			this.start = start;
			this.end1 = end1;
			this.end2 = end2;
			this.rowsProcessed = rowsProcessed;
			this.t = t;
		}

		private String format(long date, DateFormat dateFormat) {
			if (date < 0) {
				return "N/A";
			} else {
				return "'" + dateFormat.format(date) + "'";
			}
		}
		
		public String toString() {
			DateFormat dateFormat = org.lh.dmlj.schema.editor.Plugin.getDefault().getDateFormat();
			return "query='" + query.getDescription() + "', " + 
				   "context=" + (query.getContext() != null ? "'" + query.getContext() + "', " : "N/A, ") + 
				   "start=" + format(start, dateFormat) + ", " + 
				   "end1=" + format(end1, dateFormat) + ", " + 
				   "end2=" + format(end2, dateFormat) + ", " +
				   "rowsProcessed=" + rowsProcessed + 
				   (t != null ? "\n--> Exception='" + t.getClass().getSimpleName() + " (" + t.getMessage() + ")'" : "");
		}
		
	}

}
