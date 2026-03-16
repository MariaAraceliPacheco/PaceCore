package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.errores.ErrorCampoDTO;
import com.example.demo.dto.errores.ErrorRespuestaDTO;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// este codigo captura cualquier excepcion de validacion de DTO
	// extrae cada error de campo (FieldError)
	// lo convierte en nuestro formato personalizado (ErrorRespuestaDTO)
	// Devuelve un JSOn uniforme y limpio

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorRespuestaDTO> manejarValidaciones(MethodArgumentNotValidException ex) {

		List<ErrorCampoDTO> errores = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> new ErrorCampoDTO(error.getField(), error.getDefaultMessage())).toList();

		ErrorRespuestaDTO respuesta = new ErrorRespuestaDTO(errores, HttpStatus.BAD_REQUEST.value());

		return ResponseEntity.badRequest().body(respuesta);
	}

	// intercepta los errores de este tipo
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {

		List<ErrorCampoDTO> errores = new ArrayList<ErrorCampoDTO>();
		ex.getConstraintViolations().forEach(violation -> {
			String field = violation.getPropertyPath().toString();
			String message = violation.getMessage();

			ErrorCampoDTO e = new ErrorCampoDTO();
			e.setCampo(field);
			e.setMensaje(message);

			errores.add(e);
		});

		ErrorRespuestaDTO respuesta = new ErrorRespuestaDTO(errores, HttpStatus.BAD_REQUEST.value());

		return ResponseEntity.badRequest().body(respuesta);
	}

}
