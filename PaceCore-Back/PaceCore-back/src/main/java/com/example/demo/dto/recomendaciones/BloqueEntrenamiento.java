package com.example.demo.dto.recomendaciones;

/*
 *Esta clase es un POJO (Plain Old Java Object), servirá para representar 
 *cada paso dentro del json que se guardará en la tabla EntrenamientoSugerido de la bd
 */
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa un paso o bloque individual dentro de una sugerencia de entrenamiento")
public class BloqueEntrenamiento {
	@Schema(description = "Fase del entrenamiento", example = "Calentamiento")
	private String fase;
	@Schema(description = "Duración de la fase en segundos", example = "600")
	private int duracionSegundos;
	@Schema(description = "Zona de intensidad objetivo", example = "2")
	private int zonaObjetivo;
	@Schema(description = "Descripción de la tarea a realizar", example = "Trote suave para empezar")
	private String descripcion;
	@Schema(description = "Ritmo objetivo sugerido", example = "6:00 min/km")
	private String ritmoObjetivo;

	public BloqueEntrenamiento(String fase, int duracionSegundos, int zonaObjetivo, String descripcion,
			String ritmoObjetivo) {
		super();
		this.fase = fase;
		this.duracionSegundos = duracionSegundos;
		this.zonaObjetivo = zonaObjetivo;
		this.descripcion = descripcion;
		this.ritmoObjetivo = ritmoObjetivo;
	}

	public BloqueEntrenamiento() {
		super();
	}

	public String getFase() {
		return fase;
	}

	public void setFase(String fase) {
		this.fase = fase;
	}

	public int getDuracionSegundos() {
		return duracionSegundos;
	}

	public void setDuracionSegundos(int duracionSegundos) {
		this.duracionSegundos = duracionSegundos;
	}

	public int getZonaObjetivo() {
		return zonaObjetivo;
	}

	public void setZonaObjetivo(int zonaObjetivo) {
		this.zonaObjetivo = zonaObjetivo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getRitmoObjetivo() {
		return ritmoObjetivo;
	}

	public void setRitmoObjetivo(String ritmoObjetivo) {
		this.ritmoObjetivo = ritmoObjetivo;
	}

}
