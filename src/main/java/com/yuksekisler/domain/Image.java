package com.yuksekisler.domain;

import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({ "imageData", "thumbnailData" })
@Entity
public class Image implements Uploaded {

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

	@NotNull
	@Basic
	@Column(nullable = false)
	private Boolean erased = false;

	@NotNull
	@Basic
	@Column(nullable = false)
	private String mimeType;

	@Basic
	private String title;

	@Basic
	private String description;

	@NotNull
	@Lob
	@Column(nullable = false)
	private byte[] imageData;

	@Lob
	private byte[] thumbnailData;

	@NotNull
	@Basic
	@Column(nullable = false)
	private String uploadId;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(imageData);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Image other = (Image) obj;
		if (!Arrays.equals(imageData, other.imageData))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String getUploadId() {
		return uploadId;
	}

	@Override
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

}
