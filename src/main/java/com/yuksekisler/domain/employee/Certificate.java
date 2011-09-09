package com.yuksekisler.domain.employee;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


@Embeddable
public class Certificate {

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date givenDate;

	@ManyToOne
	private CertificateType type;

	private Boolean expired;

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
