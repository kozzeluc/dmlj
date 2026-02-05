/**
 * Copyright (C) 2026  Luc Hermans
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
package org.lh.dmlj.schema.editor.common;

import static java.util.Comparator.comparing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.RecordSyntaxBuilder;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.SchemaSyntaxBuilder;
import org.lh.dmlj.schema.editor.log.Logger;

public final class Tools {
	
	public static boolean areaMixesWithRecord(SchemaArea area, SchemaRecord schemaRecord) {
		if (schemaRecord.isCalc() || schemaRecord.isDirect() || schemaRecord.isVia()) {
			return canHoldNonVsamRecords(area);
		} else {
			return canHoldVsamRecords(area);
		}
	}
	
	public static boolean canHoldNonVsamRecords(SchemaArea area) {
		return !containsVsamRecord(area);
	}
	
	public static boolean canHoldSystemOwners(SchemaArea area) {
		return canHoldNonVsamRecords(area);
	}
	
	public static boolean canHoldVsamRecords(SchemaArea area) {
		return area.getIndexes().isEmpty() && !containsNonVsamRecord(area);
	}
	
	private static boolean containsNonVsamRecord(SchemaArea area) {
		if (area == null) {
			throw new IllegalArgumentException("area is null");
		} else {	
			return area.getRecords().stream()
					.anyMatch(r -> !r.isVsam() && !r.isVsamCalc());
		}
	}

	private static boolean containsVsamRecord(SchemaArea area) {
		if (area == null) {
			throw new IllegalArgumentException("area is null");
		} else {	
			return area.getRecords().stream()
					.anyMatch(r -> r.isVsam() || r.isVsamCalc());
		}
	}
	
	public static <T> T executeWithCursorBusy(Supplier<T> code) {
		var result = new ArrayDeque<T>();
		var exceptionToThrow = new ArrayDeque<Throwable>();
		BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> {
			try {
				result.push(code.get());
			} catch (Exception e) {
				exceptionToThrow.push(e);				
			}
		});
		if (!exceptionToThrow.isEmpty()) {
			var t = exceptionToThrow.pop();			
			Logger.getLogger(Plugin.getDefault()).error("", t);
			throw new IllegalStateException(t.getMessage(), t);
		} else {
			return result.pop();
		}
	}

	public static String generateRecordElementsDSL(SchemaRecord schemaRecord) {
		var dsl = new RecordSyntaxBuilder().build(schemaRecord);
		var i = dsl.indexOf("\"\"\"\n");
		var j = dsl.lastIndexOf("\n\"\"\"");
		return dsl.substring(i + 3, j).replace("\n    ", "\n").substring(1);
	}

	public static String getCalcKey(Key calcKey) {
		if (calcKey == null) {
			return "";
		}
		var p = new StringBuilder();		
		for (var keyElement :calcKey.getElements()) {
			if (!p.isEmpty()) {
				p.append(", ");
			}
			p.append(keyElement.getElement().getName());
		}
		return p.toString();
	}
	
	public static Element getDefaultSortKeyElement(SchemaRecord schemaRecord) {
		return schemaRecord.getElements() == null ? null : schemaRecord.getElements().stream()
				.filter(Tools::isSuitableAsSortElement)
				.findFirst()
				.orElse(null);
	}
	
	private static boolean isSuitableAsSortElement(Element element) {
		return !element.getName().equalsIgnoreCase("FILLER") && element.getLength() <= 256 && !isInvolvedInRedefines(element) && !isInvolvedInOccurs(element);
	}

	public static String getDuplicatesOption(Key calcKey) {
		if (calcKey == null) {
			return "";
		} else {	
			return switch (calcKey.getDuplicatesOption()) {
				case NOT_ALLOWED -> "DN";
				case FIRST -> "DF";
				case LAST -> "DL";
				case UNORDERED -> "DU";
				case BY_DBKEY -> "DD";
			};
		}
	}

	public static String getMembershipOption(MemberRole memberRole) {	
		return switch (memberRole.getMembershipOption()) {
			case MANDATORY_AUTOMATIC -> "MA";
			case MANDATORY_MANUAL -> "MM";
			case OPTIONAL_AUTOMATIC -> "OA";
			case OPTIONAL_MANUAL -> "OM";
		};
	}
	
	public static short getFirstAvailablePointerPosition(SchemaRecord schemaRecord) {
		return (short) (getAllPointersInCurrentPrefix(schemaRecord)
				.max(comparing(Function.identity()))
				.orElse((short) 0) + 1);
	}
	
	private static Stream<Short> getAllPointersInCurrentPrefix(SchemaRecord schemaRecord)  {
		return Stream.concat(getOwnerRolePointers(schemaRecord.getOwnerRoles()), getMemberRolePointers(schemaRecord.getMemberRoles()))
				.sorted();
	}
	
	private static Stream<Short> getOwnerRolePointers(List<OwnerRole> ownerRoles) {
		return ownerRoles.stream().flatMap(Tools::getOwnerRolePointers)
				.sorted();
	}
	
	private static Stream<Short> getOwnerRolePointers(OwnerRole ownerRole) {
		return Arrays.asList(ownerRole.getNextDbkeyPosition(), ownerRole.getPriorDbkeyPosition()).stream()
				.filter(Objects::nonNull);
	}
	
	private static Stream<Short> getMemberRolePointers(List<MemberRole> memberRoles) {
		return memberRoles.stream().flatMap(Tools::getMemberRolePointers)
				.sorted();
	}
	
	private static Stream<Short> getMemberRolePointers(MemberRole memberRole) {
		return Arrays.asList(memberRole.getNextDbkeyPosition(), memberRole.getPriorDbkeyPosition(),
				memberRole.getOwnerDbkeyPosition(), memberRole.getIndexDbkeyPosition()).stream()
				.filter(Objects::nonNull);
	}
	
	public static String getPointers(MemberRole memberRole) {			
		return switch(memberRole.getSet().getMode()) {
			case CHAINED -> getPointersForChainedSet(memberRole);
			case INDEXED -> getPointersForIndexedSet(memberRole);
			case VSAM_INDEX -> "";
		};
	}
	
	private static String getPointersForChainedSet(MemberRole memberRole) {
		var pointers = new StringBuilder();
		pointers.append("N");
		if (memberRole.getPriorDbkeyPosition() != null) {
			pointers.append("P");
		}
		if (memberRole.getOwnerDbkeyPosition() != null) {
			pointers.append("O");
		}
		return pointers.toString();
	}
	
	private static String getPointersForIndexedSet(MemberRole memberRole) {
		var pointers = new StringBuilder();
		if (memberRole.getIndexDbkeyPosition() != null || memberRole.getOwnerDbkeyPosition() != null) {
			if (memberRole.getIndexDbkeyPosition() != null) {
				pointers.append("I");
			}
			if (memberRole.getOwnerDbkeyPosition() != null) {
				pointers.append("O");
			}
		} else {
			pointers.append("-");
		}
		return pointers.toString();
	}

	public static String getRootMessage(Throwable t) {
		var messages = new ArrayDeque<String>();
		var message = t.getMessage();
		messages.push(message != null && !message.trim().isEmpty() ? message : "An error occurred");
		for (var next = t.getCause(); next != null; next = next.getCause()) {
		    message = next.getMessage();
		    if (message != null && !message.isBlank()) {
		    		messages.push(message);
		    }
		}
		return messages.pop();
	}
	
	public static String getSortKeys(MemberRole memberRole) {
		if (memberRole.getSet().getOrder() != SetOrder.SORTED || memberRole.getSortKey() == null) { 				
			return null;
		} else {
			var sortKeys = new StringBuilder();
			var inAscendingMode = memberRole.getSortKey().getElements().isEmpty() ||
					memberRole.getSortKey().getElements().get(0).getSortSequence() == SortSequence.ASCENDING;
			for (var keyElement : memberRole.getSortKey().getElements()) {
				inAscendingMode = appendKeyElementData(keyElement, inAscendingMode, sortKeys);
			}
			sortKeys.append(") ");		
			sortKeys.append(getDuplicatesOption(memberRole.getSortKey()));
			return sortKeys.toString();
		}
	}
	
	private static boolean appendKeyElementData(KeyElement keyElement, boolean inAscendingMode, StringBuilder target) {
		var newInAscendingMode = inAscendingMode;
		var sortSequence = keyElement.getSortSequence();
		if (target.isEmpty()) {
			// very first line
			if (newInAscendingMode) {
				target.append("ASC (");
			} else {
				target.append("DESC (");
			}				
		} else if ((sortSequence == SortSequence.ASCENDING) != newInAscendingMode) {				
			// switch of sort sequence
			newInAscendingMode = sortSequence == SortSequence.ASCENDING;
			target.append("),\n");
			if (newInAscendingMode) {
				target.append("ASC (");
			} else {
				target.append("DESC (");
			}				
		} else {
			// same sort sequence
			target.append(",\n");
			// by using a tab character, things will currently not line up as we would like them to...
			target.append("\t");
		}	
		if (!keyElement.isDbkey()) {
			target.append(keyElement.getElement().getName());
		} else {
			target.append("DBKEY");
		}
		return newInAscendingMode;
	}

	public static String getStorageMode(StorageMode storageMode) {
		return switch (storageMode) {
			case FIXED -> "F";
			case FIXED_COMPRESSED -> "FC";
			case VARIABLE -> "V";
			case VARIABLE_COMPRESSED -> "VC";
		};
	}

	public static String getSystemOwnerArea(MemberRole memberRole) {
		try {
			return memberRole.getSet().getSystemOwner().getAreaSpecification().getArea().getName();
		} catch (Exception e) {
			// if, for some reason, detecting the system owner's area fails (e.g. somewhere during an index
			// removal process or when undoing the creation of a new index), return null
			return null;
		}
	}
	
	public static boolean isInvolvedInOccurs(Element element) {
		return Stream.iterate(element, Objects::nonNull, Element::getParent)
				.map(Element::getOccursSpecification)
				.anyMatch(Objects::nonNull);
	}

	public static boolean isInvolvedInRedefines(Element element) {
		return Stream.iterate(element, Objects::nonNull, Element::getParent)
				.map(Element::getRedefines)
				.anyMatch(Objects::nonNull);
	}
	
	public static Schema readFromFile(File file) {
		if (file.getName().toLowerCase().endsWith(".schema")) {
			var resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("schema", new XMIResourceFactoryImpl());
			var uri = URI.createFileURI(file.getAbsolutePath());
			var resource = resourceSet.getResource(uri, true);
			return (Schema) resource.getContents().get(0);
		} else if (file.getName().toLowerCase().endsWith(".schemadsl")) {
			return ModelFromDslBuilderForJava.schema(file);
		} else {
			throw new IllegalArgumentException("invalid file name (wrong extension): " + file.getName());
		}
	}

	/**
	 * Removes the trailing underscore from the given name (DDLCATLOD related records and sets). The given name
	 * does not necessarily have a trailing underscore.
	 * @param name
	 * @return
	 */
	public static String removeTrailingUnderscore(String name) {
		// remove the trailing underscore from the given name (DDLCATLOD related records and sets)
		var p = new StringBuilder(name);
		if (p.charAt(p.length() - 1) == '_') {
			p.setLength(p.length() - 1);
		}
		return p.toString();
	}
	
	/**
	 * @param inputStream inputStream to process; will be closed by this method
	 * @return
	 * @throws IOException
	 */
	public static byte[] writeToBuffer(InputStream inputStream) throws IOException {
		var bytesAvailable = inputStream.available();
	    var buffer = new byte[bytesAvailable];
	    var bytesRead = inputStream.read(buffer);
	    inputStream.close();
	    if (bytesRead != bytesAvailable) {
	    		Plugin.getDefault().getLog().warn("#bytes read (" + bytesRead + ") != bytes available (" + bytesAvailable + ")");
	    }
	    return buffer;
	}
	
	public static void writeToFile(byte[] buffer, File file) throws IOException {
		try (var outputStream = new FileOutputStream(file)) {
		    outputStream.write(buffer);
			outputStream.flush();
		}
	}
	
	public static void writeToFile(String data, File file) throws IOException {
		var outputStream = new FileOutputStream(file);
		var out = new PrintWriter(outputStream);
	    out.print(data);
	    out.flush();
		out.close();
		outputStream.close();
	}
	
	public static void writeToFile(Schema schema, File file) throws IOException {
		if (file.getName().toLowerCase().endsWith(".schema")) {
			writeToFileAsXMI(schema, file);
		} else if (file.getName().toLowerCase().endsWith(".schemadsl")) {
			writeToFileAsDSL(schema, file);
		} else {
			throw new IllegalArgumentException("invalid file name (wrong extension): " + file.getName());
		}
	}
	
	private static void writeToFileAsXMI(Schema schema, File file) throws IOException {
		var resourceSet = new ResourceSetImpl();
		var fileURI = URI.createFileURI(file.getAbsolutePath());
		var resource = resourceSet.createResource(fileURI);
		resource.getContents().add(schema);						
		resource.save(null);
	}
	
	private static void writeToFileAsDSL(Schema schema, File file) throws IOException {
		writeToFile(new SchemaSyntaxBuilder().build(schema), file);
	}
	
	public static boolean isAtOwnerSideOfSet(SchemaRecord schemaRecord, ConnectionPart connectionPart) {
		var set = connectionPart.getMemberRole().getSet();
		return set.getOwner() != null && schemaRecord == set.getOwner().getRecord();
	}
	
	private Tools() {
	}
	
}
