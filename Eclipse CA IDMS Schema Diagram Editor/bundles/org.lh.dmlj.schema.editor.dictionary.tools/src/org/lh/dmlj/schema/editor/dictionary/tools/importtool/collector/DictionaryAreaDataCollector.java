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
package org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sa018;
import org.lh.dmlj.schema.editor.importtool.IAreaDataCollector;

public class DictionaryAreaDataCollector implements IAreaDataCollector<Sa018> {
	
	public DictionaryAreaDataCollector(SchemaImportSession session) {
	}

	@Override
	public String getName(Sa018 sa018) {
		return sa018.getSaNam018();
	}

	@Override
	public Collection<AreaProcedureCallFunction> getProcedureCallFunctions(Sa018 sa018) {
		List<AreaProcedureCallFunction> list = new ArrayList<>();		
		for (var sacall020 : sa018.getSacall020s()) {
			var func = sacall020.getDbpFunc020();   	// READY/FINISH/COMMIT/ROLLBACK				
			var mode = sacall020.getDbpMode020();   	// UPDATE/RETRIEVAL
			var access = sacall020.getDbpAccess020(); 	// EXCLUSIVE/PROTECTED/SHARED
			var trigger = new StringBuilder(func.toUpperCase());
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
			if (!trigger.isEmpty()) {
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
	public Collection<ProcedureCallTime> getProcedureCallTimes(Sa018 sa018) {
		var list = new ArrayList<ProcedureCallTime>();		
		for (var sacall020 : sa018.getSacall020s()) {
			if (sacall020.getCallTime020().equals("00")) {
				list.add(ProcedureCallTime.BEFORE);
			} else if (sacall020.getCallTime020().equals("01")) {
				list.add(ProcedureCallTime.ON_ERROR_DURING);
			} else if (sacall020.getCallTime020().equals("02")) {
				list.add(ProcedureCallTime.AFTER);
			}
		}		
		return list;
	}

	@Override
	public Collection<String> getProceduresCalled(Sa018 sa018) {
		var list = new ArrayList<String>();		
		for (var sacall020 : sa018.getSacall020s()) {
			var procedureName = sacall020.getCallProc020();
			list.add(procedureName);
		}		
		return list;	
	}

}
