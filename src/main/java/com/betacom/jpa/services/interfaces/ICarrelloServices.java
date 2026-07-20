package com.betacom.jpa.services.interfaces;

import com.betacom.jpa.dto.input.DettaglioCarrelloReq;
import com.betacom.jpa.dto.output.CarrelloDTO;
import com.betacom.jpa.models.Carrello;

// Proprietario: Pier
public interface ICarrelloServices {
	Carrello getOrCreateForUtente(Integer idUtente) throws Exception;

	CarrelloDTO getCarrello(Integer idUtente) throws Exception;

	void addItem(Integer idUtente, DettaglioCarrelloReq req) throws Exception;

	void updateItemQuantity(Integer idUtente, Integer idVariante, Integer quantita) throws Exception;

	void removeItem(Integer idUtente, Integer idVariante) throws Exception;

	void applyCoupon(Integer idUtente, String codice) throws Exception;

	void removeCoupon(Integer idUtente) throws Exception;

	void clear(Integer idUtente) throws Exception;
}
