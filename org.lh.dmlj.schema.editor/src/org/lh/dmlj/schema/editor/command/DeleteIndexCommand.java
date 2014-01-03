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
package org.lh.dmlj.schema.editor.command;

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.REMOVE_ITEM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;

@ModelChange(category=REMOVE_ITEM)
public class DeleteIndexCommand extends Command {	
	
	@Owner	   private Schema 	  schema;
	@Reference private EReference reference = SchemaPackage.eINSTANCE.getSchema_Sets();
	@Item 	   private Set 		  set;
	
	private SchemaArea	 		  area;
	private DiagramData  		  diagramData;
	private Key 		 		  key;
	private MemberRole 	 		  memberRole;		
	private List<Procedure>		  obsoleteProcedures = new ArrayList<>();
	private List<String>		  procedureNames = new ArrayList<>();
	private SchemaRecord 		  record;
	private SystemOwner	 		  systemOwner;
	
	private int 				  areaInSchemaIndex;
	private int 				  areaSpecificationIndex;
	private int 				  systemOwnerDiagramLocationIndex;
	private int 				  connectionLabelLocationIndex;
	private int 				  connectionLabelIndex;
	private int 				  connectionPartIndex;
	private int 				  memberRoleIndex;	
	private Map<String, Integer>  procedureInSchemaIndexes = new HashMap<>();
	private int 				  setInSchemaIndex;
	
	
	public DeleteIndexCommand(SystemOwner systemOwner) {
		super("Delete index");
		this.systemOwner = systemOwner;
	}		

	@Override
	public void execute() {
		
		schema = systemOwner.getSet().getSchema();
					
		set = systemOwner.getSet();
		memberRole = systemOwner.getSet().getMembers().get(0);
		record = memberRole.getRecord();
		diagramData = schema.getDiagramData();
		area = systemOwner.getAreaSpecification().getArea();
		key = memberRole.getSortKey();
			
		// gather all the different (re-)insertion indexes
		areaInSchemaIndex = schema.getAreas().indexOf(area);
		areaSpecificationIndex = 
			area.getAreaSpecifications().indexOf(systemOwner.getAreaSpecification());
		systemOwnerDiagramLocationIndex = 
			diagramData.getLocations().indexOf(set.getSystemOwner().getDiagramLocation());
		connectionLabelLocationIndex = 
			diagramData.getLocations().indexOf(memberRole.getConnectionLabel().getDiagramLocation());
		connectionLabelIndex = diagramData.getConnectionLabels().indexOf(memberRole.getConnectionLabel());
		connectionPartIndex = 
			diagramData.getConnectionParts().indexOf(memberRole.getConnectionParts().get(0));
		memberRoleIndex = record.getMemberRoles().indexOf(memberRole);		
		setInSchemaIndex = schema.getSets().indexOf(set);
		
		// procedure related stuff:
		for (AreaProcedureCallSpecification callSpec : area.getProcedures()) {
			Procedure procedure = callSpec.getProcedure();
			// keep track of the procedure's name
			procedureNames.add(procedure.getName());			
			// see if the procedure will become obsolete after removing the index
			boolean obsolete = true;
			for (ProcedureCallSpecification aCallSpec : procedure.getCallSpecifications()) {
				if (aCallSpec instanceof RecordProcedureCallSpecification ||
					((AreaProcedureCallSpecification) aCallSpec).getArea() != area) {
					
					// the procedure is called for a record, for another area, or both and is NOT
					// obsolete
					obsolete = false;
					break;
				}
			}
			if (obsolete) {
				int i = schema.getProcedures().indexOf(procedure);
				procedureInSchemaIndexes.put(procedure.getName(), Integer.valueOf(i));
				obsoleteProcedures.add(procedure);
			}
		}
		// make sure the obsolete procedures get added again in the right order
		Collections.sort(obsoleteProcedures, new Comparator<Procedure>() {
			@Override
			public int compare(Procedure procedure1, Procedure procedure2) {
				int i = procedureInSchemaIndexes.get(procedure1.getName()).intValue();
				int j = procedureInSchemaIndexes.get(procedure2.getName()).intValue();				
				return i - j;
			}			
		});
		
		redo();
		
	}
	
