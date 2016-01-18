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
package org.lh.dmlj.schema.editor.command

import static org.junit.Assert.fail

import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.EList
import org.junit.Before
import org.junit.Test
import org.lh.dmlj.schema.DuplicatesOption
import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.Key
import org.lh.dmlj.schema.KeyElement
import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.SchemaPackage
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.ViaSpecification
import org.lh.dmlj.schema.editor.IPluginPropertiesProvider
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType

class ChangeLocationModeCommandFactoryTest {
	
	static final String LABEL_DUPLICATES_OPTION = 'duplicates option'
	static final String LABEL_SYBOLIC_DISPLACEMENT = 'symbolic displacement'
	static final String LABEL_DISPLACEMENT_PAGE_COUNT = 'displacement page count'
	
	Map pluginProperties = ['label.org.lh.dmlj.schema.Key.duplicatesOption' : LABEL_DUPLICATES_OPTION,
							'label.org.lh.dmlj.schema.ViaSpecification.symbolicDisplacementName' : LABEL_SYBOLIC_DISPLACEMENT,
							'label.org.lh.dmlj.schema.ViaSpecification.displacementPageCount' : LABEL_DISPLACEMENT_PAGE_COUNT]
	
	IPluginPropertiesProvider PLUGIN_PROPERTIES_PROVIDER =
		[ getProperty: { String name ->
			if (pluginProperties.containsKey(name)) {
				return pluginProperties.get(name)	
			} else {
				throw new IllegalArgumentException('pluginProperties incomplete: ' + name)
			}
		} ] as IPluginPropertiesProvider
	
	ChangeLocationModeCommandFactory factory

	@Before
	void setup() {
		factory = new ChangeLocationModeCommandFactory()
		// make sure plugin.properties are resolved using our own map:
		factory.pluginPropertiesProvider = PLUGIN_PROPERTIES_PROVIDER
	}
	
	@Test
	void testInvalidTargetLocationMode() {
			
		// setup the location mode details provider
		ILocationModeDetailsProvider invalidLocationModeProvider =
			[ getLocationMode: { null } ] as ILocationModeDetailsProvider
		
		[ LocationMode.CALC,
		  LocationMode.DIRECT,
		  LocationMode.VIA,
		  LocationMode.VSAM,
		  LocationMode.VSAM_CALC ].each { locationMode ->
			
			// setup the record with the appropriate location mode
			SchemaRecord record =
				[ getName: { 'TEST' }, getLocationMode: { locationMode } ] as SchemaRecord
			
			// specifying an invalid new location mode causes an IllegalArgumentException to be thrown
			try {
				factory.getCommand(record, invalidLocationModeProvider)
				fail "should throw an IllegalArgumentException: $locationMode"
			} catch (IllegalArgumentException e) {
				assert e.message == 'invalid target location mode: null'
			}
		 }
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testDirectToDirect() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeDirectProvider =
			[ getLocationMode: { LocationMode.DIRECT } ] as ILocationModeDetailsProvider
	
		// setup the record with the appropriate location mode
		SchemaRecord record = [ getName: { 'TEST' },
							    getLocationMode: { LocationMode.DIRECT } ] as SchemaRecord	
		
		// changing from DIRECT to DIRECT makes no sense and an IllegalArgumentException will be 
		// thrown
		factory.getCommand(record, locationModeDirectProvider)
	}

	@Test
	void testOtherThanDirectToDirect() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeDirectProvider =
			[ getLocationMode: { LocationMode.DIRECT } ] as ILocationModeDetailsProvider
			
