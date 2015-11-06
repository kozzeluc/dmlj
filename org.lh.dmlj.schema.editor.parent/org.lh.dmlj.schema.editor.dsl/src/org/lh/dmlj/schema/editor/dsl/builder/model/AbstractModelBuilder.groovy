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
package org.lh.dmlj.schema.editor.dsl.builder.model

import java.io.File;

import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation
import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaFactory

import groovy.lang.Closure;

abstract class AbstractModelBuilder<T> {
	
	protected static final String BODY_DIAGRAM = "diagram"
	
	protected Schema schema
	protected boolean schemaIsAutomatic = false	
	protected List<String> bodies = []
	protected String bufferedName
	
	def methodMissing(String name, arguments) {
		// Allow for constructions like:
		//		area 'EMP-DEMO-REGION' {
		//			procedure 'IDMSCOMP BEFORE FINISH'
		//		}
		// In the example above, the methodMissing method will be called *only* with its name
		// argument set to 'EMP-DEMO-REGION' and the area body (containing the procedure calls) will
		// be available in arguments[0].
		if (arguments && arguments[0] instanceof Closure) {
			assert !bufferedName
			bufferedName = name
			arguments[0]
		} else {
			throw new MissingMethodException(name, getClass(), arguments)
		}
	}
	
	protected DiagramLocation buildAndRegisterDiagramLocation(String eyecatcher) {
		DiagramLocation diagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation()
		diagramLocation.eyecatcher = eyecatcher
		schema.diagramData.locations << diagramLocation
		diagramLocation
	}
	
	protected void buildTemporarySchema() {
		schema = new SchemaModelBuilder().build()
		schemaIsAutomatic = true
	}
	
	public T buildFromString(String definition) {
		def binding = new Binding()
		binding.builder = this
		def shell = new GroovyShell(binding)
		shell.evaluate("builder.build( { $definition } )")
	}
	
	protected Procedure createOrGetProcedure(String name) {
		Procedure procedure = schema.getProcedure(name)
		if (procedure == null) {
			procedure = SchemaFactory.eINSTANCE.createProcedure()
			procedure.name = name
			procedure.schema = schema
		}
		procedure
	}
	
	protected runClosure(Closure closure) {
		Closure clonedClosure = closure.clone()
		clonedClosure.delegate = this
		clonedClosure.resolveStrategy = Closure.DELEGATE_ONLY
		clonedClosure()
	}
	
	void x(String x) {
		// negative x values should be supplied as strings when brackets are omitted
		this.x x.toInteger()
	}
	
	void y(String y) {
		// negative y values should be supplied as strings when brackets are omitted
		this.y y.toInteger()
	}
	
}
