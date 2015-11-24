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
package org.lh.dmlj.schema.editor.dsl.builder

import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl
import org.eclipse.emf.ecore.xmi.util.XMLProcessor
import org.lh.dmlj.schema.ConnectionLabel
import org.lh.dmlj.schema.ConnectionPart
import org.lh.dmlj.schema.Connector
import org.lh.dmlj.schema.DiagramLocation
import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaPackage
import org.lh.dmlj.schema.SchemaRecord

import spock.lang.Specification

class AbstractBuilderSpec extends Specification {
	
	private SchemaPackage schemaPackage = SchemaPackage.eINSTANCE

	protected Schema loadSchema(String path) {
		URI uri = URI.createFileURI(new File(path).getAbsolutePath())
		ResourceSet resourceSet = new ResourceSetImpl()
		resourceSet.resourceFactoryRegistry
				   .extensionToFactoryMap.put('schema', new XMIResourceFactoryImpl())
		Resource resource = resourceSet.getResource(uri, true)
		return resource.contents[0]
	}
	
	protected List<String> toCompareFriendlyXmi(Schema schema) {
		
		List<SchemaArea> areas = new ArrayList<>(schema.areas)
		areas.sort( { SchemaArea area1, SchemaArea area2 -> area1.name <=> area2.name } )
		schema.areas.clear()
		areas.each { it.schema = schema }
		
		List<DiagramLocation> diagramLocations = new ArrayList<>(schema.diagramData.locations)
		diagramLocations.sort( { DiagramLocation loc1, DiagramLocation loc2 -> 
			loc1.eyecatcher <=> loc2.eyecatcher 
		} )
		schema.diagramData.locations.clear()
		diagramLocations.each { schema.diagramData.locations << it }
		
		List<ConnectionLabel> connectionLabels = new ArrayList<>(schema.diagramData.connectionLabels)
		connectionLabels.sort( { ConnectionLabel label1, ConnectionLabel label2 ->
			label1.diagramLocation.eyecatcher <=> label2.diagramLocation.eyecatcher
		} )
		schema.diagramData.connectionLabels.clear()
		connectionLabels.each { schema.diagramData.connectionLabels << it }
		
		List<ConnectionPart> connectionParts = new ArrayList<>(schema.diagramData.connectionParts)
		connectionParts.sort( { ConnectionPart part1, ConnectionPart part2 ->
			String p1 =
				"${part1.memberRole.set.name}_${part1.memberRole.record.name}_${part1.memberRole.connectionParts.indexOf(part1)}" 
			String p2 = 
				"${part2.memberRole.set.name}_${part2.memberRole.record.name}_${part2.memberRole.connectionParts.indexOf(part2)}"
			p1 <=> p2
		} )
		schema.diagramData.connectionParts.clear()
		connectionParts.each { schema.diagramData.connectionParts << it }
		
		List<Connector> connectors = new ArrayList<>(schema.diagramData.connectors)
		connectors.sort( { Connector connector1, Connector connector2 ->
			connector1.diagramLocation.eyecatcher <=> connector2.diagramLocation.eyecatcher
		} )
		schema.diagramData.connectors.clear()
		connectors.each { schema.diagramData.connectors << it }
		
		Resource resource = new XMIResourceImpl()
		XMLProcessor xmlProcessor = new XMLProcessor()
		resource.getContents().addAll(schema)
		String xmi = xmlProcessor.saveToString(resource, null);
		
		List<String> list = [ ]
		xmi.split().each { list << it }
		list
	}
	
}
