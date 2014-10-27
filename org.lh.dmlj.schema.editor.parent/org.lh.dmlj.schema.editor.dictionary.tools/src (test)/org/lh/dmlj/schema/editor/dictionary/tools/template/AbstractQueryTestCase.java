/**
 * Copyright (C) 2014  Luc Hermans
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

package org.lh.dmlj.schema.editor.dictionary.tools.template;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.preference.IDefaultDictionaryPropertyProvider;
import org.lh.dmlj.schema.editor.dictionary.tools.table.IDbkeyProvider;

public abstract class AbstractQueryTestCase {

	public static void assertEquals(List<String> expectedLines, List<String> actualLines) {
		for (int i = 0; i < expectedLines.size() && i < actualLines.size(); i++) {
			if (!expectedLines.get(i).equals(actualLines.get(i))) {
				StringBuilder message = new StringBuilder();
				message.append("expected[" + i + "]: <");
				message.append(expectedLines.get(i));
				message.append("> but was[" + i + "]: <");
				message.append(actualLines.get(i));
				message.append(">");
				throw new AssertionError(message.toString());
			}
		}
		if (expectedLines.size() > actualLines.size()) {
			StringBuilder message = new StringBuilder();
			message.append("expected[" + (expectedLines.size() - 1) + "]: <");
			message.append(expectedLines.get(expectedLines.size() - 1));
			message.append("> but was: null>");						
			throw new AssertionError(message.toString());
		} else if (expectedLines.size() != actualLines.size()) {
			StringBuilder message = new StringBuilder();
			message.append("expected: null but was[" + (actualLines.size() - 1) + "]: <");
			message.append(actualLines.get(actualLines.size() - 1));
			message.append(">");
			throw new AssertionError(message.toString());
		}
	}

	public static List<IDbkeyProvider> getDbkeyProviders(int count) {
		List<IDbkeyProvider> dbkeyProviders = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			IDbkeyProvider dbkeyProvider = mock(IDbkeyProvider.class);
			when(dbkeyProvider.getDbkey()).thenReturn(Long.valueOf(i));
			dbkeyProviders.add(dbkeyProvider);
		}
		return dbkeyProviders;
	}

	public static Dictionary getDictionary(String schema, int queryDbkeyListSizeMaximum) {
		Dictionary dictionary = mock(Dictionary.class);
		when(dictionary.getSchemaWithDefault(any(IDefaultDictionaryPropertyProvider.class))).thenReturn(schema);
		when(dictionary.getQueryDbkeyListSizeMaximumWithDefault(any(IDefaultDictionaryPropertyProvider.class)))
			.thenReturn(queryDbkeyListSizeMaximum);
		return dictionary;
	}

	public static List<String> toLines(String text) {
		List<String> lines = new ArrayList<>();
		StringTokenizer tokenizer = new StringTokenizer(text, "\n\r");
		while (tokenizer.hasMoreTokens()) {
			lines.add(tokenizer.nextToken());
		}
		return lines;
	}

	public static List<String> toLines(File file) {
		List<String> lines = new ArrayList<>();
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				lines.add(line);
			}
		} catch (IOException e) {
			fail(e.getMessage());
		}	
		return lines;
	}

}
