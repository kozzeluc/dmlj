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
import java.util.Comparator;
import java.util.stream.Stream;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;

public class DeleteRecordCommandCreationAssistant {

	static boolean canDeleteRecord(SchemaRecord schemaRecord) {
		// for now, only allow a record to be deleted when it does NOT participate in any set or index
		return schemaRecord.getOwnerRoles().isEmpty() && schemaRecord.getMemberRoles().isEmpty();
	}
	
	public static IModelChangeCommand getCommand(SchemaRecord schemaRecord) {
		if (!canDeleteRecord(schemaRecord)) {
			return null;
		}
		
		var context = new ModelChangeContext(ModelChangeType.DELETE_RECORD);
		context.putContextData(schemaRecord);
		
		var commands = new ArrayList<IModelChangeCommand>();
		
		schemaRecord.getProcedures().stream()
				.map(RemoveRecordProcedureCallSpecificationCommand::new)
				.forEach(commands::add);
				
		var deleteRecordCommand = new DeleteRecordCommand(schemaRecord);
		commands.add(deleteRecordCommand);
		
		var area = schemaRecord.getAreaSpecification().getArea(); 
		var areaIsObsolete = area.getRecords().size() == 1;
		if (areaIsObsolete) {
			area.getProcedures().stream()
					.map(RemoveAreaProcedureCallSpecificationCommand::new)
					.forEach(commands::add);
			commands.add(new DeleteAreaCommand(area));
		}
				
		// the order in which obsolete procedures are removed is random
		Stream.concat(schemaRecord.getProcedures().stream(), area.getProcedures().stream())
				.map(ProcedureCallSpecification::getProcedure)
				.filter(procedure -> isProcedureObsolete(procedure, schemaRecord, areaIsObsolete))
				.sorted(Comparator.comparing(Procedure::getName))
				.map(DeleteProcedureCommand::new)
				.forEach(commands::add);
		
		if (commands.size() > 1) {
			var compoundCommand = new ModelChangeCompoundCommand(deleteRecordCommand.getLabel());
			compoundCommand.setContext(context);
			for (var command : commands) {
				compoundCommand.add((Command) command);
			}
			return compoundCommand;
		} else {
			deleteRecordCommand.setContext(context);
			return deleteRecordCommand;
		}
	}

	protected static boolean isProcedureObsolete(Procedure procedure, SchemaRecord obsoleteRecord, boolean areaIsObsoleteAsWell) {
		var obsoleteRecordArea = obsoleteRecord.getAreaSpecification().getArea();
		for (var callSpec : procedure.getCallSpecifications()) {
			if (callSpec instanceof AreaProcedureCallSpecification areaCallSpec) {
				if (areaCallSpec.getArea() != obsoleteRecordArea || !areaIsObsoleteAsWell) {
					return false;
				}
			} else {
				var recordCallSpec = (RecordProcedureCallSpecification) callSpec;
				if (recordCallSpec.getRecord() != obsoleteRecord) {
					return false;
				}
			}
		}
		return true;
	}
	
	private DeleteRecordCommandCreationAssistant() {
	}
	
}
