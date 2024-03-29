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
package org.lh.dmlj.schema.editor.dictionary.tools.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.ui.PromptForPasswordDialog;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.log.Logger;

public class DictionarySession {
	
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
	private static final String VIRTUAL_KEYS_SQL = "SELECT INCLVIRTKEYS FROM SYSTEM.SCHEMA WHERE NAME = ?";
	
	protected Connection connection;
	private long connectionClosed = -1;
	private long connectionOpened = -1;
	private DateFormat dateFormat;
	private String description;
	protected Dictionary dictionary;
	private int queryNumber = 0;
	private int runningQueryCount = 0;
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

	private static String getColumnNames(ResultSet rs) throws SQLException {
		List<String> columnNames = new ArrayList<>();
		ResultSetMetaData metaData = rs.getMetaData();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			columnNames.add(metaData.getColumnName(i));
		}
		return columnNames.stream().collect(Collectors.joining(","));
	}

	@SuppressWarnings("unused")
	private DictionarySession() {
	}
	
	public DictionarySession(Dictionary dictionary, String description) {
		super();
		this.dictionary = dictionary;
		this.description = description;
		dateFormat = org.lh.dmlj.schema.editor.Plugin.getDefault().getDateFormat();
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
		p.append("***** Statistics for import session '" + description + "' *****\n");
		p.append("        Opened on: " + format(connectionOpened) + "\n");
		p.append("        Closed on: " + format(connectionClosed) + "\n");
		long elapseTimeInMilliseconds = connectionClosed - connectionOpened;
		p.append("      Elapse time: " + elapseTimeInMilliseconds + "ms\n");
		p.append("#Queries executed: " + statistics.size() + "\n");
		for (QueryStatistics queryStatistics : statistics) {
			p.append(queryStatistics.toString());
			p.append("\n");
		}
		logger.info(p.toString());
	}

	private String format(long date) {
		return dateFormat.format(date);
	}

	public Connection getConnection() {
		return connection;
	}

	public String getDescription() {
		return description;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}
	
	public final boolean isSchemaDefinedWithVirtualKeys() {
		if (connectionOpened == -1) {
			throw new RuntimeException("connection not open");
		} else if (connectionClosed != -1) {
			throw new RuntimeException("connection closed");
		}
		
		boolean result = false;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(VIRTUAL_KEYS_SQL);
			ps.setString(1, dictionary.getSchema());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getString(1).equals("S");
			}
		} catch (SQLException e) {
			logger.error("exception while checking schema for virtual keys", e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("exception while closing prepared statement for virtual keys check", e);
				}
			}
		}
		return result;
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

	public final void runQuery(IQuery query, IRowProcessor rowProcessor) {
		
		if (connectionOpened == -1) {
			throw new RuntimeException("session not open");
		}
		if (connectionClosed != -1) {
			throw new RuntimeException("session closed");
		}		
		if (runningQueryCount > 0) {
			// we don't allow nested queries for performance reasons; we might blow-up the CV with
			// not even that big result sets
			throw new RuntimeException("nested queries are NOT allowed");
		}
		runningQueryCount += 1;
		
		query.setNumber(++queryNumber);		
		long end1 = -1;
		long end2 = -1;
		int rowsProcessed = 0;
		long start = System.currentTimeMillis();
		try {
			logger.debug("Start execution of " + query);
			PreparedStatement ps = connection.prepareStatement(query.getSql());
			ResultSet rs = ps.executeQuery();
			end1 = System.currentTimeMillis();
			logger.debug("Start processing rows for query '" + query.getDescription() + "'\nColumns: " + getColumnNames(rs));
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
			logger.debug("Processed " + rowsProcessed + " rows for query '" + query.getDescription() + "'");
			statistics.add(new QueryStatistics(query, start, end1, end2, rowsProcessed, null));
			runningQueryCount -= 1;
		} catch (Throwable t) {
			String message = "Exception while executing query '" + query.getDescription() + 
							 "'; see log for details.";
			logger.error("Exception while running query '" + query.getDescription() + 
						 "': " + t.getClass().getName() + " (" + t.getMessage() + ")", t);
			statistics.add(new QueryStatistics(query, start, end1, end2, rowsProcessed, t));
			runningQueryCount -= 1;
			throw new RuntimeException(message, t);
		}	
	}
	
	public static class QueryStatistics {
		
		private long end1;
		private long end2;
		private IQuery query;
		private int rowsProcessed;
		private long start;
		private Throwable t;
		
		public QueryStatistics(IQuery query, long start, long end1, long end2, int rowsProcessed, 
							   Throwable t) {
			
			this.query = query;
			this.start = start;
			this.end1 = end1;
			this.end2 = end2;
			this.rowsProcessed = rowsProcessed;
			this.t = t;
		}

		public String toString() {
			return "  query #" + query.getNumber() + ", description='" + query.getDescription() + 
				   "', elapseTimeQuery=" + (end1 - start) + ", " + "elapseTimeRowProcessing=" + 
				   (end2 - end1) + ", " + "rowsProcessed=" + rowsProcessed + 
				   (t != null ? "\n--> Exception='" + t.getClass().getSimpleName() + " (" + t.getMessage() + ")'" : "");
		}
		
	}

}
