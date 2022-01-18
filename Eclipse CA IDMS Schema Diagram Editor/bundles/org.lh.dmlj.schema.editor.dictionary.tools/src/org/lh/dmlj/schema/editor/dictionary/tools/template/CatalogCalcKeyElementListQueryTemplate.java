package org.lh.dmlj.schema.editor.dictionary.tools.template;

public class CatalogCalcKeyElementListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized CatalogCalcKeyElementListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    CatalogCalcKeyElementListQueryTemplate result = new CatalogCalcKeyElementListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT TABLE.ROWID AS TABLE_1050_ROWID," + NL + "       INDEX.ROWID AS INDEX_1041_ROWID," + NL + "       INDEXKEY.ROWID AS INDEXKEY_1042_ROWID," + NL + "       TABLE.AREA AS TABLE_1050_AREA," + NL + "\t   TABLE.LOCMODE AS TABLE_1050_LOCMODE," + NL + "\t   TABLE.NAME AS TABLE_1050_NAME," + NL + "\t   TABLE.TABLEID AS TABLE_1050_TABLEID," + NL + "\t   INDEX.AREA AS INDEX_1041_AREA," + NL + "\t   INDEX.COMPRESS AS INDEX_1041_COMPRESS," + NL + "\t   INDEX.DISPLACEMENT AS INDEX_1041_DISPLACEMENT," + NL + "\t   INDEX.IXBLKCONTAINS AS INDEX_1041_IXBLKCONTAINS," + NL + "\t   INDEX.NAME AS INDEX_1041_NAME," + NL + "\t   INDEX.UNIQUE AS INDEX_1041_UNIQUE,\t" + NL + "\t   INDEXKEY.COLUMN AS INDEXKEY_1042_COLUMN," + NL + "\t   INDEXKEY.SORTORDER AS INDEXKEY_1042_SORTORDER " + NL + "FROM SYSTEM.TABLE AS TABLE_1050, " + NL + "\t SYSTEM.INDEX AS INDEX_1041," + NL + "\t SYSTEM.INDEXKEY AS INDEXKEY_1042" + NL + "WHERE TABLE.SCHEMA = 'SYSTEM' AND " + NL + "      INDEX.SCHEMA = 'SYSTEM' AND" + NL + "      INDEX.TABLE = TABLE.NAME AND" + NL + "      INDEX.NAME = 'HASH' AND" + NL + "      INDEXKEY.SCHEMA = 'SYSTEM' AND" + NL + "      INDEXKEY.TABLE = TABLE.NAME AND" + NL + "      INDEXKEY.NAME = INDEX.NAME" + NL + "ORDER BY TABLE.NAME," + NL + "\t\t INDEXKEY.SEQUENCE";

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