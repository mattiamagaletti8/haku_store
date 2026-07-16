package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Indirizzo;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
@Repository
public interface IIndirizzoRepository extends JpaRepository<Indirizzo, Integer> {
	// Query derivata: tutti gli indirizzi di un dato utente — usata sia dalla lista "i miei indirizzi"
	// sia (indirettamente) dal checkout in OrdineImpl per verificare la proprieta' dell'indirizzo scelto
	List<Indirizzo> findByUtenteIdUtente(Integer idUtente);
}
