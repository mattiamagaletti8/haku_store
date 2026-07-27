package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.enums.StatoOrdine;
import com.betacom.jpa.enums.StatoPagamento;
import com.betacom.jpa.models.Ordine;

// Proprietario: Valerio
@Repository
public interface IOrdineRepository extends JpaRepository<Ordine, Integer> {
	@Query(name = "ordine.selectByFilter")
	List<Ordine> searchByFilter(
			@Param("idUtente") Integer idUtente,
			@Param("stato") StatoOrdine stato,
			@Param("statoPagamento") StatoPagamento statoPagamento
			);

	// true se l'utente ha gia' un ordine (non annullato) con questo stesso coupon: serve per
	// impedire di riusare piu' volte un coupon "una tantum" come GERARD10
	boolean existsByUtenteIdUtenteAndCodiceCouponUsatoIgnoreCaseAndStatoNot(
			Integer idUtente, String codiceCouponUsato, StatoOrdine stato);
}
