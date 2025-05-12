/**
 * Copyright (C) 2020  Luc Hermans
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

import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.editor.dsl.builder.model.SchemaModelBuilder

class SchemaSyntaxBuilderSpec extends AbstractSyntaxBuilderSpec {
	
	def "EMPSCHM version 100"() {
		
		given: "a schema syntax builder and the EMPSCHM version 100 schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"		
		mustMatchSyntaxInFile(syntax, 'testdata/EMPSCHM version 100.schemadsl')		
	}
	
	def "IDMSNTWK version 1 (Release 18.5)"() {
		
		given: "a schema syntax builder and the IDMSNTWK version 1 (Release 18.5) schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema"		
		mustMatchSyntaxInFile(syntax, 'testdata/IDMSNTWK version 1 (Release 18.5).schemadsl')
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'

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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'

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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
    horizontalGuides"""))
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
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
    horizontalGuides"""))
	}
	
	def "Vertical ruler owned guides are horizontal guides"() {
		
		given: "a schema with 3 vertical ruler owned guides and a schema syntax builder"
		Schema schema = new SchemaModelBuilder().build {
			name 'TESTSCHM'
			version 1
			
			diagram {
				showRulersAndGuides
				horizontalGuides '1,2,3'
			}
		}
		SchemaSyntaxBuilder builder = 
			new SchemaSyntaxBuilder(generateAreaDSL : false, generateRecordDSL : false, generateSetDSL : false)
		
		expect: "no horizontal ruler guides and 3 vertical ruler owned guides"
		!schema.diagramData.horizontalRuler.guides
		schema.diagramData.verticalRuler.guides.size() == 3
		
		when: "building the DSL"
		String syntax = builder.build(schema)
		
		then: "the DSL contains the definition for the 3 horizontal guides"
		syntax.startsWith(expected(
"""
name 'TESTSCHM'
version 1
 
diagram {
    showRulersAndGuides
    horizontalGuides '1,2,3'
}
"""))
	}
	
	def "Horizontal ruler owned guides are vertical guides"() {
		
		given: "a schema with 3 horizontal ruler owned guides and a schema syntax builder"
		Schema schema = new SchemaModelBuilder().build {
			name 'TESTSCHM'
			version 1
			
			diagram {
				verticalGuides '4,5,6'
			}
		}
		SchemaSyntaxBuilder builder =
			new SchemaSyntaxBuilder(generateAreaDSL : false, generateRecordDSL : false, generateSetDSL : false)
		
		expect: "3 horizontal ruler owned guides and no vertical ruler guides"
		schema.diagramData.horizontalRuler.guides.size() == 3
		!schema.diagramData.verticalRuler.guides		
		
		when: "building the DSL"
		String syntax = builder.build(schema)
		
		then: "the DSL contains the definition for the 3 vertical guides"
		syntax.startsWith(expected(
"""
name 'TESTSCHM'
version 1
 
diagram {
    verticalGuides '4,5,6'
}
"""))
	}
	
	def "Horizontal guides preceed vertical guides"() {
		
		given: "a schema with 4 horizontal ruler owned guides and 5 vertical ruler owned guides and "
			   "a schema syntax builder"
		Schema schema = new SchemaModelBuilder().build {
			name 'TESTSCHM'
			version 1
			
			diagram {
				showRulersAndGuides
				verticalGuides '1,2,3,4'
				horizontalGuides '5,6,7,8,9'
			}
		}
		SchemaSyntaxBuilder builder =
			new SchemaSyntaxBuilder(generateAreaDSL : false, generateRecordDSL : false, generateSetDSL : false)
		
		expect: "4 horizontal ruler owned guides and 5 vertical ruler guides"
		schema.diagramData.horizontalRuler.guides.size() == 4
		schema.diagramData.verticalRuler.guides.size() == 5
		
		when: "building the DSL"
		String syntax = builder.build(schema)
		
		then: "in the DSL, the vertical ruler owned guides preceed those owned by the horizontal ruler"
		syntax.startsWith(expected(
"""
name 'TESTSCHM'
version 1
 
diagram {
    showRulersAndGuides
    horizontalGuides '5,6,7,8,9'
    verticalGuides '1,2,3,4'
}
"""))
	}
	
	def "schema with an area that does NOT contain any record (e.g. DDLDCSCR)"() {
		
		given: "a schema syntax builder and the IDMSNTWK version 1 (Release 18.5) schema"
		SchemaSyntaxBuilder builder = new SchemaSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		assert schema
		assert schema.getArea('DDLDCSCR') // this area does NOT contain any records
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema, explicitly defining the"
			  "DDLDCSCR area (mind that the area body is suppressed)"
		syntax.indexOf("area 'DDLDCSCR'\n") > -1
	}
	
	def "suppress the generation of area, record and set DSL"() {
		
		given: "a DSL syntax builder configured for not generating the DSL for areas, records and sets"
		SchemaSyntaxBuilder builder = 
			new SchemaSyntaxBuilder(generateAreaDSL : false, generateRecordDSL : false, generateSetDSL : false)
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		assert schema
		
		when: "passing the schema to the builder's build method"
		String syntax = builder.build(schema)
		
		then: "the builder creates the syntax that describes the schema but suppresses the DSL"
		      "for areas, records and sets"
		syntax == expected(
"""
name 'EMPSCHM'
version 100
description 'EMPLOYEE DEMO DATABASE'
comments 'INSTALLATION: COMMONWEATHER CORPORATION'
 
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
    horizontalGuides '199,344,431'
    verticalGuides '253,579'
}

// suppressed: area DSL

// suppressed: record DSL

// suppressed: set DSL
""")
	}
	
}
