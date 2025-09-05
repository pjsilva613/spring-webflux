package com.project.reactor.app.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.reactor.app.model.Cliente;

public interface IClienteRepository  extends ReactiveMongoRepository<Cliente, String>{

}
