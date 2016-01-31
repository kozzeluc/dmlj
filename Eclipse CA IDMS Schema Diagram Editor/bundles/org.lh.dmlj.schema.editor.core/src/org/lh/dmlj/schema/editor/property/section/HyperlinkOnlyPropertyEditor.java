/**
 * Copyright (C) 2016  Luc Hermans
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
 */package org.lh.dmlj.schema.editor.property.section;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandlerProvider;

public class HyperlinkOnlyPropertyEditor<T> implements MouseMoveListener {

	private int[] hyperlinkEnabledColumns;
	private IHyperlinkHandlerProvider<T, ?> hyperlinkHandlerProvider;
	private Table table;
	private TableEditor tableEditor;
	
	public HyperlinkOnlyPropertyEditor(Table table, 
									   IHyperlinkHandlerProvider<T, ?> hyperlinkHandlerProvider, 
									   int... hyperlinkEnabledColumns) {
		super();
		this.table = table;
		this.hyperlinkHandlerProvider = hyperlinkHandlerProvider;
		this.hyperlinkEnabledColumns = hyperlinkEnabledColumns;
		tableEditor = new TableEditor(table);
		tableEditor.horizontalAlignment = SWT.LEFT;
		table.addMouseMoveListener(this);
	}

	public void dispose() {
		if (!table.isDisposed()) {
			table.removeMouseMoveListener(this);
		}
		if (tableEditor.getEditor() != null && !tableEditor.getEditor().isDisposed()) {
			tableEditor.getEditor().dispose();
		}
		tableEditor.dispose();		
	}

	private int getColumn(int x) {
		int cumulativeColumnWidths = 0;
		for (int i = 0; i < table.getColumns().length; i++) {
			cumulativeColumnWidths += table.getColumns()[i].getWidth();
			if (x < cumulativeColumnWidths) {
				return i;
			}
		}
		throw new RuntimeException("cannot calculate column: " + x);
	}

	private int getCumulativeColumnWidthsNotIncluding(int column) {
		int cumulativeColumnWidths = 0;
		for (int i = 0; i < column; i++) {
			if (i >= table.getColumns().length) {
				throw new RuntimeException("cannot calculate cumulative column widths: " + column);
			}
			cumulativeColumnWidths += table.getColumns()[i].getWidth();
		}
		return cumulativeColumnWidths;
	}

	private void hyperlinkActivated(int row, int column) {		
		IHyperlinkHandler<T, ?> hyperlinkHandler = 
			hyperlinkHandlerProvider.getHyperlinkHandler(Integer.valueOf(column));
		T context = hyperlinkHandlerProvider.getContext(row);
		if (hyperlinkHandler != null) {
			hyperlinkHandler.hyperlinkActivated(context);
		}
	}

	private boolean isHyperlinkEnabledColumn(int column) {
		for (int hyperlinkEnabledColumn : hyperlinkEnabledColumns) {
			if (hyperlinkEnabledColumn == column) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void mouseMove(MouseEvent e) {
		
		if (hyperlinkHandlerProvider.isReadOnlyMode()) {
			return;
		}
	
		// get the Table instance
		Table table = (Table) e.getSource();
		
		// dispose of the current table editor control, if any (creating a hyperlink involves 
		// creating a new table editor control)
		Control editorControl = tableEditor.getEditor();
		if (editorControl != null && !editorControl.isDisposed()) { 
			// editorControl is a StyledText
			editorControl.dispose();			
		}
		
		// Identify the table item, quit if the mouse pointer is not moving over any table item	
		Point pt = new Point(e.x, e.y);    			
		TableItem item = table.getItem(pt);		
		if (item == null) {			
			return;
		}
		final int row = table.indexOf(item);
		final int column = getColumn(e.x);		
		
		// exit this method if the mouse pointer is not in a hyperlink enabled column
		if (!isHyperlinkEnabledColumn(column)) {											
			return;
		}
		
		// calculate the width of the text in the cell
		Dimension dimension = FigureUtilities.getTextExtents(item.getText(column), table.getFont());
		
		// exit if the mouse pointer is not on top of the text (5 denotes the margin to the left of 
		// the text and is an estimate)
		int cumulativeColumnWidths = getCumulativeColumnWidthsNotIncluding(column);
		if (e.x < (cumulativeColumnWidths + 5) || 
			e.x > (cumulativeColumnWidths + 5 + dimension.width)) {
			
			return;
		}
		
		// when we get here, we really need a hyperlink...
		
		// create a new table editor control and underline the current table
		// cell's content; make sure the user gets the right mouse pointer		
		final StyledText styledText = new StyledText(table, SWT.READ_ONLY);
		styledText.setIndent(column > 0 ? 5 : 2);
		styledText.setText(item.getText(column));
		StyleRange styleRange = 
			new StyleRange(0, item.getText(column).length(), 
						   item.getForeground(column), table.getBackground());
		styleRange.underline = true;
		styledText.setStyleRange(styleRange);
		styledText.setCursor(new Cursor(table.getDisplay(), SWT.CURSOR_HAND));
		
		// set the editor control's top margin so that the text doesn't shift up or down in it's 
		// cell when being underlined
		styledText.pack();
		int topMargin = table.getItemHeight() - 
						styledText.getBounds().height - 
						2; // this seems to be fine for Windows XP and 7
		styledText.setTopMargin(topMargin);
		
		// make sure the hyperlink control is only as wide as needed
		tableEditor.minimumWidth = dimension.width + 5;
		tableEditor.grabHorizontal = false;		
		tableEditor.setEditor(styledText, item, column);
		
		// attach a mouse listener for when the user clicks on the hyperlink
		styledText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				styledText.dispose();
				hyperlinkActivated(row, column);
			}
		});		
		
	}
	
	public void refresh() {
		for (int i = 0; i < table.getItemCount(); i++) {
			TableItem item = table.getItem(i);
			for (int j : hyperlinkEnabledColumns) {
				if (hyperlinkHandlerProvider.isReadOnlyMode()) {
					item.setForeground(j, ColorConstants.black);
				} else {
					item.setForeground(j, ColorConstants.blue);
				}
			}
		}
	}
	
}
