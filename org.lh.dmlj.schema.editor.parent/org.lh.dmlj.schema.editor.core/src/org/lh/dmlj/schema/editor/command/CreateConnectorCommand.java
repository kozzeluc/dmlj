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
package org.lh.dmlj.schema.editor.command;

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.ADD_ITEM;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;

@ModelChange(category=ADD_ITEM)
public class CreateConnectorCommand extends Command {
	
	static enum ConnectorLayout {HORIZONTAL, VERTICAL}
	
	@Owner 	   private MemberRole 	  memberRole;
	@Item	   private ConnectionPart connectionPart2;
	@Reference private EReference 	  reference = 
		SchemaPackage.eINSTANCE.getMemberRole_ConnectionParts();	
		
	private Connector[]    	  connector = new Connector[2];
	private DiagramLocation[] diagramLocation = new DiagramLocation[2];	
	protected Point 		  location; // absolute, unscaled
	private List<Point[]> 	  absoluteLineCoordinates;
	private int				  insertionIndex;
	
	protected ISupplier<MemberRole> memberRoleSupplier;
	
	// compute the absolute deltaX and deltaY; if deltaX is bigger than or equal deltaY, the layout
	// is horizontal, else vertical
	static ConnectorLayout getConnectorLayout(Point[] lineFragment) {
		
		Assert.isTrue(lineFragment.length == 2, "lineFragment.length != 2: " + lineFragment.length);
		
		Point begin = lineFragment[0];
		Point end = lineFragment[1];				
		int deltaX = begin.x > end.x ? begin.x - end.x : end.x - begin.x;
		int deltaY = begin.y > end.y ? begin.y - end.y : end.y - begin.y;
		
		return deltaX >= deltaY ? ConnectorLayout.HORIZONTAL : ConnectorLayout.VERTICAL;
		
	}

	/** 
	 * Calculates the <b>unscaled absolute location</b> for the first connector, given the line 
	 * fragment, connector layout and the point where the user clicked the mouse button; the first 
	 * connector will <em>always</em> appear to the top left of the mouse click location : 
	 *<ul>
	 * <li>In the case of a <b>horizontal</b> connector layout :  the <em>full</em> diameter of a 
	 *     connector to the left and <em>half</em> the diameter of a connector above the mouse click 
	 *     location.</li><br><br>
	 * <li>In the case of a <b>vertical</b> connector layout :	<em>half</em> the diameter of a 
	 *     connector to the left and the <em>full</em> diameter of a connector above of the mouse  
	 *     click location.</li>
	 * </ul>
	 * The connector location refers to the <b>top left point</b> where the connector will appear in 
	 * the diagram.<br><br>
	 * In the case of a <b>straight</b> horizontal or vertical line fragment (we accept a margin of   
	 * -2/+2), the x, resp. y value from the line fragment will <b>override</b> that value from the 
	 * mouse click location; this will keep the line fragment to the first connector perfectly 
	 * straight.
	 * <br><br>
	 * @param lineFragment the begin and end locations of the line fragment on which the connectors 
	 *        are to appear, expressed in unscaled absolute coordinates
	 * @param layout the connector layout
	 * @param mouseClickLocation the unscaled absolute mouse click location
	 * @return the unscaled absolute location for the first connector
	 */
	static Point getFirstConnectorLocation(Point[] lineFragment, ConnectorLayout layout, 
										   Point mouseClickLocation) {
		
		Point begin = lineFragment[0];
		Point end = lineFragment[1];				
		int deltaX = begin.x > end.x ? begin.x - end.x : end.x - begin.x;
		int deltaY = begin.y > end.y ? begin.y - end.y : end.y - begin.y;
		Point firstConnectorLocation = new Point();
		if (layout == ConnectorLayout.HORIZONTAL) {
			firstConnectorLocation.x = mouseClickLocation.x - 20;
			if (deltaY < 3) {
				// straight line (margin of -2/+2), override y with the line fragment's begin point
				firstConnectorLocation.y = begin.y - 10;
			} else {
				firstConnectorLocation.y = mouseClickLocation.y - 10;
			}
		} else {
			if (deltaX < 3) {
				// straight line (margin of -2/+2), override x with the line fragment's begin point
				firstConnectorLocation.x = begin.x - 10;
			} else {
				firstConnectorLocation.x = mouseClickLocation.x - 10;
			}			
			firstConnectorLocation.y = mouseClickLocation.y - 20;			
		}
		return firstConnectorLocation;
	}

