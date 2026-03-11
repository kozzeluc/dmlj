/**
 * Copyright (C) 2025  Luc Hermans
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
		
	private static final FilenameFilter FILTER = (dir, name) -> {
		var fileOrFolder = new File(dir, name);
		return fileOrFolder.isDirectory() || fileOrFolder.getName().endsWith(FILE_EXTENSION_SCHEMA) ||
			   fileOrFolder.getName().endsWith(FILE_EXTENSION_SCHEMADSL);	
	};
	
	private final ISelection selection;
	private final Image iconFolder = Plugin.getDefault().getImage("icons/fldr_obj.gif");
	private final Image iconProject = Plugin.getDefault().getImage("icons/prj_obj.gif");
	private final Image iconSchema = Plugin.getDefault().getImage("icons/schema.gif");		
	private final Map<TreeItem, File> map = new HashMap<>();
	private Schema schema;
	
	private Tree tree;

	public SchemaSelectionPage(ISelection selection) {
		super("schemaSelectionPage");
		this.selection = selection;
		setTitle("CA IDMS/DB Schema Syntax");
		setDescription("Select the CA IDMS/DB Schema");
	}

	public void createControl(Composite parent) {
		var container = new Composite(parent, SWT.NULL);

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
		
		addSchemasInProjectsToTree();
		dealWithSelection();
		validatePage();
	}
	
	private void addSchemasInProjectsToTree() {
		for (var project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (!project.getName().startsWith(".")) {								
				var projectFolder = project.getLocation().toFile();
				var projectTreeItem = new TreeItem(tree, SWT.NONE);
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
	}
	
	private void addTreeItems(TreeItem parentTreeItem, File folder) {
		for (var fileOrFolder : folder.listFiles(FILTER)) {
			if (!fileOrFolder.getName().startsWith(".")) {
				if (fileOrFolder.isDirectory()) {
					var folderTreeItem = new TreeItem(parentTreeItem, SWT.NONE);
					folderTreeItem.setImage(iconFolder);
					folderTreeItem.setText(fileOrFolder.getName());
					map.put(folderTreeItem, fileOrFolder);
					addTreeItems(folderTreeItem, fileOrFolder);
					if (folderTreeItem.getItemCount() == 0) {
						folderTreeItem.dispose();
					}
				} else {
					var schemaTreeItem = new TreeItem(parentTreeItem, SWT.NONE);
					schemaTreeItem.setImage(iconSchema);
					schemaTreeItem.setText(fileOrFolder.getName());
					map.put(schemaTreeItem, fileOrFolder);
				}
			}
		}		
	}
	
	private void dealWithSelection() {
		if (selection != null && !selection.isEmpty() && selection instanceof TreeSelection treeSelection) {
			if (treeSelection.getFirstElement() instanceof IProject project) {
				dealWithSelectedProject(project);
			} else if (treeSelection.getFirstElement() instanceof IFile file) {
				dealWithSelectedFile(file);
			}
		}
	}
	
	private void dealWithSelectedProject(IProject project) {
		for (var treeItem : tree.getItems()) {
			if (treeItem.getText().equals(project.getName())) {						
				tree.setSelection(treeItem);						
				break;
			}					
		}
	}
	
	private void dealWithSelectedFile(IFile file) {
		var selectedFileOrFolder =file.getRawLocation().toFile();
		for (var entry : map.entrySet()) {
			var treeItem = entry.getKey();
			var aFile = map.get(treeItem);
			if (aFile.equals(selectedFileOrFolder)) {
				tree.setSelection(treeItem);
				tree.showItem(treeItem);
				break;
			}
		}
	}

	public Schema getSchema() {
		return schema;
	}
	
	private void validatePage() {
		var pageComplete = true;
		setErrorMessage(null);
		
		var treeSelection = tree.getSelection();
		if (treeSelection.length == 1 && map.containsKey(treeSelection[0])) {
			var selectedFileOrFolder = map.get(treeSelection[0]);
			if (!selectedFileOrFolder.isDirectory()) {
				try {
					schema = Tools.executeWithCursorBusy(() -> Tools.readFromFile(selectedFileOrFolder));
				} catch (Exception e) {
					pageComplete = false;
					var p = e.getMessage() == null ? "" : ": " + e.getMessage();
					setErrorMessage(e.getClass().getSimpleName() + p);
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
