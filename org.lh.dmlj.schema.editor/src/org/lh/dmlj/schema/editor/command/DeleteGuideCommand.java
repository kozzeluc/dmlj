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
package org.lh.dmlj.schema.editor.command;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.Ruler;

public class DeleteGuideCommand extends Command {

	private Guide guide;
	private Ruler ruler;
	
	public DeleteGuideCommand(Ruler ruler, Guide guide) {
		super("Delete Guide");
		this.ruler = ruler;
		this.guide = guide;
	}		
	
	@Override
	public void execute() {		
		ruler.getGuides().remove(guide);				
	}
	
	@Override
	public void undo() {		
		ruler.getGuides().add(guide);		
	}	
	
}
