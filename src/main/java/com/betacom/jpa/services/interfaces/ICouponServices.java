package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.CouponReq;
import com.betacom.jpa.dto.output.CouponDTO;
import com.betacom.jpa.models.Coupon;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
public interface ICouponServices {
	void create(CouponReq req) throws Exception;

	void update(CouponReq req) throws Exception;

	void delete(Integer id) throws Exception;

	List<CouponDTO> list() throws Exception;

	CouponDTO getById(Integer id) throws Exception;

	/**
	 * Unica fonte di verita' per la validita' di un coupon: esistenza, attivo, finestra data.
	 * Usato sia da CarrelloImpl.applyCoupon che da OrdineImpl.checkout — cosi' un coupon che scade
	 * mentre il carrello resta aperto viene rifiutato anche al momento del checkout, non solo quando si applica.
	 */
	Coupon validateAndGet(String codice) throws Exception;
}