	/** 
	 * Calculates the <b>unscaled absolute location</b> for the second connector, given the location
	 * of the first connector and the connector layout : 
	 * <ul> 
	 * <li>In the case of a <b>horizontal</b> connector layout :  immediately to the right of the 
	 *     first connector</li>
	 * <li>In the case of a <b>vertical</b> connector layout :  right beneath the first 
	 *     connector.</li>
	 * </ul>
	 * @param firstConnectorLocation the unscaled absolute location of the first connector
	 * @param layout the connector layout	 
	 * @return the unscaled absolute location for the second connector
	 */
	static Point getSecondConnectorLocation(Point firstConnectorLocation, ConnectorLayout layout) {
		Point secondConnectorLocation = 
			new Point(firstConnectorLocation.x, firstConnectorLocation.y);
		if (layout == ConnectorLayout.HORIZONTAL) {
			secondConnectorLocation.x += 20;						
		} else {
			secondConnectorLocation.y += 20;			
		}
		return secondConnectorLocation;		
	}

	static boolean isPositionSwitch(Point[] lineFragment, ConnectorLayout layout) {
		Point begin = lineFragment[0];
		Point end = lineFragment[1];
		return layout == ConnectorLayout.HORIZONTAL && begin.x > end.x ||
			   layout == ConnectorLayout.VERTICAL && begin.y > end.y;
	}

	public CreateConnectorCommand(MemberRole memberRole, Point location) {
		super("Add connectors to connection");
		this.memberRole = memberRole;
		this.location = location;		
	}
	
	public CreateConnectorCommand(ISupplier<MemberRole> memberRoleSupplier, Point location) {
		super("Add connectors to connection");
		this.memberRoleSupplier = memberRoleSupplier;
		this.location = location;		
	}
	
	@Override
	public void execute() {
		
		if (memberRoleSupplier != null) {
			// only when a member role supplier is available, obtain the member role from that 
			// supplier (and assume that the supplied member role is not null)
			memberRole = memberRoleSupplier.supply();
		}
		
		Assert.isTrue(memberRole.getConnectionParts().size() == 1);
		
		// calculate the insertion index
		insertionIndex = getInsertionIndex();
		
		// determine the target line fragment
		Point[] lineFragment = absoluteLineCoordinates.get(insertionIndex + 1);
		
		// determine the connector layout, i.e. whether we will place the connectors next to each 
		// other or one on top of the other							
		ConnectorLayout layout = getConnectorLayout(lineFragment);			
		
		// prepare the first connector's location
		Point firstConnectorLocation = getFirstConnectorLocation(lineFragment, layout, location);	
		diagramLocation[0] = SchemaFactory.eINSTANCE.createDiagramLocation();			
		diagramLocation[0].setX(firstConnectorLocation.x);
		diagramLocation[0].setY(firstConnectorLocation.y);
		diagramLocation[0].setEyecatcher("set connector[0] " + memberRole.getSet().getName() + 
				 						 " (" + memberRole.getRecord().getName() + ")");
		
		// prepare the second connector's location
		Point secondConnectorLocation = getSecondConnectorLocation(firstConnectorLocation, layout);		
		diagramLocation[1] = SchemaFactory.eINSTANCE.createDiagramLocation();
		diagramLocation[1].setX(secondConnectorLocation.x);
		diagramLocation[1].setY(secondConnectorLocation.y);
		diagramLocation[1].setEyecatcher("set connector[1] " + memberRole.getSet().getName() + 
										 " (" + memberRole.getRecord().getName() + ")");
		
		// switch the connector positions if needed
		if (isPositionSwitch(lineFragment, layout)) {
			if (layout == ConnectorLayout.HORIZONTAL) {
				diagramLocation[0].setX(secondConnectorLocation.x);
				diagramLocation[1].setX(firstConnectorLocation.x);
			} else {
				diagramLocation[0].setY(secondConnectorLocation.y);
				diagramLocation[1].setY(firstConnectorLocation.y);
			}
		}
		
		// create the first connector and set its location
		connector[0] = SchemaFactory.eINSTANCE.createConnector();
		connector[0].setDiagramLocation(diagramLocation[0]);
		
		// create the second connector and set its location
		connector[1] = SchemaFactory.eINSTANCE.createConnector();
		connector[1].setDiagramLocation(diagramLocation[1]);
		
		// create the second connection part, set its source and target endpoint locations, if any, 
		// and its connector
		connectionPart2 = SchemaFactory.eINSTANCE.createConnectionPart();
		connectionPart2.setSourceEndpointLocation(null);
		DiagramLocation targetEndpointLocation = 
			memberRole.getConnectionParts().get(0).getTargetEndpointLocation();
		connectionPart2.setTargetEndpointLocation(targetEndpointLocation);
		connectionPart2.setConnector(connector[1]);
		
		// go finish the job
		redo();
				
	}
	
