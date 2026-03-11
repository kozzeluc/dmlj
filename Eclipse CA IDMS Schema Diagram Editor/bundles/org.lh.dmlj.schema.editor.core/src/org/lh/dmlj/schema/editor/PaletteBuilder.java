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
package org.lh.dmlj.schema.editor;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.palette.IChainedSetPlaceHolder;
import org.lh.dmlj.schema.editor.palette.IIndexedSetPlaceHolder;
import org.lh.dmlj.schema.editor.palette.IMultipleMemberSetPlaceHolder;

public class PaletteBuilder {
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaletteRoot build() {
		var palette = new PaletteRoot();
        
        // selection tool
		var tool = new PanningSelectionToolEntry();
		
		// label creation tool
		var label16 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/label16.GIF"));
		var label24 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/label24.GIF"));
        var labelCreationTool = new CombinedTemplateCreationEntry("Label", "Add diagram label", 
        			new SimpleFactory(DiagramLabel.class), label16, label24);
	    
        // record creation tool
        var record16 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/record16.gif"));
        var record24 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/record24.gif"));
        var recordCreationTool = new CombinedTemplateCreationEntry("Record", "Add record", 
        			new SimpleFactory(SchemaRecord.class), record16,record24);
        recordCreationTool.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        
        // chained set creation tool
        var chainedSet16 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/chainedSet16.gif"));
        var chainedSet24 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/chainedSet24.gif"));
        var chainedSetCreationTool = new ConnectionCreationToolEntry("Chained Set", "Add chained set", 
        			new SimpleFactory(IChainedSetPlaceHolder.class), chainedSet16, chainedSet24);
        chainedSetCreationTool.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);        
        
        // multiple-member set tool
        var multipleMemberSet16 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/multipleMemberSet16.gif"));
        var multipleMemberSet24 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/multipleMemberSet24.gif"));
        var multipleMemberSetCreationTool = new ConnectionCreationToolEntry("Multiple-member Set", 
        			"Add member record type to chained set", 
        			new SimpleFactory(IMultipleMemberSetPlaceHolder.class), 
        			multipleMemberSet16, multipleMemberSet24);
        multipleMemberSetCreationTool.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);        
        
        // indexed set creation tool
        var indexedSet16 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/indexedSet16.gif"));
        var indexedSet24 =ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/indexedSet24.gif"));
        var indexedSetCreationTool = new ConnectionCreationToolEntry("Indexed Set", "Add user owned indexed set", 
        			new SimpleFactory(IIndexedSetPlaceHolder.class), indexedSet16, indexedSet24);
        indexedSetCreationTool.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);
        
        // index creation tool
        var index16 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/index16.gif"));
        var index24 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/index24.gif"));
        var indexCreationTool = new CombinedTemplateCreationEntry("Index", "Add index to record", 
        			new SimpleFactory(SystemOwner.class), index16, index24);
        
        // VSAM index creation tool
        var vsamIndex16 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/vsamIndex16.gif"));
        var vsamIndex24 =ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/vsamIndex24.gif"));
        var vsamIndexCreationTool = new CombinedTemplateCreationEntry("VSAM Index", "Add VSAM index to record", 
        			new SimpleFactory(VsamIndex.class), vsamIndex16, vsamIndex24);
        vsamIndexCreationTool.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED, true);                
        
        // connector creation tool
        var connector16 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/connector16.GIF"));
        var connector24 = ImageDescriptor.createFromImage(Plugin.getDefault().getImage("icons/connector24.GIF"));
        var connectorCreationTool = new CombinedTemplateCreationEntry("Connector", "Add connectors to connection", 
        			new SimpleFactory(Connector.class), connector16, connector24);
        
        // Tools toolbar
        var toolbar = new PaletteToolbar("Tools");
        toolbar.add(tool);
        toolbar.add(new MarqueeToolEntry());
        palette.add(toolbar);
        
        // General drawer
        var createGeneralItemsDrawer = new PaletteDrawer("General");
        createGeneralItemsDrawer.add(labelCreationTool);
        palette.add(createGeneralItemsDrawer);
        
        // Records drawer
        var createRecordItemsDrawer = new PaletteDrawer("Records");
        createRecordItemsDrawer.add(recordCreationTool);
        palette.add(createRecordItemsDrawer);
        
        // Sets drawer
        var createSetItemsDrawer = new PaletteDrawer("Sets");
        createSetItemsDrawer.add(chainedSetCreationTool);
        createSetItemsDrawer.add(multipleMemberSetCreationTool);
        createSetItemsDrawer.add(indexedSetCreationTool);
        createSetItemsDrawer.add(indexCreationTool);
        createSetItemsDrawer.add(vsamIndexCreationTool);
        createSetItemsDrawer.add(connectorCreationTool);
        palette.add(createSetItemsDrawer);
        
        // the selection tool is the default entry
        palette.setDefaultEntry(tool);
       
        return palette;
	}

}
