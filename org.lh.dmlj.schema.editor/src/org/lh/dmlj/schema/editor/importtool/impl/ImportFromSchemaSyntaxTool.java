package org.lh.dmlj.schema.editor.importtool.impl;

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

public class ImportFromSchemaSyntaxTool implements ISchemaImportTool {

	private enum EntityType {AREA, RECORD, SET};
	
	private IDataCollectorRegistry dataCollectorRegistry;
	private File 				   file;
	
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
		
		// we need the IDataCollectorRegistry later
		this.dataCollectorRegistry = dataCollectorRegistry;
		
		// get the file from the data entry context
		file = (File) dataEntryContext.getAttribute(ContextAttributeKeys.SCHEMA_SYNTAX_FILE);
		
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

	@Override
	public boolean isOptionAddDDLCATLOD() {
		return false;
	}

	@Override
	public boolean isOptionCompleteLooak_155() {
		return false;
	}

	@Override
	public boolean isOptionCompleteOoak_012() {
		return false;
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
			for (String elementName : allElementNames) {
				if (elementName.equals(firstCalcElement)) {
					// no prefix or suffix since we have a perfect match
					return;
				}
				int i = firstCalcElement.indexOf("-" + elementName); 
				int j = firstCalcElement.indexOf(elementName + "-");
				if (i > -1) {
					String prefix = firstCalcElement.substring(0, i + 1);
					context.getProperties().put("prefix", prefix);
				}
				if (j > -1) {
					j = firstCalcElement.indexOf("-", j);
					String suffix = firstCalcElement.substring(j);
					context.getProperties().put("suffix", suffix);
				}
				if (i > -1 || j > -1) {
					// if a prefix and/or suffix could be derived, we're done
					return;
				}
			}
			throw new RuntimeException("logic error: cannot derive prefix/" +
									   "suffix for " + 
									   recordDataCollector.getName(context) + 
									   " (CALC)");
		}
		
		// for DIRECT and VIA records, see if there is at least 1 sorted set
		// (other than sorted on dbkey) in which the record participates as a 
		// member and determine the prefix and/or suffix using the first sort 
		// element name of that set 
		String firstSortElement = 
			getFirstSortElement(recordDataCollector.getName(context));
		if (firstSortElement != null) {
			for (String elementName : allElementNames) {
				if (elementName.equals(firstSortElement)) {
					// no prefix or suffix since we have a perfect match
					return;
				}
				int i = firstSortElement.indexOf("-" + elementName); 
				int j = firstSortElement.indexOf(elementName + "-");
				if (i > -1) {
					String prefix = firstSortElement.substring(0, i + 1);
					context.getProperties().put("prefix", prefix);
				}
				if (j > -1) {
					j = firstSortElement.indexOf("-", j);
					String suffix = firstSortElement.substring(j);
					context.getProperties().put("suffix", suffix);
				}
				if (i > -1 || j > -1) {
					// if a prefix and/or suffix could be derived, we're done
					return;
				}
			}
			throw new RuntimeException("logic error: cannot derive prefix/" +
					   				   "suffix for " + 
					   				   recordDataCollector.getName(context) +
					   				   " (VIA/DIRECT)");			
		}
		
	}

}