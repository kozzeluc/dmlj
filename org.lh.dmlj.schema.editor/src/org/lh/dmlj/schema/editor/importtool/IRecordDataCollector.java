/**
 * Copyright (C) 2013  Luc Hermans
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
package org.lh.dmlj.schema.editor.importtool;

import java.util.Collection;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallVerb;

public interface IRecordDataCollector<T> {	
	
	String getAreaName(T context);
	
	String getBaseName(T context);
	
	short getBaseVersion(T context);

	DuplicatesOption getCalcKeyDuplicatesOption(T context);

	Collection<String> getCalcKeyElementNames(T context);

	LocationMode getLocationMode(T context);

	Short getMinimumFragmentLength(T context);

	Short getMinimumRootLength(T context);

	String getName(T context);

	Integer getOffsetOffsetPageCount(T context);

	Short getOffsetOffsetPercent(T context);

	Integer getOffsetPageCount(T context);

	Short getOffsetPercent(T context);

	Collection<ProcedureCallTime> getProcedureCallTimes(T context);

	Collection<RecordProcedureCallVerb> getProcedureCallVerbs(T contex);

	Collection<String> getProceduresCalled(T context);

	short getRecordId(T context);	

	String getSymbolicSubareaName(T context);
	
	String getSynonymName(T context);
	
	short getSynonymVersion(T context);			
	
	Short getViaDisplacementPageCount(T context);

	String getViaSetName(T context);

	String getViaSymbolicDisplacementName(T context);	
	
}
