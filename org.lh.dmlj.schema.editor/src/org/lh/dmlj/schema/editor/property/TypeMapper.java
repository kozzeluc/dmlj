package org.lh.dmlj.schema.editor.property;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.views.properties.tabbed.AbstractTypeMapper;

public class TypeMapper extends AbstractTypeMapper {

	@Override
	public Class<?> mapType(Object object) {
		if (object instanceof EditPart) {
            return ((EditPart) object).getModel().getClass();
        }
        return super.mapType(object);
	}

}
