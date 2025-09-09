package com.project.reactor.app.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.reactor.app.model.Factura;

public interface IFacturaRepository extends ReactiveMongoRepository<Factura, String>{

}
