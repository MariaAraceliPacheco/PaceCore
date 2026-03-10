package com.example.demo.dto.zonas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalle de una zona de entrenamiento configurada")
public class ZonasResponseDTO {
	@Schema(description = "ID de la zona", example = "1")
	private int id;
	@Schema(description = "ID del usuario", example = "1")
	private int usuario_id;
	@Schema(description = "Nombre de la zona", example = "Aeróbico ligero")
	private String nombre_zona;
	@Schema(description = "Frecuencia cardíaca mínima", example = "120")
	private int fc_minima;
	@Schema(description = "Frecuencia cardíaca máxima", example = "140")
	private int fc_maxima;
	@Schema(description = "Número correlativo de la zona", example = "1")
	private int numero_zona;
	@Schema(description = "Descripción de la zona", example = "Zona para recuperación activa")
	private String descripcion;

	public ZonasResponseDTO(int id, int usuario_id, String nombre_zona, int fc_minima, int fc_maxima, int numero_zona,
			String descripcion) {
		super();
		this.id = id;
		this.usuario_id = usuario_id;
		this.nombre_zona = nombre_zona;
		this.fc_minima = fc_minima;
		this.fc_maxima = fc_maxima;
		this.numero_zona = numero_zona;
		this.descripcion = descripcion;
	}

	public ZonasResponseDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUsuario_id() {
		return usuario_id;
	}

	public void setUsuario_id(int usuario_id) {
		this.usuario_id = usuario_id;
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

	public int getNumero_zona() {
		return numero_zona;
	}

	public void setNumero_zona(int numero_zona) {
		this.numero_zona = numero_zona;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
