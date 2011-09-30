package com.yuksekisler.domain.equipment;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-09-18T16:47:39.186+0300")
@StaticMetamodel(Equipment.class)
public class Equipment_ {
	public static volatile SingularAttribute<Equipment, String> productName;
	public static volatile SingularAttribute<Equipment, String> productCode;
	public static volatile SingularAttribute<Equipment, Category> category;
	public static volatile SingularAttribute<Equipment, Brand> brand;
	public static volatile SingularAttribute<Equipment, Date> stockEntrance;
	public static volatile SingularAttribute<Equipment, Date> bestBeforeDate;
	public static volatile SingularAttribute<Equipment, Date> productionDate;
	public static volatile SetAttribute<Equipment, InspectionReport> inspectionReports;
	public static volatile SingularAttribute<Equipment, Long> id;
	public static volatile SingularAttribute<Equipment, Integer> version;
	public static volatile SingularAttribute<Equipment, Boolean> erased;
}