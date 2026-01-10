/**
 * Copyright (C) 2026  Luc Hermans
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
package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.common.ElementValueTransformer;
import org.lh.dmlj.schema.editor.importtool.IAreaDataCollector;
import org.lh.dmlj.schema.editor.importtool.IDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;
import org.lh.dmlj.schema.editor.log.Logger;

public final class SchemaImportToolProxy {
	private static final String ONE_MEMBER_RECORD_NAME_EXPECTED = "1 member record name expected: ";
	private static final String DDLCATLOD = "DDLCATLOD";
	private static final String DDLDCLOD = "DDLDCLOD";

	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
		
	private final ISchemaImportTool tool; // the import tool
	private final IDataEntryContext dataEntryContext; // the context holding all data entered in the wizard's pages
	private final Properties importToolParameters; // optional parameters configured in the import tool's defining plug-in	
	private final List<String> compressionProcedures; // the list of procedures that are used to compress records
	private ModelFactory modelFactory; // a factory for the model objects	
	private Schema schema; // the schema that we are building	
	private IDataCollectorRegistry dataCollectorRegistry = new DataCollectorRegistry(); // our data collector registry	
	private boolean importToolIsDisposed = false; // an indicator to track whether the import tool's dispose() method was called

	public SchemaImportToolProxy(ISchemaImportTool tool, IDataEntryContext dataEntryContext, Properties importToolParameters) {
		this.tool = tool;
		this.dataEntryContext = dataEntryContext;
		this.importToolParameters = importToolParameters;
		
		// get the list of compression procedure names (IDMSCOMP is no longer considered by default a compression
		// routine unless it is specified as such in the preferences)
		compressionProcedures = dataEntryContext.getAttribute(GeneralContextAttributeKeys.COMPRESSION_PROCEDURE_NAMES);
	}
	
	private boolean containsOccursDependingOnField(SchemaRecord schemaRecord) {
		return schemaRecord.getElements().stream()
				.anyMatch(element -> element.getOccursSpecification() != null && element.getOccursSpecification().getDependingOn() != null);
	}
	
	public void disposeImportTool() {
		if (isImportToolDisposed()) {
			throw new IllegalStateException("import tool is already disposed");
		}
		tool.dispose();
		importToolIsDisposed = true;
	}

	private StorageMode getStorageMode(SchemaRecord schemaRecord) {
		var p = new StringBuilder();
		if (!containsOccursDependingOnField(schemaRecord)) {
			p.append("FIXED");
		} else {
			p.append("VARIABLE");
		}
		if (isRecordCompressed(schemaRecord)) {
			p.append(" COMPRESSED");
		}
		return switch (p.toString()) {
			case "FIXED" -> StorageMode.FIXED;
			case "FIXED COMPRESSED" -> StorageMode.FIXED_COMPRESSED;
			case "VARIABLE" -> StorageMode.VARIABLE;
			default -> StorageMode.VARIABLE_COMPRESSED;
		};
	}	
	
	private void handleArea(Object areaContext) {
		@SuppressWarnings("unchecked")
		var dataCollector = (IAreaDataCollector<Object>) dataCollectorRegistry.getAreaDataCollector(areaContext.getClass());
		var areaName = dataCollector.getName(areaContext);
		logger.debug("importing area " + areaName + "...");
				
		var area = modelFactory.createArea(areaName);
				
		var procedureNames = List.copyOf(dataCollector.getProceduresCalled(areaContext));
		logger.debug("  (" + procedureNames.size() + ") procedures called: " + procedureNames);
		var procedureCallTimes = new ArrayList<>(dataCollector.getProcedureCallTimes(areaContext));
		logger.debug("  (" + procedureCallTimes.size() + ") procedure call times: " + procedureCallTimes);
		var procedureCallFunctions = new ArrayList<>(dataCollector.getProcedureCallFunctions(areaContext));
		logger.debug("  (" + procedureCallFunctions.size() + ") procedure call functions: " + procedureCallFunctions);
		Assertions.isEqualInSize(procedureCallTimes,  procedureNames, "#procedure call times != #procedures called");
		Assertions.isEqualInSize(procedureCallFunctions,  procedureNames, "#procedure call functions != #procedures called");		
		for (int i = 0; i < procedureNames.size(); i++) {
			modelFactory.createProcedureCallSpecification(area, procedureNames.get(i), procedureCallTimes.get(i), procedureCallFunctions.get(i));			
		}
	}

	private void handleChainedSet(Object setContext) {
		@SuppressWarnings("unchecked")
		var dataCollector = (ISetDataCollector<Object>) dataCollectorRegistry.getSetDataCollector(setContext.getClass());
		var setName = dataCollector.getName(setContext);
		logger.debug("importing chained set " + setName + "...");
				
		var set = modelFactory.createSet(setName, SetMode.CHAINED, dataCollector.getSetOrder(setContext));		
				
		var ownerRecordName = dataCollector.getOwnerRecordName(setContext);		
		var ownerRole = modelFactory.createSetOwner(set, ownerRecordName);
		var schemaRecord = ownerRole.getRecord();
		
		// set the mandatory next dbkey position
		var nextDbkeyPosition = Short.valueOf(dataCollector.getOwnerNextDbkeyPosition(setContext));
		Assertions.isFreeDbkeyPosition(schemaRecord, nextDbkeyPosition.shortValue());
		ownerRole.setNextDbkeyPosition(nextDbkeyPosition.shortValue());
		
		// set the optional prior dbkey position
		var priorDbkeyPosition = dataCollector.getOwnerPriorDbkeyPosition(setContext);
		if (priorDbkeyPosition != null) {
			Assertions.isFreeDbkeyPosition(schemaRecord,  priorDbkeyPosition.shortValue());
			ownerRole.setPriorDbkeyPosition(priorDbkeyPosition);
		}		
	
		// process the set members; if a record is stored VIA this set, the VIA specification will be connected
		// to this set when the member role is created for that record		
		var memberRecordNames = dataCollector.getMemberRecordNames(setContext);
		Assertions.isCollectionNotEmpty(memberRecordNames, "at least 1 member record name expected: " + set.getName());		
		for (var memberRecordName : memberRecordNames) {
			var membershipOption = dataCollector.getSetMembershipOption(setContext, memberRecordName);
			var memberRole = modelFactory.createSetMember(set, memberRecordName, membershipOption);
			schemaRecord = memberRole.getRecord();
													
			// set the mandatory next dbkey position
			nextDbkeyPosition = dataCollector.getMemberNextDbkeyPosition(setContext, memberRecordName);
			Assertions.isNotNull(nextDbkeyPosition, "next dbkey position is mandatory");
			Assertions.isFreeDbkeyPosition(schemaRecord, nextDbkeyPosition.shortValue());
			memberRole.setNextDbkeyPosition(nextDbkeyPosition);
			
			// set the optional prior dbkey position
			priorDbkeyPosition = dataCollector.getMemberPriorDbkeyPosition(setContext, memberRecordName);
			if (priorDbkeyPosition != null) {
				Assertions.isFreeDbkeyPosition(schemaRecord, priorDbkeyPosition.shortValue());
			}
			memberRole.setPriorDbkeyPosition(priorDbkeyPosition);
			
			// set the optional owner dbkey position
			var ownerDbkeyPosition = dataCollector.getMemberOwnerDbkeyPosition(setContext, memberRecordName);
			if (ownerDbkeyPosition != null) {
				Assertions.isFreeDbkeyPosition(schemaRecord, ownerDbkeyPosition.shortValue());
			}
			memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition);			
			
			// deal with the sort key, if applicable
			if (set.getOrder() == SetOrder.SORTED) {			
				handleSortKey(setContext, memberRole);
			}
		}
	}
	
	private void handleDDLCATLOD() {
		logger.debug("handling DDLCATLOD...");
		copyProceduresFromDDLDCLOD();
		copyRecordsFromDDLDCLOD();
		copySetsFromDDLDCLOD();
	}
	
	private void copyProceduresFromDDLDCLOD() {
		var area = modelFactory.createArea(DDLCATLOD);
		for (var originalSpec : schema.getArea(DDLDCLOD).getProcedures()) {
			modelFactory.createProcedureCallSpecification(area, originalSpec.getProcedure().getName(),
					originalSpec.getCallTime(), originalSpec.getFunction());
		}
	}
	
	private void copyRecordsFromDDLDCLOD() {
		for (var originalRecord : schema.getArea(DDLDCLOD).getRecords()) {
			var viaSetName = originalRecord.getViaSpecification() != null ? originalRecord.getViaSpecification().getSet().getName()  + "_" : null;
			
			var schemaRecord = modelFactory.createRecord(originalRecord.getName() + "_", originalRecord.getId(),
					originalRecord.getStorageMode(), originalRecord.getLocationMode(), viaSetName, DDLCATLOD, true);
			
			// set the base name and version
			schemaRecord.setBaseName(originalRecord.getBaseName());
			schemaRecord.setBaseVersion(originalRecord.getBaseVersion());
			
			// set the synonym name and version
			schemaRecord.setSynonymName(originalRecord.getSynonymName());
			schemaRecord.setSynonymVersion(originalRecord.getSynonymVersion());
			
			cloneElements(originalRecord, schemaRecord);
						
			if (schemaRecord.getStorageMode() != StorageMode.FIXED) {
				schemaRecord.setMinimumRootLength(originalRecord.getMinimumRootLength());
				schemaRecord.setMinimumFragmentLength(originalRecord.getMinimumFragmentLength());
			}
					
			if (schemaRecord.getLocationMode() == LocationMode.CALC) {
				cloneCalcKey(originalRecord, schemaRecord);
			} else if (schemaRecord.getLocationMode() == LocationMode.VIA) {
				cloneViaSpecification(originalRecord, schemaRecord);
			}
			
			cloneOffsetExpression(originalRecord, schemaRecord);
			
			// deal with record procedures
			for (var originalSpec : originalRecord.getProcedures()) {	
				modelFactory.createProcedureCallSpecification(schemaRecord, originalSpec.getProcedure().getName(),
						originalSpec.getCallTime(), originalSpec.getVerb());
			}
		}
	}
	
	private void cloneElements(SchemaRecord sourceRecord, SchemaRecord targetRecord) {
		for (var originalElement : sourceRecord.getElements()) {
			var parent = originalElement.getParent() != null ? targetRecord.getElement(originalElement.getParent().getName()) : null;
							
			var element = modelFactory.createElement(targetRecord, parent, originalElement.getName(), originalElement.getBaseName());
			element.setBaseName(originalElement.getBaseName());
			element.setLevel(originalElement.getLevel());		
			element.setUsage(originalElement.getUsage());						
			element.setPicture(originalElement.getPicture());		
			element.setNullable(originalElement.isNullable());
			
			cloneRedefinesClause(originalElement, element);
			cloneOccursSpecification(originalElement, element);
		}		
	}
	
	private void cloneRedefinesClause(Element sourceElement, Element targetElement) {
		var targetRecord = targetElement.getRecord();
		var redefinedElementName = sourceElement.getRedefines() != null ? sourceElement.getRedefines().getName() : null;
		if (redefinedElementName != null) {			
			var redefinedElement = targetRecord.getElement(redefinedElementName);
			if (redefinedElement == null) {
				throw new IllegalStateException("logic error: element " + targetElement.getName() + " redefines " +
						redefinedElementName + ", but " + redefinedElementName + " was not found in the record");
			} else if (redefinedElement.getLevel() != targetElement.getLevel()) {
				throw new IllegalStateException("logic error: element " + targetElement.getName() + " redefines " +
						redefinedElementName + ", but " + targetElement.getName() + "'s level number (" +
						targetElement.getLevel() + ") does not match that of " + redefinedElementName + " (" +
						redefinedElement.getLevel() + ")");
			}
			targetElement.setRedefines(redefinedElement);
		}
	}
	
	private void cloneOccursSpecification(Element sourceElement, Element targetElement) {
		var targetRecord = targetElement.getRecord();
		if (sourceElement.getOccursSpecification() != null) {
			var originalOccursSpecification = sourceElement.getOccursSpecification();
			var occursSpecification = modelFactory.createOccursSpecification(targetElement);
			
			occursSpecification.setCount(originalOccursSpecification.getCount());
			var dependsOnElementName = originalOccursSpecification.getDependingOn() != null ? originalOccursSpecification.getDependingOn().getName() : null;
			if (dependsOnElementName != null) {
				var dependsOnElement = targetRecord.getElement(dependsOnElementName);
				if (dependsOnElement == null) {
					throw new IllegalStateException("logic error: element " + targetElement.getName() +
							"'s occurs-depending-on-element, " + dependsOnElementName + ", was not found in the record");
				}
				occursSpecification.setDependingOn(dependsOnElement);
			}
		}		
	}
	
	private void cloneCalcKey(SchemaRecord sourceRecord, SchemaRecord targetRecord) {
		var duplicatesOption = sourceRecord.getCalcKey().getDuplicatesOption();
		var naturalSequence = sourceRecord.getCalcKey().isNaturalSequence();
		var key = modelFactory.createKey(targetRecord, duplicatesOption, naturalSequence);
		for (var originalKeyElement : sourceRecord.getCalcKey().getElements()) {
			modelFactory.createKeyElement(key, originalKeyElement.getElement().getName(), SortSequence.ASCENDING);
		}
	}
	
	private void cloneViaSpecification(SchemaRecord sourceRecord, SchemaRecord targetRecord) {
		var symbolicDisplacementName = sourceRecord.getViaSpecification().getSymbolicDisplacementName();
		var displacementPageCount = sourceRecord.getViaSpecification().getDisplacementPageCount();
		
		var viaSpecification = targetRecord.getViaSpecification();
		if (symbolicDisplacementName != null) {
			viaSpecification.setSymbolicDisplacementName(symbolicDisplacementName);
		} else if (displacementPageCount != null) {
			viaSpecification.setDisplacementPageCount(displacementPageCount);
		}
	}
	
	private void cloneOffsetExpression(SchemaRecord sourceRecord, SchemaRecord targetRecord) {
		var symbolicSubareaName = sourceRecord.getAreaSpecification().getSymbolicSubareaName();
		var offsetPageCount = sourceRecord.getAreaSpecification().getOffsetExpression() != null ?
				sourceRecord.getAreaSpecification().getOffsetExpression().getOffsetPageCount() : null;
		var offsetPercent = sourceRecord.getAreaSpecification().getOffsetExpression() != null ?
				sourceRecord.getAreaSpecification().getOffsetExpression().getOffsetPercent() : null;
		var pageCount = sourceRecord.getAreaSpecification().getOffsetExpression() != null ?
				sourceRecord.getAreaSpecification().getOffsetExpression().getPageCount() : null;
		var percent = sourceRecord.getAreaSpecification().getOffsetExpression() != null ?
				sourceRecord.getAreaSpecification().getOffsetExpression().getPercent() : null;
		modelFactory.createOffsetExpression(targetRecord.getAreaSpecification(), symbolicSubareaName,
				offsetPageCount, offsetPercent, pageCount, percent);		
	}
	
	private void copySetsFromDDLDCLOD() {
		var setNames =Stream.concat(
				schema.getSets().stream()
						.filter(set -> set.getOwner() != null)
						.filter(set -> set.getOwner().getRecord().getAreaSpecification().getArea().getName().equals(DDLDCLOD))
						.map(Set::getName),
				schema.getSets().stream()
						.flatMap(set -> set.getMembers().stream())
						.filter(memberRole -> memberRole.getRecord().getAreaSpecification().getArea().getName().equals(DDLDCLOD))
						.map(MemberRole::getSet)
						.map(Set::getName))
				.distinct()
				.toList();
		
		// copy the sets from those involved in the DDLDCLOD area
		for (var setName : setNames) {
			// get the original DDLDCLOD set; it should NOT be an indexed set, nor a sorted set (the DDLDCLOD
			// area does not contain any of these)
			var originalSet = schema.getSet(setName);
			if (originalSet.getMode() != SetMode.CHAINED) {
				throw new IllegalStateException("only chained sets expected in the DDLDCLOD area; cannot create DDLCATLOD entities");
			}
			if (originalSet.getOrder() == SetOrder.SORTED) {
				throw new IllegalStateException("no sorted sets expected in the DDLDCLOD area; cannot create DDLCATLOD entities");
			}
						
			var set = modelFactory.createSet(setName + "_", originalSet.getMode(), originalSet.getOrder());
			var ownerRecordName = originalSet.getOwner().getRecord().getName() + "_";
			var ownerRole = modelFactory.createSetOwner(set, ownerRecordName, true);
			var schemaRecord = ownerRole.getRecord();
			
			// set the mandatory next dbkey position
			var nextDbkeyPosition = Short.valueOf(originalSet.getOwner().getNextDbkeyPosition());
			Assertions.isFreeDbkeyPosition(schemaRecord, nextDbkeyPosition.shortValue());
			ownerRole.setNextDbkeyPosition(nextDbkeyPosition.shortValue());
			
			// set the optional prior dbkey position
			var priorDbkeyPosition = originalSet.getOwner().getPriorDbkeyPosition();
			if (priorDbkeyPosition != null) {
				Assertions.isFreeDbkeyPosition(schemaRecord, priorDbkeyPosition.shortValue());
				ownerRole.setPriorDbkeyPosition(priorDbkeyPosition);
			}				
			
			// process the set members; if a record is stored VIA this set, the VIA specification will be
			// connected to this set when the member role is created for that record
			for (var originalMemberRole : originalSet.getMembers()) {
				var membershipOption = originalMemberRole.getMembershipOption();
				var memberRecordName = originalMemberRole.getRecord().getName() + "_";
				var memberRole = modelFactory.createSetMember(set, memberRecordName, membershipOption, true);
				schemaRecord = memberRole.getRecord();
														
				// set the mandatory next dbkey position
				nextDbkeyPosition = originalMemberRole.getNextDbkeyPosition();
				Assertions.isNotNull(nextDbkeyPosition, "next dbkey position is mandatory");
				Assertions.isFreeDbkeyPosition(schemaRecord, nextDbkeyPosition.shortValue());
				memberRole.setNextDbkeyPosition(nextDbkeyPosition);
				
				// set the optional prior dbkey position
				priorDbkeyPosition = originalMemberRole.getPriorDbkeyPosition();
				if (priorDbkeyPosition != null) {
					Assertions.isFreeDbkeyPosition(schemaRecord, priorDbkeyPosition.shortValue());
				}
				memberRole.setPriorDbkeyPosition(priorDbkeyPosition);
				
				// set the optional owner dbkey position
				var ownerDbkeyPosition = originalMemberRole.getOwnerDbkeyPosition();
				if (ownerDbkeyPosition != null) {
					Assertions.isFreeDbkeyPosition(schemaRecord, ownerDbkeyPosition.shortValue());
				}
				memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition);	
			}			
		}
	}

	private void handleElement(SchemaRecord schemaRecord, Element parent, Object elementContext) {
		@SuppressWarnings("unchecked")
		var dataCollector = (IElementDataCollector<Object>) dataCollectorRegistry.getElementDataCollector(elementContext.getClass());
				
		var elementName = dataCollector.getName(elementContext);
		logger.debug("importing element " + elementName + "...");
		var baseName = dataCollector.getBaseName(elementContext);		
		
		var element = modelFactory.createElement(schemaRecord, parent, elementName, baseName);
		element.setLevel(dataCollector.getLevel(elementContext));		
		element.setUsage(dataCollector.getUsage(elementContext));						
		element.setPicture(dataCollector.getPicture(elementContext));		
		element.setNullable(dataCollector.getIsNullable(elementContext));
		
		handleRedefinesClause(element, elementContext, dataCollector);
		handleOccursSpecification(element, elementContext, dataCollector);
		
		// set the element value, if any
		var values = dataCollector.getValues(elementContext);
		element.setValue(ElementValueTransformer.toValueString(values));
		
		// deal with the element's subordinate elements, if any
		for (var childElementContext : tool.getSubordinateElementContexts(elementContext)) {
			handleElement(schemaRecord, element, childElementContext);			
		}		
	}
	
	private void handleRedefinesClause(Element element, Object elementContext, IElementDataCollector<Object> dataCollector) {
		var schemaRecord = element.getRecord();
		var redefinedElementName = dataCollector.getRedefinedElementName(elementContext);
		if (redefinedElementName != null) {			
			var redefinedElement = schemaRecord.getElement(redefinedElementName.toUpperCase());
			if (redefinedElement == null) {
				throw new IllegalStateException("logic error: element " + element.getName() + " redefines " +
						redefinedElementName + ", but " + redefinedElementName + " was not found in the record");
			} else if (redefinedElement.getLevel() != element.getLevel()) {
				throw new IllegalStateException("logic error: element " + element.getName() + " redefines " +
						redefinedElementName + ", but " + element.getName() + "'s level number (" + element.getLevel() +
						") does not match that of " + redefinedElementName + " (" + redefinedElement.getLevel() + ")");
			}
			element.setRedefines(redefinedElement);
		}
	}
	
	private void handleOccursSpecification(Element element, Object elementContext, IElementDataCollector<Object> dataCollector) {
		var schemaRecord = element.getRecord();
		var occurrenceCount = dataCollector.getOccurrenceCount(elementContext);
		if (occurrenceCount > 1) {
			var occursSpecification = modelFactory.createOccursSpecification(element);
			occursSpecification.setCount(occurrenceCount);
			var dependsOnElementName = dataCollector.getDependsOnElementName(elementContext);
			if (dependsOnElementName != null) {
				var dependsOnElement = schemaRecord.getElement(dependsOnElementName.toUpperCase());
				if (dependsOnElement == null) {
					throw new IllegalStateException("logic error: element " + element.getName() +
							"'s occurs-depending-on-element, " + dependsOnElementName + ", was not found in the record");
				}
				occursSpecification.setDependingOn(dependsOnElement);
			}
			
			var indexElementBaseNames = new ArrayList<>(dataCollector.getIndexElementBaseNames(elementContext));
			if (!indexElementBaseNames.isEmpty()) {
				var indexElementNames = new ArrayList<>(dataCollector.getIndexElementNames(elementContext));
				if (indexElementBaseNames.size() != indexElementNames.size()) {
					throw new IllegalStateException("indexElementBaseNames.size() != indexElementNames.size(): " +
							indexElementBaseNames.size() + " " + indexElementNames.size());
				}
				for (var i = 0; i < indexElementBaseNames.size(); i++) {
					var indexElement = SchemaFactory.eINSTANCE.createIndexElement();
					occursSpecification.getIndexElements().add(indexElement);
					indexElement.setBaseName(indexElementBaseNames.get(i));
					indexElement.setName(indexElementNames.get(i));					
				}
			}
		}		
	}
	
	private void handleRecord(Object recordContext) {
		@SuppressWarnings("unchecked")
		var dataCollector = (IRecordDataCollector<Object>) dataCollectorRegistry.getRecordDataCollector(recordContext.getClass());
		var recordName = dataCollector.getName(recordContext);
		logger.debug("importing record " + recordName + "...");
				
		var recordId = dataCollector.getRecordId(recordContext);
		var locationMode = dataCollector.getLocationMode(recordContext);		
		var viaSetName = locationMode == LocationMode.VIA ? dataCollector.getViaSetName(recordContext) : null;
		var areaName = dataCollector.getAreaName(recordContext);
		
		// create the record in the schema; we supply the VIA set name here, if applicable, so that the model
		// factory can resolve the VIA set when it is created later on we always specify a storage mode of FIXED
		// and will correct that later
		var schemaRecord = modelFactory.createRecord(recordName, recordId, StorageMode.FIXED, locationMode, viaSetName, areaName);
		
		var baseName = dataCollector.getBaseName(recordContext);
		var baseVersion = dataCollector.getBaseVersion(recordContext);		
		schemaRecord.setBaseName(baseName);
		schemaRecord.setBaseVersion(baseVersion);
				
		var synonymName = dataCollector.getSynonymName(recordContext);
		var synonymVersion = dataCollector.getSynonymVersion(recordContext);
		schemaRecord.setSynonymName(synonymName);
		schemaRecord.setSynonymVersion(synonymVersion);
		
		// deal with record procedures
		var procedureNames = new ArrayList<>(dataCollector.getProceduresCalled(recordContext));
		logger.debug("  (" + procedureNames.size() + ") procedures called: " + procedureNames);
		var procedureCallTimes = new ArrayList<>(dataCollector.getProcedureCallTimes(recordContext));
		logger.debug("  (" + procedureCallTimes.size() + ") procedure call times: " + procedureCallTimes);
		var procedureCallVerbs = new ArrayList<>(dataCollector.getProcedureCallVerbs(recordContext));
		logger.debug("  (" + procedureCallVerbs.size() + ") procedure call verbs: " + procedureCallVerbs);
		Assertions.isEqualInSize(procedureCallTimes,  procedureNames, "#procedure call times != #procedures called (record=" + recordName + ")");
		Assertions.isEqualInSize(procedureCallVerbs,  procedureNames, "#procedure call verbs != #procedures called (record=" + recordName + ")");
		for (var i = 0; i < procedureNames.size(); i++) {
			modelFactory.createProcedureCallSpecification(schemaRecord, procedureNames.get(i), procedureCallTimes.get(i), procedureCallVerbs.get(i));
		}		
		
		// add the (validated) elements to the record
		var elementContexts = tool.getRootElementContexts(recordContext).elements();
		logger.debug("importing " + elementContexts.size() + " root elements for " + recordName + "...");
		for (var elementContext : elementContexts) {					
			handleElement(schemaRecord, null, elementContext);									
		}
		
		// correct the record's storage mode
		schemaRecord.setStorageMode(getStorageMode(schemaRecord));
				
		// set the minimum root and fragment lengths (regardless of the record's storage mode)
		schemaRecord.setMinimumRootLength(dataCollector.getMinimumRootLength(recordContext));
		schemaRecord.setMinimumFragmentLength(dataCollector.getMinimumFragmentLength(recordContext));
			
		if (schemaRecord.getLocationMode() == LocationMode.CALC || schemaRecord.getLocationMode() == LocationMode.VSAM_CALC) {
			handleCalcKey(schemaRecord, recordContext, dataCollector);
		} else if (schemaRecord.getLocationMode() == LocationMode.VIA) {
			handleViaSpecification(schemaRecord, recordContext, dataCollector);
		} else if (schemaRecord.getLocationMode() == LocationMode.VSAM || schemaRecord.getLocationMode() == LocationMode.VSAM_CALC) {
			handleVsamType(schemaRecord, recordContext, dataCollector);		
		}
	
		// deal with the record's offset expression
		if (isIdmsntwk() && (schemaRecord.getName().equals("OOAK-012")) && isOptionCompleteOoak012() ||
			schemaRecord.getName().equals("LOOAK-155") && isOptionCompleteLooak155()) {
	
			// set the offset for OOAK-012 and LOOAK-155 in the case of an IDMSNTWK version 1 schema AND if the
			// user wants us to do this
			modelFactory.createOffsetExpression(schemaRecord.getAreaSpecification(), null, Integer.valueOf(1),
					null, Integer.valueOf(1), null);			
		} else {	
			// not an IDMSNTWK version 1 schema or the user does not want us to set the offset expression for
			// OOAK-012 and LOOAK-155; get the offset expression data from the import tool implementation and, if
			// applicable, create the offset expression
			var symbolicSubareaName = dataCollector.getSymbolicSubareaName(recordContext);
			var offsetPageCount = dataCollector.getOffsetOffsetPageCount(recordContext);
			var offsetPercent = dataCollector.getOffsetOffsetPercent(recordContext);
			var pageCount = dataCollector.getOffsetPageCount(recordContext);
			var percent = dataCollector.getOffsetPercent(recordContext);
			modelFactory.createOffsetExpression(schemaRecord.getAreaSpecification(), symbolicSubareaName,
					offsetPageCount, offsetPercent, pageCount, percent);
		}
	}
	
	private void handleCalcKey(SchemaRecord schemaRecord, Object recordContext, IRecordDataCollector<Object> dataCollector) {
		var duplicatesOption = dataCollector.getCalcKeyDuplicatesOption(recordContext);
		if (schemaRecord.getLocationMode() == LocationMode.VSAM_CALC) {
			Assert.isTrue(duplicatesOption == DuplicatesOption.NOT_ALLOWED || duplicatesOption == DuplicatesOption.UNORDERED,
					"Duplicates option invalid for VSAMs: " + duplicatesOption + " (record=)" + schemaRecord.getName());
		} else {
			Assert.isTrue(duplicatesOption != DuplicatesOption.UNORDERED, "Duplicates option invalid for non-VSAMs: " +
					duplicatesOption +"(record=)" + schemaRecord.getName());
		}
		// always set the naturalSequence attribute to false; this attribute is not applicable to CALC keys
		var key = modelFactory.createKey(schemaRecord, duplicatesOption, false);
		for (var elementName : dataCollector.getCalcKeyElementNames(recordContext)) {
			modelFactory.createKeyElement(key, elementName, SortSequence.ASCENDING);					
		}		
	}
	
	private void handleViaSpecification(SchemaRecord schemaRecord, Object recordContext, IRecordDataCollector<Object> dataCollector) {
		var symbolicDisplacementName = dataCollector.getViaSymbolicDisplacementName(recordContext);
		var displacementPageCount = dataCollector.getViaDisplacementPageCount(recordContext);
		var viaSpecification = schemaRecord.getViaSpecification();					
		if (symbolicDisplacementName != null) {
			viaSpecification.setSymbolicDisplacementName(symbolicDisplacementName);
		} else if (displacementPageCount != null) {
			viaSpecification.setDisplacementPageCount(displacementPageCount);
		}		
	}
	
	private void handleVsamType(SchemaRecord schemaRecord, Object recordContext, IRecordDataCollector<Object> dataCollector) {
		var vsamType = SchemaFactory.eINSTANCE.createVsamType();
		vsamType.setRecord(schemaRecord);
		var vsamLengthType = dataCollector.getVsamLengthType(recordContext);
		Assert.isNotNull(vsamLengthType, "VSAM length type is mandatory: " + schemaRecord.getName());
		vsamType.setLengthType(vsamLengthType);
		vsamType.setSpanned(dataCollector.isVsamSpanned(recordContext));
	}

	private void handleSet(Object setContext) {
		@SuppressWarnings("unchecked")
		var dataCollector = (ISetDataCollector<Object>) dataCollectorRegistry.getSetDataCollector(setContext.getClass());
				
		var setMode = dataCollector.getSetMode(setContext);
		Assertions.isNotNull(setMode, "set mode is null (set=" + dataCollector.getName(setContext) + ")");
		if (setMode == SetMode.CHAINED) {
			handleChainedSet(setContext);
		} else if (setMode == SetMode.INDEXED) {
			if (dataCollector.isSystemOwned(setContext)) {
				handleSystemOwnedIndexedSet(setContext);
			} else {
				handleUserOwnedIndexedSet(setContext);
			}
		} else if (setMode == SetMode.VSAM_INDEX) {
			handleVsamIndexSet(setContext);
		} else {
			throw new IllegalStateException("set mode is invalid: " + setMode);
		}
	}
	
	private void handleSortKey(Object setContext, MemberRole memberRole) {
		@SuppressWarnings("unchecked")
		var dataCollector = (ISetDataCollector<Object>) dataCollectorRegistry.getSetDataCollector(setContext.getClass());
		var recordName = memberRole.getRecord().getName();
		
		// determine the duplicates option; in the case of an indexed set, we need to know if the set is sorted
		// by dbkey, because if it is, duplicates are not allowed (a record can never be twice a member of the same set)		
		DuplicatesOption duplicatesOption = null;
		var sortedByDbkey = false;
		if (memberRole.getSet().getMode() == SetMode.INDEXED) {
			sortedByDbkey = dataCollector.isSortedByDbkey(setContext);
		}
		if (sortedByDbkey) {
			duplicatesOption = DuplicatesOption.NOT_ALLOWED;
		} else {
			duplicatesOption = dataCollector.getDuplicatesOption(setContext, recordName);
			var message = "Duplicates option invalid: " + duplicatesOption + " (set=" + memberRole.getSet().getName() + ")";
			if (memberRole.getSet().getMode() == SetMode.VSAM_INDEX) {
				Assert.isTrue(duplicatesOption == DuplicatesOption.NOT_ALLOWED || duplicatesOption == DuplicatesOption.UNORDERED, message);
			} else {
				Assert.isTrue(duplicatesOption != DuplicatesOption.UNORDERED, message);
			}
		}
		
		// get the natural sequence indicator value (always ask the set data collector for its value, even if the
		// set is sorted by dbkey)
		var naturalSequence = dataCollector.getSortKeyIsNaturalSequence(setContext, recordName);
		
		// create the sort key for the set
		var key = modelFactory.createKey(memberRole, duplicatesOption, naturalSequence);
		if (memberRole.getSet().getMode() == SetMode.INDEXED) {
			key.setCompressed(dataCollector.isKeyCompressed(setContext));
		}
				
		if (!sortedByDbkey) {
			// the set is not sorted by dbkey; create a key element for each element in the sort key
			for (var elementName : dataCollector.getSortKeyElements(setContext, recordName)) {
				var sortSequence = dataCollector.getSortSequence(setContext, recordName, elementName);
				modelFactory.createKeyElement(key, elementName, sortSequence);
			}
		} else {	
			// the set is sorted by dbkey; create 1 key element
			var sortSequence = dataCollector.getSortSequence(setContext, recordName, null);
			modelFactory.createKeyElement(key, null, sortSequence);
		}
	}

	private void handleSystemOwnedIndexedSet(Object setContext) {		
		@SuppressWarnings("unchecked")
		var dataCollector = (ISetDataCollector<Object>) dataCollectorRegistry.getSetDataCollector(setContext.getClass());
		var setName = dataCollector.getName(setContext);
		logger.debug("importing system-owned indexed set " + setName + "...");
		
		var set = modelFactory.createSet(setName, SetMode.INDEXED, dataCollector.getSetOrder(setContext));
	
		// create the system owner and add it to its area
		var areaName = dataCollector.getSystemOwnerAreaName(setContext);		
		var systemOwner = modelFactory.createSystemOwner(set, areaName);		
		
		// create the offset expression				
		var offsetPageCount = dataCollector.getSystemOwnerOffsetOffsetPageCount(setContext);
		var offsetPercent = dataCollector.getSystemOwnerOffsetOffsetPercent(setContext);
		var pageCount = dataCollector.getSystemOwnerOffsetPageCount(setContext);
		var percent = dataCollector.getSystemOwnerOffsetPercent(setContext);
		var symbolicSubareaName = dataCollector.getSystemOwnerSymbolicSubareaName(setContext);
		modelFactory.createOffsetExpression(systemOwner.getAreaSpecification(), symbolicSubareaName, offsetPageCount,
				offsetPercent, pageCount, percent);
		
		// create the indexed set mode specification
		var symbolicIndexName = dataCollector.getSymbolicIndexName(setContext);
		var keyCount = symbolicIndexName != null ? null : dataCollector.getKeyCount(setContext);
		var displacementPageCount = symbolicIndexName != null ? null : dataCollector.getDisplacementPageCount(setContext);
		if (displacementPageCount != null && displacementPageCount.shortValue() == 0) {
			displacementPageCount = null;
		}
		modelFactory.createIndexedSetModeSpecification(set, symbolicIndexName, keyCount, displacementPageCount);
		
		// process the (one and only) set member; if the record is stored VIA this set, the VIA specification
		// will be connected to this set when the member role is created
		var memberRecordNames = dataCollector.getMemberRecordNames(setContext);
		Assertions.isSingleElementCollection(memberRecordNames, ONE_MEMBER_RECORD_NAME_EXPECTED + set.getName());
		var memberRecordName = memberRecordNames.iterator().next();
		var membershipOption = dataCollector.getSetMembershipOption(setContext, memberRecordName);
		var memberRole = modelFactory.createSetMember(set, memberRecordName, membershipOption);
		var schemaRecord = memberRole.getRecord();
		
		// set the index dbkey position, which is (only) optional if the set membership option is MANDATORY AUTOMATIC
		var indexDbkeyPosition = dataCollector.getMemberIndexDbkeyPosition(setContext);
		if (membershipOption != SetMembershipOption.MANDATORY_AUTOMATIC) {
			Assertions.isNotNull(indexDbkeyPosition, "index dbkey position is mandatory: " + set.getName());
		}	
		if (indexDbkeyPosition != null) {
			Assertions.isFreeDbkeyPosition(schemaRecord, indexDbkeyPosition.shortValue());
			memberRole.setIndexDbkeyPosition(indexDbkeyPosition);	
		}
		
		// set the optional owner dbkey position
		var ownerDbkeyPosition = dataCollector.getMemberOwnerDbkeyPosition(setContext, memberRecordName);
		if (ownerDbkeyPosition != null) {
			Assertions.isFreeDbkeyPosition(schemaRecord, ownerDbkeyPosition.shortValue());
			memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition);
		}		
		
		// deal with the sort key, if applicable
		if (set.getOrder() == SetOrder.SORTED) {			
			handleSortKey(setContext, memberRole);			
		}		
	}

	private void handleUserOwnedIndexedSet(Object setContext) {
		@SuppressWarnings("unchecked")
		var dataCollector = (ISetDataCollector<Object>) dataCollectorRegistry.getSetDataCollector(setContext.getClass());
		var setName = dataCollector.getName(setContext);
		logger.debug("importing user-owned indexed set " + setName + "...");
		var set = modelFactory.createSet(setName, SetMode.INDEXED, dataCollector.getSetOrder(setContext));
		
		// create the set owner
		var ownerRecordName = dataCollector.getOwnerRecordName(setContext);
		var ownerRole = modelFactory.createSetOwner(set, ownerRecordName);
		var schemaRecord = ownerRole.getRecord();
		
		// set the mandatory next dbkey position
		var nextDbkeyPosition = Short.valueOf(dataCollector.getOwnerNextDbkeyPosition(setContext));
		Assertions.isFreeDbkeyPosition(schemaRecord, nextDbkeyPosition.shortValue());
		ownerRole.setNextDbkeyPosition(nextDbkeyPosition.shortValue());
		
		// set the mandatory prior dbkey position
		var priorDbkeyPosition = dataCollector.getOwnerPriorDbkeyPosition(setContext);
		Assertions.isNotNull(priorDbkeyPosition, "prior dbkey position is null");		
		Assertions.isFreeDbkeyPosition(schemaRecord, priorDbkeyPosition.shortValue());		
		ownerRole.setPriorDbkeyPosition(priorDbkeyPosition);
		
		// create the indexed set mode specification
		var symbolicIndexName = dataCollector.getSymbolicIndexName(setContext);
		var keyCount = symbolicIndexName != null ? null : dataCollector.getKeyCount(setContext);
		var displacementPageCount = symbolicIndexName != null ? null : dataCollector.getDisplacementPageCount(setContext);
		if (displacementPageCount != null && displacementPageCount.shortValue() == 0) {
			displacementPageCount = null;
		}
		modelFactory.createIndexedSetModeSpecification(set, symbolicIndexName, keyCount, displacementPageCount);		
		
		// process the (one and only) set member; if the record is stored VIA this set, the VIA specification
		// will be connected to this set when the member role is created
		var memberRecordNames = dataCollector.getMemberRecordNames(setContext);
		Assertions.isSingleElementCollection(memberRecordNames, ONE_MEMBER_RECORD_NAME_EXPECTED + set.getName());
		var memberRecordName = memberRecordNames.iterator().next();
		var membershipOption = dataCollector.getSetMembershipOption(setContext, memberRecordName);
		var memberRole = modelFactory.createSetMember(set, memberRecordName, membershipOption);
		schemaRecord = memberRole.getRecord();
		
		// set the mandatory index dbkey position									
		var indexDbkeyPosition = dataCollector.getMemberIndexDbkeyPosition(setContext);
		Assertions.isNotNull(indexDbkeyPosition, "index dbkey position is null");
		Assertions.isFreeDbkeyPosition(schemaRecord, indexDbkeyPosition.shortValue());
		memberRole.setIndexDbkeyPosition(indexDbkeyPosition);	
		
		// set the optional index dbkey position
		var ownerDbkeyPosition = dataCollector.getMemberOwnerDbkeyPosition(setContext, memberRecordName);
		if (ownerDbkeyPosition != null) {
			Assertions.isFreeDbkeyPosition(schemaRecord, ownerDbkeyPosition.shortValue());
			memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition);
		}						
		
		// deal with the sort key, if applicable
		if (set.getOrder() == SetOrder.SORTED) {			
			handleSortKey(setContext, memberRole);			
		}
	}

	private void handleVsamIndexSet(Object setContext) {
		@SuppressWarnings("unchecked")
		var dataCollector = (ISetDataCollector<Object>) dataCollectorRegistry.getSetDataCollector(setContext.getClass());
		var setName = dataCollector.getName(setContext);
		logger.debug("importing VSAM index set " + setName + "...");
		
		// create the set, which will always be SORTED
		var set = modelFactory.createSet(setName, SetMode.VSAM_INDEX, SetOrder.SORTED);
		
		// process the (one and only) set member
		var memberRecordNames = dataCollector.getMemberRecordNames(setContext);
		Assertions.isSingleElementCollection(memberRecordNames, ONE_MEMBER_RECORD_NAME_EXPECTED + set.getName());
		var memberRecordName = memberRecordNames.iterator().next();
		var memberRole = modelFactory.createSetMember(set, memberRecordName, SetMembershipOption.MANDATORY_AUTOMATIC);
				
		handleSortKey(setContext, memberRole);
		
		// create a VsamIndex object and have it referenced by the set; we need this type to create edit parts
		// when visualizing the VSAM index
		var vsamIndex = SchemaFactory.eINSTANCE.createVsamIndex();
		vsamIndex.setSet(set);
	}

	public Schema invokeImportTool(IProgressMonitor progressMonitor) {
		progressMonitor.subTask("(Schema)");
		logger.debug("importing schema...");
				
		modelFactory = new ModelFactory(schema);
		
		String schemaName = dataEntryContext.getAttribute(IDataEntryContext.SCHEMA_NAME);
		Short schemaVersion = dataEntryContext.getAttribute(IDataEntryContext.SCHEMA_VERSION);
		schema = modelFactory.createSchema(schemaName, schemaVersion);
		
		createDiagramData();
		tool.init(dataEntryContext, importToolParameters, dataCollectorRegistry);
		createGeneralSchemaData();
		progressMonitor.worked(10);
		
		// import areas
		progressMonitor.subTask("(Areas)");
		tool.getAreaContexts().elements().stream()
				.forEach(this::handleArea);
		progressMonitor.worked(10);
		
		// import records and elements
		progressMonitor.subTask("(Records)");
		tool.getRecordContexts().elements().stream()
				.forEach(this::handleRecord);
		progressMonitor.worked(30);
		
		// import sets
		progressMonitor.subTask("(Sets)");
		tool.getSetContexts().elements().stream()
				.forEach(this::handleSet);
		progressMonitor.worked(30);
		
		// add the DDLCATLOD entities when we're importing IDMSNTWK version 1 if the schema does not contain a
		// DDLCATLOD area AND the user has indicated that he wants us to do this
		if (isIdmsntwk() && schema.getArea(DDLCATLOD) == null && isOptionAddDDLCATLOD()) {
			progressMonitor.subTask("(DDLCATLOD)");
			handleDDLCATLOD();
		}
		progressMonitor.worked(10);
		
		progressMonitor.subTask("(Integrity checks)");
		
		// make sure all VIA sets are resolved
		if (!modelFactory.isAllViaSetsResolved()) {
			throw new IllegalStateException("not all VIA sets were resolved");
		}
		
		fixMinimumRootAndFragmentLength();
		progressMonitor.worked(5);
				
		progressMonitor.subTask("(Cleanup)");
		disposeImportTool();
		progressMonitor.worked(5);
		
		return schema;
	}
	
	private void createDiagramData() {
		// set the diagram data properties from the defaults defined in the preferences; all of these (default)
		// values should be in the data entry context as well; if not, they will default to false:
		var showRulers = false;
		if (dataEntryContext.containsAttribute(IDataEntryContext.DIAGRAMDATA_SHOW_RULERS)) {
			showRulers = ((Boolean) dataEntryContext.getAttribute(IDataEntryContext.DIAGRAMDATA_SHOW_RULERS)).booleanValue();
		}
		schema.getDiagramData().setShowRulers(showRulers);
		var showGrid = false;
		if (dataEntryContext.containsAttribute(IDataEntryContext.DIAGRAMDATA_SHOW_GRID)) {
			showGrid = ((Boolean) dataEntryContext.getAttribute(IDataEntryContext.DIAGRAMDATA_SHOW_GRID)).booleanValue();
		}
		schema.getDiagramData().setShowGrid(showGrid);
		var snapToGuides = false;
		if (dataEntryContext.containsAttribute(IDataEntryContext.DIAGRAMDATA_SNAP_TO_GUIDES)) {
			snapToGuides = ((Boolean) dataEntryContext.getAttribute(IDataEntryContext.DIAGRAMDATA_SNAP_TO_GUIDES)).booleanValue();
		}
		schema.getDiagramData().setSnapToGuides(snapToGuides);
		var snapToGrid = false;
		if (dataEntryContext.containsAttribute(IDataEntryContext.DIAGRAMDATA_SNAP_TO_GRID)) {
			snapToGrid = ((Boolean) dataEntryContext.getAttribute(IDataEntryContext.DIAGRAMDATA_SNAP_TO_GRID)).booleanValue();
		}
		schema.getDiagramData().setSnapToGrid(snapToGrid);
		var snapToGeometry = false;
		if (dataEntryContext.containsAttribute(IDataEntryContext.DIAGRAMDATA_SNAP_TO_GEOMETRY)) {
			snapToGeometry = ((Boolean) dataEntryContext.getAttribute(IDataEntryContext.DIAGRAMDATA_SNAP_TO_GEOMETRY)).booleanValue();
		}
		schema.getDiagramData().setSnapToGeometry(snapToGeometry);
	}
	
	private void createGeneralSchemaData() {
		// set the schema's description, memo date and comments
		var dataCollector = dataCollectorRegistry.getSchemaDataCollector();
		schema.setDescription(dataCollector.getSchemaDescription());
		schema.setMemoDate(dataCollector.getSchemaMemoDate());
		var comments = dataCollector.getComments();
		if (comments != null && !comments.isEmpty()) {
			// make sure no comment line exceeds 80 characters
			for (var line : comments) {				
				var message = "comment line exceeds 80 characters: '" + line + "'";
				Assert.isTrue(line.length() <= 80, message);				
			}
			schema.getComments().addAll(comments);
		}
	}
	
	private void fixMinimumRootAndFragmentLength() {
		// check the minimum root and fragment lengths; adjust them if needed
		for (var schemaRecord : schema.getRecords()) {			
			fixMinimumRootLength(schemaRecord);
			fixMinimumFragmentLength(schemaRecord);
		}		
	}
	
	private void fixMinimumRootLength(SchemaRecord schemaRecord) {
		var minimumRootLength = schemaRecord.getMinimumRootLength();
		// minimumRootLength must include all CALC, index, and sort control elements. It must be an unsigned
		// integer; if it is not a multiple of 4, we will make it so by rounding up - we treat null and 0 the
		// same here
		if (minimumRootLength != null && minimumRootLength.shortValue() > 0) {
			// round up the value if not a multiple of 4
			var i = minimumRootLength; 
			while (i % 4 > 0) {
				i++;
			}
			minimumRootLength = i;
			
			// compute the minimum value; the control length counts an extra 4 bytes if the record is fragmented,
			// so we have to take that into account
			var minimumValue = !schemaRecord.isFragmented() ? schemaRecord.getControlLength() : (short) (schemaRecord.getControlLength() - 4);	
			
			// perform the minimum value check
			if (minimumRootLength < minimumValue) {
				var message = "minimum root length invalid: must include all CALC, index, and sort control elements (record=" +
						schemaRecord.getName() + ")";
				throw new IllegalStateException(message);
			}
			
			// don't perform the maximum value check --> the CA IDMS schema compiler does NOT perform this check)
			
			// the minimum root length is valid; set the possibly adjusted value in the record
			schemaRecord.setMinimumRootLength(minimumRootLength);
		} else if (minimumRootLength != null && minimumRootLength.shortValue() < 0) {
			var message = "minimum root length invalid: must be an unsigned integer (record=" + schemaRecord.getName() + ")";
			throw new IllegalStateException(message);
		}		
	}
	
	private void fixMinimumFragmentLength(SchemaRecord schemaRecord) {
		var minimumFragmentLength = schemaRecord.getMinimumFragmentLength();
		// minimumFragmentLength must be an unsigned integer; if it is not a multiple of 4, we will make it so by
		// rounding up - we treat null and 0 the same here
		if (minimumFragmentLength != null && minimumFragmentLength.shortValue() > 0) {
			// round up the value if not a multiple of 4
			var i = minimumFragmentLength; 
			while (i % 4 > 0) {
				i++;
			}
			minimumFragmentLength = i;
			
			// the minimum fragment length is valid; set the possibly adjusted value in the record
			schemaRecord.setMinimumFragmentLength(minimumFragmentLength);
		} else if (minimumFragmentLength != null && minimumFragmentLength.shortValue() < 0) {
			var message = "minimum fragment length invalid: must be an unsigned integer (record=" + schemaRecord.getName() + ")";
			throw new IllegalStateException(message);
		}		
	}
	
	public boolean isImportToolDisposed() {
		return importToolIsDisposed;
	}

	private boolean isIdmsntwk() {
		return schema.getName().equals("IDMSNTWK") && schema.getVersion() == 1;
	}

	private boolean isOptionAddDDLCATLOD() {
		Boolean b = dataEntryContext.getAttribute(GeneralContextAttributeKeys.ADD_DDLCATLOD);
		return b.booleanValue();
	}

	private boolean isOptionCompleteLooak155() {
		Boolean b = dataEntryContext.getAttribute(GeneralContextAttributeKeys.ADD_OFFSET_FOR_LOOAK_155);
		return b.booleanValue();
	}

	private boolean isOptionCompleteOoak012() {
		Boolean b = dataEntryContext.getAttribute(GeneralContextAttributeKeys.ADD_OFFSET_FOR_OOAK_012);
		return b.booleanValue();
	}
	
	private boolean isRecordCompressed(SchemaRecord schemaRecord) {
		return schemaRecord.getProcedures().stream()
				.map(RecordProcedureCallSpecification::getProcedure)
				.map(Procedure::getName)
				.anyMatch(compressionProcedures::contains);
	}

}
