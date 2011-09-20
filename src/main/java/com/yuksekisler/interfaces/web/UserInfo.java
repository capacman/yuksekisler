package com.yuksekisler.interfaces.web;

public class UserInfo {
	private String name;
	private String surname;

	public UserInfo(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}
}
