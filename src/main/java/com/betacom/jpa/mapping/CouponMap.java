package com.betacom.jpa.mapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.betacom.jpa.dto.output.CouponDTO;
import com.betacom.jpa.models.Coupon;

// Proprietario: Pier
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
				.tipologia(c.getTipologia().toString())
				.valore(c.getValore())
				.dataInizio(c.getDataInizio())
				.dataFine(c.getDataFine())
				.isAttivo(c.getIsAttivo())
				.build();
	}

	public static BigDecimal calcolaSconto(BigDecimal totale, Coupon coupon) {
		if (coupon == null || totale == null)
			return BigDecimal.ZERO;

		BigDecimal sconto = switch (coupon.getTipologia()) {
			case PERCENTUALE -> totale.multiply(coupon.getValore())
					.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
			case FISSO -> coupon.getValore();
		};

		return sconto.min(totale);
	}
}
