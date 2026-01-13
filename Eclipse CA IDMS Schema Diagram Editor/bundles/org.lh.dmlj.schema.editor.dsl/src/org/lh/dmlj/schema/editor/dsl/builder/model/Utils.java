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
package org.lh.dmlj.schema.editor.dsl.builder.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class Utils {
	
	public static String getSchemadslFileContents(File file) throws IOException {
		var contents = new StringBuilder();
		try (var fr = new FileReader(file); var in = new BufferedReader(fr)) {
			for (var line = in.readLine(); line != null; line = in.readLine()) {
				if (!contents.isEmpty()) {
					contents.append(System.lineSeparator());
				}
				contents.append(toImprovedLine(line));
			}
		}
		return contents.toString();
	}
	
	private static String toImprovedLine(String line) {
		var trimmedLine = line.trim();
		if (trimmedLine.startsWith("call '") && trimmedLine.endsWith("'")) {
			return line.replaceFirst("call '", "callProcedure '");
		} else {
			return line;
		}
	}
	
	private Utils() {
	}

}
