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
package org.lh.dmlj.schema.editor.property.section;

import java.util.Arrays;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * An abstract superclass for sections in the tabbed properties view that show  
 * their information in a single styled text control.  Subclasses must supply  
 * the valid edit part model object types during construction and can override
 * the getFont method if they want another font than the standard 'syntax' font
 * to be used.
 */
public abstract class AbstractSectionWithStyledText extends AbstractPropertiesSection {
	
	private StyledText styledText;
	private Font 	   syntaxFont = new Font(Display.getCurrent(), "Courier New", 10, SWT.NORMAL);
	private Class<?>[] validEditPartModelObjectTypes;
		
	
	protected AbstractSectionWithStyledText(Class<?>[] validEditPartModelObjectTypes) {
		super();
		this.validEditPartModelObjectTypes = validEditPartModelObjectTypes;		
	}
	
	@Override
	public final void createControls(Composite parent, TabbedPropertySheetPage page) {
				
		super.createControls(parent, page);
        Composite composite = getWidgetFactory().createFlatFormComposite(parent);

		styledText = new StyledText(composite, SWT.MULTI | SWT.READ_ONLY);				
		styledText.setFont(getFont());
		
		FormData data = new FormData();	
		data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		styledText.setLayoutData(data);
		
		// if we ever want to add 'dynamic tooltips', a MouseTrackListener is
		// way to go...
		/*MouseTrackListener mouseTrackListener = getMouseTrackListener();
		if (mouseTrackListener != null) {
			styledText.addMouseTrackListener(mouseTrackListener);
		}*/
        
	}
	
	@Override
	public void dispose() {
		syntaxFont.dispose();
		super.dispose();
	}
	
	protected Font getFont() {
		return syntaxFont;
	}

	protected abstract String getValue(Object editPartModelObject);

	private boolean isValidType(Object object) {
		for (Class<?> _class : validEditPartModelObjectTypes) {
			if (_class.isInstance(object)) {
				return true;
			}
		}
		return false;
	}	
	
	@Override
	public final void refresh() {
		String value = getValue(modelObject);
		styledText.setText(value);
		styledText.setCaretOffset(value.length()); // avoid flickering cursor in top left corner
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {
		
		super.setInput(part, selection);
        
        Assert.isTrue(isValidType(modelObject), 
  			  		  "not a " + Arrays.asList(validEditPartModelObjectTypes) +
  			  		  " but a " + modelObject.getClass().getName());
        
	}	
	
}
