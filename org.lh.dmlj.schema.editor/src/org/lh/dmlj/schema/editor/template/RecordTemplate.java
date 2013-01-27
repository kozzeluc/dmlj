package org.lh.dmlj.schema.editor.template;

import org.lh.dmlj.schema.*;

public class RecordTemplate
{
  protected static String nl;
  public static synchronized RecordTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    RecordTemplate result = new RecordTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "     ADD" + NL + "     RECORD NAME IS ";
  protected final String TEXT_2 = NL + "         SHARE STRUCTURE OF RECORD ? VERSION ?" + NL + "*+           SYNONYM OF PRIMARY RECORD ? VERSION ?         " + NL + "         RECORD ID IS ";
  protected final String TEXT_3 = NL + "         LOCATION MODE IS CALC USING ( ";
  protected final String TEXT_4 = " )" + NL + "             DUPLICATES ARE ";
  protected final String TEXT_5 = "         ";
  protected final String TEXT_6 = NL + "         LOCATION MODE IS VIA ";
  protected final String TEXT_7 = " SET";
  protected final String TEXT_8 = NL + "             DISPLACEMENT USING ";
  protected final String TEXT_9 = NL + "             DISPLACEMENT ";
  protected final String TEXT_10 = " PAGES";
  protected final String TEXT_11 = NL + "         LOCATION MODE IS DIRECT";
  protected final String TEXT_12 = NL + "         MINIMUM ROOT LENGTH IS ";
  protected final String TEXT_13 = " CHARACTERS         ";
  protected final String TEXT_14 = NL + "         MINIMUM FRAGMENT LENGTH IS ";
  protected final String TEXT_15 = " CHARACTERS         ";
  protected final String TEXT_16 = NL + "         CALL ";
  protected final String TEXT_17 = " ";
  protected final String TEXT_18 = " ";
  protected final String TEXT_19 = NL + "         WITHIN AREA ";
  protected final String TEXT_20 = " SUBAREA ";
  protected final String TEXT_21 = NL + "         WITHIN AREA ";
  protected final String TEXT_22 = " OFFSET ";
  protected final String TEXT_23 = " FOR ";
  protected final String TEXT_24 = " ";
  protected final String TEXT_25 = NL + "         WITHIN AREA ";
  protected final String TEXT_26 = " OFFSET 0 PERCENT FOR 100 PERCENT";
  protected final String TEXT_27 = "         " + NL + "*+       OWNER OF SET ";
  protected final String TEXT_28 = NL + "*+           NEXT DBKEY POSITION IS ";
  protected final String TEXT_29 = NL + "*+           PRIOR DBKEY POSITION IS ";
  protected final String TEXT_30 = NL + "*+       MEMBER OF SET ";
  protected final String TEXT_31 = NL + "*+           NEXT DBKEY POSITION IS ";
  protected final String TEXT_32 = NL + "*+           PRIOR DBKEY POSITION IS ";
  protected final String TEXT_33 = NL + "*+           INDEX DBKEY POSITION IS ";
  protected final String TEXT_34 = NL + "*+           INDEX DBKEY POSITION IS OMITTED";
  protected final String TEXT_35 = NL + "*+           OWNER DBKEY POSITION IS ";
  protected final String TEXT_36 = NL + "         .";
  protected final String TEXT_37 = NL;
  protected final String TEXT_38 = " ";
  protected final String TEXT_39 = NL;
  protected final String TEXT_40 = "    REDEFINES ";
  protected final String TEXT_41 = NL;
  protected final String TEXT_42 = "    PICTURE IS  ";
  protected final String TEXT_43 = NL;
  protected final String TEXT_44 = "    USAGE IS ";
  protected final String TEXT_45 = " ";
  protected final String TEXT_46 = NL;
  protected final String TEXT_47 = "    ELEMENT LENGTH IS ";
  protected final String TEXT_48 = NL;
  protected final String TEXT_49 = "    BIT LENGTH IS ";
  protected final String TEXT_50 = "    ";
  protected final String TEXT_51 = NL;
  protected final String TEXT_52 = "    POSITION IS ";
  protected final String TEXT_53 = NL;
  protected final String TEXT_54 = "    OCCURS 0 TO ";
  protected final String TEXT_55 = " TIMES DEPENDING ON ";
  protected final String TEXT_56 = NL;
  protected final String TEXT_57 = "    OCCURS ";
  protected final String TEXT_58 = " TIMES";
  protected final String TEXT_59 = NL;
  protected final String TEXT_60 = "    VALUE IS ( ? )";
  protected final String TEXT_61 = NL;
  protected final String TEXT_62 = "    .         ";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    

/*
This template will generate a record's DDL syntax.  Currently these issues 
exist :

- The "SHARE STRUCTURE OF RECORD" clause will always show question marks as we 
  don't (yet) have this information available in the model.
- The order in which the "OWNER OF SET" clauses are generated is in most cases  
  the same as the order in the schema compiler's output.
- The order in which the "MEMBER OF SET" clauses are generated is (depending on
  the import tool used to create the schema) in many cases different from the 
  order in the schema compiler's output; this is a schema generation issue, we 
  are just following the model here.
- The "BIT LENGTH" clause for elements with USAGE IS BIT is not always correct
  (the MASK attribute is always generated as "MASK IS X'FF'"; for group fields
  (elements without a picture), a MASK attribute will NEVER be generated.
- The "VALUE" clause (e.g. for condition names) will always show a question mark
  as we don't (yet) have this information available in the model.
- The "ELEMENT LENGTH" and "POSITION" clauses can show a different value than 
  the schema compiler output; this has to do with the at times weird way the 
  schema compiler handles elements participating in an array ("OCCURS" clause).
- We will always show the real element (synonym) name whereas the schema 
  compiler output shows the base element name.
  
See RecordTemplateTest for a JUnit testcase.
*/

SchemaRecord record = (SchemaRecord)argument;
String recordName;
if (record.getName().endsWith("_")) {
	recordName = 
		record.getName().substring(0, record.getName().length() - 1);
} else {
	recordName = record.getName();
}

