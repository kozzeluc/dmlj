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
package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaFactory;

public class CreateConnectorCommand extends ModelChangeBasicCommand {
	enum ConnectorLayout { HORIZONTAL, VERTICAL }
	
	protected MemberRole memberRole;
	private ConnectionPart connectionPart2;
		
	private Connector[] connector = new Connector[2];
	private DiagramLocation[] diagramLocation = new DiagramLocation[2];	
	protected final Point absoluteUnscaledLocation;
	private List<Point[]> absoluteLineCoordinates;
	private int	insertionIndex;
	
	protected Supplier<MemberRole> memberRoleSupplier;
	
	// compute the absolute deltaX and deltaY; if deltaX is bigger than or equal deltaY, the layout is horizontal,
	// else vertical
	static ConnectorLayout getConnectorLayout(Point[] lineFragment) {
		Assert.isTrue(lineFragment.length == 2, "lineFragment.length != 2: " + lineFragment.length);
		
		var begin = lineFragment[0];
		var end = lineFragment[1];				
		var deltaX = begin.x > end.x ? begin.x - end.x : end.x - begin.x;
		var deltaY = begin.y > end.y ? begin.y - end.y : end.y - begin.y;
		
		return deltaX >= deltaY ? ConnectorLayout.HORIZONTAL : ConnectorLayout.VERTICAL;
	}

