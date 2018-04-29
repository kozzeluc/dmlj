/**
 * Copyright (C) 2018  Luc Hermans
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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.editor.importtool.IDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;
import org.lh.dmlj.schema.editor.wizard._import.schema.GeneralContextAttributeKeys;

public class ImportFromSchemaSyntaxTool implements ISchemaImportTool {

	private enum EntityType {AREA, RECORD, SET};
		
	private IDataCollectorRegistry 		 dataCollectorRegistry;
	private IDataEntryContext 	   		 dataEntryContext;
	private File 				   		 file;
	private IPromptForDigitCountResolver promptForDigitCountResolver; // used only when TESTing
	
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

	private void conditionallyUnsetRecordSuffix(SchemaSyntaxWrapper context) {
		
		// if the user has not checked one of the 'add suffix' options, we will
		// not correct anything
		short digitCount = -1;
		if (dataEntryContext.<Boolean>getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_3_DIGITS)) {
			digitCount = 3;
		} else if (dataEntryContext.<Boolean>getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_4_DIGITS)) {
			digitCount = 4;
		}
		if (digitCount == -1) {
			return;
		}
		
		// remove the suffix only when containsBaseNamesFlag is set to true AND
		// control fields (CALC or sort key) indicate that a suffix was set 
		// while it shouldn't have been
		boolean containsBaseNamesFlag = 
			Boolean.valueOf(context.getProperties()
				   .getProperty("containsBaseNamesFlag"))
			       .booleanValue();
		if (!containsBaseNamesFlag) {
			return;
		}		
		
		// we need the record data collector here since the context contains the
		// entire record syntax
		IRecordDataCollector<SchemaSyntaxWrapper> recordDataCollector =
			dataCollectorRegistry.getRecordDataCollector(SchemaSyntaxWrapper.class);
		
		// we don't expect a prefix...
		String prefix = context.getProperties().getProperty("prefix");
		if (prefix != null) {
			throw new RuntimeException("logic error; no prefix expected for " +
									   recordDataCollector.getName(context));
		}
		
		// ...but do expect a suffix
		String suffix = context.getProperties().getProperty("suffix");
		if (suffix == null) {
			throw new RuntimeException("logic error; suffix expected for " +
									   recordDataCollector.getName(context));
		}	
		
		// ...there might even be a base name suffix
		String baseSuffix = null;
		if (context.getProperties().containsKey("baseSuffix")) {
			baseSuffix = context.getProperties().getProperty("baseSuffix");
		}
		
		// get a list of all FULL element names (the order will not correspond 
		// with the real order); scan the whole record syntax in stead of 
		// creating temporary element contexts
		List<String> allFullElementNames = new ArrayList<String>();
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
						StringBuilder fullElementName = new StringBuilder();
						if (baseSuffix != null) {
							int i = elementName.lastIndexOf(baseSuffix);
							if (i < 0) {
								throw new RuntimeException("logic error: base suffix (" +
														   baseSuffix +
														   ") not in element name (" +
														   elementName + ")");
							}
							fullElementName.append(elementName.substring(0, i));
						} else {
							fullElementName.append(elementName);
						}
						fullElementName.append(suffix);
						allFullElementNames.add(fullElementName.toString());
					}
				}
			}
		}
		
		// in the case of a CALC record, see if there is a prefix/suffix 
		// mismatch using the first CALC element
		if (recordDataCollector.getLocationMode(context) == LocationMode.CALC) {
			// the record contained in the context is CALC
			List<String> calcElementNames =
				new ArrayList<>(recordDataCollector.getCalcKeyElementNames(context));
			// all CALC elements contain the prefix and/or suffix
			String firstCalcElement = calcElementNames.get(0);
			for (String fullElementName : allFullElementNames) {				
				if (fullElementName.equals(firstCalcElement)) {
					return;
				}
			}
			// remove the suffix; we do not expect a prefix
			context.getProperties().remove("suffix");
			System.out.println("removed suffix ('" + suffix + "') for " + 
					           recordDataCollector.getName(context));
			return;
		}
		
		// for DIRECT and VIA records, see if there is at least 1 sorted set
		// (other than sorted on dbkey) in which the record participates as a 
		// member and , see if there is a prefix/suffix mismatch using the first 
		// sort element name of that set 
		String firstSortElement = 
			getFirstSortElement(recordDataCollector.getName(context));
		if (firstSortElement != null) {	
			for (String fullElementName : allFullElementNames) {				
				if (fullElementName.equals(firstSortElement)) {
					return;
				}
			}
			// remove the suffix; we do not expect a prefix
			context.getProperties().remove("suffix");
			System.out.println("removed suffix ('" + suffix + "') for " + 
			           		   recordDataCollector.getName(context));
			return;			
		}		
		
		// if we get here, the record is not CALC and is not a member of at
		// least 1 sorted set; we don't have to fix anything 
		
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
					active = line.trim().equals("         MEMBER IS " + recordName);
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
			conditionallyUnsetRecordSuffix(context);
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
				if (context.getProperties().containsKey("containsBaseNamesFlag")) {
					String containsBaseNamesFlag = 
						context.getProperties().getProperty("containsBaseNamesFlag");
					listWrapper.getProperties().put("containsBaseNamesFlag", 
													containsBaseNamesFlag);
				}
				if (context.getProperties().containsKey("baseSuffix")) {
					String baseSuffix = 
						context.getProperties().getProperty("baseSuffix");
					listWrapper.getProperties().put("baseSuffix", baseSuffix);
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
		// schema's description, memo date and comments
		String schemaDescription = null;
		String memoDate = null;
		List<String> comments = new ArrayList<>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			// only process the syntax preceding the first period
			boolean commentsMarkerPassed = false;
			StringBuilder commentLine = null;
			for (String line = in.readLine(); line != null && !line.trim().equals("."); 
				 line = in.readLine()) {
				
				if (line.indexOf("SCHEMA DESCRIPTION IS '") > -1) {
					int i = line.indexOf("SCHEMA DESCRIPTION IS '");					
					StringBuilder p = new StringBuilder(line.substring(i + 23));					
					p.setLength(p.length() - 1);					
					schemaDescription = p.toString();					
				} else if (line.indexOf("MEMO DATE IS ") > -1) {
					int i = line.indexOf("MEMO DATE IS ");
					memoDate = line.substring(i + 13);					
				} else if (line.trim().equals("COMMENTS")) {
					commentsMarkerPassed = true;
				} else if (commentsMarkerPassed) {
					// we're dealing with a comment line (assuming nothing else than comment lines
					// follow the comment lines in the schema syntax; we might have to come here 
					// again in the future if something meaningful does appear to exist after the
					// comment lines
					if (!line.startsWith("*+")) {
						int i = line.lastIndexOf("'");
						if (line.startsWith("             '") ||						
							line.startsWith("       -     '")) {
													
							if (commentLine != null) {
								comments.add(commentLine.toString());
							}
							commentLine = new StringBuilder();						
						} else if (!line.startsWith("       +     '") || commentLine == null) {					
							in.close();
							throw new Error("logic error while parsing schema comments");
						}
						// leading and trailing spaces in comment lines are retained
						commentLine.append(line.substring(14, i));
					}
				}
			}
			if (commentLine != null) {
				// make sure we don't forget the last comment line
				comments.add(commentLine.toString());
			}
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		SchemaDataCollector schemaDataCollector = 
			new SchemaDataCollector(schemaDescription, memoDate, comments);
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
				String p = line.substring(2).trim(); // get rid of the comment indicator
				// let's see if the first 2 characters are an element level number...
				if (p.length() > 3 && 
					p.charAt(0) != ' ' && p.charAt(1) != ' ' && p.charAt(2) == ' ') {
					
					try {
						int level = Integer.valueOf(p.substring(0, 2));
						if (level >= 2 && level <= 49) {							
							// this line contains an element name; we need that, but we skip level 
							// 88 (condition names) and FILLER elements
							String elementName = p.substring(3);
							if (!elementName.equals("FILLER")) {
								allElementNames.add(elementName);
							}
						}					
					} catch (NumberFormatException e) {
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
		
		String controlElementName = null;
		
		// in the case of a CALC record, see if there is a prefix/suffix 
		// mismatch using the first CALC element
		if (recordDataCollector.getLocationMode(context) == LocationMode.CALC) {
			// the record contained in the context is CALC
			List<String> calcElementNames =
				new ArrayList<>(recordDataCollector.getCalcKeyElementNames(context));
			// all CALC elements contain the prefix and/or suffix
			String firstCalcElement = calcElementNames.get(0);
			controlElementName = firstCalcElement;
			// if the first CALC element name is contained in the list of all
			// elements, there is no prefix/suffix mismatch, but we might be 
			// able to distill a suffix later if the user requested to do so
			// - otherwise -
			// since the name of the first CALC element name is not contained in
			// the list of all element names, either a prefix or suffix, or both
			// are defined for the record synonym used; we only support prefixes
			// ending with a hyphen and suffixes starting with a hyphen
			if (!allElementNames.contains(firstCalcElement)) {				
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
		}
		
		// for DIRECT and VIA records, see if there is at least 1 sorted set
		// (other than sorted on dbkey) in which the record participates as a 
		// member and , see if there is a prefix/suffix mismatch using the first 
		// sort element name of that set 
		if (recordDataCollector.getLocationMode(context) == LocationMode.DIRECT ||
			recordDataCollector.getLocationMode(context) == LocationMode.VIA) {
			
			String firstSortElement = 
				getFirstSortElement(recordDataCollector.getName(context));
			if (firstSortElement != null) {	
				controlElementName = firstSortElement;
				// if the first sort key element name is contained in the list of 
				// all elements, there is no prefix/suffix mismatch, but we might be 
				// able to distill a suffix later if the user requested to do so
				if (!allElementNames.contains(firstSortElement)) {
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
			}
		}
		
		// no prefix/suffix mismatch or no control fields available - use the 
		// record-id as a suffix if (and only if) the user selected one of the 
		// available options, while also honoring the digit count assigned to 
		// the selected option
		short digitCount = -1;
		if (dataEntryContext.<Boolean>getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_3_DIGITS)) {
			digitCount = 3;
		} else if (dataEntryContext.<Boolean>getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_4_DIGITS)) {
			digitCount = 4;
		} else if (dataEntryContext.<Boolean>getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_PROMPT)) {
			if (promptForDigitCountResolver != null) {
				// get the digit count from the resolver (TEST circumstances only)
				digitCount = 
					promptForDigitCountResolver.getDigitCount(recordDataCollector.getName(context));
			} else {
				// prompt for digit count
				PromptForDigitCountDialog dialog = 
					new PromptForDigitCountDialog(Display.getCurrent().getActiveShell(),
								      			  context, recordDataCollector,
								      			  controlElementName);			
				if (dialog.open() == IDialogConstants.CANCEL_ID) {
					throw new RuntimeException("Import cancelled.");
				}
				digitCount = dialog.getSelectedDigitCount();
			}
		}
		if (digitCount != -1) {
			// get the record id
			short recordId = recordDataCollector.getRecordId(context);				
			// add a suffix containing the record-id		
			String suffix = "-" + pad(recordId, digitCount);		
			context.getProperties().put("suffix", suffix);
			setContainsBaseNamesFlag(context, null, suffix);
			return;
		}		
		
	}
	
	private boolean setRecordPrefixAndOrSuffix(SchemaSyntaxWrapper context,
											   List<String> allElementNames,
											   String modelElementName) {
		
		for (String elementName : allElementNames) {
			if (elementName.equals(modelElementName)) {
				// no prefix or suffix since we have a perfect match
				setContainsBaseNamesFlag(context, null, null);
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
					setContainsBaseNamesFlag(context, prefix, suffix);
					return true;
				}
			}				
		}
		
		// we could not derive a prefix nor suffix and yet we expect one... a 
		// very last attempt in matching the key element name passed to an 
		// element in the record is to try to detect a suffix in the base names		
		String baseSuffix = null;
		for (String baseElementName : allElementNames) {
			int i = baseElementName.lastIndexOf("-");
			if (i < 0) {
				return false; // no base name suffix: logic error
			}
			if (baseSuffix == null) {
				baseSuffix = baseElementName.substring(i);
			} else if (!baseElementName.substring(i).equals(baseSuffix)) {
				return false; // no base name suffix: logic error
			}
		}	
		// set the baseSuffix and containsBaseNamesFlag in the context
		context.getProperties().put("baseSuffix", baseSuffix);
		context.getProperties().put("containsBaseNamesFlag",
									String.valueOf(Boolean.TRUE));
		// now try to derive the suffix again (forget about a prefix, we will
		// not supporting that here in order not to make things too complicated)
		int j = modelElementName.lastIndexOf("-");
		if (j < 0) {
			// no suffix
			return true;
		}
		String suffix = modelElementName.substring(j);
		for (String baseElementName : allElementNames) {
			int i = baseElementName.lastIndexOf(baseSuffix);
			String elementName = baseElementName.substring(0, i);
			if (elementName.equals(modelElementName)) {
				// no suffix since we have a perfect match				
				return true;
			}
			if (modelElementName.equals(elementName + suffix)) {
				// we can confirm the suffix; set it in the context
				context.getProperties().put("suffix", suffix);
				return true;
			}
		}
		
		return false; // no suffix: logic error
		
	}

	private void setContainsBaseNamesFlag(SchemaSyntaxWrapper context,
										  String prefix, String suffix) {
		
		boolean containsBaseNamesFlag = false;
		
		List<String> allElementNames = new ArrayList<String>();
		boolean inElementSyntax = false;
		for (String line : context.getLines()) {
			if (line.trim().equals(".")) {
				inElementSyntax = true;
			} else if (inElementSyntax) {
				String p = line.substring(2).trim();
				if ((p.startsWith("0") || p.startsWith("88")) && 
					p.charAt(2) == ' ') {					
					String elementName = p.substring(3);
					if (!elementName.equals("FILLER")) {
						// skip FILLER elements
						allElementNames.add(elementName);
					}
				}
			}
		}		
		
		if (prefix != null && suffix != null) {
			for (String elementName : allElementNames) {
				if (!elementName.startsWith(prefix) || 
					!elementName.endsWith(suffix)) {
					
					containsBaseNamesFlag = true;
					break;
				}
			}			
		} else if (prefix != null) {
			for (String elementName : allElementNames) {
				if (!elementName.startsWith(prefix)) {					
					containsBaseNamesFlag = true;
					break;
				}
			}			
		} else if (suffix != null) {
			for (String elementName : allElementNames) {
				if (!elementName.endsWith(suffix)) {
					containsBaseNamesFlag = true;
					break;
				}
			}						
		}		
		
		context.getProperties().put("containsBaseNamesFlag",
									String.valueOf(containsBaseNamesFlag));
		
	}
	
	/**
	 * Provides a replacement for PromptForDigitCountDialog; sould only be set when running in a 
	 * TEST environment (e.g. when running JUnit tests).  MIGHT be interesting to provide IRL
	 * situations as well though...
	 * @param newValue the simulator for PromptForDigitCountDialog
	 */
	public void setPromptForDigitCountResolver(IPromptForDigitCountResolver newValue) {
		promptForDigitCountResolver = newValue;
	}

}
