package org.lh.dmlj.schema.editor.template;

import java.util.*;
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
  protected final String TEXT_2 = NL + "         SHARE STRUCTURE OF RECORD ";
  protected final String TEXT_3 = " VERSION ";
  protected final String TEXT_4 = NL + "*+           SYNONYM OF PRIMARY RECORD ";
  protected final String TEXT_5 = " VERSION ";
  protected final String TEXT_6 = "         " + NL + "         RECORD ID IS ";
  protected final String TEXT_7 = NL;
  protected final String TEXT_8 = NL;
  protected final String TEXT_9 = NL + "               )";
  protected final String TEXT_10 = NL;
  protected final String TEXT_11 = ")";
  protected final String TEXT_12 = NL + "             DUPLICATES ARE ";
  protected final String TEXT_13 = "         ";
  protected final String TEXT_14 = NL + "         LOCATION MODE IS VIA ";
  protected final String TEXT_15 = " SET";
  protected final String TEXT_16 = NL + "             DISPLACEMENT USING ";
  protected final String TEXT_17 = NL + "             DISPLACEMENT ";
  protected final String TEXT_18 = " PAGES";
  protected final String TEXT_19 = NL + "         LOCATION MODE IS DIRECT";
  protected final String TEXT_20 = NL + "         MINIMUM ROOT LENGTH IS ";
  protected final String TEXT_21 = " CHARACTERS         ";
  protected final String TEXT_22 = NL + "         MINIMUM FRAGMENT LENGTH IS ";
  protected final String TEXT_23 = " CHARACTERS         ";
  protected final String TEXT_24 = NL + "         CALL ";
  protected final String TEXT_25 = " ";
  protected final String TEXT_26 = " ";
  protected final String TEXT_27 = NL + "         WITHIN AREA ";
  protected final String TEXT_28 = " SUBAREA ";
  protected final String TEXT_29 = NL + "         WITHIN AREA ";
  protected final String TEXT_30 = " OFFSET ";
  protected final String TEXT_31 = " FOR ";
  protected final String TEXT_32 = " ";
  protected final String TEXT_33 = NL + "         WITHIN AREA ";
  protected final String TEXT_34 = "         " + NL + "*+       OWNER OF SET ";
  protected final String TEXT_35 = NL + "*+           NEXT DBKEY POSITION IS ";
  protected final String TEXT_36 = NL + "*+           PRIOR DBKEY POSITION IS ";
  protected final String TEXT_37 = NL + "*+       MEMBER OF SET ";
  protected final String TEXT_38 = NL + "*+           NEXT DBKEY POSITION IS ";
  protected final String TEXT_39 = NL + "*+           PRIOR DBKEY POSITION IS ";
  protected final String TEXT_40 = NL + "*+           INDEX DBKEY POSITION IS ";
  protected final String TEXT_41 = NL + "*+           INDEX DBKEY POSITION IS OMITTED";
  protected final String TEXT_42 = NL + "*+           OWNER DBKEY POSITION IS ";
  protected final String TEXT_43 = NL + "         .";
  protected final String TEXT_44 = NL;
  protected final String TEXT_45 = "         ";

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
This template will generate a record's DDL syntax.  Currently these issues 
exist when entities are NOT sorted:

- The order in which the "OWNER OF SET" clauses are generated is in most cases  
  the same as the order in the schema compiler's output.
- The order in which the "MEMBER OF SET" clauses are generated is (depending on
  the import tool used to create the schema) in many cases different from the 
  order in the schema compiler's output; this is a schema generation issue, we 
  are just following the model here.
  
See RecordTemplateTest for a JUnit testcase.
*/

Object[] args = ((List<?>) argument).toArray();
SchemaRecord record = (SchemaRecord) args[0];
boolean sortSchemaEntities = ((Boolean) args[1]).booleanValue();

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
    stringBuffer.append( record.getSynonymName() );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(record.getSynonymVersion());
    
