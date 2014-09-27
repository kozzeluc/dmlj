package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

public class ElementCommentListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized ElementCommentListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    ElementCommentListQueryTemplate result = new ElementCommentListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT SR_036.ROWID AS SR_036_ROWID," + NL + "       RCDSYN_079.ROWID AS RCDSYN_079_ROWID," + NL + "       NAMESYN_083.ROWID AS NAMESYN_083_ROWID," + NL + "       SDR_042.ROWID AS SDR_042_ROWID," + NL + "       SDES_044.ROWID AS SDES_044_ROWID," + NL + "       * " + NL + "FROM \"";
  protected final String TEXT_2 = "\".\"OOAK-012\" AS OOAK_079,                       " + NL + "     \"";
  protected final String TEXT_3 = "\".\"SR-036\" AS SR_036,                           " + NL + "     \"";
  protected final String TEXT_4 = "\".\"RCDSYN-079\" AS RCDSYN_079," + NL + "     \"";
  protected final String TEXT_5 = "\".\"NAMESYN-083\" AS NAMESYN_083," + NL + "     \"";
  protected final String TEXT_6 = "\".\"SDR-042\" AS SDR_042," + NL + "     \"";
  protected final String TEXT_7 = "\".\"SDES-044\" AS SDES_044" + NL + "WHERE OOAK_KEY_012 = 'OOAK' AND                              " + NL + "      \"OOAK-SR\" AND                                          " + NL + "      \"SR-RCDSYN\" AND " + NL + "      \"RCDSYN-NAMESYN\" AND" + NL + "      \"SDR-NAMESYN\" AND" + NL + "      \"SDR-SDES\" AND" + NL + "      (SDES_044.CMT_ID_044 = -3 OR SDES_044.CMT_ID_044 = -11) AND" + NL + "      RCDSYN_079.ROWID IN                                    " + NL + "          (SELECT RCDSYN_079.ROWID                               " + NL + "           FROM \"";
  protected final String TEXT_8 = "\".\"S-010\" AS S_010,                  " + NL + "                \"";
  protected final String TEXT_9 = "\".\"SRCD-113\" AS SRCD_113,            " + NL + "                \"";
  protected final String TEXT_10 = "\".\"RCDSYN-079\" AS RCDSYN_079,        " + NL + "                \"";
  protected final String TEXT_11 = "\".\"SR-036\" AS SR_036                 " + NL + "           WHERE S_NAM_010 = '";
  protected final String TEXT_12 = "' AND S_SER_010 = ";
  protected final String TEXT_13 = " AND" + NL + "                 \"S-SRCD\" AND SR_ID_113 > 9 AND              " + NL + "                 \"RCDSYN-SRCD\" AND" + NL + "                 \"SR-RCDSYN\")";

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

    
Object[] args = (Object[]) argument;
String sysdirlSchema = (String) args[0];
String schemaName = (String) args[1];
int schemaVersion = ((Integer) args[2]).intValue();

    stringBuffer.append(TEXT_1);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_3);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_4);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_7);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_8);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_9);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_10);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_11);
    stringBuffer.append( schemaName );
    stringBuffer.append(TEXT_12);
    stringBuffer.append( schemaVersion );
    stringBuffer.append(TEXT_13);
    return stringBuffer.toString();
  }
}