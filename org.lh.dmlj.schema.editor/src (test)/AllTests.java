/**
 * Copyright (C) 2013  Luc Hermans
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
import org.lh.dmlj.schema.editor.command.ChangeAreaSpecificationCommandTest;
import org.lh.dmlj.schema.editor.command.ChangeCalcKeyCommandTest;
import org.lh.dmlj.schema.editor.command.ChangeViaSpecificationCommandTest;
import org.lh.dmlj.schema.editor.command.CreateBendpointCommandTest;
import org.lh.dmlj.schema.editor.command.CreateConnectorCommandTest;
import org.lh.dmlj.schema.editor.command.CreateDiagramLabelCommandTest;
import org.lh.dmlj.schema.editor.command.CreateGuideCommandTest;
import org.lh.dmlj.schema.editor.command.CreateIndexCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteBendpointCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteConnectorCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteDiagramLabelCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteGuideCommandTest;
import org.lh.dmlj.schema.editor.command.DeleteIndexCommandTest;
import org.lh.dmlj.schema.editor.command.LockEndpointsCommandTest;
import org.lh.dmlj.schema.editor.command.MakeRecordCalcCommandTest;
import org.lh.dmlj.schema.editor.command.MakeRecordDirectCommandTest;
import org.lh.dmlj.schema.editor.command.MakeRecordViaCommandTest;
import org.lh.dmlj.schema.editor.command.MoveDiagramNodeCommandTest;
import org.lh.dmlj.schema.editor.command.MoveEndpointCommandTest;
import org.lh.dmlj.schema.editor.command.MoveGuideCommandTest;
import org.lh.dmlj.schema.editor.command.MoveRecordOrIndexToOtherAreaCommandTest;
import org.lh.dmlj.schema.editor.command.ResizeDiagramNodeCommandTest;
import org.lh.dmlj.schema.editor.command.SetBooleanAttributeCommandTest;
import org.lh.dmlj.schema.editor.command.SetObjectAttributeCommandTest;
import org.lh.dmlj.schema.editor.command.SetShortAttributeCommandTest;
import org.lh.dmlj.schema.editor.command.SetZoomLevelCommandTest;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcherTest;


@RunWith(Suite.class)
@SuiteClasses({
	
	// command infrastructure tests
	ModelChangeDispatcherTest.class,
	
	// command tests
	ChangeAreaSpecificationCommandTest.class,
	ChangeCalcKeyCommandTest.class,
	ChangeViaSpecificationCommandTest.class,
	CreateBendpointCommandTest.class,
	CreateConnectorCommandTest.class,
	CreateDiagramLabelCommandTest.class,
	CreateGuideCommandTest.class,
	CreateIndexCommandTest.class,
	DeleteBendpointCommandTest.class,
	DeleteConnectorCommandTest.class,
	DeleteDiagramLabelCommandTest.class,
	DeleteGuideCommandTest.class,
	DeleteIndexCommandTest.class,
	LockEndpointsCommandTest.class,
	MakeRecordCalcCommandTest.class,
	MakeRecordDirectCommandTest.class,
	MakeRecordViaCommandTest.class,
	MoveDiagramNodeCommandTest.class,
	MoveEndpointCommandTest.class,
	MoveGuideCommandTest.class,
	MoveRecordOrIndexToOtherAreaCommandTest.class,
	ResizeDiagramNodeCommandTest.class,
	SetBooleanAttributeCommandTest.class,
	SetObjectAttributeCommandTest.class,
	SetShortAttributeCommandTest.class,
	SetZoomLevelCommandTest.class
	
})
public class AllTests {	
}
