
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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.lh.dmlj.schema.editor.dsl.builder.model.AreaModelBuilderSpec;
import org.lh.dmlj.schema.editor.dsl.builder.model.ElementModelBuilderSpec;
import org.lh.dmlj.schema.editor.dsl.builder.model.RecordModelBuilderSpec;
import org.lh.dmlj.schema.editor.dsl.builder.model.SchemaModelBuilderSpec;
import org.lh.dmlj.schema.editor.dsl.builder.model.SetModelBuilderSpec;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.AreaSyntaxBuilderSpec;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.ElementSyntaxBuilderSpec;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.RecordSyntaxBuilderSpec;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.SchemaSyntaxBuilderSpec;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.SetSyntaxBuilderSpec;

@RunWith(Suite.class)
@SuiteClasses({
		
	AreaSyntaxBuilderSpec.class,
	RecordSyntaxBuilderSpec.class,
	ElementSyntaxBuilderSpec.class,
	SetSyntaxBuilderSpec.class,
	SchemaSyntaxBuilderSpec.class,
	
	AreaModelBuilderSpec.class,
	RecordModelBuilderSpec.class,
	ElementModelBuilderSpec.class,
	SchemaModelBuilderSpec.class,
	SetModelBuilderSpec.class
	
})
public class AllTests {	
}
