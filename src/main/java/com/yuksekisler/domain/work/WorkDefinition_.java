package com.yuksekisler.domain.work;

import com.yuksekisler.domain.Comment;
import com.yuksekisler.domain.employee.Employee;
import com.yuksekisler.domain.equipment.Equipment;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-10-25T13:32:27.806+0300")
@StaticMetamodel(WorkDefinition.class)
public class WorkDefinition_ {
	public static volatile SingularAttribute<WorkDefinition, Date> startDate;
	public static volatile SingularAttribute<WorkDefinition, Date> endDate;
	public static volatile SingularAttribute<WorkDefinition, String> name;
	public static volatile SetAttribute<WorkDefinition, Employee> supervisors;
	public static volatile SingularAttribute<WorkDefinition, String> customer;
	public static volatile SetAttribute<WorkDefinition, Employee> workers;
	public static volatile SetAttribute<WorkDefinition, Equipment> equipments;
	public static volatile SetAttribute<WorkDefinition, Comment> comments;
	public static volatile SingularAttribute<WorkDefinition, Long> id;
	public static volatile SingularAttribute<WorkDefinition, Integer> version;
	public static volatile SingularAttribute<WorkDefinition, Boolean> erased;
}
