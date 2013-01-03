package org.lh.dmlj.schema.editor.importtool.impl;

import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;

public class ElementDataCollector 
	implements IElementDataCollector<SchemaSyntaxWrapper> {

	private static String getFullName(String elementName, 
									  SchemaSyntaxWrapper context) {
		
		if (elementName.equals("FILLER")) {
			return elementName;
		}
		
		StringBuilder p = new StringBuilder();
		if (context.getProperties().containsKey("prefix")) {
			String prefix = context.getProperties().getProperty("prefix");
			p.append(prefix);
		}
		p.append(elementName);
		if (context.getProperties().containsKey("suffix")) {
			String suffix = context.getProperties().getProperty("suffix");
			p.append(suffix);
		}		
		return p.toString();	
		
	}
	
	public ElementDataCollector() {
		super();
	}

	@Override
	public String getDependsOnElementName(SchemaSyntaxWrapper context) {
		return null;
	}

	@Override
	public boolean getIsNullable(SchemaSyntaxWrapper context) {
		return false;
	}

	@Override
	public short getLevel(SchemaSyntaxWrapper context) {
		String p = context.getList().get(0).substring(2).trim();		
		return Short.valueOf(p.substring(0, p.indexOf(" "))).shortValue();
	}

	@Override
	public String getName(SchemaSyntaxWrapper context) {
		String p = context.getList().get(0).trim();
		return getFullName(p.substring(p.lastIndexOf(" ") + 1), context);
	}

	@Override
	public short getOccurrenceCount(SchemaSyntaxWrapper context) {
		return 0;
	}

	@Override
	public String getPicture(SchemaSyntaxWrapper context) {
		return null;
	}

	@Override
	public String getRedefinedElementName(SchemaSyntaxWrapper context) {
		return null;
	}

	@Override
	public Usage getUsage(SchemaSyntaxWrapper context) {
		return null;
	}
	
}