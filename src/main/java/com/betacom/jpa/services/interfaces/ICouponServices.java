package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.CouponReq;
import com.betacom.jpa.dto.output.CouponDTO;
import com.betacom.jpa.models.Coupon;

// Proprietario: Pier
public interface ICouponServices {
	void create(CouponReq req) throws Exception;

	void update(CouponReq req) throws Exception;

	void delete(Integer id) throws Exception;

	List<CouponDTO> list() throws Exception;

	CouponDTO getById(Integer id) throws Exception;

	// idUtente serve per controllare che l'utente non abbia gia' usato questo stesso coupon
	// in un ordine precedente (non annullato): un coupon come GERARD10 va usato una volta sola
	Coupon validateAndGet(String codice, Integer idUtente) throws Exception;
}
