package com.example.demo.dto.entrenos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

import com.example.demo.dto.intervalos.IntervaloInsertDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para crear un nuevo entrenamiento con intervalos")
public class EntrenoCreateDTO {

	@Schema(description = "ID del usuario propietario", example = "1")
	private int id_usuario;
	private Timestamp fecha;
	@Schema(description = "Distancia total del entrenamiento", example = "12.5")
	private BigDecimal distancia;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	@Schema(type = "string", example = "00:00:00")
	private LocalTime tiempo_total;
	@Schema(description = "ID del tipo de actividad", example = "1")
	private int tipo_actividad_id;
	@Schema(description = "Desnivel acumulado", example = "150.0")
	private BigDecimal desnivel;
	@Schema(description = "Título del entrenamiento", example = "Series de 1000m")
	private String titulo;
	@Schema(description = "Descripción detallada", example = "Entrenamiento de series en pista")
	private String descripcion;
	@Schema(description = "Frecuencia cardíaca media", example = "155")
	private Integer fcMedia;
	@Schema(description = "Frecuencia cardíaca máxima", example = "185")
	private Integer fcMaxima;

	private List<IntervaloInsertDTO> intervalos;

	public EntrenoCreateDTO(int id_usuario, Timestamp fecha, BigDecimal distancia, LocalTime tiempo_total,
			int tipo_actividad_id, BigDecimal desnivel, String titulo, String descripcion, Integer fcMedia,
			Integer fcMaxima,
			List<IntervaloInsertDTO> intervalos) {
		super();
		this.id_usuario = id_usuario;
		this.fecha = fecha;
		this.distancia = distancia;
		this.tiempo_total = tiempo_total;
		this.tipo_actividad_id = tipo_actividad_id;
		this.desnivel = desnivel;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.fcMedia = fcMedia;
		this.fcMaxima = fcMaxima;
		this.intervalos = intervalos;
	}

	public EntrenoCreateDTO() {
		super();
	}

	public Integer getFcMedia() {
		return fcMedia;
	}

	public void setFcMedia(Integer fcMedia) {
		this.fcMedia = fcMedia;
	}

	public Integer getFcMaxima() {
		return fcMaxima;
	}

	public void setFcMaxima(Integer fcMaxima) {
		this.fcMaxima = fcMaxima;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
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

	public LocalTime getTiempo_total() {
		return tiempo_total;
	}

	public void setTiempo_total(LocalTime tiempo_total) {
		this.tiempo_total = tiempo_total;
	}

	public int getTipo_actividad_id() {
		return tipo_actividad_id;
	}

	public void setTipo_actividad_id(int tipo_actividad_id) {
		this.tipo_actividad_id = tipo_actividad_id;
	}

	public BigDecimal getDesnivel() {
		return desnivel;
	}

	public void setDesnivel(BigDecimal desnivel) {
		this.desnivel = desnivel;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<IntervaloInsertDTO> getIntervalos() {
		return intervalos;
	}

	public void setIntervalos(List<IntervaloInsertDTO> intervalos) {
		this.intervalos = intervalos;
	}

}
