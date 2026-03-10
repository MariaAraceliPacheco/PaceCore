package com.example.demo.dto.zonas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Porcentajes de tiempo o esfuerzo en cada zona de entrenamiento")
public class PorcentajesZonasDTO {

	@Schema(description = "Porcentaje en Zona 1", example = "20.0")
	private double zona1;
	@Schema(description = "Porcentaje en Zona 2", example = "50.0")
	private double zona2;
	@Schema(description = "Porcentaje en Zona 3", example = "10.0")
	private double zona3;
	@Schema(description = "Porcentaje en Zona 4", example = "15.0")
	private double zona4;
	@Schema(description = "Porcentaje en Zona 5", example = "5.0")
	private double zona5;

	public PorcentajesZonasDTO(double zona1, double zona2, double zona3, double zona4, double zona5) {
		super();
		this.zona1 = zona1;
		this.zona2 = zona2;
		this.zona3 = zona3;
		this.zona4 = zona4;
		this.zona5 = zona5;
	}

	public PorcentajesZonasDTO() {
		super();
	}

	public double getZona1() {
		return zona1;
	}

	public void setZona1(double zona1) {
		this.zona1 = zona1;
	}

	public double getZona2() {
		return zona2;
	}

	public void setZona2(double zona2) {
		this.zona2 = zona2;
	}

	public double getZona3() {
		return zona3;
	}

	public void setZona3(double zona3) {
		this.zona3 = zona3;
	}

	public double getZona4() {
		return zona4;
	}

	public void setZona4(double zona4) {
		this.zona4 = zona4;
	}

	public double getZona5() {
		return zona5;
	}

	public void setZona5(double zona5) {
		this.zona5 = zona5;
	}

}
