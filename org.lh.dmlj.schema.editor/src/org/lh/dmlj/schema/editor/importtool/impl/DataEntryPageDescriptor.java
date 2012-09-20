package org.lh.dmlj.schema.editor.importtool.impl;

public class DataEntryPageDescriptor {

	private String id;
	private String implementingClass;
	private String name;
	private String message;		

	public DataEntryPageDescriptor() {
		super();
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

	public String getMessage() {
		return message;
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

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		StringBuilder p = new StringBuilder();
		p.append("DataEntryPageDescriptor:");			
		p.append(" id=" + id);
		p.append(" name=" + name);
		p.append(" message=" + message);
		p.append(" class=" + implementingClass);		
		return p.toString();
	}	
	
}