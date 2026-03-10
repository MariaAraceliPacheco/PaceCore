package com.example.demo.services;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.entrenos.EntrenoCreateDTO;
import com.example.demo.dto.entrenos.EntrenoUpdateDTO;
import com.example.demo.dto.entrenos.EntrenosUsuarioDTO;
import com.example.demo.dto.intervalos.IntervaloInsertDTO;
import com.example.demo.dto.intervalos.IntervaloResponseDTO;
import com.example.demo.dto.tipoActividad.TipoActividadResultDTO;
import com.example.demo.entities.Entreno;
import com.example.demo.entities.Intervalo;
import com.example.demo.entities.Tipoactividad;
import com.example.demo.entities.Usuario;
import com.example.demo.entities.ZonasUsuario;
import com.example.demo.repositories.EntrenoRepository;
import com.example.demo.repositories.IntervaloRepository;
import com.example.demo.repositories.TipoActividadRepository;
import com.example.demo.repositories.UsuarioRepository;
import com.example.demo.repositories.ZonasUsuarioRepository;

@Service
public class EntrenoService {

	private final EntrenoRepository repository;

	// Una forma de inyectar dependencias
	// @Autowired
	private UsuarioRepository usuarioRepo;

	// @Autowired
	private TipoActividadRepository tipoActividadRepo;

	private IntervaloRepository intervaloRepo;
	private final ZonasUsuarioRepository zonaRepo;
	private final ZonaService zonaServ;

	// Inyeccion de dependencias por el constructor (mejor forma)
	public EntrenoService(EntrenoRepository repository, UsuarioRepository usuarioRepo,
			TipoActividadRepository tipoActividadRepo, IntervaloRepository intervaloRepo,
			ZonasUsuarioRepository zonaRepo, ZonaService zonaServ) {
		this.repository = repository;
		this.usuarioRepo = usuarioRepo;
		this.tipoActividadRepo = tipoActividadRepo;
		this.intervaloRepo = intervaloRepo;
		this.zonaRepo = zonaRepo;
		this.zonaServ = zonaServ;
	}

	@Transactional
	public Entreno crearEntrenoCompleto(EntrenoCreateDTO dto) {
		Tipoactividad ta = tipoActividadRepo.findById(dto.getTipo_actividad_id())
				.orElseThrow(() -> new RuntimeException("Tipo de actividad no encontrado"));

		Usuario us = usuarioRepo.findById(dto.getId_usuario())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		List<ZonasUsuario> zonasDelUsuario = zonaRepo.findByUsuarioIdOrderByNumeroZonaAsc(us.getId());

		Entreno en = new Entreno();
		BigDecimal distancia = BigDecimal.ZERO;
		BigDecimal desnivelTotal = BigDecimal.ZERO;
		long tiempoTotalSegundos = 0;
		Integer fcMaxima = 0;

		// primero se guardan en la entidad entreno los datos fundamentales que tendrá
		// el entreno padre
		en.setUsuario(us);
		en.setTipoactividad(ta);
		en.setFecha(dto.getFecha());
		if (dto.getDescripcion() != null) {
			en.setDescripcion(dto.getDescripcion());
		}
		en.setTitulo(dto.getTitulo());

		// importante asignar estos atributos para que no pegue un petardazo
		en.setDistancia(distancia);
		en.setDesnivel(desnivelTotal);
		en.setTiempoTotal(tiempoTotalSegundos);
		en.setFcMaxima(fcMaxima);
		en.setFcMedia(fcMaxima);

		// en caso de que no vengan intervalos, se usan los valores que se envian como
		// atributos normales
		if (dto.getIntervalos() == null || dto.getIntervalos().isEmpty()) {
			en.setDesnivel(dto.getDesnivel() != null ? dto.getDesnivel() : null);
			en.setDistancia(dto.getDistancia());
			en.setTiempoTotal((long) dto.getTiempo_total().toSecondOfDay());
			en.setFcMedia(dto.getFcMedia());
			en.setFcMaxima(dto.getFcMaxima());
			en.setZonaAlcanzada(zonaServ.determinarZona(zonasDelUsuario, dto.getFcMedia()));

			// se guarda la entidad y se sale del metodo
			return repository.save(en);
		} else {
			// primero se guarda el entreno para que tenga un id generado

			en = repository.save(en);

			// si vienen intervalos, los atributos del entreno padre definidos arriba se
			// asignaran dependiendo del resumen de los intervalos
			for (IntervaloInsertDTO i : dto.getIntervalos()) {
				Intervalo inter = new Intervalo();
				inter.setDistancia(i.getDistancia());
				inter.setDuracion((long) i.getDuracion().toSecondOfDay());

				if (i.getDesnivel() != null) {
					inter.setDesnivel(i.getDesnivel());
					desnivelTotal = desnivelTotal.add(i.getDesnivel()); // para el entreno
				}

				// suma de los atributos resumen para el entreno
				tiempoTotalSegundos += i.getDuracion().toSecondOfDay();
				distancia = distancia.add(i.getDistancia());

				Tipoactividad tipoa = new Tipoactividad();
				tipoa = tipoActividadRepo.findById(i.getTipoActividadId())
						.orElseThrow(() -> new RuntimeException("Tipo de actividad no encontrado"));
				inter.setTipoactividad(tipoa);
				inter.setEntreno(en);
				inter.setFcMedia(i.getFcMedia());
				inter.setFcMaxima(i.getFcMaxima());

				// Obtener la FC Maxima
				if (i.getFcMaxima() != null) {
					// Math.max compara el valor actual con el nuevo y se queda con el mayor
					fcMaxima = Math.max(fcMaxima, i.getFcMaxima());
				}

				inter.setZonaAlcanzada(zonaServ.determinarZona(zonasDelUsuario, inter.getFcMedia()));

				intervaloRepo.save(inter);
			}

			// asignacion de los atributos resumen del entreno en caso de que la lista de
			// intervalos no esté vacia
			en.setDesnivel(desnivelTotal);
			en.setDistancia(distancia);
			en.setTiempoTotal(tiempoTotalSegundos);
			en.setFcMedia(calcularFcMediaPonderada(dto.getIntervalos()));
			en.setFcMaxima(fcMaxima);
			
			
			en.setZonaAlcanzada(zonaServ.determinarZona(zonasDelUsuario, en.getFcMedia()));
		}

		repository.save(en);
		return en;
	}
	
