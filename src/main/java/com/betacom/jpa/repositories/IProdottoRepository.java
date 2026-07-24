package com.betacom.jpa.repositories;

import java.util.List;

// import di Spring Data JPA (repository generico, query custom) e dell'entity da gestire
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Prodotto;

// Proprietario: Mattia
@Repository    // dice a Spring che questa è una repository, gestita in automatico
public interface IProdottoRepository extends JpaRepository<Prodotto, Integer> {
	// cerca i prodotti filtrando per categoria/marca/nome (i parametri possono essere null); la query è definita altrove col nome "prodotto.selectByFilter"
	@Query(name = "prodotto.selectByFilter")
	List<Prodotto> searchByFilter(
			@Param("idCategoria") Integer idCategoria,
			@Param("marca") String marca,
			@Param("nome") String nome
			);

	// true se esiste già un prodotto con stesso nome e marca, ignorando maiuscole/minuscole (evita duplicati)
	boolean existsByNomeIgnoreCaseAndMarcaIgnoreCase(String nome, String marca);

	// tutti i prodotti ordinati dal più recente al più vecchio, per la sezione "Novità" della home
	@Query(name = "prodotto.selectNovita")
	List<Prodotto> selectNovita();
}
