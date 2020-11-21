/**
 * Copyright (C) 2019  Luc Hermans
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
package org.lh.dmlj.schema.editor.dsl.builder.model

import org.lh.dmlj.schema.AreaSpecification
import org.lh.dmlj.schema.DuplicatesOption
import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.KeyElement
import org.lh.dmlj.schema.LabelAlignment
import org.lh.dmlj.schema.MemberRole
import org.lh.dmlj.schema.OwnerRole
import org.lh.dmlj.schema.SchemaFactory
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.Set
import org.lh.dmlj.schema.SetMembershipOption
import org.lh.dmlj.schema.SetMode
import org.lh.dmlj.schema.SetOrder
import org.lh.dmlj.schema.SortSequence

class SetModelBuilder extends AbstractModelBuilder<Set> {
	
	private static final String BODY_AREA = "area"
	private static final String BODY_BENDPOINT = "bendpoint"
	private static final String BODY_CONNECTOR = "connector"
	private static final String BODY_CONNECTORS = "connectors"
	private static final String BODY_END = "end"
	private static final String BODY_INDEX = "index"
	private static final String BODY_KEY = "key"
	private static final String BODY_LABEL = "label"
	private static final String BODY_LINE = "line"
	private static final String BODY_MEMBER = "member"
	private static final String BODY_OWNER = "owner"
	private static final String BODY_START = "start"
	private static final String BODY_SYSTEM_OWNER = "systemOwner"
	private static final String BODY_VSAM_INDEX = "vsamIndex"
	
	private Set set
	
	private boolean ownerIsAutomatic = false
	private boolean memberIsAutomatic = false
	
	boolean resolveViaSet = true

	private static short getHighestPointerPosition(SchemaRecord record) {
		short result = Integer.MIN_VALUE
		record.ownerRoles.each { OwnerRole ownerRole ->
			if (ownerRole.nextDbkeyPosition && ownerRole.nextDbkeyPosition > result) {
				result = ownerRole.nextDbkeyPosition
			}
			if (ownerRole.priorDbkeyPosition && ownerRole.priorDbkeyPosition > result) {
				result = ownerRole.priorDbkeyPosition
			}
		}
		record.memberRoles.each { MemberRole memberRole ->
			if (memberRole.nextDbkeyPosition && memberRole.nextDbkeyPosition > result) {
				result = memberRole.nextDbkeyPosition
			}
			if (memberRole.priorDbkeyPosition && memberRole.priorDbkeyPosition > result) {
				result = memberRole.priorDbkeyPosition
			}
			if (memberRole.ownerDbkeyPosition && memberRole.ownerDbkeyPosition > result) {
				result = memberRole.ownerDbkeyPosition
			}
			if (memberRole.indexDbkeyPosition && memberRole.indexDbkeyPosition > result) {
				result = memberRole.indexDbkeyPosition
			}
		}
		result
	}

	def propertyMissing(String name) {
		if (name == 'compressed') {
			compressed()
		} else if (name == 'connectors') {
			connectors({ })
		} else if (name == 'key') {
			key ({ })
		} else if (name == 'naturalSequence') {
			naturalSequence()
		} else if (name == 'systemOwner') {
			systemOwner({ })
		} else if (name == 'vsamIndex') {
			vsamIndex({ })
		} else {
			throw new MissingPropertyException('no such property: ' + name + ' for class: ' +
											   getClass().getName())
		}
	}

	private void addMissingMemberPointers(MemberRole member, boolean linkedToOwner) {
		if (set.mode == SetMode.CHAINED) {
			if (!member.nextDbkeyPosition) {
				member.nextDbkeyPosition = getHighestPointerPosition(member.record) + 1
			}
			if (linkedToOwner && !member.ownerDbkeyPosition) {
				member.ownerDbkeyPosition = getHighestPointerPosition(member.record) + 1
			}
		} else if (set.mode == SetMode.INDEXED && !set.systemOwner) {
			if (!member.indexDbkeyPosition) {
				member.indexDbkeyPosition = getHighestPointerPosition(member.record) + 1
			}
		} else if (set.mode == SetMode.INDEXED && set.systemOwner) {
			if (member.membershipOption != SetMembershipOption.MANDATORY_AUTOMATIC &&
				!member.indexDbkeyPosition) {
				
				member.indexDbkeyPosition = getHighestPointerPosition(member.record) + 1
			}
		}
	}
	
	private void addMissingOwnerPointers() {
		if (!set.owner.nextDbkeyPosition) {
			set.owner.nextDbkeyPosition = getHighestPointerPosition(set.owner.record) + 1
		}
		if (set.mode.is(SetMode.INDEXED) && !set.owner.priorDbkeyPosition) {
			set.owner.priorDbkeyPosition = getHighestPointerPosition(set.owner.record) + 1
		}
	}
	
	void area(Closure definition) {
		assert bodies == [ BODY_SYSTEM_OWNER ]
		if (bufferedName) {
			area(bufferedName, definition)
			bufferedName = null
		} else {
			area("${set.name}-AREA", definition)
		}
	}
	
	void area(String areaName) {
		area(areaName, { })	
	}
	
	void area(String areaName, Closure definition) {		
		assert bodies == [ BODY_SYSTEM_OWNER ]
		assert set.systemOwner
		assert !set.systemOwner.areaSpecification
		createSystemOwnerAreaSpecification(areaName)		
		bodies << BODY_AREA
		runClosure definition
		assert bodies == [ BODY_SYSTEM_OWNER, BODY_AREA ]
		bodies -= BODY_AREA
	}
	
	void ascending(String elementName) {
		assert bodies == [ BODY_MEMBER, BODY_KEY ]
		assert set.members
		assert set.members[-1].sortKey
		assert set.members[-1].record
		createKeyElement(set.members[-1], elementName, SortSequence.ASCENDING);
	}
	
	void bendpoint(Closure definition) {
		
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE ]
		assert set.members
		assert set.members[-1].record
		assert set.members[-1].connectionParts
		if (!set.systemOwner) {
			assert set.members[-1].connectionParts[0].sourceEndpointLocation,
				'source- and target endpoints are mandatory when bendpoints are defined'
		}
		assert !set.members[-1].connectionParts[-1].targetEndpointLocation,
			"target endpoint must be defined AFTER the line's bendpoints"
		
		int bendpointIndex = 0;
		set.members[-1].connectionParts.each { connectionPart ->
			bendpointIndex += connectionPart.bendpointLocations.size()
		}
		String eyecatcher = "bendpoint [$bendpointIndex] set ${set.name} (${set.members[-1].record.name})"
		set.members[-1].connectionParts[-1].bendpointLocations << buildAndRegisterDiagramLocation(eyecatcher)
				
		bodies << BODY_BENDPOINT
		runClosure definition
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_BENDPOINT ]
		bodies -= BODY_BENDPOINT
	}

	Set build() {
		def definition = { }
		build(null, definition)
	}
	
	Set build(Closure definition) {		
		build(null, definition)
	}
	
	Set build(String name) {
		def definition = { }
		build(name, definition)
	}
	
	Set build(String name, Closure definition) {
		
		assert !bodies
		
		if (schema == null) {
			buildTemporarySchema()
		}
		
		String setName = name ?: generateSetName()
		
		set = SchemaFactory.eINSTANCE.createSet()
		schema.sets << set
		
		set.name = setName
		set.mode = SetMode.CHAINED
		set.order = SetOrder.NEXT
		
		runClosure definition
		
		assert !bodies
		
		if ((set.mode == SetMode.CHAINED || set.mode == SetMode.INDEXED && !set.systemOwner)) {
			if (!set.owner) {
				createOwnerRole(set.name + '-O')
			}
			addMissingOwnerPointers()
		}
		
		if (!set.members) {
			createMemberRole()
			completeMemberRole(null)			
		}
		boolean linkedToOwner = set.members.any { it.ownerDbkeyPosition }
		set.members.each { MemberRole memberRole -> 
			addMissingMemberPointers(memberRole, linkedToOwner)
			if (set.order == SetOrder.SORTED && !memberRole.sortKey) {
				memberRole.sortKey = SchemaFactory.eINSTANCE.createKey()
				memberRole.record.keys << memberRole.sortKey
				memberRole.sortKey.duplicatesOption = DuplicatesOption.NOT_ALLOWED
				memberRole.sortKey.compressed = false
				memberRole.sortKey.naturalSequence = false
				createKeyElement(memberRole, null, SortSequence.ASCENDING)
			}
		}
		
		set
	}
	
	private void completeMemberRole(String memberRecordName) {
		String recordName = memberRecordName ?: "${set.name}-M"		
		if (!schemaIsAutomatic && schema.getRecord(recordName)) {
			set.members[-1].record = schema.getRecord(recordName)
			if (set.members[-1].record.isVia() && resolveViaSet) {
				// if the member record is stored VIA the set we're building, resolve its VIA set,
				// unless told NOT to do so (which will be the case when we're building the member
				// record itself)				
				Set viaSet = set.members[-1].record.viaSpecification.set 
				assert viaSet
				if (viaSet.name == set.name) {
					assert !viaSet.schema // we expect a placeholder set
					set.members[-1].record.viaSpecification.set = set
				}
			}
		} else {
			set.members[-1].record = createTemporaryRecord(recordName)
			memberIsAutomatic = true
		}
		set.members[-1].connectionLabel.diagramLocation.eyecatcher = 
			"set label ${set.name} ($recordName)"
	}
	
	private void compressed() {
		assert bodies == [ BODY_MEMBER, BODY_KEY ]
		assert set.members
		assert set.members[-1].sortKey
		assert !set.members[-1].sortKey.compressed
		set.members[-1].sortKey.compressed = true
	}
	
	void connector(Closure definition) {
		
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_CONNECTORS ]
		// we use the connector location eyecatcher to determine which connector is being defined
		assert !set.members[-1].connectionParts[0].connector.diagramLocation.eyecatcher ||
			   !set.members[-1].connectionParts[1].connector.diagramLocation.eyecatcher,
			   'only 2 connectors can be defined'
		
		bodies << BODY_CONNECTOR
		runClosure definition
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_CONNECTORS, BODY_CONNECTOR ]
		bodies -= BODY_CONNECTOR
		
		if (!set.members[-1].connectionParts[0].connector.diagramLocation.eyecatcher) {
			set.members[-1].connectionParts[0].connector.diagramLocation.eyecatcher =
				"set connector[0] ${set.name} (${set.members[-1].record.name})"
		} else {
			set.members[-1].connectionParts[1].connector.diagramLocation.eyecatcher =
				"set connector[1] ${set.name} (${set.members[-1].record.name})"
		}
	}
	
	void connectors(Closure definition) {
		
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE ]
		assert set.members[-1]
		assert set.members[-1].record
		assert set.members[-1].connectionParts
		assert set.members[-1].connectionParts.size() == 1
		assert !set.members[-1].connectionParts[0].connector
		
		set.members[-1].connectionParts[0].connector = SchemaFactory.eINSTANCE.createConnector()
		schema.diagramData.connectors << set.members[-1].connectionParts[0].connector
		set.members[-1].connectionParts[0].connector.diagramLocation = 
			buildAndRegisterDiagramLocation(null)	// don't yet set the location's eyecatcher
		
		set.members[-1].connectionParts << SchemaFactory.eINSTANCE.createConnectionPart()
		schema.diagramData.connectionParts << set.members[-1].connectionParts[1]
		set.members[-1].connectionParts[1].connector = SchemaFactory.eINSTANCE.createConnector()
		schema.diagramData.connectors << set.members[-1].connectionParts[1].connector
		set.members[-1].connectionParts[1].connector.diagramLocation = 
			buildAndRegisterDiagramLocation(null)	// don't yet set the location's eyecatcher
	
		bodies << BODY_CONNECTORS
		runClosure definition
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_CONNECTORS ]
		bodies -= BODY_CONNECTORS
		
		// make sure the connector location eyecatchers are set
		if (!set.members[-1].connectionParts[0].connector.diagramLocation.eyecatcher) {
			set.members[-1].connectionParts[0].connector.diagramLocation.eyecatcher =
				"set connector[0] ${set.name} (${set.members[-1].record.name})"
		}
		if (!set.members[-1].connectionParts[1].connector.diagramLocation.eyecatcher) {
			set.members[-1].connectionParts[1].connector.diagramLocation.eyecatcher =
				"set connector[1] ${set.name} (${set.members[-1].record.name})"
		}
	}
	
	void connectors(String connectorLabel) {
		connectors({ label connectorLabel })
	}

	private void createConnectionLabel(MemberRole memberRole) {
		memberRole.connectionLabel = SchemaFactory.eINSTANCE.createConnectionLabel()
		schema.diagramData.connectionLabels << memberRole.connectionLabel
		memberRole.connectionLabel.alignment = LabelAlignment.LEFT
		// the connection label diagram location's eyecatcher will be set at a later time
		memberRole.connectionLabel.diagramLocation = buildAndRegisterDiagramLocation(null)
	}
	
	private void createFirstConnectionPart(MemberRole memberRole) {
		memberRole.connectionParts << SchemaFactory.eINSTANCE.createConnectionPart()
		schema.diagramData.connectionParts << memberRole.connectionParts[0]
	}
	
	private void createKeyElement(MemberRole memberRole, String elementName, 
								  SortSequence sortSequence) {
								  
		assert memberRole.record
		assert memberRole.record.elements
		assert memberRole.sortKey
		
		memberRole.sortKey.elements << SchemaFactory.eINSTANCE.createKeyElement()
		memberRole.sortKey.elements[-1].sortSequence = sortSequence
		if (elementName != 'dbkey') {
			Element element
			if (elementName) {
				element = memberRole.record.getElement(elementName)
			} else {
				element = memberRole.record.elements[0]
			}
			assert element
			memberRole.sortKey.elements[-1].element = element
		} else {
			assert memberRole.getSet().indexed, "not an indexed set: ${memberRole.set.name}"
		}
	}

	private void createMemberRole() {
		if (!set.mode.is(SetMode.CHAINED)) {
			assert !set.members, 'multiple-member set not allowed'
		}
		set.members << SchemaFactory.eINSTANCE.createMemberRole()
		set.members[-1].membershipOption == SetMembershipOption.MANDATORY_AUTOMATIC
		createConnectionLabel(set.members[-1])
		createFirstConnectionPart(set.members[-1])
	}
	
	private void createOwnerRole(String ownerRecordName) {
		assert !set.owner
		assert set.mode == SetMode.CHAINED || set.mode == SetMode.INDEXED && !set.systemOwner
		assert !ownerIsAutomatic
		String recordName = ownerRecordName ?: "${set.name}-O"
		set.owner = SchemaFactory.eINSTANCE.createOwnerRole()
		if (!schemaIsAutomatic && schema.getRecord(recordName)) {
			set.owner.record = schema.getRecord(recordName)
		} else {		
			set.owner.record = createTemporaryRecord(recordName)
			ownerIsAutomatic = true
		}
	}
	
	private AreaSpecification createSystemOwnerAreaSpecification(String areaName) {
		set.systemOwner.areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification()
		if (schema.getArea(areaName)) {
			set.systemOwner.areaSpecification.area = schema.getArea(areaName)
		} else {
			set.systemOwner.areaSpecification.area = SchemaFactory.eINSTANCE.createSchemaArea()
		}
		schema.areas << set.systemOwner.areaSpecification.area
		set.systemOwner.areaSpecification.area.name = areaName ?: "${set.name}-AREA"
		set.systemOwner.areaSpecification
	}

	private SchemaRecord createTemporaryRecord(String recordName) {
		new RecordModelBuilder( schema : schema ).build(recordName) 
	}
	
	void descending(String elementName) {
		assert bodies == [ BODY_MEMBER, BODY_KEY ]
		assert set.members
		assert set.members[-1].sortKey
		assert set.members[-1].record
		createKeyElement(set.members[-1], elementName, SortSequence.DESCENDING);
	}
	
	void diagram(Closure definition) {
		assert bodies == [ BODY_SYSTEM_OWNER ] || bodies == [ BODY_MEMBER ]
		if (bodies == [ BODY_SYSTEM_OWNER ]) {
			assert set.systemOwner
			assert set.systemOwner.diagramLocation
			bodies << BODY_DIAGRAM
			runClosure definition
			assert bodies == [ BODY_SYSTEM_OWNER, BODY_DIAGRAM ]		
			bodies -= BODY_DIAGRAM
		} else {
			assert set.members[-1]
			bodies << BODY_DIAGRAM
			runClosure definition
			assert bodies == [ BODY_MEMBER, BODY_DIAGRAM ]
			bodies -= BODY_DIAGRAM
		}
	}
	
	void displacement(int displacementPageCount) {
		assert bodies == [ BODY_INDEX ]
		assert !set.indexedSetModeSpecification.symbolicIndexName
		set.indexedSetModeSpecification.displacementPageCount = displacementPageCount
	}
	
	void duplicates(String duplicatesOption) {
		assert bodies == [ BODY_MEMBER, BODY_KEY ]
		assert set.members
		assert set.members[-1].sortKey
		assert set.members[-1].record
		
		DuplicatesOption resolvedDuplicatesOption = DuplicatesOption.valueOf(duplicatesOption.replaceAll(' ', '_'))
		if (set.members[-1].sortKey.elements[-1].dbkey) {
			assert resolvedDuplicatesOption == DuplicatesOption.NOT_ALLOWED, "'$duplicatesOption' is not allowed for indexed sets: ${set.name}"
		}
		set.members[-1].sortKey.duplicatesOption = resolvedDuplicatesOption
	}
	
	void end(Closure definition) {
		
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE ]
		assert set.members
		assert set.members[-1].record
		assert set.members[-1].connectionParts
		assert !set.members[-1].connectionParts[-1].targetEndpointLocation
		
		String eyecatcher = "set ${set.name} member endpoint (${set.members[-1].record.name})"
		set.members[-1].connectionParts[-1].targetEndpointLocation = buildAndRegisterDiagramLocation(eyecatcher)
		
		bodies << BODY_END
		runClosure definition
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_END ]
		bodies -= BODY_END
	}
	
	private void fixSetName(String setName) {
		assert !bodies
		set.name = setName
		if (ownerIsAutomatic) {
			set.owner.record.name = "${set.name}-O"
			set.owner.record.areaSpecification.area.name = "${set.owner.record.name}-AREA" 
			set.owner.record.diagramLocation.eyecatcher = "record ${set.owner.record.name}"
		}
		if (memberIsAutomatic) {
			assert set.members.size() == 1
			assert set.members[0].connectionParts == 1
			assert !set.members[0].connectionParts[0].sourceEndpointLocation
			assert !set.members[0].connectionParts[0].targetEndpointLocation
			assert !set.members[0].connectionParts[0].bendpointLocations
			assert !set.members[0].connectionParts[0].connector
			set.members[0].record.name = "${set.name}-M"
			set.members[0].record.areaSpecification.area.name = "${set.members[0].record.name}-AREA"
			set.members[0].record.diagramLocation.eyecatcher = "record ${set.members[0].record.name}"
			set.members[0].connectionLabel.diagramLocation.eyecatcher =
				"set label ${set.name} (${set.members[0].record.name})"			
		}
	}

	private String generateSetName() {
		for (int i in 1..Integer.MAX_VALUE) {
			String candidate = "SET$i"
			if (!schema.getSet(candidate)) {
				return candidate
			}	
		}
		throw new RuntimeException('internal error: cannot generate set name');
	}
	
	void index(String symbolicIndexName) {
		assert !bodies		
		assert !set.indexedSetModeSpecification
		assert !set.owner
		assert !set.systemOwner
		assert !set.vsamIndex
		assert !set.members
		
		set.mode = SetMode.INDEXED
		set.indexedSetModeSpecification = SchemaFactory.eINSTANCE.createIndexedSetModeSpecification()
		set.indexedSetModeSpecification.symbolicIndexName = symbolicIndexName	
	}
	
	void index(Closure definition) {
		assert !bodies
		assert !set.indexedSetModeSpecification
		assert !set.owner
		assert !set.systemOwner
		assert !set.vsamIndex
		assert !set.members
		set.mode = SetMode.INDEXED
		set.indexedSetModeSpecification = SchemaFactory.eINSTANCE.createIndexedSetModeSpecification()
		bodies << BODY_INDEX
		runClosure definition
		assert bodies == [ BODY_INDEX ]
		bodies -= BODY_INDEX
	}
	
	void index(int indexDbkeyPosition) {
		assert bodies == [ BODY_MEMBER ]
		assert set.mode == SetMode.INDEXED		
		assert set.members.size() == 1
		set.members[-1].indexDbkeyPosition = indexDbkeyPosition		
	}
	
	void key(Closure definition) {
		
		assert bodies == [ BODY_MEMBER ]
		assert set.members
		assert !set.members[-1].sortKey
		
		if (!set.members[-1].record) {
			completeMemberRole(null)
		}
		set.members[-1].sortKey = SchemaFactory.eINSTANCE.createKey()
		set.members[-1].record.keys << set.members[-1].sortKey
		set.members[-1].sortKey.duplicatesOption = DuplicatesOption.NOT_ALLOWED
		set.members[-1].sortKey.compressed = false;
		set.members[-1].sortKey.naturalSequence = false;
		
		bodies << BODY_KEY
		runClosure definition
		assert bodies == [ BODY_MEMBER, BODY_KEY ]
		
		if (!set.members[-1].sortKey.elements) {
			createKeyElement(set.members[-1], null, SortSequence.ASCENDING)
		}
		
		bodies -= BODY_KEY
	}
	
	void keys(int keyCount) {
		assert bodies == [ BODY_INDEX ]
		assert !set.indexedSetModeSpecification.symbolicIndexName
		set.indexedSetModeSpecification.keyCount = keyCount
	}
	
	void label(Closure definition) {
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM ]		
		assert set.members[-1].connectionLabel
		assert set.members[-1].connectionLabel.diagramLocation
		bodies << BODY_LABEL
		runClosure definition
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LABEL ]
		bodies -= BODY_LABEL
	}
	
	void label(String label) {
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_CONNECTORS ]
		assert set.members[-1].connectionParts.size() == 2
		assert set.members[-1].connectionParts[0].connector
		assert !set.members[-1].connectionParts[0].connector.label
		assert set.members[-1].connectionParts[1].connector
		assert !set.members[-1].connectionParts[1].connector.label
		set.members[-1].connectionParts[0].connector.label = label
		set.members[-1].connectionParts[1].connector.label = label
	}
	
	void line(Closure definition) {
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM ]		
		assert set.members[-1]
		assert set.members[-1].connectionParts
		assert set.members[-1].connectionParts.size() == 1
		assert !set.members[-1].connectionParts[0].sourceEndpointLocation
		assert !set.members[-1].connectionParts[0].targetEndpointLocation
		assert !set.members[-1].connectionParts[0].bendpointLocations
		assert !set.members[-1].connectionParts[0].connector
		bodies << BODY_LINE
		runClosure definition
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE ]
		if (!set.systemOwner) {
			if (set.members[-1].connectionParts[0].bendpointLocations ||
				set.members[-1].connectionParts.size() > 1 &&
				set.members[-1].connectionParts[1].bendpointLocations) {
				
				assert set.members[-1].connectionParts[0].sourceEndpointLocation &&
					   set.members[-1].connectionParts[-1].targetEndpointLocation,
					   'source- and target endpoints are mandatory when bendpoints are defined'
			}
		}
		bodies -= BODY_LINE		
	}
	
	void name(String name) {
		assert !bodies || bodies == [ BODY_OWNER ] || bodies == [ BODY_MEMBER ]
		if (!bodies) {
			fixSetName(name)
		} else if (bodies == [ BODY_OWNER ]) {
			assert !set.owner
			createOwnerRole(name)
		} else if (bodies == [ BODY_MEMBER ]) {
			completeMemberRole(name)
		}
	}
	
	private void naturalSequence() {
		assert bodies == [ BODY_MEMBER, BODY_KEY ]
		assert set.members
		assert set.members[-1].sortKey
		assert !set.members[-1].sortKey.naturalSequence
		set.members[-1].sortKey.naturalSequence = true
	}
	
	void member(Closure definition) {
		assert !bodies
		if (bufferedName) {
			member(bufferedName, definition)
			bufferedName = null
		} else {
			bodies << BODY_MEMBER
			createMemberRole()
			runClosure definition
			if (!set.members[-1].record) {
				completeMemberRole(null)
			}
			assert bodies == [ BODY_MEMBER ]
			bodies -= BODY_MEMBER
		}
	}
	
	void member(String recordName) {
		member(recordName, { })
	}
	
	void member(String recordName, Closure definition) {
		assert !bodies
		createMemberRole()
		completeMemberRole(recordName)
		bodies << BODY_MEMBER
		runClosure definition
		assert bodies == [ BODY_MEMBER ]
		bodies -= BODY_MEMBER
	}
	
	void membership(String membershipOptionAsString) {
		assert bodies == [ BODY_MEMBER ]
		assert set.members
		set.members[-1].membershipOption = 
			SetMembershipOption.valueOf(membershipOptionAsString.replaceAll(" ", "_"))
	}
	
	void next(int nextDbkeyPosition) {
		assert bodies == [ BODY_OWNER ] || bodies == [ BODY_MEMBER ]
		if (bodies == [ BODY_OWNER ]) {
			assert set.owner
			set.owner.nextDbkeyPosition = nextDbkeyPosition
		} else {
			assert set.members
			set.members[-1].nextDbkeyPosition = nextDbkeyPosition
		}
	}
	
	void offsetPages(int offsetPageCount) {
		assert bodies == [ BODY_SYSTEM_OWNER, BODY_AREA ]
		assert !set.systemOwner.areaSpecification.symbolicSubareaName
		assert !set.systemOwner.areaSpecification.offsetExpression
		set.systemOwner.areaSpecification.offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression()
		set.systemOwner.areaSpecification.offsetExpression.offsetPageCount = offsetPageCount
	}
	
	void offsetPercent(int offsetPercent) {
		assert bodies == [ BODY_SYSTEM_OWNER, BODY_AREA ]
		assert !set.systemOwner.areaSpecification.symbolicSubareaName
		assert !set.systemOwner.areaSpecification.offsetExpression
		set.systemOwner.areaSpecification.offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression()
		set.systemOwner.areaSpecification.offsetExpression.offsetPercent = offsetPercent
	}

	void order(String orderAsString) {
		assert !bodies
		assert !set.owner
		assert !set.systemOwner
		assert !set.vsamIndex
		assert !set.members
		set.order = SetOrder.valueOf(orderAsString)
	}
	
	void owner(Closure definition) {
		assert !bodies
		assert !set.owner
		assert !set.systemOwner
		assert !set.vsamIndex
		if (bufferedName) {
			owner(bufferedName, definition)
			bufferedName = null
		} else {
			bodies << BODY_OWNER
			runClosure definition
			if (!set.owner) {
				createOwnerRole(null)
			}
			assert bodies == [ BODY_OWNER ]
			bodies -= BODY_OWNER
		}		
	}
	
	void owner(String recordName) {
		owner(recordName, { })	
	}
	
	void owner(String recordName, Closure definition) {
		assert !bodies
		assert !set.owner
		assert !set.systemOwner
		assert !set.vsamIndex
		createOwnerRole(recordName)
		bodies << BODY_OWNER
		runClosure definition
		assert bodies == [ BODY_OWNER ]
		bodies -= BODY_OWNER
	}
	
	void owner(int ownerDbkeyPosition) {
		assert bodies == [ BODY_MEMBER ]
		assert set.members
		set.members[-1].ownerDbkeyPosition = ownerDbkeyPosition
	}
	
	void pages(int pageCount) {
		assert bodies == [ BODY_SYSTEM_OWNER, BODY_AREA ]
		assert !set.systemOwner.areaSpecification.symbolicSubareaName
		if (set.systemOwner.areaSpecification.offsetExpression) {
			assert !set.systemOwner.areaSpecification.offsetExpression.percent
		} else {
			set.systemOwner.areaSpecification.offsetExpression = 
				SchemaFactory.eINSTANCE.createOffsetExpression()
		}
		set.systemOwner.areaSpecification.offsetExpression.pageCount = pageCount
	}
	
	void percent(int percent) {
		assert bodies == [ BODY_SYSTEM_OWNER, BODY_AREA ]
		assert !set.systemOwner.areaSpecification.symbolicSubareaName
		if (set.systemOwner.areaSpecification.offsetExpression) {
			assert !set.systemOwner.areaSpecification.offsetExpression.pageCount
		} else {
			set.systemOwner.areaSpecification.offsetExpression =
				SchemaFactory.eINSTANCE.createOffsetExpression()
		}
		set.systemOwner.areaSpecification.offsetExpression.percent = percent
	}
	
	void prior(int priorDbkeyPosition) {
		assert bodies == [ BODY_OWNER ] || bodies == [ BODY_MEMBER ]
		if (bodies == [ BODY_OWNER ]) {
			assert set.owner
			set.owner.priorDbkeyPosition = priorDbkeyPosition
		} else {
			assert set.members
			set.members[-1].priorDbkeyPosition = priorDbkeyPosition
		}
	}
	
	void start(Closure definition) {
		
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE ]
		assert set.members
		assert set.members[-1].record
		assert set.members[-1].connectionParts
		assert set.members[-1].connectionParts.size() == 1
		assert !set.members[-1].connectionParts[0].sourceEndpointLocation
		assert !set.members[-1].connectionParts[0].targetEndpointLocation
		assert !set.members[-1].connectionParts[0].bendpointLocations
		assert !set.members[-1].connectionParts[0].connector
		
		String eyecatcher = "set ${set.name} owner endpoint (${set.members[-1].record.name})"
		set.members[-1].connectionParts[0].sourceEndpointLocation = buildAndRegisterDiagramLocation(eyecatcher)
		
		bodies << BODY_START
		runClosure definition
		assert bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_START ]		
		bodies -= BODY_START
	}
	
	void subarea(String symbolicSubareaName) {
		assert bodies == [ BODY_SYSTEM_OWNER, BODY_AREA ] 
		assert !set.systemOwner.areaSpecification.symbolicSubareaName
		assert !set.systemOwner.areaSpecification.offsetExpression
		set.systemOwner.areaSpecification.symbolicSubareaName = symbolicSubareaName
	}
	
	void systemOwner(Closure definition) {
		assert !bodies
		assert !set.owner
		assert !set.systemOwner 
		assert !set.vsamIndex
		assert !set.members
		
		if (!set.indexedSetModeSpecification) {
			index({ })
		}
		
		set.systemOwner = SchemaFactory.eINSTANCE.createSystemOwner()
		set.systemOwner.diagramLocation = buildAndRegisterDiagramLocation("system owner ${set.name}")
		
		bodies << BODY_SYSTEM_OWNER
		runClosure definition
		assert bodies == [ BODY_SYSTEM_OWNER ]
		
		if (!set.systemOwner.areaSpecification) {
			createSystemOwnerAreaSpecification(null)
		}
		
		bodies -= BODY_SYSTEM_OWNER
	}
	
	void vsamIndex(Closure definition) {
		
		assert !bodies
		assert !set.indexedSetModeSpecification
		assert !set.owner
		assert !set.systemOwner
		assert !set.vsamIndex
		assert !set.members
		
		set.mode = SetMode.VSAM_INDEX
		set.order = SetOrder.SORTED
		set.vsamIndex = SchemaFactory.eINSTANCE.createVsamIndex()
		set.vsamIndex.diagramLocation = buildAndRegisterDiagramLocation("VSAM index ${set.name}")
		
		bodies << BODY_VSAM_INDEX
		runClosure definition
		assert bodies == [ BODY_VSAM_INDEX ]		
		bodies -= BODY_VSAM_INDEX
	}
	
	void x(int x) {
		assert bodies == [ BODY_SYSTEM_OWNER, BODY_DIAGRAM ] || 
			   bodies == [ BODY_VSAM_INDEX ] ||
			   bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LABEL ] ||
			   bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_START ] ||
			   bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_END ] ||
			   bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_BENDPOINT ] ||
			   bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_CONNECTORS, BODY_CONNECTOR ]
		if (bodies == [ BODY_SYSTEM_OWNER, BODY_DIAGRAM ]) {
			set.systemOwner.diagramLocation.x = x
		} else if (bodies == [ BODY_VSAM_INDEX ]) {
			set.vsamIndex.diagramLocation.x = x
		} else if (bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LABEL ] ) {
			set.members[-1].connectionLabel.diagramLocation.x = x
		} else if (bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_START ]) {
			assert set.members[-1].connectionParts[0].sourceEndpointLocation
			set.members[-1].connectionParts[0].sourceEndpointLocation.x = x
		} else if (bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_END ]) {
			assert set.members[-1].connectionParts[-1].targetEndpointLocation
			set.members[-1].connectionParts[-1].targetEndpointLocation.x = x
		} else if (bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_BENDPOINT ]) {
			assert set.members[-1].connectionParts[-1].bendpointLocations
			set.members[-1].connectionParts[-1].bendpointLocations[-1].x = x
		} else {
			assert set.members[-1].connectionParts.size() == 2
			if (!set.members[-1].connectionParts[0].connector.diagramLocation.eyecatcher) {
				set.members[-1].connectionParts[0].connector.diagramLocation.x = x
			} else {
				set.members[-1].connectionParts[1].connector.diagramLocation.x = x
			}
		}
	}
	
	void y(int y) {
		assert bodies == [ BODY_SYSTEM_OWNER, BODY_DIAGRAM ] || 
			   bodies == [ BODY_VSAM_INDEX ] ||
			   bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LABEL ] ||
			   bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_START ] ||
			   bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_END ] ||
			   bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_BENDPOINT ] ||
			   bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_CONNECTORS, BODY_CONNECTOR ]
		if (bodies == [ BODY_SYSTEM_OWNER, BODY_DIAGRAM ]) {
			set.systemOwner.diagramLocation.y = y
		} else if (bodies == [ BODY_VSAM_INDEX ]) {
			set.vsamIndex.diagramLocation.y = y
		} else if (bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LABEL ] ) {
			set.members[-1].connectionLabel.diagramLocation.y = y
		} else if (bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_START ]) {
			assert set.members[-1].connectionParts[0].sourceEndpointLocation
			set.members[-1].connectionParts[0].sourceEndpointLocation.y = y
		} else if (bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_END ]) {
			assert set.members[-1].connectionParts[-1].targetEndpointLocation
			set.members[-1].connectionParts[-1].targetEndpointLocation.y = y
		} else if (bodies == [ BODY_MEMBER, BODY_DIAGRAM, BODY_LINE, BODY_BENDPOINT ]) {
			assert set.members[-1].connectionParts[-1].bendpointLocations
			set.members[-1].connectionParts[-1].bendpointLocations[-1].y = y
		} else {
			assert set.members[-1].connectionParts.size() == 2
			if (!set.members[-1].connectionParts[0].connector.diagramLocation.eyecatcher) {
				set.members[-1].connectionParts[0].connector.diagramLocation.y = y
			} else {
				set.members[-1].connectionParts[1].connector.diagramLocation.y = y
			}
		}
	}
	 	
}
