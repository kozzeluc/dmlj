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
package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.io.File;
import java.util.List;

import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.RulerType;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

public class UpdateLayoutManager implements ILayoutManager {
	private static final int SUGGESTED_LEFT_MARGIN = 50;
	private static final int SUGGESTED_TOP_MARGIN = 100;	
	
	private Schema referenceSchema;
	private Schema targetSchema;
	
	public UpdateLayoutManager(Schema targetSchema, File referenceSchemaFile) {
		this.targetSchema = targetSchema;		
		referenceSchema = Tools.readFromFile(referenceSchemaFile);
	}
	
	private DiagramLocation clone(DiagramLocation original) {		
		var clone = SchemaFactory.eINSTANCE.createDiagramLocation();
		clone.setX(original.getX());
		clone.setY(original.getY());
		clone.setEyecatcher(original.getEyecatcher());		
		return clone;		
	}

	private void copyConnectors() {
		for (var targetSchemaRecord : targetSchema.getRecords()) {
			for (var targetMemberRole : targetSchemaRecord.getMemberRoles()) {
				var referenceMemberRole = getReferenceMemberRole(targetMemberRole);
				if (referenceMemberRole != null && referenceMemberRole.getConnectionParts().size() > 1) {
					// there are 2 connection parts for the line connecting the set owner and member in the
					// reference schema, so we need to create the second connection part and 2 connectors for the
					// target member role and copy their labels and set their diagram locations...
						
					// create the diagram location for the first connector
					var referenceLocation1 = referenceMemberRole.getConnectionParts().get(0).getConnector().getDiagramLocation();
					var targetLocation1 = SchemaFactory.eINSTANCE.createDiagramLocation();
					targetLocation1.setX(referenceLocation1.getX());
					targetLocation1.setY(referenceLocation1.getY());
					var eyeCatcher1 = "set connector[0] " + targetMemberRole.getSet().getName() + " (" + targetMemberRole.getRecord().getName() + ")";
					targetLocation1.setEyecatcher(eyeCatcher1);
					
					// create the diagram location for the second connector
					var referenceLocation2 = referenceMemberRole.getConnectionParts().get(1).getConnector().getDiagramLocation();
					var targetLocation2 = SchemaFactory.eINSTANCE.createDiagramLocation();
					targetLocation2.setX(referenceLocation2.getX());
					targetLocation2.setY(referenceLocation2.getY());
					var eyeCatcher2 = "set connector[1] " + targetMemberRole.getSet().getName() + " (" + targetMemberRole.getRecord().getName() + ")";							
					targetLocation2.setEyecatcher(eyeCatcher2);
					
					// create the first connector and set its label and location
					var connector1 = SchemaFactory.eINSTANCE.createConnector();
					var label = referenceMemberRole.getConnectionParts().get(0).getConnector().getLabel();
					connector1.setLabel(label);
					connector1.setDiagramLocation(targetLocation1);
					
					// create the second connector and set its label and location
					var connector2 = SchemaFactory.eINSTANCE.createConnector();
					connector2.setLabel(label);
					connector2.setDiagramLocation(targetLocation2);
					
					// create the second connection part, set its source and target endpoint locations, if any, and its connector
					var connectionPart1 = targetMemberRole.getConnectionParts().get(0);
					var connectionPart2 = SchemaFactory.eINSTANCE.createConnectionPart();
					connectionPart2.setSourceEndpointLocation(null);
					var targetEndpointLocation = connectionPart1.getTargetEndpointLocation();
					connectionPart2.setTargetEndpointLocation(targetEndpointLocation);
					connectionPart2.setConnector(connector2);
										
					// first connection part and connector
					connectionPart1.setTargetEndpointLocation(null);
					connectionPart1.setConnector(connector1);
					targetMemberRole.getSet().getSchema().getDiagramData().getConnectors().add(connector1);
					targetMemberRole.getSet().getSchema().getDiagramData().getLocations().add(connector1.getDiagramLocation());
					
					// second connection part and connector
					targetMemberRole.getConnectionParts().add(connectionPart2);
					targetMemberRole.getSet().getSchema().getDiagramData().getConnectors().add(connector2);
					targetMemberRole.getSet().getSchema().getDiagramData().getConnectionParts().add(connectionPart2);
					targetMemberRole.getSet().getSchema().getDiagramData().getLocations().add(connector2.getDiagramLocation());
				}
			}
		}		
	}

