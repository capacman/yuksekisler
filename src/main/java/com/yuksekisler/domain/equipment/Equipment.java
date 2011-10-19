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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.yuksekisler.domain.ContainsImage;
import com.yuksekisler.domain.IdEnabledEntity;
import com.yuksekisler.domain.Image;

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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BestBeforeDate: ").append(getBestBeforeDate()).append(", ");
		sb.append("Brand: ").append(getBrand()).append(", ");
		sb.append("Category: ").append(getCategory()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("InspectionReports: ")
				.append(getInspectionReports() == null ? "null"
						: getInspectionReports().size()).append(", ");
		sb.append("ProductCode: ").append(getProductCode()).append(", ");
		sb.append("ProductName: ").append(getProductName()).append(", ");
		sb.append("ProductionDate: ").append(getProductionDate()).append(", ");
		sb.append("StockEntrance: ").append(getStockEntrance()).append(", ");
		sb.append("Version: ").append(getVersion());
		return sb.toString();
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
}
