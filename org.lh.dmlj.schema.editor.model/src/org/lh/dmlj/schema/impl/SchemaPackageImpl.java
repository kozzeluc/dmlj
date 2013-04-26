/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.IndexElement;
import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LabelAlignment;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OccursSpecification;
import org.lh.dmlj.schema.OffsetExpression;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.Role;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.RulerType;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.ViaSpecification;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SchemaPackageImpl extends EPackageImpl implements SchemaPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass schemaEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass schemaAreaEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass schemaRecordEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass setEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass roleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass rulerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass elementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass guideEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass keyElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass memberRoleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass systemOwnerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass viaSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramLocationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass keyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass areaSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass connectionPartEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass connectionLabelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass connectorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass offsetExpressionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ownerRoleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indexedSetModeSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indexElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass areaProcedureCallSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass recordProcedureCallSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass procedureEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass occursSpecificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum setOrderEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum duplicatesOptionEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum labelAlignmentEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum locationModeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum setModeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum storageModeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum setMembershipOptionEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum sortSequenceEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum procedureCallTimeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum areaProcedureCallFunctionEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum recordProcedureCallVerbEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum rulerTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum usageEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.lh.dmlj.schema.SchemaPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SchemaPackageImpl() {
		super(eNS_URI, SchemaFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link SchemaPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SchemaPackage init() {
		if (isInited) return (SchemaPackage)EPackage.Registry.INSTANCE.getEPackage(SchemaPackage.eNS_URI);

		// Obtain or create and register package
		SchemaPackageImpl theSchemaPackage = (SchemaPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SchemaPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SchemaPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theSchemaPackage.createPackageContents();

		// Initialize created meta-data
		theSchemaPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSchemaPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SchemaPackage.eNS_URI, theSchemaPackage);
		return theSchemaPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSchema() {
		return schemaEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchema_Name() {
		return (EAttribute)schemaEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchema_Version() {
		return (EAttribute)schemaEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchema_Description() {
		return (EAttribute)schemaEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchema_MemoDate() {
		return (EAttribute)schemaEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchema_Areas() {
		return (EReference)schemaEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchema_Comments() {
		return (EAttribute)schemaEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchema_Records() {
		return (EReference)schemaEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchema_Sets() {
		return (EReference)schemaEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchema_DiagramData() {
		return (EReference)schemaEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchema_Procedures() {
		return (EReference)schemaEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSchemaArea() {
		return schemaAreaEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaArea_Name() {
		return (EAttribute)schemaAreaEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaArea_Schema() {
		return (EReference)schemaAreaEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaArea_Indexes() {
		return (EReference)schemaAreaEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaArea_Procedures() {
		return (EReference)schemaAreaEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaArea_AreaSpecifications() {
		return (EReference)schemaAreaEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaArea_Records() {
		return (EReference)schemaAreaEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSchemaRecord() {
		return schemaRecordEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_BaseName() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_BaseVersion() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_Name() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_Id() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_ControlLength() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_DataLength() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_StorageMode() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_LocationMode() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_MinimumRootLength() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_MinimumFragmentLength() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_PrefixLength() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_Fragmented() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaRecord_CalcKey() {
		return (EReference)schemaRecordEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaRecord_Schema() {
		return (EReference)schemaRecordEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_SynonymName() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchemaRecord_SynonymVersion() {
		return (EAttribute)schemaRecordEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaRecord_ViaSpecification() {
		return (EReference)schemaRecordEClass.getEStructuralFeatures().get(24);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaRecord_OwnerRoles() {
		return (EReference)schemaRecordEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaRecord_MemberRoles() {
		return (EReference)schemaRecordEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaRecord_RootElements() {
		return (EReference)schemaRecordEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaRecord_Procedures() {
		return (EReference)schemaRecordEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaRecord_Keys() {
		return (EReference)schemaRecordEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaRecord_AreaSpecification() {
		return (EReference)schemaRecordEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaRecord_Elements() {
		return (EReference)schemaRecordEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchemaRecord_Roles() {
		return (EReference)schemaRecordEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSet() {
		return setEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSet_Name() {
		return (EAttribute)setEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSet_Mode() {
		return (EAttribute)setEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSet_Order() {
		return (EAttribute)setEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSet_Schema() {
		return (EReference)setEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSet_Owner() {
		return (EReference)setEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSet_SystemOwner() {
		return (EReference)setEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSet_Members() {
		return (EReference)setEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSet_ViaMembers() {
		return (EReference)setEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRole() {
		return roleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRuler() {
		return rulerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRuler_Guides() {
		return (EReference)rulerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRuler_Type() {
		return (EAttribute)rulerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRuler_DiagramData() {
		return (EReference)rulerEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSet_IndexedSetModeSpecification() {
		return (EReference)setEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getElement() {
		return elementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElement_BaseName() {
		return (EAttribute)elementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElement_Name() {
		return (EAttribute)elementEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElement_Level() {
		return (EAttribute)elementEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElement_Usage() {
		return (EAttribute)elementEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElement_Value() {
		return (EAttribute)elementEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGuide() {
		return guideEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGuide_Position() {
		return (EAttribute)guideEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElement_Offset() {
		return (EAttribute)elementEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElement_Length() {
		return (EAttribute)elementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElement_Picture() {
		return (EAttribute)elementEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElement_Nullable() {
		return (EAttribute)elementEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElement_Record() {
		return (EReference)elementEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElement_KeyElements() {
		return (EReference)elementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElement_Children() {
		return (EReference)elementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElement_Redefines() {
		return (EReference)elementEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElement_SyntaxLength() {
		return (EAttribute)elementEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getElement_SyntaxPosition() {
		return (EAttribute)elementEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElement_OccursSpecification() {
		return (EReference)elementEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getElement_Parent() {
		return (EReference)elementEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKeyElement() {
		return keyElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKeyElement_Element() {
		return (EReference)keyElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyElement_SortSequence() {
		return (EAttribute)keyElementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKeyElement_Key() {
		return (EReference)keyElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKeyElement_Dbkey() {
		return (EAttribute)keyElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMemberRole() {
		return memberRoleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMemberRole_IndexDbkeyPosition() {
		return (EAttribute)memberRoleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMemberRole_MembershipOption() {
		return (EAttribute)memberRoleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMemberRole_NextDbkeyPosition() {
		return (EAttribute)memberRoleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMemberRole_PriorDbkeyPosition() {
		return (EAttribute)memberRoleEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMemberRole_OwnerDbkeyPosition() {
		return (EAttribute)memberRoleEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMemberRole_Record() {
		return (EReference)memberRoleEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMemberRole_Set() {
		return (EReference)memberRoleEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMemberRole_SortKey() {
		return (EReference)memberRoleEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMemberRole_ConnectionParts() {
		return (EReference)memberRoleEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMemberRole_ConnectionLabel() {
		return (EReference)memberRoleEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSystemOwner() {
		return systemOwnerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSystemOwner_AreaSpecification() {
		return (EReference)systemOwnerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSystemOwner_Set() {
		return (EReference)systemOwnerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getViaSpecification() {
		return viaSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getViaSpecification_SymbolicDisplacementName() {
		return (EAttribute)viaSpecificationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getViaSpecification_Set() {
		return (EReference)viaSpecificationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramNode() {
		return diagramNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramNode_DiagramLocation() {
		return (EReference)diagramNodeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramLocation() {
		return diagramLocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramLocation_X() {
		return (EAttribute)diagramLocationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramLocation_Y() {
		return (EAttribute)diagramLocationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramLocation_Eyecatcher() {
		return (EAttribute)diagramLocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramData() {
		return diagramDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramData_ConnectionLabels() {
		return (EReference)diagramDataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramData_ConnectionParts() {
		return (EReference)diagramDataEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramData_Connectors() {
		return (EReference)diagramDataEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramData_HorizontalRuler() {
		return (EReference)diagramDataEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramData_Locations() {
		return (EReference)diagramDataEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramData_ZoomLevel() {
		return (EAttribute)diagramDataEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramData_Rulers() {
		return (EReference)diagramDataEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramData_ShowGrid() {
		return (EAttribute)diagramDataEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramData_ShowRulers() {
		return (EAttribute)diagramDataEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramData_SnapToGeometry() {
		return (EAttribute)diagramDataEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramData_SnapToGrid() {
		return (EAttribute)diagramDataEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramData_SnapToGuides() {
		return (EAttribute)diagramDataEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDiagramData_VerticalRuler() {
		return (EReference)diagramDataEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getViaSpecification_Record() {
		return (EReference)viaSpecificationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getViaSpecification_DisplacementPageCount() {
		return (EAttribute)viaSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKey() {
		return keyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKey_CalcKey() {
		return (EAttribute)keyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKey_Length() {
		return (EAttribute)keyEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKey_DuplicatesOption() {
		return (EAttribute)keyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKey_Compressed() {
		return (EAttribute)keyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKey_NaturalSequence() {
		return (EAttribute)keyEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKey_Elements() {
		return (EReference)keyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKey_ElementSummary() {
		return (EAttribute)keyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKey_MemberRole() {
		return (EReference)keyEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKey_Record() {
		return (EReference)keyEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAreaSpecification() {
		return areaSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAreaSpecification_SymbolicSubareaName() {
		return (EAttribute)areaSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAreaSpecification_Area() {
		return (EReference)areaSpecificationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAreaSpecification_OffsetExpression() {
		return (EReference)areaSpecificationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAreaSpecification_Record() {
		return (EReference)areaSpecificationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAreaSpecification_SystemOwner() {
		return (EReference)areaSpecificationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConnectionPart() {
		return connectionPartEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConnectionPart_Connector() {
		return (EReference)connectionPartEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConnectionPart_BendpointLocations() {
		return (EReference)connectionPartEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConnectionPart_MemberRole() {
		return (EReference)connectionPartEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConnectionPart_SourceEndpointLocation() {
		return (EReference)connectionPartEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConnectionPart_TargetEndpointLocation() {
		return (EReference)connectionPartEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConnectionLabel() {
		return connectionLabelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConnectionLabel_Alignment() {
		return (EAttribute)connectionLabelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConnectionLabel_MemberRole() {
		return (EReference)connectionLabelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConnector() {
		return connectorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConnector_ConnectionPart() {
		return (EReference)connectorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConnector_Label() {
		return (EAttribute)connectorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOffsetExpression() {
		return offsetExpressionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOffsetExpression_AreaSpecification() {
		return (EReference)offsetExpressionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOffsetExpression_OffsetPageCount() {
		return (EAttribute)offsetExpressionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOffsetExpression_OffsetPercent() {
		return (EAttribute)offsetExpressionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOffsetExpression_PageCount() {
		return (EAttribute)offsetExpressionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOffsetExpression_Percent() {
		return (EAttribute)offsetExpressionEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOwnerRole() {
		return ownerRoleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOwnerRole_NextDbkeyPosition() {
		return (EAttribute)ownerRoleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOwnerRole_PriorDbkeyPosition() {
		return (EAttribute)ownerRoleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOwnerRole_Record() {
		return (EReference)ownerRoleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOwnerRole_Set() {
		return (EReference)ownerRoleEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndexedSetModeSpecification() {
		return indexedSetModeSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexedSetModeSpecification_SymbolicIndexName() {
		return (EAttribute)indexedSetModeSpecificationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndexElement() {
		return indexElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexElement_BaseName() {
		return (EAttribute)indexElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexElement_Name() {
		return (EAttribute)indexElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndexElement_OccursSpecification() {
		return (EReference)indexElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexedSetModeSpecification_KeyCount() {
		return (EAttribute)indexedSetModeSpecificationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIndexedSetModeSpecification_Set() {
		return (EReference)indexedSetModeSpecificationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexedSetModeSpecification_DisplacementPageCount() {
		return (EAttribute)indexedSetModeSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAreaProcedureCallSpecification() {
		return areaProcedureCallSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAreaProcedureCallSpecification_Area() {
		return (EReference)areaProcedureCallSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAreaProcedureCallSpecification_Procedure() {
		return (EReference)areaProcedureCallSpecificationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAreaProcedureCallSpecification_CallTime() {
		return (EAttribute)areaProcedureCallSpecificationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAreaProcedureCallSpecification_Function() {
		return (EAttribute)areaProcedureCallSpecificationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRecordProcedureCallSpecification() {
		return recordProcedureCallSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRecordProcedureCallSpecification_Procedure() {
		return (EReference)recordProcedureCallSpecificationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRecordProcedureCallSpecification_Record() {
		return (EReference)recordProcedureCallSpecificationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRecordProcedureCallSpecification_CallTime() {
		return (EAttribute)recordProcedureCallSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRecordProcedureCallSpecification_Verb() {
		return (EAttribute)recordProcedureCallSpecificationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProcedure() {
		return procedureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcedure_Name() {
		return (EAttribute)procedureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOccursSpecification() {
		return occursSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getOccursSpecification_Count() {
		return (EAttribute)occursSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOccursSpecification_DependingOn() {
		return (EReference)occursSpecificationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOccursSpecification_Element() {
		return (EReference)occursSpecificationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOccursSpecification_IndexElements() {
		return (EReference)occursSpecificationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSetOrder() {
		return setOrderEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getDuplicatesOption() {
		return duplicatesOptionEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getLabelAlignment() {
		return labelAlignmentEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getLocationMode() {
		return locationModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSetMode() {
		return setModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getStorageMode() {
		return storageModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSetMembershipOption() {
		return setMembershipOptionEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSortSequence() {
		return sortSequenceEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getProcedureCallTime() {
		return procedureCallTimeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getAreaProcedureCallFunction() {
		return areaProcedureCallFunctionEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getRecordProcedureCallVerb() {
		return recordProcedureCallVerbEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getRulerType() {
		return rulerTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getUsage() {
		return usageEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaFactory getSchemaFactory() {
		return (SchemaFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		areaProcedureCallSpecificationEClass = createEClass(AREA_PROCEDURE_CALL_SPECIFICATION);
		createEReference(areaProcedureCallSpecificationEClass, AREA_PROCEDURE_CALL_SPECIFICATION__AREA);
		createEAttribute(areaProcedureCallSpecificationEClass, AREA_PROCEDURE_CALL_SPECIFICATION__CALL_TIME);
		createEAttribute(areaProcedureCallSpecificationEClass, AREA_PROCEDURE_CALL_SPECIFICATION__FUNCTION);
		createEReference(areaProcedureCallSpecificationEClass, AREA_PROCEDURE_CALL_SPECIFICATION__PROCEDURE);

		areaSpecificationEClass = createEClass(AREA_SPECIFICATION);
		createEAttribute(areaSpecificationEClass, AREA_SPECIFICATION__SYMBOLIC_SUBAREA_NAME);
		createEReference(areaSpecificationEClass, AREA_SPECIFICATION__AREA);
		createEReference(areaSpecificationEClass, AREA_SPECIFICATION__OFFSET_EXPRESSION);
		createEReference(areaSpecificationEClass, AREA_SPECIFICATION__RECORD);
		createEReference(areaSpecificationEClass, AREA_SPECIFICATION__SYSTEM_OWNER);

		connectionPartEClass = createEClass(CONNECTION_PART);
		createEReference(connectionPartEClass, CONNECTION_PART__CONNECTOR);
		createEReference(connectionPartEClass, CONNECTION_PART__BENDPOINT_LOCATIONS);
		createEReference(connectionPartEClass, CONNECTION_PART__MEMBER_ROLE);
		createEReference(connectionPartEClass, CONNECTION_PART__SOURCE_ENDPOINT_LOCATION);
		createEReference(connectionPartEClass, CONNECTION_PART__TARGET_ENDPOINT_LOCATION);

		connectionLabelEClass = createEClass(CONNECTION_LABEL);
		createEAttribute(connectionLabelEClass, CONNECTION_LABEL__ALIGNMENT);
		createEReference(connectionLabelEClass, CONNECTION_LABEL__MEMBER_ROLE);

		connectorEClass = createEClass(CONNECTOR);
		createEReference(connectorEClass, CONNECTOR__CONNECTION_PART);
		createEAttribute(connectorEClass, CONNECTOR__LABEL);

		diagramDataEClass = createEClass(DIAGRAM_DATA);
		createEReference(diagramDataEClass, DIAGRAM_DATA__CONNECTION_LABELS);
		createEReference(diagramDataEClass, DIAGRAM_DATA__CONNECTION_PARTS);
		createEReference(diagramDataEClass, DIAGRAM_DATA__CONNECTORS);
		createEReference(diagramDataEClass, DIAGRAM_DATA__HORIZONTAL_RULER);
		createEReference(diagramDataEClass, DIAGRAM_DATA__LOCATIONS);
		createEAttribute(diagramDataEClass, DIAGRAM_DATA__SHOW_GRID);
		createEAttribute(diagramDataEClass, DIAGRAM_DATA__SHOW_RULERS);
		createEAttribute(diagramDataEClass, DIAGRAM_DATA__SNAP_TO_GEOMETRY);
		createEAttribute(diagramDataEClass, DIAGRAM_DATA__SNAP_TO_GRID);
		createEAttribute(diagramDataEClass, DIAGRAM_DATA__SNAP_TO_GUIDES);
		createEReference(diagramDataEClass, DIAGRAM_DATA__VERTICAL_RULER);
		createEAttribute(diagramDataEClass, DIAGRAM_DATA__ZOOM_LEVEL);
		createEReference(diagramDataEClass, DIAGRAM_DATA__RULERS);

		diagramLocationEClass = createEClass(DIAGRAM_LOCATION);
		createEAttribute(diagramLocationEClass, DIAGRAM_LOCATION__EYECATCHER);
		createEAttribute(diagramLocationEClass, DIAGRAM_LOCATION__X);
		createEAttribute(diagramLocationEClass, DIAGRAM_LOCATION__Y);

		diagramNodeEClass = createEClass(DIAGRAM_NODE);
		createEReference(diagramNodeEClass, DIAGRAM_NODE__DIAGRAM_LOCATION);

		elementEClass = createEClass(ELEMENT);
		createEAttribute(elementEClass, ELEMENT__BASE_NAME);
		createEReference(elementEClass, ELEMENT__CHILDREN);
		createEReference(elementEClass, ELEMENT__KEY_ELEMENTS);
		createEAttribute(elementEClass, ELEMENT__LENGTH);
		createEAttribute(elementEClass, ELEMENT__LEVEL);
		createEAttribute(elementEClass, ELEMENT__NAME);
		createEAttribute(elementEClass, ELEMENT__NULLABLE);
		createEReference(elementEClass, ELEMENT__OCCURS_SPECIFICATION);
		createEAttribute(elementEClass, ELEMENT__OFFSET);
		createEReference(elementEClass, ELEMENT__PARENT);
		createEAttribute(elementEClass, ELEMENT__PICTURE);
		createEReference(elementEClass, ELEMENT__RECORD);
		createEReference(elementEClass, ELEMENT__REDEFINES);
		createEAttribute(elementEClass, ELEMENT__SYNTAX_LENGTH);
		createEAttribute(elementEClass, ELEMENT__SYNTAX_POSITION);
		createEAttribute(elementEClass, ELEMENT__USAGE);
		createEAttribute(elementEClass, ELEMENT__VALUE);

		guideEClass = createEClass(GUIDE);
		createEAttribute(guideEClass, GUIDE__POSITION);

		indexedSetModeSpecificationEClass = createEClass(INDEXED_SET_MODE_SPECIFICATION);
		createEAttribute(indexedSetModeSpecificationEClass, INDEXED_SET_MODE_SPECIFICATION__DISPLACEMENT_PAGE_COUNT);
		createEAttribute(indexedSetModeSpecificationEClass, INDEXED_SET_MODE_SPECIFICATION__KEY_COUNT);
		createEReference(indexedSetModeSpecificationEClass, INDEXED_SET_MODE_SPECIFICATION__SET);
		createEAttribute(indexedSetModeSpecificationEClass, INDEXED_SET_MODE_SPECIFICATION__SYMBOLIC_INDEX_NAME);

		indexElementEClass = createEClass(INDEX_ELEMENT);
		createEAttribute(indexElementEClass, INDEX_ELEMENT__BASE_NAME);
		createEAttribute(indexElementEClass, INDEX_ELEMENT__NAME);
		createEReference(indexElementEClass, INDEX_ELEMENT__OCCURS_SPECIFICATION);

		keyEClass = createEClass(KEY);
		createEAttribute(keyEClass, KEY__CALC_KEY);
		createEAttribute(keyEClass, KEY__COMPRESSED);
		createEAttribute(keyEClass, KEY__DUPLICATES_OPTION);
		createEReference(keyEClass, KEY__ELEMENTS);
		createEAttribute(keyEClass, KEY__ELEMENT_SUMMARY);
		createEAttribute(keyEClass, KEY__LENGTH);
		createEReference(keyEClass, KEY__MEMBER_ROLE);
		createEAttribute(keyEClass, KEY__NATURAL_SEQUENCE);
		createEReference(keyEClass, KEY__RECORD);

		keyElementEClass = createEClass(KEY_ELEMENT);
		createEAttribute(keyElementEClass, KEY_ELEMENT__DBKEY);
		createEReference(keyElementEClass, KEY_ELEMENT__ELEMENT);
		createEReference(keyElementEClass, KEY_ELEMENT__KEY);
		createEAttribute(keyElementEClass, KEY_ELEMENT__SORT_SEQUENCE);

		memberRoleEClass = createEClass(MEMBER_ROLE);
		createEAttribute(memberRoleEClass, MEMBER_ROLE__INDEX_DBKEY_POSITION);
		createEAttribute(memberRoleEClass, MEMBER_ROLE__MEMBERSHIP_OPTION);
		createEAttribute(memberRoleEClass, MEMBER_ROLE__NEXT_DBKEY_POSITION);
		createEAttribute(memberRoleEClass, MEMBER_ROLE__PRIOR_DBKEY_POSITION);
		createEAttribute(memberRoleEClass, MEMBER_ROLE__OWNER_DBKEY_POSITION);
		createEReference(memberRoleEClass, MEMBER_ROLE__RECORD);
		createEReference(memberRoleEClass, MEMBER_ROLE__SET);
		createEReference(memberRoleEClass, MEMBER_ROLE__SORT_KEY);
		createEReference(memberRoleEClass, MEMBER_ROLE__CONNECTION_PARTS);
		createEReference(memberRoleEClass, MEMBER_ROLE__CONNECTION_LABEL);

		occursSpecificationEClass = createEClass(OCCURS_SPECIFICATION);
		createEAttribute(occursSpecificationEClass, OCCURS_SPECIFICATION__COUNT);
		createEReference(occursSpecificationEClass, OCCURS_SPECIFICATION__DEPENDING_ON);
		createEReference(occursSpecificationEClass, OCCURS_SPECIFICATION__ELEMENT);
		createEReference(occursSpecificationEClass, OCCURS_SPECIFICATION__INDEX_ELEMENTS);

		offsetExpressionEClass = createEClass(OFFSET_EXPRESSION);
		createEReference(offsetExpressionEClass, OFFSET_EXPRESSION__AREA_SPECIFICATION);
		createEAttribute(offsetExpressionEClass, OFFSET_EXPRESSION__OFFSET_PAGE_COUNT);
		createEAttribute(offsetExpressionEClass, OFFSET_EXPRESSION__OFFSET_PERCENT);
		createEAttribute(offsetExpressionEClass, OFFSET_EXPRESSION__PAGE_COUNT);
		createEAttribute(offsetExpressionEClass, OFFSET_EXPRESSION__PERCENT);

		ownerRoleEClass = createEClass(OWNER_ROLE);
		createEAttribute(ownerRoleEClass, OWNER_ROLE__NEXT_DBKEY_POSITION);
		createEAttribute(ownerRoleEClass, OWNER_ROLE__PRIOR_DBKEY_POSITION);
		createEReference(ownerRoleEClass, OWNER_ROLE__RECORD);
		createEReference(ownerRoleEClass, OWNER_ROLE__SET);

		procedureEClass = createEClass(PROCEDURE);
		createEAttribute(procedureEClass, PROCEDURE__NAME);

		recordProcedureCallSpecificationEClass = createEClass(RECORD_PROCEDURE_CALL_SPECIFICATION);
		createEAttribute(recordProcedureCallSpecificationEClass, RECORD_PROCEDURE_CALL_SPECIFICATION__CALL_TIME);
		createEReference(recordProcedureCallSpecificationEClass, RECORD_PROCEDURE_CALL_SPECIFICATION__PROCEDURE);
		createEReference(recordProcedureCallSpecificationEClass, RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD);
		createEAttribute(recordProcedureCallSpecificationEClass, RECORD_PROCEDURE_CALL_SPECIFICATION__VERB);

		roleEClass = createEClass(ROLE);

		rulerEClass = createEClass(RULER);
		createEReference(rulerEClass, RULER__GUIDES);
		createEAttribute(rulerEClass, RULER__TYPE);
		createEReference(rulerEClass, RULER__DIAGRAM_DATA);

		schemaEClass = createEClass(SCHEMA);
		createEReference(schemaEClass, SCHEMA__AREAS);
		createEAttribute(schemaEClass, SCHEMA__COMMENTS);
		createEAttribute(schemaEClass, SCHEMA__DESCRIPTION);
		createEReference(schemaEClass, SCHEMA__DIAGRAM_DATA);
		createEAttribute(schemaEClass, SCHEMA__MEMO_DATE);
		createEAttribute(schemaEClass, SCHEMA__NAME);
		createEReference(schemaEClass, SCHEMA__PROCEDURES);
		createEReference(schemaEClass, SCHEMA__RECORDS);
		createEReference(schemaEClass, SCHEMA__SETS);
		createEAttribute(schemaEClass, SCHEMA__VERSION);

		schemaAreaEClass = createEClass(SCHEMA_AREA);
		createEReference(schemaAreaEClass, SCHEMA_AREA__AREA_SPECIFICATIONS);
		createEReference(schemaAreaEClass, SCHEMA_AREA__INDEXES);
		createEAttribute(schemaAreaEClass, SCHEMA_AREA__NAME);
		createEReference(schemaAreaEClass, SCHEMA_AREA__PROCEDURES);
		createEReference(schemaAreaEClass, SCHEMA_AREA__RECORDS);
		createEReference(schemaAreaEClass, SCHEMA_AREA__SCHEMA);

		schemaRecordEClass = createEClass(SCHEMA_RECORD);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__BASE_NAME);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__BASE_VERSION);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__CONTROL_LENGTH);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__DATA_LENGTH);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__FRAGMENTED);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__ID);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__LOCATION_MODE);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__MINIMUM_FRAGMENT_LENGTH);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__MINIMUM_ROOT_LENGTH);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__NAME);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__PREFIX_LENGTH);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__STORAGE_MODE);
		createEReference(schemaRecordEClass, SCHEMA_RECORD__AREA_SPECIFICATION);
		createEReference(schemaRecordEClass, SCHEMA_RECORD__CALC_KEY);
		createEReference(schemaRecordEClass, SCHEMA_RECORD__ELEMENTS);
		createEReference(schemaRecordEClass, SCHEMA_RECORD__KEYS);
		createEReference(schemaRecordEClass, SCHEMA_RECORD__MEMBER_ROLES);
		createEReference(schemaRecordEClass, SCHEMA_RECORD__OWNER_ROLES);
		createEReference(schemaRecordEClass, SCHEMA_RECORD__PROCEDURES);
		createEReference(schemaRecordEClass, SCHEMA_RECORD__ROLES);
		createEReference(schemaRecordEClass, SCHEMA_RECORD__ROOT_ELEMENTS);
		createEReference(schemaRecordEClass, SCHEMA_RECORD__SCHEMA);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__SYNONYM_NAME);
		createEAttribute(schemaRecordEClass, SCHEMA_RECORD__SYNONYM_VERSION);
		createEReference(schemaRecordEClass, SCHEMA_RECORD__VIA_SPECIFICATION);

		setEClass = createEClass(SET);
		createEReference(setEClass, SET__INDEXED_SET_MODE_SPECIFICATION);
		createEReference(setEClass, SET__MEMBERS);
		createEAttribute(setEClass, SET__MODE);
		createEAttribute(setEClass, SET__NAME);
		createEAttribute(setEClass, SET__ORDER);
		createEReference(setEClass, SET__OWNER);
		createEReference(setEClass, SET__SCHEMA);
		createEReference(setEClass, SET__SYSTEM_OWNER);
		createEReference(setEClass, SET__VIA_MEMBERS);

		systemOwnerEClass = createEClass(SYSTEM_OWNER);
		createEReference(systemOwnerEClass, SYSTEM_OWNER__AREA_SPECIFICATION);
		createEReference(systemOwnerEClass, SYSTEM_OWNER__SET);

		viaSpecificationEClass = createEClass(VIA_SPECIFICATION);
		createEAttribute(viaSpecificationEClass, VIA_SPECIFICATION__DISPLACEMENT_PAGE_COUNT);
		createEReference(viaSpecificationEClass, VIA_SPECIFICATION__RECORD);
		createEReference(viaSpecificationEClass, VIA_SPECIFICATION__SET);
		createEAttribute(viaSpecificationEClass, VIA_SPECIFICATION__SYMBOLIC_DISPLACEMENT_NAME);

		// Create enums
		areaProcedureCallFunctionEEnum = createEEnum(AREA_PROCEDURE_CALL_FUNCTION);
		duplicatesOptionEEnum = createEEnum(DUPLICATES_OPTION);
		labelAlignmentEEnum = createEEnum(LABEL_ALIGNMENT);
		locationModeEEnum = createEEnum(LOCATION_MODE);
		procedureCallTimeEEnum = createEEnum(PROCEDURE_CALL_TIME);
		recordProcedureCallVerbEEnum = createEEnum(RECORD_PROCEDURE_CALL_VERB);
		rulerTypeEEnum = createEEnum(RULER_TYPE);
		setMembershipOptionEEnum = createEEnum(SET_MEMBERSHIP_OPTION);
		setModeEEnum = createEEnum(SET_MODE);
		setOrderEEnum = createEEnum(SET_ORDER);
		sortSequenceEEnum = createEEnum(SORT_SEQUENCE);
		storageModeEEnum = createEEnum(STORAGE_MODE);
		usageEEnum = createEEnum(USAGE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		connectionLabelEClass.getESuperTypes().add(this.getDiagramNode());
		connectorEClass.getESuperTypes().add(this.getDiagramNode());
		memberRoleEClass.getESuperTypes().add(this.getRole());
		ownerRoleEClass.getESuperTypes().add(this.getRole());
		schemaRecordEClass.getESuperTypes().add(this.getDiagramNode());
		systemOwnerEClass.getESuperTypes().add(this.getDiagramNode());

		// Initialize classes and features; add operations and parameters
		initEClass(areaProcedureCallSpecificationEClass, AreaProcedureCallSpecification.class, "AreaProcedureCallSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAreaProcedureCallSpecification_Area(), this.getSchemaArea(), this.getSchemaArea_Procedures(), "area", null, 1, 1, AreaProcedureCallSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAreaProcedureCallSpecification_CallTime(), this.getProcedureCallTime(), "callTime", null, 0, 1, AreaProcedureCallSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAreaProcedureCallSpecification_Function(), this.getAreaProcedureCallFunction(), "function", "", 0, 1, AreaProcedureCallSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAreaProcedureCallSpecification_Procedure(), this.getProcedure(), null, "procedure", null, 1, 1, AreaProcedureCallSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(areaSpecificationEClass, AreaSpecification.class, "AreaSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAreaSpecification_SymbolicSubareaName(), ecorePackage.getEString(), "symbolicSubareaName", null, 0, 1, AreaSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAreaSpecification_Area(), this.getSchemaArea(), this.getSchemaArea_AreaSpecifications(), "area", null, 1, 1, AreaSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAreaSpecification_OffsetExpression(), this.getOffsetExpression(), this.getOffsetExpression_AreaSpecification(), "offsetExpression", null, 0, 1, AreaSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAreaSpecification_Record(), this.getSchemaRecord(), this.getSchemaRecord_AreaSpecification(), "record", null, 1, 1, AreaSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAreaSpecification_SystemOwner(), this.getSystemOwner(), this.getSystemOwner_AreaSpecification(), "systemOwner", null, 1, 1, AreaSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(connectionPartEClass, ConnectionPart.class, "ConnectionPart", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getConnectionPart_Connector(), this.getConnector(), this.getConnector_ConnectionPart(), "connector", null, 0, 1, ConnectionPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConnectionPart_BendpointLocations(), this.getDiagramLocation(), null, "bendpointLocations", null, 0, -1, ConnectionPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConnectionPart_MemberRole(), this.getMemberRole(), this.getMemberRole_ConnectionParts(), "memberRole", null, 1, 1, ConnectionPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConnectionPart_SourceEndpointLocation(), this.getDiagramLocation(), null, "sourceEndpointLocation", null, 0, 1, ConnectionPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConnectionPart_TargetEndpointLocation(), this.getDiagramLocation(), null, "targetEndpointLocation", null, 0, 1, ConnectionPart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(connectionLabelEClass, ConnectionLabel.class, "ConnectionLabel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConnectionLabel_Alignment(), this.getLabelAlignment(), "alignment", "", 0, 1, ConnectionLabel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConnectionLabel_MemberRole(), this.getMemberRole(), this.getMemberRole_ConnectionLabel(), "memberRole", null, 1, 1, ConnectionLabel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(connectorEClass, Connector.class, "Connector", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getConnector_ConnectionPart(), this.getConnectionPart(), this.getConnectionPart_Connector(), "connectionPart", null, 1, 1, Connector.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConnector_Label(), ecorePackage.getEString(), "label", null, 0, 1, Connector.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(diagramDataEClass, DiagramData.class, "DiagramData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDiagramData_ConnectionLabels(), this.getConnectionLabel(), null, "connectionLabels", null, 0, -1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDiagramData_ConnectionParts(), this.getConnectionPart(), null, "connectionParts", null, 0, -1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDiagramData_Connectors(), this.getConnector(), null, "connectors", null, 0, -1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDiagramData_HorizontalRuler(), this.getRuler(), null, "horizontalRuler", null, 1, 1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDiagramData_Locations(), this.getDiagramLocation(), null, "locations", null, 0, -1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiagramData_ShowGrid(), ecorePackage.getEBoolean(), "showGrid", "false", 0, 1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiagramData_ShowRulers(), ecorePackage.getEBoolean(), "showRulers", null, 0, 1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiagramData_SnapToGeometry(), ecorePackage.getEBoolean(), "snapToGeometry", null, 0, 1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiagramData_SnapToGrid(), ecorePackage.getEBoolean(), "snapToGrid", null, 0, 1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiagramData_SnapToGuides(), ecorePackage.getEBoolean(), "snapToGuides", null, 0, 1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDiagramData_VerticalRuler(), this.getRuler(), null, "verticalRuler", null, 1, 1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiagramData_ZoomLevel(), ecorePackage.getEDouble(), "zoomLevel", "1.0d", 0, 1, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDiagramData_Rulers(), this.getRuler(), this.getRuler_DiagramData(), "rulers", null, 2, 2, DiagramData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(diagramLocationEClass, DiagramLocation.class, "DiagramLocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDiagramLocation_Eyecatcher(), ecorePackage.getEString(), "eyecatcher", null, 0, 1, DiagramLocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiagramLocation_X(), ecorePackage.getEInt(), "x", null, 0, 1, DiagramLocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiagramLocation_Y(), ecorePackage.getEInt(), "y", null, 0, 1, DiagramLocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(diagramNodeEClass, DiagramNode.class, "DiagramNode", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDiagramNode_DiagramLocation(), this.getDiagramLocation(), null, "diagramLocation", null, 1, 1, DiagramNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(elementEClass, Element.class, "Element", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getElement_BaseName(), ecorePackage.getEString(), "baseName", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElement_Children(), this.getElement(), this.getElement_Parent(), "children", null, 0, -1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElement_KeyElements(), this.getKeyElement(), this.getKeyElement_Element(), "keyElements", null, 0, -1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElement_Length(), ecorePackage.getEShort(), "length", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getElement_Level(), ecorePackage.getEShort(), "level", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElement_Name(), ecorePackage.getEString(), "name", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElement_Nullable(), ecorePackage.getEBoolean(), "nullable", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElement_OccursSpecification(), this.getOccursSpecification(), this.getOccursSpecification_Element(), "occursSpecification", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElement_Offset(), ecorePackage.getEShort(), "offset", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getElement_Parent(), this.getElement(), this.getElement_Children(), "parent", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElement_Picture(), ecorePackage.getEString(), "picture", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElement_Record(), this.getSchemaRecord(), this.getSchemaRecord_Elements(), "record", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getElement_Redefines(), this.getElement(), null, "redefines", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElement_SyntaxLength(), ecorePackage.getEShort(), "syntaxLength", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getElement_SyntaxPosition(), ecorePackage.getEShort(), "syntaxPosition", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getElement_Usage(), this.getUsage(), "usage", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getElement_Value(), ecorePackage.getEString(), "value", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(guideEClass, Guide.class, "Guide", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGuide_Position(), ecorePackage.getEInt(), "position", null, 0, 1, Guide.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indexedSetModeSpecificationEClass, IndexedSetModeSpecification.class, "IndexedSetModeSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIndexedSetModeSpecification_DisplacementPageCount(), ecorePackage.getEShortObject(), "displacementPageCount", null, 0, 1, IndexedSetModeSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndexedSetModeSpecification_KeyCount(), ecorePackage.getEShortObject(), "keyCount", null, 0, 1, IndexedSetModeSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIndexedSetModeSpecification_Set(), this.getSet(), this.getSet_IndexedSetModeSpecification(), "set", null, 1, 1, IndexedSetModeSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndexedSetModeSpecification_SymbolicIndexName(), ecorePackage.getEString(), "symbolicIndexName", null, 0, 1, IndexedSetModeSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(indexElementEClass, IndexElement.class, "IndexElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIndexElement_BaseName(), ecorePackage.getEString(), "baseName", null, 0, 1, IndexElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndexElement_Name(), ecorePackage.getEString(), "name", null, 0, 1, IndexElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIndexElement_OccursSpecification(), this.getOccursSpecification(), this.getOccursSpecification_IndexElements(), "occursSpecification", null, 1, 1, IndexElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(keyEClass, Key.class, "Key", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getKey_CalcKey(), ecorePackage.getEBoolean(), "calcKey", null, 0, 1, Key.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getKey_Compressed(), ecorePackage.getEBoolean(), "compressed", null, 0, 1, Key.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getKey_DuplicatesOption(), this.getDuplicatesOption(), "duplicatesOption", null, 0, 1, Key.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getKey_Elements(), this.getKeyElement(), this.getKeyElement_Key(), "elements", null, 1, -1, Key.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getKey_ElementSummary(), ecorePackage.getEString(), "elementSummary", null, 0, 1, Key.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getKey_Length(), ecorePackage.getEShort(), "length", null, 0, 1, Key.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getKey_MemberRole(), this.getMemberRole(), this.getMemberRole_SortKey(), "memberRole", null, 0, 1, Key.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getKey_NaturalSequence(), ecorePackage.getEBoolean(), "naturalSequence", null, 0, 1, Key.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getKey_Record(), this.getSchemaRecord(), this.getSchemaRecord_Keys(), "record", null, 0, 1, Key.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(keyElementEClass, KeyElement.class, "KeyElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getKeyElement_Dbkey(), ecorePackage.getEBoolean(), "dbkey", null, 0, 1, KeyElement.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getKeyElement_Element(), this.getElement(), this.getElement_KeyElements(), "element", null, 1, 1, KeyElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getKeyElement_Key(), this.getKey(), this.getKey_Elements(), "key", null, 1, 1, KeyElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getKeyElement_SortSequence(), this.getSortSequence(), "sortSequence", null, 0, 1, KeyElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(memberRoleEClass, MemberRole.class, "MemberRole", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMemberRole_IndexDbkeyPosition(), ecorePackage.getEShortObject(), "indexDbkeyPosition", null, 0, 1, MemberRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMemberRole_MembershipOption(), this.getSetMembershipOption(), "membershipOption", null, 0, 1, MemberRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMemberRole_NextDbkeyPosition(), ecorePackage.getEShortObject(), "nextDbkeyPosition", null, 0, 1, MemberRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMemberRole_PriorDbkeyPosition(), ecorePackage.getEShortObject(), "priorDbkeyPosition", null, 0, 1, MemberRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMemberRole_OwnerDbkeyPosition(), ecorePackage.getEShortObject(), "ownerDbkeyPosition", null, 0, 1, MemberRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMemberRole_Record(), this.getSchemaRecord(), this.getSchemaRecord_MemberRoles(), "record", null, 1, 1, MemberRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMemberRole_Set(), this.getSet(), this.getSet_Members(), "set", null, 1, 1, MemberRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMemberRole_SortKey(), this.getKey(), this.getKey_MemberRole(), "sortKey", null, 0, 1, MemberRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMemberRole_ConnectionParts(), this.getConnectionPart(), this.getConnectionPart_MemberRole(), "connectionParts", null, 1, 2, MemberRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMemberRole_ConnectionLabel(), this.getConnectionLabel(), this.getConnectionLabel_MemberRole(), "connectionLabel", null, 1, 1, MemberRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(occursSpecificationEClass, OccursSpecification.class, "OccursSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOccursSpecification_Count(), ecorePackage.getEShort(), "count", null, 0, 1, OccursSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOccursSpecification_DependingOn(), this.getElement(), null, "dependingOn", null, 0, 1, OccursSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOccursSpecification_Element(), this.getElement(), this.getElement_OccursSpecification(), "element", null, 1, 1, OccursSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOccursSpecification_IndexElements(), this.getIndexElement(), this.getIndexElement_OccursSpecification(), "indexElements", null, 0, -1, OccursSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(offsetExpressionEClass, OffsetExpression.class, "OffsetExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOffsetExpression_AreaSpecification(), this.getAreaSpecification(), this.getAreaSpecification_OffsetExpression(), "areaSpecification", null, 1, 1, OffsetExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOffsetExpression_OffsetPageCount(), ecorePackage.getEIntegerObject(), "offsetPageCount", null, 0, 1, OffsetExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOffsetExpression_OffsetPercent(), ecorePackage.getEShortObject(), "offsetPercent", null, 0, 1, OffsetExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOffsetExpression_PageCount(), ecorePackage.getEIntegerObject(), "pageCount", null, 0, 1, OffsetExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOffsetExpression_Percent(), ecorePackage.getEShortObject(), "percent", null, 0, 1, OffsetExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ownerRoleEClass, OwnerRole.class, "OwnerRole", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOwnerRole_NextDbkeyPosition(), ecorePackage.getEShort(), "nextDbkeyPosition", null, 0, 1, OwnerRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOwnerRole_PriorDbkeyPosition(), ecorePackage.getEShortObject(), "priorDbkeyPosition", null, 0, 1, OwnerRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOwnerRole_Record(), this.getSchemaRecord(), this.getSchemaRecord_OwnerRoles(), "record", null, 1, 1, OwnerRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOwnerRole_Set(), this.getSet(), this.getSet_Owner(), "set", null, 1, 1, OwnerRole.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(procedureEClass, Procedure.class, "Procedure", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProcedure_Name(), ecorePackage.getEString(), "name", null, 0, 1, Procedure.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(recordProcedureCallSpecificationEClass, RecordProcedureCallSpecification.class, "RecordProcedureCallSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRecordProcedureCallSpecification_CallTime(), this.getProcedureCallTime(), "callTime", null, 0, 1, RecordProcedureCallSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRecordProcedureCallSpecification_Procedure(), this.getProcedure(), null, "procedure", null, 1, 1, RecordProcedureCallSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRecordProcedureCallSpecification_Record(), this.getSchemaRecord(), this.getSchemaRecord_Procedures(), "record", null, 1, 1, RecordProcedureCallSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRecordProcedureCallSpecification_Verb(), this.getRecordProcedureCallVerb(), "verb", null, 0, 1, RecordProcedureCallSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(roleEClass, Role.class, "Role", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(rulerEClass, Ruler.class, "Ruler", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRuler_Guides(), this.getGuide(), null, "guides", null, 0, -1, Ruler.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRuler_Type(), this.getRulerType(), "type", null, 0, 1, Ruler.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRuler_DiagramData(), this.getDiagramData(), this.getDiagramData_Rulers(), "diagramData", null, 1, 1, Ruler.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(schemaEClass, Schema.class, "Schema", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSchema_Areas(), this.getSchemaArea(), this.getSchemaArea_Schema(), "areas", null, 0, -1, Schema.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchema_Comments(), ecorePackage.getEString(), "comments", null, 0, -1, Schema.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchema_Description(), ecorePackage.getEString(), "description", null, 0, 1, Schema.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchema_DiagramData(), this.getDiagramData(), null, "diagramData", null, 1, 1, Schema.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchema_MemoDate(), ecorePackage.getEString(), "memoDate", null, 0, 1, Schema.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchema_Name(), ecorePackage.getEString(), "name", null, 0, 1, Schema.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchema_Procedures(), this.getProcedure(), null, "procedures", null, 0, -1, Schema.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchema_Records(), this.getSchemaRecord(), this.getSchemaRecord_Schema(), "records", null, 0, -1, Schema.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchema_Sets(), this.getSet(), this.getSet_Schema(), "sets", null, 0, -1, Schema.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchema_Version(), ecorePackage.getEShort(), "version", null, 0, 1, Schema.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(schemaEClass, this.getSchemaArea(), "getArea", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(schemaEClass, this.getProcedure(), "getProcedure", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(schemaEClass, this.getSchemaRecord(), "getRecord", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(schemaEClass, this.getSchemaRecord(), "getRecord", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "areaName", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEShort(), "recordId", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(schemaEClass, this.getSet(), "getSet", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(schemaAreaEClass, SchemaArea.class, "SchemaArea", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSchemaArea_AreaSpecifications(), this.getAreaSpecification(), this.getAreaSpecification_Area(), "areaSpecifications", null, 0, -1, SchemaArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaArea_Indexes(), this.getSystemOwner(), null, "indexes", null, 0, -1, SchemaArea.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaArea_Name(), ecorePackage.getEString(), "name", null, 0, 1, SchemaArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaArea_Procedures(), this.getAreaProcedureCallSpecification(), this.getAreaProcedureCallSpecification_Area(), "procedures", null, 0, -1, SchemaArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaArea_Records(), this.getSchemaRecord(), null, "records", null, 0, -1, SchemaArea.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaArea_Schema(), this.getSchema(), this.getSchema_Areas(), "schema", null, 1, 1, SchemaArea.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(schemaAreaEClass, this.getSchemaRecord(), "getRecord", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEShort(), "recordId", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(schemaAreaEClass, this.getSchemaRecord(), "getRecord", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(schemaRecordEClass, SchemaRecord.class, "SchemaRecord", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSchemaRecord_BaseName(), ecorePackage.getEString(), "baseName", null, 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_BaseVersion(), ecorePackage.getEShort(), "baseVersion", "0", 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_ControlLength(), ecorePackage.getEShort(), "controlLength", "-1", 0, 1, SchemaRecord.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_DataLength(), ecorePackage.getEShort(), "dataLength", "-1", 0, 1, SchemaRecord.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_Fragmented(), ecorePackage.getEBoolean(), "fragmented", null, 0, 1, SchemaRecord.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_Id(), ecorePackage.getEShort(), "id", null, 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_LocationMode(), this.getLocationMode(), "locationMode", null, 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_MinimumFragmentLength(), ecorePackage.getEShortObject(), "minimumFragmentLength", null, 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_MinimumRootLength(), ecorePackage.getEShortObject(), "minimumRootLength", null, 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_Name(), ecorePackage.getEString(), "name", null, 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_PrefixLength(), ecorePackage.getEShort(), "prefixLength", null, 0, 1, SchemaRecord.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_StorageMode(), this.getStorageMode(), "storageMode", null, 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaRecord_AreaSpecification(), this.getAreaSpecification(), this.getAreaSpecification_Record(), "areaSpecification", null, 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaRecord_CalcKey(), this.getKey(), null, "calcKey", null, 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaRecord_Elements(), this.getElement(), this.getElement_Record(), "elements", null, 1, -1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaRecord_Keys(), this.getKey(), this.getKey_Record(), "keys", null, 0, -1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaRecord_MemberRoles(), this.getMemberRole(), this.getMemberRole_Record(), "memberRoles", null, 0, -1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaRecord_OwnerRoles(), this.getOwnerRole(), this.getOwnerRole_Record(), "ownerRoles", null, 0, -1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaRecord_Procedures(), this.getRecordProcedureCallSpecification(), this.getRecordProcedureCallSpecification_Record(), "procedures", null, 0, -1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaRecord_Roles(), this.getRole(), null, "roles", null, 0, -1, SchemaRecord.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaRecord_RootElements(), this.getElement(), null, "rootElements", null, 1, -1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaRecord_Schema(), this.getSchema(), this.getSchema_Records(), "schema", null, 1, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_SynonymName(), ecorePackage.getEString(), "synonymName", null, 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchemaRecord_SynonymVersion(), ecorePackage.getEShort(), "synonymVersion", "0", 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSchemaRecord_ViaSpecification(), this.getViaSpecification(), this.getViaSpecification_Record(), "viaSpecification", null, 0, 1, SchemaRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(schemaRecordEClass, this.getElement(), "getElement", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(schemaRecordEClass, this.getRole(), "getRole", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "setName", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(setEClass, Set.class, "Set", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSet_IndexedSetModeSpecification(), this.getIndexedSetModeSpecification(), this.getIndexedSetModeSpecification_Set(), "indexedSetModeSpecification", null, 0, 1, Set.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSet_Members(), this.getMemberRole(), this.getMemberRole_Set(), "members", null, 1, -1, Set.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSet_Mode(), this.getSetMode(), "mode", null, 0, 1, Set.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSet_Name(), ecorePackage.getEString(), "name", null, 0, 1, Set.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSet_Order(), this.getSetOrder(), "order", null, 0, 1, Set.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSet_Owner(), this.getOwnerRole(), this.getOwnerRole_Set(), "owner", null, 0, 1, Set.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSet_Schema(), this.getSchema(), this.getSchema_Sets(), "schema", null, 1, 1, Set.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSet_SystemOwner(), this.getSystemOwner(), this.getSystemOwner_Set(), "systemOwner", null, 0, 1, Set.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSet_ViaMembers(), this.getViaSpecification(), this.getViaSpecification_Set(), "viaMembers", null, 0, -1, Set.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(systemOwnerEClass, SystemOwner.class, "SystemOwner", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSystemOwner_AreaSpecification(), this.getAreaSpecification(), this.getAreaSpecification_SystemOwner(), "areaSpecification", null, 0, 1, SystemOwner.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSystemOwner_Set(), this.getSet(), this.getSet_SystemOwner(), "set", null, 1, 1, SystemOwner.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(viaSpecificationEClass, ViaSpecification.class, "ViaSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getViaSpecification_DisplacementPageCount(), ecorePackage.getEShortObject(), "displacementPageCount", null, 0, 1, ViaSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getViaSpecification_Record(), this.getSchemaRecord(), this.getSchemaRecord_ViaSpecification(), "record", null, 1, 1, ViaSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getViaSpecification_Set(), this.getSet(), this.getSet_ViaMembers(), "set", null, 1, 1, ViaSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViaSpecification_SymbolicDisplacementName(), ecorePackage.getEString(), "symbolicDisplacementName", null, 0, 1, ViaSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.class, "AreaProcedureCallFunction");
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.EVERY_DML_FUNCTION);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.READY_EXCLUSIVE);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.READY_EXCLUSIVE_UPDATE);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.READY_EXCLUSIVE_RETRIEVAL);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.READY_PROTECTED);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.READY_PROTECTED_UPDATE);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.READY_PROTECTED_RETRIEVAL);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.READY_SHARED);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.READY_SHARED_UPDATE);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.READY_SHARED_RETRIEVAL);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.READY_UPDATE);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.READY_RETRIEVAL);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.FINISH);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.COMMIT);
		addEEnumLiteral(areaProcedureCallFunctionEEnum, AreaProcedureCallFunction.ROLLBACK);

		initEEnum(duplicatesOptionEEnum, DuplicatesOption.class, "DuplicatesOption");
		addEEnumLiteral(duplicatesOptionEEnum, DuplicatesOption.FIRST);
		addEEnumLiteral(duplicatesOptionEEnum, DuplicatesOption.LAST);
		addEEnumLiteral(duplicatesOptionEEnum, DuplicatesOption.BY_DBKEY);
		addEEnumLiteral(duplicatesOptionEEnum, DuplicatesOption.NOT_ALLOWED);

		initEEnum(labelAlignmentEEnum, LabelAlignment.class, "LabelAlignment");
		addEEnumLiteral(labelAlignmentEEnum, LabelAlignment.LEFT);
		addEEnumLiteral(labelAlignmentEEnum, LabelAlignment.RIGHT);
		addEEnumLiteral(labelAlignmentEEnum, LabelAlignment.CENTER);

		initEEnum(locationModeEEnum, LocationMode.class, "LocationMode");
		addEEnumLiteral(locationModeEEnum, LocationMode.CALC);
		addEEnumLiteral(locationModeEEnum, LocationMode.DIRECT);
		addEEnumLiteral(locationModeEEnum, LocationMode.VIA);

		initEEnum(procedureCallTimeEEnum, ProcedureCallTime.class, "ProcedureCallTime");
		addEEnumLiteral(procedureCallTimeEEnum, ProcedureCallTime.BEFORE);
		addEEnumLiteral(procedureCallTimeEEnum, ProcedureCallTime.AFTER);
		addEEnumLiteral(procedureCallTimeEEnum, ProcedureCallTime.ON_ERROR_DURING);

		initEEnum(recordProcedureCallVerbEEnum, RecordProcedureCallVerb.class, "RecordProcedureCallVerb");
		addEEnumLiteral(recordProcedureCallVerbEEnum, RecordProcedureCallVerb.EVERY_DML_FUNCTION);
		addEEnumLiteral(recordProcedureCallVerbEEnum, RecordProcedureCallVerb.CONNECT);
		addEEnumLiteral(recordProcedureCallVerbEEnum, RecordProcedureCallVerb.DISCONNECT);
		addEEnumLiteral(recordProcedureCallVerbEEnum, RecordProcedureCallVerb.ERASE);
		addEEnumLiteral(recordProcedureCallVerbEEnum, RecordProcedureCallVerb.FIND);
		addEEnumLiteral(recordProcedureCallVerbEEnum, RecordProcedureCallVerb.GET);
		addEEnumLiteral(recordProcedureCallVerbEEnum, RecordProcedureCallVerb.MODIFY);
		addEEnumLiteral(recordProcedureCallVerbEEnum, RecordProcedureCallVerb.STORE);

		initEEnum(rulerTypeEEnum, RulerType.class, "RulerType");
		addEEnumLiteral(rulerTypeEEnum, RulerType.HORIZONTAL);
		addEEnumLiteral(rulerTypeEEnum, RulerType.VERTICAL);

		initEEnum(setMembershipOptionEEnum, SetMembershipOption.class, "SetMembershipOption");
		addEEnumLiteral(setMembershipOptionEEnum, SetMembershipOption.MANDATORY_AUTOMATIC);
		addEEnumLiteral(setMembershipOptionEEnum, SetMembershipOption.MANDATORY_MANUAL);
		addEEnumLiteral(setMembershipOptionEEnum, SetMembershipOption.OPTIONAL_AUTOMATIC);
		addEEnumLiteral(setMembershipOptionEEnum, SetMembershipOption.OPTIONAL_MANUAL);

		initEEnum(setModeEEnum, SetMode.class, "SetMode");
		addEEnumLiteral(setModeEEnum, SetMode.CHAINED);
		addEEnumLiteral(setModeEEnum, SetMode.INDEXED);

		initEEnum(setOrderEEnum, SetOrder.class, "SetOrder");
		addEEnumLiteral(setOrderEEnum, SetOrder.FIRST);
		addEEnumLiteral(setOrderEEnum, SetOrder.LAST);
		addEEnumLiteral(setOrderEEnum, SetOrder.NEXT);
		addEEnumLiteral(setOrderEEnum, SetOrder.PRIOR);
		addEEnumLiteral(setOrderEEnum, SetOrder.SORTED);

		initEEnum(sortSequenceEEnum, SortSequence.class, "SortSequence");
		addEEnumLiteral(sortSequenceEEnum, SortSequence.ASCENDING);
		addEEnumLiteral(sortSequenceEEnum, SortSequence.DESCENDING);

		initEEnum(storageModeEEnum, StorageMode.class, "StorageMode");
		addEEnumLiteral(storageModeEEnum, StorageMode.FIXED);
		addEEnumLiteral(storageModeEEnum, StorageMode.FIXED_COMPRESSED);
		addEEnumLiteral(storageModeEEnum, StorageMode.VARIABLE);
		addEEnumLiteral(storageModeEEnum, StorageMode.VARIABLE_COMPRESSED);

		initEEnum(usageEEnum, Usage.class, "Usage");
		addEEnumLiteral(usageEEnum, Usage.DISPLAY);
		addEEnumLiteral(usageEEnum, Usage.COMPUTATIONAL);
		addEEnumLiteral(usageEEnum, Usage.COMPUTATIONAL_1);
		addEEnumLiteral(usageEEnum, Usage.COMPUTATIONAL_2);
		addEEnumLiteral(usageEEnum, Usage.COMPUTATIONAL_3);
		addEEnumLiteral(usageEEnum, Usage.BIT);
		addEEnumLiteral(usageEEnum, Usage.POINTER);
		addEEnumLiteral(usageEEnum, Usage.DISPLAY_1);
		addEEnumLiteral(usageEEnum, Usage.CONDITION_NAME);

		// Create resource
		createResource(eNS_URI);
	}

} //SchemaPackageImpl
