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
import java.io.PrintWriter;

public final class Phase4Formatter {
	
	private static final String DOUBLE_STX = "\u0002\u0002";

	private static void processFile(File file, File outputFolder) throws IOException{
		try (var out = new PrintWriter(new File(outputFolder, file.getName())); var in = new BufferedReader(new FileReader(file))) {
			var inField = false;
			var inList1 = false;
			var inList2 = false;
			var inRecordLength = false;
			var p = in.readLine();
			while (p != null) {
				if (p.length() == 80 && p.startsWith("/* Record length */ -----")) {
					inRecordLength = true;
					out.println(p);
				} else if (inRecordLength) {
					out.println(p);
					inRecordLength = false;
				} else if (p.length() == 80 && p.startsWith("/* Field ") && p.contains("*/ -") && p.endsWith("-")) {
					inField = true;
					inList1 = false;
					inList2 = false;
					out.println(p);
				} else if (inField && p.startsWith("¦ ")) {
					inList1 = true;
					inList2 = false;
					out.println();
					out.println("* " + p.substring(2));
				} else if (inField && p.startsWith("– ")) {
					inList2 = true;
					out.println();
					out.println("  " + p);
				} else if (inField) {
					if (inList2) {
						out.println("    " + p);
					} else if (inList1) {
						out.println("  " + p);
					} else {
						out.println(p);
					}
				} else if (!inField && p.startsWith(DOUBLE_STX)) {
					out.println(">>" + p.substring(2));
				} else if (!trimmedLineIsNumber(p)) {
					out.println(p);
				}
				p = in.readLine();
			}
			out.flush();
		}
	}
	
	public static void performTask(File inputFolder, File outputFolder) throws IOException {
		var files = inputFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			processFile(files[i], outputFolder);
		}
	}
	
	private static boolean trimmedLineIsNumber(String untrimmedLine) {
		try {
			Integer.parseInt(untrimmedLine.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private Phase4Formatter() {
	}
	
}
