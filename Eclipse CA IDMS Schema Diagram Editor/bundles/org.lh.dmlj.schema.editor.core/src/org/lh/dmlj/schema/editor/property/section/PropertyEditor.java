/**
 * Copyright (C) 2026 Luc Hermans
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

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * A property editor that is triggered when the user clicks the mouse button somewhere in the second column of a
 * section's property table.
 */
public class PropertyEditor extends MouseAdapter implements MouseMoveListener {
	private static final String E_SHORT_OBJECT = "EShortObject";
	private static final String E_SHORT = "EShort";
	private static final String E_BOOLEAN = "EBoolean";
	private static final String E_STRING = "EString";
	
	private final AbstractAttributesBasedPropertiesSection<?> section;
	private final IStatusLineManager statusLineManager;
	private final TableEditor tableEditor;
	private CommandStack commandStack;
	private Cursor handCursor;
	
	private static Enum<?> getEnumElement(EAttribute attribute, String value) {
		var classifier = attribute.getEType();
		var enumClass = classifier.getInstanceClass();
		for (var field : enumClass.getFields()) {
			if (field.isEnumConstant() && field.getName().equals(value)) {				
				try {					
					return (Enum<?>) field.get(null);
				} catch (IllegalArgumentException | IllegalAccessException e) {					
					throw new IllegalStateException(e);
				}				
			}
		}
		var message = "no element '" + value + "' in enum '" + classifier.getInstanceClass().getSimpleName() + "'";
		throw new IllegalStateException(message);
	}
	
	public PropertyEditor(TabbedPropertySheetPage page, AbstractAttributesBasedPropertiesSection<?> section, Table table) {
		this.section = section;
		statusLineManager = page.getSite().getActionBars().getStatusLineManager();
		tableEditor = new TableEditor(table);
		tableEditor.horizontalAlignment = SWT.LEFT;
		tableEditor.minimumWidth = 100;
		table.addMouseListener(this);
		table.addMouseMoveListener(this);
	}
	
	public void dispose() {
		if (handCursor != null) {
			handCursor.dispose();
		}
		if (tableEditor.getEditor() != null && !tableEditor.getEditor().isDisposed()) {
			tableEditor.getEditor().dispose();
		}
		tableEditor.dispose();
	}
	
	private void hyperlinkActivated(EAttribute attribute) {
		// call the hyperlink handler code to get the Command
		var handler = section.getHyperlinkHandler(attribute);
		var command = handler.hyperlinkActivated(attribute);
		
		// execute the Command on the command stack if not null
		commandStack.execute(command);
	}

