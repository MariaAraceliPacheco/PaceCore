package com.example.demo.dto.usuarios;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información detallada del perfil de un usuario")
public class UsuarioResponseDTO {
	@Schema(description = "Identificador único del usuario", example = "1")
	private int id;
	@Schema(description = "Nombre completo", example = "Juan Pérez")
	private String nombre;
	@Schema(description = "Correo electrónico", example = "juan.perez@example.com")
	private String email;
	@Schema(description = "Biografía o descripción", example = "Corredor aficionado")
	private String descripcion;
	@Schema(description = "Peso en kg", example = "75.5")
	private BigDecimal peso;
	@Schema(description = "Altura en metros", example = "1.80")
	private BigDecimal altura;
	@Schema(description = "Rol asignado en el sistema", example = "usuario")
	private String rol;
	@Schema(description = "Edad", example = "30")
	private Integer edad;

	public UsuarioResponseDTO(int id, String nombre, String email, String descripcion, BigDecimal peso,
			BigDecimal altura, String rol, Integer edad) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.descripcion = descripcion;
		this.peso = peso;
		this.altura = altura;
		this.rol = rol;
		this.edad = edad;
	}

	public UsuarioResponseDTO() {
		super();
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

}