	@Transactional
	public void recalcularZonasExistentes() {
	    // Obtener todos los usuarios
	    List<Usuario> usuarios = usuarioRepo.findAll();
	    
	    for (Usuario usuario : usuarios) {
	        // Obtener las zonas configuradas del usuario
	        List<ZonasUsuario> zonasDelUsuario = zonaRepo.findByUsuarioIdOrderByNumeroZonaAsc(usuario.getId());
	        
	        if (zonasDelUsuario.isEmpty()) {
	            System.out.println("Usuario " + usuario.getId() + " no tiene zonas configuradas, saltando...");
	            continue;
	        }
	        
	        // Obtener todos los entrenos del usuario
	        List<Entreno> entrenos = repository.findAllWithIntervalosByUsuario(usuario.getId());
	        
	        for (Entreno entreno : entrenos) {
	            // Si el entreno tiene intervalos
	            if (entreno.getIntervalos() != null && !entreno.getIntervalos().isEmpty()) {
	                // Recalcular zona de cada intervalo
	                for (Intervalo intervalo : entreno.getIntervalos()) {
	                    Integer zonaCalculada = zonaServ.determinarZona(zonasDelUsuario, intervalo.getFcMedia());
	                    intervalo.setZonaAlcanzada(zonaCalculada);
	                    intervaloRepo.save(intervalo);
	                }
	                
	                // Recalcular zona del entreno padre basándose en su FC media
	                Integer zonaEntreno = zonaServ.determinarZona(zonasDelUsuario, entreno.getFcMedia());
	                entreno.setZonaAlcanzada(zonaEntreno);
	            } else {
	                // Si no tiene intervalos, recalcular directamente
	                Integer zonaEntreno = zonaServ.determinarZona(zonasDelUsuario, entreno.getFcMedia());
	                entreno.setZonaAlcanzada(zonaEntreno);
	            }
	            
	            repository.save(entreno);
	        }
	        
	        System.out.println("Zonas recalculadas para usuario " + usuario.getId() + " - " + entrenos.size() + " entrenos procesados");
	    }
	    
	    System.out.println("Migración completada");
	}