	@Override
	public void mouseMove(MouseEvent e) {
		if (section.isReadOnlyMode()) {
			return;
		}
		
		// get the Table instance
		var table = (Table) e.getSource();
		
		// if we're editing something, get out
		var editorControl = tableEditor.getEditor();
		if (editorControl != null && !(editorControl instanceof StyledText) && !editorControl.isDisposed()) {
			return;
		}
		
		// dispose of the current table editor control, if any (creating a hyperlink involves creating a new
		// table editor control)
		if (editorControl != null && !editorControl.isDisposed()) { 
			// editorControl is a StyledText
			editorControl.dispose();			
		}
		
		// Identify the table item, quit if the mouse pointer is not moving over any table item
		var pt = new Point(e.x, e.y);    			
		var item = table.getItem(pt);
		if (item == null) {			
			return;
		}
		
		// exit this method if the mouse pointer is above something else than the second column
		if (e.x < table.getColumns()[0].getWidth()) {											
			return;
		}
				
		// get the attribute over which the mouse pointer is moving
		var i = table.indexOf(item);
		final var attribute = section.getAttributes().get(i);
		
		// exit if the attribute has no hyperlink involved
		if (section.getHyperlinkHandler(attribute) == null) {
			return;
		}
		
		// calculate the width of the text in the cell
		var dimension = FigureUtilities.getTextExtents(item.getText(1), table.getFont());
		
		// exit if the mouse pointer is not on top of the text (5 denotes the margin to the left of the text and
		// is an estimate)
		if (e.x < (table.getColumns()[0].getWidth() + 5) || e.x > (table.getColumns()[0].getWidth() + 5 + dimension.width)) {
			return;
		}
		
		// when we get here, we really need a hyperlink...
		
		// create a new table editor control and underline the current table cell's content; make sure the user
		// gets the right mouse pointer		
		final var styledText = new StyledText(table, SWT.READ_ONLY);
		styledText.setIndent(5);
		styledText.setText(item.getText(1));
		var styleRange = new StyleRange(0, item.getText(1).length(), item.getForeground(1), table.getBackground());
		styleRange.underline = true;
		styledText.setStyleRange(styleRange);
		if (handCursor == null) {
			handCursor = new Cursor(table.getDisplay(), SWT.CURSOR_HAND);
		}
		styledText.setCursor(handCursor);
		
		// set the editor control's top margin so that the text doesn't shift
		// up or down in it's cell when being underlined
		styledText.pack();
		var topMargin = table.getItemHeight() - styledText.getBounds().height - 2;
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

	@Override
	public void mouseUp(MouseEvent e) {
		if (section.isReadOnlyMode()) {
			return;
		}
		
		// exit this method if not exactly 1 row is selected
		var table = (Table) e.getSource();
		if (table.getSelectionCount() != 1) {					
			return;
		}
		
		// cleanup any previous editor control, if any
		var editorControl = tableEditor.getEditor();
		if (editorControl != null) { 
			editorControl.dispose();
		}
		
		// Identify the selected row
		var item = table.getSelection()[0];
		if (item == null) {
			return;
		}
							
		// exit this method if the user clicked somewhere else than in the second column or when the attribute is
		// not editable
		var attribute = section.getAttributes().get(table.getSelectionIndex());
		if (e.x < table.getColumns()[0].getWidth() || section.getEditableObject(attribute) == null) {
			return;
		}
		
		// edit the attribute value depending on its type
		editAttributeValueDependingOnItsType(table, attribute, item);
	}
	
	private void editAttributeValueDependingOnItsType(Table table, EAttribute attribute, TableItem item) {
		var classifier = attribute.getEType();	
		if (attribute.getEType().getName().equals(E_STRING) || attribute.getEType().getName().equals(E_SHORT) ||
			attribute.getEType().getName().equals(E_SHORT_OBJECT)) {
			
			editAttributeValueUsingText(table, attribute, item);
		} else if (attribute.getEType().getName().equals(E_BOOLEAN) || classifier.getInstanceClass().isEnum()) {
			editAttributeValueUsingCombo(table, attribute, item);
		} else {
			// we (currently) don't support the 's type
			throw new IllegalStateException("unsupported  type: " + attribute.getEType().getName());
		}		
	}
	
	private void editAttributeValueUsingText(Table table, EAttribute attribute, TableItem item) {
		var oldValue = item.getText(1);
		
		// the cell editor can use all available horizontal space
		tableEditor.grabHorizontal = true;
		
		// create the Text editor control and set its initial value
		var text = new Text(table, SWT.NONE);
		text.setText(oldValue);
		text.selectAll();
		
		// hookup a key listener to set the attribute value, if changed, when enter is pressed, and dispose of
		// the editor control when escape is pressed:
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 13 || e.keyCode == 16777296) { // enter-keys	
					var newValue = text.getText();
					text.dispose();
											
					handleCellEdit(attribute, oldValue, newValue);
				} else if (e.keyCode == SWT.ESC) {
					text.dispose();						
				}
			}					
		});			
		
		// hookup a focus listener to set the attribute value, if changed, when the editor control loses focus
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				var newValue = text.getText();
				text.dispose();
				
				handleCellEdit(attribute, oldValue, newValue);
				
			}					
		});
		
		// start editing the cell in which the user clicked the mouse
		text.setFocus();
		tableEditor.setEditor(text, item, 1);
	}
	
	private void editAttributeValueUsingCombo(Table table, EAttribute attribute, TableItem item) {
		var oldValue = item.getText(1);
		
		// the attribute is of type boolean or an enumeration; we don't need an endlessly wide combo box in
		// the case of a boolean
		if (attribute.getEType().getName().equals(E_BOOLEAN)) {
			tableEditor.grabHorizontal = false;
		} else {
			tableEditor.grabHorizontal = true;
		}
		
		// create a Combo and hookup a selection listener to set the property value if changed and disposes of
		// the editor control when escape is pressed:
		var combo = createCombo(table, attribute, item);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String sNewValue;
				if (attribute.getEType().getName().equals(E_BOOLEAN)) {
					var newValue = combo.getSelectionIndex() == 1;
					sNewValue = String.valueOf(newValue);
				} else {
					sNewValue = combo.getItem(combo.getSelectionIndex());
				}
				combo.dispose();
				handleCellEdit(attribute, oldValue, sNewValue);
			}
		});
		
		// start editing the cell in which the user clicked the mouse
		combo.setFocus();
		tableEditor.setEditor(combo, item, 1);
	}
	
	private Combo createCombo(Table table, EAttribute attribute, TableItem item) {
		var combo = new Combo(table, SWT.DROP_DOWN | SWT.READ_ONLY);
		if (attribute.getEType().getName().equals(E_BOOLEAN)) {
			addBooleanValuesToComboAndSelectOldValue(combo, item);
		} else {
			addEnumValuesToComboAndSelectOldValue(combo, attribute, item);
		}
		return combo;
	}
	
	private void addBooleanValuesToComboAndSelectOldValue(Combo combo, TableItem item) {
		var oldValue = item.getText(1);
		
		combo.add("false");
		combo.add("true");
		// select the current attribute value
		if (Boolean.parseBoolean(oldValue)) {
			combo.select(1);
		} else {
			combo.select(0);
		}
	}
	
	private void addEnumValuesToComboAndSelectOldValue(Combo combo, EAttribute attribute, TableItem item) {
		var classifier = attribute.getEType();
		var oldValue = item.getText(1);
		
		for (var literal : ((EEnum) classifier).getELiterals()) {					
			var entryValue = literal.getName().replace("_", " ");
			// filter out the enum element if necessary:
			var filter = section.getEnumFilter(attribute);
			if (filter != null) {
				// filtered: we also need the enum element itself, not its String value:
				var element = getEnumElement(attribute, literal.getName());
				// add the element only when it passes the filter:
				if (filter.include(attribute, element)) {
					combo.add(entryValue);
					if (entryValue.equals(oldValue)) {
						combo.select(combo.getItemCount() - 1);
					}
				}
			} else {
				// unfiltered
				combo.add(entryValue);
				if (entryValue.equals(oldValue)) {
					combo.select(combo.getItemCount() - 1);
				}
			}
		}		
	}
	
	private void handleCellEdit(EAttribute attribute, String oldValue, String newValue) {
		if (newValue.equals(oldValue)) {
			return; 
		}
		
		var newValueOfCorrectType = getNewValueOfCorrectType(attribute, newValue);
		if (newValueOfCorrectType == null) {
			return;
		}
				
		var handler = section.getEditHandler(attribute, newValueOfCorrectType);
		
		// if the edit handler has a message and the edit action is not valid, set the error message in the status line
		var message = handler.getMessage();
		if (message != null && !handler.isValid()) {
			statusLineManager.setErrorMessage("Validation failed for '" + section.getLabel(attribute) + "': " + message);
		}
		
		// set the new attribute value with the command provided by the edit handler, provided the new value is
		// valid and has changed
		var command = handler.getEditCommand();
		if (handler.isValid() && command != null) {			
			commandStack.execute(command);
			if (message != null) {
				// note: we show the warning message via a dialog because for some reason it refuses to show up
				// in the status line
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "Warning", message);
			}
		}
	}
	
	private Object getNewValueOfCorrectType(EAttribute attribute, String newValue) {
		var classifier = attribute.getEType();
		var trimmedValue = newValue.trim();
		if (!trimmedValue.isEmpty()) {
			try {
				if (attribute.getEType().getName().equals(E_STRING)) {
					return trimmedValue;
				} else if (attribute.getEType().getName().equals(E_BOOLEAN)) {
					return Boolean.valueOf(trimmedValue);
				} else if (attribute.getEType().getName().equals(E_SHORT) || attribute.getEType().getName().equals(E_SHORT_OBJECT)) {
					return Short.valueOf(trimmedValue);
				} else if (classifier.getInstanceClass().isEnum()) {
					// we're dealing with an enumeration, so go get the element that matches newValue, keeping in
					// mind that newValue will contain spaces, not underscores
					return getEnumElement(attribute, trimmedValue.replace(" ", "_"));
				}
			} catch (Exception e) {
				statusLineManager.setErrorMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
			}
		} else if (!attribute.getEType().getName().equals(E_STRING) && !attribute.getEType().getName().equals(E_SHORT_OBJECT)) {
			// mandatory EString properties will have to be checked in the section itself
			statusLineManager.setErrorMessage("'" + section.getLabel(attribute) + "' is a mandatory property");
		}
		return null;
	}

	public void setCommandStack(CommandStack commandStack) {
		this.commandStack = commandStack;		
	}	
	
}