if (record.getBaseName() != null && 
    (!record.getBaseName().equals(record.getSynonymName()) ||
     record.getBaseVersion() != record.getSynonymVersion())) {

    stringBuffer.append(TEXT_4);
    stringBuffer.append( record.getBaseName() );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(record.getBaseVersion());
    
}

    stringBuffer.append(TEXT_6);
    stringBuffer.append( record.getId() );
    
if (record.getLocationMode() == LocationMode.CALC) {
    StringBuilder keyElements = new StringBuilder();
    keyElements.append("         LOCATION MODE IS CALC USING ( ");
    for (KeyElement keyElement : record.getCalcKey().getElements()) {
        String q = keyElement.getElement().getName();
        if (keyElements.length() + q.length() > 72) {

    stringBuffer.append(TEXT_7);
    stringBuffer.append( keyElements.toString() );
    
			keyElements = new StringBuilder();
			keyElements.append("               ");        
        }         
        keyElements.append(q);
        keyElements.append(' ');        
    }
    if (keyElements.length() + 1 > 72) {

    stringBuffer.append(TEXT_8);
    stringBuffer.append( keyElements.toString() );
    stringBuffer.append(TEXT_9);
    		
	} else {

    stringBuffer.append(TEXT_10);
    stringBuffer.append( keyElements.toString() );
    stringBuffer.append(TEXT_11);
            	    
	}

    stringBuffer.append(TEXT_12);
    stringBuffer.append( record.getCalcKey().getDuplicatesOption().toString().replaceAll("_", " ") );
    stringBuffer.append(TEXT_13);
                 
} else if (record.getLocationMode() == LocationMode.VIA) {
    String setName;
    if (record.getViaSpecification().getSet().getName().endsWith("_")) {
        setName = 
            record.getViaSpecification().getSet().getName().substring(0, record.getViaSpecification().getSet().getName().length() - 1);
    } else {
        setName =  record.getViaSpecification().getSet().getName();
    }

    stringBuffer.append(TEXT_14);
    stringBuffer.append( setName );
    stringBuffer.append(TEXT_15);
     
    if (record.getViaSpecification().getSymbolicDisplacementName() != null) {
         String symbolicName = 
             record.getViaSpecification().getSymbolicDisplacementName();

    stringBuffer.append(TEXT_16);
    stringBuffer.append( symbolicName );
                 
    } else if (record.getViaSpecification().getDisplacementPageCount() != null) {
         short pages = 
             record.getViaSpecification().getDisplacementPageCount().shortValue();

    stringBuffer.append(TEXT_17);
    stringBuffer.append( pages );
    stringBuffer.append(TEXT_18);
                 
    }
} else {

    stringBuffer.append(TEXT_19);
    
}
if (record.getMinimumRootLength() != null) {

    stringBuffer.append(TEXT_20);
    stringBuffer.append( record.getMinimumRootLength() );
    stringBuffer.append(TEXT_21);
    
}
if (record.getMinimumFragmentLength() != null) {

    stringBuffer.append(TEXT_22);
    stringBuffer.append( record.getMinimumFragmentLength() );
    stringBuffer.append(TEXT_23);
    
}
for (RecordProcedureCallSpecification procedureCall : record.getProcedures()) {

    stringBuffer.append(TEXT_24);
    stringBuffer.append( procedureCall.getProcedure().getName() );
    stringBuffer.append(TEXT_25);
    stringBuffer.append( procedureCall.getCallTime().toString() );
    stringBuffer.append(TEXT_26);
    stringBuffer.append( procedureCall.getVerb().toString() );
    	
}
String areaName = record.getAreaSpecification().getArea().getName();
String symbolicSubareaName = 
    record.getAreaSpecification().getSymbolicSubareaName();
OffsetExpression offsetExpression = 
    record.getAreaSpecification().getOffsetExpression();
