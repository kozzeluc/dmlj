/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.Collection;

import org.lh.dmlj.schema.SchemaRecord;

final class Assertions {
	private static final String MSG_DBKEY_OVERLAPS = "dbkey position overlap";

	static void isCollectionNotEmpty(Collection<?> collection, String message) {
		if (collection.isEmpty()) {
			throw new IllegalStateException(message);
		}
	}

	static void isFreeDbkeyPosition(SchemaRecord schemaRecord, short dbkeyPosition) {
		for (var ownerRole : schemaRecord.getOwnerRoles()) {
			if (dbkeyPosition == ownerRole.getNextDbkeyPosition() || ownerRole.getPriorDbkeyPosition() != null &&
				dbkeyPosition == ownerRole.getPriorDbkeyPosition().shortValue()) {
	
				throw new IllegalArgumentException(MSG_DBKEY_OVERLAPS);
			}			
		}
		for (var memberRole : schemaRecord.getMemberRoles()) {
			if (memberRole.getIndexDbkeyPosition() != null && dbkeyPosition == memberRole.getIndexDbkeyPosition().shortValue() ||
				memberRole.getNextDbkeyPosition() != null && dbkeyPosition == memberRole.getNextDbkeyPosition().shortValue() ||
				memberRole.getOwnerDbkeyPosition() != null && dbkeyPosition == memberRole.getOwnerDbkeyPosition().shortValue() ||
				memberRole.getPriorDbkeyPosition() != null && dbkeyPosition == memberRole.getPriorDbkeyPosition().shortValue()) {
	
				throw new IllegalArgumentException(MSG_DBKEY_OVERLAPS);
			}
		}
	}

	static void isSingleElementCollection(Collection<?> collection, String message) {
		if (collection.size() != 1) {
			throw new IllegalArgumentException(message);
		}
	}

	static void isEqualInSize(Collection<?> col1, Collection<?> col2, String message) {
		if (col1.size() != col2.size()) {
			throw new IllegalArgumentException(message);
		}
	}
	
	static void isNotNull(Object object, Class<?> targetClass) {
		isNotNull(object, "the supplied %s is null".formatted(targetClass.getSimpleName()));
	}

	static void isNotNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	static void isNull(Object object, Class<?> targetClass) {	
		isNotNull(object, "the supplied %s is NOT null".formatted(targetClass.getSimpleName()));
	}

	static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}
	
	private Assertions() {
	}
	
}
