package com.project.reactor.app.service.impl;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;

import com.project.reactor.app.model.Factura;
import com.project.reactor.app.repository.IFacturaRepository;
import com.project.reactor.app.service.IFacturaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl extends CRUDImpl<Factura, String> implements IFacturaService {

	
	private final IFacturaRepository repository;

	@Override
	protected ReactiveMongoRepository<Factura, String> getRepo() {		
		return repository;
	}

}
