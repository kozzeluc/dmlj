/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ReconnectRequest;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.editor.anchor.LockedRecordSourceAnchor;
import org.lh.dmlj.schema.editor.anchor.LockedRecordTargetAnchor;
import org.lh.dmlj.schema.editor.anchor.ReconnectEndpointAnchor;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.RecordFigure;
import org.lh.dmlj.schema.editor.policy.RecordGraphicalNodeEditPolicy;
import org.lh.dmlj.schema.editor.policy.RecordXYLayoutEditPolicy;

public class RecordEditPart 
	extends AbstractNonResizableDiagramNodeEditPart<SchemaRecord>  {

	private RecordEditPart() {
		super(null, null); // disabled constructor
	}
	
	public RecordEditPart(SchemaRecord record, IModelChangeProvider modelChangeProvider) {
		super(record, modelChangeProvider);		
	}	
	
	@Override
	public void afterMoveItem(EObject oldOwner, EReference reference, Object item, 
							  EObject newOwner) {

		if (item == getModel().getAreaSpecification()) {
			// the record was moved to another area
			refreshVisuals();
		}		
	}
	
	@Override
	public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {
		
		super.afterSetFeatures(owner, features);
		
		if (owner == getModel()) {			
			
			// refresh the edit part's visuals because one of the contained items has changed
			refreshVisuals();
		
		} else if (getModel().getLocationMode() == LocationMode.VIA &&
				   owner == getModel().getViaSpecification().getSet() &&
				   isFeatureSet(features, SchemaPackage.eINSTANCE.getSet_Name())) {
			
			// the name of the the record's VIA set has changed
			refreshVisuals();			
			
		} else if (getModel().getLocationMode() == LocationMode.CALC &&
				   owner == getModel().getCalcKey() && 
				   isFeatureSet(features, SchemaPackage.eINSTANCE.getKey_DuplicatesOption())) {
			
			// the CALC key's duplicates option has changed
			refreshVisuals();			
		
		} else if (owner == getModel().getAreaSpecification().getArea() &&
				   isFeatureSet(features, SchemaPackage.eINSTANCE.getSchemaArea_Name())) {
			
			// the name of the area in which the record is stored has changed
			refreshVisuals();
			
		}
		
	}
	
	@Override
	protected void createEditPolicies() {
		RecordFigure figure = (RecordFigure) getFigure();
		
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, 
						  new RecordGraphicalNodeEditPolicy(getModel(),
								  							figure,
								  						    getViewer()));
		
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new RecordXYLayoutEditPolicy(getModel()));
		
	}
	
	@Override
	protected IFigure createFigure() {
		Figure figure = new RecordFigure();
		
		// add a tooltip containing the record's name...
        String adjustedRecordName;
        SchemaRecord record = getModel();
        if (record.getName().endsWith("_")) {
            StringBuilder p = new StringBuilder(record.getName());
            p.setLength(p.length() - 1);
            adjustedRecordName = p.toString();
        } else {
            adjustedRecordName = record.getName();
        }
        Label tooltip = new Label(adjustedRecordName);
        figure.setToolTip(tooltip);
		
		return figure;
	}
	
	@Override
	protected List<ConnectionPart> getModelSourceConnections() {
		List<ConnectionPart> connectionParts = new ArrayList<>();
		for (OwnerRole ownerRole : getModel().getOwnerRoles()) {
			for (MemberRole memberRole : ownerRole.getSet().getMembers()) { 				 
				ConnectionPart connectionPart = 
					memberRole.getConnectionParts().get(0);					
				connectionParts.add(connectionPart);				
			}
		}
		return connectionParts;
	}
	
	@Override
	protected List<ConnectionPart> getModelTargetConnections() {
		List<ConnectionPart> connectionParts = new ArrayList<>();
		for (MemberRole memberRole : getModel().getMemberRoles()) {
			int i = memberRole.getConnectionParts().size();
			ConnectionPart connectionPart = 
					memberRole.getConnectionParts().get(i - 1);					
			connectionParts.add(connectionPart);
		}
		return connectionParts;
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new LockedRecordSourceAnchor((RecordFigure) getFigure(), 
									   		(ConnectionPart) connection.getModel());
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {		
		if (!(request instanceof ReconnectRequest)) {
			return super.getSourceConnectionAnchor(request);
		}
		ReconnectRequest rRequest = (ReconnectRequest)request;						
		ConnectionPart connectionPart = 
			(ConnectionPart)rRequest.getConnectionEditPart().getModel();
		return new ReconnectEndpointAnchor((RecordFigure)getFigure(), 
										   rRequest.getLocation(), 
										   connectionPart);	
	}
	
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new LockedRecordTargetAnchor((RecordFigure) getFigure(), 
										    (ConnectionPart) connection.getModel());
	}
	
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		if (!(request instanceof ReconnectRequest)) {
			return super.getSourceConnectionAnchor(request);
		}
		ReconnectRequest rRequest = (ReconnectRequest)request;		
		ConnectionPart connectionPart = 
			(ConnectionPart)rRequest.getConnectionEditPart().getModel();
		return new ReconnectEndpointAnchor((RecordFigure)getFigure(), 
										   rRequest.getLocation(), 
										   connectionPart);	
	}
	
	@Override
	protected void refreshConnections() {
		for (OwnerRole ownerRole : getModel().getOwnerRoles()) {
			for (MemberRole memberRole : ownerRole.getSet().getMembers()) {
				for (ConnectionPart connectionPart : 
					 memberRole.getConnectionParts()) {
					
					GraphicalEditPart editPart = 
						(GraphicalEditPart) getViewer().getEditPartRegistry()
												  	   .get(connectionPart);
					editPart.refresh();
				}
			}
		}		
	}

	@Override
	protected void setFigureData() {
		SchemaRecord record = getModel();
		RecordFigure figure = (RecordFigure) getFigure();
		// we need to manipulate the record name in the case of some dictionary
		// records (DDLCATLOD area, which has the same structure as DDLDCLOD)...
		StringBuilder recordName = new StringBuilder(record.getName());
		if (record.getName().endsWith("_")) {
			recordName.setLength(recordName.length() - 1);
		}
		figure.setRecordName(recordName.toString());
		figure.setRecordId(record.getId());
		figure.setStorageMode(Tools.getStorageMode(record.getStorageMode()));
		int dataLength = record.getDataLength();
		if (record.getStorageMode() != StorageMode.FIXED) {
			dataLength -= 4; 
		}
		figure.setRecordLength(dataLength);
		figure.setLocationMode(record.getLocationMode().toString());
		if (record.getLocationMode() == LocationMode.CALC) {
			String calcKey = Tools.getCalcKey(record.getCalcKey());
			figure.setLocationModeDetails(calcKey);
			String duplicatesOption = 
				Tools.getDuplicatesOption(record.getCalcKey());
			figure.setDuplicatesOption(duplicatesOption);
		} else if (record.getLocationMode() == LocationMode.VIA) {
			String setName;
			// the via specification will be null if we are changing the VIA set
			if (record.getViaSpecification() != null) {
				if (record.getViaSpecification() != null &&
					record.getViaSpecification().getSet().getName()
														 .endsWith("_")) {
					
					// we need to manipulate the via set name in the case of 
					// some dictionary records (DDLCATLOD area, which has the  
					// same structure as DDLDCLOD)...
					setName = record.getViaSpecification()
									.getSet().getName()
									.substring(0, record.getViaSpecification()
														.getSet().getName()
														.length() - 1);
				} else {
					setName = record.getViaSpecification().getSet().getName();
				}
			} else {
				setName = "?";
			}
			figure.setLocationModeDetails(setName);
			figure.setDuplicatesOption("");
		} else {
			figure.setLocationModeDetails("");
			figure.setDuplicatesOption("");
		}
		figure.setAreaName(record.getAreaSpecification().getArea().getName());
	}	
	
}
