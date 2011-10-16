package com.yuksekisler.domain;

import java.util.List;

public interface UploadedRepository extends BaseRepository {
	<E extends Uploaded> List<E> getByUploadId(String uploadID, Class<E> clazz);
}
