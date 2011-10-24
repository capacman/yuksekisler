package com.yuksekisler.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.stereotype.Repository;

import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.EquipmentRepository;
import com.yuksekisler.domain.equipment.Equipment_;
import com.yuksekisler.domain.work.LifeTime;
import com.yuksekisler.domain.work.WorkDefinition;
import com.yuksekisler.domain.work.WorkDefinition_;

@Repository
public class EquipmentRepositoryJPA extends AbstractBaseRepositoryJPA implements
		EquipmentRepository {

	@Override
	public List<Equipment> findAvailable(final LifeTime lifetime) {
		List<Equipment> resultList = new ArrayList<Equipment>();
		for (Equipment available : findAll(Equipment.class)) {
			if (available.isAvailableFor(lifetime))
				resultList.add(available);
		}
		return resultList;
	}
}
