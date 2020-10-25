package com.company.chat.dao.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class User extends AbstractItem implements Serializable {

	private static final long serialVersionUID = 7146197649656866689L;
	private String id;
	private String username;

	// TODO: eventually implement other user attributes (also from GUI)
	// private String surname;
	// private String email;
	// private String password;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "User{" + "id='" + id + '\'' + ", username='" + username + '\'' + '}';
	}
}
