package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import org.lh.dmlj.schema.editor.dictionary.tools.table.*;

public class AreaProcedureListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized AreaProcedureListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    AreaProcedureListQueryTemplate result = new AreaProcedureListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT ";
  protected final String TEXT_2 = ",";
  protected final String TEXT_3 = NL + "       ";
  protected final String TEXT_4 = " " + NL + "FROM \"";
  protected final String TEXT_5 = "\".\"SA-018\"," + NL + "     \"";
  protected final String TEXT_6 = "\".\"SACALL-020\"                         " + NL + "WHERE SA_018.ROWID = X'";
  protected final String TEXT_7 = "'";

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
String hexDbkeySa_018 = (String) args[1];

    stringBuffer.append(TEXT_1);
    stringBuffer.append( Sa_018.COLUMNS );
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    stringBuffer.append( Sacall_020.COLUMNS );
    stringBuffer.append(TEXT_4);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( hexDbkeySa_018 );
    stringBuffer.append(TEXT_7);
    return stringBuffer.toString();
  }
}