package com.project.reactor.app.controller;


import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import static reactor.function.TupleUtils.function;

import java.net.URI;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.reactor.app.model.Factura;
import com.project.reactor.app.pagination.PageSupport;
import com.project.reactor.app.service.IFacturaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/facturas")
@RequiredArgsConstructor
public class FacturaController {
	

	private final IFacturaService facturaService;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Factura>>> listar(){
		Flux<Factura> fxFacturas = facturaService.listar();
		
		return Mono.just(ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(fxFacturas)
				);
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Factura>> listarPorId(@PathVariable("id") String id){
		return facturaService.listarPorId(id) //Mono<Factura> -> Mono<ResponseEntity<Factura>>
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)
						) //Mono<ResponseEntity<Factura>>
				.defaultIfEmpty(ResponseEntity.notFound().build());
		
	}
	
	@PostMapping
	public Mono<ResponseEntity<Factura>> registrar(@Valid @RequestBody Factura factura, final ServerHttpRequest req){
		//201 | localhost:8080/clientes/123yanss
		return facturaService.registrar(factura)
				.map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)
				);
	}
	
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Factura>> modificar(@PathVariable("id") String id, @Valid @RequestBody Factura factura){
		
		Mono<Factura> monoBody = Mono.just(factura);
		Mono<Factura> monoBD = facturaService.listarPorId(id);
		
		return monoBD
				.zipWith(monoBody, (bd, f) -> {
					bd.setId(id);
					bd.setCliente(f.getCliente());
					bd.setDescripcion(f.getDescripcion());
					bd.setObservacion(f.getObservacion());
					bd.setItems(f.getItems());
					return bd;
				})
				.flatMap(facturaService::modificar) //bd -> service.modificar(bd)
				.map(pl -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(pl))
				.defaultIfEmpty(new ResponseEntity<Factura>(HttpStatus.NOT_FOUND));
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable("id") String id){
		return facturaService.listarPorId(id)
				.flatMap(p -> {
					return facturaService.eliminar(p.getId()) //Mono<Void>
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));					
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
	private Factura platoHateoas;
	
	@GetMapping("/hateoas/{id}")
	public Mono<EntityModel<Factura>> listarHateoasPorId(@PathVariable("id") String id){
		//localhost:8080/clientes/60779cc08e37a27164468033
		Mono<Link> link1 =linkTo(methodOn(FacturaController.class).listarPorId(id)).withSelfRel().toMono();
		Mono<Link> link2 =linkTo(methodOn(FacturaController.class).listarPorId(id)).withSelfRel().toMono();
		
		//PRACTICA NO RECOMENDADA
		/*return service.listarPorId(id)
				.flatMap(p -> {
					this.platoHateoas = p;
					return link1;
				})
				.map(lk -> {
					return EntityModel.of(this.platoHateoas, lk);
				});*/
		
		//PRACTICA INTERMEDIA
		/*return service.listarPorId(id)
				.flatMap(p -> {
					return link1.map(lk -> EntityModel.of(p, lk));
				});*/
		
		//PRACTICA IDEAL
		/*return service.listarPorId(id)
				.zipWith(link1, (p, lk) -> EntityModel.of(p, lk));*/

		//Más de 1 link
		return link1
				.zipWith(link2)
				.map(function((left, right) -> Links.of(left, right)))
				.zipWith(facturaService.listarPorId(id), (lk, p) -> EntityModel.of(p, lk));							
	}
	
	@GetMapping("/pageable")
	public Mono<ResponseEntity<PageSupport<Factura>>> listarPagebale(
			@RequestParam(name = "page", defaultValue = "0") int page,
		    @RequestParam(name = "size", defaultValue = "10") int size
			){
	
		Pageable pageRequest = PageRequest.of(page, size);
		
		return facturaService.listarPage(pageRequest)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)	
						)
				.defaultIfEmpty(ResponseEntity.noContent().build());
	}
}

