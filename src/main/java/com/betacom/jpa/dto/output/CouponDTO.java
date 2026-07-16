package com.betacom.jpa.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class CouponDTO {
	private Integer id;
	private String codice;
	// Qui la tipologia e' una String (non l'enum): CouponMap la converte con .toString(),
	// cosi' il DTO non dipende dal tipo enum interno e resta un semplice contenitore di dati
	private String tipologia;
	private BigDecimal valore;
	private LocalDateTime dataInizio;
	private LocalDateTime dataFine;
	private Boolean isAttivo;
}