	/**
	 * Assembles a list with the absolute coordinates of all the line fragments that make up the 
	 * first connection part.
	 * @return a list with the absolute coordinates of all the line fragments that make up the first 
	 * 		   connection part
	 */
	List<Point[]> getAbsoluteLineCoordinates() {
		
		// if we already assembled the line coordinates list, return it
		if (absoluteLineCoordinates != null) {
			return absoluteLineCoordinates;
		}
		
		// allocate the list...
		absoluteLineCoordinates = new ArrayList<>();				
		ConnectionPart firstConnectionPart = memberRole.getConnectionParts().get(0);		
		if (firstConnectionPart.getBendpointLocations().isEmpty()) {
			
			// no bendpoints; we should use the connection part's source and target endpoints, if
			// available, if not, we'll calculate the anchor points and use these			
			Point source = new Point();
			Point target = new Point();			
			if (memberRole.getSet().getOwner() != null) {
				// not a system owned indexed set: take the center of the record
				source.x = memberRole.getSet().getOwner().getRecord().getDiagramLocation().getX();
				source.y = memberRole.getSet().getOwner().getRecord().getDiagramLocation().getY();
				if (firstConnectionPart.getSourceEndpointLocation() != null) {
					source.x += firstConnectionPart.getSourceEndpointLocation().getX();
					source.y += firstConnectionPart.getSourceEndpointLocation().getY();
				} else {					
					source.x += 65;
					source.y += 26;
				}
			} else {
				// system owned indexed set: take the bottom center point
				source.x = 
					memberRole.getSet().getSystemOwner().getDiagramLocation().getX() + 11;
				source.y = 
					memberRole.getSet().getSystemOwner().getDiagramLocation().getY() + 22;			
			}		
			target.x = memberRole.getRecord().getDiagramLocation().getX();
			target.y = memberRole.getRecord().getDiagramLocation().getY();
			if (firstConnectionPart.getTargetEndpointLocation() != null) {
				target.x += firstConnectionPart.getTargetEndpointLocation().getX();
				target.y += firstConnectionPart.getTargetEndpointLocation().getY();
			} else {
				target.x += 65;				
				target.y += 26;
			}
			
			// add an array containing the source and target endpoints we've just calculated to the
			// list of coordinates; we're done
			absoluteLineCoordinates.add(new Point[] {source, target});			
			
		} else {
		
			// in the case 1 or more bendpoints are present, we ALWAYS expect a source- and target
			// endpoint
			Assert.isNotNull(firstConnectionPart.getSourceEndpointLocation());
			Assert.isNotNull(firstConnectionPart.getTargetEndpointLocation());
			
			// we need the owner's diagram location to calculate the absolute coordinates because  
			// all endpoints and the source bendpoint locations are stored as a relative offset to 
			// the set's owner
			DiagramLocation ownerLocation;
			if (memberRole.getSet().getOwner() != null) {
				// the owner is a record
				Assert.isNotNull(memberRole.getSet().getOwner().getRecord());
				ownerLocation = memberRole.getSet().getOwner().getRecord().getDiagramLocation();
			} else {
				// the owner is a system owner
				Assert.isNotNull(memberRole.getSet().getSystemOwner());
				ownerLocation = memberRole.getSet().getSystemOwner().getDiagramLocation();
			}
			
			// first line
			absoluteLineCoordinates.add(new Point[] {		
				new Point(ownerLocation.getX() + 
						  firstConnectionPart.getSourceEndpointLocation().getX(), 
						  ownerLocation.getY() + 
						  firstConnectionPart.getSourceEndpointLocation().getY()),
				new Point(ownerLocation.getX() + 
						  firstConnectionPart.getBendpointLocations().get(0).getX(), 
						  ownerLocation.getY() + 
						  firstConnectionPart.getBendpointLocations().get(0).getY()),
			}); 
					
			// intermediate line(s)
			for (int i = 1; i < firstConnectionPart.getBendpointLocations().size(); i++) {
				absoluteLineCoordinates.add(new Point[] {	
					new Point(ownerLocation.getX() + 
							  firstConnectionPart.getBendpointLocations().get(i - 1).getX(), 
							  ownerLocation.getY() + 
							  firstConnectionPart.getBendpointLocations().get(i - 1).getY()),
					new Point(ownerLocation.getX() + 
							  firstConnectionPart.getBendpointLocations().get(i).getX(), 
							  ownerLocation.getY() + 
							  firstConnectionPart.getBendpointLocations().get(i).getY()),
					
				});
			}
			
			// last line; the target endpoint location needs to be translated to an offset to the 
			// MEMBER record
			DiagramLocation memberLocation = memberRole.getRecord().getDiagramLocation();
			DiagramLocation targetEndpoint = firstConnectionPart.getTargetEndpointLocation();
			int lastBendpoint = firstConnectionPart.getBendpointLocations().size() - 1;
			absoluteLineCoordinates.add(new Point[] {		
				new Point(ownerLocation.getX() + 
						  firstConnectionPart.getBendpointLocations().get(lastBendpoint).getX(), 
						  ownerLocation.getY() + 
						  firstConnectionPart.getBendpointLocations().get(lastBendpoint).getY()),
				new Point(ownerLocation.getX() + 
						  memberLocation.getX() + targetEndpoint.getX() - ownerLocation.getX(), 
						  ownerLocation.getY() + 
						  memberLocation.getY() + targetEndpoint.getY() - ownerLocation.getY()),
				
			});
		
		}
		
		return absoluteLineCoordinates;
	}
	
