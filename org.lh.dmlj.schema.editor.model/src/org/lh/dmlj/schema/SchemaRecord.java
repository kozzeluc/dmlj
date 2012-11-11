/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Record</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getControlLength <em>Control Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getDataLength <em>Data Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#isFragmented <em>Fragmented</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getId <em>Id</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getLocationMode <em>Location Mode</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getMinimumFragmentLength <em>Minimum Fragment Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getMinimumRootLength <em>Minimum Root Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getName <em>Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getPrefixLength <em>Prefix Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getStorageMode <em>Storage Mode</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getAreaSpecification <em>Area Specification</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getCalcKey <em>Calc Key</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getElements <em>Elements</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getKeys <em>Keys</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getMemberRoles <em>Member Roles</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getOwnerRoles <em>Owner Roles</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getProcedures <em>Procedures</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getRoles <em>Roles</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getRootElements <em>Root Elements</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.SchemaRecord#getViaSpecification <em>Via Specification</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord()
 * @model
 * @generated
 */
public interface SchemaRecord extends DiagramNode {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaRecord#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_Id()
	 * @model
	 * @generated
	 */
	short getId();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaRecord#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(short value);

	/**
	 * Returns the value of the '<em><b>Control Length</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Control Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Control Length</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_ControlLength()
	 * @model default="-1" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	short getControlLength();

	/**
	 * Returns the value of the '<em><b>Data Length</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Length</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_DataLength()
	 * @model default="-1" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	short getDataLength();

	/**
	 * Returns the value of the '<em><b>Storage Mode</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.StorageMode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Storage Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Storage Mode</em>' attribute.
	 * @see org.lh.dmlj.schema.StorageMode
	 * @see #setStorageMode(StorageMode)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_StorageMode()
	 * @model
	 * @generated
	 */
	StorageMode getStorageMode();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaRecord#getStorageMode <em>Storage Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Storage Mode</em>' attribute.
	 * @see org.lh.dmlj.schema.StorageMode
	 * @see #getStorageMode()
	 * @generated
	 */
	void setStorageMode(StorageMode value);

	/**
	 * Returns the value of the '<em><b>Location Mode</b></em>' attribute.
	 * The literals are from the enumeration {@link org.lh.dmlj.schema.LocationMode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location Mode</em>' attribute.
	 * @see org.lh.dmlj.schema.LocationMode
	 * @see #setLocationMode(LocationMode)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_LocationMode()
	 * @model
	 * @generated
	 */
	LocationMode getLocationMode();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaRecord#getLocationMode <em>Location Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location Mode</em>' attribute.
	 * @see org.lh.dmlj.schema.LocationMode
	 * @see #getLocationMode()
	 * @generated
	 */
	void setLocationMode(LocationMode value);

	/**
	 * Returns the value of the '<em><b>Minimum Root Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum Root Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum Root Length</em>' attribute.
	 * @see #setMinimumRootLength(Short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_MinimumRootLength()
	 * @model
	 * @generated
	 */
	Short getMinimumRootLength();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaRecord#getMinimumRootLength <em>Minimum Root Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum Root Length</em>' attribute.
	 * @see #getMinimumRootLength()
	 * @generated
	 */
	void setMinimumRootLength(Short value);

	/**
	 * Returns the value of the '<em><b>Minimum Fragment Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum Fragment Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum Fragment Length</em>' attribute.
	 * @see #setMinimumFragmentLength(Short)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_MinimumFragmentLength()
	 * @model
	 * @generated
	 */
	Short getMinimumFragmentLength();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaRecord#getMinimumFragmentLength <em>Minimum Fragment Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum Fragment Length</em>' attribute.
	 * @see #getMinimumFragmentLength()
	 * @generated
	 */
	void setMinimumFragmentLength(Short value);

	/**
	 * Returns the value of the '<em><b>Prefix Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prefix Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prefix Length</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_PrefixLength()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	short getPrefixLength();

	/**
	 * Returns the value of the '<em><b>Fragmented</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fragmented</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fragmented</em>' attribute.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_Fragmented()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	boolean isFragmented();

	/**
	 * Returns the value of the '<em><b>Calc Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Calc Key</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Calc Key</em>' reference.
	 * @see #setCalcKey(Key)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_CalcKey()
	 * @model
	 * @generated
	 */
	Key getCalcKey();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaRecord#getCalcKey <em>Calc Key</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Calc Key</em>' reference.
	 * @see #getCalcKey()
	 * @generated
	 */
	void setCalcKey(Key value);