	/** 
	 * Calculates the <b>unscaled absolute location</b> for the first connector, given the line fragment, connector
	 * layout and the point where the user clicked the mouse button; the first connector will <em>always</em>
	 * appear to the top left of the mouse click location : 
	 *<ul>
	 * <li>In the case of a <b>horizontal</b> connector layout :  the <em>full</em> diameter of a connector to the
	 *     left and <em>half</em> the diameter of a connector above the mouse click location.</li><br><br>
	 * <li>In the case of a <b>vertical</b> connector layout :	<em>half</em> the diameter of a connector to the
	 *     left and the <em>full</em> diameter of a connector above of the mouse click location.</li>
	 * </ul>
	 * The connector location refers to the <b>top left point</b> where the connector will appear in the diagram.
	 * <br><br>
	 * In the case of a <b>straight</b> horizontal or vertical line fragment (we accept a margin of -2/+2), the x,
	 * resp. y value from the line fragment will <b>override</b> that value from the mouse click location; this
	 * will keep the line fragment to the first connector perfectly straight.
	 * <br><br>
	 * @param lineFragment the begin and end locations of the line fragment on which the connectors are to appear,
	 * expressed in unscaled absolute coordinates
	 * @param layout the connector layout
	 * @param mouseClickLocation the unscaled absolute mouse click location
	 * @return the unscaled absolute location for the first connector
	 */
	static Point getFirstConnectorLocation(Point[] lineFragment, ConnectorLayout layout, Point mouseClickLocation) {
		var begin = lineFragment[0];
		var end = lineFragment[1];				
		var deltaX = begin.x > end.x ? begin.x - end.x : end.x - begin.x;
		var deltaY = begin.y > end.y ? begin.y - end.y : end.y - begin.y;
		var firstConnectorLocation = new Point();
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
	 * Calculates the <b>unscaled absolute location</b> for the second connector, given the location of the first
	 * connector and the connector layout: 
	 * <ul> 
	 * <li>In the case of a <b>horizontal</b> connector layout :  immediately to the right of the first connector</li>
	 * <li>In the case of a <b>vertical</b> connector layout :  right beneath the first connector.</li>
	 * </ul>
	 * @param firstConnectorLocation the unscaled absolute location of the first connector
	 * @param layout the connector layout	 
	 * @return the unscaled absolute location for the second connector
	 */
	static Point getSecondConnectorLocation(Point firstConnectorLocation, ConnectorLayout layout) {
		var secondConnectorLocation = new Point(firstConnectorLocation.x, firstConnectorLocation.y);
		if (layout == ConnectorLayout.HORIZONTAL) {
			secondConnectorLocation.x += 20;						
		} else {
			secondConnectorLocation.y += 20;			
		}
		return secondConnectorLocation;		
	}

	static boolean isPositionSwitch(Point[] lineFragment, ConnectorLayout layout) {
		var begin = lineFragment[0];
		var end = lineFragment[1];
		return layout == ConnectorLayout.HORIZONTAL && begin.x > end.x || layout == ConnectorLayout.VERTICAL && begin.y > end.y;
	}

	public CreateConnectorCommand(MemberRole memberRole, Point location) {
		super("Add connectors to connection");
		this.memberRole = memberRole;
		this.absoluteUnscaledLocation = location;	
	}
	
	public CreateConnectorCommand(Supplier<MemberRole> memberRoleSupplier, Point location) {
		super("Add connectors to connection");
		this.memberRoleSupplier = memberRoleSupplier;
		this.absoluteUnscaledLocation = location;		
	}
	
	@Override
	public void execute() {
		if (memberRoleSupplier != null) {
			// only when a member role supplier is available, obtain the member role from that supplier (and
			// assume that the supplied member role is not null)
			memberRole = memberRoleSupplier.get();
		}
		
		Assert.isTrue(memberRole.getConnectionParts().size() == 1);
		
		// calculate the insertion index
		insertionIndex = getInsertionIndex();
		
		// determine the target line fragment
		var lineFragment = absoluteLineCoordinates.get(insertionIndex + 1);
		
		// determine the connector layout, i.e. whether we will place the connectors next to each other or one on
		// top of the other							
		var layout = getConnectorLayout(lineFragment);			
		
		// prepare the first connector's location
		var firstConnectorLocation = getFirstConnectorLocation(lineFragment, layout, absoluteUnscaledLocation);	
		diagramLocation[0] = SchemaFactory.eINSTANCE.createDiagramLocation();			
		diagramLocation[0].setX(firstConnectorLocation.x);
		diagramLocation[0].setY(firstConnectorLocation.y);
		diagramLocation[0].setEyecatcher("set connector[0] " + memberRole.getSet().getName() + " (" + memberRole.getRecord().getName() + ")");
		
		// prepare the second connector's location
		var secondConnectorLocation = getSecondConnectorLocation(firstConnectorLocation, layout);		
		diagramLocation[1] = SchemaFactory.eINSTANCE.createDiagramLocation();
		diagramLocation[1].setX(secondConnectorLocation.x);
		diagramLocation[1].setY(secondConnectorLocation.y);
		diagramLocation[1].setEyecatcher("set connector[1] " + memberRole.getSet().getName() + " (" + memberRole.getRecord().getName() + ")");
		
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
		
		// create the second connection part, set its source and target endpoint locations, if any, and its connector
		connectionPart2 = SchemaFactory.eINSTANCE.createConnectionPart();
		connectionPart2.setSourceEndpointLocation(null);
		var targetEndpointLocation = memberRole.getConnectionParts().get(0).getTargetEndpointLocation();
		connectionPart2.setTargetEndpointLocation(targetEndpointLocation);
		connectionPart2.setConnector(connector[1]);
		
		// go finish the job
		redo();
	}
	
	/**
	 * Assembles a list with the absolute coordinates of all the line fragments that make up the first connection part.
	 * @return a list with the absolute coordinates of all the line fragments that make up the first connection part
	 */
	List<Point[]> getAbsoluteLineCoordinates() {
		if (absoluteLineCoordinates == null) {
			if (memberRole.getConnectionParts().get(0).getBendpointLocations().isEmpty()) {
				absoluteLineCoordinates = getAbsoluteLineCoordinatesWhenNoBendpointsArePresent();			
			} else {	
				absoluteLineCoordinates = getAbsoluteLineCoordinatesWhenBendpointsArePresent();
			}			
		}
		return absoluteLineCoordinates;
	}
	
	private List<Point[]> getAbsoluteLineCoordinatesWhenNoBendpointsArePresent() {
		// use the connection part's source and target endpoints, if available, and if not, calculate the anchor
		// points and use these
		var firstConnectionPart = memberRole.getConnectionParts().get(0);	
		var source = new Point();
		var target = new Point();			
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
		} else if (memberRole.getSet().getSystemOwner() != null) {
			// system owned indexed set: take the bottom center point
			source.x = memberRole.getSet().getSystemOwner().getDiagramLocation().getX() + 11;
			source.y = memberRole.getSet().getSystemOwner().getDiagramLocation().getY() + 22;			
		} else {
			// VSAM index: take the bottom center point
			source.x = memberRole.getSet().getVsamIndex().getDiagramLocation().getX() + 11;
			source.y = memberRole.getSet().getVsamIndex().getDiagramLocation().getY() + 22;
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
				
		var resultingCoordinates = new ArrayList<Point[]>();
		resultingCoordinates.add(new Point[] { source, target });
		return resultingCoordinates;
	}
	
	private List<Point[]> getAbsoluteLineCoordinatesWhenBendpointsArePresent() {
		// we ALWAYS expect a source- and target endpoint to be present						
		var firstConnectionPart = memberRole.getConnectionParts().get(0);	
		Assert.isNotNull(firstConnectionPart.getSourceEndpointLocation());
		Assert.isNotNull(firstConnectionPart.getTargetEndpointLocation());
				
		// we need the owner's diagram location to calculate the absolute coordinates because all endpoints
		// and the source bendpoint locations are stored as a relative offset to the set's owner
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
		var resultingCoordinates = new ArrayList<Point[]>();
		resultingCoordinates.add(new Point[] {		
			new Point(ownerLocation.getX() + firstConnectionPart.getSourceEndpointLocation().getX(),
					ownerLocation.getY() + firstConnectionPart.getSourceEndpointLocation().getY()),
			new Point(ownerLocation.getX() + firstConnectionPart.getBendpointLocations().get(0).getX(),
					ownerLocation.getY() + firstConnectionPart.getBendpointLocations().get(0).getY())
		}); 
				
		// intermediate line(s)
		for (var i = 1; i < firstConnectionPart.getBendpointLocations().size(); i++) {
			resultingCoordinates.add(new Point[] {	
				new Point(ownerLocation.getX() + firstConnectionPart.getBendpointLocations().get(i - 1).getX(),
						ownerLocation.getY() +  firstConnectionPart.getBendpointLocations().get(i - 1).getY()),
				new Point(ownerLocation.getX() + firstConnectionPart.getBendpointLocations().get(i).getX(),
						ownerLocation.getY() + firstConnectionPart.getBendpointLocations().get(i).getY())
			});
		}
		
		// last line; the target endpoint location needs to be translated to an offset to the MEMBER record
		var memberLocation = memberRole.getRecord().getDiagramLocation();
		var targetEndpoint = firstConnectionPart.getTargetEndpointLocation();
		var lastBendpoint = firstConnectionPart.getBendpointLocations().size() - 1;
		resultingCoordinates.add(new Point[] {		
			new Point(ownerLocation.getX() + firstConnectionPart.getBendpointLocations().get(lastBendpoint).getX(),
					ownerLocation.getY() + firstConnectionPart.getBendpointLocations().get(lastBendpoint).getY()),
			new Point(ownerLocation.getX() + memberLocation.getX() + targetEndpoint.getX() - ownerLocation.getX(),
					ownerLocation.getY() + memberLocation.getY() + targetEndpoint.getY() - ownerLocation.getY())
		});
		return resultingCoordinates;
	}
	
	/**
	 * Calculates the index of the bendpoint after which the connectors are to be inserted, using the absolute
	 * location of the user's mouse click.
	 * @return the index of the bendpoint after which the connectors are to be inserted or -1 if the connectors
	 *         are to be inserted on the first line segment
	 */
	int getInsertionIndex() {
		// we need the absolute coordinates of all line fragments; if no coordinates are returned, there are no bendpoints
		var lineCoordinates = getAbsoluteLineCoordinates();
		if (lineCoordinates.isEmpty()) {
			return -1;			
		} else {
			var adjustedLocation = correctAllCoordinates(lineCoordinates);
			return calculateInsertionIndex(adjustedLocation, lineCoordinates);
		}
	}
	
	private Point correctAllCoordinates(List<Point[]> lineCoordinates) {
		// We're going to make a correction to all coordinates, including those where the user clicked the mouse button
		var corrX = 0;
		if (absoluteUnscaledLocation.x < 0) {
			corrX = -absoluteUnscaledLocation.x;
		}
		var corrY = 0;
		if (absoluteUnscaledLocation.y < 0) {
			corrY = -absoluteUnscaledLocation.y;
		}
		for (var points : lineCoordinates) {
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
		var adjustedLocation = new Point(absoluteUnscaledLocation.x, absoluteUnscaledLocation.y);
		if (corrX > 0 || corrY > 0) {
			adjustedLocation.x += corrX;
			adjustedLocation.y += corrY;
			for (var points : lineCoordinates) {
				points[0].x += -corrX;
				points[0].y += -corrY;
				points[1].x += -corrX;
				points[1].y += -corrY;				
			}
		}
		return adjustedLocation;
	}
	
	private int calculateInsertionIndex(Point adjustedLocation, List<Point[]> lineCoordinates) {
		// calculate the insertion index by checking each line: if the point represented by correctedLocation is
		// on a line, return the index of that line minus 1
		var cX = adjustedLocation.x; 
		var cY = adjustedLocation.y;
		var lowestSurface = Integer.MAX_VALUE;
		var surfaces = new int[lineCoordinates.size()];
		for (var i = 0; i < lineCoordinates.size(); i++) {
			// calculate the surface of the triangle represented by the following 3 points:
			// - the line start coordinates (a)
			// - the line end coordinates   (b)
			// - the mouse click location   (c)
			var points = lineCoordinates.get(i);					
			var aX = points[0].x; 
			var aY = points[0].y; 
			var bX = points[1].x; 
			var bY = points[1].y; 			
			surfaces[i] = Math.abs( aX * (bY - cY) + bX * (cY - aY) + cX * (aY - bY) ) / 2;
			if (surfaces[i] < lowestSurface) {
				lowestSurface = surfaces[i];
			}			
		}
		for (var i = 0; i < surfaces.length; i++) {
			if (surfaces[i] == lowestSurface) {
				return i - 1;
			}
		}
		throw new IllegalStateException("logic error while calculating insertionIndex");
	}
	
	@Override
	public void redo() {
		Assert.isTrue(memberRole.getConnectionParts().size() == 1);
		
		var schema = memberRole.getSet().getSchema();
		var diagramData = schema.getDiagramData();
		var firstConnectionPart = memberRole.getConnectionParts().get(0);
		var bendpoints = firstConnectionPart.getBendpointLocations();
		
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
		var toMove = new ArrayList<DiagramLocation>();
		for (var i = insertionIndex + 1; i < bendpoints.size(); i++) {
			toMove.add(bendpoints.get(i));
		}
		for (var bendpoint : toMove) {
			bendpoints.remove(bendpoint);
			connectionPart2.getBendpointLocations().add(bendpoint);
		}
	}
	
	@Override
	public void undo() {
		Assert.isTrue(memberRole.getConnectionParts().size() == 2);
		
		var schema = memberRole.getSet().getSchema();
		var diagramData = schema.getDiagramData();
		var firstConnectionPart = memberRole.getConnectionParts().get(0);
		
		// first connection part and connector
		var targetEndpointLocation = memberRole.getConnectionParts().get(1).getTargetEndpointLocation();
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
		var bendpoints = connectionPart2.getBendpointLocations();
		var toMove = new ArrayList<DiagramLocation>();
		for (var i = 0; i < bendpoints.size(); i++) {
			toMove.add(bendpoints.get(i));
		}
		for (var bendpoint : toMove) {
			bendpoints.remove(bendpoint);
			firstConnectionPart.getBendpointLocations().add(bendpoint);
		}
		
	}
}
