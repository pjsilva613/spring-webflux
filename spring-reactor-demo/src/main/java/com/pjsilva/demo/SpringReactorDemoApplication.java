package com.pjsilva.demo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringReactorDemoApplication implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(SpringReactorDemoApplication.class);
	private static List<String> platos = new ArrayList<>();
	
	public static void main(String[] args) {
		platos.add("Hamburguesa");
		platos.add("Pizza");
		platos.add("Soda");
		SpringApplication.run(SpringReactorDemoApplication.class, args);
	}
	
	public void crearMono() {
		int x = 30;
		Mono.just(x).subscribe(z -> log.info("Contenido " + z));
	}
	
	public void crearFlux() {
		Flux<String> fxPlatos = Flux.fromIterable(platos);
		//fxPlatos.subscribe(x -> log.info(x));
		
		fxPlatos
			.collectList().subscribe(lista -> log.info(lista.toString()));
		
	}
	
	public void m1doOnNext() {
		Flux<String> fxPlatos = Flux.fromIterable(platos);
		
		fxPlatos
			.doOnNext(p -> log.info(p))
			.subscribe();
	}
	
	public void m2map() {
		Flux<String> fxPlatos = Flux.fromIterable(platos);
		
		Flux<String> fx = fxPlatos
							.map(p -> "Plato: " + p);
							
		fx.subscribe(p -> log.info(p));		
	}
	
	public void m3flatMap() {
		Mono.just("Jaime")
			.flatMap(x -> Mono.just(30))
			.subscribe(p -> log.info(p.toString()));
	}
	
	public void m4range() {
		Flux<Integer> fx1 = Flux.range(0, 10);
		
		fx1
			.map(x -> {
				//más lineas
				return x + 1;
			})
			.subscribe(x -> log.info("N: " + x));
	}
	
	public void m5delayElements() throws InterruptedException {
		Flux.range(0, 10)
			.delayElements(Duration.ofSeconds(2))
			.doOnNext(i -> log.info(i.toString()))
			.subscribe();	
		
		Thread.sleep(20000);
	}
	
	public void m6zipWith() {
		List<String> clientes = new ArrayList<>();
		clientes.add("Jaime");
		clientes.add("Code");
		
		Flux<String> fxPlatos = Flux.fromIterable(platos);
		Flux<String> fxClientes = Flux.fromIterable(clientes);
		
		fxPlatos
			.zipWith(fxClientes, (p, c) -> String.format("Flux1: %s, Flux2: %s", p, c))
			.subscribe(x -> log.info(x));

	}
	
	public void m7merge() {
		List<String> clientes = new ArrayList<>();
		clientes.add("Jaime");
		clientes.add("Code");
		
		Flux<String> fxPlatos = Flux.fromIterable(platos);
		Flux<String> fxClientes = Flux.fromIterable(clientes);
		
		Flux.merge(fxPlatos, fxClientes)
			.subscribe(x -> log.info(x));
	}
	
	public void m8filter() {
		Flux<String> fxPlatos = Flux.fromIterable(platos);

		fxPlatos
		.filter(p -> p.startsWith("H"))
		.subscribe(x -> log.info(x));
	}
	
	public void m9takeLast() {
		Flux<String> fxPlatos = Flux.fromIterable(platos);

		fxPlatos	
			.takeLast(2)
			.subscribe(x -> log.info(x));
	}
	
	public void m10take() {
		Flux<String> fxPlatos = Flux.fromIterable(platos);
		fxPlatos
			.take(2)
			.subscribe(x -> log.info(x));
	}
	
	public void m11DefaultIfEmpty() {
		//platos = new ArrayList<>();

		Flux<String> fxPlatos = Flux.fromIterable(platos);
		fxPlatos
			.defaultIfEmpty("LISTA VACIA")
			.subscribe(x -> log.info(x));
	}
	
	public void m12onErrorReturn() {
		Flux<String> fxPlatos = Flux.fromIterable(platos);

		fxPlatos
			.doOnNext(p -> {				
				//calculo aritmetico
				throw new ArithmeticException("MAL CALCULO");
			})
			.onErrorMap(ex -> new Exception(ex.getMessage()))
			.subscribe(x -> log.info(x));
	}
	
	public void m13retry() {
		Flux<String> fxPlatos = Flux.fromIterable(platos);

		fxPlatos					
			.doOnNext(p -> {
				log.info("intentando....");
				throw new ArithmeticException("MAL CALCULO");
			})			
			.retry(3)			
			.onErrorReturn("ERROR!")			
			.subscribe(x -> log.info(x));		
	}
	

	@Override
	public void run(String... args) throws Exception {
		m13retry();
	}


}
