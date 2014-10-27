package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

public class CatalogForeignKeyListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized CatalogForeignKeyListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    CatalogForeignKeyListQueryTemplate result = new CatalogForeignKeyListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT CONSTKEY.ROWID AS CONSTKEY_1030_ROWID," + NL + "       CONSTKEY.NAME AS CONSTKEY_1030_NAME," + NL + "\t   CONSTKEY.NUMBER AS CONSTKEY_1030_NUMBER " + NL + "FROM SYSTEM.CONSTKEY AS CONSTKEY_1030" + NL + "WHERE CONSTKEY.SCHEMA = 'SYSTEM'" + NL + "ORDER BY CONSTKEY.NAME," + NL + "\t\t CONSTKEY.SEQUENCE";

	public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
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

    stringBuffer.append(TEXT_1);
    return stringBuffer.toString();
  }
}