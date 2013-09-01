package org.lh.dmlj.schema.editor.property.section;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.Plugin;

public abstract class AbstractDiagramDataPropertiesSection 
	extends AbstractAttributesBasedPropertiesSection<DiagramData> {	

	public AbstractDiagramDataPropertiesSection() {
		super(Plugin.getDefault());
	}

	@Override
	protected final DiagramData getTarget(Object modelObject) {				
		Assert.isTrue(modelObject instanceof Schema, "not a Schema");				
		return ((Schema) modelObject).getDiagramData();		
	}
	
}