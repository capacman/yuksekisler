package com.yuksekisler.domain.employee;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-09-18T16:47:38.579+0300")
@StaticMetamodel(Certificate.class)
public class Certificate_ {
	public static volatile SingularAttribute<Certificate, Date> givenDate;
	public static volatile SingularAttribute<Certificate, CertificateType> type;
	public static volatile SingularAttribute<Certificate, Boolean> expired;
}
