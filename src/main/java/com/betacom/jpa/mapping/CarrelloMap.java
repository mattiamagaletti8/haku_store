package com.betacom.jpa.mapping;

import java.math.BigDecimal;
import java.util.List;

import com.betacom.jpa.dto.output.CarrelloDTO;
import com.betacom.jpa.dto.output.DettaglioCarrelloDTO;
import com.betacom.jpa.models.Carrello;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
// Qui vive il calcolo dei totali del carrello: nessuna di queste cifre e' salvata sul database,
// vengono ricalcolate ogni volta a partire dai prezzi CORRENTI delle varianti (non congelati).
public class CarrelloMap {

	public static CarrelloDTO buildCarrelloDTO(Carrello c) {
		// Prima converte tutte le righe (ognuna col proprio subtotale gia' calcolato)
		List<DettaglioCarrelloDTO> righe = DettaglioCarrelloMap.buildDettaglioCarrelloDTOList(c.getRighe());

		// Totale prodotti = somma di tutti i subtotali di riga
		BigDecimal totaleProdotti = righe.stream()
				.map(DettaglioCarrelloDTO::getSubtotale)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// Se c'e' un coupon applicato, delega il calcolo dello sconto a CouponMap (stessa formula
		// usata anche durante il checkout, per coerenza)
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
