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
package org.lh.dmlj.schema.editor.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertCommandCategorySet;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertFeaturesSet;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertOwnerSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.prefix.Prefix;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class ChangePointerOrderCommandTest {

	private ObjectGraph objectGraph;
	private Xmi xmi;
	private Schema schema;		
	
	private void checkObjectGraph(ObjectGraph expected) {
		ObjectGraph actual = TestTools.asObjectGraph(schema);
		assertEquals(expected, actual);		
	}
	
	private void checkXmi(Xmi expected) {
		Xmi actual = TestTools.asXmi(schema);
		assertEquals(expected, actual);		
	}
	
	@Before
	public void setup() {
		schema = TestTools.getEmpschmSchema();
		objectGraph = TestTools.asObjectGraph(schema);
		xmi = TestTools.asXmi(schema);
	}	
	
	@Test
	public void testAnnotations() {
		
		SchemaRecord record = TestTools.getRecord(schema, "EMPLOYEE");
		Prefix originalPrefix = PrefixFactory.newPrefixForInquiry(record);
		List<Pointer<?>> originalPointers = originalPrefix.getPointers();
		List<Pointer<?>> newPointerOrder = new ArrayList<>(originalPointers); 
		
		
		Command command = new ChangePointerOrderCommand(record, newPointerOrder);
		
		
		command.execute();				
		assertCommandCategorySet(command, ModelChangeCategory.SET_FEATURES);
		assertOwnerSet(command, record);
		assertFeaturesSet(command, new EStructuralFeature[] {
		    SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition(),
			SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition(),	
			SchemaPackage.eINSTANCE.getMemberRole_NextDbkeyPosition(),	
			SchemaPackage.eINSTANCE.getMemberRole_PriorDbkeyPosition(),	
			SchemaPackage.eINSTANCE.getMemberRole_OwnerDbkeyPosition(),
			SchemaPackage.eINSTANCE.getMemberRole_IndexDbkeyPosition(),					
		});	
		
		
		command.undo();		
		assertOwnerSet(command, record);
		assertFeaturesSet(command, new EStructuralFeature[] {
		    SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition(),
			SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition(),	
			SchemaPackage.eINSTANCE.getMemberRole_NextDbkeyPosition(),	
			SchemaPackage.eINSTANCE.getMemberRole_PriorDbkeyPosition(),	
			SchemaPackage.eINSTANCE.getMemberRole_OwnerDbkeyPosition(),
			SchemaPackage.eINSTANCE.getMemberRole_IndexDbkeyPosition(),					
		});		

		
		command.redo();		
		assertOwnerSet(command, record);
		assertFeaturesSet(command, new EStructuralFeature[] {
		    SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition(),
			SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition(),	
			SchemaPackage.eINSTANCE.getMemberRole_NextDbkeyPosition(),	
			SchemaPackage.eINSTANCE.getMemberRole_PriorDbkeyPosition(),	
			SchemaPackage.eINSTANCE.getMemberRole_OwnerDbkeyPosition(),
			SchemaPackage.eINSTANCE.getMemberRole_IndexDbkeyPosition(),					
		});			
				
	}
	
	@Test
	public void testReorderPointers() {
		
		SchemaRecord record = TestTools.getRecord(schema, "EMPLOYEE");
		Prefix originalPrefix = PrefixFactory.newPrefixForInquiry(record);
		List<Pointer<?>> originalPointers = originalPrefix.getPointers();
		List<Pointer<?>> newPointerOrder = new ArrayList<>(originalPointers);
		Assert.assertEquals(1, ((MemberRole) record.getRole("DEPT-EMPLOYEE")).getNextDbkeyPosition().intValue());
		Assert.assertEquals(2, ((MemberRole) record.getRole("DEPT-EMPLOYEE")).getPriorDbkeyPosition().intValue());
		Assert.assertEquals(3, ((MemberRole) record.getRole("DEPT-EMPLOYEE")).getOwnerDbkeyPosition().intValue());
		Assert.assertEquals(4, ((MemberRole) record.getRole("EMP-NAME-NDX")).getIndexDbkeyPosition().intValue());
		Assert.assertEquals(5, ((MemberRole) record.getRole("OFFICE-EMPLOYEE")).getIndexDbkeyPosition().intValue());
		Assert.assertEquals(6, ((MemberRole) record.getRole("OFFICE-EMPLOYEE")).getOwnerDbkeyPosition().intValue());
		Assert.assertEquals(7, ((OwnerRole) record.getRole("EMP-COVERAGE")).getNextDbkeyPosition());
		Assert.assertEquals(8, ((OwnerRole) record.getRole("EMP-COVERAGE")).getPriorDbkeyPosition().intValue());
		Assert.assertEquals(9, ((OwnerRole) record.getRole("EMP-EMPOSITION")).getNextDbkeyPosition());
		Assert.assertEquals(10, ((OwnerRole) record.getRole("EMP-EMPOSITION")).getPriorDbkeyPosition().intValue());
		Assert.assertEquals(11, ((OwnerRole) record.getRole("EMP-EXPERTISE")).getNextDbkeyPosition());
		Assert.assertEquals(12, ((OwnerRole) record.getRole("EMP-EXPERTISE")).getPriorDbkeyPosition().intValue());
		Assert.assertEquals(13, ((OwnerRole) record.getRole("MANAGES")).getNextDbkeyPosition());
		Assert.assertEquals(14, ((OwnerRole) record.getRole("MANAGES")).getPriorDbkeyPosition().intValue());
		Assert.assertEquals(15, ((OwnerRole) record.getRole("REPORTS-TO")).getNextDbkeyPosition());
		Assert.assertEquals(16, ((OwnerRole) record.getRole("REPORTS-TO")).getPriorDbkeyPosition().intValue());		
		// switch some order positions:
		Collections.swap(newPointerOrder, 0, 3);
		Collections.swap(newPointerOrder, 12, 14);
		Collections.swap(newPointerOrder, 13, 15);
		assertEquals("EMP-NAME-NDX", newPointerOrder.get(0).getSetName());
		assertSame(PointerType.MEMBER_INDEX, newPointerOrder.get(0).getType());
		assertEquals("DEPT-EMPLOYEE", newPointerOrder.get(3).getSetName());
		assertSame(PointerType.MEMBER_NEXT, newPointerOrder.get(3).getType());
		assertEquals("REPORTS-TO", newPointerOrder.get(12).getSetName());
		assertSame(PointerType.OWNER_NEXT, newPointerOrder.get(12).getType());
		assertEquals("REPORTS-TO", newPointerOrder.get(13).getSetName());
		assertSame(PointerType.OWNER_PRIOR, newPointerOrder.get(13).getType());
		assertEquals("MANAGES", newPointerOrder.get(14).getSetName());
		assertSame(PointerType.OWNER_NEXT, newPointerOrder.get(14).getType());
		assertEquals("MANAGES", newPointerOrder.get(15).getSetName());
		assertSame(PointerType.OWNER_PRIOR, newPointerOrder.get(15).getType());
		
		Command command = new ChangePointerOrderCommand(record, newPointerOrder);
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		Assert.assertEquals(1, ((MemberRole) record.getRole("EMP-NAME-NDX")).getIndexDbkeyPosition().intValue());		
		Assert.assertEquals(2, ((MemberRole) record.getRole("DEPT-EMPLOYEE")).getPriorDbkeyPosition().intValue());
		Assert.assertEquals(3, ((MemberRole) record.getRole("DEPT-EMPLOYEE")).getOwnerDbkeyPosition().intValue());
		Assert.assertEquals(4, ((MemberRole) record.getRole("DEPT-EMPLOYEE")).getNextDbkeyPosition().intValue());		
		Assert.assertEquals(5, ((MemberRole) record.getRole("OFFICE-EMPLOYEE")).getIndexDbkeyPosition().intValue());
		Assert.assertEquals(6, ((MemberRole) record.getRole("OFFICE-EMPLOYEE")).getOwnerDbkeyPosition().intValue());
		Assert.assertEquals(7, ((OwnerRole) record.getRole("EMP-COVERAGE")).getNextDbkeyPosition());
		Assert.assertEquals(8, ((OwnerRole) record.getRole("EMP-COVERAGE")).getPriorDbkeyPosition().intValue());
		Assert.assertEquals(9, ((OwnerRole) record.getRole("EMP-EMPOSITION")).getNextDbkeyPosition());
		Assert.assertEquals(10, ((OwnerRole) record.getRole("EMP-EMPOSITION")).getPriorDbkeyPosition().intValue());
		Assert.assertEquals(11, ((OwnerRole) record.getRole("EMP-EXPERTISE")).getNextDbkeyPosition());
		Assert.assertEquals(12, ((OwnerRole) record.getRole("EMP-EXPERTISE")).getPriorDbkeyPosition().intValue());
		Assert.assertEquals(13, ((OwnerRole) record.getRole("REPORTS-TO")).getNextDbkeyPosition());
		Assert.assertEquals(14, ((OwnerRole) record.getRole("REPORTS-TO")).getPriorDbkeyPosition().intValue());		
		Assert.assertEquals(15, ((OwnerRole) record.getRole("MANAGES")).getNextDbkeyPosition());
		Assert.assertEquals(16, ((OwnerRole) record.getRole("MANAGES")).getPriorDbkeyPosition().intValue());
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}

}
