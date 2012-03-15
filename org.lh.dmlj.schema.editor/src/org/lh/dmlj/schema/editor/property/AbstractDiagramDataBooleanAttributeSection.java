package org.lh.dmlj.schema.editor.property;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.Schema;

public class AbstractDiagramDataBooleanAttributeSection 
	extends AbstractBooleanAttributeSection<DiagramData> {	
	
	public AbstractDiagramDataBooleanAttributeSection(EAttribute attribute) {
		super(attribute);		
	}	

	@Override
	protected final DiagramData getModelObject(Object editPartModelObject) {
		Assert.isTrue(editPartModelObject instanceof Schema, "not a Schema");
		Schema schema = (Schema) editPartModelObject;
		return schema.getDiagramData();
	}
	
}