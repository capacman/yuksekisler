package com.yuksekisler.domain.equipment;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Embeddable
public class Cost {

	private BigDecimal monetaryValue;

	@NotNull
	@Enumerated
	@Column(nullable = false)
	private Currency currency;

	protected Cost() {
	}

	public Cost(BigDecimal monetaryValue, Currency currency) {
		super();
		this.monetaryValue = monetaryValue;
		this.currency = currency;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Currency: ").append(getCurrency()).append(", ");
		sb.append("MonetaryValue: ").append(getMonetaryValue());
		return sb.toString();
	}

	public BigDecimal getMonetaryValue() {
		return this.monetaryValue;
	}

	public Currency getCurrency() {
		return this.currency;
	}
}
