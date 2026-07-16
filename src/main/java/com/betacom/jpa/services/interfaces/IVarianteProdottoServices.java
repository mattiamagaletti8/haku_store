package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.VarianteProdottoReq;
import com.betacom.jpa.dto.output.VarianteProdottoDTO;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
public interface IVarianteProdottoServices {
	void create(VarianteProdottoReq req) throws Exception;

	void update(VarianteProdottoReq req) throws Exception;

	void delete(Integer id) throws Exception;

	// Lista filtrata per un singolo prodotto (usata nella pagina di dettaglio prodotto)
	List<VarianteProdottoDTO> listByProdotto(Integer idProdotto) throws Exception;

	VarianteProdottoDTO getById(Integer id) throws Exception;
}
