package org.lh.dmlj.schema.editor.ui;

import org.eclipse.core.expressions.PropertyTester;

public class SchemaFileSelectedPropertyTester extends PropertyTester {

	public SchemaFileSelectedPropertyTester() {
		super();
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
					    Object expectedValue) {

		/*ISelection selection;
		try {
			selection = PlatformUI.getWorkbench()
			 					  .getActiveWorkbenchWindow()
					 			  .getSelectionService()
					 			  .getSelection();
		} catch (Throwable t) {
			return false;
		}*/
		//System.out.println(selection);
		return false;
	}

}
