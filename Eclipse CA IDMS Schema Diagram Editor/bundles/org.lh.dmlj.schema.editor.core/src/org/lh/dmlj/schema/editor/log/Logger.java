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
package org.lh.dmlj.schema.editor.log;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public final class Logger {
	private static final String NO_BUNDLE_SYMBOLIC_NAME_AVAILABLE = "<no bundle symbolic name available>";
	
	private String bundleSymbolicName;
	private LogProvidingPlugin logProvider;
	
	public static Logger getLogger(LogProvidingPlugin logProvider) {		
		var bundleSymbolicName =
			logProvider != null && logProvider.getBundle() != null && logProvider.getBundle().getSymbolicName() != null ? 
			logProvider.getBundle().getSymbolicName() : NO_BUNDLE_SYMBOLIC_NAME_AVAILABLE;
		if (logProvider != null) {
			return new Logger(bundleSymbolicName, logProvider);
		} else {
			return new Logger(bundleSymbolicName, new DummyLogProvidingPlugin());
		}
	}
	
	private Logger(String bundleSymbolicName, LogProvidingPlugin logProvider) {
		this.bundleSymbolicName = bundleSymbolicName;
		this.logProvider = logProvider;
	}	

	String createDebugMessageForCallingMethod(String currentThreadName, List<WrappedStackTraceElement> stackTraceElements) {
		if (stackTraceElements.size() > 3) {
			var className = stackTraceElements.get(3).getClassName();
			String simpleClassName;
			if (className.contains(".")) {
				simpleClassName = className.substring(className.lastIndexOf(".") + 1);
			} else {
				simpleClassName = className;
			}
			return "Method " + stackTraceElements.get(2).getMethodName() + "(...) in class " +
				   stackTraceElements.get(2).getClassName() + "\n       was called from " +
				   stackTraceElements.get(3).getClassName() + "." +
				   stackTraceElements.get(3).getMethodName() + "(" +
				   simpleClassName + ".java:" +
				   stackTraceElements.get(3).getLineNumber() + ")" + "\n       (thread=" +
				   currentThreadName + ")";
		} else {
			return "[stackTraceElements.length <= 3: " + stackTraceElements.size() + "]";
		}
	}
	
	public void debug(String message) {
		if (logProvider.isDebugEnabled()) {
			doLog(IStatus.INFO, message, null);
		}
	}
	
	public void debug(DebugItem debugItem) {
		if (logProvider.isDebugEnabled() && debugItem == DebugItem.CALLING_METHOD) {
			var currentThread = Thread.currentThread();
			var wrappedStackTraceElements = Arrays.stream(currentThread.getStackTrace())
					.map(WrappedStackTraceElement::new)
					.toList();
			debug(createDebugMessageForCallingMethod(currentThread.getName(), wrappedStackTraceElements));
		}
	}
	
	private void doLog(int severity, String message, Throwable exception) {
		var status = new Status(severity, bundleSymbolicName, IStatus.OK, message, exception);
		logProvider.getLog().log(status);
	}
	
	public void error(String message, Throwable exception) {
		doLog(IStatus.ERROR, message, exception);
	}
	
	String getBundleSymbolicName() {
		return bundleSymbolicName;
	}
	
	LogProvidingPlugin getLogProvider() {
		return logProvider;
	}
	
	public void info(String message) {
		doLog(IStatus.INFO, message, null);
	}

	public void warning(String message) {
		doLog(IStatus.WARNING, message, null);
	}
	
	static class WrappedStackTraceElement {
		private StackTraceElement stackTraceElement;
				
		WrappedStackTraceElement(StackTraceElement stackTraceElement) {
			this.stackTraceElement = stackTraceElement;
		}
		
		public String getClassName() {
			return stackTraceElement.getClassName();
		}
		
		public int getLineNumber() {
			return stackTraceElement.getLineNumber();
		}
		
		public String getMethodName() {
			return stackTraceElement.getMethodName();
		}
		
	}
	
}
