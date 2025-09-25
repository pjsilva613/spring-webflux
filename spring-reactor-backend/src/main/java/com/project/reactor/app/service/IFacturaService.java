package com.project.reactor.app.service;

import com.project.reactor.app.dto.FiltroDTO;
import com.project.reactor.app.model.Factura;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IFacturaService extends ICRUD<Factura, String> {

	Flux<Factura> obtenerFacturasPorFiltro(FiltroDTO filtro);

	Mono<byte[]> generarReporte(String idFactura);

}
