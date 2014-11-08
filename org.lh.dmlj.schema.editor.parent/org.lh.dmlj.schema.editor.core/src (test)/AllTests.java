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
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.lh.dmlj.schema.editor.command.AddMemberToSetCommandTest;
import org.lh.dmlj.schema.editor.command.ChangeAreaSpecificationCommandTest;
import org.lh.dmlj.schema.editor.command.ChangeCalcKeyCommandTest;
import org.lh.dmlj.schema.editor.command.ChangePointerOrderCommandTest;
import org.lh.dmlj.schema.editor.command.ChangeSetOrderCommandTest;
import org.lh.dmlj.schema.editor.command.ChangeSortKeysCommandTest;
import org.lh.dmlj.schema.editor.command.ChangeViaSpecificationCommandTest;
import org.lh.dmlj.schema.editor.command.CreateBendpointCommandTest;
import org.lh.dmlj.schema.editor.command.CreateConnectorCommandTest;
import org.lh.dmlj.schema.editor.command.CreateDiagramLabelCommandTest;
import org.lh.dmlj.schema.editor.command.CreateGuideCommandTest;
import org.lh.dmlj.schema.editor.command.CreateIndexCommandTest;
import org.lh.dmlj.schema.editor.command.CreateRecordCommandTest;
import org.lh.dmlj.schema.editor.command.CreateSetCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteBendpointCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteConnectorCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteDiagramLabelCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteGuideCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteIndexCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteSetCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteSetOrIndexCommandCreationAssistantTest;
import org.lh.dmlj.schema.editor.command.LockEndpointsCommandTest;
import org.lh.dmlj.schema.editor.command.MakeRecordCalcCommandTest;
import org.lh.dmlj.schema.editor.command.MakeRecordDirectCommandTest;
import org.lh.dmlj.schema.editor.command.MakeRecordViaCommandTest;
import org.lh.dmlj.schema.editor.command.MoveDiagramNodeCommandTest;
import org.lh.dmlj.schema.editor.command.MoveEndpointCommandTest;
import org.lh.dmlj.schema.editor.command.MoveGuideCommandTest;
import org.lh.dmlj.schema.editor.command.MoveRecordOrIndexToOtherAreaCommandTest;
import org.lh.dmlj.schema.editor.command.RemoveMemberFromSetCommandTest;
import org.lh.dmlj.schema.editor.command.ResizeDiagramNodeCommandTest;
import org.lh.dmlj.schema.editor.command.SetBooleanAttributeCommandTest;
import org.lh.dmlj.schema.editor.command.SetObjectAttributeCommandTest;
import org.lh.dmlj.schema.editor.command.SetShortAttributeCommandTest;
import org.lh.dmlj.schema.editor.command.SetZoomLevelCommandTest;
import org.lh.dmlj.schema.editor.command.helper.RemovableMemberRoleTest;
import org.lh.dmlj.schema.editor.command.helper.RemovableOwnerRoleTest;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcherTest;
import org.lh.dmlj.schema.editor.common.ToolsTest;
import org.lh.dmlj.schema.editor.outline.part.AbstractSchemaTreeEditPartTest;
import org.lh.dmlj.schema.editor.prefix.PointerFactoryTest;
import org.lh.dmlj.schema.editor.prefix.PointerTest;
import org.lh.dmlj.schema.editor.prefix.PointerToMoveTest;
import org.lh.dmlj.schema.editor.prefix.PointerToSetTest;
import org.lh.dmlj.schema.editor.prefix.PointerToUnsetTest;
import org.lh.dmlj.schema.editor.prefix.PrefixFactoryTest;
import org.lh.dmlj.schema.editor.prefix.PrefixForPointerAppendageTest;
import org.lh.dmlj.schema.editor.prefix.PrefixForPointerRemovalTest;
import org.lh.dmlj.schema.editor.prefix.PrefixUtilTest;


@RunWith(Suite.class)
@SuiteClasses({
	
	// command infrastructure tests
	ModelChangeDispatcherTest.class,
	
	// command tests
	AddMemberToSetCommandTest.class,
	ChangeAreaSpecificationCommandTest.class,
	ChangeCalcKeyCommandTest.class,
	ChangePointerOrderCommandTest.class,
	ChangeSetOrderCommandTest.class,
	ChangeSortKeysCommandTest.class,
	ChangeViaSpecificationCommandTest.class,
	CreateBendpointCommandTest.class,
	CreateConnectorCommandTest.class,
	CreateDiagramLabelCommandTest.class,
	CreateGuideCommandTest.class,
	CreateIndexCommandTest.class,
	CreateRecordCommandTest.class,
	CreateSetCommandTest.class,
	DeleteBendpointCommandTest.class,
	DeleteConnectorCommandTest.class,
	DeleteDiagramLabelCommandTest.class,
	DeleteGuideCommandTest.class,
	DeleteSetOrIndexCommandCreationAssistantTest.class,
	DeleteIndexCommandTest.class,
	DeleteSetCommandTest.class,
	LockEndpointsCommandTest.class,
	MakeRecordCalcCommandTest.class,
	MakeRecordDirectCommandTest.class,
	MakeRecordViaCommandTest.class,
	MoveDiagramNodeCommandTest.class,
	MoveEndpointCommandTest.class,
	MoveGuideCommandTest.class,
	MoveRecordOrIndexToOtherAreaCommandTest.class,
	PointerFactoryTest.class,
	PointerTest.class,
	PointerToMoveTest.class,	
	PointerToSetTest.class,
	PointerToUnsetTest.class,
	PrefixFactoryTest.class,
	PrefixForPointerAppendageTest.class,
	PrefixForPointerRemovalTest.class,
	PrefixUtilTest.class,
	RemovableMemberRoleTest.class,
	RemovableOwnerRoleTest.class,
	RemoveMemberFromSetCommandTest.class,
	ResizeDiagramNodeCommandTest.class,
	SetBooleanAttributeCommandTest.class,
	SetObjectAttributeCommandTest.class,
	SetShortAttributeCommandTest.class,
	SetZoomLevelCommandTest.class,
	
	// edit part tests
	AbstractSchemaTreeEditPartTest.class,
	
	// utility class tests
	ToolsTest.class
	
})
public class AllTests {	
}
