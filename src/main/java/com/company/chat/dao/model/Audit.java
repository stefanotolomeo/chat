package com.company.chat.dao.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Audit implements Serializable {

	private static final long serialVersionUID = 4845793731091289525L;

	private String id;

	private LocalDateTime timestamp;

	private OperationType operationType;

	// What is the involved table?
	private String table;

	// What is the record-id involved?
	private String recordId;

	// What is the record-content involved?
	private String recordContent;

	public Audit(String id, LocalDateTime timestamp, OperationType operationType, String table, String recordId, String recordContent) {
		this.id = id;
		this.timestamp = timestamp;
		this.operationType = operationType;
		this.table = table;
		this.recordId = recordId;
		this.recordContent = recordContent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getRecordContent() {
		return recordContent;
	}

	public void setRecordContent(String recordContent) {
		this.recordContent = recordContent;
	}
}
