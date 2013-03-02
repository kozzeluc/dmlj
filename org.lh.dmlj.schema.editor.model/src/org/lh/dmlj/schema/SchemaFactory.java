/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.lh.dmlj.schema.SchemaPackage
 * @generated
 */
public interface SchemaFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SchemaFactory eINSTANCE = org.lh.dmlj.schema.impl.SchemaFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Schema</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Schema</em>'.
	 * @generated
	 */
	Schema createSchema();

	/**
	 * Returns a new object of class '<em>Area</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Area</em>'.
	 * @generated
	 */
	SchemaArea createSchemaArea();

	/**
	 * Returns a new object of class '<em>Record</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Record</em>'.
	 * @generated
	 */
	SchemaRecord createSchemaRecord();

	/**
	 * Returns a new object of class '<em>Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Set</em>'.
	 * @generated
	 */
	Set createSet();

	/**
	 * Returns a new object of class '<em>Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Element</em>'.
	 * @generated
	 */
	Element createElement();

	/**
	 * Returns a new object of class '<em>Guide</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Guide</em>'.
	 * @generated
	 */
	Guide createGuide();

	/**
	 * Returns a new object of class '<em>Key Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Key Element</em>'.
	 * @generated
	 */
	KeyElement createKeyElement();

	/**
	 * Returns a new object of class '<em>Member Role</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Member Role</em>'.
	 * @generated
	 */
	MemberRole createMemberRole();

	/**
	 * Returns a new object of class '<em>System Owner</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>System Owner</em>'.
	 * @generated
	 */
	SystemOwner createSystemOwner();

	/**
	 * Returns a new object of class '<em>Via Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Via Specification</em>'.
	 * @generated
	 */
	ViaSpecification createViaSpecification();

	/**
	 * Returns a new object of class '<em>Diagram Location</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram Location</em>'.
	 * @generated
	 */
	DiagramLocation createDiagramLocation();

	/**
	 * Returns a new object of class '<em>Diagram Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram Data</em>'.
	 * @generated
	 */
	DiagramData createDiagramData();

	/**
	 * Returns a new object of class '<em>Key</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Key</em>'.
	 * @generated
	 */
	Key createKey();

	/**
	 * Returns a new object of class '<em>Area Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Area Specification</em>'.
	 * @generated
	 */
	AreaSpecification createAreaSpecification();

	/**
	 * Returns a new object of class '<em>Connection Part</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Connection Part</em>'.
	 * @generated
	 */
	ConnectionPart createConnectionPart();

	/**
	 * Returns a new object of class '<em>Connection Label</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Connection Label</em>'.
	 * @generated
	 */
	ConnectionLabel createConnectionLabel();

	/**
	 * Returns a new object of class '<em>Connector</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Connector</em>'.
	 * @generated
	 */
	Connector createConnector();

	/**
	 * Returns a new object of class '<em>Offset Expression</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Offset Expression</em>'.
	 * @generated
	 */
	OffsetExpression createOffsetExpression();

	/**
	 * Returns a new object of class '<em>Owner Role</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Owner Role</em>'.
	 * @generated
	 */
	OwnerRole createOwnerRole();

	/**
	 * Returns a new object of class '<em>Indexed Set Mode Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Indexed Set Mode Specification</em>'.
	 * @generated
	 */
	IndexedSetModeSpecification createIndexedSetModeSpecification();

	/**
	 * Returns a new object of class '<em>Index Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Index Element</em>'.
	 * @generated
	 */
	IndexElement createIndexElement();

	/**
	 * Returns a new object of class '<em>Area Procedure Call Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Area Procedure Call Specification</em>'.
	 * @generated
	 */
	AreaProcedureCallSpecification createAreaProcedureCallSpecification();

	/**
	 * Returns a new object of class '<em>Record Procedure Call Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Record Procedure Call Specification</em>'.
	 * @generated
	 */
	RecordProcedureCallSpecification createRecordProcedureCallSpecification();

	/**
	 * Returns a new object of class '<em>Ruler</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ruler</em>'.
	 * @generated
	 */
	Ruler createRuler();

	/**
	 * Returns a new object of class '<em>Procedure</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Procedure</em>'.
	 * @generated
	 */
	Procedure createProcedure();

	/**
	 * Returns a new object of class '<em>Occurs Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Occurs Specification</em>'.
	 * @generated
	 */
	OccursSpecification createOccursSpecification();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SchemaPackage getSchemaPackage();

} //SchemaFactory
