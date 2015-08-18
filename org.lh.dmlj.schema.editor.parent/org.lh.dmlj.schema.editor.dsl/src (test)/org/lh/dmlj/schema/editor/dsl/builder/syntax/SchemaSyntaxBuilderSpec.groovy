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
package org.lh.dmlj.schema.editor.dsl.builder.syntax

import static org.junit.Assert.fail

import org.lh.dmlj.schema.Schema

class SchemaSyntaxBuilderSpec extends AbstractSyntaxBuilderSpec {
	
	def "EMPSCHM version 100"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		//capture(syntax, 'testdata/EMPSCHM version 100.syntax')
		
		then: "the builder creates the syntax that describes the schema"		
		mustMatchSyntaxInFile(syntax, 'testdata/EMPSCHM version 100.syntax')		
	}
	
	def "IDMSNTWK version 1 (Release 18.5)"() {
		
		given: "a schema syntax builder and the IDMSNTWK version 1 (Release 18.5) schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		//capture(syntax, 'testdata/IDMSNTWK version 1 (Release 18.5).syntax')
		
		then: "the builder creates the syntax that describes the schema"		
		mustMatchSyntaxInFile(syntax, 'testdata/IDMSNTWK version 1 (Release 18.5).syntax')
	}
	
	def "Schema without a description"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		schema.description = null
		assert schema.comments
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"		
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
comments 'INSTALLATION: COMMONWEATHER CORPORATION')

diagram {
"""))
	}
	
	def "Schema with a memo date"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		schema.memoDate = '13/08/64'
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
memoDate '13/08/64'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')

diagram {
"""))
	}
	
	def "Schema without a diagram label"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		schema.diagramData.label = null // note: we do not remove the diagram label LOCATION
		assert schema.diagramData.showRulers
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    showRulersAndGuides
"""))
	}
	
	def "Schema with default zoom level"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		schema.diagramData.zoomLevel = 1.0
		assert schema.diagramData.showRulers
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    showRulersAndGuides
"""))
	}
	
	def "Schema with non-default zoom level"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		schema.diagramData.zoomLevel = 2.0
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    zoom 200
    showRulersAndGuides
"""))
	}
	
	def "Schema with showRulersAndGuides"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		assert schema.diagramData.showRulers
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    showRulersAndGuides
"""))
	}
	
	def "Schema without showRulersAndGuides"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		schema.diagramData.showRulers = false
		assert schema.diagramData.showGrid
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    showGrid
"""))
	}
	
	def "Schema with showGrid"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		assert schema.diagramData.showRulers
		assert schema.diagramData.showGrid == true
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    showRulersAndGuides
    showGrid
"""))
	}
	
	def "Schema without showGrid"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		assert schema.diagramData.showRulers
		schema.diagramData.showGrid = false
		assert schema.diagramData.snapToGuides
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    showRulersAndGuides
    snapToGuides
"""))
	}
	
	def "Schema with snapToGuides"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		assert schema.diagramData.showRulers
		assert schema.diagramData.showGrid
		assert schema.diagramData.snapToGuides
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    showRulersAndGuides
    showGrid
    snapToGuides
"""))
	}
	
	def "Schema without snapToGuides"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		assert schema.diagramData.showRulers
		assert schema.diagramData.showGrid
		schema.diagramData.snapToGuides = false
		assert schema.diagramData.snapToGrid
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    showRulersAndGuides
    showGrid
    snapToGrid
"""))
	}
	
	def "Schema with snapToGrid"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		assert schema.diagramData.showRulers
		assert schema.diagramData.showGrid
		assert schema.diagramData.snapToGuides
		assert schema.diagramData.snapToGrid
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    showRulersAndGuides
    showGrid
    snapToGuides
    snapToGrid
"""))
	}
	
	def "Schema without snapToGrid"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		assert schema.diagramData.showRulers
		assert schema.diagramData.showGrid
		assert schema.diagramData.snapToGuides
		schema.diagramData.snapToGrid = false
		assert schema.diagramData.snapToGeometry
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    showRulersAndGuides
    showGrid
    snapToGuides
    snapToGeometry
"""))
	}
	
	def "Schema with snapToGeometry"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		assert schema.diagramData.showRulers
		assert schema.diagramData.showGrid
		assert schema.diagramData.snapToGuides
		assert schema.diagramData.snapToGrid
		assert schema.diagramData.snapToGeometry
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    showRulersAndGuides
    showGrid
    snapToGuides
    snapToGrid
    snapToGeometry
}
"""))
	}
	
	def "Schema without snapToGeometry"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		assert schema.diagramData.showRulers
		assert schema.diagramData.showGrid
		assert schema.diagramData.snapToGuides
		assert schema.diagramData.snapToGrid
		schema.diagramData.snapToGeometry = false
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"
		syntax.startsWith(expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION')
 
diagram {
    label {
        description "Employee Demo Database"
        x 33
        y 551
        width 169
        height 45
    }
    showRulersAndGuides
    showGrid
    snapToGuides
    snapToGrid
}
"""))
	}
	
}
