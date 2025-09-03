package com.project.reactor.app.model;


import java.time.LocalDate;

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
@Document(collection = "platos")
public class Plato {

	@Id
	private String id;
		
	@Field(name = "nombre")
	private String nombre;
	
	@Field(name = "precio")
	private Double precio;
	
	@Field(name = "estado")
	private Boolean estado;
}