if (symbolicSubareaName != null) {

    stringBuffer.append(TEXT_27);
    stringBuffer.append( areaName );
    stringBuffer.append(TEXT_28);
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

    stringBuffer.append(TEXT_29);
    stringBuffer.append( areaName );
    stringBuffer.append(TEXT_30);
    stringBuffer.append( p );
    stringBuffer.append(TEXT_31);
    stringBuffer.append( q );
    stringBuffer.append(TEXT_32);
    
} else {

    stringBuffer.append(TEXT_33);
    stringBuffer.append( areaName );
    
}
List<OwnerRole> ownerRoles = new ArrayList<>(record.getOwnerRoles());
if (sortSchemaEntities) {
	Collections.sort(ownerRoles, new Comparator<OwnerRole>() {
		public int compare(OwnerRole o1, OwnerRole o2) {
			return o1.getSet().getName().compareTo(o2.getSet().getName());
		}
	});
}
for (OwnerRole role : ownerRoles) {
    String setName;
    if (role.getSet().getName().endsWith("_")) {
        setName = 
            role.getSet().getName().substring(0, role.getSet().getName().length() - 1);
    } else {
        setName = role.getSet().getName();
    }

    stringBuffer.append(TEXT_34);
    stringBuffer.append( setName );
    stringBuffer.append(TEXT_35);
    stringBuffer.append( role.getNextDbkeyPosition() );
    
    if (role.getPriorDbkeyPosition() != null) {

    stringBuffer.append(TEXT_36);
    stringBuffer.append( role.getPriorDbkeyPosition() );
    
    }
}
List<String> setNames = new ArrayList<>();
for (MemberRole role : record.getMemberRoles()) {
	setNames.add(role.getSet().getName());
}
if (record.getSchema().getName().equals("IDMSNTWK") &&
    record.getSchema().getVersion() == 1 ||
    record.getSchema().getName().equals("EMPSCHM") &&
    record.getSchema().getVersion() == 100 ||
    sortSchemaEntities) {

    // always sort the "member of" comments for all schemas other than IDMSNTWK version 1 and 
    // EMPSCHM version 100, unless the user explicitly requested to sort schema entities
	Collections.sort(setNames);
}
MemberRole[] memberRoles = new MemberRole[setNames.size()];
for (MemberRole role : record.getMemberRoles()) {
	int i = setNames.indexOf(role.getSet().getName());
	memberRoles[i] = role;
}
for (MemberRole role : memberRoles) {
    String setName;
    if (role.getSet().getName().endsWith("_")) {
        setName = 
            role.getSet().getName().substring(0, role.getSet().getName().length() - 1);
    } else {
        setName = role.getSet().getName();
    }

    stringBuffer.append(TEXT_37);
    stringBuffer.append( setName );
    
    if (role.getNextDbkeyPosition() != null) {

    stringBuffer.append(TEXT_38);
    stringBuffer.append( role.getNextDbkeyPosition() );
    
    }
    if (role.getPriorDbkeyPosition() != null) {

    stringBuffer.append(TEXT_39);
    stringBuffer.append( role.getPriorDbkeyPosition() );
    
    }
    if (role.getIndexDbkeyPosition() != null) {

    stringBuffer.append(TEXT_40);
    stringBuffer.append( role.getIndexDbkeyPosition() );
    
    } else if (role.getSet().getSystemOwner() != null) {

    stringBuffer.append(TEXT_41);
    
    }
    if (role.getOwnerDbkeyPosition() != null) {

    stringBuffer.append(TEXT_42);
    stringBuffer.append( role.getOwnerDbkeyPosition() );
    
    }
}

    stringBuffer.append(TEXT_43);
    
ElementTemplate elementTemplate = new ElementTemplate();
for (Element element : record.getElements()) {
    String syntax = elementTemplate.generate(element);

    stringBuffer.append(TEXT_44);
    stringBuffer.append( syntax );
    stringBuffer.append(TEXT_45);
        
}

    return stringBuffer.toString();
  }
}
