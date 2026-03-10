package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.intervalos.IntervaloUpdateDTO;
import com.example.demo.entities.Intervalo;
import com.example.demo.repositories.EntrenoRepository;
import com.example.demo.repositories.TipoActividadRepository;
import com.example.demo.services.IntervaloService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/intervalo")
@Tag(name = "Intervalos", description = "Gestión de los segmentos de un entrenamiento")
public class IntervaloController {

	private final IntervaloService service;

	// Seria el servicio el que deberia tener acceso a los repositorios solamente
	@Autowired
	private EntrenoRepository entrenoRepo;
	@Autowired
	private TipoActividadRepository tipoactividadRepo;

	public IntervaloController(IntervaloService service) {
		this.service = service;
	}

	// Realmente un intervalo no se podria crear solo, ya que el usuario a la hora
	// de crearlo, siempre va a crear un entreno primeramente

	// Toda esta logica que he hecho aqui, lo ideal seria que la tuviera el servicio
	/*
	 * @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) public Intervalo
	 * crear(@RequestBody IntervaloInsertDTO dto) { Entreno e =
	 * entrenoRepo.findById(dto.getEntrenoId()) .orElseThrow(() -> new
	 * RuntimeException("Entreno no encontrado")); Tipoactividad t =
	 * tipoactividadRepo.findById(dto.getTipoActividadId()) .orElseThrow(() -> new
	 * RuntimeException("Tipo de actividad no encontrado"));
	 * 
	 * Intervalo inter = new Intervalo(); inter.setDistancia(dto.getDistancia());
	 * inter.setDuracion(dto.getDuracion()); inter.setEntreno(e);
	 * inter.setTipoactividad(t);
	 * 
	 * return service.crear(inter); }
	 */

	@Operation(summary = "Listar intervalos", description = "Obtiene todos los intervalos registrados")
	@ApiResponse(responseCode = "200", description = "Lista recuperada")
	@GetMapping
	public List<Intervalo> obtenerIntervalos() {
		return service.obtenerTodosLosIntervalos();
	}

	@Operation(summary = "Obtener intervalo", description = "Busca un intervalo por su ID")
	@ApiResponse(responseCode = "200", description = "Intervalo encontrado")
	@GetMapping("/{id}")
	public ResponseEntity<Intervalo> obtenerIntervaloPorId(@PathVariable int id) {
		return service.getIntervaloPorId(id);
	}

	@Operation(summary = "Eliminar intervalo", description = "Borra un intervalo específico")
	@ApiResponse(responseCode = "200", description = "Intervalo eliminado")
	@DeleteMapping("/{id}")
	public void eliminarIntervalo(@PathVariable int id) {
		service.eliminarIntervalos(id);
	}

	@Operation(summary = "Modificar intervalo", description = "Actualiza los datos (distancia, duración) de un intervalo")
	@ApiResponse(responseCode = "200", description = "Intervalo actualizado")
	@ApiResponse(responseCode = "404", description = "Intervalo no encontrado")
	@PutMapping("/{id}")
	public ResponseEntity<Intervalo> modificarIntervalo(@RequestBody IntervaloUpdateDTO dto, @PathVariable int id) {
		try {
			Intervalo ta = service.modificarIntervalo(dto, id);
			return ResponseEntity.ok(ta);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}

	}
}
