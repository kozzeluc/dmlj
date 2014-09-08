package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import org.lh.dmlj.schema.editor.dictionary.tools.table.*;

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
  protected final String TEXT_1 = " ";
  protected final String TEXT_2 = NL + "SELECT ";
  protected final String TEXT_3 = ",";
  protected final String TEXT_4 = NL + "       ";
  protected final String TEXT_5 = " " + NL + "FROM SYSTEM.TABLE, " + NL + "     SYSTEM.COLUMN" + NL + "WHERE TABLE.ROWID = X'";
  protected final String TEXT_6 = "' " + NL + "      COLUMN.SCHEMA = TABLE.SCHEMA AND" + NL + "\t  COLUMN.TABLE = TABLE.NAME" + NL + "ORDER BY NUMBER";

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
    
Object[] args = (Object[]) argument;
String hexDbkeyTable_1050 = (String) args[0];

    stringBuffer.append(TEXT_2);
    stringBuffer.append( Table_1050.COLUMNS );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(TEXT_4);
    stringBuffer.append( Column_1028.COLUMNS );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( hexDbkeyTable_1050 );
    stringBuffer.append(TEXT_6);
    return stringBuffer.toString();
  }
}