package org.lh.dmlj.schema.editor.dictguide.helper;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public abstract class Phase3Checker {	
	
	private static void checkField(String fileName, String fieldName, 
								   Vector<String> vector) {
		String recordName = fileName;
		if (fileName.length() > 5) {
			recordName = fileName.substring(0, fileName.length() - 4);
		}
		String[] p = new String[vector.size()];
		for (int i = 0; i < vector.size(); i++) {
			p[i] = vector.elementAt(i);
		}
		if (p.length < 2) {
			System.out.println(recordName+ "/" + fieldName + 
							   " has too few lines: " + p.length);
			return;
		}
		if (p[0].length() < 4) {
			System.out.println(recordName + "/" + fieldName + 
							   ": first line doesn't start with " +
						       "a level number (line is too short)");
			return;
		}
		if (p[0].charAt(2) != ' ') {
			System.out.println(recordName + "/" + fieldName + 
							   ": first line doesn't start with " +
						       "a level number (no space at position 3)");
			return;
		} 		
		try {
			int i = Integer.parseInt(p[0].substring(0, 2));
			if ((i < 2 || i > 49) && i != 66 && i != 77 && i != 78 && i != 88) {
				System.out.println(recordName + "/" + fieldName + 
						   		   ": first line doesn't start " + 
								   "with a valid level number ('" + i + "')");
				return;				
			}
		} catch (NumberFormatException e) {
			System.out.println(recordName + "/" + fieldName + 
					   		   ": first line doesn't start with a" +
		       				   "level number (first 2 positions not numeric)");
			return;
		}
		if (p[0].indexOf(' ', 3) != -1) {
			System.out.println(recordName + "/" + fieldName + 
					   		   ": first line contains more than " +
							   "level number and field name");
			return;
		}
		if (!p[0].endsWith(fieldName)) {
			System.out.println(recordName + "/" + fieldName + 
					   		   ": first line does not end with " +
			   				   "field name");
			return;
		}
		if (!p[1].startsWith("X") && !p[1].startsWith("S9") &&
			!p[1].startsWith("COND VALUE") && !p[1].startsWith("DISPLAY") &&
			!p[1].startsWith("9(") && !p[1].startsWith("BIT") &&
			!p[1].startsWith("99V9") && !p[1].equals("USAGE COMP-1")) {
			
			System.out.println(recordName + "/" + fieldName + 
					   		   ": second line does not start " +
							   "with a valid picture or usage ('" + p[1] + 
							   "')");
			return;
		}
	}
	
	private static String extractFieldName(String p) {
		if (p.startsWith("/* Field ")) {
			int i = p.indexOf(" */ -");
			if (i != -1 && i > 9) {
				return p.substring(9, i);
			}
		}
		return null;
	}	
	
	public static void performTask(File inputFolder) throws IOException {
		File[] file = inputFolder.listFiles();
		for (int i = 0; i < file.length; i++) {
			processFile(file[i]);
		}		
	}

	private static void processFile(File file) throws IOException {		
		BufferedReader in = new BufferedReader(new FileReader(file));
		Vector<String> vector = new Vector<String>();
		String currentFieldName = null;
		String p = in.readLine();
		while (p != null) {
			if (p.startsWith("/* Field ")) {
				if (currentFieldName != null) {
					checkField(file.getName(), currentFieldName, vector);
				}
				currentFieldName = extractFieldName(p);
				vector.clear();
			} else if (currentFieldName != null) {
				vector.addElement(p);
			}
			p = in.readLine();
		}
		in.close();
		if (currentFieldName != null) {
			checkField(file.getName(), currentFieldName, vector);
		}
	}
}