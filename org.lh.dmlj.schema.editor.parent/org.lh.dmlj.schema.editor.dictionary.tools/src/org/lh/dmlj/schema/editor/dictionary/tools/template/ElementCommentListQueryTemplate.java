package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import java.util.*;
import org.lh.dmlj.schema.editor.dictionary.tools.table.*;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.*;

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
  protected final String TEXT_1 = "SELECT RCDSYN_079.ROWID AS RCDSYN_079_ROWID," + NL + "       NAMESYN_083.ROWID AS NAMESYN_083_ROWID," + NL + "       SDR_042.ROWID AS SDR_042_ROWID," + NL + "       SDES_044.ROWID AS SDES_044_ROWID," + NL + "       * " + NL + "FROM \"";
  protected final String TEXT_2 = "\".\"RCDSYN-079\" AS RCDSYN_079," + NL + "     \"";
  protected final String TEXT_3 = "\".\"NAMESYN-083\" AS NAMESYN_083," + NL + "     \"";
  protected final String TEXT_4 = "\".\"SDR-042\" AS SDR_042," + NL + "     \"";
  protected final String TEXT_5 = "\".\"SDES-044\" AS SDES_044" + NL + "WHERE RCDSYN_079.ROWID IN                                    ";
  protected final String TEXT_6 = NL + "       ";
  protected final String TEXT_7 = " " + NL + "      \"RCDSYN-NAMESYN\" AND" + NL + "      \"SDR-NAMESYN\" AND" + NL + "      \"SDR-SDES\" AND" + NL + "      (SDES_044.CMT_ID_044 = -3 OR SDES_044.CMT_ID_044 = -11)";

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
@SuppressWarnings("unchecked")
List<Rcdsyn_079> rcdsyn_079s = (List<Rcdsyn_079>) args[1];
List<String> rcdsyn_079_hexDbkeys = new ArrayList<>();
for (int i = 0; i < rcdsyn_079s.size(); i++) {    
    Rcdsyn_079 rcdsyn_079 = rcdsyn_079s.get(i);    
	StringBuilder rcdsyn_079_hexDbkey = new StringBuilder();
	if (i == 0) {
		rcdsyn_079_hexDbkey.append("(");    
	} else {
		rcdsyn_079_hexDbkey.append(" ");
	}
	rcdsyn_079_hexDbkey.append("X'");
	rcdsyn_079_hexDbkey.append(JdbcTools.toHexString(rcdsyn_079.getDbkey()));
	rcdsyn_079_hexDbkey.append("'");
	if (i == (rcdsyn_079s.size() - 1)) {
		rcdsyn_079_hexDbkey.append(") AND");    
	} else {
		rcdsyn_079_hexDbkey.append(",");    
	}
	rcdsyn_079_hexDbkeys.add(rcdsyn_079_hexDbkey.toString());	
}

    stringBuffer.append(TEXT_1);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_3);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_4);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_5);
    
for (String rcdsyn_079_hexDbkey : rcdsyn_079_hexDbkeys) {

    stringBuffer.append(TEXT_6);
    stringBuffer.append( rcdsyn_079_hexDbkey );
    
}

    stringBuffer.append(TEXT_7);
    return stringBuffer.toString();
  }
}