    stringBuffer.append(TEXT_1);
    stringBuffer.append( recordName );
    stringBuffer.append(TEXT_2);
    stringBuffer.append( record.getId() );
    
if (record.getLocationMode() == LocationMode.CALC) {
    StringBuilder keyElements = new StringBuilder();
    for (KeyElement keyElement : record.getCalcKey().getElements()) {
        if (keyElements.length() > 0) {
            keyElements.append(' ');
        }
        keyElements.append(keyElement.getElement().getName());
    }

    stringBuffer.append(TEXT_3);
    stringBuffer.append( keyElements.toString() );
    stringBuffer.append(TEXT_4);
    stringBuffer.append( record.getCalcKey().getDuplicatesOption().toString().replaceAll("_", " ") );
    stringBuffer.append(TEXT_5);
                 
} else if (record.getLocationMode() == LocationMode.VIA) {
    String setName;
    if (record.getViaSpecification().getSet().getName().endsWith("_")) {
        setName = 
            record.getViaSpecification().getSet().getName().substring(0, record.getViaSpecification().getSet().getName().length() - 1);
    } else {
        setName =  record.getViaSpecification().getSet().getName();
    }

    stringBuffer.append(TEXT_6);
    stringBuffer.append( setName );
    stringBuffer.append(TEXT_7);
     
    if (record.getViaSpecification().getSymbolicDisplacementName() != null) {
         String symbolicName = 
             record.getViaSpecification().getSymbolicDisplacementName();

    stringBuffer.append(TEXT_8);
    stringBuffer.append( symbolicName );
                 
    } else if (record.getViaSpecification().getDisplacementPageCount() != null) {
         short pages = 
             record.getViaSpecification().getDisplacementPageCount().shortValue();

    stringBuffer.append(TEXT_9);
    stringBuffer.append( pages );
    stringBuffer.append(TEXT_10);
                 
    }
} else {

    stringBuffer.append(TEXT_11);
    
}
if (record.getMinimumRootLength() != null) {

    stringBuffer.append(TEXT_12);
    stringBuffer.append( record.getMinimumRootLength() );
    stringBuffer.append(TEXT_13);
    
}
if (record.getMinimumFragmentLength() != null) {

    stringBuffer.append(TEXT_14);
    stringBuffer.append( record.getMinimumFragmentLength() );
    stringBuffer.append(TEXT_15);
    
}
for (RecordProcedureCallSpecification procedureCall : record.getProcedures()) {

    stringBuffer.append(TEXT_16);
    stringBuffer.append( procedureCall.getProcedure().getName() );
    stringBuffer.append(TEXT_17);
    stringBuffer.append( procedureCall.getCallTime().toString() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append( procedureCall.getVerb().toString() );
    	
}
String areaName = record.getAreaSpecification().getArea().getName();
String symbolicSubareaName = 
    record.getAreaSpecification().getSymbolicSubareaName();
OffsetExpression offsetExpression = 
    record.getAreaSpecification().getOffsetExpression();
if (symbolicSubareaName != null) {

    stringBuffer.append(TEXT_19);
    stringBuffer.append( areaName );
    stringBuffer.append(TEXT_20);
    stringBuffer.append( symbolicSubareaName );
    
} else if (offsetExpression != null) { 
    String p;
    if (offsetExpression.getOffsetPageCount() != null) {
        p = offsetExpression.getOffsetPageCount() + " PAGES";
    } else if (offsetExpression.getOffsetPercent() != null) {
    	p = offsetExpression.getOffsetPercent() + " PERCENT";
    } else {
        p = "0";
    }
    String q;
    if (offsetExpression.getPercent() != null) {
        q = offsetExpression.getPercent() + " PERCENT";
    } else if (offsetExpression.getPageCount() != null) {
        q = offsetExpression.getPageCount() + " PAGES";
    } else {
        q = "100 PERCENT";
    }

    stringBuffer.append(TEXT_21);
    stringBuffer.append( areaName );
    stringBuffer.append(TEXT_22);
    stringBuffer.append( p );
    stringBuffer.append(TEXT_23);
    stringBuffer.append( q );
    stringBuffer.append(TEXT_24);
    
} else {

    stringBuffer.append(TEXT_25);
    stringBuffer.append( areaName );
    stringBuffer.append(TEXT_26);
    
}
for (OwnerRole role : record.getOwnerRoles()) {
    String setName;
    if (role.getSet().getName().endsWith("_")) {
        setName = 
            role.getSet().getName().substring(0, role.getSet().getName().length() - 1);
    } else {
        setName = role.getSet().getName();
    }

    stringBuffer.append(TEXT_27);
    stringBuffer.append( setName );
    stringBuffer.append(TEXT_28);
    stringBuffer.append( role.getNextDbkeyPosition() );
    
    if (role.getPriorDbkeyPosition() != null) {

    stringBuffer.append(TEXT_29);
    stringBuffer.append( role.getPriorDbkeyPosition() );
    
    }
}
for (MemberRole role : record.getMemberRoles()) {
    String setName;
    if (role.getSet().getName().endsWith("_")) {
        setName = 
            role.getSet().getName().substring(0, role.getSet().getName().length() - 1);
    } else {
        setName = role.getSet().getName();
    }

    stringBuffer.append(TEXT_30);
    stringBuffer.append( setName );
    
    if (role.getNextDbkeyPosition() != null) {

    stringBuffer.append(TEXT_31);
    stringBuffer.append( role.getNextDbkeyPosition() );
    
    }
    if (role.getPriorDbkeyPosition() != null) {

    stringBuffer.append(TEXT_32);
    stringBuffer.append( role.getPriorDbkeyPosition() );
    
    }
    if (role.getIndexDbkeyPosition() != null) {

    stringBuffer.append(TEXT_33);
    stringBuffer.append( role.getIndexDbkeyPosition() );
    
    } else if (role.getSet().getSystemOwner() != null) {

    stringBuffer.append(TEXT_34);
    
    }
    if (role.getOwnerDbkeyPosition() != null) {

    stringBuffer.append(TEXT_35);
    stringBuffer.append( role.getOwnerDbkeyPosition() );
    
    }
}

    stringBuffer.append(TEXT_36);
    
for (Element element : record.getElements()) {
    String left;
    if (element.getLevel() == 88) {
        left = "*+                   ";
    } else {
        StringBuilder p = new StringBuilder("*+   ");
        for (int i = element.getLevel(); i > 2; i--) {
            p.append("    ");
        }
        left = p.toString();
    }
    String level;
    if (element.getLevel() < 10) {
        level = "0" + element.getLevel();
    } else {
        level = String.valueOf(element.getLevel());
    }

    stringBuffer.append(TEXT_37);
    stringBuffer.append( left );
    stringBuffer.append( level );
    stringBuffer.append(TEXT_38);
    stringBuffer.append( element.getName() );
    
    if (element.getRedefines() != null) {

    stringBuffer.append(TEXT_39);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_40);
    stringBuffer.append( element.getRedefines().getName() );
    
    }
    if (element.getPicture() != null) {

    stringBuffer.append(TEXT_41);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_42);
    stringBuffer.append( element.getPicture() );
    
    }

