package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

public class ValidSchemaListForSysdirlDictionariesQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized ValidSchemaListForSysdirlDictionariesQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    ValidSchemaListForSysdirlDictionariesQueryTemplate result = new ValidSchemaListForSysdirlDictionariesQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT S_010.ROWID AS S_010_ROWID," + NL + "       *" + NL + "FROM \"";
  protected final String TEXT_2 = "\".\"S-010\" AS S_010" + NL + "WHERE S_NAM_010 <> 'NON IDMS' AND ERR_010 = 0" + NL + "ORDER BY S_NAM_010, S_SER_010";

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

    stringBuffer.append(TEXT_1);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_2);
    return stringBuffer.toString();
  }
}