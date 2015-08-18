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
import org.lh.dmlj.schema.Set;;

class AreaSyntaxBuilder extends AbstractSyntaxBuilder<SchemaArea> {
	
	@Override
	protected String build() {				
		name()
		procedures()
	}
	
	private void name() {
		if (generateName) {		
			without_tab "name '${model.name}'"
		}		
	}
	
	private void procedures() {
		if (model.procedures && generateName) {
			blank_line()
		}
		for (call in model.procedures) {
			def procedureCallSpecification = call.getProcedure().name
			procedureCallSpecification <<= ' '
			procedureCallSpecification <<= call.callTime
			procedureCallSpecification <<= ' '
			procedureCallSpecification <<= replaceUnderscoresBySpaces(call.function)
			without_tab "procedure '$procedureCallSpecification'"
		}
	}

}
