/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.dictionary.tools.preference;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.lh.dmlj.schema.editor.dictionary.tools.Plugin;
import org.eclipse.swt.widgets.Group;

public class DriverInformationPage extends PreferencePage implements IWorkbenchPreferencePage {
	
	private Text textVersion;
	private Text textId;
	private Text textBundleVersion;
	private Text textBundleName;
	private Text textBundleVendor;

	public DriverInformationPage() {
		super();
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {

		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		
		Group grpDriverInformation = new Group(container, SWT.NONE);
		grpDriverInformation.setLayout(new GridLayout(2, false));
		grpDriverInformation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpDriverInformation.setText("CA IDMS JDBC Driver information");
		
		Label lblVersion = new Label(grpDriverInformation, SWT.NONE);
		lblVersion.setText("Version:");
		
		textVersion = new Text(grpDriverInformation, SWT.BORDER | SWT.READ_ONLY);
		textVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpProviderPlugin = new Group(container, SWT.NONE);
		grpProviderPlugin.setText("Provider plug-in");
		grpProviderPlugin.setLayout(new GridLayout(2, false));
		GridData gd_grpProviderPlugin = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_grpProviderPlugin.verticalIndent = 10;
		grpProviderPlugin.setLayoutData(gd_grpProviderPlugin);
		
		Label lblNewLabel = new Label(grpProviderPlugin, SWT.NONE);
		lblNewLabel.setText("ID:");
		
		textId = new Text(grpProviderPlugin, SWT.BORDER | SWT.READ_ONLY);
		textId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(grpProviderPlugin, SWT.NONE);
		lblNewLabel_1.setText("Version:");
		
		textBundleVersion = new Text(grpProviderPlugin, SWT.BORDER | SWT.READ_ONLY);
		textBundleVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_2 = new Label(grpProviderPlugin, SWT.NONE);
		lblNewLabel_2.setText("Name:");
		
		textBundleName = new Text(grpProviderPlugin, SWT.BORDER | SWT.READ_ONLY);
		textBundleName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_3 = new Label(grpProviderPlugin, SWT.NONE);
		lblNewLabel_3.setText("Vendor:");
		
		textBundleVendor = new Text(grpProviderPlugin, SWT.BORDER | SWT.READ_ONLY);
		textBundleVendor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		initialize();
		
		return container;
		
	}

	private void initialize() {
		
		textVersion.setText(Plugin.getDefault().getDriverVersion());
		
		textId.setText(Plugin.getDefault().getDriverBundleId());
		textBundleVersion.setText(Plugin.getDefault().getDriverBundleVersion());
		textBundleName.setText(Plugin.getDefault().getDriverBundleName());
		textBundleVendor.setText(Plugin.getDefault().getDriverBundleVendor());
		
	}
}
