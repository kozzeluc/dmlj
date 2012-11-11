package org.lh.dmlj.schema.editor.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OccursSpecification;
import org.lh.dmlj.schema.OffsetExpression;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.ViaSpecification;

public class SchemaPrinter {
		
	private static List<EObject> 	    eObjects = new ArrayList<>();
	private static Map<String, EObject> map1 = new HashMap<>();
	private static Map<String, String>  map2 = new HashMap<>();
	private static Map<String, String>  map3 = new HashMap<>();
	private static Map<String, Integer> seqNoMap = new HashMap<>();

	static {
		@SuppressWarnings("unused")
		SchemaPackage schemaPackage = SchemaPackage.eINSTANCE;
	}
	
	private static void addReferencedObjects(EObject eObject) {
		if (!eObjects.contains(eObject)) {
			eObjects.add(eObject); 
		}
		for (EReference eReference : eObject.eClass().getEAllReferences()) {
			Object referencedObject = eObject.eGet(eReference);
			if (referencedObject instanceof EObject) {
				if (!eObjects.contains(referencedObject)) {
					addReferencedObjects((EObject) referencedObject);
				}
			} else if (referencedObject instanceof EList<?>) {
				EList<?> eList = (EList<?>) referencedObject;
				for (Object listObject : eList) {
					if (!eObjects.contains(listObject)) {
						addReferencedObjects((EObject) listObject);
					}
				}
			}
		}		
	}
	
	private static String createEObjectIdent(EObject eObject) {
		String p = eObject.toString();
		int i = p.indexOf(" ");
		if (i > -1) {
			return p.substring(0, i);
		} else {
			return p;
		}
	}

