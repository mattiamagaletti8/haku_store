package com.betacom.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Utente;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
@Repository
public interface IUtenteRepository extends JpaRepository<Utente, Integer> {
	// Punto d'ingresso usato sia dal login (AuthImpl) sia da UtenteDetailsService.loadUserByUsername:
	// l'email E' il modo in cui il sistema identifica un utente per autenticarlo
	Optional<Utente> findByEmail(String email);

	// Controllo duplicati in registrazione: niente due account con la stessa email
	boolean existsByEmail(String email);
}
