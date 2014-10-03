package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import java.util.*;
import org.lh.dmlj.schema.editor.dictionary.tools.table.*;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.*;

public class BaseRecordSynonymListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized BaseRecordSynonymListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    BaseRecordSynonymListQueryTemplate result = new BaseRecordSynonymListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT RCDSYN_079.ROWID AS RCDSYN_079_ROWID," + NL + "       SR_036.ROWID AS SR_036_ROWID," + NL + "       *      " + NL + "FROM \"";
  protected final String TEXT_2 = "\".\"SR-036\" AS SR_036," + NL + "     \"";
  protected final String TEXT_3 = "\".\"RCDSYN-079\" AS RCDSYN_079" + NL + "WHERE SR_036.ROWID IN";
  protected final String TEXT_4 = NL + "       ";
  protected final String TEXT_5 = "       " + NL + "      \"SR-RCDSYN\" AND" + NL + "      RSYN_NAME_079 = SR_NAM_036 AND" + NL + "      RSYN_VER_079 = RCD_VERS_036";

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
List<Sr_036> sr_036s = (List<Sr_036>) args[1];
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
    
for (String sr_036_hexDbkey : sr_036_hexDbkeys) {

    stringBuffer.append(TEXT_4);
    stringBuffer.append( sr_036_hexDbkey );
    
}

    stringBuffer.append(TEXT_5);
    return stringBuffer.toString();
  }
}