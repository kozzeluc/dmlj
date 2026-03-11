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
package org.lh.dmlj.schema.editor.ui.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.log.Logger;

public class CopyDiagramToClipboardHandler implements IHandler {
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// nothing to do here
	}

	@Override
	public void dispose() {
		// nothing to do here
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		var schemaEditor = (SchemaEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		var viewer = (GraphicalViewer) schemaEditor.getAdapter(GraphicalViewer.class);
		var layerManager = (LayerManager)viewer.getEditPartRegistry().get(LayerManager.ID);
		var figure = layerManager.getLayer(LayerConstants.PRINTABLE_LAYERS);
		
		var image = new Image(Display.getDefault(), figure.getSize().width, figure.getSize().height);
		var gc = new GC(image);
		var graphics = new SWTGraphics(gc);
		figure.paint(graphics);
		graphics.dispose();
		gc.dispose();
		
		var clipboard = new Clipboard(Display.getDefault());
		try {
			var imageData = image.getImageData();
			var transfer = ImageTransfer.getInstance();
			clipboard.setContents(new ImageData[] { imageData }, new Transfer[] { transfer });
			var message = "Your diagram was successfully copied to the clipboard.";
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Information", message);
		} catch (Exception e) {
			var message = "An error occurred while copying your diagram to the clipboard.";
			logger.error(message, e);
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", message + "  See log.");
		} finally {
			clipboard.dispose();
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// nothing to do here
	}

}
