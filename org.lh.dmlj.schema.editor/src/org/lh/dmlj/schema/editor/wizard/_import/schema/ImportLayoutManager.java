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


public class ImportLayoutManager implements ILayoutManager {
	
	private Properties					configuredParms;
	private AbstractRecordLayoutManager recordLayoutManager;
	private Schema 						schema;	
	private Properties					userParms;

	public ImportLayoutManager(Schema schema,
						 AbstractRecordLayoutManager recordLayoutManager,
						 Properties configuredParms, Properties userParms) {
		super();
		this.schema = schema;
		this.recordLayoutManager = recordLayoutManager;
		this.configuredParms = configuredParms;
		this.userParms = userParms;
	}
	
	@Override
	public Schema getReferenceSchema() {
		return null;
	}
	
	public void layout() {
		if (schema.getRecords().isEmpty()) {
			return;
		}				
		recordLayoutManager.layout(schema.getRecords(), configuredParms,
								   userParms);
		layoutConnectionLabelsAndSystemOwners(schema);		
	}
	
	private void layoutConnectionLabelsAndSystemOwners(Schema schema) {		
		for (SchemaRecord record : schema.getRecords()) {
			int i = 0;
			for (MemberRole memberRole : record.getMemberRoles()) {
	
				// deal with the set label...
				ConnectionLabel connectionLabel = 
					memberRole.getConnectionLabel();
	
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
					// next index will be located to the right of this one:
					i += 1;
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