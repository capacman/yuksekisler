package com.yuksekisler.domain.equipment;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.yuksekisler.domain.ContainsImage;
import com.yuksekisler.domain.IdEnabledEntity;
import com.yuksekisler.domain.Image;
import com.yuksekisler.domain.employee.Employee;

@Entity
public class InspectionReport implements IdEnabledEntity<Long>, ContainsImage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6592000860553641586L;

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Employee inspector;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	@Column(nullable = false)
	private Date inspectionDate;

	@Size(max = 10000)
	@Column(length = 10000)
	private String report;

	@NotNull
	@Enumerated
	@Column(nullable = false)
	private InspectionStatus status;

	public InspectionReport(Employee inspector, Date inspectionDate,
			String comment, InspectionStatus status) {
		super();
		this.inspector = inspector;
		this.inspectionDate = inspectionDate;
		this.report = comment;
		this.status = status;
	}

	public Employee getInspector() {
		return this.inspector;
	}

	public Date getInspectionDate() {
		return this.inspectionDate;
	}

	public String getReport() {
		return this.report;
	}

	public InspectionStatus getStatus() {
		return this.status;
	}

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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable
	private Set<Image> images = new HashSet<Image>();

	public InspectionReport() {
		super();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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
		InspectionReport other = (InspectionReport) obj;
		if (id == null) {
			if (other.id != null)
				return false;
			else if (this != other)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public void setStatus(InspectionStatus status) {
		this.status = status;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public void setInspector(Employee inspector) {
		this.inspector = inspector;
	}

	public Boolean getErased() {
		return erased;
	}

	public void setErased(Boolean enabled) {
		this.erased = enabled;
	}

	@Override
	public void addImage(Image image) {
		images.add(image);
	}

	@Override
	public Set<Image> getImages() {
		return images;
	}
}
