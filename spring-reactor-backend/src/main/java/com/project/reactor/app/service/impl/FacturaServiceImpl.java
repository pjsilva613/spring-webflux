package com.project.reactor.app.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;

import com.project.reactor.app.dto.FiltroDTO;
import com.project.reactor.app.model.Factura;
import com.project.reactor.app.repository.IClienteRepository;
import com.project.reactor.app.repository.IFacturaRepository;
import com.project.reactor.app.repository.IPlatoRepository;
import com.project.reactor.app.service.IFacturaService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl extends CRUDImpl<Factura, String> implements IFacturaService {

	
	private final IFacturaRepository repository;
	private final IClienteRepository clienteRepository;
	private final IPlatoRepository platoRepository;

	@Override
	protected ReactiveMongoRepository<Factura, String> getRepo() {		
		return repository;
	}
	
	@Override
	public Flux<Factura> obtenerFacturasPorFiltro(FiltroDTO filtro) {
		String criterio = filtro.getIdCliente() != null ? "C" : "O";
		
		if(criterio.equalsIgnoreCase("C")) {
			return repository.obtenerFacturasPorCliente(filtro.getIdCliente());
		}else {
			return repository.obtenerFacturasPorFecha(filtro.getFechaInicio(), filtro.getFechaFin());
		}
	}

	@Override
	public Mono<byte[]> generarReporte(String idFactura) {
		return repository.findById(idFactura) //Mono<Factura>
				//Obteniendo Cliente				
				.flatMap(f -> {
					return Mono.just(f)
							.zipWith(clienteRepository.findById(f.getCliente().getId()), (fa, cl) -> {
								fa.setCliente(cl);
								return fa;
							});
				})
				//Obteniendo cada Plato
				.flatMap(f -> {						
					return Flux.fromIterable(f.getItems()).flatMap(it -> {					
						return platoRepository.findById(it.getPlato().getId())
								.map(p -> {
									it.setPlato(p);									
									return it;
								});						
					}).collectList().flatMap(list -> {	
						//Seteando la nueva lista a factura
						f.setItems(list);
						return Mono.just(f);
						});						
				})
				.map(f -> {
					InputStream stream;
					try {									
						Map<String, Object> parametros = new HashMap<String, Object>();						
						parametros.put("txt_cliente", f.getCliente().getNombres() + " " + f.getCliente().getApellidos());
						
						stream = getClass().getResourceAsStream("/facturas.jrxml");
						JasperReport report = JasperCompileManager.compileReport(stream);
						JasperPrint print = JasperFillManager.fillReport(report, parametros, new JRBeanCollectionDataSource(f.getItems()));
						return JasperExportManager.exportReportToPdf(print);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return new byte[0];					
				});
	}
}
