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
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;

public class RemoveRecordProcedureCallSpecificationCommandTest {

	@Test
	public void test() {
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		SchemaRecord record = SchemaFactory.eINSTANCE.createSchemaRecord();
		record.setSchema(schema);
		Procedure procedure = SchemaFactory.eINSTANCE.createProcedure();
		procedure.setSchema(schema);
		for (int i = 0; i < 5; i++) {			
			RecordProcedureCallSpecification callSpec =
				SchemaFactory.eINSTANCE.createRecordProcedureCallSpecification();
			callSpec.setProcedure(procedure);
			callSpec.setRecord(record);
		}
		
		List<RecordProcedureCallSpecification> originalRecordCallSpecs = 
			new ArrayList<>(record.getProcedures());
		assertEquals(5, originalRecordCallSpecs.size());
		List<ProcedureCallSpecification> originalProcedureCallSpecs = 
			new ArrayList<>(procedure.getCallSpecifications());
		assertEquals(5, originalProcedureCallSpecs.size());
		RecordProcedureCallSpecification itemToRemove = originalRecordCallSpecs.get(2);
		assertSame(itemToRemove, originalProcedureCallSpecs.get(2));
		RemoveRecordProcedureCallSpecificationCommand command =
			new RemoveRecordProcedureCallSpecificationCommand(itemToRemove);
		
		command.execute();
		EList<RecordProcedureCallSpecification> currentRecordCallSpecs = record.getProcedures();
		assertEquals(4, currentRecordCallSpecs.size());
		assertSame(originalRecordCallSpecs.get(0), currentRecordCallSpecs.get(0));
		assertSame(originalRecordCallSpecs.get(1), currentRecordCallSpecs.get(1));
		assertSame(originalRecordCallSpecs.get(3), currentRecordCallSpecs.get(2));
		assertSame(originalRecordCallSpecs.get(4), currentRecordCallSpecs.get(3));
		assertTrue(currentRecordCallSpecs.indexOf(itemToRemove) < 0);
		EList<ProcedureCallSpecification> currentProcedureCallSpecs = 
			procedure.getCallSpecifications();
		assertEquals(4, currentProcedureCallSpecs.size());
		assertSame(originalProcedureCallSpecs.get(0), currentProcedureCallSpecs.get(0));
		assertSame(originalProcedureCallSpecs.get(1), currentProcedureCallSpecs.get(1));	
		assertSame(originalProcedureCallSpecs.get(3), currentProcedureCallSpecs.get(2));
		assertSame(originalProcedureCallSpecs.get(4), currentProcedureCallSpecs.get(3));				
		assertTrue(currentProcedureCallSpecs.indexOf(itemToRemove) < 0);
		
		command.undo();
		currentRecordCallSpecs = record.getProcedures();
		assertEquals(5, currentRecordCallSpecs.size());
		assertSame(originalRecordCallSpecs.get(0), currentRecordCallSpecs.get(0));
		assertSame(originalRecordCallSpecs.get(1), currentRecordCallSpecs.get(1));
		assertSame(originalRecordCallSpecs.get(2), currentRecordCallSpecs.get(2));
		assertSame(originalRecordCallSpecs.get(3), currentRecordCallSpecs.get(3));
		assertSame(originalRecordCallSpecs.get(4), currentRecordCallSpecs.get(4));
		currentProcedureCallSpecs = procedure.getCallSpecifications();
		assertEquals(5, currentProcedureCallSpecs.size());
		assertSame(originalProcedureCallSpecs.get(0), currentProcedureCallSpecs.get(0));
		assertSame(originalProcedureCallSpecs.get(1), currentProcedureCallSpecs.get(1));
		assertSame(originalProcedureCallSpecs.get(2), currentProcedureCallSpecs.get(2));
		assertSame(originalProcedureCallSpecs.get(3), currentProcedureCallSpecs.get(3));
		assertSame(originalProcedureCallSpecs.get(4), currentProcedureCallSpecs.get(4));
		
		command.redo();
		currentRecordCallSpecs = record.getProcedures();
		assertEquals(4, currentRecordCallSpecs.size());
		assertSame(originalRecordCallSpecs.get(0), currentRecordCallSpecs.get(0));
		assertSame(originalRecordCallSpecs.get(1), currentRecordCallSpecs.get(1));
		assertSame(originalRecordCallSpecs.get(3), currentRecordCallSpecs.get(2));
		assertSame(originalRecordCallSpecs.get(4), currentRecordCallSpecs.get(3));
		assertTrue(currentRecordCallSpecs.indexOf(itemToRemove) < 0);
		currentProcedureCallSpecs = procedure.getCallSpecifications();
		assertEquals(4, currentProcedureCallSpecs.size());
		assertSame(originalProcedureCallSpecs.get(0), currentProcedureCallSpecs.get(0));
		assertSame(originalProcedureCallSpecs.get(1), currentProcedureCallSpecs.get(1));	
		assertSame(originalProcedureCallSpecs.get(3), currentProcedureCallSpecs.get(2));
		assertSame(originalProcedureCallSpecs.get(4), currentProcedureCallSpecs.get(3));
		assertTrue(currentProcedureCallSpecs.indexOf(itemToRemove) < 0);
	}

}
