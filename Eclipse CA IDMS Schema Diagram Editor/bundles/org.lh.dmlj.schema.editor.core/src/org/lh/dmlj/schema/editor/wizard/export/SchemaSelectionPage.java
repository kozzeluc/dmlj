/**
 * Copyright (C) 2020  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.wizard.export;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.log.Logger;

public class SchemaSelectionPage extends WizardPage {

	private static final String FILE_EXTENSION_SCHEMA = ".schema";
	private static final String FILE_EXTENSION_SCHEMADSL = ".schemadsl";

	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
		
	private static final FilenameFilter FILTER = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			File fileOrFolder = new File(dir, name);
			return fileOrFolder.isDirectory() || fileOrFolder.getName().endsWith(FILE_EXTENSION_SCHEMA) || 
				   fileOrFolder.getName().endsWith(FILE_EXTENSION_SCHEMADSL);
		}
	};
	
	private Image iconFolder =Plugin.getDefault().getImage("icons/fldr_obj.gif");
	private Image iconProject = Plugin.getDefault().getImage("icons/prj_obj.gif");
	private Image iconSchema = Plugin.getDefault().getImage("icons/schema.gif");		
	private Map<TreeItem, File> map = new HashMap<>();
	private Schema schema;
	private ISelection selection;
	private Tree tree;

	public SchemaSelectionPage(ISelection selection) {
		super("schemaSelectionPage");
		this.selection = selection;
		setTitle("CA IDMS/DB Schema Syntax");
		setDescription("Select the CA IDMS/DB Schema");
	}

	private void addTreeItems(TreeItem parentTreeItem, File folder) {
		for (File fileOrFolder : folder.listFiles(FILTER)) {
			if (!fileOrFolder.getName().startsWith(".")) {
				if (fileOrFolder.isDirectory()) {
					TreeItem folderTreeItem = 
						new TreeItem(parentTreeItem, SWT.NONE);
					folderTreeItem.setImage(iconFolder);
					folderTreeItem.setText(fileOrFolder.getName());
					map.put(folderTreeItem, fileOrFolder);
					addTreeItems(folderTreeItem, fileOrFolder);
					if (folderTreeItem.getItemCount() == 0) {
						folderTreeItem.dispose();
					}
				} else {
					TreeItem schemaTreeItem = 
						new TreeItem(parentTreeItem, SWT.NONE);
					schemaTreeItem.setImage(iconSchema);
					schemaTreeItem.setText(fileOrFolder.getName());
					map.put(schemaTreeItem, fileOrFolder);
				}
			}
		}		
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		tree = new Tree(container, SWT.BORDER);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validatePage();
			}
		});
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();		
		
		for (IProject project : root.getProjects()) {
			if (!project.getName().startsWith(".")) {								
				File projectFolder = project.getLocation().toFile();
				TreeItem projectTreeItem = new TreeItem(tree, SWT.NONE);
				projectTreeItem.setImage(iconProject);
				projectTreeItem.setText(projectFolder.getName());				
				try {
					addTreeItems(projectTreeItem, projectFolder);
				} catch (NullPointerException e) {
					// this situation usually indicates an open project without any .schema file
					logger.debug("caught NPE (export) : " + project.getName());
				}
				if (projectTreeItem.getItemCount() == 0) {
					projectTreeItem.dispose();
				}				
			}
		}	
				
		if (selection != null && !selection.isEmpty() && 
			selection instanceof TreeSelection) {
			
			TreeSelection treeSelection = (TreeSelection) selection;
			if (treeSelection.getFirstElement() instanceof IProject) {
				IProject project = (IProject) treeSelection.getFirstElement();
				for (TreeItem treeItem : tree.getItems()) {
					if (treeItem.getText().equals(project.getName())) {						
						tree.setSelection(treeItem);						
						break;
					}					
				}
			} else if (treeSelection.getFirstElement() instanceof IFile) {
				File selectedFileOrFolder = 
					((IFile) treeSelection.getFirstElement()).getRawLocation()
															 .toFile();
				for (TreeItem treeItem : map.keySet()) {
					File aFile = map.get(treeItem);
					if (aFile.equals(selectedFileOrFolder)) {
						tree.setSelection(treeItem);
						tree.showItem(treeItem);
						break;
					}
				}
			}
		}
		
		validatePage();
	}	

	public Schema getSchema() {
		return schema;
	}
	
	private void validatePage() {
		
		boolean pageComplete = true;
		setErrorMessage(null);
		
		TreeItem[] selection = tree.getSelection();
		if (selection.length == 1 && map.containsKey(selection[0])) {
			File selectedFileOrFolder = map.get(selection[0]);
			if (!selectedFileOrFolder.isDirectory()) {
				try {
					schema = Tools.executeWithCursorBusy(() -> Tools.readFromFile(selectedFileOrFolder));
				} catch (Throwable t) {
					pageComplete = false;
					String p = t.getMessage() == null ? "" : ": " + t.getMessage();
					setErrorMessage(t.getClass().getSimpleName() + p);
				}
			} else {
				pageComplete = false;
			}
		} else {
			pageComplete = false;
		}
				
		setPageComplete(pageComplete);
	}
	
}
