package com.example.demo.services;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.tipoActividad.TipoActividadResultDTO;
import com.example.demo.dto.usuarios.UsuarioEstadisticasDTO;
import com.example.demo.dto.usuarios.UsuarioEstadisticasSemanalesDTO;
import com.example.demo.dto.usuarios.UsuarioInsertDTO;
import com.example.demo.dto.usuarios.UsuarioUpdateDTO;
import com.example.demo.entities.Entreno;
import com.example.demo.entities.Intervalo;
import com.example.demo.entities.Tipoactividad;
import com.example.demo.entities.Usuario;
import com.example.demo.repositories.EntrenoRepository;
import com.example.demo.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository repoUser;
	private final EntrenoRepository repository;
	private final ZonaService zonaService;

	public UsuarioService(UsuarioRepository repoUser, EntrenoRepository repository, ZonaService zonaService) {
		this.repoUser = repoUser;
		this.repository = repository;
		this.zonaService = zonaService;
	}

	// post
	@Transactional
	public Usuario crear(UsuarioInsertDTO u) {
		Usuario us = new Usuario();
		us.setNombre(u.getNombre());
		us.setEmail(u.getEmail());
		us.setPassword(u.getPassword());
		us.setFechaCreacion(u.getFecha_creacion());
		if (u.getDescripcion() != null) {
			us.setDescripcion(u.getDescripcion());
		}
		us.setAltura(u.getAltura());
		us.setPeso(u.getPeso());
		us.setEdad(u.getEdad());

		Usuario nuevo = repoUser.save(us);
		zonaService.crearZonaUsuario(nuevo.getId());

		return nuevo;
	}

	// get
	public List<Usuario> obtenerTodosLosUsuarios() {
		return repoUser.findAll();
	}

	// get
	// El servicio no deberia devlver ResponseEntity, solo el controller
	public ResponseEntity<Usuario> getUsuarioPorId(int id) {
		// si no existe el usuario, se devuelve un error 404 para que el cliente sepa
		// que el codigo de la api está bien, pero que no existe el usuario especificado
		return repoUser.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// delete
	public void eliminarUsuario(int id) {
		if (!repoUser.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
		}

		repoUser.deleteById(id);
	}

	public Usuario modificarUsuario(UsuarioUpdateDTO u, int id) {
		Usuario user = repoUser.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		user.setNombre(u.getNombre());
		user.setEmail(u.getEmail());
		user.setAltura(u.getAltura());
		if (u.getDescripcion() != null) {
			user.setDescripcion(u.getDescripcion());
		}
		user.setEdad(u.getEdad());
		user.setPeso(u.getPeso());
		return repoUser.save(user);
	}

	public UsuarioEstadisticasDTO obtenerEstadisticasUsuario(int id) {
		UsuarioEstadisticasDTO stats = new UsuarioEstadisticasDTO();
		List<Entreno> entrenos = repository.findAllWithIntervalosByUsuario(id);

		double tiempoRunningSeg = 0;
		double distanciaRunningKm = 0;

		double tiempoBiciSeg = 0;
		double distanciaBiciKm = 0;

		double totalCalorias = 0;
		long tiempoTotalGlobal = 0;

		for (Entreno e : entrenos) {
			totalCalorias += calcularCaloriasEntreno(e);
			tiempoTotalGlobal += e.getTiempoTotal();

			String tipo = e.getTipoactividad().getNombre().toLowerCase();
			double distanciaEntreno = (e.getDistancia() != null) ? e.getDistancia().doubleValue() : 0;
			long tiempoEntreno = e.getTiempoTotal();

			// si es de tipo running y no tiene intervalos, se cogen los datos del
			// entrenamiento
			if (esRunning(tipo) && e.getIntervalos() == null && e.getIntervalos().isEmpty()) {
				tiempoRunningSeg += tiempoEntreno;
				distanciaRunningKm += distanciaEntreno;
			}

			// si es de tipo running y tiene intervalos, se cogen solamente los datos de los
			// intervalos
			if (esRunning(tipo) && !e.getIntervalos().isEmpty()) {
				for (Intervalo i : e.getIntervalos()) {
					tiempoRunningSeg += i.getDuracion();
					distanciaRunningKm += i.getDistancia().doubleValue();
				}
			}

			if (esCiclismo(tipo)) {
				tiempoBiciSeg += tiempoEntreno;
				distanciaBiciKm += distanciaEntreno;
			}
		}
		// Ritmo Medio Running: (Minutos totales / Km totales)
		if (distanciaRunningKm > 0) {
			double ritmoMedio = (tiempoRunningSeg / 60.0) / distanciaRunningKm;
			stats.setRitmoMedioRunning(ritmoMedio);
		} else {
			stats.setRitmoMedioRunning(0);
		}

		// Velocidad Media Bici: (Km totales / Horas totales)
		if (tiempoBiciSeg > 0) {
			double horasBici = tiempoBiciSeg / 3600.0;
			stats.setVelocidadMediaBici(distanciaBiciKm / horasBici);
		} else {
			stats.setVelocidadMediaBici(0);
		}

		stats.setDistanciaTotalKm(repository.getKmTotalesByUsuario(id));
		stats.setDesnivelTotal(repository.getDesnivelTotalByUsuario(id));
		stats.setCaloriasTotales(totalCalorias);
		stats.setTiempoTotal(tiempoTotalGlobal);

		double ritmo = stats.getRitmoMedioRunning();
		stats.setMarca5k(calcularTiempoEstimadoConRitmo(5, ritmo));
		stats.setMarca10k(calcularTiempoEstimadoConRitmo(10, ritmo));
		stats.setMarca21k(calcularTiempoEstimadoConRitmo(21, ritmo));
		stats.setMarca42k(calcularTiempoEstimadoConRitmo(42, ritmo));

		return stats;
	}

	private String formatearRitmoMedia(double ritmoDecimal) {
		if (ritmoDecimal <= 0)
			return "0:00";

		int minutos = (int) ritmoDecimal; // Te quedas con el 7
		double parteDecimal = ritmoDecimal - minutos; // Te quedas con el 0.67
		int segundos = (int) Math.round(parteDecimal * 60); // 0.67 * 60 = 40

		// Si los segundos redondean a 60, sumamos un minuto
		if (segundos == 60) {
			minutos++;
			segundos = 0;
		}

		return String.format("%d:%02d", minutos, segundos);
	}

	// --------------------- Métodos auxiliares ---------------------

	// 3. Calorias (Usando segundos directamente)
	private double calcularCaloriasEntreno(Entreno e) {
		double peso = e.getUsuario().getPeso().doubleValue();
		double duracionHoras = e.getTiempoTotal() / 3600.0;
		return getMetPorActividad(e.getTipoactividad()) * peso * duracionHoras;
	}

	// 4. Marcas estimadas (Convertimos el ritmo a LocalTime para el DTO)
	private LocalTime calcularTiempoEstimadoConRitmo(double distanciaKm, double ritmoMinKm) {
		if (ritmoMinKm <= 0)
			return LocalTime.MIDNIGHT;

		long totalSegundos = (long) (distanciaKm * ritmoMinKm * 60);

		return LocalTime.ofSecondOfDay(totalSegundos % 86400);
	}

	private boolean esRunning(String tipo) {
		return tipo.toLowerCase().equals("correr") || tipo.toLowerCase().equals("trail");
	}

	private boolean esCiclismo(String tipo) {
		return tipo.toLowerCase().contains("bici") || tipo.toLowerCase().contains("bike");
	}

	private double getMetPorActividad(Tipoactividad tipo) {
		String nombre = tipo.getNombre().toLowerCase();
		switch (nombre) {
		case "correr":
			return 10;
		case "caminar":
			return 3.5;
		case "trail":
			return 11;
		case "bici":
			return 8;
		case "bici de carretera":
			return 10;
		case "mountain bike":
			return 11;
		default:
			return 1;
		}
	}

	/**
	 * Este metodo lo que hace es devolver una lista de objetos de tipo
	 * UsuarioEstadisticasSemanalesDTO que contiene los datos necesarios para luego
	 * en el front, mostrar los km totales de cada semana y de cada dia
	 * 
	 * Este dto lo que haria realmente es devolver los entrenos del usuario con
	 * menos datos, los necesarios para hacer los calculos de los km en el front
	 * 
	 * @param idUsuario
	 * @param tipoActividad
	 * @return
	 */
	public List<UsuarioEstadisticasSemanalesDTO> obtenerEstadisticasSemanalesUsuarioFiltradasPorTipoActividad(
			int idUsuario, Integer tipoActividad) {

		List<UsuarioEstadisticasSemanalesDTO> resultado = new ArrayList<>();
		List<Entreno> entrenos = repository.findAllWithIntervalosByUsuario(idUsuario);

		// Acumuladores globales

		for (Entreno entreno : entrenos) {
			BigDecimal distanciaTotal = BigDecimal.ZERO;
			Tipoactividad tipoResultado = null;

			// Distancia del entreno principal
			if (tipoActividad == null || Objects.equals(entreno.getTipoactividad().getId(), tipoActividad)) {
				if (entreno.getDistancia() != null) {
					distanciaTotal = distanciaTotal.add(entreno.getDistancia());
					tipoResultado = entreno.getTipoactividad();
				}
			}

			// Intervalos
			if (entreno.getIntervalos() != null) {
				for (Intervalo in : entreno.getIntervalos()) {
					if (tipoActividad == null || Objects.equals(in.getTipoactividad().getId(), tipoActividad)) {

						if (in.getDistancia() != null) {
							distanciaTotal = distanciaTotal.add(in.getDistancia());
							tipoResultado = in.getTipoactividad();
						}
					}
				}
			}

			// Si no hay datos útiles, saltamos
			if (distanciaTotal.compareTo(BigDecimal.ZERO) == 0 || tipoResultado == null) {
				continue;
			}

			UsuarioEstadisticasSemanalesDTO dto = new UsuarioEstadisticasSemanalesDTO();
			dto.setFecha(entreno.getFecha());
			dto.setDistancia(distanciaTotal);
			dto.setFcMedia(entreno.getFcMedia());
			dto.setFcMaxima(entreno.getFcMaxima());

			TipoActividadResultDTO ta = new TipoActividadResultDTO();
			ta.setId(tipoResultado.getId());
			ta.setNombre(tipoResultado.getNombre());
			dto.setTipoActividad(ta);

			resultado.add(dto);
		}

		return resultado;
	}

}
