package com.example.demo.dto.intervalos;

import java.math.BigDecimal;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para insertar un nuevo intervalo o serie")
public class IntervaloInsertDTO {

	@NotNull(message = "La distancia no puede ser null")
	@DecimalMin("0")
	@DecimalMax("99999")
	@Schema(description = "Distancia del intervalo", example = "1000")
	private BigDecimal distancia;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	@Schema(type = "string", example = "00:00:00")
	private LocalTime duracion;

	@NotNull
	@JsonProperty("tipo_actividad_id")
	@Schema(description = "ID del tipo de actividad", example = "1")
	private int tipoActividadId;

	@DecimalMax("99999")
	@Schema(description = "Desnivel acumulado del intervalo", example = "0.0")
	private BigDecimal desnivel;

	@Min(0)
	@Max(250)
	@Schema(description = "Frecuencia cardíaca media en el intervalo", example = "165")
	private Integer fcMedia;

	@Min(0)
	@Max(250)
	@Schema(description = "Frecuencia cardíaca máxima en el intervalo", example = "175")
	private Integer fcMaxima;

	// esto se calcula automaticamente
	@Schema(description = "Zona de intensidad alcanzada", example = "4")
	private Integer zona_alcanzada;

	public IntervaloInsertDTO(BigDecimal distancia, LocalTime duracion, int tipoActividadId, BigDecimal desnivel,
			Integer fcMedia, Integer fcMaxima, Integer zona_alcanzada) {
		super();
		this.distancia = distancia;
		this.duracion = duracion;
		this.tipoActividadId = tipoActividadId;
		this.desnivel = desnivel;
		this.fcMedia = fcMedia;
		this.fcMaxima = fcMaxima;
		this.zona_alcanzada = zona_alcanzada;
	}

	public IntervaloInsertDTO() {
		super();
	}

	public Integer getZona_alcanzada() {
		return zona_alcanzada;
	}

	public void setZona_alcanzada(Integer zona_alcanzada) {
		this.zona_alcanzada = zona_alcanzada;
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

	public BigDecimal getDesnivel() {
		return desnivel;
	}

	public void setDesnivel(BigDecimal desnivel) {
		this.desnivel = desnivel;
	}

	public BigDecimal getDistancia() {
		return distancia;
	}

	public void setDistancia(BigDecimal distancia) {
		this.distancia = distancia;
	}

	public LocalTime getDuracion() {
		return duracion;
	}

	public void setDuracion(LocalTime duracion) {
		this.duracion = duracion;
	}

	public int getTipoActividadId() {
		return tipoActividadId;
	}

	public void setTipoActividadId(int tipoActividadId) {
		this.tipoActividadId = tipoActividadId;
	}
}
