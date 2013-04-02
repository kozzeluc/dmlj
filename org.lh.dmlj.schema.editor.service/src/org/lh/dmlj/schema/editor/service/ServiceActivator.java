package org.lh.dmlj.schema.editor.service;

import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;

public class ServiceActivator {
	
	public ServiceActivator() {
		super();		
	}	
	
	public void setPdfExtractorService(IPdfExtractorService pdfExtractorService) {
		ServicesPlugin.getDefault().setService(IPdfExtractorService.class, pdfExtractorService);
	}	
	
}