package org.lh.dmlj.schema.editor.importtool.syntax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.editor.importtool.IDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;
import org.lh.dmlj.schema.editor.wizard._import.schema.GeneralContextAttributeKeys;

public class ImportFromSchemaSyntaxTool implements ISchemaImportTool {

	private enum EntityType {AREA, RECORD, SET};
		
	private IDataCollectorRegistry dataCollectorRegistry;
	private IDataEntryContext 	   dataEntryContext;
	private File 				   file;
	
	private static String pad(short number, int length) {
		StringBuilder p = new StringBuilder();
		p.append(String.valueOf(number));
		while (p.length() < length) {
			p.insert(0, "0");
		}
		return p.toString();
	}

	public ImportFromSchemaSyntaxTool() {
		super();
	}

	@Override
	public void dispose() {		
	}
	
	private List<SchemaSyntaxWrapper> extractEntities(EntityType entityType) {
		String trigger = "     " + entityType.toString() + " NAME IS ";
		List<SchemaSyntaxWrapper> list = new ArrayList<>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			SchemaSyntaxWrapper listWrapper = null;
			for (String line = in.readLine(); line != null; line = in.readLine()) {				
				if (line.startsWith(trigger)) {					
					listWrapper = new SchemaSyntaxWrapper();
					list.add(listWrapper);
					listWrapper.getLines().add("     ADD");
					listWrapper.getLines().add(line);
				} else if (line.startsWith("     ADD")) {
					listWrapper = null;
				} else if (listWrapper != null) {
					listWrapper.getLines().add(line);
				}
			}
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
		return list;
	}

	@Override
	public Collection<?> getAreaContexts() {
		return extractEntities(EntityType.AREA);
	}

