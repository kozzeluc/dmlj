package org.lh.dmlj.schema.editor.testtool;

import java.io.File;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;

public final class TestTools {	

	static {
		// reference the SchemaPackage instance to be able to read schemas from the file system
		@SuppressWarnings("unused")
		SchemaPackage schemaPackage = SchemaPackage.eINSTANCE;
	}
	
	private static void assertEquals(List<String> expectedLines, List<String> actualLines) {
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

	public static void assertEquals(ObjectGraph expected, ObjectGraph actual) {
		List<String> expectedLines = expected.getLines(); 
		List<String> actualLines = actual.getLines();
		assertEquals(expectedLines, actualLines);
	}
	
	public static void assertEquals(Syntax expected, Syntax actual) {
		List<String> expectedLines = expected.getLines(); 
		List<String> actualLines = actual.getLines();
		assertEquals(expectedLines, actualLines);
	}
	
	public static void assertEquals(Xmi expected, Xmi actual) {
		List<String> expectedLines = expected.getLines(); 
		List<String> actualLines = actual.getLines();
		assertEquals(expectedLines, actualLines);
	}
	
	public static ObjectGraph asObjectGraph(EObject root) {				
		return new ObjectGraph(root);			
	}	
	
	public static Syntax asSyntax(Schema schema) {
		return new Syntax(schema);
	}
	
	public static Xmi asXmi(Schema schema) {				
		return new Xmi(schema);			
	}
	
	public static Schema getEmpschmSchema() {		
		return getSchema("testdata/EMPSCHM version 100.schema");
	}
	
	public static Schema getIdmsntwkSchema() {
		return getSchema("testdata/IDMSNTWK version 1.schema");		
	}
	
	public static Schema getSchema(String path) {
		
		URI uri = URI.createFileURI(new File(path).getAbsolutePath());
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
		   		   .getExtensionToFactoryMap()
		   		   .put("schema", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.getResource(uri, true);
		Schema schema = (Schema) resource.getContents().get(0);
		
		return schema;		
		
	}

	private TestTools() {
		// disabled constructor
	}
	
}
