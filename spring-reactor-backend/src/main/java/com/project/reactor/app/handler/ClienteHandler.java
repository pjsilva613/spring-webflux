package com.project.reactor.app.handler;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.project.reactor.app.model.Cliente;
import com.project.reactor.app.service.IClienteService;
import com.project.reactor.app.validators.RequestValidator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class ClienteHandler {


	private final IClienteService clienteService;

	private final RequestValidator validadorGeneral;
	
	public Mono<ServerResponse> listar(ServerRequest req){
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(clienteService.listar(), Cliente.class);
	}
	
	public Mono<ServerResponse> listarPorId(ServerRequest req){
		String id = req.pathVariable("id");
		
		return clienteService.listarPorId(id)
				.flatMap(c -> ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(fromValue(c))
						)
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> registrar(ServerRequest req){
		Mono<Cliente> monoCliente = req.bodyToMono(Cliente.class);
		
		return monoCliente
				.flatMap(validadorGeneral::validate) 
				.flatMap(c -> clienteService.registrar(c))
				.flatMap(c -> ServerResponse.created(URI.create(req.uri().toString().concat(c.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(c))
				);			
	}
	
	public Mono<ServerResponse> modificar(ServerRequest req){
		Mono<Cliente> monoCliente = req.bodyToMono(Cliente.class);
		Mono<Cliente> monoBD = clienteService.listarPorId(req.pathVariable("id"));
				
		return monoBD
				.zipWith(monoCliente, (bd, c) -> {				
					bd.setId(req.pathVariable("id"));
					bd.setNombres(c.getNombres());
					bd.setApellidos(c.getApellidos());
					bd.setFechaNac(c.getFechaNac());
					bd.setUrlFoto(c.getUrlFoto());
					return bd;
				})				
				.flatMap(validadorGeneral::validate) 
				.flatMap(clienteService::modificar)
				.flatMap(c -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(c))
				)
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest req){
		String id = req.pathVariable("id");

		return clienteService.listarPorId(id)
				.flatMap(c -> clienteService.eliminar(c.getId())
							.then(ServerResponse.noContent().build())
				)
				.switchIfEmpty(ServerResponse.notFound().build());
		
	}
}

