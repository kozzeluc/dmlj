package org.lh.dmlj.schema.editor.testtool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class Xmi {

	private List<String> lines = new ArrayList<>();
	
	Xmi(EObject root) {
		super();
		try {
			File tmpFile = new File("testdata/tmp/" + System.currentTimeMillis() + ".xmi");
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry()
			   		   .getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
			URI uri = URI.createFileURI(tmpFile.getAbsolutePath());	
			Resource resource = resourceSet.createResource(uri);
			resource.getContents().add(root);
			resource.save(null);			
			BufferedReader in = new BufferedReader(new FileReader(tmpFile));
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				lines.add(line);
			}
			in.close();
			if (!tmpFile.delete()) {
				System.err.println("could not delete " + tmpFile.getAbsolutePath());
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(t);
		}		
	}
	
	List<String> getLines() {
		return lines;
	}
	
}
