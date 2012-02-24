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
import org.lh.dmlj.schema.SchemaRecord;

public class RecordTemplateTest extends TestCase {	
	
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

	private static List<String> generateDDL(SchemaRecord record) {
		RecordTemplate template = new RecordTemplate();
		return toFilteredAndRTrimmedList(template.generate(record));
	}
	
	private static List<String> getRecordTargetDDL(String schemaSourceFileName, 
											 	   String recordName) {
		List<String> schemaSource = getSchemaSource(schemaSourceFileName);
		String searchString = "     RECORD NAME IS " + recordName;
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
		return line.indexOf("SHARE STRUCTURE OF RECORD") > -1 ||
			   line.indexOf("RECORD NAME SYNONYM IS") > -1 ||
			   line.indexOf("ELEMENT NAME SYNONYM FOR LANGUAGE") > -1 ||
			   line.indexOf("VALUE IS (") > -1 ||
			   line.indexOf("ELEMENT LENGTH") > -1 ||
			   line.indexOf("POSITION IS") > -1 ||
			   line.indexOf("SYNONYM OF PRIMARY RECORD") > -1;			
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
		
		// check the generated DDL for each record in the schema
		for (SchemaRecord record : schema.getRecords()) {
			// get the target record DDL (filtered and right trimmed)
			List<String> target = 
				getRecordTargetDDL("EMPSCHM version 100.txt", record.getName());
			
			// generate the record DDL from the schema record (filtered and 
			// right trimmed)											
			List<String> generated = generateDDL(record);
					
			// compare the generated record DDL to the target DDL
			int lineCount = Math.min(target.size(), generated.size());
			for (int i = 0; i < lineCount; i++) {
				String targetLine = target.get(i);			
				String generatedLine = generated.get(i);
				assertEquals("[" + record.getName() + "] line " + (i + 1) + ", ", 
							 targetLine, generatedLine);				
			}		
			assertEquals("[" + record.getName() + "] line count mismatch, ", 
						 target.size(), generated.size());
		}
	}
	
	public void testGenerate_IDMSNTWK() {
		
		// get the (extended) schema
		Schema schema = getSchema("IDMSNTWK version 1.schema");
		
		// check the generated DDL for each record in the schema
		for (SchemaRecord record : schema.getRecords()) {
			
			if (!(record.getName().equals("AM-1024") ||
				  record.getName().equals("AMDEP-1025") ||
				  record.getName().equals("COLUMN-1028") ||
				  record.getName().equals("CONSTRAINT-1029") ||
				  record.getName().equals("CONSTKEY-1030") ||
				  record.getName().equals("INDEX-1041") ||
				  record.getName().equals("INDEXKEY-1042") ||
				  record.getName().equals("ORDERKEY-1044") ||
				  record.getName().equals("SCHEMA-1045") ||
				  record.getName().equals("SECTION-1046") ||
				  record.getName().equals("SYNTAX-1049") ||
				  record.getName().equals("TABLE-1050") ||
				  record.getName().equals("VIEWDEP-1051") ||
				  record.getName().equals("RESOURCE-1052") ||
				  record.getName().equals("RESOURCEGROUP-1053") ||
				  record.getName().equals("RESOURCEAUTH-1054") ||
				  record.getName().equals("RESGROUPAUTH-1055"))) {
				
				// check only those records for which we have the syntax 
				// available
				
				String recordName;
				boolean ddlcatlodRecord = false;
				if (record.getName().endsWith("_")) {
					recordName = 
						record.getName().substring(0, record.getName().length() - 1);
					ddlcatlodRecord = true;
				} else {
					recordName = record.getName();
				}
				
				// get the target record DDL (filtered and right trimmed)
				List<String> target = 
					getRecordTargetDDL("IDMSNTWK version 1.txt", recordName);
				
				// generate the record DDL from the schema record (filtered and 
				// right trimmed)											
				List<String> generated = generateDDL(record);
						
				// compare the generated record DDL to the target DDL
				int lineCount = Math.min(target.size(), generated.size());
				for (int i = 0; i < lineCount; i++) {
					String targetLine = target.get(i);			
					String generatedLine = generated.get(i);
					if (targetLine.indexOf("WITHIN AREA") == -1 || !ddlcatlodRecord) {
						assertEquals("[" + record.getName() + "] line " + (i + 1) + ", ", 
								 	 targetLine, generatedLine);
					} else if (record.getId() == 155) {
						assertEquals("[" + record.getName() + "] line " + (i + 1) + ", ", 
							 	 	 "         WITHIN AREA DDLCATLOD OFFSET 1 PAGES FOR 1 PAGES", generatedLine);
				    } else {
				    	assertEquals("[" + record.getName() + "] line " + (i + 1) + ", ", 
						 	 	 	 "         WITHIN AREA DDLCATLOD", generatedLine);
				    }
				}		
				assertEquals("[" + record.getName() + "] line count mismatch, ", 
							 target.size(), generated.size());
			}
		}
	}	
	
}