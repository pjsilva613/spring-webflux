package com.project.reactor.app.service;

import org.springframework.data.domain.Pageable;

import com.project.reactor.app.pagination.PageSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICRUD<T, ID> {

	Mono<T> registrar(T t);
	Mono<T> modificar(T t);
	Flux<T> listar();
	Mono<T> listarPorId(ID id);
	Mono<Void> eliminar(ID id);
	Mono<PageSupport<T>> listarPage(Pageable page);
}
