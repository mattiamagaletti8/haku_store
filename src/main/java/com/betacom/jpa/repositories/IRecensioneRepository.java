package com.betacom.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.Recensione;

// Proprietario: Sarah
@Repository
public interface IRecensioneRepository extends JpaRepository<Recensione, Integer> {
	List<Recensione> findByProdottoIdProdotto(Integer idProdotto);
}
