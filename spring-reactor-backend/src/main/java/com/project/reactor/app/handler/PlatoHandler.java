package com.project.reactor.app.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.project.reactor.app.model.Plato;
import com.project.reactor.app.service.IPlatoService;
import com.project.reactor.app.validators.RequestValidator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PlatoHandler {

	private final IPlatoService platoService;
	
	private final Validator validador;
	
	private final RequestValidator validadorGeneral;
	
	public Mono<ServerResponse> listar(ServerRequest req){
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(platoService.listar(), Plato.class);
	}
	
	public Mono<ServerResponse> listarPorId(ServerRequest req){
		String id = req.pathVariable("id");
		
		return platoService.listarPorId(id)
				.flatMap(p -> ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(fromValue(p))
						)
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> registrar(ServerRequest req){
		Mono<Plato> monoPlato = req.bodyToMono(Plato.class);			
		
		/*return monoPlato
				.flatMap(p -> {
					Errors errores = new BeanPropertyBindingResult(p, Plato.class.getName());
					validador.validate(p, errores);
					
					if(errores.hasErrors()) {
						return Flux.fromIterable(errores.getFieldErrors())
								.map(error -> new ValidacionDTO(error.getField(), error.getDefaultMessage()))						
								.collectList() //Mono<List<ValidacionDTO>
								.flatMap(listaErrores -> {							
									return ServerResponse.badRequest()
											.contentType(MediaType.APPLICATION_JSON)
											.body(fromValue(listaErrores));	
											}
										); 
			
					}else {
						return service.registrar(p)
								.flatMap(pdb -> ServerResponse
								.created(URI.create(req.uri().toString().concat(p.getId())))
								.contentType(MediaType.APPLICATION_JSON)
								.body(fromValue(pdb))
								);
					}
					
				});*/
		
		return monoPlato
				//validacion
				.flatMap(validadorGeneral::validate) //p -> validadorGeneral.validate(p)
				.flatMap(p -> platoService.registrar(p))
				.flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				);	
	}
	
	public Mono<ServerResponse> modificar(ServerRequest req){
		Mono<Plato> monoPlato = req.bodyToMono(Plato.class);
		Mono<Plato> monoBD = platoService.listarPorId(req.pathVariable("id"));
				
		return monoBD
				.zipWith(monoPlato, (bd, p) -> {				
					bd.setId(p.getId());
					bd.setNombre(p.getNombre());
					bd.setEstado(p.getEstado());
					return bd;
				})		
				.flatMap(validadorGeneral::validate)
				.flatMap(platoService::modificar)
				.flatMap(p -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				)
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest req){
		String id = req.pathVariable("id");

		return platoService.listarPorId(id)
				.flatMap(p -> platoService.eliminar(p.getId())
							.then(ServerResponse.noContent().build())
				)
				.switchIfEmpty(ServerResponse.notFound().build());
		
	}
}

