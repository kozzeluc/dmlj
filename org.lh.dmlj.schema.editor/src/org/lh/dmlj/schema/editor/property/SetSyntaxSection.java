package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.template.SetTemplate;

public class SetSyntaxSection extends AbstractSyntaxSection {

	// in the future, the MemberRole class will have to be replaced by the
	// Connection class since the model object type for connections will change. 
	private static final Class<?>[] VALID_EDIT_PART_MODEL_OBJECTS =
		new Class[] {MemberRole.class, ConnectionLabel.class, SystemOwner.class};
	
	public SetSyntaxSection() {
		super(VALID_EDIT_PART_MODEL_OBJECTS, new SetTemplate());				
	}

	@Override
	protected EObject getTemplateObject(Object editPartModelObject) {		
		// the template object is the edit part model object's set
		if (editPartModelObject instanceof MemberRole) {
			// member role edit part model object (todo: switch this to a
			// Connection once we're ready to implement this change)
			MemberRole memberRole = (MemberRole) editPartModelObject;
			return memberRole.getSet();
		} else if (editPartModelObject instanceof ConnectionLabel) {
			// connection label edit part model object
			ConnectionLabel connectionLabel = 
				(ConnectionLabel) editPartModelObject;
			return connectionLabel.getMemberRole().getSet();
		} else {
			// system owner edit part model object
			SystemOwner systemOwner = (SystemOwner) editPartModelObject;
			return systemOwner.getSet();
		}		
	}

}