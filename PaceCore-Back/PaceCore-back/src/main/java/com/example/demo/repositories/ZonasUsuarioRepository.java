package com.example.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.ZonasUsuario;
import java.util.List;


@Repository
public interface ZonasUsuarioRepository extends JpaRepository<ZonasUsuario, Integer>{

	public List<ZonasUsuario> findByUsuarioIdOrderByNumeroZonaAsc(int idUsuario);
	
}
