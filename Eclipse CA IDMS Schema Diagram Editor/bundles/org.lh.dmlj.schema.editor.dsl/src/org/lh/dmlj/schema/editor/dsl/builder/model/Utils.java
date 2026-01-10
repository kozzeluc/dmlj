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
package org.lh.dmlj.schema.editor.dsl.builder.model;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class Utils {
	
	public static String getSchemadslFileContents(File file) throws IOException {
		try (var lines = Files.lines(file.toPath())) {
			return lines
					.map(Utils::toImprovedLine)
					.collect(joining(System.lineSeparator()));
		}
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