	private void copyOptionsAndRulers() {
		var targetDiagramData = targetSchema.getDiagramData();
		var referenceDiagramData = referenceSchema.getDiagramData();		
		
		// options
		targetDiagramData.setShowGrid(referenceDiagramData.isShowGrid());
		targetDiagramData.setShowRulers(referenceDiagramData.isShowRulers());
		targetDiagramData.setSnapToGeometry(referenceDiagramData.isSnapToGeometry());
		targetDiagramData.setSnapToGrid(referenceDiagramData.isSnapToGrid());
		targetDiagramData.setSnapToGuides(referenceDiagramData.isSnapToGuides());
		
		targetDiagramData.setZoomLevel(referenceDiagramData.getZoomLevel());
				
		var targetHorizontalRuler = findRuler(targetDiagramData.getRulers(), RulerType.HORIZONTAL);
		var referenceHorizontalRuler = findRuler(referenceDiagramData.getRulers(), RulerType.HORIZONTAL);
		copyGuides(targetHorizontalRuler, referenceHorizontalRuler);
		var targetVerticalRuler = findRuler(targetDiagramData.getRulers(), RulerType.VERTICAL);
		var referenceVerticalRuler = findRuler(referenceDiagramData.getRulers(), RulerType.VERTICAL);
		copyGuides(targetVerticalRuler, referenceVerticalRuler);
	}
	
	private Ruler findRuler(List<Ruler> rulers, RulerType type) {
		return rulers.stream()
				.filter(r -> r.getType() == type)
				.findFirst()
				.orElseThrow();
	}
	
	private void copyGuides(Ruler targetRuler, Ruler referenceRuler) {
		for (var referenceGuide : referenceRuler.getGuides()) {
			var targetGuide = SchemaFactory.eINSTANCE.createGuide();
			targetGuide.setPosition(referenceGuide.getPosition());
			targetRuler.getGuides().add(targetGuide);
		}		
	}

	private MemberRole getReferenceMemberRole(MemberRole targetMemberRole) {
		var referenceRecord = getReferenceRecord(targetMemberRole.getRecord());
		if (referenceRecord != null) {
			var targetSetName = targetMemberRole.getSet().getName();
			for (var referenceMemberRole : referenceRecord.getMemberRoles()) {
				var referenceSetName = referenceMemberRole.getSet().getName();
				if (referenceSetName.equals(targetSetName)) {
					// we've found a matching reference member role
					return referenceMemberRole;
				}
			}
		}
		return null; // no matching member role in the reference record
	}
	
	private SchemaRecord getReferenceRecord(SchemaRecord targetRecord) {
		return referenceSchema.getRecord(targetRecord.getName());
	}

	@Override
	public Schema getReferenceSchema() {
		return referenceSchema;
	}

	@Override
	public void layout() {
		copyOptionsAndRulers();
		if (!targetSchema.getRecords().isEmpty()) {
			layoutRecords();		
			layoutSystemOwners();
			copyConnectors();
			layoutConnectionParts();
			layoutConnectionLabels();
		}
	}

