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
package org.lh.dmlj.schema.editor.dictionary.tools.template;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.IDbkeyProvider;

public class BaseRecordSynonymListQueryTemplateTest extends AbstractQueryTestCase {

	private static IQueryTemplate template = new BaseRecordSynonymListQueryTemplate();
	
	@Test
	public void test_10_dbkeys_grouped_by_3() {		
		List<IDbkeyProvider> dbkeyProviders = getDbkeyProviders(10);		
		Dictionary dictionary = getDictionary("SYSDICT", 3);
		String sql = template.generate(new Object[] {dictionary, dbkeyProviders});		
		assertEquals(toLines(new File("testdata/ExpectedBaseRecordSynonymListQuery_10_3.txt")), 
					 toLines(sql));		
	}
	
	@Test
	public void test_1_dbkey_grouped_by_3() {		
		List<IDbkeyProvider> dbkeyProviders = getDbkeyProviders(1);		
		Dictionary dictionary = getDictionary("SYSDICT", 3);
		String sql = template.generate(new Object[] {dictionary, dbkeyProviders});		
		assertEquals(toLines(new File("testdata/ExpectedBaseRecordSynonymListQuery_1_3.txt")), 
					 toLines(sql));		
	}	

}
