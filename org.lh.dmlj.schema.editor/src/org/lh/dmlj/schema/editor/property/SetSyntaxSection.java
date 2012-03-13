package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.template.SetTemplate;

public class SetSyntaxSection extends AbstractSyntaxSection {

	private static final Class<?>[] VALID_EDIT_PART_MODEL_OBJECTS =
		new Class[] {ConnectionLabel.class, ConnectionPart.class, 
					 Connector.class, SystemOwner.class};
	
	public SetSyntaxSection() {
		super(VALID_EDIT_PART_MODEL_OBJECTS, new SetTemplate());				
	}

	@Override
	protected EObject getTemplateObject(Object editPartModelObject) {		
		// the template object is the edit part model object's set
		if (editPartModelObject instanceof ConnectionPart) {
			// connection part edit part model object
			MemberRole memberRole = 
				((ConnectionPart) editPartModelObject).getMemberRole();
			return memberRole.getSet();
		} else if (editPartModelObject instanceof ConnectionLabel) {
			// connection label edit part model object
			ConnectionLabel connectionLabel = 
				(ConnectionLabel) editPartModelObject;
			return connectionLabel.getMemberRole().getSet();
		} else if (editPartModelObject instanceof Connector) {
			// connector edit part model object
			Connector connector = (Connector) editPartModelObject;
			return connector.getConnectionPart().getMemberRole().getSet();
		} else {
			// system owner edit part model object
			SystemOwner systemOwner = (SystemOwner) editPartModelObject;
			return systemOwner.getSet();
		}		
	}

}