package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.OrdineReq;
import com.betacom.jpa.dto.output.OrdineDTO;

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
public interface IOrdineServices {
	/**
	 * Trasforma il carrello attivo dell'utente in un ordine. Vedi il piano per il dettaglio
	 * dei passi (validazione stock, ri-validazione coupon, congelamento prezzi, decremento stock
	 * con retry ottimistico, transizione carrello a CONVERTITO).
	 */
	// idUtente arriva sempre dal principal autenticato nel controller, mai dal body della richiesta
	OrdineDTO checkout(Integer idUtente, OrdineReq req) throws Exception;

	/**
	 * Se isAdmin=false, callerId forza il filtro sui soli ordini propri indipendentemente
	 * da idUtenteFiltro. Se isAdmin=true, idUtenteFiltro (se presente) filtra per un utente specifico.
	 */
	// Questo doppio parametro (callerId + idUtenteFiltro) e' il modo con cui lo stesso endpoint
	// serve sia "il cliente vede i propri ordini" sia "l'admin vede/filtra tutti gli ordini"
	List<OrdineDTO> list(Integer callerId, boolean isAdmin, Integer idUtenteFiltro, String stato, String statoPagamento) throws Exception;

	// Stesso pattern di ownership: solo l'autore dell'ordine o un ADMIN possono vederne il dettaglio
	OrdineDTO getById(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	// Solo ADMIN (controllato nel controller): cambia lo stato logistico (spedizione)
	void updateStato(OrdineReq req) throws Exception;

	// Solo ADMIN: cambia lo stato del pagamento, indipendente dallo stato logistico
	void updateStatoPagamento(OrdineReq req) throws Exception;
}
