package com.project.reactor.app.validators;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RequestValidator {
	
	private final Validator validador;

	public <T> Mono<T> validate(T obj) {

		if (obj == null) {
			return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
		}

		Set<ConstraintViolation<T>> violations = this.validador.validate(obj);
		if (violations == null || violations.isEmpty()) {
			return Mono.just(obj);
		}

		return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
	}

}
