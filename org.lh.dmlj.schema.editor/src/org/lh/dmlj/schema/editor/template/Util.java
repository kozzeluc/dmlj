package org.lh.dmlj.schema.editor.template;

import org.lh.dmlj.schema.Usage;

public abstract class Util {
	
	public static String getUsageShortform(Usage usage) {
		if (usage == Usage.COMPUTATIONAL) {
			return "COMP";
		} else if (usage == Usage.COMPUTATIONAL_1) {
			return "COMP-1";			
		} else if (usage == Usage.COMPUTATIONAL_3) {
			return "COMP-3";			
		}
		return usage.toString().replaceAll("_", "-");
	}
	
}