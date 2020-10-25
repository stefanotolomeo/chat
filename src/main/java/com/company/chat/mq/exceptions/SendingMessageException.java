package com.company.chat.mq.exceptions;

import com.company.chat.dao.model.Message;

public class SendingMessageException extends Exception {

	private final Message msg;

	public SendingMessageException(Message msg, String message) {
		super(message);
		this.msg = msg;
	}

	public SendingMessageException(Message msg, String message, Throwable cause) {
		super(message, cause);
		this.msg = msg;
	}

	public Message getMsg() {
		return msg;
	}
}