	private String getFirstSortElement(String recordName) {
		try {
			String elementName = null;
			BufferedReader in = new BufferedReader(new FileReader(file));
			boolean active = false;
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				if (line.startsWith("         MEMBER IS ")) {
					active = line.startsWith("         MEMBER IS " + recordName);
				} else if (active && 
						   (line.indexOf(" ASCENDING") > -1 || 
							line.indexOf(" DESCENDING") > -1) &&
						   line.indexOf("DBKEY ") < 0) {
					
					// grab the element name and return it to the caller
					String p = line.trim();
					int i = p.indexOf(" ");
					elementName = p.substring(0, i);
					break;
				}
			}
			in.close();
			return elementName;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Collection<?> getRecordContexts() {
		List<SchemaSyntaxWrapper> contexts = extractEntities(EntityType.RECORD);
		// if applicable, set a record prefix and/or suffix in each context
		for (SchemaSyntaxWrapper context : contexts) {
			setRecordPrefixAndOrSuffix(context);
		}
		return contexts;
	}

	@Override
	public Collection<?> getRootElementContexts(Object recordContext) {		
		return getSubordinateElementContexts(recordContext);	
	}

	@Override
	public Collection<?> getSetContexts() {
		return extractEntities(EntityType.SET);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getSubordinateElementContexts(T recordOrElementContext) {

		if (!(recordOrElementContext instanceof SchemaSyntaxWrapper)) {		
			throw new IllegalArgumentException("unknown context type: " +
											   recordOrElementContext.getClass()
											   						 .getName());
		}
		
		SchemaSyntaxWrapper context = 
			(SchemaSyntaxWrapper) recordOrElementContext;
		
		int i = 0;
		while (!context.getLines().get(i).substring(2).trim().equals(".")) {			
			i += 1;
		}
		i += 1;
		
		if (i >= context.getLines().size()) {
			// the element does not have subordinate elements
			return Collections.emptyList();
		}
		
		int j = 2;
		String line = context.getLines().get(i); 
		while (line.charAt(j) == ' ') {
			j += 1;
		}
		String scanItem = line.substring(0, j + 3);
		
		List<SchemaSyntaxWrapper> list = new ArrayList<>();
		SchemaSyntaxWrapper listWrapper = null;
		for (String aLine : context.getLines()) {
			if (aLine.startsWith(scanItem)) {
				listWrapper = new SchemaSyntaxWrapper();
				list.add(listWrapper);
				// add the record prefix and/or suffix if applicable; it will be
				// contained in the record or element context
				if (context.getProperties().containsKey("prefix")) {
					String prefix = 
						context.getProperties().getProperty("prefix");
					listWrapper.getProperties().put("prefix", prefix);					
				}
				if (context.getProperties().containsKey("suffix")) {
					String suffix = 
						context.getProperties().getProperty("suffix");
					listWrapper.getProperties().put("suffix", suffix);
				}				
			} 
			if (listWrapper != null) {
				listWrapper.getLines().add(aLine);
			}
		}
	
		return (Collection<T>) list;		
		
	}

	@Override
	public void init(IDataEntryContext dataEntryContext, Properties parameters,
					 IDataCollectorRegistry dataCollectorRegistry) {
		
		// we need the IDataEntryContext and IDataCollectorRegistry later 
		this.dataEntryContext = dataEntryContext;
		this.dataCollectorRegistry = dataCollectorRegistry;
		
		// get the file from the data entry context
		file = (File) dataEntryContext.getAttribute(GeneralContextAttributeKeys.SCHEMA_SYNTAX_FILE);
		
		// create and register the schema data collector; first extract the 
		// schema's description and memo date
		String schemaDescription = null;
		String memoDate = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			// only process the syntax preceding the first period
			for (String line = in.readLine(); 
				 line != null && !line.trim().equals("."); 
				 line = in.readLine()) {
				
				if (line.indexOf("SCHEMA DESCRIPTION IS '") > -1) {
					int i = line.indexOf("SCHEMA DESCRIPTION IS '");					
					StringBuilder p = new StringBuilder(line.substring(i + 23));					
					p.setLength(p.length() - 1);					
					schemaDescription = p.toString();					
				} else if (line.indexOf("MEMO DATE IS ") > -1) {
					int i = line.indexOf("MEMO DATE IS ");
					memoDate = line.substring(i + 13);					
				}
			}
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		SchemaDataCollector schemaDataCollector = 
			new SchemaDataCollector(schemaDescription, memoDate);
		dataCollectorRegistry.registerSchemaDataCollector(schemaDataCollector);
		
		// create and register the area data collector
		AreaDataCollector areaDataCollector = new AreaDataCollector();
		dataCollectorRegistry.registerAreaDataCollector(SchemaSyntaxWrapper.class, 
														areaDataCollector);
		
		// create and register the record data collector
		RecordDataCollector recordDataCollector = new RecordDataCollector();
		dataCollectorRegistry.registerRecordDataCollector(SchemaSyntaxWrapper.class, 
														  recordDataCollector);
		
		// create and register the element data collector
		ElementDataCollector elementDataCollector = new ElementDataCollector();
		dataCollectorRegistry.registerElementDataCollector(SchemaSyntaxWrapper.class, 
														   elementDataCollector);
		
		// create and register the set data collector
		SetDataCollector setDataCollector = new SetDataCollector();
		dataCollectorRegistry.registerSetDataCollector(SchemaSyntaxWrapper.class, 
													   setDataCollector);
		
	}	

	private void setRecordPrefixAndOrSuffix(SchemaSyntaxWrapper context) {		
		
		// we need the record data collector here since the context contains the
		// entire record syntax
		IRecordDataCollector<SchemaSyntaxWrapper> recordDataCollector =
			dataCollectorRegistry.getRecordDataCollector(SchemaSyntaxWrapper.class);
		
		// get a list of all elements (the order will not correspond with the
		// real order); scan the whole record syntax in stead of creating
		// temporary element contexts
		List<String> allElementNames = new ArrayList<String>();
		boolean inElementSyntax = false;
		for (String line : context.getLines()) {
			if (line.trim().equals(".")) {
				inElementSyntax = true;
			} else if (inElementSyntax) {
				String p = line.substring(2).trim();
				if (p.startsWith("0") && p.charAt(2) == ' ') {
					// skip level 88 elements
					String elementName = p.substring(3);
					if (!elementName.equals("FILLER")) {
						// skip FILLER elements
						allElementNames.add(elementName);
					}
				}
			}
		}
		
		// if the record is composed of only 1 or more FILLER elements, there is
		// no way we can detect a prefix and/or suffix and there is no need for
		// them neither, so get out in that case
		if (allElementNames.isEmpty()) {
			return;
		}
		
		// in the case of a CALC record, determine the prefix/suffix using the
		// first CALC element
		if (recordDataCollector.getLocationMode(context) == LocationMode.CALC) {
			// the record contained in the context is CALC
			List<String> calcElementNames =
				new ArrayList<>(recordDataCollector.getCalcKeyElementNames(context));
			// all CALC elements contain the prefix and/or suffix
			String firstCalcElement = calcElementNames.get(0);
			// if the first CALC element name is contained in the list of all
			// elements, there is no prefix nor a suffix, so we're done
			if (allElementNames.contains(firstCalcElement)) {
				return;
			}
			// since the name of the first CALC element name is not contained in
			// the list of all element names, either a prefix or suffix, or both
			// are defined for the record synonym used; we only support prefixes
			// ending with a hyphen and suffixes starting with a hypen
			if (setRecordPrefixAndOrSuffix(context, allElementNames, 
										   firstCalcElement)) {
				return;
			} else {
				throw new RuntimeException("logic error: cannot derive " +
										   "prefix/suffix for " + 
										   recordDataCollector.getName(context) +
										   " (CALC)");
			}
		}
		
		// for DIRECT and VIA records, see if there is at least 1 sorted set
		// (other than sorted on dbkey) in which the record participates as a 
		// member and determine the prefix and/or suffix using the first sort 
		// element name of that set 
		String firstSortElement = 
			getFirstSortElement(recordDataCollector.getName(context));
		if (firstSortElement != null) {	
			// if the first sort key element name is contained in the list of 
			// all elements, there is no prefix nor a suffix, so we're done
			if (allElementNames.contains(firstSortElement)) {
				return;
			}
			if (setRecordPrefixAndOrSuffix(context, allElementNames, 
									       firstSortElement)) {
				
				return;
			} else {
				throw new RuntimeException("logic error: cannot derive prefix/" +
						   				   "suffix for " + 
						   				   recordDataCollector.getName(context) +
						   				   " (VIA/DIRECT)");
			}			
		}
		
		// use the record-id as a suffix if the user selected one of the 
		// available options, while also honoring the digit count assigned to 
		// the selected option - we don't have to bother about this) IF AND ONLY 
		// IF no suffix is already there (a suffix is considered to always start 
		// with a hyphen)
		short digitCount;
		if (dataEntryContext.getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_3_DIGITS)) {
			digitCount = 3;
		} else if (dataEntryContext.getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_4_DIGITS)) {
			digitCount = 4;
		} else {
			return;
		}
		// get the record id
		short recordId = recordDataCollector.getRecordId(context);
		// see if there is already a suffix present
		int i = allElementNames.get(0).lastIndexOf("-");
		boolean addSuffix = false;
		if (i > -1) {
			// there seems to be a hyphen present; see if the record id can be
			// derived from the characters following that hyphen and see if all
			// element names end with that same character sequence		
			String p = allElementNames.get(0).substring(i);
			try {
				short j = Short.valueOf(p.substring(1));
				if (j == recordId) {				
					for (String elementName : allElementNames) {
						if (!elementName.endsWith(p)) {
							addSuffix = true;
							break;
						}
					}		
				}
			} catch (NumberFormatException e) {
				// not a suffix with the record id
				addSuffix = true;
			}			
		} else {
			// no hyphen so we consider no suffix to be present
			addSuffix = true;
		}
		// get out if a suffix seems to be there already
		if (!addSuffix) {
			return;
		}		
		// add a suffix containing the record-id
		
		String suffix = "-" + pad(recordId, digitCount);		
		context.getProperties().put("suffix", suffix);		
		
	}
	
