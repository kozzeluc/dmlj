/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.lh.dmlj.schema.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.Role;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.ViaSpecification;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Record</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getBaseName <em>Base Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getBaseVersion <em>Base Version</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getControlLength <em>Control Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getDataLength <em>Data Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#isFragmented <em>Fragmented</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getLocationMode <em>Location Mode</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getMinimumFragmentLength <em>Minimum Fragment Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getMinimumRootLength <em>Minimum Root Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getPrefixLength <em>Prefix Length</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getStorageMode <em>Storage Mode</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getAreaSpecification <em>Area Specification</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getCalcKey <em>Calc Key</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getElements <em>Elements</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getKeys <em>Keys</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getMemberRoles <em>Member Roles</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getOwnerRoles <em>Owner Roles</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getProcedures <em>Procedures</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getRoles <em>Roles</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getRootElements <em>Root Elements</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getSynonymName <em>Synonym Name</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getSynonymVersion <em>Synonym Version</em>}</li>
 *   <li>{@link org.lh.dmlj.schema.impl.SchemaRecordImpl#getViaSpecification <em>Via Specification</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SchemaRecordImpl extends DiagramNodeImpl implements SchemaRecord {
	/**
	 * The default value of the '{@link #getBaseName() <em>Base Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseName()
	 * @generated
	 * @ordered
	 */
	protected static final String BASE_NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getBaseName() <em>Base Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseName()
	 * @generated
	 * @ordered
	 */
	protected String baseName = BASE_NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getBaseVersion() <em>Base Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseVersion()
	 * @generated
	 * @ordered
	 */
	protected static final short BASE_VERSION_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getBaseVersion() <em>Base Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseVersion()
	 * @generated
	 * @ordered
	 */
	protected short baseVersion = BASE_VERSION_EDEFAULT;
	/**
	 * The default value of the '{@link #getControlLength() <em>Control Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getControlLength()
	 * @generated
	 * @ordered
	 */
	protected static final short CONTROL_LENGTH_EDEFAULT = -1;
	/**
	 * The default value of the '{@link #getDataLength() <em>Data Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataLength()
	 * @generated
	 * @ordered
	 */
	protected static final short DATA_LENGTH_EDEFAULT = -1;
	/**
	 * The default value of the '{@link #isFragmented() <em>Fragmented</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFragmented()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FRAGMENTED_EDEFAULT = false;
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final short ID_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected short id = ID_EDEFAULT;
	/**
	 * The default value of the '{@link #getLocationMode() <em>Location Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocationMode()
	 * @generated
	 * @ordered
	 */
	protected static final LocationMode LOCATION_MODE_EDEFAULT = LocationMode.CALC;
	/**
	 * The cached value of the '{@link #getLocationMode() <em>Location Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocationMode()
	 * @generated
	 * @ordered
	 */
	protected LocationMode locationMode = LOCATION_MODE_EDEFAULT;
	/**
	 * The default value of the '{@link #getMinimumFragmentLength() <em>Minimum Fragment Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumFragmentLength()
	 * @generated
	 * @ordered
	 */
	protected static final Short MINIMUM_FRAGMENT_LENGTH_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getMinimumFragmentLength() <em>Minimum Fragment Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumFragmentLength()
	 * @generated
	 * @ordered
	 */
	protected Short minimumFragmentLength = MINIMUM_FRAGMENT_LENGTH_EDEFAULT;
	/**
	 * The default value of the '{@link #getMinimumRootLength() <em>Minimum Root Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumRootLength()
	 * @generated
	 * @ordered
	 */
	protected static final Short MINIMUM_ROOT_LENGTH_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getMinimumRootLength() <em>Minimum Root Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumRootLength()
	 * @generated
	 * @ordered
	 */
	protected Short minimumRootLength = MINIMUM_ROOT_LENGTH_EDEFAULT;
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getPrefixLength() <em>Prefix Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrefixLength()
	 * @generated
	 * @ordered
	 */
	protected static final short PREFIX_LENGTH_EDEFAULT = 0;
	/**
	 * The default value of the '{@link #getStorageMode() <em>Storage Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStorageMode()
	 * @generated
	 * @ordered
	 */
	protected static final StorageMode STORAGE_MODE_EDEFAULT = StorageMode.FIXED;
	/**
	 * The cached value of the '{@link #getStorageMode() <em>Storage Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStorageMode()
	 * @generated
	 * @ordered
	 */
	protected StorageMode storageMode = STORAGE_MODE_EDEFAULT;
	/**
	 * The cached value of the '{@link #getAreaSpecification() <em>Area Specification</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAreaSpecification()
	 * @generated
	 * @ordered
	 */
	protected AreaSpecification areaSpecification;
	/**
	 * The cached value of the '{@link #getCalcKey() <em>Calc Key</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCalcKey()
	 * @generated
	 * @ordered
	 */
	protected Key calcKey;
	/**
	 * The cached value of the '{@link #getElements() <em>Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElements()
	 * @generated
	 * @ordered
	 */
	protected EList<Element> elements;
	/**
	 * The cached value of the '{@link #getKeys() <em>Keys</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeys()
	 * @generated
	 * @ordered
	 */
	protected EList<Key> keys;
	/**
	 * The cached value of the '{@link #getMemberRoles() <em>Member Roles</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMemberRoles()
	 * @generated
	 * @ordered
	 */
	protected EList<MemberRole> memberRoles;
	/**
	 * The cached value of the '{@link #getOwnerRoles() <em>Owner Roles</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnerRoles()
	 * @generated
	 * @ordered
	 */
	protected EList<OwnerRole> ownerRoles;
	/**
	 * The cached value of the '{@link #getProcedures() <em>Procedures</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcedures()
	 * @generated
	 * @ordered
	 */
	protected EList<RecordProcedureCallSpecification> procedures;
	/**
	 * The cached value of the '{@link #getRootElements() <em>Root Elements</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRootElements()
	 * @generated
	 * @ordered
	 */
	protected EList<Element> rootElements;
	/**
	 * The default value of the '{@link #getSynonymName() <em>Synonym Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSynonymName()
	 * @generated
	 * @ordered
	 */
	protected static final String SYNONYM_NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getSynonymName() <em>Synonym Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSynonymName()
	 * @generated
	 * @ordered
	 */
	protected String synonymName = SYNONYM_NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getSynonymVersion() <em>Synonym Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSynonymVersion()
	 * @generated
	 * @ordered
	 */
	protected static final short SYNONYM_VERSION_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getSynonymVersion() <em>Synonym Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSynonymVersion()
	 * @generated
	 * @ordered
	 */
	protected short synonymVersion = SYNONYM_VERSION_EDEFAULT;
	/**
	 * The cached value of the '{@link #getViaSpecification() <em>Via Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViaSpecification()
	 * @generated
	 * @ordered
	 */
	protected ViaSpecification viaSpecification;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SchemaRecordImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.SCHEMA_RECORD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBaseName() {
		return baseName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseName(String newBaseName) {
		String oldBaseName = baseName;
		baseName = newBaseName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__BASE_NAME, oldBaseName, baseName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public short getBaseVersion() {
		return baseVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseVersion(short newBaseVersion) {
		short oldBaseVersion = baseVersion;
		baseVersion = newBaseVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__BASE_VERSION, oldBaseVersion, baseVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public short getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(short newId) {
		short oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public short getControlLength() {
		short controlLength = 0;			
		// traverse all keys to calculate the control length...
		for (Key key : getKeys()) {			
			for (KeyElement keyElement : key.getElements()) {
				if (!keyElement.isDbkey()) {
					Element element = keyElement.getElement(); 							
					short i = (short)(element.getOffset() + 
									  element.getLength());
					if (i > controlLength) {
						controlLength = i;
					}
				}
			}			
		}
		// add 4 bytes when the record is fragmented...
		if (isFragmented()) {
			controlLength += 4;
		}		
		return controlLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public short getDataLength() {
		short dataLength = 0;
		// traverse all root elements to calculate the data length...
		for (Element element : getRootElements()) {
			short length = element.getLength();
			if (element.getOccursSpecification() != null) {
				length *= element.getOccursSpecification().getCount();
			}
			short i = (short)(element.getOffset() + length);
			if (i > dataLength) {
				dataLength = i;
			}
		}
		// add 4 bytes when the record is fragmented...
		if (isFragmented()) {
			dataLength += 4;
		} else {
			// if the record is compressed, add 4 bytes too...
			boolean ok = false;
			for (RecordProcedureCallSpecification call : getProcedures()) {
				if (!ok && call.getProcedure().getName().equals("IDMSCOMP")) {						
					dataLength += 4;
					ok = true;
				}
			}
		}	
		// round the data length to the next higher or equal multiple of four...
		while (dataLength % 4 > 0) {
			dataLength++;
		}
		return dataLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StorageMode getStorageMode() {
		return storageMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStorageMode(StorageMode newStorageMode) {
		StorageMode oldStorageMode = storageMode;
		storageMode = newStorageMode == null ? STORAGE_MODE_EDEFAULT : newStorageMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__STORAGE_MODE, oldStorageMode, storageMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocationMode getLocationMode() {
		return locationMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocationMode(LocationMode newLocationMode) {
		LocationMode oldLocationMode = locationMode;
		locationMode = newLocationMode == null ? LOCATION_MODE_EDEFAULT : newLocationMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__LOCATION_MODE, oldLocationMode, locationMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Short getMinimumRootLength() {
		return minimumRootLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinimumRootLength(Short newMinimumRootLength) {
		Short oldMinimumRootLength = minimumRootLength;
		minimumRootLength = newMinimumRootLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__MINIMUM_ROOT_LENGTH, oldMinimumRootLength, minimumRootLength));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Short getMinimumFragmentLength() {
		return minimumFragmentLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinimumFragmentLength(Short newMinimumFragmentLength) {
		Short oldMinimumFragmentLength = minimumFragmentLength;
		minimumFragmentLength = newMinimumFragmentLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__MINIMUM_FRAGMENT_LENGTH, oldMinimumFragmentLength, minimumFragmentLength));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public short getPrefixLength() {
		// calculate the prefix length...
		short prefixLength = 0;
		// add space for 2 pointers when the location mode is CALC...
		if (getLocationMode() == LocationMode.CALC) {
			prefixLength += 8;
		}
		// add 4 bytes for each pointer in regular sets...
		for (OwnerRole role : getOwnerRoles()) {
			// NEXT pointer...
			prefixLength += 4; // always there
			// PRIOR pointer...
			if (role.getPriorDbkeyPosition() != null) {
				prefixLength += 4;
			}
		}
		for (MemberRole role : getMemberRoles()) {
			// NEXT pointer...
			if (role.getNextDbkeyPosition() != null) {
				prefixLength += 4;
			}
			// PRIOR pointer...
			if (role.getPriorDbkeyPosition() != null) {
				prefixLength += 4;
			}
			// OWNER pointer...
			if (role.getOwnerDbkeyPosition() != null) {
				prefixLength += 4;
			}
			// INDEX pointer...
			if (role.getIndexDbkeyPosition() != null) {
				prefixLength += 4;
			}
		}
		// add space for 1 pointer when the record is fragmented...
		if (isFragmented()) {
			prefixLength += 4;
		}						
		return prefixLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean isFragmented() {
		return getMinimumRootLength() != null || 
			   getMinimumFragmentLength() != null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Key getCalcKey() {
		if (calcKey != null && calcKey.eIsProxy()) {
			InternalEObject oldCalcKey = (InternalEObject)calcKey;
			calcKey = (Key)eResolveProxy(oldCalcKey);
			if (calcKey != oldCalcKey) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.SCHEMA_RECORD__CALC_KEY, oldCalcKey, calcKey));
			}
		}
		return calcKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Key basicGetCalcKey() {
		return calcKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCalcKey(Key newCalcKey) {
		Key oldCalcKey = calcKey;
		calcKey = newCalcKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__CALC_KEY, oldCalcKey, calcKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Schema getSchema() {
		if (eContainerFeatureID() != SchemaPackage.SCHEMA_RECORD__SCHEMA) return null;
		return (Schema)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSchema(Schema newSchema, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSchema, SchemaPackage.SCHEMA_RECORD__SCHEMA, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSchema(Schema newSchema) {
		if (newSchema != eInternalContainer() || (eContainerFeatureID() != SchemaPackage.SCHEMA_RECORD__SCHEMA && newSchema != null)) {
			if (EcoreUtil.isAncestor(this, newSchema))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSchema != null)
				msgs = ((InternalEObject)newSchema).eInverseAdd(this, SchemaPackage.SCHEMA__RECORDS, Schema.class, msgs);
			msgs = basicSetSchema(newSchema, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__SCHEMA, newSchema, newSchema));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSynonymName() {
		return synonymName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSynonymName(String newSynonymName) {
		String oldSynonymName = synonymName;
		synonymName = newSynonymName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__SYNONYM_NAME, oldSynonymName, synonymName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public short getSynonymVersion() {
		return synonymVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSynonymVersion(short newSynonymVersion) {
		short oldSynonymVersion = synonymVersion;
		synonymVersion = newSynonymVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__SYNONYM_VERSION, oldSynonymVersion, synonymVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ViaSpecification getViaSpecification() {
		return viaSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetViaSpecification(ViaSpecification newViaSpecification, NotificationChain msgs) {
		ViaSpecification oldViaSpecification = viaSpecification;
		viaSpecification = newViaSpecification;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__VIA_SPECIFICATION, oldViaSpecification, newViaSpecification);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setViaSpecification(ViaSpecification newViaSpecification) {
		if (newViaSpecification != viaSpecification) {
			NotificationChain msgs = null;
			if (viaSpecification != null)
				msgs = ((InternalEObject)viaSpecification).eInverseRemove(this, SchemaPackage.VIA_SPECIFICATION__RECORD, ViaSpecification.class, msgs);
			if (newViaSpecification != null)
				msgs = ((InternalEObject)newViaSpecification).eInverseAdd(this, SchemaPackage.VIA_SPECIFICATION__RECORD, ViaSpecification.class, msgs);
			msgs = basicSetViaSpecification(newViaSpecification, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__VIA_SPECIFICATION, newViaSpecification, newViaSpecification));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OwnerRole> getOwnerRoles() {
		if (ownerRoles == null) {
			ownerRoles = new EObjectWithInverseResolvingEList<OwnerRole>(OwnerRole.class, this, SchemaPackage.SCHEMA_RECORD__OWNER_ROLES, SchemaPackage.OWNER_ROLE__RECORD);
		}
		return ownerRoles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MemberRole> getMemberRoles() {
		if (memberRoles == null) {
			memberRoles = new EObjectWithInverseResolvingEList<MemberRole>(MemberRole.class, this, SchemaPackage.SCHEMA_RECORD__MEMBER_ROLES, SchemaPackage.MEMBER_ROLE__RECORD);
		}
		return memberRoles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Element> getRootElements() {
		if (rootElements == null) {
			rootElements = new EObjectResolvingEList<Element>(Element.class, this, SchemaPackage.SCHEMA_RECORD__ROOT_ELEMENTS);
		}
		return rootElements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<RecordProcedureCallSpecification> getProcedures() {
		if (procedures == null) {
			procedures = new EObjectContainmentWithInverseEList<RecordProcedureCallSpecification>(RecordProcedureCallSpecification.class, this, SchemaPackage.SCHEMA_RECORD__PROCEDURES, SchemaPackage.RECORD_PROCEDURE_CALL_SPECIFICATION__RECORD);
		}
		return procedures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Key> getKeys() {
		if (keys == null) {
			keys = new EObjectContainmentWithInverseEList<Key>(Key.class, this, SchemaPackage.SCHEMA_RECORD__KEYS, SchemaPackage.KEY__RECORD);
		}
		return keys;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AreaSpecification getAreaSpecification() {
		if (areaSpecification != null && areaSpecification.eIsProxy()) {
			InternalEObject oldAreaSpecification = (InternalEObject)areaSpecification;
			areaSpecification = (AreaSpecification)eResolveProxy(oldAreaSpecification);
			if (areaSpecification != oldAreaSpecification) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION, oldAreaSpecification, areaSpecification));
			}
		}
		return areaSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AreaSpecification basicGetAreaSpecification() {
		return areaSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAreaSpecification(AreaSpecification newAreaSpecification, NotificationChain msgs) {
		AreaSpecification oldAreaSpecification = areaSpecification;
		areaSpecification = newAreaSpecification;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION, oldAreaSpecification, newAreaSpecification);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAreaSpecification(AreaSpecification newAreaSpecification) {
		if (newAreaSpecification != areaSpecification) {
			NotificationChain msgs = null;
			if (areaSpecification != null)
				msgs = ((InternalEObject)areaSpecification).eInverseRemove(this, SchemaPackage.AREA_SPECIFICATION__RECORD, AreaSpecification.class, msgs);
			if (newAreaSpecification != null)
				msgs = ((InternalEObject)newAreaSpecification).eInverseAdd(this, SchemaPackage.AREA_SPECIFICATION__RECORD, AreaSpecification.class, msgs);
			msgs = basicSetAreaSpecification(newAreaSpecification, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION, newAreaSpecification, newAreaSpecification));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Element> getElements() {
		if (elements == null) {
			elements = new EObjectContainmentWithInverseEList<Element>(Element.class, this, SchemaPackage.SCHEMA_RECORD__ELEMENTS, SchemaPackage.ELEMENT__RECORD);
		}
		return elements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Role> getRoles() {
		EStructuralFeature eFeature = 
			SchemaPackage.Literals.SCHEMA_RECORD__ROLES;		
		Collection<Role> result = new ArrayList<Role>();
		for (Role ownerRole : getOwnerRoles()) {
			result.add(ownerRole);
		}
		for (Role memberRole : getMemberRoles()) {
			result.add(memberRole);
		}
		return new EcoreEList.UnmodifiableEList<Role>(this, eFeature, 
													  result.size(), 
													  result.toArray());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Role getRole(String setName) {
		for (OwnerRole ownerRole : getOwnerRoles()) {
			if (ownerRole.getSet().getName().equals(setName)) {
				return ownerRole;
			}
		}
		for (MemberRole memberRole : getMemberRoles()) {
			if (memberRole.getSet().getName().equals(setName)) {
				return memberRole;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Element getElement(String name) {
		for (Element element : getElements()) {
			if (element.getName().equals(name)) {
				return element;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION:
				if (areaSpecification != null)
					msgs = ((InternalEObject)areaSpecification).eInverseRemove(this, SchemaPackage.AREA_SPECIFICATION__RECORD, AreaSpecification.class, msgs);
				return basicSetAreaSpecification((AreaSpecification)otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__ELEMENTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getElements()).basicAdd(otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__KEYS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getKeys()).basicAdd(otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__MEMBER_ROLES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getMemberRoles()).basicAdd(otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__OWNER_ROLES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getOwnerRoles()).basicAdd(otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__PROCEDURES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getProcedures()).basicAdd(otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__SCHEMA:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetSchema((Schema)otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__VIA_SPECIFICATION:
				if (viaSpecification != null)
					msgs = ((InternalEObject)viaSpecification).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.SCHEMA_RECORD__VIA_SPECIFICATION, null, msgs);
				return basicSetViaSpecification((ViaSpecification)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION:
				return basicSetAreaSpecification(null, msgs);
			case SchemaPackage.SCHEMA_RECORD__ELEMENTS:
				return ((InternalEList<?>)getElements()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__KEYS:
				return ((InternalEList<?>)getKeys()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__MEMBER_ROLES:
				return ((InternalEList<?>)getMemberRoles()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__OWNER_ROLES:
				return ((InternalEList<?>)getOwnerRoles()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__PROCEDURES:
				return ((InternalEList<?>)getProcedures()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_RECORD__SCHEMA:
				return basicSetSchema(null, msgs);
			case SchemaPackage.SCHEMA_RECORD__VIA_SPECIFICATION:
				return basicSetViaSpecification(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case SchemaPackage.SCHEMA_RECORD__SCHEMA:
				return eInternalContainer().eInverseRemove(this, SchemaPackage.SCHEMA__RECORDS, Schema.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.SCHEMA_RECORD__BASE_NAME:
				return getBaseName();
			case SchemaPackage.SCHEMA_RECORD__BASE_VERSION:
				return getBaseVersion();
			case SchemaPackage.SCHEMA_RECORD__CONTROL_LENGTH:
				return getControlLength();
			case SchemaPackage.SCHEMA_RECORD__DATA_LENGTH:
				return getDataLength();
			case SchemaPackage.SCHEMA_RECORD__FRAGMENTED:
				return isFragmented();
			case SchemaPackage.SCHEMA_RECORD__ID:
				return getId();
			case SchemaPackage.SCHEMA_RECORD__LOCATION_MODE:
				return getLocationMode();
			case SchemaPackage.SCHEMA_RECORD__MINIMUM_FRAGMENT_LENGTH:
				return getMinimumFragmentLength();
			case SchemaPackage.SCHEMA_RECORD__MINIMUM_ROOT_LENGTH:
				return getMinimumRootLength();
			case SchemaPackage.SCHEMA_RECORD__NAME:
				return getName();
			case SchemaPackage.SCHEMA_RECORD__PREFIX_LENGTH:
				return getPrefixLength();
			case SchemaPackage.SCHEMA_RECORD__STORAGE_MODE:
				return getStorageMode();
			case SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION:
				if (resolve) return getAreaSpecification();
				return basicGetAreaSpecification();
			case SchemaPackage.SCHEMA_RECORD__CALC_KEY:
				if (resolve) return getCalcKey();
				return basicGetCalcKey();
			case SchemaPackage.SCHEMA_RECORD__ELEMENTS:
				return getElements();
			case SchemaPackage.SCHEMA_RECORD__KEYS:
				return getKeys();
			case SchemaPackage.SCHEMA_RECORD__MEMBER_ROLES:
				return getMemberRoles();
			case SchemaPackage.SCHEMA_RECORD__OWNER_ROLES:
				return getOwnerRoles();
			case SchemaPackage.SCHEMA_RECORD__PROCEDURES:
				return getProcedures();
			case SchemaPackage.SCHEMA_RECORD__ROLES:
				return getRoles();
			case SchemaPackage.SCHEMA_RECORD__ROOT_ELEMENTS:
				return getRootElements();
			case SchemaPackage.SCHEMA_RECORD__SCHEMA:
				return getSchema();
			case SchemaPackage.SCHEMA_RECORD__SYNONYM_NAME:
				return getSynonymName();
			case SchemaPackage.SCHEMA_RECORD__SYNONYM_VERSION:
				return getSynonymVersion();
			case SchemaPackage.SCHEMA_RECORD__VIA_SPECIFICATION:
				return getViaSpecification();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SchemaPackage.SCHEMA_RECORD__BASE_NAME:
				setBaseName((String)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__BASE_VERSION:
				setBaseVersion((Short)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__ID:
				setId((Short)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__LOCATION_MODE:
				setLocationMode((LocationMode)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__MINIMUM_FRAGMENT_LENGTH:
				setMinimumFragmentLength((Short)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__MINIMUM_ROOT_LENGTH:
				setMinimumRootLength((Short)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__NAME:
				setName((String)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__STORAGE_MODE:
				setStorageMode((StorageMode)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION:
				setAreaSpecification((AreaSpecification)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__CALC_KEY:
				setCalcKey((Key)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__ELEMENTS:
				getElements().clear();
				getElements().addAll((Collection<? extends Element>)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__KEYS:
				getKeys().clear();
				getKeys().addAll((Collection<? extends Key>)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__MEMBER_ROLES:
				getMemberRoles().clear();
				getMemberRoles().addAll((Collection<? extends MemberRole>)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__OWNER_ROLES:
				getOwnerRoles().clear();
				getOwnerRoles().addAll((Collection<? extends OwnerRole>)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__PROCEDURES:
				getProcedures().clear();
				getProcedures().addAll((Collection<? extends RecordProcedureCallSpecification>)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__ROOT_ELEMENTS:
				getRootElements().clear();
				getRootElements().addAll((Collection<? extends Element>)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__SCHEMA:
				setSchema((Schema)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__SYNONYM_NAME:
				setSynonymName((String)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__SYNONYM_VERSION:
				setSynonymVersion((Short)newValue);
				return;
			case SchemaPackage.SCHEMA_RECORD__VIA_SPECIFICATION:
				setViaSpecification((ViaSpecification)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case SchemaPackage.SCHEMA_RECORD__BASE_NAME:
				setBaseName(BASE_NAME_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_RECORD__BASE_VERSION:
				setBaseVersion(BASE_VERSION_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_RECORD__ID:
				setId(ID_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_RECORD__LOCATION_MODE:
				setLocationMode(LOCATION_MODE_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_RECORD__MINIMUM_FRAGMENT_LENGTH:
				setMinimumFragmentLength(MINIMUM_FRAGMENT_LENGTH_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_RECORD__MINIMUM_ROOT_LENGTH:
				setMinimumRootLength(MINIMUM_ROOT_LENGTH_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_RECORD__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_RECORD__STORAGE_MODE:
				setStorageMode(STORAGE_MODE_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION:
				setAreaSpecification((AreaSpecification)null);
				return;
			case SchemaPackage.SCHEMA_RECORD__CALC_KEY:
				setCalcKey((Key)null);
				return;
			case SchemaPackage.SCHEMA_RECORD__ELEMENTS:
				getElements().clear();
				return;
			case SchemaPackage.SCHEMA_RECORD__KEYS:
				getKeys().clear();
				return;
			case SchemaPackage.SCHEMA_RECORD__MEMBER_ROLES:
				getMemberRoles().clear();
				return;
			case SchemaPackage.SCHEMA_RECORD__OWNER_ROLES:
				getOwnerRoles().clear();
				return;
			case SchemaPackage.SCHEMA_RECORD__PROCEDURES:
				getProcedures().clear();
				return;
			case SchemaPackage.SCHEMA_RECORD__ROOT_ELEMENTS:
				getRootElements().clear();
				return;
			case SchemaPackage.SCHEMA_RECORD__SCHEMA:
				setSchema((Schema)null);
				return;
			case SchemaPackage.SCHEMA_RECORD__SYNONYM_NAME:
				setSynonymName(SYNONYM_NAME_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_RECORD__SYNONYM_VERSION:
				setSynonymVersion(SYNONYM_VERSION_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_RECORD__VIA_SPECIFICATION:
				setViaSpecification((ViaSpecification)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case SchemaPackage.SCHEMA_RECORD__BASE_NAME:
				return BASE_NAME_EDEFAULT == null ? baseName != null : !BASE_NAME_EDEFAULT.equals(baseName);
			case SchemaPackage.SCHEMA_RECORD__BASE_VERSION:
				return baseVersion != BASE_VERSION_EDEFAULT;
			case SchemaPackage.SCHEMA_RECORD__CONTROL_LENGTH:
				return getControlLength() != CONTROL_LENGTH_EDEFAULT;
			case SchemaPackage.SCHEMA_RECORD__DATA_LENGTH:
				return getDataLength() != DATA_LENGTH_EDEFAULT;
			case SchemaPackage.SCHEMA_RECORD__FRAGMENTED:
				return isFragmented() != FRAGMENTED_EDEFAULT;
			case SchemaPackage.SCHEMA_RECORD__ID:
				return id != ID_EDEFAULT;
			case SchemaPackage.SCHEMA_RECORD__LOCATION_MODE:
				return locationMode != LOCATION_MODE_EDEFAULT;
			case SchemaPackage.SCHEMA_RECORD__MINIMUM_FRAGMENT_LENGTH:
				return MINIMUM_FRAGMENT_LENGTH_EDEFAULT == null ? minimumFragmentLength != null : !MINIMUM_FRAGMENT_LENGTH_EDEFAULT.equals(minimumFragmentLength);
			case SchemaPackage.SCHEMA_RECORD__MINIMUM_ROOT_LENGTH:
				return MINIMUM_ROOT_LENGTH_EDEFAULT == null ? minimumRootLength != null : !MINIMUM_ROOT_LENGTH_EDEFAULT.equals(minimumRootLength);
			case SchemaPackage.SCHEMA_RECORD__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SchemaPackage.SCHEMA_RECORD__PREFIX_LENGTH:
				return getPrefixLength() != PREFIX_LENGTH_EDEFAULT;
			case SchemaPackage.SCHEMA_RECORD__STORAGE_MODE:
				return storageMode != STORAGE_MODE_EDEFAULT;
			case SchemaPackage.SCHEMA_RECORD__AREA_SPECIFICATION:
				return areaSpecification != null;
			case SchemaPackage.SCHEMA_RECORD__CALC_KEY:
				return calcKey != null;
			case SchemaPackage.SCHEMA_RECORD__ELEMENTS:
				return elements != null && !elements.isEmpty();
			case SchemaPackage.SCHEMA_RECORD__KEYS:
				return keys != null && !keys.isEmpty();
			case SchemaPackage.SCHEMA_RECORD__MEMBER_ROLES:
				return memberRoles != null && !memberRoles.isEmpty();
			case SchemaPackage.SCHEMA_RECORD__OWNER_ROLES:
				return ownerRoles != null && !ownerRoles.isEmpty();
			case SchemaPackage.SCHEMA_RECORD__PROCEDURES:
				return procedures != null && !procedures.isEmpty();
			case SchemaPackage.SCHEMA_RECORD__ROLES:
				return !getRoles().isEmpty();
			case SchemaPackage.SCHEMA_RECORD__ROOT_ELEMENTS:
				return rootElements != null && !rootElements.isEmpty();
			case SchemaPackage.SCHEMA_RECORD__SCHEMA:
				return getSchema() != null;
			case SchemaPackage.SCHEMA_RECORD__SYNONYM_NAME:
				return SYNONYM_NAME_EDEFAULT == null ? synonymName != null : !SYNONYM_NAME_EDEFAULT.equals(synonymName);
			case SchemaPackage.SCHEMA_RECORD__SYNONYM_VERSION:
				return synonymVersion != SYNONYM_VERSION_EDEFAULT;
			case SchemaPackage.SCHEMA_RECORD__VIA_SPECIFICATION:
				return viaSpecification != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (baseName: ");
		result.append(baseName);
		result.append(", baseVersion: ");
		result.append(baseVersion);
		result.append(", id: ");
		result.append(id);
		result.append(", locationMode: ");
		result.append(locationMode);
		result.append(", minimumFragmentLength: ");
		result.append(minimumFragmentLength);
		result.append(", minimumRootLength: ");
		result.append(minimumRootLength);
		result.append(", name: ");
		result.append(name);
		result.append(", storageMode: ");
		result.append(storageMode);
		result.append(", synonymName: ");
		result.append(synonymName);
		result.append(", synonymVersion: ");
		result.append(synonymVersion);
		result.append(')');
		return result.toString();
	}	

} //SchemaRecordImpl
