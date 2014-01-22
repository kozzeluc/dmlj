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
package org.lh.dmlj.schema.util;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.lh.dmlj.schema.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.lh.dmlj.schema.SchemaPackage
 * @generated
 */
public class SchemaSwitch<T1> extends Switch<T1> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static SchemaPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaSwitch() {
		if (modelPackage == null) {
			modelPackage = SchemaPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T1 doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION: {
				AreaProcedureCallSpecification areaProcedureCallSpecification = (AreaProcedureCallSpecification)theEObject;
				T1 result = caseAreaProcedureCallSpecification(areaProcedureCallSpecification);
				if (result == null) result = caseProcedureCallSpecification(areaProcedureCallSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.AREA_SPECIFICATION: {
				AreaSpecification areaSpecification = (AreaSpecification)theEObject;
				T1 result = caseAreaSpecification(areaSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.CONNECTION_PART: {
				ConnectionPart connectionPart = (ConnectionPart)theEObject;
				T1 result = caseConnectionPart(connectionPart);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.CONNECTION_LABEL: {
				ConnectionLabel connectionLabel = (ConnectionLabel)theEObject;
				T1 result = caseConnectionLabel(connectionLabel);
				if (result == null) result = caseDiagramNode(connectionLabel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.CONNECTOR: {
				Connector connector = (Connector)theEObject;
				T1 result = caseConnector(connector);
				if (result == null) result = caseDiagramNode(connector);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.DIAGRAM_DATA: {
				DiagramData diagramData = (DiagramData)theEObject;
				T1 result = caseDiagramData(diagramData);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.DIAGRAM_LABEL: {
				DiagramLabel diagramLabel = (DiagramLabel)theEObject;
				T1 result = caseDiagramLabel(diagramLabel);
				if (result == null) result = caseResizableDiagramNode(diagramLabel);
				if (result == null) result = caseINodeTextProvider(diagramLabel);
				if (result == null) result = caseDiagramNode(diagramLabel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.DIAGRAM_LOCATION: {
				DiagramLocation diagramLocation = (DiagramLocation)theEObject;
				T1 result = caseDiagramLocation(diagramLocation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.DIAGRAM_NODE: {
				DiagramNode diagramNode = (DiagramNode)theEObject;
				T1 result = caseDiagramNode(diagramNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.ELEMENT: {
				Element element = (Element)theEObject;
				T1 result = caseElement(element);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.GUIDE: {
				Guide guide = (Guide)theEObject;
				T1 result = caseGuide(guide);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION: {
				IndexedSetModeSpecification indexedSetModeSpecification = (IndexedSetModeSpecification)theEObject;
				T1 result = caseIndexedSetModeSpecification(indexedSetModeSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.INDEX_ELEMENT: {
				IndexElement indexElement = (IndexElement)theEObject;
				T1 result = caseIndexElement(indexElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.INODE_TEXT_PROVIDER: {
				INodeTextProvider<?> iNodeTextProvider = (INodeTextProvider<?>)theEObject;
				T1 result = caseINodeTextProvider(iNodeTextProvider);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.KEY: {
				Key key = (Key)theEObject;
				T1 result = caseKey(key);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.KEY_ELEMENT: {
				KeyElement keyElement = (KeyElement)theEObject;
				T1 result = caseKeyElement(keyElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.MEMBER_ROLE: {
				MemberRole memberRole = (MemberRole)theEObject;
				T1 result = caseMemberRole(memberRole);
				if (result == null) result = caseRole(memberRole);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.OCCURS_SPECIFICATION: {
				OccursSpecification occursSpecification = (OccursSpecification)theEObject;
				T1 result = caseOccursSpecification(occursSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.OFFSET_EXPRESSION: {
				OffsetExpression offsetExpression = (OffsetExpression)theEObject;
				T1 result = caseOffsetExpression(offsetExpression);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.OWNER_ROLE: {
				OwnerRole ownerRole = (OwnerRole)theEObject;
				T1 result = caseOwnerRole(ownerRole);
				if (result == null) result = caseRole(ownerRole);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.PROCEDURE: {
				Procedure procedure = (Procedure)theEObject;
				T1 result = caseProcedure(procedure);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.PROCEDURE_CALL_SPECIFICATION: {
				ProcedureCallSpecification procedureCallSpecification = (ProcedureCallSpecification)theEObject;
				T1 result = caseProcedureCallSpecification(procedureCallSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION: {
				RecordProcedureCallSpecification recordProcedureCallSpecification = (RecordProcedureCallSpecification)theEObject;
				T1 result = caseRecordProcedureCallSpecification(recordProcedureCallSpecification);
				if (result == null) result = caseProcedureCallSpecification(recordProcedureCallSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.RESIZABLE_DIAGRAM_NODE: {
				ResizableDiagramNode resizableDiagramNode = (ResizableDiagramNode)theEObject;
				T1 result = caseResizableDiagramNode(resizableDiagramNode);
				if (result == null) result = caseDiagramNode(resizableDiagramNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.ROLE: {
				Role role = (Role)theEObject;
				T1 result = caseRole(role);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.RULER: {
				Ruler ruler = (Ruler)theEObject;
				T1 result = caseRuler(ruler);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SCHEMA: {
				Schema schema = (Schema)theEObject;
				T1 result = caseSchema(schema);
				if (result == null) result = caseINodeTextProvider(schema);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SCHEMA_AREA: {
				SchemaArea schemaArea = (SchemaArea)theEObject;
				T1 result = caseSchemaArea(schemaArea);
				if (result == null) result = caseINodeTextProvider(schemaArea);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SCHEMA_RECORD: {
				SchemaRecord schemaRecord = (SchemaRecord)theEObject;
				T1 result = caseSchemaRecord(schemaRecord);
				if (result == null) result = caseDiagramNode(schemaRecord);
				if (result == null) result = caseINodeTextProvider(schemaRecord);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SET: {
				Set set = (Set)theEObject;
				T1 result = caseSet(set);
				if (result == null) result = caseINodeTextProvider(set);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SYSTEM_OWNER: {
				SystemOwner systemOwner = (SystemOwner)theEObject;
				T1 result = caseSystemOwner(systemOwner);
				if (result == null) result = caseDiagramNode(systemOwner);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.VIA_SPECIFICATION: {
				ViaSpecification viaSpecification = (ViaSpecification)theEObject;
				T1 result = caseViaSpecification(viaSpecification);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Schema</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Schema</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSchema(Schema object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Area</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Area</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSchemaArea(SchemaArea object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Record</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Record</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSchemaRecord(SchemaRecord object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Set</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSet(Set object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Role</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Role</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseRole(Role object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ruler</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ruler</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseRuler(Ruler object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseElement(Element object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Guide</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Guide</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseGuide(Guide object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Key Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Key Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseKeyElement(KeyElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Member Role</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Member Role</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseMemberRole(MemberRole object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>System Owner</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>System Owner</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSystemOwner(SystemOwner object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Via Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Via Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseViaSpecification(ViaSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDiagramNode(DiagramNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Location</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Location</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDiagramLocation(DiagramLocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Data</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Data</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDiagramData(DiagramData object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Diagram Label</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagram Label</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDiagramLabel(DiagramLabel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Key</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Key</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseKey(Key object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Area Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Area Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseAreaSpecification(AreaSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Connection Part</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Connection Part</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseConnectionPart(ConnectionPart object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Connection Label</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Connection Label</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseConnectionLabel(ConnectionLabel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Connector</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Connector</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseConnector(Connector object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Offset Expression</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Offset Expression</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseOffsetExpression(OffsetExpression object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Owner Role</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Owner Role</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseOwnerRole(OwnerRole object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Indexed Set Mode Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Indexed Set Mode Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseIndexedSetModeSpecification(IndexedSetModeSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Index Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Index Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseIndexElement(IndexElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>INode Text Provider</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>INode Text Provider</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T> T1 caseINodeTextProvider(INodeTextProvider<T> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Area Procedure Call Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Area Procedure Call Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseAreaProcedureCallSpecification(AreaProcedureCallSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Record Procedure Call Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Record Procedure Call Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseRecordProcedureCallSpecification(RecordProcedureCallSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resizable Diagram Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resizable Diagram Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseResizableDiagramNode(ResizableDiagramNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Procedure</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Procedure</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseProcedure(Procedure object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Procedure Call Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Procedure Call Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseProcedureCallSpecification(ProcedureCallSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Occurs Specification</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Occurs Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseOccursSpecification(OccursSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T1 defaultCase(EObject object) {
		return null;
	}

} //SchemaSwitch
