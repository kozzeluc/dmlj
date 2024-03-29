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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.IRowidProvider;

public abstract class JdbcTools {
		
	public static Rowid getRowid(ResultSet resultSet, String rowIdColumnLabel) throws SQLException {
		return Rowid.fromHexString(resultSet.getString(rowIdColumnLabel));
	}
	
	public static List<List<Rowid>> getSplitQueryRowidList(List<IRowidProvider> rowidProviders, Dictionary dictionary) {
		int queryRowidyListSizeMaximum = dictionary.getQueryRowidListSizeMaximumWithDefault(Plugin.getDefault());
		List<List<Rowid>> splitQueryRowidList = new ArrayList<>();
		List<Rowid> queryRowidList = new ArrayList<>();
		splitQueryRowidList.add(queryRowidList);
		for (IRowidProvider rowidProvider : rowidProviders) {
			Rowid rowid = rowidProvider.getRowid();
			if (queryRowidList.size() == queryRowidyListSizeMaximum) {
				queryRowidList = new ArrayList<>();
				splitQueryRowidList.add(queryRowidList);
			}
			queryRowidList.add(rowid);
		}
		if (splitQueryRowidList.size() == 1 && splitQueryRowidList.get(0).isEmpty()) {
			return Collections.emptyList();
		} else {
			return splitQueryRowidList;
		}
	}

	public static String removeTrailingSpaces(String aString) {
		StringBuilder p = new StringBuilder(aString);
		while (p.length() > 0 && p.charAt(p.length() - 1) == ' ') {
			p.setLength(p.length() - 1);
		}
		return p.toString();
	}

	private static TestConnectionResult testConnection(Dictionary dictionary) {			
		DictionarySession session = null;
		try {
			session = 
				new DictionarySession(dictionary, "Test connection to dictionary " + dictionary.getId());
			session.open();
			IQuery query = new Query.Builder().forValidSchemaList(session).build();
			session.runQuery(query, new IRowProcessor() {
				@Override
				public void processRow(ResultSet row) throws SQLException {					
				}				
			});
			return new TestConnectionResult();
		} catch (Throwable t) {
			StringBuilder fullMessage = 
				new StringBuilder("Connection was NOT successful for dictionary ");
			fullMessage.append(dictionary.getId());
			fullMessage.append(":\n\n");
			fullMessage.append(t.getMessage());
			return new TestConnectionResult(fullMessage.toString());
		} finally {
			try {
				session.close();
			} catch (Throwable t) {
			}
		}
	}
	
	public static void testConnectionWithOperationInProgressIndicator(final Dictionary dictionary) {
		final TestConnectionResult[] result = new TestConnectionResult[1];
		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) 
				throws InvocationTargetException, InterruptedException {
				
				monitor.beginTask("Testing connection for dictionary " + 
								  dictionary.getId() + "...", IProgressMonitor.UNKNOWN);
				result[0] = testConnection(dictionary);
				monitor.done();
			}};
		org.lh.dmlj.schema.editor.Plugin.getDefault().runWithOperationInProgressIndicator(runnableWithProgress);
		// display the result after making sure the 'Operation in progress' window is no longer 
		// visible (note that the 'Password required' dialog will be presented while the 'Operation
		// in progress' window IS visible, but behind that dialog; hopefully people will get 
		// anoyed sooner or later with that dialog and decide to have the (encrypted) password 
		// stored in the workspace so that this 'ugly stack of windows and dialogs' disappears)
		String title = "Test Connection";
		if (result[0].ok) {
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), title, 
					  					  "Connection was successful for dictionary " +
					  					  dictionary.getId() + ".");
		} else {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), title, result[0].message);
		}
	}
	
	public static boolean[] toBooleanArray(ResultSet row, String columnLabel) {
		byte[] b1 = toByteArray(row, columnLabel);
		boolean[] b2 = new boolean[8 * b1.length];
		for (int i = 0; i < b2.length; i++) {
			if ((b1[i / 8] & (1 << (7 - (i % 8)))) > 0) {
				b2[i] = true;
			}
	    }
		return b2;
	}
	
	public static byte[] toByteArray(ResultSet row, String columnLabel) {
		InputStream in;
		try {
			in = row.getBinaryStream(columnLabel);
			byte[] b = new byte[1];
			in.read(b);
			in.close();
			return b;
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		}		
	}
	
	public static class TestConnectionResult {
		
		private boolean ok;
		private String message;

		public TestConnectionResult() {
			super();
			this.ok = true;
		}
		
		public TestConnectionResult(String message) {
			super();
			this.ok = false;
			this.message = message;
		}
		
	}
	
}
