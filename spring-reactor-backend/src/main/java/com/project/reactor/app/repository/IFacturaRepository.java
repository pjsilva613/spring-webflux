package com.project.reactor.app.repository;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.reactor.app.model.Factura;

import reactor.core.publisher.Flux;

public interface IFacturaRepository extends ReactiveMongoRepository<Factura, String>{

	@Query("{ 'cliente' : { _id : ?0 }}")
	Flux<Factura> obtenerFacturasPorCliente(String idCliente);
	
	@Query("{'creadoEn' : { $gte: ?0, $lt: ?1} }")	
	Flux<Factura> obtenerFacturasPorFecha(LocalDate fechaInicio, LocalDate fechaFin);
	


}
