package com.betacom.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.jpa.models.DettaglioOrdine;

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
// Nessun metodo custom: le righe dell'ordine vengono create solo dentro OrdineImpl.checkout,
// bastano i metodi ereditati da JpaRepository (save, findAll...), non serve altro
@Repository
public interface IDettaglioOrdineRepository extends JpaRepository<DettaglioOrdine, Integer> {
}
