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
package org.lh.dmlj.schema.editor.property.section;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.Plugin;

public class DiagramLabelSection extends AbstractAttributesBasedPropertiesSection<DiagramLabel> {

	public DiagramLabelSection() {
		super(Plugin.getDefault());
	}

	@Override
	public List<EAttribute> getAttributes() {
		List<EAttribute> attributes = new ArrayList<>();
		attributes.add(SchemaPackage.eINSTANCE.getDiagramLabel_Description());
		return attributes;
	}
	
	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getDiagramLabel_Description()) {			
			return target;
		} else {
			return super.getEditableObject(attribute);
		}
	}

	@Override
	public DiagramLabel getTarget(Object object) {
		Assert.isTrue(modelObject instanceof DiagramLabel, "not a DiagramLabel");
		return (DiagramLabel) modelObject;
	}

}
