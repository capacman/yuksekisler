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
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.yuksekisler.domain.Comment;
import com.yuksekisler.domain.IdEnabledEntity;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.EquipmentNotAwailable;

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
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Employee> supervisors = new HashSet<Employee>();

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

	@Transient
	private LifeTime lifeTime;

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
		if (startDate == null)
			throw new IllegalArgumentException("startDate cannot be null");
		if (getEndDate() != null && startDate.after(getEndDate()))
			throw new IllegalArgumentException("endDate " + endDate
					+ " could not be before startDate " + getStartDate());
		this.startDate = startDate;
		this.lifeTime = new LifeTime(this.startDate, this.endDate);
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		if (endDate != null && getStartDate() != null
				&& endDate.before(getStartDate()))
			throw new IllegalArgumentException("endDate " + endDate
					+ " could not be before startDate " + getStartDate());
		this.endDate = endDate;
		this.lifeTime = new LifeTime(this.startDate, this.endDate);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Employee> getSupervisors() {
		return this.supervisors;
	}

	public boolean addSupervisor(Employee employee) {
		return this.supervisors.add(employee);
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
		if (getLifeTime().isFinished()) {
			throw new WorkAlreadyFinished(this);
		}
		if (!equipment.isAvailableFor(getLifeTime())) {
			throw new EquipmentNotAwailable(equipment, getStartDate(),
					getEndDate());
		}
		this.equipments.add(equipment);
		equipment.addedTo(this);
	}

	public Set<Comment> getComments() {
		return Collections.unmodifiableSet(this.comments);
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	public void removeEquipments() {
		Set<Equipment> unmodifiableSet = new HashSet<Equipment>(equipments);
		for (Equipment equipment : unmodifiableSet) {
			removeEquipment(equipment);
		}
	}

	public void removeEquipment(Equipment equipment) {
		if (equipments.contains(equipment)) {
			equipments.remove(equipment);
			equipment.removedFrom(this);
		}
	}

	public LifeTime getLifeTime() {
		if (lifeTime == null) {
			lifeTime = new LifeTime(startDate, endDate);
		}
		return lifeTime;
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

	public void removeSupervisors() {
		supervisors.clear();
	}

	public void removeWorkers() {
		workers.clear();
	}
}
