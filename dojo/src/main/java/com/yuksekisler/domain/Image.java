package com.yuksekisler.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Version;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({ "imageData","thumbnailData" })
@Entity
public class Image implements IdEnabledEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2548369695621104271L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@Basic
	@Column(nullable = false)
	private Boolean erased = false;

	@Basic
	@Column(nullable = false)
	private String mimeType;

	@Basic
	private String title;

	@Basic
	private String description;

	@Lob
	@Column(nullable = false)
	private byte[] imageData;
	
	@Lob
	private byte[] thumbnailData;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public Boolean getErased() {
		return erased;
	}

	@Override
	public void setErased(Boolean value) {
		this.erased = value;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public Integer getVersion() {
		return version;
	}

	public void setThumbnailData(byte[] thumbnailData) {
		this.thumbnailData = thumbnailData;
	}

	public byte[] getThumbnailData() {
		return thumbnailData;
	}

}
