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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RowidTest {

	@Test
	public void fromShortHexStringHasPageInformationAfterInitialization() {
		Rowid rowid = Rowid.fromHexString("06370308002D0008");
		assertEquals("06370308002D0008", rowid.getHexString());
		assertFalse(rowid.isInitialized());
		assertEquals(104268552L, rowid.getDbkey());
		assertTrue(rowid.isInitialized());
		assertNotNull(rowid.getPageInformation());
		assertTrue(rowid.getPageInformation().isPresent());
		assertEquals(45, rowid.getPageInformation().get().getPageGroup());
		assertEquals(8, rowid.getPageInformation().get().getRadix());
		assertEquals("X'06370308002D0008'", rowid.toString());
	}
	
	@Test
	public void fromLongHexStringHasNoPageInformationAfterInitialization() {
		Rowid rowid = Rowid.fromHexString("06370308");
		assertEquals("06370308", rowid.getHexString());
		assertFalse(rowid.isInitialized());
		assertNotNull(rowid.getPageInformation());
		assertTrue(rowid.isInitialized());
		assertEquals(104268552L, rowid.getDbkey());
		assertFalse(rowid.getPageInformation().isPresent());
		assertEquals("X'06370308'", rowid.toString());
	}
	
	@Test
	public void fromInvalidHexStringThrowsIllegalArgumentExceptionWhenInitializingTriggeredByGetDbkey() {
		Rowid rowid = Rowid.fromHexString("0637030");
		assertThrows(IllegalArgumentException.class, () -> rowid.getDbkey() );
	}
	
	@Test
	public void fromInvalidHexStringThrowsIllegalArgumentExceptionWhenInitializingTriggeredByGetPageInformation() {
		Rowid rowid = Rowid.fromHexString("0637030801");
		assertThrows(IllegalArgumentException.class, () -> rowid.getPageInformation() );
	}
	
	@Test
	public void twoRowidsAreEqualWhenTheyHaveTheSameHexString() {
		Rowid rowid1 = Rowid.fromHexString("0637030801");
		Rowid rowid2 = Rowid.fromHexString("0637030801");
		assertTrue(rowid1.equals(rowid2));
		assertTrue(rowid2.equals(rowid1));
	}
	
	@Test
	public void theSameRowidsEqualsItself() {
		Rowid rowid = Rowid.fromHexString("0637030801");		
		assertTrue(rowid.equals(rowid));		
	}
	
	@Test
	public void aRowidNeverEqualsNull() {
		Rowid rowid = Rowid.fromHexString("0637030801");		
		assertFalse(rowid.equals(null));		
	}
	
	@Test
	public void aRowidNeverEqualsAnObjectOfAnotherClass() {
		Rowid rowid = Rowid.fromHexString("0637030801");
		String string = "a rowid never equals an object of another class";
		assertFalse(rowid.equals(string));		
	}
	
	@Test
	public void twoRowidsAreNotEqualWhenTheyHaveDifferentHexStrings() {
		Rowid rowid1 = Rowid.fromHexString("0637030801");
		Rowid rowid2 = Rowid.fromHexString("0637030802");
		assertFalse(rowid1.equals(rowid2));
		assertFalse(rowid2.equals(rowid1));
	}
	
	@Test
	public void twoRowidsHaveTheSameHashcodeWhenTheyHaveTheSameHexString() {
		Rowid rowid1 = Rowid.fromHexString("0637030801");
		Rowid rowid2 = Rowid.fromHexString("0637030801");
		assertTrue(rowid1.hashCode() == rowid2.hashCode());		
	}
	
	@Test
	public void twoRowidsMayHaveDifferentHashcodesWhenTheyHaveDifferentHexStrings() {
		Rowid rowid1 = Rowid.fromHexString("0637030801");
		Rowid rowid2 = Rowid.fromHexString("0637030802");
		assertFalse(rowid1.hashCode() == rowid2.hashCode());
	}
	
}
