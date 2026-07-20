package com.betacom.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Utente;

// Proprietario: Sarah
@Repository
public interface IUtenteRepository extends JpaRepository<Utente, Integer> {
	Optional<Utente> findByEmail(String email);

	boolean existsByEmail(String email);
}
