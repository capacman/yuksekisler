package com.yuksekisler.domain.equipment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import com.yuksekisler.domain.ContainsImage;
import com.yuksekisler.domain.IdEnabledEntity;
import com.yuksekisler.domain.Image;
import com.yuksekisler.domain.work.LifeTime;
import com.yuksekisler.domain.work.WorkDefinition;

@Entity
public class Equipment implements IdEnabledEntity<Long>, ContainsImage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6172182731905953514L;

	@Size(max = 500)
	@Column(length = 500)
	private String productName;

	@Size(max = 500)
	@Column(length = 500)
	private String productCode;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(nullable = false)
	private Category category;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(nullable = false)
	private Brand brand;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	@Column(nullable = false)
	private Date stockEntrance;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	@Column(nullable = false)
	private Date bestBeforeDate;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	@Column(nullable = false)
	private Date productionDate;

	// be carefull linked hashset preserve the order
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("inspectionDate ASC")
	@JoinTable
	private Set<InspectionReport> inspectionReports = new LinkedHashSet<InspectionReport>();

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

	@JsonManagedReference("work-equipment")
	@ManyToMany(mappedBy = "equipments", fetch = FetchType.EAGER)
	private Set<WorkDefinition> usedIn = new HashSet<WorkDefinition>();

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

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return this.brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Date getStockEntrance() {
		return this.stockEntrance;
	}

	public void setStockEntrance(Date stockEntrance) {
		this.stockEntrance = stockEntrance;
	}

	public Date getBestBeforeDate() {
		return this.bestBeforeDate;
	}

	public void setBestBeforeDate(Date bestBeforeDate) {
		this.bestBeforeDate = bestBeforeDate;
	}

	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public List<InspectionReport> getInspectionReports() {
		return new ArrayList<InspectionReport>(this.inspectionReports);
	}

	public void addInspectionReport(InspectionReport inspectionReport) {
		this.inspectionReports.add(inspectionReport);
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
		Equipment other = (Equipment) obj;
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

	@Override
	public void addImage(Image image) {
		this.images.add(image);
	}

	@Override
	public Set<Image> getImages() {
		return images;
	}

	public boolean isUsable() {
		List<InspectionReport> inspectionReports = getInspectionReports();
		InspectionStatus lastStatus = inspectionReports.get(
				inspectionReports.size() - 1).getStatus();
		return lastStatus == InspectionStatus.USABLE
				|| lastStatus == InspectionStatus.FIXED;
	}

	public Set<WorkDefinition> getUsedIn() {
		return usedIn;
	}

	public boolean isAvailableFor(LifeTime lifeTime) {
		if (lifeTime == null)
			throw new IllegalArgumentException("lifeTime is null");
		for (WorkDefinition workDefinition : usedIn) {
			if (workDefinition.getLifeTime().isConflictedWith(lifeTime))
				return false;
		}
		return true;
	}

	public boolean isInActiveUse() {
		for (WorkDefinition wd : usedIn) {
			if (!wd.getLifeTime().isFinished())
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Equipment [productName=");
		builder.append(productName);
		builder.append(", productCode=");
		builder.append(productCode);
		builder.append(", category=");
		builder.append(category);
		builder.append(", brand=");
		builder.append(brand);
		builder.append(", stockEntrance=");
		builder.append(stockEntrance);
		builder.append(", bestBeforeDate=");
		builder.append(bestBeforeDate);
		builder.append(", productionDate=");
		builder.append(productionDate);
		builder.append(", inspectionReports=");
		builder.append(inspectionReports);
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}

	public void addedTo(WorkDefinition workDefinition) {
		if (!usedIn.contains(workDefinition)
				&& !workDefinition.getEquipments().contains(this)) {
			workDefinition.addEquipment(this);
			usedIn.add(workDefinition);
		} else if (!usedIn.contains(workDefinition)) {
			usedIn.add(workDefinition);
		}
	}
}
