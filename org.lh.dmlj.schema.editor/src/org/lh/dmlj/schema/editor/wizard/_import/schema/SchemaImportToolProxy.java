package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.IndexElement;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OccursSpecification;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.ViaSpecification;
import org.lh.dmlj.schema.editor.importtool.IAreaDataCollector;
import org.lh.dmlj.schema.editor.importtool.IDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISchemaDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

public final class SchemaImportToolProxy {
	
	// the context holding all data entered in the wizard's pages
	private IDataEntryContext dataEntryContext;
	
	// optional parameters configured in the import tool's defining plug-in
	private Properties importToolParameters;
	
	// the import tool
	private ISchemaImportTool tool;	
	
	// a factory for the model objects:
	private ModelFactory modelFactory;
	
	// the schema that we are building
	private Schema schema;
	
	// our data collector registry
	IDataCollectorRegistry dataCollectorRegistry = new DataCollectorRegistry();
	
	// the list of procedures that are used to compress records; will always
	// contain "IDMSCOMP"
	private List<String> compressionProcedures;

	public SchemaImportToolProxy(ISchemaImportTool tool,
								 IDataEntryContext dataEntryContext,
								 Properties importToolParameters) {
		super();		
		
		this.tool = tool;
		this.dataEntryContext = dataEntryContext;
		this.importToolParameters = importToolParameters;
		
		// create the list of compression procedure names
		compressionProcedures = new ArrayList<>();
		compressionProcedures.add("IDMSCOMP");
		List<String> contextCompressionProcedures = 
			dataEntryContext.getAttribute(GeneralContextAttributeKeys.COMPRESSION_PROCEDURE_NAMES);
		if (contextCompressionProcedures != null &&
			!contextCompressionProcedures.isEmpty()) {
			
			for (String procedureName : contextCompressionProcedures) {
				String p = procedureName.trim().toUpperCase();
				if (!compressionProcedures.contains(p)) {
					compressionProcedures.add(p);
				}
			}
		}
	}
	
	private boolean containsOccursDependingOnField(SchemaRecord record) {
		for (Element element : record.getElements()) {
			if (element.getOccursSpecification() != null &&
				element.getOccursSpecification().getDependingOn() != null) {
				
				return true;
			}
		}
		return false;
	}

	private StorageMode getStorageMode(SchemaRecord record) {
				
		StringBuilder p = new StringBuilder();
		if (!containsOccursDependingOnField(record)) {
			p.append("FIXED");
		} else {
			p.append("VARIABLE");
		}
		if (isRecordCompressed(record)) {
			p.append(" COMPRESSED");
		}
		String q = p.toString(); 
		if (q.equals("FIXED")) {
			return StorageMode.FIXED;
		} else if (q.equals("FIXED COMPRESSED")) {
			return StorageMode.FIXED_COMPRESSED;
		} else if (q.equals("VARIABLE")) {
			return StorageMode.VARIABLE;
		} else {
			return StorageMode.VARIABLE_COMPRESSED;
		}
	}	
	
	private void handleArea(Object areaContext) {
		
		// get the data collector
		@SuppressWarnings("unchecked")
		IAreaDataCollector<Object> dataCollector = 
			(IAreaDataCollector<Object>) dataCollectorRegistry.getAreaDataCollector(areaContext.getClass());
		
		// get the area name
		String areaName = dataCollector.getName(areaContext);
		
		// create the area
		SchemaArea area = modelFactory.createArea(areaName);
		
		// deal with area procedures
		for (String procedureName : 
			 dataCollector.getProceduresCalled(areaContext)) {
								
			List<ProcedureCallTime> callTimes = 
				new ArrayList<ProcedureCallTime>();
			callTimes.addAll(dataCollector.getProcedureCallTimes(areaContext, 
													     	 	 procedureName));
			Assertions.isCollectionNotEmpty(callTimes, "callTimes is empty");
			
			List<AreaProcedureCallFunction> callFunctions =
				new ArrayList<AreaProcedureCallFunction>();
			callFunctions.addAll(dataCollector.getProcedureCallFunctions(areaContext, 
														 	 	 	 	 procedureName));
			Assertions.isCollectionNotEmpty(callFunctions, 
											"callFunctions is empty");
			
			if (callTimes.size() != callFunctions.size()) {
				throw new RuntimeException("callTimes and callFunctions " +
										   "sizes do not match (area=" + 
										   area.getName() + " procedure=" + 
										   procedureName);
			}
			
			for (int i = 0; i < callTimes.size(); i++) {				
				modelFactory.createProcedureCallSpecification(area, 
															  procedureName,
															  callTimes.get(i), 
															  callFunctions.get(i));
			}
			
		}
		
	}

