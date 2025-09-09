package com.project.reactor.app.service.impl;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;

import com.project.reactor.app.model.Plato;
import com.project.reactor.app.repository.IPlatoRepository;
import com.project.reactor.app.service.IPlatoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlatoServiceImpl extends CRUDImpl<Plato, String> implements IPlatoService {

	private final IPlatoRepository repository;

	@Override
	protected ReactiveMongoRepository<Plato, String> getRepo() {
		return repository;
	}

}
