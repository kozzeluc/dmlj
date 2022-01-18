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

import java.util.Objects;
import java.util.Optional;

public class Rowid {
	private final String hexString;
	
	private long dbkey;
	private Optional<PageInformation> pageInformation;
	
	public static Rowid fromHexString(String hexString) {
		return new Rowid(hexString);
	}

	private Rowid(String hexString) {
		this.hexString = hexString;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rowid other = (Rowid) obj;
		return Objects.equals(hexString, other.hexString);
	}

	public long getDbkey() {
		if (!isInitialized()) {
			initialize();
		}
		return dbkey;
	}

	public String getHexString() {
		return hexString;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(hexString);
	}

	boolean isInitialized() {
		return pageInformation != null;
	}
	
	public Optional<PageInformation> getPageInformation() {
		if (!isInitialized()) {
			initialize();
		}
		return pageInformation;
	}
	
	private void initialize() {
		if (hexString.length() == 8) {
			dbkey = Long.parseLong(hexString, 16);
			pageInformation = Optional.empty();
		} else if (hexString.length() == 16) {
			dbkey = Long.parseLong(hexString.substring(0, 8), 16); 
			short pageGroup = Short.parseShort(hexString.substring(8, 12), 16);
			short radix = Short.parseShort(hexString.substring(12), 16);
			pageInformation = Optional.of(new PageInformation(pageGroup, radix));
		} else {
			throw new IllegalArgumentException("invalid hexString; cannot initialize: " + hexString);
		}
	}
	
	@Override
	public String toString() {
		return "X'" + hexString + "'";
	}
	
	public static class PageInformation {
		private final short pageGroup;
		private final short radix;
		
		private PageInformation(short pageGroup, short radix) {
			this.pageGroup = pageGroup;
			this.radix = radix;
		}
		
		public short getPageGroup() {
			return pageGroup;
		}

		public short getRadix() {
			return radix;
		}
		
	}

}
