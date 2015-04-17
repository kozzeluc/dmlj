/**
 * Copyright (C) 2015  Luc Hermans
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

import java.util.Properties;

import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
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
		layoutConnectionLabels(schema);
		layoutSystemOwners(schema);
		layoutVsamIndexes(schema);
	}
	
	private void layoutConnectionLabels(Schema schema2) {
		for (SchemaRecord record : schema.getRecords()) {
			for (MemberRole memberRole : record.getMemberRoles()) {
				ConnectionLabel connectionLabel = memberRole.getConnectionLabel();
				int x = record.getDiagramLocation().getX();
				int y = record.getDiagramLocation().getY() - 25;
				setDiagramData(connectionLabel, x, y);
			}
		}		
	}

	private void layoutSystemOwners(Schema schema2) {
		for (SchemaRecord record : schema.getRecords()) {
			int i = 0;
			for (MemberRole memberRole : record.getMemberRoles()) {
				SystemOwner systemOwner = memberRole.getSet().getSystemOwner();
				if (systemOwner != null) {
					int x = record.getDiagramLocation().getX() + 25 * i - 11;
					int y = record.getDiagramLocation().getY() - RecordFigure.UNSCALED_HEIGHT + 5;
					setDiagramData(systemOwner, x, y);
					// next index will be located to the right of this one:
					i += 1;
				}
			}
		}
	}

	private void layoutVsamIndexes(Schema schema2) {
		for (SchemaRecord record : schema.getRecords()) {
			int i = 0;
			for (MemberRole memberRole : record.getMemberRoles()) {
				VsamIndex vsamIndex = memberRole.getSet().getVsamIndex();
				if (vsamIndex != null) {
					int x = record.getDiagramLocation().getX() + 25 * i - 11;
					int y = record.getDiagramLocation().getY() - RecordFigure.UNSCALED_HEIGHT + 5;
					setDiagramData(vsamIndex, x, y);
					// next VSAM index will be located to the right of this one:
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
	
	private void setDiagramData(VsamIndex vsamIndex, int x, int y) {
		DiagramLocation location = SchemaFactory.eINSTANCE.createDiagramLocation();
		vsamIndex.getSet().getSchema().getDiagramData().getLocations().add(location);
		vsamIndex.setDiagramLocation(location);
		location.setX(x);
		location.setY(y);		
		location.setEyecatcher("VSAM index " + vsamIndex.getSet().getName());
	}
	
}
