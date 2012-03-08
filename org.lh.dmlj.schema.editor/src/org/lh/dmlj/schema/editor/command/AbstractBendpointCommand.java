package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaFactory;

public abstract class AbstractBendpointCommand extends Command {

	protected int 		 index;
	protected MemberRole memberRole;
	protected int		 oldX;
	protected int		 oldY;
	protected int		 x;
	protected int		 y;
	
	public AbstractBendpointCommand(MemberRole memberRole, int index) {
		super();
		this.memberRole = memberRole; 
		this.index = index;
	}
	
	public AbstractBendpointCommand(MemberRole memberRole, int index, int x, 
									int y) {
		super();
		this.memberRole = memberRole; 
		this.index = index;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates a new bendpoint and inserts it at the right index in the 
	 * MemberRole (which represents the connection) and as the last element of 
	 * the schema container's diagram data.
	 * @param index the index at which the bendpoints needs to be inserted
	 * @param x the bendpoint's (absolute) x coordinate
	 * @param y the bendpoint's (absolute) y coordinate
	 * @return the newly created and inserted bendpoint
	 */
	protected DiagramLocation insertBendpoint(int index, int x, int y) {
	
		// create the bendpoint...
		DiagramLocation bendpoint = 
			SchemaFactory.eINSTANCE.createDiagramLocation();
		bendpoint.setX(x);
		bendpoint.setY(y);		
		bendpoint.setEyecatcher("bendpoint [" + index + "] set " + 
								memberRole.getSet().getName() + " (" + 
								memberRole.getRecord().getName() + ")");
		
		// add it to the schema (container)...
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getLocations()
		  		  .add(bendpoint);

		// insert it at the right place in the connection (we currently don't
		// support split connections, i.e. only 1 connection part per set, not 
		// 2)...
		memberRole.getConnectionParts()
				  .get(0)
				  .getBendpointLocations()
				  .add(index, bendpoint);
		
		// modify the eyecatcher of subsequent bendpoints, if any...
		int j = 
			memberRole.getConnectionParts().get(0).getBendpointLocations().size();
		for (int i = index + 1; i < j; i++) {
			DiagramLocation aBendpoint = memberRole.getConnectionParts()
												   .get(0)
												   .getBendpointLocations()
												   .get(i);
			aBendpoint.setEyecatcher("bendpoint [" + i + "] set " + 
									 memberRole.getSet().getName() + " (" + 
									 memberRole.getRecord().getName() + ")");
		}
		
		return bendpoint;
		
	}
	
	/**
	 * Removes a bendpoint from the first Connection in the MemberRole (which 
	 * represents the connection; we don't support split connections for a set) 
	 * and the schema (container).  Upon return, oldX and oldY will contain the 
	 * removed bendpoint's x and y attributes.
	 * @param index the index of the bendpoint to remove
	 */
	protected void removeBendpoint(int index) {
		
		// go grab the bendpoint and save the x and y attributes...
		DiagramLocation bendpoint = memberRole.getConnectionParts()
					  						  .get(0)
					  						  .getBendpointLocations()
					  						  .get(index);
		oldX = bendpoint.getX();
		oldY = bendpoint.getY();
			
		// remove it from the schema (container)...
		memberRole.getSet()
				  .getSchema()
				  .getDiagramData()
				  .getLocations()
				  .remove(bendpoint);
		
		// remove it from the MemberRole's first connection...
		memberRole.getConnectionParts()
				  .get(0)
				  .getBendpointLocations()
				  .remove(index);
		
		// modify the eyecatcher of subsequent bendpoints, if any...
		int j = 
			memberRole.getConnectionParts().get(0).getBendpointLocations().size();
		for (int i = index; i < j; i++) {
			DiagramLocation aBendpoint = memberRole.getConnectionParts()
												   .get(0)
												   .getBendpointLocations()
												   .get(i);
			aBendpoint.setEyecatcher("bendpoint [" + i + "] set " + 
									 memberRole.getSet().getName() + " (" + 
									 memberRole.getRecord().getName() + ")");
		}
	}
	
}