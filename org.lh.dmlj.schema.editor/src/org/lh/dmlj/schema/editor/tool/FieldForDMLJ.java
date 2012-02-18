package org.lh.dmlj.schema.editor.tool;

import static dmlj.core.NavigationType.OWNER;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.lh.dmlj.idmsntwk.Namesyn_083;
import org.lh.dmlj.idmsntwk.Sdr_042;

import dmlj.core.IDatabase;
import dmlj.core.type.Type;

public class FieldForDMLJ implements TmpFieldInterface {	
	private List<FieldForDMLJ> children = new LinkedList<FieldForDMLJ>();
	private FieldForDMLJ	   dependsOnField = null;	
	private Namesyn_083 	   namesyn_083 = null;
	private FieldForDMLJ       parent = null;
	private Sdr_042			   sdr_042 = null;
	private Type			   type = null;	
	
	public static String swapPrimitiveType(String type) {
		if (type.equals("Boolean")) {
			return "boolean";
		} else if (type.equals("Double")) {
			return "double";
		} else if (type.equals("Integer")) {
			return "int";
		} else if (type.equals("Long")) {
			return "long";
		} else if (type.equals("Short")) {
			return "short";
		} else {
			return type;
		}
	}
	
	public static String swapWrapperClass(String type) {
		if (type.equals("boolean")) {
			return "Boolean";
		} else if (type.equals("double")) {
			return "Double";
		} else if (type.equals("int")) {
			return "Integer";
		} else if (type.equals("long")) {
			return "Long";
		} else if (type.equals("short")) {
			return "Short";
		} else {
			return type;
		}
	}	
	
	public FieldForDMLJ(IDatabase dictionary, Namesyn_083 namesyn_083) {		
		super();
		this.namesyn_083 = namesyn_083;
		sdr_042 = dictionary.find(namesyn_083, "SDR-NAMESYN", OWNER);
		// set the type...
		type = Type.forUsage(sdr_042.getUse_042());						
	}
	
	public void addChild(FieldForDMLJ field) {
		children.add(field);
	}
	
	private short getCalculatedLength() {		
		if (!isGroupField()) {
			short i = sdr_042.getLength_042();			
			if (type == Type.BIT) {
				i /= 8;
				if (sdr_042.getDrBoff_042() > 0) {
					i++;
				}
			} else if (type == Type.SHORT_POINT) {
				i = 4;
			}
			return i;
		} else {			
			short i = 0;		
			for (Iterator<?> list = children.iterator(); list.hasNext(); ) {
				FieldForDMLJ field = (FieldForDMLJ)list.next();
				if (!field.isInRedefines()) {
					int j = field.sdr_042.getOcc_042();
					if (j == 0) {
						j = 1;
					}
					i += field.getCalculatedLength() * j;
				}
			}
			return i;
		}
	}
	
	/*private short getCalculatedOffset() {
		if (isConditionName()) {
			return parent.getCalculatedOffset();
		} else {
			short offset = (short)(namesyn_083.getDrLpos_083() - 1);		
			FieldForDMLJ current = parent;
			while (current != null) {			
				offset = (short)(parent.getCalculatedOffset() + 
								 namesyn_083.getDrLpos_083() - 1);			
				current = current.parent;
			}		
			return offset;
		} 
	}*/	
	
	public Collection<FieldForDMLJ> getChildren() {
		return children;
	}
	
	public Collection<FieldForDMLJ> getChildrenCascaded() {
		Vector<FieldForDMLJ> vector = new Vector<FieldForDMLJ>();
		for (Iterator<?> list = children.iterator(); list.hasNext(); ) {
			FieldForDMLJ field = (FieldForDMLJ)list.next();
			vector.addElement(field);
			vector.addAll(field.getChildrenCascaded());
		}
		return vector;
	}
	
	public FieldForDMLJ getDependsOnField() {
		return dependsOnField;
	}
	
	public short getLength() {
		return getCalculatedLength();
	}
	
	public short getLevel() {
		return sdr_042.getDrLvl_042();
	}	
	
	public String getName() {
		return namesyn_083.getSynName_083().trim();
	}
	
	public Namesyn_083 getNamesyn_083() {
		return namesyn_083;
	}
	
	public short getOccurrenceCount() {
		short i = sdr_042.getOcc_042();
		if (i > 0) {
			return i;
		}
		return 1;
	}
	
	/*public short getOffset() {
		short offset = (short)(namesyn_083.getDrLpos_083() - 1);		
		if (sdr_042.getOccLvl_042() > 0) {
			FieldForDMLJ current = parent;
			while (current != null) {
				if (current.namesyn_083.getDrLpos_083() > 
				    namesyn_083.getDrLpos_083()) {
					
					offset = getCalculatedOffset();
					current = null;
				} else {
					current = current.parent;
				}
			}
		}
		return offset;
	}*/
	
	public FieldForDMLJ getParent() {
		return parent;
	}
	
	public String getPicture() {		
		if (!isGroupField() && !isConditionName() &&
			sdr_042.getUse_042() != 2) {	// COMPUTATIONAL-1
			
			return sdr_042.getPic_042().trim();
		} else {
			return null;
		}
	}
	
	public String getRedefinedFieldName() {
		if (isInRedefines()) {
			return namesyn_083.getRdfNam_083().trim();
		} else {
			return null;
		}
	}
	
	public Sdr_042 getSdr_042() {
		return sdr_042;
	}
	
	public boolean isConditionName() {
		return getLevel() == 88;
	}
	
	public boolean isFiller() {
		return getName().trim().equalsIgnoreCase("FILLER");
	}
	
	public boolean isGroupField() {
		// remark: this method probably isn't 100% correct
		return sdr_042.getPicLgth_042() == -1 && !isConditionName() &&
			   type != Type.SHORT_POINT;
	}
	
	public boolean isInRedefines() {		
		//return sdr_042.getRdfLvl_042() > 0;
		return !namesyn_083.getRdfNam_083().trim().equals("");
	}
	
	public void resolveDependsOnField(Map<String, FieldForDMLJ> fields) {
		String dependsOn = namesyn_083.getDependOn_083().trim();
		if (!dependsOn.equals("")) {
			dependsOnField = fields.get(dependsOn);
			if (dependsOnField == null) {
				throw new RuntimeException("errors while resolving occurs " + 
										   "depending on for element " + 
										   getName() + 
										   " - cannot find element " + 
										   dependsOn);
			}			
		}
	}
	
	public void setParent(FieldForDMLJ newValue) {
		parent = newValue;
	}
}