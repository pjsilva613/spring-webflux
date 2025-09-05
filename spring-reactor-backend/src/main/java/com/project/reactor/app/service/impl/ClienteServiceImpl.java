package com.project.reactor.app.service.impl;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;

import com.project.reactor.app.model.Cliente;
import com.project.reactor.app.repository.IClienteRepository;
import com.project.reactor.app.service.IClienteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl extends CRUDImpl<Cliente, String> implements IClienteService {

	
	private final IClienteRepository repository;

	@Override
	protected ReactiveMongoRepository<Cliente, String> getRepo() {		
		return repository;
	}
	

}
