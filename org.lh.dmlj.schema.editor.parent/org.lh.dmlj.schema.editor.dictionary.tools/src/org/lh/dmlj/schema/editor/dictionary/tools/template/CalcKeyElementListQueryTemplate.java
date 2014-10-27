package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

public class CalcKeyElementListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized CalcKeyElementListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    CalcKeyElementListQueryTemplate result = new CalcKeyElementListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT S_010.ROWID AS S_010_ROWID," + NL + "       SRCD_113.ROWID AS SRCD_113_ROWID," + NL + "       SAM_056.ROWID AS SAM_056_ROWID," + NL + "       SMR_052.ROWID AS SMR_052_ROWID," + NL + "       SCR_054.ROWID AS SCR_054_ROWID," + NL + "       *" + NL + "FROM \"";
  protected final String TEXT_2 = "\".\"S-010\" AS S_010, " + NL + "\t \"";
  protected final String TEXT_3 = "\".\"SRCD-113\" AS SRCD_113," + NL + "\t \"";
  protected final String TEXT_4 = "\".\"SAM-056\" AS SAM_056, " + NL + "\t \"";
  protected final String TEXT_5 = "\".\"SMR-052\" AS SMR_052," + NL + "\t \"";
  protected final String TEXT_6 = "\".\"SCR-054\" AS SCR_054" + NL + "WHERE S_010.S_NAM_010 = '";
  protected final String TEXT_7 = "' AND S_010.S_SER_010 = ";
  protected final String TEXT_8 = " AND" + NL + "      \"S-SRCD\" AND SRCD_113.SR_ID_113 > 9 AND SRCD_113.MODE_113 = 1 AND" + NL + "      FIRST \"SRCD-SAM\" AND" + NL + "      \"SRCD-SMR\" AND SMR_052.SET_NAM_052 = 'CALC' AND" + NL + "      \"SMR-SCR\"";

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
    stringBuffer.append( schemaName );
    stringBuffer.append(TEXT_7);
    stringBuffer.append( schemaVersion );
    stringBuffer.append(TEXT_8);
    return stringBuffer.toString();
  }
}