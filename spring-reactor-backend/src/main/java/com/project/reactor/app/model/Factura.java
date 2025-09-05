package com.project.reactor.app.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "facturas")
public class Factura {

	@Id
	private String id;

	@Field(name = "descripcion")
	private String descripcion;

	@Field(name = "observacion")
	private String observacion;

	@Field(name = "cliente")
	private Cliente cliente;

	@Field(name = "items")
	private List<FacturaItem> items;

	private LocalDateTime creadoEn = LocalDateTime.now();
}
