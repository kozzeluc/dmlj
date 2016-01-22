package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import java.util.*;
import org.lh.dmlj.schema.editor.dictionary.tools.*;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.*;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.*;

public class ElementSynonymCommentListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized ElementSynonymCommentListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    ElementSynonymCommentListQueryTemplate result = new ElementSynonymCommentListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT RCDSYN_079.ROWID AS RCDSYN_079_ROWID," + NL + "       NAMESYN_083.ROWID AS NAMESYN_083_ROWID," + NL + "       NAMEDES_186.ROWID AS NAMEDES_186_ROWID," + NL + "       * " + NL + "FROM \"";
  protected final String TEXT_2 = "\".\"RCDSYN-079\" AS RCDSYN_079," + NL + "     \"";
  protected final String TEXT_3 = "\".\"NAMESYN-083\" AS NAMESYN_083," + NL + "\t \"";
  protected final String TEXT_4 = "\".\"NAMEDES-186\" AS NAMEDES_186                                   " + NL + "WHERE RCDSYN_079.ROWID IN ";
  protected final String TEXT_5 = " AND" + NL + "      \"RCDSYN-NAMESYN\" AND" + NL + "      \"NAMESYN-NAMEDES\" AND" + NL + "      NAMEDES_186.CMT_ID_186 = -11";

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
Dictionary dictionary = (Dictionary) args[0];
@SuppressWarnings("unchecked")
List<IDbkeyProvider> rcdsyn_079s = (List<IDbkeyProvider>) args[1];
String sysdirlSchema = dictionary.getSchemaWithDefault(Plugin.getDefault());
List<List<Long>> splitQueryDbkeyList = JdbcTools.getSplitQueryDbkeyList(rcdsyn_079s, dictionary);
DbkeyListTemplate dbkeyListTemplate = new DbkeyListTemplate();
boolean first = true;
for (List<Long> dbkeys : splitQueryDbkeyList) {
    String dbkeyList = dbkeyListTemplate.generate(new Object[] {dbkeys});    
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
    stringBuffer.append( dbkeyList );
    stringBuffer.append(TEXT_5);
    
}

    return stringBuffer.toString();
  }
}