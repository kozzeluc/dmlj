package org.lh.dmlj.schema.editor.property;

import java.util.List;

import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.SchemaPackage;

public class RecordCalcKeySection extends AbstractKeyAttributeSection {

	public RecordCalcKeySection() {
		super(SchemaPackage.eINSTANCE.getKey_Elements());
	}
	
	@Override
	protected String getDescription() {
		return "Specifies the record element(s) whose value(s) will be used " +
			   "to calculate the page to store an occurrence of the record";
	}
	
	@Override
	protected String getLabel() {
		return "Key element(s)";
	}
	
	@Override
	protected String getStringValue(Object value) {
		@SuppressWarnings("unchecked")
		List<KeyElement> keyElements = (List<KeyElement>) value;
		
		StringBuilder p = new StringBuilder();
		for (KeyElement keyElement : keyElements) {
			if (p.length() > 0) {
				p.append(", ");
			}
			p.append(keyElement.getElement().getName());
		}
		return p.toString();
	}

}
