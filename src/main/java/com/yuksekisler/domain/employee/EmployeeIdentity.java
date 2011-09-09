package com.yuksekisler.domain.employee;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class EmployeeIdentity {

	@NotNull
	@Size(max = 100)
	private String identityNumber;
	@Enumerated
	@NotNull
	private IdentityType type = IdentityType.TCKN;

	public EmployeeIdentity() {
		super();
	}

	public EmployeeIdentity(String identityNumber, IdentityType type) {
		super();
		this.identityNumber = identityNumber;
		this.type = type;
	}

	public String getIdentityNumber() {
		return this.identityNumber;
	}

	public IdentityType getType() {
		return type;
	}

	public void setType(IdentityType type) {
		this.type = type;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	@Override
	public String toString() {
		return String.format("EmployeeIdentity [identityNumber=%s, type=%s]",
				identityNumber, type);
	}

}
