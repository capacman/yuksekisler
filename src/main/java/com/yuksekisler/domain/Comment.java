package com.yuksekisler.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.yuksekisler.domain.employee.Employee;

@Entity
public class Comment implements IdEnabledEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7927485389994428534L;

	@NotNull
	@ManyToOne
	private Employee owner;

	@ManyToOne
	private com.yuksekisler.domain.Comment replyTo;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date creationDate;

	@Size(max = 10000)
	private String content;

	protected Comment() {
	}

	public Comment(Employee owner, Comment replyTo, Date creationDate,
			String content) {
		this.owner = owner;
		this.replyTo = replyTo;
		this.creationDate = creationDate;
		this.content = content;
	}

	public Employee getOwner() {
		return this.owner;
	}

	public Comment getReplyTo() {
		return this.replyTo;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public String getContent() {
		return this.content;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Content: ").append(getContent()).append(", ");
		sb.append("CreationDate: ").append(getCreationDate()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("Owner: ").append(getOwner()).append(", ");
		sb.append("Version: ").append(getVersion());
		return sb.toString();
	}

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

	public Long getId() {
		return this.id;
	}

	public Integer getVersion() {
		return this.version;
	}

	@Override
	public Boolean getErased() {
		return erased;
	}

	@Override
	public void setErased(Boolean value) {
		this.erased = value;

	}

}
