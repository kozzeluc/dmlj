/**
 * Copyright (C) 2025  Luc Hermans
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
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.common.Pair;
import org.lh.dmlj.schema.editor.importtool.IDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;
import org.lh.dmlj.schema.editor.wizard._import.schema.GeneralContextAttributeKeys;

public class ImportFromSchemaSyntaxTool implements ISchemaImportTool {
	private static final String PREFIX = "prefix";
	private static final String SUFFIX = "suffix";
	private static final String BASE_SUFFIX = "baseSuffix";
	private static final String FILLER = "FILLER";
	private static final String CONTAINS_BASE_NAMES_FLAG = "containsBaseNamesFlag";

	private enum EntityType { AREA, RECORD, SET }
		
	private IDataCollectorRegistry dataCollectorRegistry;
	private IDataEntryContext dataEntryContext;
	private File file;
	private IPromptForDigitCountResolver promptForDigitCountResolver; // used only when TESTing
	
	private static String pad(short number, int length) {
		var p = new StringBuilder();
		p.append(String.valueOf(number));
		while (p.length() < length) {
			p.insert(0, "0");
		}
		return p.toString();
	}

	private void conditionallyUnsetRecordSuffix(SchemaSyntaxWrapper context) {
		// if the user has not checked one of the 'add suffix' options, we will not correct anything
		var use3DigitsForMissingSuffixes = dataEntryContext.<Boolean>getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_3_DIGITS).booleanValue();
		var use4DigitsForMissingSuffixes = dataEntryContext.<Boolean>getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_4_DIGITS).booleanValue();
		if (!use3DigitsForMissingSuffixes && !use4DigitsForMissingSuffixes) {	
			return;
		}
		
		// remove the suffix only when containsBaseNamesFlag is set to true AND control fields (CALC or sort key)
		// indicate that a suffix was set while it shouldn't have been
		var containsBaseNamesFlag = Boolean.parseBoolean(context.getProperties().getProperty(CONTAINS_BASE_NAMES_FLAG));
		if (!containsBaseNamesFlag) {
			return;
		}		
		
		// we need the record data collector here since the context contains the entire record syntax
		var recordDataCollector = dataCollectorRegistry.getRecordDataCollector(SchemaSyntaxWrapper.class);
		
		// we don't expect a prefix...
		if (context.getProperties().getProperty(PREFIX) != null) {
			throw new IllegalStateException("logic error; no prefix expected for " + recordDataCollector.getName(context));
		}
		
		// ...but do expect a suffix
		var suffix = context.getProperties().getProperty(SUFFIX);
		if (context.getProperties().getProperty(SUFFIX) == null) {
			throw new IllegalStateException("logic error; suffix expected for " + recordDataCollector.getName(context));
		}	
		
		// ...there might even be a base name suffix
		String baseSuffix = null;
		if (context.getProperties().containsKey(BASE_SUFFIX)) {
			baseSuffix = context.getProperties().getProperty(BASE_SUFFIX);
		}
		
		// get a list of all FULL element names (the order will not correspond with the real order); scan the
		// whole record syntax in stead of creating temporary element contexts
		var allFullElementNames = getAllFullElementNames(context, suffix, baseSuffix);
		
		// in the case of a CALC record, see if there is a prefix/suffix mismatch using the first CALC element
		if (recordDataCollector.getLocationMode(context) == LocationMode.CALC) {
			// the record contained in the context is CALC
			var calcElementNames = new ArrayList<>(recordDataCollector.getCalcKeyElementNames(context));
			// all CALC elements contain the prefix and/or suffix
			var firstCalcElement = calcElementNames.get(0);
			if (allFullElementNames.stream().anyMatch(fullElementName -> fullElementName.equals(firstCalcElement))) {
				return;
			}
			// remove the suffix; we do not expect a prefix
			context.getProperties().remove(SUFFIX);
			Plugin.getDefault().getLog().info("removed suffix ('" + suffix + "') for " + recordDataCollector.getName(context));
			return;
		}
		
		// for DIRECT and VIA records, see if there is at least 1 sorted set (other than sorted on dbkey) in
		// which the record participates as a member and , see if there is a prefix/suffix mismatch using the
		// first sort element name of that set
		var firstSortElement = getFirstSortElement(recordDataCollector.getName(context));
		if (firstSortElement != null) {	
			if (allFullElementNames.stream().anyMatch(fullElementName -> fullElementName.equals(firstSortElement))) {
				return;
			}
			// remove the suffix; we do not expect a prefix
			context.getProperties().remove(SUFFIX);
			Plugin.getDefault().getLog().info("removed suffix ('" + suffix + "') for " + recordDataCollector.getName(context));
		}		
		
		// if we get here, the record is not CALC and is not a member of at least 1 sorted set; we don't have to fix anything 		
	}
	
	private List<String> getAllFullElementNames(SchemaSyntaxWrapper context, String suffix, String baseSuffix) {
		var allFullElementNames = new ArrayList<String>();
		var inElementSyntax = false;
		for (var line : context.getLines()) {
			if (line.trim().equals(".")) {
				inElementSyntax = true;
			} else if (inElementSyntax) {
				var p = line.substring(2).trim();
				if (p.startsWith("0") && p.charAt(2) == ' ') {
					// skip level 88 elements
					var elementName = p.substring(3);
					if (!elementName.equals(FILLER)) {
						allFullElementNames.add(getFullElementName(elementName, suffix, baseSuffix));
					}
				}
			}
		}
		return allFullElementNames;
	}
	
	private String getFullElementName(String elementName, String suffix, String baseSuffix) {
		var fullElementName = new StringBuilder();
		if (baseSuffix != null) {
			var i = elementName.lastIndexOf(baseSuffix);
			if (i < 0) {
				throw new IllegalStateException("logic error: base suffix (" + baseSuffix + ") not in element name (" + elementName + ")");
			}
			fullElementName.append(elementName.substring(0, i));
		} else {
			fullElementName.append(elementName);
		}
		fullElementName.append(suffix);
		return fullElementName.toString();
	}

	@Override
	public void dispose() {
		// nothing to do here
	}
	
	private List<SchemaSyntaxWrapper> extractEntities(EntityType entityType) {
		var trigger = "     " + entityType.toString() + " NAME IS ";
		var list = new ArrayList<SchemaSyntaxWrapper>();
		try (var in = new BufferedReader(new FileReader(file))) {
			SchemaSyntaxWrapper listWrapper = null;
			for (var line = in.readLine(); line != null; line = in.readLine()) {				
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
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}		
		return list;
	}

	@Override
	public ContextCollection getAreaContexts() {
		return new ContextCollection(extractEntities(EntityType.AREA));
	}

	private String getFirstSortElement(String recordName) {
		try (var in = new BufferedReader(new FileReader(file))) {
			String elementName = null;
			var active = false;
			for (var line = in.readLine(); line != null; line = in.readLine()) {
				if (line.startsWith("         MEMBER IS ")) {
					active = line.trim().equals("         MEMBER IS " + recordName);
				} else if (active && (line.indexOf(" ASCENDING") > -1 || line.indexOf(" DESCENDING") > -1) && line.indexOf("DBKEY ") < 0) {
					// grab the element name and return it to the caller
					var p = line.trim();
					int i = p.indexOf(" ");
					elementName = p.substring(0, i);
					break;
				}
			}
			return elementName;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public ContextCollection getRecordContexts() {
		var contexts = extractEntities(EntityType.RECORD);
		// if applicable, set a record prefix and/or suffix in each context
		for (var context : contexts) {
			setRecordPrefixAndOrSuffix(context);			
			conditionallyUnsetRecordSuffix(context);
		}
		return new ContextCollection(contexts);
	}

	@Override
	public ContextCollection getRootElementContexts(Object recordContext) {		
		return new ContextCollection(getSubordinateElementContexts(recordContext));	
	}

	@Override
	public ContextCollection getSetContexts() {
		return new ContextCollection(extractEntities(EntityType.SET));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getSubordinateElementContexts(T recordOrElementContext) {
		if (!(recordOrElementContext instanceof SchemaSyntaxWrapper)) {		
			throw new IllegalArgumentException("unknown context type: " + recordOrElementContext.getClass().getName());
		}
		var context = (SchemaSyntaxWrapper) recordOrElementContext;
		
		var i = 0;
		while (!context.getLines().get(i).substring(2).trim().equals(".")) {
			i += 1;
		}
		i += 1;
		
		if (i >= context.getLines().size()) {
			// the element does not have subordinate elements
			return Collections.emptyList();
		}
		
		var j = 2;
		var line = context.getLines().get(i); 
		while (line.charAt(j) == ' ') {
			j += 1;
		}
		var scanItem = line.substring(0, j + 3);
		
		var list = new ArrayList<SchemaSyntaxWrapper>();
		SchemaSyntaxWrapper listWrapper = null;
		for (var aLine : context.getLines()) {
			if (aLine.startsWith(scanItem)) {
				listWrapper = new SchemaSyntaxWrapper();
				list.add(listWrapper);
				// add the record prefix and/or suffix if applicable; it will be contained in the record or element context
				putListWrapperProperties(context, listWrapper);
			} 
			if (listWrapper != null) {
				listWrapper.getLines().add(aLine);
			}
		}
		return (Collection<T>) list;
	}
	
	private void putListWrapperProperties(SchemaSyntaxWrapper context, SchemaSyntaxWrapper listWrapper) {
		if (context.getProperties().containsKey(PREFIX)) {
			var prefix = context.getProperties().getProperty(PREFIX);
			listWrapper.getProperties().put(PREFIX, prefix);					
		}
		if (context.getProperties().containsKey(SUFFIX)) {
			var suffix = context.getProperties().getProperty(SUFFIX);
			listWrapper.getProperties().put(SUFFIX, suffix);
		}
		if (context.getProperties().containsKey(CONTAINS_BASE_NAMES_FLAG)) {
			var containsBaseNamesFlag = context.getProperties().getProperty(CONTAINS_BASE_NAMES_FLAG);
			listWrapper.getProperties().put(CONTAINS_BASE_NAMES_FLAG, containsBaseNamesFlag);
		}
		if (context.getProperties().containsKey(BASE_SUFFIX)) {
			var baseSuffix = context.getProperties().getProperty(BASE_SUFFIX);
			listWrapper.getProperties().put(BASE_SUFFIX, baseSuffix);
		}
	}

	@Override
	public void init(IDataEntryContext dataEntryContext, Properties parameters, IDataCollectorRegistry dataCollectorRegistry) {
		// we need the IDataEntryContext and IDataCollectorRegistry later 
		this.dataEntryContext = dataEntryContext;
		this.dataCollectorRegistry = dataCollectorRegistry;
		
		// get the file from the data entry context
		file = (File) dataEntryContext.getAttribute(GeneralContextAttributeKeys.SCHEMA_SYNTAX_FILE);
		
		// create and register the schema data collector; first extract the schema's description, memo date and comments
		registerDataCollectors(getDataCollectorData());
	}
	
	private DataCollectorData getDataCollectorData() {
		String schemaDescription = null;
		String memoDate = null;
		var comments = new ArrayList<String>();
		try (var in = new BufferedReader(new FileReader(file))) {
			// only process the syntax preceding the first period
			var commentsMarkerPassed = false;
			var commentLine = new StringBuilder();
			for (var line = in.readLine(); line != null && !line.trim().equals("."); line = in.readLine()) {
				if (line.contains("SCHEMA DESCRIPTION IS '")) {
					schemaDescription = getSchemaDescription(line);		
				} else if (line.indexOf("MEMO DATE IS ") > -1) {
					memoDate = getMemoDate(line);		
				} else if (line.trim().equals("COMMENTS")) {
					commentsMarkerPassed = true;
				} else if (commentsMarkerPassed && !line.startsWith("*+")) {
					// we're dealing with a comment line (assuming nothing else than comment lines follow the
					// comment lines in the schema syntax; we might have to come here again in the future if
					// something meaningful does appear to exist after the comment lines
					processCommentLine(line, commentLine, comments);
				}
			}
			if (!commentLine.isEmpty()) {
				// make sure we don't forget the last comment line
				comments.add(commentLine.toString());
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}	
		return new DataCollectorData(schemaDescription, memoDate, comments);
	}
	
	private String getSchemaDescription(String line) {
		var i = line.indexOf("SCHEMA DESCRIPTION IS '");					
		var p = new StringBuilder(line.substring(i + 23));					
		p.setLength(p.length() - 1);					
		return p.toString();
	}
	
	private String getMemoDate(String line) {
		var i = line.indexOf("MEMO DATE IS ");
		return line.substring(i + 13);
	}
	
	private void processCommentLine(String line, StringBuilder commentLine, List<String> comments) {
		var i = line.lastIndexOf("'");
		if (line.startsWith("             '") || line.startsWith("       -     '")) {
			if (!commentLine.isEmpty()) {
				comments.add(commentLine.toString());
			}
			commentLine.setLength(0);						
		} else if (!line.startsWith("       +     '") || commentLine.isEmpty()) {
			throw new IllegalStateException("logic error while parsing schema comments");
		}
		// leading and trailing spaces in comment lines are retained
		commentLine.append(line.substring(14, i));		
	}
	
	private void registerDataCollectors(DataCollectorData dataCollectorData) {
		var schemaDataCollector = new SchemaDataCollector(dataCollectorData.schemaDescription(),
				dataCollectorData.memoDate(), dataCollectorData.comments());
		dataCollectorRegistry.registerSchemaDataCollector(schemaDataCollector);
		
		// create and register the area data collector
		var areaDataCollector = new AreaDataCollector();
		dataCollectorRegistry.registerAreaDataCollector(SchemaSyntaxWrapper.class, areaDataCollector);
		
		// create and register the record data collector
		var recordDataCollector = new RecordDataCollector();
		dataCollectorRegistry.registerRecordDataCollector(SchemaSyntaxWrapper.class, recordDataCollector);
		
		// create and register the element data collector
		var elementDataCollector = new ElementDataCollector();
		dataCollectorRegistry.registerElementDataCollector(SchemaSyntaxWrapper.class, elementDataCollector);
		
		// create and register the set data collector
		var setDataCollector = new SetDataCollector();
		dataCollectorRegistry.registerSetDataCollector(SchemaSyntaxWrapper.class, setDataCollector);
	}

	private void setRecordPrefixAndOrSuffix(SchemaSyntaxWrapper context) {
		// we need the record data collector here since the context contains the entire record syntax
		var recordDataCollector = dataCollectorRegistry.getRecordDataCollector(SchemaSyntaxWrapper.class);
		
		// get a list of all elements (the order will not correspond with the real order); scan the whole record
		// syntax in stead of creating temporary element contexts - if the record is composed of only 1 or more
		// FILLER elements, there is no way we can detect a prefix and/or suffix and there is no need for them
		// neither, so get out in that case
		var allElementNames = getAllElementNames(context);
		if (allElementNames.isEmpty()) {
			return;
		}
		
		String controlElementName = null;
		var locationMode = recordDataCollector.getLocationMode(context);
		
		// in the case of a CALC record, see if there is a prefix/suffix mismatch using the first CALC element
		if (locationMode == LocationMode.CALC) {
			controlElementName = getControlElementNameForCalc(context, recordDataCollector, allElementNames);
			if (controlElementName == null) {
				return;
			}
		}
		
		// for DIRECT and VIA records, see if there is at least 1 sorted set (other than sorted on dbkey) in
		// which the record participates as a member and , see if there is a prefix/suffix mismatch using the
		// first sort element name of that set 
		if (locationMode == LocationMode.DIRECT || locationMode == LocationMode.VIA) {
			var firstSortElement = getFirstSortElement(recordDataCollector.getName(context));
			if (firstSortElement != null) {
				controlElementName = getControlElementNameForViaOrDirect(context, recordDataCollector, allElementNames, firstSortElement);
				if (controlElementName == null) {
					return;
				}
			}
		}
		
		// no prefix/suffix mismatch or no control fields available - use the record-id as a suffix if 
		// (and only if) the user selected one of the available options, while also honoring the digit count
		// assigned to the selected option
		var digitCount = getDigitCount(context, recordDataCollector, controlElementName);
		if (digitCount != -1) {
			// get the record id
			var recordId = recordDataCollector.getRecordId(context);				
			// add a suffix containing the record-id		
			var suffix = "-" + pad(recordId, digitCount);		
			context.getProperties().put(SUFFIX, suffix);
			setContainsBaseNamesFlag(context, null, suffix);
		}
	}
	
	private List<String> getAllElementNames(SchemaSyntaxWrapper context) {
		var allElementNames = new ArrayList<String>();
		var inElementSyntax = false;
		for (var line : context.getLines()) {
			if (line.trim().equals(".")) {
				inElementSyntax = true;
			} else if (inElementSyntax) {
				var elementName = getElementNameIgnoringConditionName(line);
				if (elementName != null) {
					allElementNames.add(elementName);
				}
			}
		}
		return allElementNames;
	}
	
	private String getControlElementNameForCalc(SchemaSyntaxWrapper context,
			IRecordDataCollector<SchemaSyntaxWrapper> recordDataCollector, List<String> allElementNames) {
		
		var calcElementNames = new ArrayList<>(recordDataCollector.getCalcKeyElementNames(context));
		// all CALC elements contain the prefix and/or suffix
		var firstCalcElement = calcElementNames.get(0);
		var controlElementName = firstCalcElement;
		// if the first CALC element name is contained in the list of all elements, there is no prefix/suffix
		// mismatch, but we might be able to distill a suffix later if the user requested to do so - otherwise -
		// since the name of the first CALC element name is not contained in the list of all element names,
		// either a prefix or suffix, or both are defined for the record synonym used; we only support prefixes
		// ending with a hyphen and suffixes starting with a hyphen
		if (!allElementNames.contains(firstCalcElement)) {				
			if (setRecordPrefixAndOrSuffix(context, allElementNames, firstCalcElement)) {
				return null;
			} else {
				throw new IllegalStateException("logic error: cannot derive prefix/suffix for " + recordDataCollector.getName(context) + " (CALC)");
			}
		}
		return controlElementName;
	}
	
	private String getControlElementNameForViaOrDirect(SchemaSyntaxWrapper context,
			IRecordDataCollector<SchemaSyntaxWrapper> recordDataCollector, List<String> allElementNames, String firstSortElement) {

		var controlElementName = firstSortElement;
		// if the first sort key element name is contained in the list of all elements, there is no prefix/suffix
		// mismatch, but we might be able to distill a suffix later if the user requested to do so
		if (!allElementNames.contains(firstSortElement)) {
			if (setRecordPrefixAndOrSuffix(context, allElementNames, firstSortElement)) {
				return null;
			} else {
				throw new IllegalStateException("logic error: cannot derive prefix/suffix for " + recordDataCollector.getName(context) + " (VIA/DIRECT)");
			}
		}
		return controlElementName;
	}
	
	private String getElementNameIgnoringConditionName(String line) {
		var p = line.substring(2).trim(); // get rid of the comment indicator
		// let's see if the first 2 characters are an element level number...
		if (p.length() > 3 && p.charAt(0) != ' ' && p.charAt(1) != ' ' && p.charAt(2) == ' ') {
			try {
				var level = Integer.parseInt(p.substring(0, 2));
				if (level >= 2 && level <= 49) {							
					// this line contains an element name; we need that, but we skip level 88 (condition
					// names) and FILLER elements
					var elementName = p.substring(3);
					if (!elementName.equals(FILLER)) {
						return elementName;
					}
				}					
			} catch (NumberFormatException e) {
				// ignore
			}
		}
		return null;
	}
	
	private int getDigitCount(SchemaSyntaxWrapper context, IRecordDataCollector<SchemaSyntaxWrapper> recordDataCollector,
			String controlElementName) {
		
		var use3DigitsForMissingSuffixes = dataEntryContext.<Boolean>getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_3_DIGITS).booleanValue();
		var use4DigitsForMissingSuffixes = dataEntryContext.<Boolean>getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_4_DIGITS).booleanValue();
		var promptForDigitCount = dataEntryContext.<Boolean>getAttribute(SyntaxContextAttributeKeys.DIGIT_COUNT_FOR_MISSING_SUFFIXES_PROMPT).booleanValue();
		var digitCount = -1;
		if (use3DigitsForMissingSuffixes) {
			digitCount = 3;
		} else if (use4DigitsForMissingSuffixes) {
			digitCount = 4;
		} else if (promptForDigitCount) {
			if (promptForDigitCountResolver != null) {
				// get the digit count from the resolver (TEST circumstances only)
				digitCount = promptForDigitCountResolver.getDigitCount(recordDataCollector.getName(context));
			} else {
				// prompt for digit count
				var dialog = new PromptForDigitCountDialog(Display.getCurrent().getActiveShell(), context, recordDataCollector, controlElementName);			
				if (dialog.open() == IDialogConstants.CANCEL_ID) {
					throw new IllegalStateException("Import cancelled.");
				}
				digitCount = dialog.getSelectedDigitCount();
			}
		}
		return digitCount;
	}
	
	private boolean setRecordPrefixAndOrSuffix(SchemaSyntaxWrapper context, List<String> allElementNames, String modelElementName) {
		if (putPrefixAndSuffixInContext(context, allElementNames, modelElementName)) {
			return true;
		}
		
		// we could not derive a prefix nor suffix and yet we expect one... a very last attempt in matching the
		// key element name passed to an element in the record is to try to detect a suffix in the base names		
		String baseSuffix = null;
		for (var baseElementName : allElementNames) {
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
		context.getProperties().put(BASE_SUFFIX, baseSuffix);
		context.getProperties().put(CONTAINS_BASE_NAMES_FLAG, String.valueOf(Boolean.TRUE));
		// now try to derive the suffix again (forget about a prefix, we will not supporting that here in order
		// not to make things too complicated)
		var j = modelElementName.lastIndexOf("-");
		if (j < 0) {
			// no suffix
			return true;
		}
		var suffix = modelElementName.substring(j);
		for (var baseElementName : allElementNames) {
			var i = baseElementName.lastIndexOf(baseSuffix);
			var elementName = baseElementName.substring(0, i);
			if (elementName.equals(modelElementName)) {
				// no suffix since we have a perfect match				
				return true;
			}
			if (modelElementName.equals(elementName + suffix)) {
				// we can confirm the suffix; set it in the context
				context.getProperties().put(SUFFIX, suffix);
				return true;
			}
		}
		return false; // no suffix: logic error
	}
	
	private boolean putPrefixAndSuffixInContext(SchemaSyntaxWrapper context, List<String> allElementNames, String modelElementName) {
		for (var elementName : allElementNames) {
			if (elementName.equals(modelElementName)) {
				// no prefix or suffix since we have a perfect match
				setContainsBaseNamesFlag(context, null, null);
				return true;
			}
			var prefixAndSuffix = computePrefixAndSuffix(modelElementName, elementName);
			if (putPrefixAndSuffixInContext(context, prefixAndSuffix, modelElementName, elementName)) {
				return true;
			}
		}		
		return false;
	}
	
	private Pair<String> computePrefixAndSuffix(String modelElementName, String elementName) {
		var i = modelElementName.indexOf("-" + elementName); 
		var j = modelElementName.indexOf(elementName + "-");
		String prefix = null;
		String suffix = null;
		if (i > -1) {
			var p = modelElementName.substring(0, i + 1);
			// only allow for 1 hyphen in the prefix
			if (p.substring(0, p.length() -1).indexOf("-") == -1) {
				prefix = p;
			}
		}
		if (j > -1) {
			j += elementName.length();
			var p = modelElementName.substring(j);
			// only allow for 1 hyphen in the suffix
			if (p.indexOf("-", 1) == -1) {
				suffix = p;
			}
		}
		return new Pair<>(prefix, suffix);
	}
	
	private boolean putPrefixAndSuffixInContext(SchemaSyntaxWrapper context, Pair<String> prefixAndSuffix,
			String modelElementName, String elementName) {
		
		var prefix = prefixAndSuffix.left();
		var suffix = prefixAndSuffix.right();			
		if (prefix != null || suffix != null) {
			// check if we've found the right prefix and/or suffix...
			var p = new StringBuilder();
			if (prefix != null) {
				p.append(prefix);
			}
			p.append(elementName);
			if (suffix != null) {
				p.append(suffix);
			}
			if (p.toString().equals(modelElementName)) {
				// if a prefix and/or suffix could be derived, we need to store them in the context and we're done
				if (prefix != null) {
					context.getProperties().put(PREFIX, prefix);
				}
				if (suffix != null) {
					context.getProperties().put(SUFFIX, suffix);
				}
				setContainsBaseNamesFlag(context, prefix, suffix);
				return true;
			}
		}
		return false;
	}

	private void setContainsBaseNamesFlag(SchemaSyntaxWrapper context, String prefix, String suffix) {
		var allElementNames = new ArrayList<String>();
		var inElementSyntax = false;
		for (var line : context.getLines()) {
			if (line.trim().equals(".")) {
				inElementSyntax = true;
			} else if (inElementSyntax) {
				var p = line.substring(2).trim();
				if ((p.startsWith("0") || p.startsWith("88")) && p.charAt(2) == ' ') {					
					String elementName = p.substring(3);
					if (!elementName.equals(FILLER)) {
						// skip FILLER elements
						allElementNames.add(elementName);
					}
				}
			}
		}
		var containsBaseNamesFlag = calculateContainsBaseNamesFlag(prefix, suffix, allElementNames);
		context.getProperties().put(CONTAINS_BASE_NAMES_FLAG, String.valueOf(containsBaseNamesFlag));
	}
	
	private boolean calculateContainsBaseNamesFlag(String prefix, String suffix, List<String> allElementNames) {
		if (prefix != null && suffix != null) {
			return allElementNames.stream()
					.anyMatch(elementName -> !elementName.startsWith(prefix) || !elementName.endsWith(suffix));
		} else if (prefix != null) {
			return allElementNames.stream()
					.anyMatch(elementName -> !elementName.startsWith(prefix));
		} else if (suffix != null) {
			return allElementNames.stream()
					.anyMatch(elementName -> !elementName.endsWith(suffix));
		}
		return false;
	}
	
	/**
	 * Provides a replacement for PromptForDigitCountDialog; sould only be set when running in a TEST environment
	 * (e.g. when running JUnit tests).  MIGHT be interesting to provide IRL situations as well though...
	 * @param newValue the simulator for PromptForDigitCountDialog
	 */
	public void setPromptForDigitCountResolver(IPromptForDigitCountResolver newValue) {
		promptForDigitCountResolver = newValue;
	}
	
	private static record DataCollectorData(String schemaDescription, String memoDate, List<String> comments) {
	}

}
