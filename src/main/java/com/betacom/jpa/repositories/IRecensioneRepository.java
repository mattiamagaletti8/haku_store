package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Recensione;

// Proprietario: Sarah
@Repository   // Spring Data JPA genera l'implementazione da solo, a partire dai nomi dei metodi
public interface IRecensioneRepository extends JpaRepository<Recensione, Integer> {
	// tutte le recensioni di un certo prodotto (usato per mostrarle nella scheda prodotto)
	List<Recensione> findByProdottoIdProdotto(Integer idProdotto);
}
