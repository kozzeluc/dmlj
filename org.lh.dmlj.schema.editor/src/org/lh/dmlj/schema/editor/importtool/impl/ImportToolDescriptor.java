package org.lh.dmlj.schema.editor.importtool.impl;

import java.util.ArrayList;
import java.util.List;

public class ImportToolDescriptor {

	private String 						  description;
	private String 						  id;
	private String 						  implementingClass;
	private String 						  name;	
	private List<DataEntryPageDescriptor> pageDescriptors = 
		new ArrayList<>();
	private String 						  pluginId;
	private String 						  source;
	
	public ImportToolDescriptor() {
		super();
	}
	
	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}

	public String getImplementingClass() {
		return implementingClass;
	}

	public String getName() {
		return name;
	}	
	
	public List<DataEntryPageDescriptor> getPageDescriptors() {
		return pageDescriptors;
	}

	public String getPluginId() {
		return pluginId;
	}

	public String getSource() {
		return source;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setImplementingClass(String implementingClass) {
		this.implementingClass = implementingClass;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		StringBuilder p = new StringBuilder();
		p.append("ImportToolDescriptor:");
		p.append(" defining plug-in:" + pluginId);
		p.append(" id=" + id);
		p.append(" name=" + name);
		p.append(" source=" + source);
		p.append(" class=" + implementingClass);
		p.append(" description=" + description);		
		for (DataEntryPageDescriptor pageDescriptor : pageDescriptors) {
			p.append("\n ");
			p.append(pageDescriptor.toString());
		}
		return p.toString();
	}	
	
}