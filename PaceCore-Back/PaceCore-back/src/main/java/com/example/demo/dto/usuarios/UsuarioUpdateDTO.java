package com.example.demo.dto.usuarios;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos permitidos para actualizar el perfil de un usuario")
public class UsuarioUpdateDTO {

	@Schema(description = "Nombre completo actualizado", example = "Juan Pérez")
	private String nombre;
	@Schema(description = "Correo electrónico actualizado", example = "juan.perez@example.com")
	private String email;
	@Schema(description = "Nueva biografía o descripción", example = "Entusiasta del trail running")
	private String descripcion;
	@Schema(description = "Peso actual en kg", example = "73.2")
	private BigDecimal peso;
	@Schema(description = "Altura actual en metros", example = "1.80")
	private BigDecimal altura;
	@Schema(description = "Edad actualizada", example = "31")
	private Integer edad;

	public UsuarioUpdateDTO(String nombre, String email, String descripcion, BigDecimal peso, BigDecimal altura,
			Integer edad) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.descripcion = descripcion;
		this.peso = peso;
		this.altura = altura;
		this.edad = edad;
	}

	public UsuarioUpdateDTO() {
		super();
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public BigDecimal getAltura() {
		return altura;
	}

	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
