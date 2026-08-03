package com.betacom.jpa.mapping;

import java.math.BigDecimal;
import java.util.List;

import com.betacom.jpa.dto.output.CarrelloDTO;
import com.betacom.jpa.dto.output.DettaglioCarrelloDTO;
import com.betacom.jpa.models.Carrello;

// Proprietario: Pier
public class CarrelloMap {

	public static CarrelloDTO buildCarrelloDTO(Carrello c) {
		List<DettaglioCarrelloDTO> righe = DettaglioCarrelloMap.buildDettaglioCarrelloDTOList(c.getRighe());

		BigDecimal totaleProdotti = righe.stream()
				.map(DettaglioCarrelloDTO::getSubtotale)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal valoreSconto = c.getCoupon() == null
				? BigDecimal.ZERO
				: CouponMap.calcolaSconto(totaleProdotti, c.getCoupon());

		BigDecimal totalePagato = totaleProdotti.subtract(valoreSconto);

		return CarrelloDTO.builder()
				.id(c.getIdCarrello())
				.dataCreazione(c.getDataCreazione())
				.righe(righe)
				.coupon(c.getCoupon() == null ? null : CouponMap.buildCouponDTO(c.getCoupon()))
				.totaleProdotti(totaleProdotti)
				.valoreSconto(valoreSconto)
				.totalePagato(totalePagato)
				.build();
	}
}
