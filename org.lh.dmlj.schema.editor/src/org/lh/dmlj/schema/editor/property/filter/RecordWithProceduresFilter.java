package org.lh.dmlj.schema.editor.property.filter;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.SchemaRecord;

public class RecordWithProceduresFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (!(object instanceof EditPart)) {
			return false;
		}
        Object modelObject = ((EditPart) object).getModel();        
        if (modelObject instanceof SchemaRecord) {
        	SchemaRecord record = (SchemaRecord) modelObject;
        	return !record.getProcedures().isEmpty();
        } else {
        	return false;
        }        
	}

}