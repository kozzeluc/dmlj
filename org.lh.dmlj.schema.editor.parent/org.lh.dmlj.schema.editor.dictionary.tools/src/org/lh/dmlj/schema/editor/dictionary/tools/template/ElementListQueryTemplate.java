package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import java.util.*;
import org.lh.dmlj.schema.editor.dictionary.tools.table.*;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.*;

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
  protected final String TEXT_1 = "SELECT SR_036.ROWID AS SR_036_ROWID,                         " + NL + "       RCDSYN_079.ROWID AS RCDSYN_079_ROWID,                 " + NL + "       NAMESYN_083.ROWID AS NAMESYN_083_ROWID,               " + NL + "       SDR_042.ROWID AS SDR_042_ROWID,                       " + NL + "       *                                                     " + NL + "FROM \"";
  protected final String TEXT_2 = "\".\"OOAK-012\" AS OOAK_079,                       " + NL + "     \"";
  protected final String TEXT_3 = "\".\"SR-036\" AS SR_036,                           " + NL + "     \"";
  protected final String TEXT_4 = "\".\"RCDSYN-079\" AS RCDSYN_079,                   " + NL + "     \"";
  protected final String TEXT_5 = "\".\"NAMESYN-083\" AS NAMESYN_083,                 " + NL + "     \"";
  protected final String TEXT_6 = "\".\"SDR-042\" AS SDR_042                          " + NL + "WHERE OOAK_KEY_012 = 'OOAK' AND                              " + NL + "      \"OOAK-SR\" AND" + NL + "      SR_036.ROWID IN";
  protected final String TEXT_7 = NL + "       ";
  protected final String TEXT_8 = " " + NL + "      \"SR-RCDSYN\" AND" + NL + "      RCDSYN_079.ROWID IN";
  protected final String TEXT_9 = NL + "       ";
  protected final String TEXT_10 = NL + "      \"RCDSYN-NAMESYN\" AND                                   " + NL + "      \"SDR-NAMESYN\"        ";

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
Map<Long, Sr_036> sr_036_map = new HashMap<>();
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
	
	Sr_036 sr_036 = rcdsyn_079.getSr_036();
	if (!sr_036_map.containsKey(Long.valueOf(sr_036.getDbkey()))) {
		sr_036_map.put(Long.valueOf(sr_036.getDbkey()), sr_036);
	}	
}
List<Sr_036> sr_036s = new ArrayList<>(sr_036_map.values());
Collections.sort(sr_036s, new Comparator<Sr_036>() {
	@Override
	public int compare(Sr_036 r1, Sr_036 r2) {
		if (r1.getSrNam_036().equals(r2.getSrNam_036())) {
			return r1.getRcdVers_036() - r2.getRcdVers_036();
		} else {
			return r1.getSrNam_036().compareTo(r2.getSrNam_036());
		}
	}			
});
List<String> sr_036_hexDbkeys = new ArrayList<>();
for (int i = 0; i < sr_036s.size(); i++) {    
	StringBuilder sr_036_hexDbkey = new StringBuilder();
	if (i == 0) {
		sr_036_hexDbkey.append("(");    
	} else {
		sr_036_hexDbkey.append(" ");
	}
	sr_036_hexDbkey.append("X'");
	sr_036_hexDbkey.append(JdbcTools.toHexString(sr_036s.get(i).getDbkey()));
	sr_036_hexDbkey.append("'");
	if (i == (sr_036s.size() - 1)) {
		sr_036_hexDbkey.append(") AND");    
	} else {
		sr_036_hexDbkey.append(",");    
	}
	sr_036_hexDbkeys.add(sr_036_hexDbkey.toString());	
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
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_6);
    
for (String sr_036_hexDbkey : sr_036_hexDbkeys) {

    stringBuffer.append(TEXT_7);
    stringBuffer.append( sr_036_hexDbkey );
    
}

    stringBuffer.append(TEXT_8);
    
for (String rcdsyn_079_hexDbkey : rcdsyn_079_hexDbkeys) {

    stringBuffer.append(TEXT_9);
    stringBuffer.append( rcdsyn_079_hexDbkey );
    
}

    stringBuffer.append(TEXT_10);
    return stringBuffer.toString();
  }
}