	/**
	 * Returns the value of the '<em><b>Schema</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Schema#getRecords <em>Records</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schema</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schema</em>' container reference.
	 * @see #setSchema(Schema)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_Schema()
	 * @see org.lh.dmlj.schema.Schema#getRecords
	 * @model opposite="records" required="true" transient="false"
	 * @generated
	 */
	Schema getSchema();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaRecord#getSchema <em>Schema</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' container reference.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(Schema value);

	/**
	 * Returns the value of the '<em><b>Via Specification</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.ViaSpecification#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Via Specification</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Via Specification</em>' containment reference.
	 * @see #setViaSpecification(ViaSpecification)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_ViaSpecification()
	 * @see org.lh.dmlj.schema.ViaSpecification#getRecord
	 * @model opposite="record" containment="true"
	 * @generated
	 */
	ViaSpecification getViaSpecification();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaRecord#getViaSpecification <em>Via Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Via Specification</em>' containment reference.
	 * @see #getViaSpecification()
	 * @generated
	 */
	void setViaSpecification(ViaSpecification value);

	/**
	 * Returns the value of the '<em><b>Owner Roles</b></em>' reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.OwnerRole}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.OwnerRole#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owner Roles</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owner Roles</em>' reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_OwnerRoles()
	 * @see org.lh.dmlj.schema.OwnerRole#getRecord
	 * @model opposite="record" derived="true"
	 * @generated
	 */
	EList<OwnerRole> getOwnerRoles();

	/**
	 * Returns the value of the '<em><b>Member Roles</b></em>' reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.MemberRole}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.MemberRole#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Member Roles</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Member Roles</em>' reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_MemberRoles()
	 * @see org.lh.dmlj.schema.MemberRole#getRecord
	 * @model opposite="record" derived="true"
	 * @generated
	 */
	EList<MemberRole> getMemberRoles();

	/**
	 * Returns the value of the '<em><b>Root Elements</b></em>' reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.Element}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Root Elements</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Root Elements</em>' reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_RootElements()
	 * @model required="true"
	 * @generated
	 */
	EList<Element> getRootElements();

	/**
	 * Returns the value of the '<em><b>Procedures</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.RecordProcedureCallSpecification}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.RecordProcedureCallSpecification#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Procedures</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Procedures</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_Procedures()
	 * @see org.lh.dmlj.schema.RecordProcedureCallSpecification#getRecord
	 * @model opposite="record" containment="true"
	 * @generated
	 */
	EList<RecordProcedureCallSpecification> getProcedures();

	/**
	 * Returns the value of the '<em><b>Keys</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.Key}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Key#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Keys</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Keys</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_Keys()
	 * @see org.lh.dmlj.schema.Key#getRecord
	 * @model opposite="record" containment="true"
	 * @generated
	 */
	EList<Key> getKeys();

	/**
	 * Returns the value of the '<em><b>Area Specification</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.AreaSpecification#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Area Specification</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Area Specification</em>' reference.
	 * @see #setAreaSpecification(AreaSpecification)
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_AreaSpecification()
	 * @see org.lh.dmlj.schema.AreaSpecification#getRecord
	 * @model opposite="record"
	 * @generated
	 */
	AreaSpecification getAreaSpecification();

	/**
	 * Sets the value of the '{@link org.lh.dmlj.schema.SchemaRecord#getAreaSpecification <em>Area Specification</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Area Specification</em>' reference.
	 * @see #getAreaSpecification()
	 * @generated
	 */
	void setAreaSpecification(AreaSpecification value);

	/**
	 * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.Element}.
	 * It is bidirectional and its opposite is '{@link org.lh.dmlj.schema.Element#getRecord <em>Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' containment reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_Elements()
	 * @see org.lh.dmlj.schema.Element#getRecord
	 * @model opposite="record" containment="true" required="true"
	 * @generated
	 */
	EList<Element> getElements();

	/**
	 * Returns the value of the '<em><b>Roles</b></em>' reference list.
	 * The list contents are of type {@link org.lh.dmlj.schema.Role}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Roles</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Roles</em>' reference list.
	 * @see org.lh.dmlj.schema.SchemaPackage#getSchemaRecord_Roles()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	EList<Role> getRoles();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model setNameRequired="true"
	 * @generated
	 */
	Role getRole(String setName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model nameRequired="true"
	 * @generated
	 */
	Element getElement(String name);

} // SchemaRecord
