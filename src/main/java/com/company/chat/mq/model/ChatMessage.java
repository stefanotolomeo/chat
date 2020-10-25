package com.company.chat.mq.model;

import com.company.chat.dao.model.Message;

public class ChatMessage {

	private final String type = MessageType.MESSAGE.name();
	private Message message;

	public String getType() {
		return type;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ChatMessage{" + "type='" + type + '\'' + ", message=" + message + '}';
	}
}
