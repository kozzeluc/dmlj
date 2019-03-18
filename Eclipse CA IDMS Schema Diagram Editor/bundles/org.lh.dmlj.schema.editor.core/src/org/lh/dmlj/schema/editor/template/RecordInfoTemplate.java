package org.lh.dmlj.schema.editor.template;

import org.lh.dmlj.schema.editor.property.*;

public class RecordInfoTemplate
{
  protected static String nl;
  public static synchronized RecordInfoTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    RecordInfoTemplate result = new RecordInfoTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "<HTML>" + NL + "<TITLE>";
  protected final String TEXT_2 = "</TITLE>" + NL + "<BODY>" + NL + "<H2>";
  protected final String TEXT_3 = "</H2>" + NL + "<P>";
  protected final String TEXT_4 = "</P>";
  protected final String TEXT_5 = "\t" + NL + "<P><B>Record length: </B>";
  protected final String TEXT_6 = "</P>\t";
  protected final String TEXT_7 = "\t" + NL + "<P><B>Established by: </B>";
  protected final String TEXT_8 = "\t" + NL + "<P><B>Owner of: </B>";
  protected final String TEXT_9 = "\t" + NL + "<P><B>Member of: </B>";
  protected final String TEXT_10 = "\t" + NL + "<P><B>Location mode: </B>";
  protected final String TEXT_11 = "\t" + NL + "<P><B>Within area: </B>";
  protected final String TEXT_12 = "</P>" + NL + "<TABLE WIDTH=100%>" + NL + "<TR>" + NL + "<TD COLSPAN=3><HR></TD>" + NL + "</TR>" + NL + "<TR>" + NL + "<TH WIDTH=30% ALIGN=LEFT>Field</TH> " + NL + "<TH WIDTH=20% ALIGN=LEFT>Picture</TH> " + NL + "<TH WIDTH=50% ALIGN=LEFT>Description</TH>" + NL + "</TR>\t";
  protected final String TEXT_13 = NL + "<TR>" + NL + "<TD COLSPAN=3><HR></TD>" + NL + "</TR>" + NL + "<TR>" + NL + "<TD VALIGN=TOP>";
  protected final String TEXT_14 = "</TD>";
  protected final String TEXT_15 = NL + "<TD VALIGN=TOP>";
  protected final String TEXT_16 = NL + "<TD VALIGN=TOP></TD>";
  protected final String TEXT_17 = NL + "</TR>";
  protected final String TEXT_18 = NL + "<TR>" + NL + "<TD COLSPAN=3><HR></TD>" + NL + "</TR>" + NL + "</TABLE>";
  protected final String TEXT_19 = NL + "<P align=\"center\"><SMALL>Source: ";
  protected final String TEXT_20 = "</SMALL></P>";
  protected final String TEXT_21 = NL + "</BODY>" + NL + "</HTML>";

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

    
RecordInfoValueObject valueObject = (RecordInfoValueObject)argument;

    stringBuffer.append(TEXT_1);
    stringBuffer.append( valueObject.getRecordName() );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( valueObject.getRecordName() );
    stringBuffer.append(TEXT_3);
    stringBuffer.append( valueObject.getDescription() );
    stringBuffer.append(TEXT_4);
    
if (valueObject.getRecordLength() != null) {

    stringBuffer.append(TEXT_5);
    stringBuffer.append( valueObject.getRecordLength() );
    stringBuffer.append(TEXT_6);
    
}
if (valueObject.getEstablishedBy() != null) {

    stringBuffer.append(TEXT_7);
    stringBuffer.append( valueObject.getEstablishedBy() );
    stringBuffer.append(TEXT_6);
    
}
if (valueObject.getOwnerOf() != null) {

    stringBuffer.append(TEXT_8);
    stringBuffer.append( valueObject.getOwnerOf() );
    stringBuffer.append(TEXT_6);
    
}
if (valueObject.getMemberOf() != null) {

    stringBuffer.append(TEXT_9);
    stringBuffer.append( valueObject.getMemberOf() );
    stringBuffer.append(TEXT_6);
    
}
if (valueObject.getLocationMode() != null) {

    stringBuffer.append(TEXT_10);
    stringBuffer.append( valueObject.getLocationMode() );
    stringBuffer.append(TEXT_6);
    
}
if (valueObject.getWithinArea() != null) {

    stringBuffer.append(TEXT_11);
    stringBuffer.append( valueObject.getWithinArea() );
    stringBuffer.append(TEXT_12);
    
}
for (ElementInfoValueObject valueObject2 : valueObject.getElementInfoValueObjects()) {

    stringBuffer.append(TEXT_13);
    stringBuffer.append( valueObject2.getLevelAndElementName() );
    stringBuffer.append(TEXT_14);
    
if (valueObject2.getPictureAndUsage() != null) {

    stringBuffer.append(TEXT_15);
    stringBuffer.append( valueObject2.getPictureAndUsage() );
    stringBuffer.append(TEXT_14);
    
} else {

    stringBuffer.append(TEXT_16);
    
}
if (valueObject2.getDescription() != null) {

    stringBuffer.append(TEXT_15);
    stringBuffer.append( valueObject2.getDescription() );
    stringBuffer.append(TEXT_14);
    
} else {

    stringBuffer.append(TEXT_16);
    
}

    stringBuffer.append(TEXT_17);
    
}

    stringBuffer.append(TEXT_18);
    
if (valueObject.getDocumentName() != null) {

    stringBuffer.append(TEXT_19);
    stringBuffer.append( valueObject.getDocumentName() );
    stringBuffer.append(TEXT_20);
    
}

    stringBuffer.append(TEXT_21);
    return stringBuffer.toString();
  }
}
