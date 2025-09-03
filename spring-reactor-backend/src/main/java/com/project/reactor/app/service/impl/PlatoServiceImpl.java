package com.project.reactor.app.service.impl;

import org.springframework.stereotype.Service;

import com.project.reactor.app.model.Plato;
import com.project.reactor.app.repository.IPlatoRepository;
import com.project.reactor.app.service.IPlatoService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlatoServiceImpl implements IPlatoService {

	
	private final IPlatoRepository iPlatoRepository;
	
	@Override
	public Mono<Plato> registrar(Plato p) {
		return iPlatoRepository.save(p);
	}

	@Override
	public Mono<Plato> modificar(Plato p) {
		return iPlatoRepository.save(p);
	}

	@Override
	public Flux<Plato> listar() {
		return iPlatoRepository.findAll();
	}

	@Override
	public Mono<Plato> listarPorId(String id) {
		return iPlatoRepository.findById(id);
	}

	@Override
	public Mono<Void> eliminar(String id) {
		return iPlatoRepository.deleteById(id);
	}

}
