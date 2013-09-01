package org.lh.dmlj.schema.editor.property.filter;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.SchemaArea;

public class AreaWithProceduresFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (!(object instanceof EditPart)) {
			return false;
		}
        Object modelObject = ((EditPart) object).getModel();        
        if (modelObject instanceof SchemaArea) {
        	SchemaArea area = (SchemaArea) modelObject;
        	return !area.getProcedures().isEmpty();
        } else {
        	return false;
        }        
	}

}