package org.lh.dmlj.schema.editor.importtool.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.editor.common.ImageCache;

public class LayoutManagerSelectionPage extends WizardPage {

	private Canvas 						  canvas;
	private Combo 						  combo;
	private Color 						  imageBackground = 
		Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	private ImageCache 				 	  imageCache = new ImageCache();
	private LayoutManagerDescriptor 	  layoutManagerDescriptor;
	private List<LayoutManagerDescriptor> layoutManagerDescriptors = 
		new ArrayList<>();
	private Text 					  text;
	private Label lblExample;
	
	public LayoutManagerSelectionPage() {
		super("_layoutManagerSelectionPage", "CA IDMS/DB Schema", null);
		setMessage("Select the record layout manager");
		setPageComplete(false);
	}
	
	@Override
	public void createControl(Composite parent) {		
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label lblInstalledLayoutManagers = new Label(container, SWT.NONE);
		lblInstalledLayoutManagers.setText("Layout manager :");
		
		combo = new Combo(container, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblDescription.setText("Description :");
		
		text = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_text.widthHint = 300;
		gd_text.heightHint = 75;
		text.setLayoutData(gd_text);
		
		lblExample = new Label(container, SWT.NONE);
		GridData gd_lblExample = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblExample.verticalIndent = 10;
		lblExample.setLayoutData(gd_lblExample);
		lblExample.setText("Example :");
		
		canvas = new Canvas(container, SWT.NONE);
		GridData gd_canvas = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_canvas.verticalIndent = 10;
		canvas.setLayoutData(gd_canvas);
		
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				int i = combo.getSelectionIndex();
				
				layoutManagerDescriptor = layoutManagerDescriptors.get(i);
				
				text.setText(layoutManagerDescriptor.getDescription());
				text.redraw();				
			
				drawImage();
				
				setPageComplete(combo.getSelectionIndex() > -1);	
			}
		});		
		
		// we use the following MO to draw the layout manager image for the 
		// first time in the canvas because the image might otherwise not show 
		// up...
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				drawImage();
			}
		});
		
		setLayoutManagerDescriptors(layoutManagerDescriptors);
			
	}
	
	@Override
	public void dispose() {
		imageBackground.dispose();
		imageCache.dispose();
		super.dispose();
	}

	private void drawImage() {
				
		final GC gc = new GC(canvas);
		// clear the canvas first
		gc.setBackground(imageBackground);
		gc.fillRectangle(0, 0, canvas.getBounds().width, 
						 canvas.getBounds().height);
		// get the image if possible and available
		Image image = 
			layoutManagerDescriptor != null ? 
			imageCache.getImage(layoutManagerDescriptor.getImageDescriptor()) : 
			null;
		// fill the canvas with the image
		if (image != null) {			
			// an image is available; make it show up in the canvas
			int width = 
				Math.min(image.getBounds().width, canvas.getBounds().width);
			int height = 
				Math.min(image.getBounds().height, canvas.getBounds().height);			
			gc.drawImage(image, 0, 0, width, height, 0, 0, width, height);
		}
		gc.dispose();		
				
	}

	public LayoutManagerDescriptor getLayoutManagerDescriptor() {
		return layoutManagerDescriptor;
	}	
	
	public void setLayoutManagerDescriptors(List<LayoutManagerDescriptor> layoutManagerDescriptors) {
		
		this.layoutManagerDescriptors = layoutManagerDescriptors;
		
		if (combo == null) {
			return;
		}
		
		combo.removeAll();
		
		for (LayoutManagerDescriptor layoutManagerDescriptor : 
			 layoutManagerDescriptors) {
			
			combo.add(layoutManagerDescriptor.getName());
		}
		
		if (combo.getItemCount() > 0) {
			combo.select(0);
			layoutManagerDescriptor = layoutManagerDescriptors.get(0);
			text.setText(layoutManagerDescriptor.getDescription());
			drawImage();
		} else {
			// we shouldn't get into this situation because our plug-in provides 
			// some layout managers itself and at least 1 of them should be
			// available since it is valid for all schemas
			combo.setEnabled(false);
			setErrorMessage("No layout managers installed or none of the " +
							"layout managers is valid");
			text.setText("Please install at least 1 plug-in that provides a " +
						 "schema import layout manager for the CA IDMS/DB " +
						 "schema you want to import.");
			drawImage();
		}		
		
		setPageComplete(combo.getSelectionIndex() > -1);		
		
	}

}