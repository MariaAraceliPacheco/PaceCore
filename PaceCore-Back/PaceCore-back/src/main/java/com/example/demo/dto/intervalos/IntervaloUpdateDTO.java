package com.example.demo.dto.intervalos;

import java.math.BigDecimal;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para actualizar un intervalo específico")
public class IntervaloUpdateDTO {

	@Schema(description = "ID del nuevo tipo de actividad", example = "1")
	private int tipo_actividad_id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	@Schema(type = "string", example = "00:00:00")
	private LocalTime duracion;

	@Schema(type = "string", example = "2,4", description = "Nueva distancia")
	private BigDecimal distancia;

	@Schema(type = "string", example = "8", description = "Nuevo desnivel")
	private BigDecimal desnivel;

	public IntervaloUpdateDTO(int tipo_actividad_id, LocalTime duracion, BigDecimal distancia, BigDecimal desnivel) {
		super();
		this.tipo_actividad_id = tipo_actividad_id;
		this.duracion = duracion;
		this.distancia = distancia;
		this.desnivel = desnivel;
	}

	public IntervaloUpdateDTO() {
		super();
	}

	public BigDecimal getDesnivel() {
		return desnivel;
	}

	public void setDesnivel(BigDecimal desnivel) {
		this.desnivel = desnivel;
	}

	public int getTipo_actividad_id() {
		return tipo_actividad_id;
	}

	public void setTipo_actividad_id(int tipo_actividad_id) {
		this.tipo_actividad_id = tipo_actividad_id;
	}

	public LocalTime getDuracion() {
		return duracion;
	}

	public void setDuracion(LocalTime duracion) {
		this.duracion = duracion;
	}

	public BigDecimal getDistancia() {
		return distancia;
	}

	public void setDistancia(BigDecimal distancia) {
		this.distancia = distancia;
	}

}
