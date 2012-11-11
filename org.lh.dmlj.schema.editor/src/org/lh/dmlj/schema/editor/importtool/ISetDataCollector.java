package org.lh.dmlj.schema.editor.importtool;

import java.util.Collection;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;

public interface ISetDataCollector<T> {	

	Short getDisplacementPageCount(T context);

	DuplicatesOption getDuplicatesOption(T context, String memberRecordName);

	Short getKeyCount(T context);

	Short getMemberIndexDbkeyPosition(T context);

	Short getMemberNextDbkeyPosition(T context, String memberRecordName);

	Short getMemberOwnerDbkeyPosition(T context, String memberRecordName);

	Short getMemberPriorDbkeyPosition(T context, String memberRecordName);

	Collection<String> getMemberRecordNames(T context);

	String getName(T context);

	short getOwnerNextDbkeyPosition(T context);

	Short getOwnerPriorDbkeyPosition(T context);

	String getOwnerRecordName(T context);

	SetMembershipOption getSetMembershipOption(T context, 
											   String memberRecordName);

	SetMode getSetMode(T context);

	SetOrder getSetOrder(T context);

	Collection<String> getSortKeyElements(T context, String memberRecordName);

	SortSequence getSortSequence(T context, String memberRecordName, 
								 String keyElementName);

	String getSymbolicIndexName(T context);

	String getSystemOwnerAreaName(T context);

	Integer getSystemOwnerOffsetOffsetPageCount(T context);

	Short getSystemOwnerOffsetOffsetPercent(T context);

	Integer getSystemOwnerOffsetPageCount(T context);

	Short getSystemOwnerOffsetPercent(T context);

	String getSystemOwnerSymbolicSubareaName(T context);

	boolean getSortKeyIsNaturalSequence(T context, String memberRecordName);

	boolean isKeyCompressed(T context);
	
	boolean isSortedByDbkey(T context);

	boolean isSystemOwned(T context);

}