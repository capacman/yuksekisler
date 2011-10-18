package com.yuksekisler.domain.employee;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Embeddable
public class Certificate {

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date givenDate;

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private CertificateType type;

	private Boolean expired;

	protected Certificate() {
	}

	public Certificate(Date givenDate, CertificateType type, Boolean expired) {
		super();
		this.givenDate = givenDate;
		this.type = type;
		this.expired = expired;
	}

	public Date getGivenDate() {
		return this.givenDate;
	}

	public CertificateType getType() {
		return this.type;
	}

	public Boolean getExpired() {
		return this.expired;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Expired: ").append(getExpired()).append(", ");
		sb.append("GivenDate: ").append(getGivenDate()).append(", ");
		sb.append("Type: ").append(getType());
		return sb.toString();
	}
}