    stringBuffer.append(TEXT_43);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_44);
    stringBuffer.append( Util.getUsageShortform(element.getUsage()) );
    stringBuffer.append(TEXT_45);
    
    if (element.getUsage() != Usage.CONDITION_NAME &&
        element.getUsage() != Usage.BIT) {

    stringBuffer.append(TEXT_46);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_47);
    stringBuffer.append( element.getLength() );
    
    }
    if (element.getUsage() == Usage.BIT) {
        String bitLength;        
        if (element.getLength() == 1) {
            bitLength = "8 ";
        } else {
            bitLength = String.valueOf(element.getLength() * 8);
        }
        String mask;
        if (element.getPicture() != null) {
            mask = "MASK IS X'FF'";
        } else {
            mask = "";
        }

    stringBuffer.append(TEXT_48);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_49);
    stringBuffer.append( bitLength );
    stringBuffer.append(TEXT_50);
    stringBuffer.append( mask );
    
    }

    stringBuffer.append(TEXT_51);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_52);
    stringBuffer.append( element.getOffset() + 1 );
    
    if (element.getOccursSpecification() != null) {
        OccursSpecification occurs = element.getOccursSpecification();
        if (occurs.getDependingOn() != null) {

    stringBuffer.append(TEXT_53);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_54);
    stringBuffer.append( occurs.getCount() );
    stringBuffer.append(TEXT_55);
    stringBuffer.append( occurs.getDependingOn().getName() );
      
        } else {

    stringBuffer.append(TEXT_56);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_57);
    stringBuffer.append( occurs.getCount() );
    stringBuffer.append(TEXT_58);
    
        }
    }
    if (element.getUsage() == Usage.CONDITION_NAME) {

    stringBuffer.append(TEXT_59);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_60);
    
    }

    stringBuffer.append(TEXT_61);
    stringBuffer.append( left );
    stringBuffer.append(TEXT_62);
    
}

    return stringBuffer.toString();
  }
}