	private Integer calcularFcMediaPonderada(List<IntervaloInsertDTO> intervalos) {
		double sumaFCPonderada = 0;
		long tiempoTotalConFC = 0;

		for (IntervaloInsertDTO i : intervalos) {
			// Solo sumamos si el intervalo tiene dato de FC media
			if (i.getFcMedia() != null && i.getFcMedia() > 0) {
				long duracionSegundos = i.getDuracion().toSecondOfDay();

				sumaFCPonderada += (i.getFcMedia() * duracionSegundos);
				tiempoTotalConFC += duracionSegundos;
			}
		}

		// Si ningún intervalo tenía FC, devolvemos null o 0
		if (tiempoTotalConFC == 0)
			return null;

		// Retornamos el promedio redondeado
		return (int) Math.round(sumaFCPonderada / tiempoTotalConFC);
	}

	public List<Entreno> obtenerTodosLosEntrenos() {
		return repository.findAll();
	}

	// Haciendo este metodo transaccional, spring mantiene la sesion abierta
	// mientras se accede a los intervalos
	@Transactional(readOnly = true)
	public EntrenosUsuarioDTO obtenerEntrenoPorId(int id) {
		Entreno e = repository.findById(id).orElseThrow(() -> new RuntimeException("Entreno no encontrado"));
		EntrenosUsuarioDTO eu = new EntrenosUsuarioDTO();

		List<ZonasUsuario> zonas = zonaRepo.findByUsuarioIdOrderByNumeroZonaAsc(e.getUsuario().getId());

		eu.setId(e.getId());
		eu.setTitulo(e.getTitulo());
		eu.setDescripcion(e.getDescripcion());
		eu.setDesnivel_entreno(e.getDesnivel());
		eu.setDistancia_entreno(e.getDistancia());
		eu.setFecha(e.getFecha());
		eu.setFcMaxima(e.getFcMaxima());
		eu.setFcMedia(e.getFcMedia());
		eu.setZona_alcanzada(e.getZonaAlcanzada());

		// para pasar de Long a LocalTime se usa el modulo 86400 para evitar que pegue
		// un petardazo si hay un entrenamiento superior a 24H
		// se usa 86400 porque el dia tiene 86400 segundos, si se intenta acceder al
		// segundo 90000, java lanzará una DateTimeException porque no existe la hora 25
		if (e.getTiempoTotal() != null) {
			eu.setTiempo_total_entreno(LocalTime.ofSecondOfDay(e.getTiempoTotal() % 86400));
		}

		eu.setTipo_actividad_entreno(e.getTipoactividad());

		if (esRunning(e.getTipoactividad().getNombre())) {
			eu.setRitmoMedio(redondear(ritmoMinKm(e.getTiempoTotal(), eu.getDistancia_entreno())));
		} else if (esCiclismo(e.getTipoactividad().getNombre())) {
			eu.setRitmoMedio(redondear(calcularVelocidadMedia(e.getTiempoTotal(), eu.getDistancia_entreno())));
		}

		if (e.getIntervalos() != null && !e.getIntervalos().isEmpty()) {
			List<IntervaloResponseDTO> intervalos = new ArrayList<>();

			for (Intervalo i : e.getIntervalos()) {
				IntervaloResponseDTO iDTO = new IntervaloResponseDTO();
				iDTO.setId(i.getId());
				iDTO.setDuracion(LocalTime.ofSecondOfDay(i.getDuracion() % 86400));
				iDTO.setDistancia(i.getDistancia());
				iDTO.setDesnivel(i.getDesnivel());
				iDTO.setFcMedia(i.getFcMedia());
				iDTO.setFcMaxima(i.getFcMaxima());

				TipoActividadResultDTO taDTO = new TipoActividadResultDTO();
				taDTO.setId(i.getTipoactividad().getId());
				taDTO.setNombre(i.getTipoactividad().getNombre());

				iDTO.setTipoActividadId(taDTO);
				iDTO.setZona_alcanzada(zonaServ.determinarZona(zonas, i.getFcMedia()));

				if (esRunning(i.getTipoactividad().getNombre())) {
					iDTO.setRitmoMedio(redondear(ritmoMinKm(i.getDuracion(), i.getDistancia())));
				} else if (esCiclismo(i.getTipoactividad().getNombre())) {
					iDTO.setRitmoMedio(redondear(calcularVelocidadMedia(i.getDuracion(), i.getDistancia())));
				}

				intervalos.add(iDTO);
			}

			eu.setIntervalos(intervalos);
		}

		return eu;
	}

