package org.lh.dmlj.schema.editor.importtool.syntax;

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
	public String getBaseName(SchemaSyntaxWrapper context) {
		String p = context.getLines().get(0).trim();
		return p.substring(p.lastIndexOf(" ") + 1);
	}

	@Override
	public String getDependsOnElementName(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			if (line.indexOf(" OCCURS ") > -1 &&
				line.indexOf(" TIMES ") > -1 &&
				line.indexOf(" DEPENDING ON ") > -1) {
				
				int i = line.indexOf(" DEPENDING ON ");
				String elementName = line.substring(i + 14).trim();
				return getFullName(elementName, context);
			}
		}
		return null;
	}

	@Override
	public boolean getIsNullable(SchemaSyntaxWrapper context) {
		// this is only applicable for catalog tables, which we don't support
		return false;
	}

	@Override
	public short getLevel(SchemaSyntaxWrapper context) {
		String p = context.getLines().get(0).substring(2).trim();		
		return Short.valueOf(p.substring(0, p.indexOf(" "))).shortValue();
	}

	@Override
	public String getName(SchemaSyntaxWrapper context) {
		String p = context.getLines().get(0).trim();
		return getFullName(p.substring(p.lastIndexOf(" ") + 1), context);
	}

	@Override
	public short getOccurrenceCount(SchemaSyntaxWrapper context) {
		// examples:
		// *+           OCCURS 48 TIMES
		// *+       OCCURS 0 TO 500 TIMES DEPENDING ON SRHVSIZE-139
		for (String line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			if (line.indexOf(" OCCURS ") > 1 &&
				line.indexOf(" TIMES") > 1) {
			
				int i = line.indexOf(" OCCURS ");
				int j = line.indexOf(" TIMES", i);
				String p = line.substring(i + 8, j).trim();
				if (line.indexOf(" TIMES DEPENDING ON ") < 0) {
					// not an OCCURS DEPENDING ON
					return Short.valueOf(p).shortValue();
				} else {
					// OCCURS DEPENDING ON
					int k = p.indexOf(" TO ");
					return Short.valueOf(p.substring(k + 4));
				}
			}
		}
		return 0;
	}

	@Override
	public String getPicture(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			int i = line.indexOf(" PICTURE IS ");
			if (i > -1) {
				return line.substring(i + 12).trim();
			}
		}
		return null;
	}

	@Override
	public String getRedefinedElementName(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			int i = line.indexOf(" REDEFINES ");
			if (i > -1) {								
				String elementName = line.substring(i + 11).trim();
				return getFullName(elementName, context);
			}
		}
		return null;
	}

	@Override
	public Usage getUsage(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			int i = line.indexOf(" USAGE IS ");
			if (i > -1) {								
				String p = line.substring(i + 10).trim();
				// TODO refine the syntax scanning because it is likely to fail
				// on some USAGEs; we should be able to do this using the
				// IDD ref guide
				if (p.equals("COMP")) {
					return Usage.COMPUTATIONAL;
				} else if (p.equals("COMP-1")) {
					return Usage.COMPUTATIONAL_1;
				} else if (p.equals("COMP-3")) {
					return Usage.COMPUTATIONAL_3;
				} else {
					return Usage.valueOf(p.replaceAll("-", "_"));
				}
			}
		}
		return null;
	}
	
}