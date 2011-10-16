package com.yuksekisler.domain.employee;

import javax.persistence.Embeddable;

@Embeddable
public class Phone {

	private String phoneNumber;

	public Phone() {
	}

	public Phone(String phoneNumber) {
		super();
		this.phoneNumber = phoneNumber;

	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PhoneNumber: ").append(getPhoneNumber());
		return sb.toString();
	}
}
