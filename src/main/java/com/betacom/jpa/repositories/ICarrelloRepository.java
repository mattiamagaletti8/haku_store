package com.betacom.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Carrello;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
@Repository
public interface ICarrelloRepository extends JpaRepository<Carrello, Integer> {
	// Punto d'ingresso del rapporto 1:1 con Utente: dato l'id dell'utente, trova il SUO carrello
	Optional<Carrello> findByUtenteIdUtente(Integer idUtente);

	// Trova tutti i carrelli che hanno applicato un determinato coupon — usata in CouponImpl.delete
	// per scollegarli prima di cancellare il coupon (altrimenti la FK impedirebbe la delete)
	List<Carrello> findByCouponIdCoupon(Integer idCoupon);
}
