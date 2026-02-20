package org.lh.dmlj.schema.editor.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.dialog.FixEnableAutoscaleSystemPropertyDialog;
import org.lh.dmlj.schema.editor.log.Logger;

public class CheckEnableAutoscaleSystemPropertyJob extends Job {
	private static final String WIN32 = "win32";
	private static final String PROPERTY_NAME = "draw2d.enableAutoscale";
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());

	public CheckEnableAutoscaleSystemPropertyJob() {
		super("Check system property 'enableAutoscale'");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if (!WIN32.equals(SWT.getPlatform()) || SWT.getVersion() < 4971) {
			var message = "Check of system property '%s' is NOT applicable (platform: %s, SWT Version:%s)"
					.formatted(PROPERTY_NAME, SWT.getPlatform(), SWT.getVersion());
			logger.info(message);
			return ok();
		}
		var enableAutoScale = WIN32.equals(SWT.getPlatform()) && Boolean.parseBoolean(System.getProperty(PROPERTY_NAME, Boolean.TRUE.toString()));
		var message = "Check of system property '%s' is applicable (platform: %s, SWT Version:%s): value=%s"
				.formatted(PROPERTY_NAME, SWT.getPlatform(), SWT.getVersion(), System.getProperty(PROPERTY_NAME));
		logger.info(message);
		if (enableAutoScale) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(this::launchFixEnableAutoscaleSystemPropertyDialog);
		}
		return ok();
	}
	
	private void launchFixEnableAutoscaleSystemPropertyDialog() {
		new FixEnableAutoscaleSystemPropertyDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()).open();
	}
	
	private IStatus ok() {
		return new Status(IStatus.OK, Plugin.getDefault().getBundle().getSymbolicName(), "Completed normally: %s".formatted(getName()));
	}

}
