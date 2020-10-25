package com.company.chat.dao.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class User extends AbstractItem implements Serializable {

	private static final long serialVersionUID = 7146197649656866689L;
	private String id;
	private String name;
	private String surname;
	private String email;
	private String password;

	public User(String name, String surname, String email, String password) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", surname='" + surname + '\'' + ", email='" + email + '\''
				+ ", password='" + password + '\'' + '}';
	}
}
