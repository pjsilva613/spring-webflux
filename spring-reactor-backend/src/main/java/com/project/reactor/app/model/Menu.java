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
@Document(collection = "menus")
public class Menu {

	@Id
	private String id;

	@Field(name = "icono")
	private String icono;

	@Field(name = "nombre")
	private String nombre;

	@Field(name = "url")
	private String url;

	@Field(name = "roles")
	private List<String> roles;
}
