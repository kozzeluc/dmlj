package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import org.lh.dmlj.schema.editor.dictionary.tools.table.*;

public class SchemaDescriptionAndCommentListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized SchemaDescriptionAndCommentListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    SchemaDescriptionAndCommentListQueryTemplate result = new SchemaDescriptionAndCommentListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "%" + NL + "/**" + NL + " * Copyright (C) 2014  Luc Hermans" + NL + " * " + NL + " * This program is free software: you can redistribute it and/or modify it under the terms of the" + NL + " * GNU General Public License as published by the Free Software Foundation, either version 3 of the" + NL + " * License, or (at your option) any later version." + NL + " * " + NL + " * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without" + NL + " * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU" + NL + " * General Public License for more details." + NL + " * " + NL + " * You should have received a copy of the GNU General Public License along with this program.  If" + NL + " * not, see <http://www.gnu.org/licenses/>." + NL + " * " + NL + " * Contact information: kozzeluc@gmail.com." + NL + " */" + NL + "%>";
  protected final String TEXT_2 = " " + NL + "SELECT ";
  protected final String TEXT_3 = ",";
  protected final String TEXT_4 = NL + "       ";
  protected final String TEXT_5 = " " + NL + "FROM \"";
  protected final String TEXT_6 = "\".\"S-010\",  " + NL + "\t \"";
  protected final String TEXT_7 = "\".\"SCHEMACMT-181\"" + NL + "WHERE S_NAM_010 = '";
  protected final String TEXT_8 = "' AND S_SER_010 = ";
  protected final String TEXT_9 = " AND " + NL + "      \"S-SCHEMACMT\"";

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
    stringBuffer.append( Schemacmt_181.COLUMNS );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_7);
    stringBuffer.append( schemaName );
    stringBuffer.append(TEXT_8);
    stringBuffer.append( schemaVersion );
    stringBuffer.append(TEXT_9);
    return stringBuffer.toString();
  }
}