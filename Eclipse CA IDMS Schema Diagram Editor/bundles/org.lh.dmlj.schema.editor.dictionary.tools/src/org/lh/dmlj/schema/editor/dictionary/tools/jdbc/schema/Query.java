/**
 * Copyright (C) 2016  Luc Hermans
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
package org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema;

import java.util.List;

import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.DictionarySession;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IQuery;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Rcdsyn_079;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sr_036;
import org.lh.dmlj.schema.editor.dictionary.tools.template.AreaListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.AreaProcedureListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.BaseRecordSynonymListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.CalcKeyElementListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.CatalogCalcKeyElementListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.CatalogElementListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.CatalogForeignKeyListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.CatalogIndexListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.CatalogRecordListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.CatalogSetListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.CatalogSortKeyElementListForSystemOwnedSetsQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.CatalogSortKeyElementListForUserOwnedSetsQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.CatalogViaSetListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.ElementCommentListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.ElementListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.ElementSynonymCommentListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.IQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.RecordListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.RecordProcedureListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.RecordSynonymListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.SchemaDescriptionAndCommentListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.SetListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.SetOwnerListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.SortKeyElementListQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.ValidSchemaListForNonSysdirlDictionariesQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.ValidSchemaListForSysdirlDictionariesQueryTemplate;
import org.lh.dmlj.schema.editor.dictionary.tools.template.ViaSetListQueryTemplate;


public class Query implements IQuery {
	
	private static final IQueryTemplate areaListQueryTemplate = new AreaListQueryTemplate();
	private static final IQueryTemplate areaProcedureListQueryTemplate = new AreaProcedureListQueryTemplate();
	private static final IQueryTemplate baseRecordSynonymListQueryTemplate = new BaseRecordSynonymListQueryTemplate();
	private static final IQueryTemplate calcKeyElementListQueryTemplate = new CalcKeyElementListQueryTemplate();
	private static final IQueryTemplate catalogCalcKeyElementListQueryTemplate = new CatalogCalcKeyElementListQueryTemplate();
	private static final IQueryTemplate catalogElementListQueryTemplate = new CatalogElementListQueryTemplate();
	private static final IQueryTemplate catalogForeignKeyListQueryTemplate = new CatalogForeignKeyListQueryTemplate();
	private static final IQueryTemplate catalogIndexListQueryTemplate = new CatalogIndexListQueryTemplate();
	private static final IQueryTemplate catalogRecordListQueryTemplate = new CatalogRecordListQueryTemplate();
	private static final IQueryTemplate catalogSetListQueryTemplate = new CatalogSetListQueryTemplate();
	private static final IQueryTemplate catalogSortKeyElementListForSystemOwnedSetQueryTemplate = new CatalogSortKeyElementListForSystemOwnedSetsQueryTemplate();
	private static final IQueryTemplate catalogSortKeyElementListForUserOwnedSetQueryTemplate = new CatalogSortKeyElementListForUserOwnedSetsQueryTemplate();
	private static final IQueryTemplate catalogViaSetListQueryTemplate = new CatalogViaSetListQueryTemplate();
	private static final IQueryTemplate elementCommentListQueryTemplate = new ElementCommentListQueryTemplate();
	private static final IQueryTemplate elementListQueryTemplate = new ElementListQueryTemplate();
	private static final IQueryTemplate elementSynonymCommentsListQueryTemplate = new ElementSynonymCommentListQueryTemplate();
	private static final IQueryTemplate recordListQueryTemplate = new RecordListQueryTemplate();
	private static final IQueryTemplate recordSynonymListQueryTemplate = new RecordSynonymListQueryTemplate();
	private static final IQueryTemplate recordProcedureListQueryTemplate = new RecordProcedureListQueryTemplate();
	private static final IQueryTemplate schemaDescriptionAndCommentListQueryTemplate = new SchemaDescriptionAndCommentListQueryTemplate();
	private static final IQueryTemplate setListQueryTemplate = new SetListQueryTemplate();
	private static final IQueryTemplate setOwnerListQueryTemplate = new SetOwnerListQueryTemplate();
	private static final IQueryTemplate sortKeyElementListQueryTemplate = new SortKeyElementListQueryTemplate();
	private static final IQueryTemplate validSchemaListForNonSysdirlDictionariesQueryTemplate = new ValidSchemaListForNonSysdirlDictionariesQueryTemplate();
	private static final IQueryTemplate validSchemaListForSysdirlDictionariesQueryTemplate = new ValidSchemaListForSysdirlDictionariesQueryTemplate();
	private static final IQueryTemplate viaSetListQueryTemplate = new ViaSetListQueryTemplate();
	
	private String description;
	private int number;
	private String sql;

	private Query(Builder builder) {
		super();
		this.setDescription(builder.description);
		this.setSql(builder.sql);
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public String getSql() {
		return sql;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setNumber(int number) {
		this.number = number;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public String toString() {
		return "Query #" + number + " - description='" + description + "', sql=[see below]\n" + sql;
	}

	public static class Builder {
		
		private String description;
		private String sql;
		
		public Builder() {
			super();
		}
		
		public IQuery build() {
			return new Query(this);
		}

		public Builder forAreaList(SchemaImportSession session) {
			description = "area list";
			IQueryTemplate template = areaListQueryTemplate; 
			sql = template.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault()),
												  session.getSchemaName(), 
												  session.getSchemaVersion()});
			return this;
		}
		
		public Builder forAreaProcedureList(SchemaImportSession session) {
			description = "area procedure list";
			IQueryTemplate template = areaProcedureListQueryTemplate; 
			sql = template.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault()),
												  session.getSchemaName(), 
												  session.getSchemaVersion()});
			return this;
		}

		public Builder forBaseRecordSynonymList(DictionarySession session, List<Sr_036> sr_036s) {
			description = "base record synonym list";
			IQueryTemplate template = baseRecordSynonymListQueryTemplate;
			sql = template.generate(new Object[] {session.getDictionary(), sr_036s});
			return this;
		}

		public Builder forCalcKeyElementList(SchemaImportSession session) {
			description = "CALC key element list";
			IQueryTemplate template = calcKeyElementListQueryTemplate;
			sql = template.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault()),
												  session.getSchemaName(), 
												  session.getSchemaVersion()});
			return this;
		}

		public Builder forCatalogCalcKeyElementList(SchemaImportSession session) {
			description = "CALC key element list (catalog)";
			sql = catalogCalcKeyElementListQueryTemplate.generate(new Object[] {});
			return this;
		}

		public Builder forCatalogElementList() {		
			description = "element list (catalog)";
			IQueryTemplate template = catalogElementListQueryTemplate;
			sql = template.generate(new Object[] {});
			return this;
		}
		
		public Builder forCatalogForeignKeyList() {
			description = "foreign key list (catalog)";
			sql = catalogForeignKeyListQueryTemplate.generate(new Object[] {});
			return this;
		}

		public Builder forCatalogIndexList() {		
			description = "index list (catalog)";
			sql = catalogIndexListQueryTemplate.generate(new Object[] {});
			return this;
		}

		public Builder forCatalogRecordList() {		
			description = "record list (catalog)";
			sql = catalogRecordListQueryTemplate.generate(new Object[] {});
			return this;
		}
		
		public Builder forCatalogSetList() {		
			description = "set list (catalog)";
			sql = catalogSetListQueryTemplate.generate(new Object[] {});
			return this;
		}

		public Builder forCatalogSortKeyElementListForSystemOwnedSets(SchemaImportSession session) {
			description = "sort key element list (system owned sets, catalog)";
			sql = catalogSortKeyElementListForSystemOwnedSetQueryTemplate.generate(new Object[] {});
			return this;
		}

		public Builder forCatalogSortKeyElementListForUserOwnedSetsQuery(SchemaImportSession session) {
			description = "sort key element list  (user owned sets, catalog)";
			sql = catalogSortKeyElementListForUserOwnedSetQueryTemplate.generate(new Object[] {});
			return this;
		}

		public Builder forCatalogViaSetList(SchemaImportSession session) {
			description = "VIA set list (catalog)";
			sql = catalogViaSetListQueryTemplate.generate(new Object[] {});
			return this;
		}

		public Builder forElementCommentList(DictionarySession session, List<Rcdsyn_079> rcdsyn_079s) {
			description = "element comment list";
			sql = elementCommentListQueryTemplate.generate(new Object[] {session.getDictionary(),
																		 rcdsyn_079s});			
			return this;
		}		
		
		public Builder forElementList(DictionarySession session, List<Rcdsyn_079> rcdsyn_079s) {
			description = "element list";
			IQueryTemplate template = elementListQueryTemplate;
			sql = template.generate(new Object[] {session.getDictionary(), rcdsyn_079s});			
			return this;
		}
		
		public Builder forElementSynonymCommentList(DictionarySession session, 
													List<Rcdsyn_079> rcdsyn_079s) {
			
			description = "element synonym comment list";
			IQueryTemplate template = elementSynonymCommentsListQueryTemplate;
			sql = template.generate(new Object[] {session.getDictionary(), rcdsyn_079s});			
			return this;
		}		
		
		public Builder forRecordList(SchemaImportSession session) {
			description = "record list";
			IQueryTemplate template = recordListQueryTemplate;
			sql = template.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault()),
								 				  session.getSchemaName(),
								 				  session.getSchemaVersion()});
			return this;
		}

		public Builder forRecordSynonymList(RecordElementsImportSession session) {
			description = "record synonym list";
			IQueryTemplate template = recordSynonymListQueryTemplate;
			sql = template.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault()),
								 				  session.getRecordSynonymName()});
			return this;
		}		
		
		public Builder forRecordProcedureList(SchemaImportSession session) {
			description = "record procedure list";
			IQueryTemplate template = recordProcedureListQueryTemplate;
			sql = template.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault()),
						  						  session.getSchemaName(),
						  						  session.getSchemaVersion()}); 
			return this;
		}

		public Builder forSchemaDescriptionAndCommentList(SchemaImportSession session) {			
			description = "schema description and comment list";
			IQueryTemplate template = schemaDescriptionAndCommentListQueryTemplate;
			sql = template.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault()),
												  session.getSchemaName(),
												  session.getSchemaVersion()});
			return this;
		}

		public Builder forSetList(SchemaImportSession session) {
			description = "set list";
			sql = setListQueryTemplate.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault()),
					  								  		  session.getSchemaName(),
					  								  		  session.getSchemaVersion()});
			return this;
		}
		
		public Builder forSetOwnerList(SchemaImportSession session) {
			description = "set owner list";
			IQueryTemplate template = setOwnerListQueryTemplate;
			sql = template.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault()),
												  session.getSchemaName(),
												  session.getSchemaVersion()});
			return this;
		}
		
		public Builder forSortKeyElementList(SchemaImportSession session) {
			description = "sort key element list";
			IQueryTemplate template = sortKeyElementListQueryTemplate;
			sql = template.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault()),
												  session.getSchemaName(), 
												  session.getSchemaVersion()});
			return this;
		}

		public Builder forValidSchemaList(DictionarySession session) {
			IQueryTemplate template;
			if (session.getDictionary().isSysdirl()) {
				description = "valid schema list (SYSDIRL)";
				template = validSchemaListForSysdirlDictionariesQueryTemplate;
			} else {
				description = "valid schema list";
				template = validSchemaListForNonSysdirlDictionariesQueryTemplate;
			}
			sql = template.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault())});
			return this;
		}

		public Builder forViaSetList(SchemaImportSession session) {
			description = "VIA set list";
			IQueryTemplate template = viaSetListQueryTemplate;
			sql = template.generate(new Object[] {session.getDictionary().getSchemaWithDefault(Plugin.getDefault()),
												  session.getSchemaName(),
												  session.getSchemaVersion()});
			return this;
		}
		
	}
	
}
