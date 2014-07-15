package org.lh.dmlj.schema.editor.template;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import org.lh.dmlj.schema.*;

public class SchemaTemplate
{
  protected static String nl;
  public static synchronized SchemaTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    SchemaTemplate result = new SchemaTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "     ADD" + NL + "     SCHEMA NAME IS ";
  protected final String TEXT_2 = " VERSION IS ";
  protected final String TEXT_3 = NL + "         SCHEMA DESCRIPTION IS '";
  protected final String TEXT_4 = "'";
  protected final String TEXT_5 = "         " + NL + "         MEMO DATE IS ";
  protected final String TEXT_6 = NL + "         ASSIGN RECORD IDS FROM 1001" + NL + "         PUBLIC ACCESS IS ALLOWED FOR ALL";
  protected final String TEXT_7 = NL + "         COMMENTS" + NL + "             '";
  protected final String TEXT_8 = "'";
  protected final String TEXT_9 = NL + "       -     '";
  protected final String TEXT_10 = "'";
  protected final String TEXT_11 = NL + "         COMMENTS" + NL + "             '";
  protected final String TEXT_12 = "'" + NL + "       +     '";
  protected final String TEXT_13 = "'             ";
  protected final String TEXT_14 = NL + "       -     '";
  protected final String TEXT_15 = "'" + NL + "       +     '";
  protected final String TEXT_16 = "'       ";
  protected final String TEXT_17 = "         " + NL + "         .";
  protected final String TEXT_18 = NL;
  protected final String TEXT_19 = NL;
  protected final String TEXT_20 = "         ";
  protected final String TEXT_21 = NL;
  protected final String TEXT_22 = "         ";

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

    

/*
This template will generate a schema's DDL syntax.
*/

Object[] args = ((List<?>) argument).toArray();
Schema schema = (Schema) args[0];
boolean fullSyntax = ((Boolean) args[1]).booleanValue();
boolean sortSchemaEntities = ((Boolean) args[2]).booleanValue();

String description = schema.getDescription();
String memoDate = schema.getMemoDate();

    stringBuffer.append(TEXT_1);
    stringBuffer.append( schema.getName() );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( schema.getVersion() );
    
if (description != null) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( description );
    stringBuffer.append(TEXT_4);
    
}
if (memoDate != null) {

    stringBuffer.append(TEXT_5);
    stringBuffer.append( memoDate );
    
}

    stringBuffer.append(TEXT_6);
    
boolean first = true;
for (String line : schema.getComments()) {
    if (line.length() <= 56) {
        if (first) {
            first = false;

    stringBuffer.append(TEXT_7);
    stringBuffer.append( line );
    stringBuffer.append(TEXT_8);
    
        } else {

    stringBuffer.append(TEXT_9);
    stringBuffer.append( line );
    stringBuffer.append(TEXT_10);
    
        } 
    } else {
        if (first) {
            first = false;            

    stringBuffer.append(TEXT_11);
    stringBuffer.append( line.substring(0, 56) );
    stringBuffer.append(TEXT_12);
    stringBuffer.append( line.substring(56) );
    stringBuffer.append(TEXT_13);
    
        } else {

    stringBuffer.append(TEXT_14);
    stringBuffer.append( line.substring(0, 56) );
    stringBuffer.append(TEXT_15);
    stringBuffer.append( line.substring(56) );
    stringBuffer.append(TEXT_16);
            
        }
    }
}

    stringBuffer.append(TEXT_17);
    
if (fullSyntax) {
	AreaTemplate areaTemplate = new AreaTemplate();
	List<SchemaArea> areaList = new ArrayList<>(schema.getAreas());
	if (sortSchemaEntities) {
	    Collections.sort(areaList);
	}
	for (SchemaArea area : areaList) {    
    	String syntax = areaTemplate.generate(Arrays.asList(new Object[] {area, Boolean.valueOf(sortSchemaEntities)}));

    stringBuffer.append(TEXT_18);
    stringBuffer.append( syntax );
    
	} 
	RecordTemplate recordTemplate = new RecordTemplate();	
	List<SchemaRecord> recordList = new ArrayList<>(schema.getRecords());
	if (sortSchemaEntities) {
	    Collections.sort(recordList);
	}
	for (SchemaRecord record : recordList) {
	    String syntax = 
	    	recordTemplate.generate(Arrays.asList(new Object[] {record, Boolean.valueOf(sortSchemaEntities)}));

    stringBuffer.append(TEXT_19);
    stringBuffer.append( syntax );
    stringBuffer.append(TEXT_20);
    
	}
	SetTemplate setTemplate = new SetTemplate();
	List<Set> setList = new ArrayList<>(schema.getSets());
	if (sortSchemaEntities) {
	    Collections.sort(setList);
	}
	for (Set set : setList) {    
    	String syntax = setTemplate.generate(Arrays.asList(new Object[] {set, Boolean.valueOf(sortSchemaEntities)}));

    stringBuffer.append(TEXT_21);
    stringBuffer.append( syntax );
    stringBuffer.append(TEXT_22);
    
	}
}

    return stringBuffer.toString();
  }
}
