package com.example.demo.dto.auth;

import com.example.demo.dto.usuarios.UsuarioResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta tras un inicio de sesión exitoso")
public class LoginResponse {

	@Schema(description = "Token JWT para autenticación")
	private String token;
	@Schema(description = "Información del usuario autenticado")
	private UsuarioResponseDTO usuario;

	public LoginResponse(String token, UsuarioResponseDTO usuario) {
		super();
		this.token = token;
		this.usuario = usuario;
	}

	public LoginResponse() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UsuarioResponseDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioResponseDTO usuario) {
		this.usuario = usuario;
	}

}
