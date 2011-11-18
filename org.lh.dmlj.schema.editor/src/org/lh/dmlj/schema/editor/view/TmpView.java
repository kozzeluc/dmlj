package org.lh.dmlj.schema.editor.view;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.anchor.IndexSourceAnchor;
import org.lh.dmlj.schema.editor.anchor.IndexTargetAnchor;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;
import org.lh.dmlj.schema.editor.figure.IndexFigure;
import org.lh.dmlj.schema.editor.figure.RecordFigure;
import org.lh.dmlj.schema.editor.figure.SetDescription;

public class TmpView extends ViewPart {
	
	public static final String ID = "org.lh.dmlj.schema.editor.tmpview";
	
	private ConnectionLayer     		connections;
	private FreeformLayer	    		primary;
	private ScalableFreeformLayeredPane root;
	private Schema						schema;

	public TmpView() {
		super();
		
		String fileName = "c:/IDMS/EMPSCHM_version_100.schema";
		URI uri = URI.createFileURI(fileName);
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
		   		   .getExtensionToFactoryMap()
		   		   .put("schema", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.getResource(uri, true);
		schema = (Schema)resource.getContents().get(0);
	}
	
	private PolylineConnection connect(IFigure parent, IFigure child, 
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
	}	

	@Override
	public void createPartControl(Composite parent) {
		
		// Create a layered pane along with primary and connection layers
		root = new ScalableFreeformLayeredPane();
		root.setFont(parent.getFont());
		
		primary = new FreeformLayer();
		primary.setLayoutManager(new FreeformLayout());
		root.add(primary, "Primary");
		
		connections = new ConnectionLayer();
		connections.setConnectionRouter(new ShortestPathConnectionRouter(primary));
		root.add(connections, "Connections");
		
		// Create a canvas to display the root figure
		FigureCanvas canvas = new FigureCanvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setViewport(new FreeformViewport());
		canvas.setBackground(ColorConstants.white);
		canvas.setContents(root);
		
		RecordFigure dmclRecordFigure = new RecordFigure();
		dmclRecordFigure.setRecordName("DMCL");
		dmclRecordFigure.setRecordId((short)1035);
		dmclRecordFigure.setStorageMode("F");
		dmclRecordFigure.setRecordLength(140);
		dmclRecordFigure.setLocationMode("CALC");
		dmclRecordFigure.setLocationModeDetails("NAME");
		dmclRecordFigure.setDuplicatesOption("U");
		dmclRecordFigure.setAreaName("DDLCAT");		
		primary.add(dmclRecordFigure, 
				    new Rectangle(new Point(100, 100), 
						 	      dmclRecordFigure.getPreferredSize()));
		new FigureMover(dmclRecordFigure);
		
		RecordFigure dmclSegmentRecordFigure = new RecordFigure();
		dmclSegmentRecordFigure.setRecordName("DMCLSEGMENT");
		dmclSegmentRecordFigure.setRecordId((short)1038);
		dmclSegmentRecordFigure.setStorageMode("F");
		dmclSegmentRecordFigure.setRecordLength(136);
		dmclSegmentRecordFigure.setLocationMode("VIA");
		dmclSegmentRecordFigure.setLocationModeDetails("SEGMENT-DMCLSEG");
		dmclSegmentRecordFigure.setDuplicatesOption("");
		dmclSegmentRecordFigure.setAreaName("DDLCAT");		
		primary.add(dmclSegmentRecordFigure, 
				    new Rectangle(new Point(100, 225), 
				    			  dmclSegmentRecordFigure.getPreferredSize()));
		new FigureMover(dmclSegmentRecordFigure);
		
		IndexFigure indexFigure = new IndexFigure();
		primary.add(indexFigure, 
			    	new Rectangle(new Point(80, 10), 
			    				  indexFigure.getPreferredSize()));
		new FigureMover(indexFigure);
		
		RecordFigure ooakFigure = new RecordFigure();
		ooakFigure.setRecordName("OOAK-012");
		ooakFigure.setRecordId((short)12);
		ooakFigure.setStorageMode("F");
		ooakFigure.setRecordLength(244);
		ooakFigure.setLocationMode("CALC");
		ooakFigure.setLocationModeDetails("OOAK-KEY-012");
		ooakFigure.setDuplicatesOption("DN");
		ooakFigure.setAreaName("DDLDML");		
		primary.add(ooakFigure, 
				    new Rectangle(new Point(330, 225), 
				    			  ooakFigure.getPreferredSize()));
		new FigureMover(ooakFigure);
		
		RecordFigure lineFigure = new RecordFigure();
		lineFigure.setRecordName("LINE-109");
		lineFigure.setRecordId((short)109);
		lineFigure.setStorageMode("FC");
		lineFigure.setRecordLength(14);
		lineFigure.setLocationMode("CALC");
		lineFigure.setLocationModeDetails("LINE-NAME-109");
		lineFigure.setDuplicatesOption("DL");
		lineFigure.setAreaName("DDLDML");		
		primary.add(lineFigure, 
				    new Rectangle(new Point(530, 225), 
				    			  lineFigure.getPreferredSize()));
		new FigureMover(lineFigure);		
		
		ConnectorFigure connectorFigure1 = new ConnectorFigure();
		connectorFigure1.setLabel("A");
		primary.add(connectorFigure1, 
			    	new Rectangle(new Point(386, 325), 
			    				  connectorFigure1.getPreferredSize()));
		new FigureMover(connectorFigure1);
		
		ConnectorFigure connectorFigure2 = new ConnectorFigure();
		connectorFigure2.setLabel("A");
		primary.add(connectorFigure2, 
			    	new Rectangle(new Point(586, 175), 
			    				  connectorFigure2.getPreferredSize()));
		new FigureMover(connectorFigure2);
		
		String ixDmclLabel = 
			"IX-DMCL\n- MA SORTED\nASC (NAME-1035) DN\nDDLCATX";
		connections.add(connect(indexFigure, dmclRecordFigure, ixDmclLabel));
		String segmentDmclSegLabel = "SEGMENT-DMCLSEG\nNPO MA LAST";
		connections.add(connect(dmclRecordFigure, dmclSegmentRecordFigure, 
								  segmentDmclSegLabel));
		connections.add(connect(ooakFigure, connectorFigure1, null));
		String ooakLineLabel = "OOAK-LINE\nNP MA NEXT";
		connections.add(connect(connectorFigure2, lineFigure, ooakLineLabel));
	}

	@Override
	public void setFocus() {
	}

	public void zoom(int percentage) {
		if (percentage == 50) {
			root.setScale(0.5);
		} else if (percentage == 100) {
			root.setScale(1.0);
		} else if (percentage == 200) {
			root.setScale(2.0);
		} else if (percentage == 500) {
			root.setScale(5.0);
		} else {
			// zoom to fit
			FreeformViewport viewport = (FreeformViewport)root.getParent();
			Rectangle viewArea = viewport.getClientArea();
			
			root.setScale(1);
			Rectangle extent = root.getFreeformExtent().union(0, 0);
			double wScale = ((double)viewArea.width) / extent.width;
			double hScale = ((double)viewArea.height) / extent.height;
			double newScale = Math.min(wScale, hScale);
			
			root.setScale(newScale);			
		}
	}

}