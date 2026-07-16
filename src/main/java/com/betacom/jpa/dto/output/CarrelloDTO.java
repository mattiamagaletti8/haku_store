package com.betacom.jpa.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
public class CarrelloDTO {
	private Integer id;
	private LocalDateTime dataCreazione;
	private List<DettaglioCarrelloDTO> righe;
	private CouponDTO coupon;
	// Questi 3 totali NON sono colonne della tabella carrello: non esistono a livello di database,
	// sono calcolati al volo in CarrelloMap ogni volta che il carrello viene letto
	private BigDecimal totaleProdotti;
	private BigDecimal valoreSconto;
	private BigDecimal totalePagato;
}
