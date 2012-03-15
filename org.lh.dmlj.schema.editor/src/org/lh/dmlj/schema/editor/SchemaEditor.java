package org.lh.dmlj.schema.editor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.SetBooleanAttributeCommand;
import org.lh.dmlj.schema.editor.command.SetZoomLevelCommand;
import org.lh.dmlj.schema.editor.part.SchemaDiagramEditPartFactory;
import org.lh.dmlj.schema.editor.ruler.SchemaEditorRulerProvider;

public class SchemaEditor 
	extends GraphicalEditorWithFlyoutPalette 
	implements ITabbedPropertySheetPageContributor {
	
	public static final String ID = "org.lh.dmlj.schema.editor.schemaeditor";
	
	private static final EAttribute ATTRIBUTE_SHOW_GRID = 
		SchemaPackage.eINSTANCE.getDiagramData_ShowGrid();
	private static final EAttribute ATTRIBUTE_SHOW_RULERS = 
		SchemaPackage.eINSTANCE.getDiagramData_ShowRulers();	
	
	// This class listens to changes to the file system in the workspace, and
	// makes changes accordingly.
	// 1) An open, saved file gets deleted -> close the editor
	// 2) An open file gets renamed or moved -> change the editor's input
	// accordingly
	class ResourceTracker 
		implements IResourceChangeListener, IResourceDeltaVisitor {
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			try {
				if (delta != null) {
					delta.accept(this);
				}
			} catch (CoreException exception) {
				// What should be done here?
			}
		}

		public boolean visit(IResourceDelta delta) {
			if (delta == null || 
				!delta.getResource().equals(((IFileEditorInput) getEditorInput()).getFile())) {
					return true;
			}

			if (delta.getKind() == IResourceDelta.REMOVED) {
				Display display = getSite().getShell().getDisplay();
				if ((IResourceDelta.MOVED_TO & delta.getFlags()) == 0) { // if
																		 // the
																		 // file
																		 // was
																		 // deleted
					// NOTE: The case where an open, unsaved file is deleted is
					// being handled by the
					// PartListener added to the Workbench in the initialize()
					// method.
					display.asyncExec(new Runnable() {
						public void run() {
							if (!isDirty()) {
								closeEditor(false);
							}
						}
					});
				} else { // else if it was moved or renamed
					final IFile newFile = 
						ResourcesPlugin.getWorkspace()
									   .getRoot()
									   .getFile(delta.getMovedToPath());
					display.asyncExec(new Runnable() {
						public void run() {
							superSetInput(new FileEditorInput(newFile));
						}
					});
				}
			} else if (delta.getKind() == IResourceDelta.CHANGED) {
				if (!editorSaving) {
					// the file was overwritten somehow (could have been
					// replaced by another
					// version in the respository)
					final IFile newFile = 
						ResourcesPlugin.getWorkspace()
									   .getRoot()
									   .getFile(delta.getFullPath());
					Display display = getSite().getShell().getDisplay();
					display.asyncExec(new Runnable() {
						public void run() {
							setInput(new FileEditorInput(newFile));
							getCommandStack().flush();
						}
					});
				}
			}
			return false;
		}
	}

	private IPartListener partListener = new IPartListener() {
		// If an open, unsaved file was deleted, query the user to either do a
		// "Save As" or close the editor.
		public void partActivated(IWorkbenchPart part) {
			if (part != SchemaEditor.this) {
				return;
			}
			if (!((IFileEditorInput) getEditorInput()).getFile().exists()) {
				Shell shell = getSite().getShell();
				String title = "File Deleted";
				String message = 
					"The file has been deleted from the file system.  Do you " +
					"want to save your changes or close the editor without " + 
					"saving ?";
				String[] buttons = { "Save", "Close" };
				MessageDialog dialog = 
					new MessageDialog(shell, title, null, message, 
									  MessageDialog.QUESTION, buttons, 0);
				if (dialog.open() == 0) {
					if (!performSaveAs()) {
						partActivated(part);
					}
				} else {
					closeEditor(false);
				}
			}
		}

		public void partBroughtToTop(IWorkbenchPart part) {
		}

		public void partClosed(IWorkbenchPart part) {
		}

		public void partDeactivated(IWorkbenchPart part) {
		}

		public void partOpened(IWorkbenchPart part) {
		}
	};
	
	private boolean 					editorSaving = false;
	private SchemaEditorRulerProvider   horizontalRulerProvider;
	private ResourceTracker 			resourceListener = new ResourceTracker();	
	private RulerComposite				rulerComp;	
	private Schema 						schema;
	private URI    						uri;
	private SchemaEditorRulerProvider   verticalRulerProvider;
	private IResource 	   				workspaceResource;	
	
	public SchemaEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));		
	}
	
	private void closeEditor(final boolean save) {
		final Display display = PlatformUI.getWorkbench().getDisplay();        
    	display.asyncExec(new Runnable() {
    		public void run() {		
    			getSite().getPage().closeEditor(SchemaEditor.this, save);
    		}
    	});
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
		
	    // left (vertical) ruler properties				
	    verticalRulerProvider = 
	        new SchemaEditorRulerProvider(schema.getDiagramData()
	        								    .getVerticalRuler());
	    getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER,
	  				     				 verticalRulerProvider);		
	    
	    // top (horizontal) ruler properties
	    horizontalRulerProvider = 
	        new SchemaEditorRulerProvider(schema.getDiagramData()
	        									.getHorizontalRuler());		
	    getGraphicalViewer().setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER, 
					     				 horizontalRulerProvider);
	    
	    // ruler visibility (currently, the rulers are always visible)
	    getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY,
					     				 schema.getDiagramData().isShowRulers());

	    // Snap to Geometry property
	    getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED,
	    				       			 schema.getDiagramData()
	    				       			 	   .isSnapToGeometry());		
		
		// Grid properties
		getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED,
			new Boolean(schema.getDiagramData().isSnapToGrid()));
		getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE,
			new Boolean(schema.getDiagramData().isShowGrid()));
		// Set the grid spacing; the value that we need (for a spacing of half a
	    // centimeter) is somewhere between 18 and 19 (pixels); 19 seems to be 
	    // the better choice over 18 but is not exactly what we need.
		// todo: store the grid spacing in the model.
	    getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING,
					     				 new Dimension(19, 19));
		
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
		
		// add a listener to the command stack to 
		// - change the zoom manager's zoom level when the user performs an undo 
		//   or redo of a set zoom level command
		// - show or hide the rulers
		// - show or hide the grid
		// we should consider changing this implementation to add a listener 
		// that is notified when an attribute of the schema's diagram data 
		// changes value
		getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {
			@Override
			public void stackChanged(CommandStackEvent event) {				
				if (event.isPostChangeEvent() && 
					event.getCommand() instanceof SetZoomLevelCommand &&
					(event.getDetail() == CommandStack.POST_UNDO ||
					 event.getDetail() == CommandStack.POST_REDO) &&
					manager != null) {
					
					manager.setZoom(schema.getDiagramData().getZoomLevel());
				} else if (event.isPostChangeEvent() && 
						   event.getCommand() instanceof SetBooleanAttributeCommand &&
						   ((SetBooleanAttributeCommand)event.getCommand())
						   									 .getAttribute() == ATTRIBUTE_SHOW_RULERS &&
						   (event.getDetail() == CommandStack.POST_UNDO ||
							event.getDetail() == CommandStack.POST_REDO ||
							event.getDetail() == CommandStack.POST_EXECUTE)) {
							
					boolean showRulers = schema.getDiagramData().isShowRulers();
					getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY,
													 Boolean.valueOf(showRulers));
				} else if (event.isPostChangeEvent() && 
						   event.getCommand() instanceof SetBooleanAttributeCommand &&
						   ((SetBooleanAttributeCommand)event.getCommand())
						   									 .getAttribute() == ATTRIBUTE_SHOW_GRID &&
						   (event.getDetail() == CommandStack.POST_UNDO ||
							event.getDetail() == CommandStack.POST_REDO ||
							event.getDetail() == CommandStack.POST_EXECUTE)) {
							
					boolean showGrid = schema.getDiagramData().isShowGrid();					
					getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE,
													 Boolean.valueOf(showGrid));
				}
			}
		});
				
	}
	
	@Override
	protected void createGraphicalViewer(Composite parent) {
		rulerComp = new RulerComposite(parent, SWT.NONE);
	    super.createGraphicalViewer(rulerComp);
	    ScrollingGraphicalViewer graphicalViewer = 
	    	(ScrollingGraphicalViewer) getGraphicalViewer();
	    rulerComp.setGraphicalViewer(graphicalViewer);
	}
	
	public void dispose() {
		verticalRulerProvider.dispose();
		horizontalRulerProvider.dispose();
		getSite().getWorkbenchWindow().getPartService()
				.removePartListener(partListener);
		partListener = null;
		((IFileEditorInput) getEditorInput()).getFile().getWorkspace()
				.removeResourceChangeListener(resourceListener);
		super.dispose();
	}	
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		editorSaving = true;
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
		
		// refresh the resource in the workspace to avoid 'Resource is out of 
		// sync with the file system' messages
		try {			
			workspaceResource.refreshLocal(IResource.DEPTH_ZERO, null);
		} catch (Throwable e) {
			e.printStackTrace(); // just log whatever problem we encounter
		}		
		
		// Update the editor state to indicate that the contents have been saved 
		// and notify all listeners about the change in state
		getCommandStack().markSaveLocation();
		firePropertyChange(PROP_DIRTY);
		
		editorSaving = false;
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
		} else if (type == IPropertySheetPage.class) {
            return new TabbedPropertySheetPage(this);
		}

		return super.getAdapter(type);
	}	
	
	@Override
	public CommandStack getCommandStack() {
		// we need this if we want to be able to change attributes in the
		// Properties View, so that's why we override this method and make it
		// public
		return super.getCommandStack();
	}
	
	@Override
	public String getContributorId() {
		return getSite().getId();
	}
	
	@Override
	protected Control getGraphicalControl() {
		return rulerComp;
	}	

	@Override
	protected PaletteRoot getPaletteRoot() {
		PaletteRoot palette = new PaletteRoot();
        
        // selection tools
		PaletteToolbar toolbar = new PaletteToolbar("Tools");
        ToolEntry tool = new PanningSelectionToolEntry();
        toolbar.add(tool);
        palette.setDefaultEntry(tool);
        toolbar.add(new MarqueeToolEntry());
        palette.add(toolbar);
        
        // connector creation tool  
        ImageDescriptor connector16 = 
        	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/connector16.GIF"));
        ImageDescriptor connector24 = 
           	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/connector24.GIF"));
        CombinedTemplateCreationEntry component = 
        	new CombinedTemplateCreationEntry("Connector", 
        									  "Add connectors to connection", 
        									  new SimpleFactory(Connector.class), 
        									  connector16, 
        									  connector24);
        
        // Elements drawer
        PaletteDrawer createComponentsDrawer = new PaletteDrawer("Sets");
        createComponentsDrawer.add(component);
        palette.add(createComponentsDrawer);
       
        return palette; 
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
	
	private boolean performSaveAs() {
		SaveAsDialog dialog = 
			new SaveAsDialog(getSite().getWorkbenchWindow().getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();
		IPath path = dialog.getResult();

		if (path == null) {
			return false;
		}

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IFile file = workspace.getRoot().getFile(path);

		if (!file.exists()) {
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				public void execute(final IProgressMonitor monitor) {
					// Serialize the model
					ResourceSet resourceSet = new ResourceSetImpl();
					URI tmpURI = 
						URI.createFileURI(file.getLocation().toFile().getAbsolutePath());
					Resource resource = resourceSet.createResource(tmpURI);
					resource.getContents().add(schema);
					try {
						resource.save(null);
					} catch (IOException e) {
						e.printStackTrace();						
					}
				}
			};
			try {
				new ProgressMonitorDialog(getSite().getWorkbenchWindow()
						.getShell()).run(false, true, op);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			try {	
				IContainer container = file.getParent(); 
				if (container != null) {
					container.refreshLocal(IResource.DEPTH_INFINITE, null);
				}
			} catch (Throwable e) {
				e.printStackTrace(); // just log whatever problem we encounter
			}
			superSetInput(new FileEditorInput(file));
			try {
				if (workspaceResource != null) {
					workspaceResource.refreshLocal(IResource.DEPTH_ZERO, null);
				}
			} catch (Throwable e) {
				e.printStackTrace(); // just log whatever problem we encounter
			}
			getCommandStack().markSaveLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}	
	
	protected void setInput(IEditorInput input) {
		superSetInput(input);
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
		   		   .getExtensionToFactoryMap()
		   		   .put("schema", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.getResource(uri, true);
		schema = (Schema)resource.getContents().get(0);		
	}
	
	@Override
	protected void setSite(IWorkbenchPartSite site) {
		super.setSite(site);
		getSite().getWorkbenchWindow()
				 .getPartService()
				 .addPartListener(partListener);
	}	
	
	private void superSetInput(IEditorInput input) {
		// The workspace never changes for an editor. So, removing and re-adding
		// the
		// resourceListener is not necessary. But it is being done here for the
		// sake
		// of proper implementation. Plus, the resourceListener needs to be
		// added
		// to the workspace the first time around.
		if (getEditorInput() != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.getWorkspace().removeResourceChangeListener(resourceListener);
		}

		super.setInput(input);

		if (getEditorInput() != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			workspaceResource = 
				ResourcesPlugin.getWorkspace()
							   .getRoot()
							   .findMember(file.getFullPath());
			uri = URI.createFileURI(file.getLocation().toFile().getAbsolutePath());			
			file.getWorkspace().addResourceChangeListener(resourceListener);
			setPartName(file.getName());
		}
	}
}