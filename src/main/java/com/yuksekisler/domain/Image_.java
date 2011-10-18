package com.yuksekisler.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-10-18T09:39:03.879+0300")
@StaticMetamodel(Image.class)
public class Image_ {
	public static volatile SingularAttribute<Image, Long> id;
	public static volatile SingularAttribute<Image, Integer> version;
	public static volatile SingularAttribute<Image, Boolean> erased;
	public static volatile SingularAttribute<Image, String> mimeType;
	public static volatile SingularAttribute<Image, String> title;
	public static volatile SingularAttribute<Image, String> description;
	public static volatile SingularAttribute<Image, byte[]> imageData;
	public static volatile SingularAttribute<Image, byte[]> thumbnailData;
	public static volatile SingularAttribute<Image, String> uploadId;
}
