package org.lh.dmlj.schema.editor.property.section;

import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.template.RecordTemplate;

public class RecordSyntaxSection extends AbstractSyntaxSection {
	
	private static final Class<?>[] VALID_EDIT_PART_MODEL_OBJECTS =
		new Class[] {SchemaRecord.class};
	
	public RecordSyntaxSection() {
		super(VALID_EDIT_PART_MODEL_OBJECTS, new RecordTemplate());
	}	
	
	// if we ever want to provide tooltips for the elements in the syntax, the
	// following method is a good start...
	/*@Override
	protected MouseTrackListener getMouseTrackListener() {
		return new MouseTrackAdapter() {			
			@Override
			public void mouseHover(MouseEvent e) {				
				if (!(e.getSource() instanceof StyledText)) {					
					return;
				}
				StyledText styledText = (StyledText) e.getSource();								
				int offset;
				try {
					offset = 
						styledText.getOffsetAtLocation(new Point(e.x, e.y)) - 1;
				} catch (Throwable t) {
					styledText.setToolTipText(null);
					return;
				}
				String text = styledText.getText();
				String tooltip = null;				
				while (offset > 0 && text.charAt(offset) != '.') {
					offset--;
				}
				if (offset > 0) {
					int i = text.indexOf(" 0", offset);
					int j = text.indexOf(".", offset + 1);
					if (i == -1 || i > j) {
						i = text.indexOf(" 88", offset);
					}
					if (i > -1 && i < text.length() - 3 && 
						text.charAt(i + 3) == ' ') {
						
						j = text.indexOf('\r', i);
						if (j > -1) {
							String elementName = text.substring(i + 4, j);
							if (!elementName.equals("FILLER")) {
								SchemaRecord record = 
									(SchemaRecord) editPartModelObject;
								String recordName = record.getName();
								ElementInfoValueObject valueObject = 
									Plugin.getDefault()
										  .getElementInfoValueObject(recordName, 
												  					 elementName);
								if (valueObject != null) {
									tooltip = valueObject.getDescription();
								}
							}
						}
					}					
				}
				styledText.setToolTipText(tooltip);
			}			
		};
	}*/	
	
}