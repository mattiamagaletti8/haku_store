package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.VarianteProdotto;

@Repository
public interface IVarianteProdottoRepository extends JpaRepository<VarianteProdotto, Integer> {
	List<VarianteProdotto> findByProdottoIdProdotto(Integer idProdotto);

	boolean existsByProdottoIdProdottoAndGustoAndFormatoAndColore(Integer idProdotto, String gusto, String formato, String colore);
}
