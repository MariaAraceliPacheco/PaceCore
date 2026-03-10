package com.example.demo.dto.entrenos;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos resumidos para insertar un entrenamiento")
public class EntrenoInsertDTO {
	@Schema(description = "ID del usuario", example = "1")
	private int usuario_id;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Timestamp fecha;

	@Schema(description = "Distancia total", example = "8.0")
	private BigDecimal distancia;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	@Schema(type = "string", example = "00:00:00")
	private String tiempo_total;
	@Schema(description = "ID del tipo de actividad", example = "2")
	private int tipo_actividad_id;
	@Schema(description = "Descripción", example = "Rodaje suave")
	private String descripcion;
	@Schema(description = "Título", example = "Rodaje Z2")
	private String titulo;
	@Schema(description = "Desnivel", example = "50.0")
	private BigDecimal desnivel;

	public EntrenoInsertDTO(int usuario_id, Timestamp fecha, BigDecimal distancia, String tiempo_total,
			int tipo_actividad_id, String descripcion, String titulo, BigDecimal desnivel) {
		super();
		this.usuario_id = usuario_id;
		this.fecha = fecha;
		this.distancia = distancia;
		this.tiempo_total = tiempo_total;
		this.tipo_actividad_id = tipo_actividad_id;
		this.descripcion = descripcion;
		this.titulo = titulo;
		this.desnivel = desnivel;
	}

	public EntrenoInsertDTO() {
		super();
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public BigDecimal getDesnivel() {
		return desnivel;
	}

	public void setDesnivel(BigDecimal desnivel) {
		this.desnivel = desnivel;
	}

	public int getUsuario_id() {
		return usuario_id;
	}

	public void setUsuario_id(int usuario_id) {
		this.usuario_id = usuario_id;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getDistancia() {
		return distancia;
	}

	public void setDistancia(BigDecimal distancia) {
		this.distancia = distancia;
	}

	public String getTiempo_total() {
		return tiempo_total;
	}

	public void setTiempo_total(String tiempo_total) {
		this.tiempo_total = tiempo_total;
	}

	public int getTipo_actividad_id() {
		return tipo_actividad_id;
	}

	public void setTipo_actividad_id(int tipo_actividad_id) {
		this.tipo_actividad_id = tipo_actividad_id;
	}

}
