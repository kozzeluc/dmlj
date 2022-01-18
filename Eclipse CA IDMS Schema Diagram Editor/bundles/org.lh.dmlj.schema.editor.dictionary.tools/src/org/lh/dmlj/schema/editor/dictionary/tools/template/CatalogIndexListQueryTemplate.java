package org.lh.dmlj.schema.editor.dictionary.tools.template;

public class CatalogIndexListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized CatalogIndexListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    CatalogIndexListQueryTemplate result = new CatalogIndexListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT INDEX.ROWID AS INDEX_1041_ROWID," + NL + "       TABLE.ROWID AS TABLE_1050_ROWID," + NL + "       INDEX.AREA AS INDEX_1041_AREA," + NL + "\t   INDEX.COMPRESS AS INDEX_1041_COMPRESS," + NL + "\t   INDEX.DISPLACEMENT AS INDEX_1041_DISPLACEMENT," + NL + "\t   INDEX.IXBLKCONTAINS AS INDEX_1041_IXBLKCONTAINS," + NL + "\t   INDEX.NAME AS INDEX_1041_NAME," + NL + "\t   INDEX.UNIQUE AS INDEX_1041_UNIQUE\t" + NL + "FROM SYSTEM.INDEX AS INDEX_1041," + NL + "     SYSTEM.TABLE AS TABLE_1050 " + NL + "WHERE INDEX.SCHEMA = 'SYSTEM' AND " + NL + "      INDEX.NAME <> 'HASH' AND" + NL + "      TABLE.SCHEMA = 'SYSTEM' AND" + NL + "      INDEX.TABLE = TABLE.NAME" + NL + "ORDER BY INDEX.NAME";

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