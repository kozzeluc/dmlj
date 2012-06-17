package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
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

/**
 * A property editor that is triggered when the user clicks the mouse button
 * somewhere in the second column of a section's property table.
 */
public class PropertyEditor extends MouseAdapter {

	private CommandStack	 		 	 commandStack;
	private AbstractPropertiesSection<?> section;
	private TableEditor 				 tableEditor;
	
	public PropertyEditor(AbstractPropertiesSection<?> section, Table table) {
		super();
		this.section = section;
		// create the editor for the table cells
		tableEditor = new TableEditor(table);
		tableEditor.horizontalAlignment = SWT.LEFT;
		tableEditor.minimumWidth = 50;
		// add the mouse listener
		table.addMouseListener(this);
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
		
		// get the selected feature and its current (old) value
		int i = table.getSelectionIndex();
		final EStructuralFeature feature = section.getFeatures().get(i);
		final String oldValue = item.getText(1);		
		
		// exit this method if the user clicked somewhere else than in 
		// the second column
		if (e.x < table.getColumns()[0].getWidth()) {					
			return;
		}
		
		// if the feature is not editable we're also done
		if (!section.isEditableFeature(feature)) {
			return;
		}
		
		// edit the feature value depending on its type
		if (feature.getEType().getName().equals("EString")) {
			
			// the cell editor can use all available horizontal space
			tableEditor.grabHorizontal = true;
			
			// create the Text editor control and set its initial value
			final Text text = new Text(table, SWT.NONE);
			text.setText(item.getText(1));
			text.selectAll();
			
			// hookup a key listener to set the feature's value, if 
			// changed, when enter is pressed, and dispose of the 
			// editor control when escape is pressed:
			text.addKeyListener(new KeyAdapter() {
				/*public void keyPressed(KeyEvent e) {
					
					// this method is only needed for features with a
					// numeric type							
					if (feature == null) {
						return;
					}
					
					// make sure that only digits can be entered
					if (!(e.keyCode >= SWT.KEYPAD_0 && e.keyCode <= SWT.KEYPAD_9 ||
						  e.keyCode == SWT.ARROW_LEFT ||
						  e.keyCode == SWT.ARROW_RIGHT ||
						  e.keyCode == SWT.HOME ||
						  e.keyCode == SWT.END ||
						  e.keyCode == SWT.DEL ||
						  e.keyCode == '\b')) {
						
						e.doit = false;
					}
					
				}*/						
				public void keyReleased(KeyEvent e) {
					if (e.keyCode == 13 || 
						e.keyCode == 16777296) { // enter-keys								
						
						String newValue = text.getText();
						text.dispose();
						
						if (newValue.equals(oldValue)) {
							return; // feature value not changed
						}
						
						// set the new feature value with the proper 
						// command
						Command command =
							section.getEditCommand(feature, newValue);
						commandStack.execute(command);
						
					} else if (e.keyCode == SWT.ESC) {
						text.dispose();						
					}
				}					
			});			
			
			// hookup a focus listener to set the feature value, if 
			// changed, when the editor control loses focus
			text.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					
					String newValue = text.getText();
					text.dispose();
					
					if (!newValue.equals(oldValue)) {
						Command command =
							section.getEditCommand(feature, newValue);
						commandStack.execute(command);
					}
					
				}					
			});
			
			// start editing the cell in which the user clicked the
			// mouse
			text.setFocus();
			tableEditor.setEditor(text, item, 1);				
			
		} else if (feature.getEType().getName().equals("EBoolean")) { 
			
			// the feature is of type boolean; we don't need an 
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
					boolean bOldValue = 
						Boolean.valueOf(oldValue).booleanValue(); 
					if (newValue != bOldValue) {								
						String newStringValue = 
							String.valueOf(newValue);
						Command command =
							section.getEditCommand(feature, newStringValue);
						commandStack.execute(command);
					}
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
						
						boolean newValue = 
							combo.getSelectionIndex() == 1;
						combo.dispose();
						boolean bOldValue = 
							Boolean.valueOf(oldValue).booleanValue();
						if (newValue != bOldValue) {
							String newStringValue = 
								String.valueOf(newValue);
							Command command =
								section.getEditCommand(feature, newStringValue);
							commandStack.execute(command);
						}
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
					boolean bOldValue = 
						Boolean.valueOf(oldValue).booleanValue();
					if (newValue != bOldValue) {
						String newStringValue = 
							String.valueOf(newValue);
						Command command =
							section.getEditCommand(feature, newStringValue);
						commandStack.execute(command);
					}
				}					
			});
			
			// start editing the cell in which the user clicked the
			// mouse
			combo.setFocus();
			tableEditor.setEditor(combo, item, 1);					
			
		} else {
			
			// we (currently) don't support the feature's type
			throw new Error("unsupported feature type: " + 
						    feature.getEType().getName());
			
		}
	}

	public void setCommandStack(CommandStack commandStack) {
		this.commandStack = commandStack;		
	}	
	
}