	private static String createReadableIdent(EObject eObject) {
		if (eObject.getClass().getInterfaces().length != 1) {
			throw new RuntimeException("logic error: class implements no or " +
									   "more than 1 interface");
		}
		String _interface = 
			eObject.getClass().getInterfaces()[0].getSimpleName();
		if (!(eObject instanceof Element) &&
			eObject.eClass().getEStructuralFeature("name") != null &&
			eObject.eClass().getEStructuralFeature("name") instanceof EAttribute) {
			
			String name = 
				(String) eObject.eGet(eObject.eClass().getEStructuralFeature("name"));
			return _interface + " name=" + name;
		} else if (eObject instanceof Element) {
			Element element = (Element) eObject;
			if (!element.getName().equals("FILLER")) {
				return _interface + " record=" + element.getRecord().getName() + 
					   " element=" + element.getName();
			} else {
				int fillerIndex = 0;
				for (Element anElement : element.getRecord().getElements()) {
					if (anElement.getName().equals("FILLER")) {
						fillerIndex += 1;
						if (anElement == element) {						
							break;
						}
					}
				}
				if (fillerIndex == 0) {
					throw new RuntimeException("logic error: cannot find FILLER");
				}
				return _interface + " record=" + element.getRecord().getName() + 
					   " FILLER offset=" + element.getOffset() +
					   " length=" + element.getLength() +
					   " (" + fillerIndex + ")";
			}
		} else if (eObject instanceof AreaSpecification) {
			AreaSpecification areaSpecification = (AreaSpecification) eObject;
			if (areaSpecification.getRecord() != null) {
				return _interface + " area=" + 
						areaSpecification.getArea().getName() +  
						" record=" + areaSpecification.getRecord().getName();
			} else {
				return _interface + " area=" + 
					   areaSpecification.getArea().getName() +  
					   " system_owner=" + 
					   areaSpecification.getSystemOwner().getSet().getName();
			}
		} else if (eObject instanceof OffsetExpression) {
			OffsetExpression offsetExpression = (OffsetExpression) eObject;
			AreaSpecification areaSpecification = 
				offsetExpression.getAreaSpecification();
			if (areaSpecification.getRecord() != null) {
				return _interface + " area=" + 
						areaSpecification.getArea().getName() +  
						" record=" + areaSpecification.getRecord().getName();
			} else {
				return _interface + " area=" + 
					   areaSpecification.getArea().getName() +  
					   " system_owner=" + 
					   areaSpecification.getSystemOwner().getSet().getName();
			}
		} else if (eObject instanceof SystemOwner) {
			SystemOwner systemOwner = (SystemOwner) eObject;
			return _interface + " set=" + systemOwner.getSet().getName();
		} else if (eObject instanceof MemberRole) {
			MemberRole memberRole = (MemberRole) eObject;
			return _interface + " set=" + memberRole.getSet().getName() + 
				   " record=" + memberRole.getRecord().getName();
		} else if (eObject instanceof ViaSpecification) {
			ViaSpecification viaSpecification = (ViaSpecification) eObject;
			return _interface + " record=" + 
				   viaSpecification.getRecord().getName();
		} else if (eObject instanceof DiagramLocation) {
			DiagramLocation diagramLocation = (DiagramLocation) eObject;
			return _interface + " eyecatcher=" + diagramLocation.getEyecatcher();
		} else if (eObject instanceof ConnectionLabel) {
			ConnectionLabel connectionLabel = (ConnectionLabel) eObject;
			return _interface + " set=" + 
				   connectionLabel.getMemberRole().getSet().getName() + 
				   " record=" + 
				   connectionLabel.getMemberRole().getRecord().getName();
		} else if (eObject instanceof IndexedSetModeSpecification) {
			IndexedSetModeSpecification indexedSetModeSpecification = 
				(IndexedSetModeSpecification) eObject;
			return _interface + " set=" + 
				   indexedSetModeSpecification.getSet().getName();
		} else if (eObject instanceof Key) {
			Key key = (Key) eObject;
			if (key.getMemberRole() != null) {
				// SORT key
				return _interface + " set=" + 
					   key.getMemberRole().getSet().getName() + " record=" + 
					   key.getMemberRole().getRecord().getName() + 
					   " (sort key)";				
			} else {	
				// CALC key
				return _interface + " record=" + key.getRecord().getName() + 
					   " (CALC key)";				
			}
		} else if (eObject instanceof ConnectionPart) {
			ConnectionPart connectionPart = (ConnectionPart) eObject;
			return _interface + " set=" + 
				   connectionPart.getMemberRole().getSet().getName() + 
				   " record=" + 
				   connectionPart.getMemberRole().getRecord().getName();
		} else if (eObject instanceof KeyElement) {
			KeyElement keyElement = (KeyElement) eObject;
			String p = keyElement.getKey().isCalcKey() ? "CALC key" : 
					   keyElement.getKey().getMemberRole().getSet().getName();
			return _interface + " record=" + 
				   keyElement.getElement().getRecord().getName() + " element=" +
				   keyElement.getElement().getName() + " (" + p + ")";
		} else if (eObject instanceof OccursSpecification) {
			OccursSpecification occursSpecification = 
				(OccursSpecification) eObject;
			return _interface + " record=" +
				   occursSpecification.getElement().getRecord().getName() + 
				   "element=" + occursSpecification.getElement().getName();
		} else if (eObject instanceof OwnerRole) {
			OwnerRole ownerRole = (OwnerRole) eObject;
			return _interface + " set=" + ownerRole.getSet().getName();
		} else if (eObject instanceof RecordProcedureCallSpecification) {
			RecordProcedureCallSpecification recordProcedureCallSpecification = 
				(RecordProcedureCallSpecification) eObject;
			return _interface + " record=" + 
				   recordProcedureCallSpecification.getRecord().getName() + 
				   " procedure=" + 
				   recordProcedureCallSpecification.getProcedure().getName() + 
				   " (" + recordProcedureCallSpecification.getCallTime() + " " + 
				   recordProcedureCallSpecification.getVerb() + ")";
		} else {
			int i = 0;
			if (seqNoMap.containsKey(_interface)) {
				i = seqNoMap.get(_interface).intValue();
			}			
			seqNoMap.put(_interface, Integer.valueOf(++i));		
			return _interface + " " + fix(i, 5);
		}
	}	

