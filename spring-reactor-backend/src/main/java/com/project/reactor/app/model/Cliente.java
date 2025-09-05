package com.project.reactor.app.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "clientes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

	@Id
	private String id;
	
	@NotNull
	@Size(min = 3)	
	@Field(name = "nombres")
	private String nombres;
	
	@NotNull
	@Size(min = 3)
	@Field(name = "apellidos")
	private String apellidos;
		
	@NotNull
	@Field(name = "fechaNac")
	private LocalDate fechaNac;	
	
	@Field(name = "urlFoto")
	private String urlFoto;
}
