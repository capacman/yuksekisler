package com.yuksekisler.domain.equipment;

import java.util.List;

import com.yuksekisler.domain.BaseRepository;
import com.yuksekisler.domain.work.LifeTime;

public interface EquipmentRepository extends BaseRepository {

	List<Equipment> findAvailable(LifeTime lifetime);

}