	private void layoutConnectionLabels() {
		for (var targetRecord : targetSchema.getRecords()) {
			for (var targetMemberRole : targetRecord.getMemberRoles()) {
				var targetConnectionLabel = targetMemberRole.getConnectionLabel();
				var referenceMemberRole = getReferenceMemberRole(targetMemberRole);
				if (referenceMemberRole != null) {
					// existing set participation
					var referenceLocation = referenceMemberRole.getConnectionLabel().getDiagramLocation();
					setDiagramLocation(targetConnectionLabel, referenceLocation.getX(), referenceLocation.getY());
				} else {
					// new set participation: place the label above the record
					var x = targetRecord.getDiagramLocation().getX();
					var y = targetRecord.getDiagramLocation().getY() - 25;
					setDiagramLocation(targetConnectionLabel, x, y);					
				}
			}
		}		
	}

	private void layoutConnectionParts() {
		targetSchema.getRecords().stream()
				.map(SchemaRecord::getMemberRoles)
				.flatMap(List::stream)
				.forEach(this::layoutConnectionPartsForMemberRole);
	}
	
	private void layoutConnectionPartsForMemberRole(MemberRole targetMemberRole) {
		var referenceMemberRole = getReferenceMemberRole(targetMemberRole);
		if (referenceMemberRole != null) {
			var bendpointIndex = 0;
			for (var i = 0; i < targetMemberRole.getConnectionParts().size(); i++) {
				var target = targetMemberRole.getConnectionParts().get(i);
				var reference = referenceMemberRole.getConnectionParts().get(i);
				if (reference.getSourceEndpointLocation() != null) {
					cloneSourceEndpointLocation(reference, target);
				}
				if (reference.getTargetEndpointLocation() != null) {
					cloneTargetEndpointLocation(reference, target);
				}
				bendpointIndex = cloneBendpointLocation(reference, target, bendpointIndex);
			}
		}
	}
	
	private void cloneSourceEndpointLocation(ConnectionPart source, ConnectionPart target) {
		var targetMemberRole = target.getMemberRole();
		var diagramData = targetMemberRole.getSet().getSchema().getDiagramData();
		var location = clone(source.getSourceEndpointLocation());
		var eyecatcher = "set " + targetMemberRole.getSet().getName() + " owner endpoint (" +
				targetMemberRole.getRecord().getName() + ")";							
		location.setEyecatcher(eyecatcher);
		diagramData.getLocations().add(location);
		target.setSourceEndpointLocation(location);
	}
	
	private void cloneTargetEndpointLocation(ConnectionPart source, ConnectionPart target) {
		var targetMemberRole = target.getMemberRole();
		var diagramData = targetMemberRole.getSet().getSchema().getDiagramData();
		var location = clone(source.getTargetEndpointLocation());
		var eyecatcher = "set " + targetMemberRole.getSet().getName() + " member endpoint (" +
				targetMemberRole.getRecord().getName() + ")";							
		location.setEyecatcher(eyecatcher);
		diagramData.getLocations().add(location);
		target.setTargetEndpointLocation(location);
	}
	
	private int cloneBendpointLocation(ConnectionPart reference, ConnectionPart target, int initialBendpointIndex) {
		var targetMemberRole = target.getMemberRole();
		var diagramData = targetMemberRole.getSet().getSchema().getDiagramData();
		var bendpointIndex = initialBendpointIndex;
		for (var j = 0; j < reference.getBendpointLocations().size(); j++) {
			var location = clone(reference.getBendpointLocations().get(j));
			var eyecatcher = "bendpoint [" + bendpointIndex++ + "] set " + targetMemberRole.getSet().getName() +
					" (" + targetMemberRole.getRecord().getName() + ")";
			location.setEyecatcher(eyecatcher);
			diagramData.getLocations().add(location);
			target.getBendpointLocations().add(location);
		}
		return bendpointIndex;
	}

