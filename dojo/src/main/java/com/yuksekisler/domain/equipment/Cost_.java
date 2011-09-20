package com.yuksekisler.domain.equipment;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2011-09-18T16:47:39.132+0300")
@StaticMetamodel(Cost.class)
public class Cost_ {
	public static volatile SingularAttribute<Cost, BigDecimal> monetaryValue;
	public static volatile SingularAttribute<Cost, Currency> currency;
}
