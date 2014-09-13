package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import org.lh.dmlj.schema.editor.dictionary.tools.table.*;

public class BaseRecordSynonymQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized BaseRecordSynonymQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    BaseRecordSynonymQueryTemplate result = new BaseRecordSynonymQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT ";
  protected final String TEXT_2 = ",";
  protected final String TEXT_3 = NL + "       ";
  protected final String TEXT_4 = " " + NL + "FROM \"";
  protected final String TEXT_5 = "\".\"SR-036\" AS SR_036, " + NL + "     \"";
  protected final String TEXT_6 = "\".\"RCDSYN-079\" AS RCDSYN_079" + NL + "WHERE SR_036.ROWID = X'";
  protected final String TEXT_7 = "' AND " + NL + "      \"SR-RCDSYN\" AND" + NL + "      RCDSYN_079.RSYN_NAME_079 = SR_036.SR_NAM_036 AND" + NL + "      RCDSYN_079.RSYN_VER_079 = SR_036.RCD_VERS_036";

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
String hexDbkeySr_036 = (String) args[1];

    stringBuffer.append(TEXT_1);
    stringBuffer.append( Sr_036.COLUMNS );
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    stringBuffer.append( Rcdsyn_079.COLUMNS );
    stringBuffer.append(TEXT_4);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( hexDbkeySr_036 );
    stringBuffer.append(TEXT_7);
    return stringBuffer.toString();
  }
}