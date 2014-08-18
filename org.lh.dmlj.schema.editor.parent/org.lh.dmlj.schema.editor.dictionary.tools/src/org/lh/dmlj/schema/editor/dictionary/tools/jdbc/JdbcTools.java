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
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;

public abstract class JdbcTools {
	
	private static final String SYSDIRL_SCHEMA = "<sysdirl-schema>";
	
	private static final String SQL_VALID_SCHEMAS = 
		"SELECT * FROM \"" + SYSDIRL_SCHEMA + "\".OOAK-012\", \"" + SYSDIRL_SCHEMA + "\".\"S-010\" " +
		"WHERE OOAK_KEY_012 = 'OOAK' AND \"OOAK-S\" AND S_NAM_010 <> 'NON IDMS' AND ERR_010 = 0 " +
		"ORDER BY S_NAM_010, S_SER_010";	
	
	public static String buildQueryForValidSchemas(Dictionary dictionary) {
		return SQL_VALID_SCHEMAS.replace(SYSDIRL_SCHEMA, dictionary.getSchema());
	}

	private static Connection connect(String url, String userid, String password) 
		throws SQLException {
        
		return DriverManager.getConnection(url, userid, password);
    }
	
	public static Connection connect(Dictionary dictionary) throws Throwable {	
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
	
	public static void testConnection(Dictionary dictionary) {			
		String title = "Test Connection";
		Connection connection = null;
		try {
			connection = connect(dictionary);
			String sql = buildQueryForValidSchemas(dictionary);
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.executeQuery();
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
				connection.close();
			} catch (SQLException e) {
			}
		}
	}	
	
}
