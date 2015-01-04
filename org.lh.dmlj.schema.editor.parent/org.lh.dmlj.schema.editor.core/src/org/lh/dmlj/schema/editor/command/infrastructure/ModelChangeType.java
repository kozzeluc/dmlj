/**
 * Copyright (C) 2015  Luc Hermans
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
package org.lh.dmlj.schema.editor.command.infrastructure;

public enum ModelChangeType {

    ADD_BENDPOINT,
	ADD_CONNECTORS,
	ADD_DIAGRAM_LABEL,
	ADD_GUIDE,
    ADD_MEMBER_TO_SET,
    ADD_RECORD,
    ADD_SYSTEM_OWNED_SET,
    ADD_USER_OWNED_SET,
    
    ADD_OR_REMOVE_SET_POINTERS,
    
    CHANGE_AREA_SPECIFICATION,
    CHANGE_CALCKEY,
    CHANGE_LOCATION_MODE,
    CHANGE_SET_ORDER,
    CHANGE_SORTKEYS,
    CHANGE_VIA_SPECIFICATION,
	
    DELETE_BENDPOINT,
    DELETE_CONNECTORS,
	DELETE_DIAGRAM_LABEL, 
	DELETE_GUIDE,
	DELETE_RECORD,				// not yet hooked
	DELETE_SYSTEM_OWNED_SET,
	DELETE_USER_OWNED_SET,
	
	MOVE_BENDPOINT, 
	MOVE_CONNECTOR, 
	MOVE_DIAGRAM_LABEL, 
	MOVE_ENDPOINT, 
	MOVE_GUIDE, 
	MOVE_INDEX,
	MOVE_RECORD,
	MOVE_SET_OR_INDEX_LABEL,
	
	REMOVE_MEMBER_FROM_SET,
	
	RESIZE_DIAGRAM_LABEL,
	
	SET_PROPERTY,
	
	SWAP_RECORD_ELEMENTS, 
	
	ZOOM_IN,
	ZOOM_OUT
	
}
