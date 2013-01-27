package org.lh.dmlj.schema.editor.importtool;

import java.util.Collection;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallVerb;

public interface IRecordDataCollector<T> {	
	
	String getAreaName(T context);

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

	Collection<ProcedureCallTime> getProcedureCallTimes(T context, 
										  			    String procedureName);

	Collection<RecordProcedureCallVerb> getProcedureCallVerbs(T context, 
												 		      String procedureName);

	Collection<String> getProceduresCalled(T context);

	short getRecordId(T context);	

	String getSymbolicSubareaName(T context);
	
	Short getViaDisplacementPageCount(T context);

	String getViaSetName(T context);

	String getViaSymbolicDisplacementName(T context);	
	
}