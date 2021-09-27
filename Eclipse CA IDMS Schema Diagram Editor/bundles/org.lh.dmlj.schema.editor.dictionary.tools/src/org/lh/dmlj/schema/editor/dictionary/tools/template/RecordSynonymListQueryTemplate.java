package org.lh.dmlj.schema.editor.dictionary.tools.template;

public class RecordSynonymListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized RecordSynonymListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    RecordSynonymListQueryTemplate result = new RecordSynonymListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT RCDSYN_079.ROWID AS RCDSYN_079_ROWID,                                  " + NL + "       RCDSYN_079.RSYN_NAME_079 AS RSYN_NAME_079,                   " + NL + "       RCDSYN_079.RSYN_VER_079 AS RSYN_VER_079," + NL + "       SR_036.ROWID AS SR_036_ROWID,                                         " + NL + "       SR_036.SR_NAM_036 AS SR_NAM_036," + NL + "       SR_036.RCD_VERS_036 AS RCD_VERS_036                      " + NL + "FROM \"";
  protected final String TEXT_2 = "\".\"RCDSYN-079\" AS RCDSYN_079," + NL + "     \"";
  protected final String TEXT_3 = "\".\"SR-036\" AS SR_036                               " + NL + "WHERE RSYN_NAME_079 = '";
  protected final String TEXT_4 = "' AND" + NL + "      \"SR-RCDSYN\"                                          " + NL + "ORDER BY RCDSYN_079.RSYN_VER_079";

	public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/**
 * Copyright (C) 2016  Luc Hermans
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

    
Object[] args = (Object[]) argument;
String sysdirlSchema = (String) args[0];
String recordSynonymName = (String) args[1];

    stringBuffer.append(TEXT_1);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_3);
    stringBuffer.append( recordSynonymName );
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}