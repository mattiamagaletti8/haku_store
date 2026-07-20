package com.betacom.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.DettaglioOrdine;

// Proprietario: Valerio
@Repository
public interface IDettaglioOrdineRepository extends JpaRepository<DettaglioOrdine, Integer> {
}
