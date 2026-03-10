package com.example.demo.dto.zonas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para actualizar una zona de entrenamiento")
public class ZonasUpdateDTO {
	@Schema(description = "ID de la zona", example = "1")
	private int id;
	@Schema(description = "Número correlativo", example = "1")
	private int numero_zona;
	@Schema(description = "Nuevo nombre", example = "Resistencia aeróbica")
	private String nombre_zona;
	@Schema(description = "Nueva FC mínima", example = "130")
	private int fc_minima;
	@Schema(description = "Nueva FC máxima", example = "150")
	private int fc_maxima;
	@Schema(description = "Nueva descripción", example = "Zona de fondo")
	private String descripcion;

	public ZonasUpdateDTO(int id, int numero_zona, String nombre_zona, int fc_minima, int fc_maxima,
			String descripcion) {
		super();
		this.id = id;
		this.numero_zona = numero_zona;
		this.nombre_zona = nombre_zona;
		this.fc_minima = fc_minima;
		this.fc_maxima = fc_maxima;
		this.descripcion = descripcion;
	}

	public ZonasUpdateDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumero_zona() {
		return numero_zona;
	}

	public void setNumero_zona(int numero_zona) {
		this.numero_zona = numero_zona;
	}

	public String getNombre_zona() {
		return nombre_zona;
	}

	public void setNombre_zona(String nombre_zona) {
		this.nombre_zona = nombre_zona;
	}

	public int getFc_minima() {
		return fc_minima;
	}

	public void setFc_minima(int fc_minima) {
		this.fc_minima = fc_minima;
	}

	public int getFc_maxima() {
		return fc_maxima;
	}

	public void setFc_maxima(int fc_maxima) {
		this.fc_maxima = fc_maxima;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
