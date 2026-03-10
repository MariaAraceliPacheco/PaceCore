package com.example.demo.dto.tipoActividad;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Conteo de actividades por tipo")
public class ActividadConteoDTO {

	@Schema(description = "Nombre de la actividad", example = "Carrera")
	private String nombreActividad;
	@Schema(description = "Cantidad total de sesiones", example = "15")
	private long cantidad;

	public ActividadConteoDTO(String nombreActividad, long cantidad) {
		super();
		this.nombreActividad = nombreActividad;
		this.cantidad = cantidad;
	}

	public ActividadConteoDTO() {
		super();
	}

	public String getNombreActividad() {
		return nombreActividad;
	}

	public void setNombreActividad(String nombreActividad) {
		this.nombreActividad = nombreActividad;
	}

	public long getCantidad() {
		return cantidad;
	}

	public void setCantidad(long cantidad) {
		this.cantidad = cantidad;
	}

}
