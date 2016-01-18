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
