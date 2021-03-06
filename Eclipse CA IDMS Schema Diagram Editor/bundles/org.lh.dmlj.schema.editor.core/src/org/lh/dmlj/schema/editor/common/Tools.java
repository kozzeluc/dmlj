/**
 * Copyright (C) 2020  Luc Hermans
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.RecordSyntaxBuilder;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.SchemaSyntaxBuilder;
import org.lh.dmlj.schema.editor.log.Logger;

public abstract class Tools {
	
	public static boolean areaMixesWithRecord(SchemaArea area, SchemaRecord record) {
		if (record.isCalc() || record.isDirect() || record.isVia()) {
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
		}
		for (SchemaRecord record : area.getRecords()) {
			if (!record.isVsam() && !record.isVsamCalc()) {
				return true;
			}
		}
		return false;
	}

	private static boolean containsVsamRecord(SchemaArea area) {
		if (area == null) {
			throw new IllegalArgumentException("area is null");
		}
		for (SchemaRecord record : area.getRecords()) {
			if (record.isVsam() || record.isVsamCalc()) {
				return true;
			}
		}
		return false;
	}
	
	public static <T> T executeWithCursorBusy(Supplier<T> code) {
		Stack<T> result = new Stack<>();
		Stack<Throwable> exceptionToThrow = new Stack<>();
		BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> {
			try {
				result.push(code.get());
			} catch (Throwable t) {
				exceptionToThrow.push(t);				
			}
		});
		if (!exceptionToThrow.isEmpty()) {
			Throwable t = exceptionToThrow.pop();			
			Logger.getLogger(Plugin.getDefault()).error("", t);
			throw new IllegalStateException(t.getMessage(), t);
		} else {
			return result.pop();
		}
	}

	public static String generateRecordElementsDSL(SchemaRecord record) {
		String dsl = new RecordSyntaxBuilder().build(record);
		int i = dsl.indexOf("\"\"\"\n");
		int j = dsl.lastIndexOf("\n\"\"\"");
		return dsl.substring(i + 3, j).replace("\n    ", "\n").substring(1);
	}

	public static String getCalcKey(Key calcKey) {
		if (calcKey == null) {
			return "";
		}
		StringBuilder p = new StringBuilder();		
		for (KeyElement keyElement :calcKey.getElements()) {
			if (p.length() > 0) {
				p.append(", ");
			}
			p.append(keyElement.getElement().getName());
		}
		return p.toString();
	}
	
	public static Element getDefaultSortKeyElement(SchemaRecord record) {
		
		if (record.getElements() == null) {
			// element list not set in record (shouldn't really happen)
			return null;
		}
		
		// traverse all elements; find and return the first suitable element
		for (Element element : record.getElements()) {
			if (!element.getName().equalsIgnoreCase("FILLER") &&
				element.getLength() <= 256 &&
				!isInvolvedInRedefines(element) &&
				!isInvolvedInOccurs(element)) {
				
				return element;
			}
		}
		
		// no suitable element defined; the record cannot participate in a sorted set
		return null;
		
	}	

	public static String getDuplicatesOption(Key calcKey) {
		if (calcKey == null) {
			return "";
		}
		DuplicatesOption duplicatesOption = calcKey.getDuplicatesOption();
		if (duplicatesOption == DuplicatesOption.NOT_ALLOWED) {
			return "DN";
		} else if (duplicatesOption == DuplicatesOption.FIRST) {
			return "DF";
		} else if (duplicatesOption == DuplicatesOption.LAST) {
			return "DL";
		} else if (duplicatesOption == DuplicatesOption.UNORDERED) {
			return "DU";
		} else {
			return "DD";
		}
	}

	public static String getMembershipOption(MemberRole memberRole) {
		SetMembershipOption membershipOption = memberRole.getMembershipOption();
		if (membershipOption == SetMembershipOption.MANDATORY_AUTOMATIC) {
			return "MA";
		} else if (membershipOption == SetMembershipOption.MANDATORY_MANUAL) {
			return "MM";
		} else if (membershipOption == SetMembershipOption.OPTIONAL_AUTOMATIC) {
			return "OA";
		} else {
			return "OM";
		}
	}
	
	public static short getFirstAvailablePointerPosition(SchemaRecord record) {
		
		short highestPointerPosition = 0;
		
		for (OwnerRole ownerRole : record.getOwnerRoles()) {
			if (ownerRole.getNextDbkeyPosition() > highestPointerPosition) {
				highestPointerPosition = ownerRole.getNextDbkeyPosition();
			}
			if (ownerRole.getPriorDbkeyPosition() != null &&
				ownerRole.getPriorDbkeyPosition()
						 .shortValue() > highestPointerPosition) {
				
				highestPointerPosition = 
					ownerRole.getPriorDbkeyPosition().shortValue();
			}			
		}
		
		for (MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole.getNextDbkeyPosition() != null &&
				memberRole.getNextDbkeyPosition()
						  .shortValue() > highestPointerPosition) {
				
				highestPointerPosition = 
					memberRole.getNextDbkeyPosition().shortValue();
			}
			if (memberRole.getPriorDbkeyPosition() != null &&
				memberRole.getPriorDbkeyPosition()
						  .shortValue() > highestPointerPosition) {
				
				highestPointerPosition = 
					memberRole.getPriorDbkeyPosition().shortValue();
			}
			if (memberRole.getOwnerDbkeyPosition() != null &&
				memberRole.getOwnerDbkeyPosition()
						  .shortValue() > highestPointerPosition) {
				
				highestPointerPosition = 
					memberRole.getOwnerDbkeyPosition().shortValue();
			}
			if (memberRole.getIndexDbkeyPosition() != null &&
				memberRole.getIndexDbkeyPosition()
						  .shortValue() > highestPointerPosition) {
				
				highestPointerPosition = 
					memberRole.getIndexDbkeyPosition().shortValue();
			}
		}
		
		return (short) (highestPointerPosition + 1);
		
	}
	
	public static String getPointers(MemberRole memberRole) {
		
		if (memberRole.getSet().isVsam()) {
			return "";
		}
		
		StringBuilder p = new StringBuilder();
		
		if (memberRole.getSet().getMode() == SetMode.CHAINED) {
			p.append("N");
			if (memberRole.getPriorDbkeyPosition() != null) {
				p.append("P");
			}
			if (memberRole.getOwnerDbkeyPosition() != null) {
				p.append("O");
			}
		} else {
			if (memberRole.getIndexDbkeyPosition() != null ||
				memberRole.getOwnerDbkeyPosition() != null) {
				
				if (memberRole.getIndexDbkeyPosition() != null) {
					p.append("I");
				}
				if (memberRole.getOwnerDbkeyPosition() != null) {
					p.append("O");
				}
			} else {
				p.append("-");
			}
		}
		
		return p.toString();
	}

	public static String getRootMessage(Throwable t) {
		Stack<String> messages = new Stack<>();
		String message = t.getMessage();
		messages.push(message != null && !message.trim().isEmpty() ? message : "An error occurred");
		for (Throwable next = t.getCause(); next != null; next = next.getCause()) {
		    message = next.getMessage();
		    if (message != null && !message.trim().isEmpty()) {
		    	messages.push(message);
		    }
		}
		message = messages.pop();
		return message;
	}
	
	public static String getSortKeys(MemberRole memberRole) {
		
		if (memberRole.getSet().getOrder() != SetOrder.SORTED || memberRole.getSortKey() == null) { 				
			return null;
		}
		
		StringBuilder p = new StringBuilder();
		boolean ascending = 
			memberRole.getSortKey().getElements().get(0).getSortSequence() == SortSequence.ASCENDING;
		for (KeyElement keyElement : memberRole.getSortKey().getElements()) {
			SortSequence sortSequence = keyElement.getSortSequence();
			if (p.length() == 0) {
				// very first line
				if (ascending) {
					p.append("ASC (");
				} else {
					p.append("DESC (");
				}				
			} else if ((sortSequence == SortSequence.ASCENDING) != ascending) {				
				// switch of sort sequence
				ascending = keyElement.getSortSequence() == SortSequence.ASCENDING;
				p.append("),\n");
				if (ascending) {
					p.append("ASC (");
				} else {
					p.append("DESC (");
				}				
			} else {
				// same sort sequence
				p.append(",\n");
				// by using a tab character, things will currently not line up 
				// as we would like them to...
				p.append("\t");
			}	
			if (!keyElement.isDbkey()) {
				p.append(keyElement.getElement().getName());
			} else {
				p.append("DBKEY");
			}
		}
		p.append(") ");		
		p.append(getDuplicatesOption(memberRole.getSortKey()));
		return p.toString();
	}

	public static String getStorageMode(StorageMode storageMode) {
		if (storageMode == StorageMode.FIXED) {
			return "F";
		} else if (storageMode == StorageMode.FIXED_COMPRESSED) {
			return "FC";
		} else if (storageMode == StorageMode.VARIABLE) {
			return "V";
		} else {
			return "VC";
		}
	}

	public static String getSystemOwnerArea(MemberRole memberRole) {
		try {
			return memberRole.getSet()
							 .getSystemOwner()
							 .getAreaSpecification()
							 .getArea()
							 .getName();
		} catch (Throwable t) {
			// if, for some reason, detecting the system owner's area fails (e.g. somewhere during
			// an index removal process or when undoing the creation of a new index), return null
			return null;
		}
	}
	
	public static boolean isInvolvedInOccurs(Element element) {
		
		// check if the element itself has an OCCURS specification 
		if (element.getOccursSpecification() != null) {
			return true;
		}
		
		// check if any of the element's parent fields, if any, has an OCCURS specification
		Element parent = element.getParent();
		while (parent != null) {
			if (parent.getOccursSpecification() != null) {
				return true;
			}
			parent = parent.getParent();
		}
		
		return false;
		
	}

	public static boolean isInvolvedInRedefines(Element element) {		
		
		// check if the element itself refers to a redefined field 
		if (element.getRedefines() != null) {
			return true;
		}
		
		// check if any of the element's parent fields, if any, refers to a redefined field
		Element parent = element.getParent();
		while (parent != null) {
			if (parent.getRedefines() != null) {
				return true;
			}
			parent = parent.getParent();
		}
		
		return false;
		
	}
	
	public static Schema readFromFile(File file) {
		if (file.getName().toLowerCase().endsWith(".schema")) {
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry()
			   		   .getExtensionToFactoryMap()
			   		   .put("schema", new XMIResourceFactoryImpl());
			URI uri = URI.createFileURI(file.getAbsolutePath());
			Resource resource = resourceSet.getResource(uri, true);
			return (Schema) resource.getContents().get(0);
		} else if (file.getName().toLowerCase().endsWith(".schemadsl")) {
			return ModelFromDslBuilderForJava.schema(file);
		} else {
			throw new IllegalArgumentException("invalid file name (wrong extension): " + file.getName());
		}
	}

	/**
	 * Removes the trailing underscore from the given name (DDLCATLOD related
	 * records and sets).  The given name does not necessarily have a trailing
	 * underscore.
	 * @param name
	 * @return
	 */
	public static String removeTrailingUnderscore(String name) {
		// remove the trailing underscore from the given name (DDLCATLOD related
		// records and sets)
		StringBuilder p = new StringBuilder(name);
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
	    byte[] buffer = new byte[inputStream.available()];
	    inputStream.read(buffer);
	    inputStream.close();
	    return buffer;
	}
	
	public static void writeToFile(byte[] buffer, File file) throws IOException {
		OutputStream outputStream = new FileOutputStream(file);
	    outputStream.write(buffer);
		outputStream.flush();
		outputStream.close();
	}
	
	public static void writeToFile(String data, File file) throws IOException {
		OutputStream outputStream = new FileOutputStream(file);
		PrintWriter out = new PrintWriter(outputStream);
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
		ResourceSet resourceSet = new ResourceSetImpl();
		URI fileURI = URI.createFileURI(file.getAbsolutePath());
		Resource resource = resourceSet.createResource(fileURI);
		resource.getContents().add(schema);						
		resource.save(null);
	}
	
	private static void writeToFileAsDSL(Schema schema, File file) throws IOException {
		writeToFile(new SchemaSyntaxBuilder().build(schema), file);
	}
	
}
