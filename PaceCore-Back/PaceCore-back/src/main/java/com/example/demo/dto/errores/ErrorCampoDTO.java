package com.example.demo.dto.errores;

public class ErrorCampoDTO {
	private String campo;
	private String mensaje;

	public ErrorCampoDTO(String campo, String mensaje) {
		super();
		this.campo = campo;
		this.mensaje = mensaje;
	}

	public ErrorCampoDTO() {
		super();
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
