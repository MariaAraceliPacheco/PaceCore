package com.example.demo.dto.usuarios;

import java.math.BigDecimal;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estadísticas generales de rendimiento de un usuario")
public class UsuarioEstadisticasDTO {

	@Schema(description = "Distancia total recorrida en kilómetros", example = "150.5")
	private BigDecimal distanciaTotalKm;
	@Schema(description = "Desnivel acumulado positivo en metros", example = "1200.0")
	private BigDecimal desnivelTotal;
	@Schema(description = "Total de calorías quemadas", example = "5000.0")
	private double caloriasTotales;
	@Schema(description = "Ritmo medio en actividades de carrera (min/km)", example = "5.30")
	private double ritmoMedioRunning;
	@Schema(description = "Velocidad media en actividades de ciclismo (km/h)", example = "25.0")
	private double velocidadMediaBici;
	@Schema(description = "Tiempo total de actividad en segundos", example = "36000")
	private long tiempoTotal;

	private LocalTime marca5k;
	private LocalTime marca10k;
	private LocalTime marca21k;
	private LocalTime marca42k;

	public UsuarioEstadisticasDTO(BigDecimal distanciaTotalKm, BigDecimal desnivelTotal, double caloriasTotales,
			double ritmoMedioRunning, double velocidadMediaBici, LocalTime marca5k, LocalTime marca10k,
			LocalTime marca21k, LocalTime marca42k, long tiempoTotal) {
		super();
		this.distanciaTotalKm = distanciaTotalKm;
		this.desnivelTotal = desnivelTotal;
		this.caloriasTotales = caloriasTotales;
		this.ritmoMedioRunning = ritmoMedioRunning;
		this.velocidadMediaBici = velocidadMediaBici;
		this.marca5k = marca5k;
		this.marca10k = marca10k;
		this.marca21k = marca21k;
		this.marca42k = marca42k;
		this.tiempoTotal = tiempoTotal;
	}

	public UsuarioEstadisticasDTO() {
		super();
	}

	public long getTiempoTotal() {
		return tiempoTotal;
	}

	public void setTiempoTotal(long tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}

	public double getRitmoMedioRunning() {
		return ritmoMedioRunning;
	}

	public void setRitmoMedioRunning(double ritmoMedioRunning) {
		this.ritmoMedioRunning = ritmoMedioRunning;
	}

	public double getVelocidadMediaBici() {
		return velocidadMediaBici;
	}

	public void setVelocidadMediaBici(double velocidadMediaBici) {
		this.velocidadMediaBici = velocidadMediaBici;
	}

	public BigDecimal getDistanciaTotalKm() {
		return distanciaTotalKm;
	}

	public void setDistanciaTotalKm(BigDecimal distanciaTotalKm) {
		this.distanciaTotalKm = distanciaTotalKm;
	}

	public BigDecimal getDesnivelTotal() {
		return desnivelTotal;
	}

	public void setDesnivelTotal(BigDecimal desnivelTotal) {
		this.desnivelTotal = desnivelTotal;
	}

	public double getCaloriasTotales() {
		return caloriasTotales;
	}

	public void setCaloriasTotales(double caloriasTotales) {
		this.caloriasTotales = caloriasTotales;
	}

	public LocalTime getMarca5k() {
		return marca5k;
	}

	public void setMarca5k(LocalTime marca5k) {
		this.marca5k = marca5k;
	}

	public LocalTime getMarca10k() {
		return marca10k;
	}

	public void setMarca10k(LocalTime marca10k) {
		this.marca10k = marca10k;
	}

	public LocalTime getMarca21k() {
		return marca21k;
	}

	public void setMarca21k(LocalTime marca21k) {
		this.marca21k = marca21k;
	}

	public LocalTime getMarca42k() {
		return marca42k;
	}

	public void setMarca42k(LocalTime marca42k) {
		this.marca42k = marca42k;
	}

}
