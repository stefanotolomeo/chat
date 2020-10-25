package com.company.chat.mq.model;

import com.company.chat.dao.model.Message;

public class ChatMessage {

	private final String type = MessageType.MESSAGE.name();
	private Message message;
	private String error;

	public String getType() {
		return type;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "ChatMessage{" + "type='" + type + '\'' + ", message=" + message + ", error='" + error + '\'' + '}';
	}
}
