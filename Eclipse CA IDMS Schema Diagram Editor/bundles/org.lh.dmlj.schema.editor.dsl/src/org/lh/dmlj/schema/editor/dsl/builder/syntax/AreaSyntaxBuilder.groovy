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
package org.lh.dmlj.schema.editor.dsl.builder.syntax

import org.lh.dmlj.schema.AreaProcedureCallSpecification
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.Set

import groovy.transform.CompileStatic;;;

@CompileStatic
class AreaSyntaxBuilder extends AbstractSyntaxBuilder<SchemaArea> {
	
	private SchemaArea area
	
	@Override
	protected String doBuild(SchemaArea model) {
		area = model			
		name()
		procedures()
	}
	
	private void name() {
		if (generateName) {		
			without_tab "name '${area.name}'"
		}		
	}
	
	private void procedures() {
		if (area.procedures && generateName) {
			blank_line()
		}
		area.procedures.each { call ->
			def procedureCallSpecification = call.getProcedure().name
			procedureCallSpecification <<= ' '
			procedureCallSpecification <<= call.callTime
			procedureCallSpecification <<= ' '
			procedureCallSpecification <<= replaceUnderscoresBySpaces(call.function)
			without_tab "procedure '$procedureCallSpecification'"
		}
	}

}
