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
package org.lh.dmlj.schema.editor.dsl.builder.syntax

import org.lh.dmlj.schema.AreaSpecification
import org.lh.dmlj.schema.ConnectionPart
import org.lh.dmlj.schema.DiagramLocation
import org.lh.dmlj.schema.KeyElement
import org.lh.dmlj.schema.MemberRole
import org.lh.dmlj.schema.OffsetExpression
import org.lh.dmlj.schema.Set
import org.lh.dmlj.schema.SetMembershipOption
import org.lh.dmlj.schema.SetMode
import org.lh.dmlj.schema.SetOrder

class SetSyntaxBuilder extends AbstractSyntaxBuilder<Set> {

	@Override
	protected String build() {		
		name()
		order()
		index()
		owner()
		members()
	}
	
	private void name() {
		if (generateName) {
			without_tab "name '${model.name}'"
		}
	}
	
	private void order() {
		if (model.order != SetOrder.NEXT) {
			if (generateName) {
				blank_line()
			}
			without_tab "order '${model.order}'"
		}
	}
	
	private void index() {
		if (model.mode == SetMode.INDEXED) {
			if (generateName || model.order != SetOrder.NEXT) {
				blank_line()
			}
			if (model.indexedSetModeSpecification.symbolicIndexName) {
				without_tab "index '${model.indexedSetModeSpecification.symbolicIndexName}'"
			} else {
				without_tab "index {"
				with_1_tab "keys ${model.indexedSetModeSpecification.keyCount}"
				if (model.indexedSetModeSpecification.displacementPageCount) {
					with_1_tab "displacement ${model.indexedSetModeSpecification.displacementPageCount}"
				}
				without_tab "}"
			}
		}
	}
	
	private void owner() {
		ownerRecord()
		systemOwner()
		vsamIndex()
	}
	
	private void ownerRecord() {
		if (model.owner) {
			if (generateName || model.order != SetOrder.NEXT || model.mode == SetMode.INDEXED) {
				blank_line()
			}			
			without_tab "owner '${model.owner.record.name}' {"
			with_1_tab "next ${model.owner.nextDbkeyPosition}"
			if (model.owner.priorDbkeyPosition) {
				with_1_tab "prior ${model.owner.priorDbkeyPosition}"
			}
			without_tab "}"
		}		
	}
	
	private void systemOwner() {
		if (model.mode == SetMode.INDEXED && model.systemOwner) {
			blank_line()
			AreaSpecification areaSpecification = model.systemOwner.areaSpecification
			without_tab "systemOwner {"
			if (!areaSpecification.symbolicSubareaName &&
				!areaSpecification.offsetExpression ||
				areaSpecification.offsetExpression &&
				!areaSpecification.offsetExpression.offsetPageCount &&
				!areaSpecification.offsetExpression.offsetPercent &&
				!areaSpecification.offsetExpression.pageCount &&
				!areaSpecification.offsetExpression.percent) {
			
				with_1_tab "area '${areaSpecification.area.name}'"
			} else {
				with_1_tab "area '${areaSpecification.area.name}' {"
				if (areaSpecification.symbolicSubareaName) {
					with_2_tabs "subarea '${areaSpecification.symbolicSubareaName}'"
				} else if (areaSpecification.offsetExpression) {
					OffsetExpression offsetExpression = areaSpecification.offsetExpression
					if (offsetExpression.offsetPageCount) {
						with_2_tabs "offsetPages ${offsetExpression.offsetPageCount}"
					} else if (offsetExpression.offsetPercent) {
						with_2_tabs "offsetPercent ${offsetExpression.offsetPercent}"
					}
					if (offsetExpression.pageCount) {
						with_2_tabs "pages ${offsetExpression.pageCount}"
					} else if (offsetExpression.percent) {
						with_2_tabs "percent ${offsetExpression.percent}"
					}
				}
				with_1_tab "}"
			}
			blank_line()		
			with_1_tab "diagram {"
			with_2_tabs "x ${model.systemOwner.diagramLocation.x}"
			with_2_tabs "y ${model.systemOwner.diagramLocation.y}"
			with_1_tab "}"
			without_tab "}"
		}
	}
	
	private void vsamIndex() {
		if (model.mode == SetMode.VSAM_INDEX && model.vsamIndex) {
			blank_line()
			without_tab "vsamIndex {"
			with_1_tab "x ${model.vsamIndex.diagramLocation.x}"
			with_1_tab "y ${model.vsamIndex.diagramLocation.y}"
			without_tab "}"
		}
	}
	
	private void members() {
		for (memberRole in model.members) {
			blank_line()
			without_tab "member '${memberRole.record.name}' {"
			memberPointers(memberRole)
			membershipOption(memberRole)
			sortKey(memberRole)
			diagram(memberRole)
			without_tab "}"
		}
	}
	
