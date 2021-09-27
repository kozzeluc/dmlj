package org.lh.dmlj.schema.editor.dictionary.tools.template;

import java.util.*;
import org.lh.dmlj.schema.editor.dictionary.tools.*;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
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
  protected final String TEXT_1 = "SELECT RCDSYN_079.ROWID AS RCDSYN_079_ROWID,                 " + NL + "       NAMESYN_083.ROWID AS NAMESYN_083_ROWID,               " + NL + "       SDR_042.ROWID AS SDR_042_ROWID,                       " + NL + "       *                                                     " + NL + "FROM \"";
  protected final String TEXT_2 = "\".\"RCDSYN-079\" AS RCDSYN_079,                   " + NL + "     \"";
  protected final String TEXT_3 = "\".\"NAMESYN-083\" AS NAMESYN_083,                 " + NL + "     \"";
  protected final String TEXT_4 = "\".\"SDR-042\" AS SDR_042                               " + NL + "WHERE RCDSYN_079.ROWID IN ";
  protected final String TEXT_5 = " AND" + NL + "      \"RCDSYN-NAMESYN\" AND                                   " + NL + "      \"SDR-NAMESYN\"";
  protected final String TEXT_6 = NL + "ORDER BY RCDSYN_079.ROWID, SEQ_042";

	public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/**
 * Copyright (C) 2021  Luc Hermans
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
Dictionary dictionary = (Dictionary) args[0];
@SuppressWarnings("unchecked")
List<IRowidProvider> rcdsyn_079s = (List<IRowidProvider>) args[1];
String sysdirlSchema = dictionary.getSchemaWithDefault(Plugin.getDefault());
List<List<Rowid>> splitQueryRowidList = JdbcTools.getSplitQueryRowidList(rcdsyn_079s, dictionary);
RowidListTemplate rowidListTemplate = new RowidListTemplate();
boolean first = true;
for (List<Rowid> rowids : splitQueryRowidList) {
    String rowidList = rowidListTemplate.generate(new Object[] {rowids});    
    if (first) {
        first = false;
    } else {
		stringBuffer.append("\nUNION\n");        
    }

    stringBuffer.append(TEXT_1);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_3);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_4);
    stringBuffer.append( rowidList );
    stringBuffer.append(TEXT_5);
    
}

    stringBuffer.append(TEXT_6);
    return stringBuffer.toString();
  }
}