package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

import org.lh.dmlj.schema.editor.dictionary.tools.table.*;

public class BaseRecordSynonymQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized BaseRecordSynonymQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    BaseRecordSynonymQueryTemplate result = new BaseRecordSynonymQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "* This program is free software: you can redistribute it and/or modify it under the terms of the" + NL + " * GNU General Public License as published by the Free Software Foundation, either version 3 of the" + NL + " * License, or (at your option) any later version." + NL + " * " + NL + " * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without" + NL + " * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU" + NL + " * General Public License for more details." + NL + " * " + NL + " * You should have received a copy of the GNU General Public License along with this program.  If" + NL + " * not, see <http://www.gnu.org/licenses/>." + NL + " * " + NL + " * Contact information: kozzeluc@gmail.com." + NL + " */" + NL + "%>";
  protected final String TEXT_2 = NL + "SELECT ";
  protected final String TEXT_3 = ",";
  protected final String TEXT_4 = NL + "       ";
  protected final String TEXT_5 = " " + NL + "FROM \"";
  protected final String TEXT_6 = "\".\"SR-036\", " + NL + "     \"";
  protected final String TEXT_7 = "\".\"RCDSYN_079\"" + NL + "WHERE SR_036.ROWID = X'";
  protected final String TEXT_8 = "' AND " + NL + "      \"SR-RCDSYN\" AND" + NL + "      RCDSYN_079.RSYN_NAME_079 = SR_036.SR_NAM_036 AND" + NL + "      RCDSYN_079.RSYN_VER_079 = SR_036.RCD_VERS_036";

	public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
Object[] args = (Object[]) argument;
String sysdirlSchema = (String) args[0];
String hexDbkeySr_036 = (String) args[1];

    stringBuffer.append(TEXT_2);
    stringBuffer.append( Sr_036.COLUMNS );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(TEXT_4);
    stringBuffer.append( Rcdsyn_079.COLUMNS );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( sysdirlSchema );
    stringBuffer.append(TEXT_7);
    stringBuffer.append( hexDbkeySr_036 );
    stringBuffer.append(TEXT_8);
    return stringBuffer.toString();
  }
}