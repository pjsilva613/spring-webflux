package com.project.reactor.app.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.project.reactor.app.model.Factura;
import com.project.reactor.app.service.IFacturaService;
import com.project.reactor.app.validators.RequestValidator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FacturaHandler {


	private final IFacturaService facturaService;

	private final RequestValidator validadorGeneral;
	
	public Mono<ServerResponse> listar(ServerRequest req){
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(facturaService.listar(), Factura.class);
	}
	
	public Mono<ServerResponse> listarPorId(ServerRequest req){
		String id = req.pathVariable("id");
		
		return facturaService.listarPorId(id)
				.flatMap(f -> ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(fromValue(f))
						)
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> registrar(ServerRequest req){
		Mono<Factura> monoPlato = req.bodyToMono(Factura.class);
		
		return monoPlato
				.flatMap(validadorGeneral::validate) 
				.flatMap(f -> facturaService.registrar(f))
				.flatMap(f -> ServerResponse.created(URI.create(req.uri().toString().concat(f.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(f))
				);			
	}
	
	public Mono<ServerResponse> modificar(ServerRequest req){
		Mono<Factura> monoPlato = req.bodyToMono(Factura.class);
		Mono<Factura> monoBD = facturaService.listarPorId(req.pathVariable("id"));
				
		return monoBD
				.zipWith(monoPlato, (bd, f) -> {				
					bd.setId(req.pathVariable("id"));
					bd.setCliente(f.getCliente());
					bd.setDescripcion(f.getDescripcion());
					bd.setObservacion(f.getObservacion());
					bd.setItems(f.getItems());
					return bd;
				})				
				.flatMap(validadorGeneral::validate) 
				.flatMap(facturaService::modificar)
				.flatMap(f -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(f))
				)
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest req){
		String id = req.pathVariable("id");

		return facturaService.listarPorId(id)
				.flatMap(f -> facturaService.eliminar(f.getId())
							.then(ServerResponse.noContent().build())
				)
				.switchIfEmpty(ServerResponse.notFound().build());
		
	}
}

