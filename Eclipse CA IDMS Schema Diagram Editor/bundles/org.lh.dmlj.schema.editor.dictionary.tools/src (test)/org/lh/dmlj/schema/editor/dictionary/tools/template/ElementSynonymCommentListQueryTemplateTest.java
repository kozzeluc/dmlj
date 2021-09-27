/**
 * Copyright (C) 2021  Luc Hermans
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
import org.lh.dmlj.schema.editor.dictionary.tools.table.IRowidProvider;

public class ElementSynonymCommentListQueryTemplateTest extends AbstractQueryTestCase {

	private static IQueryTemplate template = new ElementSynonymCommentListQueryTemplate();
	
	@Test
	public void test_10_rowids_grouped_by_3() {		
		List<IRowidProvider> rowidProviders = getRowidProviders(10, true);		
		Dictionary dictionary = getDictionary("SYSDICT", 3);
		String sql = template.generate(new Object[] {dictionary, rowidProviders});		
		assertEquals(toLines(new File("testdata/ElementSynonymCommentListQuery_10_3.txt")), toLines(sql));		
	}
	
	@Test
	public void test_1_rowid_grouped_by_3() {		
		List<IRowidProvider> rowidProviders = getRowidProviders(1, true);		
		Dictionary dictionary = getDictionary("SYSDICT", 3);
		String sql = template.generate(new Object[] {dictionary, rowidProviders});		
		assertEquals(toLines(new File("testdata/ElementSynonymCommentListQuery_1_3.txt")), toLines(sql));		
	}

}
