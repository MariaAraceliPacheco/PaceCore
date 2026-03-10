package com.example.demo.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.intervalos.IntervaloUpdateDTO;
import com.example.demo.entities.Intervalo;
import com.example.demo.entities.Tipoactividad;
import com.example.demo.entities.ZonasUsuario;
import com.example.demo.repositories.EntrenoRepository;
import com.example.demo.repositories.IntervaloRepository;
import com.example.demo.repositories.TipoActividadRepository;
import com.example.demo.repositories.ZonasUsuarioRepository;

@Service
public class IntervaloService {

	private final IntervaloRepository repository;
	private final TipoActividadRepository tipoActividadRepo;
	private final EntrenoRepository entrenoRepo;
	private final ZonaService zonaServ;
	private final ZonasUsuarioRepository zonaRepo;
	
	public IntervaloService(IntervaloRepository repository, TipoActividadRepository tipoActividadRepo,
			EntrenoRepository entrenoRepo, ZonaService zonaServ, ZonasUsuarioRepository zonaRepo) {
		super();
		this.repository = repository;
		this.tipoActividadRepo = tipoActividadRepo;
		this.entrenoRepo = entrenoRepo;
		this.zonaServ = zonaServ;
		this.zonaRepo = zonaRepo;
	}

	public Intervalo crear(Intervalo u) {
		if (!entrenoRepo.existsById(u.getEntreno().getId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el entreno");
		}
		List<ZonasUsuario> zonasDelUsuario = zonaRepo.findByUsuarioIdOrderByNumeroZonaAsc(u.getEntreno().getUsuario().getId());
		// como las zonas se van a calcular siempre en la api, nunca las va a poner el
		// usuario, se pone aqui manualmente
		u.setZonaAlcanzada(zonaServ.determinarZona(zonasDelUsuario, u.getFcMedia()));

		return repository.save(u);
	}

	// get
	public List<Intervalo> obtenerTodosLosIntervalos() {
		return repository.findAll();
	}

	// get
	public ResponseEntity<Intervalo> getIntervaloPorId(int id) {
		return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// delete
	public void eliminarIntervalos(int id) {
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el intervalo");
		}

		repository.deleteById(id);
	}

	public Intervalo modificarIntervalo(IntervaloUpdateDTO dto, int id) {
		Intervalo i = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("No se ha encontrado el intervalo"));
		Tipoactividad ta = tipoActividadRepo.findById(dto.getTipo_actividad_id())
				.orElseThrow(() -> new RuntimeException("No se ha encontrado el tipo de actividad"));

		i.setTipoactividad(ta);
		i.setDuracion((long) dto.getDuracion().toSecondOfDay());
		i.setDistancia(dto.getDistancia());

		return repository.save(i);
	}

}
