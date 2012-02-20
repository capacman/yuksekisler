package com.yuksekisler.domain;

import java.util.List;

public interface ContainsImage {
	void addImage(Image image);

	List<Image> getImages();
	
	Long getImage();
	
	boolean hasImage();
}
