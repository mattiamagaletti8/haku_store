package com.betacom.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Carrello;

// Proprietario: Pier
@Repository
public interface ICarrelloRepository extends JpaRepository<Carrello, Integer> {
	Optional<Carrello> findByUtenteIdUtente(Integer idUtente);

	List<Carrello> findByCouponIdCoupon(Integer idCoupon);
}
