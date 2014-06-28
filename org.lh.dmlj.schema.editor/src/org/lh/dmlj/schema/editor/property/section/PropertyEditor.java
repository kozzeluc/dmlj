/**
 * Copyright (C) 2014 Luc Hermans
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
package org.lh.dmlj.schema.editor.property.section;

import java.lang.reflect.Field;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.editor.property.filter.IEnumFilter;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;

/**
 * A property editor that is triggered when the user clicks the mouse button
 * somewhere in the second column of a section's property table.
 */
public class PropertyEditor extends MouseAdapter implements MouseMoveListener {

	private CommandStack	 		 	 commandStack;
	private AbstractAttributesBasedPropertiesSection<?> section;
	private IStatusLineManager 		 	 statusLineManager;
	private TableEditor 				 tableEditor;
	
	private static Enum<?> getEnumElement(EAttribute attribute, String value) {
		EClassifier classifier = attribute.getEType();
		Class<?> enumClass = classifier.getInstanceClass();
		for (Field field : enumClass.getFields()) {
			if (field.isEnumConstant() && field.getName().equals(value)) {				
				try {					
					return (Enum<?>) field.get(null);
				} catch (IllegalArgumentException | IllegalAccessException e) {					
					throw new RuntimeException(e);
				}				
			}
		}
		String message = "no element '" + value + "' in enum '" +
				   		 classifier.getInstanceClass().getSimpleName() + "'";
		throw new RuntimeException(message);
	}
	
	public PropertyEditor(TabbedPropertySheetPage page, 
			  			  AbstractAttributesBasedPropertiesSection<?> section, Table table) {
		super();
		this.section = section;
		statusLineManager = 
			page.getSite().getActionBars().getStatusLineManager();
		// create the editor for the table cells
		tableEditor = new TableEditor(table);
		tableEditor.horizontalAlignment = SWT.LEFT;
		tableEditor.minimumWidth = 50;
		// add both mouse listeners
		table.addMouseListener(this);
		table.addMouseMoveListener(this);
	}
	
	public void dispose() {
		if (tableEditor.getEditor() != null && 
			!tableEditor.getEditor().isDisposed()) {
			
			tableEditor.getEditor().dispose();
		}
		tableEditor.dispose();
	}
	
	private void handleCellEdit(EAttribute attribute, String oldValue, 
								String newValue) {
		
		// nothing to do if attribute's value did not change
		if (newValue.equals(oldValue)) {
			return; 
		}
		
		// validate the attribute's type on the new value, set the error message
		// if needed and reject the cell edit - trim the value before doing
		// anything else
		EClassifier classifier = attribute.getEType();
		String trimmedValue = newValue.trim();
		Object oNewValue = null;
		if (!trimmedValue.equals("")) {
			try {
				if (attribute.getEType().getName().equals("EString")) {
					oNewValue = trimmedValue;
				} else if (attribute.getEType().getName().equals("EBoolean")) {
					oNewValue = Boolean.valueOf(trimmedValue);
				} else if (attribute.getEType().getName().equals("EShort")) {
					oNewValue = Short.valueOf(trimmedValue);
				} else if (attribute.getEType()						
									.getName().equals("EShortObject")) {
					
					oNewValue = Short.valueOf(trimmedValue);
				} else if (classifier.getInstanceClass().isEnum()) {
					// we're dealing with an enumeration, so go get the element
					// that matches newValue, keeping in mind that newValue will
					// contain spaces, not underscores					
					oNewValue = 
						getEnumElement(attribute, newValue.replaceAll(" ", "_"));					
				}
			} catch (Throwable t) {
				String message = 
					t.getClass().getSimpleName() + ": " + t.getMessage();
				statusLineManager.setErrorMessage(message);
				return;
			}
		} else if (!attribute.getEType().getName().equals("EString") &&
				   !attribute.getEType().getName().equals("EShortObject")) {
			
			// mandatory EString properties will have to be checked in the
			// section itself
			String message = 
				"'" + section.getLabel(attribute) + "' is a mandatory property";
			statusLineManager.setErrorMessage(message);
			return;
		}
		
		// we need an edit handler
		IEditHandler handler = section.getEditHandler(attribute, oNewValue);
		
		// if the edit handler has a message and the edit action is not valid, 
		// set the error message in the status line
		String message = handler.getMessage();
		if (message != null && !handler.isValid()) {
			statusLineManager.setErrorMessage("Validation failed for '" + 
											  section.getLabel(attribute) + 
											  "' :  " + message);			
		}
		
		// set the new attribute value with the command provided by the edit 
		// handler, provided the new value is valid and has changed
		Command command = handler.getEditCommand();
		if (handler.isValid() && command != null) {			
			commandStack.execute(command);
			// if there is a message, show it now (we have to do this after 
			// executing the command or it will not shop up):
			if (message != null) {
				statusLineManager.setMessage("Warning: " + message);
			}
		}
	}	
	
