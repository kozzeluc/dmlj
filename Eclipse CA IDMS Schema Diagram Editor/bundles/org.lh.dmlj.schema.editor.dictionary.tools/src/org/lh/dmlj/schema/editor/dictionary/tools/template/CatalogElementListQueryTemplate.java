package org.lh.dmlj.schema.editor.dictionary.tools.template;

public class CatalogElementListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized CatalogElementListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    CatalogElementListQueryTemplate result = new CatalogElementListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = " " + NL + "SELECT TABLE.ROWID AS TABLE_1050_ROWID," + NL + "       COLUMN.ROWID AS COLUMN_1028_ROWID," + NL + "       TABLE.AREA AS TABLE_1050_AREA," + NL + "\t   TABLE.LOCMODE AS TABLE_1050_LOCMODE," + NL + "\t   TABLE.NAME AS TABLE_1050_NAME," + NL + "\t   TABLE.TABLEID AS TABLE_1050_TABLEID," + NL + "\t   COLUMN.NAME AS COLUMN_1028_NAME," + NL + "\t   COLUMN.NULLS AS COLUMN_1028_NULLS," + NL + "\t   COLUMN.NUMBER AS COLUMN_1028_NUMBER," + NL + "\t   COLUMN.TYPE AS COLUMN_1028_TYPE," + NL + "\t   COLUMN.VLENGTH AS COLUMN_1028_VLENGTH " + NL + "FROM SYSTEM.TABLE AS TABLE_1050, " + NL + "     SYSTEM.COLUMN AS COLUMN_1028" + NL + "WHERE TABLE.SCHEMA = 'SYSTEM' AND " + NL + "      COLUMN.SCHEMA = TABLE.SCHEMA AND" + NL + "\t  COLUMN.TABLE = TABLE.NAME" + NL + "ORDER BY TABLE.ROWID, NUMBER";

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