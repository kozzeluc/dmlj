package org.lh.dmlj.schema.editor.dsl.template;

import org.lh.dmlj.schema.*;

public class AreaDslTemplate
{
  protected static String nl;
  public static synchronized AreaDslTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    AreaDslTemplate result = new AreaDslTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "    area { " + NL + "        name '";
  protected final String TEXT_2 = "'";
  protected final String TEXT_3 = NL + "        call '";
  protected final String TEXT_4 = " ";
  protected final String TEXT_5 = " ";
  protected final String TEXT_6 = "'";
  protected final String TEXT_7 = NL + "    }         ";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/**
 * Copyright (C) 2015  Luc Hermans
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

    
SchemaArea area = (SchemaArea) argument;

    stringBuffer.append(TEXT_1);
    stringBuffer.append( area.getName() );
    stringBuffer.append(TEXT_2);
    
for (AreaProcedureCallSpecification call : area.getProcedures()) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( call.getProcedure().getName() );
    stringBuffer.append(TEXT_4);
    stringBuffer.append( call.getCallTime() );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( call.getFunction().toString().replaceAll("_", " ") );
    stringBuffer.append(TEXT_6);
    
}

    stringBuffer.append(TEXT_7);
    return stringBuffer.toString();
  }
}
