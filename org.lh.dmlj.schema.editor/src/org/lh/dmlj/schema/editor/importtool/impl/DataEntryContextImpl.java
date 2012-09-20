package org.lh.dmlj.schema.editor.importtool.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class DataEntryContextImpl implements IDataEntryContext {

	private Map<String, Object> map = new HashMap<>();
	
	public DataEntryContextImpl() {
		super();
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