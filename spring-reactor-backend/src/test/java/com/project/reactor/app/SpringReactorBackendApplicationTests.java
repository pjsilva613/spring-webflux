package com.project.reactor.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.project.reactor.app.model.Plato;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class SpringReactorBackendApplicationTests {

	private final WebTestClient cliente;

	/*
	 * @Test void listarTest() { cliente.get() .uri("/platos") .exchange()
	 * .expectStatus().isOk()
	 * .expectHeader().contentType(MediaType.APPLICATION_JSON)
	 * .expectBodyList(Plato.class) .hasSize(4); }
	 * 
	 * @Test void registrarTest() { Plato plato = new Plato();
	 * plato.setNombre("pachamanca"); plato.setPrecio(20.0); plato.setEstado(true);
	 * 
	 * cliente.post() .uri("/platos") .body(Mono.just(plato), Plato.class)
	 * .exchange() .expectStatus().isCreated()
	 * .expectHeader().contentType(MediaType.APPLICATION_JSON) .expectBody()
	 * .jsonPath("$.nombre").isNotEmpty() .jsonPath("$.precio").isNumber(); }
	 */

	@Test
	void modificarTest() {
		Plato plato = new Plato();
		plato.setId("60fa2ce25eb4c66cc8825c67");
		plato.setNombre("arroz pollo");
		plato.setPrecio(25.0);
		plato.setEstado(true);

		cliente.put().uri("/platos/" + plato.getId()).body(Mono.just(plato), Plato.class).exchange().expectStatus()
				.isOk().expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.nombre")
				.isNotEmpty().jsonPath("$.precio").isNumber();
	}

	@Test
	void eliminarTest() {
		Plato plato = new Plato();
		plato.setId("60fa2ce25eb4c66cc8825c67");

		cliente.delete().uri("/platos/" + plato.getId()).exchange().expectStatus().isNoContent();
	}

}
