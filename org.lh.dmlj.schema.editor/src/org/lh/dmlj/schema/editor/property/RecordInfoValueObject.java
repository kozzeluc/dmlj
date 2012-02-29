package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

public class RecordInfoValueObject {

	private String  					 description;
	private List<ElementInfoValueObject> elementInfoValueObjects = new ArrayList<>();
	private String  					 establishedBy;
	private String  					 documentId;	
	private String  					 documentName;
	private String  					 locationMode;
	private String  					 memberOf;
	private String  					 ownerOf;
	private String						 recordLength;
	private String  				  	 recordName;
	private String  					 withinArea;
	
	public RecordInfoValueObject() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public String getEstablishedBy() {
		return establishedBy;
	}

	public String getDocumentId() {
		return documentId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public List<ElementInfoValueObject> getElementInfoValueObjects() {
		return elementInfoValueObjects;
	}

	public String getLocationMode() {
		return locationMode;
	}

	public String getMemberOf() {
		return memberOf;
	}

	public String getOwnerOf() {
		return ownerOf;
	}

	public String getRecordLength() {
		return recordLength;
	}

	public String getRecordName() {
		return recordName;
	}

	public String getWithinArea() {
		return withinArea;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEstablishedBy(String establishedBy) {
		this.establishedBy = establishedBy;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public void setLocationMode(String locationMode) {
		this.locationMode = locationMode;
	}

	public void setMemberOf(String memberOf) {
		this.memberOf = memberOf;
	}

	public void setOwnerOf(String ownerOf) {
		this.ownerOf = ownerOf;
	}

	public void setRecordLength(String recordLength) {
		this.recordLength = recordLength;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public void setWithinArea(String withinArea) {
		this.withinArea = withinArea;
	}
	
}