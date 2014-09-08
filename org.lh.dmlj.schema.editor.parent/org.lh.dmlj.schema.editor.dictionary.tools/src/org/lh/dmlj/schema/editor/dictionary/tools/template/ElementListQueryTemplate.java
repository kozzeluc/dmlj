package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import org.lh.dmlj.schema.editor.dictionary.tools.table.*;

public class ElementListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized ElementListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    ElementListQueryTemplate result = new ElementListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT ";
  protected final String TEXT_2 = ",";
  protected final String TEXT_3 = NL + "       ";
  protected final String TEXT_4 = ",";
  protected final String TEXT_5 = NL + "       ";
  protected final String TEXT_6 = " " + NL + "FROM \"";
  protected final String TEXT_7 = "\".\"RCDSYN-079\",            " + NL + "     \"";
  protected final String TEXT_8 = "\".\"NAMESYN-083\"," + NL + "\t \"";
  protected final String TEXT_9 = "\".\"SDR-042\"                         " + NL + "WHERE RCDSYN_079.ROWID = X'";
  protected final String TEXT_10 = "' AND " + NL + "      \"RCDSYN-NAMESYN\" AND" + NL + "      \"SDR-NAMESYN\"";

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
String hexDbkeyRcdsyn_079 = (String) args[1];

    stringBuffer.append(TEXT_1);
    stringBuffer.append( Rcdsyn_079.COLUMNS );
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    stringBuffer.append( Namesyn_083.COLUMNS );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(TEXT_5);
    stringBuffer.append( Sdr_042.COLUMNS );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_7);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_8);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_9);
    stringBuffer.append( hexDbkeyRcdsyn_079 );
    stringBuffer.append(TEXT_10);
    return stringBuffer.toString();
  }
}