package com.company.chat.dao.exceptions;

import com.company.chat.dao.model.OperationType;

public class FailedCRUDException extends Exception {

	private final OperationType operation;

	public FailedCRUDException(OperationType operation, String message) {
		super(message);
		this.operation = operation;
	}

	public FailedCRUDException(OperationType operation, String message, Throwable cause) {
		super(message, cause);
		this.operation = operation;
	}

	public OperationType getOperation() {
		return operation;
	}
}