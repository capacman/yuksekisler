package com.yuksekisler.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import com.yuksekisler.domain.work.WorkRepository;

@Repository
public class WorkRepositoryJPA extends AbstractBaseRepositoryJPA implements
		WorkRepository {

}
