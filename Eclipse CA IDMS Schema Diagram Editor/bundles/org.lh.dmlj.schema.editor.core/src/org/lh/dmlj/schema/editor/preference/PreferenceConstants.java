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
package org.lh.dmlj.schema.editor.preference;

public abstract class PreferenceConstants {
	
	public static final int CLOSE_SCHEMADSL_EDITORS_YES = 1;
	public static final int CLOSE_SCHEMADSL_EDITORS_NO = 0;
	public static final int CLOSE_SCHEMADSL_EDITORS_ASK = 2;
	
	public static final String UNITS = "units";
	public static final String LOG_DIAGNISTIC_MESSAGES = "logDiagnosticMessages";
	
	public static final String COMPRESSION_PROCEDURES = "compressionProcedures";
	
	public static final String TOP_MARGIN = "topMargin";
	public static final String BOTTOM_MARGIN = "bottomMargin";
	public static final String LEFT_MARGIN = "leftMargin";
	public static final String RIGHT_MARGIN = "rightMargin";
	
	public static final String DIAGRAMLABEL_ORGANISATION = "organisation";
	public static final String DIAGRAMLABEL_SHOW_LAST_MODIFIED = "showLastModified";
	public static final String DIAGRAMLABEL_LAST_MODIFIED_DATE_FORMAT_PATTERN = 
		"lastModifiedDateFormatPattern";
	
	public static final String SHOW_RULERS = "showRulers";
	public static final String SHOW_GRID = "showGrid";
	public static final String SNAP_TO_GUIDES = "snapToGuides";
	public static final String SNAP_TO_GRID = "snapToGrid";
	public static final String SNAP_TO_GEOMETRY = "snapToGeometry";
	
	public static final String SORT_SCHEMA_ENTITIES_ON_EXPORT_TO_SYNTAX = 
		"sortSchemaEntitiesOnExportToSyntax";
	
	public static final String OPERATING_SYSTEM_TEXT_SIZE = "operatingSystemTextSize";
	
	public static final String READ_ONLY_MODE = "readOnlyMode";
	
	public static final String DEFAULT_FILE_EXTENSION = "defaultFileExtension";
	
	public static final String CLOSE_SCHEMADSL_EDITORS = "closeSchemaDslEditors";
	
}
