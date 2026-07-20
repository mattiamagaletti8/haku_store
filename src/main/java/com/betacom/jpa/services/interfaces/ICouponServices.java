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

	Coupon validateAndGet(String codice) throws Exception;
}
