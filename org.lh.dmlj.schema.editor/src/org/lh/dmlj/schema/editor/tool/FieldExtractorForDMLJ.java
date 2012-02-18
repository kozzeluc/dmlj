package org.lh.dmlj.schema.editor.tool;

import static dmlj.core.NavigationType.NEXT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.lh.dmlj.idmsntwk.Namesyn_083;
import org.lh.dmlj.idmsntwk.Rcdsyn_079;

import dmlj.core.IDatabase;

public class FieldExtractorForDMLJ {
	private IDatabase				  dictionary;
	private Map<String, FieldForDMLJ> fields = 
		new LinkedHashMap<String, FieldForDMLJ>(); // only top level fields
	private Rcdsyn_079 				  rcdsyn_079;
	
	protected FieldExtractorForDMLJ(IDatabase dictionary, 
									Rcdsyn_079 rcdsyn_079) {
		super();
		this.dictionary = dictionary;
		this.rcdsyn_079 = rcdsyn_079;
	}
	
	protected Iterator<FieldForDMLJ> createFieldIterator() {
		Vector<FieldForDMLJ> vector = new Vector<FieldForDMLJ>();
		for (FieldForDMLJ field : fields.values()) {
			vector.addElement(field);
			vector.addAll(field.getChildrenCascaded());
		}
		List<FieldForDMLJ> result = new LinkedList<FieldForDMLJ>();
		for (Iterator<FieldForDMLJ> list = vector.iterator(); list.hasNext(); ) {
			FieldForDMLJ field = list.next();
			if (!field.isFiller()) {				
				result.add(field);
			}
		}
		return result.iterator();
	}
	
	public FieldForDMLJ getField(String name) {
		if (fields.containsKey(name)) {
			return fields.get(name);
		}
		for (FieldForDMLJ field : fields.values()) {
			if (field.isGroupField()) {
				for (FieldForDMLJ subordinate : field.getChildrenCascaded()) {
					if (subordinate.getName().equals(name)) {
						return subordinate;
					}
				}
			}
		}
		return null;
	}
	
	public List<FieldForDMLJ> getTopLevelFields() {
		return new ArrayList<FieldForDMLJ>(fields.values());
	}
	
	protected void processFields() {
		Map<String, FieldForDMLJ> noFillerFields = new HashMap<>();
		FieldForDMLJ currentGroup = null;
		FieldForDMLJ lastField = null;
		int toplevel = -1;
		for (Namesyn_083 namesyn_083 : 
			 dictionary.<Namesyn_083>walk(rcdsyn_079, "RCDSYN-NAMESYN", NEXT)) {
			
			FieldForDMLJ field = new FieldForDMLJ(dictionary, namesyn_083);
			short level = field.getLevel();
			if (toplevel == -1) {
				toplevel = level;				
			}	
			if (!field.isConditionName()) {
				if (!field.isFiller()) {
					if (noFillerFields.get(field.getName()) != null) {
						throw new RuntimeException("element name not unique: " + 
												   field.getName());
					}
					noFillerFields.put(field.getName(), field);
				}
				if (level == toplevel) {
					String key = field.getName();
					if (field.isFiller()) {
						int i = 1;
						while (fields.containsKey(key + "_" + i)) {
							i++;
						}
						key = key + "_" + i;
					}
					fields.put(key, field);
					currentGroup = null;
				}				
				if (currentGroup == null) {
					if (level != toplevel) {							
						throw new RuntimeException("logic error: level " +
												   "of element " + 
												   field.getName() + 
												   " (" + field.getLevel() + 
												   ") != toplevel in record (" + 									
												   toplevel + ")");
					}
				} else if (level > currentGroup.getLevel()) {						
					currentGroup.addChild(field);
					field.setParent(currentGroup);						
				} else {						
					currentGroup = currentGroup.getParent();
					while (currentGroup != null && 
						   level <= currentGroup.getLevel()) {
						
						currentGroup = currentGroup.getParent();
					}
					if (currentGroup == null) {
						if (field.getLevel() != toplevel) {								
							throw new RuntimeException("logic error: level " +
													   "of element " + 
													   field.getName() + " ("+ 
													   field.getLevel() + 
													   ") != toplevel in record (" + 									
													   toplevel + ")");
						}
					} else {
						currentGroup.addChild(field);
						field.setParent(currentGroup);						
					}
				}	
				if (field.isGroupField()) {
					currentGroup = field;
				}
				lastField = field;
			} else {
				if (lastField == null) {
					throw new RuntimeException("logic error: no field for " +
											   "condition-name " + 
											   field.getName());
				}
				lastField.addChild(field);
				field.setParent(lastField);
			}
		}
		for (Iterator<FieldForDMLJ> list = noFillerFields.values().iterator(); 
			 list.hasNext(); ) {
			
			FieldForDMLJ field = (FieldForDMLJ)list.next();
			field.resolveDependsOnField(noFillerFields);
		}		
	}
}