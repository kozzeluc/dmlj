package org.lh.dmlj.schema.editor.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Set;

public class SetTemplateTest extends TestCase {	
	
    static {
    	@SuppressWarnings("unused")
    	SchemaPackage _package = SchemaPackage.eINSTANCE;
    }	
	
	private static List<String> filterAndRTrim(List<String> lines) {
		List<String> result = new ArrayList<>();
		for (String line : lines) {
			if (!isLineToIgnore(line)) {
				result.add(rtrim(line));
			}
		}
		return result;
	}

	private static List<String> generateDDL(Set set) {
		SetTemplate template = new SetTemplate();
		return toFilteredAndRTrimmedList(template.generate(set));
	}
	
	private static List<String> getSetTargetDDL(String schemaSourceFileName, 
											 	String setName) {
		List<String> schemaSource = getSchemaSource(schemaSourceFileName);
		String searchString = "     SET NAME IS " + setName;
		List<String> lines = new ArrayList<>();
		boolean copying = false;		
		for (String line : schemaSource) {
			if (copying) {
				if (line.startsWith("     ADD")) {
					break;
				} else {
					lines.add(line);
				}
			} else if (line.equals(searchString)) {
				lines.add("     ADD");
				lines.add(line);
				copying = true;
			}
		}
		return filterAndRTrim(lines);
	}

	private static Schema getSchema(String fileName) {
		File file = new File(new File("testdata"), fileName);
		URI uri = URI.createFileURI(file.getAbsolutePath());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
		   		   .getExtensionToFactoryMap()
		   		   .put("schema", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.getResource(uri, true);
		Schema schema = (Schema)resource.getContents().get(0);
		if (schema == null) {
			throw new Error("schema == null");
		}
		return schema;
	}
	
	private static List<String> getSchemaSource(String fileName) {
		List<String> lines = new ArrayList<>();
		try {
			File file = new File(new File("testdata"), fileName);
			BufferedReader in = new BufferedReader(new FileReader(file));
			for (String line = in.readLine(); line != null; line = in.readLine()) {				
				lines.add(line);				
			}
			in.close();
		} catch (IOException e) {
			throw new Error(e);
		}
		return lines;
	}
	
	private static boolean isLineToIgnore(String line) {
		return false;			
	}

	private static String rtrim(String string) {
		if (!string.endsWith(" ")) {
			return string;
		}
		StringBuilder p = new StringBuilder(string);
		while (p.charAt(p.length() - 1) == ' ') {
			p.setLength(p.length() - 1);
		}
		return p.toString();
	}

	private static List<String> toFilteredAndRTrimmedList(String ddl) {
		List<String> lines = new ArrayList<>();
		StringTokenizer tokenizer = new StringTokenizer(ddl, "\n\r");
		while (tokenizer.hasMoreTokens()) {
			lines.add(tokenizer.nextToken());
		}		
		return filterAndRTrim(lines);
	}

	public void testGenerate_EMPSCHM() {
		
		// get the (extended) schema
		Schema schema = getSchema("EMPSCHM version 100.schema");
		
		// check the generated DDL for each set in the schema
		for (Set set : schema.getSets()) {
			// get the target set DDL (filtered and right trimmed)
			List<String> target = 
				getSetTargetDDL("EMPSCHM version 100.txt", set.getName());
			
			// generate the set DDL from the schema record (filtered and right 
			// trimmed)											
			List<String> generated = generateDDL(set);
					
			// compare the generated set DDL to the target DDL
			int lineCount = Math.min(target.size(), generated.size());
			for (int i = 0; i < lineCount; i++) {
				String targetLine = target.get(i);			
				String generatedLine = generated.get(i);
				assertEquals("[" + set.getName() + "] line " + (i + 1) + ", ", 
							 targetLine, generatedLine);				
			}		
			assertEquals("[" + set.getName() + "] line count mismatch, ", 
						 target.size(), generated.size());
		}
	}
	
	public void testGenerate_IDMSNTWK() {
		
		// get the (extended) schema
		Schema schema = getSchema("IDMSNTWK version 1.schema");
		
		// check the generated DDL for each set in the schema
		for (Set set : schema.getSets()) {
			
			if (!(set.getName().equals("AM-AMDEP") ||
				  set.getName().equals("CONSTRAINT-KEY") ||
				  set.getName().equals("CONSTRAINT-ORDER") ||
				  set.getName().equals("INDEX-INDEXKEY") ||
				  set.getName().equals("REFERENCED-TABLE") ||
				  set.getName().equals("REFERENCING-TABLE") ||
				  set.getName().equals("SCHEMA-CONSTRAINT") ||
				  set.getName().equals("SCHEMA-TABLE") |
				  set.getName().equals("TABLE-COLNAME") ||
				  set.getName().equals("TABLE-COLNUM") ||
				  set.getName().equals("TABLE-INDEX") ||
				  set.getName().equals("TABLE-SECTION") ||
				  set.getName().equals("TABLE-SYNTAX") ||
				  set.getName().equals("VIEW-DEPENDENT") ||
				  set.getName().equals("VIEW-REFERENCED") ||
				  set.getName().equals("RESGROUP-AUTH") ||
				  set.getName().equals("RESGROUP-RES") ||
				  set.getName().equals("RESGROUP-AUTH") ||
				  set.getName().equals("RESOURCE-AUTH") ||
				  set.getName().equals("IX-CONSTRAINT") ||
				  set.getName().equals("IX-INDEX") ||
				  set.getName().equals("IX-RESGROUP") ||
				  set.getName().equals("IX-RESOURCE") ||
				  set.getName().equals("IX-SCHEMA") ||
				  set.getName().equals("IX-TABLE"))) {
				
				// check only those sets for which we have the syntax available
				
				String setName;
				boolean ddlcatlodSet = false;
				if (set.getName().endsWith("_")) {
					setName = 
						set.getName().substring(0, set.getName().length() - 1);
					ddlcatlodSet = true;
				} else {
					setName = set.getName();
				}
				
				// get the target ste DDL (filtered and right trimmed)
				List<String> target = 
					getSetTargetDDL("IDMSNTWK version 1.txt", setName);
				
				// generate the set DDL from the set (filtered and right  
				// trimmed)											
				List<String> generated = generateDDL(set);
						
				// compare the generated set DDL to the target DDL
				int lineCount = Math.min(target.size(), generated.size());
				for (int i = 0; i < lineCount; i++) {
					String targetLine = target.get(i);			
					String generatedLine = generated.get(i);
					if (targetLine.indexOf("WITHIN AREA") == -1 || !ddlcatlodSet) {
						assertEquals("[" + set.getName() + "] line " + (i + 1) + ", ", 
								 	 targetLine, generatedLine);
					} else {
				    	assertEquals("[" + set.getName() + "] line " + (i + 1) + ", ", 
					 	 	 	 	 "*+           WITHIN AREA DDLCATLOD", 
					 	 	 	 	 generatedLine);						
					}
				}		
				assertEquals("[" + set.getName() + "] line count mismatch, ", 
							 target.size(), generated.size());
			}
		}
	}	
	
}