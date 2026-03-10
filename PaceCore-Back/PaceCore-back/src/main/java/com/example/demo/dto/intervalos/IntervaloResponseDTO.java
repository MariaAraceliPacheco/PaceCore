package com.example.demo.dto.intervalos;

import java.math.BigDecimal;
import java.time.LocalTime;

import com.example.demo.dto.tipoActividad.TipoActividadResultDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información detallada de un intervalo")
public class IntervaloResponseDTO {
	/*
	 * Esta clase se usará como tipo de una lista que se enviará cuando se cree o se
	 * modifique un entrenamiento nuevo En caso de que un entrenamiento no tenga
	 * intervalos, la lista de intervalos estará vacia, por lo que no es necesario
	 * poner el id de esta clase como Long o Integer, ya que este id nunca será null
	 * 
	 */
	@Schema(description = "ID único del intervalo", example = "101")
	private int id;
	@Schema(description = "Distancia recorrida", example = "5000.0")
	private BigDecimal distancia;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	@Schema(type = "string", example = "00:20:00")
	private LocalTime duracion;

	@JsonProperty("tipo_actividad_id")
	@Schema(description = "Detalle del tipo de actividad")
	private TipoActividadResultDTO tipoActividadId;
	@Schema(description = "Desnivel", example = "20.0")
	private BigDecimal desnivel;
	@Schema(description = "FC Media", example = "150")
	private Integer fcMedia;
	@Schema(description = "FC Máxima", example = "170")
	private Integer fcMaxima;
	@Schema(description = "Zona alcanzada", example = "2")
	private Integer zona_alcanzada;
	@Schema(description = "Ritmo medio por km", example = "4.0")
	private double ritmoMedio;

	public IntervaloResponseDTO(int id, BigDecimal distancia, LocalTime duracion,
			TipoActividadResultDTO tipoActividadId, BigDecimal desnivel, Integer fcMedia, Integer fcMaxima,
			Integer zona_alcanzada, double ritmoMedio) {
		super();
		this.id = id;
		this.distancia = distancia;
		this.duracion = duracion;
		this.tipoActividadId = tipoActividadId;
		this.desnivel = desnivel;
		this.fcMedia = fcMedia;
		this.fcMaxima = fcMaxima;
		this.zona_alcanzada = zona_alcanzada;
		this.ritmoMedio = ritmoMedio;
	}

	public IntervaloResponseDTO() {
		super();
	}

	public double getRitmoMedio() {
		return ritmoMedio;
	}

	public void setRitmoMedio(double ritmoMedio) {
		this.ritmoMedio = ritmoMedio;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public TipoActividadResultDTO getTipoActividadId() {
		return tipoActividadId;
	}

	public void setTipoActividadId(TipoActividadResultDTO tipoActividadId) {
		this.tipoActividadId = tipoActividadId;
	}

	public BigDecimal getDesnivel() {
		return desnivel;
	}

	public void setDesnivel(BigDecimal desnivel) {
		this.desnivel = desnivel;
	}
}
