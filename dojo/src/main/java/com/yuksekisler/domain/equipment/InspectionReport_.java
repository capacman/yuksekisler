package com.yuksekisler.domain.equipment;

import com.yuksekisler.domain.Comment;
import com.yuksekisler.domain.employee.Employee;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-09-18T16:47:39.254+0300")
@StaticMetamodel(InspectionReport.class)
public class InspectionReport_ {
	public static volatile SingularAttribute<InspectionReport, Employee> inspector;
	public static volatile SingularAttribute<InspectionReport, Date> inspectionDate;
	public static volatile SingularAttribute<InspectionReport, String> report;
	public static volatile SingularAttribute<InspectionReport, InspectionStatus> status;
	public static volatile SetAttribute<InspectionReport, Comment> comments;
	public static volatile SingularAttribute<InspectionReport, Long> id;
	public static volatile SingularAttribute<InspectionReport, Integer> version;
	public static volatile SingularAttribute<InspectionReport, Boolean> enabled;
}
