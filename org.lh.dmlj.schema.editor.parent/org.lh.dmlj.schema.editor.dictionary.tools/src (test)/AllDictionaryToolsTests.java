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
import org.lh.dmlj.schema.editor.dictionary.tools.JdbcToolsTest;
import org.lh.dmlj.schema.editor.dictionary.tools.template.BaseRecordSynonymListQueryTemplateTest;
import org.lh.dmlj.schema.editor.dictionary.tools.template.ElementCommentListQueryTemplateTest;
import org.lh.dmlj.schema.editor.dictionary.tools.template.ElementListQueryTemplateTest;


@RunWith(Suite.class)
@SuiteClasses({
	
	JdbcToolsTest.class,
	BaseRecordSynonymListQueryTemplateTest.class,
	ElementListQueryTemplateTest.class,
	ElementCommentListQueryTemplateTest.class
	
})
public class AllDictionaryToolsTests {	
}
