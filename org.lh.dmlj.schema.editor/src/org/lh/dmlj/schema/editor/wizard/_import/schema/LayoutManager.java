package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.Properties;

import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.figure.RecordFigure;
import org.lh.dmlj.schema.editor.importtool.AbstractRecordLayoutManager;


public class LayoutManager {
	
	private Properties					parms;
	private AbstractRecordLayoutManager recordLayoutManager;
	private Schema 						schema;	

	public LayoutManager(Schema schema,
						 AbstractRecordLayoutManager recordLayoutManager,
						 Properties parms) {
		super();
		this.schema = schema;
		this.recordLayoutManager = recordLayoutManager;
		this.parms = parms;
	}
	
	public void layout() {
		if (schema.getRecords().isEmpty()) {
			return;
		}				
		recordLayoutManager.layout(schema.getRecords(), parms);
		layoutConnectionLabelsAndSystemOwners(schema);		
	}
	
	private void layoutConnectionLabelsAndSystemOwners(Schema schema) {		
		for (SchemaRecord record : schema.getRecords()) {
			int i = 0;
			for (MemberRole memberRole : record.getMemberRoles()) {
	
				// deal with the set label...
				ConnectionLabel connectionLabel = 
					memberRole.getConnectionLabel();
				/*DiagramLocation location = 
					SchemaFactory.eINSTANCE.createDiagramLocation();
				schema.getDiagramData().getLocations().add(location);
				connectionLabel.setDiagramLocation(location);*/				
	
				// we don't provide a source- and targetAnchor nor any
				// bendpoints for the diagram connection here				
				int x = record.getDiagramLocation().getX();
				int y = record.getDiagramLocation().getY() - 25;
				setDiagramData(connectionLabel, x, y);
					
				SystemOwner systemOwner = memberRole.getSet().getSystemOwner();
				if (systemOwner != null) {
					x = record.getDiagramLocation().getX() + 25 * i - 11;
					y = record.getDiagramLocation().getY() - 
						RecordFigure.UNSCALED_HEIGHT + 5;
					setDiagramData(systemOwner, x, y);					
				}
				
			}
		}		
	}
	
	private void setDiagramData(ConnectionLabel connectionLabel, int x, int y) {
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
	
	private void setDiagramData(SystemOwner systemOwner, int x, int y) {
		DiagramLocation location = 
			SchemaFactory.eINSTANCE.createDiagramLocation();
		systemOwner.getSet()
				   .getSchema()
				   .getDiagramData()
				   .getLocations()
				   .add(location);
		systemOwner.setDiagramLocation(location);
		location.setX(x);
		location.setY(y);		
		location.setEyecatcher("system owner " + systemOwner.getSet().getName());
	}	
	
}