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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.lh.dmlj.schema.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.lh.dmlj.schema.SchemaPackage
 * @generated
 */
public class SchemaAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static SchemaPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = SchemaPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SchemaSwitch<Adapter> modelSwitch =
		new SchemaSwitch<Adapter>() {
			@Override
			public Adapter caseAreaProcedureCallSpecification(AreaProcedureCallSpecification object) {
				return createAreaProcedureCallSpecificationAdapter();
			}
			@Override
			public Adapter caseAreaSpecification(AreaSpecification object) {
				return createAreaSpecificationAdapter();
			}
			@Override
			public Adapter caseConnectionPart(ConnectionPart object) {
				return createConnectionPartAdapter();
			}
			@Override
			public Adapter caseConnectionLabel(ConnectionLabel object) {
				return createConnectionLabelAdapter();
			}
			@Override
			public Adapter caseConnector(Connector object) {
				return createConnectorAdapter();
			}
			@Override
			public Adapter caseDiagramData(DiagramData object) {
				return createDiagramDataAdapter();
			}
			@Override
			public Adapter caseDiagramLabel(DiagramLabel object) {
				return createDiagramLabelAdapter();
			}
			@Override
			public Adapter caseDiagramLocation(DiagramLocation object) {
				return createDiagramLocationAdapter();
			}
			@Override
			public Adapter caseDiagramNode(DiagramNode object) {
				return createDiagramNodeAdapter();
			}
			@Override
			public Adapter caseElement(Element object) {
				return createElementAdapter();
			}
			@Override
			public Adapter caseGuide(Guide object) {
				return createGuideAdapter();
			}
			@Override
			public Adapter caseIndexedSetModeSpecification(IndexedSetModeSpecification object) {
				return createIndexedSetModeSpecificationAdapter();
			}
			@Override
			public Adapter caseIndexElement(IndexElement object) {
				return createIndexElementAdapter();
			}
			@Override
			public <T> Adapter caseINodeTextProvider(INodeTextProvider<T> object) {
				return createINodeTextProviderAdapter();
			}
			@Override
			public Adapter caseKey(Key object) {
				return createKeyAdapter();
			}
			@Override
			public Adapter caseKeyElement(KeyElement object) {
				return createKeyElementAdapter();
			}
			@Override
			public Adapter caseMemberRole(MemberRole object) {
				return createMemberRoleAdapter();
			}
			@Override
			public Adapter caseOccursSpecification(OccursSpecification object) {
				return createOccursSpecificationAdapter();
			}
			@Override
			public Adapter caseOffsetExpression(OffsetExpression object) {
				return createOffsetExpressionAdapter();
			}
			@Override
			public Adapter caseOwnerRole(OwnerRole object) {
				return createOwnerRoleAdapter();
			}
			@Override
			public Adapter caseProcedure(Procedure object) {
				return createProcedureAdapter();
			}
			@Override
			public Adapter caseProcedureCallSpecification(ProcedureCallSpecification object) {
				return createProcedureCallSpecificationAdapter();
			}
			@Override
			public Adapter caseRecordProcedureCallSpecification(RecordProcedureCallSpecification object) {
				return createRecordProcedureCallSpecificationAdapter();
			}
			@Override
			public Adapter caseResizableDiagramNode(ResizableDiagramNode object) {
				return createResizableDiagramNodeAdapter();
			}
			@Override
			public Adapter caseRole(Role object) {
				return createRoleAdapter();
			}
			@Override
			public Adapter caseRuler(Ruler object) {
				return createRulerAdapter();
			}
			@Override
			public Adapter caseSchema(Schema object) {
				return createSchemaAdapter();
			}
			@Override
			public Adapter caseSchemaArea(SchemaArea object) {
				return createSchemaAreaAdapter();
			}
			@Override
			public Adapter caseSchemaRecord(SchemaRecord object) {
				return createSchemaRecordAdapter();
			}
			@Override
			public Adapter caseSet(Set object) {
				return createSetAdapter();
			}
			@Override
			public Adapter caseSystemOwner(SystemOwner object) {
				return createSystemOwnerAdapter();
			}
			@Override
			public Adapter caseViaSpecification(ViaSpecification object) {
				return createViaSpecificationAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.Schema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.Schema
	 * @generated
	 */
	public Adapter createSchemaAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.SchemaArea <em>Area</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.SchemaArea
	 * @generated
	 */
	public Adapter createSchemaAreaAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.SchemaRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.SchemaRecord
	 * @generated
	 */
	public Adapter createSchemaRecordAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.Set <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.Set
	 * @generated
	 */
	public Adapter createSetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.Role <em>Role</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.Role
	 * @generated
	 */
	public Adapter createRoleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.Ruler <em>Ruler</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.Ruler
	 * @generated
	 */
	public Adapter createRulerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.Element <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.Element
	 * @generated
	 */
	public Adapter createElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.Guide <em>Guide</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.Guide
	 * @generated
	 */
	public Adapter createGuideAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.KeyElement <em>Key Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.KeyElement
	 * @generated
	 */
	public Adapter createKeyElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.MemberRole <em>Member Role</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.MemberRole
	 * @generated
	 */
	public Adapter createMemberRoleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.SystemOwner <em>System Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.SystemOwner
	 * @generated
	 */
	public Adapter createSystemOwnerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.ViaSpecification <em>Via Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.ViaSpecification
	 * @generated
	 */
	public Adapter createViaSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.DiagramNode <em>Diagram Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.DiagramNode
	 * @generated
	 */
	public Adapter createDiagramNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.DiagramLocation <em>Diagram Location</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.DiagramLocation
	 * @generated
	 */
	public Adapter createDiagramLocationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.DiagramData <em>Diagram Data</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.DiagramData
	 * @generated
	 */
	public Adapter createDiagramDataAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.DiagramLabel <em>Diagram Label</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.DiagramLabel
	 * @generated
	 */
	public Adapter createDiagramLabelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.Key <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.Key
	 * @generated
	 */
	public Adapter createKeyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.AreaSpecification <em>Area Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.AreaSpecification
	 * @generated
	 */
	public Adapter createAreaSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.ConnectionPart <em>Connection Part</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.ConnectionPart
	 * @generated
	 */
	public Adapter createConnectionPartAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.ConnectionLabel <em>Connection Label</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.ConnectionLabel
	 * @generated
	 */
	public Adapter createConnectionLabelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.Connector <em>Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.Connector
	 * @generated
	 */
	public Adapter createConnectorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.OffsetExpression <em>Offset Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.OffsetExpression
	 * @generated
	 */
	public Adapter createOffsetExpressionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.OwnerRole <em>Owner Role</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.OwnerRole
	 * @generated
	 */
	public Adapter createOwnerRoleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.IndexedSetModeSpecification <em>Indexed Set Mode Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.IndexedSetModeSpecification
	 * @generated
	 */
	public Adapter createIndexedSetModeSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.IndexElement <em>Index Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.IndexElement
	 * @generated
	 */
	public Adapter createIndexElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.INodeTextProvider <em>INode Text Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.INodeTextProvider
	 * @generated
	 */
	public Adapter createINodeTextProviderAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.AreaProcedureCallSpecification <em>Area Procedure Call Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.AreaProcedureCallSpecification
	 * @generated
	 */
	public Adapter createAreaProcedureCallSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.RecordProcedureCallSpecification <em>Record Procedure Call Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.RecordProcedureCallSpecification
	 * @generated
	 */
	public Adapter createRecordProcedureCallSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.ResizableDiagramNode <em>Resizable Diagram Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.ResizableDiagramNode
	 * @generated
	 */
	public Adapter createResizableDiagramNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.Procedure <em>Procedure</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.Procedure
	 * @generated
	 */
	public Adapter createProcedureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.ProcedureCallSpecification <em>Procedure Call Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.ProcedureCallSpecification
	 * @generated
	 */
	public Adapter createProcedureCallSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.lh.dmlj.schema.OccursSpecification <em>Occurs Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.lh.dmlj.schema.OccursSpecification
	 * @generated
	 */
	public Adapter createOccursSpecificationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //SchemaAdapterFactory
