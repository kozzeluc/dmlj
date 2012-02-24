package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.model.SetDescription;
import org.lh.dmlj.schema.editor.template.SetTemplate;

public class SetSyntaxSection extends AbstractSyntaxSection {

	private static final Class<?>[] VALID_EDIT_PART_MODEL_OBJECTS =
		new Class[] {MemberRole.class, SetDescription.class, SystemOwner.class};
	
	public SetSyntaxSection() {
		super(VALID_EDIT_PART_MODEL_OBJECTS, new SetTemplate());				
	}

	@Override
	protected EObject getTemplateObject(Object editPartModelObject) {		
		// the template object is the edit part model object's set
		if (editPartModelObject instanceof MemberRole) {
			// member role edit part model object
			MemberRole memberRole = (MemberRole) editPartModelObject;
			return memberRole.getSet();
		} else if (editPartModelObject instanceof SetDescription) {
			// set description edit part model object
			SetDescription setDescription = (SetDescription) editPartModelObject;
			return setDescription.getMemberRole().getSet();
		} else {
			// system owner edit part model object
			SystemOwner systemOwner = (SystemOwner) editPartModelObject;
			return systemOwner.getSet();
		}		
	}

}