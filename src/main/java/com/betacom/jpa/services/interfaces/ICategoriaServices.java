package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.CategoriaReq;
import com.betacom.jpa.dto.output.CategoriaDTO;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
// Contratto del servizio Categoria: il controller dipende da questa interfaccia, non dall'implementazione
// concreta (CategoriaImpl) — permette di sostituire l'implementazione senza toccare il controller.
public interface ICategoriaServices {
	// Crea una nuova categoria a partire dai dati validati nel CategoriaReq
	void create(CategoriaReq req) throws Exception;

	// Aggiorna una categoria esistente (identificata da req.getId())
	void update(CategoriaReq req) throws Exception;

	// Elimina la categoria con questo id (fallisce se ha ancora prodotti collegati)
	void delete(Integer id) throws Exception;

	// Restituisce tutte le categorie, gia' convertite in DTO
	List<CategoriaDTO> list() throws Exception;

	// Restituisce una singola categoria per id, gia' convertita in DTO
	CategoriaDTO getById(Integer id) throws Exception;
}
