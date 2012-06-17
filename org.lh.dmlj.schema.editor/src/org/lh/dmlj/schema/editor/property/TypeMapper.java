package org.lh.dmlj.schema.editor.property;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.views.properties.tabbed.AbstractTypeMapper;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SystemOwner;

public class TypeMapper extends AbstractTypeMapper {

	@Override
	public Class<?> mapType(Object object) {
		if (object instanceof EditPart) {
			EditPart editPart = (EditPart) object;
			Object modelObject = editPart.getModel();            
            if (modelObject instanceof ConnectionPart ||
            	modelObject instanceof ConnectionLabel ||
            	modelObject instanceof SystemOwner ||
            	modelObject instanceof Connector) {
            	
            	return MemberRole.class;
            } else { 
            	return editPart.getModel().getClass();
            }
        }
        return super.mapType(object);
	}

}