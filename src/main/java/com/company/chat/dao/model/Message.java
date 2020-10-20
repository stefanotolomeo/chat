package com.company.chat.dao.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private Timestamp timestamp;
	private String content;
	private String userSenderId;
	private String userReceiverId;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserSenderId() {
		return userSenderId;
	}

	public void setUserSenderId(String userSenderId) {
		this.userSenderId = userSenderId;
	}

	public String getUserReceiverId() {
		return userReceiverId;
	}

	public void setUserReceiverId(String userReceiverId) {
		this.userReceiverId = userReceiverId;
	}

	@Override
	public String toString() {
		return "Message{" + "id='" + id + '\'' + ", timestamp=" + timestamp + ", content='" + content + '\'' + ", userSenderId="
				+ userSenderId + ", userReceiverId=" + userReceiverId + '}';
	}
}