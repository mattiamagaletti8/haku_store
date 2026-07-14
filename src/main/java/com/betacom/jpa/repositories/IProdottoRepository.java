package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Prodotto;

@Repository
public interface IProdottoRepository extends JpaRepository<Prodotto, Integer> {
	@Query(name = "prodotto.selectByFilter")
	List<Prodotto> searchByFilter(
			@Param("idCategoria") Integer idCategoria,
			@Param("marca") String marca,
			@Param("nome") String nome
			);

	boolean existsByNomeIgnoreCaseAndMarcaIgnoreCase(String nome, String marca);
}
