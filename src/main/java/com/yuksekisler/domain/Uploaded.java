package com.yuksekisler.domain;

public interface Uploaded extends IdEnabledEntity<Long> {

	void setUploadId(String uploadId);

	String getUploadId();

}
