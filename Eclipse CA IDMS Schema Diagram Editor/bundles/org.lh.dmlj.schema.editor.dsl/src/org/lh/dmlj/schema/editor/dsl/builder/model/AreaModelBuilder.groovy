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
package org.lh.dmlj.schema.editor.dsl.builder.model

import groovy.transform.CompileStatic

import org.lh.dmlj.schema.AreaProcedureCallFunction
import org.lh.dmlj.schema.AreaProcedureCallSpecification
import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaFactory

class AreaModelBuilder extends AbstractModelBuilder<SchemaArea> {
	
	private SchemaArea area
	
	SchemaArea build(Closure definition) {
		
		assert !bodies
		
		if (schema == null) {
			buildTemporarySchema()
		}
		area = SchemaFactory.eINSTANCE.createSchemaArea()
		schema.areas.add(area)
		
		runClosure definition
		
		assert !bodies
		
		area
	}
	
	SchemaArea build(String name) {
		
		assert !bodies
		
		if (schema == null) {
			buildTemporarySchema()
		}
		area = SchemaFactory.eINSTANCE.createSchemaArea()
		area.name = name
		schema.areas.add(area)
		area
	}
	
	SchemaArea build(String name, Closure definition) {
		area = build(definition)
		area.name = name
		area
	}
	
	void name(String name) {
		assert !bodies
		area.name = name	
	}

	void callProcedure(String procedureCallSpecAsString) {
		
		assert !bodies
		
		int i = procedureCallSpecAsString.indexOf(" ")
		int j = procedureCallSpecAsString.indexOf(" ", i + 1)
		String procedureName = procedureCallSpecAsString.substring(0, i);
		String callTimeAsString
		String functionAsString
		if (j > -1) {
			callTimeAsString = procedureCallSpecAsString.substring(i + 1, j).replaceAll(" ", "_")
			functionAsString = procedureCallSpecAsString.substring(j + 1).replaceAll(" ", "_")
		} else {
			callTimeAsString = procedureCallSpecAsString.substring(i + 1).replaceAll(" ", "_")
			functionAsString = AreaProcedureCallFunction.EVERY_DML_FUNCTION.toString()
		}
			
		ProcedureCallTime callTime = ProcedureCallTime.valueOf(callTimeAsString)
		AreaProcedureCallFunction function = AreaProcedureCallFunction.valueOf(functionAsString)
		
		Procedure procedure = createOrGetProcedure(procedureName)
		
		AreaProcedureCallSpecification areaProcedureCallSpecification = 
			SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification()
		areaProcedureCallSpecification.area = area
		areaProcedureCallSpecification.procedure = procedure
		areaProcedureCallSpecification.callTime = callTime
		areaProcedureCallSpecification.function = function
	}
}
