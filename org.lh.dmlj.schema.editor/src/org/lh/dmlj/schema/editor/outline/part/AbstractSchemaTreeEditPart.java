/**
 * Copyright (C) 2013  Luc Hermans
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
package org.lh.dmlj.schema.editor.outline.part;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;
import org.lh.dmlj.schema.editor.Plugin;

public abstract class AbstractSchemaTreeEditPart<T extends EObject> 
	extends AbstractTreeEditPart implements CommandStackEventListener {
	
	private CommandStack commandStack;
	
	protected AbstractSchemaTreeEditPart(T model, CommandStack commandStack) {
		super(model);	
		this.commandStack = commandStack;
	}
	
	public final void activate() {
		super.activate();
		commandStack.addCommandStackEventListener(this);
	}

	public final void deactivate() {
		commandStack.removeCommandStackEventListener(this);
		super.deactivate();
	}
	
	@Override
	protected final Image getImage() {
		String imagePath = getImagePath();
		if (imagePath == null) {
			return null;
		} else {
			return Plugin.getDefault().getImage(imagePath);
		}
	}
	
	protected abstract String getImagePath();
	
	@Override
	@SuppressWarnings("unchecked")
	public final T getModel() {
		return (T) super.getModel();
	}
	
	protected abstract String getNodeText();

	protected final EObject getParentModelObject() {
		if (getParent() instanceof AbstractSchemaTreeEditPart<?>) {
			AbstractSchemaTreeEditPart<?> parentEditPart = 
				(AbstractSchemaTreeEditPart<?>) getParent();
			return parentEditPart.getModel();
		} else {
			return null;
		}
	}
	
	@Override
	protected final String getText() {
		return getNodeText();
	}
	
	@Override
	protected final void refreshVisuals() {
		// just make sure subclasses are aware that they do NOT need to override this method
		super.refreshVisuals();
	}
	
	@Override
	public final void stackChanged(CommandStackEvent event) {
		if (event.isPostChangeEvent() &&
			(event.getDetail() == CommandStack.POST_EXECUTE ||
			 event.getDetail() == CommandStack.POST_REDO ||
			 event.getDetail() == CommandStack.POST_UNDO)) {
							
			refresh();
		}
	}	
	
}
