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
import java.sql.SQLException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;

public abstract class JdbcTools {
	
	private static final String TEST_CONNECTION_SQL_PART_1 = 
		"SELECT * FROM \"";
	private static final String TEST_CONNECTION_SQL_PART_2 = 
		"\".\"S-010\" ORDER BY S_NAM_010, S_SER_010";
	
	public static Connection connect(String url, String userid, String password) 
		throws SQLException {
        
		return DriverManager.getConnection(url, userid, password);
    }
	
	public static void testConnection(Shell shell, Dictionary dictionary) {			
		
		String password;
		if (dictionary.getPassword() == null) {
			PromptForPasswordDialog dialog = new PromptForPasswordDialog(shell, dictionary);
			if (dialog.open() == IDialogConstants.CANCEL_ID) {
				return;
			}
			password = dialog.getPassword();
			if (dialog.isStorePassword()) {
				dictionary.setPassword(password);
				try {
					dictionary.toFile(Plugin.getDefault().getDictionaryFolder());
				} catch (Throwable t) {
					t.printStackTrace();
					MessageDialog.openError(shell, "Error while saving dictionary data", 
											t.getMessage());
				}
			}
		} else {
			password = dictionary.getPassword();
		}
			
		String message = null;
		try {
			Connection connection = JdbcTools.connect(dictionary.getConnectionUrl(), 
													  dictionary.getUser(), password);
			String sql = 
				TEST_CONNECTION_SQL_PART_1 + dictionary.getSchema() + TEST_CONNECTION_SQL_PART_2;
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.executeQuery();
			connection.close();
		} catch (SQLException e) {
			message = e.getMessage();
		}
		
		String title = "Test Connection";
		if (message != null) {
			StringBuilder fullMessage = 
				new StringBuilder("Connection was NOT successful for dictionary ");
			fullMessage.append(dictionary.getId());
			fullMessage.append(":\n\n");
			fullMessage.append(message);
			MessageDialog.openError(shell, title, fullMessage.toString());
		} else {
			MessageDialog.openInformation(shell, title, "Connection was successful for dictionary " +
					 					  dictionary.getId() + ".");
		}
		
	}	
	
}
