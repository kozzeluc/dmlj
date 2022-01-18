package org.lh.dmlj.schema.editor.dictionary.tools.template;

public class SetListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized SetListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    SetListQueryTemplate result = new SetListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT S_010.ROWID AS S_010_ROWID," + NL + "       SOR_046.ROWID AS SOR_046_ROWID," + NL + "       SMR_052.ROWID AS SMR_052_ROWID," + NL + "       SRCD_113.ROWID AS SRCD_113_ROWID," + NL + "       * " + NL + "FROM \"";
  protected final String TEXT_2 = "\".\"S-010\" AS S_010,  " + NL + "\t \"";
  protected final String TEXT_3 = "\".\"SOR-046\" AS SOR_046," + NL + "\t \"";
  protected final String TEXT_4 = "\".\"SMR-052\" AS SMR_052," + NL + "\t \"";
  protected final String TEXT_5 = "\".\"SRCD-113\" AS SRCD_113" + NL + "WHERE S_010.S_NAM_010 = '";
  protected final String TEXT_6 = "' AND S_010.S_SER_010 = ";
  protected final String TEXT_7 = "  AND" + NL + "\t  \"S-SOR\" AND SET_NAM_046 <> 'CALC' AND" + NL + "\t  \"SOR-SMR\" AND" + NL + "\t  \"SRCD-SMR\"";

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
    stringBuffer.append( schemaName );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( schemaVersion );
    stringBuffer.append(TEXT_7);
    return stringBuffer.toString();
  }
}