		[ LocationMode.CALC, 
		  LocationMode.VIA, 
		  LocationMode.VSAM, 
		  LocationMode.VSAM_CALC ].each { locationMode -> 
			
		 	// setup the record with the appropriate location mode
			SchemaRecord record = 
				[ getName: { 'TEST' }, getLocationMode: { locationMode } ] as SchemaRecord
			
			// we expect the factory to produce a MakeRecordDirectCommand for our record 
			MakeRecordDirectCommand command = factory.getCommand(record, locationModeDirectProvider)
			assert command
			assert command.record == record
			
			// check the model change context
			ModelChangeContext context = command.context
			assert context
			assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
		 }	
	}
	
	@Test
	void testDirectToCalc() {
		
		// setup a dummy CALC key element
		Element calcKeyElement = [] as Element
		
		// setup the location mode details provider
		List calcKeyElements = [ calcKeyElement ]
		ILocationModeDetailsProvider locationModeCalcProvider =
			[ getLocationMode: { LocationMode.CALC },
			  getCalcKeyElements: { calcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.NOT_ALLOWED } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.DIRECT } ] as SchemaRecord
			
		// we expect the factory to produce a MakeRecordCalcCommand for our record
		MakeRecordCalcCommand command = factory.getCommand(record, locationModeCalcProvider)
		assert command
		assert command.record == record
		assert command.calcKeyElements == calcKeyElements
		assert command.duplicatesOption == DuplicatesOption.NOT_ALLOWED
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
	}

	@Test
	void testDirectToVia() {
		// setup the location mode details provider; note that we provide all possible data
		// regarding the displacement; under normal circumstances there would be at most 1 such item
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST' },
			  getSymbolicDisplacementName: { 'SDN' },
			  getDisplacementPageCount: { (short) 13 } ] as ILocationModeDetailsProvider
					
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.DIRECT } ] as SchemaRecord
		
		// we expect the factory to produce a MakeRecordViaCommand for our record
		MakeRecordViaCommand command = factory.getCommand(record, locationModeViaProvider)
		assert command
		assert command.record == record
		assert command.viaSetName == locationModeViaProvider.getViaSetName()
		assert command.symbolicDisplacementName == locationModeViaProvider.getSymbolicDisplacementName()
		assert command.displacementPageCount == locationModeViaProvider.getDisplacementPageCount()
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
	}

	@Test
	void testDirectToVsam() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeVsamProvider =
			[ getLocationMode: { LocationMode.VSAM } ] as ILocationModeDetailsProvider
					
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.DIRECT } ] as SchemaRecord
		
		// we expect the factory to produce a MakeRecordVsamCommand for our record
		MakeRecordVsamCommand command = factory.getCommand(record, locationModeVsamProvider)
		assert command
		assert command.record == record
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
	}

	@Test
	void testDirectToVsamCalc() {
		
		// setup a dummy CALC key element
		Element calcKeyElement = [] as Element
		
		// setup the location mode details provider
		List calcKeyElements = [ calcKeyElement ]
		ILocationModeDetailsProvider locationModeVsamCalcProvider =
			[ getLocationMode: { LocationMode.VSAM_CALC },
			  getCalcKeyElements: { calcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.UNORDERED } ] as ILocationModeDetailsProvider
					
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.DIRECT } ] as SchemaRecord
		
		// we expect the factory to produce a MakeRecordVsamCalcCommand for our record
		MakeRecordVsamCalcCommand command = factory.getCommand(record, locationModeVsamCalcProvider)
		assert command
		assert command.record == record
		assert command.calcKeyElements == calcKeyElements
		assert command.duplicatesOption == DuplicatesOption.UNORDERED
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
	}

	@Test
	void testChangeCalcDuplicatesOption() {
		
		// setup a dummy CALC key element
		Element retainedCalcKeyElement = [] as Element
		
		// setup the location mode details provider
		List calcKeyElements = [ retainedCalcKeyElement ]
		ILocationModeDetailsProvider locationModeCalcProvider =
			[ getLocationMode: { LocationMode.CALC },
			  getCalcKeyElements: { calcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.LAST } ] as ILocationModeDetailsProvider
		
		// define the record with the appropriate location mode
		KeyElement keyElement = [ getElement: { retainedCalcKeyElement } ] as KeyElement
		EList keyElements = [ keyElement ] as BasicEList
		Key calcKey = [ getElements: { getElements: keyElements},
						getDuplicatesOption: { DuplicatesOption.FIRST } ] as Key  
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.CALC },
			  getCalcKey: { calcKey } ] as SchemaRecord
			
		// we expect the factory to produce a SetObjectAttributeCommand for our CALC key 
		SetObjectAttributeCommand command = factory.getCommand(record, locationModeCalcProvider)
		assert command
		assert command.owner == calcKey
		assert command.features == [ SchemaPackage.eINSTANCE.getKey_DuplicatesOption() ]
		assert command.newValue == DuplicatesOption.LAST
		assert command.label == "Set '" + LABEL_DUPLICATES_OPTION + "' to 'LAST'"
		
		// check the model change context	
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_CALCKEY			
	}

	@Test
	void testOtherCalcKeyElement() {
		
		// setup the location mode details provider
		Element newCalcKeyElement = [] as Element
		List<Element> newCalcKeyElements = [ newCalcKeyElement ]
		ILocationModeDetailsProvider locationModeCalcProvider =
			[ getLocationMode: { LocationMode.CALC },
			  getCalcKeyElements: { newCalcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.LAST } ] as ILocationModeDetailsProvider
		
		// define the record with the appropriate location mode
		Element oldCalcKeyElement = [] as Element
		KeyElement oldKeyElement = [ getElement: { oldCalcKeyElement } ] as KeyElement
		EList oldKeyElements = [ oldKeyElement ] as BasicEList
		Key oldCalcKey = [ getElements: { getElements: oldKeyElements},
						   getDuplicatesOption: { DuplicatesOption.FIRST } ] as Key
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.CALC },
			  getCalcKey: { oldCalcKey } ] as SchemaRecord
			
		// we expect the factory to produce a ChangeCalcKeyCommand for our record 
		ChangeCalcKeyCommand command = factory.getCommand(record, locationModeCalcProvider)
		assert command
		assert command.record == record
		assert command.newCalcKeyElements == newCalcKeyElements
		assert command.newDuplicatesOption == DuplicatesOption.LAST
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_CALCKEY			
	}
	
	@Test
	void testAdditionalCalcKeyElement() {
		
		// setup a dummy CALC key element
		Element retainedCalcKeyElement = [] as Element
		
		// setup the location mode details provider
		Element newCalcKeyElement = [] as Element
		List<Element> newCalcKeyElements = [ retainedCalcKeyElement, newCalcKeyElement ]
		ILocationModeDetailsProvider locationModeCalcProvider =
			[ getLocationMode: { LocationMode.CALC },
			  getCalcKeyElements: { newCalcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.FIRST } ] as ILocationModeDetailsProvider
		
		// define the record with the appropriate location mode
		KeyElement retainedKeyElement = [ getElement: { retainedCalcKeyElement } ] as KeyElement
		EList retainedKeyElements = [ retainedKeyElement ] as BasicEList
		Key oldCalcKey = [ getElements: { getElements: retainedKeyElements},
						   getDuplicatesOption: { DuplicatesOption.FIRST } ] as Key
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.CALC },
			  getCalcKey: { oldCalcKey } ] as SchemaRecord
			
		// we expect the factory to produce a ChangeCalcKeyCommand for our record
		ChangeCalcKeyCommand command = factory.getCommand(record, locationModeCalcProvider)
		assert command
		assert command.record == record
		assert command.newCalcKeyElements == newCalcKeyElements
		assert command.newDuplicatesOption == DuplicatesOption.FIRST
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_CALCKEY
	}

	@Test
	void testCalcToVia() {
		// setup the location mode details provider; note that we provide all possible data 
		// regarding the displacement; under normal circumstances there would be at most 1 such item
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA }, 
			  getViaSetName: { 'TEST' },
			  getSymbolicDisplacementName: { 'SDN' },
			  getDisplacementPageCount: { (short) 13 } ] as ILocationModeDetailsProvider
					
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.CALC } ] as SchemaRecord
		
		// we expect the factory to produce a compound command
		ModelChangeCompoundCommand command = factory.getCommand(record, locationModeViaProvider)
		assert command 
		assert command.label == "Set 'Location mode' to 'VIA'"
		
		// check the individual commands
		assert command.commands.size == 2
		assert command.commands.getAt(0) instanceof MakeRecordDirectCommand
		assert command.commands.getAt(0).record == record
		assert command.commands.getAt(1) instanceof MakeRecordViaCommand
		assert command.commands.getAt(1).record == record
		assert command.commands.getAt(1).viaSetName == locationModeViaProvider.getViaSetName()
		assert command.commands.getAt(1).symbolicDisplacementName == locationModeViaProvider.getSymbolicDisplacementName()
		assert command.commands.getAt(1).displacementPageCount == locationModeViaProvider.getDisplacementPageCount()
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
	}
	
	@Test
	void testCalcToVsam() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeVsamProvider =
			[ getLocationMode: { LocationMode.VSAM } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.CALC } ] as SchemaRecord
		
		// we expect the factory to produce a compound command
		ModelChangeCompoundCommand command = factory.getCommand(record, locationModeVsamProvider)
		assert command
		assert command.label == "Set 'Location mode' to 'VSAM'"
		
		// check the individual commands
		assert command.commands.size == 2
		assert command.commands.getAt(0) instanceof MakeRecordDirectCommand
		assert command.commands.getAt(0).record == record
		assert command.commands.getAt(1) instanceof MakeRecordVsamCommand
		assert command.commands.getAt(1).record == record
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE		
	}
	
	@Test
	void testCalcToVsamCalc() {
		
		// setup a dummy CALC key element
		Element calcKeyElement = [] as Element
		
		// setup the location mode details provider
		List calcKeyElements = [ calcKeyElement ]
		ILocationModeDetailsProvider locationModeVsamCalcProvider =
			[ getLocationMode: { LocationMode.VSAM_CALC },
			  getCalcKeyElements: { calcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.UNORDERED } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.CALC } ] as SchemaRecord
			
		// we expect the factory to produce a compound command
		ModelChangeCompoundCommand command = factory.getCommand(record, locationModeVsamCalcProvider)
		assert command
		assert command.label == "Set 'Location mode' to 'VSAM CALC'"
		
		// check the individual commands
		assert command.commands.size == 2
		assert command.commands.getAt(0) instanceof MakeRecordDirectCommand
		assert command.commands.getAt(0).record == record
		assert command.commands.getAt(1) instanceof MakeRecordVsamCalcCommand
		assert command.commands.getAt(1).record == record
		assert command.commands.getAt(1).calcKeyElements == calcKeyElements
		assert command.commands.getAt(1).duplicatesOption == DuplicatesOption.UNORDERED
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
	}
	
	@Test
	void testViaToCalc() {
		
		// setup a dummy CALC key element
		Element calcKeyElement = [] as Element
		
		// setup the location mode details provider
		List calcKeyElements = [ calcKeyElement ]
		ILocationModeDetailsProvider locationModeCalcProvider =
			[ getLocationMode: { LocationMode.CALC },
			  getCalcKeyElements: { calcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.NOT_ALLOWED } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VIA } ] as SchemaRecord
		
		// we expect the factory to produce a compound command
		ModelChangeCompoundCommand command = factory.getCommand(record, locationModeCalcProvider)
		assert command
		assert command.label == "Set 'Location mode' to 'CALC'"
		
		// check the individual commands
		assert command.commands.size == 2
		assert command.commands.getAt(0) instanceof MakeRecordDirectCommand
		assert command.commands.getAt(0).record == record
		assert command.commands.getAt(1) instanceof MakeRecordCalcCommand
		assert command.commands.getAt(1).record == record
		assert command.commands.getAt(1).calcKeyElements == calcKeyElements
		assert command.commands.getAt(1).duplicatesOption == DuplicatesOption.NOT_ALLOWED
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
				
	}

	@Test
	void testChangeViaSetOnly() {
		// setup the location mode details provider; note that we provide all possible data
		// regarding the displacement; under normal circumstances there would be at most 1 such item
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST2' },
			  getSymbolicDisplacementName: { 'SDN1' },
			  getDisplacementPageCount: { (short) 100 } ] as ILocationModeDetailsProvider
					
		// define the record with the appropriate location mode
		org.lh.dmlj.schema.Set set = [ getName: { 'TEST1' } ] as org.lh.dmlj.schema.Set
		ViaSpecification viaSpecification =
			[ getSet: { set },
			  getSymbolicDisplacementName: { 'SDN1' },
			  getDisplacementPageCount: { (short) 100  } ] as ViaSpecification
		SchemaRecord record =
			[ getName: { 'TEST' }, 
			  getLocationMode: { LocationMode.VIA } ,
			  getViaSpecification: { viaSpecification } ] as SchemaRecord
		
		// we expect the factory to produce a ChangeViaSpecificationCommand for our record
		ChangeViaSpecificationCommand command = factory.getCommand(record, locationModeViaProvider)
		assert command
		assert command.record == record
		assert command.newViaSetName == locationModeViaProvider.getViaSetName()
		assert command.newSymbolicDisplacementName == locationModeViaProvider.getSymbolicDisplacementName()
		assert command.newDisplacementPageCount == locationModeViaProvider.getDisplacementPageCount()
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_VIA_SPECIFICATION
	}
	
	@Test
	void testChangeViaSetAndDisplacementSpecification() {
		// setup the location mode details provider; note that we provide all possible data
		// regarding the displacement; under normal circumstances there would be at most 1 such item
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST2' },
			  getSymbolicDisplacementName: { 'SDN2' },
			  getDisplacementPageCount: { (short) 200 } ] as ILocationModeDetailsProvider
					
		// define the record with the appropriate location mode
		org.lh.dmlj.schema.Set set = [ getName: { 'TEST1' } ] as org.lh.dmlj.schema.Set
		ViaSpecification viaSpecification =
			[ getSet: { set },
			  getSymbolicDisplacementName: { 'SDN1' },
			  getDisplacementPageCount: { (short) 100  } ] as ViaSpecification
		SchemaRecord record =
			[ getName: { 'TEST' },
			  getLocationMode: { LocationMode.VIA } ,
			  getViaSpecification: { viaSpecification } ] as SchemaRecord
		
		// we expect the factory to produce a ChangeViaSpecificationCommand for our record
		ChangeViaSpecificationCommand command = factory.getCommand(record, locationModeViaProvider)
		assert command
		assert command.record == record
		assert command.newViaSetName == locationModeViaProvider.getViaSetName()
		assert command.newSymbolicDisplacementName == locationModeViaProvider.getSymbolicDisplacementName()
		assert command.newDisplacementPageCount == locationModeViaProvider.getDisplacementPageCount()
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_VIA_SPECIFICATION
	}
	
	@Test
	void testNullifyViaSymbolicDisplacementName() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST' },
			  getSymbolicDisplacementName: { null },
			  getDisplacementPageCount: { null } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		org.lh.dmlj.schema.Set set = [ getName: { 'TEST' } ] as org.lh.dmlj.schema.Set
		ViaSpecification viaSpecification = 
	  		[ getSet: { set },
			  getSymbolicDisplacementName: { 'SDN1' } ] as ViaSpecification
		SchemaRecord record =
			[ getName: { 'TEST' },
			  getLocationMode: { LocationMode.VIA } ,
		      getViaSpecification: { viaSpecification } ] as SchemaRecord
		 
		 // we expect the factory to produce a SetObjectAttributeCommand for our record
		 SetObjectAttributeCommand command = factory.getCommand(record, locationModeViaProvider)
		 assert command
		 assert command.owner == record.getViaSpecification()
		 assert command.features == [ SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName() ]
		 assert !(command.newValue)
		 assert command.label == "Remove 'symbolic displacement'"
		 
		 // check the model change context
		 ModelChangeContext context = command.context
		 assert context
		 assert context.modelChangeType == ModelChangeType.CHANGE_VIA_SPECIFICATION
	}
	
	@Test
	void testNullifyViaDisplacementPageCount() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST' },
			  getSymbolicDisplacementName: { null },
			  getDisplacementPageCount: { null } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		org.lh.dmlj.schema.Set set = [ getName: { 'TEST' } ] as org.lh.dmlj.schema.Set
		ViaSpecification viaSpecification =
			  [ getSet: { set },
				getSymbolicDisplacementName: { null },
			    getDisplacementPageCount: { Short.valueOf((short) 100)  } ] as ViaSpecification
		SchemaRecord record =
			[ getName: { 'TEST' },
			  getLocationMode: { LocationMode.VIA } ,
			  getViaSpecification: { viaSpecification } ] as SchemaRecord
		 
		 // we expect the factory to produce a SetObjectAttributeCommand for our record
		 SetObjectAttributeCommand command = factory.getCommand(record, locationModeViaProvider)
		 assert command
		 assert command.owner == record.getViaSpecification()
		 assert command.features == [ SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount() ]
		 assert !(command.newValue)
		 assert command.label == "Remove 'displacement page count'"
		 
		 // check the model change context
		 ModelChangeContext context = command.context
		 assert context
		 assert context.modelChangeType == ModelChangeType.CHANGE_VIA_SPECIFICATION
	}
	
	@Test
	void testNoChangesWhenNoDisplacementAtAll() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST' },
			  getSymbolicDisplacementName: { null },
			  getDisplacementPageCount: { null } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		org.lh.dmlj.schema.Set set = [ getName: { 'TEST' } ] as org.lh.dmlj.schema.Set
		ViaSpecification viaSpecification =
			  [ getSet: { set },
				getSymbolicDisplacementName: { null },
				getDisplacementPageCount: { null  } ] as ViaSpecification
		SchemaRecord record =
			[ getName: { 'TEST' },
			  getLocationMode: { LocationMode.VIA } ,
			  getViaSpecification: { viaSpecification } ] as SchemaRecord
		 
		// specifying no changes at all makes no sense and an IllegalArgumentException will be thrown
		try { 
			factory.getCommand(record, locationModeViaProvider)
			fail 'should throw an IllegalArgumentException'
		} catch (IllegalArgumentException e) {
			assert e.message == 'no displacement specification was set'
		}		 
	}
	
	@Test
	void testSetViaSymbolicDisplacementNameWithoutDisplacementPageCountNullification() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST' },
			  getSymbolicDisplacementName: { 'SDN1' },
			  getDisplacementPageCount: { null } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		org.lh.dmlj.schema.Set set = [ getName: { 'TEST' } ] as org.lh.dmlj.schema.Set
		ViaSpecification viaSpecification =
			  [ getSet: { set },
				getSymbolicDisplacementName: { null },
				getDisplacementPageCount: { null  } ] as ViaSpecification
		SchemaRecord record =
			[ getName: { 'TEST' },
			  getLocationMode: { LocationMode.VIA } ,
			  getViaSpecification: { viaSpecification } ] as SchemaRecord
		 
		 // we expect the factory to produce a SetObjectAttributeCommand for our record
		 SetObjectAttributeCommand command = factory.getCommand(record, locationModeViaProvider)
		 assert command
		 assert command.owner == record.getViaSpecification()
		 assert command.features == [ SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName() ]
		 assert command.newValue == 'SDN1'
		 assert command.label == "Set 'symbolic displacement' to 'SDN1'"
		 
		 // check the model change context
		 ModelChangeContext context = command.context
		 assert context
		 assert context.modelChangeType == ModelChangeType.CHANGE_VIA_SPECIFICATION
	}
	
	@Test
	void testChangeViaSymbolicDisplacementName() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST' },
			  getSymbolicDisplacementName: { 'SDN2' },
			  getDisplacementPageCount: { null } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		org.lh.dmlj.schema.Set set = [ getName: { 'TEST' } ] as org.lh.dmlj.schema.Set
		ViaSpecification viaSpecification =
			  [ getSet: { set },
				getSymbolicDisplacementName: { 'SDN1' },
				getDisplacementPageCount: { null  } ] as ViaSpecification
		SchemaRecord record =
			[ getName: { 'TEST' },
			  getLocationMode: { LocationMode.VIA } ,
			  getViaSpecification: { viaSpecification } ] as SchemaRecord
		 
		 // we expect the factory to produce a SetObjectAttributeCommand for our record
		 SetObjectAttributeCommand command = factory.getCommand(record, locationModeViaProvider)
		 assert command
		 assert command.owner == record.getViaSpecification()
		 assert command.features == [ SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName() ]
		 assert command.newValue == 'SDN2'
		 assert command.label == "Set 'symbolic displacement' to 'SDN2'"
		 
		 // check the model change context
		 ModelChangeContext context = command.context
		 assert context
		 assert context.modelChangeType == ModelChangeType.CHANGE_VIA_SPECIFICATION
	}
	
	@Test
	void testSetViaSymbolicDisplacementNameWithDisplacementPageCountNullification() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST' },
			  getSymbolicDisplacementName: { 'SDN1' },
			  getDisplacementPageCount: { null } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		org.lh.dmlj.schema.Set set = [ getName: { 'TEST' } ] as org.lh.dmlj.schema.Set
		ViaSpecification viaSpecification =
			  [ getSet: { set },
				getSymbolicDisplacementName: { null },
				getDisplacementPageCount: { Short.valueOf((short) 100) } ] as ViaSpecification
		SchemaRecord record =
			[ getName: { 'TEST' },
			  getLocationMode: { LocationMode.VIA } ,
			  getViaSpecification: { viaSpecification } ] as SchemaRecord
		 
		 // we expect the factory to produce a compound command for our record
		 ModelChangeCompoundCommand command = factory.getCommand(record, locationModeViaProvider)
		 assert command
		 assert command.label == "Set 'symbolic displacement' to 'SDN1'"
		 
		 // check the individual commands
		 assert command.commands.size == 2
		 assert command.commands.getAt(0) instanceof SetObjectAttributeCommand
		 assert command.commands.getAt(0).owner == record.getViaSpecification()
		 assert command.commands.getAt(0).features == [ SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount() ]
		 assert command.commands.getAt(0).newValue == null
		 assert command.commands.getAt(1) instanceof SetObjectAttributeCommand
		 assert command.commands.getAt(1).owner == record.getViaSpecification()
		 assert command.commands.getAt(1).features == [ SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName() ]
		 assert command.commands.getAt(1).newValue == 'SDN1'
		 
		 // check the model change context
		 ModelChangeContext context = command.context
		 assert context
		 assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
		 
	}
	
	@Test
	void testSetViaDisplacementPageCountWithoutSymbolicDisplacementNameNullification() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST' },
			  getSymbolicDisplacementName: { null },
			  getDisplacementPageCount: { Short.valueOf((short) 100) } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		org.lh.dmlj.schema.Set set = [ getName: { 'TEST' } ] as org.lh.dmlj.schema.Set
		ViaSpecification viaSpecification =
			  [ getSet: { set },
				getSymbolicDisplacementName: { null },
				getDisplacementPageCount: { null  } ] as ViaSpecification
		SchemaRecord record =
			[ getName: { 'TEST' },
			  getLocationMode: { LocationMode.VIA } ,
			  getViaSpecification: { viaSpecification } ] as SchemaRecord
		 
		 // we expect the factory to produce a SetObjectAttributeCommand for our record
		 SetObjectAttributeCommand command = factory.getCommand(record, locationModeViaProvider)
		 assert command
		 assert command.owner == record.getViaSpecification()
		 assert command.features == [ SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount() ]
		 assert command.newValue == Short.valueOf((short) 100)
		 assert command.label == "Set 'displacement page count' to '100'"
		 
		 // check the model change context
		 ModelChangeContext context = command.context
		 assert context
		 assert context.modelChangeType == ModelChangeType.CHANGE_VIA_SPECIFICATION
	}
	
	@Test
	void testChangeViaDisplacementPageCount() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST' },
			  getSymbolicDisplacementName: { null },
			  getDisplacementPageCount: { Short.valueOf((short) 200) } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		org.lh.dmlj.schema.Set set = [ getName: { 'TEST' } ] as org.lh.dmlj.schema.Set
		ViaSpecification viaSpecification =
			  [ getSet: { set },
				getSymbolicDisplacementName: { null },
				getDisplacementPageCount: { Short.valueOf((short) 100)  } ] as ViaSpecification
		SchemaRecord record =
			[ getName: { 'TEST' },
			  getLocationMode: { LocationMode.VIA } ,
			  getViaSpecification: { viaSpecification } ] as SchemaRecord
		 
		 // we expect the factory to produce a SetObjectAttributeCommand for our record
		 SetObjectAttributeCommand command = factory.getCommand(record, locationModeViaProvider)
		 assert command
		 assert command.owner == record.getViaSpecification()
		 assert command.features == [ SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount() ]
		 assert command.newValue == Short.valueOf((short) 200)
		 assert command.label == "Set 'displacement page count' to '200'"
		 
		 // check the model change context
		 ModelChangeContext context = command.context
		 assert context
		 assert context.modelChangeType == ModelChangeType.CHANGE_VIA_SPECIFICATION
	}
	
	@Test
	void testSetViaDisplacementPageCountWithSymbolicDisplacementNameNullification() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeViaProvider =
			[ getLocationMode: { LocationMode.VIA },
			  getViaSetName: { 'TEST' },
			  getSymbolicDisplacementName: { null },
			  getDisplacementPageCount: { Short.valueOf((short) 100) } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		org.lh.dmlj.schema.Set set = [ getName: { 'TEST' } ] as org.lh.dmlj.schema.Set
		ViaSpecification viaSpecification =
			  [ getSet: { set },
				getSymbolicDisplacementName: { 'SDN1' },
				getDisplacementPageCount: { null  } ] as ViaSpecification
		SchemaRecord record =
			[ getName: { 'TEST' },
			  getLocationMode: { LocationMode.VIA } ,
			  getViaSpecification: { viaSpecification } ] as SchemaRecord
		 
		 // we expect the factory to produce a compound command for our record
		 ModelChangeCompoundCommand command = factory.getCommand(record, locationModeViaProvider)
		 assert command
		 assert command.label == "Set 'displacement page count' to '100'"
		 
		 // check the individual commands
		 assert command.commands.size == 2
		 assert command.commands.getAt(0) instanceof SetObjectAttributeCommand
		 assert command.commands.getAt(0).owner == record.getViaSpecification()
		 assert command.commands.getAt(0).features == [ SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName() ]
		 assert command.commands.getAt(0).newValue == null
		 assert command.commands.getAt(1) instanceof SetObjectAttributeCommand
		 assert command.commands.getAt(1).owner == record.getViaSpecification()
		 assert command.commands.getAt(1).features == [ SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount() ]
		 assert command.commands.getAt(1).newValue == Short.valueOf((short) 100) 
		 
		 // check the model change context
		 ModelChangeContext context = command.context
		 assert context
		 assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
	}
	
	@Test
	void testViaToVsam() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeVsamProvider =
			[ getLocationMode: { LocationMode.VSAM } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VIA } ] as SchemaRecord
		
		// we expect the factory to throw an IllegalArgumentException because the record participates
		// in at least 1 set
		try {
			factory.getCommand(record, locationModeVsamProvider)
			fail 'should throw an IllegalArgumentException'
		} catch (IllegalArgumentException e) {
			assert e.message == 'cannot change a record from VIA to VSAM'
		}	
	}
	
	@Test
	void testViaToVsamCalc() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeVsamCalcProvider =
			[ getLocationMode: { LocationMode.VSAM_CALC } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VIA } ] as SchemaRecord
		
		// we expect the factory to throw an IllegalArgumentException because the record participates
		// in at least 1 set
		try {
			factory.getCommand(record, locationModeVsamCalcProvider)
			fail 'should throw an IllegalArgumentException'
		} catch (IllegalArgumentException e) {
			assert e.message == 'cannot change a record from VIA to VSAM CALC'
		}
	}
	
	@Test
	void testVsamToCalc() {
		
		// setup a dummy CALC key element
		Element calcKeyElement = [] as Element
		
		// setup the location mode details provider
		List calcKeyElements = [ calcKeyElement ]
		ILocationModeDetailsProvider locationModeCalcProvider =
			[ getLocationMode: { LocationMode.CALC },
			  getCalcKeyElements: { calcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.NOT_ALLOWED } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VSAM } ] as SchemaRecord
		
		// we expect the factory to produce a compound command
		ModelChangeCompoundCommand command = factory.getCommand(record, locationModeCalcProvider)
		assert command
		assert command.label == "Set 'Location mode' to 'CALC'"
		
		// check the individual commands
		assert command.commands.size == 2
		assert command.commands.getAt(0) instanceof MakeRecordDirectCommand
		assert command.commands.getAt(0).record == record
		assert command.commands.getAt(1) instanceof MakeRecordCalcCommand
		assert command.commands.getAt(1).record == record
		assert command.commands.getAt(1).calcKeyElements == calcKeyElements
		assert command.commands.getAt(1).duplicatesOption == DuplicatesOption.NOT_ALLOWED
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
				
	}
	
	@Test
	void testVsamToVia() {
	
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeViaProvider =
		[ getLocationMode: { LocationMode.VIA } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VSAM } ] as SchemaRecord
			
		// we cannot make a VSAM record VIA because then it would be a member in at least 1 set
		try {
			factory.getCommand(record, locationModeViaProvider)
			fail 'should throw an IllegalArgumentException'
		} catch (IllegalArgumentException e) {
			assert e.message == 'cannot change a record from VSAM to VIA'
		}
	}

	@Test
	void testVsamToVsamCalc() {		
		
		// setup a dummy CALC key element
		Element calcKeyElement = [] as Element
		
		// setup the location mode details provider
		List calcKeyElements = [ calcKeyElement ]
		ILocationModeDetailsProvider locationModeVsamCalcProvider =
			[ getLocationMode: { LocationMode.VSAM_CALC },
			  getCalcKeyElements: { calcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.NOT_ALLOWED } ] as ILocationModeDetailsProvider
					
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VSAM } ] as SchemaRecord
		
		// we expect the factory to produce a MakeRecordVsamCalcCommand for our record
		MakeRecordVsamCalcCommand command = factory.getCommand(record, locationModeVsamCalcProvider)
		assert command
		assert command.record == record
		assert command.calcKeyElements == calcKeyElements
		assert command.duplicatesOption == DuplicatesOption.NOT_ALLOWED
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE				
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testVsamToVsam() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeDirectProvider =
			[ getLocationMode: { LocationMode.VSAM } ] as ILocationModeDetailsProvider
	
		// setup the record with the appropriate location mode
		SchemaRecord record = [ getName: { 'TEST' },
								getLocationMode: { LocationMode.VSAM } ] as SchemaRecord
		
		// changing from DIRECT to DIRECT makes no sense and an IllegalArgumentException will be
		// thrown
		factory.getCommand(record, locationModeDirectProvider)
	}
	
	@Test
	void testVsamCalcToCalc() {
		
		// setup a dummy CALC key element
		Element calcKeyElement = [] as Element
		
		// setup the location mode details provider
		List calcKeyElements = [ calcKeyElement ]
		ILocationModeDetailsProvider locationModeCalcProvider =
			[ getLocationMode: { LocationMode.CALC },
			  getCalcKeyElements: { calcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.NOT_ALLOWED } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VSAM_CALC } ] as SchemaRecord
		
		// we expect the factory to produce a compound command
		ModelChangeCompoundCommand command = factory.getCommand(record, locationModeCalcProvider)
		assert command
		assert command.label == "Set 'Location mode' to 'CALC'"
		
		// check the individual commands
		assert command.commands.size == 2
		assert command.commands.getAt(0) instanceof MakeRecordDirectCommand
		assert command.commands.getAt(0).record == record
		assert command.commands.getAt(1) instanceof MakeRecordCalcCommand
		assert command.commands.getAt(1).record == record
		assert command.commands.getAt(1).calcKeyElements == calcKeyElements
		assert command.commands.getAt(1).duplicatesOption == DuplicatesOption.NOT_ALLOWED
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
	}
	
	@Test
	void testVsamCalcToVia() {
	
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeViaProvider =
		[ getLocationMode: { LocationMode.VIA } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VSAM_CALC } ] as SchemaRecord
			
		// we cannot make a VSAM record VIA because then it would be a member in at least 1 set
		try {
			factory.getCommand(record, locationModeViaProvider)
			fail 'should throw an IllegalArgumentException'
		} catch (IllegalArgumentException e) {
			assert e.message == 'cannot change a record from VSAM CALC to VIA'
		}
	}
	
	@Test
	void testVsamCalcToVsam() {
		
		// setup the location mode details provider
		ILocationModeDetailsProvider locationModeVsamProvider =
			[ getLocationMode: { LocationMode.VSAM } ] as ILocationModeDetailsProvider
		  
		// define the record with the appropriate location mode
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VSAM_CALC } ] as SchemaRecord
		
		// we expect the factory to produce a MakeRecordVsamCommand
		MakeRecordVsamCommand command = factory.getCommand(record, locationModeVsamProvider)
		assert command	
		assert command.record == record
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_LOCATION_MODE
	}
	
	@Test
	void testChangeVsamCalcDuplicatesOption() {
		
		// setup a dummy CALC key element
		Element retainedCalcKeyElement = [] as Element
		
		// setup the location mode details provider
		List calcKeyElements = [ retainedCalcKeyElement ]
		ILocationModeDetailsProvider locationModeCalcProvider =
			[ getLocationMode: { LocationMode.VSAM_CALC },
			  getCalcKeyElements: { calcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.UNORDERED } ] as ILocationModeDetailsProvider
		
		// define the record with the appropriate location mode
		KeyElement keyElement = [ getElement: { retainedCalcKeyElement } ] as KeyElement
		EList keyElements = [ keyElement ] as BasicEList
		Key calcKey = [ getElements: { getElements: keyElements},
						getDuplicatesOption: { DuplicatesOption.NOT_ALLOWED } ] as Key
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VSAM_CALC },
			  getCalcKey: { calcKey } ] as SchemaRecord
			
		// we expect the factory to produce a SetObjectAttributeCommand for our CALC key
		SetObjectAttributeCommand command = factory.getCommand(record, locationModeCalcProvider)
		assert command
		assert command.owner == calcKey
		assert command.features == [ SchemaPackage.eINSTANCE.getKey_DuplicatesOption() ]
		assert command.newValue == DuplicatesOption.UNORDERED
		assert command.label == "Set '" + LABEL_DUPLICATES_OPTION + "' to 'UNORDERED'"
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_CALCKEY
	}

	@Test
	void testOtherVsamCalcKeyElement() {
		
		// setup the location mode details provider
		Element newCalcKeyElement = [] as Element
		List<Element> newCalcKeyElements = [ newCalcKeyElement ]
		ILocationModeDetailsProvider locationModeCalcProvider =
			[ getLocationMode: { LocationMode.VSAM_CALC },
			  getCalcKeyElements: { newCalcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.NOT_ALLOWED } ] as ILocationModeDetailsProvider
		
		// define the record with the appropriate location mode
		Element oldCalcKeyElement = [] as Element
		KeyElement oldKeyElement = [ getElement: { oldCalcKeyElement } ] as KeyElement
		EList oldKeyElements = [ oldKeyElement ] as BasicEList
		Key oldCalcKey = [ getElements: { getElements: oldKeyElements},
						   getDuplicatesOption: { DuplicatesOption.UNORDERED } ] as Key
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VSAM_CALC },
			  getCalcKey: { oldCalcKey } ] as SchemaRecord
			
		// we expect the factory to produce a ChangeCalcKeyCommand for our record
		ChangeCalcKeyCommand command = factory.getCommand(record, locationModeCalcProvider)
		assert command
		assert command.record == record
		assert command.newCalcKeyElements == newCalcKeyElements
		assert command.newDuplicatesOption == DuplicatesOption.NOT_ALLOWED
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_CALCKEY
	}
	
	@Test
	void testAdditionalVsamCalcKeyElement() {
		
		// setup a dummy CALC key element
		Element retainedCalcKeyElement = [] as Element
		
		// setup the location mode details provider
		Element newCalcKeyElement = [] as Element
		List<Element> newCalcKeyElements = [ retainedCalcKeyElement, newCalcKeyElement ]
		ILocationModeDetailsProvider locationModeCalcProvider =
			[ getLocationMode: { LocationMode.VSAM_CALC },
			  getCalcKeyElements: { newCalcKeyElements },
			  getDuplicatesOption: { DuplicatesOption.NOT_ALLOWED } ] as ILocationModeDetailsProvider
		
		// define the record with the appropriate location mode
		KeyElement retainedKeyElement = [ getElement: { retainedCalcKeyElement } ] as KeyElement
		EList retainedKeyElements = [ retainedKeyElement ] as BasicEList
		Key oldCalcKey = [ getElements: { getElements: retainedKeyElements},
						   getDuplicatesOption: { DuplicatesOption.NOT_ALLOWED } ] as Key
		SchemaRecord record =
			[ getName: { 'TEST' }, getLocationMode: { LocationMode.VSAM_CALC },
			  getCalcKey: { oldCalcKey } ] as SchemaRecord
			
		// we expect the factory to produce a ChangeCalcKeyCommand for our record
		ChangeCalcKeyCommand command = factory.getCommand(record, locationModeCalcProvider)
		assert command
		assert command.record == record
		assert command.newCalcKeyElements == newCalcKeyElements
		assert command.newDuplicatesOption == DuplicatesOption.NOT_ALLOWED
		
		// check the model change context
		ModelChangeContext context = command.context
		assert context
		assert context.modelChangeType == ModelChangeType.CHANGE_CALCKEY
	}
	
}