	public void eliminarEntreno(int id) {
		if (!repository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el entreno");
		}
		repository.deleteById(id);
	}

	// el parametro de tipo EntrenoUpdateDTO se podria cambiar por el de
	// EntrenoCreateDTO, ya que tienen las mismas propiedades
	@Transactional
	public EntrenoUpdateDTO modificarEntreno(EntrenoUpdateDTO dto, int id) {
		// 1. Buscar entreno existente
		Entreno en = repository.findById(id).orElseThrow(() -> new RuntimeException("Entreno no encontrado"));
		List<ZonasUsuario> zonasDelUsuario = zonaRepo.findByUsuarioIdOrderByNumeroZonaAsc(id);

		Tipoactividad ta = tipoActividadRepo.findById(dto.getTipo_actividad_id())
				.orElseThrow(() -> new RuntimeException("Tipo de actividad no encontrado"));

		// 2. Actualizar datos básicos
		en.setTipoactividad(ta);
		en.setFecha(dto.getFecha());
		en.setTitulo(dto.getTitulo());
		en.setDescripcion(dto.getDescripcion()); // Si el DTO trae null, se pondrá null.

		// 3. GESTIÓN DE INTERVALOS (Limpieza)
		// Borramos los intervalos antiguos para evitar duplicados
		en.getIntervalos().clear();
		intervaloRepo.deleteByEntreno(en);

		if (dto.getIntervalos() == null || dto.getIntervalos().isEmpty()) {
			// Si ya no hay intervalos, usamos los datos manuales del DTO
			en.setDistancia(dto.getDistancia());
			en.setDesnivel(dto.getDesnivel());
			en.setTiempoTotal((long) dto.getTiempo_total().toSecondOfDay());
			en.setFcMedia(dto.getFcMedia());
			en.setFcMaxima(dto.getFcMaxima());
			en.setZonaAlcanzada(zonaServ.determinarZona(zonasDelUsuario, dto.getFcMedia()));
		} else {
			// Si hay intervalos, guardamos los nuevos y recalculamos los totales del padre
			// Podrías llamar aquí al método de lógica que creamos antes para evitar repetir
			// código
			resumirIntervalosEnPadre(en, dto.getIntervalos());

			// Guardar cada intervalo nuevo
			for (IntervaloInsertDTO i : dto.getIntervalos()) {
				Intervalo inter = new Intervalo();
				inter.setDistancia(i.getDistancia());
				inter.setDuracion((long) i.getDuracion().toSecondOfDay());
				inter.setDesnivel(i.getDesnivel() != null ? i.getDesnivel() : BigDecimal.ZERO);
				inter.setFcMedia(i.getFcMedia());
				inter.setFcMaxima(i.getFcMaxima());
				inter.setZonaAlcanzada(zonaServ.determinarZona(zonasDelUsuario, i.getFcMedia()));

				Tipoactividad tipoa = tipoActividadRepo.findById(i.getTipoActividadId())
						.orElseThrow(() -> new RuntimeException("Tipo de actividad no encontrado"));
				inter.setTipoactividad(tipoa);
				inter.setEntreno(en);

				intervaloRepo.save(inter);
			}
		}

		Entreno nuevoE = repository.save(en);
		EntrenoUpdateDTO response = new EntrenoUpdateDTO();
		response.setDescripcion(nuevoE.getDescripcion());
		response.setDesnivel(nuevoE.getDesnivel());
		response.setDistancia(nuevoE.getDistancia());
		response.setFcMaxima(nuevoE.getFcMaxima());
		response.setFcMedia(nuevoE.getFcMedia());
		response.setFecha(nuevoE.getFecha());

		List<IntervaloInsertDTO> listaIidto = new ArrayList<IntervaloInsertDTO>();
		for (Intervalo i : nuevoE.getIntervalos()) {
			IntervaloInsertDTO iidto = new IntervaloInsertDTO();
			iidto.setDesnivel(i.getDesnivel());
			iidto.setDistancia(i.getDistancia());
			iidto.setDuracion(LocalTime.ofSecondOfDay(i.getDuracion()));
			iidto.setFcMaxima(i.getFcMaxima());
			iidto.setFcMedia(i.getFcMedia());
			iidto.setTipoActividadId(i.getTipoactividad().getId());

			listaIidto.add(iidto);
		}

		response.setIntervalos(listaIidto);
		response.setTiempo_total(LocalTime.ofSecondOfDay(nuevoE.getTiempoTotal()));
		response.setTipo_actividad_id(nuevoE.getTipoactividad().getId());
		response.setTitulo(nuevoE.getTitulo());

		return response;
	}

