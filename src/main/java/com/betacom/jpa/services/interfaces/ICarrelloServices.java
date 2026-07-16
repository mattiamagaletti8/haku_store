package com.betacom.jpa.services.interfaces;

import com.betacom.jpa.dto.input.DettaglioCarrelloReq;
import com.betacom.jpa.dto.output.CarrelloDTO;
import com.betacom.jpa.models.Carrello;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
public interface ICarrelloServices {
	/**
	 * Restituisce il carrello ATTIVO dell'utente, creandolo se non esiste ancora (nessuna
	 * azione esplicita "crea carrello" e' prevista dallo schema).
	 */
	// Restituisce l'entity grezza (non il DTO): usata internamente anche da OrdineImpl durante il checkout,
	// che ha bisogno dell'oggetto Carrello vero per leggerne le righe e poi svuotarlo
	Carrello getOrCreateForUtente(Integer idUtente) throws Exception;

	// Versione "pubblica" per il frontend: stessa logica di getOrCreateForUtente ma gia' convertita in DTO coi totali
	CarrelloDTO getCarrello(Integer idUtente) throws Exception;

	// Aggiunge una riga al carrello, o incrementa la quantita' se la variante e' gia' presente
	void addItem(Integer idUtente, DettaglioCarrelloReq req) throws Exception;

	// Imposta la quantita' di una riga esistente a un valore assoluto (non la incrementa)
	void updateItemQuantity(Integer idUtente, Integer idVariante, Integer quantita) throws Exception;

	void removeItem(Integer idUtente, Integer idVariante) throws Exception;

	void applyCoupon(Integer idUtente, String codice) throws Exception;

	void removeCoupon(Integer idUtente) throws Exception;

	// Svuota completamente il carrello (righe + coupon), lasciandolo pronto per una nuova sessione di shopping
	void clear(Integer idUtente) throws Exception;
}
