/**
 * Copyright (C) 2014  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
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
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
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
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.print.PrintGraphicalViewerOperation;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.SetZoomLevelCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;
import org.lh.dmlj.schema.editor.outline.OutlinePage;
import org.lh.dmlj.schema.editor.palette.IChainedSetPlaceHolder;
import org.lh.dmlj.schema.editor.palette.IIndexedSetPlaceHolder;
import org.lh.dmlj.schema.editor.palette.IMultipleMemberSetPlaceHolder;
import org.lh.dmlj.schema.editor.part.SchemaDiagramEditPartFactory;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;
import org.lh.dmlj.schema.editor.ruler.SchemaEditorRulerProvider;

public class SchemaEditor 
	extends GraphicalEditorWithFlyoutPalette 
	implements CommandStackEventListener, ITabbedPropertySheetPageContributor {
	
	public static final String ID = "org.lh.dmlj.schema.editor.schemaeditor";
	
	private static final EAttribute ATTRIBUTE_SHOW_GRID = 
		SchemaPackage.eINSTANCE.getDiagramData_ShowGrid();
	private static final EAttribute ATTRIBUTE_SHOW_RULERS = 
		SchemaPackage.eINSTANCE.getDiagramData_ShowRulers();
	private static final EAttribute ATTRIBUTE_ZOOM_LEVEL =
		SchemaPackage.eINSTANCE.getDiagramData_ZoomLevel();
	
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
	
	private boolean editorSaving = false;
	private SchemaEditorRulerProvider horizontalRulerProvider;
	private ModelChangeDispatcher modelChangeDispatcher = new ModelChangeDispatcher();
	private IModelChangeListener modelChangeListener;
	private OutlinePage outlinePage;
	private PaletteRoot palette;
	private ResourceTracker resourceListener = new ResourceTracker();	
	private RulerComposite rulerComp;	
	private Schema schema;
	private SelectionSynchronizer selectionSynchronizer;
	private URI uri;
	private SchemaEditorRulerProvider verticalRulerProvider;
	private IResource workspaceResource;
	
	public SchemaEditor() {
		super();
	}
	
	private void activePaletteToolChanged(ToolEntry tool) {
		ModifiedPaletteViewerProvider paletteViewerProvider = 
			(ModifiedPaletteViewerProvider) getPaletteViewerProvider();
		paletteViewerProvider.selectTool(tool);		
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
	public void stackChanged(CommandStackEvent event) {												
		modelChangeDispatcher.dispatch(event);				
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
		viewer.setEditPartFactory(new SchemaDiagramEditPartFactory(this));		
		viewer.setSelectionManager(new ModifiedSelectionManager(viewer));
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		
	    // left (vertical) ruler properties				
	    verticalRulerProvider = 
	        new SchemaEditorRulerProvider(schema.getDiagramData().getVerticalRuler(), this);
	    getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER,
	  				     				 verticalRulerProvider);		
	    
	    // top (horizontal) ruler properties
	    horizontalRulerProvider = 
	        new SchemaEditorRulerProvider(schema.getDiagramData().getHorizontalRuler(), this);		
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
			
			// get the zoom level from the model and set the zoom managers level
			// to it (it will be ignored if out of range)
			double zoomLevel = schema.getDiagramData().getZoomLevel();
			manager.setZoom(zoomLevel);
			
			// make sure the zoom level in the model matches the value in the 
			// zoom level combo; if there is a mismatch, connection endpoints 
			// and bendpoints will go crazy
			if (manager.getZoom() != zoomLevel) {
				// the zoom level is probably 'Page', 'Width' or 'Height'; for
				// now, we will adjust the zoom level in the model (the file IS
				// marked as dirty), but it would be better if we could just
				// 'select' the right zoom level.				
				SetZoomLevelCommand command = 
					new SetZoomLevelCommand(schema, manager.getZoom(), false);
				getCommandStack().execute(command);				
			}
			
			// make sure we are informed of zoom changes
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
		
		// add a listener to the command stack to have the model change dispatcher dispatch the  
		// command stack event to inform all of its listeners of a model change 'event' - a command 
		// annotated with @ModelChange should always leave the model in a consistent state after its 
		// execute(), redo() or undo() method has been called (like any other command actually; the 
		// annotation is merely used to extract the kind [category] of model change)
		getCommandStack().addCommandStackEventListener(this);
		
		// attach a model change listener to respond to changes to rulers&guides, grid visibility 
		// and zoom level
		modelChangeListener = new IModelChangeListener() {
						
			@Override
			public void afterAddItem(EObject owner, EReference reference, Object item) {								
			}

			@Override
			public void afterMoveItem(EObject oldOwner, EReference reference, Object item, 
									  EObject newOwner) {								
			}

			@Override
			public void afterRemoveItem(EObject owner, EReference reference, Object item) {								
			}

			@Override
			public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {
				if (owner == schema.getDiagramData() && 
					features.length == 1 && features[0] == ATTRIBUTE_SHOW_RULERS) {
					
					// the rulers are to be shown or hidden
					boolean showRulers = schema.getDiagramData().isShowRulers();
					getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY,
													 Boolean.valueOf(showRulers));
				} else if (owner == schema.getDiagramData() && 
						   features.length == 1 && features[0] == ATTRIBUTE_SHOW_GRID) {
					
					// the grid has to be shown or hidden
					boolean showGrid = schema.getDiagramData().isShowGrid();					
					getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE,
													 Boolean.valueOf(showGrid));
				} else if (owner == schema.getDiagramData() && 
						   features.length == 1 && features[0] == ATTRIBUTE_ZOOM_LEVEL) {
					
					// the zoom level has changed; it is important to set the manager's zoom level
					// only when it's different from the current model value (to avoid assertion
					// errors regarding the 'dispatching' indicator in the model change dispatcher,
					// meaning we've put a command on the command stack while dispatching a command
					// stack event)
					if (manager != null && 
						schema.getDiagramData().getZoomLevel() != manager.getZoom()) {
						
						manager.setZoom(schema.getDiagramData().getZoomLevel());
					}
				}				
			}
			
		};
		modelChangeDispatcher.addModelChangeListener(modelChangeListener);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void createActions() {
		ActionRegistry registry = getActionRegistry();
		IAction action;

		action = new UndoAction(this);
		registry.registerAction(action);
		getStackActions().add(action.getId());

		action = new RedoAction(this);
		registry.registerAction(action);
		getStackActions().add(action.getId());

		action = new SelectAllAction(this);
		registry.registerAction(action);

		action = new DeleteAction((IWorkbenchPart) this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());		

		action = new PrintAction(this) {
			public void run() {
				GraphicalViewer viewer;
				viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);

				PrintDialog dialog = new PrintDialog(viewer.getControl().getShell(), SWT.NULL);
				PrinterData data = dialog.open();

				if (data != null) {
					// create a print operation
					PrintGraphicalViewerOperation op = 
						new PrintGraphicalViewerOperation(new Printer(data), viewer);
					// set the printmargins; margins are stored in pels (logical pixels; 
					// 72 pels == 1 inch)
					IPreferenceStore store = Plugin.getDefault().getPreferenceStore();					
					int topMargin = (int) (store.getInt(PreferenceConstants.TOP_MARGIN));
					int leftMargin = (int) (store.getInt(PreferenceConstants.LEFT_MARGIN));
					int bottomMargin = (int) (store.getInt(PreferenceConstants.BOTTOM_MARGIN));
					int rightMargin = (int) (store.getInt(PreferenceConstants.RIGHT_MARGIN));						
					Insets printMargin = 
						new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
					op.setPrintMargin(printMargin);					
					// run the print operation
					op.run(getWorkbenchPart().getTitle());
				}
			}			
		};
		registry.registerAction(action);
	}	
	
	@Override
	protected void createGraphicalViewer(Composite parent) {
		rulerComp = new RulerComposite(parent, SWT.NONE);
	    super.createGraphicalViewer(rulerComp);
	    ScrollingGraphicalViewer graphicalViewer = 
	    	(ScrollingGraphicalViewer) getGraphicalViewer();
	    rulerComp.setGraphicalViewer(graphicalViewer);
	}
	
	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new ModifiedPaletteViewerProvider(this);
	}
	
	public void dispose() {
		getCommandStack().removeCommandStackEventListener(this);
		ModifiedPaletteViewerProvider paletteViewerProvider = 
			(ModifiedPaletteViewerProvider) getPaletteViewerProvider();
		paletteViewerProvider.dispose(); 
		hookActivePaletteViewerToEditDomain();		
		verticalRulerProvider.dispose();
		horizontalRulerProvider.dispose();
		modelChangeDispatcher.dispose();
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

	void fireActivePaletteToolChanged(SchemaEditor source, ToolEntry tool) {
		Assert.isTrue(source == this, "event fired by another editor, this is NOT expected");
		for (SchemaEditor anEditor : getAllEditorsForInput(getEditorInput())) {
			if (anEditor != source) {
				anEditor.activePaletteToolChanged(tool);
			}
		}
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
		
		if (type == ZoomManager.class) {
			String key = ZoomManager.class.toString();
			return getGraphicalViewer().getProperty(key);
		} else if (type == IPropertySheetPage.class) {
            return new TabbedPropertySheetPage(this);
		} else if (type == IContentOutlinePage.class) {
			outlinePage = new OutlinePage(this);
			return outlinePage;
		} else if (type == CommandStack.class) {
			// the command stack is accessible by anybody else but only for executing commands -
			// implementing the IModelChangeListener is the preferred way to catch up with model 
			// changes
			Assert.isNotNull(getEditDomain(), "edit domain not yet set");
			return getCommandStack();
		} else if (type == GraphicalViewer.class) {
			return getGraphicalViewer();
		} else if (type == IModelChangeProvider.class) {
			return modelChangeDispatcher;
		} else if (type == ActionRegistry.class) {
			return getActionRegistry();
		} else if (type == DefaultEditDomain.class) {
			Assert.isNotNull(getEditDomain(), "edit domain not yet set");
			return getEditDomain();
		} else if (type == SelectionSynchronizer.class) {
			return getSelectionSynchronizer();
		}

		return super.getAdapter(type);
	}
	
	private List<SchemaEditor> getAllEditorsForInput(IEditorInput editorInput) {
		List<SchemaEditor> editors = new ArrayList<>();
		for (IWorkbenchWindow workbenchWindow : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage workbenchPage : workbenchWindow.getPages()) {
				for (IEditorReference editorReference : workbenchPage.getEditorReferences()) {					
					try {
						if (editorReference.getEditorInput().equals(editorInput)) {
							SchemaEditor schemaEditor = (SchemaEditor) editorReference.getEditor(true);
							if (schemaEditor != null) {
								editors.add(schemaEditor);
							}
						}
					} catch (PartInitException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return editors;
	}

	private SchemaEditor getFirstEditorForSameInput(IEditorInput editorInput) {
		for (IWorkbenchWindow workbenchWindow : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for (IWorkbenchPage workbenchPage : workbenchWindow.getPages()) {
				for (IEditorReference editorReference : workbenchPage.getEditorReferences()) {					
					try {
						if (editorReference.getEditorInput().equals(editorInput)) {
							SchemaEditor schemaEditor = (SchemaEditor) editorReference.getEditor(true);
							if (schemaEditor != null && schemaEditor != this) {
								return schemaEditor;
							}
						}
					} catch (PartInitException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return null;
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
		
		if (palette != null) {
			return palette;
		}
		
		SchemaEditor firstEditorForSameInput = getFirstEditorForSameInput(getEditorInput());		
		if (firstEditorForSameInput != null) {
			palette = firstEditorForSameInput.palette;
			return palette;
		}
		
		palette = new PaletteRoot();
        
        // selection tool
		ToolEntry tool = new PanningSelectionToolEntry();
		
		// label creation tool
		ImageDescriptor label16 = 
		   	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/label16.GIF"));
		ImageDescriptor label24 = 
           	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/label24.GIF"));
        CombinedTemplateCreationEntry labelCreationTool = 
        	new CombinedTemplateCreationEntry("Label", 
        									  "Add diagram label", 
        									  new SimpleFactory(DiagramLabel.class), 
        									  label16, 
        									  label24);
	    
        // record creation tool
        ImageDescriptor record16 = 
        	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/record16.gif"));
        ImageDescriptor record24 = 
           	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/record24.gif"));
        CombinedTemplateCreationEntry recordCreationTool = 
        	new CombinedTemplateCreationEntry("Record", "Add record", 
        									  new SimpleFactory(SchemaRecord.class), record16, 
        									  record24);
        recordCreationTool.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        
        // chained set creation tool
        ImageDescriptor chainedSet16 = 
        	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/chainedSet16.gif"));
        ImageDescriptor chainedSet24 = 
           	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/chainedSet24.gif"));
        ConnectionCreationToolEntry chainedSetCreationTool = 
        	new ConnectionCreationToolEntry("Chained Set", "Add chained set", 
        									new SimpleFactory(IChainedSetPlaceHolder.class), 
        									chainedSet16, chainedSet24);
        chainedSetCreationTool.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);        
        
        // multiple-member set tool
        ImageDescriptor multipleMemberSet16 = 
        	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/multipleMemberSet16.gif"));
        ImageDescriptor multipleMemberSet24 = 
           	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/multipleMemberSet24.gif"));
        ConnectionCreationToolEntry multipleMemberSetCreationTool = 
        	new ConnectionCreationToolEntry("Multiple-member Set", 
        									"Add member record type to chained set", 
        									new SimpleFactory(IMultipleMemberSetPlaceHolder.class), 
        									multipleMemberSet16, multipleMemberSet24);
        multipleMemberSetCreationTool.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, 
        											  true);        
        
        // indexed set creation tool
        ImageDescriptor indexedSet16 = 
        	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/indexedSet16.gif"));
        ImageDescriptor indexedSet24 = 
           	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/indexedSet24.gif"));
        ConnectionCreationToolEntry indexedSetCreationTool = 
        	new ConnectionCreationToolEntry("Indexed Set", "Add user owned indexed set", 
        									new SimpleFactory(IIndexedSetPlaceHolder.class), 
        									indexedSet16, indexedSet24);
        indexedSetCreationTool.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);        
        
        // index creation tool
        ImageDescriptor index16 = 
        	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/index16.gif"));
        ImageDescriptor index24 = 
           	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/index24.gif"));
        CombinedTemplateCreationEntry indexCreationTool = 
        	new CombinedTemplateCreationEntry("Index", 
        									  "Add index to record", 
        									  new SimpleFactory(SystemOwner.class), 
        									  index16, 
        									  index24);
        
        // connector creation tool  
        ImageDescriptor connector16 = 
        	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/connector16.GIF"));
        ImageDescriptor connector24 = 
           	ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/connector24.GIF"));
        CombinedTemplateCreationEntry connectorCreationTool = 
        	new CombinedTemplateCreationEntry("Connector", 
        									  "Add connectors to connection", 
        									  new SimpleFactory(Connector.class), 
        									  connector16, 
        									  connector24);
        
        // Tools toolbar
        PaletteToolbar toolbar = new PaletteToolbar("Tools");
        toolbar.add(tool);
        toolbar.add(new MarqueeToolEntry());
        palette.add(toolbar);
        
        // General drawer
        PaletteDrawer createGeneralItemsDrawer = new PaletteDrawer("General");
        createGeneralItemsDrawer.add(labelCreationTool);
        palette.add(createGeneralItemsDrawer);
        
        // Records drawer
        PaletteDrawer createRecordItemsDrawer = new PaletteDrawer("Records");
        createRecordItemsDrawer.add(recordCreationTool);
        palette.add(createRecordItemsDrawer);
        
        // Sets drawer
        PaletteDrawer createSetItemsDrawer = new PaletteDrawer("Sets");
        createSetItemsDrawer.add(chainedSetCreationTool);
        createSetItemsDrawer.add(multipleMemberSetCreationTool);
        createSetItemsDrawer.add(indexedSetCreationTool);
        createSetItemsDrawer.add(indexCreationTool);
        createSetItemsDrawer.add(connectorCreationTool);
        palette.add(createSetItemsDrawer);
        
        // the selection tool is the default entry
        palette.setDefaultEntry(tool);
       
        return palette; 
	}
	
	public Schema getSchema() {
		return schema;
	}
	
	@Override
	protected SelectionSynchronizer getSelectionSynchronizer() {
		if (selectionSynchronizer == null)
			selectionSynchronizer = new SelectionSynchronizer() {
			
			@Override
			protected EditPart convert(EditPartViewer viewer, EditPart part) {
				if (outlinePage != null && outlinePage.canConvertEditPart(viewer, part)) {
					// make sure the most relevant edit part is selected in the outline page
					return outlinePage.convert(viewer, part);
				} else {
					// this request is not for the outline page, so have the standard selection
					// synchronizer's pick the edit part
					return super.convert(viewer, part);
				}				
			}
		};
		return selectionSynchronizer;
	}

	private void hookActivePaletteViewerToEditDomain() {
		// unless the workbench is closing, we need to make sure that editors for the same diagram
		// (editor input) have a functioning palette; we need to hook an active palette viewer to 
		// the edit domain 
		if (!PlatformUI.getWorkbench().isClosing()) {
			SchemaEditor editor = getFirstEditorForSameInput(getEditorInput()); 
			if (editor != null) {
				((ModifiedPaletteViewerProvider) editor.getPaletteViewerProvider()).hookPaletteViewer();
			}			
		}
	}

	@Override
	protected void initializeGraphicalViewer() {		
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(schema);		
	}
	
	@Override
	public boolean isDirty() {
		// if the workbench is NOT closing, refer to the super's method
		if (!PlatformUI.getWorkbench().isClosing()) {			
			return super.isDirty();
		}
		// make sure we get to see exactly 1 line if the editor is dirty, and not more
		List<SchemaEditor> openEditors = getAllEditorsForInput(getEditorInput());
		if (openEditors.isEmpty() || openEditors.get(0) == this) {
			return super.isDirty();
		} else {
			return false;
		}
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
		
		Plugin.logDebug(Plugin.DebugItem.CALLING_METHOD);
		
		superSetInput(input);
		
		if (schema != null) {
			// Always keep the same Schema object to avoid trouble (note that if the file was
			// overwritten with something else, there is a mismatch between the Schema object and
			// the file contents).  We can't show a message because there is a chance that the user
			// will get to see it while importing a schema.
			/*MessageDialog.openWarning(getSite().getShell(), "Warning", "The file holding the " +
					  				  "diagram for schema " + schema.getName() + " version " +
					  				  schema.getVersion() + " (" + getTitle() + ") seems to have " +
					  				  "changed; these changes are NOT reflected in the diagram, " +
					  				  "so make sure you fix this situation.");*/
			return;
		}
		
		SchemaEditor firstEditorForSameInput = getFirstEditorForSameInput(input);		
		if (firstEditorForSameInput != null) {
			setEditDomain(firstEditorForSameInput.getEditDomain());
			schema = firstEditorForSameInput.getSchema();
		} else {
			setEditDomain(new DefaultEditDomain(null));
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry()
			   		   .getExtensionToFactoryMap()
			   		   .put("schema", new XMIResourceFactoryImpl());
			Resource resource = resourceSet.getResource(uri, true);
			schema = (Schema)resource.getContents().get(0);	
		}
			
		if (!editorSaving) {
			if (outlinePage != null) {
				outlinePage.setSchema(schema);
			}
		}
		
	}
	
	@Override
	protected void setSite(IWorkbenchPartSite site) {
		super.setSite(site);
		getSite().getWorkbenchWindow()
				 .getPartService()
				 .addPartListener(partListener);
	}	
	
	private void superSetInput(IEditorInput input) {
		// The workspace never changes for an editor. So, removing and re-adding the
		// resourceListener is not necessary. But it is being done here for the sake of proper 
		// implementation. Plus, the resourceListener needs to be added to the workspace the first 
		// time around.
		if (getEditorInput() != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.getWorkspace().removeResourceChangeListener(resourceListener);
		}

		super.setInput(input);

		if (getEditorInput() != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			workspaceResource = 
				ResourcesPlugin.getWorkspace().getRoot().findMember(file.getFullPath());
			uri = URI.createFileURI(file.getLocation().toFile().getAbsolutePath());			
			file.getWorkspace().addResourceChangeListener(resourceListener);
			setPartName(file.getName());
		}
	}

}
