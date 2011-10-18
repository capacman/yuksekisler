package com.yuksekisler.domain;

import com.yuksekisler.domain.employee.Employee;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-10-18T09:39:03.813+0300")
@StaticMetamodel(Comment.class)
public class Comment_ {
	public static volatile SingularAttribute<Comment, Employee> owner;
	public static volatile SingularAttribute<Comment, Comment> replyTo;
	public static volatile SingularAttribute<Comment, Date> creationDate;
	public static volatile SingularAttribute<Comment, String> content;
	public static volatile SingularAttribute<Comment, Long> id;
	public static volatile SingularAttribute<Comment, Integer> version;
	public static volatile SingularAttribute<Comment, Boolean> erased;
}
