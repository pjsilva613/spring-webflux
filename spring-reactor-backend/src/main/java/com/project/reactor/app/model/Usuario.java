package com.project.reactor.app.model;

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
@Document(collection = "usuarios")
public class Usuario {

	@Id
	private String id;

	@Field(name = "usuario")
	private String usuario;

	@Field(name = "clave")
	private String clave;

	@Field(name = "estado")
	private Boolean estado;

	@Field(name = "roles")
	private List<Rol> roles;
}
