/**
 * Copyright (C) 2013  Luc Hermans
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
package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class DataEntryContext implements IDataEntryContext {

	private Map<String, Object> map = new HashMap<>();
	
	public DataEntryContext() {
		super();
	}
	
	@Override
	public void clear() {
		map.clear();		
	}

	@Override
	public void clearAttribute(String key) {		
		if (map.containsKey(key)) {
			map.remove(key);
		}
	}

	@Override
	public boolean containsAttribute(String key) {
		return map.containsKey(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttribute(String key) {
		if (map.containsKey(key)) {
			return (T) map.get(key);
		} else {
			return null;
		}
	}

	@Override
	public List<String> getAttributeNames() {
		List<String> attributeNames = new ArrayList<>(map.keySet());		
		Collections.sort(attributeNames);
		return attributeNames;
	}

	@Override
	public void setAttribute(String key, Object value) {
		map.put(key, value);		
	}

}
