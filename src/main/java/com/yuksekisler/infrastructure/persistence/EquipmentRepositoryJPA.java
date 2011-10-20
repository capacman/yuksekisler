package com.yuksekisler.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.yuksekisler.domain.equipment.EquipmentRepository;

@Repository
public class EquipmentRepositoryJPA extends AbstractBaseRepositoryJPA implements
		EquipmentRepository {

}
