/**
 * Copyright (C) 2015  Luc Hermans
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

import org.lh.dmlj.schema.ConnectionLabel
import org.lh.dmlj.schema.ConnectionPart
import org.lh.dmlj.schema.DiagramLocation
import org.lh.dmlj.schema.DuplicatesOption
import org.lh.dmlj.schema.IndexedSetModeSpecification
import org.lh.dmlj.schema.Key
import org.lh.dmlj.schema.LabelAlignment
import org.lh.dmlj.schema.MemberRole
import org.lh.dmlj.schema.OwnerRole
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.Set
import org.lh.dmlj.schema.SetMembershipOption
import org.lh.dmlj.schema.SetMode
import org.lh.dmlj.schema.SetOrder
import org.lh.dmlj.schema.SortSequence

import spock.lang.Unroll

class SetModelBuilderSpec extends AbstractModelBuilderSpec {
	
	void assertChainedSet(Set set, String expectedName) {
		assertChainedSet(set, expectedName, [:])
	}
	
	void assertChainedSet(Set set, String expectedName, Schema schema) {
		assertChainedSet(set, expectedName, schema, [:])
	}
	
	void assertChainedSet(Set set, String expectedName, Map<String, ?> extras) {
		assert set
		assert set.schema
		assert set.schema.name == TEMP_SCHEMA_NAME
		assert set.schema.version == TEMP_SCHEMA_VERSION		
		assertChainedSet(set, expectedName, set.schema, extras)
	}
	
	void assertChainedSet(Set set, String expectedName, Schema schema, Map<String, ?> extras) {		
								 
		assert set
		assert set.schema
		assert set.schema == schema
		assertBasicSchema(set.schema, schema.name, schema.version)
		assert set.schema.procedures.empty
		
		assert set.name == expectedName
		assert set.mode == SetMode.CHAINED
		if (extras.setOrder) {
			assert set.order == extras.setOrder
		} else {
			assert set.order == SetOrder.NEXT
		}
		
		assertOwner(set.owner, extras)
		
		assert set.members
		assert set.members.size() == 1	
		if (!extras.memberNextDbkeyPosition) {	
			extras.memberNextDbkeyPosition = 1
		}
		assertMember(set.members[0], extras)
	}
	
	void assertIndexedSetModeSpecification(IndexedSetModeSpecification indexedSetModeSpecification,
										   Map<String, ?> extras) {
										   
		if (extras.symbolicIndexName) {
			assert indexedSetModeSpecification.symbolicIndexName == extras.symbolicIndexName
		} else {
			assert !indexedSetModeSpecification.symbolicIndexName
		}
		if (extras.indexKeyCount) {
			assert indexedSetModeSpecification.keyCount == extras.indexKeyCount
		} else {
			assert !indexedSetModeSpecification.keyCount
		}
		if (extras.indexDisplacementPageCount) {
			assert indexedSetModeSpecification.displacementPageCount == extras.indexDisplacementPageCount
		} else {
			assert !indexedSetModeSpecification.displacementPageCount
		}
	}
	
	void assertMember(MemberRole memberRole, Map<String, ?> extras) {
		
		assert memberRole
		assert memberRole.set
		assert memberRole.set.schema
		assert memberRole.record
		assert memberRole.record.schema == memberRole.set.schema
		
		if (!extras.memberRecord && !extras.memberRecordName) {			
			extras.memberRecordName = "${memberRole.set.name}-M"
		}
		if (!extras.memberAreaName) {			
			extras.memberAreaName = "${memberRole.record.name}-AREA"
		}
		if (!extras.memberLocationX) {			
			extras.ownerLocationX = 0
		}
		if (!extras.memberLocationY) {			
			extras.ownerLocationY = 0
		}
		
		if (extras.memberRecord) {
			assert memberRole.record.is(extras.memberRecord)
		} else {
			assert memberRole.record.name == extras.memberRecordName
		}
		assert memberRole.set.schema.records
		if (extras.memberRecord) {
			assert memberRole.set.schema.records.find {
				SchemaRecord record -> record.is(extras.memberRecord)
			}
		} else {
			assert memberRole.set.schema.records.find { 
				SchemaRecord record -> record.name == "${extras.memberRecordName}"
			}
		}
		
		assert memberRole.record.areaSpecification
		assert memberRole.record.areaSpecification.area.name == "${extras.memberAreaName}"
		assert memberRole.set.schema.areas.find { 
			SchemaArea area -> area.name == "${extras.memberAreaName}" 
		}
		
		if (!extras.memberLocationX) {
			extras.memberLocationX = 0
		}
		if (!extras.memberLocationY) {
			extras.memberLocationY = 0
		}
		if (extras.memberNextDbkeyPosition) {	
			assert memberRole.nextDbkeyPosition == extras.memberNextDbkeyPosition
		} else {
			assert !memberRole.nextDbkeyPosition
		}
		if (extras.memberPriorDbkeyPosition) {	
			assert memberRole.priorDbkeyPosition == extras.memberPriorDbkeyPosition
		} else {
			assert !memberRole.priorDbkeyPosition
		}
		if (extras.memberOwnerDbkeyPosition) {	
			assert memberRole.ownerDbkeyPosition == extras.memberOwnerDbkeyPosition
		} else {
			assert !memberRole.ownerDbkeyPosition
		}
		if (extras.memberIndexDbkeyPosition) {
			assert memberRole.indexDbkeyPosition == extras.memberIndexDbkeyPosition
		} else {
			assert !memberRole.indexDbkeyPosition
		}
		if (memberRole.set.order == SetOrder.SORTED && !extras.sortKeyChecker) {
			extras.sortKeyChecker = { Key sortKey, int index ->
				assert sortKey
				assert sortKey.memberRole
				assert sortKey.memberRole.record
				assert sortKey.memberRole.record.keys.find { Key key -> key.is(sortKey) }
				if (extras.duplicatesOption) {
					assert sortKey.duplicatesOption == extras.duplicatesOption
				} else {
					assert sortKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
				}
				if (extras.naturalSequence) {
					assert sortKey.naturalSequence == extras.naturalSequence
				} else {
					assert !sortKey.naturalSequence
				}
				if (extras.compressed) {
					assert sortKey.compressed == extras.compressed
				} else {
					assert !sortKey.compressed
				}
				assert sortKey.elements, 
					   "member record without sort key: ${sortKey.memberRole.record.name}"
				assert sortKey.elements.size() == 1
				assert sortKey.elements[0].element == sortKey.memberRole.record.elements[0]
				if (extras.sortSequence) {
					assert sortKey.elements[0].sortSequence == extras.sortSequence
				} else {
					assert sortKey.elements[0].sortSequence == SortSequence.ASCENDING
				}
			}	
		}
		if (!extras.connectionLabelX) {
			extras.connectionLabelX = 0
		}
		if (!extras.connectionLabelY) {
			extras.connectionLabelY = 0
		}
		if (!extras.bendpointChecker) {
			extras.bendpointChecker = { List<ConnectionPart> connectionParts ->
				assert connectionParts.size() in [ 1, 2 ]
				assert !connectionParts[0].bendpointLocations
				assert !connectionParts[-1].bendpointLocations
			}
		}
		if (!extras.connectorChecker) {
			extras.connectorChecker = { List<ConnectionPart> connectionParts ->
				assert !memberRole.connectionParts[0].connector
				assert !memberRole.connectionParts[-1].connector
			}
		}
		
		assert memberRole.record.diagramLocation
		assert memberRole.record.diagramLocation.x == extras.memberLocationX
		assert memberRole.record.diagramLocation.y == extras.memberLocationY
		assert memberRole.record.diagramLocation.eyecatcher == "record ${memberRole.record.name}"
		assert memberRole.set.schema.diagramData
		assert memberRole.set.schema.diagramData.locations
		assert memberRole.set.schema.diagramData.locations.find {
			DiagramLocation diagramLocation -> diagramLocation == memberRole.record.diagramLocation 
		}
		
		if (extras.membershipOption) {
			assert memberRole.membershipOption == extras.membershipOption
		} else {
			assert memberRole.membershipOption == SetMembershipOption.MANDATORY_AUTOMATIC
		}
		
		if (memberRole.set.order != SetOrder.SORTED) {
			assert !memberRole.sortKey
		} else {
			assert memberRole.sortKey
			extras.sortKeyChecker(memberRole.sortKey, memberRole.set.members.indexOf(memberRole))
			assert memberRole.record.keys.find { Key sortKey -> sortKey.is(memberRole.sortKey) }
		}
		
		assert memberRole.connectionLabel
		assert memberRole.connectionLabel.alignment == LabelAlignment.LEFT
		assert memberRole.connectionLabel.diagramLocation
		assert memberRole.connectionLabel.diagramLocation.x == extras.connectionLabelX
		assert memberRole.connectionLabel.diagramLocation.y == extras.connectionLabelY
		assert memberRole.connectionLabel.diagramLocation.eyecatcher ==
			"set label ${memberRole.set.name} (${memberRole.record.name})"
		assert memberRole.set.schema.diagramData.connectionLabels
		assert memberRole.set.schema.diagramData.connectionLabels.find {
			ConnectionLabel connectionLabel -> connectionLabel == memberRole.connectionLabel
		}
		
		assert memberRole.connectionParts
		assert memberRole.connectionParts.size() in [ 1, 2 ]
		assert memberRole.set.schema.diagramData.connectionParts.find { connectionPart ->
			connectionPart.is(memberRole.connectionParts[0])	
		}
		if (memberRole.connectionParts.size() > 1) {
			assert memberRole.set.schema.diagramData.connectionParts.find { connectionPart ->
				connectionPart.is(memberRole.connectionParts[1])
			}
		}
		
		if (extras.sourceEndpointLocationX || extras.sourceEndpointLocationY) {			
			assert memberRole.connectionParts[0].sourceEndpointLocation
			assert memberRole.connectionParts[0].sourceEndpointLocation.x == 
				extras.sourceEndpointLocationX ?: 0
			assert memberRole.connectionParts[0].sourceEndpointLocation.y ==
				extras.sourceEndpointLocationY ?: 0
			assert memberRole.connectionParts[0].sourceEndpointLocation.eyecatcher == "set ${memberRole.set.name} owner endpoint (${memberRole.record.name})"
			assert memberRole.set.schema.diagramData.locations.find { 
				diagramLocation -> 
					diagramLocation == memberRole.connectionParts[0].sourceEndpointLocation
			}
		} else {
			memberRole.connectionParts.each { assert !it.sourceEndpointLocation }
		}
		if (memberRole.connectionParts.size() > 1) {
			assert !memberRole.connectionParts[-1].sourceEndpointLocation
		}
		
		if (extras.targetEndpointLocationX || extras.targetEndpointLocationY) {
			assert memberRole.connectionParts[-1].targetEndpointLocation.x == 
				extras.targetEndpointLocationX ?: 0
			assert memberRole.connectionParts[-1].targetEndpointLocation.y == 
				extras.targetEndpointLocationY ?: 0
			assert memberRole.connectionParts[-1].targetEndpointLocation.eyecatcher == "set ${memberRole.set.name} member endpoint ($memberRole.record.name)"
			assert memberRole.set.schema.diagramData.locations.find {
				diagramLocation ->
					diagramLocation == memberRole.connectionParts[-1].targetEndpointLocation 
			}
		} else {
			memberRole.connectionParts.each { assert !it.targetEndpointLocation }
		}
		if (memberRole.connectionParts.size() > 1) {
			assert !memberRole.connectionParts[0].targetEndpointLocation
		}
		
		extras.bendpointChecker(memberRole.connectionParts)		
		extras.connectorChecker(memberRole.connectionParts)
		
		// make sure the bendpoint and connector diagram locations are anchored in the schema's
		// diagram data object [that's right, we don't trust the supplied checkers here :-)]
		memberRole.connectionParts.each { 
			ConnectionPart connectionPart ->
				connectionPart.bendpointLocations.each { DiagramLocation bendpoint ->
					assert memberRole.set.schema.diagramData.locations.find {
						diagramLocation -> diagramLocation == bendpoint }
				if (connectionPart.connector) {
					assert memberRole.set.schema.diagramData.locations.find {
						diagramLocation -> 
							diagramLocation == connectionPart.connector.diagramLocation 
						}
				}
			}
		}
	}
	
	void assertMultipleMemberSet(Set set, String expectedName, Map<String, ?> extras) {
		assert set
		assert set.schema
		assert set.schema.name == TEMP_SCHEMA_NAME
		assert set.schema.version == TEMP_SCHEMA_VERSION
		assertMultipleMemberSet(set, expectedName, set.schema, extras)
	}
	
	void assertMultipleMemberSet(Set set, String expectedName, Schema schema, Map<String, ?> extras) {
								 
		assert set
		assert set.schema
		assert set.schema == schema
		assertBasicSchema(set.schema, schema.name, schema.version)
		assert set.schema.procedures.empty
		
		assert set.name == expectedName
		assert set.mode == SetMode.CHAINED
		if (extras.setOrder) {
			assert set.order == extras.setOrder
		} else {
			assert set.order == SetOrder.NEXT
		}
		
		assertOwner(set.owner, extras)
		
		assert set.members
		assert extras.memberChecker
		if (!extras.memberNextDbkeyPosition) {
			extras.memberNextDbkeyPosition = 1
		}
		if (set.order == SetOrder.SORTED) {
			assert extras.sortKeyCheckers
			assert extras.sortKeyCheckers.size() == set.members.size(),
				   "sortKeyCheckers: member count mismatch (expected: ${set.members.size()} " +
				   "actual: ${extras.sortKeyCheckers.size()})"
		}
		extras.memberChecker(set.members)
		set.members.eachWithIndex { memberRole, index ->
						
			// make sure the standard member test ALWAYS passes regarding member record and area
			assert memberRole.record
			extras.memberRecordName = memberRole.record.name
			extras.memberRecord = null
			extras.memberAreaName = memberRole.record.areaSpecification.area.name
			
			// make sure the standard member test ALWAYS passes regarding set pointers; it's up to
			// the memberChecker to properly check all pointers
			extras.memberNextDbkeyPosition = memberRole.nextDbkeyPosition
			if (memberRole.priorDbkeyPosition) {
				extras.memberPriorDbkeyPosition = memberRole.priorDbkeyPosition
			} else {
				extras.memberPriorDbkeyPosition = null	
			}
			if (memberRole.ownerDbkeyPosition) {
				extras.memberOwnerDbkeyPosition = memberRole.ownerDbkeyPosition
			} else {
				extras.memberOwnerDbkeyPosition = null	
			}
			
			// make sure the standard member test ALWAYS passes regarding set membership option
			extras.membershipOption = memberRole.membershipOption
			
			// select the member specific sortKeyChecker in case of a sorted set
			if (set.order == SetOrder.SORTED) {
				extras.sortKeyChecker = extras.sortKeyCheckers[index]
			}
			
			// perform the standard member checks
			assertMember(memberRole, extras)
		}		
	}
	
	void assertOwner(OwnerRole ownerRole, Map<String, ?> extras) {
				
		assert ownerRole
		assert ownerRole.set
		assert ownerRole.set.schema
		assert ownerRole.record
		assert ownerRole.record.schema == ownerRole.set.schema
		assert !ownerRole.set.vsamIndex
		assert !ownerRole.set.systemOwner
		
		if (!extras.ownerRecord && !extras.ownerRecordName) {
			extras.ownerRecordName = "${ownerRole.set.name}-O"
		}
		if (!extras.ownerAreaName) {
			extras.ownerAreaName = "${ownerRole.record.name}-AREA"
		}
		if (!extras.ownerNextDbkeyPosition) {
			extras.ownerNextDbkeyPosition = 1
		}
		if (!extras.ownerLocationX) {
			extras.ownerLocationX = 0
		}
		if (!extras.ownerLocationY) {
			extras.ownerLocationY = 0
		}		
		
		if (extras.ownerRecord) {
			assert ownerRole.record.is(extras.ownerRecord)	
		} else {
			assert ownerRole.record.name == extras.ownerRecordName
		}
		assert ownerRole.set.schema.records
		if (extras.ownerRecord) {
			assert ownerRole.set.schema.records.find {
				SchemaRecord record -> record.is(extras.ownerRecord)
			}
		} else {
			assert ownerRole.set.schema.records.find { 
				SchemaRecord record -> record.name == "${extras.ownerRecordName}"
			}
		}
		
		assert ownerRole.record.areaSpecification
		assert ownerRole.record.areaSpecification.area
		assert ownerRole.record.areaSpecification.area.name == extras.ownerAreaName
		assert ownerRole.set.schema.areas
		assert ownerRole.set.schema.areas.find { 
			SchemaArea area -> area.name == "${extras.ownerAreaName}"
		}
		assert ownerRole.record.areaSpecification.area.schema == ownerRole.record.schema
				
		assert ownerRole.nextDbkeyPosition == extras.ownerNextDbkeyPosition		
		if (extras.ownerPriorDbkeyPosition) {
			assert ownerRole.priorDbkeyPosition == extras.ownerPriorDbkeyPosition
		} 
		
		assert ownerRole.record.diagramLocation
		assert ownerRole.record.diagramLocation.x == extras.ownerLocationX
		assert ownerRole.record.diagramLocation.y == extras.ownerLocationY
		assert ownerRole.record.diagramLocation.eyecatcher == "record ${ownerRole.record.name}"
		assert ownerRole.set.schema.diagramData
		assert ownerRole.set.schema.diagramData.locations
		assert ownerRole.set.schema.diagramData.locations.find {
			DiagramLocation diagramLocation -> diagramLocation == ownerRole.record.diagramLocation
		}
	}
	
	void assertUserOwnedIndexedSet(Set set, String expectedName) {
		assertUserOwnedIndexedSet(set, expectedName, [:])
	}
	
	void assertUserOwnedIndexedSet(Set set, String expectedName, Schema schema) {
		assertUserOwnedIndexedSet(set, expectedName, schema, [:])
	}
	
	void assertUserOwnedIndexedSet(Set set, String expectedName, Map<String, ?> extras) {
		assert set
		assert set.schema
		assert set.schema.name == TEMP_SCHEMA_NAME
		assert set.schema.version == TEMP_SCHEMA_VERSION
		assertUserOwnedIndexedSet(set, expectedName, set.schema, extras)
	}
	
	void assertUserOwnedIndexedSet(Set set, String expectedName, Schema schema, Map<String, ?> extras) {
					   
		assert set
		assert set.schema
		assert set.schema == schema
		assertBasicSchema(set.schema, schema.name, schema.version)
		
		assert set.name == expectedName
		assert set.mode == SetMode.INDEXED
		if (extras.setOrder) {
			assert set.order == extras.setOrder
		} else {
			assert set.order == SetOrder.NEXT
		}
		
		assertIndexedSetModeSpecification(set.indexedSetModeSpecification, extras)
		
		assertOwner(set.owner, extras)
		
		assert set.members
		assert set.members.size() == 1
		if (!extras.memberIndexDbkeyPosition) {
			extras.memberIndexDbkeyPosition = 1
		}
		assertMember(set.members[0], extras)
	}
			
	void assertSystemOwnedIndexedSet(Set set, String expectedName) {
		assertSystemOwnedIndexedSet(set, expectedName, [:])
	}
   
	void assertSystemOwnedIndexedSet(Set set, String expectedName, Schema schema) {
		assertSystemOwnedIndexedSet(set, expectedName, schema, [:])
	}
   
	void assertSystemOwnedIndexedSet(Set set, String expectedName, Map<String, ?> extras) {
		assert set
		assert set.schema
		assert set.schema.name == TEMP_SCHEMA_NAME
		assert set.schema.version == TEMP_SCHEMA_VERSION
		assertSystemOwnedIndexedSet(set, expectedName, set.schema, extras)
	}
								   					   
	void assertSystemOwnedIndexedSet(Set set, String expectedName, Schema schema, Map<String, ?> extras) {
					   
		assert set
		assert set.schema
		assert set.schema == schema
		assertBasicSchema(set.schema, schema.name, schema.version)
		assert set.schema.procedures.empty
	
		assert set.name == expectedName
		assert set.mode == SetMode.INDEXED
		if (extras.setOrder) {
			assert set.order == extras.setOrder
		} else {
			assert set.order == SetOrder.NEXT
		}
		
		assertIndexedSetModeSpecification(set.indexedSetModeSpecification, extras)
		
		assert !set.owner
		assert !set.vsamIndex
		assert set.systemOwner
		assert set.systemOwner.areaSpecification
		assert set.systemOwner.areaSpecification.area
		if (extras.systemOwnerArea) {
			assert set.systemOwner.areaSpecification.area.is(extras.systemOwnerArea)
		} else {
			assert set.systemOwner.areaSpecification.area.name == extras.systemOwnerAreaName ?: "${set.name}-AREA"
		}
		if (extras.systemOwnerSymbolicSubareaName) {
			assert set.systemOwner.areaSpecification.symbolicSubareaName == extras.systemOwnerSymbolicSubareaName
		} else {
			assert !set.systemOwner.areaSpecification.symbolicSubareaName
		}
		assert set.schema.areas
		if (extras.systemOwnerArea) {
			assert set.schema.areas.find { 
				SchemaArea area -> area.is(extras.systemOwnerArea) 
			}
		} else {
			assert set.schema.areas.find { 
				SchemaArea area -> area.name == extras.systemOwnerAreaName ?: "${set.name}-AREA" 
			}
		}
		if (extras.systemOwnerOffsetExpressionOffsetPageCount ||
			extras.systemOwnerOffsetExpressionOffsetPercent ||
			extras.systemOwnerOffsetExpressionPageCount ||
			extras.systemOwnerOffsetExpressionPercent) {
			
			assert set.systemOwner.areaSpecification.offsetExpression
			if (extras.systemOwnerOffsetExpressionOffsetPageCount) {
				assert set.systemOwner.areaSpecification.offsetExpression.offsetPageCount == extras.systemOwnerOffsetExpressionOffsetPageCount
			} else {
				assert !set.systemOwner.areaSpecification.offsetExpression.offsetPageCount
			}
			if (extras.systemOwnerOffsetExpressionOffsetPercent) {
				assert set.systemOwner.areaSpecification.offsetExpression.offsetPercent == extras.systemOwnerOffsetExpressionOffsetPercent
			} else {
				assert !set.systemOwner.areaSpecification.offsetExpression.offsetPercent
			}
			if (extras.systemOwnerOffsetExpressionPageCount) {
				assert set.systemOwner.areaSpecification.offsetExpression.pageCount == extras.systemOwnerOffsetExpressionPageCount
			} else {
				assert !set.systemOwner.areaSpecification.offsetExpression.pageCount
			}
			if (extras.systemOwnerOffsetExpressionPercent) {
				assert set.systemOwner.areaSpecification.offsetExpression.percent == extras.systemOwnerOffsetExpressionPercent
			} else {
				assert !set.systemOwner.areaSpecification.offsetExpression.percent
			}
		} else {
			assert !set.systemOwner.areaSpecification.offsetExpression
		}
		assert set.systemOwner.diagramLocation
		if (extras.systemOwnerDiagramDataX) {
			assert set.systemOwner.diagramLocation.x == extras.systemOwnerDiagramDataX
		} else {
			assert set.systemOwner.diagramLocation.x == 0
		}
		if (extras.systemOwnerDiagramDataY) {
			assert set.systemOwner.diagramLocation.y == extras.systemOwnerDiagramDataY
		} else {
			assert set.systemOwner.diagramLocation.y == 0
		}
		assert set.schema.diagramData
		assert set.schema.diagramData.locations
		assert set.schema.diagramData.locations.find {
			DiagramLocation diagramLocation -> diagramLocation == set.systemOwner.diagramLocation 
		}
			
		assert set.members
		assert set.members.size() == 1
		assertMember(set.members[0], extras)
	}
									  
	void assertVsamIndex(Set set, String expectedName) {
		assertVsamIndex(set, expectedName, [:])
	}
									 
	void assertVsamIndex(Set set, String expectedName, Schema schema) {
		assertVsamIndex(set, expectedName, schema, [:])
	}
									 
	void assertVsamIndex(Set set, String expectedName, Map<String, ?> extras) {
		assert set
		assert set.schema
		assert set.schema.name == TEMP_SCHEMA_NAME
		assert set.schema.version == TEMP_SCHEMA_VERSION
		assertVsamIndex(set, expectedName, set.schema, extras)
	}
																							
	void assertVsamIndex(Set set, String expectedName, Schema schema, Map<String, ?> extras) {
  
		String expectedMemberRecordName =
		extras.memberRecordName ? extras.memberRecordName : "$expectedName-M"
					 
		assert set
		assert set.schema
		assert set.schema == schema
		assertBasicSchema(set.schema, schema.name, schema.version)
		assert set.schema.procedures.empty
  
		assert set.name == expectedName
		assert set.mode == SetMode.VSAM_INDEX
		assert set.order == SetOrder.SORTED
	  
		assert !set.indexedSetModeSpecification  
		assert !set.owner
		assert !set.systemOwner
		assert set.vsamIndex
		assert set.vsamIndex.diagramLocation
		if (extras.vsamIndexDiagramDataX) {
			assert set.vsamIndex.diagramLocation.x == extras.vsamIndexDiagramDataX
		} else {
			assert set.vsamIndex.diagramLocation.x == 0
		}
		if (extras.vsamIndexDiagramDataY) {
			assert set.vsamIndex.diagramLocation.y == extras.vsamIndexDiagramDataY
		} else {
			assert set.vsamIndex.diagramLocation.y == 0
		}
		assert set.schema.diagramData
		assert set.schema.diagramData.locations
		assert set.schema.diagramData.locations.find {
			DiagramLocation diagramLocation -> diagramLocation == set.vsamIndex.diagramLocation }
	  
		assert set.members
		assert set.members.size() == 1
		assert !extras.membershipOption || 
			   extras.membershipOption == SetMembershipOption.MANDATORY_AUTOMATIC
		assertMember(set.members[0], extras)
	}
	
	def "build the simpliest set, NOT referencing a given schema, with no build arguments"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		Set set = builder.build()
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema"
		assertChainedSet(set, 'SET1')
	}
	
	def "build the simpliest set, NOT referencing a given schema, from an empty closure"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		Set set = builder.build( { } )
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema"
		assertChainedSet(set, 'SET1')
	}

	def "build the simpliest set, NOT referencing a given schema, from only a name"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		Set set = builder.build('DEPT-EMPLOYEE')
		
		then: "the result will be a set with its name property set to the given set name"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema"		
		assertChainedSet(set, 'DEPT-EMPLOYEE')
	}
	
	def "build the simpliest set, NOT referencing a given schema, from a name and an empty closure"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		Set set = builder.build( 'DEPT-EMPLOYEE', { } )
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema"
		assertChainedSet(set, 'DEPT-EMPLOYEE')
	}
	
	def "build the simpliest set, NOT referencing a given schema, a closure specifying only a name"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			name 'DEPT-EMPLOYEE'
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema"
		assertChainedSet(set, 'DEPT-EMPLOYEE')
	}
	
	def "build the simpliest set, referencing a given schema, with no build arguments"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
		})
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		Set set = builder.build()
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema"
		assertChainedSet(set, 'SET1', schema)
	}
	
	def "build the simpliest set, referencing a given schema, from an empty closure"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
		})
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		Set set = builder.build( { } )
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema"
		assertChainedSet(set, 'SET1', schema)
	}

	def "build the simpliest set, referencing a given schema, from only a name"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
		})
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		Set set = builder.build('DEPT-EMPLOYEE')
		
		then: "the result will be a set with its name property set to the given set name"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema"
		assertChainedSet(set, 'DEPT-EMPLOYEE', schema)
	}
	
	def "build the simpliest set, referencing a given schema, from a name and an empty closure"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
		})
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		Set set = builder.build( 'DEPT-EMPLOYEE', { } )
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema"
		assertChainedSet(set, 'DEPT-EMPLOYEE', schema)
	}
	
	def "build the simpliest set, referencing a given schema, a closure specifying only a name"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
		})
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			name 'DEPT-EMPLOYEE'
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema"
		assertChainedSet(set, 'DEPT-EMPLOYEE', schema)
	}
	
	def "build a set, NOT referencing a given schema, from a closure that directly specifies the owner record name"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			owner 'DEPARTMENT'
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "owner record will have the name specified"
		assertChainedSet(set, 'SET1', [ ownerRecordName : 'DEPARTMENT'] )
	}
	
	def "build a set, NOT referencing a given schema, from a closure that specifies the owner record name in a closure"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			owner { 
				name 'DEPARTMENT'
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "owner record will have the name specified"
		assertChainedSet(set, 'SET1', [ ownerRecordName : 'DEPARTMENT'] )
	}
	
	def "build a set, NOT referencing a given schema, from a closure that directly specifies the owner record name (and an empty owner closure)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			owner 'DEPARTMENT' {
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "owner record will have the name specified"
		assertChainedSet(set, 'SET1', [ ownerRecordName : 'DEPARTMENT'] )
	}
	
	def "build a set, referencing a given schema, from a closure that directly specifies the (undefined) owner record name"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
		})
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			owner 'DEPARTMENT'
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "owner record will have the name specified"
		assertChainedSet(set, 'SET1', schema, [ ownerRecordName : 'DEPARTMENT'] )
	}
	
	def "build a set, referencing a given schema, from a closure that specifies the (undefined) owner record name in a closure"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
		})
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			owner {
				name 'DEPARTMENT'
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "owner record will have the name specified"
		assertChainedSet(set, 'SET1', schema, [ ownerRecordName : 'DEPARTMENT'] )
	}
	
	def "build a set, referencing a given schema, from a closure that directly specifies the (undefined) owner record name (and an empty owner closure)"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
		})
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			owner 'DEPARTMENT' {
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "owner record will have the name specified"
		assertChainedSet(set, 'SET1', schema, [ ownerRecordName : 'DEPARTMENT'] )
	}
	
	def "build a set, referencing a given schema, from a closure that directly specifies an existing owner record name"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100			
			record 'DEPARTMENT'
		})
		SchemaRecord department = schema.getRecord('DEPARTMENT')
		assert department
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			owner 'DEPARTMENT'
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose owner refers to the given record"
		assertChainedSet(set, 'SET1', schema, [ ownerRecord : department ] )
	}
	
	def "build a set, referencing a given schema, from a closure that specifies an existing owner record name in a closure"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
			record 'DEPARTMENT'
		})
		SchemaRecord department = schema.getRecord('DEPARTMENT')
		assert department
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			owner {
				name 'DEPARTMENT'
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose owner refers to the given record"
		assertChainedSet(set, 'SET1', schema, [ ownerRecord : department ] )
	}
	
	def "build a set, referencing a given schema, from a closure that directly specifies the an existing owner record name (and an empty owner closure)"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
			record 'DEPARTMENT'
		})
		SchemaRecord department = schema.getRecord('DEPARTMENT')
		assert department
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			owner 'DEPARTMENT' {
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose owner refers to the given record"
		assertChainedSet(set, 'SET1', schema, [ ownerRecord : department ] )
	}
	
	def "build a set, NOT referencing a given schema, from a closure that directly specifies the (undefined) member record name"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'EMPLOYEE'
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "member record will have the name specified"
		assertChainedSet(set, 'SET1', [ memberRecordName : 'EMPLOYEE' ] )
	}
	
	def "build a set, NOT referencing a given schema, from a closure that specifies the member record name in a closure"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member {
				name 'EMPLOYEE'
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "member record will have the name specified"
		assertChainedSet(set, 'SET1', [ memberRecordName : 'EMPLOYEE' ] )
	}
	
	def "build a set, NOT referencing a given schema, from a closure that directly specifies the member record name (and an empty member closure)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'EMPLOYEE' {
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "member record will have the name specified"
		assertChainedSet(set, 'SET1', [ memberRecordName : 'EMPLOYEE' ] )
	}
	
	def "build a set, referencing a given schema, from a closure that directly specifies the (undefined) member record name"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
		})
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			member 'EMPLOYEE'
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "member record will have the name specified"
		assertChainedSet(set, 'SET1', schema, [ memberRecordName : 'EMPLOYEE' ] )
	}
	
	def "build a set, referencing a given schema, from a closure that specifies the not existing member record name in a closure"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
		})
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			member {
				name 'EMPLOYEE'
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "member record will have the name specified"
		assertChainedSet(set, 'SET1', schema, [ memberRecordName : 'EMPLOYEE' ] )
	}
	
	def "build a set, referencing a given schema, from a closure that directly specifies the not existing member record name (and an empty member closure)"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
		})
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			member 'EMPLOYEE' {
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with its name property set to a generated value"
			  "and with a number of default characteristics; it will belong to a temporary schema"
			  "and its owner and member records will also be temporary records belonging to the same"
			  "schema and each be contained in a temporary area in - again - the same schema - the"
			  "member record will have the name specified"
		assertChainedSet(set, 'SET1', schema, [ memberRecordName : 'EMPLOYEE' ] )
	}
	
	def "build a set, referencing a given schema, from a closure that directly specifies an existing member record name"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
			record 'EMPLOYEE'
		})
		SchemaRecord employee = schema.getRecord('EMPLOYEE')
		assert employee
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			member 'EMPLOYEE'
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member refers to the given record"
		assertChainedSet(set, 'SET1', schema, [ memberRecord : employee ] )
	}
	
	def "build a set, referencing a given schema, from a closure that specifies the existing member record name in a closure"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
			record 'EMPLOYEE'
		})
		SchemaRecord employee = schema.getRecord('EMPLOYEE')
		assert employee
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			member {
				name 'EMPLOYEE'
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member refers to the given record"
		assertChainedSet(set, 'SET1', schema, [ memberRecord : employee ] )
	}
	
	def "build a set, referencing a given schema, from a closure that directly specifies the existing member record name (and an empty member closure)"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
			record 'EMPLOYEE'
		})
		SchemaRecord employee = schema.getRecord('EMPLOYEE')
		assert employee
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			member 'EMPLOYEE' {
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member refers to the given record"
		assertChainedSet(set, 'SET1', schema, [ memberRecord : employee ] )
	}
	
	def "owner prior pointer specification with automatic owner next pointer assignment"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			owner 'EMPLOYEE' {
				prior 1
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the given owner prior pointer position; the owner next"
		      "pointer position is automatically assigned"
		assertChainedSet(set, 'SET1', [ ownerRecordName : 'EMPLOYEE', 
										ownerNextDbkeyPosition : 2, 
										ownerPriorDbkeyPosition : 1 ] )
	}
	
	def "owner next and prior pointer specification"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			owner 'EMPLOYEE' {
				next 5 
				prior 6
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the given owner pointer positions"
		assertChainedSet(set, 'SET1', [ ownerRecordName : 'EMPLOYEE', 
										ownerNextDbkeyPosition : 5, 
										ownerPriorDbkeyPosition : 6 ] )
	}
	
	def "member owner pointer specification with automatic next pointer assignment"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'EMPLOYEE' {
				owner 1
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the given member pointer positions"
		assertChainedSet(set, 'SET1', [ memberRecordName : 'EMPLOYEE', 
										memberNextDbkeyPosition : 2, 
										memberOwnerDbkeyPosition : 1 ] )
	}
	
	def "member prior and owner pointer specification with automatic next pointer assignment"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'EMPLOYEE' {
				prior 1
				owner 2
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the given member pointer positions"
		assertChainedSet(set, 'SET1', [ memberRecordName : 'EMPLOYEE', 
										memberNextDbkeyPosition : 3, 
										memberPriorDbkeyPosition : 1, 
										memberOwnerDbkeyPosition : 2 ] )
	}
	
	def "member next, prior and owner pointer specification"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'EMPLOYEE' {
				next 5
				prior 6
				owner 7
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the given member pointer positions"
		assertChainedSet(set, 'SET1', [ memberRecordName : 'EMPLOYEE', 
										memberNextDbkeyPosition : 5, 
										memberPriorDbkeyPosition : 6, 
										memberOwnerDbkeyPosition : 7 ] )
	}
	
	def "member index pointer specification"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			index { }
			systemOwner
			member 'EMPLOYEE' {
				index 4
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the given member pointer position(s)"
		assertSystemOwnedIndexedSet(set, 'SET1', [ memberRecordName : 'EMPLOYEE',
												   memberIndexDbkeyPosition : 4 ] )
	}
	
	def "member index and owner pointer specification"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			index { }
			systemOwner
			member 'EMPLOYEE' {
				index 4
				owner 5
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the given member pointer position(s)"
		assertSystemOwnedIndexedSet(set, 'SET1', [ memberRecordName : 'EMPLOYEE',
												   memberIndexDbkeyPosition : 4,
												   memberOwnerDbkeyPosition : 5 ] )
	}
	
	@Unroll
	def "set membership option specification (chained set)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'EMPLOYEE' {
				membership setMembershipOption
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the given member pointer positions"
		assertChainedSet(set, 'SET1', [ memberRecordName : 'EMPLOYEE', 
										membershipOption : expectedSetMembershipOption ] )
		
		where:
		setMembershipOption   | expectedSetMembershipOption
		'MANDATORY AUTOMATIC' | SetMembershipOption.MANDATORY_AUTOMATIC
		'MANDATORY MANUAL' 	  | SetMembershipOption.MANDATORY_MANUAL
		'OPTIONAL AUTOMATIC'  | SetMembershipOption.OPTIONAL_AUTOMATIC
		'OPTIONAL MANUAL' 	  | SetMembershipOption.OPTIONAL_MANUAL
	}
	
	@Unroll
	def "set membership option specification (indexed set)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			index {
				keys 13
			}
			member 'EMPLOYEE' {
				membership setMembershipOption
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the given member pointer positions"
		assertUserOwnedIndexedSet(set, 'SET1', [ memberRecordName : 'EMPLOYEE', 
												 membershipOption : expectedSetMembershipOption, 
												 indexKeyCount : 13] )
		
		where:
		setMembershipOption   | expectedSetMembershipOption
		'MANDATORY AUTOMATIC' | SetMembershipOption.MANDATORY_AUTOMATIC
		'MANDATORY MANUAL' 	  | SetMembershipOption.MANDATORY_MANUAL
		'OPTIONAL AUTOMATIC'  | SetMembershipOption.OPTIONAL_AUTOMATIC
		'OPTIONAL MANUAL' 	  | SetMembershipOption.OPTIONAL_MANUAL
	}
	
	@Unroll
	def "set order specification (chained set)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			order setOrder
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the given member pointer positions"
		set
		assertChainedSet(set, 'SET1', [ setOrder : expectedSetOrder ] )
		
		where:
		setOrder | expectedSetOrder
		'FIRST'  | SetOrder.FIRST
		'LAST' 	 | SetOrder.LAST
		'NEXT' 	 | SetOrder.NEXT
		'PRIOR'  | SetOrder.PRIOR
		'SORTED' | SetOrder.SORTED
	}
	
	@Unroll
	def "set order specification (indexed set)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			index {
				keys 12
			}
			order setOrder
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the given member pointer positions"
		set
		assertUserOwnedIndexedSet(set, 'SET1', [ setOrder : expectedSetOrder, indexKeyCount: 12 ] )
		
		where:
		setOrder | expectedSetOrder
		'FIRST'  | SetOrder.FIRST
		'LAST' 	 | SetOrder.LAST
		'NEXT' 	 | SetOrder.NEXT
		'PRIOR'  | SetOrder.PRIOR
		'SORTED' | SetOrder.SORTED
	}
	
	def "symbolic index specification (user owned set)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			index 'SYMBOL1'
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the appropriate indexed set mode specification"
		assertUserOwnedIndexedSet(set, 'SET1', [ symbolicIndexName : 'SYMBOL1'])		
	}
	
	def "key count specification (user owned indexed set)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			index {
				keys 13
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the appropriate indexed set mode specification"
		assertUserOwnedIndexedSet(set, 'SET1', [ indexKeyCount : 13])
	}
	
	def "displacement specification (user owned indexed set)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			index {
				displacement 17
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the appropriate indexed set mode specification"
		assertUserOwnedIndexedSet(set, 'SET1', [ indexDisplacementPageCount : 17])
	}
	
	def "system owner (no area specification; no closure)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner will reside in an"
		      "automatically generated area"
		assertSystemOwnedIndexedSet(set, 'SET1')
	}
	
	def "system owner (no area specification; empty closure)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner { }
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner will reside in an"
			  "automatically generated area"
		assertSystemOwnedIndexedSet(set, 'SET1')
	}
	
	def "system owner (area specification with only a not existing area name)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner { 
				area 'TEST-INDEX-AREA'
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner will reside in an"
			  "automatically generated area (with the given name)"
		assertSystemOwnedIndexedSet(set, 'SET1', [ systemOwnerAreaName : 'TEST-INDEX-AREA' ] )
	}
	
	def "system owner (area specification without an area name but with a symbolic subarea name)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner {
				area {
					subarea 'SUBAREA1'
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner will reside in an"
			  "automatically generated area and the symbolic subarea name will be set"
		assertSystemOwnedIndexedSet(set, 'SET1', [ systemOwnerSymbolicSubareaName : 'SUBAREA1' ] )
	}
	
	def "system owner (area specification with a not existing area name and a symbolic subarea name)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner {
				area 'TEST-INDEX-AREA' {
					subarea 'SUBAREA1'
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner will reside in an"
			  "automatically generated area (with the given name) and the symbolic subarea name" 
			  "will be set"
		assertSystemOwnedIndexedSet(set, 'SET1', [ systemOwnerAreaName : 'TEST-INDEX-AREA', 
												   systemOwnerSymbolicSubareaName : 'SUBAREA1' ] )
	}
	
	def "system owner (area specification without an area name but with an offset expression expressed in pages)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner {
				area {
					offsetPages 12 
					pages 5000
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner will reside in an"
			  "automatically generated area and an appropriate offset expression"
		assertSystemOwnedIndexedSet(set, 'SET1', [ systemOwnerOffsetExpressionOffsetPageCount : 12, 
												   systemOwnerOffsetExpressionPageCount : 5000 ] )
	}
	
	def "system owner (area specification with a not existing area name and an offset expression expressed in pages)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner {
				area 'TEST-INDEX-AREA' {
					offsetPages 15 
					pages 7500
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner will reside in an"
			  "automatically generated area (with the given name) and an appropriate offset"
			  "expression"
		assertSystemOwnedIndexedSet(set, 'SET1', [ systemOwnerAreaName : 'TEST-INDEX-AREA', 
												   systemOwnerOffsetExpressionOffsetPageCount : 15, 
												   systemOwnerOffsetExpressionPageCount : 7500 ] )
	}
	
	def "system owner (area specification without an area name but with an offset expression expressed in percents)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner {
				area {
					offsetPercent 5
					percent 50
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner will reside in an"
			  "automatically generated area and an appropriate offset expression"
		assertSystemOwnedIndexedSet(set, 'SET1', [ systemOwnerOffsetExpressionOffsetPercent : 5,
												   systemOwnerOffsetExpressionPercent : 50 ] )
	}
	
	def "system owner (area specification with a not existing area name and an offset expression expressed in percents)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner {
				area 'TEST-INDEX-AREA' {
					offsetPercent 10
					percent 15
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner will reside in an"
			  "automatically generated area (with the given name) and an appropriate offset"
			  "expression"
		assertSystemOwnedIndexedSet(set, 'SET1', [ systemOwnerAreaName : 'TEST-INDEX-AREA',
												   systemOwnerOffsetExpressionOffsetPercent : 10,
												   systemOwnerOffsetExpressionPercent : 15 ] )
	}
	
	def "system owner (area specification without an area name but with an offset expression expressed in both pages and a percent)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner {
				area {
					offsetPercent 7
					pages 1500
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner will reside in an"
			  "automatically generated area and an appropriate offset expression"
		assertSystemOwnedIndexedSet(set, 'SET1', [ systemOwnerOffsetExpressionOffsetPercent : 7,
												   systemOwnerOffsetExpressionPageCount : 1500 ] )
	}
	
	def "system owner (area specification with a not existing area name and an offset expression expressed in both pages and a percent)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner {
				area 'TEST-INDEX-AREA' {
					offsetPages 100
					percent 25
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner will reside in an"
			  "automatically generated area (with the given name) and an appropriate offset"
			  "expression"
		assertSystemOwnedIndexedSet(set, 'SET1', [ systemOwnerAreaName : 'TEST-INDEX-AREA',
												   systemOwnerOffsetExpressionOffsetPageCount : 100,
												   systemOwnerOffsetExpressionPercent : 25 ] )
	}
	
	def "system owner (diagram data)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			systemOwner {
				diagram {
					x '-15'
					y '-25'
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner's diagram location" 
			  "will correspond with the definition within the closure"
		assertSystemOwnedIndexedSet(set, 'SET1', [ systemOwnerDiagramDataX : -15,
												   systemOwnerDiagramDataY : -25 ] )
	}
	
	def "system owner (existing area)"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
			area 'EMP-DEMO-REGION'
		})
		SchemaArea empDemoRegion = schema.getArea('EMP-DEMO-REGION')
		assert empDemoRegion
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			systemOwner {
				area 'EMP-DEMO-REGION' {
					offsetPages 100
					percent 25
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner's area will refer to"
			  "the given area"
		assertSystemOwnedIndexedSet(set, 'SET1', schema, 
									[ systemOwnerArea : empDemoRegion,
									  systemOwnerOffsetExpressionOffsetPageCount : 100,
									  systemOwnerOffsetExpressionPercent : 25 ] )
	}

	def "VSAM index (default diagram location)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			vsamIndex
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner's diagram location"
			  "will correspond with the definition within the closure"
		assertVsamIndex(set, 'SET1')
	}
	
	def "VSAM index (specific diagram location)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			vsamIndex {
				x 30
				y 40
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a system-owned indexed set; the system owner's diagram location"
			  "will correspond with the definition within the closure"
		assertVsamIndex(set, 'SET1', [ vsamIndexDiagramDataX : 30,
									   vsamIndexDiagramDataY : 40 ] )
	}
	
	def "sort key (without a member and key definition)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			order 'SORTED'
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member will refer to the given sort key"
		assertChainedSet(set, 'SET1', [ setOrder : SetOrder.SORTED ] )
	}
	
	def "sort key (without a key definition)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			order 'SORTED'
			member 'SET1-M'
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member will refer to the given sort key"
		assertChainedSet(set, 'SET1', [ setOrder : SetOrder.SORTED ] )
	}
	
	def "sort key (no closure)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			order 'SORTED'
			member {
				key
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member will refer to the given sort key"
		assertChainedSet(set, 'SET1', [ setOrder : SetOrder.SORTED ] )
	}
	
	def "sort key (empty closure)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			order 'SORTED'
			member {
				key {
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member will refer to the given sort key"
		assertChainedSet(set, 'SET1', [ setOrder : SetOrder.SORTED ] )
	}
	
	def "sort key with only defaults"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			order 'SORTED'
			member {
				key {
					ascending 'ELEMENT1'
					duplicates 'NOT_ALLOWED'
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member will refer to the given sort key"
		assertChainedSet(set, 'SET1', [ setOrder : SetOrder.SORTED ] )
	}
	
	def "sort key: descending sort sequence"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			order 'SORTED'
			member {
				key {
					descending 'ELEMENT1'
					duplicates 'NOT_ALLOWED'
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member will refer to the given sort key"
		assertChainedSet(set, 'SET1', [ setOrder : SetOrder.SORTED, 
										sortSequence : SortSequence.DESCENDING ] )
	}
	
	@Unroll
	def "sort key: duplicate option"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			order 'SORTED'
			member {
				key {
					ascending 'ELEMENT1'
					duplicates duplicatesOption
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member will refer to the given sort key"
		assertChainedSet(set, 'SET1', [ setOrder : SetOrder.SORTED, 
										duplicatesOption : expectedDuplicatesOption ] )
		
		where:
		duplicatesOption | expectedDuplicatesOption
		'BY DBKEY'	 	 | DuplicatesOption.BY_DBKEY
		'FIRST'	 		 | DuplicatesOption.FIRST
		'LAST'	 		 | DuplicatesOption.LAST
		'NOT ALLOWED'	 | DuplicatesOption.NOT_ALLOWED
		'UNORDERED'	 	 | DuplicatesOption.UNORDERED
	}
	
	def "sort key: natural sequence option"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			order 'SORTED'
			member {
				key {
					naturalSequence
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member will refer to the given sort key"
		assertChainedSet(set, 'SET1', [ setOrder : SetOrder.SORTED,
										naturalSequence : true ] )
	}
	
	def "sort key: compressed option"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			order 'SORTED'
			member {
				key {
					compressed
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member will refer to the given sort key"
		assertChainedSet(set, 'SET1', [ setOrder : SetOrder.SORTED,
										compressed : true ] )		
	}
	
	def "sort key: multiple elements with varying sort sequences"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
			record 'EMPLOYEE' {
				elements {
					element 'EMP-ID-0415' { 
						level 2
						picture '9(4)' 
					}
					element 'EMP-NAME-0415' {
						level 2
						children {
							element 'EMP-FIRST-NAME-0415' {
								level 3	
								picture 'X(10)'
							}
							element 'EMP-LAST-NAME-0415' {
								level 3
								picture 'X(15)'
							}						
						}
					}
					element 'EMP-ADDRESS-0415' {
						level 2
						children {
							element 'EMP-STREET-0415' { 
								level 3
								picture 'X(20)'
							}
							element 'EMP-CITY-0415' { 
								level 3
								picture 'X(15)'
							}
							element 'EMP-STATE-0415' { 
								level 3
								picture 'X(2)'
							}
						}
					}
				}
			}
		})
		SchemaRecord employee = schema.getRecord('EMPLOYEE')
		assert employee
		assert employee.elements.size() == 8
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			order 'SORTED'
			member 'EMPLOYEE' {
				key {
					ascending 'EMP-FIRST-NAME-0415'
					descending 'EMP-CITY-0415'
					ascending 'EMP-ID-0415'
					duplicates 'FIRST'
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set whose member will refer to the given sort key"
		def sortKeyChecker = { Key sortKey, int index ->
			assert index == 0
			assert sortKey.elements.size() == 3
			assert sortKey.elements[0].element == employee.getElement('EMP-FIRST-NAME-0415')
			assert sortKey.elements[0].sortSequence == SortSequence.ASCENDING
			assert sortKey.elements[1].element == employee.getElement('EMP-CITY-0415')
			assert sortKey.elements[1].sortSequence == SortSequence.DESCENDING
			assert sortKey.elements[2].element == employee.getElement('EMP-ID-0415')
			assert sortKey.elements[2].sortSequence == SortSequence.ASCENDING
			assert sortKey.duplicatesOption == DuplicatesOption.FIRST
			assert !sortKey.compressed
			assert !sortKey.naturalSequence
		}
		assertChainedSet(set, 'SET1', schema, [ setOrder : SetOrder.SORTED,
												memberRecord : employee,
												sortKeyChecker : sortKeyChecker ] )
	}
	
	def "connection label specification"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member {
				diagram {
					label {
						x 12
						y 13
					}
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the member's connection label at the given location"
		assertChainedSet(set, 'SET1', [ connectionLabelX : 12,
										connectionLabelY : 13 ] )
	}
	
	def "source endpoint specification"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'XYZ' {
				diagram {
					line {
						start {
							x 51
							y 52
						}
					}
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the member's source endpoint at the given location"
		assertChainedSet(set, 'SET1', [ memberRecordName : 'XYZ', 
										sourceEndpointLocationX : 51,
										sourceEndpointLocationY : 52 ])
	}
	
	def "target endpoint specification (no connectors)s"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'XYZ' {
				diagram {
					line {
						end {
							x 61
							y 62
						}
					}
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the member's source endpoint at the given location"
		assertChainedSet(set, 'SET1', [ memberRecordName : 'XYZ',
										targetEndpointLocationX : 61,
										targetEndpointLocationY : 62 ])
	}
	
	def "bendpoint specifications on first and only connection part"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'XYZ' {
				diagram {
					line {
						start {
							x 41
							y 42
						}
						bendpoint {
							x 31
							y 32
						}
						bendpoint {
							x 51
							y 52
						}
						end {
							x 61
							y 62
						}
					}
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the member's bendpoints at the given locations; note"
		      "that source and target endpoints are always required when bendpoints are involved"
		def bendpointChecker = { List<ConnectionPart> connectionParts ->
			assert connectionParts.size() == 1
			assert connectionParts[0].bendpointLocations.size() == 2
			assert connectionParts[0].bendpointLocations[0].x == 31
			assert connectionParts[0].bendpointLocations[0].y == 32
			assert connectionParts[0].bendpointLocations[0].eyecatcher == 'bendpoint [0] set SET1 (XYZ)'
			assert connectionParts[0].bendpointLocations[1].x == 51
			assert connectionParts[0].bendpointLocations[1].y == 52
			assert connectionParts[0].bendpointLocations[1].eyecatcher == 'bendpoint [1] set SET1 (XYZ)'
		}
		assertChainedSet(set, 'SET1', [ memberRecordName : 'XYZ', 
										sourceEndpointLocationX : 41,
										sourceEndpointLocationY : 42,
										bendpointChecker : bendpointChecker,
										targetEndpointLocationX : 61,
										targetEndpointLocationY : 62 ] ) 
	}
	
	def "connectors: default definition, no label"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'XYZ' {
				diagram {
					line {
						connectors
					}
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the member's line having 2 connectors"
		def connectorChecker = { List<ConnectionPart> connectionParts ->
			assert connectionParts.size() == 2
			assert connectionParts[0].connector
			assert !connectionParts[0].connector.label
			assert connectionParts[0].connector.diagramLocation
			assert connectionParts[0].connector.diagramLocation.x == 0
			assert connectionParts[0].connector.diagramLocation.y == 0
			assert connectionParts[0].connector.diagramLocation.eyecatcher == 'set connector[0] SET1 (XYZ)'
			assert connectionParts[1].connector
			assert !connectionParts[1].connector.label
			assert connectionParts[1].connector.diagramLocation
			assert connectionParts[1].connector.diagramLocation.x == 0
			assert connectionParts[1].connector.diagramLocation.y == 0
			assert connectionParts[1].connector.diagramLocation.eyecatcher == 'set connector[1] SET1 (XYZ)'
		}
		assertChainedSet(set, 'SET1', [ memberRecordName : 'XYZ',
										connectorChecker : connectorChecker ] )
	}
	
	def "connectors: default definition with label"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'XYZ' {
				diagram {
					line {
						connectors 'X'
					}
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the member's line having 2 connectors"
		def connectorChecker = { List<ConnectionPart> connectionParts ->
			assert connectionParts.size() == 2
			assert connectionParts[0].connector
			assert connectionParts[0].connector.label == 'X'
			assert connectionParts[0].connector.diagramLocation
			assert connectionParts[0].connector.diagramLocation.x == 0
			assert connectionParts[0].connector.diagramLocation.y == 0
			assert connectionParts[0].connector.diagramLocation.eyecatcher == 'set connector[0] SET1 (XYZ)'
			assert connectionParts[1].connector
			assert connectionParts[1].connector.label == 'X'
			assert connectionParts[1].connector.diagramLocation
			assert connectionParts[1].connector.diagramLocation.x == 0
			assert connectionParts[1].connector.diagramLocation.y == 0
			assert connectionParts[1].connector.diagramLocation.eyecatcher == 'set connector[1] SET1 (XYZ)'
		}
		assertChainedSet(set, 'SET1', [ memberRecordName : 'XYZ',
										connectorChecker : connectorChecker ] )
	}
	
	def "connectors: explicit definition with only a label"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'XYZ' {
				diagram {
					line {
						connectors {
							label 'X'
						}
					}
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the member's line having 2 connectors"
		def connectorChecker = { List<ConnectionPart> connectionParts ->
			assert connectionParts.size() == 2
			assert connectionParts[0].connector
			assert connectionParts[0].connector.label == 'X'
			assert connectionParts[0].connector.diagramLocation
			assert connectionParts[0].connector.diagramLocation.x == 0
			assert connectionParts[0].connector.diagramLocation.y == 0
			assert connectionParts[0].connector.diagramLocation.eyecatcher == 'set connector[0] SET1 (XYZ)'
			assert connectionParts[1].connector
			assert connectionParts[1].connector.label == 'X'
			assert connectionParts[1].connector.diagramLocation
			assert connectionParts[1].connector.diagramLocation.x == 0
			assert connectionParts[1].connector.diagramLocation.y == 0
			assert connectionParts[1].connector.diagramLocation.eyecatcher == 'set connector[1] SET1 (XYZ)'
		}
		assertChainedSet(set, 'SET1', [ memberRecordName : 'XYZ',
										connectorChecker : connectorChecker ] )
	}
	
	def "connectors: explicit definition of the 2 connectors"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'XYZ' {
				diagram {
					line {
						connectors {
							label 'A'
							connector {
								x 71
								y 72
							}
							connector {
								x 81
								y 82
							}
						}
					}
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the member's line having 2 connectors"
		def connectorChecker = { List<ConnectionPart> connectionParts ->
			assert connectionParts.size() == 2
			assert connectionParts[0].connector
			assert connectionParts[0].connector.label == 'A'
			assert connectionParts[0].connector.diagramLocation
			assert connectionParts[0].connector.diagramLocation.x == 71
			assert connectionParts[0].connector.diagramLocation.y == 72
			assert connectionParts[0].connector.diagramLocation.eyecatcher == 'set connector[0] SET1 (XYZ)'
			assert connectionParts[1].connector
			assert connectionParts[1].connector.label == 'A'
			assert connectionParts[1].connector.diagramLocation
			assert connectionParts[1].connector.diagramLocation.x == 81
			assert connectionParts[1].connector.diagramLocation.y == 82
			assert connectionParts[1].connector.diagramLocation.eyecatcher == 'set connector[1] SET1 (XYZ)'
		}
		assertChainedSet(set, 'SET1', [ memberRecordName : 'XYZ',
										connectorChecker : connectorChecker ] )
	}
	
	def "connectors: explicit definition of only the first connector"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'XYZ' {
				diagram {
					line {
						connectors {
							label 'A'
							connector {
								x 71
								y 72
							}
						}
					}
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the member's line having 2 connectors"
		def connectorChecker = { List<ConnectionPart> connectionParts ->
			assert connectionParts.size() == 2
			assert connectionParts[0].connector
			assert connectionParts[0].connector.label == 'A'
			assert connectionParts[0].connector.diagramLocation
			assert connectionParts[0].connector.diagramLocation.x == 71
			assert connectionParts[0].connector.diagramLocation.y == 72
			assert connectionParts[0].connector.diagramLocation.eyecatcher == 'set connector[0] SET1 (XYZ)'
			assert connectionParts[1].connector
			assert connectionParts[1].connector.label == 'A'
			assert connectionParts[1].connector.diagramLocation
			assert connectionParts[1].connector.diagramLocation.x == 0
			assert connectionParts[1].connector.diagramLocation.y == 0
			assert connectionParts[1].connector.diagramLocation.eyecatcher == 'set connector[1] SET1 (XYZ)'
		}
		assertChainedSet(set, 'SET1', [ memberRecordName : 'XYZ',
										connectorChecker : connectorChecker ] )
	}
	
	def "bendpoint specifications on both connection parts"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'XYZ' {
				diagram {
					line {
						start {
							x 41
							y 42
						}
						bendpoint {
							x 31
							y 32
						}
						connectors
						bendpoint {
							x 51
							y 52
						}
						bendpoint {
							x 91
							y 92
						}
						end {
							x 61
							y 62
						}
					}
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the result will be a set with the member's bendpoints at the given locations; note"
			  "that source and target endpoints are always required when bendpoints are involved"
		def bendpointChecker = { List<ConnectionPart> connectionParts ->
			assert connectionParts.size() == 2
			assert connectionParts[0].bendpointLocations.size() == 1
			assert connectionParts[1].bendpointLocations.size() == 2
			assert connectionParts[0].bendpointLocations[0].x == 31
			assert connectionParts[0].bendpointLocations[0].y == 32
			assert connectionParts[0].bendpointLocations[0].eyecatcher == 'bendpoint [0] set SET1 (XYZ)'
			assert connectionParts[1].bendpointLocations[0].x == 51
			assert connectionParts[1].bendpointLocations[0].y == 52
			assert connectionParts[1].bendpointLocations[0].eyecatcher == 'bendpoint [1] set SET1 (XYZ)'
			assert connectionParts[1].bendpointLocations[1].x == 91
			assert connectionParts[1].bendpointLocations[1].y == 92
			assert connectionParts[1].bendpointLocations[1].eyecatcher == 'bendpoint [2] set SET1 (XYZ)'
		}
		def connectorChecker = { List<ConnectionPart> connectionParts ->
			assert connectionParts.size() == 2
			assert connectionParts[0].connector
			assert connectionParts[1].connector
		}
		assertChainedSet(set, 'SET1', [ memberRecordName : 'XYZ',
										sourceEndpointLocationX : 41,
										sourceEndpointLocationY : 42,
										bendpointChecker : bendpointChecker,
										connectorChecker : connectorChecker,
										targetEndpointLocationX : 61,
										targetEndpointLocationY : 62 ] )
	}
	
	def "unsorted multiple member set (automatically generated member records- 1st style)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = { 
			member 'SET1-M-1'
			member 'SET1-M-2'
			member 'SET1-M-3'
		}
		Set set = builder.build(definition)
		
		then: "the set will have multiple member records"
		def memberChecker = { List<MemberRole> memberRoles ->
			
			assert memberRoles.size() == 3
			
			assert memberRoles[0].record.name == 'SET1-M-1'
			assert memberRoles[0].nextDbkeyPosition == 1
			assert !memberRoles[0].priorDbkeyPosition
			assert !memberRoles[0].ownerDbkeyPosition
			assert !memberRoles[0].indexDbkeyPosition
			
			assert memberRoles[1].record.name == 'SET1-M-2'
			assert memberRoles[1].nextDbkeyPosition == 1
			assert !memberRoles[1].priorDbkeyPosition
			assert !memberRoles[1].ownerDbkeyPosition
			assert !memberRoles[1].indexDbkeyPosition
			
			assert memberRoles[2].record.name == 'SET1-M-3'
			assert memberRoles[2].nextDbkeyPosition == 1
			assert !memberRoles[2].priorDbkeyPosition
			assert !memberRoles[2].ownerDbkeyPosition
			assert !memberRoles[2].indexDbkeyPosition			
		}
		assertMultipleMemberSet(set, 'SET1', [ memberChecker : memberChecker ] )
	}
	
	def "unsorted multiple member set (automatically generated member records - 2nd style)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'SET1-M-1' { }
			member 'SET1-M-2' { }
			member 'SET1-M-3' { }
		}
		Set set = builder.build(definition)
		
		then: "the set will have multiple member records"
		def memberChecker = { List<MemberRole> memberRoles ->
			
			assert memberRoles.size() == 3
			
			assert memberRoles[0].record.name == 'SET1-M-1'
			assert memberRoles[0].nextDbkeyPosition == 1
			assert !memberRoles[0].priorDbkeyPosition
			assert !memberRoles[0].ownerDbkeyPosition
			assert !memberRoles[0].indexDbkeyPosition
			
			assert memberRoles[1].record.name == 'SET1-M-2'
			assert memberRoles[1].nextDbkeyPosition == 1
			assert !memberRoles[1].priorDbkeyPosition
			assert !memberRoles[1].ownerDbkeyPosition
			assert !memberRoles[1].indexDbkeyPosition
			
			assert memberRoles[2].record.name == 'SET1-M-3'
			assert memberRoles[2].nextDbkeyPosition == 1
			assert !memberRoles[2].priorDbkeyPosition
			assert !memberRoles[2].ownerDbkeyPosition
			assert !memberRoles[2].indexDbkeyPosition
			
		}
		assertMultipleMemberSet(set, 'SET1', [ memberChecker : memberChecker ] )
	}
	
	def "unsorted multiple member set (automatically generated member records - 3rd style)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member { name 'MBR1' }
			member { name 'MBR2' }
			member { name 'MBR3' }
		}
		Set set = builder.build(definition)
		
		then: "the set will have multiple member records"
		def memberChecker = { List<MemberRole> memberRoles ->
			
			assert memberRoles.size() == 3
			
			assert memberRoles[0].record.name == 'MBR1'
			assert memberRoles[0].nextDbkeyPosition == 1
			assert !memberRoles[0].priorDbkeyPosition
			assert !memberRoles[0].ownerDbkeyPosition
			assert !memberRoles[0].indexDbkeyPosition
			
			assert memberRoles[1].record.name == 'MBR2'
			assert memberRoles[1].nextDbkeyPosition == 1
			assert !memberRoles[1].priorDbkeyPosition
			assert !memberRoles[1].ownerDbkeyPosition
			assert !memberRoles[1].indexDbkeyPosition
			
			assert memberRoles[2].record.name == 'MBR3'
			assert memberRoles[2].nextDbkeyPosition == 1
			assert !memberRoles[2].priorDbkeyPosition
			assert !memberRoles[2].ownerDbkeyPosition
			assert !memberRoles[2].indexDbkeyPosition
		}
		assertMultipleMemberSet(set, 'SET1', [ memberChecker : memberChecker ] )
	}
	
	def "sorted multiple member set (automatically generated records and/or keys - mixed style)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			order 'SORTED'
			member 'SET1-M-1'
			member { 
				name 'SET1-M-2' 
			}
			member 'SET1-M-3' {
				key
			}
			member 'SET1-M-4' {
				key {
					ascending 'ELEMENT1'
					duplicates 'FIRST'
				}
			}
			member {
				name 'SET1-M-5'
				key
			}
			member {
				name 'SET1-M-6'
				key {
					descending 'ELEMENT1'
				}
			}
		}
		Set set = builder.build(definition)
		
		then: "the set will have multiple member records"
		def memberChecker = { List<MemberRole> memberRoles ->
			
			assert memberRoles.size() == 6
			
			assert memberRoles[0].record.name == 'SET1-M-1'
			assert memberRoles[0].nextDbkeyPosition == 1
			assert !memberRoles[0].priorDbkeyPosition
			assert !memberRoles[0].ownerDbkeyPosition
			assert !memberRoles[0].indexDbkeyPosition
			
			assert memberRoles[1].record.name == 'SET1-M-2'
			assert memberRoles[1].nextDbkeyPosition == 1
			assert !memberRoles[1].priorDbkeyPosition
			assert !memberRoles[1].ownerDbkeyPosition
			assert !memberRoles[1].indexDbkeyPosition
			
			assert memberRoles[2].record.name == 'SET1-M-3'
			assert memberRoles[2].nextDbkeyPosition == 1
			assert !memberRoles[2].priorDbkeyPosition
			assert !memberRoles[2].ownerDbkeyPosition
			assert !memberRoles[2].indexDbkeyPosition
						
			assert memberRoles[3].record.name == 'SET1-M-4'
			assert memberRoles[3].nextDbkeyPosition == 1
			assert !memberRoles[3].priorDbkeyPosition
			assert !memberRoles[3].ownerDbkeyPosition
			assert !memberRoles[3].indexDbkeyPosition
			
			assert memberRoles[4].record.name == 'SET1-M-5'
			assert memberRoles[4].nextDbkeyPosition == 1
			assert !memberRoles[4].priorDbkeyPosition
			assert !memberRoles[4].ownerDbkeyPosition
			assert !memberRoles[4].indexDbkeyPosition
			
			assert memberRoles[5].record.name == 'SET1-M-6'
			assert memberRoles[5].nextDbkeyPosition == 1
			assert !memberRoles[5].priorDbkeyPosition
			assert !memberRoles[5].ownerDbkeyPosition
			assert !memberRoles[5].indexDbkeyPosition			
		}
		def sortKeyCheckers = 
			[ { Key sortKey, int index ->
				assert index == 0
				assert sortKey.elements
				assert sortKey.elements.size() == 1
				assert sortKey.elements[0].element
				assert sortKey.elements[0].element.name == 'ELEMENT1'
				assert sortKey.elements[0].sortSequence == SortSequence.ASCENDING
				assert sortKey.elements[0].element.record == sortKey.memberRole.record
				assert sortKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
			}, { Key sortKey, int index ->
				assert index == 1
				assert sortKey.elements
				assert sortKey.elements.size() == 1
				assert sortKey.elements[0].element
				assert sortKey.elements[0].element.name == 'ELEMENT1'
				assert sortKey.elements[0].sortSequence == SortSequence.ASCENDING
				assert sortKey.elements[0].element.record == sortKey.memberRole.record
				assert sortKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
			}, { Key sortKey, int index ->
				assert index == 2
				assert sortKey.elements
				assert sortKey.elements.size() == 1
				assert sortKey.elements[0].element
				assert sortKey.elements[0].element.name == 'ELEMENT1'
				assert sortKey.elements[0].sortSequence == SortSequence.ASCENDING
				assert sortKey.elements[0].element.record == sortKey.memberRole.record
				assert sortKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
			}, { Key sortKey, int index ->
				assert index == 3
				assert sortKey.elements
				assert sortKey.elements.size() == 1
				assert sortKey.elements[0].element
				assert sortKey.elements[0].element.name == 'ELEMENT1'
				assert sortKey.elements[0].sortSequence == SortSequence.ASCENDING
				assert sortKey.elements[0].element.record == sortKey.memberRole.record
				assert sortKey.duplicatesOption == DuplicatesOption.FIRST
			}, { Key sortKey, int index ->
				assert index == 4
				assert sortKey.elements
				assert sortKey.elements.size() == 1
				assert sortKey.elements[0].element
				assert sortKey.elements[0].element.name == 'ELEMENT1'
				assert sortKey.elements[0].sortSequence == SortSequence.ASCENDING
				assert sortKey.elements[0].element.record == sortKey.memberRole.record
				assert sortKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
			}, { Key sortKey, int index ->
				assert index == 5
				assert sortKey.elements
				assert sortKey.elements.size() == 1
				assert sortKey.elements[0].element
				assert sortKey.elements[0].element.name == 'ELEMENT1'
				assert sortKey.elements[0].sortSequence == SortSequence.DESCENDING
				assert sortKey.elements[0].element.record == sortKey.memberRole.record
				assert sortKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
			} ]
		assertMultipleMemberSet(set, 'SET1', [ setOrder : SetOrder.SORTED,
											   memberChecker : memberChecker,
											   sortKeyCheckers : sortKeyCheckers ] )
	}
	
	def "multiple member set pointers (automatically generated member records without owner pointers)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'SET1-M-1' {
				next 5
				prior 6
			}
			member 'SET1-M-2' {
				prior 4
			}
			member 'SET1-M-3'
			member 'SET1-M-4' {
				prior 2
			}
			member 'SET1-M-5' {
				next 7
				prior 8
			}
			member 'SET1-M-6' {
				next 13
			}
		}
		Set set = builder.build(definition)
		
		then: "the set will have multiple member records and ALL member record types will have"
			  "owner pointers because at least 1 (but not all) member record(s) is (are) defined"
			  "with owner pointers"
		def memberChecker = { List<MemberRole> memberRoles ->
			
			assert memberRoles.size() == 6
			
			// mind that all member records have an inconsistent prefix !
			
			assert memberRoles[0].record.name == 'SET1-M-1'
			assert memberRoles[0].nextDbkeyPosition == 5
			assert memberRoles[0].priorDbkeyPosition == 6
			assert !memberRoles[0].ownerDbkeyPosition
			assert !memberRoles[0].indexDbkeyPosition
			
			assert memberRoles[1].record.name == 'SET1-M-2'
			assert memberRoles[1].nextDbkeyPosition == 5  // automatically added AFTER prior pointer
			assert memberRoles[1].priorDbkeyPosition == 4
			assert !memberRoles[1].ownerDbkeyPosition
			assert !memberRoles[1].indexDbkeyPosition
			
			assert memberRoles[2].record.name == 'SET1-M-3'
			assert memberRoles[2].nextDbkeyPosition == 1  // automatically added
			assert !memberRoles[2].priorDbkeyPosition
			assert !memberRoles[2].ownerDbkeyPosition
			assert !memberRoles[2].indexDbkeyPosition
			
			assert memberRoles[3].record.name == 'SET1-M-4'
			assert memberRoles[3].nextDbkeyPosition == 3  // automatically added AFTER prior pointer
			assert memberRoles[3].priorDbkeyPosition == 2
			assert !memberRoles[3].ownerDbkeyPosition
			assert !memberRoles[3].indexDbkeyPosition
			
			assert memberRoles[4].record.name == 'SET1-M-5'
			assert memberRoles[4].nextDbkeyPosition == 7
			assert memberRoles[4].priorDbkeyPosition == 8
			assert !memberRoles[4].ownerDbkeyPosition
			assert !memberRoles[4].indexDbkeyPosition
			
			assert memberRoles[5].record.name == 'SET1-M-6'
			assert memberRoles[5].nextDbkeyPosition == 13
			assert !memberRoles[5].priorDbkeyPosition
			assert !memberRoles[5].ownerDbkeyPosition
			assert !memberRoles[5].indexDbkeyPosition
		}
		assertMultipleMemberSet(set, 'SET1', [ memberChecker : memberChecker ] )
	}
	
	def "multiple member set pointers (automatically generated member records with owner pointers)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'SET1-M-1' { 
				next 5
				prior 6
				owner 7
			}
			member 'SET1-M-2' { 
				prior 4
			}
			member 'SET1-M-3' { 
				owner 2
			}
			member 'SET1-M-4' {
				prior 2
				owner 3				
			}
			member 'SET1-M-5' {
				next 7
				prior 8
			}
			member 'SET1-M-6' {
				next 13
				owner 14
			}
		}
		Set set = builder.build(definition)
		
		then: "the set will have multiple member records and ALL member record types will have"
			  "owner pointers because at least 1 (but not all) member record(s) is (are) defined"
			  "with owner pointers"
		def memberChecker = { List<MemberRole> memberRoles ->
			
			assert memberRoles.size() == 6
			
			// mind that all member records have an inconsistent prefix !
			
			assert memberRoles[0].record.name == 'SET1-M-1'
			assert memberRoles[0].nextDbkeyPosition == 5
			assert memberRoles[0].priorDbkeyPosition == 6
			assert memberRoles[0].ownerDbkeyPosition == 7
			assert !memberRoles[0].indexDbkeyPosition
			
			assert memberRoles[1].record.name == 'SET1-M-2'
			assert memberRoles[1].nextDbkeyPosition == 5  // automatically added AFTER prior pointer
			assert memberRoles[1].priorDbkeyPosition == 4
			assert memberRoles[1].ownerDbkeyPosition == 6 // automatically added AFTER next pointer
			assert !memberRoles[1].indexDbkeyPosition
			
			assert memberRoles[2].record.name == 'SET1-M-3'
			assert memberRoles[2].nextDbkeyPosition == 3  // automatically added AFTER owner pointer
			assert !memberRoles[2].priorDbkeyPosition
			assert memberRoles[2].ownerDbkeyPosition == 2
			assert !memberRoles[2].indexDbkeyPosition
			
			assert memberRoles[3].record.name == 'SET1-M-4'
			assert memberRoles[3].nextDbkeyPosition == 4  // automatically added AFTER owner pointer
			assert memberRoles[3].priorDbkeyPosition == 2
			assert memberRoles[3].ownerDbkeyPosition == 3
			assert !memberRoles[3].indexDbkeyPosition
			
			assert memberRoles[4].record.name == 'SET1-M-5'
			assert memberRoles[4].nextDbkeyPosition == 7
			assert memberRoles[4].priorDbkeyPosition == 8
			assert memberRoles[4].ownerDbkeyPosition == 9 // automatically added AFTER prior pointer
			assert !memberRoles[4].indexDbkeyPosition
			
			assert memberRoles[5].record.name == 'SET1-M-6'
			assert memberRoles[5].nextDbkeyPosition == 13
			assert !memberRoles[5].priorDbkeyPosition
			assert memberRoles[5].ownerDbkeyPosition == 14
			assert !memberRoles[5].indexDbkeyPosition
		}
		assertMultipleMemberSet(set, 'SET1', [ memberChecker : memberChecker ] )
	}
	
	def "multiple member set membership option (automatically generated member records)"() {
		
		given: "a set model builder without a schema"
		def SetModelBuilder builder = new SetModelBuilder()
		
		when: "building the set"
		def definition = {
			member 'SET1-M-1' {
				membership 'MANDATORY AUTOMATIC'
			}
			member 'SET1-M-2' {
				membership 'MANDATORY MANUAL'
			}
			member 'SET1-M-3' {
				membership 'OPTIONAL AUTOMATIC'
			}
			member 'SET1-M-4' {
				membership 'OPTIONAL MANUAL'
			}
		}
		Set set = builder.build(definition)
		
		then: "the set will have multiple member records with the specified membership options"
		def memberChecker = { List<MemberRole> memberRoles ->
			
			assert memberRoles.size() == 4
			
			assert memberRoles[0].record.name == 'SET1-M-1'
			assert memberRoles[0].membershipOption == SetMembershipOption.MANDATORY_AUTOMATIC
			
			assert memberRoles[1].record.name == 'SET1-M-2'
			assert memberRoles[1].membershipOption == SetMembershipOption.MANDATORY_MANUAL
			
			assert memberRoles[2].record.name == 'SET1-M-3'
			assert memberRoles[2].membershipOption == SetMembershipOption.OPTIONAL_AUTOMATIC
			
			assert memberRoles[3].record.name == 'SET1-M-4'
			assert memberRoles[3].membershipOption == SetMembershipOption.OPTIONAL_MANUAL
		}
		assertMultipleMemberSet(set, 'SET1', [ memberChecker : memberChecker ] )
	}
	
	def "unsorted multiple member set (existing member records- 1st style)"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
			record 'RECORD1'
			record 'RECORD2'
			record 'RECORD3'
		})
		SchemaRecord record1 = schema.getRecord('RECORD1')
		assert record1
		SchemaRecord record2 = schema.getRecord('RECORD2')
		assert record2
		SchemaRecord record3 = schema.getRecord('RECORD3')
		assert record3
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			member 'RECORD1'
			member 'RECORD2'
			member 'RECORD3'
		}
		Set set = builder.build(definition)
		
		then: "the set will have multiple member records"
		def memberChecker = { List<MemberRole> memberRoles ->
			
			assert memberRoles.size() == 3
			
			assert memberRoles[0].record.is(record1)
			assert memberRoles[0].nextDbkeyPosition == 1
			assert !memberRoles[0].priorDbkeyPosition
			assert !memberRoles[0].ownerDbkeyPosition
			assert !memberRoles[0].indexDbkeyPosition
			
			assert memberRoles[1].record.is(record2)
			assert memberRoles[1].nextDbkeyPosition == 1
			assert !memberRoles[1].priorDbkeyPosition
			assert !memberRoles[1].ownerDbkeyPosition
			assert !memberRoles[1].indexDbkeyPosition
			
			assert memberRoles[2].record.is(record3)
			assert memberRoles[2].nextDbkeyPosition == 1
			assert !memberRoles[2].priorDbkeyPosition
			assert !memberRoles[2].ownerDbkeyPosition
			assert !memberRoles[2].indexDbkeyPosition
		}
		assertMultipleMemberSet(set, 'SET1', schema, [ memberChecker : memberChecker ] )
	}
	
	def "VIA record: VIA set is resolved"() {
		
		given: "a set model builder with a schema"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
			record 'RECORD1' {
				via { 
					set 'SET1' 
				}	
			}			
		})
		SchemaRecord record1 = schema.getRecord('RECORD1')
		assert record1
		assert record1.viaSpecification
		assert record1.viaSpecification.set
		assert !record1.viaSpecification.set.schema
		def SetModelBuilder builder = new SetModelBuilder( schema : schema )
		
		when: "building the set"
		def definition = {
			member 'RECORD1'			
		}
		Set set = builder.build(definition)
		
		then: "the VIA record's VIA specification will refer to the set that was built and not to a"
		      "placeholder set"
		assertChainedSet(set, 'SET1', schema, [ memberRecordName : 'RECORD1' ] )
		record1.viaSpecification.set.is(set)
	}
	
	def "VIA record: VIA set is NOT resolved"() {
		
		given: "a set model builder with a schema, which we tell to NOT resolve the VIA record's"
		       "VIA set"
		Schema schema = new SchemaModelBuilder().build({
			name 'EMPSCHM'
			version 100
			record 'RECORD1' {
				via {
					set 'SET1'
				}
			}
		})
		SchemaRecord record1 = schema.getRecord('RECORD1')
		assert record1
		assert record1.viaSpecification
		assert record1.viaSpecification.set
		assert !record1.viaSpecification.set.schema
		def SetModelBuilder builder = new SetModelBuilder( schema : schema, resolveViaSet : false )
		
		when: "building the set"
		def definition = {
			member 'RECORD1'
		}
		Set set = builder.build(definition)
		
		then: "the VIA record's VIA specification will refer to the set that was built and not to a"
			  "placeholder set"
		assertChainedSet(set, 'SET1', schema, [ memberRecordName : 'RECORD1' ] )
		record1.viaSpecification.set
		!record1.viaSpecification.set.is(set)
		!record1.viaSpecification.set.schema
	}
}
