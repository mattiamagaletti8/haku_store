package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.OrdineReq;
import com.betacom.jpa.dto.output.OrdineDTO;

public interface IOrdineServices {
	/**
	 * Trasforma il carrello attivo dell'utente in un ordine. Vedi il piano per il dettaglio
	 * dei passi (validazione stock, ri-validazione coupon, congelamento prezzi, decremento stock
	 * con retry ottimistico, transizione carrello a CONVERTITO).
	 */
	OrdineDTO checkout(Integer idUtente, OrdineReq req) throws Exception;

	/**
	 * Se isAdmin=false, callerId forza il filtro sui soli ordini propri indipendentemente
	 * da idUtenteFiltro. Se isAdmin=true, idUtenteFiltro (se presente) filtra per un utente specifico.
	 */
	List<OrdineDTO> list(Integer callerId, boolean isAdmin, Integer idUtenteFiltro, String stato, String statoPagamento) throws Exception;

	OrdineDTO getById(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	void updateStato(OrdineReq req) throws Exception;

	void updateStatoPagamento(OrdineReq req) throws Exception;
}
