package com.example.demo.dto.usuarios;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.example.demo.dto.tipoActividad.TipoActividadResultDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estadísticas semanales de entrenamiento")
public class UsuarioEstadisticasSemanalesDTO {

	private Timestamp fecha;
	@Schema(description = "Distancia recorrida en el bloque", example = "10.2")
	private BigDecimal distancia;
	@Schema(description = "Tipo de actividad realizada")
	private TipoActividadResultDTO tipoActividad;
	@Schema(description = "Frecuencia cardíaca media", example = "145")
	private Integer fcMedia;
	@Schema(description = "Frecuencia cardíaca máxima", example = "175")
	private Integer fcMaxima;

	public UsuarioEstadisticasSemanalesDTO(Timestamp fecha, BigDecimal distancia, TipoActividadResultDTO tipoActividad,
			Integer fcMedia, Integer fcMaxima) {
		super();
		this.fecha = fecha;
		this.distancia = distancia;
		this.tipoActividad = tipoActividad;
		this.fcMedia = fcMedia;
		this.fcMaxima = fcMaxima;
	}

	public UsuarioEstadisticasSemanalesDTO() {
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

	public TipoActividadResultDTO getTipoActividad() {
		return tipoActividad;
	}

	public void setTipoActividad(TipoActividadResultDTO tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

}
