package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import org.lh.dmlj.schema.editor.dictionary.tools.table.*;

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
  protected final String TEXT_1 = "SELECT ";
  protected final String TEXT_2 = " " + NL + "FROM SYSTEM.CONSTKEY" + NL + "WHERE CONSTKEY.SCHEMA = 'SYSTEM' AND" + NL + "      CONSTKEY.CONSTRAINT = CONSTRAINT.NAME" + NL + "ORDER BY CONSTKEY.NAME," + NL + "\t\t CONSTKEY.SEQUENCE";

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
    stringBuffer.append( Constkey_1030.COLUMNS );
    stringBuffer.append(TEXT_2);
    return stringBuffer.toString();
  }
}