	/**
	 * Calculates the index of the bendpoint after which the connectors are to be inserted, using 
	 * the absolute location of the user's mouse click.
	 * @return the index of the bendpoint after which the connectors are to be inserted or -1 if the
	 *         connectors are to be inserted on the first line segment
	 */
	int getInsertionIndex() {		
		
		// we need the absolute coordinates of all line fragments; if no coordinates are returned,
		// there are no bendpoints
		List<Point[]> lineCoordinates = getAbsoluteLineCoordinates();
		if (lineCoordinates.isEmpty()) {
			return -1;			
		}		
		
		// We're going to make a correction to all coordinates, including those where the user 
		// clicked the mouse button
		int corrX = 0;
		if (location.x < 0) {
			corrX = -location.x;
		}
		int corrY = 0;
		if (location.y < 0) {
			corrY = -location.y;
		}
		for (Point[] points : lineCoordinates) {
			if (points[0].x < -corrX) {
				corrX = -points[0].x;
			}
			if (points[0].y < -corrY) {
				corrY = -points[0].y;
			}
			if (points[1].x < -corrX) {
				corrX = -points[1].x;
			}
			if (points[1].y < -corrY) {
				corrY = -points[1].y;
			}
		}
		Point adjustedLocation = new Point(location.x, location.y);
		if (corrX > 0 || corrY > 0) {
			adjustedLocation.x += corrX;
			adjustedLocation.y += corrY;
			for (Point[] points : lineCoordinates) {
				points[0].x += -corrX;
				points[0].y += -corrY;
				points[1].x += -corrX;
				points[1].y += -corrY;				
			}
		}
		
		// calculate the insertion index by checking each line: if the point represented by
		// correctedLocation is on a line, return the index of that line minus 1
		int cX = adjustedLocation.x; 
		int cY = adjustedLocation.y;
		int lowestSurface = Integer.MAX_VALUE;
		int[] surfaces = new int[lineCoordinates.size()];
		for (int i = 0; i < lineCoordinates.size(); i++) {
			// calculate the surface of the triangle represented by the following 3 points :
			// - the line start coordinates (a)
			// - the line end coordinates	(b)
			// - the mouse click location	(c)
			Point[] points = lineCoordinates.get(i);					
			int aX = points[0].x; 
			int aY = points[0].y; 
			int bX = points[1].x; 
			int bY = points[1].y; 			
			surfaces[i] = Math.abs( aX * (bY - cY) + bX * (cY - aY) + cX * (aY - bY) ) / 2;
			if (surfaces[i] < lowestSurface) {
				lowestSurface = surfaces[i];
			}			
		}
		for (int i = 0; i < surfaces.length; i++) {
			if (surfaces[i] == lowestSurface) {
				return i - 1;
			}
		}
		
		throw new RuntimeException("logic error while calculating insertionIndex");
	}
	
