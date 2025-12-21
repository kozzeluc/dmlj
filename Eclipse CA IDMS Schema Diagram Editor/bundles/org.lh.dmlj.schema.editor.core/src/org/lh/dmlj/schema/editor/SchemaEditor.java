/**
 * Copyright (C) 2025  Luc Hermans
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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.SetZoomLevelCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.log.DebugItem;
import org.lh.dmlj.schema.editor.log.Logger;
import org.lh.dmlj.schema.editor.outline.OutlinePage;
import org.lh.dmlj.schema.editor.part.SchemaDiagramEditPartFactory;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;
import org.lh.dmlj.schema.editor.ruler.SchemaEditorRulerProvider;

public class SchemaEditor 
	extends GraphicalEditorWithFlyoutPalette 
	implements CommandStackEventListener, ITabbedPropertySheetPageContributor {
	
	public static final String ID = "org.lh.dmlj.schema.editor.schemaeditor";
	private static final String ORG_ECLIPSE_GEF_ID = "org.eclipse.gef";
		
	private static final String FILE_EXTENSION_SCHEMA = "schema";
	private static final String FILE_EXTENSION_SCHEMADSL = "schemadsl";
	private static final String ADD_ZOOM_LISTENER = "addZoomListener";
	private static final String ZOOM_CHANGED = "zoomChanged";
	
	private static final EAttribute ATTRIBUTE_SHOW_GRID = SchemaPackage.eINSTANCE.getDiagramData_ShowGrid();
	private static final EAttribute ATTRIBUTE_SHOW_RULERS = SchemaPackage.eINSTANCE.getDiagramData_ShowRulers();
	
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
		
	private static final Function<String, Optional<Class<?>>> toZoomListenerClass = name -> {
		try {
			return Optional.of(Class.forName(name));
		} catch (ClassNotFoundException e) {
			return Optional.empty();
		}
	};
	private static final Function<Class<?>, Optional<Method>> toAddZoomListenerMethod = c -> 
		Arrays.stream(ZoomManager.class.getMethods())
				.filter(m -> ADD_ZOOM_LISTENER.equals(m.getName()))
				.filter(m -> m.getParameterCount() == 1)
				.filter(m -> m.getParameterTypes()[0] == c)
				.findFirst();
	
	private static List<String> zoomListenerClassCandidateNames =
			List.of("org.eclipse.draw2d.zoom.ZoomListener", "org.eclipse.gef.editparts.ZoomListener");
	private static Method addZoomListenerMethod;
		
	static {
		addZoomListenerMethod = zoomListenerClassCandidateNames.stream()
				.map(toZoomListenerClass)
				.flatMap(Optional::stream)
				.map(toAddZoomListenerMethod)
				.flatMap(Optional::stream)
				.findFirst()
				.orElse(null);
		if (addZoomListenerMethod == null) {
			Plugin.getDefault().getLog().error("No ZoomListener class found for " + ZoomManager.class.getName() + 
					" or no addZoomListener method found for neither " + zoomListenerClassCandidateNames + "; zooming will NOT function properly");
		} else {
			Plugin.getDefault().getLog().info("ZoomListener class: " + addZoomListenerMethod.getParameterTypes()[0].getName());
		}
	}
	
	private static IEditorInput getEditorInput(IEditorReference editorReference) {
		try {
			return editorReference.getEditorInput();
		} catch (PartInitException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private static Stream<SchemaEditor> getSchemaEditorsWithSameInput(IEditorInput editorInput) {
		Function<IEditorReference, IEditorPart> getEditorWithRestore = e -> e.getEditor(true);
		return Arrays.stream(PlatformUI.getWorkbench().getWorkbenchWindows())
				.map(IWorkbenchWindow::getPages)
				.flatMap(Arrays::stream)
				.map(IWorkbenchPage::getEditorReferences)
				.flatMap(Arrays::stream)
				.filter(editorReference -> editorInput.equals(getEditorInput(editorReference)))
				.map(getEditorWithRestore)
				.filter(SchemaEditor.class::isInstance)
				.map(SchemaEditor.class::cast);
	}

	private IPartListener partListener = new SchemaEditorPartListener(this);
	private boolean editorSaving = false;
	private SchemaEditorRulerProvider horizontalRulerProvider;
	private ModelChangeDispatcher modelChangeDispatcher = new ModelChangeDispatcher();
	private OutlinePage outlinePage;
	private PaletteRoot palette;
	private SchemaEditorResourceTracker resourceListener = new SchemaEditorResourceTracker(this);	
	private RulerComposite rulerComp;	
	private Schema schema;
	private SelectionSynchronizer selectionSynchronizer;
	private URI uri;
	private SchemaEditorRulerProvider verticalRulerProvider;
	private IResource workspaceResource;
	private boolean readOnlyFlag = false;
	private String fileExtension;
	private boolean needToRemoveAsCommandStackEventListener = false;
	
	@Override
	protected void createGraphicalViewer(Composite parent) {
		rulerComp = new RulerComposite(parent, SWT.NONE);
	    super.createGraphicalViewer(rulerComp);
	    var graphicalViewer = (ScrollingGraphicalViewer) getGraphicalViewer();
	    rulerComp.setGraphicalViewer(graphicalViewer);
	}
	
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		
		// create the root edit part...
		var root = new ScalableFreeformRootEditPart();
		
		var zoomLevels = new ArrayList<String>();
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevels);
		
		var zoomIn = new ZoomInAction(root.getZoomManager());
		var zoomOut = new ZoomOutAction(root.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);		
		
		// configure the graphical viewer...
		var viewer = getGraphicalViewer();
		viewer.setRootEditPart(root);
		viewer.setEditPartFactory(new SchemaDiagramEditPartFactory(this));		
		viewer.setSelectionManager(new ModifiedSelectionManager(viewer));
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		
	    // left (vertical) ruler properties				
	    verticalRulerProvider = new SchemaEditorRulerProvider(schema.getDiagramData().getVerticalRuler(), this);
	    getGraphicalViewer().setProperty(RulerProvider.PROPERTY_VERTICAL_RULER, verticalRulerProvider);		
	    
	    // top (horizontal) ruler properties
	    horizontalRulerProvider = new SchemaEditorRulerProvider(schema.getDiagramData().getHorizontalRuler(), this);		
	    getGraphicalViewer().setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER, horizontalRulerProvider);
	    
	    // ruler visibility (currently, the rulers are always visible)
	    getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, schema.getDiagramData().isShowRulers());

	    // Snap to Geometry property
	    getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, schema.getDiagramData().isSnapToGeometry());		
		
		// Grid properties
		getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, schema.getDiagramData().isSnapToGrid());
		getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, schema.getDiagramData().isShowGrid());
		// Set the grid spacing; the value that we need (for a spacing of half a centimeter) is somewhere between
		// 18 and 19 (pixels); 19 seems to be the better choice over 18 but is not exactly what we need.	
	    getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(19, 19));
		
		// configure the zoom manager with the zoom level stored in the schema and attach a zoom listener to change
	    // the model via the command stack whenever the user zooms in or out...
		var manager = (ZoomManager) getGraphicalViewer().getProperty(ZoomManager.class.toString());
		if (manager != null) {
			// get the zoom level from the model and set the zoom managers level to it (it will be ignored if out
			// of range)
			var zoomLevel = schema.getDiagramData().getZoomLevel();
			manager.setZoom(zoomLevel);
			
			// make sure the zoom level in the model matches the value in the zoom level combo; if there is a 
			// mismatch, connection endpoints and bendpoints will go crazy
			if (manager.getZoom() != zoomLevel) {
				// the zoom level is probably 'Page', 'Width' or 'Height'; for now, we will adjust the zoom level
				// in the model (the file IS marked as dirty), but it would be better if we could just 'select'
				// the right zoom level.				
				var command = new SetZoomLevelCommand(schema, manager.getZoom(), false);
				getCommandStack().execute(command);				
			}
			
			// make sure we are informed of zoom changes (the graphical editor automatically adjusts the graphics)
			if (addZoomListenerMethod != null) {
				addZoomListener(manager);
			}
		}
		
		// add a listener to the command stack to have the model change dispatcher dispatch the command stack event
		// to inform all of its listeners of a model change 'event' - a command annotated with @ModelChange should
		// always leave the model in a consistent state after its execute(), redo() or undo() method has been
		// called (like any other command actually; the annotation is merely used to extract the kind [category]
		// of model change)
		// ATTENTION: since GEF 3.24, the GraphicalEditor's init method will already have added us as a listener and we
		// don't want any command stack event to be processed twice because that causes trouble
		if (!alreadyAddedAsCommandStackEventListener()) {
			getCommandStack().addCommandStackEventListener(this);
			needToRemoveAsCommandStackEventListener = true;
		}
		
		// attach a model change listener to respond to changes to rulers&guides, grid visibility and zoom level
		var modelChangeListener = new IModelChangeListener() {
						
			@Override
			public void afterModelChange(ModelChangeContext context) {		
				if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&
					context.isPropertySet(ATTRIBUTE_SHOW_RULERS)) {
					
					// the rulers are to be shown or hidden
					var showRulers = schema.getDiagramData().isShowRulers();
					getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, Boolean.valueOf(showRulers));
				} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY &&
						   context.isPropertySet(ATTRIBUTE_SHOW_GRID)) { 
					
					// the grid has to be shown or hidden
					var showGrid = schema.getDiagramData().isShowGrid();					
					getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, Boolean.valueOf(showGrid));
				} else if ((context.getModelChangeType() == ModelChangeType.ZOOM_IN || context.getModelChangeType() == ModelChangeType.ZOOM_OUT) &&
							manager != null && schema.getDiagramData().getZoomLevel() != manager.getZoom()) {
						
					// the zoom level has changed; it is important to set the manager's zoom level only when it's
					// different from the current model value (to avoid assertion errors regarding the 'dispatching' 
					// indicator in the model change dispatcher, meaning we've put a command on the command stack
					// while dispatching a command stack event); it turns out that this is only the case when the
					// zoom command is undone or redone (which makes sense since we are not using the normal zoom
					// controls in that situation)
					manager.setZoom(schema.getDiagramData().getZoomLevel());
				}
			}
			
			@Override
			public void beforeModelChange(ModelChangeContext context) {
				// ignore
			}
			
		};
		modelChangeDispatcher.addModelChangeListener(modelChangeListener);
	}
	
	private boolean alreadyAddedAsCommandStackEventListener() {
		var gefBundle = Platform.getBundle(ORG_ECLIPSE_GEF_ID);
		var gefMajorVersion = gefBundle.getVersion().getMajor();
		var gefMinorVersion = gefBundle.getVersion().getMinor();
		return gefMajorVersion == 3 && gefMinorVersion >= 24 || gefMajorVersion > 3;
	}

	private void addZoomListener(ZoomManager manager) {
		try {
			var invocationHandler = new InvocationHandler() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					if (ZOOM_CHANGED.equals(method.getName()) && args.length == 1 && args[0].getClass() == Double.class) {
						handleZoom((double) args[0]);
					}
					return null;
				}
			};
			var zoomListener = Proxy.newProxyInstance(manager.getClass().getClassLoader(),
					new Class[] { addZoomListenerMethod.getParameterTypes()[0] }, invocationHandler);
			addZoomListenerMethod.invoke(manager, zoomListener);
		} catch (IllegalAccessException | InvocationTargetException e) {
			Plugin.getDefault().getLog().error("An Exception was thrown while adding the ZoomListener", e);
		}
	}

	private void handleZoom(double zoom) {
		if (zoom != schema.getDiagramData().getZoomLevel()) {
			ModelChangeType modelChangeType;
			if (zoom < schema.getDiagramData().getZoomLevel()) {
				modelChangeType = ModelChangeType.ZOOM_OUT;
			} else {
				modelChangeType = ModelChangeType.ZOOM_IN;
			}
			var context = new ModelChangeContext(modelChangeType);
			var command = new SetZoomLevelCommand(schema, zoom);
			command.setContext(context);
			getCommandStack().execute(command);
		}
	}	
	
	@Override
	protected void createActions() {
		new SchemaEditorActionCreator(this).createActions(getActionRegistry(), getStackActions(), getSelectionActions());
	}
	
	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new ModifiedPaletteViewerProvider(this);
	}
	
	void fireActivePaletteToolChanged(SchemaEditor source, ToolEntry tool) {
		Assert.isTrue(source == this, "event fired by another editor, this is NOT expected");
		for (var anEditor : getAllEditorsForInput(getEditorInput())) {
			if (anEditor != source) {
				anEditor.activePaletteToolChanged(tool);
			}
		}
	}
	
	private void activePaletteToolChanged(ToolEntry tool) {
		var paletteViewerProvider = (ModifiedPaletteViewerProvider) getPaletteViewerProvider();
		paletteViewerProvider.selectTool(tool);		
	}
	
	@Override
	public void stackChanged(CommandStackEvent event) {
		if (event.isPostChangeEvent() && !isReadOnlyMode()) {
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
		// As of GEF 3.11 (Eclipse Neon), CommandStackEventListener instances are also notified when:
		// - flushing the stack (event.detail == 64/256)
		// - marking the save location of the stack (event.detail == 128/512)
		// In both cases, the event's command is set to null so there is nothing we should do (apart from preventing a
		// NPE further down the line, e.g. when saving a diagram).
		if (event.getCommand() != null) {
			modelChangeDispatcher.setSchema(schema);
			modelChangeDispatcher.dispatch(event);
		}
	}
	
	void flushCommandStack() {
		getCommandStack().flush();
	}
	
	boolean isEditorSaving() {
		return editorSaving;
	}
	
	@Override
	public void doSaveAs() {
		var dialog = new SaveAsDialog(getSite().getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();
		var path = dialog.getResult();
		if (path == null) {
			return;
		}
		var iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		super.setInput(new FileEditorInput(iFile));
		var file = iFile.getLocation().toFile();
		setFileExtension(file);
		uri = URI.createFileURI(file.getAbsolutePath());
		doSave(null);
		// refresh the resource in the workspace to avoid 'Resource is out of  sync with the file system' messages
		try {
			iFile.refreshLocal(IResource.DEPTH_ZERO, null);
			// we should probably select the file in the package explorer too
		} catch (Exception e) {
			Status status = new Status(IStatus.ERROR, ID, "An exception occurred while saving the file", e);
			ErrorDialog.openError(getSite().getShell(), "Exception", e.getMessage(), status);
		}
		setPartName(iFile.getName());
		firePropertyChange(PROP_INPUT);
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		editorSaving = true;
		// Serialize the model
		writeSchemaToFile();
		
		// refresh the resource in the workspace to avoid 'Resource is out of  sync with the file system' messages
		try {			
			workspaceResource.refreshLocal(IResource.DEPTH_ZERO, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}		
		
		// Update the editor state to indicate that the contents have been saved  and notify all listeners about
		// the change in state
		getCommandStack().markSaveLocation();
		firePropertyChange(PROP_DIRTY);
		
		editorSaving = false;
	}
	
	private void writeSchemaToFile() {
		try {
			Tools.writeToFile(schema, new File(uri.toFileString()));
		} catch (IOException | IllegalArgumentException e) {
			Status status = new Status(IStatus.ERROR, ID, "An exception occurred while saving the file", e);
			ErrorDialog.openError(getSite().getShell(), "Exception", e.getMessage(), status);
		}
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class) {
			var key = ZoomManager.class.toString();
			return type.cast(getGraphicalViewer().getProperty(key));
		} else if (type == IPropertySheetPage.class) {
            return type.cast(new TabbedPropertySheetPage(this));
		} else if (type == IContentOutlinePage.class) {
			outlinePage = new OutlinePage(this);
			return type.cast(outlinePage);
		} else if (type == CommandStack.class) {
			// the command stack is accessible by anybody else but only for executing commands - implementing the
			// IModelChangeListener is the preferred way to catch up with model changes
			Assert.isNotNull(getEditDomain(), "edit domain not yet set");
			return type.cast(getCommandStack());
		} else if (type == GraphicalViewer.class) {
			return type.cast(getGraphicalViewer());
		} else if (type == IModelChangeProvider.class) {
			return type.cast(modelChangeDispatcher);
		} else if (type == ActionRegistry.class) {
			return type.cast(getActionRegistry());
		} else if (type == DefaultEditDomain.class) {
			Assert.isNotNull(getEditDomain(), "edit domain not yet set");
			return type.cast(getEditDomain());
		} else if (type == SelectionSynchronizer.class) {
			return type.cast(getSelectionSynchronizer());
		} else {
			return super.getAdapter(type);
		}
	}
	
	private List<SchemaEditor> getAllEditorsForInput(IEditorInput editorInput) {
		return getSchemaEditorsWithSameInput(editorInput)
				.toList();
	}

	private SchemaEditor getFirstEditorForSameInput(IEditorInput editorInput) {
		return getSchemaEditorsWithSameInput(editorInput)
				.findFirst()
				.orElse(null);
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
		if (palette == null) {
			var firstEditorForSameInput = getFirstEditorForSameInput(getEditorInput());		
			if (firstEditorForSameInput != null) {
				palette = firstEditorForSameInput.palette;
			} else {	
				palette = new PaletteBuilder().build();
			}
		}
		return palette;
	}
	
	public Schema getSchema() {
		return schema;
	}
	
	@Override
	protected SelectionSynchronizer getSelectionSynchronizer() {
		if (selectionSynchronizer == null) {
			selectionSynchronizer = new ModifiedSelectionSynchronizer(outlinePage);
		}
		return selectionSynchronizer;
	}

	@Override
	public String getTitleToolTip() {
		if (isReadOnlyMode()) {
			return String.format("%s (read-only)", super.getTitleToolTip());
		} else {
			return super.getTitleToolTip();
		}
	}

	private void hookActivePaletteViewerToEditDomain() {
		// unless the workbench is closing, we need to make sure that editors for the same diagram (editor input)
		// have a functioning palette; we need to hook an active palette viewer to the edit domain 
		if (!PlatformUI.getWorkbench().isClosing()) {
			var editor = getFirstEditorForSameInput(getEditorInput()); 
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
		if (isReadOnlyMode()) {
			// we should never be dirty when in read-only mode but force it anyway
			return false;
		}
		// if the workbench is NOT closing, refer to the super's method
		if (!PlatformUI.getWorkbench().isClosing()) {			
			return super.isDirty();
		}
		// make sure we get to see exactly 1 line if the editor is dirty, and not more
		var openEditors = getAllEditorsForInput(getEditorInput());
		if (openEditors.isEmpty() || openEditors.get(0) == this) {
			return super.isDirty();
		} else {
			return false;
		}
	}
	
	public boolean isReadOnlyMode() {
		return readOnlyFlag;
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	public void markSaveLocationAndResetDirtyFlag() {
		getCommandStack().markSaveLocation();
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	boolean performSaveAs() {
		var dialog = new SaveAsDialog(getSite().getWorkbenchWindow().getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();
		var path = dialog.getResult();

		if (path == null) {
			return false;
		}

		var workspace = ResourcesPlugin.getWorkspace();
		var file = workspace.getRoot().getFile(path);
		if (!file.exists()) {
			var op = new WorkspaceModifyOperation() {
				public void execute(final IProgressMonitor monitor) {
					// Serialize the model
					var resourceSet = new ResourceSetImpl();
					var tmpURI = URI.createFileURI(file.getLocation().toFile().getAbsolutePath());
					var resource = resourceSet.createResource(tmpURI);
					resource.getContents().add(schema);
					try {
						resource.save(null);
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			};
			try {
				new ProgressMonitorDialog(getSite().getWorkbenchWindow().getShell()).run(false, true, op);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				Thread.currentThread().interrupt();
			}
		}

		try {
			refreshContainer(file.getParent(), IResource.DEPTH_INFINITE);
			superSetInput(new FileEditorInput(file));
			refreshResource(workspaceResource, IResource.DEPTH_ZERO);
			getCommandStack().markSaveLocation();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}
	
	private void refreshContainer(IContainer container, int depth) {
		try {
			if (container != null) {
				container.refreshLocal(depth, null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private void refreshResource(IResource resource, int depth) {
		try {
			if (resource != null) {
				resource.refreshLocal(depth, null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private void setFileExtension(IEditorInput input) {
		if (input instanceof IFileEditorInput fileEditorInput) {
			var file = fileEditorInput.getFile();
			fileExtension = file.getFileExtension();
		} else if (input instanceof IStorageEditorInput) {
			var name = input.getName();
			if (name.indexOf(".schemadsl") > -1) {
				fileExtension =  FILE_EXTENSION_SCHEMADSL;
			} else {
				fileExtension =  FILE_EXTENSION_SCHEMA;
			}
		} else {
			throw new IllegalArgumentException("Unsupported editor input: " + input);
		}		
	}

	private void setFileExtension(File file) {
		var i = file.getName().lastIndexOf('.');
		if (i < 0) {
			fileExtension = FILE_EXTENSION_SCHEMA;
		} else {
			fileExtension = file.getName().substring(i + 1);
		}
	}
	
	@Override
	protected void setInput(IEditorInput input) {
		logger.debug(DebugItem.CALLING_METHOD);
		
		superSetInput(input);
		
		if (schema != null) {
			// Always keep the same Schema object to avoid trouble (note that if the file was overwritten with
			// something else, there is a mismatch between the Schema object and the file contents). We can't
			// show a message because there is a chance that the user will get to see it while importing a schema.
			return;
		}
		
		var firstEditorForSameInput = getFirstEditorForSameInput(input);		
		if (firstEditorForSameInput != null) {
			setEditDomain(firstEditorForSameInput.getEditDomain());
			schema = firstEditorForSameInput.getSchema();
		} else {
			setEditDomain(new DefaultEditDomain(null));
			try {
				schema = Tools.readFromFile(new File(uri.toFileString()));
			} catch (Exception t) {
				var message = "An error occurred while opening file '" + uri.toFileString() + "'";
				logger.error(message, t);
				try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
					t.printStackTrace(pw);
					throw new IllegalArgumentException(message + "\n\n" + sw.toString());
				} catch (IOException e) {
					throw new IllegalArgumentException(message);
				}
			}
		}
		if (!editorSaving && outlinePage != null) {
			outlinePage.setSchema(schema);
		}
	}
	
	private void setReadOnlyFlag() {
		var preferredReadOnlyFlag = Plugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.READ_ONLY_MODE);
		readOnlyFlag = preferredReadOnlyFlag || !(getEditorInput() instanceof IFileEditorInput); 	
	}

	@Override
	protected void setSite(IWorkbenchPartSite site) {
		super.setSite(site);
		getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
	}	
	
	void superSetInput(IEditorInput input) {
		// The workspace never changes for an editor. So, removing and re-adding the resourceListener is not
		// necessary. But it is being done here for the sake of proper implementation. Plus, the resourceListener
		// needs to be added to the workspace the first time around.
		if (getEditorInput() instanceof IFileEditorInput fileEditorInput) {
			var file = fileEditorInput.getFile();
			file.getWorkspace().removeResourceChangeListener(resourceListener);
		}

		super.setInput(input);

		var editorInput = getEditorInput();
		setReadOnlyFlag();
		setFileExtension(editorInput);
			
		if (editorInput instanceof IFileEditorInput fileEditorInput) {
			var file = fileEditorInput.getFile();
			workspaceResource = ResourcesPlugin.getWorkspace().getRoot().findMember(file.getFullPath());
			uri = URI.createFileURI(file.getLocation().toFile().getAbsolutePath());			
			file.getWorkspace().addResourceChangeListener(resourceListener);
			setPartName(file.getName());
		} else if (editorInput instanceof IStorageEditorInput storageEditorInput) {
			// this will be the case when browsing a diagram from within the SVN Repositories view of Subversive
			// or Subclipse; get the contents and write it to a temporary file...
			var tmpFile = Plugin.getDefault().createTmpFile(fileExtension); 
			try {
				var buffer = Tools.writeToBuffer(storageEditorInput.getStorage().getContents());
				Tools.writeToFile(buffer, tmpFile);
			} catch (CoreException | IOException e) {
				throw new IllegalStateException("Error while retrieving diagram", e);
			}
			uri = URI.createFileURI(tmpFile.getAbsolutePath());
			setPartName(storageEditorInput.getName());
		} else {
			throw new IllegalArgumentException("Unsupported editor input: " + input);
		}
	}
	
	void closeEditor(boolean save) {
		var display = PlatformUI.getWorkbench().getDisplay();
		display.asyncExec(() -> getSite().getPage().closeEditor(SchemaEditor.this, save));
	}
	
	@Override
	public void dispose() {
		if (needToRemoveAsCommandStackEventListener) {
			getCommandStack().removeCommandStackEventListener(this);
		}
		var paletteViewerProvider = (ModifiedPaletteViewerProvider) getPaletteViewerProvider();
		paletteViewerProvider.dispose(); 
		hookActivePaletteViewerToEditDomain();		
		verticalRulerProvider.dispose();
		horizontalRulerProvider.dispose();
		modelChangeDispatcher.dispose();
		getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
		partListener = null;
		if (getEditorInput() instanceof IFileEditorInput fileEditorInput) {
			fileEditorInput.getFile().getWorkspace().removeResourceChangeListener(resourceListener);
		}
		super.dispose();
	}

}
