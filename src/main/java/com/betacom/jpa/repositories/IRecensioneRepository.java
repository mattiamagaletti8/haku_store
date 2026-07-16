package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Recensione;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
@Repository
public interface IRecensioneRepository extends JpaRepository<Recensione, Integer> {
	// Tutte le recensioni di un prodotto — usata dalla lista pubblica sulla pagina di dettaglio prodotto
	List<Recensione> findByProdottoIdProdotto(Integer idProdotto);
}