	private void resumirIntervalosEnPadre(Entreno padre, List<IntervaloInsertDTO> intervalosDtos) {
		BigDecimal distanciaTotal = BigDecimal.ZERO;
		BigDecimal desnivelTotal = BigDecimal.ZERO;
		long tiempoTotalSegundos = 0;
		int fcMaximaAbsoluta = 0;

		// Variables para la media ponderada de FC
		double sumaFCPonderada = 0;
		long tiempoTotalConFC = 0;

		for (IntervaloInsertDTO iDto : intervalosDtos) {
			// 1. Suma de Distancia
			if (iDto.getDistancia() != null) {
				distanciaTotal = distanciaTotal.add(iDto.getDistancia());
			}

			// 2. Suma de Desnivel (evitando nulos)
			if (iDto.getDesnivel() != null) {
				desnivelTotal = desnivelTotal.add(iDto.getDesnivel());
			}

			// 3. Suma de Tiempo (convertido a segundos)
			long duracionI = (long) iDto.getDuracion().toSecondOfDay();
			tiempoTotalSegundos += duracionI;

			// 4. FC Máxima (buscamos el pico más alto de todos los intervalos)
			if (iDto.getFcMaxima() != null) {
				fcMaximaAbsoluta = Math.max(fcMaximaAbsoluta, iDto.getFcMaxima());
			}

			// 5. Preparación para FC Media Ponderada
			if (iDto.getFcMedia() != null && iDto.getFcMedia() > 0) {
				sumaFCPonderada += (iDto.getFcMedia() * duracionI);
				tiempoTotalConFC += duracionI;
			}
		}

		// --- ASIGNACIÓN DE TOTALES AL PADRE ---
		padre.setDistancia(distanciaTotal);
		padre.setDesnivel(desnivelTotal);
		padre.setTiempoTotal(tiempoTotalSegundos);

		// FC Máxima: si no se encontró ninguna, guardamos null
		padre.setFcMaxima(fcMaximaAbsoluta > 0 ? fcMaximaAbsoluta : null);

		// FC Media: Calculamos el promedio ponderado final
		if (tiempoTotalConFC > 0) {
			int fcMediaFinal = (int) Math.round(sumaFCPonderada / tiempoTotalConFC);
			padre.setFcMedia(fcMediaFinal);
		} else {
			padre.setFcMedia(null);
		}
	}

