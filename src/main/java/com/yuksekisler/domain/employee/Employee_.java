package com.yuksekisler.domain.employee;

import com.yuksekisler.domain.Image;
import com.yuksekisler.infrastructure.security.GrantedAuthorityImpl;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-10-18T09:39:03.854+0300")
@StaticMetamodel(Employee.class)
public class Employee_ {
	public static volatile SingularAttribute<Employee, String> name;
	public static volatile SingularAttribute<Employee, Date> startDate;
	public static volatile SingularAttribute<Employee, EmployeeTitle> title;
	public static volatile SetAttribute<Employee, Certificate> certificates;
	public static volatile SingularAttribute<Employee, Phone> phone;
	public static volatile SingularAttribute<Employee, EmployeeIdentity> employeeIdentity;
	public static volatile CollectionAttribute<Employee, GrantedAuthorityImpl> authorities;
	public static volatile SingularAttribute<Employee, String> password;
	public static volatile SingularAttribute<Employee, String> email;
	public static volatile SingularAttribute<Employee, Boolean> accountNonExpired;
	public static volatile SingularAttribute<Employee, Boolean> accountNonLocked;
	public static volatile SingularAttribute<Employee, Boolean> accountEnabled;
	public static volatile SingularAttribute<Employee, Boolean> credentialsNonExpired;
	public static volatile SingularAttribute<Employee, Image> image;
	public static volatile SingularAttribute<Employee, Boolean> erased;
	public static volatile SingularAttribute<Employee, Long> id;
	public static volatile SingularAttribute<Employee, Integer> version;
}