	private static void dump(String readableIdent, EObject eObject, 
							 PrintWriter out) {
		
		String p = formatReadableIdent(readableIdent);
		out.println(fix("", p.length(), '*'));
		out.println(p);
		out.println(fix("", p.length(), '*'));
				
		for (EStructuralFeature eStructuralFeature : 
			 eObject.eClass().getEAllStructuralFeatures()) {
			
			Object value = eObject.eGet(eStructuralFeature);
			if (value == null) {
				out.println(eStructuralFeature.getName() + ": [null]");			
			} else if (value instanceof EObject) {
				String objectIdent2 = createEObjectIdent((EObject) value);
				String readableIdent2 = map3.get(objectIdent2);
				out.println(eStructuralFeature.getName() + ": [" + 
							formatReadableIdent(readableIdent2) + "]");
			} else if (value instanceof EList<?>) {
				List<String> list = new ArrayList<>();
				for (Object object : (EList<?>) value) {
					if (object instanceof EObject) {
						String objectIdent2 = 
							createEObjectIdent((EObject) object);
						String readableIdent2 = map3.get(objectIdent2);
						list.add("[" + formatReadableIdent(readableIdent2) + "]");
					} else {
						list.add(object.toString());
					}
				}
				if (list.size() < 2) {
					p = list.toString();
					out.println(eStructuralFeature.getName() + ": {" + 
								p.substring(1,  p.length() - 1) + "}");
				} else {
					out.println(eStructuralFeature.getName() + ": {" + 
								list.get(0) + ",");
					for (int i = 1; i < list.size() - 1; i++) {
						out.println(fix("", eStructuralFeature.getName()
													  		  .length() + 3) + 
									list.get(i) + ",");
					}
					out.println(fix("", eStructuralFeature.getName()
													  	  .length() + 3) + 
								list.get(list.size() - 1) + "}");
				}
			} else {
				out.println(eStructuralFeature.getName() + ": " + 
							value.toString());
			}
		}		
		out.println();
	}

	private static String fix(int i, int length) {
		StringBuilder p = new StringBuilder();
		p.append(String.valueOf(i));		
		while (p.length() < length) {
			p.insert(0, " ");
		}
		return p.toString();
	}

	private static String fix(String string, int length) {
		return fix(string, length, ' ');
	}
	
	private static String fix(String string, int length, char pad) {
		StringBuilder p = new StringBuilder();
		p.append(string);		
		while (p.length() < length) {
			p.append(pad);
		}
		return p.toString();
	}	
	
	private static String formatReadableIdent(String readableIdent) {
		if (readableIdent.indexOf(" name=") > 1) {
			return readableIdent;
		}
		StringBuilder p = new StringBuilder(); 
		p.append(readableIdent);
		for (int i = p.indexOf("  "); i > -1; i= p.indexOf("  ")) {
			p.replace(i, i + 1, "");
		}
		return p.toString();
	}

	public static void main(String[] args) throws IOException {		
		
		File inputFile = new File(args[0]);
		URI uri = URI.createFileURI(inputFile.getAbsolutePath());
	
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
		   		   .getExtensionToFactoryMap()
		   		   .put("schema", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.getResource(uri, true);
		EObject root = resource.getContents().get(0);
				
		addReferencedObjects(root);	
		
		for (EObject eObject : eObjects) {
			String eObjectIdent = createEObjectIdent(eObject);
			String readableIdent = createReadableIdent(eObject);
			if (map1.containsKey(eObjectIdent)) {
				throw new RuntimeException("map1 already contains key \"" +
										   eObjectIdent + "\"");
			}
			map1.put(eObjectIdent, eObject);
			if (map2.containsKey(readableIdent)) {
				throw new RuntimeException("map2 already contains key \"" +
										   readableIdent + "\"");
			}
			map2.put(readableIdent, eObjectIdent);
			if (map3.containsKey(eObjectIdent)) {
				throw new RuntimeException("map3 already contains key \"" +
										   eObjectIdent + "\"");
			}
			map3.put(eObjectIdent, readableIdent);
		}
		List<String> readableIdents = new ArrayList<>(map2.keySet());
		Collections.sort(readableIdents);
		
		File outputFolder = new File(args[1]);
		int i = inputFile.getName().toLowerCase().indexOf(".schema");
		File outputFile = 
			new File(outputFolder, inputFile.getName().substring(0, i)+ ".txt");
		PrintWriter out = new PrintWriter(new FileWriter(outputFile));
				
		dump(map3.get(createEObjectIdent(root)), root, out);		
		for (String readableIdent : readableIdents) {
			String eObjectIdent = map2.get(readableIdent);
			EObject eObject = map1.get(eObjectIdent);
			if (eObject != root) {
				dump(readableIdent, eObject, out);
			}
		}
		out.flush();
		out.close();
		
	}

}