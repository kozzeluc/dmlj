/**
 * Copyright (C) 2021  Luc Hermans
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

public abstract class Phase4Formatter {
	
	private static void processFile(File file, File outputFolder) 
		throws IOException{
		
		PrintWriter out = 
			new PrintWriter(new File(outputFolder, file.getName()));
		boolean inField = false;
		boolean inList1 = false;
		boolean inList2 = false;
		boolean inRecordLength = false;
		BufferedReader in = new BufferedReader(new FileReader(file));
		String p = in.readLine();
		while (p != null) {
			if (p.length() == 80 && p.startsWith("/* Record length */ -----")) {
				inRecordLength = true;
				out.println(p);
			} else if (inRecordLength) {
				out.println(p);
				inRecordLength = false;
			} else if (p.length() == 80 && p.startsWith("/* Field ") && 
				p.indexOf("*/ -") != -1 && p.endsWith("-")) {
				
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
			} else if (!inField && p.startsWith("")) {
				out.println(">>" + p.substring(2));
			} else if (!trimmedLineIsNumber(p)) {
				out.println(p);
			}
			p = in.readLine();
		}
		in.close();
		out.flush();
		out.close();
	}
	
	public static void performTask(File inputFolder, File outputFolder) 
		throws IOException {
		
		File[] file = inputFolder.listFiles();
		for (int i = 0; i < file.length; i++) {
			processFile(file[i], outputFolder);
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
}
