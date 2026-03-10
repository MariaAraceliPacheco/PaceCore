package com.example.demo.dto.tipoActividad;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de un tipo de actividad")
public class TipoActividadDTO {

	@Schema(type = "string", example = "correr")
	private String nombre;

	public TipoActividadDTO(String nombre) {
		super();
		this.nombre = nombre;
	}

	public TipoActividadDTO() {
		super();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
