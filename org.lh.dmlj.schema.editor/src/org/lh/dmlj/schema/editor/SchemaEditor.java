package org.lh.dmlj.schema.editor;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.part.SchemaDiagramEditPartFactory;

public class SchemaEditor extends GraphicalEditorWithFlyoutPalette {
	
	private Schema schema;

	private static Schema readSchemaFromFile(File file) {
		// load the EMSCHM schema...
		URI uri = URI.createFileURI(file.getAbsolutePath());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
		   		   .getExtensionToFactoryMap()
		   		   .put("schema", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.getResource(uri, true);
		Schema schema = (Schema)resource.getContents().get(0);
		return schema;
	}	
	
	public SchemaEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));		
	}

	@Override
	protected void configureGraphicalViewer() {		
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new SchemaDiagramEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		return null;
	}

	@Override
	protected void initializeGraphicalViewer() {		
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(schema);
	}
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		IFile iFile = ((IFileEditorInput) input).getFile();
		setPartName(iFile.getName());
		File file = iFile.getLocation().toFile();
		schema = readSchemaFromFile(file);
		System.out.println("setInput() OK");
	}
}