/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sa_018;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sacall_020;
import org.lh.dmlj.schema.editor.importtool.IAreaDataCollector;

public class DictionaryAreaDataCollector implements IAreaDataCollector<Sa_018> {
	
	public DictionaryAreaDataCollector(SchemaImportSession session) {
		super();
	}

	@Override
	public String getName(Sa_018 sa_018) {
		return sa_018.getSaNam_018();
	}

	@Override
	public Collection<AreaProcedureCallFunction> getProcedureCallFunctions(Sa_018 sa_018) {
		List<AreaProcedureCallFunction> list = new ArrayList<>();		
		for (Sacall_020 sacall_020 : sa_018.getSacall_020s()) {
			String func = sacall_020.getDbpFunc_020();   	// READY/FINISH/COMMIT/ROLLBACK				
			String mode = sacall_020.getDbpMode_020();   	// UPDATE/RETRIEVAL
			String access = sacall_020.getDbpAccess_020(); 	// EXCLUSIVE/PROTECTED/SHARED
			StringBuilder trigger = new StringBuilder(func.toLowerCase());
			if (func.equalsIgnoreCase("READY")) {
				if (!access.equals("")) {
					trigger.append("_");
					trigger.append(access.toUpperCase());
				}
				if (!mode.equals("")) {
					trigger.append("_");
					trigger.append(mode.toUpperCase());
				}
			}
			AreaProcedureCallFunction function;
			if (trigger.length() > 0) {
				function = AreaProcedureCallFunction.valueOf(trigger.toString());					
			} else {
				// null doesn't work here and hence the EVERY_DML_FUNCTION function was created					
				function = AreaProcedureCallFunction.EVERY_DML_FUNCTION;
			}
			list.add(function);					
		}		
		return list;
	}

	@Override
	public Collection<ProcedureCallTime> getProcedureCallTimes(Sa_018 sa_018) {
		List<ProcedureCallTime> list = new ArrayList<>();		
		for (Sacall_020 sacall_020 : sa_018.getSacall_020s()) {
			if (sacall_020.getCallTime_020().equals("00")) {
				list.add(ProcedureCallTime.BEFORE);
			} else if (sacall_020.getCallTime_020().equals("01")) {
				list.add(ProcedureCallTime.ON_ERROR_DURING);
			} else if (sacall_020.getCallTime_020().equals("02")) {
				list.add(ProcedureCallTime.AFTER);
			}
		}		
		return list;
	}

	@Override
	public Collection<String> getProceduresCalled(Sa_018 sa_018) {
		List<String> list = new ArrayList<>();		
		for (Sacall_020 sacall_020 : sa_018.getSacall_020s()) {
			String procedureName = sacall_020.getCallProc_020();
			list.add(procedureName);
		}		
		return list;	
	}

}
