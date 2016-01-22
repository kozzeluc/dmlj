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
package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;

public abstract  class DeleteRecordCommandCreationAssistant {

	protected static boolean canDeleteRecord(SchemaRecord record) {
		// for now, only allow a record to be deleted when it does NOT participate in any set or index
		return record.getOwnerRoles().isEmpty() && record.getMemberRoles().isEmpty();
	}
	
	public static IModelChangeCommand getCommand(SchemaRecord record) {
		
		if (!canDeleteRecord(record)) {
			return null;
		}
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.DELETE_RECORD);
		context.putContextData(record);
		
		List<IModelChangeCommand> commands = new ArrayList<>();
		
		for (RecordProcedureCallSpecification callSpec : record.getProcedures()) {
			IModelChangeCommand removeCallSpecCommand =
				new RemoveRecordProcedureCallSpecificationCommand(callSpec);
			commands.add(removeCallSpecCommand);
		}
		
		DeleteRecordCommand deleteRecordCommand = new DeleteRecordCommand(record);
		commands.add(deleteRecordCommand);
		
		SchemaArea area = record.getAreaSpecification().getArea(); 
		boolean areaIsObsolete = area.getRecords().size() == 1;
		if (areaIsObsolete) {
			
			for (AreaProcedureCallSpecification callSpec : area.getProcedures()) {
				IModelChangeCommand removeCallSpecCommand =
					new RemoveAreaProcedureCallSpecificationCommand(callSpec);
				commands.add(removeCallSpecCommand);
			}
			
			IModelChangeCommand deleteAreaCommand = new DeleteAreaCommand(area);
			commands.add(deleteAreaCommand);
		}
		
		Set<Procedure> procedures = new HashSet<>();
		for (ProcedureCallSpecification callSpec : record.getProcedures()) {
			procedures.add(callSpec.getProcedure());
		}		
		for (ProcedureCallSpecification callSpec : area.getProcedures()) {
			procedures.add(callSpec.getProcedure());
		}	
		List<Procedure> procedureList = new ArrayList<>(procedures);
		Collections.sort(procedureList, new Comparator<Procedure>() {
			@Override
			public int compare(Procedure procedure1, Procedure procedure2) {
				return procedure1.getName().compareTo(procedure2.getName());
			}});
		for (Procedure procedure : procedureList) {
			// please note that the order in which obsolete procedures are removed is random
			if (isProcedureObsolete(procedure, record, areaIsObsolete)) {
				IModelChangeCommand deleteProcedureCommand = new DeleteProcedureCommand(procedure);
				commands.add(deleteProcedureCommand);
			}
		}
		
		if (commands.size() > 1) {
			ModelChangeCompoundCommand compoundCommand = 
				new ModelChangeCompoundCommand(deleteRecordCommand.getLabel());
			compoundCommand.setContext(context);
			for (IModelChangeCommand command : commands) {
				compoundCommand.add((Command) command);
			}
			return compoundCommand;
		} else {
			deleteRecordCommand.setContext(context);
			return deleteRecordCommand;
		}
	}

	protected static boolean isProcedureObsolete(Procedure procedure, SchemaRecord obsoleteRecord, 
											     boolean areaIsObsoleteAsWell) {
		
		SchemaArea obsoleteRecordArea = obsoleteRecord.getAreaSpecification().getArea();
		for (ProcedureCallSpecification callSpec : procedure.getCallSpecifications()) {
			if (callSpec instanceof AreaProcedureCallSpecification) {
				AreaProcedureCallSpecification areaCallSpec = 
					(AreaProcedureCallSpecification) callSpec;
				if (areaCallSpec.getArea() != obsoleteRecordArea || !areaIsObsoleteAsWell) {
					return false;
				}
			} else {
				RecordProcedureCallSpecification recordCallSpec = 
					(RecordProcedureCallSpecification) callSpec;
				if (recordCallSpec.getRecord() != obsoleteRecord) {
					return false;
				}
			}
		}
		return true;
	}
	
}
