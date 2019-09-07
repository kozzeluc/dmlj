package org.lh.dmlj.schema.editor.template;

import org.lh.dmlj.schema.*;

public class ElementTemplate
{
  protected static String nl;
  public static synchronized ElementTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    ElementTemplate result = new ElementTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = " ";
  protected final String TEXT_3 = NL;
  protected final String TEXT_4 = "    REDEFINES ";
  protected final String TEXT_5 = "    PICTURE IS  ";
  protected final String TEXT_6 = "    USAGE IS ";
  protected final String TEXT_7 = "    ELEMENT LENGTH IS ";
  protected final String TEXT_8 = "    BIT LENGTH IS ";
  protected final String TEXT_9 = "    ";
  protected final String TEXT_10 = "    POSITION IS ";
  protected final String TEXT_11 = "    OCCURS 0 TO ";
  protected final String TEXT_12 = " TIMES DEPENDING ON ";
  protected final String TEXT_13 = "    OCCURS ";
  protected final String TEXT_14 = " TIMES";
  protected final String TEXT_15 = NL + "\t\t            )";
  protected final String TEXT_16 = ")";
  protected final String TEXT_17 = "    VALUE IS ( ";
  protected final String TEXT_18 = " )";
  protected final String TEXT_19 = "    .";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/**
 * Copyright (C) 2013  Luc Hermans
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
This template will generate an element's DDL syntax.
*/

Element element = (Element)argument;

String left;
if (element.getLevel() == 88) {
    left = "*+                   ";
} else {
    StringBuilder p = new StringBuilder("*+   ");
    for (int i = element.getLevel(); i > 2; i--) {
        p.append("    ");
    }
    if (p.length() > 21) {
        p.setLength(21);
    }
    left = p.toString();
}
String level;
if (element.getLevel() < 10) {
    level = "0" + element.getLevel();
} else {
    level = String.valueOf(element.getLevel());
}

    stringBuffer.append(TEXT_1);
    stringBuffer.append( left );
    stringBuffer.append( level );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( element.getBaseName() );
    
if (element.getRedefines() != null) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_4);
    stringBuffer.append( element.getRedefines().getBaseName() );
    
}
if (element.getPicture() != null) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( element.getPicture() );
    
}

    stringBuffer.append(TEXT_3);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( Util.getUsageShortform(element.getUsage()) );
    
if (element.getUsage() != Usage.CONDITION_NAME &&
    element.getUsage() != Usage.BIT) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_7);
    stringBuffer.append( element.getSyntaxLength() );
    
}
if (element.getUsage() == Usage.BIT) {
    String bitLength;        
    if (element.getLength() == 1) {
        bitLength = "8 ";
    } else {
        bitLength = String.valueOf(element.getLength() * 8);
    }
    String mask;
    if (element.getPicture() != null && element.getLength() == 1) {
        mask = "MASK IS X'FF'";
    } else {
        mask = "";
    }

    stringBuffer.append(TEXT_3);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_8);
    stringBuffer.append( bitLength );
    stringBuffer.append(TEXT_9);
    stringBuffer.append( mask );
    
}

    stringBuffer.append(TEXT_3);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_10);
    stringBuffer.append( element.getSyntaxPosition() );
    
if (element.getOccursSpecification() != null) {
    OccursSpecification occurs = element.getOccursSpecification();
    if (occurs.getDependingOn() != null) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_11);
    stringBuffer.append( occurs.getCount() );
    stringBuffer.append(TEXT_12);
    stringBuffer.append( occurs.getDependingOn().getName() );
      
    } else {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_13);
    stringBuffer.append( occurs.getCount() );
    stringBuffer.append(TEXT_14);
    
    }
	if (!occurs.getIndexElements().isEmpty()) {
	    StringBuilder indexElements = new StringBuilder();
	    indexElements.append(left);
	    indexElements.append("    INDEXED BY ( ");
	    for (IndexElement indexElement : occurs.getIndexElements()) {
	        String q = indexElement.getBaseName();
		    if (indexElements.length() + q.length() > 72) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( indexElements.toString() );
    
				indexElements = new StringBuilder();
				indexElements.append(left);
				indexElements.append("    ");        
		       }         
		    indexElements.append(q);
		    indexElements.append(' ');        
		}
		if (indexElements.length() + 1 > 72) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( indexElements.toString() );
    stringBuffer.append(TEXT_15);
    		
		} else {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( indexElements.toString() );
    stringBuffer.append(TEXT_16);
            	    
		}
	}        
}
if (element.getValue() != null) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_17);
    stringBuffer.append( element.getValue() );
    stringBuffer.append(TEXT_18);
    
}

    stringBuffer.append(TEXT_3);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_19);
    return stringBuffer.toString();
  }
}