	private void handleChainedSet(Object setContext) {		
		
		// get the data collector
		@SuppressWarnings("unchecked")
		ISetDataCollector<Object> dataCollector = 
			(ISetDataCollector<Object>) dataCollectorRegistry.getSetDataCollector(setContext.getClass());
		
		// get the set name
		String setName = dataCollector.getName(setContext);
		
		// create the set
		Set set = modelFactory.createSet(setName, SetMode.CHAINED,
								   		 dataCollector.getSetOrder(setContext));		
		
		// create the set owner
		String ownerRecordName = dataCollector.getOwnerRecordName(setContext);		
		OwnerRole ownerRole = modelFactory.createSetOwner(set, ownerRecordName);
		SchemaRecord record = ownerRole.getRecord();
		
		// set the mandatory next dbkey position
		Short nextDbkeyPosition = 
			Short.valueOf(dataCollector.getOwnerNextDbkeyPosition(setContext));
		Assertions.isFreeDbkeyPosition(record, nextDbkeyPosition.shortValue());
		ownerRole.setNextDbkeyPosition(nextDbkeyPosition.shortValue());
		
		// set the optional prior dbkey position
		Short priorDbkeyPosition = 
			dataCollector.getOwnerPriorDbkeyPosition(setContext);
		if (priorDbkeyPosition != null) {
			Assertions.isFreeDbkeyPosition(record, 
										   priorDbkeyPosition.shortValue());
			ownerRole.setPriorDbkeyPosition(priorDbkeyPosition);
		}		
	
		// process the set members; if a record is stored VIA this set, the VIA 
		// specification will be connected to this set when the member role is 
		// created for that record		
		Collection<String> memberRecordNames = 
			dataCollector.getMemberRecordNames(setContext);
		Assertions.isCollectionNotEmpty(memberRecordNames, 
								 		"at least 1 member record name " +
								 		"expected: " + set.getName());		
		for (String memberRecordName : memberRecordNames) {
	
			// create the memberRole
			SetMembershipOption membershipOption = 
				dataCollector.getSetMembershipOption(setContext, 
													 memberRecordName);
			MemberRole memberRole = 
				modelFactory.createSetMember(set, memberRecordName,
											 membershipOption);
			record = memberRole.getRecord();
													
			// set the mandatory next dbkey position
			nextDbkeyPosition = 
				dataCollector.getMemberNextDbkeyPosition(setContext, 
														 memberRecordName);	
			Assertions.isNotNull(nextDbkeyPosition, 
								 "next dbkey position is mandatory");			
			Assertions.isFreeDbkeyPosition(record, 
										   nextDbkeyPosition.shortValue());						
			memberRole.setNextDbkeyPosition(nextDbkeyPosition);
			
			// set the optional prior dbkey position
			priorDbkeyPosition = 
				dataCollector.getMemberPriorDbkeyPosition(setContext, 
														  memberRecordName);
			if (priorDbkeyPosition != null) {
				Assertions.isFreeDbkeyPosition(record, 
											   priorDbkeyPosition.shortValue());
			}
			memberRole.setPriorDbkeyPosition(priorDbkeyPosition);
			
			// set the optional owner dbkey position
			Short ownerDbkeyPosition =  
				dataCollector.getMemberOwnerDbkeyPosition(setContext, 
														  memberRecordName);
			if (ownerDbkeyPosition != null) {
				Assertions.isFreeDbkeyPosition(record, 
											   ownerDbkeyPosition.shortValue());
			}
			memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition);			
			
			// deal with the sort key, if applicable
			if (set.getOrder() == SetOrder.SORTED) {			
				handleSortKey(setContext, memberRole);
			}				
			
		}
		
	}
	
	private void handleDDLCATLOD() {
			
		// create the DDLCATLOD area
		SchemaArea area = modelFactory.createArea("DDLCATLOD");
		
		// copy area procedures (if any)
		for (AreaProcedureCallSpecification originalSpec :
			 schema.getArea("DDLDCLOD").getProcedures()) {
			
			modelFactory.createProcedureCallSpecification(area, 
														  originalSpec.getProcedure().getName(), 
														  originalSpec.getCallTime(), 
														  originalSpec.getFunction());
		}
		
		// copy the records from those contained in the DDLDCLOD area
		boolean ignoreNamingConventionsForElements = 
			isOptionIgnoreNamingConventionsForElements();
		for (SchemaRecord originalRecord : 
			 schema.getArea("DDLDCLOD").getRecords()) {
			
			String viaSetName = 
				originalRecord.getViaSpecification() != null ?
				originalRecord.getViaSpecification().getSet().getName()  + "_" : 
				null;
			
			SchemaRecord record = 
				modelFactory.createRecord(originalRecord.getName() + "_", 
										  originalRecord.getId(), 
										  originalRecord.getStorageMode(), 
										  originalRecord.getLocationMode(), 
									  	  viaSetName, "DDLCATLOD", true);
			
			// add the (validated) elements to the record
			for (Element originalElement : originalRecord.getElements()) {
	
				Element parent = originalElement.getParent() != null ?
					record.getElement(originalElement.getParent().getName()) :
					null;
				
				// create the element
				Element element = 
					modelFactory.createElement(record, parent, 
											   originalElement.getName(), 
											   originalElement.getBaseName(),
											   ignoreNamingConventionsForElements);	
				
				// set some of the element's attributes
				element.setLevel(originalElement.getLevel());		
				element.setUsage(originalElement.getUsage());						
				element.setPicture(originalElement.getPicture());		
				element.setNullable(originalElement.isNullable());
				
				// deal with the REDEFINES clause, if specified
				String redefinedElementName = 
					originalElement.getRedefines() != null ?
					originalElement.getRedefines().getName() : null;
				if (redefinedElementName != null) {			
					Element redefinedElement = 
						record.getElement(redefinedElementName);
					if (redefinedElement == null) {
						throw new RuntimeException("logic error: element " +
												   element.getName() + " redefines " +
												   redefinedElementName + ", but " +
												   redefinedElementName + 
												   " was not found in the record");
					} else if (redefinedElement.getLevel() != element.getLevel()) {
						throw new RuntimeException("logic error: element " +
								   				   element.getName() + " redefines " +
								   				   redefinedElementName + ", but " + 
								   				   element.getName() + 
								   				   "'s level number (" + 
								   				   element.getLevel() + 
								   				   ") does not match that of " +
								   				   redefinedElementName + 
								   				   " (" + redefinedElement.getLevel() + 
								   				   ")");
					}
					element.setRedefines(redefinedElement);
				}
				
				// set the occurs specification if applicable
				if (originalElement.getOccursSpecification() != null) {
					
					OccursSpecification originalOccursSpecification =
						originalElement.getOccursSpecification();
							
					OccursSpecification occursSpecification =
						modelFactory.createOccursSpecification(element);
					
					occursSpecification.setCount(originalOccursSpecification.getCount());
					String dependsOnElementName = 
						originalOccursSpecification.getDependingOn() != null ?
						originalOccursSpecification.getDependingOn().getName() : 
						null;
					if (dependsOnElementName != null) {
						Element dependsOnElement = 
							record.getElement(dependsOnElementName);
						if (dependsOnElement == null) {
							throw new RuntimeException("logic error: element " +
									   				   element.getName() + 
									   				   "'s occurs-depending-on-element, " +							   
									   				   dependsOnElementName + 
									   				   ", was not found in the record");
						}
						occursSpecification.setDependingOn(dependsOnElement);
					}
					
				}					
				
			}		
					
			// set the minimum root and fragment lengths if (and only if) 
			// the record has a storage mode other than fixed
			if (record.getStorageMode() != StorageMode.FIXED) {
				Short minimumRootLength = 
					originalRecord.getMinimumRootLength();
				record.setMinimumRootLength(minimumRootLength);
				Short minimumFragmentLength =
					originalRecord.getMinimumFragmentLength();
				record.setMinimumFragmentLength(minimumFragmentLength);
			}
		
			// if the record has a location mode of CALC, create the CALC 
			// key
			if (record.getLocationMode() == LocationMode.CALC) {									
			
				DuplicatesOption duplicatesOption = 
					originalRecord.getCalcKey().getDuplicatesOption();
				boolean naturalSequence = 
					originalRecord.getCalcKey().isNaturalSequence();
				Key key = modelFactory.createKey(record, duplicatesOption, 
												 naturalSequence);
				
				for (KeyElement originalKeyElement : 
					 originalRecord.getCalcKey().getElements()) {	
					
					modelFactory.createKeyElement(key, 
												  originalKeyElement.getElement()
												  					.getName(), 
												  SortSequence.ASCENDING,
												  ignoreNamingConventionsForElements);					
				}			
				
			}
			
			// if the record has a location mode of VIA, complete the VIA 
			// specification, which will have been created
			if (record.getLocationMode() == LocationMode.VIA) {
				
				String symbolicDisplacementName = 
					originalRecord.getViaSpecification()
								  .getSymbolicDisplacementName();
				Short displacementPageCount = 
					originalRecord.getViaSpecification()
								  .getDisplacementPageCount();
				
				ViaSpecification viaSpecification = 
					record.getViaSpecification();
						
				if (symbolicDisplacementName != null) {
					viaSpecification.setSymbolicDisplacementName(symbolicDisplacementName);
				} else if (displacementPageCount != null) {
					viaSpecification.setDisplacementPageCount(displacementPageCount);
				}							
				
			}
			
			// if applicable, create the offset expression
			String symbolicSubareaName = 
				originalRecord.getAreaSpecification()
							  .getSymbolicSubareaName();
			Integer offsetPageCount =
				originalRecord.getAreaSpecification()
							  .getOffsetExpression() != null ?
				originalRecord.getAreaSpecification()
							  .getOffsetExpression()
							  .getOffsetPageCount() : 
				null;
			Short offsetPercent =
				originalRecord.getAreaSpecification()
					  		  .getOffsetExpression() != null ?						
				originalRecord.getAreaSpecification()
				  		  	  .getOffsetExpression().getOffsetPercent() : 
				null;
			Integer pageCount =
				originalRecord.getAreaSpecification()
			  		  		  .getOffsetExpression() != null ?						
			    originalRecord.getAreaSpecification()
					  		  .getOffsetExpression().getPageCount() : 
				null;
			Short percent =
				originalRecord.getAreaSpecification()
	  		  		  		  .getOffsetExpression() != null ?						
	  		  	originalRecord.getAreaSpecification()
		  		  		  	  .getOffsetExpression().getPercent() :
				null;
			
			modelFactory.createOffsetExpression(record.getAreaSpecification(),
												symbolicSubareaName,
												offsetPageCount, 
												offsetPercent, pageCount, 
												percent);								
			
			// deal with record procedures
			for (RecordProcedureCallSpecification originalSpec :
				 originalRecord.getProcedures()) {
																							
				modelFactory.createProcedureCallSpecification(record, 
															  originalSpec.getProcedure()
															  			  .getName(),
															  originalSpec.getCallTime(), 
															  originalSpec.getVerb());					
				
			}								
			
		}			
		
		// create a sorted list of DDLDCLOD set names
		List<String> setNames = new ArrayList<>();
		for (Set set : schema.getSets()) {
			if (set.getOwner() != null &&
				set.getOwner().getRecord().getAreaSpecification().getArea().getName().equals("DDLDCLOD")) {
				
				setNames.add(set.getName());
			}
			for (MemberRole memberRole : set.getMembers()) {
				if (memberRole.getRecord().getAreaSpecification().getArea().getName().equals("DDLDCLOD") &&
					!setNames.contains(set.getName())) {
					
					setNames.add(set.getName());
				}
			}
		}		
		
		// copy the sets from those involved in the DDLDCLOD area
		for (String setName : setNames) {
			
			// get the original DDLDCLOD set; it should NOT be an indexed
			// set, nor a sorted set (the DDLDCLOD area does not contain any of
			// these)
			Set originalSet = schema.getSet(setName);
			if (originalSet.getMode() != SetMode.CHAINED) {
				throw new RuntimeException("only chained sets expected in " +
										   "the DDLDCLOD area; cannot create " +
										   "DDLCATLOD entities");
			}
			if (originalSet.getOrder() == SetOrder.SORTED) {
				throw new RuntimeException("no sorted sets expected in the" +
										   "DDLDCLOD area; cannot create " +
										   "DDLCATLOD entities");
			}
			
			// create the set
			Set set = modelFactory.createSet(setName + "_", originalSet.getMode(),												
											 originalSet.getOrder());
			
			// create the set owner
			String ownerRecordName = 
				originalSet.getOwner().getRecord().getName() + "_";		
			OwnerRole ownerRole = 
				modelFactory.createSetOwner(set, ownerRecordName, true);
			SchemaRecord record = ownerRole.getRecord();
			
			// set the mandatory next dbkey position
			Short nextDbkeyPosition = 
				Short.valueOf(originalSet.getOwner().getNextDbkeyPosition());
			Assertions.isFreeDbkeyPosition(record, nextDbkeyPosition.shortValue());
			ownerRole.setNextDbkeyPosition(nextDbkeyPosition.shortValue());
			
			// set the optional prior dbkey position
			Short priorDbkeyPosition = 
				originalSet.getOwner().getPriorDbkeyPosition();
			if (priorDbkeyPosition != null) {
				Assertions.isFreeDbkeyPosition(record, 
											   priorDbkeyPosition.shortValue());
				ownerRole.setPriorDbkeyPosition(priorDbkeyPosition);
			}				
			
			// process the set members; if a record is stored VIA this set, 
			// the VIA specification will be connected to this set when the 
			// member role is created for that record		
			for (MemberRole originalMemberRole : originalSet.getMembers()) {
		
				// create the memberRole
				SetMembershipOption membershipOption = 
					originalMemberRole.getMembershipOption();
				String memberRecordName = 
					originalMemberRole.getRecord().getName() + "_";
				MemberRole memberRole = 
					modelFactory.createSetMember(set, memberRecordName,
												 membershipOption, true);
				record = memberRole.getRecord();
														
				// set the mandatory next dbkey position
				nextDbkeyPosition = 
					originalMemberRole.getNextDbkeyPosition();	
				Assertions.isNotNull(nextDbkeyPosition, 
									 "next dbkey position is mandatory");			
				Assertions.isFreeDbkeyPosition(record, 
											   nextDbkeyPosition.shortValue());						
				memberRole.setNextDbkeyPosition(nextDbkeyPosition);
				
				// set the optional prior dbkey position
				priorDbkeyPosition = 
					originalMemberRole.getPriorDbkeyPosition();
				if (priorDbkeyPosition != null) {
					Assertions.isFreeDbkeyPosition(record, 
												   priorDbkeyPosition.shortValue());
				}
				memberRole.setPriorDbkeyPosition(priorDbkeyPosition);
				
				// set the optional owner dbkey position
				Short ownerDbkeyPosition =  
					originalMemberRole.getOwnerDbkeyPosition();
				if (ownerDbkeyPosition != null) {
					Assertions.isFreeDbkeyPosition(record, 
												   ownerDbkeyPosition.shortValue());
				}
				memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition);			
				
				// no sort keys to deal with since we don't expect any sorted set
				
			}				
			
		}
				
	}

	private void handleElement(SchemaRecord record, Element parent,
							   Object elementContext,
							   boolean ignoreNamingConventions) {
		
		// get the data collector
		@SuppressWarnings("unchecked")
		IElementDataCollector<Object> dataCollector =
			(IElementDataCollector<Object>) dataCollectorRegistry.getElementDataCollector(elementContext.getClass());
		
		// get the element name
		String elementName = dataCollector.getName(elementContext);
		
		// get the base element name
		String baseName = dataCollector.getBaseName(elementContext);		
		
		// create the element
		Element element = 
			modelFactory.createElement(record, parent, elementName, baseName,
									   ignoreNamingConventions);	
		
		// set some of the element's attributes
		element.setLevel(dataCollector.getLevel(elementContext));		
		element.setUsage(dataCollector.getUsage(elementContext));						
		element.setPicture(dataCollector.getPicture(elementContext));		
		element.setNullable(dataCollector.getIsNullable(elementContext));
		
		// deal with the REDEFINES clause, if specified
		String redefinedElementName = 
			dataCollector.getRedefinedElementName(elementContext);
		if (redefinedElementName != null) {			
			Element redefinedElement = 
				record.getElement(redefinedElementName.toUpperCase());
			if (redefinedElement == null) {
				throw new RuntimeException("logic error: element " +
										   element.getName() + " redefines " +
										   redefinedElementName + ", but " +
										   redefinedElementName + 
										   " was not found in the record");
			} else if (redefinedElement.getLevel() != element.getLevel()) {
				throw new RuntimeException("logic error: element " +
						   				   element.getName() + " redefines " +
						   				   redefinedElementName + ", but " + 
						   				   element.getName() + 
						   				   "'s level number (" + 
						   				   element.getLevel() + 
						   				   ") does not match that of " +
						   				   redefinedElementName + 
						   				   " (" + redefinedElement.getLevel() + 
						   				   ")");
			}
			element.setRedefines(redefinedElement);
		}
		
		// set the occurs specification if applicable
		short occurrenceCount = 
			dataCollector.getOccurrenceCount(elementContext);
		if (occurrenceCount > 1) {
			
			OccursSpecification occursSpecification =
				modelFactory.createOccursSpecification(element);
			
			occursSpecification.setCount(occurrenceCount);
			String dependsOnElementName = 
				dataCollector.getDependsOnElementName(elementContext);
			if (dependsOnElementName != null) {
				Element dependsOnElement = 
					record.getElement(dependsOnElementName.toUpperCase());
				if (dependsOnElement == null) {
					throw new RuntimeException("logic error: element " +
							   				   element.getName() + 
							   				   "'s occurs-depending-on-element, " +							   
							   				   dependsOnElementName + 
							   				   ", was not found in the record");
				}
				occursSpecification.setDependingOn(dependsOnElement);
			}
			
			List<String> indexElementBaseNames = new ArrayList<>();
				indexElementBaseNames.addAll(dataCollector.getIndexElementBaseNames(elementContext));
			if (!indexElementBaseNames.isEmpty()) {
				List<String> indexElementNames = new ArrayList<>();
				indexElementNames.addAll(dataCollector.getIndexElementNames(elementContext));
				if (indexElementBaseNames.size() != indexElementNames.size()) {
					throw new RuntimeException("");
				}
				for (int i = 0; i < indexElementBaseNames.size(); i++) {
					IndexElement indexElement = 
						SchemaFactory.eINSTANCE.createIndexElement();
					occursSpecification.getIndexElements().add(indexElement);
					indexElement.setBaseName(indexElementBaseNames.get(i));
					indexElement.setName(indexElementNames.get(i));					
				}
			}
			
		}
		
		// set the element value, if any
		String value = dataCollector.getValue(elementContext);
		if (value != null) {
			element.setValue(value);
		}
		
		// deal with the element's subordinate elements, if any
		for (Object childElementContext :  
			tool.getSubordinateElementContexts(elementContext)) {
			
			handleElement(record, element, childElementContext,
						  ignoreNamingConventions);			
		}				
		
	}
	
	private void handleRecord(Object recordContext) {						
		
		// get the data collector
		@SuppressWarnings("unchecked")
		IRecordDataCollector<Object> dataCollector = 
			(IRecordDataCollector<Object>) dataCollectorRegistry.getRecordDataCollector(recordContext.getClass());
		
		// get the record name
		String recordName = dataCollector.getName(recordContext);
		
		// get the record id
		short recordId = dataCollector.getRecordId(recordContext);
		
		// get the record's location mode
		LocationMode locationMode = dataCollector.getLocationMode(recordContext);				
				
		// if the record is VIA, we need the VIA set name
		String viaSetName = null; // not applicable for CALC and DIRECT records
		if (locationMode == LocationMode.VIA) {
			viaSetName = dataCollector.getViaSetName(recordContext);			
		}
		
		// get the name of the area in which the record is stored 
		String areaName = dataCollector.getAreaName(recordContext);
		
		// create the record in the schema; we supply the VIA set name here, if
		// applicable, so that the model factory can resolve the VIA set when it 
		// is created later on
		// we always specify a storage mode of FIXED and will correct that later
		SchemaRecord record = 
			modelFactory.createRecord(recordName, recordId, StorageMode.FIXED, 
									  locationMode, viaSetName, areaName);
		
		// set the base name and versiontring baseName = dataCollector.getBaseName(recordContext);
		String baseName = dataCollector.getBaseName(recordContext);
		short baseVersion = dataCollector.getBaseVersion(recordContext);		
		record.setBaseName(baseName);
		record.setBaseVersion(baseVersion);
		
		// set the synonym name and version
		String synonymName = dataCollector.getSynonymName(recordContext);
		short synonymVersion = dataCollector.getSynonymVersion(recordContext);
		record.setSynonymName(synonymName);
		record.setSynonymVersion(synonymVersion);
		
		// deal with record procedures
		for (String procedureName : 
			 dataCollector.getProceduresCalled(recordContext)) {
								
			List<ProcedureCallTime> callTimes = 
				new ArrayList<ProcedureCallTime>();
			callTimes.addAll(dataCollector.getProcedureCallTimes(recordContext, 
													     	 	 procedureName));
			Assertions.isCollectionNotEmpty(callTimes, "callTimes is empty");
			
			List<RecordProcedureCallVerb> callVerbs =
				new ArrayList<RecordProcedureCallVerb>();
			callVerbs.addAll(dataCollector.getProcedureCallVerbs(recordContext, 
														 	 	 procedureName));
			Assertions.isCollectionNotEmpty(callVerbs, "callVerbs is empty");
			
			if (callTimes.size() != callVerbs.size()) {
				throw new RuntimeException("callTimes and callVerbs sizes do " +
										   "not match (record=" + record.getName() + 
										   " procedure=" + procedureName);
			}
			
			for (int i = 0; i < callTimes.size(); i++) {				
				modelFactory.createProcedureCallSpecification(record, 
															  procedureName,
															  callTimes.get(i), 
															  callVerbs.get(i));
			}
			
		}		
		
		// add the (validated) elements to the record
		boolean ignoreNamingConventionsForElements = 
				isOptionIgnoreNamingConventionsForElements();
		for (Object elementContext : tool.getRootElementContexts(recordContext)) {					
			handleElement(record, null, elementContext, 
						  ignoreNamingConventionsForElements);									
		}
		
		// correct the record's storage mode
		record.setStorageMode(getStorageMode(record));
				
		// set the minimum root and fragment lengths if (and only if) the record 
		// has a storage mode other than fixed
		if (record.getStorageMode() != StorageMode.FIXED) {
			Short minimumRootLength = 
				dataCollector.getMinimumRootLength(recordContext);
			record.setMinimumRootLength(minimumRootLength);
			Short minimumFragmentLength =
				dataCollector.getMinimumFragmentLength(recordContext);
			record.setMinimumFragmentLength(minimumFragmentLength);
		}
	
		// if the record has a location mode of CALC, create the CALC key
		if (record.getLocationMode() == LocationMode.CALC) {									
		
			DuplicatesOption duplicatesOption = 
				dataCollector.getCalcKeyDuplicatesOption(recordContext);
			// always set the naturalSequence attribute to false; this attribute
			// is not applicable to CALC keys
			Key key = modelFactory.createKey(record, duplicatesOption, false);
			
			for (String elementName : 
				 dataCollector.getCalcKeyElementNames(recordContext)) {	
				
				modelFactory.createKeyElement(key, elementName, 
											  SortSequence.ASCENDING,
											  ignoreNamingConventionsForElements);					
			}			
			
		}
		
		// if the record has a location mode of VIA, complete the VIA 
		// specification, which will have been created
		if (record.getLocationMode() == LocationMode.VIA) {
			
			String symbolicDisplacementName = 
				dataCollector.getViaSymbolicDisplacementName(recordContext);
			Short displacementPageCount = 
				dataCollector.getViaDisplacementPageCount(recordContext);
			
			ViaSpecification viaSpecification = record.getViaSpecification();
					
			if (symbolicDisplacementName != null) {
				viaSpecification.setSymbolicDisplacementName(symbolicDisplacementName);
			} else if (displacementPageCount != null) {
				viaSpecification.setDisplacementPageCount(displacementPageCount);
			}							
			
		}
	
		// deal with the record's offset expression
		if (isIdmsntwk() && 
			(record.getName().equals("OOAK-012")) &&
			 isOptionCompleteOoak_012() ||			 
			 record.getName().equals("LOOAK-155") &&
			 isOptionCompleteLooak_155()) {
	
			// set the offset for OOAK-012 and LOOAK-155 in the case of an 
			// IDMSNTWK version 1 schema AND if the user wants us to do this			
			modelFactory.createOffsetExpression(record.getAreaSpecification(), 
												null, Integer.valueOf(1),
												null, Integer.valueOf(1), null);			
			
		} else {	
			
			// not an IDMSNTWK version 1 schema or the user does not want us to
			// set the offset expression for OOAK-012 and LOOAK-155; get the
			// offset expression data from the import tool implementation and,
			// if applicable, create the offset expression
			String symbolicSubareaName = 
				dataCollector.getSymbolicSubareaName(recordContext);
			Integer offsetPageCount = 
				dataCollector.getOffsetOffsetPageCount(recordContext);
			Short offsetPercent = 
				dataCollector.getOffsetOffsetPercent(recordContext);
			Integer pageCount = 
				dataCollector.getOffsetPageCount(recordContext);
			Short percent = dataCollector.getOffsetPercent(recordContext);
			
			modelFactory.createOffsetExpression(record.getAreaSpecification(),
												symbolicSubareaName,
												offsetPageCount, offsetPercent, 
												pageCount, percent);
			
		}							
		
	}

	private void handleSet(Object setContext) {
		
		// get the data collector
		@SuppressWarnings("unchecked")
		ISetDataCollector<Object> dataCollector = 
			(ISetDataCollector<Object>) dataCollectorRegistry.getSetDataCollector(setContext.getClass());		
				
		SetMode setMode = dataCollector.getSetMode(setContext);
		Assertions.isNotNull(setMode, "set mode is null (set=" +
							 dataCollector.getName(setContext) + ")");
		if (setMode == SetMode.CHAINED) {
			handleChainedSet(setContext);
		} else if (setMode == SetMode.INDEXED) {			
			if (dataCollector.isSystemOwned(setContext)) {
				handleSystemOwnedIndexedSet(setContext);
			} else {
				handleUserOwnedIndexedSet(setContext);
			}
		} else {
			throw new RuntimeException("set mode is invalid: " + setMode);
		}
		
	}
	
	private void handleSortKey(Object setContext, MemberRole memberRole) {		
		
		// get the data collector
		@SuppressWarnings("unchecked")
		ISetDataCollector<Object> dataCollector = 
			(ISetDataCollector<Object>) dataCollectorRegistry.getSetDataCollector(setContext.getClass());
		
		String recordName = memberRole.getRecord().getName();
		
		// determine the duplicates option; in the case of an indexed set, we
		// need to know if the set is sorted by dbkey, because if it is, 
		// duplicates are not allowed (a record can never be twice a member of
		// the same set)		
		DuplicatesOption duplicatesOption = null;
		boolean sortedByDbkey = false;
		if (memberRole.getSet().getMode() == SetMode.INDEXED) {
			sortedByDbkey = dataCollector.isSortedByDbkey(setContext);
		}
		if (sortedByDbkey) {
			duplicatesOption = DuplicatesOption.NOT_ALLOWED;
		} else {
			duplicatesOption = 
				dataCollector.getDuplicatesOption(setContext, recordName);			
		}
		
		// get the natural sequence indicator value (always ask the set data 
		// collector for its value, even if the set is sorted by dbkey)
		boolean naturalSequence = 
			dataCollector.getSortKeyIsNaturalSequence(setContext, recordName);		
		
		// create the sort key for the set
		Key key = modelFactory.createKey(memberRole, duplicatesOption, 
										 naturalSequence);
		if (memberRole.getSet().getMode() == SetMode.INDEXED) {
			key.setCompressed(dataCollector.isKeyCompressed(setContext));
		}
				
		if (!sortedByDbkey) {
			
			// the set is not sorted by dbkey; create a key element for each
			// element in the sort key
			boolean ignoreNamingConventionsForElements = 
				isOptionIgnoreNamingConventionsForElements();
			for (String elementName : 
				 dataCollector.getSortKeyElements(setContext, recordName)) {
								
				// get the sort sequence (ASC/DESC)
				SortSequence sortSequence = 
					dataCollector.getSortSequence(setContext, recordName, 
													 elementName);
				// create the key element
				modelFactory.createKeyElement(key, elementName, sortSequence,
											  ignoreNamingConventionsForElements);
				
			}
			
		} else {
			
			// the set is sorted by dbkey; create 1 key element
			
			// get the sort sequence (ASC/DESC)
			SortSequence sortSequence = 
				dataCollector.getSortSequence(setContext, recordName, null);
			
			// create the key element
			modelFactory.createKeyElement(key, null, sortSequence,
										  isOptionIgnoreNamingConventionsForElements());
									
		}
		
	}

	private void handleSystemOwnedIndexedSet(Object setContext) {		
		
		// get the data collector
		@SuppressWarnings("unchecked")
		ISetDataCollector<Object> dataCollector = 
			(ISetDataCollector<Object>) dataCollectorRegistry.getSetDataCollector(setContext.getClass());
		
		// get the set name
		String setName = dataCollector.getName(setContext);
		
		// create the set in the schema		
		Set set = 
			modelFactory.createSet(setName, SetMode.INDEXED,
								   dataCollector.getSetOrder(setContext));
	
		// create the system owner and add it to its area
		String areaName = dataCollector.getSystemOwnerAreaName(setContext);		
		SystemOwner systemOwner = modelFactory.createSystemOwner(set, areaName);		
		
		// create the offset expression				
		Integer offsetPageCount = 
			dataCollector.getSystemOwnerOffsetOffsetPageCount(setContext);
		Short offsetPercent = 
			dataCollector.getSystemOwnerOffsetOffsetPercent(setContext);
		Integer pageCount = 
			dataCollector.getSystemOwnerOffsetPageCount(setContext);
		Short percent = 
			dataCollector.getSystemOwnerOffsetPercent(setContext);
		String symbolicSubareaName = 
			dataCollector.getSystemOwnerSymbolicSubareaName(setContext);
		modelFactory.createOffsetExpression(systemOwner.getAreaSpecification(), 
											symbolicSubareaName, 
											offsetPageCount, offsetPercent, 
											pageCount, percent);
		
		// create the indexed set mode specification
		String symbolicIndexName = 
			dataCollector.getSymbolicIndexName(setContext);
		Short keyCount = symbolicIndexName != null ? null :
						 dataCollector.getKeyCount(setContext);
		Short displacementPageCount = 
			symbolicIndexName != null ? null :
			dataCollector.getDisplacementPageCount(setContext);
		if (displacementPageCount != null && 
			displacementPageCount.shortValue() == 0) {
			
			displacementPageCount = null;
		}
		modelFactory.createIndexedSetModeSpecification(set, symbolicIndexName,
													   keyCount,
													   displacementPageCount);				
		
		// process the (one and only) set member; if the record is stored VIA 
		// this set, the VIA specification will be connected to this set when
		// the member role is created
		Collection<String> memberRecordNames = 
			dataCollector.getMemberRecordNames(setContext);
		Assertions.isSingleElementCollection(memberRecordNames, 
											 "1 member record name expected: " + 
											 set.getName());		
		String memberRecordName = memberRecordNames.iterator().next();
		SetMembershipOption membershipOption = 
			dataCollector.getSetMembershipOption(setContext, 
													memberRecordName);		
		MemberRole memberRole = 
			modelFactory.createSetMember(set, memberRecordName,
										 membershipOption);
		SchemaRecord record = memberRole.getRecord();
		
		// set the index dbkey position, which is (only) optional if the set 
		// membership option is MANDATORY AUTOMATIC
		Short indexDbkeyPosition = 
			dataCollector.getMemberIndexDbkeyPosition(setContext);
		if (membershipOption != SetMembershipOption.MANDATORY_AUTOMATIC) {
			Assertions.isNotNull(indexDbkeyPosition, 
								 "index dbkey position is mandatory: " + 
								 set.getName());			
		}	
		if (indexDbkeyPosition != null) {
			Assertions.isFreeDbkeyPosition(record, 
										   indexDbkeyPosition.shortValue());
			memberRole.setIndexDbkeyPosition(indexDbkeyPosition);	
		}
		
		// set the optional owner dbkey position
		Short ownerDbkeyPosition =  
			dataCollector.getMemberOwnerDbkeyPosition(setContext, 
														 memberRecordName);
		if (ownerDbkeyPosition != null) {
			Assertions.isFreeDbkeyPosition(record, 
										   ownerDbkeyPosition.shortValue());			
			memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition);
		}		
		
		// deal with the sort key, if applicable
		if (set.getOrder() == SetOrder.SORTED) {			
			handleSortKey(setContext, memberRole);			
		}		
		
	}

	private void handleUserOwnedIndexedSet(Object setContext) {
		
		// get the data collector
		@SuppressWarnings("unchecked")
		ISetDataCollector<Object> dataCollector =
			(ISetDataCollector<Object>) dataCollectorRegistry.getSetDataCollector(setContext.getClass());
		
		// get the set name
		String setName = dataCollector.getName(setContext);
		
		// create the set		
		Set set = 
			modelFactory.createSet(setName, SetMode.INDEXED,
								   dataCollector.getSetOrder(setContext));
		
		// create the set owner
		String ownerRecordName = 
			dataCollector.getOwnerRecordName(setContext);
		OwnerRole ownerRole = modelFactory.createSetOwner(set, ownerRecordName);
		SchemaRecord record = ownerRole.getRecord();
		
		// set the mandatory next dbkey position
		Short nextDbkeyPosition = 
			Short.valueOf(dataCollector.getOwnerNextDbkeyPosition(setContext));
		Assertions.isFreeDbkeyPosition(record, nextDbkeyPosition.shortValue());
		ownerRole.setNextDbkeyPosition(nextDbkeyPosition.shortValue());
		
		// set the mandatory prior dbkey position
		Short priorDbkeyPosition = 
			dataCollector.getOwnerPriorDbkeyPosition(setContext);
		Assertions.isNotNull(priorDbkeyPosition, "prior dbkey position is null");		
		Assertions.isFreeDbkeyPosition(record, priorDbkeyPosition.shortValue());		
		ownerRole.setPriorDbkeyPosition(priorDbkeyPosition);
		
		// create the indexed set mode specification
		String symbolicIndexName = 
			dataCollector.getSymbolicIndexName(setContext);
		Short keyCount = symbolicIndexName != null ? null :
						 dataCollector.getKeyCount(setContext);
		Short displacementPageCount = 
			symbolicIndexName != null ? null :
			dataCollector.getDisplacementPageCount(setContext);
		if (displacementPageCount != null && 
			displacementPageCount.shortValue() == 0) {
			
			displacementPageCount = null;
		}
		modelFactory.createIndexedSetModeSpecification(set, symbolicIndexName,
													   keyCount,
													   displacementPageCount);		
		
		// process the (one and only) set member; if the record is stored VIA 
		// this set, the VIA specification will be connected to this set when
		// the member role is created
		Collection<String> memberRecordNames = 
			dataCollector.getMemberRecordNames(setContext);
		Assertions.isSingleElementCollection(memberRecordNames, 
											 "1 member record name expected: " + 
											 set.getName());		
		String memberRecordName = memberRecordNames.iterator().next();
		SetMembershipOption membershipOption = 
			dataCollector.getSetMembershipOption(setContext, 
													memberRecordName);		
		MemberRole memberRole = 
			modelFactory.createSetMember(set, memberRecordName,
										 membershipOption);
		record = memberRole.getRecord();		
		
		// set the mandatory index dbkey position									
		Short indexDbkeyPosition = 
			dataCollector.getMemberIndexDbkeyPosition(setContext);
		Assertions.isNotNull(indexDbkeyPosition, "index dbkey position is null");
		Assertions.isFreeDbkeyPosition(record, indexDbkeyPosition.shortValue());
		memberRole.setIndexDbkeyPosition(indexDbkeyPosition);	
		
		// set the optional index dbkey position
		Short ownerDbkeyPosition =  
			dataCollector.getMemberOwnerDbkeyPosition(setContext, 
														 memberRecordName);
		if (ownerDbkeyPosition != null) {
			Assertions.isFreeDbkeyPosition(record, 
										   ownerDbkeyPosition.shortValue());
			memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition);
		}	
						
		
		// deal with the sort key, if applicable
		if (set.getOrder() == SetOrder.SORTED) {			
			handleSortKey(setContext, memberRole);			
		}			
		
	}

	public Schema invokeImportTool() {
					
		// create the model factory
		modelFactory = new ModelFactory(schema);
		
		// create the schema
		String schemaName = 
			dataEntryContext.getAttribute(IDataEntryContext.SCHEMA_NAME);		
		Short schemaVersion = 
			dataEntryContext.getAttribute(IDataEntryContext.SCHEMA_VERSION);
		schema = modelFactory.createSchema(schemaName, schemaVersion);				
		
		// initialize the import tool
		tool.init(dataEntryContext, importToolParameters, dataCollectorRegistry);
		
		// set the schema's description and memo date
		ISchemaDataCollector dataCollector = 
			dataCollectorRegistry.getSchemaDataCollector();
		schema.setDescription(dataCollector.getSchemaDescription());
		schema.setMemoDate(dataCollector.getSchemaMemoDate());		
		
		// import areas
		for (Object areaContext : tool.getAreaContexts()) {			
			handleArea(areaContext);
		}
		
		// import records and elements
		for (Object recordContext : tool.getRecordContexts()) {			
			handleRecord(recordContext);
		}
		
		// import sets
		for (Object setContext : tool.getSetContexts()) {
			handleSet(setContext);
		}	
		
		// add the DDLCATLOD entities when we're importing IDMSNTWK version 1 if
		// the schema does not contain a DDLCATLOD area AND the user has 
		// indicated that he wants us to do this
		if (isIdmsntwk() && schema.getArea("DDLCATLOD") == null &&
			isOptionAddDDLCATLOD()) {
			
			handleDDLCATLOD();
		}
		
		// make sure all VIA sets are resolved
		if (!modelFactory.isAllViaSetsResolved()) {
			throw new RuntimeException("not all VIA sets were resolved");
		}
		
		// check the minimum root and fragment lengths; adjust them if needed
		for (SchemaRecord record : schema.getRecords()) {			
			
			Short minimumRootLength = record.getMinimumRootLength();
			// minimumRootLength must include all CALC, index, and sort control 
			// elements. It must be an unsigned integer; if it is not a multiple 
			// of 4, we will make it so by rounding up - we treat null and 0 the
			// same here
			if (minimumRootLength != null &&
				minimumRootLength.shortValue() > 0) {				
				
				// round up the value if not a multiple of 4
				short i = minimumRootLength; 
				while (i % 4 > 0) {
					i++;
				}
				minimumRootLength = Short.valueOf(i);
				
				// compute the minimum value; the control length counts an extra
				// 4 bytes if the record is fragmented, so we have to take that
				// into account
				short minimumValue = 
					!record.isFragmented() ? record.getControlLength() :
					(short) (record.getControlLength() - 4);				
				
				// perform the minimum value check
				if (minimumRootLength < minimumValue) {
					String message = 
						"minimum root length invalid: must include all CALC, " +
						"index, and sort control elements (record=" +
								record.getName() + ")";
					throw new RuntimeException(message);
				}
				
				// perform the maximum value check (not sure if the CA IDMS
				// schema performs this check)				
				if (minimumRootLength > record.getDataLength()) {
					String message = 
						"minimum root length invalid: exceeds 'Data length' " +
						"(record=" + record.getName() + ")";
					throw new RuntimeException(message);
				}
				// the minimum root length is valid; set the possibly adjusted 
				// value in the record
				record.setMinimumRootLength(Short.valueOf(minimumRootLength));
			
			} else if (minimumRootLength != null && 
					   minimumRootLength.shortValue() < 0) {
					
				String message = 
					"minimum root length invalid: must be an unsigned " +
					"integer (record=" + record.getName() + ")";
				throw new RuntimeException(message);				
			}
			
			Short minimumFragmentLength = record.getMinimumFragmentLength();
			// minimumFragmentLength must be an unsigned integer; if it is not a 
			// multiple of 4, we will make it so by rounding up - we treat null
			// and 0 the same here
			if (minimumFragmentLength != null && 
				minimumFragmentLength.shortValue() > 0) {
				
				// round up the value if not a multiple of 4
				short i = minimumFragmentLength; 
				while (i % 4 > 0) {
					i++;
				}
				minimumFragmentLength = Short.valueOf(i);
				
				// perform the maximum value check (not sure if the CA IDMS
				// schema performs this check)				
				if (minimumFragmentLength > record.getDataLength()) {
					String message = 
						"minimum fragment length (" + minimumFragmentLength +
						") invalid: exceeds data length (record=" + 
						record.getName() + "; data length=" + 
						record.getDataLength() + ")";
					throw new RuntimeException(message);
				}
				
				// the minimum fragment length is valid; set the possibly 
				// adjusted value in the record
				record.setMinimumFragmentLength(Short.valueOf(minimumFragmentLength));
				
			} else if (minimumFragmentLength != null && 
					   minimumFragmentLength.shortValue() < 0) {
				
				String message = 
					"minimum fragment length invalid: must be an unsigned " +
					"integer (record=" + record.getName() + ")";
				throw new RuntimeException(message);
			}			
			
		}
		
		// dispose of the import tool
		tool.dispose();
		
		// return the schema to the caller
		return schema;
		
	}

	private boolean isIdmsntwk() {
		return schema.getName().equals("IDMSNTWK") && schema.getVersion() == 1;
	}

	private boolean isOptionAddDDLCATLOD() {
		Boolean b = dataEntryContext.getAttribute(GeneralContextAttributeKeys.ADD_DDLCATLOD);
		return b.booleanValue();
	}

	private boolean isOptionCompleteLooak_155() {
		Boolean b = dataEntryContext.getAttribute(GeneralContextAttributeKeys.ADD_OFFSET_FOR_LOOAK_155);
		return b.booleanValue();
	}

	private boolean isOptionCompleteOoak_012() {
		Boolean b = dataEntryContext.getAttribute(GeneralContextAttributeKeys.ADD_OFFSET_FOR_OOAK_012);
		return b.booleanValue();
	}
	
	private boolean isOptionIgnoreNamingConventionsForElements() {
		Boolean b = 
			dataEntryContext.getAttribute(GeneralContextAttributeKeys.IGNORE_NAMING_CONVENTIONS_FOR_ELEMENTS);
		return b.booleanValue();
	}

	private boolean isRecordCompressed(SchemaRecord record) {
		for (RecordProcedureCallSpecification procedureCallSpec :
			 record.getProcedures()) {
			
			String procedureName = procedureCallSpec.getProcedure().getName();
			if (compressionProcedures.contains(procedureName)) {
				return true;
			}
		}
		return false;
	}

}