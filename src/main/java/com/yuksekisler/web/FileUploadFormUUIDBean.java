package com.yuksekisler.web;

import java.io.Serializable;
import java.util.UUID;

public class FileUploadFormUUIDBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3084793464332883174L;
	private String uuid = UUID.randomUUID().toString();

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
