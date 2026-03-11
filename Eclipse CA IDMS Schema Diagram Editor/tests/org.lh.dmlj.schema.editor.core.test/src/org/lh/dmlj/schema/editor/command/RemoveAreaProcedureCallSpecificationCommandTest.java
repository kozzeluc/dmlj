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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.junit.Test;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;

public class RemoveAreaProcedureCallSpecificationCommandTest {

	@Test
	public void test() {
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		SchemaArea area = SchemaFactory.eINSTANCE.createSchemaArea();
		area.setSchema(schema);
		Procedure procedure = SchemaFactory.eINSTANCE.createProcedure();
		procedure.setSchema(schema);
		for (int i = 0; i < 5; i++) {			
			AreaProcedureCallSpecification callSpec =
				SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification();
			callSpec.setProcedure(procedure);
			callSpec.setArea(area);
		}
		
		List<AreaProcedureCallSpecification> originalAreaCallSpecs = 
			new ArrayList<>(area.getProcedures());
		assertEquals(5, originalAreaCallSpecs.size());
		List<ProcedureCallSpecification> originalProcedureCallSpecs = 
			new ArrayList<>(procedure.getCallSpecifications());
		assertEquals(5, originalProcedureCallSpecs.size());
		AreaProcedureCallSpecification itemToRemove = originalAreaCallSpecs.get(2);
		assertSame(itemToRemove, originalProcedureCallSpecs.get(2));
		RemoveAreaProcedureCallSpecificationCommand command =
			new RemoveAreaProcedureCallSpecificationCommand(itemToRemove);
		
		command.execute();
		EList<AreaProcedureCallSpecification> currentAreaCallSpecs = area.getProcedures();
		assertEquals(4, currentAreaCallSpecs.size());
		assertSame(originalAreaCallSpecs.get(0), currentAreaCallSpecs.get(0));
		assertSame(originalAreaCallSpecs.get(1), currentAreaCallSpecs.get(1));
		assertSame(originalAreaCallSpecs.get(3), currentAreaCallSpecs.get(2));
		assertSame(originalAreaCallSpecs.get(4), currentAreaCallSpecs.get(3));
		assertTrue(currentAreaCallSpecs.indexOf(itemToRemove) < 0);
		EList<ProcedureCallSpecification> currentProcedureCallSpecs = 
			procedure.getCallSpecifications();
		assertEquals(4, currentProcedureCallSpecs.size());
		assertSame(originalProcedureCallSpecs.get(0), currentProcedureCallSpecs.get(0));
		assertSame(originalProcedureCallSpecs.get(1), currentProcedureCallSpecs.get(1));	
		assertSame(originalProcedureCallSpecs.get(3), currentProcedureCallSpecs.get(2));
		assertSame(originalProcedureCallSpecs.get(4), currentProcedureCallSpecs.get(3));				
		assertTrue(currentProcedureCallSpecs.indexOf(itemToRemove) < 0);
		
		command.undo();
		currentAreaCallSpecs = area.getProcedures();
		assertEquals(5, currentAreaCallSpecs.size());
		assertSame(originalAreaCallSpecs.get(0), currentAreaCallSpecs.get(0));
		assertSame(originalAreaCallSpecs.get(1), currentAreaCallSpecs.get(1));
		assertSame(originalAreaCallSpecs.get(2), currentAreaCallSpecs.get(2));
		assertSame(originalAreaCallSpecs.get(3), currentAreaCallSpecs.get(3));
		assertSame(originalAreaCallSpecs.get(4), currentAreaCallSpecs.get(4));
		currentProcedureCallSpecs = procedure.getCallSpecifications();
		assertEquals(5, currentProcedureCallSpecs.size());
		assertSame(originalProcedureCallSpecs.get(0), currentProcedureCallSpecs.get(0));
		assertSame(originalProcedureCallSpecs.get(1), currentProcedureCallSpecs.get(1));
		assertSame(originalProcedureCallSpecs.get(2), currentProcedureCallSpecs.get(2));
		assertSame(originalProcedureCallSpecs.get(3), currentProcedureCallSpecs.get(3));
		assertSame(originalProcedureCallSpecs.get(4), currentProcedureCallSpecs.get(4));
		
		command.redo();
		currentAreaCallSpecs = area.getProcedures();
		assertEquals(4, currentAreaCallSpecs.size());
		assertSame(originalAreaCallSpecs.get(0), currentAreaCallSpecs.get(0));
		assertSame(originalAreaCallSpecs.get(1), currentAreaCallSpecs.get(1));
		assertSame(originalAreaCallSpecs.get(3), currentAreaCallSpecs.get(2));
		assertSame(originalAreaCallSpecs.get(4), currentAreaCallSpecs.get(3));
		assertTrue(currentAreaCallSpecs.indexOf(itemToRemove) < 0);
		currentProcedureCallSpecs = procedure.getCallSpecifications();
		assertEquals(4, currentProcedureCallSpecs.size());
		assertSame(originalProcedureCallSpecs.get(0), currentProcedureCallSpecs.get(0));
		assertSame(originalProcedureCallSpecs.get(1), currentProcedureCallSpecs.get(1));	
		assertSame(originalProcedureCallSpecs.get(3), currentProcedureCallSpecs.get(2));
		assertSame(originalProcedureCallSpecs.get(4), currentProcedureCallSpecs.get(3));
		assertTrue(currentProcedureCallSpecs.indexOf(itemToRemove) < 0);
	}

}
