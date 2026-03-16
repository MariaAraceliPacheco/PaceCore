package com.example.demo.dto.zonas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para actualizar una zona de entrenamiento")
public class ZonasUpdateDTO {

	@NotNull
	@Schema(description = "ID de la zona", example = "1")
	private int id;

	@NotNull
	@Schema(description = "Número correlativo", example = "1")
	private int numero_zona;

	@NotBlank(message = "El nombre de la zona no puede estar vacio")
	@Size(min = 1, max = 64)
	@Schema(description = "Nuevo nombre", example = "Resistencia aeróbica")
	private String nombre_zona;

	@NotNull(message = "La FC minima no puede ser null")
	@Min(0)
	@Max(250)
	@Schema(description = "Nueva FC mínima", example = "130")
	private Integer fc_minima;

	@NotNull(message = "La FC maxima no puede ser null")
	@Min(0)
	@Max(250)
	@Schema(description = "Nueva FC máxima", example = "150")
	private Integer fc_maxima;

	@Size(min = 0, max = 255, message = "La descripcion no puede tener mas de 255 caracteres")
	@Schema(description = "Nueva descripción", example = "Zona de fondo")
	private String descripcion;

	public ZonasUpdateDTO(int id, int numero_zona, String nombre_zona, Integer fc_minima, Integer fc_maxima,
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

	public Integer getFc_minima() {
		return fc_minima;
	}

	public void setFc_minima(Integer fc_minima) {
		this.fc_minima = fc_minima;
	}

	public Integer getFc_maxima() {
		return fc_maxima;
	}

	public void setFc_maxima(Integer fc_maxima) {
		this.fc_maxima = fc_maxima;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
