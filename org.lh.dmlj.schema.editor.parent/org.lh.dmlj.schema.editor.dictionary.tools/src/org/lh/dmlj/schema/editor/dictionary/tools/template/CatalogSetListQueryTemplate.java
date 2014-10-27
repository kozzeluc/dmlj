package org.lh.dmlj.schema.editor.dictionary.tools.template;

import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;

public class CatalogSetListQueryTemplate implements IQueryTemplate {

  protected static String nl;
  public static synchronized CatalogSetListQueryTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    CatalogSetListQueryTemplate result = new CatalogSetListQueryTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "SELECT TABLE.ROWID AS TABLE_1050_ROWID," + NL + "       CONSTRAINT.ROWID AS CONSTRAINT_1029_ROWID," + NL + "       TABLE.AREA AS TABLE_1050_AREA," + NL + "\t   TABLE.LOCMODE AS TABLE_1050_LOCMODE," + NL + "\t   TABLE.NAME AS TABLE_1050_NAME," + NL + "\t   TABLE.TABLEID AS TABLE_1050_TABLEID," + NL + "\t   CONSTRAINT.COMPRESS AS CONSTRAINT_1029_COMPRESS," + NL + "\t   CONSTRAINT.DISPLACEMENT AS CONSTRAINT_1029_DISPLACEMENT," + NL + "\t   CONSTRAINT.IXBLKCONTAINS AS CONSTRAINT_1029_IXBLKCONTAINS," + NL + "\t   CONSTRAINT.NAME AS CONSTRAINT_1029_NAME," + NL + "\t   CONSTRAINT.NEXT AS CONSTRAINT_1029_NEXT," + NL + "\t   CONSTRAINT.OWNER AS CONSTRAINT_1029_OWNER," + NL + "\t   CONSTRAINT.PRIOR AS CONSTRAINT_1029_PRIOR," + NL + "\t   CONSTRAINT.REFNEXT AS CONSTRAINT_1029_REFNEXT," + NL + "\t   CONSTRAINT.REFPRIOR AS CONSTRAINT_1029_REFPRIOR," + NL + "\t   CONSTRAINT.SORTORDER AS CONSTRAINT_1029_SORTORDER," + NL + "\t   CONSTRAINT.TABLE AS CONSTRAINT_1029_TABLE," + NL + "\t   CONSTRAINT.TYPE AS CONSTRAINT_1029_TYPE," + NL + "\t   CONSTRAINT.UNIQUE AS CONSTRAINT_1029_UNIQUE " + NL + "FROM SYSTEM.TABLE AS TABLE_1050," + NL + "     SYSTEM.CONSTRAINT AS CONSTRAINT_1029 " + NL + "WHERE TABLE.SCHEMA = 'SYSTEM' AND" + NL + "      CONSTRAINT.REFSCHEMA = 'SYSTEM' AND" + NL + "      CONSTRAINT.REFTABLE = TABLE.NAME";

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

    stringBuffer.append(TEXT_1);
    return stringBuffer.toString();
  }
}