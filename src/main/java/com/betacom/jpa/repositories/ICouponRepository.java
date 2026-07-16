package com.betacom.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Coupon;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
@Repository
public interface ICouponRepository extends JpaRepository<Coupon, Integer> {
	// Punto d'ingresso principale per la validazione: il cliente applica un coupon per codice, non per id
	Optional<Coupon> findByCodice(String codice);

	// Controllo duplicati in creazione: niente due coupon con lo stesso codice
	boolean existsByCodice(String codice);
}
