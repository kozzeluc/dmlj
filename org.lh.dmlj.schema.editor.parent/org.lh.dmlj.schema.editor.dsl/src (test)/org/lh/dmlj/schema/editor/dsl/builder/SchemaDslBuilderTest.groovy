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

import static org.junit.Assert.fail

import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.junit.Before
import org.junit.Test
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaPackage;

class SchemaDslBuilderTest {
	
	SchemaDslBuilder builder

	private static Schema loadSchema(String path) {
		
		URI uri = URI.createFileURI(new File(path).getAbsolutePath());
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
					  .getExtensionToFactoryMap()
					  .put("schema", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.getResource(uri, true);
		Schema schema = (Schema) resource.getContents().get(0);
		
		return schema;
	}
	
	@Before
	void setup() {
		SchemaPackage.eINSTANCE
		builder = new SchemaDslBuilder()
	}
	
	@Test
	void idmsntwk() {
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		String dsl = builder.build(schema)
		assert dsl
		println dsl		
	}
	
}
