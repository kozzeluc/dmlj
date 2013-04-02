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
