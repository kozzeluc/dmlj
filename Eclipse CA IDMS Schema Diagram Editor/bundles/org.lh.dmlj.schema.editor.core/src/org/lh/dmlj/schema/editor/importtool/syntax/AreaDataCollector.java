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
package org.lh.dmlj.schema.editor.importtool.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.editor.importtool.IAreaDataCollector;

public class AreaDataCollector implements IAreaDataCollector<SchemaSyntaxWrapper> {
	private static final String CALL = "         CALL ";

	private static List<String> getProcedureLines(SchemaSyntaxWrapper context) {
		return context.getLines().stream()
			.filter(line -> line.startsWith(CALL))
			.toList();
	}

	@Override
	public String getName(SchemaSyntaxWrapper context) {
		return context.getLines().get(1).substring(18).trim();
	}

	@Override
	public Collection<ProcedureCallTime> getProcedureCallTimes(SchemaSyntaxWrapper context) {
		var procedureNames = new ArrayList<>(getProceduresCalled(context));
		var procedureLines = getProcedureLines(context);
		
		var list = new ArrayList<ProcedureCallTime>();
		for (var k = 0; k < procedureLines.size(); k++) {
			var procedureName = procedureNames.get(k);
			var line = procedureLines.get(k);
			
			var i = line.indexOf(" " + procedureName + " ") + procedureName.length() + 2;
			
			if (line.startsWith("ON ERROR DURING", i)) {
				list.add(ProcedureCallTime.ON_ERROR_DURING);
			} else {
				var j = line.indexOf(" ", i);
				String p;
				if (j > -1) {
					p = line.substring(i, j);
				} else {
					p = line.substring(i);
				}
				list.add(ProcedureCallTime.valueOf(p));
			}
		}
		return list;
	}

	@Override
	public Collection<AreaProcedureCallFunction> getProcedureCallFunctions(SchemaSyntaxWrapper context) {
		var procedureNames = new ArrayList<>(getProceduresCalled(context));
		var procedureLines = getProcedureLines(context);
		return IntStream.range(0, procedureLines.size())
			.mapToObj(k -> extractAreaProcedureCallFunction(procedureNames.get(k), procedureLines.get(k)))
			.toList();
	}
	
	private AreaProcedureCallFunction extractAreaProcedureCallFunction(String procedureName, String line) {
		var i = line.indexOf(" " + procedureName + " ") + procedureName.length() + 2;
		int j;
		if (line.startsWith("ON ERROR DURING", i)) {
			j = i + 15;
		} else {
			j = line.indexOf(" ", i);
		}			
		if (j > -1) {
			var p = line.substring(j).trim();
			if (p.isEmpty()) {
				return AreaProcedureCallFunction.EVERY_DML_FUNCTION;									
			} else {					
				if (!p.startsWith("READY")) {
					return AreaProcedureCallFunction.valueOf(p);
				} else {
					var q = p.replace(" ", "_").replaceFirst("_FOR", "");
					return AreaProcedureCallFunction.valueOf(q);
				}
			}
		} else {
			return AreaProcedureCallFunction.EVERY_DML_FUNCTION;
		}
	}

	@Override
	public Collection<String> getProceduresCalled(SchemaSyntaxWrapper context) {
		var list = new ArrayList<String>();
		for (var line : context.getLines()) {
			if (line.startsWith(CALL)) {
				int i = line.indexOf(" ", 14);
				var procedureName = line.substring(14, i).trim();
				list.add(procedureName);
			}
		}
		return list;
	}

}
