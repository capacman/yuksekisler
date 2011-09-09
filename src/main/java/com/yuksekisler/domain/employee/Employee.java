package com.yuksekisler.domain.employee;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yuksekisler.domain.IdEnabledEntity;
import com.yuksekisler.infrastructure.security.GrantedAuthorityImpl;

@Entity
public class Employee implements UserDetails, CredentialsContainer,
		IdEnabledEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(max = 1000)
	private String name;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date startDate;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private EmployeeTitle title;

	@ElementCollection
	private Set<Certificate> certificates = new HashSet<Certificate>();;

	@com.yuksekisler.domain.validation.Phone
	@Embedded
	private Phone phone;

	@Embedded
	private EmployeeIdentity employeeIdentity=new EmployeeIdentity();

	@ElementCollection(targetClass = GrantedAuthorityImpl.class, fetch = FetchType.EAGER)
	private Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

	private String password;
	@Email
	@NotNull
	@Column(unique = true)
	private String email;

	private boolean accountNonExpired = true;

	private boolean accountNonLocked = true;

	private boolean accountEnabled = true;

	private boolean credentialsNonExpired = true;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public EmployeeTitle getTitle() {
		return this.title;
	}

	public void setTitle(EmployeeTitle title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Certificate> getCertificates() {
		return Collections.unmodifiableSet(this.certificates);
	}

	public void addCertificate(Certificate certificate) {
		this.certificates.add(certificate);
	}

	public Phone getPhone() {
		return this.phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public void setEmployeeIdentity(EmployeeIdentity employeeIdentity) {
		this.employeeIdentity = employeeIdentity;
	}

	public EmployeeIdentity getEmployeeIdentity() {
		return employeeIdentity;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yuksekisler.domain.employee.IdEnabledEntity#getId()
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	public Integer getVersion() {
		return this.version;
	}

	@Override
	public void eraseCredentials() {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean value) {
		this.accountNonExpired = value;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean value) {
		this.accountNonLocked = value;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean value) {
		this.credentialsNonExpired = value;
	}

	@Override
	public boolean isEnabled() {
		return accountEnabled;
	}

	public void setEnabled(boolean value) {
		this.accountEnabled = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Employee other = (Employee) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String
				.format("Employee [name=%s, startDate=%s, title=%s, phone=%s, employeeIdentity=%s, authorities=%s, password=%s, email=%s, accountNonExpired=%s, accountNonLocked=%s, accountEnabled=%s, credentialsNonExpired=%s, id=%s, version=%s]",
						name, startDate, title, phone, employeeIdentity,
						authorities, password, email, accountNonExpired,
						accountNonLocked, accountEnabled,
						credentialsNonExpired, id, version);
	}
}
