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
  protected final String TEXT_5 = NL;
  protected final String TEXT_6 = "    PICTURE IS  ";
  protected final String TEXT_7 = NL;
  protected final String TEXT_8 = "    USAGE IS ";
  protected final String TEXT_9 = NL;
  protected final String TEXT_10 = "    ELEMENT LENGTH IS ";
  protected final String TEXT_11 = NL;
  protected final String TEXT_12 = "    BIT LENGTH IS ";
  protected final String TEXT_13 = "    ";
  protected final String TEXT_14 = NL;
  protected final String TEXT_15 = "    POSITION IS ";
  protected final String TEXT_16 = NL;
  protected final String TEXT_17 = "    OCCURS 0 TO ";
  protected final String TEXT_18 = " TIMES DEPENDING ON ";
  protected final String TEXT_19 = NL;
  protected final String TEXT_20 = "    OCCURS ";
  protected final String TEXT_21 = " TIMES";
  protected final String TEXT_22 = NL;
  protected final String TEXT_23 = NL;
  protected final String TEXT_24 = NL + "\t\t            )";
  protected final String TEXT_25 = NL;
  protected final String TEXT_26 = ")";
  protected final String TEXT_27 = NL;
  protected final String TEXT_28 = "    VALUE IS ( ";
  protected final String TEXT_29 = " )";
  protected final String TEXT_30 = NL;
  protected final String TEXT_31 = "    .";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    

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

    stringBuffer.append(TEXT_5);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( element.getPicture() );
    
}

    stringBuffer.append(TEXT_7);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_8);
    stringBuffer.append( Util.getUsageShortform(element.getUsage()) );
    
if (element.getUsage() != Usage.CONDITION_NAME &&
    element.getUsage() != Usage.BIT) {

    stringBuffer.append(TEXT_9);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_10);
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

    stringBuffer.append(TEXT_11);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_12);
    stringBuffer.append( bitLength );
    stringBuffer.append(TEXT_13);
    stringBuffer.append( mask );
    
}

    stringBuffer.append(TEXT_14);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_15);
    stringBuffer.append( element.getSyntaxPosition() );
    
if (element.getOccursSpecification() != null) {
    OccursSpecification occurs = element.getOccursSpecification();
    if (occurs.getDependingOn() != null) {

    stringBuffer.append(TEXT_16);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_17);
    stringBuffer.append( occurs.getCount() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( occurs.getDependingOn().getName() );
      
    } else {

    stringBuffer.append(TEXT_19);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_20);
    stringBuffer.append( occurs.getCount() );
    stringBuffer.append(TEXT_21);
    
    }
	if (!occurs.getIndexElements().isEmpty()) {
	    StringBuilder indexElements = new StringBuilder();
	    indexElements.append(left);
	    indexElements.append("    INDEXED BY ( ");
	    for (IndexElement indexElement : occurs.getIndexElements()) {
	        String q = indexElement.getBaseName();
		    if (indexElements.length() + q.length() > 72) {

    stringBuffer.append(TEXT_22);
    stringBuffer.append( indexElements.toString() );
    
				indexElements = new StringBuilder();
				indexElements.append(left);
				indexElements.append("    ");        
		       }         
		    indexElements.append(q);
		    indexElements.append(' ');        
		}
		if (indexElements.length() + 1 > 72) {

    stringBuffer.append(TEXT_23);
    stringBuffer.append( indexElements.toString() );
    stringBuffer.append(TEXT_24);
    		
		} else {

    stringBuffer.append(TEXT_25);
    stringBuffer.append( indexElements.toString() );
    stringBuffer.append(TEXT_26);
            	    
		}
	}        
}
if (element.getValue() != null) {

    stringBuffer.append(TEXT_27);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_28);
    stringBuffer.append( element.getValue() );
    stringBuffer.append(TEXT_29);
    
}

    stringBuffer.append(TEXT_30);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_31);
    return stringBuffer.toString();
  }
}
