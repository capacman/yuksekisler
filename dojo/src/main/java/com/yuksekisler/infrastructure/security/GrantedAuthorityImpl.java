package com.yuksekisler.infrastructure.security;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.security.core.GrantedAuthority;

@Embeddable
public class GrantedAuthorityImpl implements GrantedAuthority {

	public static final GrantedAuthority USER = new GrantedAuthorityImpl(
			"ROLE_USER");
	public static final GrantedAuthority ADMIN = new GrantedAuthorityImpl(
			"ROLE_ADMIN");

	/**
	 * 
	 */
	private static final long serialVersionUID = -1523058200451462079L;
	@Basic
	@Column(length = 100)
	private String authority;

	GrantedAuthorityImpl(String authority) {
		this.authority = authority;
	}

	GrantedAuthorityImpl() {
	}

	@Override
	public String getAuthority() {
		return authority;
	}

	@Override
	public String toString() {
		return authority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authority == null) ? 0 : authority.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GrantedAuthorityImpl other = (GrantedAuthorityImpl) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		return true;
	}

}
