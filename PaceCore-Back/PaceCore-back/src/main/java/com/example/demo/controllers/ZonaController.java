package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.zonas.PorcentajesZonasDTO;
import com.example.demo.dto.zonas.ZonasResponseDTO;
import com.example.demo.dto.zonas.ZonasUpdateDTO;
import com.example.demo.entities.ZonasUsuario;
import com.example.demo.services.ZonaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/zonas")
@Tag(name = "Zonas de entrenamiento", description = "Gestion de los rangos de frecuencia cardiaca para los usuarios")
public class ZonaController {

	private final ZonaService service;

	public ZonaController(ZonaService service) {
		super();
		this.service = service;
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener las zonas de un usuario", description = "Devuelve la lista de las 5 zonas de frecuencia cardiaca del usuario pasando como parametro el id del usuario")
	@ApiResponse(description = "Devuelve las zonas del usuario que se le pasa como parametro")
	public ResponseEntity<List<ZonasResponseDTO>> obtenerZonasUsuario(@PathVariable int id) {
		List<ZonasUsuario> zonas = service.obtenerZonasPorUsuario(id);
		List<ZonasResponseDTO> response = new ArrayList<ZonasResponseDTO>();

		for (ZonasUsuario z : zonas) {
			ZonasResponseDTO a = new ZonasResponseDTO();
			a.setDescripcion(z.getDescripcion());
			a.setFc_maxima(z.getFcMaxima());
			a.setFc_minima(z.getFcMinima());
			a.setId(z.getId());
			a.setNombre_zona(z.getNombreZona());
			a.setNumero_zona(z.getNumeroZona());
			a.setUsuario_id(z.getUsuario().getId());

			response.add(a);
		}

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Modificar las zonas de un usuario", description = "Actualiza los rangos de FC para las 5 zonas de entrenamiento")
	@ApiResponse(responseCode = "200", description = "Zonas actualizadas")
	@PutMapping("/{id}")
	public void modificarZona(@PathVariable int id, @RequestBody List<ZonasUpdateDTO> dtos) {
		service.actualizarZonasUsuario(id, dtos);
	}

	@Operation(summary = "Generar zonas genéricas", description = "Crea automáticamente las 5 zonas de FC basadas en fórmulas estándar para el usuario")
	@ApiResponse(responseCode = "200", description = "Zonas generadas")
	@PostMapping("/{id}")
	public void generarZonasGenericasUsuario(@PathVariable int idUsuario) {
		service.crearZonaUsuario(idUsuario);
	}

	@Operation(summary = "Obtener los porcentajes de cada zona", description = "Calcula el porcentaje de tiempo que el usuario ha pasado en cada zona de FC")
	@ApiResponse(responseCode = "200", description = "Porcentajes calculados")
	@GetMapping("/porcentajesZonas/{id}")
	public ResponseEntity<?> obtenerPorcentajesZonasDelUsuario(@PathVariable int id,
			@RequestParam(required = false) Integer idTipoActividad,
			@RequestParam(required = false) LocalDateTime inicio, @RequestParam(required = false) LocalDateTime fin) {
		PorcentajesZonasDTO response = service.obtenerPorcentajesZonasPorUsuario(id, idTipoActividad, inicio, fin);

		return ResponseEntity.ok(response);
	}

}
