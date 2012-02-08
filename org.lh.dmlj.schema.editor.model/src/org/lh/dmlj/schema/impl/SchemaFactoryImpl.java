/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.lh.dmlj.schema.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SchemaFactoryImpl extends EFactoryImpl implements SchemaFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SchemaFactory init() {
		try {
			SchemaFactory theSchemaFactory = (SchemaFactory)EPackage.Registry.INSTANCE.getEFactory("http://org.lh.dmlj.schema/schema.ecore"); 
			if (theSchemaFactory != null) {
				return theSchemaFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SchemaFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case SchemaPackage.AREA_PROCEDURE_CALL_SPECIFICATION: return createAreaProcedureCallSpecification();
			case SchemaPackage.AREA_SPECIFICATION: return createAreaSpecification();
			case SchemaPackage.ELEMENT: return createElement();
			case SchemaPackage.INDEXED_SET_MODE_SPECIFICATION: return createIndexedSetModeSpecification();
			case SchemaPackage.KEY: return createKey();
			case SchemaPackage.KEY_ELEMENT: return createKeyElement();
			case SchemaPackage.MEMBER_ROLE: return createMemberRole();
			case SchemaPackage.OCCURS_SPECIFICATION: return createOccursSpecification();
			case SchemaPackage.OFFSET_EXPRESSION: return createOffsetExpression();
			case SchemaPackage.OWNER_ROLE: return createOwnerRole();
			case SchemaPackage.PROCEDURE: return createProcedure();
			case SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION: return createRecordProcedureCallSpecification();
			case SchemaPackage.SCHEMA: return createSchema();
			case SchemaPackage.SCHEMA_AREA: return createSchemaArea();
			case SchemaPackage.SCHEMA_RECORD: return createSchemaRecord();
			case SchemaPackage.SET: return createSet();
			case SchemaPackage.SYSTEM_OWNER: return createSystemOwner();
			case SchemaPackage.VIA_SPECIFICATION: return createViaSpecification();
			case SchemaPackage.DIAGRAM_NODE: return createDiagramNode();
			case SchemaPackage.DIAGRAM_LOCATION: return createDiagramLocation();
			case SchemaPackage.DIAGRAM_DATA: return createDiagramData();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case SchemaPackage.AREA_PROCEDURE_CALL_FUNCTION:
				return createAreaProcedureCallFunctionFromString(eDataType, initialValue);
			case SchemaPackage.DUPLICATES_OPTION:
				return createDuplicatesOptionFromString(eDataType, initialValue);
			case SchemaPackage.LOCATION_MODE:
				return createLocationModeFromString(eDataType, initialValue);
			case SchemaPackage.PROCEDURE_CALL_TIME:
				return createProcedureCallTimeFromString(eDataType, initialValue);
			case SchemaPackage.RECORD_PROCEDURE_CALL_VERB:
				return createRecordProcedureCallVerbFromString(eDataType, initialValue);
			case SchemaPackage.SET_MEMBERSHIP_OPTION:
				return createSetMembershipOptionFromString(eDataType, initialValue);
			case SchemaPackage.SET_MODE:
				return createSetModeFromString(eDataType, initialValue);
			case SchemaPackage.SET_ORDER:
				return createSetOrderFromString(eDataType, initialValue);
			case SchemaPackage.SORT_SEQUENCE:
				return createSortSequenceFromString(eDataType, initialValue);
			case SchemaPackage.STORAGE_MODE:
				return createStorageModeFromString(eDataType, initialValue);
			case SchemaPackage.USAGE:
				return createUsageFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case SchemaPackage.AREA_PROCEDURE_CALL_FUNCTION:
				return convertAreaProcedureCallFunctionToString(eDataType, instanceValue);
			case SchemaPackage.DUPLICATES_OPTION:
				return convertDuplicatesOptionToString(eDataType, instanceValue);
			case SchemaPackage.LOCATION_MODE:
				return convertLocationModeToString(eDataType, instanceValue);
			case SchemaPackage.PROCEDURE_CALL_TIME:
				return convertProcedureCallTimeToString(eDataType, instanceValue);
			case SchemaPackage.RECORD_PROCEDURE_CALL_VERB:
				return convertRecordProcedureCallVerbToString(eDataType, instanceValue);
			case SchemaPackage.SET_MEMBERSHIP_OPTION:
				return convertSetMembershipOptionToString(eDataType, instanceValue);
			case SchemaPackage.SET_MODE:
				return convertSetModeToString(eDataType, instanceValue);
			case SchemaPackage.SET_ORDER:
				return convertSetOrderToString(eDataType, instanceValue);
			case SchemaPackage.SORT_SEQUENCE:
				return convertSortSequenceToString(eDataType, instanceValue);
			case SchemaPackage.STORAGE_MODE:
				return convertStorageModeToString(eDataType, instanceValue);
			case SchemaPackage.USAGE:
				return convertUsageToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Schema createSchema() {
		SchemaImpl schema = new SchemaImpl();
		return schema;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaArea createSchemaArea() {
		SchemaAreaImpl schemaArea = new SchemaAreaImpl();
		return schemaArea;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaRecord createSchemaRecord() {
		SchemaRecordImpl schemaRecord = new SchemaRecordImpl();
		return schemaRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Set createSet() {
		SetImpl set = new SetImpl();
		return set;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Element createElement() {
		ElementImpl element = new ElementImpl();
		return element;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KeyElement createKeyElement() {
		KeyElementImpl keyElement = new KeyElementImpl();
		return keyElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MemberRole createMemberRole() {
		MemberRoleImpl memberRole = new MemberRoleImpl();
		return memberRole;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemOwner createSystemOwner() {
		SystemOwnerImpl systemOwner = new SystemOwnerImpl();
		return systemOwner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ViaSpecification createViaSpecification() {
		ViaSpecificationImpl viaSpecification = new ViaSpecificationImpl();
		return viaSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramNode createDiagramNode() {
		DiagramNodeImpl diagramNode = new DiagramNodeImpl();
		return diagramNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramLocation createDiagramLocation() {
		DiagramLocationImpl diagramLocation = new DiagramLocationImpl();
		return diagramLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramData createDiagramData() {
		DiagramDataImpl diagramData = new DiagramDataImpl();
		return diagramData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Key createKey() {
		KeyImpl key = new KeyImpl();
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AreaSpecification createAreaSpecification() {
		AreaSpecificationImpl areaSpecification = new AreaSpecificationImpl();
		return areaSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OffsetExpression createOffsetExpression() {
		OffsetExpressionImpl offsetExpression = new OffsetExpressionImpl();
		return offsetExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OwnerRole createOwnerRole() {
		OwnerRoleImpl ownerRole = new OwnerRoleImpl();
		return ownerRole;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndexedSetModeSpecification createIndexedSetModeSpecification() {
		IndexedSetModeSpecificationImpl indexedSetModeSpecification = new IndexedSetModeSpecificationImpl();
		return indexedSetModeSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AreaProcedureCallSpecification createAreaProcedureCallSpecification() {
		AreaProcedureCallSpecificationImpl areaProcedureCallSpecification = new AreaProcedureCallSpecificationImpl();
		return areaProcedureCallSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RecordProcedureCallSpecification createRecordProcedureCallSpecification() {
		RecordProcedureCallSpecificationImpl recordProcedureCallSpecification = new RecordProcedureCallSpecificationImpl();
		return recordProcedureCallSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Procedure createProcedure() {
		ProcedureImpl procedure = new ProcedureImpl();
		return procedure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OccursSpecification createOccursSpecification() {
		OccursSpecificationImpl occursSpecification = new OccursSpecificationImpl();
		return occursSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SetOrder createSetOrderFromString(EDataType eDataType, String initialValue) {
		SetOrder result = SetOrder.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSetOrderToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DuplicatesOption createDuplicatesOptionFromString(EDataType eDataType, String initialValue) {
		DuplicatesOption result = DuplicatesOption.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDuplicatesOptionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocationMode createLocationModeFromString(EDataType eDataType, String initialValue) {
		LocationMode result = LocationMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLocationModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SetMode createSetModeFromString(EDataType eDataType, String initialValue) {
		SetMode result = SetMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSetModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StorageMode createStorageModeFromString(EDataType eDataType, String initialValue) {
		StorageMode result = StorageMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertStorageModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SetMembershipOption createSetMembershipOptionFromString(EDataType eDataType, String initialValue) {
		SetMembershipOption result = SetMembershipOption.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSetMembershipOptionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SortSequence createSortSequenceFromString(EDataType eDataType, String initialValue) {
		SortSequence result = SortSequence.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSortSequenceToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcedureCallTime createProcedureCallTimeFromString(EDataType eDataType, String initialValue) {
		ProcedureCallTime result = ProcedureCallTime.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertProcedureCallTimeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AreaProcedureCallFunction createAreaProcedureCallFunctionFromString(EDataType eDataType, String initialValue) {
		AreaProcedureCallFunction result = AreaProcedureCallFunction.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAreaProcedureCallFunctionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RecordProcedureCallVerb createRecordProcedureCallVerbFromString(EDataType eDataType, String initialValue) {
		RecordProcedureCallVerb result = RecordProcedureCallVerb.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRecordProcedureCallVerbToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Usage createUsageFromString(EDataType eDataType, String initialValue) {
		Usage result = Usage.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertUsageToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaPackage getSchemaPackage() {
		return (SchemaPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SchemaPackage getPackage() {
		return SchemaPackage.eINSTANCE;
	}

} //SchemaFactoryImpl
