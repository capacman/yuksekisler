package com.yuksekisler.domain.equipment;

import java.util.List;

import com.yuksekisler.application.QueryParameters;
import com.yuksekisler.domain.BaseRepository;

public interface EquipmentRepository extends BaseRepository<Long, Equipment> {

	List<Equipment> queryEquipment(QueryParameters queryParameters);

}
