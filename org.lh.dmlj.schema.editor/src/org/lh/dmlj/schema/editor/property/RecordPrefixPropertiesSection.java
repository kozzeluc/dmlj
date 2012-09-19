package org.lh.dmlj.schema.editor.property;        

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Role;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.SchemaEditor;
                      
public class RecordPrefixPropertiesSection 
	extends AbstractPropertySection 
	implements CommandStackEventListener {	

	private CommandStack 			commandStack;
	private TabbedPropertySheetPage page;	
	private ISelection 			    selection;
	private ISelection 			    selectionCopy;
	private Table		   			table;
	protected SchemaRecord 			target;
		       
	@Override
	public void dispose() {
		// remove us as a command stack event listener if we have a command
		// stack
		if (commandStack != null) {
			commandStack.removeCommandStackEventListener(this);
		}		
	};	
	
	private static String getPointerType(Role role, short position) {
		if (role instanceof OwnerRole) {
			OwnerRole ownerRole = (OwnerRole) role;
			if (ownerRole.getNextDbkeyPosition() == position) {
				return "NEXT";
			} else if (ownerRole.getPriorDbkeyPosition() != null &&
					   ownerRole.getPriorDbkeyPosition().shortValue() == position) {
				
				return "PRIOR";
			}
		} else {
			MemberRole memberRole = (MemberRole) role;
			if (memberRole.getNextDbkeyPosition() != null &&
				memberRole.getNextDbkeyPosition().shortValue() == position) {
				
				return "NEXT";
			} else if (memberRole.getPriorDbkeyPosition() != null &&
					   memberRole.getPriorDbkeyPosition().shortValue() == position) {
				
				return "PRIOR";
			} else if (memberRole.getOwnerDbkeyPosition() != null &&
					   memberRole.getOwnerDbkeyPosition().shortValue() == position) {
				
				return "OWNER";
			} else if (memberRole.getIndexDbkeyPosition() != null &&
					   memberRole.getIndexDbkeyPosition().shortValue() == position) {
				return "INDEX";
			}
		}
		return "?";
	}

	private static String getSetName(Role role) {
		if (role instanceof OwnerRole) {
			return ((OwnerRole) role).getSet().getName();
		} else {
			return ((MemberRole) role).getSet().getName();
		}
	}

	public RecordPrefixPropertiesSection() {
		super();
	}	

	@Override
	public final void createControls(Composite parent,
							   		 TabbedPropertySheetPage page) {

		super.createControls(parent, page);		
		
		this.page = page;

		// create the container to hold the table
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(Display.getCurrent()
			     					   .getSystemColor(SWT.COLOR_WHITE));
		GridLayout gridLayout = new GridLayout(1, false);
		composite.setLayout(gridLayout);

		// create the table and set its layout data
		table = new Table(composite, SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = SWT.FILL;
		table.setLayoutData(gridData);

		// create the first table column, holding the pointer position
		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setWidth(50);
		column1.setText("Pos.");		
		
		// create the second table column, holding the set to which the pointer
		// applies
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setWidth(125);
		column2.setText("Set");
		
		// create the third table column, holding the pointer type
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setWidth(60);
		column3.setText("Role");
		
		// create the third table column, holding the pointer type
		TableColumn column4 = new TableColumn(table, SWT.NONE);
		column4.setWidth(60);
		column4.setText("Pointer");	

	}		

	@Override
	public final void refresh() {		
	
		// remove all table rows
		table.removeAll();		
		
		// build a map with the pointer position as the key and the role as the
		// value
		Map<Short, Role> map = new HashMap<>();
		for (Role role : target.getRoles()) {
			if (role instanceof OwnerRole) {
				OwnerRole ownerRole = (OwnerRole) role;
				map.put(Short.valueOf(ownerRole.getNextDbkeyPosition()), 
						ownerRole);
				if (ownerRole.getPriorDbkeyPosition() != null) {
					map.put(ownerRole.getPriorDbkeyPosition(), ownerRole);
				}
			} else {
				MemberRole memberRole = (MemberRole) role;
				if (memberRole.getNextDbkeyPosition() != null) {
					map.put(memberRole.getNextDbkeyPosition(), memberRole);
				}
				if (memberRole.getPriorDbkeyPosition() != null) {
					map.put(memberRole.getPriorDbkeyPosition(), memberRole);
				}
				if (memberRole.getOwnerDbkeyPosition() != null) {
					map.put(memberRole.getOwnerDbkeyPosition(), memberRole);
				}
				if (memberRole.getIndexDbkeyPosition() != null) {
					map.put(memberRole.getIndexDbkeyPosition(), memberRole);
				}
			}
		}		
		
		// (re-)populate the table
		List<Short> positions = new ArrayList<>(map.keySet());
		Collections.sort(positions);
		for (Short position : positions) {			
			Role role = map.get(position);
			String setName = getSetName(role);
			String pointerType = getPointerType(role, position.shortValue());
			TableItem item = new TableItem(table, SWT.NONE);			
			item.setText(0, position.toString());			
			item.setText(1, setName);						
			if (role instanceof OwnerRole) {
				item.setText(2, "owner");
			} else {
				item.setText(2, "member");
			}
			item.setText(3, pointerType);
		}
	
		// we don't want any vertical scrollbar in the table; the following
		// sequence allows us to do just that (i.e. vertically stretch the table
		// as needed)          
		for (Composite parent = table.getParent(); parent != null; 
			 parent = parent.getParent()) {                      
		     
			parent.layout();                                          
		}
	
	}

	@Override
	public final void setInput(IWorkbenchPart part, ISelection selection) {

		// remove us as a command stack event listener if we have a command
		// stack
		if (commandStack != null) {
			commandStack.removeCommandStackEventListener(this);
			commandStack = null;
		}		
		
		super.setInput(part, selection);
		this.selection = selection;
		
		// we can only work with SchemaEditor instances
		Assert.isTrue(part instanceof SchemaEditor, "not a SchemaEditor");
		Assert.isTrue(selection instanceof IStructuredSelection,
        			  "not a IStructuredSelection");
        Object input = ((IStructuredSelection) selection).getFirstElement();        

        Object modelObject = ((EditPart) input).getModel();
        target = (SchemaRecord) modelObject;
       
	    // we need to remember the editor's command stack, not to change any 
        // model data (this section contains nothing but read-only data), but 
        // to remove us as a listener from the command stack later on
	    commandStack = ((SchemaEditor) part).getCommandStack();	     
	    commandStack.addCommandStackEventListener(this); 

	}	
	
	@Override
	public void stackChanged(CommandStackEvent event) {		
		// by listening for command stack events we are able to refresh the
		// whole property sheet page; this seems to be the most practical
		// approach in keeping all tabs and sections in sync with the model
		if (event.isPreChangeEvent() &&
			(event.getDetail() == CommandStack.PRE_EXECUTE ||
			 event.getDetail() == CommandStack.PRE_REDO ||
			 event.getDetail() == CommandStack.PRE_UNDO)) {
			
			// for some awkard reason, the setInput method is called somewhere
			// between the event's pre- and post processing, so we set aside the
			// current selection here
			selectionCopy = selection;
		} else if (event.isPostChangeEvent() &&
			       (event.getDetail() == CommandStack.POST_EXECUTE ||
			        event.getDetail() == CommandStack.POST_REDO ||
			        event.getDetail() == CommandStack.POST_UNDO)) {
							
			// we need to execute this asynchronously to avoid Widget' is 
			// disposed' errors (don't ask why ;-)):
			Display.getCurrent().asyncExec(new Runnable() {
				@Override
				public void run() {					
					// (simulate a) clear (of) the current selection
					page.selectionChanged(getPart(), new StructuredSelection());
					// set the (original) selection again
					page.selectionChanged(getPart(), selectionCopy);					
				}				
			});
		}
	}	

}