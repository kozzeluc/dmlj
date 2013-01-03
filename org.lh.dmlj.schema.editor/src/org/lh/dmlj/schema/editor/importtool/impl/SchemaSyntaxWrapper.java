package org.lh.dmlj.schema.editor.importtool.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SchemaSyntaxWrapper {

	private List<String> list = new ArrayList<>();
	private Properties   properties = new Properties();
	
	public SchemaSyntaxWrapper() {
		super();
	}
	
	public List<String> getList() {
		return list;
	}
	
	public Properties getProperties() {
		return properties;
	}

}