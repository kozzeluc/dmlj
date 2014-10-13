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
package org.lh.dmlj.schema.editor.dictionary.tools;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.IDbkeyProvider;
import org.lh.dmlj.schema.editor.dictionary.tools.template.AbstractQueryTest;

public class JdbcToolsTest {

	@Test
	public void testGetSplitQueryDbkeyList() {
		
		List<IDbkeyProvider> dbkeyProviders = AbstractQueryTest.getDbkeyProviders(10);		
		Dictionary dictionary = AbstractQueryTest.getDictionary("SYSDICT", 3);
		
		// have the list of dbkey providers split into several lists, each containing at most 3
		// items (as specified in the mock dictionary above); each list will contain 3 elements, 
		// except for the last one that will contain only the last dbkey provider
		List<List<Long>> splitQueryDbkeyList = 
			JdbcTools.getSplitQueryDbkeyList(dbkeyProviders, dictionary);
		assertEquals(4, splitQueryDbkeyList.size());
		
		assertEquals(3, splitQueryDbkeyList.get(0).size());
		assertEquals(0, splitQueryDbkeyList.get(0).get(0).longValue());
		assertEquals(1, splitQueryDbkeyList.get(0).get(1).longValue());
		assertEquals(2, splitQueryDbkeyList.get(0).get(2).longValue());
		
		assertEquals(3, splitQueryDbkeyList.get(1).size());
		assertEquals(3, splitQueryDbkeyList.get(1).get(0).longValue());
		assertEquals(4, splitQueryDbkeyList.get(1).get(1).longValue());
		assertEquals(5, splitQueryDbkeyList.get(1).get(2).longValue());
		
		assertEquals(3, splitQueryDbkeyList.get(2).size());
		assertEquals(6, splitQueryDbkeyList.get(2).get(0).longValue());
		assertEquals(7, splitQueryDbkeyList.get(2).get(1).longValue());
		assertEquals(8, splitQueryDbkeyList.get(2).get(2).longValue());
		
		assertEquals(1, splitQueryDbkeyList.get(3).size());
		assertEquals(9, splitQueryDbkeyList.get(3).get(0).longValue());
		
	}
	
	@Test
	public void testGetSplitQueryDbkeyList_EmptyList() {
		List<IDbkeyProvider> dbkeyProviders = AbstractQueryTest.getDbkeyProviders(0);		
		Dictionary dictionary = AbstractQueryTest.getDictionary("SYSDICT", 3);
		List<List<Long>> splitQueryDbkeyList = 
			JdbcTools.getSplitQueryDbkeyList(dbkeyProviders, dictionary);
		assertEquals(0, splitQueryDbkeyList.size());
	}

}
