package com.project.reactor.app.service;

import com.project.reactor.app.model.Plato;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPlatoService {

	Mono<Plato> registrar(Plato p);

	Mono<Plato> modificar(Plato p);

	Flux<Plato> listar();

	Mono<Plato> listarPorId(String id);

	Mono<Void> eliminar(String id);

}
