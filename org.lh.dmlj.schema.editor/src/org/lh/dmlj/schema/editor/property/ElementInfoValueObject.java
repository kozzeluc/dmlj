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
package org.lh.dmlj.schema.editor.property;

public class ElementInfoValueObject {
	
	private String description;
	private String elementName;		
	private String levelAndElementName;
	private String pictureAndUsage;
	private int	   seqNo;
	
	public ElementInfoValueObject(int seqNo) {
		super();	
		this.seqNo = seqNo;
	}

	public String getDescription() {
		return description;
	}

	public String getElementName() {
		return elementName;
	}

	public String getLevelAndElementName() {
		return levelAndElementName;
	}

	public String getPictureAndUsage() {
		return pictureAndUsage;
	}
	
	public int getSeqNo() {
		return seqNo;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public void setLevelAndElementName(String levelAndElementName) {
		this.levelAndElementName = levelAndElementName;
	}

	public void setPictureAndUsage(String pictureAndUsage) {
		this.pictureAndUsage = pictureAndUsage;
	}	
	
}
