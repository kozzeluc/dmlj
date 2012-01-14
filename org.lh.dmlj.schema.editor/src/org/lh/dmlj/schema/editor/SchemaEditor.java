package org.lh.dmlj.schema.editor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.FileEditorInput;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.command.SetZoomLevelCommand;
import org.lh.dmlj.schema.editor.part.SchemaDiagramEditPartFactory;

public class SchemaEditor extends GraphicalEditorWithFlyoutPalette {
	
	public static final String ID = "org.lh.dmlj.schema.editor.schemaeditor";	
	
	private Schema schema;
	private URI    uri;
	
	public SchemaEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));		
	}

	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}
	
	@Override
	protected void configureGraphicalViewer() {		
		
		super.configureGraphicalViewer();
		
		// create the root edit part...
		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
		
		// set clipping strategy for connection layer
		/*ConnectionLayer connectionLayer = 
			(ConnectionLayer) root.getLayer(LayerConstants.CONNECTION_LAYER);
		connectionLayer.setClippingStrategy(new ViewportAwareConnectionLayerClippingStrategy(connectionLayer));*/
		
		List<String> zoomLevels = new ArrayList<>(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevels);
		
		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);		
		
		IHandlerService handlerService = 
			(IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class);
		handlerService.activateHandler(zoomIn.getActionDefinitionId(), 
									   new ActionHandler(zoomIn));
		handlerService.activateHandler(zoomOut.getActionDefinitionId(), 
				   					   new ActionHandler(zoomOut));		
		
		// configure the graphical viewer...
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setRootEditPart(root);
		viewer.setEditPartFactory(new SchemaDiagramEditPartFactory());		
		viewer.setSelectionManager(new ModifiedSelectionManager(viewer));
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		
		// configure the zoom manager with the zoom level stored in the schema
		// and attach a zoom listener to change the model via the command stack 
		// whenever the user zooms in or out...
		final ZoomManager manager = 
			(ZoomManager) getGraphicalViewer().getProperty(ZoomManager.class
																	  .toString());
		if (manager != null) {
			manager.setZoom(schema.getDiagramData().getZoomLevel());
			manager.addZoomListener(new ZoomListener() {
				@Override
				public void zoomChanged(double zoom) {			
					if (zoom != schema.getDiagramData().getZoomLevel()) {
						SetZoomLevelCommand command = 
							new SetZoomLevelCommand(schema, zoom);
						getCommandStack().execute(command);
					}
				}
			});			
		}		
		// Scroll-wheel Zoom
		/*getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
										 MouseWheelZoomHandler.SINGLETON);*/
		
		// add a listener to the command stack to change the zoom manager's zoom
		// level when the user performs an undo or redo of a set zoom level
		// command...
		getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {
			@Override
			public void stackChanged(CommandStackEvent event) {
				if (event.isPostChangeEvent() && 
					event.getCommand() instanceof SetZoomLevelCommand &&
					(event.getDetail() == CommandStack.POST_UNDO ||
					 event.getDetail() == CommandStack.POST_REDO) &&
					manager != null) {
					
					manager.setZoom(schema.getDiagramData().getZoomLevel());
				}
			}
		});
		
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		// Serialize the model
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(uri);
		resource.getContents().add(schema);
		try {
			resource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
			Status status = 
				new Status(IStatus.ERROR, ID,
						   "An exception occurred while saving the file", e);
			ErrorDialog.openError(getSite().getShell(), "Exception", 
								  e.getMessage(), status);
		}
		// Update the editor state to indicate that the contents have been saved 
		// and notify all listeners about the change in state
		getCommandStack().markSaveLocation();
		firePropertyChange(PROP_DIRTY);
	}
	
	@Override
	public void doSaveAs() {
		SaveAsDialog dialog = new SaveAsDialog(getSite().getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();
		IPath path = dialog.getResult();
		if (path == null) {
			return;
		}
		IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		super.setInput(new FileEditorInput(iFile));
		File file = iFile.getLocation().toFile();
		uri = URI.createFileURI(file.getAbsolutePath());
		doSave(null);
		// refresh the resource in the workspace to avoid 'Resource is out of 
		// sync with the file system' messages
		try {
			iFile.refreshLocal(IResource.DEPTH_ZERO, null);
			// we should probably select the file in the package explorer too
		} catch (Throwable e) {
			Status status = 
				new Status(IStatus.ERROR, ID,
						   "An exception occurred while saving the file", e);
			ErrorDialog.openError(getSite().getShell(), "Exception", 
								  e.getMessage(), status);
		}
		setPartName(iFile.getName());
		firePropertyChange(PROP_INPUT);
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
		
		if (type == ZoomManager.class) {
			String key = ZoomManager.class.toString();
			return getGraphicalViewer().getProperty(key);
		}

		return super.getAdapter(type);
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
	
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		IFile iFile = ((IFileEditorInput) input).getFile();
		setPartName(iFile.getName());
		File file = iFile.getLocation().toFile();
		uri = URI.createFileURI(file.getAbsolutePath());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
		   		   .getExtensionToFactoryMap()
		   		   .put("schema", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.getResource(uri, true);
		schema = (Schema)resource.getContents().get(0);		
	}
}