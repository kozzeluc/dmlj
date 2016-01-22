/**
 * Copyright (C) 2013  Luc Hermans
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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.RulerType;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

public class UpdateLayoutManager implements ILayoutManager {

	private static final int SUGGESTED_LEFT_MARGIN = 50;
	private static final int SUGGESTED_TOP_MARGIN = 100;	
	
	private Schema referenceSchema;
	private Schema targetSchema;
	
	public UpdateLayoutManager(Schema targetSchema, File referenceSchemaFile) {
		
		super();
		
		// set the target schema
		this.targetSchema = targetSchema;
		
		// get the reference schema from the reference schema file
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
		   		   .getExtensionToFactoryMap()
		   		   .put("schema", new XMIResourceFactoryImpl());
		URI uri = URI.createFileURI(referenceSchemaFile.getAbsolutePath());
		Resource resource = resourceSet.getResource(uri, true);
		referenceSchema = (Schema)resource.getContents().get(0);
		
	}
	
	private DiagramLocation clone(DiagramLocation original) {		
		DiagramLocation clone = SchemaFactory.eINSTANCE.createDiagramLocation();
		clone.setX(original.getX());
		clone.setY(original.getY());
		clone.setEyecatcher(original.getEyecatcher());		
		return clone;		
	}

	private void copyConnectors() {
		for (SchemaRecord targetSchemaRecord : targetSchema.getRecords()) {
			for (MemberRole targetMemberRole : 
				 targetSchemaRecord.getMemberRoles()) {
				
				MemberRole referenceMemberRole = 
					getReferenceMemberRole(targetMemberRole);
				if (referenceMemberRole != null &&
					referenceMemberRole.getConnectionParts().size() > 1) {
					
					// there are 2 connection parts for the line connecting the
					// set owner and member in the reference schema, so we need 
					// to create the second connection part and 2 connectors for
					// the target member role and copy their labels and set 
					// their diagram locations...
						
					// create the diagram location for the first connector
					DiagramLocation referenceLocation1 =
						referenceMemberRole.getConnectionParts()
										   .get(0)
										   .getConnector()
										   .getDiagramLocation();
					DiagramLocation targetLocation1 =
						SchemaFactory.eINSTANCE.createDiagramLocation();
					targetLocation1.setX(referenceLocation1.getX());
					targetLocation1.setY(referenceLocation1.getY());
					String eyeCatcher1 =
						"set connector[0] " + targetMemberRole.getSet().getName() + 
				 		" (" + targetMemberRole.getRecord().getName() + ")";
					targetLocation1.setEyecatcher(eyeCatcher1);
					
					// create the diagram location for the second connector
					DiagramLocation referenceLocation2 =
						referenceMemberRole.getConnectionParts()
										   .get(1)
										   .getConnector()
										   .getDiagramLocation();
					DiagramLocation targetLocation2 = 
						SchemaFactory.eINSTANCE.createDiagramLocation();
					targetLocation2.setX(referenceLocation2.getX());
					targetLocation2.setY(referenceLocation2.getY());
					String eyeCatcher2 =
						"set connector[1] " + targetMemberRole.getSet().getName() + 
						" (" + targetMemberRole.getRecord().getName() + ")";							
					targetLocation2.setEyecatcher(eyeCatcher2);
					
					// create the first connector and set its label and location
					Connector connector1 = 
						SchemaFactory.eINSTANCE.createConnector();
					String label = referenceMemberRole.getConnectionParts()
													  .get(0)
													  .getConnector()
													  .getLabel();
					connector1.setLabel(label);
					connector1.setDiagramLocation(targetLocation1);
					
					// create the second connector and set its label and location
					Connector connector2 = 
						SchemaFactory.eINSTANCE.createConnector();
					connector2.setLabel(label);
					connector2.setDiagramLocation(targetLocation2);
					
					// create the second connection part, set its source and 
					// target endpoint locations, if any, and its connector
					ConnectionPart connectionPart1 =
						targetMemberRole.getConnectionParts().get(0);
					ConnectionPart connectionPart2 = 
						SchemaFactory.eINSTANCE.createConnectionPart();
					connectionPart2.setSourceEndpointLocation(null);
					DiagramLocation targetEndpointLocation = 
						connectionPart1.getTargetEndpointLocation();
					connectionPart2.setTargetEndpointLocation(targetEndpointLocation);
					connectionPart2.setConnector(connector2);
										
					// first connection part and connector
					connectionPart1.setTargetEndpointLocation(null);
					connectionPart1.setConnector(connector1);
					targetMemberRole.getSet()
					  		  		.getSchema()
					  		  		.getDiagramData()
					  		  		.getConnectors()
					  		  		.add(connector1);
					targetMemberRole.getSet()
					  		  		.getSchema()
					  		  		.getDiagramData()
					  		  		.getLocations()
					  		  		.add(connector1.getDiagramLocation());
					
					// second connection part and connector
					targetMemberRole.getConnectionParts().add(connectionPart2);
					targetMemberRole.getSet()
			  		  				.getSchema()
			  		  				.getDiagramData()
			  		  				.getConnectors()
			  		  				.add(connector2);
					targetMemberRole.getSet()
							  		.getSchema()
							  		.getDiagramData()
							  		.getConnectionParts()
							  		.add(connectionPart2);				
					targetMemberRole.getSet()
					  		  		.getSchema()
					  		  		.getDiagramData()
					  		  		.getLocations()
					  		  		.add(connector2.getDiagramLocation());					
					
				}
			}
		}		
	}

	private void copyGuides(Ruler targetRuler, Ruler referenceRuler) {
		for (Guide referenceGuide : referenceRuler.getGuides()) {
			Guide targetGuide = SchemaFactory.eINSTANCE.createGuide();
			targetGuide.setPosition(referenceGuide.getPosition());
			targetRuler.getGuides().add(targetGuide);
		}		
	}

	private void copyOptionsAndRulers() {
		
		DiagramData targetDiagramData = targetSchema.getDiagramData();
		DiagramData referenceDiagramData = referenceSchema.getDiagramData();		
		
		// options
		targetDiagramData.setShowGrid(referenceDiagramData.isShowGrid());
		targetDiagramData.setShowRulers(referenceDiagramData.isShowRulers());
		targetDiagramData.setSnapToGeometry(referenceDiagramData.isSnapToGeometry());
		targetDiagramData.setSnapToGrid(referenceDiagramData.isSnapToGrid());
		targetDiagramData.setSnapToGuides(referenceDiagramData.isSnapToGuides());
		
		targetDiagramData.setZoomLevel(referenceDiagramData.getZoomLevel());
		
		// rulers
		Ruler targetHorizontalRuler = null;
		Ruler targetVerticalRuler = null;
		for (Ruler targetRuler : targetDiagramData.getRulers()) {
			if (targetRuler.getType() == RulerType.HORIZONTAL) {
				targetHorizontalRuler = targetRuler;
			} else {
				targetVerticalRuler = targetRuler;
			}
		}
		Ruler referenceHorizontalRuler = null;
		Ruler referenceVerticalRuler = null;
		for (Ruler referenceRuler : referenceDiagramData.getRulers()) {
			if (referenceRuler.getType() == RulerType.HORIZONTAL) {
				referenceHorizontalRuler = referenceRuler;
			} else {
				referenceVerticalRuler = referenceRuler;
			}
		}
		copyGuides(targetHorizontalRuler, referenceHorizontalRuler);
		copyGuides(targetVerticalRuler, referenceVerticalRuler);
			
	}

	private MemberRole getReferenceMemberRole(MemberRole targetMemberRole) {
		
		SchemaRecord referenceRecord = 
			getReferenceRecord(targetMemberRole.getRecord());
		
		if (referenceRecord != null) {
			String targetSetName = targetMemberRole.getSet().getName();
			for (MemberRole referenceMemberRole :
				 referenceRecord.getMemberRoles()) {
				
				String referenceSetName = 
					referenceMemberRole.getSet().getName();
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
		
		if (targetSchema.getRecords().isEmpty()) {
			// nothing more to do when no records
			return;
		}
		
		layoutRecords();		
		layoutSystemOwners();
		
		copyConnectors();		
		layoutConnectionParts();
		
		layoutConnectionLabels();
		
	}

	private void layoutConnectionLabels() {
		for (SchemaRecord targetRecord : targetSchema.getRecords()) {
			for (MemberRole targetMemberRole : targetRecord.getMemberRoles()) {
				ConnectionLabel targetConnectionLabel =
					targetMemberRole.getConnectionLabel();
				MemberRole referenceMemberRole =
					getReferenceMemberRole(targetMemberRole);
				if (referenceMemberRole != null) {
					// existing set participation
					DiagramLocation referenceLocation =
						referenceMemberRole.getConnectionLabel()
										   .getDiagramLocation();
					setDiagramLocation(targetConnectionLabel, 
									   referenceLocation.getX(), 
									   referenceLocation.getY());
				} else {
					// new set participation: place the label above the record
					int x = targetRecord.getDiagramLocation().getX();
					int y = targetRecord.getDiagramLocation().getY() - 25;
					setDiagramLocation(targetConnectionLabel, x, y);					
				}
			}
		}		
	}

	private void layoutConnectionParts() {
		DiagramData diagramData = targetSchema.getDiagramData();
		for (SchemaRecord targetRecord : targetSchema.getRecords()) {
			for (MemberRole targetMemberRole : targetRecord.getMemberRoles()) {
				MemberRole referenceMemberRole =
					getReferenceMemberRole(targetMemberRole);
				if (referenceMemberRole != null) {
					int bendpointIndex = 0;
					for (int i = 0; 
						 i < targetMemberRole.getConnectionParts().size(); i++) {
						
						ConnectionPart target = 
							targetMemberRole.getConnectionParts().get(i);
						ConnectionPart reference = 
							referenceMemberRole.getConnectionParts().get(i);
						
						if (reference.getSourceEndpointLocation() != null) {
							DiagramLocation location =
								clone(reference.getSourceEndpointLocation());
							String eyecatcher =
								"set " + targetMemberRole.getSet().getName() + 
								" owner endpoint (" + 
								targetMemberRole.getRecord().getName() + ")";							
							location.setEyecatcher(eyecatcher);
							diagramData.getLocations().add(location);
							target.setSourceEndpointLocation(location);							
						}
						
						if (reference.getTargetEndpointLocation() != null) {
							DiagramLocation location =
								clone(reference.getTargetEndpointLocation());
							String eyecatcher =
								"set " + targetMemberRole.getSet().getName() + 
								" member endpoint (" + 
								targetMemberRole.getRecord().getName() + ")";							
							location.setEyecatcher(eyecatcher);
							diagramData.getLocations().add(location);
							target.setTargetEndpointLocation(location);
						}
						
						for (int j = 0; j < reference.getBendpointLocations().size(); j++) {
							DiagramLocation location =
								clone(reference.getBendpointLocations().get(j));
							String eyecatcher =
								"bendpoint [" + bendpointIndex++ + "] set " + 
								targetMemberRole.getSet().getName() + " (" +
								targetMemberRole.getRecord().getName() + ")";									
							location.setEyecatcher(eyecatcher);
							diagramData.getLocations().add(location);
							target.getBendpointLocations().add(location);
							
						}
						
					}
				}
			}
		}
	}

	private void layoutRecords() {
		
		// process the records that are present in both the target and reference
		// schema
		int highestX = Integer.MIN_VALUE;
		int lowestY = Integer.MAX_VALUE;
		for (SchemaRecord targetRecord : targetSchema.getRecords()) {
			SchemaRecord referenceRecord = 
				referenceSchema.getRecord(targetRecord.getName());
			if (referenceRecord != null) {
				int x = referenceRecord.getDiagramLocation().getX();
				int y = referenceRecord.getDiagramLocation().getY();
				setDiagramLocation(targetRecord, x, y);								   
				if (x > highestX) {
					highestX = x;
				}
				if (y < lowestY) {
					lowestY = y;
				}
			}
		}
		
		// process the records without diagram data (i.e. the records that are
		// only defined in the target schema and not the reference schema): 
		// place them in 1 column to the right of all existing records		
		int x = 
			highestX != Integer.MIN_VALUE ? 
						highestX + 2 * RecordFigure.UNSCALED_WIDTH : 
						SUGGESTED_LEFT_MARGIN;
		int y = 
			lowestY != Integer.MAX_VALUE ? lowestY : SUGGESTED_TOP_MARGIN;
		for (SchemaRecord targetRecord : targetSchema.getRecords()) {
			if (targetRecord.getDiagramLocation() == null) {
				setDiagramLocation(targetRecord, x, y);				
				y += 2 * RecordFigure.UNSCALED_HEIGHT;				
			}
		}
	}

	private void layoutSystemOwners() {
		for (SchemaRecord targetRecord : targetSchema.getRecords()) {
			// traverse all sets in which the record participates as a member
			int i = 0; // new index counter
			for (MemberRole targetMemberRole : targetRecord.getMemberRoles()) {
				// we're only interested in system owned indexed sets
				if (targetMemberRole.getSet().getSystemOwner() != null) {					
					// system owner (index) encountered, see if the index was
					// already defined on the record in the reference schema
					// and, if so, copy its diagram location
					SystemOwner targetSystemOwner = 
						targetMemberRole.getSet().getSystemOwner();						
					MemberRole referenceMemberRole =
						getReferenceMemberRole(targetMemberRole);					
					if (referenceMemberRole != null &&
						referenceMemberRole.getSet().getSystemOwner() != null) {
						
						SystemOwner referenceSystemOwner = 
							referenceMemberRole.getSet().getSystemOwner();
						DiagramLocation referenceLocation =
							referenceSystemOwner.getDiagramLocation();
						setDiagramLocation(targetSystemOwner, 
										   referenceLocation.getX(), 
										   referenceLocation.getY());						
					}
					// if no diagram location is set, create one; all new
					// indexes will be placed above the record, next to each
					// other
					if (targetSystemOwner.getDiagramLocation() == null) {
						int x = targetRecord.getDiagramLocation().getX() + 
								25 * i - 11;
						int y = targetRecord.getDiagramLocation().getY() - 
								RecordFigure.UNSCALED_HEIGHT + 5;
						setDiagramLocation(targetSystemOwner, x, y);
						// next new index will be located to the right of this 
						// one:
						i += 1; 
					}
				}
			}
		}		
	}	

	private void setDiagramLocation(ConnectionLabel connectionLabel, int x, 
									int y) {
		
		DiagramLocation location = 
			SchemaFactory.eINSTANCE.createDiagramLocation();
		SchemaRecord record = connectionLabel.getMemberRole().getRecord();
		record.getSchema().getDiagramData().getLocations().add(location);
		connectionLabel.setDiagramLocation(location);				
		location.setX(x);
		location.setY(y);
		location.setEyecatcher("set label " + connectionLabel.getMemberRole()
							   				  				 .getSet()
							   				  				 .getName() + 
							   " (" + record.getName() + ")");		
	}

	private void setDiagramLocation(SchemaRecord record, int x, int y) {
		
		DiagramLocation diagramLocation = 
			SchemaFactory.eINSTANCE.createDiagramLocation();
		targetSchema.getDiagramData()
					.getLocations()
					.add(diagramLocation);
		record.setDiagramLocation(diagramLocation);
		diagramLocation.setX(x);
		diagramLocation.setY(y);		
		diagramLocation.setEyecatcher("record " + record.getName());
		
	}
	
	private void setDiagramLocation(SystemOwner systemOwner, int x, int y) {
		DiagramLocation targetLocation = 
			SchemaFactory.eINSTANCE.createDiagramLocation();
		systemOwner.getSet()
				   .getSchema()
				   .getDiagramData()
				   .getLocations()
				   .add(targetLocation);
		systemOwner.setDiagramLocation(targetLocation);
		targetLocation.setX(x);
		targetLocation.setY(y);		
		targetLocation.setEyecatcher("system owner " + 
									 systemOwner.getSet().getName());
	}	

}
