/**
 * Copyright (C) 2016  Luc Hermans
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

import static org.junit.Assert.fail

import org.lh.dmlj.schema.AreaProcedureCallFunction
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea

import spock.lang.Unroll

class AreaSyntaxBuilderSpec extends AbstractSyntaxBuilderSpec {
	
	def "Area without procedures"() {
		
		given: "an area syntax builder and the EMPSCHM version 100 schema"
		AreaSyntaxBuilder builder = new AreaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		
		when: "passing the EMP-DEMO-REGION area to the builder's build method"
		
		String syntax = builder.build(schema.getArea("EMP-DEMO-REGION"))
		
		then: "the builder creates the syntax that describes the area: no procedures are called"
		syntax == expected(
"""
name 'EMP-DEMO-REGION'
""")
	}
	
	@Unroll
	def "Area with procedures"() {
		
		given: "an area syntax builder and a slighty tweaked version of the IDMSNTWK version 1" 
			   "(Release 18.5) schema"
		AreaSyntaxBuilder builder = new AreaSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		SchemaArea area = schema.getArea('DDLDML')
		for (callSpec in area.procedures) {
			callSpec.callTime = callTime
			callSpec.function = function
		}
		
		when: "passing the DDLDML area to the builder's build method"
		
		String syntax = builder.build(schema.getArea("DDLDML"))
		
		then: "the builder creates the syntax that describes the area: 2 procedures are called"
		syntax == expected(
"""
name 'DDLDML'

call 'IDMSCOMP$syntaxPart'
call 'IDMSDCOM$syntaxPart'
""")
		
		where: "the procedure call is specified as follows"
		callTime 				 | function 										   || syntaxPart
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.EVERY_DML_FUNCTION 	   || ' BEFORE'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.READY_EXCLUSIVE 		   || ' BEFORE READY EXCLUSIVE'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.READY_EXCLUSIVE_UPDATE	   || ' BEFORE READY EXCLUSIVE UPDATE'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.READY_EXCLUSIVE_RETRIEVAL || ' BEFORE READY EXCLUSIVE RETRIEVAL'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.READY_PROTECTED 		   || ' BEFORE READY PROTECTED'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.READY_PROTECTED_UPDATE	   || ' BEFORE READY PROTECTED UPDATE'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.READY_PROTECTED_RETRIEVAL || ' BEFORE READY PROTECTED RETRIEVAL'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.READY_SHARED 			   || ' BEFORE READY SHARED'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.READY_SHARED_UPDATE	   || ' BEFORE READY SHARED UPDATE'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.READY_SHARED_RETRIEVAL	   || ' BEFORE READY SHARED RETRIEVAL'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.READY_UPDATE			   || ' BEFORE READY UPDATE'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.READY_RETRIEVAL		   || ' BEFORE READY RETRIEVAL'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.FINISH					   || ' BEFORE FINISH'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.COMMIT					   || ' BEFORE COMMIT'
		ProcedureCallTime.BEFORE | AreaProcedureCallFunction.ROLLBACK				   || ' BEFORE ROLLBACK'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.EVERY_DML_FUNCTION 	   || ' AFTER'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.READY_EXCLUSIVE 		   || ' AFTER READY EXCLUSIVE'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.READY_EXCLUSIVE_UPDATE	   || ' AFTER READY EXCLUSIVE UPDATE'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.READY_EXCLUSIVE_RETRIEVAL || ' AFTER READY EXCLUSIVE RETRIEVAL'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.READY_PROTECTED 		   || ' AFTER READY PROTECTED'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.READY_PROTECTED_UPDATE	   || ' AFTER READY PROTECTED UPDATE'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.READY_PROTECTED_RETRIEVAL || ' AFTER READY PROTECTED RETRIEVAL'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.READY_SHARED 			   || ' AFTER READY SHARED'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.READY_SHARED_UPDATE	   || ' AFTER READY SHARED UPDATE'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.READY_SHARED_RETRIEVAL	   || ' AFTER READY SHARED RETRIEVAL'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.READY_UPDATE			   || ' AFTER READY UPDATE'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.READY_RETRIEVAL		   || ' AFTER READY RETRIEVAL'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.FINISH					   || ' AFTER FINISH'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.COMMIT					   || ' AFTER COMMIT'
		ProcedureCallTime.AFTER  | AreaProcedureCallFunction.ROLLBACK				   || ' AFTER ROLLBACK'
	}
	
	def "Syntax for inclusion in a schema"() {
		
		given: "an area syntax builder for which we indicate that we want to suppress the 'name" 
		       "property and set an initial indent"
		AreaSyntaxBuilder builder = new AreaSyntaxBuilder( generateName : false, initialTabs : 1)
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		
		when: "passing an area to the builder's build method"		
		String syntax = builder.build(schema.getArea("DDLDML"))
		
		then: "the builder creates the syntax that describes the area and will omit the name"
		      "property and indent the syntax with 1 'tab' (being actually spaces)"
		syntax == expected(
"""
    call 'IDMSCOMP BEFORE'
    call 'IDMSDCOM BEFORE'
""")
	}
	
}
