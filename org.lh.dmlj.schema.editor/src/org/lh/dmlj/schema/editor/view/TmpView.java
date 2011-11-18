package org.lh.dmlj.schema.editor.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.part.SchemaDiagramEditPartFactory;

public class TmpView extends ViewPart {
	
	public static final String 			ID = "org.lh.dmlj.schema.editor.tmpview";
	
	private static final Schema 		schema;
	
	private ScrollingGraphicalViewer	viewer;
	
	static {
		// load the EMSCHM schema...
		String fileName = "c:/IDMS/EMPSCHM_version_100.schema";
		URI uri = URI.createFileURI(fileName);
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
		   		   .getExtensionToFactoryMap()
		   		   .put("schema", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.getResource(uri, true);
		schema = (Schema)resource.getContents().get(0);
	}
	
	
	public TmpView() {
		super();		
	}
	
/*	private PolylineConnection connect(IFigure parent, IFigure child, 
									   String label) {
		PolylineConnection connection = new PolylineConnection();
		
		ConnectionAnchor sourceAnchor;
		ConnectionAnchor targetAnchor;
		PolylineDecoration decoration = null;
		
		if (parent instanceof RecordFigure) {
			sourceAnchor = new ChopboxAnchor(parent);
			targetAnchor = new ChopboxAnchor(child);
			decoration = new PolylineDecoration();
			decoration.setTemplate(PolylineDecoration.TRIANGLE_TIP);
		} else if (parent instanceof IndexFigure) {
			sourceAnchor = new IndexSourceAnchor((IndexFigure)parent);
			targetAnchor = new IndexTargetAnchor((RecordFigure)child);
		} else if (parent instanceof ConnectorFigure) {
			sourceAnchor = new EllipseAnchor(parent);
			targetAnchor = new ChopboxAnchor(child);
			decoration = new PolylineDecoration();
			decoration.setTemplate(PolylineDecoration.TRIANGLE_TIP);
		} else {
			sourceAnchor = new ChopboxAnchor(parent);
			targetAnchor = new ChopboxAnchor(child);
		}
		
		connection.setSourceAnchor(sourceAnchor);
		connection.setTargetAnchor(targetAnchor);
		if (decoration != null) {
			connection.setTargetDecoration(decoration);
		}
		connection.setLineWidth(1);		
		
		if (parent != null) {
			SetDescription setDescription = 
				new SetDescription(label, parent, targetAnchor);
			setDescription.setFont(Plugin.getDefault().getFont());
			primary.add(setDescription);
			new FigureMover(setDescription);
		}
		
		return connection;
	}*/	

	@Override
	public void createPartControl(Composite parent) {
		viewer = new ScrollingGraphicalViewer();
		viewer.createControl(parent);
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		viewer.getControl().setBackground(ColorConstants.white);	
		viewer.setEditPartFactory(new SchemaDiagramEditPartFactory());
		viewer.setContents(schema);
	}

	@Override
	public void setFocus() {
	}

}