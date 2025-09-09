package com.project.reactor.app.service.impl;

import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.reactor.app.pagination.PageSupport;
import com.project.reactor.app.service.ICRUD;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class CRUDImpl<T, ID> implements ICRUD<T, ID> {

	protected abstract ReactiveMongoRepository<T, ID> getRepo();

	@Override
	public Mono<T> registrar(T t) {
		return getRepo().save(t);
	}

	@Override
	public Mono<T> modificar(T t) {
		return getRepo().save(t);
	}

	@Override
	public Flux<T> listar() {
		return getRepo().findAll();
	}

	@Override
	public Mono<T> listarPorId(ID id) {
		return getRepo().findById(id);
	}

	@Override
	public Mono<Void> eliminar(ID id) {
		return getRepo().deleteById(id);
	}

	public Mono<PageSupport<T>> listarPage(Pageable page) {
		return getRepo().findAll() // Flux<T>
				.collectList() // Mono<List<T>>
				.map(list -> new PageSupport<>(list.stream().skip(page.getPageNumber() * page.getPageSize())
						.limit(page.getPageSize()).collect(Collectors.toList()), page.getPageNumber(),
						page.getPageSize(), list.size()));
	}

}
