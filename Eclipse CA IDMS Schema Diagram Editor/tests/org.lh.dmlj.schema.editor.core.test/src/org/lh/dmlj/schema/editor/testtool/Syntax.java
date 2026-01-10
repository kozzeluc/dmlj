package org.lh.dmlj.schema.editor.testtool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.template.SchemaTemplate;

public class Syntax {

	private List<String> lines = new ArrayList<>();
	
	Syntax(Schema schema) {
		super();
		SchemaTemplate template = new SchemaTemplate();
		String syntax = 
			template.generate(Arrays.asList(new Object[] {schema, Boolean.TRUE, Boolean.FALSE}));
		try {
			BufferedReader in = new BufferedReader(new StringReader(syntax));
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				lines.add(line);				
			}
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	List<String> getLines() {
		return lines;
	}
	
}
