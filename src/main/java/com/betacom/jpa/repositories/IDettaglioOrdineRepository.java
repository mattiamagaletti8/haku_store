package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.DettaglioOrdine;

// Proprietario: Valerio
@Repository
public interface IDettaglioOrdineRepository extends JpaRepository<DettaglioOrdine, Integer> {
	// id prodotto + quantita' totale venduta (ordini non annullati), dal piu' venduto, per la sezione "In evidenza" della home
	@Query(name = "dettaglioOrdine.selectProdottiPiuVenduti")
	List<Object[]> selectProdottiPiuVenduti();
}
