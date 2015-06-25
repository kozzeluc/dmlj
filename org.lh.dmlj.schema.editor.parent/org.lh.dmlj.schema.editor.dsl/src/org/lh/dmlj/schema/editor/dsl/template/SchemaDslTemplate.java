package org.lh.dmlj.schema.editor.dsl.template;

import org.lh.dmlj.schema.*;

public class SchemaDslTemplate
{
  protected static String nl;
  public static synchronized SchemaDslTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    SchemaDslTemplate result = new SchemaDslTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "schema {" + NL + "    name '";
  protected final String TEXT_2 = "'" + NL + "    version ";
  protected final String TEXT_3 = NL + "    description '";
  protected final String TEXT_4 = "'";
  protected final String TEXT_5 = NL + "    memoDate '";
  protected final String TEXT_6 = "'";
  protected final String TEXT_7 = NL + "    comments '";
  protected final String TEXT_8 = "'";
  protected final String TEXT_9 = NL + "    diagram {";
  protected final String TEXT_10 = NL + "        label {" + NL + "            description '";
  protected final String TEXT_11 = "'" + NL + "            x ";
  protected final String TEXT_12 = NL + "            y ";
  protected final String TEXT_13 = NL + "            width ";
  protected final String TEXT_14 = NL + "            height ";
  protected final String TEXT_15 = NL + "        }";
  protected final String TEXT_16 = NL + "        zoom ";
  protected final String TEXT_17 = NL + "        showRulersAndGuides";
  protected final String TEXT_18 = " " + NL + "        showGrid";
  protected final String TEXT_19 = NL + "        snapToGuides";
  protected final String TEXT_20 = NL + "        snapToGrid";
  protected final String TEXT_21 = NL + "        snapToGeometry";
  protected final String TEXT_22 = NL + "    }";
  protected final String TEXT_23 = NL;
  protected final String TEXT_24 = NL + "}         ";

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

    
Schema schema = (Schema) argument;
DiagramData diagramData = schema.getDiagramData();
AreaDslTemplate areaTemplate = new AreaDslTemplate();
RecordDslTemplate recordTemplate = new RecordDslTemplate();

    stringBuffer.append(TEXT_1);
    stringBuffer.append( schema.getName() );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( schema.getVersion() );
    
if (schema.getDescription() != null) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append( schema.getDescription() );
    stringBuffer.append(TEXT_4);
    
}
if (schema.getMemoDate() != null) {

    stringBuffer.append(TEXT_5);
    stringBuffer.append( schema.getMemoDate() );
    stringBuffer.append(TEXT_6);
    
}
if (schema.getComments() != null && !schema.getComments().isEmpty()) {
    for (String comment : schema.getComments()) {

    stringBuffer.append(TEXT_7);
    stringBuffer.append( comment );
    stringBuffer.append(TEXT_8);
    
    }
}
if (diagramData != null) {

    stringBuffer.append(TEXT_9);
    
    if (diagramData.getLabel() != null) {

    stringBuffer.append(TEXT_10);
    stringBuffer.append( diagramData.getLabel().getDescription() );
    stringBuffer.append(TEXT_11);
    stringBuffer.append( diagramData.getLabel().getDiagramLocation().getX() );
    stringBuffer.append(TEXT_12);
    stringBuffer.append( diagramData.getLabel().getDiagramLocation().getY() );
    stringBuffer.append(TEXT_13);
    stringBuffer.append( diagramData.getLabel().getWidth() );
    stringBuffer.append(TEXT_14);
    stringBuffer.append( diagramData.getLabel().getHeight() );
    stringBuffer.append(TEXT_15);
    
    }

    stringBuffer.append(TEXT_16);
    stringBuffer.append( (int) (diagramData.getZoomLevel() * 100.0) );
    
    if (diagramData.isShowRulers()) {

    stringBuffer.append(TEXT_17);
    
}
    if (diagramData.isShowGrid()) {

    stringBuffer.append(TEXT_18);
    
}
    if (diagramData.isSnapToGuides()) {

    stringBuffer.append(TEXT_19);
    
    }
    if (diagramData.isSnapToGrid()) {

    stringBuffer.append(TEXT_20);
    
    }
    if (diagramData.isSnapToGeometry()) {

    stringBuffer.append(TEXT_21);
    
    }

    stringBuffer.append(TEXT_22);
    
}

    stringBuffer.append(TEXT_23);
    
for (SchemaArea area : schema.getAreas()) {
    stringBuffer.append(areaTemplate.generate(area));
    stringBuffer.append('\n');
}
for (SchemaRecord record : schema.getRecords()) {
    stringBuffer.append(recordTemplate.generate(record));
    stringBuffer.append('\n');
}

    stringBuffer.append(TEXT_24);
    return stringBuffer.toString();
  }
}
