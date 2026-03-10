package com.example.demo.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto para las credenciales de inicio de sesión")
public class LoginDTO {

	@Schema(description = "Correo electrónico del usuario", example = "usuario@example.com")
	private String email;
	@Schema(description = "Contraseña del usuario", example = "password123")
	private String password;

	public LoginDTO(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public LoginDTO() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pass) {
		this.password = pass;
	}

}
