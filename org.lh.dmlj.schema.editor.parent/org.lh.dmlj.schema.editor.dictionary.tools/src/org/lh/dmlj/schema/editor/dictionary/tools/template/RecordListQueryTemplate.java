package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import org.lh.dmlj.schema.editor.dictionary.tools.table.*;

public class RecordListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized RecordListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    RecordListQueryTemplate result = new RecordListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "%" + NL + "/**" + NL + " * Copyright (C) 2014  Luc Hermans" + NL + " * " + NL + " * This program is free software: you can redistribute it and/or modify it under the terms of the" + NL + " * GNU General Public License as published by the Free Software Foundation, either version 3 of the" + NL + " * License, or (at your option) any later version." + NL + " * " + NL + " * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without" + NL + " * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU" + NL + " * General Public License for more details." + NL + " * " + NL + " * You should have received a copy of the GNU General Public License along with this program.  If" + NL + " * not, see <http://www.gnu.org/licenses/>." + NL + " * " + NL + " * Contact information: kozzeluc@gmail.com." + NL + " */" + NL + "%>";
  protected final String TEXT_2 = " " + NL + "SELECT ";
  protected final String TEXT_3 = ",";
  protected final String TEXT_4 = NL + "       ";
  protected final String TEXT_5 = ",";
  protected final String TEXT_6 = NL + "       ";
  protected final String TEXT_7 = ",  ";
  protected final String TEXT_8 = NL + "       ";
  protected final String TEXT_9 = ",";
  protected final String TEXT_10 = NL + "       ";
  protected final String TEXT_11 = "  " + NL + "FROM \"";
  protected final String TEXT_12 = "\".\"S-010\", " + NL + "\t \"";
  protected final String TEXT_13 = "\".\"SRCD-113\"," + NL + "\t \"";
  protected final String TEXT_14 = "\".\"SAM-056\", " + NL + "\t \"";
  protected final String TEXT_15 = "\".\"SR_036\", " + NL + "\t \"";
  protected final String TEXT_16 = "\".\"RCDSYN_079\"" + NL + "WHERE S_NAM_010 = '";
  protected final String TEXT_17 = "' AND S_SER_010 = ";
  protected final String TEXT_18 = " AND " + NL + "      \"S-SRCD\" AND SR_ID_113 > 9 AND " + NL + "      \"SRCD-SAM\" AND " + NL + "      \"RCDSYN-SRCD\" AND" + NL + "      \"SR-RCDSYN\"";

	public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
Object[] args = (Object[]) argument;
String sysdirlSchema = (String) args[0];
String schemaName = (String) args[1];
int schemaVersion = ((Integer) args[2]).intValue();

    stringBuffer.append(TEXT_2);
    stringBuffer.append( S_010.COLUMNS );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(TEXT_4);
    stringBuffer.append( Srcd_113.COLUMNS );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(TEXT_6);
    stringBuffer.append( Sam_056.COLUMNS );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(TEXT_8);
    stringBuffer.append( Sr_036.COLUMNS );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(TEXT_10);
    stringBuffer.append( Rcdsyn_079.COLUMNS );
    stringBuffer.append(TEXT_11);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_12);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_13);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_14);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_15);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_16);
    stringBuffer.append( schemaName );
    stringBuffer.append(TEXT_17);
    stringBuffer.append( schemaVersion );
    stringBuffer.append(TEXT_18);
    return stringBuffer.toString();
  }
}