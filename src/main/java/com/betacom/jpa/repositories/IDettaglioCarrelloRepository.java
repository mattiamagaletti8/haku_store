package com.betacom.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.DettaglioCarrello;

// Proprietario: Pier
@Repository
public interface IDettaglioCarrelloRepository extends JpaRepository<DettaglioCarrello, Integer> {
	Optional<DettaglioCarrello> findByCarrelloIdCarrelloAndVarianteIdVariante(Integer idCarrello, Integer idVariante);
}