	private void hyperlinkActivated(EAttribute attribute) {		

		// call the hyperlink handler code to get the Command
		IHyperlinkHandler<EAttribute, Command> handler = section.getHyperlinkHandler(attribute);
		Command command = handler.hyperlinkActivated(attribute);
		
		// execute the Command on the command stack if not null
		commandStack.execute(command);
	}

	@Override
	public void mouseMove(MouseEvent e) {
		
		// get the Table instance
		Table table = (Table) e.getSource();
		
		// if we're editing something, get out
		Control editorControl = tableEditor.getEditor();
		if (editorControl != null && !(editorControl instanceof StyledText) &&
			!editorControl.isDisposed()) {
			
			return;
		}
		
		// dispose of the current table editor control, if any (creating a 
		// hyperlink involves creating a new table editor control)
		if (editorControl != null && !editorControl.isDisposed()) { 
			// editorControl is a StyledText
			editorControl.dispose();			
		}
		
		// Identify the table item, quit if the mouse pointer is not moving over 
		// any table item	
		Point pt = new Point(e.x, e.y);    			
		TableItem item = table.getItem(pt);
		if (item == null) {			
			return;
		}
		
		// exit this method if the mouse pointer is above something else than  
		// the second column
		if (e.x < table.getColumns()[0].getWidth()) {											
			return;
		}
				
		// get the attribute over which the mouse pointer is moving
		int i = table.indexOf(item);
		final EAttribute attribute = section.getAttributes().get(i);
		
		// exit if the attribute has no hyperlink involved
		if (section.getHyperlinkHandler(attribute) == null) {
			return;
		}
		
		// calculate the width of the text in the cell
		Dimension dimension = 
			FigureUtilities.getTextExtents(item.getText(1), table.getFont());
		
		// exit if the mouse pointer is not on top of the text (5 denotes the
		// margin to the left of the text and is an estimate)
		if (e.x < (table.getColumns()[0].getWidth() + 5) || 
			e.x > (table.getColumns()[0].getWidth() + 5 + dimension.width)) {
			
			return;
		}
		
		// when we get here, we really need a hyperlink...
		
		// create a new table editor control and underline the current table
		// cell's content; make sure the user gets the right mouse pointer		
		final StyledText styledText = new StyledText(table, SWT.READ_ONLY);
		styledText.setIndent(5);
		styledText.setText(item.getText(1));
		StyleRange styleRange = 
			new StyleRange(0, item.getText(1).length(), 
						   item.getForeground(1), table.getBackground());
		styleRange.underline = true;
		styledText.setStyleRange(styleRange);
		styledText.setCursor(new Cursor(table.getDisplay(), SWT.CURSOR_HAND));
		
		// set the editor control's top margin so that the text doesn't shift
		// up or down in it's cell when being underlined
		styledText.pack();
		int topMargin = table.getItemHeight() - 
						styledText.getBounds().height - 
						2; // this seems to be fine for Windows XP and 7
		styledText.setTopMargin(topMargin);
		
		// make sure the hyperlink control is only as wide as needed
		tableEditor.minimumWidth = dimension.width + 5;
		tableEditor.grabHorizontal = false;		
		tableEditor.setEditor(styledText, item, 1);
		
		// attach a mouse listener for when the user clicks on the hyperlink
		styledText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				styledText.dispose();
				hyperlinkActivated(attribute);
			}
		});
				
	}

	public void mouseUp(MouseEvent e) {					
		
		// get the Table instance
		Table table = (Table) e.getSource();
		
		// exit this method if no row is selected
		if (table.getSelectionCount() != 1) {					
			return;
		}
		
		// cleanup any previous editor control, if any
		Control editorControl = tableEditor.getEditor();
		if (editorControl != null) { 
			editorControl.dispose();
		}
		
		// Identify the selected row
		TableItem item = (TableItem)table.getSelection()[0];
		if (item == null) {
			return;
		}
		
		// get the selected attribute and its current (old) value
		int i = table.getSelectionIndex();
		final EAttribute attribute = section.getAttributes().get(i);
		final String oldValue = item.getText(1);		
		
		// exit this method if the user clicked somewhere else than in 
		// the second column
		if (e.x < table.getColumns()[0].getWidth()) {					
			return;
		}
		
		// if the attribute is not editable we're also done
		if (section.getEditableObject(attribute) == null) {
			return;
		}
		
		// edit the attribute value depending on its type
		final EClassifier classifier = attribute.getEType();	
		if (attribute.getEType().getName().equals("EString") ||
			attribute.getEType().getName().equals("EShort") ||
			attribute.getEType().getName().equals("EShortObject")) {
			
			// the cell editor can use all available horizontal space
			tableEditor.grabHorizontal = true;
			
			// create the Text editor control and set its initial value
			final Text text = new Text(table, SWT.NONE);
			text.setText(item.getText(1));
			text.selectAll();
			
			// hookup a key listener to set the attribute value, if changed, 
			// when enter is pressed, and dispose of the editor control when 
			// escape is pressed:
			text.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					if (e.keyCode == 13 || 
						e.keyCode == 16777296) { // enter-keys								
						
						String newValue = text.getText();
						text.dispose();
												
						handleCellEdit(attribute, oldValue, newValue);
						
					} else if (e.keyCode == SWT.ESC) {
						text.dispose();						
					}
				}					
			});			
			
			// hookup a focus listener to set the attribute value, if 
			// changed, when the editor control loses focus
			text.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					
					String newValue = text.getText();
					text.dispose();
					
					handleCellEdit(attribute, oldValue, newValue);
					
				}					
			});
			
			// start editing the cell in which the user clicked the mouse
			text.setFocus();
			tableEditor.setEditor(text, item, 1);				
			
		} else if (attribute.getEType().getName().equals("EBoolean") ||
				   classifier.getInstanceClass().isEnum()) { 			
			
			// the attribute is of type boolean or an enumeration; we don't need 
			// an endlessly wide combo box in the case of a boolean
			if (attribute.getEType().getName().equals("EBoolean")) {
				tableEditor.grabHorizontal = false;
			} else {
				tableEditor.grabHorizontal = true;
			}
			
			// we need a combobox 					
			final Combo combo = new Combo(table, SWT.DROP_DOWN | SWT.READ_ONLY);
			if (attribute.getEType().getName().equals("EBoolean")) {
				// for a boolean attribute, add values false and true (in that 
				// order):
				combo.add("false");
				combo.add("true");
				// select the current attribute value
				if (Boolean.valueOf(oldValue)) {
					combo.select(1);
				} else {
					combo.select(0);
				}	
			} else {
				for (EEnumLiteral literal : ((EEnum) classifier).getELiterals()) {					
					String entryValue = 
							literal.getName().replaceAll("_", " ");
					// filter out the enum element if necessary:
					@SuppressWarnings("unchecked")
					IEnumFilter<Enum<?>> filter = 
						(IEnumFilter<Enum<?>>) section.getEnumFilter(attribute);
					if (filter != null) {
						// filtered: we also need the enum element itself, not 
						// its String value:
						Enum<?> element = 
							getEnumElement(attribute, literal.getName());
						// add the element only when it passes the filter:
						if (filter.include(attribute, element)) {
							combo.add(entryValue);
							if (entryValue.equals(oldValue.toString())) {
								combo.select(combo.getItemCount() - 1);
							}
						}
					} else {
						// unfiltered
						combo.add(entryValue);
						if (entryValue.equals(oldValue.toString())) {
							combo.select(combo.getItemCount() - 1);
						}
					}
				}
			}			
			
			// hookup a key listener to set the property value, if 
			// changed, when enter is pressed, and disposes of the
			// editor control when escape is pressed:
			combo.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.keyCode == 13 || 
						e.keyCode == 16777296) { // enter-keys								
						
						String sNewValue;
						if (attribute.getEType().getName().equals("EBoolean")) {
							boolean newValue = combo.getSelectionIndex() == 1;
							sNewValue = String.valueOf(newValue);
						} else {
							sNewValue = 
								combo.getItem(combo.getSelectionIndex());
						}

						combo.dispose();
						
						handleCellEdit(attribute, oldValue, sNewValue);
					} else if (e.keyCode == SWT.ESC) {
						combo.dispose();
					}
				}					
			});
			
			// hookup a focus listener to set the property value, if 
			// changed, when the editor control loses focus
			combo.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					
					String sNewValue;
					if (attribute.getEType().getName().equals("EBoolean")) {
						boolean newValue = combo.getSelectionIndex() == 1;
						sNewValue = String.valueOf(newValue);
					} else {
						sNewValue = combo.getItem(combo.getSelectionIndex());
					}
					
					combo.dispose();					
					
					handleCellEdit(attribute, oldValue, sNewValue);
				}					
			});						
			
			// start editing the cell in which the user clicked the
			// mouse
			combo.setFocus();
			tableEditor.setEditor(combo, item, 1);							
			
		} else {
					
			// we (currently) don't support the 's type
			throw new Error("unsupported  type: " + 
						    attribute.getEType().getName());
			
		}
	}

	public void setCommandStack(CommandStack commandStack) {
		this.commandStack = commandStack;		
	}	
	
}
