package org.lh.dmlj.schema.editor.template;

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
  protected final String TEXT_6 = NL + "         ASSIGN RECORD IDS FROM 1001" + NL + "         PUBLIC ACCESS IS ALLOWED FOR ALL" + NL + "         .";
  protected final String TEXT_7 = NL;
  protected final String TEXT_8 = NL;
  protected final String TEXT_9 = "         ";
  protected final String TEXT_10 = NL;
  protected final String TEXT_11 = "         ";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    

/*
This template will generate a schema's DDL syntax.
*/

Schema schema = (Schema)argument;
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
    
AreaTemplate areaTemplate = new AreaTemplate();
for (SchemaArea area : schema.getAreas()) {    
    String syntax = areaTemplate.generate(area);

    stringBuffer.append(TEXT_7);
    stringBuffer.append( syntax );
    
} 
RecordTemplate recordTemplate = new RecordTemplate();
for (SchemaRecord record : schema.getRecords()) {    
    String syntax = recordTemplate.generate(record);

    stringBuffer.append(TEXT_8);
    stringBuffer.append( syntax );
    stringBuffer.append(TEXT_9);
    
}
SetTemplate setTemplate = new SetTemplate();
for (Set set : schema.getSets()) {    
    String syntax = setTemplate.generate(set);

    stringBuffer.append(TEXT_10);
    stringBuffer.append( syntax );
    stringBuffer.append(TEXT_11);
    
}

    return stringBuffer.toString();
  }
}
