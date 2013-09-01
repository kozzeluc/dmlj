package org.lh.dmlj.schema.editor.property;

import org.eclipse.gef.ui.parts.GraphicalEditor;

public interface IGraphicalEditorProvider<T extends GraphicalEditor> {

	T getEditor();
	
}