package com.betacom.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Coupon;

// Proprietario: Pier
@Repository
public interface ICouponRepository extends JpaRepository<Coupon, Integer> {
	Optional<Coupon> findByCodice(String codice);

	boolean existsByCodice(String codice);
}
