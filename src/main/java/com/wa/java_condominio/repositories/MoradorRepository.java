package com.wa.java_condominio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wa.java_condominio.model.Morador;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Long> {

}
