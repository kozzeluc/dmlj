package org.lh.dmlj.schema.editor.dsl.template;

import org.lh.dmlj.schema.*;

public class RecordDslTemplate
{
  protected static String nl;
  public static synchronized RecordDslTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    RecordDslTemplate result = new RecordDslTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "    record { " + NL + "        name '";
  protected final String TEXT_2 = "'" + NL + "        shareStructure '";
  protected final String TEXT_3 = " version ";
  protected final String TEXT_4 = "'" + NL + "        primaryRecord '";
  protected final String TEXT_5 = " version ";
  protected final String TEXT_6 = "'" + NL + "        recordId ";
  protected final String TEXT_7 = NL + "        direct";
  protected final String TEXT_8 = NL + "        calc {";
  protected final String TEXT_9 = NL + "        vsamCalc {";
  protected final String TEXT_10 = NL + "            elements '";
  protected final String TEXT_11 = "'" + NL + "            duplicates '";
  protected final String TEXT_12 = "'" + NL + "        }";
  protected final String TEXT_13 = NL + "        via {" + NL + "            set '";
  protected final String TEXT_14 = "'";
  protected final String TEXT_15 = NL + "            displacement '";
  protected final String TEXT_16 = "' (symbolic displacement)";
  protected final String TEXT_17 = NL + "            displacement ";
  protected final String TEXT_18 = " displacement pages";
  protected final String TEXT_19 = NL + "        }";
  protected final String TEXT_20 = NL + "        vsam";
  protected final String TEXT_21 = NL + "        area { " + NL + "            name '";
  protected final String TEXT_22 = "'";
  protected final String TEXT_23 = NL + "            subarea '";
  protected final String TEXT_24 = "'";
  protected final String TEXT_25 = NL + "            offsetPages ";
  protected final String TEXT_26 = NL + "            offsetPercent ";
  protected final String TEXT_27 = NL + "            pages ";
  protected final String TEXT_28 = NL + "            percent ";
  protected final String TEXT_29 = NL + "        }";
  protected final String TEXT_30 = NL + "        call '";
  protected final String TEXT_31 = " ";
  protected final String TEXT_32 = " ";
  protected final String TEXT_33 = "'";
  protected final String TEXT_34 = NL + "    }         ";

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

    
SchemaRecord record = (SchemaRecord) argument;

    stringBuffer.append(TEXT_1);
    stringBuffer.append( record.getName() );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( record.getSynonymName() );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(record.getSynonymVersion());
    stringBuffer.append(TEXT_4);
    stringBuffer.append( record.getBaseName() );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(record.getBaseVersion());
    stringBuffer.append(TEXT_6);
    stringBuffer.append(record.getId() );
    
if (record.getLocationMode() == LocationMode.DIRECT) {

    stringBuffer.append(TEXT_7);
    
} else if (record.getLocationMode() == LocationMode.CALC ||
           record.getLocationMode() == LocationMode.VSAM_CALC) {
    
    StringBuilder calcKeyElementNames = new StringBuilder();
    for (KeyElement keyElement : record.getCalcKey().getElements()) {
        if (calcKeyElementNames.length() > 0) {
            calcKeyElementNames.append(' ');
        }
        calcKeyElementNames.append(keyElement.getElement().getName());
    }
    if (record.getLocationMode() == LocationMode.CALC) {

    stringBuffer.append(TEXT_8);
    
    } else {

    stringBuffer.append(TEXT_9);
    
    }

    stringBuffer.append(TEXT_10);
    stringBuffer.append( calcKeyElementNames.toString() );
    stringBuffer.append(TEXT_11);
    stringBuffer.append( record.getCalcKey().getDuplicatesOption().toString().replaceAll("_", " ") );
    stringBuffer.append(TEXT_12);
    
} else if (record.getLocationMode() == LocationMode.VIA) {

    stringBuffer.append(TEXT_13);
    stringBuffer.append( record.getViaSpecification().getSet().getName() );
    stringBuffer.append(TEXT_14);
    
    if (record.getViaSpecification().getSymbolicDisplacementName() != null) {

    stringBuffer.append(TEXT_15);
    stringBuffer.append( record.getViaSpecification().getSymbolicDisplacementName() );
    stringBuffer.append(TEXT_16);
    
    } else if (record.getViaSpecification().getDisplacementPageCount() != null) {

    stringBuffer.append(TEXT_17);
    stringBuffer.append( record.getViaSpecification().getDisplacementPageCount().shortValue() );
    stringBuffer.append(TEXT_18);
    
    }

    stringBuffer.append(TEXT_19);
    
} else {

    stringBuffer.append(TEXT_20);
    
}

    stringBuffer.append(TEXT_21);
    stringBuffer.append( record.getAreaSpecification().getArea().getName() );
    stringBuffer.append(TEXT_22);
    
    if (record.getAreaSpecification().getSymbolicSubareaName() != null) {

    stringBuffer.append(TEXT_23);
    stringBuffer.append( record.getAreaSpecification().getSymbolicSubareaName() );
    stringBuffer.append(TEXT_24);
    
    } else if (record.getAreaSpecification().getOffsetExpression() != null) {
        OffsetExpression offsetExpression = record.getAreaSpecification().getOffsetExpression();
        if (offsetExpression.getOffsetPageCount() != null) {

    stringBuffer.append(TEXT_25);
    stringBuffer.append( offsetExpression.getOffsetPageCount().intValue() );
    
        } else if (offsetExpression.getOffsetPercent() != null) {

    stringBuffer.append(TEXT_26);
    stringBuffer.append( offsetExpression.getOffsetPercent().shortValue() );
            
        }
        if (offsetExpression.getPageCount() != null) {

    stringBuffer.append(TEXT_27);
    stringBuffer.append( offsetExpression.getPageCount().intValue() );
    
        } else if (offsetExpression.getPercent() != null) {

    stringBuffer.append(TEXT_28);
    stringBuffer.append( offsetExpression.getPercent().shortValue() );
    
        }
    }

    stringBuffer.append(TEXT_29);
    
for (RecordProcedureCallSpecification call : record.getProcedures()) {

    stringBuffer.append(TEXT_30);
    stringBuffer.append( call.getProcedure().getName() );
    stringBuffer.append(TEXT_31);
    stringBuffer.append( call.getCallTime() );
    stringBuffer.append(TEXT_32);
    stringBuffer.append( call.getVerb().toString().replaceAll("_", " ") );
    stringBuffer.append(TEXT_33);
    
}

    stringBuffer.append(TEXT_34);
    return stringBuffer.toString();
  }
}