	private void layoutRecords() {
		// process the records that are present in both the target and reference schema
		var highestX = Integer.MIN_VALUE;
		var lowestY = Integer.MAX_VALUE;
		for (var targetRecord : targetSchema.getRecords()) {
			var referenceRecord = referenceSchema.getRecord(targetRecord.getName());
			if (referenceRecord != null) {
				var x = referenceRecord.getDiagramLocation().getX();
				var y = referenceRecord.getDiagramLocation().getY();
				setDiagramLocation(targetRecord, x, y);								   
				if (x > highestX) {
					highestX = x;
				}
				if (y < lowestY) {
					lowestY = y;
				}
			}
		}
		
		// process the records without diagram data (i.e. the records that are only defined in the target schema
		// and not the reference schema): place them in 1 column to the right of all existing records
		var x = highestX != Integer.MIN_VALUE ? highestX + 2 * RecordFigure.UNSCALED_WIDTH : SUGGESTED_LEFT_MARGIN;
		var y = lowestY != Integer.MAX_VALUE ? lowestY : SUGGESTED_TOP_MARGIN;
		for (var targetRecord : targetSchema.getRecords()) {
			if (targetRecord.getDiagramLocation() == null) {
				setDiagramLocation(targetRecord, x, y);				
				y += 2 * RecordFigure.UNSCALED_HEIGHT;				
			}
		}
	}

	private void layoutSystemOwners() {
		for (var targetRecord : targetSchema.getRecords()) {
			// traverse all sets in which the record participates as a member
			var i = 0; // new index counter
			for (var targetMemberRole : targetRecord.getMemberRoles()) {
				// we're only interested in system owned indexed sets
				if (targetMemberRole.getSet().getSystemOwner() != null) {					
					// system owner (index) encountered, see if the index was already defined on the record in
					// the reference schema and, if so, copy its diagram location
					var targetSystemOwner = targetMemberRole.getSet().getSystemOwner();
					var referenceMemberRole = getReferenceMemberRole(targetMemberRole);
					if (referenceMemberRole != null && referenceMemberRole.getSet().getSystemOwner() != null) {
						var referenceSystemOwner = referenceMemberRole.getSet().getSystemOwner();
						var referenceLocation = referenceSystemOwner.getDiagramLocation();
						setDiagramLocation(targetSystemOwner, referenceLocation.getX(), referenceLocation.getY());
					}
					// if no diagram location is set, create one; all new indexes will be placed above the record,
					// next to each other
					if (targetSystemOwner.getDiagramLocation() == null) {
						var x = targetRecord.getDiagramLocation().getX() + 25 * i - 11;
						var y = targetRecord.getDiagramLocation().getY() - RecordFigure.UNSCALED_HEIGHT + 5;
						setDiagramLocation(targetSystemOwner, x, y);
						// next new index will be located to the right of this one:
						i += 1; 
					}
				}
			}
		}		
	}	

	private void setDiagramLocation(ConnectionLabel connectionLabel, int x, int y) {
		var location = SchemaFactory.eINSTANCE.createDiagramLocation();
		var schemaRecord = connectionLabel.getMemberRole().getRecord();
		schemaRecord.getSchema().getDiagramData().getLocations().add(location);
		connectionLabel.setDiagramLocation(location);				
		location.setX(x);
		location.setY(y);
		location.setEyecatcher("set label " + connectionLabel.getMemberRole().getSet().getName() + " (" + schemaRecord.getName() + ")");		
	}

	private void setDiagramLocation(SchemaRecord schemaRecord, int x, int y) {
		var diagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		targetSchema.getDiagramData().getLocations().add(diagramLocation);
		schemaRecord.setDiagramLocation(diagramLocation);
		diagramLocation.setX(x);
		diagramLocation.setY(y);		
		diagramLocation.setEyecatcher("record " + schemaRecord.getName());
	}
	
	private void setDiagramLocation(SystemOwner systemOwner, int x, int y) {
		var targetLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		systemOwner.getSet().getSchema().getDiagramData().getLocations().add(targetLocation);
		systemOwner.setDiagramLocation(targetLocation);
		targetLocation.setX(x);
		targetLocation.setY(y);		
		targetLocation.setEyecatcher("system owner " + systemOwner.getSet().getName());
	}

}
