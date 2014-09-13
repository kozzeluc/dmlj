package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import org.lh.dmlj.schema.editor.dictionary.tools.table.*;

public class ViaSetListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized ViaSetListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    ViaSetListQueryTemplate result = new ViaSetListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT ";
  protected final String TEXT_2 = ",";
  protected final String TEXT_3 = NL + "       ";
  protected final String TEXT_4 = ",";
  protected final String TEXT_5 = NL + "       ";
  protected final String TEXT_6 = ",";
  protected final String TEXT_7 = NL + "       ";
  protected final String TEXT_8 = " " + NL + "FROM \"";
  protected final String TEXT_9 = "\".\"S-010\" AS S_010," + NL + "     \"";
  protected final String TEXT_10 = "\".\"SRCD-113\" AS SRCD_113," + NL + "     \"";
  protected final String TEXT_11 = "\".\"SAM-056\" AS SAM_056," + NL + "     \"";
  protected final String TEXT_12 = "\".\"SMR-052\" AS SMR_052" + NL + "WHERE S_010.S_NAM_010 = '";
  protected final String TEXT_13 = "' AND" + NL + "      S_010.S_SER_010 = ";
  protected final String TEXT_14 = " AND" + NL + "      \"S-SRCD\" AND" + NL + "      FIRST \"SRCD-SAM\" AND" + NL + "      \"SRCD-SMR\" AND" + NL + "      SMR_052.VIA_052 = 1 AND SMR_052.SET_NAM_052 <> 'CALC'";

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
    stringBuffer.append( S_010.COLUMNS );
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    stringBuffer.append( Srcd_113.COLUMNS );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(TEXT_5);
    stringBuffer.append( Sam_056.COLUMNS );
    stringBuffer.append(TEXT_6);
    stringBuffer.append(TEXT_7);
    stringBuffer.append( Smr_052.COLUMNS );
    stringBuffer.append(TEXT_8);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_9);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_10);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_11);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_12);
    stringBuffer.append( schemaName );
    stringBuffer.append(TEXT_13);
    stringBuffer.append( schemaVersion );
    stringBuffer.append(TEXT_14);
    return stringBuffer.toString();
  }
}