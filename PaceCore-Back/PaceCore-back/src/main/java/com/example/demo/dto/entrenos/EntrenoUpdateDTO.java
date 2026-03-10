package com.example.demo.dto.entrenos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

import com.example.demo.dto.intervalos.IntervaloInsertDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para actualizar un entrenamiento existente")
public class EntrenoUpdateDTO {

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Timestamp fecha;
	@Schema(description = "Nueva distancia", example = "10.0")
	private BigDecimal distancia;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	@Schema(type = "string", example = "00:00:00")
	private LocalTime tiempo_total;
	@Schema(description = "ID del tipo de actividad", example = "1")
	private int tipo_actividad_id;

	@Schema(description = "Nueva descripción", example = "Salida matinal")
	private String descripcion;
	@Schema(description = "Nuevo título", example = "Rodaje regenerativo")
	private String titulo;
	@Schema(description = "Nuevo desnivel", example = "100.0")
	private BigDecimal desnivel;
	@Schema(description = "Frecuencia cardíaca media", example = "140")
	private Integer fcMedia;
	@Schema(description = "Frecuencia cardíaca máxima", example = "165")
	private Integer fcMaxima;
	@Schema(description = "Zona de entrenamiento máxima alcanzada", example = "3")
	private Integer zonaAlcanzada;

	private List<IntervaloInsertDTO> intervalos;

	public EntrenoUpdateDTO(Timestamp fecha, BigDecimal distancia, LocalTime tiempo_total, int tipo_actividad_id,
			String descripcion, String titulo, BigDecimal desnivel, Integer fcMedia, Integer fcMaxima,
			List<IntervaloInsertDTO> intervalos, Integer zonaAlcanzada) {
		super();
		this.fecha = fecha;
		this.distancia = distancia;
		this.tiempo_total = tiempo_total;
		this.tipo_actividad_id = tipo_actividad_id;
		this.descripcion = descripcion;
		this.titulo = titulo;
		this.desnivel = desnivel;
		this.fcMedia = fcMedia;
		this.fcMaxima = fcMaxima;
		this.intervalos = intervalos;
		this.zonaAlcanzada = zonaAlcanzada;
	}

	public EntrenoUpdateDTO() {
		super();
	}

	public Integer getZonaAlcanzada() {
		return zonaAlcanzada;
	}

	public void setZonaAlcanzada(Integer zonaAlcanzada) {
		this.zonaAlcanzada = zonaAlcanzada;
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

	public List<IntervaloInsertDTO> getIntervalos() {
		return intervalos;
	}

	public void setIntervalos(List<IntervaloInsertDTO> intervalos) {
		this.intervalos = intervalos;
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

}
