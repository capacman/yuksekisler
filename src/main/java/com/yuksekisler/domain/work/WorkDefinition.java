package com.yuksekisler.domain.work;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.yuksekisler.domain.Comment;
import com.yuksekisler.domain.IdEnabledEntity;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.equipment.Equipment;

@Entity
public class WorkDefinition implements IdEnabledEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1652328536647975113L;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	@Column(nullable = false)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date endDate;

	@NotNull
	@Size(max = 500)
	@Column(nullable = false, length = 500)
	private String name;

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Employee supervisor;

	@NotNull
	@Size(max = 500)
	@Column(nullable = false, length = 500)
	private String customer;

	@NotNull
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Employee> workers = new HashSet<Employee>();

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Equipment> equipments = new HashSet<Equipment>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Comment> comments = new HashSet<Comment>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@NotNull
	@Basic
	@Column(nullable = false)
	private Boolean erased = false;

	public Long getId() {
		return this.id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Employee getSupervisor() {
		return this.supervisor;
	}

	public void setSupervisor(Employee supervisor) {
		this.supervisor = supervisor;
	}

	public String getCustomer() {
		return this.customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public Set<Employee> getWorkers() {
		return Collections.unmodifiableSet(this.workers);
	}

	public void addWorker(Employee worker) {
		this.workers.add(worker);
	}

	public Set<Equipment> getEquipments() {
		return Collections.unmodifiableSet(this.equipments);
	}

	public void addEquipment(Equipment equipment) {
		this.equipments.add(equipment);
	}

	public Set<Comment> getComments() {
		return Collections.unmodifiableSet(this.comments);
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Comments: ")
				.append(getComments() == null ? "null" : getComments().size())
				.append(", ");
		sb.append("Customer: ").append(getCustomer()).append(", ");
		sb.append("EndDate: ").append(getEndDate()).append(", ");
		sb.append("Equipments: ")
				.append(getEquipments() == null ? "null" : getEquipments()
						.size()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("Name: ").append(getName()).append(", ");
		sb.append("StartDate: ").append(getStartDate()).append(", ");
		sb.append("Supervisor: ").append(getSupervisor()).append(", ");
		sb.append("Version: ").append(getVersion()).append(", ");
		sb.append("Workers: ").append(
				getWorkers() == null ? "null" : getWorkers().size());
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
		WorkDefinition other = (WorkDefinition) obj;
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
