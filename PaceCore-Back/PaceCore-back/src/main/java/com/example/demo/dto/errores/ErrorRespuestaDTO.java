package com.example.demo.dto.errores;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ErrorRespuestaDTO {

	private List<ErrorCampoDTO> errores;
	private int status;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime timestamp = LocalDateTime.now();

	public ErrorRespuestaDTO(List<ErrorCampoDTO> errores, int status) {
		super();
		this.errores = errores;
		this.status = status;
	}

	public ErrorRespuestaDTO() {
		super();
	}

	public List<ErrorCampoDTO> getErrores() {
		return errores;
	}

	public void setErrores(List<ErrorCampoDTO> errores) {
		this.errores = errores;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
