package org.lh.dmlj.schema.editor.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.editor.view.TmpView;

public class TmpZoomHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		TmpView tmpView = (TmpView)PlatformUI.getWorkbench()
											 .getActiveWorkbenchWindow()
											 .getActivePage()
											 .findView(TmpView.ID);
		int percentage = -1;
		if (event.getCommand().getId().endsWith("50")) {
			percentage = 50;
		} else if (event.getCommand().getId().endsWith("100")) {
			percentage = 100;
		} else if (event.getCommand().getId().endsWith("200")) {
			percentage = 200;
		} else if (event.getCommand().getId().endsWith("500")) {
			percentage = 500;
		}
		//tmpView.zoom(percentage); // temporarily unavailable
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
	}

}