	private void memberPointers(MemberRole memberRole) {
	    if (memberRole.indexDbkeyPosition) {
	        with_1_tab "index ${memberRole.indexDbkeyPosition}"
	    }
	    if (memberRole.nextDbkeyPosition) {	
	        with_1_tab "next ${memberRole.nextDbkeyPosition}"
	    }
	    if (memberRole.priorDbkeyPosition) {	            
	        with_1_tab "prior ${memberRole.priorDbkeyPosition}"			
	    }
	    if (memberRole.ownerDbkeyPosition) {	
	        with_1_tab "owner ${memberRole.ownerDbkeyPosition}"
	    }
	}
	
	private void membershipOption(MemberRole memberRole) {
		if (memberRole.membershipOption != SetMembershipOption.MANDATORY_AUTOMATIC) {
			blank_line()
			with_1_tab "membership '${replaceUnderscoresBySpaces(memberRole.membershipOption)}'"
		}
	}
	
	private void sortKey(MemberRole memberRole) {
		if (model.order == SetOrder.SORTED && memberRole.sortKey) {
			blank_line()
			with_1_tab "key {"
			for (KeyElement keyElement in memberRole.sortKey.elements) {
				String sequence = keyElement.sortSequence.toString().toLowerCase()
				String elementName = keyElement.dbkey ? 'dbkey' : keyElement.element.name
				with_2_tabs "$sequence '$elementName'"
			}
			if (memberRole.sortKey.naturalSequence) {
				with_2_tabs "naturalSequence"
			}
			if (memberRole.sortKey.compressed) {
				with_2_tabs "compressed"
			}
			with_2_tabs "duplicates '${replaceUnderscoresBySpaces(memberRole.sortKey.duplicatesOption)}'"
			with_1_tab "}"
		}
	}
	
	private void diagram(MemberRole memberRole) {
		
		blank_line()
		
		ConnectionPart firstConnectionPart = memberRole.connectionParts[0]
		ConnectionPart secondConnectionPart = null
		if (memberRole.connectionParts.size() > 1) {
			secondConnectionPart = memberRole.connectionParts[1]
		}
		ConnectionPart lastConnectionPart = secondConnectionPart ?: firstConnectionPart
		
		with_1_tab "diagram {"
		
		with_2_tabs "label {"
		with_3_tabs "x ${memberRole.connectionLabel.diagramLocation.x}"
		with_3_tabs "y ${memberRole.connectionLabel.diagramLocation.y}"
		with_2_tabs "}"
		
		if (firstConnectionPart.sourceEndpointLocation || firstConnectionPart.bendpointLocations ||
			secondConnectionPart || lastConnectionPart.targetEndpointLocation) {
			
			blank_line()
			with_2_tabs "line {"
		}
		
		if (firstConnectionPart.sourceEndpointLocation) {
			with_3_tabs "start {"
			with_4_tabs "x ${firstConnectionPart.sourceEndpointLocation.x}"
			with_4_tabs "y ${firstConnectionPart.sourceEndpointLocation.y}"
			with_3_tabs "}"
		}
		
		for (DiagramLocation bendpoint in firstConnectionPart.bendpointLocations) {			
			blank_line()
			with_3_tabs "bendpoint {"
			with_4_tabs "x ${bendpoint.x}"
			with_4_tabs "y ${bendpoint.y}"
			with_3_tabs "}"
		}
		
		if (secondConnectionPart) {
			blank_line()			
			with_3_tabs "connectors {"
			with_4_tabs "label ${withQuotes(firstConnectionPart.connector.label)}"
			for (ConnectionPart connectionPart in memberRole.connectionParts) {
				blank_line()
				with_4_tabs "connector {"
				with_5_tabs "x ${connectionPart.getConnector().diagramLocation.x}"
				with_5_tabs "y ${connectionPart.getConnector().diagramLocation.y}"
				with_4_tabs "}"
			}
			with_3_tabs "}"	
			
			for (DiagramLocation bendpoint in secondConnectionPart.bendpointLocations) {
				blank_line()
				with_3_tabs "bendpoint {"
				with_4_tabs "x ${bendpoint.x}"
				with_4_tabs "y ${bendpoint.y}"
				with_3_tabs "}"
			}
		}
		
		if (lastConnectionPart.targetEndpointLocation) {
			if (firstConnectionPart.sourceEndpointLocation) {
				blank_line()
			}
			with_3_tabs "end {"
			with_4_tabs "x ${lastConnectionPart.targetEndpointLocation.x}"
			with_4_tabs "y ${lastConnectionPart.targetEndpointLocation.y}"
			with_3_tabs "}"
		}
		
		if (firstConnectionPart.sourceEndpointLocation || firstConnectionPart.bendpointLocations ||
			secondConnectionPart || lastConnectionPart.targetEndpointLocation) {
			
			with_2_tabs "}"
		}
			
		with_1_tab "}"
	}
	
}
