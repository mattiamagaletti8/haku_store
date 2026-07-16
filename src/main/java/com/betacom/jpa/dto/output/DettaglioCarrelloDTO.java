package com.betacom.jpa.dto.output;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DettaglioCarrelloDTO {
	private Integer id;
	// Variante gia' convertita in DTO: il frontend riceve subito nome prodotto, prezzo e stock
	private VarianteProdottoDTO variante;
	private Integer quantita;
	// Anche questo non e' una colonna del DB: prezzo * quantita, calcolato in DettaglioCarrelloMap
	private BigDecimal subtotale;
}
