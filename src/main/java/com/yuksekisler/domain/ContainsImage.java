package com.yuksekisler.domain;

import java.util.Set;

public interface ContainsImage {
	void addImage(Image image);

	Set<Image> getImages();
	
	Long getImage();
	
	boolean hasImage();
}
