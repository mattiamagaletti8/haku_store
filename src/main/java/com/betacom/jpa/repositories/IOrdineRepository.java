package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.enums.StatoOrdine;
import com.betacom.jpa.enums.StatoPagamento;
import com.betacom.jpa.models.Ordine;

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
@Repository
public interface IOrdineRepository extends JpaRepository<Ordine, Integer> {
	// Query nominata (jpa-named-queries.properties), stesso idioma di prodotto.selectByFilter:
	// tutti e 3 i parametri sono opzionali, cosi' un ADMIN puo' filtrare per utente/stato/pagamento
	// mentre un CLIENTE (che passa sempre il proprio idUtente, mai null) vede solo i propri ordini
	@Query(name = "ordine.selectByFilter")
	List<Ordine> searchByFilter(
			@Param("idUtente") Integer idUtente,
			@Param("stato") StatoOrdine stato,
			@Param("statoPagamento") StatoPagamento statoPagamento
			);
}
