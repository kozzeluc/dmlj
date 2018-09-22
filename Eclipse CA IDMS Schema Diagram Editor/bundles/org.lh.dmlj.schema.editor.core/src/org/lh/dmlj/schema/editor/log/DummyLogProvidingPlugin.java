/**
 * Copyright (C) 2018  Luc Hermans
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

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.osgi.framework.Bundle;

final class DummyLogProvidingPlugin implements LogProvidingPlugin {

	private final ILog log = new ILog() {
		@Override
		public void addLogListener(ILogListener listener) {
			throw new UnsupportedOperationException();
		}
		@Override
		public Bundle getBundle() {
			throw new UnsupportedOperationException();
		}
		@Override
		public void log(IStatus status) {
		}
		@Override
		public void removeLogListener(ILogListener listener) {
			throw new UnsupportedOperationException();
		}					
	};
	
	@Override
	public Bundle getBundle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ILog getLog() {
		return log;
	}

	@Override
	public boolean isDebugEnabled() {
		return false;
	}
	
}