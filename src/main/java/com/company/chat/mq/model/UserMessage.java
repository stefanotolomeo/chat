package com.company.chat.mq.model;

import com.company.chat.dao.model.User;

public class UserMessage {

	private final String type = MessageType.USER.name();
	private String action;	// JOIN or LEAVE
	private User user;
	private String error;

	public String getType() {
		return type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "UserMessage{" + "type='" + type + '\'' + ", action='" + action + '\'' + ", user=" + user + ", error='" + error + '\'' + '}';
	}
}
