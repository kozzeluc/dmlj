package org.lh.dmlj.schema.editor.property;

import java.util.List;

import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.SchemaPackage;

public class SetSortKeySection extends AbstractKeyAttributeSection {

	public SetSortKeySection() {
		super(SchemaPackage.eINSTANCE.getKey_Elements());
	}
	
	@Override
	protected String getDescription() {
		return "Identifies the member record element(s) on whose values the " +
			   "set is to be sorted (that is, the sort control element)";
	}
	
	@Override
	protected String getLabel() {
		return "Key element(s)";
	}
	
	@Override
	protected String getStringValue(Object value) {
		@SuppressWarnings("unchecked")
		List<KeyElement> keyElements = (List<KeyElement>) value;
		
		if (keyElements.size() == 1 && keyElements.get(0).isDbkey()) {
			return "DBKEY";
		} else {
			StringBuilder p = new StringBuilder();
			for (KeyElement keyElement : keyElements) {
				if (p.length() > 0) {
					p.append(", ");
				}
				p.append(keyElement.getElement().getName());
				p.append(" ");
				p.append(keyElement.getSortSequence().toString());
			}
			return p.toString();
		}
	}

}
