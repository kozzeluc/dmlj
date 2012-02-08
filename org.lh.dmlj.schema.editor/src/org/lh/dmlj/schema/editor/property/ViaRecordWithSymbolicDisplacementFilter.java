package org.lh.dmlj.schema.editor.property;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.ViaSpecification;

public class ViaRecordWithSymbolicDisplacementFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (!(object instanceof EditPart)) {
			return false;
		}
        Object modelObject = ((EditPart) object).getModel();        
        if (modelObject instanceof SchemaRecord) {
        	SchemaRecord record = (SchemaRecord) modelObject;
        	if (record.getLocationMode() == LocationMode.VIA) {
        		ViaSpecification viaSpecification =
        			record.getViaSpecification();
        		return viaSpecification.getSymbolicDisplacementName() != null;
        	}
        } 
        return false;       
	}

}