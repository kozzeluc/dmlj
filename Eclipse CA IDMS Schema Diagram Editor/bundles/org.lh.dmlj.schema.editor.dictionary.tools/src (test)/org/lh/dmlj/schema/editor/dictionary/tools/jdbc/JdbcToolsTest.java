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
package org.lh.dmlj.schema.editor.dictionary.tools.jdbc;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.IRowidProvider;
import org.lh.dmlj.schema.editor.dictionary.tools.template.AbstractQueryTestCase;

public class JdbcToolsTest {

	@Test
	public void testGetSplitQueryRowidList() {
		
		List<IRowidProvider> rowidProviders = AbstractQueryTestCase.getRowidProviders(10, true);		
		Dictionary dictionary = AbstractQueryTestCase.getDictionary("SYSDICT", 3);
		
		// have the list of rowid providers split into several lists, each containing at most 3
		// items (as specified in the mock dictionary above); each list will contain 3 elements, 
		// except for the last one that will contain only the last rowid provider
		List<List<Rowid>> splitQueryRowidList = JdbcTools.getSplitQueryRowidList(rowidProviders, dictionary);
		assertEquals(4, splitQueryRowidList.size());
		
		assertEquals(3, splitQueryRowidList.get(0).size());
		assertEquals(0, splitQueryRowidList.get(0).get(0).getDbkey());
		assertEquals(1, splitQueryRowidList.get(0).get(1).getDbkey());
		assertEquals(2, splitQueryRowidList.get(0).get(2).getDbkey());
		
		assertEquals(3, splitQueryRowidList.get(1).size());
		assertEquals(3, splitQueryRowidList.get(1).get(0).getDbkey());
		assertEquals(4, splitQueryRowidList.get(1).get(1).getDbkey());
		assertEquals(5, splitQueryRowidList.get(1).get(2).getDbkey());
		
		assertEquals(3, splitQueryRowidList.get(2).size());
		assertEquals(6, splitQueryRowidList.get(2).get(0).getDbkey());
		assertEquals(7, splitQueryRowidList.get(2).get(1).getDbkey());
		assertEquals(8, splitQueryRowidList.get(2).get(2).getDbkey());
		
		assertEquals(1, splitQueryRowidList.get(3).size());
		assertEquals(9, splitQueryRowidList.get(3).get(0).getDbkey());
		
	}
	
	@Test
	public void testGetSplitQueryRowidList_EmptyList() {
		List<IRowidProvider> rowidProviders = AbstractQueryTestCase.getRowidProviders(0, true);		
		Dictionary dictionary = AbstractQueryTestCase.getDictionary("SYSDICT", 3);
		List<List<Rowid>> splitQueryRowidList = JdbcTools.getSplitQueryRowidList(rowidProviders, dictionary);
		assertEquals(0, splitQueryRowidList.size());
	}

}