	private boolean setRecordPrefixAndOrSuffix(SchemaSyntaxWrapper context,
											   List<String> allElementNames,
											   String modelElementName) {
		
		for (String elementName : allElementNames) {
			if (elementName.equals(modelElementName)) {
				// no prefix or suffix since we have a perfect match
				return true;
			}
			int i = modelElementName.indexOf("-" + elementName); 
			int j = modelElementName.indexOf(elementName + "-");
			String prefix = null;
			String suffix = null;
			if (i > -1) {
				String p = modelElementName.substring(0, i + 1);
				// only allow for 1 hyphen in the prefix
				if (p.substring(0, p.length() -1).indexOf("-") == -1) {
					prefix = p;
				}
			}
			if (j > -1) {
				j += elementName.length();
				String p = modelElementName.substring(j);
				// only allow for 1 hyphen in the suffix
				if (p.substring(1).indexOf("-") == -1) {
					suffix = p;
				}
			}
			if (prefix != null || suffix != null) {
				// check if we've found the right prefix and/or suffix...
				StringBuilder p = new StringBuilder();
				if (prefix != null) {
					p.append(prefix);
				}
				p.append(elementName);
				if (suffix != null) {
					p.append(suffix);
				}
				if (p.toString().equals(modelElementName)) {
					// if a prefix and/or suffix could be derived, we need
					// to store them in the context and we're done
					if (prefix != null) {
						context.getProperties().put("prefix", prefix);
					}
					if (suffix != null) {
						context.getProperties().put("suffix", suffix);
					}
					return true;
				}
			}				
		}
		return false;		
		
	}

}