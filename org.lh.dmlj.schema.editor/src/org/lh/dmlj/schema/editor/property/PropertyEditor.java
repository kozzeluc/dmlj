package org.lh.dmlj.schema.editor.property;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * A property editor that is triggered when the user clicks the mouse button
 * somewhere in the second column of a section's property table.
 */
public class PropertyEditor extends MouseAdapter {

	private CommandStack	 		 	 commandStack;
	private AbstractPropertiesSection<?> section;
	private IStatusLineManager 		 	 statusLineManager;
	private TableEditor 				 tableEditor;
	
	public PropertyEditor(TabbedPropertySheetPage page, 
			  			  AbstractPropertiesSection<?> section, Table table) {
		super();
		this.section = section;
		statusLineManager = 
			page.getSite().getActionBars().getStatusLineManager();
		// create the editor for the table cells
		tableEditor = new TableEditor(table);
		tableEditor.horizontalAlignment = SWT.LEFT;
		tableEditor.minimumWidth = 50;
		// add the mouse listener
		table.addMouseListener(this);
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
				}
			} catch (Throwable t) {
				String message = 
					t.getClass().getSimpleName() + ": " + t.getMessage();
				statusLineManager.setErrorMessage(message);
				return;
			}
		} else if (!attribute.getEType().getName().equals("EString")) {
			// mandatory EString properties will have to be checked in the
			// section itself
			String message = 
				"'" + section.getLabel(attribute) + "' is a mandatory property";
			statusLineManager.setErrorMessage(message);
			return;
		}
		
		// we need an edit handler
		IEditHandler handler = section.getEditHandler(attribute, oNewValue);
		
		// if the edit handler has a message, set it in the status line
		String message = handler.getMessage();
		if (message != null) {
			if (handler.isValid()) {
				statusLineManager.setMessage(message);
			} else {
				statusLineManager.setErrorMessage("Validation failed for '" + 
												  section.getLabel(attribute) + 
												  "' :  " + message);
			}
		}
		
		// set the new attribute value with the command provided by the edit 
		// handler, provided the new value is valid
		if (handler.isValid()) {
			Command command = handler.getEditCommand();
			Assert.isNotNull(command, "no edit command provided to set " +
							 "attribute " + attribute.getName());			
			commandStack.execute(command);
		}
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
		
		// get the selected  and its current (old) value
		int i = table.getSelectionIndex();
		final EAttribute attribute = section.getAttributes().get(i);
		final String oldValue = item.getText(1);		
		
		// exit this method if the user clicked somewhere else than in 
		// the second column
		if (e.x < table.getColumns()[0].getWidth()) {					
			return;
		}
		
		// if the  is not editable we're also done
		if (section.getEditableObject(attribute) == null) {
			return;
		}
		
		// edit the  value depending on its type
		if (attribute.getEType().getName().equals("EString") ||
			attribute.getEType().getName().equals("EShort")) {
			
			// the cell editor can use all available horizontal space
			tableEditor.grabHorizontal = true;
			
			// create the Text editor control and set its initial value
			final Text text = new Text(table, SWT.NONE);
			text.setText(item.getText(1));
			text.selectAll();
			
			// hookup a key listener to set the 's value, if changed, when enter 
			// is pressed, and dispose of the editor control when escape is 
			// pressed:
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
			
			// hookup a focus listener to set the  value, if 
			// changed, when the editor control loses focus
			text.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					
					String newValue = text.getText();
					text.dispose();
					
					handleCellEdit(attribute, oldValue, newValue);
					
				}					
			});
			
			// start editing the cell in which the user clicked the
			// mouse
			text.setFocus();
			tableEditor.setEditor(text, item, 1);				
			
		} else if (attribute.getEType().getName().equals("EBoolean")) { 
			
			// the  is of type boolean; we don't need an 
			// endlessly wide combo box
			tableEditor.grabHorizontal = false;
			
			// we need a combobox with values false and true (in that 
			// order):					
			final Combo combo = 
				new Combo(table, SWT.DROP_DOWN | SWT.READ_ONLY);					
			combo.add("false");
			combo.add("true");
			if (Boolean.valueOf(oldValue)) {
				combo.select(1);
			} else {
				combo.select(0);
			}	
			
			// hookup the selection listener
			combo.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					
					boolean newValue = combo.getSelectionIndex() == 1;
					combo.dispose();
					
					handleCellEdit(attribute, oldValue, String.valueOf(newValue));
				}
			});
			
			// hookup a key listener to set the property value, if 
			// changed, when enter is pressed, and disposes of the
			// editor control when escape is pressed:
			combo.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.keyCode == 13 || 
						e.keyCode == 16777296) { // enter-keys								
						
						boolean newValue = combo.getSelectionIndex() == 1;
						combo.dispose();
						
						handleCellEdit(attribute, oldValue, 
									   String.valueOf(newValue));
					} else if (e.keyCode == SWT.ESC) {
						combo.dispose();
					}
				}					
			});
			
			// hookup a focus listener to set the property value, if 
			// changed, when the editor control loses focus
			combo.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					
					boolean newValue = combo.getSelectionIndex() == 1;
					combo.dispose();
					
					handleCellEdit(attribute, oldValue, String.valueOf(newValue));
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