package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.tipoActividad.ActividadConteoDTO;
import com.example.demo.dto.tipoActividad.TipoActividadDTO;
import com.example.demo.dto.tipoActividad.TipoActividadResultDTO;
import com.example.demo.entities.Tipoactividad;
import com.example.demo.repositories.EntrenoRepository;
import com.example.demo.repositories.TipoActividadRepository;

@Service
public class TipoActividadService {

	private final TipoActividadRepository repository;
	private final EntrenoRepository entrenoRepo;

	public TipoActividadService(TipoActividadRepository repository, EntrenoRepository entrenoRepo) {
		super();
		this.repository = repository;
		this.entrenoRepo = entrenoRepo;
	}

	// post
	// Se le pasa solamente el nombre porque la tabla solamente tiene dos columnas,
	// el id (auto_increment) y el nombre
	public Tipoactividad crear(TipoActividadDTO dto) {
		Tipoactividad u = new Tipoactividad();
		u.setNombre(dto.getNombre());
		return repository.save(u);
	}

	// get
	public List<Tipoactividad> obtenerTodosLosTiposActividad() {
		return repository.findAll();
	}

	// get
	public ResponseEntity<Tipoactividad> getTipoActividadPorId(int id) {
		return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// delete
	public void eliminarTipoActividad(int id) {
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el tipo de actividad");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalStateException("No se puede eliminar la actividad porque hay entrenos asociados a ella");
			// TODO: handle exception
		}
	}

	public Tipoactividad modificarTipoActividad(int id, TipoActividadDTO dto) {
		Tipoactividad ta = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("No se ha encontrado el tipo de actividad"));

		ta.setNombre(dto.getNombre());

		return repository.save(ta);
	}

	public List<ActividadConteoDTO> obtenerConteoTiposActividad(int idUsuario, LocalDateTime inicio,
			LocalDateTime fin) {
		List<ActividadConteoDTO> response = entrenoRepo.obtenerConteoActividades(idUsuario, inicio, fin);

		return response;
	}

	public List<TipoActividadResultDTO> obtenerTiposActividadesDelUsuario(int id) {
		List<Tipoactividad> tipos = repository.obtenerTipoActividadesDelDelUsuario(id);
		List<TipoActividadResultDTO> response = new ArrayList<TipoActividadResultDTO>();

		for (Tipoactividad t : tipos) {
			TipoActividadResultDTO ta = new TipoActividadResultDTO();
			ta.setId(t.getId());
			ta.setNombre(t.getNombre());

			response.add(ta);
		}

		return response;
	}

}
