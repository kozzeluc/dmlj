/**
 * Copyright (C) 2013  Luc Hermans
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
package org.lh.dmlj.schema.editor.service;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ServicesPlugin implements BundleActivator {

	private static ServicesPlugin plugin;
	private Map<Class<?>, Object> services = new HashMap<>();	
	
	public static ServicesPlugin getDefault() {
		return plugin;
	}
	
	public ServicesPlugin() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> serviceInterface) {
		return (T) services.get(serviceInterface);
	}
	
	<T> void setService(Class<T> serviceInterface, T serviceImpl) {
		services.put(serviceInterface, serviceImpl);
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;		
	}

	@Override
	public void stop(BundleContext context) throws Exception {				
	}

}
