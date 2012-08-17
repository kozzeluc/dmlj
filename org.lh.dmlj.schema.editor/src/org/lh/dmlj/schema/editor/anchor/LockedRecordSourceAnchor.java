package org.lh.dmlj.schema.editor.anchor;

import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

/**
 * An anchor that will locate the source (owner) connection endpoint for a 
 * record as follows :
 * <ul>
 * <li>If the sourceEndpointLocation attribute of the connection's 
 *     ConnectionPart model object is set, these offsets to the record figure 
 *     location (stored at a scale of 1) are used to calculate the location of 
 *     the connection endpoint.<br><br></li>
 * <li>If the sourceEndpointLocation attribute of the connection's 
 *     ConnectionPart model object is NOT set, the location returned by a 
 *     standard chopbox anchor for the record figure will be used and locked 
 *     (i.e. the same value will be returned until the sourceEndpointLocation 
 *     attribute of the connection's ConnectionPart model object is set).</li>
 * </ul>
 */
public class LockedRecordSourceAnchor extends AbstractLockedRecordAnchor {

	/**
	 * Constructs a LockedRecordSourceAnchor with the given record figure.
	 * @param figure The source (owner) record figure
	 * @param connectionPart The ConnectionPart model object representing the 
	 *        connection
	 */
	public LockedRecordSourceAnchor(RecordFigure figure, 
									ConnectionPart connectionPart) {
		super(figure, connectionPart);
	}
	
	@Override
	protected DiagramLocation getModelEndpoint() {		
		return connectionPart.getSourceEndpointLocation();
	}	
	
}