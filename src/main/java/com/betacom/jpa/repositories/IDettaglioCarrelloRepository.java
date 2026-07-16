package com.betacom.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.DettaglioCarrello;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
@Repository
public interface IDettaglioCarrelloRepository extends JpaRepository<DettaglioCarrello, Integer> {
	// Cerca la riga esistente per una data coppia carrello+variante — e' cosi' che CarrelloImpl.addItem
	// decide se incrementare una riga gia' presente o crearne una nuova (rispecchia il vincolo UNIQUE della tabella)
	Optional<DettaglioCarrello> findByCarrelloIdCarrelloAndVarianteIdVariante(Integer idCarrello, Integer idVariante);
}
