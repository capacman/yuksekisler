package com.yuksekisler.domain.equipment;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.yuksekisler.domain.IdEnabledEntity;

@Entity
public class Category implements IdEnabledEntity{

	@NotNull
	@Column(unique = true)
	@Size(max = 500)
	private String name;

	@Size(max = 10000)
	private String description;
	
	@Basic
	@Column(nullable = false)
	private Boolean enabled=true;

	public Category(String name, String description) {
		super();
		this.name = name;
		this.description = description;
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

	public Category() {
		super();
	}

	public Long getId() {
		return this.id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: ").append(getDescription()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("Name: ").append(getName()).append(", ");
		sb.append("Version: ").append(getVersion());
		return sb.toString();
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
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
