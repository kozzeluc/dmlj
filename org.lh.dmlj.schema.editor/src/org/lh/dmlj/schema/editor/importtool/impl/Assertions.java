package org.lh.dmlj.schema.editor.importtool.impl;

import java.util.Collection;

import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaRecord;

abstract class Assertions {
	
	private static final String MSG_DBKEY_OVERLAPS = "dbkey position overlap";

	static void isCollectionNotEmpty(Collection<?> collection, String message) {
		if (collection.isEmpty()) {
			throw new RuntimeException(message);
		}
	}

	static void isFreeDbkeyPosition(SchemaRecord record, short dbkeyPosition) {
	
		for (OwnerRole ownerRole : record.getOwnerRoles()) {
			if (dbkeyPosition == ownerRole.getNextDbkeyPosition() ||
				ownerRole.getPriorDbkeyPosition() != null &&
				dbkeyPosition == ownerRole.getPriorDbkeyPosition().shortValue()) {
	
				throw new RuntimeException(MSG_DBKEY_OVERLAPS);
			}			
		}
		
		for (MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole.getIndexDbkeyPosition() != null &&
				dbkeyPosition == memberRole.getIndexDbkeyPosition()
										   .shortValue() ||
				memberRole.getNextDbkeyPosition() != null &&
				dbkeyPosition == memberRole.getNextDbkeyPosition()
										   .shortValue() ||
				memberRole.getOwnerDbkeyPosition() != null &&
				dbkeyPosition == memberRole.getOwnerDbkeyPosition()
										   .shortValue() ||
				memberRole.getPriorDbkeyPosition() != null &&
				dbkeyPosition == memberRole.getPriorDbkeyPosition()
										   .shortValue()) {
	
				throw new RuntimeException(MSG_DBKEY_OVERLAPS);
			}
		}
		
	}

	static void isSingleElementCollection(Collection<?> collection,
										  String message) {
		
		if (collection.size() != 1) {
			throw new RuntimeException(message);
		}
	}

	static void isNotNull(Object object, Class<?> targetClass) {
		String message = 
			"the supplied " + targetClass.getSimpleName() + " is null";
		isNotNull(object, message);
	}

	static void isNotNull(Object object, String message) {
		if (object == null) {
			throw new RuntimeException(message);
		}
	}

	static void isNull(Object object, Class<?> targetClass) {
		String message = 
			"the supplied " + targetClass.getSimpleName() + " is NOT null";
		isNotNull(object, message);
	}

	static void isNull(Object object, String message) {
		if (object != null) {
			throw new RuntimeException(message);
		}
	}	
	
}