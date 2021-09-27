package org.lh.dmlj.schema.editor.dictionary.tools.template;

public class RecordProcedureListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized RecordProcedureListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    RecordProcedureListQueryTemplate result = new RecordProcedureListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT S_010.ROWID AS S_010_ROWID, " + NL + "       SRCD_113.ROWID AS SRCD_113_ROWID," + NL + "       SRCALL_040.ROWID AS SRCALL_040_ROWID, " + NL + "       * " + NL + "FROM \"";
  protected final String TEXT_2 = "\".\"S-010\" AS S_010," + NL + "     \"";
  protected final String TEXT_3 = "\".\"SRCD-113\" AS SRCD_113," + NL + "     \"";
  protected final String TEXT_4 = "\".\"SRCALL-040\" AS SRCALL_040                      " + NL + "WHERE S_NAM_010 = '";
  protected final String TEXT_5 = "' AND S_SER_010 = ";
  protected final String TEXT_6 = " AND" + NL + "      \"S-SRCD\" AND" + NL + "      \"SRCD-SRCALL\"";

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
    stringBuffer.append( schemaName );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( schemaVersion );
    stringBuffer.append(TEXT_6);
    return stringBuffer.toString();
  }
}