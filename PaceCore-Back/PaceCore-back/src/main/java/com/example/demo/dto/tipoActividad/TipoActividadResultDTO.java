package com.example.demo.dto.tipoActividad;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado simplificado de un tipo de actividad")
public class TipoActividadResultDTO {

	@Schema(description = "Nombre de la actividad", example = "Ciclismo")
	private String nombre;
	@Schema(description = "ID único", example = "2")
	private int id;

	public TipoActividadResultDTO(String nombre, int id) {
		super();
		this.nombre = nombre;
		this.id = id;
	}

	public TipoActividadResultDTO() {
		super();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
