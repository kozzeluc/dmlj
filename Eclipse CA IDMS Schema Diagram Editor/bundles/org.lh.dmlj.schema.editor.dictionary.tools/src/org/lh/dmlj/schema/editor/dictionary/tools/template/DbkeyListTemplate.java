package org.lh.dmlj.schema.editor.dictionary.tools.template;

import java.util.*;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.*;

public class DbkeyListTemplate
{
  protected static String nl;
  public static synchronized DbkeyListTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    DbkeyListTemplate result = new DbkeyListTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";

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
@SuppressWarnings("unchecked")
List<Long> dbkeys = (List<Long>) args[0];
for (int i = 0; i < dbkeys.size(); i++) {    
	StringBuilder hexDbkey = new StringBuilder();
	if (i == 0) {
		hexDbkey.append("(");    
	} else {
		hexDbkey.append(" ");
	}
	hexDbkey.append("X'");
	hexDbkey.append(JdbcTools.toHexString(Long.valueOf(dbkeys.get(i))));
	hexDbkey.append("'");
	if (i == (dbkeys.size() - 1)) {
		hexDbkey.append(")");    
	} else {
		hexDbkey.append(",");    
	}

    stringBuffer.append(TEXT_1);
    stringBuffer.append( hexDbkey );
    
}

    return stringBuffer.toString();
  }
}
