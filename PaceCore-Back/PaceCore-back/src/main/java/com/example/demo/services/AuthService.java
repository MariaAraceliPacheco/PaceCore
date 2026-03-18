package com.example.demo.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.auth.LoginDTO;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.dto.usuarios.UsuarioInsertDTO;
import com.example.demo.dto.usuarios.UsuarioResponseDTO;
import com.example.demo.entities.Usuario;
import com.example.demo.mappers.usuario.UsuarioMapper;
import com.example.demo.repositories.UsuarioRepository;
import com.example.demo.security.AutenticadorJWT;

@Service
public class AuthService {

	private final UsuarioRepository repo;
	private final BCryptPasswordEncoder encoder;
	private final ZonaService zonaService;
	private UsuarioMapper usuarioMapper;

	public AuthService(UsuarioRepository repo, UsuarioMapper usuarioMapper, BCryptPasswordEncoder encoder,
			ZonaService zonaService) {
		super();
		this.repo = repo;
		this.encoder = encoder;
		this.zonaService = zonaService;
		this.usuarioMapper = usuarioMapper;
	}

	public LoginResponse login(LoginDTO dto) {
		Usuario u = repo.findByEmail(dto.getEmail())
				.orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

		if (!encoder.matches(dto.getPassword(), u.getPassword())) {
			throw new RuntimeException("Credenciales incorrectas");
		}

		String token = AutenticadorJWT.codificaJWT(u);
		UsuarioResponseDTO e = usuarioMapper.toUsuarioResponseDTO(u);

		LoginResponse resp = new LoginResponse();
		resp.setToken(token);
		resp.setUsuario(e);

		return resp;
	}

	@Transactional
	public Usuario register(UsuarioInsertDTO dto) {
		if (repo.existsByEmail(dto.getEmail())) {
			throw new RuntimeException("Email ya registrado");
		}

		Usuario us = usuarioMapper.toEntityFromUsuarioInsertDTO(dto);
		String password = encoder.encode(dto.getPassword());
		us.setPassword(password);

		// con save and flush se obliga a Spring Data JPA a hacer el insert y el commit
		// en la bd, para que asi podamos tener acceso al id del usuario que se acaba de
		// crear
		Usuario response = repo.saveAndFlush(us);
		zonaService.crearZonaUsuario(response.getId());

		return response;
	}

	public UsuarioResponseDTO obtenerUsuarioPorJwt(String token) {

		int id = AutenticadorJWT.getIdUsuarioDesdeJWT(token);

		if (id == -1) {
			return null;
		}
		Usuario u = repo.findById(id).orElseThrow(() -> new RuntimeException("No se ha encontrado el usuario"));
		UsuarioResponseDTO e = usuarioMapper.toUsuarioResponseDTO(u);

		return e;
	}

}
