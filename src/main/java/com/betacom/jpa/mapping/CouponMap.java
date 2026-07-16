package com.betacom.jpa.mapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.betacom.jpa.dto.output.CouponDTO;
import com.betacom.jpa.models.Coupon;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
public class CouponMap {

	public static List<CouponDTO> buildCouponDTOList(List<Coupon> lC) {
		return lC.stream()
				.map(CouponMap::buildCouponDTO)
				.toList();
	}

	public static CouponDTO buildCouponDTO(Coupon c) {
		return CouponDTO.builder()
				.id(c.getIdCoupon())
				.codice(c.getCodice())
				// L'enum viene trasformato in stringa qui, non prima: il DTO non conosce il tipo TipologiaCoupon
				.tipologia(c.getTipologia().toString())
				.valore(c.getValore())
				.dataInizio(c.getDataInizio())
				.dataFine(c.getDataFine())
				.isAttivo(c.getIsAttivo())
				.build();
	}

	/**
	 * Calcola lo sconto da applicare a un totale dato un coupon (PERCENTUALE o FISSO),
	 * mai superiore al totale stesso. Unica formula riusata da carrello e checkout.
	 */
	public static BigDecimal calcolaSconto(BigDecimal totale, Coupon coupon) {
		if (coupon == null || totale == null)
			return BigDecimal.ZERO;

		// switch su enum: PERCENTUALE calcola una quota del totale, FISSO usa il valore cosi' com'e'
		BigDecimal sconto = switch (coupon.getTipologia()) {
			case PERCENTUALE -> totale.multiply(coupon.getValore())
					.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
			case FISSO -> coupon.getValore();
		};

		// Non lasciare mai che lo sconto superi il totale (altrimenti il totale pagato diventerebbe negativo)
		return sconto.min(totale);
	}
}
