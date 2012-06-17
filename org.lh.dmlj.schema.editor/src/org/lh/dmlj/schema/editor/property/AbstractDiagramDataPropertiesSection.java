package org.lh.dmlj.schema.editor.property;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.Schema;

public abstract class AbstractDiagramDataPropertiesSection 
	extends AbstractPropertiesSection<DiagramData> {	

	@Override
	protected final DiagramData getTarget(Object modelObject) {				
		Assert.isTrue(modelObject instanceof Schema, "not a Schema");				
		return ((Schema) modelObject).getDiagramData();		
	}
	
}