/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.lh.dmlj.schema.SchemaFactory
 * @model kind="package"
 * @generated
 */
public interface SchemaPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "schema";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://org.lh.dmlj.schema/schema.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.lh.dmlj.schema";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SchemaPackage eINSTANCE = org.lh.dmlj.schema.impl.SchemaPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.SchemaImpl <em>Schema</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.SchemaImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSchema()
	 * @generated
	 */
	int SCHEMA = 21;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.SchemaAreaImpl <em>Area</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.SchemaAreaImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSchemaArea()
	 * @generated
	 */
	int SCHEMA_AREA = 22;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.SchemaRecordImpl <em>Record</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.SchemaRecordImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSchemaRecord()
	 * @generated
	 */
	int SCHEMA_RECORD = 23;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.SetImpl <em>Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.SetImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSet()
	 * @generated
	 */
	int SET = 24;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.ElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.ElementImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getElement()
	 * @generated
	 */
	int ELEMENT = 8;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.KeyElementImpl <em>Key Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.KeyElementImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getKeyElement()
	 * @generated
	 */
	int KEY_ELEMENT = 12;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.SystemOwnerImpl <em>System Owner</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.SystemOwnerImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSystemOwner()
	 * @generated
	 */
	int SYSTEM_OWNER = 25;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.ViaSpecificationImpl <em>Via Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.ViaSpecificationImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getViaSpecification()
	 * @generated
	 */
	int VIA_SPECIFICATION = 26;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.KeyImpl <em>Key</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.KeyImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getKey()
	 * @generated
	 */
	int KEY = 11;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.AreaSpecificationImpl <em>Area Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.AreaSpecificationImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getAreaSpecification()
	 * @generated
	 */
	int AREA_SPECIFICATION = 1;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.OffsetExpressionImpl <em>Offset Expression</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.OffsetExpressionImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getOffsetExpression()
	 * @generated
	 */
	int OFFSET_EXPRESSION = 15;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.IndexedSetModeSpecificationImpl <em>Indexed Set Mode Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.IndexedSetModeSpecificationImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getIndexedSetModeSpecification()
	 * @generated
	 */
	int INDEXED_SET_MODE_SPECIFICATION = 10;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.AreaProcedureCallSpecificationImpl <em>Area Procedure Call Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.AreaProcedureCallSpecificationImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getAreaProcedureCallSpecification()
	 * @generated
	 */
	int AREA_PROCEDURE_CALL_SPECIFICATION = 0;

	/**
	 * The feature id for the '<em><b>Call Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AREA_PROCEDURE_CALL_SPECIFICATION__CALL_TIME = 0;

	/**
	 * The feature id for the '<em><b>Function</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AREA_PROCEDURE_CALL_SPECIFICATION__FUNCTION = 1;

	/**
	 * The feature id for the '<em><b>Procedure</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AREA_PROCEDURE_CALL_SPECIFICATION__PROCEDURE = 2;

	/**
	 * The number of structural features of the '<em>Area Procedure Call Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AREA_PROCEDURE_CALL_SPECIFICATION_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Symbolic Subarea Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AREA_SPECIFICATION__SYMBOLIC_SUBAREA_NAME = 0;

	/**
	 * The feature id for the '<em><b>Area</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AREA_SPECIFICATION__AREA = 1;

	/**
	 * The feature id for the '<em><b>Offset Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AREA_SPECIFICATION__OFFSET_EXPRESSION = 2;

	/**
	 * The feature id for the '<em><b>Record</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AREA_SPECIFICATION__RECORD = 3;

	/**
	 * The feature id for the '<em><b>System Owner</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AREA_SPECIFICATION__SYSTEM_OWNER = 4;

	/**
	 * The number of structural features of the '<em>Area Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AREA_SPECIFICATION_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.ConnectionPartImpl <em>Connection Part</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.ConnectionPartImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getConnectionPart()
	 * @generated
	 */
	int CONNECTION_PART = 2;

	/**
	 * The feature id for the '<em><b>Connector</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_PART__CONNECTOR = 0;

	/**
	 * The feature id for the '<em><b>Bendpoint Locations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_PART__BENDPOINT_LOCATIONS = 1;

	/**
	 * The feature id for the '<em><b>Member Role</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_PART__MEMBER_ROLE = 2;

	/**
	 * The feature id for the '<em><b>Source Endpoint Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_PART__SOURCE_ENDPOINT_LOCATION = 3;

	/**
	 * The feature id for the '<em><b>Target Endpoint Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_PART__TARGET_ENDPOINT_LOCATION = 4;

	/**
	 * The number of structural features of the '<em>Connection Part</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_PART_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.RoleImpl <em>Role</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.RoleImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getRole()
	 * @generated
	 */
	int ROLE = 19;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.RecordProcedureCallSpecificationImpl <em>Record Procedure Call Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.RecordProcedureCallSpecificationImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getRecordProcedureCallSpecification()
	 * @generated
	 */
	int RECORD_PROCEDURE_CALL_SPECIFICATION = 18;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.ProcedureImpl <em>Procedure</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.ProcedureImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getProcedure()
	 * @generated
	 */
	int PROCEDURE = 17;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.OccursSpecificationImpl <em>Occurs Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.OccursSpecificationImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getOccursSpecification()
	 * @generated
	 */
	int OCCURS_SPECIFICATION = 14;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.MemberRoleImpl <em>Member Role</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.MemberRoleImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getMemberRole()
	 * @generated
	 */
	int MEMBER_ROLE = 13;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.OwnerRoleImpl <em>Owner Role</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.OwnerRoleImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getOwnerRole()
	 * @generated
	 */
	int OWNER_ROLE = 16;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.DiagramNodeImpl <em>Diagram Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.DiagramNodeImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getDiagramNode()
	 * @generated
	 */
	int DIAGRAM_NODE = 7;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.DiagramLocationImpl <em>Diagram Location</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.DiagramLocationImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getDiagramLocation()
	 * @generated
	 */
	int DIAGRAM_LOCATION = 6;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.DiagramDataImpl <em>Diagram Data</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.DiagramDataImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getDiagramData()
	 * @generated
	 */
	int DIAGRAM_DATA = 5;

	/**
	 * The feature id for the '<em><b>Diagram Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_NODE__DIAGRAM_LOCATION = 0;

	/**
	 * The number of structural features of the '<em>Diagram Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_NODE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.ConnectionLabelImpl <em>Connection Label</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.ConnectionLabelImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getConnectionLabel()
	 * @generated
	 */
	int CONNECTION_LABEL = 3;

	/**
	 * The feature id for the '<em><b>Diagram Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_LABEL__DIAGRAM_LOCATION = DIAGRAM_NODE__DIAGRAM_LOCATION;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_LABEL__ALIGNMENT = DIAGRAM_NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Member Role</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_LABEL__MEMBER_ROLE = DIAGRAM_NODE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Connection Label</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_LABEL_FEATURE_COUNT = DIAGRAM_NODE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.ConnectorImpl <em>Connector</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.ConnectorImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getConnector()
	 * @generated
	 */
	int CONNECTOR = 4;

	/**
	 * The feature id for the '<em><b>Diagram Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTOR__DIAGRAM_LOCATION = DIAGRAM_NODE__DIAGRAM_LOCATION;

	/**
	 * The feature id for the '<em><b>Connection Part</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTOR__CONNECTION_PART = DIAGRAM_NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTOR__LABEL = DIAGRAM_NODE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Connector</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTOR_FEATURE_COUNT = DIAGRAM_NODE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Connection Labels</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__CONNECTION_LABELS = 0;

	/**
	 * The feature id for the '<em><b>Connection Parts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__CONNECTION_PARTS = 1;

	/**
	 * The feature id for the '<em><b>Connectors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__CONNECTORS = 2;

	/**
	 * The feature id for the '<em><b>Horizontal Ruler</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__HORIZONTAL_RULER = 3;

	/**
	 * The feature id for the '<em><b>Locations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__LOCATIONS = 4;

	/**
	 * The feature id for the '<em><b>Show Grid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__SHOW_GRID = 5;

	/**
	 * The feature id for the '<em><b>Show Rulers</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__SHOW_RULERS = 6;

	/**
	 * The feature id for the '<em><b>Snap To Geometry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__SNAP_TO_GEOMETRY = 7;

	/**
	 * The feature id for the '<em><b>Snap To Grid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__SNAP_TO_GRID = 8;

	/**
	 * The feature id for the '<em><b>Snap To Guides</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__SNAP_TO_GUIDES = 9;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__UNIT = 10;

	/**
	 * The feature id for the '<em><b>Vertical Ruler</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__VERTICAL_RULER = 11;

	/**
	 * The feature id for the '<em><b>Zoom Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__ZOOM_LEVEL = 12;

	/**
	 * The feature id for the '<em><b>Rulers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA__RULERS = 13;

	/**
	 * The number of structural features of the '<em>Diagram Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_DATA_FEATURE_COUNT = 14;

	/**
	 * The feature id for the '<em><b>Eyecatcher</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LOCATION__EYECATCHER = 0;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LOCATION__X = 1;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LOCATION__Y = 2;

	/**
	 * The number of structural features of the '<em>Diagram Location</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_LOCATION_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Children</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__CHILDREN = 0;

	/**
	 * The feature id for the '<em><b>Key Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__KEY_ELEMENTS = 1;

	/**
	 * The feature id for the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__LENGTH = 2;

	/**
	 * The feature id for the '<em><b>Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__LEVEL = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__NAME = 4;

	/**
	 * The feature id for the '<em><b>Nullable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__NULLABLE = 5;

	/**
	 * The feature id for the '<em><b>Occurs Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__OCCURS_SPECIFICATION = 6;

	/**
	 * The feature id for the '<em><b>Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__OFFSET = 7;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__PARENT = 8;

	/**
	 * The feature id for the '<em><b>Picture</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__PICTURE = 9;

	/**
	 * The feature id for the '<em><b>Record</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__RECORD = 10;

	/**
	 * The feature id for the '<em><b>Redefines</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__REDEFINES = 11;

	/**
	 * The feature id for the '<em><b>Usage</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT__USAGE = 12;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ELEMENT_FEATURE_COUNT = 13;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.GuideImpl <em>Guide</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.GuideImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getGuide()
	 * @generated
	 */
	int GUIDE = 9;

	/**
	 * The feature id for the '<em><b>Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GUIDE__POSITION = 0;

	/**
	 * The number of structural features of the '<em>Guide</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GUIDE_FEATURE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Displacement Page Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEXED_SET_MODE_SPECIFICATION__DISPLACEMENT_PAGE_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Key Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEXED_SET_MODE_SPECIFICATION__KEY_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Set</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEXED_SET_MODE_SPECIFICATION__SET = 2;

	/**
	 * The feature id for the '<em><b>Symbolic Index Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEXED_SET_MODE_SPECIFICATION__SYMBOLIC_INDEX_NAME = 3;

	/**
	 * The number of structural features of the '<em>Indexed Set Mode Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INDEXED_SET_MODE_SPECIFICATION_FEATURE_COUNT = 4;

	/**
	 * The feature id for the '<em><b>Calc Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__CALC_KEY = 0;

	/**
	 * The feature id for the '<em><b>Compressed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__COMPRESSED = 1;

	/**
	 * The feature id for the '<em><b>Duplicates Option</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__DUPLICATES_OPTION = 2;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__ELEMENTS = 3;

	/**
	 * The feature id for the '<em><b>Element Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__ELEMENT_SUMMARY = 4;

	/**
	 * The feature id for the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__LENGTH = 5;

	/**
	 * The feature id for the '<em><b>Member Role</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__MEMBER_ROLE = 6;

	/**
	 * The feature id for the '<em><b>Natural Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__NATURAL_SEQUENCE = 7;

	/**
	 * The feature id for the '<em><b>Record</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__RECORD = 8;

	/**
	 * The number of structural features of the '<em>Key</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY_FEATURE_COUNT = 9;

	/**
	 * The feature id for the '<em><b>Dbkey</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY_ELEMENT__DBKEY = 0;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY_ELEMENT__ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY_ELEMENT__KEY = 2;

	/**
	 * The feature id for the '<em><b>Sort Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY_ELEMENT__SORT_SEQUENCE = 3;

	/**
	 * The number of structural features of the '<em>Key Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY_ELEMENT_FEATURE_COUNT = 4;

	/**
	 * The number of structural features of the '<em>Role</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROLE_FEATURE_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Index Dbkey Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_ROLE__INDEX_DBKEY_POSITION = ROLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Membership Option</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_ROLE__MEMBERSHIP_OPTION = ROLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Next Dbkey Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_ROLE__NEXT_DBKEY_POSITION = ROLE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Prior Dbkey Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_ROLE__PRIOR_DBKEY_POSITION = ROLE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Owner Dbkey Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_ROLE__OWNER_DBKEY_POSITION = ROLE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Record</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_ROLE__RECORD = ROLE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Set</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_ROLE__SET = ROLE_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Sort Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_ROLE__SORT_KEY = ROLE_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Connection Parts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_ROLE__CONNECTION_PARTS = ROLE_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Connection Label</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_ROLE__CONNECTION_LABEL = ROLE_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>Member Role</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBER_ROLE_FEATURE_COUNT = ROLE_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCCURS_SPECIFICATION__COUNT = 0;

	/**
	 * The feature id for the '<em><b>Depending On</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCCURS_SPECIFICATION__DEPENDING_ON = 1;

	/**
	 * The feature id for the '<em><b>Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCCURS_SPECIFICATION__ELEMENT = 2;

	/**
	 * The number of structural features of the '<em>Occurs Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCCURS_SPECIFICATION_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Area Specification</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_EXPRESSION__AREA_SPECIFICATION = 0;

	/**
	 * The feature id for the '<em><b>Offset Page Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_EXPRESSION__OFFSET_PAGE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Offset Percent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_EXPRESSION__OFFSET_PERCENT = 2;

	/**
	 * The feature id for the '<em><b>Page Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_EXPRESSION__PAGE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Percent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_EXPRESSION__PERCENT = 4;

	/**
	 * The number of structural features of the '<em>Offset Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OFFSET_EXPRESSION_FEATURE_COUNT = 5;

	/**
	 * The feature id for the '<em><b>Next Dbkey Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OWNER_ROLE__NEXT_DBKEY_POSITION = ROLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Prior Dbkey Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OWNER_ROLE__PRIOR_DBKEY_POSITION = ROLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Record</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OWNER_ROLE__RECORD = ROLE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Set</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OWNER_ROLE__SET = ROLE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Owner Role</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OWNER_ROLE_FEATURE_COUNT = ROLE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCEDURE__NAME = 0;

	/**
	 * The number of structural features of the '<em>Procedure</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCEDURE_FEATURE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Call Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECORD_PROCEDURE_CALL_SPECIFICATION__CALL_TIME = 0;

	/**
	 * The feature id for the '<em><b>Procedure</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECORD_PROCEDURE_CALL_SPECIFICATION__PROCEDURE = 1;

	/**
	 * The feature id for the '<em><b>Record</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD = 2;

	/**
	 * The feature id for the '<em><b>Verb</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECORD_PROCEDURE_CALL_SPECIFICATION__VERB = 3;

	/**
	 * The number of structural features of the '<em>Record Procedure Call Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECORD_PROCEDURE_CALL_SPECIFICATION_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.impl.RulerImpl <em>Ruler</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.impl.RulerImpl
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getRuler()
	 * @generated
	 */
	int RULER = 20;

	/**
	 * The feature id for the '<em><b>Guides</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULER__GUIDES = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULER__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Diagram Data</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULER__DIAGRAM_DATA = 2;

	/**
	 * The number of structural features of the '<em>Ruler</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULER_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Areas</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA__AREAS = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Diagram Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA__DIAGRAM_DATA = 2;

	/**
	 * The feature id for the '<em><b>Memo Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA__MEMO_DATE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA__NAME = 4;

	/**
	 * The feature id for the '<em><b>Procedures</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA__PROCEDURES = 5;

	/**
	 * The feature id for the '<em><b>Records</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA__RECORDS = 6;

	/**
	 * The feature id for the '<em><b>Sets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA__SETS = 7;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA__VERSION = 8;

	/**
	 * The number of structural features of the '<em>Schema</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_FEATURE_COUNT = 9;

	/**
	 * The feature id for the '<em><b>Area Specifications</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_AREA__AREA_SPECIFICATIONS = 0;

	/**
	 * The feature id for the '<em><b>Indexes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_AREA__INDEXES = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_AREA__NAME = 2;

	/**
	 * The feature id for the '<em><b>Procedures</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_AREA__PROCEDURES = 3;

	/**
	 * The feature id for the '<em><b>Records</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_AREA__RECORDS = 4;

	/**
	 * The feature id for the '<em><b>Schema</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_AREA__SCHEMA = 5;

	/**
	 * The number of structural features of the '<em>Area</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_AREA_FEATURE_COUNT = 6;

	/**
	 * The feature id for the '<em><b>Diagram Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__DIAGRAM_LOCATION = DIAGRAM_NODE__DIAGRAM_LOCATION;

	/**
	 * The feature id for the '<em><b>Control Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__CONTROL_LENGTH = DIAGRAM_NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Data Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__DATA_LENGTH = DIAGRAM_NODE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Fragmented</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__FRAGMENTED = DIAGRAM_NODE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__ID = DIAGRAM_NODE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Location Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__LOCATION_MODE = DIAGRAM_NODE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Minimum Fragment Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__MINIMUM_FRAGMENT_LENGTH = DIAGRAM_NODE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Minimum Root Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__MINIMUM_ROOT_LENGTH = DIAGRAM_NODE_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__NAME = DIAGRAM_NODE_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Prefix Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__PREFIX_LENGTH = DIAGRAM_NODE_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Storage Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__STORAGE_MODE = DIAGRAM_NODE_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Area Specification</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__AREA_SPECIFICATION = DIAGRAM_NODE_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Calc Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__CALC_KEY = DIAGRAM_NODE_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__ELEMENTS = DIAGRAM_NODE_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Keys</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__KEYS = DIAGRAM_NODE_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Member Roles</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__MEMBER_ROLES = DIAGRAM_NODE_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>Owner Roles</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__OWNER_ROLES = DIAGRAM_NODE_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Procedures</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__PROCEDURES = DIAGRAM_NODE_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>Roles</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__ROLES = DIAGRAM_NODE_FEATURE_COUNT + 17;

	/**
	 * The feature id for the '<em><b>Root Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__ROOT_ELEMENTS = DIAGRAM_NODE_FEATURE_COUNT + 18;

	/**
	 * The feature id for the '<em><b>Schema</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__SCHEMA = DIAGRAM_NODE_FEATURE_COUNT + 19;

	/**
	 * The feature id for the '<em><b>Via Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD__VIA_SPECIFICATION = DIAGRAM_NODE_FEATURE_COUNT + 20;

	/**
	 * The number of structural features of the '<em>Record</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEMA_RECORD_FEATURE_COUNT = DIAGRAM_NODE_FEATURE_COUNT + 21;

	/**
	 * The feature id for the '<em><b>Indexed Set Mode Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__INDEXED_SET_MODE_SPECIFICATION = 0;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__MEMBERS = 1;

	/**
	 * The feature id for the '<em><b>Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__MODE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__NAME = 3;

	/**
	 * The feature id for the '<em><b>Order</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__ORDER = 4;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__OWNER = 5;

	/**
	 * The feature id for the '<em><b>Schema</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__SCHEMA = 6;

	/**
	 * The feature id for the '<em><b>System Owner</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__SYSTEM_OWNER = 7;

	/**
	 * The feature id for the '<em><b>Via Members</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET__VIA_MEMBERS = 8;

	/**
	 * The number of structural features of the '<em>Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET_FEATURE_COUNT = 9;

	/**
	 * The feature id for the '<em><b>Diagram Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYSTEM_OWNER__DIAGRAM_LOCATION = DIAGRAM_NODE__DIAGRAM_LOCATION;

	/**
	 * The feature id for the '<em><b>Area Specification</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYSTEM_OWNER__AREA_SPECIFICATION = DIAGRAM_NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Set</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYSTEM_OWNER__SET = DIAGRAM_NODE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>System Owner</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYSTEM_OWNER_FEATURE_COUNT = DIAGRAM_NODE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Displacement Page Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIA_SPECIFICATION__DISPLACEMENT_PAGE_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Record</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIA_SPECIFICATION__RECORD = 1;

	/**
	 * The feature id for the '<em><b>Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIA_SPECIFICATION__SET = 2;

	/**
	 * The feature id for the '<em><b>Symbolic Displacement Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIA_SPECIFICATION__SYMBOLIC_DISPLACEMENT_NAME = 3;

	/**
	 * The number of structural features of the '<em>Via Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIA_SPECIFICATION_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.SetOrder <em>Set Order</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.SetOrder
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSetOrder()
	 * @generated
	 */
	int SET_ORDER = 36;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.DuplicatesOption <em>Duplicates Option</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.DuplicatesOption
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getDuplicatesOption()
	 * @generated
	 */
	int DUPLICATES_OPTION = 28;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.LabelAlignment <em>Label Alignment</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.LabelAlignment
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getLabelAlignment()
	 * @generated
	 */
	int LABEL_ALIGNMENT = 29;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.LocationMode <em>Location Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.LocationMode
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getLocationMode()
	 * @generated
	 */
	int LOCATION_MODE = 30;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.SetMode <em>Set Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.SetMode
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSetMode()
	 * @generated
	 */
	int SET_MODE = 35;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.StorageMode <em>Storage Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.StorageMode
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getStorageMode()
	 * @generated
	 */
	int STORAGE_MODE = 38;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.Unit <em>Unit</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.Unit
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getUnit()
	 * @generated
	 */
	int UNIT = 39;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.SetMembershipOption <em>Set Membership Option</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.SetMembershipOption
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSetMembershipOption()
	 * @generated
	 */
	int SET_MEMBERSHIP_OPTION = 34;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.SortSequence <em>Sort Sequence</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.SortSequence
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSortSequence()
	 * @generated
	 */
	int SORT_SEQUENCE = 37;


	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.ProcedureCallTime <em>Procedure Call Time</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.ProcedureCallTime
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getProcedureCallTime()
	 * @generated
	 */
	int PROCEDURE_CALL_TIME = 31;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.AreaProcedureCallFunction <em>Area Procedure Call Function</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.AreaProcedureCallFunction
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getAreaProcedureCallFunction()
	 * @generated
	 */
	int AREA_PROCEDURE_CALL_FUNCTION = 27;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.RecordProcedureCallVerb <em>Record Procedure Call Verb</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.RecordProcedureCallVerb
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getRecordProcedureCallVerb()
	 * @generated
	 */
	int RECORD_PROCEDURE_CALL_VERB = 32;


	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.RulerType <em>Ruler Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.RulerType
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getRulerType()
	 * @generated
	 */
	int RULER_TYPE = 33;

	/**
	 * The meta object id for the '{@link org.lh.dmlj.schema.Usage <em>Usage</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.lh.dmlj.schema.Usage
	 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getUsage()
	 * @generated
	 */
	int USAGE = 40;


	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.Schema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Schema</em>'.
	 * @see org.lh.dmlj.schema.Schema
	 * @generated
	 */
	EClass getSchema();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Schema#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.lh.dmlj.schema.Schema#getName()
	 * @see #getSchema()
	 * @generated
	 */
	EAttribute getSchema_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Schema#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see org.lh.dmlj.schema.Schema#getVersion()
	 * @see #getSchema()
	 * @generated
	 */
	EAttribute getSchema_Version();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Schema#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.lh.dmlj.schema.Schema#getDescription()
	 * @see #getSchema()
	 * @generated
	 */
	EAttribute getSchema_Description();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Schema#getMemoDate <em>Memo Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Memo Date</em>'.
	 * @see org.lh.dmlj.schema.Schema#getMemoDate()
	 * @see #getSchema()
	 * @generated
	 */
	EAttribute getSchema_MemoDate();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.Schema#getAreas <em>Areas</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Areas</em>'.
	 * @see org.lh.dmlj.schema.Schema#getAreas()
	 * @see #getSchema()
	 * @generated
	 */
	EReference getSchema_Areas();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.Schema#getRecords <em>Records</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Records</em>'.
	 * @see org.lh.dmlj.schema.Schema#getRecords()
	 * @see #getSchema()
	 * @generated
	 */
	EReference getSchema_Records();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.Schema#getSets <em>Sets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sets</em>'.
	 * @see org.lh.dmlj.schema.Schema#getSets()
	 * @see #getSchema()
	 * @generated
	 */
	EReference getSchema_Sets();

	/**
	 * Returns the meta object for the containment reference '{@link org.lh.dmlj.schema.Schema#getDiagramData <em>Diagram Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Diagram Data</em>'.
	 * @see org.lh.dmlj.schema.Schema#getDiagramData()
	 * @see #getSchema()
	 * @generated
	 */
	EReference getSchema_DiagramData();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.Schema#getProcedures <em>Procedures</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Procedures</em>'.
	 * @see org.lh.dmlj.schema.Schema#getProcedures()
	 * @see #getSchema()
	 * @generated
	 */
	EReference getSchema_Procedures();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.SchemaArea <em>Area</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Area</em>'.
	 * @see org.lh.dmlj.schema.SchemaArea
	 * @generated
	 */
	EClass getSchemaArea();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.SchemaArea#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.lh.dmlj.schema.SchemaArea#getName()
	 * @see #getSchemaArea()
	 * @generated
	 */
	EAttribute getSchemaArea_Name();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.SchemaArea#getSchema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Schema</em>'.
	 * @see org.lh.dmlj.schema.SchemaArea#getSchema()
	 * @see #getSchemaArea()
	 * @generated
	 */
	EReference getSchemaArea_Schema();

	/**
	 * Returns the meta object for the reference list '{@link org.lh.dmlj.schema.SchemaArea#getIndexes <em>Indexes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Indexes</em>'.
	 * @see org.lh.dmlj.schema.SchemaArea#getIndexes()
	 * @see #getSchemaArea()
	 * @generated
	 */
	EReference getSchemaArea_Indexes();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.SchemaArea#getProcedures <em>Procedures</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Procedures</em>'.
	 * @see org.lh.dmlj.schema.SchemaArea#getProcedures()
	 * @see #getSchemaArea()
	 * @generated
	 */
	EReference getSchemaArea_Procedures();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.SchemaArea#getAreaSpecifications <em>Area Specifications</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Area Specifications</em>'.
	 * @see org.lh.dmlj.schema.SchemaArea#getAreaSpecifications()
	 * @see #getSchemaArea()
	 * @generated
	 */
	EReference getSchemaArea_AreaSpecifications();

	/**
	 * Returns the meta object for the reference list '{@link org.lh.dmlj.schema.SchemaArea#getRecords <em>Records</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Records</em>'.
	 * @see org.lh.dmlj.schema.SchemaArea#getRecords()
	 * @see #getSchemaArea()
	 * @generated
	 */
	EReference getSchemaArea_Records();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.SchemaRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Record</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord
	 * @generated
	 */
	EClass getSchemaRecord();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.SchemaRecord#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getName()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EAttribute getSchemaRecord_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.SchemaRecord#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getId()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EAttribute getSchemaRecord_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.SchemaRecord#getControlLength <em>Control Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Control Length</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getControlLength()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EAttribute getSchemaRecord_ControlLength();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.SchemaRecord#getDataLength <em>Data Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data Length</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getDataLength()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EAttribute getSchemaRecord_DataLength();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.SchemaRecord#getStorageMode <em>Storage Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Storage Mode</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getStorageMode()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EAttribute getSchemaRecord_StorageMode();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.SchemaRecord#getLocationMode <em>Location Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location Mode</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getLocationMode()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EAttribute getSchemaRecord_LocationMode();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.SchemaRecord#getMinimumRootLength <em>Minimum Root Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minimum Root Length</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getMinimumRootLength()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EAttribute getSchemaRecord_MinimumRootLength();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.SchemaRecord#getMinimumFragmentLength <em>Minimum Fragment Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minimum Fragment Length</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getMinimumFragmentLength()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EAttribute getSchemaRecord_MinimumFragmentLength();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.SchemaRecord#getPrefixLength <em>Prefix Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prefix Length</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getPrefixLength()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EAttribute getSchemaRecord_PrefixLength();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.SchemaRecord#isFragmented <em>Fragmented</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fragmented</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#isFragmented()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EAttribute getSchemaRecord_Fragmented();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.SchemaRecord#getCalcKey <em>Calc Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Calc Key</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getCalcKey()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EReference getSchemaRecord_CalcKey();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.SchemaRecord#getSchema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Schema</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getSchema()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EReference getSchemaRecord_Schema();

	/**
	 * Returns the meta object for the containment reference '{@link org.lh.dmlj.schema.SchemaRecord#getViaSpecification <em>Via Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Via Specification</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getViaSpecification()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EReference getSchemaRecord_ViaSpecification();

	/**
	 * Returns the meta object for the reference list '{@link org.lh.dmlj.schema.SchemaRecord#getOwnerRoles <em>Owner Roles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Owner Roles</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getOwnerRoles()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EReference getSchemaRecord_OwnerRoles();

	/**
	 * Returns the meta object for the reference list '{@link org.lh.dmlj.schema.SchemaRecord#getMemberRoles <em>Member Roles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Member Roles</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getMemberRoles()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EReference getSchemaRecord_MemberRoles();

	/**
	 * Returns the meta object for the reference list '{@link org.lh.dmlj.schema.SchemaRecord#getRootElements <em>Root Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Root Elements</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getRootElements()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EReference getSchemaRecord_RootElements();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.SchemaRecord#getProcedures <em>Procedures</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Procedures</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getProcedures()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EReference getSchemaRecord_Procedures();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.SchemaRecord#getKeys <em>Keys</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Keys</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getKeys()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EReference getSchemaRecord_Keys();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.SchemaRecord#getAreaSpecification <em>Area Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Area Specification</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getAreaSpecification()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EReference getSchemaRecord_AreaSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.SchemaRecord#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Elements</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getElements()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EReference getSchemaRecord_Elements();

	/**
	 * Returns the meta object for the reference list '{@link org.lh.dmlj.schema.SchemaRecord#getRoles <em>Roles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Roles</em>'.
	 * @see org.lh.dmlj.schema.SchemaRecord#getRoles()
	 * @see #getSchemaRecord()
	 * @generated
	 */
	EReference getSchemaRecord_Roles();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.Set <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Set</em>'.
	 * @see org.lh.dmlj.schema.Set
	 * @generated
	 */
	EClass getSet();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Set#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.lh.dmlj.schema.Set#getName()
	 * @see #getSet()
	 * @generated
	 */
	EAttribute getSet_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Set#getMode <em>Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mode</em>'.
	 * @see org.lh.dmlj.schema.Set#getMode()
	 * @see #getSet()
	 * @generated
	 */
	EAttribute getSet_Mode();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Set#getOrder <em>Order</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Order</em>'.
	 * @see org.lh.dmlj.schema.Set#getOrder()
	 * @see #getSet()
	 * @generated
	 */
	EAttribute getSet_Order();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.Set#getSchema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Schema</em>'.
	 * @see org.lh.dmlj.schema.Set#getSchema()
	 * @see #getSet()
	 * @generated
	 */
	EReference getSet_Schema();

	/**
	 * Returns the meta object for the containment reference '{@link org.lh.dmlj.schema.Set#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owner</em>'.
	 * @see org.lh.dmlj.schema.Set#getOwner()
	 * @see #getSet()
	 * @generated
	 */
	EReference getSet_Owner();

	/**
	 * Returns the meta object for the containment reference '{@link org.lh.dmlj.schema.Set#getSystemOwner <em>System Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>System Owner</em>'.
	 * @see org.lh.dmlj.schema.Set#getSystemOwner()
	 * @see #getSet()
	 * @generated
	 */
	EReference getSet_SystemOwner();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.Set#getMembers <em>Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Members</em>'.
	 * @see org.lh.dmlj.schema.Set#getMembers()
	 * @see #getSet()
	 * @generated
	 */
	EReference getSet_Members();

	/**
	 * Returns the meta object for the reference list '{@link org.lh.dmlj.schema.Set#getViaMembers <em>Via Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Via Members</em>'.
	 * @see org.lh.dmlj.schema.Set#getViaMembers()
	 * @see #getSet()
	 * @generated
	 */
	EReference getSet_ViaMembers();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.Role <em>Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Role</em>'.
	 * @see org.lh.dmlj.schema.Role
	 * @generated
	 */
	EClass getRole();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.Ruler <em>Ruler</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ruler</em>'.
	 * @see org.lh.dmlj.schema.Ruler
	 * @generated
	 */
	EClass getRuler();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.Ruler#getGuides <em>Guides</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Guides</em>'.
	 * @see org.lh.dmlj.schema.Ruler#getGuides()
	 * @see #getRuler()
	 * @generated
	 */
	EReference getRuler_Guides();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Ruler#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.lh.dmlj.schema.Ruler#getType()
	 * @see #getRuler()
	 * @generated
	 */
	EAttribute getRuler_Type();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.Ruler#getDiagramData <em>Diagram Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Diagram Data</em>'.
	 * @see org.lh.dmlj.schema.Ruler#getDiagramData()
	 * @see #getRuler()
	 * @generated
	 */
	EReference getRuler_DiagramData();

	/**
	 * Returns the meta object for the containment reference '{@link org.lh.dmlj.schema.Set#getIndexedSetModeSpecification <em>Indexed Set Mode Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Indexed Set Mode Specification</em>'.
	 * @see org.lh.dmlj.schema.Set#getIndexedSetModeSpecification()
	 * @see #getSet()
	 * @generated
	 */
	EReference getSet_IndexedSetModeSpecification();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.Element <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.lh.dmlj.schema.Element
	 * @generated
	 */
	EClass getElement();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Element#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.lh.dmlj.schema.Element#getName()
	 * @see #getElement()
	 * @generated
	 */
	EAttribute getElement_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Element#getLevel <em>Level</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Level</em>'.
	 * @see org.lh.dmlj.schema.Element#getLevel()
	 * @see #getElement()
	 * @generated
	 */
	EAttribute getElement_Level();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Element#getUsage <em>Usage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Usage</em>'.
	 * @see org.lh.dmlj.schema.Element#getUsage()
	 * @see #getElement()
	 * @generated
	 */
	EAttribute getElement_Usage();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.Guide <em>Guide</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Guide</em>'.
	 * @see org.lh.dmlj.schema.Guide
	 * @generated
	 */
	EClass getGuide();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Guide#getPosition <em>Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Position</em>'.
	 * @see org.lh.dmlj.schema.Guide#getPosition()
	 * @see #getGuide()
	 * @generated
	 */
	EAttribute getGuide_Position();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Element#getOffset <em>Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Offset</em>'.
	 * @see org.lh.dmlj.schema.Element#getOffset()
	 * @see #getElement()
	 * @generated
	 */
	EAttribute getElement_Offset();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Element#getLength <em>Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Length</em>'.
	 * @see org.lh.dmlj.schema.Element#getLength()
	 * @see #getElement()
	 * @generated
	 */
	EAttribute getElement_Length();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Element#getPicture <em>Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Picture</em>'.
	 * @see org.lh.dmlj.schema.Element#getPicture()
	 * @see #getElement()
	 * @generated
	 */
	EAttribute getElement_Picture();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Element#isNullable <em>Nullable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Nullable</em>'.
	 * @see org.lh.dmlj.schema.Element#isNullable()
	 * @see #getElement()
	 * @generated
	 */
	EAttribute getElement_Nullable();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.Element#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Record</em>'.
	 * @see org.lh.dmlj.schema.Element#getRecord()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_Record();

	/**
	 * Returns the meta object for the reference list '{@link org.lh.dmlj.schema.Element#getKeyElements <em>Key Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Key Elements</em>'.
	 * @see org.lh.dmlj.schema.Element#getKeyElements()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_KeyElements();

	/**
	 * Returns the meta object for the reference list '{@link org.lh.dmlj.schema.Element#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Children</em>'.
	 * @see org.lh.dmlj.schema.Element#getChildren()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_Children();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.Element#getRedefines <em>Redefines</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Redefines</em>'.
	 * @see org.lh.dmlj.schema.Element#getRedefines()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_Redefines();

	/**
	 * Returns the meta object for the containment reference '{@link org.lh.dmlj.schema.Element#getOccursSpecification <em>Occurs Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Occurs Specification</em>'.
	 * @see org.lh.dmlj.schema.Element#getOccursSpecification()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_OccursSpecification();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.Element#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parent</em>'.
	 * @see org.lh.dmlj.schema.Element#getParent()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_Parent();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.KeyElement <em>Key Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Key Element</em>'.
	 * @see org.lh.dmlj.schema.KeyElement
	 * @generated
	 */
	EClass getKeyElement();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.KeyElement#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element</em>'.
	 * @see org.lh.dmlj.schema.KeyElement#getElement()
	 * @see #getKeyElement()
	 * @generated
	 */
	EReference getKeyElement_Element();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.KeyElement#getSortSequence <em>Sort Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sort Sequence</em>'.
	 * @see org.lh.dmlj.schema.KeyElement#getSortSequence()
	 * @see #getKeyElement()
	 * @generated
	 */
	EAttribute getKeyElement_SortSequence();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.KeyElement#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Key</em>'.
	 * @see org.lh.dmlj.schema.KeyElement#getKey()
	 * @see #getKeyElement()
	 * @generated
	 */
	EReference getKeyElement_Key();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.KeyElement#isDbkey <em>Dbkey</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dbkey</em>'.
	 * @see org.lh.dmlj.schema.KeyElement#isDbkey()
	 * @see #getKeyElement()
	 * @generated
	 */
	EAttribute getKeyElement_Dbkey();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.MemberRole <em>Member Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Member Role</em>'.
	 * @see org.lh.dmlj.schema.MemberRole
	 * @generated
	 */
	EClass getMemberRole();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.MemberRole#getIndexDbkeyPosition <em>Index Dbkey Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Index Dbkey Position</em>'.
	 * @see org.lh.dmlj.schema.MemberRole#getIndexDbkeyPosition()
	 * @see #getMemberRole()
	 * @generated
	 */
	EAttribute getMemberRole_IndexDbkeyPosition();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.MemberRole#getMembershipOption <em>Membership Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Membership Option</em>'.
	 * @see org.lh.dmlj.schema.MemberRole#getMembershipOption()
	 * @see #getMemberRole()
	 * @generated
	 */
	EAttribute getMemberRole_MembershipOption();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.MemberRole#getNextDbkeyPosition <em>Next Dbkey Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Next Dbkey Position</em>'.
	 * @see org.lh.dmlj.schema.MemberRole#getNextDbkeyPosition()
	 * @see #getMemberRole()
	 * @generated
	 */
	EAttribute getMemberRole_NextDbkeyPosition();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.MemberRole#getPriorDbkeyPosition <em>Prior Dbkey Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prior Dbkey Position</em>'.
	 * @see org.lh.dmlj.schema.MemberRole#getPriorDbkeyPosition()
	 * @see #getMemberRole()
	 * @generated
	 */
	EAttribute getMemberRole_PriorDbkeyPosition();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.MemberRole#getOwnerDbkeyPosition <em>Owner Dbkey Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Owner Dbkey Position</em>'.
	 * @see org.lh.dmlj.schema.MemberRole#getOwnerDbkeyPosition()
	 * @see #getMemberRole()
	 * @generated
	 */
	EAttribute getMemberRole_OwnerDbkeyPosition();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.MemberRole#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Record</em>'.
	 * @see org.lh.dmlj.schema.MemberRole#getRecord()
	 * @see #getMemberRole()
	 * @generated
	 */
	EReference getMemberRole_Record();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.MemberRole#getSet <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Set</em>'.
	 * @see org.lh.dmlj.schema.MemberRole#getSet()
	 * @see #getMemberRole()
	 * @generated
	 */
	EReference getMemberRole_Set();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.MemberRole#getSortKey <em>Sort Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sort Key</em>'.
	 * @see org.lh.dmlj.schema.MemberRole#getSortKey()
	 * @see #getMemberRole()
	 * @generated
	 */
	EReference getMemberRole_SortKey();

	/**
	 * Returns the meta object for the reference list '{@link org.lh.dmlj.schema.MemberRole#getConnectionParts <em>Connection Parts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Connection Parts</em>'.
	 * @see org.lh.dmlj.schema.MemberRole#getConnectionParts()
	 * @see #getMemberRole()
	 * @generated
	 */
	EReference getMemberRole_ConnectionParts();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.MemberRole#getConnectionLabel <em>Connection Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Connection Label</em>'.
	 * @see org.lh.dmlj.schema.MemberRole#getConnectionLabel()
	 * @see #getMemberRole()
	 * @generated
	 */
	EReference getMemberRole_ConnectionLabel();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.SystemOwner <em>System Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>System Owner</em>'.
	 * @see org.lh.dmlj.schema.SystemOwner
	 * @generated
	 */
	EClass getSystemOwner();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.SystemOwner#getAreaSpecification <em>Area Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Area Specification</em>'.
	 * @see org.lh.dmlj.schema.SystemOwner#getAreaSpecification()
	 * @see #getSystemOwner()
	 * @generated
	 */
	EReference getSystemOwner_AreaSpecification();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.SystemOwner#getSet <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Set</em>'.
	 * @see org.lh.dmlj.schema.SystemOwner#getSet()
	 * @see #getSystemOwner()
	 * @generated
	 */
	EReference getSystemOwner_Set();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.ViaSpecification <em>Via Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Via Specification</em>'.
	 * @see org.lh.dmlj.schema.ViaSpecification
	 * @generated
	 */
	EClass getViaSpecification();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.ViaSpecification#getSymbolicDisplacementName <em>Symbolic Displacement Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Symbolic Displacement Name</em>'.
	 * @see org.lh.dmlj.schema.ViaSpecification#getSymbolicDisplacementName()
	 * @see #getViaSpecification()
	 * @generated
	 */
	EAttribute getViaSpecification_SymbolicDisplacementName();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.ViaSpecification#getSet <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Set</em>'.
	 * @see org.lh.dmlj.schema.ViaSpecification#getSet()
	 * @see #getViaSpecification()
	 * @generated
	 */
	EReference getViaSpecification_Set();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.DiagramNode <em>Diagram Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Node</em>'.
	 * @see org.lh.dmlj.schema.DiagramNode
	 * @generated
	 */
	EClass getDiagramNode();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.DiagramNode#getDiagramLocation <em>Diagram Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Diagram Location</em>'.
	 * @see org.lh.dmlj.schema.DiagramNode#getDiagramLocation()
	 * @see #getDiagramNode()
	 * @generated
	 */
	EReference getDiagramNode_DiagramLocation();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.DiagramLocation <em>Diagram Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Location</em>'.
	 * @see org.lh.dmlj.schema.DiagramLocation
	 * @generated
	 */
	EClass getDiagramLocation();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.DiagramLocation#getX <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see org.lh.dmlj.schema.DiagramLocation#getX()
	 * @see #getDiagramLocation()
	 * @generated
	 */
	EAttribute getDiagramLocation_X();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.DiagramLocation#getY <em>Y</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Y</em>'.
	 * @see org.lh.dmlj.schema.DiagramLocation#getY()
	 * @see #getDiagramLocation()
	 * @generated
	 */
	EAttribute getDiagramLocation_Y();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.DiagramLocation#getEyecatcher <em>Eyecatcher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Eyecatcher</em>'.
	 * @see org.lh.dmlj.schema.DiagramLocation#getEyecatcher()
	 * @see #getDiagramLocation()
	 * @generated
	 */
	EAttribute getDiagramLocation_Eyecatcher();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.DiagramData <em>Diagram Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram Data</em>'.
	 * @see org.lh.dmlj.schema.DiagramData
	 * @generated
	 */
	EClass getDiagramData();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.DiagramData#getConnectionLabels <em>Connection Labels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Connection Labels</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#getConnectionLabels()
	 * @see #getDiagramData()
	 * @generated
	 */
	EReference getDiagramData_ConnectionLabels();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.DiagramData#getConnectionParts <em>Connection Parts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Connection Parts</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#getConnectionParts()
	 * @see #getDiagramData()
	 * @generated
	 */
	EReference getDiagramData_ConnectionParts();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.DiagramData#getConnectors <em>Connectors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Connectors</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#getConnectors()
	 * @see #getDiagramData()
	 * @generated
	 */
	EReference getDiagramData_Connectors();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.DiagramData#getHorizontalRuler <em>Horizontal Ruler</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Horizontal Ruler</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#getHorizontalRuler()
	 * @see #getDiagramData()
	 * @generated
	 */
	EReference getDiagramData_HorizontalRuler();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.DiagramData#getLocations <em>Locations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Locations</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#getLocations()
	 * @see #getDiagramData()
	 * @generated
	 */
	EReference getDiagramData_Locations();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.DiagramData#getZoomLevel <em>Zoom Level</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Zoom Level</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#getZoomLevel()
	 * @see #getDiagramData()
	 * @generated
	 */
	EAttribute getDiagramData_ZoomLevel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.DiagramData#getRulers <em>Rulers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rulers</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#getRulers()
	 * @see #getDiagramData()
	 * @generated
	 */
	EReference getDiagramData_Rulers();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.DiagramData#isShowGrid <em>Show Grid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Show Grid</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#isShowGrid()
	 * @see #getDiagramData()
	 * @generated
	 */
	EAttribute getDiagramData_ShowGrid();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.DiagramData#isShowRulers <em>Show Rulers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Show Rulers</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#isShowRulers()
	 * @see #getDiagramData()
	 * @generated
	 */
	EAttribute getDiagramData_ShowRulers();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.DiagramData#isSnapToGeometry <em>Snap To Geometry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Snap To Geometry</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#isSnapToGeometry()
	 * @see #getDiagramData()
	 * @generated
	 */
	EAttribute getDiagramData_SnapToGeometry();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.DiagramData#isSnapToGrid <em>Snap To Grid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Snap To Grid</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#isSnapToGrid()
	 * @see #getDiagramData()
	 * @generated
	 */
	EAttribute getDiagramData_SnapToGrid();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.DiagramData#isSnapToGuides <em>Snap To Guides</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Snap To Guides</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#isSnapToGuides()
	 * @see #getDiagramData()
	 * @generated
	 */
	EAttribute getDiagramData_SnapToGuides();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.DiagramData#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#getUnit()
	 * @see #getDiagramData()
	 * @generated
	 */
	EAttribute getDiagramData_Unit();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.DiagramData#getVerticalRuler <em>Vertical Ruler</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vertical Ruler</em>'.
	 * @see org.lh.dmlj.schema.DiagramData#getVerticalRuler()
	 * @see #getDiagramData()
	 * @generated
	 */
	EReference getDiagramData_VerticalRuler();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.ViaSpecification#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Record</em>'.
	 * @see org.lh.dmlj.schema.ViaSpecification#getRecord()
	 * @see #getViaSpecification()
	 * @generated
	 */
	EReference getViaSpecification_Record();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.ViaSpecification#getDisplacementPageCount <em>Displacement Page Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Displacement Page Count</em>'.
	 * @see org.lh.dmlj.schema.ViaSpecification#getDisplacementPageCount()
	 * @see #getViaSpecification()
	 * @generated
	 */
	EAttribute getViaSpecification_DisplacementPageCount();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.Key <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Key</em>'.
	 * @see org.lh.dmlj.schema.Key
	 * @generated
	 */
	EClass getKey();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Key#isCalcKey <em>Calc Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Calc Key</em>'.
	 * @see org.lh.dmlj.schema.Key#isCalcKey()
	 * @see #getKey()
	 * @generated
	 */
	EAttribute getKey_CalcKey();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Key#getLength <em>Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Length</em>'.
	 * @see org.lh.dmlj.schema.Key#getLength()
	 * @see #getKey()
	 * @generated
	 */
	EAttribute getKey_Length();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Key#getDuplicatesOption <em>Duplicates Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duplicates Option</em>'.
	 * @see org.lh.dmlj.schema.Key#getDuplicatesOption()
	 * @see #getKey()
	 * @generated
	 */
	EAttribute getKey_DuplicatesOption();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Key#isCompressed <em>Compressed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Compressed</em>'.
	 * @see org.lh.dmlj.schema.Key#isCompressed()
	 * @see #getKey()
	 * @generated
	 */
	EAttribute getKey_Compressed();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Key#isNaturalSequence <em>Natural Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Natural Sequence</em>'.
	 * @see org.lh.dmlj.schema.Key#isNaturalSequence()
	 * @see #getKey()
	 * @generated
	 */
	EAttribute getKey_NaturalSequence();

	/**
	 * Returns the meta object for the containment reference list '{@link org.lh.dmlj.schema.Key#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Elements</em>'.
	 * @see org.lh.dmlj.schema.Key#getElements()
	 * @see #getKey()
	 * @generated
	 */
	EReference getKey_Elements();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Key#getElementSummary <em>Element Summary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Element Summary</em>'.
	 * @see org.lh.dmlj.schema.Key#getElementSummary()
	 * @see #getKey()
	 * @generated
	 */
	EAttribute getKey_ElementSummary();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.Key#getMemberRole <em>Member Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Member Role</em>'.
	 * @see org.lh.dmlj.schema.Key#getMemberRole()
	 * @see #getKey()
	 * @generated
	 */
	EReference getKey_MemberRole();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.Key#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Record</em>'.
	 * @see org.lh.dmlj.schema.Key#getRecord()
	 * @see #getKey()
	 * @generated
	 */
	EReference getKey_Record();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.AreaSpecification <em>Area Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Area Specification</em>'.
	 * @see org.lh.dmlj.schema.AreaSpecification
	 * @generated
	 */
	EClass getAreaSpecification();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.AreaSpecification#getSymbolicSubareaName <em>Symbolic Subarea Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Symbolic Subarea Name</em>'.
	 * @see org.lh.dmlj.schema.AreaSpecification#getSymbolicSubareaName()
	 * @see #getAreaSpecification()
	 * @generated
	 */
	EAttribute getAreaSpecification_SymbolicSubareaName();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.AreaSpecification#getArea <em>Area</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Area</em>'.
	 * @see org.lh.dmlj.schema.AreaSpecification#getArea()
	 * @see #getAreaSpecification()
	 * @generated
	 */
	EReference getAreaSpecification_Area();

	/**
	 * Returns the meta object for the containment reference '{@link org.lh.dmlj.schema.AreaSpecification#getOffsetExpression <em>Offset Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Offset Expression</em>'.
	 * @see org.lh.dmlj.schema.AreaSpecification#getOffsetExpression()
	 * @see #getAreaSpecification()
	 * @generated
	 */
	EReference getAreaSpecification_OffsetExpression();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.AreaSpecification#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Record</em>'.
	 * @see org.lh.dmlj.schema.AreaSpecification#getRecord()
	 * @see #getAreaSpecification()
	 * @generated
	 */
	EReference getAreaSpecification_Record();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.AreaSpecification#getSystemOwner <em>System Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>System Owner</em>'.
	 * @see org.lh.dmlj.schema.AreaSpecification#getSystemOwner()
	 * @see #getAreaSpecification()
	 * @generated
	 */
	EReference getAreaSpecification_SystemOwner();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.ConnectionPart <em>Connection Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connection Part</em>'.
	 * @see org.lh.dmlj.schema.ConnectionPart
	 * @generated
	 */
	EClass getConnectionPart();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.ConnectionPart#getConnector <em>Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Connector</em>'.
	 * @see org.lh.dmlj.schema.ConnectionPart#getConnector()
	 * @see #getConnectionPart()
	 * @generated
	 */
	EReference getConnectionPart_Connector();

	/**
	 * Returns the meta object for the reference list '{@link org.lh.dmlj.schema.ConnectionPart#getBendpointLocations <em>Bendpoint Locations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Bendpoint Locations</em>'.
	 * @see org.lh.dmlj.schema.ConnectionPart#getBendpointLocations()
	 * @see #getConnectionPart()
	 * @generated
	 */
	EReference getConnectionPart_BendpointLocations();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.ConnectionPart#getMemberRole <em>Member Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Member Role</em>'.
	 * @see org.lh.dmlj.schema.ConnectionPart#getMemberRole()
	 * @see #getConnectionPart()
	 * @generated
	 */
	EReference getConnectionPart_MemberRole();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.ConnectionPart#getSourceEndpointLocation <em>Source Endpoint Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source Endpoint Location</em>'.
	 * @see org.lh.dmlj.schema.ConnectionPart#getSourceEndpointLocation()
	 * @see #getConnectionPart()
	 * @generated
	 */
	EReference getConnectionPart_SourceEndpointLocation();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.ConnectionPart#getTargetEndpointLocation <em>Target Endpoint Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target Endpoint Location</em>'.
	 * @see org.lh.dmlj.schema.ConnectionPart#getTargetEndpointLocation()
	 * @see #getConnectionPart()
	 * @generated
	 */
	EReference getConnectionPart_TargetEndpointLocation();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.ConnectionLabel <em>Connection Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connection Label</em>'.
	 * @see org.lh.dmlj.schema.ConnectionLabel
	 * @generated
	 */
	EClass getConnectionLabel();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.ConnectionLabel#getAlignment <em>Alignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alignment</em>'.
	 * @see org.lh.dmlj.schema.ConnectionLabel#getAlignment()
	 * @see #getConnectionLabel()
	 * @generated
	 */
	EAttribute getConnectionLabel_Alignment();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.ConnectionLabel#getMemberRole <em>Member Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Member Role</em>'.
	 * @see org.lh.dmlj.schema.ConnectionLabel#getMemberRole()
	 * @see #getConnectionLabel()
	 * @generated
	 */
	EReference getConnectionLabel_MemberRole();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.Connector <em>Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connector</em>'.
	 * @see org.lh.dmlj.schema.Connector
	 * @generated
	 */
	EClass getConnector();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.Connector#getConnectionPart <em>Connection Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Connection Part</em>'.
	 * @see org.lh.dmlj.schema.Connector#getConnectionPart()
	 * @see #getConnector()
	 * @generated
	 */
	EReference getConnector_ConnectionPart();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Connector#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see org.lh.dmlj.schema.Connector#getLabel()
	 * @see #getConnector()
	 * @generated
	 */
	EAttribute getConnector_Label();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.OffsetExpression <em>Offset Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Offset Expression</em>'.
	 * @see org.lh.dmlj.schema.OffsetExpression
	 * @generated
	 */
	EClass getOffsetExpression();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.OffsetExpression#getAreaSpecification <em>Area Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Area Specification</em>'.
	 * @see org.lh.dmlj.schema.OffsetExpression#getAreaSpecification()
	 * @see #getOffsetExpression()
	 * @generated
	 */
	EReference getOffsetExpression_AreaSpecification();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.OffsetExpression#getOffsetPageCount <em>Offset Page Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Offset Page Count</em>'.
	 * @see org.lh.dmlj.schema.OffsetExpression#getOffsetPageCount()
	 * @see #getOffsetExpression()
	 * @generated
	 */
	EAttribute getOffsetExpression_OffsetPageCount();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.OffsetExpression#getOffsetPercent <em>Offset Percent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Offset Percent</em>'.
	 * @see org.lh.dmlj.schema.OffsetExpression#getOffsetPercent()
	 * @see #getOffsetExpression()
	 * @generated
	 */
	EAttribute getOffsetExpression_OffsetPercent();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.OffsetExpression#getPageCount <em>Page Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Page Count</em>'.
	 * @see org.lh.dmlj.schema.OffsetExpression#getPageCount()
	 * @see #getOffsetExpression()
	 * @generated
	 */
	EAttribute getOffsetExpression_PageCount();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.OffsetExpression#getPercent <em>Percent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Percent</em>'.
	 * @see org.lh.dmlj.schema.OffsetExpression#getPercent()
	 * @see #getOffsetExpression()
	 * @generated
	 */
	EAttribute getOffsetExpression_Percent();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.OwnerRole <em>Owner Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Owner Role</em>'.
	 * @see org.lh.dmlj.schema.OwnerRole
	 * @generated
	 */
	EClass getOwnerRole();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.OwnerRole#getNextDbkeyPosition <em>Next Dbkey Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Next Dbkey Position</em>'.
	 * @see org.lh.dmlj.schema.OwnerRole#getNextDbkeyPosition()
	 * @see #getOwnerRole()
	 * @generated
	 */
	EAttribute getOwnerRole_NextDbkeyPosition();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.OwnerRole#getPriorDbkeyPosition <em>Prior Dbkey Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prior Dbkey Position</em>'.
	 * @see org.lh.dmlj.schema.OwnerRole#getPriorDbkeyPosition()
	 * @see #getOwnerRole()
	 * @generated
	 */
	EAttribute getOwnerRole_PriorDbkeyPosition();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.OwnerRole#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Record</em>'.
	 * @see org.lh.dmlj.schema.OwnerRole#getRecord()
	 * @see #getOwnerRole()
	 * @generated
	 */
	EReference getOwnerRole_Record();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.OwnerRole#getSet <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Set</em>'.
	 * @see org.lh.dmlj.schema.OwnerRole#getSet()
	 * @see #getOwnerRole()
	 * @generated
	 */
	EReference getOwnerRole_Set();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.IndexedSetModeSpecification <em>Indexed Set Mode Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Indexed Set Mode Specification</em>'.
	 * @see org.lh.dmlj.schema.IndexedSetModeSpecification
	 * @generated
	 */
	EClass getIndexedSetModeSpecification();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getSymbolicIndexName <em>Symbolic Index Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Symbolic Index Name</em>'.
	 * @see org.lh.dmlj.schema.IndexedSetModeSpecification#getSymbolicIndexName()
	 * @see #getIndexedSetModeSpecification()
	 * @generated
	 */
	EAttribute getIndexedSetModeSpecification_SymbolicIndexName();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getKeyCount <em>Key Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key Count</em>'.
	 * @see org.lh.dmlj.schema.IndexedSetModeSpecification#getKeyCount()
	 * @see #getIndexedSetModeSpecification()
	 * @generated
	 */
	EAttribute getIndexedSetModeSpecification_KeyCount();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getSet <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Set</em>'.
	 * @see org.lh.dmlj.schema.IndexedSetModeSpecification#getSet()
	 * @see #getIndexedSetModeSpecification()
	 * @generated
	 */
	EReference getIndexedSetModeSpecification_Set();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.IndexedSetModeSpecification#getDisplacementPageCount <em>Displacement Page Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Displacement Page Count</em>'.
	 * @see org.lh.dmlj.schema.IndexedSetModeSpecification#getDisplacementPageCount()
	 * @see #getIndexedSetModeSpecification()
	 * @generated
	 */
	EAttribute getIndexedSetModeSpecification_DisplacementPageCount();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.AreaProcedureCallSpecification <em>Area Procedure Call Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Area Procedure Call Specification</em>'.
	 * @see org.lh.dmlj.schema.AreaProcedureCallSpecification
	 * @generated
	 */
	EClass getAreaProcedureCallSpecification();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getProcedure <em>Procedure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Procedure</em>'.
	 * @see org.lh.dmlj.schema.AreaProcedureCallSpecification#getProcedure()
	 * @see #getAreaProcedureCallSpecification()
	 * @generated
	 */
	EReference getAreaProcedureCallSpecification_Procedure();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getCallTime <em>Call Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Call Time</em>'.
	 * @see org.lh.dmlj.schema.AreaProcedureCallSpecification#getCallTime()
	 * @see #getAreaProcedureCallSpecification()
	 * @generated
	 */
	EAttribute getAreaProcedureCallSpecification_CallTime();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.AreaProcedureCallSpecification#getFunction <em>Function</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Function</em>'.
	 * @see org.lh.dmlj.schema.AreaProcedureCallSpecification#getFunction()
	 * @see #getAreaProcedureCallSpecification()
	 * @generated
	 */
	EAttribute getAreaProcedureCallSpecification_Function();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.RecordProcedureCallSpecification <em>Record Procedure Call Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Record Procedure Call Specification</em>'.
	 * @see org.lh.dmlj.schema.RecordProcedureCallSpecification
	 * @generated
	 */
	EClass getRecordProcedureCallSpecification();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getProcedure <em>Procedure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Procedure</em>'.
	 * @see org.lh.dmlj.schema.RecordProcedureCallSpecification#getProcedure()
	 * @see #getRecordProcedureCallSpecification()
	 * @generated
	 */
	EReference getRecordProcedureCallSpecification_Procedure();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Record</em>'.
	 * @see org.lh.dmlj.schema.RecordProcedureCallSpecification#getRecord()
	 * @see #getRecordProcedureCallSpecification()
	 * @generated
	 */
	EReference getRecordProcedureCallSpecification_Record();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getCallTime <em>Call Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Call Time</em>'.
	 * @see org.lh.dmlj.schema.RecordProcedureCallSpecification#getCallTime()
	 * @see #getRecordProcedureCallSpecification()
	 * @generated
	 */
	EAttribute getRecordProcedureCallSpecification_CallTime();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getVerb <em>Verb</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Verb</em>'.
	 * @see org.lh.dmlj.schema.RecordProcedureCallSpecification#getVerb()
	 * @see #getRecordProcedureCallSpecification()
	 * @generated
	 */
	EAttribute getRecordProcedureCallSpecification_Verb();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.Procedure <em>Procedure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Procedure</em>'.
	 * @see org.lh.dmlj.schema.Procedure
	 * @generated
	 */
	EClass getProcedure();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.Procedure#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.lh.dmlj.schema.Procedure#getName()
	 * @see #getProcedure()
	 * @generated
	 */
	EAttribute getProcedure_Name();

	/**
	 * Returns the meta object for class '{@link org.lh.dmlj.schema.OccursSpecification <em>Occurs Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Occurs Specification</em>'.
	 * @see org.lh.dmlj.schema.OccursSpecification
	 * @generated
	 */
	EClass getOccursSpecification();

	/**
	 * Returns the meta object for the attribute '{@link org.lh.dmlj.schema.OccursSpecification#getCount <em>Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Count</em>'.
	 * @see org.lh.dmlj.schema.OccursSpecification#getCount()
	 * @see #getOccursSpecification()
	 * @generated
	 */
	EAttribute getOccursSpecification_Count();

	/**
	 * Returns the meta object for the reference '{@link org.lh.dmlj.schema.OccursSpecification#getDependingOn <em>Depending On</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Depending On</em>'.
	 * @see org.lh.dmlj.schema.OccursSpecification#getDependingOn()
	 * @see #getOccursSpecification()
	 * @generated
	 */
	EReference getOccursSpecification_DependingOn();

	/**
	 * Returns the meta object for the container reference '{@link org.lh.dmlj.schema.OccursSpecification#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Element</em>'.
	 * @see org.lh.dmlj.schema.OccursSpecification#getElement()
	 * @see #getOccursSpecification()
	 * @generated
	 */
	EReference getOccursSpecification_Element();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.SetOrder <em>Set Order</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Set Order</em>'.
	 * @see org.lh.dmlj.schema.SetOrder
	 * @generated
	 */
	EEnum getSetOrder();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.DuplicatesOption <em>Duplicates Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Duplicates Option</em>'.
	 * @see org.lh.dmlj.schema.DuplicatesOption
	 * @generated
	 */
	EEnum getDuplicatesOption();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.LabelAlignment <em>Label Alignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Label Alignment</em>'.
	 * @see org.lh.dmlj.schema.LabelAlignment
	 * @generated
	 */
	EEnum getLabelAlignment();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.LocationMode <em>Location Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Location Mode</em>'.
	 * @see org.lh.dmlj.schema.LocationMode
	 * @generated
	 */
	EEnum getLocationMode();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.SetMode <em>Set Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Set Mode</em>'.
	 * @see org.lh.dmlj.schema.SetMode
	 * @generated
	 */
	EEnum getSetMode();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.StorageMode <em>Storage Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Storage Mode</em>'.
	 * @see org.lh.dmlj.schema.StorageMode
	 * @generated
	 */
	EEnum getStorageMode();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.Unit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Unit</em>'.
	 * @see org.lh.dmlj.schema.Unit
	 * @generated
	 */
	EEnum getUnit();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.SetMembershipOption <em>Set Membership Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Set Membership Option</em>'.
	 * @see org.lh.dmlj.schema.SetMembershipOption
	 * @generated
	 */
	EEnum getSetMembershipOption();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.SortSequence <em>Sort Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Sort Sequence</em>'.
	 * @see org.lh.dmlj.schema.SortSequence
	 * @generated
	 */
	EEnum getSortSequence();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.ProcedureCallTime <em>Procedure Call Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Procedure Call Time</em>'.
	 * @see org.lh.dmlj.schema.ProcedureCallTime
	 * @generated
	 */
	EEnum getProcedureCallTime();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.AreaProcedureCallFunction <em>Area Procedure Call Function</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Area Procedure Call Function</em>'.
	 * @see org.lh.dmlj.schema.AreaProcedureCallFunction
	 * @generated
	 */
	EEnum getAreaProcedureCallFunction();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.RecordProcedureCallVerb <em>Record Procedure Call Verb</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Record Procedure Call Verb</em>'.
	 * @see org.lh.dmlj.schema.RecordProcedureCallVerb
	 * @generated
	 */
	EEnum getRecordProcedureCallVerb();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.RulerType <em>Ruler Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Ruler Type</em>'.
	 * @see org.lh.dmlj.schema.RulerType
	 * @generated
	 */
	EEnum getRulerType();

	/**
	 * Returns the meta object for enum '{@link org.lh.dmlj.schema.Usage <em>Usage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Usage</em>'.
	 * @see org.lh.dmlj.schema.Usage
	 * @generated
	 */
	EEnum getUsage();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SchemaFactory getSchemaFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.SchemaImpl <em>Schema</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.SchemaImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSchema()
		 * @generated
		 */
		EClass SCHEMA = eINSTANCE.getSchema();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA__NAME = eINSTANCE.getSchema_Name();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA__VERSION = eINSTANCE.getSchema_Version();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA__DESCRIPTION = eINSTANCE.getSchema_Description();

		/**
		 * The meta object literal for the '<em><b>Memo Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA__MEMO_DATE = eINSTANCE.getSchema_MemoDate();

		/**
		 * The meta object literal for the '<em><b>Areas</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA__AREAS = eINSTANCE.getSchema_Areas();

		/**
		 * The meta object literal for the '<em><b>Records</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA__RECORDS = eINSTANCE.getSchema_Records();

		/**
		 * The meta object literal for the '<em><b>Sets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA__SETS = eINSTANCE.getSchema_Sets();

		/**
		 * The meta object literal for the '<em><b>Diagram Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA__DIAGRAM_DATA = eINSTANCE.getSchema_DiagramData();

		/**
		 * The meta object literal for the '<em><b>Procedures</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA__PROCEDURES = eINSTANCE.getSchema_Procedures();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.SchemaAreaImpl <em>Area</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.SchemaAreaImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSchemaArea()
		 * @generated
		 */
		EClass SCHEMA_AREA = eINSTANCE.getSchemaArea();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA_AREA__NAME = eINSTANCE.getSchemaArea_Name();

		/**
		 * The meta object literal for the '<em><b>Schema</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_AREA__SCHEMA = eINSTANCE.getSchemaArea_Schema();

		/**
		 * The meta object literal for the '<em><b>Indexes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_AREA__INDEXES = eINSTANCE.getSchemaArea_Indexes();

		/**
		 * The meta object literal for the '<em><b>Procedures</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_AREA__PROCEDURES = eINSTANCE.getSchemaArea_Procedures();

		/**
		 * The meta object literal for the '<em><b>Area Specifications</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_AREA__AREA_SPECIFICATIONS = eINSTANCE.getSchemaArea_AreaSpecifications();

		/**
		 * The meta object literal for the '<em><b>Records</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_AREA__RECORDS = eINSTANCE.getSchemaArea_Records();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.SchemaRecordImpl <em>Record</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.SchemaRecordImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSchemaRecord()
		 * @generated
		 */
		EClass SCHEMA_RECORD = eINSTANCE.getSchemaRecord();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA_RECORD__NAME = eINSTANCE.getSchemaRecord_Name();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA_RECORD__ID = eINSTANCE.getSchemaRecord_Id();

		/**
		 * The meta object literal for the '<em><b>Control Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA_RECORD__CONTROL_LENGTH = eINSTANCE.getSchemaRecord_ControlLength();

		/**
		 * The meta object literal for the '<em><b>Data Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA_RECORD__DATA_LENGTH = eINSTANCE.getSchemaRecord_DataLength();

		/**
		 * The meta object literal for the '<em><b>Storage Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA_RECORD__STORAGE_MODE = eINSTANCE.getSchemaRecord_StorageMode();

		/**
		 * The meta object literal for the '<em><b>Location Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA_RECORD__LOCATION_MODE = eINSTANCE.getSchemaRecord_LocationMode();

		/**
		 * The meta object literal for the '<em><b>Minimum Root Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA_RECORD__MINIMUM_ROOT_LENGTH = eINSTANCE.getSchemaRecord_MinimumRootLength();

		/**
		 * The meta object literal for the '<em><b>Minimum Fragment Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA_RECORD__MINIMUM_FRAGMENT_LENGTH = eINSTANCE.getSchemaRecord_MinimumFragmentLength();

		/**
		 * The meta object literal for the '<em><b>Prefix Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA_RECORD__PREFIX_LENGTH = eINSTANCE.getSchemaRecord_PrefixLength();

		/**
		 * The meta object literal for the '<em><b>Fragmented</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEMA_RECORD__FRAGMENTED = eINSTANCE.getSchemaRecord_Fragmented();

		/**
		 * The meta object literal for the '<em><b>Calc Key</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_RECORD__CALC_KEY = eINSTANCE.getSchemaRecord_CalcKey();

		/**
		 * The meta object literal for the '<em><b>Schema</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_RECORD__SCHEMA = eINSTANCE.getSchemaRecord_Schema();

		/**
		 * The meta object literal for the '<em><b>Via Specification</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_RECORD__VIA_SPECIFICATION = eINSTANCE.getSchemaRecord_ViaSpecification();

		/**
		 * The meta object literal for the '<em><b>Owner Roles</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_RECORD__OWNER_ROLES = eINSTANCE.getSchemaRecord_OwnerRoles();

		/**
		 * The meta object literal for the '<em><b>Member Roles</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_RECORD__MEMBER_ROLES = eINSTANCE.getSchemaRecord_MemberRoles();

		/**
		 * The meta object literal for the '<em><b>Root Elements</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_RECORD__ROOT_ELEMENTS = eINSTANCE.getSchemaRecord_RootElements();

		/**
		 * The meta object literal for the '<em><b>Procedures</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_RECORD__PROCEDURES = eINSTANCE.getSchemaRecord_Procedures();

		/**
		 * The meta object literal for the '<em><b>Keys</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_RECORD__KEYS = eINSTANCE.getSchemaRecord_Keys();

		/**
		 * The meta object literal for the '<em><b>Area Specification</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_RECORD__AREA_SPECIFICATION = eINSTANCE.getSchemaRecord_AreaSpecification();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_RECORD__ELEMENTS = eINSTANCE.getSchemaRecord_Elements();

		/**
		 * The meta object literal for the '<em><b>Roles</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEMA_RECORD__ROLES = eINSTANCE.getSchemaRecord_Roles();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.SetImpl <em>Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.SetImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSet()
		 * @generated
		 */
		EClass SET = eINSTANCE.getSet();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SET__NAME = eINSTANCE.getSet_Name();

		/**
		 * The meta object literal for the '<em><b>Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SET__MODE = eINSTANCE.getSet_Mode();

		/**
		 * The meta object literal for the '<em><b>Order</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SET__ORDER = eINSTANCE.getSet_Order();

		/**
		 * The meta object literal for the '<em><b>Schema</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SET__SCHEMA = eINSTANCE.getSet_Schema();

		/**
		 * The meta object literal for the '<em><b>Owner</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SET__OWNER = eINSTANCE.getSet_Owner();

		/**
		 * The meta object literal for the '<em><b>System Owner</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SET__SYSTEM_OWNER = eINSTANCE.getSet_SystemOwner();

		/**
		 * The meta object literal for the '<em><b>Members</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SET__MEMBERS = eINSTANCE.getSet_Members();

		/**
		 * The meta object literal for the '<em><b>Via Members</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SET__VIA_MEMBERS = eINSTANCE.getSet_ViaMembers();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.RoleImpl <em>Role</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.RoleImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getRole()
		 * @generated
		 */
		EClass ROLE = eINSTANCE.getRole();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.RulerImpl <em>Ruler</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.RulerImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getRuler()
		 * @generated
		 */
		EClass RULER = eINSTANCE.getRuler();

		/**
		 * The meta object literal for the '<em><b>Guides</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULER__GUIDES = eINSTANCE.getRuler_Guides();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULER__TYPE = eINSTANCE.getRuler_Type();

		/**
		 * The meta object literal for the '<em><b>Diagram Data</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULER__DIAGRAM_DATA = eINSTANCE.getRuler_DiagramData();

		/**
		 * The meta object literal for the '<em><b>Indexed Set Mode Specification</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SET__INDEXED_SET_MODE_SPECIFICATION = eINSTANCE.getSet_IndexedSetModeSpecification();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.ElementImpl <em>Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.ElementImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getElement()
		 * @generated
		 */
		EClass ELEMENT = eINSTANCE.getElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT__NAME = eINSTANCE.getElement_Name();

		/**
		 * The meta object literal for the '<em><b>Level</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT__LEVEL = eINSTANCE.getElement_Level();

		/**
		 * The meta object literal for the '<em><b>Usage</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT__USAGE = eINSTANCE.getElement_Usage();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.GuideImpl <em>Guide</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.GuideImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getGuide()
		 * @generated
		 */
		EClass GUIDE = eINSTANCE.getGuide();

		/**
		 * The meta object literal for the '<em><b>Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GUIDE__POSITION = eINSTANCE.getGuide_Position();

		/**
		 * The meta object literal for the '<em><b>Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT__OFFSET = eINSTANCE.getElement_Offset();

		/**
		 * The meta object literal for the '<em><b>Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT__LENGTH = eINSTANCE.getElement_Length();

		/**
		 * The meta object literal for the '<em><b>Picture</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT__PICTURE = eINSTANCE.getElement_Picture();

		/**
		 * The meta object literal for the '<em><b>Nullable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ELEMENT__NULLABLE = eINSTANCE.getElement_Nullable();

		/**
		 * The meta object literal for the '<em><b>Record</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT__RECORD = eINSTANCE.getElement_Record();

		/**
		 * The meta object literal for the '<em><b>Key Elements</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT__KEY_ELEMENTS = eINSTANCE.getElement_KeyElements();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT__CHILDREN = eINSTANCE.getElement_Children();

		/**
		 * The meta object literal for the '<em><b>Redefines</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT__REDEFINES = eINSTANCE.getElement_Redefines();

		/**
		 * The meta object literal for the '<em><b>Occurs Specification</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT__OCCURS_SPECIFICATION = eINSTANCE.getElement_OccursSpecification();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ELEMENT__PARENT = eINSTANCE.getElement_Parent();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.KeyElementImpl <em>Key Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.KeyElementImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getKeyElement()
		 * @generated
		 */
		EClass KEY_ELEMENT = eINSTANCE.getKeyElement();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KEY_ELEMENT__ELEMENT = eINSTANCE.getKeyElement_Element();

		/**
		 * The meta object literal for the '<em><b>Sort Sequence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEY_ELEMENT__SORT_SEQUENCE = eINSTANCE.getKeyElement_SortSequence();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KEY_ELEMENT__KEY = eINSTANCE.getKeyElement_Key();

		/**
		 * The meta object literal for the '<em><b>Dbkey</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEY_ELEMENT__DBKEY = eINSTANCE.getKeyElement_Dbkey();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.MemberRoleImpl <em>Member Role</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.MemberRoleImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getMemberRole()
		 * @generated
		 */
		EClass MEMBER_ROLE = eINSTANCE.getMemberRole();

		/**
		 * The meta object literal for the '<em><b>Index Dbkey Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MEMBER_ROLE__INDEX_DBKEY_POSITION = eINSTANCE.getMemberRole_IndexDbkeyPosition();

		/**
		 * The meta object literal for the '<em><b>Membership Option</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MEMBER_ROLE__MEMBERSHIP_OPTION = eINSTANCE.getMemberRole_MembershipOption();

		/**
		 * The meta object literal for the '<em><b>Next Dbkey Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MEMBER_ROLE__NEXT_DBKEY_POSITION = eINSTANCE.getMemberRole_NextDbkeyPosition();

		/**
		 * The meta object literal for the '<em><b>Prior Dbkey Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MEMBER_ROLE__PRIOR_DBKEY_POSITION = eINSTANCE.getMemberRole_PriorDbkeyPosition();

		/**
		 * The meta object literal for the '<em><b>Owner Dbkey Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MEMBER_ROLE__OWNER_DBKEY_POSITION = eINSTANCE.getMemberRole_OwnerDbkeyPosition();

		/**
		 * The meta object literal for the '<em><b>Record</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MEMBER_ROLE__RECORD = eINSTANCE.getMemberRole_Record();

		/**
		 * The meta object literal for the '<em><b>Set</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MEMBER_ROLE__SET = eINSTANCE.getMemberRole_Set();

		/**
		 * The meta object literal for the '<em><b>Sort Key</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MEMBER_ROLE__SORT_KEY = eINSTANCE.getMemberRole_SortKey();

		/**
		 * The meta object literal for the '<em><b>Connection Parts</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MEMBER_ROLE__CONNECTION_PARTS = eINSTANCE.getMemberRole_ConnectionParts();

		/**
		 * The meta object literal for the '<em><b>Connection Label</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MEMBER_ROLE__CONNECTION_LABEL = eINSTANCE.getMemberRole_ConnectionLabel();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.SystemOwnerImpl <em>System Owner</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.SystemOwnerImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSystemOwner()
		 * @generated
		 */
		EClass SYSTEM_OWNER = eINSTANCE.getSystemOwner();

		/**
		 * The meta object literal for the '<em><b>Area Specification</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SYSTEM_OWNER__AREA_SPECIFICATION = eINSTANCE.getSystemOwner_AreaSpecification();

		/**
		 * The meta object literal for the '<em><b>Set</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SYSTEM_OWNER__SET = eINSTANCE.getSystemOwner_Set();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.ViaSpecificationImpl <em>Via Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.ViaSpecificationImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getViaSpecification()
		 * @generated
		 */
		EClass VIA_SPECIFICATION = eINSTANCE.getViaSpecification();

		/**
		 * The meta object literal for the '<em><b>Symbolic Displacement Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIA_SPECIFICATION__SYMBOLIC_DISPLACEMENT_NAME = eINSTANCE.getViaSpecification_SymbolicDisplacementName();

		/**
		 * The meta object literal for the '<em><b>Set</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIA_SPECIFICATION__SET = eINSTANCE.getViaSpecification_Set();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.DiagramNodeImpl <em>Diagram Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.DiagramNodeImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getDiagramNode()
		 * @generated
		 */
		EClass DIAGRAM_NODE = eINSTANCE.getDiagramNode();

		/**
		 * The meta object literal for the '<em><b>Diagram Location</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_NODE__DIAGRAM_LOCATION = eINSTANCE.getDiagramNode_DiagramLocation();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.DiagramLocationImpl <em>Diagram Location</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.DiagramLocationImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getDiagramLocation()
		 * @generated
		 */
		EClass DIAGRAM_LOCATION = eINSTANCE.getDiagramLocation();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM_LOCATION__X = eINSTANCE.getDiagramLocation_X();

		/**
		 * The meta object literal for the '<em><b>Y</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM_LOCATION__Y = eINSTANCE.getDiagramLocation_Y();

		/**
		 * The meta object literal for the '<em><b>Eyecatcher</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM_LOCATION__EYECATCHER = eINSTANCE.getDiagramLocation_Eyecatcher();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.DiagramDataImpl <em>Diagram Data</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.DiagramDataImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getDiagramData()
		 * @generated
		 */
		EClass DIAGRAM_DATA = eINSTANCE.getDiagramData();

		/**
		 * The meta object literal for the '<em><b>Connection Labels</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_DATA__CONNECTION_LABELS = eINSTANCE.getDiagramData_ConnectionLabels();

		/**
		 * The meta object literal for the '<em><b>Connection Parts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_DATA__CONNECTION_PARTS = eINSTANCE.getDiagramData_ConnectionParts();

		/**
		 * The meta object literal for the '<em><b>Connectors</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_DATA__CONNECTORS = eINSTANCE.getDiagramData_Connectors();

		/**
		 * The meta object literal for the '<em><b>Horizontal Ruler</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_DATA__HORIZONTAL_RULER = eINSTANCE.getDiagramData_HorizontalRuler();

		/**
		 * The meta object literal for the '<em><b>Locations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_DATA__LOCATIONS = eINSTANCE.getDiagramData_Locations();

		/**
		 * The meta object literal for the '<em><b>Zoom Level</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM_DATA__ZOOM_LEVEL = eINSTANCE.getDiagramData_ZoomLevel();

		/**
		 * The meta object literal for the '<em><b>Rulers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_DATA__RULERS = eINSTANCE.getDiagramData_Rulers();

		/**
		 * The meta object literal for the '<em><b>Show Grid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM_DATA__SHOW_GRID = eINSTANCE.getDiagramData_ShowGrid();

		/**
		 * The meta object literal for the '<em><b>Show Rulers</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM_DATA__SHOW_RULERS = eINSTANCE.getDiagramData_ShowRulers();

		/**
		 * The meta object literal for the '<em><b>Snap To Geometry</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM_DATA__SNAP_TO_GEOMETRY = eINSTANCE.getDiagramData_SnapToGeometry();

		/**
		 * The meta object literal for the '<em><b>Snap To Grid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM_DATA__SNAP_TO_GRID = eINSTANCE.getDiagramData_SnapToGrid();

		/**
		 * The meta object literal for the '<em><b>Snap To Guides</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM_DATA__SNAP_TO_GUIDES = eINSTANCE.getDiagramData_SnapToGuides();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM_DATA__UNIT = eINSTANCE.getDiagramData_Unit();

		/**
		 * The meta object literal for the '<em><b>Vertical Ruler</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM_DATA__VERTICAL_RULER = eINSTANCE.getDiagramData_VerticalRuler();

		/**
		 * The meta object literal for the '<em><b>Record</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIA_SPECIFICATION__RECORD = eINSTANCE.getViaSpecification_Record();

		/**
		 * The meta object literal for the '<em><b>Displacement Page Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIA_SPECIFICATION__DISPLACEMENT_PAGE_COUNT = eINSTANCE.getViaSpecification_DisplacementPageCount();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.KeyImpl <em>Key</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.KeyImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getKey()
		 * @generated
		 */
		EClass KEY = eINSTANCE.getKey();

		/**
		 * The meta object literal for the '<em><b>Calc Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEY__CALC_KEY = eINSTANCE.getKey_CalcKey();

		/**
		 * The meta object literal for the '<em><b>Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEY__LENGTH = eINSTANCE.getKey_Length();

		/**
		 * The meta object literal for the '<em><b>Duplicates Option</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEY__DUPLICATES_OPTION = eINSTANCE.getKey_DuplicatesOption();

		/**
		 * The meta object literal for the '<em><b>Compressed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEY__COMPRESSED = eINSTANCE.getKey_Compressed();

		/**
		 * The meta object literal for the '<em><b>Natural Sequence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEY__NATURAL_SEQUENCE = eINSTANCE.getKey_NaturalSequence();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KEY__ELEMENTS = eINSTANCE.getKey_Elements();

		/**
		 * The meta object literal for the '<em><b>Element Summary</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEY__ELEMENT_SUMMARY = eINSTANCE.getKey_ElementSummary();

		/**
		 * The meta object literal for the '<em><b>Member Role</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KEY__MEMBER_ROLE = eINSTANCE.getKey_MemberRole();

		/**
		 * The meta object literal for the '<em><b>Record</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KEY__RECORD = eINSTANCE.getKey_Record();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.AreaSpecificationImpl <em>Area Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.AreaSpecificationImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getAreaSpecification()
		 * @generated
		 */
		EClass AREA_SPECIFICATION = eINSTANCE.getAreaSpecification();

		/**
		 * The meta object literal for the '<em><b>Symbolic Subarea Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AREA_SPECIFICATION__SYMBOLIC_SUBAREA_NAME = eINSTANCE.getAreaSpecification_SymbolicSubareaName();

		/**
		 * The meta object literal for the '<em><b>Area</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AREA_SPECIFICATION__AREA = eINSTANCE.getAreaSpecification_Area();

		/**
		 * The meta object literal for the '<em><b>Offset Expression</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AREA_SPECIFICATION__OFFSET_EXPRESSION = eINSTANCE.getAreaSpecification_OffsetExpression();

		/**
		 * The meta object literal for the '<em><b>Record</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AREA_SPECIFICATION__RECORD = eINSTANCE.getAreaSpecification_Record();

		/**
		 * The meta object literal for the '<em><b>System Owner</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AREA_SPECIFICATION__SYSTEM_OWNER = eINSTANCE.getAreaSpecification_SystemOwner();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.ConnectionPartImpl <em>Connection Part</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.ConnectionPartImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getConnectionPart()
		 * @generated
		 */
		EClass CONNECTION_PART = eINSTANCE.getConnectionPart();

		/**
		 * The meta object literal for the '<em><b>Connector</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTION_PART__CONNECTOR = eINSTANCE.getConnectionPart_Connector();

		/**
		 * The meta object literal for the '<em><b>Bendpoint Locations</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTION_PART__BENDPOINT_LOCATIONS = eINSTANCE.getConnectionPart_BendpointLocations();

		/**
		 * The meta object literal for the '<em><b>Member Role</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTION_PART__MEMBER_ROLE = eINSTANCE.getConnectionPart_MemberRole();

		/**
		 * The meta object literal for the '<em><b>Source Endpoint Location</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTION_PART__SOURCE_ENDPOINT_LOCATION = eINSTANCE.getConnectionPart_SourceEndpointLocation();

		/**
		 * The meta object literal for the '<em><b>Target Endpoint Location</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTION_PART__TARGET_ENDPOINT_LOCATION = eINSTANCE.getConnectionPart_TargetEndpointLocation();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.ConnectionLabelImpl <em>Connection Label</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.ConnectionLabelImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getConnectionLabel()
		 * @generated
		 */
		EClass CONNECTION_LABEL = eINSTANCE.getConnectionLabel();

		/**
		 * The meta object literal for the '<em><b>Alignment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONNECTION_LABEL__ALIGNMENT = eINSTANCE.getConnectionLabel_Alignment();

		/**
		 * The meta object literal for the '<em><b>Member Role</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTION_LABEL__MEMBER_ROLE = eINSTANCE.getConnectionLabel_MemberRole();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.ConnectorImpl <em>Connector</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.ConnectorImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getConnector()
		 * @generated
		 */
		EClass CONNECTOR = eINSTANCE.getConnector();

		/**
		 * The meta object literal for the '<em><b>Connection Part</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTOR__CONNECTION_PART = eINSTANCE.getConnector_ConnectionPart();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONNECTOR__LABEL = eINSTANCE.getConnector_Label();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.OffsetExpressionImpl <em>Offset Expression</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.OffsetExpressionImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getOffsetExpression()
		 * @generated
		 */
		EClass OFFSET_EXPRESSION = eINSTANCE.getOffsetExpression();

		/**
		 * The meta object literal for the '<em><b>Area Specification</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OFFSET_EXPRESSION__AREA_SPECIFICATION = eINSTANCE.getOffsetExpression_AreaSpecification();

		/**
		 * The meta object literal for the '<em><b>Offset Page Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OFFSET_EXPRESSION__OFFSET_PAGE_COUNT = eINSTANCE.getOffsetExpression_OffsetPageCount();

		/**
		 * The meta object literal for the '<em><b>Offset Percent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OFFSET_EXPRESSION__OFFSET_PERCENT = eINSTANCE.getOffsetExpression_OffsetPercent();

		/**
		 * The meta object literal for the '<em><b>Page Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OFFSET_EXPRESSION__PAGE_COUNT = eINSTANCE.getOffsetExpression_PageCount();

		/**
		 * The meta object literal for the '<em><b>Percent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OFFSET_EXPRESSION__PERCENT = eINSTANCE.getOffsetExpression_Percent();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.OwnerRoleImpl <em>Owner Role</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.OwnerRoleImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getOwnerRole()
		 * @generated
		 */
		EClass OWNER_ROLE = eINSTANCE.getOwnerRole();

		/**
		 * The meta object literal for the '<em><b>Next Dbkey Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OWNER_ROLE__NEXT_DBKEY_POSITION = eINSTANCE.getOwnerRole_NextDbkeyPosition();

		/**
		 * The meta object literal for the '<em><b>Prior Dbkey Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OWNER_ROLE__PRIOR_DBKEY_POSITION = eINSTANCE.getOwnerRole_PriorDbkeyPosition();

		/**
		 * The meta object literal for the '<em><b>Record</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OWNER_ROLE__RECORD = eINSTANCE.getOwnerRole_Record();

		/**
		 * The meta object literal for the '<em><b>Set</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OWNER_ROLE__SET = eINSTANCE.getOwnerRole_Set();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.IndexedSetModeSpecificationImpl <em>Indexed Set Mode Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.IndexedSetModeSpecificationImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getIndexedSetModeSpecification()
		 * @generated
		 */
		EClass INDEXED_SET_MODE_SPECIFICATION = eINSTANCE.getIndexedSetModeSpecification();

		/**
		 * The meta object literal for the '<em><b>Symbolic Index Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEXED_SET_MODE_SPECIFICATION__SYMBOLIC_INDEX_NAME = eINSTANCE.getIndexedSetModeSpecification_SymbolicIndexName();

		/**
		 * The meta object literal for the '<em><b>Key Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEXED_SET_MODE_SPECIFICATION__KEY_COUNT = eINSTANCE.getIndexedSetModeSpecification_KeyCount();

		/**
		 * The meta object literal for the '<em><b>Set</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INDEXED_SET_MODE_SPECIFICATION__SET = eINSTANCE.getIndexedSetModeSpecification_Set();

		/**
		 * The meta object literal for the '<em><b>Displacement Page Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INDEXED_SET_MODE_SPECIFICATION__DISPLACEMENT_PAGE_COUNT = eINSTANCE.getIndexedSetModeSpecification_DisplacementPageCount();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.AreaProcedureCallSpecificationImpl <em>Area Procedure Call Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.AreaProcedureCallSpecificationImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getAreaProcedureCallSpecification()
		 * @generated
		 */
		EClass AREA_PROCEDURE_CALL_SPECIFICATION = eINSTANCE.getAreaProcedureCallSpecification();

		/**
		 * The meta object literal for the '<em><b>Procedure</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AREA_PROCEDURE_CALL_SPECIFICATION__PROCEDURE = eINSTANCE.getAreaProcedureCallSpecification_Procedure();

		/**
		 * The meta object literal for the '<em><b>Call Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AREA_PROCEDURE_CALL_SPECIFICATION__CALL_TIME = eINSTANCE.getAreaProcedureCallSpecification_CallTime();

		/**
		 * The meta object literal for the '<em><b>Function</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AREA_PROCEDURE_CALL_SPECIFICATION__FUNCTION = eINSTANCE.getAreaProcedureCallSpecification_Function();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.RecordProcedureCallSpecificationImpl <em>Record Procedure Call Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.RecordProcedureCallSpecificationImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getRecordProcedureCallSpecification()
		 * @generated
		 */
		EClass RECORD_PROCEDURE_CALL_SPECIFICATION = eINSTANCE.getRecordProcedureCallSpecification();

		/**
		 * The meta object literal for the '<em><b>Procedure</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RECORD_PROCEDURE_CALL_SPECIFICATION__PROCEDURE = eINSTANCE.getRecordProcedureCallSpecification_Procedure();

		/**
		 * The meta object literal for the '<em><b>Record</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD = eINSTANCE.getRecordProcedureCallSpecification_Record();

		/**
		 * The meta object literal for the '<em><b>Call Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RECORD_PROCEDURE_CALL_SPECIFICATION__CALL_TIME = eINSTANCE.getRecordProcedureCallSpecification_CallTime();

		/**
		 * The meta object literal for the '<em><b>Verb</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RECORD_PROCEDURE_CALL_SPECIFICATION__VERB = eINSTANCE.getRecordProcedureCallSpecification_Verb();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.ProcedureImpl <em>Procedure</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.ProcedureImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getProcedure()
		 * @generated
		 */
		EClass PROCEDURE = eINSTANCE.getProcedure();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCEDURE__NAME = eINSTANCE.getProcedure_Name();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.impl.OccursSpecificationImpl <em>Occurs Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.impl.OccursSpecificationImpl
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getOccursSpecification()
		 * @generated
		 */
		EClass OCCURS_SPECIFICATION = eINSTANCE.getOccursSpecification();

		/**
		 * The meta object literal for the '<em><b>Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OCCURS_SPECIFICATION__COUNT = eINSTANCE.getOccursSpecification_Count();

		/**
		 * The meta object literal for the '<em><b>Depending On</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OCCURS_SPECIFICATION__DEPENDING_ON = eINSTANCE.getOccursSpecification_DependingOn();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OCCURS_SPECIFICATION__ELEMENT = eINSTANCE.getOccursSpecification_Element();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.SetOrder <em>Set Order</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.SetOrder
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSetOrder()
		 * @generated
		 */
		EEnum SET_ORDER = eINSTANCE.getSetOrder();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.DuplicatesOption <em>Duplicates Option</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.DuplicatesOption
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getDuplicatesOption()
		 * @generated
		 */
		EEnum DUPLICATES_OPTION = eINSTANCE.getDuplicatesOption();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.LabelAlignment <em>Label Alignment</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.LabelAlignment
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getLabelAlignment()
		 * @generated
		 */
		EEnum LABEL_ALIGNMENT = eINSTANCE.getLabelAlignment();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.LocationMode <em>Location Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.LocationMode
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getLocationMode()
		 * @generated
		 */
		EEnum LOCATION_MODE = eINSTANCE.getLocationMode();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.SetMode <em>Set Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.SetMode
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSetMode()
		 * @generated
		 */
		EEnum SET_MODE = eINSTANCE.getSetMode();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.StorageMode <em>Storage Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.StorageMode
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getStorageMode()
		 * @generated
		 */
		EEnum STORAGE_MODE = eINSTANCE.getStorageMode();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.Unit <em>Unit</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.Unit
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getUnit()
		 * @generated
		 */
		EEnum UNIT = eINSTANCE.getUnit();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.SetMembershipOption <em>Set Membership Option</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.SetMembershipOption
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSetMembershipOption()
		 * @generated
		 */
		EEnum SET_MEMBERSHIP_OPTION = eINSTANCE.getSetMembershipOption();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.SortSequence <em>Sort Sequence</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.SortSequence
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getSortSequence()
		 * @generated
		 */
		EEnum SORT_SEQUENCE = eINSTANCE.getSortSequence();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.ProcedureCallTime <em>Procedure Call Time</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.ProcedureCallTime
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getProcedureCallTime()
		 * @generated
		 */
		EEnum PROCEDURE_CALL_TIME = eINSTANCE.getProcedureCallTime();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.AreaProcedureCallFunction <em>Area Procedure Call Function</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.AreaProcedureCallFunction
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getAreaProcedureCallFunction()
		 * @generated
		 */
		EEnum AREA_PROCEDURE_CALL_FUNCTION = eINSTANCE.getAreaProcedureCallFunction();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.RecordProcedureCallVerb <em>Record Procedure Call Verb</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.RecordProcedureCallVerb
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getRecordProcedureCallVerb()
		 * @generated
		 */
		EEnum RECORD_PROCEDURE_CALL_VERB = eINSTANCE.getRecordProcedureCallVerb();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.RulerType <em>Ruler Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.RulerType
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getRulerType()
		 * @generated
		 */
		EEnum RULER_TYPE = eINSTANCE.getRulerType();

		/**
		 * The meta object literal for the '{@link org.lh.dmlj.schema.Usage <em>Usage</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.lh.dmlj.schema.Usage
		 * @see org.lh.dmlj.schema.impl.SchemaPackageImpl#getUsage()
		 * @generated
		 */
		EEnum USAGE = eINSTANCE.getUsage();

	}

} //SchemaPackage
