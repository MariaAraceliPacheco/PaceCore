package com.example.demo.dto.entrenos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

import com.example.demo.dto.intervalos.IntervaloResponseDTO;
import com.example.demo.entities.Tipoactividad;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información resumida de un entrenamiento para listados de usuario")
public class EntrenosUsuarioDTO {

	@Schema(description = "ID del entrenamiento", example = "50")
	private int id;
	private Timestamp fecha;
	@Schema(description = "Distancia total", example = "42.195")
	private BigDecimal distancia_entreno;
	@Schema(description = "Tiempo total", example = "03:30:00")
	private LocalTime tiempo_total_entreno;
	@Schema(description = "Detalle del tipo de actividad")
	private Tipoactividad tipo_actividad_entreno;
	@Schema(description = "Desnivel acumulado", example = "400.0")
	private BigDecimal desnivel_entreno;
	@Schema(description = "Título", example = "Maratón Valencia")
	private String titulo;
	@Schema(description = "Nota o comentario", example = "Muy buenas sensaciones")
	private String descripcion;
	@Schema(description = "FC Media", example = "162")
	private Integer fcMedia;
	@Schema(description = "FC Máxima", example = "180")
	private Integer fcMaxima;
	@Schema(description = "Zona predominante", example = "4")
	private Integer zona_alcanzada;
	@Schema(description = "Ritmo medio calculado (min/km o km/h)", example = "4.58")
	private double ritmoMedio;

	private List<IntervaloResponseDTO> intervalos;

	public EntrenosUsuarioDTO(int id, Timestamp fecha, BigDecimal distancia_entreno, LocalTime tiempo_total_entreno,
			Tipoactividad tipo_actividad_entreno, BigDecimal desnivel_entreno, String titulo, String descripcion,
			Integer fcMedia, Integer fcMaxima, Integer zona_alcanzada, List<IntervaloResponseDTO> intervalos,
			double ritmoMedio) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.distancia_entreno = distancia_entreno;
		this.tiempo_total_entreno = tiempo_total_entreno;
		this.tipo_actividad_entreno = tipo_actividad_entreno;
		this.desnivel_entreno = desnivel_entreno;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.fcMedia = fcMedia;
		this.fcMaxima = fcMaxima;
		this.intervalos = intervalos;
		this.zona_alcanzada = zona_alcanzada;
		this.ritmoMedio = ritmoMedio;
	}

	public EntrenosUsuarioDTO() {
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

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getDistancia_entreno() {
		return distancia_entreno;
	}

	public void setDistancia_entreno(BigDecimal distancia_entreno) {
		this.distancia_entreno = distancia_entreno;
	}

	public LocalTime getTiempo_total_entreno() {
		return tiempo_total_entreno;
	}

	public void setTiempo_total_entreno(LocalTime tiempo_total_entreno) {
		this.tiempo_total_entreno = tiempo_total_entreno;
	}

	public Tipoactividad getTipo_actividad_entreno() {
		return tipo_actividad_entreno;
	}

	public void setTipo_actividad_entreno(Tipoactividad tipo_actividad_entreno) {
		this.tipo_actividad_entreno = tipo_actividad_entreno;
	}

	public BigDecimal getDesnivel_entreno() {
		return desnivel_entreno;
	}

	public void setDesnivel_entreno(BigDecimal desnivel_entreno) {
		this.desnivel_entreno = desnivel_entreno;
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

	public List<IntervaloResponseDTO> getIntervalos() {
		return intervalos;
	}

	public void setIntervalos(List<IntervaloResponseDTO> intervalos) {
		this.intervalos = intervalos;
	}
}