	public List<EntrenosUsuarioDTO> obtenerEntrenosConIntervalosPorIdUsuario(int id, Integer tipoActividadId) {
		List<Entreno> entrenos = new ArrayList<Entreno>();
		List<ZonasUsuario> zonasDelUsuario = zonaRepo.findByUsuarioIdOrderByNumeroZonaAsc(id);

		if (tipoActividadId != null) {
			// si se ha enviado como parametro el tipo de actividad, la lista de entrenos se
			// rellena con lo que devuelva este metodo del repositorio
			entrenos = repository.findAllWithIntervalosByUsuarioAndTipoActividad(id, tipoActividadId);
		} else {
			entrenos = repository.findAllWithIntervalosByUsuario(id);
		}

		List<EntrenosUsuarioDTO> result = new ArrayList<EntrenosUsuarioDTO>();

		for (Entreno e : entrenos) {
			EntrenosUsuarioDTO eu = new EntrenosUsuarioDTO();
			eu.setId(e.getId());
			eu.setTitulo(e.getTitulo());
			eu.setDescripcion(e.getDescripcion());
			eu.setDesnivel_entreno(e.getDesnivel());
			eu.setDistancia_entreno(e.getDistancia());
			eu.setFecha(e.getFecha());
			eu.setTiempo_total_entreno(LocalTime.ofSecondOfDay(e.getTiempoTotal() % 86400));
			eu.setTipo_actividad_entreno(e.getTipoactividad());
			eu.setFcMedia(e.getFcMedia());
			eu.setFcMaxima(e.getFcMaxima());

			if (esRunning(e.getTipoactividad().getNombre())) {
				eu.setRitmoMedio(redondear(ritmoMinKm(e.getTiempoTotal(), eu.getDistancia_entreno())));
			} else if (esCiclismo(e.getTipoactividad().getNombre())) {
				eu.setRitmoMedio(redondear(calcularVelocidadMedia(e.getTiempoTotal(), eu.getDistancia_entreno())));
			}

			if (e.getFcMedia() != null && e.getFcMedia() > 0) {
				eu.setZona_alcanzada(zonaServ.determinarZona(zonasDelUsuario, e.getFcMedia()));
			} else {
				eu.setZona_alcanzada(null);
			}

			List<IntervaloResponseDTO> intervalosDTO = new ArrayList<>();
			for (Intervalo i : e.getIntervalos()) {
				IntervaloResponseDTO iDTO = new IntervaloResponseDTO();
				Tipoactividad ta = tipoActividadRepo.findById(i.getTipoactividad().getId())
						.orElseThrow(() -> new RuntimeException("Tipo de actividad no encontrado"));
				TipoActividadResultDTO dtoTa = new TipoActividadResultDTO();
				dtoTa.setId(ta.getId());
				dtoTa.setNombre(ta.getNombre());

				iDTO.setId(i.getId());
				iDTO.setTipoActividadId(dtoTa);
				iDTO.setDuracion(LocalTime.ofSecondOfDay(i.getDuracion() % 86400));
				iDTO.setDistancia(i.getDistancia());
				iDTO.setDesnivel(i.getDesnivel());

				if (esRunning(i.getTipoactividad().getNombre())) {
					iDTO.setRitmoMedio(redondear(ritmoMinKm(i.getDuracion(), i.getDistancia())));
				} else if (esCiclismo(i.getTipoactividad().getNombre())) {
					iDTO.setRitmoMedio(redondear(calcularVelocidadMedia(i.getDuracion(), i.getDistancia())));
				}

				if (i.getFcMedia() != null && i.getFcMedia() > 0) {
					iDTO.setZona_alcanzada(zonaServ.determinarZona(zonasDelUsuario, i.getFcMedia()));
				} else {
					iDTO.setZona_alcanzada(null);
				}

				intervalosDTO.add(iDTO);
			}
			eu.setIntervalos(intervalosDTO);

			result.add(eu);
		}

		return result;
	}

	private double ritmoMinKm(Long segundos, BigDecimal distanciaKm) {
		if (segundos == null || segundos <= 0 || distanciaKm == null || distanciaKm.doubleValue() <= 0) {
			return 0;
		}
		// Convertimos segundos a minutos decimales y dividimos por distancia
		return (segundos / 60.0) / distanciaKm.doubleValue();
	}

	// 2. Velocidad: Km/h
	private double calcularVelocidadMedia(Long segundos, BigDecimal distanciaKm) {
		if (segundos == null || segundos <= 0 || distanciaKm == null)
			return 0;
		double horas = segundos / 3600.0;
		return distanciaKm.doubleValue() / horas;
	}

	private boolean esRunning(String tipo) {
		return tipo.toLowerCase().equals("correr") || tipo.toLowerCase().equals("caminar")
				|| tipo.toLowerCase().equals("trail");
	}

	private boolean esCiclismo(String tipo) {
		return tipo.toLowerCase().contains("bici") || tipo.toLowerCase().contains("bike")
				|| tipo.toLowerCase().contains("Bici");
	}

	private double redondear(double valor) {
		// al multiplicar por 100.0, redondear y luego divitir por 100.0, lo que se hace
		// es cortar el numero para quedarse solo con 2 decimales
		// en caso de que quisieramos solamente 1 decimal, multiplicariamos y
		// dividiriamos por 10.0
		// y si no queremos decimales, directamente no se multiplica ni divide el valor,
		// solo se le hace el Math.round y ya
		return Math.round(valor * 100.0) / 100.0; // para obtener 2 decimales
	}

}
