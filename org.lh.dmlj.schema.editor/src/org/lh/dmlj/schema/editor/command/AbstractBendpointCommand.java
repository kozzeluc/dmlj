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
		String p;
		if (memberRole.getSet().getSystemOwner() != null) {
			p = "system owner";
		} else {
			p = memberRole.getRecord().getName();
		}
		bendpoint.setEyecatcher("bendpoint set " + 
								memberRole.getSet().getName() + "(" + p + ")");
		
		// at it to the schema (container)...
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getLocations()
		  		  .add(bendpoint);

		// insert it at the right place in the connection...
		memberRole.getDiagramBendpoints().add(index, bendpoint);
		
		return bendpoint;
		
	}
	
	/**
	 * Removes a bendpoint from the MemberRole (which represents the connection) 
	 * and the schema (container).  Upon return, oldX and oldY will contain the 
	 * removed bendpoint's x and y attributes.
	 * @param index the index of the bendpoint to remove
	 */
	protected void removeBendpoint(int index) {
		
		// go grab the bendpoint and save the x and y attributes...
		DiagramLocation bendpoint = 
			memberRole.getDiagramBendpoints().get(index);
		oldX = bendpoint.getX();
		oldY = bendpoint.getY();
			
		// remove it from the schema (container)...
		memberRole.getSet()
				  .getSchema()
				  .getDiagramData()
				  .getLocations()
				  .remove(bendpoint);
		
		// remove it from the connection...
		memberRole.getDiagramBendpoints().remove(index);
	}
	
}