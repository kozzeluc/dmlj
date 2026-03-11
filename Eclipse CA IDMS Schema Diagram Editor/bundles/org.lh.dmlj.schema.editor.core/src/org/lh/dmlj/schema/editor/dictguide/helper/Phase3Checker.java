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
package org.lh.dmlj.schema.editor.dictguide.helper;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.editor.Plugin;

public final class Phase3Checker {	
	
	private static void checkField(String fileName, String fieldName, List<String> lines) {
		var recordName = fileName;
		if (fileName.length() > 5) {
			recordName = fileName.substring(0, fileName.length() - 4);
		}
		if (lines.size() < 2) {
			Plugin.getDefault().getLog().warn(recordName+ "/" + fieldName + " has too few lines: " + lines.size());
			return;
		}		
		
		var firstLine = lines.get(0);
		if (firstLine.length() < 4) {
			Plugin.getDefault().getLog().warn(recordName + "/" + fieldName + ": first line doesn't start with a level number (line is too short)");
			return;
		}
		if (firstLine.charAt(2) != ' ') {
			Plugin.getDefault().getLog().warn(recordName + "/" + fieldName + ": first line doesn't start with a level number (no space at position 3)");
			return;
		} 		
		try {
			var i = Integer.parseInt(firstLine.substring(0, 2));
			if ((i < 2 || i > 49) && i != 66 && i != 77 && i != 78 && i != 88) {
				Plugin.getDefault().getLog().warn(recordName + "/" + fieldName + ": first line doesn't start with a valid level number ('" + i + "')");
				return;				
			}
		} catch (NumberFormatException e) {
			Plugin.getDefault().getLog().warn(recordName + "/" + fieldName + ": first line doesn't start with a level number (first 2 positions not numeric)");
			return;
		}
		if (firstLine.indexOf(' ', 3) != -1) {
			Plugin.getDefault().getLog().warn(recordName + "/" + fieldName + ": first line contains more than level number and field name");
			return;
		}
		if (!firstLine.endsWith(fieldName)) {
			Plugin.getDefault().getLog().warn(recordName + "/" + fieldName + ": first line does not end with field name");
			return;
		}
		
		var secondLine = lines.get(1);
		if (!secondLine.startsWith("X") && !secondLine.startsWith("S9") && !secondLine.startsWith("COND VALUE") &&
			!secondLine.startsWith("DISPLAY") && !secondLine.startsWith("9(") && !secondLine.startsWith("BIT") &&
			!secondLine.startsWith("99V9") && !secondLine.equals("USAGE COMP-1")) {
			
			Plugin.getDefault().getLog().warn(recordName + "/" + fieldName + ": second line does not start with a valid picture or usage ('" + secondLine + "')");
		}
	}
	
	private static String extractFieldName(String p) {
		if (p.startsWith("/* Field ")) {
			var i = p.indexOf(" */ -");
			if (i != -1 && i > 9) {
				return p.substring(9, i);
			}
		}
		return null;
	}	
	
	public static void performTask(File inputFolder) throws IOException {
		var file = inputFolder.listFiles();
		for (int i = 0; i < file.length; i++) {
			processFile(file[i]);
		}		
	}

	private static void processFile(File file) throws IOException {		
		try (var in = new BufferedReader(new FileReader(file))) {
			var lines = new ArrayList<String>();
			String currentFieldName = null;
			var line = in.readLine();
			while (line != null) {
				if (line.startsWith("/* Field ")) {
					if (currentFieldName != null) {
						checkField(file.getName(), currentFieldName, lines);
					}
					currentFieldName = extractFieldName(line);
					lines.clear();
				} else if (currentFieldName != null) {
					lines.add(line);
				}
				line = in.readLine();
			}
			if (currentFieldName != null) {
				checkField(file.getName(), currentFieldName, lines);
			}
		}
	}
	
	private Phase3Checker() {
	}
	
}
