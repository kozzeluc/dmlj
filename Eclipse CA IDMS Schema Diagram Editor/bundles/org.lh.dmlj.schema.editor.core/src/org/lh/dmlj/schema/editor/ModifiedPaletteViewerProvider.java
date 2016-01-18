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

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * This class allows us to synchronize the selected tool on the palettes for all editors opened for 
 * the same input (cloned or split editors.  This is important because there can be only 1 active
 * palette for an edit domain (the same edit domain is shared between cloned/split editors).  By
 * synchronizing the active palette tool, the user has the <i>impression</i> that the palette he
 * uses is creating the entities he wants, but in reality this can be another one (i.e. the one
 * belonging to another cloned/split editor).
 */
public class ModifiedPaletteViewerProvider extends PaletteViewerProvider implements PaletteListener {

	private boolean dontFireEvent;
	private SchemaEditor editor;
	private PaletteViewer paletteViewer;
	
	public ModifiedPaletteViewerProvider(SchemaEditor editor) {
		super((EditDomain) editor.getAdapter(DefaultEditDomain.class));
		this.editor = editor;
	}
	
	@Override
	public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
		if (!dontFireEvent) {
			editor.fireActivePaletteToolChanged(editor, tool);
		}
	}

	@Override
	public PaletteViewer createPaletteViewer(Composite parent) {
		paletteViewer = super.createPaletteViewer(parent);
		paletteViewer.addPaletteListener(this);
		return paletteViewer;
	}

	public void dispose() {	
		paletteViewer.removePaletteListener(this);
		editor = null;
		paletteViewer = null;
	}
	
	public void hookPaletteViewer() {
		hookPaletteViewer(paletteViewer);
	}
	
	public void selectTool(ToolEntry tool) {
		if (paletteViewer != null) {
			dontFireEvent = true;
			paletteViewer.setActiveTool(tool);
			dontFireEvent = false;
		}
	}

}