	@Override
	public void redo() {
		
		Assert.isTrue(memberRole.getConnectionParts().size() == 1);
		
		Schema schema = memberRole.getSet().getSchema();
		DiagramData diagramData = schema.getDiagramData();
		ConnectionPart firstConnectionPart = memberRole.getConnectionParts().get(0);
		List<DiagramLocation> bendpoints = firstConnectionPart.getBendpointLocations();
		
		// first connection part and connector
		firstConnectionPart.setTargetEndpointLocation(null);
		firstConnectionPart.setConnector(connector[0]);		
		diagramData.getLocations().add(connector[0].getDiagramLocation());
		diagramData.getConnectors().add(connector[0]);
		
		// second connection part and connector
		memberRole.getConnectionParts().add(connectionPart2);		
		diagramData.getConnectionParts().add(connectionPart2);				
		diagramData.getLocations().add(connector[1].getDiagramLocation());
		diagramData.getConnectors().add(connector[1]);
		
		// move bendpoints from the first to the second connection part if needed
		List<DiagramLocation> toMove = new ArrayList<>();
		for (int i = insertionIndex + 1; i < bendpoints.size(); i++) {
			toMove.add(bendpoints.get(i));
		}
		for (DiagramLocation bendpoint : toMove) {
			bendpoints.remove(bendpoint);
			connectionPart2.getBendpointLocations().add(bendpoint);
		}
				
	}
	
	@Override
	public void undo() {
	
		Assert.isTrue(memberRole.getConnectionParts().size() == 2);
		
		Schema schema = memberRole.getSet().getSchema();
		DiagramData diagramData = schema.getDiagramData();
		ConnectionPart firstConnectionPart = memberRole.getConnectionParts().get(0);
		
		// first connection part and connector
		DiagramLocation targetEndpointLocation = 
			memberRole.getConnectionParts().get(1).getTargetEndpointLocation();
		firstConnectionPart.setTargetEndpointLocation(targetEndpointLocation);
		firstConnectionPart.setConnector(null);		
		diagramData.getLocations().remove(connector[0].getDiagramLocation());
		diagramData.getConnectors().remove(connector[0]);
		
		// second connection part and connector
		memberRole.getConnectionParts().remove(connectionPart2);
		diagramData.getConnectionParts().remove(connectionPart2);			
		diagramData.getLocations().remove(connector[1].getDiagramLocation());
		diagramData.getConnectors().remove(connector[1]);
		
		// move all bendpoints from the second to the first connection part if needed
		List<DiagramLocation> bendpoints = connectionPart2.getBendpointLocations();
		List<DiagramLocation> toMove = new ArrayList<>();
		for (int i = 0; i < bendpoints.size(); i++) {
			toMove.add(bendpoints.get(i));
		}
		for (DiagramLocation bendpoint : toMove) {
			bendpoints.remove(bendpoint);
			firstConnectionPart.getBendpointLocations().add(bendpoint);
		}
		
	}
}
