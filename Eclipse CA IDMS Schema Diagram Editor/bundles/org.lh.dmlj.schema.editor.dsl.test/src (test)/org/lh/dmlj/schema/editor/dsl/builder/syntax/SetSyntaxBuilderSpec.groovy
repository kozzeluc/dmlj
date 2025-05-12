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

import org.lh.dmlj.schema.DuplicatesOption
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaFactory
import org.lh.dmlj.schema.SetMembershipOption
import org.lh.dmlj.schema.SetMode
import org.lh.dmlj.schema.SetOrder
import org.lh.dmlj.schema.VsamIndex

import spock.lang.Unroll

class SetSyntaxBuilderSpec extends AbstractSyntaxBuilderSpec {

	@Unroll
	def "Order"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('DEPT-EMPLOYEE')
		set.order = order
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set"
		syntax.startsWith(expected(
"""
name 'DEPT-EMPLOYEE'
$syntaxFragment
owner 'DEPARTMENT' {
    next 1
    prior 2
}

member 'EMPLOYEE' {
"""))
		
		where:
		order 		   	| syntaxFragment
		SetOrder.NEXT  	| ''						// default
		SetOrder.PRIOR 	| "\norder 'PRIOR'\n"
		SetOrder.FIRST 	| "\norder 'FIRST'\n"
		SetOrder.LAST  	| "\norder 'LAST'\n"
		SetOrder.SORTED | "\norder 'SORTED'\n"
	}
	
	def "Index is suppressed for chained sets"() {
		
		given: "a record syntax builder and the EMPSCHM version 100 schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('DEPT-EMPLOYEE')
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set: no index property is"
			  "generated which denotes a chained set"
		syntax.startsWith(expected(
"""
name 'DEPT-EMPLOYEE'

order 'SORTED'

owner 'DEPARTMENT' {
    next 1
    prior 2
}

member 'EMPLOYEE' {
"""))
	}
	
	@Unroll
	def "Index (indexed sets)"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('OFFICE-EMPLOYEE')
		set.indexedSetModeSpecification.symbolicIndexName = symbolicIndexName
		set.indexedSetModeSpecification.keyCount = keyCount
		set.indexedSetModeSpecification.displacementPageCount = displacementPageCount
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set: an index property is"
			  "generated which denotes an indexed set"
		syntax.startsWith(expected(
"""
name 'OFFICE-EMPLOYEE'

order 'SORTED'

index $syntaxFragment

owner 'OFFICE' {
    next 1
    prior 2
}

member 'EMPLOYEE' {
"""))
		
		where:
		symbolicIndexName | keyCount | displacementPageCount | syntaxFragment
		'SYMBOL1'		  | null 	 | null 				 | "'SYMBOL1'"
		null		  	  | 10 	 	 | null 				 | "{\n    keys 10\n}"
		null		  	  | 10	 	 | 20 					 | "{\n    keys 10\n    displacement 20\n}"
	}
	
	@Unroll
	def "Owner record"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('DEPT-EMPLOYEE')
		set.owner.priorDbkeyPosition = priorDbkeyPosition
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set: the owner record's prior"
			  "pointer is optional"
		syntax.startsWith(expected(
"""
name 'DEPT-EMPLOYEE'

order 'SORTED'

owner 'DEPARTMENT' {
    next 1$syntaxFragment
}

member 'EMPLOYEE' {
"""))
		
		where:
		priorDbkeyPosition | syntaxFragment
		null			   | ''
		2			   	   | '\n    prior 2'
	}
	
	@Unroll
	def "System owner"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('EMP-NAME-NDX')
		if (symbolicSubareaName) {
			set.systemOwner.areaSpecification.symbolicSubareaName = symbolicSubareaName
		} else if (offsetPageCount || offsetPercent || pageCount || percent) {
			set.systemOwner.areaSpecification.offsetExpression = 
				SchemaFactory.eINSTANCE.createOffsetExpression()
			set.systemOwner.areaSpecification.offsetExpression.offsetPageCount = offsetPageCount
			set.systemOwner.areaSpecification.offsetExpression.offsetPercent = offsetPercent
			set.systemOwner.areaSpecification.offsetExpression.pageCount = pageCount
			set.systemOwner.areaSpecification.offsetExpression.percent = percent
		} else {
			set.systemOwner.areaSpecification.symbolicSubareaName = null
			set.systemOwner.areaSpecification.offsetExpression = null
		}
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set: a system owner element is"
			  "generated"
		syntax.startsWith(expected(
"""
name 'EMP-NAME-NDX'

order 'SORTED'

index {
    keys 40
}

systemOwner {
    area 'EMP-DEMO-REGION'$syntaxFragment

    diagram {
        x 274
        y 190
    }
}

member 'EMPLOYEE' {
"""))
		
		where:
		symbolicSubareaName | offsetPageCount | offsetPercent | pageCount | percent | syntaxFragment
		null			    | null            | null          | null      | null    | ''
		'SYMBOL1'	    	| null            | null          | null      | null    | " {\n        subarea 'SYMBOL1'\n    }"
		null				| 10			  |	null		  | null	  |	null	| ' {\n        offsetPages 10\n    }'
		null				| null			  |	20		  	  | null	  |	null	| ' {\n        offsetPercent 20\n    }'
		null				| null			  |	null	  	  | 30	  	  |	null	| ' {\n        pages 30\n    }'
		null				| null			  |	null	  	  | null	  |	40		| ' {\n        percent 40\n    }'
		null				| 11			  |	null	  	  | 12	  	  |	null	| ' {\n        offsetPages 11\n        pages 12\n    }'
		null				| 11			  |	null	  	  | null  	  |	12		| ' {\n        offsetPages 11\n        percent 12\n    }'
		null				| null			  |	21	  	  	  | null	  |	22		| ' {\n        offsetPercent 21\n        percent 22\n    }'
		null				| null			  |	21	  	  	  | 22		  |	null	| ' {\n        offsetPercent 21\n        pages 22\n    }'
		null				| 0			  	  |	null	  	  | 12	  	  |	null	| ' {\n        offsetPages 0\n        pages 12\n    }'
		null				| null			  |	0	  	  	  | null	  |	22		| ' {\n        offsetPercent 0\n        percent 22\n    }'		
	}
	
	def "VSAM index"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema (which isn't functionally correct by the way)"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('EMP-NAME-NDX')
		set.mode = SetMode.VSAM_INDEX
		VsamIndex vsamIndex = SchemaFactory.eINSTANCE.createVsamIndex()
		vsamIndex.diagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation()
		vsamIndex.diagramLocation.x = 10
		vsamIndex.diagramLocation.y = 20
		set.vsamIndex = vsamIndex
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set: a vsamIndex element is" 
			  "generated"
		syntax.startsWith(expected(
"""
name 'EMP-NAME-NDX'
 
order 'SORTED'
 
vsamIndex {
    x 10
    y 20
}
 
member 'EMPLOYEE' {
"""
			))
	}
	
	def "Set member (single-member set)"() {
		
		given: "a record syntax builder and the EMPSCHM version 100 schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('DEPT-EMPLOYEE')
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set: 1 member element is generated"
		syntax == expected(
"""
name 'DEPT-EMPLOYEE'
 
order 'SORTED'
 
owner 'DEPARTMENT' {
    next 1
    prior 2
}
 
member 'EMPLOYEE' {
    next 1
    prior 2
    owner 3

    membership 'OPTIONAL AUTOMATIC'

    key {
        ascending 'EMP-LAST-NAME-0415'
        ascending 'EMP-FIRST-NAME-0415'
        naturalSequence
        duplicates 'LAST'
    }

    diagram {
        label {
            x 242
            y 84
        }

        line {
            start {
                x 66
                y 53
            }

            bendpoint {
                x 66
                y 114
            }

            bendpoint {
                x 152
                y 114
            }

            end {
                x 38
                y 0
            }
        }
    }
}
""")	
	}
	
	def "Set member (multiple-member set)"() {
		
		given: "a record syntax builder and the EMPSCHM version 100 schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('COVERAGE-CLAIMS')
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set: 1 member element is"
			  "generated for each member in the set"
		syntax == expected(
"""
name 'COVERAGE-CLAIMS'
 
order 'LAST'
 
owner 'COVERAGE' {
    next 4
    prior 5
}
 
member 'HOSPITAL-CLAIM' {
    next 1
    prior 2

    diagram {
        label {
            x 358
            y 445
        }

        line {
            start {
                x 65
                y 53
            }

            end {
                x 65
                y 0
            }
        }
    }
}
 
member 'NON-HOSP-CLAIM' {
    next 1
    prior 2

    diagram {
        label {
            x 163
            y 445
        }

        line {
            start {
                x 65
                y 53
            }

            bendpoint {
                x 65
                y 76
            }

            bendpoint {
                x '-127'
                y 76
            }

            end {
                x 63
                y 0
            }
        }
    }
}
 
member 'DENTAL-CLAIM' {
    next 1
    prior 2

    diagram {
        label {
            x 544
            y 445
        }

        line {
            start {
                x 65
                y 53
            }

            bendpoint {
                x 65
                y 76
            }

            bendpoint {
                x 254
                y 76
            }

            end {
                x 64
                y 0
            }
        }
    }
}
""")	
	}
	
	@Unroll
	def "Member record pointers"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema (which isn't always functionally correct by the way)"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('DEPT-EMPLOYEE')
		set.members[0].nextDbkeyPosition = nextDbkeyPosition
		set.members[0].priorDbkeyPosition = priorDbkeyPosition
		set.members[0].ownerDbkeyPosition = ownerDbkeyPosition
		set.members[0].indexDbkeyPosition = indexDbkeyPosition
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set: a system owner element is"
			  "generated"
		syntax.indexOf(expected(
"""
member 'EMPLOYEE' {$syntaxFragment

    membership 'OPTIONAL AUTOMATIC'

    key {
""")) > -1

		where:
		nextDbkeyPosition | priorDbkeyPosition | ownerDbkeyPosition | indexDbkeyPosition | syntaxFragment
		null			  | null			   | null				| null				 | ''
		1			  	  | null			   | null				| null				 | '\n    next 1'
		null		  	  | 2			   	   | null				| null				 | '\n    prior 2'
		null		  	  | null		   	   | 3					| null				 | '\n    owner 3'
		1			  	  | 2				   | null				| null				 | '\n    next 1\n    prior 2'
		1			  	  | null			   | 3					| null				 | '\n    next 1\n    owner 3'
		1			  	  | 2			   	   | 3					| null				 | '\n    next 1\n    prior 2\n    owner 3'
		null		  	  | null		   	   | null				| 4				 	 | '\n    index 4'
		null		  	  | null		   	   | 3					| 4				 	 | '\n    index 4\n    owner 3'
	}
	
	@Unroll
	def "Membership option"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('DEPT-EMPLOYEE')
		set.members[0].membershipOption = membershipOption
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set: the membership option is only"
		      "generated when it's NOT MANDATORY AUTOMATIC"
		syntax.startsWith(expected(
"""
name 'DEPT-EMPLOYEE'
 
order 'SORTED'
 
owner 'DEPARTMENT' {
    next 1
    prior 2
}
 
member 'EMPLOYEE' {
    next 1
    prior 2
    owner 3
$syntaxFragment 
    key {
"""))
		
		where:
		membershipOption 						| syntaxFragment
		SetMembershipOption.MANDATORY_AUTOMATIC | ''
		SetMembershipOption.MANDATORY_MANUAL 	| "\n    membership 'MANDATORY MANUAL'\n"
		SetMembershipOption.OPTIONAL_AUTOMATIC 	| "\n    membership 'OPTIONAL AUTOMATIC'\n"
		SetMembershipOption.OPTIONAL_MANUAL 	| "\n    membership 'OPTIONAL MANUAL'\n"
	}
	
	@Unroll
	def "Sort key"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema (which isn't always functionally correct by the way)"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('DEPT-EMPLOYEE')
		set.members[0].sortKey.naturalSequence = naturalSequence
		set.members[0].sortKey.compressed = compressed
		set.members[0].sortKey.duplicatesOption = duplicatesOption
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set: the membership option is only"
			  "generated when it's NOT MANDATORY AUTOMATIC"
		syntax.startsWith(expected(
"""
name 'DEPT-EMPLOYEE'
 
order 'SORTED'
 
owner 'DEPARTMENT' {
    next 1
    prior 2
}
 
member 'EMPLOYEE' {
    next 1
    prior 2
    owner 3
 
    membership 'OPTIONAL AUTOMATIC'
 
    key {
        ascending 'EMP-LAST-NAME-0415'
        ascending 'EMP-FIRST-NAME-0415'
        $syntaxFragment
    }
 
    diagram {
"""))
		
		where:
		naturalSequence | compressed | duplicatesOption 			| syntaxFragment
		false 			| false 	 | DuplicatesOption.NOT_ALLOWED | "duplicates 'NOT ALLOWED'"
		false 			| false 	 | DuplicatesOption.BY_DBKEY 	| "duplicates 'BY DBKEY'"
		false 			| false 	 | DuplicatesOption.FIRST 		| "duplicates 'FIRST'"
		false 			| false 	 | DuplicatesOption.LAST 		| "duplicates 'LAST'"
		false 			| false 	 | DuplicatesOption.UNORDERED   | "duplicates 'UNORDERED'"
		true 			| false 	 | DuplicatesOption.NOT_ALLOWED | "naturalSequence\n        duplicates 'NOT ALLOWED'"
		false 			| true 	 	 | DuplicatesOption.NOT_ALLOWED | "compressed\n        duplicates 'NOT ALLOWED'"
		true 			| true 	 	 | DuplicatesOption.NOT_ALLOWED | "naturalSequence\n        compressed\n        duplicates 'NOT ALLOWED'"
	}
	
	def "Diagram: a single line without bendpoints and endpoints"() {
		given: "a record syntax builder and a slightly tweaked version of the IDMSNTWK version 1"
		 	   "(Release 18.5) schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		org.lh.dmlj.schema.Set set = schema.getSet('RCDSYN-RCDCOPY')
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set: no line element is generated"
		syntax.endsWith(expected(
"""
    owner 6
 
    diagram {
        label {
            x 1425
            y 2205
        }
    }
}
"""))
	}
	
	def "Diagram: a single line with only a source endpoint"() {
		given: "a record syntax builder and a slightly tweaked version of the IDMSNTWK version 1"
				"(Release 18.5) schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		org.lh.dmlj.schema.Set set = schema.getSet('RCDSYN-RCDCOPY')
		set.members[0].connectionParts[0].sourceEndpointLocation = 
			SchemaFactory.eINSTANCE.createDiagramLocation()
		set.members[0].connectionParts[0].sourceEndpointLocation.x = 1
		set.members[0].connectionParts[0].sourceEndpointLocation.y = 2
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set"
		syntax.endsWith(expected(
"""
    owner 6
 
    diagram {
        label {
            x 1425
            y 2205
        }

        line {
            start {
                x 1
                y 2
            }
        }
    }
}
"""))
	}
	
	def "Diagram: a single line with only a target endpoint"() {
		given: "a record syntax builder and a slightly tweaked version of the IDMSNTWK version 1"
				"(Release 18.5) schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		org.lh.dmlj.schema.Set set = schema.getSet('RCDSYN-RCDCOPY')
		set.members[0].connectionParts[0].targetEndpointLocation =
			SchemaFactory.eINSTANCE.createDiagramLocation()
		set.members[0].connectionParts[0].targetEndpointLocation.x = 1
		set.members[0].connectionParts[0].targetEndpointLocation.y = 2
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set"
		syntax.endsWith(expected(
"""
    owner 6
 
    diagram {
        label {
            x 1425
            y 2205
        }

        line {
            end {
                x 1
                y 2
            }
        }
    }
}
"""))
	}
	
	def "Diagram: a single line with only a source and target endpoint"() {
		given: "a record syntax builder and a slightly tweaked version of the IDMSNTWK version 1"
				"(Release 18.5) schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		org.lh.dmlj.schema.Set set = schema.getSet('RCDSYN-RCDCOPY')
		set.members[0].connectionParts[0].sourceEndpointLocation =
		SchemaFactory.eINSTANCE.createDiagramLocation()
		set.members[0].connectionParts[0].sourceEndpointLocation.x = 1
		set.members[0].connectionParts[0].sourceEndpointLocation.y = 2
		set.members[0].connectionParts[0].targetEndpointLocation =
			SchemaFactory.eINSTANCE.createDiagramLocation()
		set.members[0].connectionParts[0].targetEndpointLocation.x = 3
		set.members[0].connectionParts[0].targetEndpointLocation.y = 4
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set"
		syntax.endsWith(expected(
"""
    owner 6
 
    diagram {
        label {
            x 1425
            y 2205
        }

        line {
            start {
                x 1
                y 2
            }

            end {
                x 3
                y 4
            }
        }
    }
}
"""))
	}
	
	def "Diagram: a single line with bendpoints (and thus both source and target endpoints)"() {
		given: "a record syntax builder and the EMPSCHM version 100 schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('DEPT-EMPLOYEE')
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set"
		syntax.endsWith(expected(
"""
    diagram {
        label {
            x 242
            y 84
        }
 
        line {
            start {
                x 66
                y 53
            }

            bendpoint {
                x 66
                y 114
            }

            bendpoint {
                x 152
                y 114
            }

            end {
                x 38
                y 0
            }
        }
    }
}
"""))
	}
	
	def "Diagram: 2 lines (connectors) without bendpoints (but always with both source and target endpoints)"() {
		given: "a record syntax builder andthe IDMSNTWK version 1 (Release 18.5) schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		org.lh.dmlj.schema.Set set = schema.getSet('OOAK-SYS')
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set"
		syntax.endsWith(expected(
"""
    diagram {
        label {
            x 2594
            y 295
        }
 
        line {
            start {
                x 25
                y 0
            }

            connectors {
                label 'C21'

                connector {
                    x 152
                    y 437
                }

                connector {
                    x 2650
                    y 294
                }
            }

            end {
                x 9
                y 0
            }
        }
    }
}
"""))
	}
	
	def "Diagram: 2 lines (connectors) with a bendpoint on the second line (and with both source and target endpoints)"() {
		given: "a record syntax builder andthe IDMSNTWK version 1 (Release 18.5) schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		org.lh.dmlj.schema.Set set = schema.getSet('OOAK-USER')
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set"
		syntax.endsWith(expected(
"""
    diagram {
        label {
            x 1973
            y 706
        }
 
        line {
            start {
                x 0
                y 13
            }
 
            connectors {
                label 'E17'
 
                connector {
                    x 95
                    y 465
                }
 
                connector {
                    x 2033
                    y 702
                }
            }
 
            bendpoint {
                x 1862
                y 168
            }
 
            end {
                x 0
                y 33
            }
        }
    }
}
"""))
	}
	
	def "Diagram: 2 lines (connectors) with bendpoints on both of them (and with both source and target endpoints)"() {
		given: "a record syntax builder andthe EMPSCHM version 100 schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet('OFFICE-EMPLOYEE')
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set"
		syntax.endsWith(expected(
"""
    diagram {
        label {
            x 384
            y 198
        }
 
        line {
            start {
                x 65
                y 53
            }
 
            bendpoint {
                x 65
                y 77
            }
 
            bendpoint {
                x 35
                y 77
            }
 
            connectors {
                label 'A'
 
                connector {
                    x 425
                    y 116
                }
 
                connector {
                    x 454
                    y 132
                }
            }
 
            bendpoint {
                x 65
                y 171
            }
 
            bendpoint {
                x '-19'
                y 171
            }
 
            end {
                x 95
                y 0
            }
        }
    }
}
"""))
	}
	
	def "Diagram: no connector label"() {
		given: "a record syntax builder andthe IDMSNTWK version 1 (Release 18.5) schema"
		SetSyntaxBuilder builder = new SetSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		org.lh.dmlj.schema.Set set = schema.getSet('OOAK-SYS')
		set.members[0].connectionParts[0].connector.label = null
		set.members[0].connectionParts[1].connector.label = null
		
		when: "passing the set to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set"
		syntax.endsWith(expected(
"""
    diagram {
        label {
            x 2594
            y 295
        }
 
        line {
            start {
                x 25
                y 0
            }

            connectors {

                connector {
                    x 152
                    y 437
                }

                connector {
                    x 2650
                    y 294
                }
            }

            end {
                x 9
                y 0
            }
        }
    }
}
"""))
	}
	
	def "Syntax for inclusion in a schema - non-default order"() {
		
		given: "a set syntax builder for which we indicate that we want to suppress the 'name"
			   "property and set an initial indent"
		SetSyntaxBuilder builder = new SetSyntaxBuilder( generateName : false, initialTabs : 1)
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		
		when: "passing an area to the builder's build method"
		String syntax = builder.build(schema.getSet("DEPT-EMPLOYEE"))
		
		then: "the builder creates the syntax that describes the set and will omit the name"
			  "property and indent the syntax with 1 'tab' (being actually spaces)"
		syntax == expected(
"""
    order 'SORTED'
 
    owner 'DEPARTMENT' {
        next 1
        prior 2
    }
 
    member 'EMPLOYEE' {
        next 1
        prior 2
        owner 3
 
        membership 'OPTIONAL AUTOMATIC'
 
        key {
            ascending 'EMP-LAST-NAME-0415'
            ascending 'EMP-FIRST-NAME-0415'
            naturalSequence
            duplicates 'LAST'
        }
 
        diagram {
            label {
                x 242
                y 84
            }
 
            line {
                start {
                    x 66
                    y 53
                }
 
                bendpoint {
                    x 66
                    y 114
                }
 
                bendpoint {
                    x 152
                    y 114
                }
 
                end {
                    x 38
                    y 0
                }
            }
        }
    }
""")
	}
	
	def "Syntax for inclusion in a schema - default order (chained set)"() {
		
		given: "a set syntax builder for which we indicate that we want to suppress the 'name"
			   "property and set an initial indent"
		SetSyntaxBuilder builder = new SetSyntaxBuilder( generateName : false, initialTabs : 1)
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		
		when: "passing an area to the builder's build method"
		String syntax = builder.build(schema.getSet("REPORTS-TO"))
		
		then: "the builder creates the syntax that describes the set and will omit the name"
			  "property and indent the syntax with 1 'tab' (being actually spaces)"
		syntax == expected(
"""
    owner 'EMPLOYEE' {
        next 15
        prior 16
    }

    member 'STRUCTURE' {
        next 4
        prior 5
        owner 6

        membership 'OPTIONAL MANUAL'

        diagram {
            label {
                x 39
                y 332
            }

            line {
                start {
                    x 19
                    y 53
                }

                bendpoint {
                    x 19
                    y 77
                }

                bendpoint {
                    x '-183'
                    y 77
                }

                end {
                    x 63
                    y 0
                }
            }
        }
    }
""")
	}
	
	def "Syntax for inclusion in a schema - default order (user-owned indexed set)"() {
		
		given: "a set syntax builder for which we indicate that we want to suppress the 'name"
			   "property and set an initial indent"
		SetSyntaxBuilder builder = new SetSyntaxBuilder( generateName : false, initialTabs : 1)
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet("OFFICE-EMPLOYEE")
		set.order = SetOrder.NEXT
		
		when: "passing an area to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set and will omit the name"
			  "property and indent the syntax with 1 'tab' (being actually spaces)"
		syntax == expected(
"""
    index {
        keys 30
    }

    owner 'OFFICE' {
        next 1
        prior 2
    }
 
    member 'EMPLOYEE' {
        index 5
        owner 6
 
        membership 'OPTIONAL AUTOMATIC'
 
        diagram {
            label {
                x 384
                y 198
            }
 
            line {
                start {
                    x 65
                    y 53
                }
 
                bendpoint {
                    x 65
                    y 77
                }
 
                bendpoint {
                    x 35
                    y 77
                }
 
                connectors {
                    label 'A'
 
                    connector {
                        x 425
                        y 116
                    }
 
                    connector {
                        x 454
                        y 132
                    }
                }
 
                bendpoint {
                    x 65
                    y 171
                }
 
                bendpoint {
                    x '-19'
                    y 171
                }
 
                end {
                    x 95
                    y 0
                }
            }
        }
    }
""")
	}
	
	def "Syntax for inclusion in a schema - default order (system-owned indexed set)"() {
		
		given: "a set syntax builder for which we indicate that we want to suppress the 'name"
			   "property and set an initial indent"
		SetSyntaxBuilder builder = new SetSyntaxBuilder( generateName : false, initialTabs : 1)
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		org.lh.dmlj.schema.Set set = schema.getSet("EMP-NAME-NDX")
		set.order = SetOrder.NEXT
		
		when: "passing an area to the builder's build method"
		String syntax = builder.build(set)
		
		then: "the builder creates the syntax that describes the set and will omit the name"
			  "property and indent the syntax with 1 'tab' (being actually spaces)"
		syntax == expected(
"""
    index {
        keys 40
    }

    systemOwner {
        area 'EMP-DEMO-REGION' {
            offsetPages 1
            pages 4
        }
 
        diagram {
            x 274
            y 190
        }
    }
 
    member 'EMPLOYEE' {
        index 4
 
        membership 'OPTIONAL AUTOMATIC'
 
        diagram {
            label {
                x 178
                y 139
            }
        }
    }
""")
	}
	
}
