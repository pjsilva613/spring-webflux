package com.project.reactor.app.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.reactor.app.model.Plato;

public interface IPlatoRepository extends ReactiveMongoRepository<Plato, String>{

}
