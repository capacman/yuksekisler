package com.yuksekisler.domain.employee;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.yuksekisler.domain.IdEnabledEntity;

@JsonAutoDetect(getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY)
@Entity
public class EmployeeTitle implements IdEnabledEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 587697845258623293L;

	@NotNull
	@Size(max = 255)
	@Column(length = 255, nullable = false, unique = true)
	private String name;

	@Size(max = 10000)
	@Column(length = 10000)
	private String description;

	@NotNull
	@Basic
	@Column(nullable = false)
	private Boolean erased = false;

	public EmployeeTitle(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: ").append(getDescription()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("Name: ").append(getName()).append(", ");
		sb.append("Version: ").append(getVersion());
		return sb.toString();
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	public EmployeeTitle() {
		super();
	}

	public Long getId() {
		return this.id;
	}

	public Integer getVersion() {
		return this.version;
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
		EmployeeTitle other = (EmployeeTitle) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getErased() {
		return erased;
	}

	public void setErased(Boolean enabled) {
		this.erased = enabled;
	}
}
