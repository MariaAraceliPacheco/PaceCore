package com.example.demo.dto.entrenos;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.example.demo.dto.intervalos.IntervaloInsertDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para actualizar un entrenamiento existente")
public class EntrenoUpdateDTO {

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@NotNull(message = "La fecha no puede ser null")
	private LocalDateTime fecha;

	@NotNull(message = "La distancia no puede ser null")
	@DecimalMin("0")
	@DecimalMax("99999")
	@Schema(description = "Nueva distancia", example = "10.0")
	private BigDecimal distancia;

	@NotNull(message = "El tiempo total no puede ser null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	@Schema(type = "string", example = "00:00:00")
	private LocalTime tiempo_total;

	@NotNull
	@Schema(description = "ID del tipo de actividad", example = "1")
	private int tipo_actividad_id;

	@Size(min = 0, max = 255, message = "La descripcion debe tener entre 0 y 255 caracteres")
	@Schema(description = "Nueva descripción", example = "Salida matinal")
	private String descripcion;

	@NotBlank(message = "El titulo no puede estar vacio")
	@Size(min = 1, max = 124)
	@Schema(description = "Nuevo título", example = "Rodaje regenerativo")
	private String titulo;

	@DecimalMax("99999")
	@Schema(description = "Nuevo desnivel", example = "100.0")
	private BigDecimal desnivel;

	@Min(0)
	@Max(250)
	@Schema(description = "Frecuencia cardíaca media", example = "140")
	private Integer fcMedia;

	@Min(0)
	@Max(250)
	@Schema(description = "Frecuencia cardíaca máxima", example = "165")
	private Integer fcMaxima;

	// esto se calcula automaticamente en el servicio
	@Schema(description = "Zona de entrenamiento máxima alcanzada", example = "3")
	private Integer zonaAlcanzada;

	private List<IntervaloInsertDTO> intervalos;

	public EntrenoUpdateDTO(LocalDateTime fecha, BigDecimal distancia, LocalTime tiempo_total, int tipo_actividad_id,
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

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
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