	@Override
	public void redo() {				
		
		// remove the system owner's area specification from the area; if the area has become 
		// obsolete, remove it and, together with it, any obsolete procedures
		area.getAreaSpecifications().remove(systemOwner.getAreaSpecification());
		if (area.getAreaSpecifications().isEmpty()) {
			// the area is obsolete, so remove it from the schema
			schema.getAreas().remove(area);
			// nullify the reference to the procedure for all of the obsolete area's procedure call 
			// specifications
			for (AreaProcedureCallSpecification callSpec : area.getProcedures()) {
				callSpec.setProcedure(null);
			}
			// remove any obsolete procedures
			for (String procedureName : procedureInSchemaIndexes.keySet()) {
				Procedure procedure = schema.getProcedure(procedureName);
				procedure.setSchema(null);
			}
		}
		
		// remove the diagram locations of both the system owner and the connection label
		diagramData.getLocations().remove(set.getSystemOwner().getDiagramLocation());
		diagramData.getLocations().remove(memberRole.getConnectionLabel().getDiagramLocation());
		
		// remove the connection label
		diagramData.getConnectionLabels().remove(memberRole.getConnectionLabel());
		
		// remove the (one and only) connection part
		diagramData.getConnectionParts().remove(memberRole.getConnectionParts().get(0));		
		
		// remove the member role and its key, if any
		record.getMemberRoles().remove(memberRole);		
		if (key != null) {
			key.setMemberRole(null);
		}
		
		// remove the set from the schema
		schema.getSets().remove(set);
		
	}

	@Override
	public void undo() {		
				
		// restore the area if it was obsolete
		if (area.getSchema() == null) {
			schema.getAreas().add(areaInSchemaIndex, area);
		}
		
		// restore any obsolete procedures		
		for (Procedure procedure : obsoleteProcedures) {
			int i = procedureInSchemaIndexes.get(procedure.getName()).intValue();
			schema.getProcedures().add(i, procedure);
		}
		
		// restore the references to the called procedures
		for (int i = 0; i < area.getProcedures().size(); i++) {
			AreaProcedureCallSpecification callSpec = area.getProcedures().get(i);
			Procedure procedure = schema.getProcedure(procedureNames.get(i));
			callSpec.setProcedure(procedure);			
		}
		
		// restore the system owner's area specification within the area
		area.getAreaSpecifications().add(areaSpecificationIndex, 
										 systemOwner.getAreaSpecification()); 
		
		// restore the diagram location for both the connection label and system owner
		if (connectionLabelLocationIndex < systemOwnerDiagramLocationIndex) {
			diagramData.getLocations().add(connectionLabelLocationIndex, 
					   					   memberRole.getConnectionLabel().getDiagramLocation());
			diagramData.getLocations().add(systemOwnerDiagramLocationIndex, 
					   					   set.getSystemOwner().getDiagramLocation()); 			
		} else {
			diagramData.getLocations().add(systemOwnerDiagramLocationIndex, 
					   					   set.getSystemOwner().getDiagramLocation()); 
			diagramData.getLocations().add(connectionLabelLocationIndex, 
					   					   memberRole.getConnectionLabel().getDiagramLocation());
		}
		
		// restore the connection label
		diagramData.getConnectionLabels().add(connectionLabelIndex, 
											  memberRole.getConnectionLabel());
		
		// restore the (one and only) connection part
		diagramData.getConnectionParts().add(connectionPartIndex, 
											 memberRole.getConnectionParts().get(0));
		
		// restore the member role and its key, if applicable
		record.getMemberRoles().add(memberRoleIndex, memberRole);
		if (key != null) {
			key.setMemberRole(memberRole);
		}
		
		//add the set to the schema again
		schema.getSets().add(setInSchemaIndex, set);		
		
	}
	
}