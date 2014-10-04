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

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.editor.Plugin;
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

	private static TestConnectionResult testConnection(Dictionary dictionary) {			
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
				
				result[0] = testConnection(dictionary);
			}};
		Plugin.getDefault().runWithOperationInProgressIndicator(runnableWithProgress);
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

	public static String toHexString(long dbkey) {
		StringBuilder p = new StringBuilder(Long.toHexString(dbkey));
		while (p.length() < 8) {
			p.insert(0, '0');
		}
		return p.toString();
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
