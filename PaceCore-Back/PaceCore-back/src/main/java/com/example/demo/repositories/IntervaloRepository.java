package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Entreno;
import com.example.demo.entities.Intervalo;

@Repository
public interface IntervaloRepository extends JpaRepository<Intervalo, Integer>{

	public void deleteByEntreno(Entreno entreno);
	
}
