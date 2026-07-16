package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.ProdottoReq;
import com.betacom.jpa.dto.output.ProdottoDTO;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
public interface IProdottoServices {
	void create(ProdottoReq req) throws Exception;

	void update(ProdottoReq req) throws Exception;

	void delete(Integer id) throws Exception;

	// I tre parametri sono i filtri opzionali della ricerca: categoria, marca, nome (ricerca testuale)
	List<ProdottoDTO> list(Integer idCategoria, String marca, String nome) throws Exception;

	ProdottoDTO getById(Integer id) throws Exception;
}
